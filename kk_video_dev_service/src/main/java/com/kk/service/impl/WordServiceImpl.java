package com.kk.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kk.mapper.WordMapper;
import com.kk.pojo.Word;
import com.kk.service.WordService;

@Service
public class WordServiceImpl implements WordService{
	
	@Autowired
	private WordMapper wordMapper;

	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public List<Word> queryWordList() {
		return wordMapper.selectAll();
	}
	
	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public Word queryWordById(Integer wordId) {
		return wordMapper.selectByPrimaryKey(wordId);
	}
	
	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public Word queryMaxWord(){
		return wordMapper.queryMaxWord();
	}
    
}
