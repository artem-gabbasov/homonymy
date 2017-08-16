package newgrammar.partsofspeech;

import newgrammar.GrammaticalInstance;

public class Numeral extends GrammaticalInstance {

	public Numeral()
	{
		super();
		instantiate(new String[] {"NumeralType", "Case", "Number", "Gender"});
		
		omit = "AnimacyComparison";
	}
	
	protected String className()
	{
		return "Numeral";
	}
}
