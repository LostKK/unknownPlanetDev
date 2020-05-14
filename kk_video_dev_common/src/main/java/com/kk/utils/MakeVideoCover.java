package com.kk.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MakeVideoCover {
	private String ffmpegModel;

	public MakeVideoCover(String ffmpegModel){
		super();
		this.ffmpegModel = ffmpegModel;
	}

	public void convertor(String coverInputPath,String coverOutputPath)throws Exception{ //转换的方法
		
		//ffmpeg.exe -ss 00:00:01 -y -i jumping.mp4 -vframes 1 kk.jpg
        //命令行格式
		List<String> command = new ArrayList<>();
		
		command.add(ffmpegModel);
		
		command.add("-ss");
		command.add("00:00:01");
		
		command.add("-y");
		command.add("-i");
		command.add(coverInputPath);
		
		command.add("-vframes");
		command.add("1");
		
		command.add(coverOutputPath);          //拼接命令
		
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
		MakeVideoCover ffmpeg = new MakeVideoCover("D:/my ware/ffmpeg/bin/ffmpeg.exe");
		try {
			ffmpeg.convertor("D:/my ware/ffmpeg/bin/楚原.mp4", "D:/my ware/ffmpeg/bin/楚原.jpg");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
