package com.kk.service.impl;

import java.util.Date;
import java.util.List;

import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kk.mapper.CommentsMapper;
import com.kk.mapper.CommentsMapperCustom;
import com.kk.mapper.SearchRecordsMapper;
import com.kk.mapper.UsersLikeVideosMapper;
import com.kk.mapper.UsersMapper;
import com.kk.mapper.VideosMapper;
import com.kk.mapper.VideosMapperCustom;
import com.kk.pojo.Comments;
import com.kk.pojo.SearchRecords;
import com.kk.pojo.UsersLikeVideos;
import com.kk.pojo.Videos;
import com.kk.pojo.vo.CommentsVO;
import com.kk.pojo.vo.VideosVO;
import com.kk.service.VideoService;
import com.kk.utils.PagedResult;
import com.kk.utils.TimeAgoUtils;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service
public class VideoServiceImpl implements VideoService {

	@Autowired
	private VideosMapper videosMapper;

	@Autowired
	private VideosMapperCustom videosMapperCustom;

	@Autowired
	private SearchRecordsMapper searchRecordsMapper;

	@Autowired
	private UsersLikeVideosMapper usersLikeVideosMapper;

	@Autowired
	private UsersMapper usersMapper;

	@Autowired
	private CommentsMapper commentMapper;
	
	@Autowired
	private CommentsMapperCustom commentMapperCustom;

	@Autowired
	private Sid sid;

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public String saveVideo(Videos video) {
		String id = sid.nextShort();
		video.setId(id);
		videosMapper.insertSelective(video);
		return id;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void updateVideoForCover(String videoId, String coverPath) {
		Videos video = new Videos();
		video.setId(videoId);
		video.setCoverPath(coverPath);
		videosMapper.updateByPrimaryKeySelective(video);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public PagedResult getAllVideos(Videos video, Integer isSaveRecord, Integer page, Integer pageSize) {

		// 保存热搜词
		String desc = video.getVideoDesc();
		String userId = video.getUserId();
		if (isSaveRecord != null && isSaveRecord == 1) {
			SearchRecords record = new SearchRecords();
			String recordId = sid.nextShort();
			record.setContent(desc);
			record.setId(recordId);
			searchRecordsMapper.insert(record);
		}

		PageHelper.startPage(page, pageSize);
		List<VideosVO> allVideos = videosMapperCustom.queryAllVideos(desc, userId);

		PageInfo<VideosVO> pagelist = new PageInfo(allVideos);
		PagedResult pagedResult = new PagedResult();
		pagedResult.setPage(page);
		pagedResult.setTotal(pagelist.getPages());
		pagedResult.setRows(allVideos);
		pagedResult.setRecords(pagelist.getTotal());

		return pagedResult;
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public List<String> getHotWords() {
		return searchRecordsMapper.getHotWords();
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void userLikeVideo(String userId, String videoId, String videoCreaterId) {
		// 1.保存用户和视频的喜欢点赞关联关系表
		String likeId = sid.nextShort();
		UsersLikeVideos u = new UsersLikeVideos();
		u.setId(likeId);
		u.setUserId(userId);
		u.setVideoId(videoId);
		usersLikeVideosMapper.insert(u);

		// 2.视频喜欢数量增加
		videosMapperCustom.addVideoLikeCount(videoId);

		// 3.用户受喜欢数量的累加
		usersMapper.addReceiveLikeCount(videoCreaterId);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void userUnLikeVideo(String userId, String videoId, String videoCreaterId) {
		// 1.删除用户和视频的喜欢点赞关联关系表
		Example example = new Example(UsersLikeVideos.class);
		Criteria criteria = example.createCriteria();

		criteria.andEqualTo("userId", userId);
		criteria.andEqualTo("videoId", videoId);

		usersLikeVideosMapper.deleteByExample(example);

		// 2.视频喜欢数量减少
		videosMapperCustom.reduceVideoLikeCount(videoId);

		// 3.用户受喜欢数量的减少
		usersMapper.reduceReceiveLikeCount(videoCreaterId);

	}

	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public PagedResult queryMyLikeVideos(String userId, Integer page, Integer pageSize) {
		PageHelper.startPage(page, pageSize);
		List<VideosVO> list = videosMapperCustom.queryMyLikeVideos(userId);

		PageInfo<VideosVO> pageList = new PageInfo<>(list);

		PagedResult pagedResult = new PagedResult();
		pagedResult.setTotal(pageList.getPages());
		pagedResult.setRows(list);
		pagedResult.setPage(page);
		pagedResult.setRecords(pageList.getTotal());

		return pagedResult;
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public PagedResult queryMyFollowVideos(String userId, Integer page, Integer pageSize) {
		PageHelper.startPage(page, pageSize);
		List<VideosVO> list = videosMapperCustom.queryMyFollowVideos(userId);

		PageInfo<VideosVO> pageList = new PageInfo<>(list);

		PagedResult pagedResult = new PagedResult();
		pagedResult.setTotal(pageList.getPages());
		pagedResult.setRows(list);
		pagedResult.setPage(page);
		pagedResult.setRecords(pageList.getTotal());

		return pagedResult;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void saveComment(Comments comment) {
		String id = sid.nextShort();
		comment.setId(id);
		comment.setCreateTime(new Date());
		commentMapper.insert(comment);
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public PagedResult getAllComments(String videoId, Integer page) {
		PageHelper.startPage(page, 10);
		
		List<CommentsVO> list = commentMapperCustom.queryComments(videoId);
		
		for(CommentsVO c:list){
			String timeAgo = TimeAgoUtils.format(c.getCreateTime());
			c.setTimeAgoStr(timeAgo);
		}
		
		PageInfo<CommentsVO> pageList = new PageInfo<>(list);
		
		PagedResult grid = new PagedResult();
		grid.setTotal(pageList.getPages());
		grid.setRows(list);
		grid.setPage(page);
		grid.setRecords(pageList.getTotal());
		
		return grid;
	}

}
