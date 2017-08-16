package newgrammar.partsofspeech;

@SuppressWarnings("serial")
public class UnknownGrammemeException extends GrammarException {

	public UnknownGrammemeException(String category)
	{
		super("Unknown category: " + category);
	}
}
