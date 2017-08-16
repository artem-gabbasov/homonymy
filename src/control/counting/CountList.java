package control.counting;

import java.util.LinkedList;

public class CountList {

	LinkedList<CountRecord> counts;
	public static ZeroCountRecord zero;
	
	public CountList()
	{
		counts = new LinkedList<CountRecord>();
	}
	
	public void add(int id)
	{
		CountRecord newRecord = new CountRecord(id);
		
		int index;
		if ((index = counts.indexOf(newRecord)) == -1)
			counts.add(newRecord);
		else
			(counts.get(index)).inc();
	}
	
	public void prepare(int threshold)
	{
		zero.setThreshold(threshold);
		
		while (counts.remove(zero))
			;
	}
	
	public CountRecord getNext()
	{
		if (counts.isEmpty())
			return null;
		
		return counts.removeFirst();
	}
}
