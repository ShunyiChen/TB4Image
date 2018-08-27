package com.maxtree.imageprocessor.services;

import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Transparency;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.imageio.ImageIO;

import ij.ImagePlus;
import ij.plugin.Scaler;
import ij.plugin.filter.Shadows;
import ij.process.ColorProcessor;
import ij.process.ImageProcessor;

/**
 * 
 * @author Chen
 *
 */
public class ImageProcessorAPI {
	
	/**
	 * Find edges
	 * 
	 * @param is
	 * @return
	 * @throws IOException
	 */
	public InputStream findEdges(InputStream is) throws IOException {
		BufferedInputStream bis = new BufferedInputStream(is);  
		bis.mark(is.available()+1);  
		String fileExtension = getFileType(bis);
		bis.reset();  
		BufferedImage img = ImageIO.read(bis);
		ColorProcessor bp = new ColorProcessor(img);
		bp.findEdges();
		BufferedImage lastImage = bp.getBufferedImage();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(lastImage, fileExtension, baos);
		byte[] lastBytes = baos.toByteArray();
		InputStream outis = new ByteArrayInputStream(lastBytes);
		return outis;
	}
	
	/**
	 * Rotate right
	 * 
	 * @param is
	 * @return
	 * @throws IOException
	 */
	public InputStream rotateRight(InputStream is) throws IOException {
		BufferedInputStream bis = new BufferedInputStream(is);  
		bis.mark(is.available()+1);  
		String fileExtension = getFileType(bis);
		bis.reset();  
		BufferedImage img = ImageIO.read(bis);
		ColorProcessor bp = new ColorProcessor(img);
		ImageProcessor ip = bp.rotateRight();
		BufferedImage lastImage = ip.getBufferedImage();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(lastImage, fileExtension, baos);
		byte[] lastBytes = baos.toByteArray();
		InputStream outis = new ByteArrayInputStream(lastBytes);
		return outis;
	}
	
	/**
	 * Rotate left
	 * 
	 * @param is
	 * @return
	 * @throws IOException
	 */
	public InputStream rotateLeft(InputStream is) throws IOException {
		BufferedInputStream bis = new BufferedInputStream(is);  
		bis.mark(is.available()+1);  
		String fileExtension = getFileType(bis);
		bis.reset();  
		BufferedImage img = ImageIO.read(bis);
		ColorProcessor bp = new ColorProcessor(img);
		ImageProcessor ip = bp.rotateLeft();
		BufferedImage lastImage = ip.getBufferedImage();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(lastImage, fileExtension, baos);
		byte[] lastBytes = baos.toByteArray();
		InputStream outis = new ByteArrayInputStream(lastBytes);
		return outis;
	}
	
	/**
	 * Rotate angle
	 * 
	 * @param is
	 * @param angle
	 * @return
	 * @throws IOException
	 */
	public InputStream rotate(InputStream is, double angle) throws IOException {
//		BufferedInputStream bis = new BufferedInputStream(is);  
//		bis.mark(is.available()+1);  
//		String fileExtension = getFileType(bis);
//		bis.reset();  
//		BufferedImage img = ImageIO.read(bis);
//		
//		ColorProcessor bp = new ColorProcessor(img);
//		bp.rotate(angle);
//		BufferedImage bimg = bp.getBufferedImage();
//		
//		ByteArrayOutputStream baos = new ByteArrayOutputStream();
//		ImageIO.write(bimg, fileExtension, baos);
//		InputStream outis = new ByteArrayInputStream(baos.toByteArray());
//		return outis;
		BufferedInputStream bis = new BufferedInputStream(is);  
		bis.mark(is.available()+1);  
		String fileExtension = getFileType(bis);
		bis.reset();  
		BufferedImage img = ImageIO.read(bis);
	    double sin = Math.abs(Math.sin(angle)), cos = Math.abs(Math.cos(angle));
	    int w = img.getWidth(), h = img.getHeight();
	    int neww = (int)Math.floor(w*cos+h*sin), newh = (int) Math.floor(h * cos + w * sin);
	    BufferedImage result = null;
	    if (fileExtension.equals("PNG")) {
	    	result = new BufferedImage(neww, newh, BufferedImage.TYPE_INT_ARGB);
	    } else {
	    	result = new BufferedImage(neww, newh, BufferedImage.TYPE_INT_RGB);
	    }
	    Graphics2D g = result.createGraphics();
	    g.translate((neww - w) / 2, (newh - h) / 2);
	    g.rotate(angle, w / 2, h / 2);
	    g.drawRenderedImage(img, null);
	    g.dispose();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(result, fileExtension, baos);
		byte[] lastBytes = baos.toByteArray();
		InputStream outis = new ByteArrayInputStream(lastBytes);
		return outis;
	}
	
	
	/**
	 * Sharpen
	 * 
	 * @param is
	 * @return
	 * @throws IOException
	 */
	public InputStream sharpen(InputStream is) throws IOException {
		BufferedInputStream bis = new BufferedInputStream(is);  
		bis.mark(is.available()+1);  
		String fileExtension = getFileType(bis);
		bis.reset();  
		BufferedImage img = ImageIO.read(bis);
		ColorProcessor bp = new ColorProcessor(img);
		bp.sharpen();
		BufferedImage lastImage = bp.getBufferedImage();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(lastImage, fileExtension, baos);
		byte[] lastBytes = baos.toByteArray();
		InputStream outis = new ByteArrayInputStream(lastBytes);
		return outis;
	}
	
