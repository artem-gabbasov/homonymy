package newgrammar;

public class GrammarCodes {

	int category;
	int value;
	
	public GrammarCodes(int category, int value)
	{
		super();
		this.category = category;
		this.value = value;
	}

	public int getCategory() {
		return category;
	}

	public int getValue() {
		return value;
	}

	public void setCategory(int category) {
		this.category = category;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public void orValueWith(int value) {
		this.value |= value;
	}
}
