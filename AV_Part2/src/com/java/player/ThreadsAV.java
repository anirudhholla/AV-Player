package com.java.player;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.RandomAccessFile;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import java.io.IOException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class ThreadsAV {
	
		
	 public static void main(String[] args) {
	        //even numbers
		 	int start = 2;
		 	int end = 80;
		 
	        Thread t1 = new Thread() {
	            public void run() {
	            	try{
	        			//String imageFilePath = "C:/Users/Holla/Documents/dataset/Videos/data_test1.rgb";
	        			
	        			File file_vid = new File("C:/Users/Holla/Documents/dataset/Videos/data_test1.rgb");
	        			File file_aud = new File("C:/Users/Holla/Documents/dataset/Videos/data_test1.wav");
	        			
	        			RandomAccessFile is = new RandomAccessFile(file_vid,"r");
	        			InputStream inputStream = new FileInputStream(file_vid);

	        			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file_aud);
	        		    AudioFormat format = audioInputStream.getFormat();
	        		    long audioFileLength = file_aud.length();
	        		    int frameSize = format.getFrameSize();
	        		    float frameRate = format.getFrameRate();
	        		    float durationInSeconds = (audioFileLength / (frameSize * frameRate));
	        		    System.out.println("Audio Length"+ durationInSeconds + "Hello");
	        		    
	        			long byteLength = (long) is.length();
	        			FileOutputStream of = new FileOutputStream("C:/Users/Holla/Documents/ani/x.rgb");
	        			System.out.println("File Length" +(int)file_vid.length());
	        			long vid_length = file_vid.length();
	        			long num_frames = vid_length / (480*270*3);
	        			
	        			long byte_length = vid_length * 30 / num_frames;
	        			int byte_int = (int) byte_length;
	        			byte[] bytes = new byte[byte_int];
	        			
	        			long offset = 0;
	        			long numRead = 0;
	        			int count = 0;
	        			
	        			while(offset < byteLength && (numRead = is.read(bytes,0,bytes.length))>0){
	        				count++;
	        				offset += numRead;
	        				is.seek(offset);
	        				System.out.println(offset);
	        				if(count > start && count <= end){
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
	        		} catch (UnsupportedAudioFileException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	            }
	        };
	        t1.start();
	        Thread t2 = new Thread() {
	            public void run() {
	            	try{
	        			//String imageFilePath = "C:/Users/Holla/Documents/dataset/Videos/data_test1.rgb";
	        			
	        			File file_aud = new File("C:/Users/Holla/Documents/dataset/Videos/data_test1.wav");
	        			RandomAccessFile is = new RandomAccessFile(file_aud,"r");
	        			
	        			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file_aud);
	        		    AudioFormat format = audioInputStream.getFormat();
	        		    long audioFileLength = file_aud.length();
	        		    int frameSize = format.getFrameSize();
	        		    float frameRate = format.getFrameRate();
	        		    float durationInSeconds = (audioFileLength / (frameSize * frameRate));
	        		    System.out.println("Audio Length"+ durationInSeconds + "Hello");
	        		    long byteLength = (long) is.length();
	        			
	        			FileOutputStream of = new FileOutputStream("C:/Users/Holla/Documents/ani/x.wav");
	        			byte[] bytes = new byte[(int) file_aud.length() / (int)durationInSeconds];
	        			
	        			long offset = 0;
	        			long numRead = 0;
	        			int count = 0;
	        			
	        			while(offset < byteLength && (numRead = is.read(bytes,0,bytes.length))>0){
	        				count++;
	        				offset += numRead;
	        				is.seek(offset);
	        				System.out.println(offset);
	        				if(count > start && count <= end){
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
	        		} catch (UnsupportedAudioFileException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	            }
	            
	        };
	        t2.start();

	    }
	 }
