package com.kk.interceptor;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.kk.utils.JsonUtils;
import com.kk.utils.KKJSONResult;
import com.kk.utils.RedisOperator;

public class MyInterceptor implements HandlerInterceptor {
	
	@Autowired
	public RedisOperator redis;
	public static final String USER_REDIS_SESSION = "user-redis-session";
	

	//拦截请求，在controller调用之前
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object arg2) throws Exception {
		
		System.out.println("拦截器开始工作");
		
		String userId = request.getHeader("userId");                      //从header获取userId和userToken
		String userToken = request.getHeader("userToken");
		
		if(StringUtils.isNotBlank(userId) && StringUtils.isNotBlank(userToken)){
			String uniqueToken = redis.get(USER_REDIS_SESSION + ":" + userId);      //获取redis.userId
			if(StringUtils.isEmpty(uniqueToken) && StringUtils.isBlank(uniqueToken)){
				System.out.println("信息过期...");
				returnErrorResponse(response,new KKJSONResult().errorTokenMsg("请登录..."));
				return false;
			}else{                                                         //此处对手机端，服务器端token判别
				if(!uniqueToken.equals(userToken)){
					System.out.println("帐号被挤出...");
					returnErrorResponse(response,new KKJSONResult().errorTokenMsg("帐号被挤出..."));
					return false;
				}
			}
		}else{
			System.out.println("用户信息为空,请登录");
			returnErrorResponse(response,new KKJSONResult().errorTokenMsg("请登录..."));
			return false;
		}
		return true;
	}
	
	//从服务器端返回json类型的结果
	public void returnErrorResponse(HttpServletResponse response, KKJSONResult result) throws IOException, UnsupportedEncodingException{ 
		OutputStream out = null;
		try{
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/json");
			out = response.getOutputStream();
			out.write(JsonUtils.objectToJson(result).getBytes("utf-8"));
			out.flush();
		}finally{
			if(out!=null){
				out.close();
			}
		}
	}
	
	
	//请求controller之后，渲染视图之前
	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
			throws Exception {
		// TODO Auto-generated method stub

	}

	//请求controller之后，视图渲染之后
	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		// TODO Auto-generated method stub

	}

}
