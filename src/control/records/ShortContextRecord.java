package control.records;

import java.util.Stack;
import java.util.concurrent.LinkedBlockingDeque;

public class ShortContextRecord {

	int mainElement;
	Stack<Integer> contextBefore;
	LinkedBlockingDeque<Integer> contextAfter;
	int lemma;
	
	public ShortContextRecord(int elementId)
	{
		mainElement = elementId;
	
		contextBefore = new Stack<Integer>();
		contextAfter = new LinkedBlockingDeque<Integer>();
	}
	
	public void recordBefore(int elementId)
	{
		contextBefore.push(elementId);
	}
	
	public void recordAfter(int elementId)
	{
		try {
			contextAfter.put(elementId);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int getElementId() {
		return mainElement;
	}
	
	public Stack<Integer> getContextBefore() {
		Stack<Integer> temp = new Stack<Integer>();
		Object[] obj = contextBefore.toArray();
		for(int i = 0; i < obj.length; i++)
			temp.push((Integer)obj[i]);
		
		return temp;
	}

	public LinkedBlockingDeque<Integer> getContextAfter() {
		LinkedBlockingDeque<Integer> temp = new LinkedBlockingDeque<Integer>();
		Object[] obj = contextAfter.toArray();
		try {
			for(int i = 0; i < obj.length; i++)
				temp.put((Integer)obj[i]);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return temp;
	}
	
	public void setLemma(int lemma)
	{
		this.lemma = lemma;
	}
	
	public int getLemma()
	{
		return lemma;
	}
}
