package com.chinamobo.ue.utils;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
 
import javax.imageio.ImageIO;
 
import org.icepdf.core.pobjects.Document;
import org.icepdf.core.pobjects.Page;
import org.icepdf.core.util.GraphicsRenderingHints;

public class PdfUtil {  
	
	public static int PdfToJpg(String p,String getPdfFilePath) throws Exception  {  
		
        String filePath = p;
 
        Document document = new Document();
        try {
            document.setFile(filePath);
        } catch (Exception ex) {
        }
 
        // save page caputres to file.
        float scale = 2f;
        float rotation = 0f;
 
        // Paint each pages content to an image and write the image to file
        for (int i = 0; i < document.getNumberOfPages(); i++) {
            BufferedImage image = (BufferedImage)
            document.getPageImage(i,GraphicsRenderingHints.SCREEN,Page.BOUNDARY_CROPBOX, rotation, scale);
            RenderedImage rendImage = image;
            // capture the page image to file
            try {
                File file = new File(getPdfFilePath+"/" + (i+1) + ".jpg");
                ImageIO.write(rendImage, "jpeg", file);
 
            } catch (IOException e) {
                e.printStackTrace();
            }
            image.flush();
        }
        // clean up resources
        document.dispose();
		return document.getNumberOfPages();
    }
}

