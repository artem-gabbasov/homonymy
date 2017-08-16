package intf;

public class ConsoleWriter {

	int oldPercentage;
	long startTime;
	int eraseCount;
	
	public ConsoleWriter()
	{
		oldPercentage = -1; // in order to print "0%"
	}
	
	public void start(String message)
	{
		startTime = System.currentTimeMillis(); // start time
		System.out.print(message);
	}
	
	public void finish(String message)
	{
		long time = (System.currentTimeMillis() - startTime) / 1000; // end time in seconds
		long seconds = time % 60;
		long minutes = (time / 60) % 60;
		long hours = time / 60 / 60;
		
		String output = message + " in ";
		if (hours > 0)
			output += hours + "h " + minutes + "m " + seconds + "s";
		
		else
		if (minutes > 0)
			output += minutes + "m " + seconds + "s";
		
		else output += seconds + "s";
		System.out.println(output);
	}
	
	public void printPercents(double percentage)
	{
		int toPrint = (int)(percentage * 100);
		if (toPrint == oldPercentage)
			eraseCount = 0; // no character should be printed
		else
		{
			oldPercentage = toPrint;
			
			System.out.print(toPrint);
			System.out.print('%');
			
			eraseCount = (toPrint > 9) ? 3 : 2; // number of characters printed
		}
	}
	
	public void printText(String text)
	{
		System.out.print(text);
		
		eraseCount = text.length(); // number of characters printed		
	}
	
	public void erase()
	{
		for (; eraseCount > 0; eraseCount--)
			System.out.print('\b');
	}
}
