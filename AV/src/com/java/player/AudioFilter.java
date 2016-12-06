package com.java.player;

import java.io.File;
import javax.swing.filechooser.*;

import com.java.common.Utils;

public class AudioFilter extends FileFilter {
	
	public String getDescription() {
		return "Select Sound file (*.WAV)";
	}

	public boolean accept(File file) {
		if (!file.isDirectory()) {
			String extension = Utils.getExtension(file);
			return extension.equalsIgnoreCase("wav");
		} else {
			 return true;
		}
	}

}
