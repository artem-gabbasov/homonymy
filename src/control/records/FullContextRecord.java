package control.records;

import lexis.WordForm;

public class FullContextRecord extends ContextRecord{

	WordForm[] contents;
	
	public FullContextRecord()
	{
		contents = new WordForm[size];
	}
	
	public void add(int zeroBasedIndex, WordForm wordForm) // index = 0 .. size-1
	{
		contents[zeroBasedIndex] = wordForm;
	}
	
	public WordForm getWordForm(int zeroBasedIndex)
	{
		return contents[zeroBasedIndex];
	}
}
