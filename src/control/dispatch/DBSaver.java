package control.dispatch;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedList;

import control.records.ShortContextRecord;

import dao.MyDAO;
import lexis.WordForm;

public class DBSaver extends ContextDispatcher {

	public DBSaver(MyDAO dao)
	{
		super(dao);
	}
	
	protected int register(WordForm wordForm) throws SQLException
	{
		int wordFormId = dao.addWordForm(wordForm.getTxt());
		int lemmaId = dao.addWordForm(wordForm.getLemma());
		int propertiesId = dao.addProperties(wordForm.getGram());
		
		return dao.addElement(wordFormId, lemmaId, propertiesId);
	}

	protected void process(ShortContextRecord newRecord) throws SQLException
	{
		dao.addContext(newRecord.getElementId(), newRecord.getContextBefore(), newRecord.getContextAfter());
	}
}
