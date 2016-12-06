package com.java.player;


import java.io.*;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.*;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.sound.sampled.LineUnavailableException;

public class PlayAudio implements LineListener {
	private static final int SECONDS_IN_HOUR = 60 * 60, SECONDS_IN_MINUTE = 60;
	private boolean finish_play, paused_flag, replicated_flag = true, stopped_flag;
	private AudioFormat format = null;
	private Clip audio_clip;
	
	public String getAudioLength() {
		String len = "";
		long h = 0, m = 0, s = audio_clip.getMicrosecondLength() / 1000000;

		if (s < SECONDS_IN_HOUR) {
			len += "00:";
		} else {
			h = s / SECONDS_IN_HOUR;
			len = String.format("%02d:", h);
		}

		m = s - h * SECONDS_IN_HOUR;
		if (m < SECONDS_IN_MINUTE) {
			m = 0;
			len += "00:";
		} else {
			m = m / SECONDS_IN_MINUTE;
			len += String.format("%02d:", m);
		}

		long second = s - h * SECONDS_IN_HOUR - m * SECONDS_IN_MINUTE;

		len += String.format("%02d", second);

		return len;
	}
	
	public void audioLoad(String audio_file_path) throws LineUnavailableException,
												  UnsupportedAudioFileException,
	                                              IOException  {

		format = null;
		audio_clip = null;
		File new_file = new File(audio_file_path);
		
		AudioInputStream audio_stream = AudioSystem.getAudioInputStream(new_file);
		format = audio_stream.getFormat();

		DataLine.Info info = new DataLine.Info(Clip.class, format);

		audio_clip = (Clip) AudioSystem.getLine(info);
		audio_clip.addLineListener(this);
		audio_clip.open(audio_stream);
	}

	public long getAudioLengthInSeconds() {
		return audio_clip.getMicrosecondLength() / 1000000;
	}
	
	void audioPlay() throws IOException {
		audio_clip.start();
		finish_play = false;
		stopped_flag = false;

		while (!finish_play) {
			try {
				Thread.sleep(1000);
			} 
			
			catch (ArithmeticException ex) {
				System.out.println("Sleep stopped unexpectedly");
			}
			
			catch (InterruptedException ex) {
				if (stopped_flag) {
					audio_clip.stop();
					break;
				}
				
				else if (paused_flag) {
					audio_clip.stop();
				}
				
				else {
					audio_clip.start();
				}
			}
		}

		audio_clip.close();

	}

	public void stopAudio() {
		stopped_flag = true;
	}
	
	public Clip getAudio() {
		return audio_clip;
	}
	
	public void pauseAudio() {
		paused_flag = true;
	}
	
	public void resumeAudio() {
		paused_flag = false;
	}
	
	public float getFrameRate() {
		return format.getFrameRate();
	}
	
	public void replicate() {
		replicated_flag = true;
	}
	
	public long getFramePosition() {
		return audio_clip.getLongFramePosition();
	}

	public void update(LineEvent event) {
		LineEvent.Type type = event.getType();
		
		if (type != LineEvent.Type.STOP) {
			replicated_flag = false;
		}
		else {
			if (stopped_flag) {
				if (!paused_flag) {
					finish_play = true;
				}
			}
		}
	}
}