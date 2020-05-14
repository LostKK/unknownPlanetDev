package com.kk.service;

import java.util.List;

import com.kk.pojo.Word;

public interface WordService {
	
	//查询文章列表
	public List<Word> queryWordList();
	
	//根据id查询word信息
	public Word queryWordById(Integer wordId);
	
	//查询最新word信息
	public Word queryMaxWord();

}
