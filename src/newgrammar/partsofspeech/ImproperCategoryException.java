package newgrammar.partsofspeech;

@SuppressWarnings("serial")
public class ImproperCategoryException extends GrammarException {

	public ImproperCategoryException(String category, String instance)
	{
		super("Improper category: " + category + " for the instance " + instance);
	}
}
