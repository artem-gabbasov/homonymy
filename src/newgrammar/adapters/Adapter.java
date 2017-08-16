package newgrammar.adapters;

import java.util.HashMap;

import newgrammar.GrammaticalInstance;
import newgrammar.GrammarCodes;
import newgrammar.partsofspeech.AmbiguousGrammemeException;
import newgrammar.partsofspeech.Consts;
import newgrammar.partsofspeech.ImproperCategoryException;
import newgrammar.partsofspeech.UnknownGrammemeException;
import newgrammar.partsofspeech.UnknownPartOfSpeechException;

public abstract class Adapter {

	protected Category[] categories;
	protected String unused;
	protected String[] others;
	
	public Adapter() {}
	
	public abstract GrammaticalInstance createInstance(String part, String grammar) throws UnknownPartOfSpeechException, ImproperCategoryException, UnknownGrammemeException, AmbiguousGrammemeException;
	
	protected void parseGrammar(GrammaticalInstance instance, String grammar) throws ImproperCategoryException, UnknownGrammemeException, AmbiguousGrammemeException
	{
		//System.out.println(grammar);
		
		String[] tokens = grammar.split("[\\s,]");
		for (int i = 0; i < tokens.length; i++)
		{
			if (!tokens[i].isEmpty())
			{
				GrammarCodes codes = getCodes(tokens[i]);
				
				if (codes != null)
				{
					if (codes.getCategory() == Consts.CATEGORY_OTHERS)
					{
						instance.setProperty("Others", codes.getValue());
					} else
					{
						if (instance.setProperty(categories[codes.getCategory()].getKey(), codes.getValue()) != Consts.DEFAULT)
							throw new AmbiguousGrammemeException(tokens[i], grammar, instance.toString());
					}
				}
			}
		}
	}
	
	protected GrammarCodes getCodes(String token) throws UnknownGrammemeException
	{
		String tmpToken = "," + token + ",";
		
		for (int i = 0; i < categories.length; i++)
		{
			String[] values = categories[i].getValues();
			for (int j = 0; j < values.length; j++)
			{
				if (values[j].contains(tmpToken))
					return new GrammarCodes(i, j);
			}
		}
		
		if (unused.contains(tmpToken))
			return null;
		
		int value = 0;
		
		for (int i = 0; i < others.length; i++)
		{
			if (others[i].contains(tmpToken))
			{
				value |= (1 << i);
			}
		}

		if (value > 0)
		{
			return new GrammarCodes(Consts.CATEGORY_OTHERS, value);
		}
		
		throw new UnknownGrammemeException(token);
	}
	
	//protected abstract int getValue(String category, String token); 
}