	/**
	 * Adjust contrast
	 * 
	 * @param is
	 * @return
	 * @throws IOException
	 */
	public InputStream adjustContrast(InputStream is, int cvalue) throws IOException {
		BufferedInputStream bis = new BufferedInputStream(is);  
		bis.mark(is.available()+1);  
		String fileExtension = getFileType(bis);
		bis.reset();  
		BufferedImage img = ImageIO.read(bis);
		ImagePlus imp = new ImagePlus("", img);
		ContrastAdjusterWithoutGUI ad = new ContrastAdjusterWithoutGUI();
		ad.min = imp.getDisplayRangeMin();
		ad.max = imp.getDisplayRangeMax();
		ad.defaultMax = imp.getDisplayRangeMax();
		ad.RGBImage = true;
		ImageProcessor ip = imp.getProcessor();
		ad.adjustContrast222(imp, ip, cvalue);
		BufferedImage lastImage = ip.getBufferedImage();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(lastImage, fileExtension, baos);
		byte[] lastBytes = baos.toByteArray();
		InputStream outis = new ByteArrayInputStream(lastBytes);
		return outis;
	}
	
	/**
	 * Adujst brightness
	 * 
	 * @param is
	 * @param bvalue
	 * @return
	 * @throws IOException
	 */
	public InputStream adjustBrightness(InputStream is, double bvalue) throws IOException {
		BufferedInputStream bis = new BufferedInputStream(is);  
		bis.mark(is.available()+1);  
		String fileExtension = getFileType(bis);
		bis.reset();  
		BufferedImage img = ImageIO.read(bis);
		ImagePlus imp = new ImagePlus("", img);
		ContrastAdjusterWithoutGUI ad = new ContrastAdjusterWithoutGUI();
		ad.min = imp.getDisplayRangeMin();
		ad.max = imp.getDisplayRangeMax();
		ad.defaultMax = imp.getDisplayRangeMax();
		ad.RGBImage = true;
		ImageProcessor ip = imp.getProcessor();
		ad.adjustBrightness222(imp, ip, bvalue);
		BufferedImage lastImage = ip.getBufferedImage();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(lastImage, fileExtension, baos);
		byte[] lastBytes = baos.toByteArray();
		InputStream outis = new ByteArrayInputStream(lastBytes);
		return outis;
	}
	
