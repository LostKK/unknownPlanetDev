package com.kk.enums;

public enum VideoStatusEnum {
	
	SUCCESS(1), //发布成功
	FORBID(2);    //审核不通过，管理员禁止播放
	
	public final int value;
	
	VideoStatusEnum(int value){
		this.value = value;
	}
	
	public int getValue(){
		return value;
	}

}
