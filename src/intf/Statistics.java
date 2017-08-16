package intf;

public class Statistics {

	int correct;
	int total;
	
	public Statistics()
	{
		correct = 0;
		total = 0;
	}
	
	public void registerCorrect()
	{
		correct++;
		total++;
	}
	
	public void registerWrong()
	{
		total++;
	}
	
	public int getCorrect() {
		return correct;
	}

	public int getTotal() {
		return total;
	}
	
	public int getPercentage()
	{
		return (int)((double)correct / total * 100);
	}
}
