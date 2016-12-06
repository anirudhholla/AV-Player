package com.java.player;

import java.util.*;
import java.text.*;
import javax.sound.sampled.Clip;
import javax.swing.*;


public class PlayingTimer extends Thread {
	private JLabel labelRecordTime = null;
	private JSlider slider = null;
	private Clip audioClip = null;
	private int sleep_time = 100;
	
	private boolean isRunning = false, isPause = false, isReset = false;
	private long begin_counting = 0, stop_counting = 0;
	private DateFormat dateFormater = new SimpleDateFormat("HH:mm:ss");	

	public void run() {
		isRunning = true;
		
		begin_counting = System.currentTimeMillis();
		
		for (;isRunning;) {
			try {
				Thread.sleep(sleep_time);
				if (isPause) {
					stop_counting += sleep_time;
					
				} else {
					if (audioClip != null) {
						if (audioClip.isRunning()) {
							labelRecordTime.setText(toTimeString());
							int currentSecond = (int) audioClip.getMicrosecondPosition() / 1000000; 
							slider.setValue(currentSecond);
						}
					} else {
						continue;
					}
				}
			} 
			
			catch (ArithmeticException e) {
				System.out.println ("Error: Arithmetic number exception");
			}
			
			catch (InterruptedException ex) {
				if (!isReset) {
					continue;
				} else {
					isRunning = false;
					slider.setValue(0);
					labelRecordTime.setText("00:00:00");
					break;
				}
			}
			
			catch(Exception e){
				System.out.println ("Error in the thread playing timer");
			}
		}
	}
	
	void resumeTimer() {
		isPause = false;
	}

	PlayingTimer(JLabel labelRecordTime, JSlider slider) {
		this.labelRecordTime = labelRecordTime;
		this.slider = slider;
	}
	
	void pauseTimer() {
		isPause = true;
	}
	
	public void setAudioClip(Clip audioClip) {
		this.audioClip = audioClip;
	}
	
	void reset() {
		isReset = true;
		isRunning = false;
	}
	
	private String toTimeString() {
		long now = System.currentTimeMillis();
		long date_var = now - begin_counting - stop_counting;
		Date current = new Date(date_var);
		dateFormater.setTimeZone(TimeZone.getTimeZone("GMT"));
		String timeCounter = dateFormater.format(current);
		return timeCounter;
	}
}