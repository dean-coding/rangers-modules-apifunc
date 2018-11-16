package com.rangers.manage.apifunc.handler;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class ApiWebConfigure extends WebMvcConfigurerAdapter {
	@Override
	public void addInterceptors(InterceptorRegistry registry){
		registry.addInterceptor(getApiInterceptor())
			.addPathPatterns("/funcs/**");
	}
	@Bean
	public ApiInterceptor getApiInterceptor(){
		return new ApiInterceptor();
	}
}
