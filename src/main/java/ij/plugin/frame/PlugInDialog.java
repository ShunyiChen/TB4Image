package ij.plugin.frame;
import java.awt.*;
import java.awt.event.*;

import javax.swing.JDialog;
import javax.swing.JFrame;

import ij.*;
import ij.plugin.*;

/**  This is a non-modal dialog that plugins can extend. */
public class PlugInDialog extends JDialog implements PlugIn, WindowListener, FocusListener {

	String title;
	
	public PlugInDialog(String title) {
		super(IJ.isMacOSX()?IJ.getInstance():IJ.isJava16()?null:new JFrame(),title);
		enableEvents(AWTEvent.WINDOW_EVENT_MASK);
		this.title = title;
		ImageJ ij = IJ.getInstance();
		if (IJ.isMacOSX() && ij!=null) {
			ij.toFront(); // needed for keyboard shortcuts to work
			IJ.wait(250);
		}
		addWindowListener(this);
 		addFocusListener(this);
		if (IJ.isLinux()) setBackground(ImageJ.backgroundColor);
		if (ij!=null && !IJ.isMacOSX() && IJ.isJava16()) {
			Image img = ij.getIconImage();
			if (img!=null)
				try {setIconImage(img);} catch (Exception e) {}
		}
	}
	
	public void run(String arg) {
	}
	
    public void windowClosing(WindowEvent e) {
    	if (e.getSource()==this) {
    		close();
    		if (Recorder.record)
    			Recorder.record("run", "Close");
    	}
    }
    
    /** Closes this window. */
    public void close() {
		//setVisible(false);
		dispose();
		WindowManager.removeWindow(this);
    }

	public void windowActivated(WindowEvent e) {
		ImageJ ij = IJ.getInstance();
		//if (IJ.isMacOSX() && ij!=null && !ij.isActive() && !(this instanceof ThresholdAdjuster))
		//	ij.toFront();
		WindowManager.setWindow(this);
	}

	public void focusGained(FocusEvent e) {
		WindowManager.setWindow(this);
	}

    public void windowOpened(WindowEvent e) {}
    public void windowClosed(WindowEvent e) {}
    public void windowIconified(WindowEvent e) {}
    public void windowDeiconified(WindowEvent e) {}
    public void windowDeactivated(WindowEvent e) {}
	public void focusLost(FocusEvent e) {}
}
