package com.kk.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.kk.pojo.Videos;
import com.kk.pojo.vo.VideosVO;
import com.kk.utils.MyMapper;

public interface VideosMapperCustom extends MyMapper<Videos>{
	//条件查询所有视频列表
	public List<VideosVO> queryAllVideos(@Param("videoDesc") String videoDesc, @Param("userId") String userId);
	
	//查询关注的视频
	public List<VideosVO> queryMyFollowVideos(@Param("userId") String userId);
	
	//查询点赞的视频
	public List<VideosVO> queryMyLikeVideos(@Param("userId") String userId);
	
	//对视频喜欢的数量进行累加
	public void addVideoLikeCount(String videoId);
	
	//对视频喜欢的数量进行累减
	public void reduceVideoLikeCount(String videoId);
	
}
