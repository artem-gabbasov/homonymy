package control.records;

public class IDRecord {

	int wordFormId;
	GrammaticalRecord properties;
	
	public IDRecord(int wordFormId,	GrammaticalRecord properties)
	{
		this.wordFormId = wordFormId;
		this.properties = properties;
	}

	public boolean isEmpty()
	{
		if (wordFormId == 0 && properties.isEmpty())
			return true;
		
		return false;
	}
	
	public int getWordFormId() {
		return wordFormId;
	}

	public GrammaticalRecord getProperties() {
		return properties;
	}
}
