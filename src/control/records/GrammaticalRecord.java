package control.records;

import newgrammar.partsofspeech.Consts;

public class GrammaticalRecord {

	int partOfSpeech;
	int[] grammemes; // "Gender", "Animacy", "Number", "Case", "Brevity", "Comparison", "Aspect", "Mood", "Reflexivity", "Transitivity", "Tense", "Person", "Voice", "NumeralType", "AdjectiveType", "PronounType"
	
	/*
	 * bits:
	1 2       gender
	3         animacy
	4         number
	5 6 7 8   case
	9         brevity
	10 11     comparison
	12        aspect
	13 14     mood
	15        reflexivity
	16        transitivity
	17 18     tense
	19 20     person
	21        voice
	22        numeral type
	23 24     adjective type
	25 26     pronoun type
	*/
	
	public static final String[] categories = {"Gender", "Animacy", "Number", "Case", "Brevity", "Comparison", "Aspect", "Mood", "Reflexivity", "Transitivity", "Tense", "Person", "Voice", "NumeralType", "AdjectiveType", "PronounType"};
	public static final int[] offset = {0, 2, 3, 4, 8, 9, 11, 12, 14, 15, 16, 18, 20, 21, 22, 24};
	public static final int[] sizes = {4, 2, 2, 16, 2, 4, 2, 4, 2, 2, 4, 4, 2, 2, 4, 4};
	
	//private static final int size = categories.length;
	
	public GrammaticalRecord(int partOfSpeech)
	{
		this.partOfSpeech = partOfSpeech;
		
		grammemes = new int[categories.length];
		for (int i = 0; i< categories.length; i++)
		{
			grammemes[i] = IDContextRecord.NULL;
		}
	}

	public boolean isEmpty()
	{
		if (partOfSpeech != IDContextRecord.NULL)
			return false;
		
		for (int i = 0; i < categories.length; i++)
		{
			if (grammemes[i] != IDContextRecord.NULL)
				return false;
		}
		
		return true;
	}
	
	public int getPartOfSpeech()
	{
		return partOfSpeech;
	}

	public int getGrammeme(int zeroBasedIndex)
	{
		return grammemes[zeroBasedIndex];
	}
	
	public void setGrammeme(int zeroBasedIndex, int value)
	{
		grammemes[zeroBasedIndex] = value;
	}
}