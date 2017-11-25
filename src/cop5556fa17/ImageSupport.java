package cop5556fa17;

import static javax.swing.JFrame.EXIT_ON_CLOSE;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

public class ImageSupport {

	public final static String className = "cop5556fa17/ImageSupport";
	public final static String desc = "Lcop5556fa17/ImageSupport;";
	public static final String StringDesc = "Ljava/lang/String;";
	public final static String stringArrayDesc = "[Ljava/lang/String;";
	public final static String IntegerDesc = "Ljava/lang/Integer;";
	public static final String ImageClassName = "java/awt/image/BufferedImage";
	public static final String ImageDesc = "Ljava/awt/image/BufferedImage;";
	public static String JFrameDesc = "Ljavax/swing/JFrame;";
//	public final static String getRGBSig = "(II)I";


    
	public static final String getURLSig = "([Ljava/lang/String;I)Ljava/net/URL;";
	
	
	public static URL getURL(String[] args, int index) {
		URL url;
		try {
			url = new URL(args[index]);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
		return url;
	}

	public static final String readFromFileSig = "(" + StringDesc + ")" + ImageDesc;
	static BufferedImage readFromFile(String filename) {
		File f = new File(filename);
		BufferedImage bi;
		try {
			bi = ImageIO.read(f);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return bi;
	}
	
	public static final String makeImageSig = "(II)" + ImageDesc;
	public static BufferedImage makeImage(int maxX, int maxY) {
		return new BufferedImage(maxX, maxY, BufferedImage.TYPE_INT_ARGB);
	}
	
	
	public static BufferedImage resize(BufferedImage before, int maxX, int maxY ) {
		int w = before.getWidth();
		int h = before.getHeight();	
		AffineTransform at = new AffineTransform();
		at.scale(((float)maxX)/w, ((float)maxY)/h);
		AffineTransformOp scaleOp = 
		   new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
		BufferedImage after = null;
		after = scaleOp.filter(before, after);		
		return after;		
	}
	
	public static final String readImageSig = "(" + StringDesc + IntegerDesc + IntegerDesc +")" + ImageDesc;
	/**
	 * Reads the image from the indicated URL or filename.  I
	 * If the given source is not a valid URL, it assumes it is a file.
	 * 
	 * If X and Y are not null, the image is resized to this width and height. 
	 * If they are null, the image stays its original size.
	 * 
	 * @param source  String with source or filename on local filesystem.
	 * @param X  Desired width of image, or null 
	 * @param Y  Desired height of image, or null
	 * @return  BufferedImage representing the indicated image.
	 */
	public static BufferedImage readImage(String source, Integer X, Integer Y) {	
		BufferedImage image;
		try {
			URL url = new URL(source);
			image = readFromURL(url);
		}catch(MalformedURLException e) {//wasn't a URL, maybe it is a file
			image = readFromFile(source);			
		}
		if( X != null) {
			return resize(image, X, Y);
		}
		return image;
	}
	
	
	
//	public static BufferedImage readFromFileAndResize(String filename, int X, int Y) {
//		return resize(readFromFile(filename), X, Y);
//	}

	public static final String writeSig = "(" + ImageDesc + StringDesc + ")V";
	/**
	 * Writes the given image to a file on the local system indicated by the given filename.
	 * 
	 * @param image
	 * @param filename
	 */
	public static void write(BufferedImage image, String filename) {
		File f = new File(filename);
		try {
			System.out.println("writing image to file " + filename + "(in File.toString) " + f);
			ImageIO.write(image, "jpeg", f);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public final static String readFromURLSig = "(Ljava/net/URL;)" + ImageDesc;
	/**
	 * Read and returns the image at the given URL
	 * 
	 * Throws RuntimeException if this fails
	 * @param url
	 * @return BufferedImage representing the indicated image
	 */
	static BufferedImage readFromURL(URL url) {
		try {
			System.out.println("reading image from url " + url);
			return ImageIO.read(url);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
//	public static BufferedImage readFromURLAndResize(URL url, int maxX, int maxY) {
//		return resize(readFromURL(url), maxX, maxY);
//	}

	public final static String getScreenWidthSig = "()I";
	/**
	 * Returns the width of the screen.  (Not used in assignment 6)
	 * @return
	 */
	public static int getScreenWidth() {
		return (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	}

	public final static String getScreenHeightSig = "()I";
	/**
	 * Returns the height of the screen.  (Not used in assignment 6)
	 * @return
	 */
	public static int getScreenHeight() {
		return (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	}
	
	public final static String getPixelSig = "(" + ImageDesc + "II)I";
			/**
			 * Returns the indicated pixel with the alpha component masked to 0.
			 * If the given x and y is out of bounds, return 0.
			 * 
			 * @param image   
			 * @param x
			 * @param y
			 * @return
			 */
	public static int getPixel(BufferedImage image, int x, int y) {
		int pixel = (x < 0 || x >=  image.getWidth() || y < 0 || y >= image.getHeight() ) ? 0 : image.getRGB(x, y);
		return pixel & 0x00FFFFFF;
	}
	
	public final static String setPixelSig = "(I"+ ImageDesc + "II)V";
	/**
	 * Inserts the given pixel into the image at the indicated location.
	 * If x or y are out of bounds, do nothing.
	 * 
	 * Before inserting, the alpha component is set to FF.
	 * 
	 * @param rgb
	 * @param image
	 * @param x
	 * @param y
	 */
	public static void setPixel(int rgb, BufferedImage image, int x, int y) {
		if (x < 0 || x >=  image.getWidth() || y < 0 || y >= image.getHeight() ) { System.out.println("pixel out of bounds"); return; }
		image.setRGB(x, y, rgb | 0xFF000000);
	}
	
	public final static String getXSig="("+ImageDesc+")I";
	/**
	 * Returns the width of the given image.
	 * 
	 * @param image
	 * @return
	 */
	public static int getX(BufferedImage image) {
		return image.getWidth();
	}
	
	public final static String getYSig="("+ImageDesc+")I";
	/**
	 * Returns the height of the given image.
	 * 
	 * @param image
	 * @return
	 */
	public static int getY(BufferedImage image) {
		return image.getHeight();
	}
	
	public static String imageToStringSig = "(" + ImageDesc + ")" + StringDesc;

	/**
	 * Returns a String representation of the pixels in the given image.  
	 * 
	 * @param image
	 * @return
	 */
	public final static String imageToString(BufferedImage image) {
		StringBuffer sb = new StringBuffer();
		for (int x = 0; x < image.getWidth(); x++) {
			for (int y=0; y < image.getHeight(); y++) {
				sb.append(image.getRGB(x, y)).append(",");
			}
			sb.append("\n");
		}
		String string = sb.toString();
		return string;		
	}
	

	public static String makeFrameSig = "(" + ImageDesc + ")" + JFrameDesc;	
	/**
	 * Creates and shows a JFrame displaying the given image.
	 * 
	 * @param image   The image to display
	 * @return  The new JFrame
	 * @throws InvocationTargetException
	 * @throws InterruptedException
	 */
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
	
	/**
	 * Compares the pixels after masking the alpha component.
	 * Used for testing.
	 *   
	 * @param image1
	 * @param image2
	 * @return
	 */
	public static boolean compareImages(BufferedImage image1, BufferedImage image2) {
		System.out.println(image1.getColorModel());
		System.out.println(image2.getColorModel());
		int X = image1.getWidth();
		int Y = image1.getHeight();
		if (X != image2.getWidth()) {System.out.println("image widths did not match"); return false; }
		if (Y != image2.getHeight()) {System.out.println("image heights did not match"); return false; }
		for (int y = 0; y < 20; y++) {
		for (int x = 0; x < 20; x++) {
//			if ( getPixel(image1,x,y) != getPixel(image2,x,y)) {
//				System.out.println("Image comparison failed at (x,y)=("+x+","+y+"). Expected " 
//			+ getPixel(image1,x,y) + ", was " + getPixel(image2,x,y) ); 
////				return false;
			System.out.println("(x,y)=("+x+","+y+") " + getPixel(image1,x,y) + ", " + getPixel(image2,x,y));
			
			}
		}
		return true;
	}
	
	
	public static BufferedImage makeConstantImage(int val, int X, int Y) {
		BufferedImage image = makeImage(X,Y);
		for(int y=0; y < Y; ++y) {
			for (int x = 0; x < X; ++x ) {
				setPixel(val, image, x, y);
			}
		}
		return image;
	}

}
