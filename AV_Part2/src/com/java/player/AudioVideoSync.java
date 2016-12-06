package com.java.player;

import java.awt.*;
import java.awt.event.*;
import org.apache.commons.lang3.StringUtils;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;

public class AudioVideoSync extends JFrame implements ActionListener{

	private final int width = 480,height = 270;
	private final double frames_per_second = 30; 
	private BufferedImage img;
	private static final long serialVersionUID = 1L;	
	private Thread video_thread,audio_thread;
	private VideoTimer timer;
	private boolean play_state = false,pause_state = false;
	private String video_file,video_last_path,audio_file,audio_last_path;
	
	private JLabel video_timer,video_duration;
	private JButton open_btn_video,open_btn_audio,play_btn,pause_btn;
	private JSlider time_slider = new JSlider();
	private ImageIcon open_icon,play_icon,stop_icon,pause_icon;
	
	ReadImageInternals component = new ReadImageInternals();
	PlayAudio audio_player = new PlayAudio();
	ReadImage image_reader = new ReadImage();

	public AudioVideoSync() {		
		super("Audio Video Player");
		setLayout(new GridBagLayout());
		open_icon = new ImageIcon(getClass().getResource("../icons/open.png"));
		play_icon = new ImageIcon(getClass().getResource("../icons/Play.png"));
		stop_icon = new ImageIcon(getClass().getResource("../icons/Stop.png"));
		pause_icon = new ImageIcon(getClass().getResource("../icons/Pause.png"));
		
		video_timer = new JLabel("00:00:00");
		video_duration = new JLabel("00:00:00");
		video_timer.setFont(new Font("Arial", Font.BOLD, 12));
		video_duration.setFont(new Font("Arial", Font.BOLD, 12));
		
		open_btn_video = new JButton("Upload Video");
		open_btn_video.setFont(new Font("Arial", Font.BOLD, 12));
		open_btn_audio = new JButton("Upload Audio");
		open_btn_audio.setFont(new Font("Arial", Font.BOLD, 12));
		play_btn = new JButton("Play");
		play_btn.setFont(new Font("Arial", Font.BOLD, 12));
		pause_btn = new JButton("Pause");
		pause_btn.setFont(new Font("Arial", Font.BOLD, 12));
		GridBagConstraints grid_constraints = new GridBagConstraints();
		
		grid_constraints.insets = new Insets(5, 5, 5, 5);
		
		open_btn_video.setIcon(open_icon);
		open_btn_audio.setIcon(open_icon);
		play_btn.setIcon(play_icon);
		pause_btn.setIcon(pause_icon);
		
		grid_constraints.anchor = GridBagConstraints.CENTER;
		
		open_btn_video.setActionCommand("OpenVideo");
		open_btn_audio.setActionCommand("OpenAudio");
		play_btn.setActionCommand("PlayButton");
		pause_btn.setActionCommand("PauseButton");
		
		play_btn.setEnabled(false);
		pause_btn.setEnabled(false);
		time_slider.setEnabled(false);
		time_slider.setPaintTicks(true);
		time_slider.setPaintLabels(true);
		time_slider.setPreferredSize(new Dimension(500, 30));
		time_slider.setValue(0);
		
		grid_constraints.gridx = 0;
		grid_constraints.gridy = 3;
		JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.CENTER));
		panelButtons.add(open_btn_video);
		grid_constraints.gridwidth = 3;
		panelButtons.add(open_btn_audio);
		add(panelButtons, grid_constraints);
		
		grid_constraints.gridx = 1;
		grid_constraints.gridy = 12;
		grid_constraints.gridheight = 3;
		component.setPreferredSize(new Dimension(250, 320));
		grid_constraints.gridwidth = 3;
		grid_constraints.fill = GridBagConstraints.BOTH;
		grid_constraints.insets = new Insets(5, 5, 5, 5);
		add(component, grid_constraints);
		
		grid_constraints.gridy = 18;
		grid_constraints.gridwidth = 1;
		grid_constraints.anchor = GridBagConstraints.CENTER;;
		add(video_timer, grid_constraints);
		
		grid_constraints.gridx = 2;
		setResizable(true);
		add(time_slider, grid_constraints);
		
		grid_constraints.gridx = 5;
		JPanel panelButtons1 = new JPanel(new FlowLayout(FlowLayout.CENTER));
		add(video_duration, grid_constraints);
		
		grid_constraints.gridx = 0;
		grid_constraints.gridy = 15;
		panelButtons1.add(play_btn);
		grid_constraints.gridwidth = 3;
		panelButtons1.add(pause_btn);
		add(panelButtons1, grid_constraints);
		
		pause_btn.addActionListener(this);
		open_btn_audio.addActionListener(this);
		pack();
		open_btn_video.addActionListener(this);
		play_btn.addActionListener(this);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}
	
	public void actionPerformed(ActionEvent event) {
		String action = event.getActionCommand();
		if (action.equalsIgnoreCase("PlayButton")) {
			if (play_state) {
				stopVideo();
			} else {
				playAudioVideo();
			}
			} else if (action.equalsIgnoreCase("OpenAudio")) {
				openAudio();
			} else if (action.equalsIgnoreCase("OpenVideo")) {
				openVideo();
			} else if (action.equalsIgnoreCase("PauseButton")) {
				if (pause_state) {
					resumeVideo();
				} else {
					pauseVideo();
				}
			}
		
	}
	
	private void openVideo() {
		JFileChooser file_selector = null;
		
		if (video_last_path == null || video_last_path.equals("")) {
			file_selector = new JFileChooser();
		} else {
			file_selector = new JFileChooser(video_last_path);
		}

		file_selector.addChoosableFileFilter(new VideoFilter());
		file_selector.setDialogTitle("Open Video File");
		file_selector.setAcceptAllFileFilterUsed(false);

		int user_selected_val = file_selector.showOpenDialog(this);
		
		if (user_selected_val != JFileChooser.APPROVE_OPTION) {
			System.out.println("UserChoice-NotValid");
		}
		else{
			video_file = file_selector.getSelectedFile().getAbsolutePath();
			video_last_path = file_selector.getSelectedFile().getParent();
			
			if (play_state) {
				stopVideo();
			}
			else if(pause_state){
				stopVideo();
			}
		}
		
	}
	
		  public double getShannonEntropy_Image(BufferedImage actualImage){
			  ArrayList<String> values= new ArrayList<String>();
			              int n = 0;
			              Map<Integer, Integer> occ = new HashMap<>();
			              for(int i=0;i<actualImage.getHeight();i++){
			                  for(int j=0;j<actualImage.getWidth();j++){
			                    int pixel = actualImage.getRGB(j, i);
			                    int alpha = (pixel >> 24) & 0xff;
			                    int red = (pixel >> 16) & 0xff;
			                    int green = (pixel >> 8) & 0xff;
			                    int blue = (pixel) & 0xff;
			  //0.2989 * R + 0.5870 * G + 0.1140 * B greyscale conversion
			 //System.out.println("i="+i+" j="+j+" argb: " + alpha + ", " + red + ", " + green + ", " + blue);
			                    int d= (int)Math.round(0.2989 * red + 0.5870 * green + 0.1140 * blue);
			                   if(!values.contains(String.valueOf(d)))
			                       values.add(String.valueOf(d));
			                   if (occ.containsKey(d)) {
			                       occ.put(d, occ.get(d) + 1);
			                   } else {
			                       occ.put(d, 1);
			                   }
			                   ++n;
			            }
			         }
			         double e = 0.0;
			         for (Map.Entry<Integer, Integer> entry : occ.entrySet()) {
			              int cx = entry.getKey();
			              double p = (double) entry.getValue() / (480 * 270);
			              e += p * (Math.log(p));
			         }
			         
			  return -e;
			 }

	private void openAudio() {
		JFileChooser file_selector = null;
		
		if(!(StringUtils.isBlank(audio_last_path))) {
			file_selector = new JFileChooser(audio_last_path);
		}
		
		else if (!(StringUtils.isBlank(video_last_path))){
			file_selector = new JFileChooser(video_last_path);
		}
		
		else {
			file_selector = new JFileChooser();
		}
		
		file_selector.addChoosableFileFilter(new AudioFilter());
		file_selector.setDialogTitle("Open Audio File");
		file_selector.setAcceptAllFileFilterUsed(false);

		int user_selected_val = file_selector.showOpenDialog(this);
		int thread_val = 100;
		if (user_selected_val == JFileChooser.APPROVE_OPTION) {
			audio_file = file_selector.getSelectedFile().getAbsolutePath();
			audio_last_path = file_selector.getSelectedFile().getParent();
			if (play_state) {
				stopVideo();
				for(;audio_player.getAudio().isRunning();){
					try {
						Thread.sleep(thread_val);
					}
					catch (ArithmeticException e) {
						System.out.println ("Error: Arithmetic number exception");
					}
					catch (InterruptedException ex) {
						ex.printStackTrace();
					}
					catch(Exception e){
						System.out.println ("Error in the thread playing timer");
					}
				}
			}
			else if (pause_state) {
				stopVideo();
				for(;audio_player.getAudio().isRunning();){
					try {
						Thread.sleep(thread_val);
					}
					catch (ArithmeticException e) {
						System.out.println ("Error: Arithmetic number exception");
					}
					catch (InterruptedException ex) {
						ex.printStackTrace();
					}
					catch(Exception e){
						System.out.println ("Error in the thread playing timer");
					}
				}
			}
					
			if(StringUtils.isNotBlank(video_last_path)){
				if(StringUtils.isNotBlank(audio_last_path)){
					play_btn.setEnabled(true);
				}
			}
			else{
				play_btn.setEnabled(false);
			}
		}
	}
	
	private void pauseVideo() {
		pause_state = true;
		audio_player.pauseAudio();
		timer.videoPause();
		pause_btn.setText("Resume");
		audio_thread.interrupt();
		video_thread.interrupt();
	}
	
	private void resumeVideo() {
		pause_state = false;
		audio_player.resumeAudio();
		timer.videoResume();
		pause_btn.setText("Pause");
		audio_thread.interrupt();		
		video_thread.interrupt();		
	}
	
	private void resetVideo() {
		play_state = false;
		timer.videoReset();
		timer.interrupt();
		play_btn.setText("Play");
		play_btn.setIcon(play_icon);
		pause_btn.setEnabled(false);
	}
	
	private void stopVideo() {
		pause_state = false;
		timer.videoReset();
		timer.interrupt();	
		audio_player.stopAudio();
		image_reader.resetInputStream(video_file);
		pause_btn.setText("Pause");
		pause_btn.setEnabled(false);
		audio_thread.interrupt();	
		video_thread.interrupt();		
	}

	private void playAudioVideo() {
		timer = new VideoTimer(video_timer, time_slider);
		timer.start();
		
		try {
			play_state = true;
			audio_player.audioLoad(audio_file);		
		}
		catch (ArithmeticException e) {
			System.out.println ("Error: Arithmetic number exception");
		}
		catch (IOException e1) {
			JOptionPane.showMessageDialog(AudioVideoSync.this,  
					"Input/Output Error! Audio cannot be played", "Error", JOptionPane.ERROR_MESSAGE);
			resetVideo();
		}
		catch(Exception e){
			System.out.println ("Error in the thread playing timer");
		}
		
		runAudio(2); 
		image_reader.load(video_file, audio_player);
		boolean isRunning=true;
		runVideo(isRunning);
		syncStart();
	}
	
	public void syncStart() {
		audio_thread.start();
		video_thread.start();
	}
	
	public void runVideo(boolean isRunning) {
		video_thread = new Thread(new Runnable() {
			public void run() {						
				long length = width*height*3;
				double spf = audio_player.getFrameRate()/frames_per_second;
				
				long no_of_frames = image_reader.getImageFileLength()/length;
				
				/*Entropy
				
				PrintWriter printWriter = null;
				File file = new File ("test1.txt");
			    
			   
				try {
					printWriter = new PrintWriter ("test1.txt");
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			    */
				int j;
				for(j=0;j < Math.round(audio_player.getFramePosition()/spf);) {
					img = image_reader.readBytes();
					component.setImg(img);
					repaint();
					j++;
				}
				
				for( ;j > Math.round(audio_player.getFramePosition()/spf); ) {
				}
				
				int i = j;
				double x = 0.0;
				double previous = 0.0;
				while( i < no_of_frames ) {

					for(;i > Math.round(audio_player.getFramePosition()/spf);) {
						// Do Nothing
					}

					for(;i < Math.round(audio_player.getFramePosition()/spf);) {
						i++;
						img = image_reader.readBytes();
						/*Entropy
						previous = x;
						x = getShannonEntropy_Image(img);
						printWriter.println (i + "--->" +Double.toString(x - previous));
						*/
						component.setImg(img);
						repaint();
					}

					img = image_reader.readBytes();
					component.setImg(img);
					repaint();			
					
					System.gc();
					i++;
				}
			}
		});
	}
	
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
	
	public void runAudio(int flag) {
		audio_thread = new Thread(new Runnable() {
			public void run() {
				try {					
					
					video_duration.setText(audio_player.getAudioLength());
					pause_btn.setText("Pause");
					play_btn.setText("Stop");
					play_btn.setIcon(stop_icon);
					timer.setAudioSnippet(audio_player.getAudio());
					
					play_btn.setEnabled(true);
					pause_btn.setEnabled(true);
					
					time_slider.setMaximum((int) audio_player.getAudioLengthInSeconds());
					audio_player.audioPlay();
					resetVideo();
				}
				catch (ArithmeticException e) {
					System.out.println ("Error: Arithmetic number exception");
				}
				catch (IOException e1) {
					JOptionPane.showMessageDialog(AudioVideoSync.this,  
							"Input/Output Error! Could not play audio file", "Error", JOptionPane.ERROR_MESSAGE);
					resetVideo();
				}
				catch(Exception e){
					System.out.println ("Error in the thread playing timer");
				}
			}
		});
		
	}
	
	public static void invokeSwing(){
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new AudioVideoSync().setVisible(true);
			}
		});
	}
	//Driver for loading the player
	public static void main(String[] args) {
		try {
			//UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			modifyVideo(4,12);
		}
		
		catch (ArithmeticException e) {
			System.out.println ("Error: Arithmetic number exception");
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		//invokeSwing(); 
	}

}