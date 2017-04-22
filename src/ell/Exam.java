package ell;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.xwpf.usermodel.XWPFParagraph;

public class Exam {
	public List<XWPFParagraph> header;
	public List<XWPFParagraph> document;
	public Exam()
	{
		header = new ArrayList<XWPFParagraph>();
		document = new ArrayList<XWPFParagraph>();
	}
}
