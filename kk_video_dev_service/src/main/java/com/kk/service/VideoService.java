package com.kk.service;

import java.util.List;

import com.kk.pojo.Comments;
import com.kk.pojo.Videos;
import com.kk.utils.PagedResult;

public interface VideoService {
	
	//保存视频
	public String saveVideo(Videos video);
	
	//更改封面
	public void updateVideoForCover(String videoId, String coverPath);
	
	//分页查询视频列表
	public PagedResult getAllVideos(Videos video,Integer isSaveRecord,Integer page, Integer pageSize);
	
	//分页查询我喜欢的视频列表
	public PagedResult queryMyLikeVideos(String userId, Integer page, Integer pageSize);
	
	//分页查询我关注的人的视频列表
	public PagedResult queryMyFollowVideos(String userId, Integer page, Integer pageSize);
	
	//分页热搜词列表
	public List<String> getHotWords();
	
	//用户为喜欢的视频点赞
	public void userLikeVideo(String userId, String videoId, String videoCreaterId);
	
	//用户为已点赞的视频取消点赞
	public void userUnLikeVideo(String userId, String videoId, String videoCreaterId);
	
	//用户为视频添加评论
	public void saveComment(Comments comment);
	
	//用户查看视频评论
	public PagedResult getAllComments(String videoId, Integer page);

}
