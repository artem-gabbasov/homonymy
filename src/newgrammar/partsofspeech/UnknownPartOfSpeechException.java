package newgrammar.partsofspeech;

@SuppressWarnings("serial")
public class UnknownPartOfSpeechException extends GrammarException {

	public UnknownPartOfSpeechException(String part)
	{
		super("Unknown part of speech: " + part);
	}
}
