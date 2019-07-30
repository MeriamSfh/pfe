package com.pfe.customcode.connectorcv;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.gnostice.pdfone.PDFOne;
import com.gnostice.pdfone.PdfDocument;
import com.gnostice.pdfone.PdfException;
import com.gnostice.pdfone.PdfPageElement;
import com.gnostice.pdfone.PdfPageImageElement;
import com.gnostice.pdfone.PdfPageTextElement;

public class Test {

	public static void main(String[] args) throws IOException, PdfException {

		int i, n;
	    PdfPageTextElement PdfPageTextElement1;
	    PdfPageImageElement PdfPageImageElement1;
	    BufferedImage BufferedImage1;
	    
	    // Load a PDF document
	    PdfDocument doc = new PdfDocument();
	    doc.load("C:/tika/Meriam-SFAIHI-cv.pdf");
	    
	    // Retrieve image elements from page 1 of the document
	    ArrayList lstImageElements = 
	        (ArrayList) doc.getPageElements(1, PdfPageElement.ELEMENT_TYPE_IMAGE);
	    // Retrieve text elements from page 1 of the document
	    ArrayList lstTextElements = 
	        (ArrayList) doc.getPageElements(1, PdfPageElement.ELEMENT_TYPE_TEXT);
	    
	    // Iterate through retrieved image elements 
	    n = lstImageElements.size();        
	    for (i = 0; i < n; i++) {
	      // Save image content of the current image element to file
	      PdfPageImageElement1 = (PdfPageImageElement) lstImageElements.get(i);
	      BufferedImage1 = PdfPageImageElement1.getImage();            
	      File File1 = new File("page1_image" + (i+1) + ".png");
	      try {
	        ImageIO.write(BufferedImage1, "png", File1);
	      } catch (Exception e) {
	        System.out.println("Sorry, there was an error." + e.getMessage());
	      }

	      // Print details of the current image element
	      System.out.println("Image Element #" + (i+1) + " saved to: " +
	                         "page1_image" + (i+1) + ".bmp (" + 
	                         PdfPageImageElement1.getImageHeight() +
	                         " x " + 
	                         PdfPageImageElement1.getImageWidth() + 
	                         ")");
	    }
	    
	    // Close the document - it needs to be loaded only when images  
	    // need to be extracted - images are accessed only on-demand. 
	    doc.close();           
	    
	    // Iterate through retrieved text elements
	    n = lstTextElements.size();        
	    for (i = 0; i < n; i++) {
	      PdfPageTextElement1 = (PdfPageTextElement) lstTextElements.get(i);
	      // Print details of the current text element
	      System.out.println("Text Element #" + (i+1) + " \"" + 
	                         PdfPageTextElement1.getText() + "\" uses font " + 
	                         PdfPageTextElement1.getTextFontInfo().getFontName());
	    }
	}

}
