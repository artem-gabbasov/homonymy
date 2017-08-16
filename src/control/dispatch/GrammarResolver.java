package control.dispatch;

import intf.Logger;

import java.io.FileOutputStream;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedList;

import newgrammar.GrammaticalInstance;
import newgrammar.partsofspeech.AmbiguousGrammemeException;
import newgrammar.partsofspeech.ImproperCategoryException;
import newgrammar.partsofspeech.UnknownGrammemeException;
import newgrammar.partsofspeech.UnknownPartOfSpeechException;

import control.PHPMorphyProxy;
import control.records.PHPMorphyRecord;
import control.records.ShortContextRecord;

import dao.MyDAO;
import lexis.WordForm;

public class GrammarResolver extends ContextDispatcher {

	PHPMorphyProxy proxy;
	Logger grammarLogger, exceptionsLogger;
	
	public GrammarResolver(String path, String fileName, Logger grammarLogger, Logger exceptionsLogger)
	{
		super(null);
		
		proxy = new PHPMorphyProxy(path, fileName);
		this.grammarLogger = grammarLogger;
		this.exceptionsLogger = exceptionsLogger;
	}
	
	protected int register(WordForm wordForm) throws SQLException
	{
		grammarLogger.log(wordForm.getTxt() + "\n" + wordForm.getGram() + "\n");
		
		LinkedList<PHPMorphyRecord> info = null;
		try {
			info = proxy.getInfo(wordForm.getTxt());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
			exceptionsLogger.log(e.getMessage());
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
					GrammaticalInstance grammar = grammars.next();
					grammarLogger.log(grammar + " (" + record.getLemma() + ")\n");
				}
			}
		}
		
		grammarLogger.log("\n");
		return 0;
	}

	protected void process(ShortContextRecord newRecord) throws SQLException
	{
	}
}
