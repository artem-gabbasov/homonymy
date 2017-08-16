package control.records;

import java.util.LinkedList;

import newgrammar.GrammaticalInstance;

public class PHPMorphyRecord
{
	String lemma;
	LinkedList<GrammaticalInstance> grammars;
	
	public PHPMorphyRecord(String lemma)
	{
		this.lemma = lemma;
		
		grammars = new LinkedList<GrammaticalInstance>();
	}
	
	public void addGrammar(GrammaticalInstance instance)
	{
		grammars.add(instance);
	}

	public String getLemma() {
		return lemma;
	}

	public LinkedList<GrammaticalInstance> getGrammars() {
		return grammars;
	}
}
