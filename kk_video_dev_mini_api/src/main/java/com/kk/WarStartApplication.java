package com.kk;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

/**
 * 继承SpringBootServletInitializer，相当于使用web.xml的形式去启动部署
 * @author Administrator
 *
 */
public class WarStartApplication extends SpringBootServletInitializer{
	
	/**
	 * 重写配置 -->configure
	 */
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder){
		//使用web.xml运行应用程序，指向Application，最后启动springboot
		return builder.sources(Application.class);
	}

}
