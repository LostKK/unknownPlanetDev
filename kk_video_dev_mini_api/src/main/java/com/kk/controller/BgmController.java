package com.kk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kk.service.BgmService;
import com.kk.utils.KKJSONResult;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value="背景音乐业务的接口",tags={"背景音乐业务的controller"})
@RequestMapping("/bgms")
public class BgmController {
	
	@Autowired
	private BgmService bgmService;
	
	@ApiOperation(value="获取背景音乐列表",notes="获取背景音乐列表的接口")
	@PostMapping("/list")
	public KKJSONResult list(){
		return KKJSONResult.ok(bgmService.queryBgmList());
	}
	

}
