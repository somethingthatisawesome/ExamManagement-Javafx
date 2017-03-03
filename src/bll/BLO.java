package bll;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import application.PragraphController;
import ell.Gobal;
import ell.Paragraph;
import ell.Question;

public class BLO {
	private String tempPath ;
	private FileInputStream fis = null;
	private XWPFDocument xdoc = null;
	public int readParagraph(String path)
	{
		tempPath  =path;
		try {
			fis = new FileInputStream(path);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
	    		   if(pr.getRuns().get(0).getColor()!=null)
	    		   {
	    			   lpr.isCorrect=true;
	    	    	   System.out.println(pr.getRuns().get(0).getColor());
	    		   }
	    		   lpr.belongtoQuestion = qusindex;
	    	   }

	    	   prList.add(lpr);
	    	   i++;
	       }
	       Gobal.paragraph = prList;
	       
	    return 0;
	}
	private List<Question> randomizeQuestions(List<Paragraph> lpr)
	{
		List<Question> questions = convertToQuestion(lpr);
		for(Question q:questions)
		{
			if(q.value.isStatic==false)
			{
				Collections.shuffle(q.answers);
			}
		}
		Collections.shuffle(questions);
		return questions;
	}
	private List<Question> convertToQuestion(List<Paragraph> lpr)
	{
		Paragraph pr;
		List<Question> questions = new ArrayList<Question>();
		int size = lpr.size();
		for(int i=0;i<size;i++)
		{
			pr = lpr.get(i);
			if(pr.isQuestion==true)
			{
				Question tq = new Question();
				tq.value = pr;
				
				do
				{
					i++;
					pr = lpr.get(i);
					if(pr.isQuestion==false)
					{
						tq.answers.add(pr);
					}
				}while(i<size-1&&pr.isQuestion==false);
				i--;
				questions.add(tq);
			}
		}
		return questions;
	}
	private List<Paragraph> convertToParagraph(List<Question> lq)
	{
		List<Paragraph> lpr = new ArrayList<Paragraph>();
		for(Question q:lq)
		{
			lpr.add(q.value);
			for(Paragraph ans:q.answers)
			{
				lpr.add(ans);
			}
		}
		return lpr;
	}
	private List<Paragraph> randomizeParagraph(List<Paragraph> lpr)
	{
		return convertToParagraph(randomizeQuestions(lpr));
	}
	private int exportRandomizeExam(List<Paragraph> prs,String path)
	{
		List<Paragraph> lpr = randomizeParagraph(prs);
		try {
			xdoc = new XWPFDocument(new FileInputStream(tempPath));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		int size = xdoc.getParagraphs().size();
		 for(int i=0;i<size;i++)
				xdoc.removeBodyElement(0);
        int pos;
        for(Paragraph pr:lpr) {
        	xdoc.createParagraph();
        	 pos = xdoc.getParagraphs().size()-1;
        	xdoc.setParagraph(pr.value, pos);
        }
       
        try {
            FileOutputStream out = new FileOutputStream(path);
            xdoc.write(out);
            out.close();
            fis.close();
        } catch(Exception e) {}
		return 0;
	}
	public void exportExams(List<Paragraph> lpr, String path, int number, boolean b) {
		if(b==false)
		{
			for(int i=1;i<=number;i++)
			{
				exportRandomizeExam(lpr, path+"_"+i+".docx");
			}
		}else
		{
			for(int i=1;i<=number;i++)
			{
				exportRandomizeExam(lpr, path+"_"+ convertNumbertoAphabet(i)+".docx");
			}
		}
		
	}
	private String convertNumbertoAphabet(int number)
	{
		String str = Character.toString((char) (number+64));
		return str;
	}
	
}
