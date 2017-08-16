package control.rules;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Stack;
import java.util.concurrent.LinkedBlockingDeque;

import newgrammar.GrammaticalInstance;

import control.EmptyWordFormException;
import control.PHPMorphyProxy;
import control.records.ContextRecord;
import control.records.GrammaticalRecord;
import control.records.IDContextRecord;
import control.records.IDRecord;
import control.records.PHPMorphyRecord;
import control.records.RuleIDRecord;
import control.records.ShortContextRecord;
import dao.MyDAO;

public class RuleChecker {

	MyDAO dao;
	PHPMorphyProxy proxy;
	
	public RuleChecker(MyDAO dao, PHPMorphyProxy proxy)
	{
		this.dao = dao;
		this.proxy = proxy;
	}
	
	public LinkedList<RuleIDRecord> check(ShortContextRecord record, LinkedList<Rule> rules)
	{
		Iterator<Rule> iterator = rules.iterator();
		
		double weight = 0;
		int currentLemma = 0;
		LinkedList<RuleIDRecord> weights = new LinkedList<RuleIDRecord>();
		
		while (iterator.hasNext())
		{
			Rule rule = iterator.next();

			if (currentLemma == 0)
				currentLemma = rule.getLemmaId();
			
			if (rule.getLemmaId() != currentLemma)
			{
				weights.add(new RuleIDRecord(currentLemma, weight));
				currentLemma = rule.getLemmaId();
				weight = 0;
			}
			
			IDContextRecord ruleContext = rule.getContext();
			
			Stack<Integer> contextBefore = record.getContextBefore();
			
			boolean flag = true;
			
			for (int i = ContextRecord.sizeBefore - 1; i >= 0 && flag; i--)
			{
				if (contextBefore.isEmpty())
				{
					if (!ruleContext.getRecord(i).isEmpty())
						flag = false;
				}
				else
				{
					if (!match(ruleContext.getRecord(i), contextBefore.pop()))
						flag = false;
				}
			}
			
			LinkedBlockingDeque<Integer> contextAfter = record.getContextAfter();
			
			for (int i = ContextRecord.sizeBefore; i < ContextRecord.size && flag; i++)
			{
				if (contextAfter.isEmpty())
				{
					if (!ruleContext.getRecord(i).isEmpty())
						flag = false;
				}
				else
				{
					if (!match(ruleContext.getRecord(i), contextAfter.poll()))
						flag = false;
				}
			}
			
			if (flag)
				weight += rule.getWeight();
		}

		return weights;
	}
	
	protected boolean match(IDRecord ruleRecord, int contextWordFormId)
	{
		if (contextWordFormId <= 0)
		{
			(new EmptyWordFormException()).printStackTrace();
			return false;
		}
		
		if (ruleRecord.isEmpty())
			return true;
		
		if (ruleRecord.getWordFormId() >= 0 && ruleRecord.getWordFormId() != contextWordFormId)
			return false;
		
		GrammaticalRecord ruleGrammar = ruleRecord.getProperties();

		if (ruleGrammar.isEmpty())
			return true;
		
		LinkedList<PHPMorphyRecord> info = null;
		try {
			info = proxy.getInfo(dao.getWordForm(contextWordFormId));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (info != null)
		{
			Iterator<PHPMorphyRecord> records = info.iterator();
			while (records.hasNext())
			{
				PHPMorphyRecord record = records.next();
				
				Iterator<GrammaticalInstance> grammars = record.getGrammars().iterator();
				
				while (grammars.hasNext())
				{
					GrammaticalInstance contextGrammar = grammars.next();
					
					if (ruleGrammar.getPartOfSpeech() == IDContextRecord.NULL || ruleGrammar.getPartOfSpeech() == contextGrammar.getNumber())
					{
						int i;
						for (i = 0; i < GrammaticalRecord.categories.length && (ruleGrammar.getGrammeme(i) == IDContextRecord.NULL || ruleGrammar.getGrammeme(i) == contextGrammar.getProperty(GrammaticalRecord.categories[i])); i++)
							;
						
						if (i >= GrammaticalRecord.categories.length) // all grammemes matched
							return true;
					}
				}
			}
		}
		
		return false;
	}
	
}
