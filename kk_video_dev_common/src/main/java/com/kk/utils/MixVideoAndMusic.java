package com.kk.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MixVideoAndMusic {

	private String ffmpegModel;

	public MixVideoAndMusic(String ffmpegModel){
		super();
		this.ffmpegModel = ffmpegModel;
	}

	public void convertor(String musicInputPath, String videoInputPath,double time,String videoOutputPath)throws Exception{ //转换的方法
		
		//ffmpeg.exe -i love.mp3 -i beauty.mp4 -t 7 -y 新的视频2.mp4
        //命令行格式
		List<String> command = new ArrayList<>();
		
		command.add(ffmpegModel);
		
		command.add("-i");
		command.add(musicInputPath);
		
		command.add("-i");
		command.add(videoInputPath);
		
		command.add("-t");
		command.add(String.valueOf(time));
		
		command.add("-y");
		command.add(videoOutputPath);          //拼接命令
		
		ProcessBuilder builder = new ProcessBuilder(command);
		Process process = builder.start();
		
		InputStream errorStream = process.getErrorStream();           //将转换过程中产生的碎片文件释放
		InputStreamReader inputStreamReader = new InputStreamReader(errorStream);
		BufferedReader br = new BufferedReader(inputStreamReader);
		
		String line = "";
		while((line = br.readLine()) != null){
		}
		
		if(br != null){
			br.close();
		}
		if(inputStreamReader != null){
			inputStreamReader.close();
		}
		if(errorStream != null){
			errorStream.close();
		}
		
	}

	public static void main(String[] args) {
		MixVideoAndMusic ffmpeg = new MixVideoAndMusic("D:/my ware/ffmpeg/bin/ffmpeg.exe");
		try {
			ffmpeg.convertor("D:/my ware/ffmpeg/bin/love.mp3","D:/my ware/ffmpeg/bin/beauty.mp4",8, "D:/my ware/ffmpeg/bin/java.mp4");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
