package com.kk.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kk.enums.VideoStatusEnum;
import com.kk.pojo.Bgm;
import com.kk.pojo.Comments;
import com.kk.pojo.Users;
import com.kk.pojo.Videos;
import com.kk.service.BgmService;
import com.kk.service.VideoService;
import com.kk.utils.KKJSONResult;
import com.kk.utils.MakeVideoCover;
import com.kk.utils.MixVideoAndMusic;
import com.kk.utils.PagedResult;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@Api(value = "视频相关业务的接口", tags = { "视频相关业务的controller" })
@RequestMapping("/video")
public class VideoController extends BaseController {

	@Autowired
	private BgmService bgmService;

	@Autowired
	private VideoService videoService;

	@ApiOperation(value = "上传视频", notes = "上传视频的接口")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "userId", value = "用户ID", required = true, dataType = "String", paramType = "form"),
			@ApiImplicitParam(name = "bgmId", value = "背景音乐ID", required = false, dataType = "String", paramType = "form"),
			@ApiImplicitParam(name = "videoSeconds", value = "音乐长度", required = true, dataType = "String", paramType = "form"),
			@ApiImplicitParam(name = "videoWidth", value = "视频宽度", required = true, dataType = "String", paramType = "form"),
			@ApiImplicitParam(name = "videoHeight", value = "视频长度", required = true, dataType = "String", paramType = "form"),
			@ApiImplicitParam(name = "desc", value = "视频描述", required = false, dataType = "String", paramType = "form") })
	@PostMapping(value = "/uploadVideo", headers = "content-type=multipart/form-data")
	public KKJSONResult uploadVideo(String userId, String bgmId, double videoSeconds, int videoWidth, int videoHeight,
			String desc, @ApiParam(value = "短视频", required = true) MultipartFile file) throws Exception {

		if (StringUtils.isBlank(userId)) {
			return KKJSONResult.errorMsg("用户id不能为空...");
		}

		// 文件保存的命名空间
		// String fileSpace = "D:/my ware/tiktok/kk_video_dev";

		// 保存到数据库中的相对路径
		//String uploadPathDB = "/" + userId + "/video";
		String uploadPathDB = "/../kk_video_dev/" + userId + "/video";
		
		//String coverPathDB = "/" + userId + "/video";
		String coverPathDB = "/../kk_video_dev/" + userId + "/video";
		FileOutputStream fileOutputStream = null;
		InputStream inputStream = null;
		String finalVideoPath = "";

		try {
			if (file != null) {

				String fileName = file.getOriginalFilename();
				
				String arrayFilenameItem[] =  fileName.split("\\.");
				String fileNamePrefix = "";
				for (int i = 0 ; i < arrayFilenameItem.length-1 ; i ++) {
					fileNamePrefix += arrayFilenameItem[i];
				}
				// fix bug: 解决小程序端OK，PC端不OK的bug，原因：PC端和小程序端对临时视频的命名不同
//				String fileNamePrefix = fileName.split("\\.")[0];  //截取前缀作为封面的名称
				
				
				if (StringUtils.isNotBlank(fileName)) {
					// 文件上传的最终保存路径
					finalVideoPath = FILE_SPACE + uploadPathDB + "/" + fileName;
					// 设置数据库保存的路径
					uploadPathDB += ("/" + fileName);
					//设置封面保存的路径
					coverPathDB = coverPathDB + "/" + fileNamePrefix + ".jpg";
					

					File outFile = new File(finalVideoPath);
					if (outFile.getParentFile() != null || !outFile.getParentFile().isDirectory()) {
						// 创建父文件夹
						outFile.getParentFile().mkdirs();
					}

					fileOutputStream = new FileOutputStream(outFile);
					inputStream = file.getInputStream();
					IOUtils.copy(inputStream, fileOutputStream);
				}
			} else {
				return KKJSONResult.errorMsg("上传有问题");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return KKJSONResult.errorMsg("上传有问题");
		} finally {
			if (fileOutputStream != null) {
				fileOutputStream.flush();
				fileOutputStream.close();
			}
		}

		// 判断bgmId是否为空，如果不为空
		// 那就查询bgm的信息，并且合并视频，生成新的视频
		if (StringUtils.isNotBlank(bgmId)) {
			Bgm bgm = bgmService.queryBgmById(bgmId);
			String mp3InputPath = FILE_SPACE + bgm.getPath(); // 获取mp3的基地址

			MixVideoAndMusic mixtool = new MixVideoAndMusic(FFMPEG_EXE);
			String videoInputPath = finalVideoPath;

			String videoOutputName = UUID.randomUUID().toString() + ".mp4";
			uploadPathDB = "/" + userId + "/video" + "/" + videoOutputName;
			finalVideoPath = FILE_SPACE + uploadPathDB;
			mixtool.convertor(mp3InputPath, videoInputPath, videoSeconds, finalVideoPath);
		}
		System.out.println("uploadPathDB" + uploadPathDB);
		System.out.println("finalVideoPath" + finalVideoPath);
		
		//对视频进行截图
		MakeVideoCover coverTool = new MakeVideoCover(FFMPEG_EXE);
		coverTool.convertor(finalVideoPath, FILE_SPACE + coverPathDB);

		// 保存视频信息到数据库
		Videos video = new Videos();
		video.setAudioId(bgmId);
		video.setUserId(userId);
		video.setVideoSeconds((float) videoSeconds);
		video.setVideoHeight(videoHeight);
		video.setVideoWidth(videoWidth);
		video.setVideoDesc(desc);
		video.setVideoPath(uploadPathDB);
		video.setCoverPath(coverPathDB);
		System.out.println("get cover path:"+coverPathDB);
		video.setStatus(VideoStatusEnum.SUCCESS.value);
		video.setCreateTime(new Date());

		String videoId = videoService.saveVideo(video);

		return KKJSONResult.ok(videoId);
	}

//	@ApiOperation(value = "上传视频封面", notes = "上传视频封面的接口")
//	@ApiImplicitParams({
//			@ApiImplicitParam(name = "userId", value = "用户ID", required = true, dataType = "String", paramType = "form"),
//			@ApiImplicitParam(name = "videoId", value = "视频主键ID", required = true, dataType = "String", paramType = "form") })
//	@PostMapping(value = "/uploadCover", headers = "content-type=multipart/form-data")
//	public KKJSONResult uploadCover(String userId,String videoId, @ApiParam(value = "封面", required = true) MultipartFile file)
//			throws Exception {
//
//		if (StringUtils.isBlank(videoId) || StringUtils.isBlank(userId)) {
//			return KKJSONResult.errorMsg("视频主键id或用户id不能为空...");
//		}
//
//		// 文件保存的命名空间
//		// String fileSpace = "D:/my ware/tiktok/kk_video_dev";
//
//		// 保存到数据库中的相对路径
//		String uploadPathDB = "/" + userId + "/video";
//
//		FileOutputStream fileOutputStream = null;
//		InputStream inputStream = null;
//		String finalCoverPath = "";
//
//		try {
//			if (file != null) {
//
//				String fileName = file.getOriginalFilename();
//				if (StringUtils.isNotBlank(fileName)) {
//					// 文件上传的最终保存路径
//					finalCoverPath = FILE_SPACE + uploadPathDB + "/" + fileName;
//					// 设置数据库保存的路径
//					uploadPathDB += ("/" + fileName);
//
//					File outFile = new File(finalCoverPath);
//					if (outFile.getParentFile() != null || !outFile.getParentFile().isDirectory()) {
//						// 创建父文件夹
//						outFile.getParentFile().mkdirs();
//					}
//
//					fileOutputStream = new FileOutputStream(outFile);
//					inputStream = file.getInputStream();
//					IOUtils.copy(inputStream, fileOutputStream);
//				}
//			} else {
//				return KKJSONResult.errorMsg("上传有问题");
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			return KKJSONResult.errorMsg("上传有问题");
//		} finally {
//			if (fileOutputStream != null) {
//				fileOutputStream.flush();
//				fileOutputStream.close();
//			}
//		}
//		
//		videoService.updateVideoForCover(videoId, uploadPathDB);
//        System.out.println("22:"+uploadPathDB);
//		return KKJSONResult.ok();
//	}
	/**
	 * 
	 * @param video
	 * @param isSaveRecord 1:需要保存 0:不需要保存，或者为空的时候
	 * @param page
	 * @return
	 * @throws Exception
	 */

	@PostMapping(value = "/showAll")
	public KKJSONResult uploadVideo(@RequestBody Videos video,Integer isSaveRecord,Integer page) throws Exception {
		if(page == null){
			page = 1;
		}
		PagedResult result = videoService.getAllVideos(video,isSaveRecord,page, PAGE_SIZE);
		return KKJSONResult.ok(result);
	}
	
	/**
	 * 我收藏（点赞）过的视频列表
	 * @param userId
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/showMyLike")
	public KKJSONResult showMyLike(String userId, Integer page) throws Exception {
		
		if (StringUtils.isBlank(userId)) {
			return KKJSONResult.ok();
		}
		
		if (page == null) {
			page = 1;
		}

		PagedResult videosList = videoService.queryMyLikeVideos(userId, page, PAGE_SIZE);
		
		return KKJSONResult.ok(videosList);
	}
	
	/**
	 * 我关注的人发的视频
	 * @param userId
	 * @param page
	 * @return
	 * @throws Exception
	 */
	
	@PostMapping("/showMyFollow")
	public KKJSONResult showMyFollow(String userId, Integer page) throws Exception {
		
		if (StringUtils.isBlank(userId)) {
			return KKJSONResult.ok();
		}
		
		if (page == null) {
			page = 1;
		}
		
		PagedResult videosList = videoService.queryMyFollowVideos(userId, page, PAGE_SIZE);
		
		return KKJSONResult.ok(videosList);
	}
	
	@PostMapping(value = "/hot")
	public KKJSONResult hot() throws Exception {
		return KKJSONResult.ok(videoService.getHotWords());
	}
	
	@PostMapping(value = "/userLike")
	public KKJSONResult userLike(String userId, String videoId,String videoCreaterId) throws Exception {
		videoService.userLikeVideo(userId, videoId, videoCreaterId);
		return KKJSONResult.ok();
	}
	
	@PostMapping(value = "/userUnLike")
	public KKJSONResult userUnLike(String userId, String videoId,String videoCreaterId) throws Exception {
		videoService.userUnLikeVideo(userId, videoId, videoCreaterId);
		return KKJSONResult.ok();
	}
	
	@PostMapping(value = "/saveComment")
	public KKJSONResult saveComment(@RequestBody Comments comment, String fatherCommentId, String toUserId) throws Exception{
		
		if(fatherCommentId != null && fatherCommentId != "" && toUserId != null && toUserId != ""){
			comment.setFatherCommentId(fatherCommentId);
			comment.setToUserId(toUserId);
		}
		videoService.saveComment(comment);
		return KKJSONResult.ok();
	}
	
	@PostMapping(value = "/getVideoComments")
	public KKJSONResult getVideoComments(String videoId, Integer page) throws Exception{
		
		if(StringUtils.isBlank(videoId)){
			return KKJSONResult.ok();
		}
		
		//分页查询视频列表，时间顺序倒序排序
		if(page == null){
			page = 1;
		}
		
		PagedResult list = videoService.getAllComments(videoId, page);
		
		return KKJSONResult.ok(list);
	}
}
