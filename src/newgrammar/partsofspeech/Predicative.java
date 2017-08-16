package newgrammar.partsofspeech;

import newgrammar.GrammaticalInstance;

public class Predicative extends GrammaticalInstance {

	public Predicative()
	{
		super();
		instantiate(new String[] {"Comparison"});
	}
	
	protected String className()
	{
		return "Predicative";
	}
}
