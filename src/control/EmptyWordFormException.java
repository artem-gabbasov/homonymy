package control;

@SuppressWarnings("serial")
public class EmptyWordFormException extends Exception {

	public EmptyWordFormException()
	{
		super("Empty wordform");
	}
}
