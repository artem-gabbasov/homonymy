package control.records;

public class IDContextRecord extends ContextRecord{
	
	IDRecord[] contents;
	
	public static final int NULL = -1; // NULL constant
	
	public IDContextRecord()
	{
		contents = new IDRecord[size];
	}
	
	public void add(int zeroBasedIndex, IDRecord record) // index = 0 .. size-1
	{
		contents[zeroBasedIndex] = record;
	}
	
	public IDRecord getRecord(int zeroBasedIndex)
	{
		return contents[zeroBasedIndex];
	}
	
	public int getContentsLength()
	{
		return contents.length;
	}
}
