package com.kk.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kk.pojo.Users;
import com.kk.pojo.UsersReport;
import com.kk.pojo.vo.PublisherVideo;
import com.kk.pojo.vo.UsersVO;
import com.kk.service.UserService;
import com.kk.utils.KKJSONResult;
import com.kk.utils.MD5Utils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "用户相关业务的接口", tags = { "用户相关业务的controller" })
@RequestMapping("/user")
public class UserController extends BaseController {

	@Autowired
	private UserService userService;

	@ApiOperation(value = "用户上传头像", notes = "用户上传头像的接口")
	@ApiImplicitParam(name="userId",value="用户ID",required=true,dataType="String",paramType="query")
	@PostMapping("/uploadFace")
	public KKJSONResult uploadFace(String userId,@RequestParam("file") MultipartFile[] files) throws Exception {
		
		if (StringUtils.isBlank(userId)) {
			return KKJSONResult.errorMsg("用户id不能为空...");
		}
		
		//文件保存的命名空间
		//String fileSpace = "D:/my ware/tiktok/kk_video_dev";
		String fileSpace = "C:/Tomcat 8.5/webapps/kk_video_dev";
		
		//保存到数据库中的相对路径
		//String uploadPathDB = "/" + userId + "/face";
		String uploadPathDB = "/../kk_video_dev/" + userId + "/face";
				
		FileOutputStream fileOutputStream = null;
		InputStream inputStream = null;
		
		try {
			if(files != null && files.length > 0){
				
				String fileName = files[0].getOriginalFilename();
				if(StringUtils.isNotBlank(fileName)){
					//文件上传的最终保存路径
					String finalFacePath = fileSpace + uploadPathDB + "/" + fileName;
					//设置数据库保存的路径
					uploadPathDB += ("/" + fileName);
					
					File outFile = new File(finalFacePath);
					if(outFile.getParentFile() != null || !outFile.getParentFile().isDirectory()){
						//创建父文件夹
						outFile.getParentFile().mkdirs();
					}
					
					fileOutputStream = new FileOutputStream(outFile);
					inputStream = files[0].getInputStream();
					IOUtils.copy(inputStream, fileOutputStream);
				}
			}else{
				return KKJSONResult.errorMsg("上传有问题");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return KKJSONResult.errorMsg("上传有问题");
		}finally{
			if(fileOutputStream !=null){
				fileOutputStream.flush();
				fileOutputStream.close();
			}
		}
		
		Users user = new Users();
		user.setId(userId);
		user.setFaceImage(uploadPathDB);
		userService.updataUserInfo(user);
		
		return KKJSONResult.ok(uploadPathDB);
	}
	
	@ApiOperation(value = "查询用户信息", notes = "查询用户信息的接口")
	@ApiImplicitParam(name="userId",value="用户ID",required=true,dataType="String",paramType="query")
	@PostMapping("/query")
	public KKJSONResult query(String userId,String fanId) throws Exception{
		if(StringUtils.isBlank(userId)){
			return KKJSONResult.errorMsg("用户id不能为空...");
		}
		
		Users userInfo = userService.queryUserInfo(userId);
		UsersVO userVO = new UsersVO();
		BeanUtils.copyProperties(userInfo, userVO);
		
		userVO.setFollow(userService.queryIfFollow(userId, fanId));
		
		return KKJSONResult.ok(userVO);
		
	}
	
	@PostMapping("/queryPublisher")
	public KKJSONResult queryPublisher(String loginUserId, String videoId, String publisherUserId) throws Exception{
		
		if(StringUtils.isBlank(publisherUserId)){
			return KKJSONResult.errorMsg("");
		}
		
		//1.查询视频发布者的信息
		Users userInfo = userService.queryUserInfo(publisherUserId);
		UsersVO publisher = new UsersVO();
		BeanUtils.copyProperties(userInfo, publisher);
		
		//2.查询当前登录者和视频的点赞关系
		boolean userLikeVideo = userService.isUserLikeVideo(loginUserId, videoId);
		
		PublisherVideo bean = new PublisherVideo();
		bean.setPublisher(publisher);
		bean.setUserLikeVideo(userLikeVideo);
		
		return KKJSONResult.ok(bean);
	}
	
	@PostMapping("/becomeFans")
	public KKJSONResult becomeFans(String userId, String fanId) throws Exception{
		if(StringUtils.isBlank(userId) || StringUtils.isBlank(fanId)){
			return KKJSONResult.errorMsg("");
		}
		
		userService.saveUserFanRelation(userId, fanId);
		
		return KKJSONResult.ok("关注成功");
	}
	
	@PostMapping("/cancelFans")
	public KKJSONResult cancelFans(String userId, String fanId) throws Exception{
		if(StringUtils.isBlank(userId) || StringUtils.isBlank(fanId)){
			return KKJSONResult.errorMsg("");
		}
		
		userService.deleteUserFanRelation(userId, fanId);
		
		return KKJSONResult.ok("取消关注成功");
	}
	
	@PostMapping("/reportUser")
	public KKJSONResult reportUser(@RequestBody UsersReport usersReport) throws Exception{
		
		//保存举报信息
		userService.reportUser(usersReport);
		
		return KKJSONResult.errorMsg("举报成功，向KK反馈有奖励哦");
	}
}
