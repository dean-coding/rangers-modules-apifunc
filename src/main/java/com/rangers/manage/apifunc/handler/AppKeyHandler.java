package com.rangers.manage.apifunc.handler;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.AlternativeJdkIdGenerator;

import com.rangers.manage.apifunc.db.AppKeyDbAccess;
import com.rangers.manage.apifunc.domain.ApifuncEnum;
import com.rangers.manage.apifunc.domain.AppKey;

/**
 * app应用业务
 * 
 * @author fuhw
 * @date 2017年12月13日
 */
@Service
public class AppKeyHandler {

	@Autowired
	private AppKeyDbAccess dbAccess;
	
	public AppKey generate(String refToken,Date validDate, String appGrade, int alltimes) {
		AppKey appKey = new AppKey();
		appKey.setRefToken(refToken);
		appKey.setAppId(new AlternativeJdkIdGenerator().generateId().toString().replaceAll("-", ""));
		appKey.setSecretKey(
				new AlternativeJdkIdGenerator().generateId().toString().replaceAll("-", "").substring(0, 18));
		appKey.setDisabled(false);
		appKey.setAppGrade(appGrade);
		appKey.setValidDate(validDate);
		setDefaultApis(appKey, alltimes);
		appKey.setCreateDate(new Date());
		appKey = dbAccess.save(appKey);
		return appKey;
	}

	public void setDefaultApis(AppKey appKey, int alltimes) {
		ApifuncEnum[] apifuncEnums = ApifuncEnum.values();
		Map<String, Map<String, Integer>> apiMap = new HashMap<>();
		for (ApifuncEnum apifuncEnum : apifuncEnums) {
			Map<String, Integer> tmpMap = new HashMap<String, Integer>();
			tmpMap.put("calltimes", 0);
			tmpMap.put("alltimes", alltimes);
			apiMap.put(apifuncEnum.name(), tmpMap);
		}
		appKey.setApis(apiMap);
	}

	public void setDefaultApis(AppKey appKey) {
		setDefaultApis(appKey, 1000);
	}

}
