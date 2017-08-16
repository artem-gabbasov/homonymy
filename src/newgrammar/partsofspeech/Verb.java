package newgrammar.partsofspeech;

import newgrammar.GrammaticalInstance;

public class Verb extends GrammaticalInstance {

	public Verb()
	{
		super();
		instantiate(new String[] {"Aspect", "Mood", "Reflexivity", "Transitivity", "Tense", "Person", "Gender", "Number"});
		
		omit = "Voice";
	}
	
	protected String className()
	{
		return "Verb";
	}
}
