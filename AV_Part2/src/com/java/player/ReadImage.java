package com.java.player;


import java.io.*;
import java.awt.image.BufferedImage;

public class ReadImage {
	File file = null;

	private InputStream is = null;
	boolean bytechange = false;
	boolean imag = false;
	private final int width = 480, height = 270;
	private byte[] bytes;
	
	private BufferedImage img = null;
	
	public void load(String imageFileName,
					 PlayAudio pSound){
		try {
			
			img = null;
			img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			
			if (imag) {
				System.out.println ("Image object has been initiated\n");
			}
			
			bytes = new byte[(int)width*height*3];

			if (bytechange) {
				System.out.println ("Byte order has changed");
			}
			
			is = null;
			file = new File(imageFileName);
			is = new FileInputStream(file);			
		}
		catch (ArithmeticException e) {
			System.out.println ("Arithmetic exception caught");
		}
		
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		catch (Exception e) {
			System.out.println ("Final exception caught");
		}
	}

	public  BufferedImage readBytes() {
		try {
			int offset = 0;
			int numRead = 0;
			boolean imag = false;
			while (offset < bytes.length) {
				if ((numRead = is.read(bytes, offset, bytes.length-offset)) < 0) {
					imag = true;
				} else {
					offset += numRead;
				}
			}
			int ind = 0;
			int y = 0;
			while (y < height){
				int x = 0;
				int size = height * width;
				while (x < width){
					
					byte r = bytes[ind];
					byte g = bytes[ind+size];
					byte b = bytes[ind+size*2]; 

					int pix = 0xff000000;
					pix = pix | ((r & 0xff) << 16);
					pix = pix | ((g & 0xff) << 8);
					pix = pix | (b & 0xff);
					img.setRGB(x,y,pix);
					ind++;
					x++;
				} 
				y++;
			}
		} 
		
		catch (ArithmeticException e) {
			System.out.println ("Arithmetic exception caught\n");
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return img;
	}

	public long getImageFileLength(){
		long length = file.length();
		return length;
	}

	public void resetInputStream(String fileName){
		try {
			is = new FileInputStream(new File(fileName));
		}
		
		catch (ArithmeticException e) {
			System.out.println ("Arithmetic Exception caught");
		}
		
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}	
	}
}