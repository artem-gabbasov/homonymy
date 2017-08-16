package control.parse;

import java.util.Iterator;
import java.util.LinkedList;

public class RecognitionObserver extends Observer {

	String result = "";
	
	LinkedList<String> spaces = new LinkedList<String>();
	
	@Override
	public void observe(String predictedLemma, String realLemma,
			String wordForm, double weight) {
		// TODO Auto-generated method stub
		result += spaces.removeFirst() + wordForm + "{" + predictedLemma/* + ", " + weight*/ + "}";
	}

	public void addSpace(String space/*, boolean concat*/)
	{
		/*if (concat && !spaces.isEmpty())
		{
			spaces.add(spaces.removeLast().concat(space));
		} else
		{*/
			spaces.add(space);
		//}
	}
	
	public String getResult()
	{
		while (!spaces.isEmpty())
		{
			result += spaces.removeFirst();
		}
		
		return result;
	}
}
