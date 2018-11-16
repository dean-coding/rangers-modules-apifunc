package com.rangers.manage.apifunc.handler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.rangers.manage.apifunc.db.ApiInfoDbAccess;
import com.rangers.manage.apifunc.domain.ApiInfo;
import com.rangers.manage.apifunc.domain.Apifunc;


/**
 * 监听容器启动
 * @author vencano
 *
 */
//@WebListener
@Component
public class ApiServletContextListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		WebApplicationContext context = WebApplicationContextUtils
				.getRequiredWebApplicationContext(arg0.getServletContext());

		ApiInfoDbAccess apiInfoDbAccess = context.getBean(ApiInfoDbAccess.class);

		RequestMappingHandlerMapping reqMapping = context.getBean(RequestMappingHandlerMapping.class);

		Map<RequestMappingInfo, HandlerMethod> handlerMethods = reqMapping.getHandlerMethods();

		List<ApiInfo> apiInfos = new ArrayList<>();
		Date curDate = new Date();
		handlerMethods.forEach((info, handler) -> {
			Set<RequestMethod> methods = info.getMethodsCondition().getMethods();
			if (methods.size() == 0)
				return;
			if (!handler.hasMethodAnnotation(Apifunc.class))
				return;
			Apifunc apifunc = handler.getMethodAnnotation(Apifunc.class);
			ApiInfo apiInfo = new ApiInfo();
			apiInfo.setName(apifunc.name().name());
			apiInfo.setComment((apifunc.name().getComment()));
			apiInfo.setUri(info.getPatternsCondition().getPatterns().toArray()[0].toString());
			apiInfo.setVersion(apifunc.version());
			apiInfo.setDisabled(apifunc.disabled());
			apiInfo.setAccessLimit(apifunc.accessLimit());
			apiInfo.setReqMethod(methods.toArray()[0].toString());
			apiInfo.setCreateDate(curDate);
			apiInfos.add(apiInfo);
		});
		apiInfoDbAccess.save(apiInfos);
	}
}
