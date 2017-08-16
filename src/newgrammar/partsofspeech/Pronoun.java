package newgrammar.partsofspeech;

import newgrammar.GrammaticalInstance;

public class Pronoun extends GrammaticalInstance {
	
	public Pronoun()
	{
		super();
		instantiate(new String[] {"PronounType", "Person", "Case", "Gender", "Number", "Comparison"});
		
		omit = "Animacy";
	}
	
	protected String className()
	{
		return "Pronoun";
	}
}
