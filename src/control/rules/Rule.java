package control.rules;

import control.records.IDContextRecord;

public class Rule {

	int wordFormId;
	IDContextRecord context;
	int lemmaId;
	double weight;
	
	public Rule(int wordFormId, IDContextRecord context, int lemmaId, double weight)
	{
		this.wordFormId = wordFormId;
		this.context = context;
		this.lemmaId = lemmaId;
		this.weight = weight;
	}

	public int getWordFormId() {
		return wordFormId;
	}

	public IDContextRecord getContext() {
		return context;
	}

	public int getLemmaId() {
		return lemmaId;
	}

	public double getWeight() {
		return weight;
	}
}
