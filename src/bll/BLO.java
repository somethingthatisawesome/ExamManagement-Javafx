package bll;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.paint.Color;
import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.Document;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFPictureData;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBody;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHMerge;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyles;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STHighlightColor;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STHighlightColor.Enum;
import org.xml.sax.InputSource;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import ell.Gobal;
import ell.Paragraph;
import ell.Question;
/*
 * Class BLO đóng vai trò xử lý tất cả các thông tin nghiệp vụ của chương trình
 */
public class BLO {
	private final String _FORMATPATH = "./src/asset/format.txt";
	private String tempPath ;
	private FileInputStream fis = null;
	private XWPFDocument xdoc = null;
	static private boolean yestoall =false;
	//
	public List<List<Paragraph>> Exams = new ArrayList<List<Paragraph>>();
	BigInteger restart;
	BigInteger cont;
	//TODO tạo ra file tạm để kiểm tra
	/*
	 * Tạo xuất ra thông tin bị sai sót trong cấu trúc đề thi vào tập tin đang sử dụng
	 * các thông tin bị sai sót sẽ được đánh dấu hightlight
	 */
	public void exportTempExam(List<Paragraph> lpr,String path,List<XWPFParagraph> header,List<XWPFParagraph>essay)
	{
		try {
			xdoc = new XWPFDocument(new FileInputStream(path));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		int size = xdoc.getParagraphs().size();
		 for(int i=0;i<size;i++)
				xdoc.removeBodyElement(0);
        int pos;
        for(XWPFParagraph pr:header) {
        	xdoc.createParagraph();
        	 pos = xdoc.getParagraphs().size()-1;

        	xdoc.setParagraph(pr, pos);
        }
        for(Paragraph pr:lpr) {
        	xdoc.createParagraph();
        	 pos = xdoc.getParagraphs().size()-1;

        	xdoc.setParagraph(pr.value, pos);
        }
        for(XWPFParagraph pr:essay) {
        	xdoc.createParagraph();
        	 pos = xdoc.getParagraphs().size()-1;

        	xdoc.setParagraph(pr, pos);
        }
        try {
        	
        	exportDocument("./src/asset/headers/temp.docx", xdoc);
        	fis.close();
        } catch(Exception e) {
        	e.printStackTrace();
        }
	}
	//TODO Chạy file tạm
	/*
	 * Khởi động file tạm bằng các chương trình đọc docx mặc định có trên máy
	 * 
	 */
	public void openTempExam(String path)
	{
		File f = new File(path);
		try {
			String cmd = "cmd /C start winword.exe /t\"" + f.getAbsolutePath() + "\"";
	        Process child = Runtime.getRuntime().exec(cmd);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//TODO xóa highlight của đoạn văn
	/*
	 * Trước khi xuất văn bản hoàn chỉnh cần phải xóa hightlight đã được đánh dấu trước đó
	 */
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
	/*
	 * Xóa tất cả các HightLight có mặt trong văn bản
	 */
		private void clearAllHighLight(List<Paragraph> lpr)
		{
			for(Paragraph pr:lpr)
			{
				clearHighLight(pr.value);
			}
		}
	//TODO đọc file
		/*
		 * Đọc file docx chức dữ liệu đề thi từ bộ nhớ máy tính theo đường dẫn path
		 */	
	public int readParagraph(String path,Color staticQ,Color Question)
	{
		tempPath =path;
		String staticColor="";
		String correctQuestion = "";
		//System.out.println("#" + Integer.toHexString(staticQ.hashCode()).substring(0,6).toUpperCase());
		if(staticQ!=null)
			staticColor =  String.format( "%02X%02X%02X",
		            (int)( staticQ.getRed() * 255 ),
		            (int)( staticQ.getGreen() * 255 ),
		            (int)( staticQ.getBlue() * 255 ) );
		if(Question!=null)
			correctQuestion =  String.format( "%02X%02X%02X",
		            (int)( Question.getRed() * 255 ),
		            (int)( Question.getGreen() * 255 ),
		            (int)( Question.getBlue() * 255 ) );
		System.out.println("Màu:"+correctQuestion+" "+staticColor);
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

	       int headersize = 0;
	       
	       for(XWPFParagraph pr:paragraphList)
	       {
	    	   Gobal.header.add(pr);
	    	   if(pr.getText().toLowerCase().contains("phần thi trắc nghiệm"))
	    	   {
	    		  
	    		   break;
	    	   }
	    	   headersize++;
	    	   
	       }
	       
	       Gobal.headersize = headersize;
	       //headersize + 1

	       int size = paragraphList.size();
	       System.out.println(headersize+""+size);
	       if(headersize==size)
	       {
	    	   headersize = -1;
	    	   Gobal.headersize = 0;
	       }
	       for(int i=headersize+1;i<size;i++)
	       {
	    	   XWPFParagraph pr = paragraphList.get(i);
	    	   if(pr.getText().toLowerCase().contains("phần thi tự luận"))
	    		   break;
	    	   Paragraph lpr = new Paragraph();
	    	   lpr.value = pr;
	    	   if(pr.getNumFmt()==numbfmr)
	    	   {
	    	     
	    		   lpr.isQuestion = true;
	    		   String color  = pr.getRuns().get(0).getColor();
	    		   System.out.println(color);
	    		   if(staticColor=="")
	    		   {
		    		   if(color!=null)
		    		   {
		    			   lpr.isStatic=true;
		    		   }
	    		   }
	    		   else
	    		   {
	    			  
	    			   if(color!=null&&color.equals(staticColor))
		    		   {
		    			   lpr.isStatic=true;
		    		   }
	    		   }
	    	   }
	    	   else
	    	   {
	    		   List<XWPFRun> runs= pr.getRuns();
	    		   
	    		   String color = runs.get(0).getColor();
	    		   //TODO in mã màu
	    		   System.out.println(color);
	    		   if(correctQuestion=="")
	    		   {
		    		   if(color!=null)
		    		   {
		    			   
		    			   lpr.isCorrect=true;
		    	    	   
		    		   }
	    		   }
	    		   else
	    		   {
	    			   if(color!=null&&color.equals(correctQuestion))
		    		   {
		    			   
		    			   lpr.isCorrect=true;
		    	    	   
		    		   }
	    		   }
	    	   }
	    	   
	    	   prList.add(lpr);
	    	   System.out.println(lpr.value.getText());
	       }
	       clearAllHighLight(prList);
	       Gobal.paragraph = prList;
	       int pos = Gobal.header.size()+Gobal.paragraph.size()-1;
	       for(int i=pos;i<size;i++)
	       {
	    	   XWPFParagraph pr = paragraphList.get(i);
	    	   Gobal.essay.add(pr);
	       }
	       
	    return 0;
	}
	/*
	 * Xóa màu chữ của văn bản 
	 * bởi vì văn bản chỉ có màu đen khi được xuất ra
	 */
	private List<Paragraph> clearParaphColor(List<Paragraph> lpr)
	{
		for(Paragraph pr:lpr)
		{
			
			pr.value.getCTP().getPPr().setRPr(null);
			List<XWPFRun> runs = pr.value.getRuns();
			 
			   for(XWPFRun run:runs)
			   {
				   run.getCTR().setRPr(null);
				  
			   }
			  
		}
		return lpr;
	}
	/*
	 * Trộn câu hỏi
	 */
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
			
			int asize = q.answers.size();
			for(int i=0;i<asize;i++)
			{
				
		        q.answers.get(i).value.setNumID(q.numID);
			}
		}
		Collections.shuffle(questions);
		return questions;
	}
	/*
	 * Chuyển văn bản sang câu hỏi
	 */
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
	/*
	 * Chuyển câu hỏi sang văn bản
	 */
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
	/*
	 * Trộn văn bản
	 */
	private List<Paragraph> randomizeParagraph(List<Paragraph> lpr)
	{
		return convertToParagraph(randomizeQuestions(lpr));
	}
	/*
	 * Nạp header
	 */
	private XWPFDocument loadHeader(String path)
	{
		FileInputStream fis =null;
		XWPFDocument header = null;
		try {
			fis = new FileInputStream(path);
			header = new XWPFDocument(fis);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return header;
	}
	private static void appendBody(CTBody src, CTBody append) throws Exception {
	    XmlOptions optionsOuter = new XmlOptions();
	    optionsOuter.setSaveOuter();
	    String appendString = append.xmlText(optionsOuter);
	    String srcString = src.xmlText();
	    String prefix = srcString.substring(0,srcString.indexOf(">")+1);
	    String mainPart = srcString.substring(srcString.indexOf(">")+1,srcString.lastIndexOf("<"));
	    String sufix = srcString.substring( srcString.lastIndexOf("<") );
	    String addPart = appendString.substring(appendString.indexOf(">") + 1, appendString.lastIndexOf("<"));
	    CTBody makeBody = CTBody.Factory.parse(prefix+mainPart+addPart+sufix);
	    src.set(makeBody);
	}
	/*
	 * Xuất văn bản đã được trộn
	 */
	private int exportRandomizeExam(List<Paragraph> prs,String path,String id)
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
		//passStyle(xdoc, header);
		/*
		try {
			appendBody(xdoc.getDocument().getBody(),header.getDocument().getBody());
		} catch (Exception e1) {
			// TODO Auto-generated cat0h block
			e1.printStackTrace();
		}*/
        int pos;
        String name = "";
        if(Gobal.headersize!=0)
        try {
        	List<XWPFParagraph> hpr = null;
			hpr = new ArrayList<XWPFParagraph>();
			for(XWPFParagraph pr:Gobal.header)
			{
				hpr.add(pr);
			}
			//Change text box
			 CTR ctr = hpr.get(0).getCTP().getRArray(0);
			    InputSource is = new InputSource(new StringReader(ctr.xmlText()));
			    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		        DocumentBuilder db = dbf.newDocumentBuilder();
		        org.w3c.dom.Document doc = db.parse(is);
		        NodeList elements = doc.getElementsByTagName("w:t");
		        int sizel = elements.getLength();
		        System.out.println(elements.getLength());
		        Node element = null;
		        if(Gobal.textbox_pos==-1)
		        for(int i=0;i<sizel;i++)
		        {
		        	element = elements.item(i);
		        	if(element.getTextContent().equals("A"))
		        	{
		        		Gobal.textbox_pos = i;
		        		break;
		        	}
		        	System.out.println(element.getTextContent());
		        }
		       elements.item(Gobal.textbox_pos).setTextContent(id);
		        
		      XmlObject xobj = XmlObject.Factory.parse(toString(doc));
		      ctr.set(xobj);
			for(XWPFParagraph pr:hpr)
			{
				xdoc.createParagraph();
				pos = xdoc.getParagraphs().size()-1;
				xdoc.setParagraph(pr, pos);
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			//e1.printStackTrace();
			System.out.println(e1.getMessage());
		}
        
        for(Paragraph pr:lpr) {
        	xdoc.createParagraph();
        	pos = xdoc.getParagraphs().size()-1;
        	xdoc.setParagraph(pr.value, pos);
        }
        for(XWPFParagraph e:Gobal.essay)
        {
        	xdoc.createParagraph();
        	pos = xdoc.getParagraphs().size()-1;
        	xdoc.setParagraph(e, pos);
        }
        //TODO thêm đề thi vào biến tạm
        Exams.add(lpr);
       //XWPFDocument adocument = exportAnswer(lpr);
        
        try {

        	exportDocument(path, xdoc);
            fis.close();
        } catch(Exception e) {
        	e.printStackTrace();
        }
		return 0;
	}
	/*
	 * Xuất văn bản
	 */
	public void exportExams(List<Paragraph> lpr, String path, int number, boolean b) {
		Exams.clear();
		if(b==false)
		{
			String st="";
			for(int i=1;i<=number;i++)
			{
				st = path+"_"+i+".docx";
				int check  =0;
				if(yestoall==false)
					check = checkFileExist(st);
				if(check==2) yestoall=true;
				if(check==3) continue;
				exportRandomizeExam(lpr, st,Integer.toString(i));
			}
			String tempPath = path.substring(0,path.lastIndexOf("\\"));

			if(yestoall==true)
			{
				exportDocument(tempPath+"\\Đáp án.docx",exportAnswer(false));
				exportExcel(tempPath+"\\Đáp án.xlsx", exportAnswerExcel(false));
			}
			else
			{
				int checkAnsDoc = checkFileExist(tempPath+"\\Đáp án.docx");
				if(checkAnsDoc==2)
				{
					yestoall=true;
				}
				if(checkAnsDoc!=3||yestoall==true)
				{
					exportDocument(tempPath+"\\Đáp án.docx",exportAnswer(false));
				}
				
				if(yestoall==false)
				{
					int checkAnsExcel = checkFileExist(tempPath+"\\Đáp án.xlsx");
					if(checkAnsExcel!=3)
					{
						exportExcel(tempPath+"\\Đáp án.xlsx", exportAnswerExcel(false));
					}
				}
			}
		}else
		{
			String st="";
			for(int i=1;i<=number;i++)
			{
				
				
				String t = convertNumbertoAphabet(i);
				st = path+"_"+t +".docx";
				int check  =0;
				if(yestoall==false)
					check = checkFileExist(st);
				if(check==2) yestoall=true;
				if(check==3) continue;
				
				exportRandomizeExam(lpr, st,t);
			}
			String tempPath = path.substring(0,path.lastIndexOf("\\"));

			if(yestoall==true)
			{
				exportDocument(tempPath+"\\Đáp án.docx",exportAnswer(true));
				exportExcel(tempPath+"\\Đáp án.xlsx", exportAnswerExcel(true));
			}
			else
			{
				int checkAnsDoc = checkFileExist(tempPath+"\\Đáp án.docx");
				if(checkAnsDoc==2)
				{
					yestoall=true;
				}
				if(checkAnsDoc!=3||yestoall==true)
				{
					exportDocument(tempPath+"\\Đáp án.docx",exportAnswer(true));
				}
				
				if(yestoall==false)
				{
					int checkAnsExcel = checkFileExist(tempPath+"\\Đáp án.xlsx");
					if(checkAnsExcel!=3)
					{
						exportExcel(tempPath+"\\Đáp án.xlsx", exportAnswerExcel(true));
					}
				}
			}
			
			
		}
		yestoall = false;
		
	}
	//TODO nạp dữ liệu từ format.xml
	/*
	 * Nạp định dạng màu từ file format.xml vào cài đặt cho hệ thống
	 */
	public List<String> loadFormat()
	{
		List<String> formatFile=null;
		try {
			formatFile= Files.readAllLines(Paths.get(_FORMATPATH));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	return formatFile;
	}
	//TODO lưu format
	/*
	 * Lưu định dạng của chương trình vào xml
	 */
	public void saveFormat(List<String> format)
	{
		
		try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(_FORMATPATH))) {
           for(String string:format)
        	   writer.write(string+"\n");
        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		System.out.println("saved!");
	}
	//TODO HightLight đoạn văn
	/*
	 * HighLight đoạn văn bản tùy theo mã màu được truyền vào
	 */
	public void hightLightParagraph(Paragraph pr,String color)
	{
		Enum a = STHighlightColor.YELLOW;
		color = color.toLowerCase();
		switch(color)
		{
		case "black":
			a = STHighlightColor.BLACK;
			break;
		case "dark red":
			a = STHighlightColor.DARK_RED;
			break;
		case "red":
			a = STHighlightColor.RED;
			break;
		case "dark yellow":
			a = STHighlightColor.DARK_YELLOW;
			break;
		case "yellow":
			a = STHighlightColor.YELLOW;
			break;
		case "dark blue":
			a = STHighlightColor.DARK_BLUE;
			break;
		case "bright green":
			a= STHighlightColor.GREEN;
			break;
		case "green":
			a= STHighlightColor.DARK_GREEN;
			break;
		case "turquoise":
			a= STHighlightColor.CYAN;
			break;
		case "violet":
			a= STHighlightColor.MAGENTA;
			break;
		default:
			a= STHighlightColor.YELLOW;
			break;
		}
		List<XWPFRun> runs = pr.value.getRuns();
		for(XWPFRun run:runs)
		{
			 run.getCTR().addNewRPr().addNewHighlight().setVal(a);
		}
	}
	/*
	 * Xuất đáp án của đề thi
	 * tùy theo giá trị b mà người dùng xuất theo định dạn số 1 2 3 hay ký tự A B C
	 */
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
	/*
	 * Chuyển số sang ký tự Aphabet VD: 1->A
	 */
	private String convertNumbertoAphabet(int number)
	{
		 String st = "";
		    int tempnumber = number;
		    while (tempnumber > 0)
		    {
		        int currentLetterNumber = (tempnumber - 1) % 26;
		        char currentLetter = (char)(currentLetterNumber + 65);
		        st = currentLetter + st;
		        tempnumber = (tempnumber - (currentLetterNumber + 1)) / 26;
		    }
		    return st;
	}
	/*
	 * Xuất đáp án dạng Bảng tính Excel
	 * tùy theo giá trị b mà xuất ra định dạng số hay ký tự
	 */
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
	/*
	 * Xuất văn bản dưới định dạng Word document
	 * và lưu ở đường dẫn theo biến path truyền vào
	 */
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
	/*
	 * Xuất văn bản dưới định dạng Excel
	 * và lưu ở đường dẫn theo biến path truyền vào
	 */
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
	private  String toString(org.w3c.dom.Document newDoc) throws Exception{
	    DOMSource domSource = new DOMSource(newDoc);
	    javax.xml.transform.Transformer transformer = TransformerFactory.newInstance().newTransformer();
	    StringWriter sw = new StringWriter();
	    StreamResult sr = new StreamResult(sw);
	    transformer.transform(domSource, sr);
	   return sw.toString();
	  }
	private int checkFileExist(String path)
	{
		File f = new File(path);
		ButtonType yes = new ButtonType("Ghi đè",ButtonData.OK_DONE);
		ButtonType yestoall = new ButtonType("Ghi đè tất cả",ButtonData.OK_DONE);
		ButtonType skip = new ButtonType("Bỏ qua",ButtonData.OK_DONE);
		Alert alert = new Alert(AlertType.WARNING,"",yes,yestoall,skip);
		alert.setTitle("Tập tin đã tồn tại");
		
		alert.setContentText("Tập tin "+f.getName()+" đã tồn tại.");
		if(f.exists() && !f.isDirectory()) { 
			Optional<ButtonType> result = alert.showAndWait();
			if(result.get()==yes)
			{
				return 1;
			}
			if(result.get()==yestoall)
			{
				return 2;
			}
			if(result.get()==skip)
			{
				return 3;
			}
		}
		return 0;
	}
}

