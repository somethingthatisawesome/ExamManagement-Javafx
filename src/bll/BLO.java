package bll;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.IRunElement;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFStyle;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHMerge;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STHighlightColor;

import application.PragraphController;
import ell.Gobal;
import ell.Paragraph;
import ell.Question;

public class BLO {
	private String tempPath ;
	private FileInputStream fis = null;
	private XWPFDocument xdoc = null;
	public List<List<Paragraph>> Exams = new ArrayList<List<Paragraph>>();
	BigInteger restart;
	BigInteger cont;
	//TODO tạo ra file tạm để kiểm tra
	public void exportTempExam(List<Paragraph> lpr,String path)
	{
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
        	
        	exportDocument(path, xdoc);
        	fis.close();
        } catch(Exception e) {
        	e.printStackTrace();
        }
	}
	//TODO chạy file tạm
	public void openTempExam(String path)
	{
		File f = new File(path);
		try {
			Desktop.getDesktop().open(f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//TODO xóa highlight của đoạn văn
	private void clearHighLight(XWPFParagraph pr)
	{
		List<XWPFRun> runs = pr.getRuns();
		for(XWPFRun run:runs)
		{
			CTRPr a = run.getCTR().addNewRPr();
			
			a.addNewHighlight().setVal(STHighlightColor.NONE);
			System.out.println(run.getCTR());
		}
	}
	//TODO xóa highlight của văn bản
		private void clearAllHighLight(List<Paragraph> lpr)
		{
		
			for(Paragraph pr:lpr)
			{
				clearHighLight(pr.value);
			}
		}
	//TODO đọc file
	public int readParagraph(String path)
	{
		tempPath =path;
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
	       XWPFParagraph first = paragraphList.get(0);
	       numbfmr = first.getNumFmt();
	       int qusindex=0;
	       int i=0;
	       for(XWPFParagraph pr:paragraphList)
	       {
	    	   Paragraph lpr = new Paragraph();
	    	   lpr.value = pr;
	    	   
	    	   if(pr.getNumFmt()==numbfmr)
	    	   {
	    		   lpr.isQuestion = true;
	    		   if(pr.getRuns().get(0).getColor()!=null)
	    		   {
	    			   lpr.isStatic=true;
	    		   }
	    		  qusindex = i;
	    	   }
	    	   else
	    	   {
	    		   List<XWPFRun> runs= pr.getRuns();
	    		   
	    		   String color = runs.get(0).getColor();
	    		   //TODO in mã màu
	    		   System.out.println(color);
	    		   if(color!=null)
	    		   {
	    			   
	    			   lpr.isCorrect=true;
	    	    	   
	    		   }
	    		   lpr.belongtoQuestion = qusindex;
	    	   }

	    	   prList.add(lpr);
	    	   i++;
	       }
	
	       Gobal.paragraph = prList;
	       
	    return 0;
	}
	private List<Paragraph> clearParaphColor(List<Paragraph> lpr)
	{
		for(Paragraph pr:lpr)
		{
			// TODO In kết quả của Pr
			   //System.out.println(pr.value.getCTP().getPPr());
			   
			pr.value.getCTP().getPPr().setRPr(null);
			List<XWPFRun> runs = pr.value.getRuns();
			 
			   for(XWPFRun run:runs)
			   {
				   run.getCTR().setRPr(null);
				  
			   }
			  
		}
		return lpr;
	}
	private List<Question> randomizeQuestions(List<Paragraph> lpr)
	{
		List<Question> questions = convertToQuestion(lpr);
		for(Question q:questions)
		{
			;
			if(q.value.isStatic==false)
			{
				Collections.shuffle(q.answers);
			}
			//BigInteger b = q.answers.get(0).value.getNumID();
			int asize = q.answers.size();
			for(int i=0;i<asize;i++)
			{
				
		        q.answers.get(i).value.setNumID(q.numID);
			}
		}
		Collections.shuffle(questions);
		return questions;
	}
	public List<Question> convertToQuestion(List<Paragraph> lpr)
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
				tq.numID=tq.answers.get(0).value.getNumID();
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
		List<Paragraph> lpr =randomizeParagraph(prs);
		clearAllHighLight(lpr);
		clearParaphColor(lpr);
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
        	 //xóa màu

        	xdoc.setParagraph(pr.value, pos);
        }
        //TODO thêm đề thi vào biến tạm
        Exams.add(lpr);
       //XWPFDocument adocument = exportAnswer(lpr);
       
        try {
            //FileOutputStream out = new FileOutputStream(path);
            //FileOutputStream aout = new FileOutputStream(path.substring(0,path.lastIndexOf("\\"))+"\\Đáp án.docx");
            //xdoc.write(out);
            //adocument.write(aout);
            //aout.close();
            //out.close();
        	exportDocument(path, xdoc);
            fis.close();
        } catch(Exception e) {
        	e.printStackTrace();
        }
		return 0;
	}
	public void exportExams(List<Paragraph> lpr, String path, int number, boolean b) {
		Exams.clear();
		if(b==false)
		{
			for(int i=1;i<=number;i++)
			{
				exportRandomizeExam(lpr, path+"_"+i+".docx");
			}
			String tempPath = path.substring(0,path.lastIndexOf("\\"));
			exportDocument(tempPath+"\\Đáp án.docx",exportAnswer(false));
			exportExcel(tempPath+"\\Đáp án.xlsx", exportAnswerExcel(false));
		}else
		{
			for(int i=1;i<=number;i++)
			{
				exportRandomizeExam(lpr, path+"_"+ convertNumbertoAphabet(i)+".docx");
			}
			String tempPath = path.substring(0,path.lastIndexOf("\\"));
			exportDocument(tempPath+"\\Đáp án.docx",exportAnswer(true));
			exportExcel(tempPath+"\\Đáp án.xlsx", exportAnswerExcel(true));
		}
		
	}
	//TODO HightLight đoạn văn
	public void hightLightParagraph(Paragraph pr)
	{
		List<XWPFRun> runs = pr.value.getRuns();
		for(XWPFRun run:runs)
		{
			 run.getCTR().addNewRPr().addNewHighlight().setVal(STHighlightColor.YELLOW);
		}
	}
	private XWPFDocument exportAnswer(boolean b)
	{
		XWPFTableRow tableRow;
		XWPFTableCell cell;
		XWPFDocument document = new XWPFDocument();
		
		int index=0;
		for(List<Paragraph> lpr :Exams)
		{
		index++;
		CTHMerge hMerge = CTHMerge.Factory.newInstance();
		XWPFTable table = document.createTable();
		List<Question> lqs = convertToQuestion(lpr);
		int size = lqs.size();
		XWPFTableRow firstrow = table.getRow(0);
		
		cell=firstrow.getCell(0);
		if (cell.getCTTc().getTcPr() == null) cell.getCTTc().addNewTcPr();
		if (cell.getCTTc().getTcPr().getGridSpan() == null) cell.getCTTc().getTcPr().addNewGridSpan();
		cell.getCTTc().getTcPr().getGridSpan().setVal(BigInteger.valueOf(2));
		
		if(b==false)
		{
		firstrow.getCell(0).setText("Đề "+index);
		}
		else
		{
			firstrow.getCell(0).setText("Đề "+convertNumbertoAphabet(index));	
		}
		tableRow = table.createRow();
		cell=tableRow.getCell(0);
		cell.getCTTc().addNewTcPr().addNewTcW().setW(BigInteger.valueOf(1000));
		cell.setText("Câu");
		cell = tableRow.createCell();
		cell.getCTTc().addNewTcPr().addNewTcW().setW(BigInteger.valueOf(2000));
		cell.setText("Đáp án");
		String holder;
		for(int i=0;i<size;i++)
		{
			holder="";
			tableRow = table.createRow();
			 tableRow.getCell(0).setText(Integer.toString(i+1));
			 cell = tableRow.createCell();
			 List<Paragraph> las = lqs.get(i).answers;
			 int asize = las.size();
			 for(int j=0;j<asize;j++)
			 {
				 if(las.get(j).isCorrect)
				 {
					 holder+=convertNumbertoAphabet(j+1)+" ";
				 }
			 }
			 cell.setText(holder);
			 
		}
		document.createParagraph();
		}
		return document;
	}
	private String convertNumbertoAphabet(int number)
	{
		String str = Character.toString((char) (number+64));
		return str;
	}
	private XSSFWorkbook exportAnswerExcel(Boolean b)
	{
		XSSFWorkbook wb = new XSSFWorkbook();
		XSSFSheet sheet;
		XSSFRow row;
		XSSFCell cell;
		String holder="";
		int index=0;
		int size = 1;
		String sheetname="";
		for(List<Paragraph> lpr: this.Exams)
		{
			List<Question> lqs = convertToQuestion(lpr);
			size = lqs.size();
			index++;
			if(b==false)
			sheetname = "Đề "+index;
			else
				sheetname="Đề "+convertNumbertoAphabet(index);
			sheet = wb.createSheet(sheetname);
			sheet.createRow(0);
			row = sheet.getRow(0);
			row.createCell(0);
			row.createCell(1);
			cell = row.getCell(0);
			cell.setCellValue(sheetname);
			sheet.addMergedRegion(new CellRangeAddress(0,0,0,1));
			row = sheet.createRow(1);
			cell = row.createCell(0);
			cell.setCellValue("Câu");
			cell = row.createCell(1);
			cell.setCellValue("Đáp án");
			for(int i=0;i<size;i++)
			{
				holder="";
				row = sheet.createRow(i+2);
				row.createCell(0);
				row.createCell(1);
				cell = row.getCell(0);
				cell.setCellValue(i+1);
				cell = row.getCell(1);
				List<Paragraph> las = lqs.get(i).answers;
				 int asize = las.size();
				 for(int j=0;j<asize;j++)
				 {
					 if(las.get(j).isCorrect)
					 {
						 holder+=convertNumbertoAphabet(j+1)+" ";
					 }
				 }
				 
				 cell.setCellValue(holder);
			}
			
			 
		}
		return wb;
	}
	private void exportDocument(String path,XWPFDocument document)
	{

			try {
				FileOutputStream out = new FileOutputStream(path);
				document.write(out);
				out.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 
	}
	private void exportExcel(String path,XSSFWorkbook document)
	{
		try {
			FileOutputStream out = new FileOutputStream(path);
			document.write(out);
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
