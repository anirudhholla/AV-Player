package com.java.player;

import java.io.File;
import javax.swing.filechooser.*;

import com.java.common.Utils;

public class VideoFilter extends FileFilter {
	
	public String getDescription() {
		return "Select Video (*.RGB)";
	}

	public boolean accept(File file) {
		if (file.isDirectory()) {
			return true;
		} 
		
		String extension = Utils.getExtension(file);
	    if (extension != null) {
	        if (extension.equalsIgnoreCase("rgb")) {
	            return true;
	        } else {
	            return false;
	        }
	    }
	    return false;		
	}
}