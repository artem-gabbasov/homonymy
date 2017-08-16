package control.rules;

import java.util.LinkedList;

import control.records.IDContextRecord;

public class ContextsGroup {

	LinkedList<IDContextRecord> records;
	
	public ContextsGroup()
	{
		records = new LinkedList<IDContextRecord>();
	}
	
	public void add(IDContextRecord record)
	{
		records.add(record);
	}
}
