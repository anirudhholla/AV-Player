package com.java.player;

import java.util.*;
import java.text.*;
import javax.sound.sampled.Clip;
import javax.swing.*;


public class VideoTimer extends Thread {
	private JLabel time_recorder = null;
	private JSlider slider = null;
	private Clip audio_snippet = null;
	private int sleep_time = 100;
	
	private boolean running_state = false, pause_state = false, reset_state = false;
	private long begin_counting = 0, stop_counting = 0;
	private DateFormat format_date = new SimpleDateFormat("HH:mm:ss");	

	public void run() {
		running_state = true;
		
		begin_counting = System.currentTimeMillis();
		
		for (;running_state;) {
			try {
				Thread.sleep(sleep_time);
				if (pause_state) {
					stop_counting += sleep_time;
					
				} else {
					if (audio_snippet != null) {
						if (audio_snippet.isRunning()) {
							time_recorder.setText(toTimeString());
							int currentSecond = (int) audio_snippet.getMicrosecondPosition() / 1000000; 
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
				if (!reset_state) {
					continue;
				} else {
					running_state = false;
					slider.setValue(0);
					time_recorder.setText("00:00:00");
					break;
				}
			}
			
			catch(Exception e){
				System.out.println ("Error in the thread playing timer");
			}
		}
	}
	
	void videoResume() {
		pause_state = false;
	}

	VideoTimer(JLabel labelRecordTime, JSlider slider) {
		this.time_recorder = labelRecordTime;
		this.slider = slider;
	}
	
	void videoPause() {
		pause_state = true;
	}
	
	public void setAudioSnippet(Clip audioClip) {
		this.audio_snippet = audioClip;
	}
	
	void videoReset() {
		reset_state = true;
		running_state = false;
	}
	
	private String toTimeString() {
		long now = System.currentTimeMillis();
		long date_variable = now - begin_counting - stop_counting;
		Date current_date = new Date(date_variable);
		format_date.setTimeZone(TimeZone.getTimeZone("GMT"));
		String time_counter = format_date.format(current_date);
		return time_counter;
	}
}