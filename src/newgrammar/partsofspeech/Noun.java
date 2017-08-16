package newgrammar.partsofspeech;

import newgrammar.GrammaticalInstance;

public class Noun extends GrammaticalInstance {

	public Noun()
	{
		super();
		instantiate(new String[] {"Gender", "Animacy", "Number", "Case"});
	}
	
	protected String className()
	{
		return "Noun";
	}
}
