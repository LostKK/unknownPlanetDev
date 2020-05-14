package com.kk.mapper;

import com.kk.pojo.Word;
import com.kk.utils.MyMapper;

public interface WordMapper extends MyMapper<Word> {
	
	//查询最新发布的文章
	public Word queryMaxWord();
}