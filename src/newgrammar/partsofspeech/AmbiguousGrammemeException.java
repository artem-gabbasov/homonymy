package newgrammar.partsofspeech;

@SuppressWarnings("serial")
public class AmbiguousGrammemeException extends GrammarException {

	public AmbiguousGrammemeException(String grammaticalInstance, String grammar, String grammeme)
	{
		super("Ambiguous grammeme \"" + grammeme + "\" in the grammar string \"" + grammar + "\" for \"" + grammaticalInstance);
	}
}
