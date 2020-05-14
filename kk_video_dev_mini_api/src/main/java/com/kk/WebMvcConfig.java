package com.kk;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.kk.interceptor.MyInterceptor;

@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {    //配置虚拟路径加载静态资源

//	@Override
//	public void addResourceHandlers(ResourceHandlerRegistry registry) {
//		registry.addResourceHandler("/**")
//		.addResourceLocations("classpath:/META-INF/resources/")
//		        .addResourceLocations("file:///D:/my ware/tiktok/kk_video_dev/");
//	}
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/**")
		.addResourceLocations("classpath:/META-INF/resources/")
		        .addResourceLocations("file:///C:/Tomcat 8.5/webapps/kk_video_dev/");
	}
	
	@Bean(initMethod="init")
	public ZKCuratorClient zkCuratorClient(){
		return new ZKCuratorClient();
	}
	
	@Bean              //以bean的形式把拦截器注册起来
	public MyInterceptor myInterceptor(){ 
		return new MyInterceptor();
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {    //不允许绕过前端访问后台/user
		registry.addInterceptor(myInterceptor()).addPathPatterns("/user/**")
		.addPathPatterns("/video/upload", "/video/uploadCover","/video/userLike","/video/userUnLike","/video/saveComment")
				                                .addPathPatterns("/bgm/**")
				                                .excludePathPatterns("/user/queryPublisher");
		super.addInterceptors(registry);
	}

}
