package ell;

import org.apache.poi.xwpf.usermodel.IBody;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
/*
 * Cấu trúc văn bản
 */
public class Paragraph  {
	public XWPFParagraph value;
	public boolean isQuestion = false;
	public boolean isStatic = false;
	public boolean isCorrect = false;
	public int belongtoQuestion=-1;
}
