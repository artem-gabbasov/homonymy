package control.rules;

import intf.ConsoleWriter;

import java.sql.SQLException;
import java.util.Iterator;

import newgrammar.partsofspeech.Consts;
import newgrammar.partsofspeech.GrammarException;

import control.counting.ContextCounts;
import control.counting.CountList;
import control.counting.ZeroCountRecord;
import control.records.GrammaticalRecord;
import control.records.IDContextRecord;
import control.records.IDRecord;
import control.records.RuleIDRecord;

import lexis.WordForm;

import dao.MyDAO;

public class RuleBuilder {

	MyDAO dao;
	
	private static final int SETMODE_WORDFORM = 0;
	private static final int SETMODE_PARTOFSPEECH = 1;
	private static final int SETMODE_GRAMMEME = 2;
	private int grammemeIndex = 0;
	
	public RuleBuilder(MyDAO dao)
	{
		this.dao = dao;
	}
	
	public void processVariants() throws SQLException
	{
		//LinkedList<VariantRecord> variants = dao.getVariants();
		
		CountList.zero = new ZeroCountRecord();
		
		dao.findVariants();
		
		VariantsGroup group;
		
		ConsoleWriter writer = new ConsoleWriter();
		int count = 1;
		
		while ((group = dao.nextVariants()) != null)
		{
			writer.printText(Integer.toString(count));
			
			int currentWordFormId = group.wordFormId;
			Iterator<Integer> iterator = group.lemmas.iterator();
			
			while (iterator.hasNext())
			{
				int currentLemma = iterator.next();
				
				createRules(currentWordFormId, currentLemma);
				
				// do something
				/*if (currentWordForm.id == 2 && currentLemma.id == 2)
				{
					for (int i = 0; i < 5; i++)
						System.out.print(contexts.records.getFirst().contents[i].getTxt() + " ");
				}*/
			}
			
			count++;
			writer.erase();
		}
	}
	
	private void createRules(int wordFormId, int lemmaId) throws SQLException
	{
		ContextsGroup contextsGroup = dao.getContexts(wordFormId, lemmaId);
		ContextCounts wordFormCounts = new ContextCounts();
		ContextCounts partsOfSpeechCounts = new ContextCounts();
		
		int grammemesSize = GrammaticalRecord.categories.length;
		
		ContextCounts[] grammemeCounts = new ContextCounts[grammemesSize];
		for (int i = 0; i < grammemesSize; i++)
			grammemeCounts[i] = new ContextCounts();
		
		Iterator<IDContextRecord> iterator = contextsGroup.records.iterator();
		
		while (iterator.hasNext())
		{
			IDContextRecord currentRecord = iterator.next();
			for (int i = 0; i < currentRecord.getContentsLength(); i++)
			{
				if (currentRecord.getRecord(i).getWordFormId() != 0)
				{
					wordFormCounts.addElement(i, currentRecord.getRecord(i).getWordFormId());
					
					GrammaticalRecord grammar = currentRecord.getRecord(i).getProperties();
					
					partsOfSpeechCounts.addElement(i, grammar.getPartOfSpeech());
					
					for (int j = 0; j < grammemesSize; j++)
					{
						if (grammar.getGrammeme(j) != IDContextRecord.NULL)
							grammemeCounts[j].addElement(i, grammar.getGrammeme(j));
					}
				}
			}
		}
		
		processCounts(wordFormId, lemmaId, wordFormCounts, SETMODE_WORDFORM);
		processCounts(wordFormId, lemmaId, partsOfSpeechCounts, SETMODE_PARTOFSPEECH);
		
		for (int i = 0; i < grammemesSize; i++)
		{
			grammemeIndex = i;
			processCounts(wordFormId, lemmaId, grammemeCounts[i], SETMODE_GRAMMEME);
		}
	}
	
	private void processCounts(int wordFormId, int lemmaId, ContextCounts counts, int setMode) throws SQLException
	{
		for (int i = 0; i < IDContextRecord.size; i++)
		{
			Iterator<RuleIDRecord> IDIterator = counts.records(i).iterator();
			
			while (IDIterator.hasNext())
			{
				RuleIDRecord record = IDIterator.next();
				
				IDContextRecord context = new IDContextRecord();
				
				int wordForm = 0;
				int partOfSpeech = IDContextRecord.NULL;
				int grammeme = IDContextRecord.NULL;
				
				if (setMode == SETMODE_WORDFORM)
				{
					wordForm = record.getId();
				} else
				
					if (setMode == SETMODE_PARTOFSPEECH)
					{
						partOfSpeech = record.getId();
					} else
						
						if (setMode == SETMODE_GRAMMEME)
						{
							grammeme = record.getId();
						}
				
				GrammaticalRecord grammar = new GrammaticalRecord(partOfSpeech);
				
				if (grammeme != IDContextRecord.NULL)
					grammar.setGrammeme(grammemeIndex, grammeme);
				
				context.add(i, new IDRecord(wordForm, grammar));
				
				dao.addRule(new Rule(wordFormId, context, lemmaId, record.getWeight()));
			}
		}		
	}
}
