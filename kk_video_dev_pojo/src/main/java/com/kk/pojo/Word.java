package com.kk.pojo;

import java.util.Date;
import javax.persistence.*;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value="文章对象",description="这是文章对象")
public class Word {
    /**
     * 序号
     */
    @ApiModelProperty(value="文章序号",name="id",example="1000",required=true)
    @Id
    private Integer id;

    /**
     * 内容
     */
	@ApiModelProperty(hidden=true)
    private String words;

    /**
     * 作者
     */
	@ApiModelProperty(hidden=true)
    private String writter;

    /**
     * 图片路径
     */
	@ApiModelProperty(hidden=true)
    @Column(name = "pic_path")
    private String picPath;

	@ApiModelProperty(hidden=true)
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 获取序号
     *
     * @return id - 序号
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置序号
     *
     * @param id 序号
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取内容
     *
     * @return words - 内容
     */
    public String getWords() {
        return words;
    }

    /**
     * 设置内容
     *
     * @param words 内容
     */
    public void setWords(String words) {
        this.words = words;
    }

    /**
     * 获取作者
     *
     * @return writter - 作者
     */
    public String getWritter() {
        return writter;
    }

    /**
     * 设置作者
     *
     * @param writter 作者
     */
    public void setWritter(String writter) {
        this.writter = writter;
    }

    /**
     * 获取图片路径
     *
     * @return pic_path - 图片路径
     */
    public String getPicPath() {
        return picPath;
    }

    /**
     * 设置图片路径
     *
     * @param picPath 图片路径
     */
    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

    /**
     * @return create_time
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}