package com.rangers.manage;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
public class SysConfig {

	@Configuration
	@EnableSwagger2
	public static class SwaggerConfig {

		@Bean
		public Docket api() {
			// @formatter:off
			return new Docket(DocumentationType.SWAGGER_2).select()
					.apis(RequestHandlerSelectors.basePackage("com.rangers.manage.apifunc.ctrl"))
					.paths(PathSelectors.any()).build()
					.apiInfo(apiInfo());
			// @formatter:on
		}

		private ApiInfo apiInfo() {
			// @formatter:off
			return new ApiInfoBuilder().title("开发接口测试").description("接口测试文档").version("v1.0").build();
			// @formatter:on
		}

	}

}
