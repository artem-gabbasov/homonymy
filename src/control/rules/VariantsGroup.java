package control.rules;

import java.util.LinkedList;

public class VariantsGroup {

	int wordFormId;
	LinkedList<Integer> lemmas;
	
	public VariantsGroup(int wordFormId)
	{
		this.wordFormId = wordFormId;
		
		lemmas = new LinkedList<Integer>();
	}
	
	public void addLemma(int lemma)
	{
		lemmas.add(lemma);
	}
}
