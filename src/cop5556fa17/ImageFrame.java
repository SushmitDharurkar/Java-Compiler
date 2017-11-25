package cop5556fa17;

import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import static javax.swing.JFrame.EXIT_ON_CLOSE;

public class ImageFrame {
	
	public static String desc = "Lcop5556fa17/ImageFrame;";
	public static String JFrameDesc = "Ljavax/.swing/JFrame;";
	public static String className = "cop5556fa17/ImageFrame";
	
//	public JFrame frame;  //FIX THIS
//	public JLabel label;
//	BufferedImage image;
//	
//	public ImageFrame(BufferedImage image){
//	    frame = new JFrame();
//	    frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
////	    frame.setSize(image.getWidth() , image.getHeight());
////	    System.out.println("*******ImageFrame:  w=" + image.getWidth() + ", h="+image.getHeight());
//	    label = new JLabel(new ImageIcon(image));
//	    frame.add(label);
//	    frame.pack();
//        SwingUtilities.invokeLater(new Runnable() {
//            public void run() {
//            	frame.setVisible(true);		
//            }
//        });
//	    
//	}
//	
//	public static final String makeFrameSig = "(" +ImageSupport.ImageDesc + ")" + desc;
//	public static ImageFrame makeFrame(BufferedImage image) {
//		ImageFrame frame = new ImageFrame(image);
//		return frame;
//	}
	
	public static final JFrame makeFrame(BufferedImage image) throws InvocationTargetException, InterruptedException {
		JFrame frame = new JFrame();
	    frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
    frame.setSize(image.getWidth() , image.getHeight());
    JLabel label = new JLabel(new ImageIcon(image));
    frame.add(label);
    frame.pack();
    SwingUtilities.invokeAndWait(new Runnable() {
        public void run() {
        	frame.setVisible(true);		
        }
    });
    return frame;
	}
	

}