package com.kk.mapper;

import java.util.List;

import com.kk.pojo.Comments;
import com.kk.pojo.vo.CommentsVO;
import com.kk.utils.MyMapper;

public interface CommentsMapperCustom extends MyMapper<Comments>{
   public List<CommentsVO> queryComments(String videoId);
}
