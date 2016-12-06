package com.java.player;

import java.awt.List;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class Entropy {
	  public static double getShannonEntropy_Image(BufferedImage actualImage){
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

	  public static void main(String[] args) throws IOException{
		  BufferedImage img = null;
		  double x = 0.0;
		  img = ImageIO.read(Entropy.class.getResource("../icons/starbuckslogo.png"));
		  x = getShannonEntropy_Image(img);
		  System.out.println(x);
	  }
}
