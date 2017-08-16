package control.counting;

public class ZeroCountRecord extends CountRecord {

	int threshold;
	
	public ZeroCountRecord()
	{
		super(0);
		
		threshold = 0;
	}

	public void setThreshold(int threshold)
	{
		this.threshold = threshold;
	}
	
/*	public boolean equals(Object o)
	{
		if (o instanceof CountRecord)
		{
			if (((CountRecord)o).count == 1)
				return true;
			else
				return false;
		}
		else
			return false;
	}*/
	
	public boolean equals(Object o)
	{
		if (o instanceof CountRecord)
		{
			if (((CountRecord)o).count < threshold)
				return true;
			else
				return false;
		}
		else
			return false;
	}
}
