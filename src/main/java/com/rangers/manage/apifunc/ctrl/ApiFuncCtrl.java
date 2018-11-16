package com.rangers.manage.apifunc.ctrl;

import java.util.Collections;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rangers.manage.apifunc.domain.Apifunc;
import com.rangers.manage.apifunc.domain.ApifuncEnum;

@RestController
@RequestMapping("/funcs/api")
public class ApiFuncCtrl {

	@Apifunc(name = ApifuncEnum.GET_MONTH_ORDERS)
	@GetMapping("/one-month")
	public Map<String, String> getOne() {
		return Collections.singletonMap("msg", "you get orders of one month");
	}

	@Apifunc(name = ApifuncEnum.GET_YERAR_ORDERS)
	@GetMapping("/one-year")
	public Map<String, String> getTow() {
		return Collections.singletonMap("msg", "you get orders of one year");
	}

}