	/**
	 * Scale
	 * 
	 * @param is
	 * @param xscale
	 * @param yscale
	 * @return
	 * @throws IOException
	 */
	public InputStream scale(InputStream is, double xscale, double yscale) throws IOException {
		BufferedInputStream bis = new BufferedInputStream(is);  
		bis.mark(is.available()+1);  
		String fileExtension = getFileType(bis);
		bis.reset();  
		BufferedImage img = ImageIO.read(bis);
		Scaler scaler = new Scaler();
		scaler.imp =  new ImagePlus("", img);
		ImageProcessor ip = scaler.imp.getProcessor();
		scaler.xscale = xscale;
		scaler.yscale = yscale;
		scaler.newWidth = (int)(scaler.xscale*img.getWidth());
		scaler.newHeight = (int)(scaler.yscale*img.getHeight());
		scaler.averageWhenDownsizing = true;
		scaler.scale222(ip,null);
		BufferedImage lastImage = scaler.imp2.getBufferedImage();
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(lastImage, fileExtension, baos);
		byte[] lastBytes = baos.toByteArray();
		InputStream outis = new ByteArrayInputStream(lastBytes);
		return outis;
	}
	
//	/**
//	 * Re-size
//	 * 
//	 * @param is
//	 * @param window_width
//	 * @param window_height
//	 * @return
//	 * @throws IOException
//	 */
//	public InputStream resize(InputStream is, double window_width, double window_height) throws IOException {
//		BufferedInputStream bis = new BufferedInputStream(is);  
//		bis.mark(is.available()+1);  
//		String fileExtension = getFileType(bis);
//		bis.reset();  
//		BufferedImage img = ImageIO.read(bis);
//		int actualImageWidth = img.getWidth();
//		int actualImageHeight = img.getHeight();
//		
//		if(window_width > actualImageWidth
//				&& window_height > actualImageHeight) {
//			ByteArrayOutputStream baos = new ByteArrayOutputStream();
//			ImageIO.write(img, fileExtension, baos);
//			byte[] lastBytes = baos.toByteArray();
//			InputStream outis = new ByteArrayInputStream(lastBytes);
//			return outis;
//		}
//		ImagePlus imp = new ImagePlus("", img);
//		ImageProcessor ip = imp.getProcessor();
//		int newImageWidth = 0;
//		int newImageHeight = 0;
//		double rate = 0d;
////		if (window_width < window_height) {
////			rate = (double)(window_width / actualImageWidth);
////			newImageHeight = (int) (actualImageHeight * rate);
////			newImageWidth = (int) window_width;
////			
////		} else {
////			rate = (double)(window_height / actualImageHeight);
////			newImageWidth = (int) (actualImageWidth * rate);
////			newImageHeight = (int) window_height;
////		}
//		
//		if (actualImageWidth > actualImageHeight) {
//			if (window_width > actualImageWidth) {
//				rate = (double)(actualImageWidth / window_width);
//				newImageWidth = (int) window_width;
//				newImageHeight = (int) (actualImageHeight * rate);
//				
//			} else {
//				rate = (double)(window_width / actualImageWidth);
//				
//				newImageWidth = (int) actualImageWidth ;
//				newImageHeight = (int) (actualImageHeight * rate);
//			}
//		} else {
//			if (window_height > newImageHeight) {
//				rate = (double)(newImageHeight / window_height);
//				newImageHeight = (int) window_height;
//				newImageWidth = (int) (actualImageWidth * rate);
//			} else {
//				rate = (double)(window_height / newImageHeight);
//				newImageHeight = (int) window_height;
//				newImageWidth = (int) (actualImageWidth * rate);
//			}
//		}
//		
//		ip = ip.resize(newImageWidth, newImageHeight, true);
//		
//		BufferedImage lastImage = ip.getBufferedImage();
//		ByteArrayOutputStream baos = new ByteArrayOutputStream();
//		ImageIO.write(lastImage, fileExtension, baos);
//		byte[] lastBytes = baos.toByteArray();
//		InputStream outis = new ByteArrayInputStream(lastBytes);
//		return outis;
//	}
	
	/**
	 * 
	 * @param is
	 * @return
	 * @throws IOException
	 */
	public int[] getActualSize(InputStream is) throws IOException {
		BufferedInputStream bis = new BufferedInputStream(is);  
		bis.mark(is.available()+1);  
		bis.reset();  
		BufferedImage img = ImageIO.read(bis);
		
		int actualImageWidth = img.getWidth();
		int actualImageHeight = img.getHeight();
		return new int[] {actualImageWidth, actualImageHeight};
	}
	
	/**
	 * 
	 * @param is
	 * @param arg
	 * @return
	 * @throws IOException
	 */
	public InputStream shadows(InputStream is, String arg) throws IOException {
		BufferedInputStream bis = new BufferedInputStream(is);  
		bis.mark(is.available()+1);  
		String fileExtension = getFileType(bis);
		bis.reset();  
		BufferedImage img = ImageIO.read(bis);
		
		Shadows shadows = new Shadows();
		ImagePlus imp = new ImagePlus("", img);
		shadows.setup(arg, imp);
		ImageProcessor ip = imp.getProcessor();
		shadows.run(ip);
		
		BufferedImage lastImage = ip.getBufferedImage();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(lastImage, fileExtension, baos);
		byte[] lastBytes = baos.toByteArray();
		InputStream outis = new ByteArrayInputStream(lastBytes);
		return outis;
	}
	
