package com.kk;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration     //配置
@EnableSwagger2     //打开
public class Swagger2 {

	//swagger2的配置文件，这里可以配置swagger2的一些基本内容，比如扫描的包等等
	@Bean
	public Docket createRestApi(){
		return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select()
				.apis(RequestHandlerSelectors.basePackage("com.kk.controller"))
				.paths(PathSelectors.any()).build();
	}
	
	//构建api文档的信息
	private ApiInfo apiInfo(){
		return new ApiInfoBuilder()
				//设置页面标题
				.title("KK-Api---使用swagger2构建短视频后端api接口文档")
				//设置联系人
				.contact(new Contact("吟游诗人-captain kk","http://www.baidu.com","1395175536@qq.com"))
				//描述
				.description("欢迎访问短视频接口文档，这里是描述信息")
				//定义版本号
				.version("6.0").build();
	}
}
