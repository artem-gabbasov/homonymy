package control.parse;

public abstract class Observer {

	//public abstract void observe(int predictedLemmaId, int realLemmaId, double weight);
	public abstract void observe(String predictedLemma, String realLemma, String wordForm, double weight);
}
