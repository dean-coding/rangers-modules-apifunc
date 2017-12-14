package com.rangers.manage.apifunc.ctrl;

import java.text.ParseException;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rangers.manage.apifunc.domain.AppKey;
import com.rangers.manage.apifunc.handler.AppKeyHandler;

import lombok.Data;

@RestController
@RequestMapping("/access/control")
public class AppCtrl {

	@Autowired
	private AppKeyHandler appKeyHandler;

	@PostMapping
	public AppKey addApp(@RequestBody @Valid AppDto appDto) throws ParseException {

		return appKeyHandler.generate(appDto.getRefToken(), DateUtils.parseDate(appDto.getValidate(), "yyyy-MM-dd"),
				appDto.getAppGrade(), appDto.getAlltimes());

	}

	@Data
	public static class AppDto {
		@NotNull(message = "the refToken must be not null")
		private String refToken;
		@NotNull(message = "the validate must be not null")
		private String validate;
		@NotNull(message = "the appGrade must be not null")
		private String appGrade;
		@NotNull(message = "the alltimes must be not null")
		private Integer alltimes;
	}
}
