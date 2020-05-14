package com.kk.mapper;

import com.kk.pojo.Users;
import com.kk.utils.MyMapper;

public interface UsersMapper extends MyMapper<Users> {
	
	//用户受喜欢数累加
	public void addReceiveLikeCount(String userId);
	
	//用户受喜欢数累减
	public void reduceReceiveLikeCount(String userId);
	
	//增加粉丝数
	public void addFansCount(String userId);
	
	//减少粉丝数
	public void reduceFansCount(String userId);
	
	//增加关注数
	public void addFollowCount(String userId);
	
	//减少关注数
	public void reduceFollowCount(String userId);
}