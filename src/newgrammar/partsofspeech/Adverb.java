package newgrammar.partsofspeech;

import newgrammar.GrammaticalInstance;

public class Adverb extends GrammaticalInstance {

	public Adverb()
	{
		super();
		instantiate(new String[] {/*"AdverbType", */"Comparison"});
	}
	
	protected String className()
	{
		return "Adverb";
	}
}
