package com.kk.service;

import com.kk.pojo.Users;
import com.kk.pojo.UsersReport;

public interface UserService {

	// 判断用户名是否存在
	public boolean queryUsernameIsExist(String username);

	// 保存用户（用户注册）
	public void saveUser(Users user);

	// 用户登录，根据用户名和密码查询用户是否存在
	public Users queryUserForLogin(String username, String password);

	// 用户修改信息
	public void updataUserInfo(Users user);

	// 查询用户信息
	public Users queryUserInfo(String userId);

	// 查询用户是否已经点赞一个视频
	public boolean isUserLikeVideo(String userId, String videoId);
	
	//增加用户与粉丝的关联
	public void saveUserFanRelation(String userId, String fanId);
	
	//删除用户与粉丝的关联
	public void deleteUserFanRelation(String userId, String fanId);
	
	//查询用户是否关注
	public boolean queryIfFollow(String userId, String fanId);
	
	//举报用户
	public void reportUser(UsersReport usersReport);
}
