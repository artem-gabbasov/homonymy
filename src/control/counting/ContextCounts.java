package control.counting;

import java.util.LinkedList;

import control.records.IDContextRecord;
import control.records.RuleIDRecord;

public class ContextCounts {

	CountList[] contents;
	int[] sizes;
	
	final double thresholdValue = 0.3;
	
	public ContextCounts()
	{
		int arraySize = IDContextRecord.size;
		
		contents = new CountList[arraySize];

		for (int i = 0; i < arraySize; i++)
		{
			contents[i] = new CountList();
		}
		
		sizes = new int[arraySize];
	}
	
	public void addElement(int zeroBasedIndex, int elementId) // index = 0 .. contentsSize-1
	{
		sizes[zeroBasedIndex]++;
		contents[zeroBasedIndex].add(elementId);
	}

	
	public LinkedList<RuleIDRecord> records(int zeroBasedIndex)
	{
		LinkedList<RuleIDRecord> res = new LinkedList<RuleIDRecord>();
		
		contents[zeroBasedIndex].prepare((int)(thresholdValue * sizes[zeroBasedIndex]));
		
		CountRecord currentRecord;
		
		while ((currentRecord = contents[zeroBasedIndex].getNext()) != null)
		{
			res.add(new RuleIDRecord(currentRecord.id, (double)currentRecord.count / sizes[zeroBasedIndex]));
		}
		
		return res;
	}
}
