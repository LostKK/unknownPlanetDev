package com.kk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kk.pojo.Word;
import com.kk.service.WordService;
import com.kk.utils.KKJSONResult;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value="文章业务的接口",tags={"文章业务的controller"})
@RequestMapping("/word")
public class WordController {
  
	@Autowired
	private WordService wordService;
	
	@ApiOperation(value="获取文章",notes="获取文章的接口")
	@PostMapping("/list")
	public KKJSONResult list(){
		return KKJSONResult.ok(wordService.queryWordList());
	}
	
	@ApiOperation(value="获取最新文章",notes="获取最新文章的接口")
	@PostMapping("/newest")
	public KKJSONResult newest(){
		return KKJSONResult.ok(wordService.queryMaxWord());
	}
	
	@ApiOperation(value="根据id获取文章",notes="根据id获取文章的接口")
	@PostMapping("/getOne")
	public KKJSONResult getOne(@RequestBody Word word){
		
		return KKJSONResult.ok(wordService.queryWordById(word.getId()));
	}
	
}