	/**
	Attempts to determine the image file type by looking for
	'magic numbers' and the file name extension.
	 */
	public String getFileType(InputStream is) {
		byte[] buf = new byte[132];
		try {
			is.read(buf, 0, 132);
			is.reset();
		} catch (IOException e) {
			return "UNKNOWN";
		}
		int b0=buf[0]&255, b1=buf[1]&255, b2=buf[2]&255, b3=buf[3]&255;
		 // Combined TIFF and DICOM created by GE Senographe scanners
		if (buf[128]==68 && buf[129]==73 && buf[130]==67 && buf[131]==77
		&& ((b0==73 && b1==73)||(b0==77 && b1==77)))
			return "TIFF_AND_DICOM";
		 // Little-endian TIFF ("II")
		if (b0==77 && b1==77 && b2==0 && b3==42)
			return "TIFF";
		 // JPEG
		if (b0==255 && b1==216 && b2==255)
			return "JPEG";

		 // GIF ("GIF8")
		if (b0==71 && b1==73 && b2==70 && b3==56)
			return "GIF";
		 // DICOM ("DICM" at offset 128)
		if (buf[128]==68 && buf[129]==73 && buf[130]==67 && buf[131]==77) {
			return "DICOM";
		}
		// PGM ("P1", "P4", "P2", "P5", "P3" or "P6")
		if (b0==80&&(b1==49||b1==52||b1==50||b1==53||b1==51||b1==54)&&(b2==10||b2==13||b2==32||b2==9))
			return "PGM";
		// PNG
		if (b0==137 && b1==80 && b2==78 && b3==71)
			return "PNG";
		// ImageJ, NIH Image, Scion Image for Windows ROI
		if (b0==73 && b1==111) // "Iout"
			return "ROI";
		// ObjectJ project
		if ((b0=='o' && b1=='j' && b2=='j' && b3==0)  )
			return "OJJ";
		// Text file
		boolean isText = true;
		for (int i=0; i<10; i++) {
		  int c = buf[i]&255;
		  if ((c<32&&c!=9&&c!=10&&c!=13) || c>126) {
			  isText = false;
			  break;
		  }
		}
		if (isText)
		   return "TEXT";
		// BMP ("BM")
		if ((b0==66 && b1==77))
			return "BMP";
		return "UNKNOWN";
	}
 
	public InputStream executeCommands(InputStream originalInputStream, List<Command> lstCommands) throws IOException, ImageProcessorException {
		InputStream istream = originalInputStream;
		for (Command command : lstCommands) {
			if (command.option.equals("findEdges")) {
				istream = findEdges(istream);
			} else if (command.option.equals("rotate")) {
				istream = rotate(istream, command.value[0]);
			} else if (command.option.equals("sharpen")) {
				istream = sharpen(istream);
			} else if (command.option.equals("adjustContrast")) {
				istream = adjustContrast(istream, (int)command.value[0]);
			} else if (command.option.equals("adjustBrightness")) {
				istream = adjustBrightness(istream, (int)command.value[0]);
			} else if (command.option.equals("scale")) {
				istream = scale(istream, command.value[0], command.value[1]);
			} else if (command.option.equals("shadows")) {
				istream = shadows(istream, command.shadowsArg);
			} else {
				throw new ImageProcessorException("It does not support "+command.option+".");
			}
		}
		return istream;
	}
	
