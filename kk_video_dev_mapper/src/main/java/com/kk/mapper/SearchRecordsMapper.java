package com.kk.mapper;

import java.util.List;

import com.kk.pojo.SearchRecords;
import com.kk.utils.MyMapper;

public interface SearchRecordsMapper extends MyMapper<SearchRecords> {
	public List<String> getHotWords();
}