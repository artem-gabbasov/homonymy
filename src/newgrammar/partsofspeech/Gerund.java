package newgrammar.partsofspeech;

import newgrammar.GrammaticalInstance;

public class Gerund extends GrammaticalInstance {

	public Gerund()
	{
		super();
		instantiate(new String[] {"Aspect", "Transitivity", "Tense", "Reflexivity"});
		
		omit = "Voice";
	}
	
	protected String className()
	{
		return "Gerund";
	}
}
