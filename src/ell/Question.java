package ell;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
/*
 * Cấu trúc câu hỏi
 */
public class Question {
	public Paragraph value;
	public List<Paragraph> answers = new ArrayList<Paragraph>();
	public BigInteger numID;
}
