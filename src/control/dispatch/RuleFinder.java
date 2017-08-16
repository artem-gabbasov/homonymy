package control.dispatch;

import intf.Logger;
import intf.Statistics;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedList;

import control.PHPMorphyProxy;
import control.parse.Observer;
import control.records.MostProbableLemmaRecord;
import control.records.RuleIDRecord;
import control.records.ShortContextRecord;
import control.rules.Rule;
import control.rules.RuleChecker;

import dao.MyDAO;

import lexis.WordForm;

public class RuleFinder extends ContextDispatcher {

	RuleChecker checker;
	Observer observer;
	
	public RuleFinder(MyDAO dao, PHPMorphyProxy proxy) {
		super(dao);
		// TODO Auto-generated constructor stub
		
		checker = new RuleChecker(dao, proxy);
	}
	
	@Override
	protected int register(WordForm wordForm) throws SQLException {
		// TODO Auto-generated method stub
		
		return dao.addWordForm(wordForm.getTxt()); // it finds the ID of the existing WordForm
	}

	@Override
	protected void process(ShortContextRecord newRecord) throws SQLException {
		// TODO Auto-generated method stub

		LinkedList<Rule> rules = null;
		
		rules = dao.getRules(newRecord.getElementId()); // use wordFormId 
		
		RuleIDRecord res;
		MostProbableLemmaRecord mostProbable = dao.getMostProbableLemma(newRecord.getElementId());
		
		//int unique = dao.getUniqueLemma(newRecord.getElementId());
		
		/*if (unique > 0)
			res = new RuleIDRecord(unique, 0, Integer.MAX_VALUE);*/
		if (mostProbable.isUnique())
			res = new RuleIDRecord(mostProbable.getLemmaId(), Double.MAX_VALUE);
		else
		//{ // remove these braces
			if (rules == null || (res = best(checker.check(newRecord, rules))).getId() <= 0)
			{
				//res = new RuleIDRecord(dao.getMostProbableLemmaId(newRecord.getElementId()), 0, 0);
				//if (mostProbable.getLemmaId() > 0)
					res = new RuleIDRecord(mostProbable.getLemmaId(), 0);
				/*else
					res = new RuleIDRecord(newRecord.getElementId(), 0);*/
			}
			
		if (res.getId() <= 0)
			res = new RuleIDRecord(newRecord.getElementId(), 0);
	
		observer.observe(dao.getWordForm(res.getId()), dao.getWordForm(newRecord.getLemma()), dao.getWordForm(newRecord.getElementId()), res.getWeight());
		//}
	}

	protected RuleIDRecord best(LinkedList<RuleIDRecord> list)
	{
		double max = 0;
		int lemma = 0;
		
		Iterator<RuleIDRecord> iterator = list.iterator();
		
		RuleIDRecord record;
		
		while (iterator.hasNext())
		{
			record = iterator.next();
			
			if (record.getWeight() > max)
			{
				max = record.getWeight();
				lemma = record.getId();
			}
		}
		
		return new RuleIDRecord(lemma, max);
	}
	
	protected void registerFurther(ShortContextRecord record, WordForm wordForm) throws SQLException
	{
		record.setLemma(dao.addWordForm(wordForm.getLemma()));
	}
	
	public void setObserver(Observer observer)
	{
		this.observer = observer;
	}
}
