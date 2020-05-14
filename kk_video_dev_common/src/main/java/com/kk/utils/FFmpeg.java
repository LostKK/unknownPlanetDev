package com.kk.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class FFmpeg {
	
	private String ffmpegModel;
	
	public FFmpeg(String ffmpegModel){
		super();
		this.ffmpegModel = ffmpegModel;
	}
	
	public void convertor(String videoInputPath,String videoOutputPath)throws Exception{ //转换的方法
		
		//ffmpeg -i input.mp4 output.avi     命令行格式
		List<String> command = new ArrayList<>();
		command.add(ffmpegModel);
		command.add("-i");
		command.add(videoInputPath);
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
		FFmpeg ffmpeg = new FFmpeg("D:/my ware/ffmpeg/bin/ffmpeg.exe");
		try {
			ffmpeg.convertor("D:/my ware/ffmpeg/bin/jumping.mp4", "D:/my ware/ffmpeg/bin/jumping2.mp4");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
