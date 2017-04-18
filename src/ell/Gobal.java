package ell;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.xwpf.usermodel.XWPFParagraph;
/*
 * Chứa tất cả thông tin tĩnh công khai để dễ dàng truy cập
 */

import javafx.scene.paint.Color;
public class Gobal {
	public final static Color  _BLACK = Color.web("#000000");
	public final static Color  _DARKRED = Color.web("#C00000");
	public final static Color  _RED = Color.web("#FF0000");
	public final static Color  _ORANGE = Color.web("#FFC000");
	public final static Color  _YELLOW = Color.web("#FFFF00");
	public final static Color  _LIGHTGREEN = Color.web("#92D050");
	public final static Color  _GREEN = Color.web("#00B050");
	public final static Color  _LIGHTBLUE = Color.web("#00B0F0");
	public final static Color  _BLUE = Color.web("#0070C0");
	public final static Color  _DARKBLUE = Color.web("#002060");
	public final static Color  _PURPLE = Color.web("#7030A0");
	public static List<Paragraph> paragraph;
	public static int status = 0;
	public static String path="";
	public static String statusInfo="";
	public static int warningsize=0;
	public static int numquestion=0;
	public static String _DA_error = "Yellow";
	public static String _DQ_error = "Yellow";
	public static String _NCA_error = "Yellow";
	public static String _staticQuestion = "Light Blue";
	public static String _isCorrectAns = "Red";
	public static String headerpath="";
}
