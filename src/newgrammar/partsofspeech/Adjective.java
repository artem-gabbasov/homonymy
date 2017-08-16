package newgrammar.partsofspeech;

import newgrammar.GrammaticalInstance;

public class Adjective extends GrammaticalInstance {

	public Adjective()
	{
		super();
		instantiate(new String[] {"AdjectiveType", "Brevity", "Comparison", "Gender", "Number", "Case"});
		
		omit = "Animacy";
	}
	
	protected String className()
	{
		return "Adjective";
	}
}
