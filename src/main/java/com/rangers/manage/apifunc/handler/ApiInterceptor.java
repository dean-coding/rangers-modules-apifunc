package com.rangers.manage.apifunc.handler;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.http.MediaType;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.rangers.manage.apifunc.db.ApiAccessLogDbAccess;
import com.rangers.manage.apifunc.db.ApiInfoDbAccess;
import com.rangers.manage.apifunc.db.AppKeyDbAccess;
import com.rangers.manage.apifunc.domain.ApiAccessLog;
import com.rangers.manage.apifunc.domain.ApiInfo;
import com.rangers.manage.apifunc.domain.AppKey;


/**
 * 拦截器处理，API接口校验&接口访问日志
 * @author vencano
 *
 */
public class ApiInterceptor implements HandlerInterceptor {

	private static ImmutableMap<String, String> errorCodeMap;

	static {
		try {
			errorCodeMap = Maps.fromProperties(PropertiesLoaderUtils.loadAllProperties("error_code.properties"));
		} catch (IOException e) {
			throw new RuntimeException("数据接口响应码配置加载失败");
		}
	}
	@Autowired
	private ApiInfoDbAccess apiInfoDbAccess;
	@Autowired
	private ApiAccessLogDbAccess apiAccessLogDbAccess;
	@Autowired
	private AppKeyDbAccess appKeyDbAccess;

	/**
	 * ?funcName=***&appId=****&accessToken=***
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object arg2) throws Exception {

		String apiName = request.getParameter("funcName");// 获取API名称
		if (!StringUtils.hasText(apiName)) {// 请求地址错误
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return false;
		}
		response.setCharacterEncoding("UTF-8");
		response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
		PrintWriter out = null;
		ApiInfo apiInfo = apiInfoDbAccess.findOne(apiName);
		if (null == apiInfo) { // 接口不存在
			out = response.getWriter();
			out.append(joinResStr("40004"));
			out.flush();
			out.close();
			return false;
		}

		String requestURI = request.getRequestURI();
		if(!apiInfo.getUri().equals(requestURI)) {
			out = response.getWriter();
			out.append(joinResStr("40009"));
			out.flush();
			out.close();
			return false;
		}
		
		if (apiInfo.isDisabled()) { // 接口已禁用
			out = response.getWriter();
			out.append(joinResStr("40003"));
			out.flush();
			out.close();
			return false;
		}

		String method = request.getMethod();
		if (!method.equalsIgnoreCase(apiInfo.getReqMethod())) { // http
			out = response.getWriter();
			out.append(joinResStr("40005"));
			out.flush();
			out.close();
			return false;
		}

		/** tip:针对AppKey认证的业务处理,校验appId,加密校验secretKey等等 */
		String appId = request.getParameter("appId");
		if (null == appId || "".equals(appId)) {
			out = response.getWriter();
			out.append(joinResStr("40001"));
			out.flush();
			out.close();
			return false;
		}
		// 获取appid,请求是否合法
		AppKey appKey = appKeyDbAccess.findOne(appId);
		if (null == appKey) {
			out = response.getWriter();
			out.append(joinResStr("40001"));
			out.flush();
			out.close();
			return false;
		}
		
		Date validDate = appKey.getValidDate();
		if(validDate != null && validDate.before(new Date())) {//应用截止日期
			out = response.getWriter();
			out.append(joinResStr("40010"));
			out.flush();
			out.close();
			return false;
		}
		String accessToken = request.getParameter("accessToken");
		if (!StringUtils.hasText(accessToken)) {// 不合法的accessToken
			out = response.getWriter();
			out.append(joinResStr("40002"));
			out.flush();
			out.close();
			return false;
		}
		/**
		 * 确定appId,appSecret,accessToken的校验规则进行校验,如MD5(appId+appSecret)
		 * =accessToken 32位,不区分大小写
		 */
		String tempToken = appId + appKey.getSecretKey();
		String accessTokenTemp = DigestUtils.md5DigestAsHex(tempToken.getBytes());
		if (!accessToken.equalsIgnoreCase(accessTokenTemp)) {
			out = response.getWriter();
			out.append(joinResStr("40002"));
			out.flush();
			out.close();
			return false;
		}
		// 判断是否有接口调用权限
		Map<String, Map<String, Integer>> apiMap = appKey.getApis();
		if (null == apiMap || apiMap.size() == 0 || !apiMap.containsKey(apiName)) {
			// 无调用权限
			out = response.getWriter();
			out.append(joinResStr("40006"));
			out.flush();
			out.close();
			return false;
		}
		Map<String, Integer> methodInfo = apiMap.get(apiName);
		if (methodInfo.get("calltimes") > methodInfo.get("alltimes")) { // 超出调用次数
			out = response.getWriter();
			out.append(joinResStr("40007"));
			out.flush();
			out.close();
			return false;
		}
		// 通过，更新调用次数
		methodInfo.put("calltimes", methodInfo.get("calltimes") + 1);
		apiMap.put(apiName, methodInfo);
		appKey.setApis(apiMap);
		appKeyDbAccess.save(appKey);
		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object arg2, Exception exp)
			throws Exception {
		String apiName = request.getParameter("funcName");
		ApiInfo apiInfo = apiInfoDbAccess.findOne(apiName);
		if (apiInfo != null) {
			ApiAccessLog accessLog = new ApiAccessLog();
			accessLog.setApiName(apiName);
			accessLog.setHost(request.getRemoteAddr());
			accessLog.setUri(apiInfo.getUri());
			accessLog.setAccessDate(new Date());
			if (exp != null) {
				accessLog.setExp(exp.getMessage());
			}
			accessLog.setReqParam(joinReqParams(request));
//			accessLog.setResParam(response.getWriter().toString());
			apiAccessLogDbAccess.save(accessLog);
			PrintWriter out = response.getWriter();
			out.flush();
			out.close();
		}
	}

	/**
	 * 拼接request参数by: key1=value1&key2=value2的形式
	 * 
	 * @param request
	 * @return
	 */
	private String joinReqParams(HttpServletRequest request) {
		StringBuffer paramStr = new StringBuffer();
		Map<String, String[]> params = request.getParameterMap();
		if (params != null && params.size() > 0) {
			for (Map.Entry<String, String[]> p : params.entrySet()) {
				if (p.getValue() == null || p.getValue().length == 0) {
					continue;
				}
				paramStr.append(p.getKey()).append("=").append(p.getValue()[0]).append("&");
			}
			return paramStr.substring(0, paramStr.length() - 1);
		}
		return paramStr.toString();
	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
			throws Exception {

	}

	private String joinResStr(String errorCode) {
		return "{\"errorCode\":" + errorCode + ",\"msg\":\"" + errorCodeMap.get(errorCode) + "\"}";
	}
}
