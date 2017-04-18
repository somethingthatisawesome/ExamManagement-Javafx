package application;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.poi.hssf.util.HSSFColor.BLACK;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import bll.BLO;
import ell.Gobal;
import ell.Paragraph;
import ell.Question;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
/*
 * Kiểm tra tính đúng đắn của văn bản
 */
public class FileStatusController implements Initializable {
	private BLO blo = new BLO();
	private int qsize =0;
	private int warningsize=0;
	private String value="";
	@FXML
	private TextArea progressTextArea;
	@FXML
	private Label numWarningLabel;
	@FXML
	private Label numQuestionFoundLabel;
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
			if(Gobal.statusInfo=="")
			{
			progressTextArea.setText("Không tìm thấy lỗi trong đề thi");
			warningsize=0;
			Gobal.status = check(Gobal.paragraph);
			numQuestionFoundLabel.setText("Số câu hỏi tìm thấy: "+qsize);
			numWarningLabel.setText("Số lỗi: "+warningsize);
			Gobal.numquestion = qsize;
			Gobal.warningsize = warningsize;
			Gobal.statusInfo = value;
			}
			else
			{
				progressTextArea.setText(Gobal.statusInfo);
				numQuestionFoundLabel.setText("Số câu hỏi tìm thấy: "+Gobal.numquestion);
				numWarningLabel.setText("Số lỗi: "+Gobal.warningsize);
				
			}
	}
	private final String _NO_ANWSER="Câu hỏi không có câu trả lời";
	private final String _NO_CORRECT_ANWSER="Câu hỏi không có đáp án đúng";
	private final String _DUPLICATE_ANSWER="Đáp án bị trùng";
	private final String _BLANK_ANSWER="Đáp án rỗng";
	/*
	 * Kiểm tra những điều kiện gây sai sót
	 */
	private int check(List<Paragraph> lpr)
	{
		List<Question> lqs = blo.convertToQuestion(lpr);
		int flag=0;
		qsize = lqs.size();
		for(Question q:lqs)
		{
			if(isBlankAnswer(q)||isDuplicatedAnswer(q)||isNoAnswer(q)||isNoCorrectAnswer(q)||isDuplicatedQuestion(q, lqs))
			{
				flag = 1;
			}
		}
		return flag;
	}
	/*
	 * Đáp rỗng
	 */
	private boolean isBlankAnswer(Question q)
	{
		for(Paragraph pr:q.answers)
		{
			if(pr.value.getText()==""){
				value+="\n"+q.value.value.getText()+"\n\t"+_BLANK_ANSWER+"\n";
				System.out.println(_BLANK_ANSWER);
				
				return true;
			}
			
		}
		return false;
	}
	/*
	 * Trùng đáp án
	 */
	private boolean isDuplicatedAnswer(Question q)
	{
		int size = q.answers.size()-1;
		Paragraph pholder;
		Paragraph pholder1;
		for(int i=0;i<size;i++)
		{
			pholder = q.answers.get(i);
			String holder = pholder.value.getText();
			for(int j=i+1;j<size;j++)
			{
				pholder1 = q.answers.get(j);
				String s = pholder1.value.getText();
				if(holder.equals(s))
				{
					value+="\n"+q.value.value.getText()+"\n\t"+_DUPLICATE_ANSWER+"\n";
					blo.hightLightParagraph(pholder1,Gobal._DA_error);
					blo.hightLightParagraph(pholder,Gobal._DA_error);
					System.out.println(_DUPLICATE_ANSWER);
					return true;
				}
			}
		}
		return false;
	}
	/*
	 * Không có câu trả lời
	 */
	private boolean isNoAnswer(Question q)
	{
		if(q.answers.isEmpty()) 
			{
			value+="\n"+q.value.value.getText()+"\n\t"+_NO_ANWSER+"\n";
			System.out.println(_NO_ANWSER);

			return true;
			}
		return false;
	}
	private boolean isDuplicatedQuestion(Question q, List<Question> lq)
	{
		String sholder =q.value.value.getText();
		
		for(Question oq:lq)
		{
			if(!q.equals(oq))
			if(sholder.equals(oq.value.value.getText()))
			{
				blo.hightLightParagraph(q.value, Gobal._DQ_error);
				blo.hightLightParagraph(oq.value, Gobal._DQ_error);
				return true;
			}
		}
		return false;
	}
	/*
	 * Không có câu trả lời đúng
	 */
	private boolean isNoCorrectAnswer(Question q)
	{
		for(Paragraph pr:q.answers)
		{
			if(pr.isCorrect) 
				{
				
				return false;
				}
		}
		blo.hightLightParagraph(q.value,Gobal._NCA_error);
		value+="\n"+q.value.value.getText()+"\n\t"+_NO_CORRECT_ANWSER+"\n";
		System.out.println(_NO_CORRECT_ANWSER);
		return true;
	}
}
