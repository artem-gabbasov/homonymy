package control.dispatch;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedList;

import control.records.ShortContextRecord;

import lexis.WordForm;
import dao.MyDAO;

public abstract class ContextDispatcher {

	MyDAO dao;
	LinkedList<Integer> prevElements;
	LinkedList<ShortContextRecord> prevContexts;
	int prevContextLength;
	int postContextLength;
	
	protected ContextDispatcher(MyDAO dao)
	{
		this.dao = dao;
	
		prevElements = new LinkedList<Integer>();
		prevContexts = new LinkedList<ShortContextRecord>();
		
		try {
			prevContextLength = dao.getPrevContextLength();
			postContextLength = dao.getPostContextLength();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void dispatch(WordForm wordForm) throws SQLException
	{
		if (!wordForm.getTxt().isEmpty())
		{
			wordForm.toLowerCase();
			
			int elementId = register(wordForm);
					
			ShortContextRecord record = new ShortContextRecord(elementId);
			
			registerFurther(record, wordForm);
			
			updateLists(record, elementId);
			
			if (prevContexts.size() > postContextLength)
				react();
		}
	}
	
	protected void updateLists(ShortContextRecord record, int elementId) throws SQLException
	{
		// context before
		
		if (prevElements.size() >= prevContextLength)
		{
			record.recordBefore(prevElements.remove(0));
		}
		
		Iterator<Integer> elementsIterator = prevElements.iterator();
		
		while (elementsIterator.hasNext())
		{
			record.recordBefore(elementsIterator.next());
		}
		
		prevElements.add(elementId);
		
		// context after
		
		Iterator<ShortContextRecord> contextsIterator = prevContexts.iterator();
		
		while (contextsIterator.hasNext())
		{
			contextsIterator.next().recordAfter(elementId);
		}
		
		prevContexts.add(record);
	}
	
	public void clearLists() throws SQLException
	{
		prevElements.clear();
		
		while (!prevContexts.isEmpty())
			react();
	}
	
	protected abstract int register(WordForm wordForm) throws SQLException;
	
	protected void react() throws SQLException
	{
		process(prevContexts.remove(0));
	}
	
	protected abstract void process(ShortContextRecord newRecord) throws SQLException;
	
	protected void registerFurther(ShortContextRecord record, WordForm wordForm) throws SQLException
	{}
}
