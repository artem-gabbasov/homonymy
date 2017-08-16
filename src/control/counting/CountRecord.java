package control.counting;

public class CountRecord {

	int id;
	int count;
	
	public CountRecord(int id)
	{
		this.id = id;
		count = 1;
	}
	
	public void inc()
	{
		count++;
	}
	
	public boolean equals(Object o)
	{
		if (o instanceof CountRecord)
		{
			return (this.id == ((CountRecord)o).id);
		}
		else
			return false;
	}
}
