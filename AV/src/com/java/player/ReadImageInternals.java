package com.java.player;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

public class ReadImageInternals extends JComponent {

	private static final long serialVersionUID = 1L;
	public void paintComponent(Graphics g) {

		Graphics2D g2 = (Graphics2D) g;
		g2.drawImage(img,0,0,this);
	}

	public void setImg(BufferedImage newimg) {
		this.img = newimg;
	}

	private BufferedImage img;
}