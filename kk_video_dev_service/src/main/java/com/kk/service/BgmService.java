package com.kk.service;

import java.util.List;

import com.kk.pojo.Bgm;

public interface BgmService {

	//查询背景音乐列表
	public List<Bgm> queryBgmList();
	
	//根据id查询bgm信息
	public Bgm queryBgmById(String bgmId);
}
