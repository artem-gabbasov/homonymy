package lexis;

import newgrammar.GrammaticalInstance;

public class WordForm {

	String txt;
	String lemma;
	GrammaticalInstance gram;
	//String specific;
	
	public WordForm(String txt, String lemma, GrammaticalInstance gram/*, String specific*/)
	{
		this.txt = txt;
		this.lemma = lemma;
		
		this.gram = gram;
		
		//this.specific = specific;
	}
	
	public void toLowerCase()
	{
		if (txt != null) txt = txt.toLowerCase();
		if (lemma != null) lemma = lemma.toLowerCase();
		//gram.toLowerCase();
		//if (specific != null) specific = specific.toLowerCase();
	}

	public String getTxt() {
		return txt;
	}

	public String getLemma() {
		return lemma;
	}

	public GrammaticalInstance getGram() {
		return gram;
	}

	/*public String getSpecific() {
		return specific;
	}*/

	public void setTxt(String txt) {
		this.txt = txt;
	}

	public void setLemma(String lemma) {
		this.lemma = lemma;
	}

	public void setGram(GrammaticalInstance gram) {
		this.gram = gram;
	}

	/*public void setSpecific(String specific) {
		this.specific = specific;
	}*/
}
