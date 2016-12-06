package com.java.player;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

public class ClipVideo {

	public static void modifyVideo(int start,int end) throws IOException, UnsupportedAudioFileException{
		try{
			//String imageFilePath = "C:/Users/Holla/Documents/dataset/Videos/data_test1.rgb";
			int sampleRate = 30;
			int totalSamples = 9000;
			
			File file = new File("C:/Users/Holla/Documents/dataset/Ads/Starbucks_Ad_15s.rgb");
			File file_aud = new File("C:/Users/Holla/Documents/dataset/Ads/Starbucks_Ad_15s.wav");
			
			RandomAccessFile is = new RandomAccessFile(file,"r");
			InputStream inputStream = new FileInputStream(file);

			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file_aud);
		    AudioFormat format = audioInputStream.getFormat();
		    long audioFileLength = file_aud.length();
		    int frameSize = format.getFrameSize();
		    float frameRate = format.getFrameRate();
		    float durationInSeconds = (audioFileLength / (frameSize * frameRate));
		    System.out.println("Audio Length"+ durationInSeconds + "Hello");
		    
			long byteLength = (long) is.length();
			FileOutputStream of = new FileOutputStream("C:/Users/Holla/Documents/ani/x.rgb");
			byte[] bytes = new byte[(int) file.length() / (int)durationInSeconds];
			
			long offset = 0;
			long numRead = 0;
			int count = 0;
			
			while(offset < byteLength && (numRead = is.read(bytes,0,bytes.length))>0){
				count++;
				offset += numRead;
				is.seek(offset);
				System.out.println(offset);
				if(count > start && count <=end){
					System.out.println ("Unwanted");
				}
				else{
					of.write(bytes);
				}	
			}
			
			System.out.println(count);
			is.close();
			of.close();
		}
		catch(FileNotFoundException e){
			e.printStackTrace();
		}
	}
	
	public static void main(String args[]) throws IOException, UnsupportedAudioFileException{
		modifyVideo(4,12);
		
		
	}
}
