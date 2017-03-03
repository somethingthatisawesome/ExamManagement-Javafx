package bll;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import ell.Gobal;
import ell.Paragraph;

public class BLO {
	public int readParagraph(String path)
	{
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(path);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		XWPFDocument xdoc = null;
		try {
			xdoc = new XWPFDocument(OPCPackage.open(fis));
		} catch (InvalidFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	       List<XWPFParagraph> paragraphList =  xdoc.getParagraphs();
	       List<Paragraph> prList = new ArrayList<Paragraph>();
	       String numbfmr =null;
	       numbfmr = paragraphList.get(0).getNumFmt();
	       int qusindex=0;
	       int i=0;
	       for(XWPFParagraph pr:paragraphList)
	       {
	    	   Paragraph lpr = new Paragraph();
	    	   lpr.value = pr;
	    	   if(pr.getNumFmt()==numbfmr)
	    	   {
	    		   lpr.isQuestion = true;
	    		  qusindex = i;
	    	   }
	    	   else
	    	   {
	    		   lpr.belongtoQuestion = qusindex;
	    	   }
	    	   prList.add(lpr);
	    	   i++;
	       }
	       Gobal.paragraph = prList;
	       
	    return 0;
	}
}
