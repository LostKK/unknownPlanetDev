package com.kk.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kk.mapper.UsersFansMapper;
import com.kk.mapper.UsersLikeVideosMapper;
import com.kk.mapper.UsersMapper;
import com.kk.mapper.UsersReportMapper;
import com.kk.pojo.Users;
import com.kk.pojo.UsersFans;
import com.kk.pojo.UsersLikeVideos;
import com.kk.pojo.UsersReport;
import com.kk.service.UserService;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UsersMapper userMapper;

	@Autowired
	private UsersFansMapper usersFansMapper;

	@Autowired
	private UsersLikeVideosMapper usersLikeVideosMapper;
	
	@Autowired
	private UsersReportMapper usersReportMapper;

	@Autowired
	private Sid sid; // 注入自增的id工具

	@Transactional(propagation = Propagation.SUPPORTS) // 事务支持
	@Override
	public boolean queryUsernameIsExist(String username) {
		Users user = new Users();
		user.setUsername(username);
		Users result = userMapper.selectOne(user);
		return result == null ? false : true;
	}

	@Transactional(propagation = Propagation.REQUIRED) // 事务需要
	@Override
	public void saveUser(Users user) {
		String userId = sid.nextShort();
		user.setId(userId);
		userMapper.insert(user);

	}

	@Transactional(propagation = Propagation.SUPPORTS) // 事务支持
	@Override
	public Users queryUserForLogin(String username, String password) {
		Example userExample = new Example(Users.class);
		Criteria criteria = userExample.createCriteria(); // Criteria类提供数据查询方式
		criteria.andEqualTo("username", username);
		criteria.andEqualTo("password", password);
		Users result = userMapper.selectOneByExample(userExample);

		return result;
	}

	@Transactional(propagation = Propagation.REQUIRED) // 事务需要
	@Override
	public void updataUserInfo(Users user) {
		Example userExample = new Example(Users.class);
		Criteria criteria = userExample.createCriteria(); // Criteria类提供数据查询方式
		criteria.andEqualTo("id", user.getId());
		userMapper.updateByExampleSelective(user, userExample);
	}

	@Transactional(propagation = Propagation.SUPPORTS) // 事务支持
	@Override
	public Users queryUserInfo(String userId) {
		Example userExample = new Example(Users.class);
		Criteria criteria = userExample.createCriteria(); // Criteria类提供数据查询方式
		criteria.andEqualTo("id", userId);
		Users user = userMapper.selectOneByExample(userExample);
		return user;
	}

	@Transactional(propagation = Propagation.SUPPORTS) // 事务支持
	@Override
	public boolean isUserLikeVideo(String userId, String videoId) {

		if (StringUtils.isBlank(userId) || StringUtils.isBlank(videoId)) {
			return false;
		}

		Example userExample = new Example(UsersLikeVideos.class);
		Criteria criteria = userExample.createCriteria(); // Criteria类提供数据查询方式

		criteria.andEqualTo("userId", userId);
		criteria.andEqualTo("videoId", videoId);

		List<UsersLikeVideos> list = usersLikeVideosMapper.selectByExample(userExample);

		if (list != null && list.size() > 0) {
			return true;
		}

		return false;
	}

	@Transactional(propagation = Propagation.REQUIRED) // 事务需要
	@Override
	public void saveUserFanRelation(String userId, String fanId) {

		String relId = sid.nextShort();

		UsersFans userFan = new UsersFans();
		userFan.setId(relId);
		userFan.setUserId(userId);
		userFan.setFanId(fanId);

		usersFansMapper.insert(userFan);

		userMapper.addFansCount(userId);
		userMapper.addFollowCount(fanId);
	}

	@Transactional(propagation = Propagation.REQUIRED) // 事务需要
	@Override
	public void deleteUserFanRelation(String userId, String fanId) {

		Example example = new Example(UsersFans.class);

		Criteria criteria = example.createCriteria(); // Criteria类提供数据查询方式

		criteria.andEqualTo("userId", userId);
		criteria.andEqualTo("fanId", fanId);

		usersFansMapper.deleteByExample(example);

		userMapper.reduceFansCount(userId);
		userMapper.reduceFollowCount(fanId);
	}

	@Transactional(propagation = Propagation.SUPPORTS) // 事务支持
	@Override
	public boolean queryIfFollow(String userId, String fanId) {
		Example example = new Example(UsersFans.class);

		Criteria criteria = example.createCriteria(); // Criteria类提供数据查询方式

		criteria.andEqualTo("userId", userId);
		criteria.andEqualTo("fanId", fanId);

		List<UsersFans> list = usersFansMapper.selectByExample(example);

		if (list != null && !list.isEmpty() && list.size() > 0) {
			return true;
		}
		return false;
	}

	@Transactional(propagation = Propagation.REQUIRED) // 事务需要
	@Override
	public void reportUser(UsersReport usersReport) {
		String urId = sid.nextShort();
		usersReport.setId(urId);
		usersReport.setCreateDate(new Date());
		
		usersReportMapper.insert(usersReport);
		
	}

}
