package newgrammar.partsofspeech;

import newgrammar.GrammaticalInstance;

public class Participle extends GrammaticalInstance {

	public Participle()
	{
		super();
		instantiate(new String[] {"Voice", "Tense", "Aspect", "Transitivity", "Brevity", "Case", "Number", "Gender", "Reflexivity"});
		
		omit = "Animacy";
	}
	
	protected String className()
	{
		return "Participle";
	}
}