	public static void main(String[] args) {
		
		testResize();
		
//		testRotate();
		
//		testShadows();
		
		
//		 JOptionPane.showMessageDialog(new JFrame(), "Hello,Maxtree image-processor!!!");
//		  测试缩放
//		try {
//			FileInputStream fis = new FileInputStream(new File("F:\\迅雷下载\\aa.jpg"));
//			IJExternalService service = new IJExternalService();
//			InputStream is = service.scale(fis, 1.5, 1.5);
//			FileOutputStream output = new FileOutputStream("F:\\迅雷下载\\timg25.jpg");
//			byte[] buf = new byte[1024];
//			int bytesRead;
//			while ((bytesRead = is.read(buf)) > 0) {
//				output.write(buf, 0, bytesRead);
//			}
//			output.close();
//			is.close();
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		// 测试调节对比度
//		try {
//			FileInputStream fis = new FileInputStream(new File("F:\\迅雷下载\\aa.jpg"));
//			IJExternalService service = new IJExternalService();
//			InputStream is = service.adjustContrast(fis, 155);
//			FileOutputStream output = new FileOutputStream("E:\\timg26.jpg");
//			byte[] buf = new byte[1024];
//			int bytesRead;
//			while ((bytesRead = is.read(buf)) > 0) {
//				output.write(buf, 0, bytesRead);
//			}
//			output.close();
//			is.close();
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		try {
//			BufferedImage img = ImageIO.read(new File("E:\\timg.jpg"));
//			// 查找边缘
//			ColorProcessor bp = new ColorProcessor (img);
//			bp.findEdges();
//			BufferedImage bimg = bp.getBufferedImage();
//			ImageIO.write(bimg, "jpg", new File("E:\\查找边缘.jpg"));
//			// 自定义按比例缩放
//			MyImageProcessor ed = new MyImageProcessor();
//			BufferedImage bimg = ed.scale(img, 1.0, 1.0);
//			ImageIO.write(bimg, "jpg", new File("E:\\timg25.jpg"));
//		 	左右旋转90度
//			ColorProcessor bp = new ColorProcessor (img);
//			ImageProcessor ip = bp.rotateRight();
//			BufferedImage bimg = ip.getBufferedImage();
//			ImageIO.write(bimg, "jpg", new File("E:\\timg26.jpg"));
//			// 锐化
//			ColorProcessor bp = new ColorProcessor (img);
//			bp.sharpen();
//			BufferedImage bimg = bp.getBufferedImage();
//			ImageIO.write(bimg, "jpg", new File("E:\\timg27.jpg"));
//			调整对比度
//			ImagePlus imp = new ImagePlus("", img);
//			ContrastAdjuster ad = new ContrastAdjuster();
//			ad.min = imp.getDisplayRangeMin();
//			ad.max = imp.getDisplayRangeMax();
//			ad.defaultMax = imp.getDisplayRangeMax();
//			ad.RGBImage = true;
//			ImageProcessor ip = imp.getProcessor();
//			ad.adjustContrast222(imp, ip, 130);
//			BufferedImage bimg = ip.getBufferedImage();
//			ImageIO.write(bimg, "jpg", new File("E:\\timg28.jpg"));
//			图像按比例伸缩
//			Scaler scaler = new Scaler();
//			scaler.imp =  new ImagePlus("", img);
//			ImageProcessor ip = scaler.imp.getProcessor();
//			scaler.xscale = 1.5;
//			scaler.yscale = 1.5;
//			scaler.newWidth = (int)(scaler.xscale*img.getWidth());
//			scaler.newHeight = (int)(scaler.yscale*img.getHeight());
//			scaler.averageWhenDownsizing = true;
//			scaler.scale222(ip,null);
//			BufferedImage bimg = scaler.imp2.getBufferedImage();
//			ImageIO.write(bimg, "jpg", new File("E:\\timg29.jpg"));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}
	
	/**
	 * 
	 */
	public static void testRotate() {
		// 测试旋转角度
		try {	
			FileInputStream fis = new FileInputStream(new File("C:\\1.jpg"));
			ImageProcessorAPI service = new ImageProcessorAPI();
			InputStream is = service.rotate(fis, Math.toRadians(1.1));
			FileOutputStream output = new FileOutputStream("C:\\Temp\\3.jpg");
			byte[] buf = new byte[1024];
			int bytesRead;
			while ((bytesRead = is.read(buf)) > 0) {
				output.write(buf, 0, bytesRead);
			}
			output.close();
			is.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void testShadows() {
		// 测试shadows
		try {	
			FileInputStream fis = new FileInputStream(new File("C:\\Demo\\1.jpg"));
			ImageProcessorAPI api = new ImageProcessorAPI();
			InputStream is = api.shadows(fis, "east");
			FileOutputStream output = new FileOutputStream("C:\\Demo\\12.jpg");
			byte[] buf = new byte[1024];
			int bytesRead;
			while ((bytesRead = is.read(buf)) > 0) {
				output.write(buf, 0, bytesRead);
			}
			output.close();
			is.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void testResize() {
//		// 测试shadows
//		try {	
//			FileInputStream fis = new FileInputStream(new File("C:\\Demo\\1.jpg"));
//			ImageProcessorAPI api = new ImageProcessorAPI();
//			InputStream is = api.resize(fis, 763, 535);
//			FileOutputStream output = new FileOutputStream("C:\\Demo\\0726_2.jpg");
//			byte[] buf = new byte[1024];
//			int bytesRead;
//			while ((bytesRead = is.read(buf)) > 0) {
//				output.write(buf, 0, bytesRead);
//			}
//			output.close();
//			is.close();
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}

//	 public BufferedImage scale(BufferedImage bufferedThumb, double sx, double sy) {
//		 AffineTransform af = new AffineTransform();
//		 af.scale(sx, sy);
//
//		 AffineTransformOp operation = new AffineTransformOp(af, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
//		 bufferedThumb = operation.filter(bufferedThumb, null);
//		 return bufferedThumb;
//	 }
	
}
