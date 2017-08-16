package newgrammar.adapters;

import newgrammar.GrammaticalInstance;
import newgrammar.partsofspeech.*;

public class RusCorporaAdapter extends Adapter {

	//int count = 1;
	
	public RusCorporaAdapter()
	{
		categories = new Category[]{
				new Category("Gender", new String[] {",m,", ",f,", ",n,", ",m-f,"}),
				new Category("Animacy", new String[] {",anim,", ",inan,"}),
				new Category("Number", new String[] {",sg,", ",pl,"}),
				new Category("Case", new String[] {",nom,", ",gen,", ",dat,", ",acc,", ",ins,", ",loc,", ",gen2,", ",loc2,", ",voc,", ",acc2,", ",adnum,"}),
				new Category("Brevity", new String[] {",brev,", ",plen,"}),
				new Category("Comparison", new String[] {"", ",comp,comp2,", ",supr,"}),
				new Category("Aspect", new String[] {",pf,", ",ipf,"}),
				new Category("Mood", new String[] {",indic,", "", ",imper,imper2,", ",inf,"}),
				new Category("Reflexivity", new String[] {",med,", ""}),
				new Category("Transitivity", new String[] {",tran,", ",intr,"}),
				new Category("Tense", new String[] {",praet,", ",praes,", ",fut,"}),
				new Category("Person", new String[] {",1p,", ",2p,", ",3p,", ""}),
				new Category("Voice", new String[] {",act,", ",pass,"}),
				new Category("NumeralType", new String[] {"", ""}),
				new Category("AdjectiveType", new String[] {"", "", ""}),
//				new Category("AdverbType", new String[] {"", "", "", "", "", "", ""}),
				new Category("PronounType", new String[] {"", "", "", ""})				
		};
		
		unused = ",ger,partcp,normal,ciph,"; // ciph!!!
		
		others = new String[] {",0,", ",persn,zoon,", ",famn,", ",patrn,", ",abbr,", ",distort,obsc,", ",anom,"};
	}
	
	@Override
	public GrammaticalInstance createInstance(String part, String grammar) throws UnknownPartOfSpeechException, ImproperCategoryException, UnknownGrammemeException, AmbiguousGrammemeException
	{
		//System.out.println(count++ + " " + part + " : " + grammar);
		
		GrammaticalInstance instance;
		if (part.equals("S")) {instance = new Noun();} else
		if (part.equals("A")) {instance = new Adjective();} else
		if (part.equals("NUM"))
		{
			Numeral tmp = new Numeral();
			tmp.setProperty("NumeralType", Consts.NUMERALTYPE_CARDINAL);
			instance = tmp;
		} else
		if (part.equals("ANUM"))
		{
			Numeral tmp = new Numeral();
			tmp.setProperty("NumeralType", Consts.NUMERALTYPE_ORDINAL);
			instance = tmp;
		} else	
		if (part.equals("V"))
		{
			if (grammar.contains("partcp"))
				instance = new Participle();
			else if (grammar.contains("ger"))
					instance = new Gerund();
				else
					instance = new Verb();
		} else
		if (part.equals("ADV")) {instance = new Adverb();} else
		if (part.equals("PRAEDIC")) {instance = new Predicative();} else
		if (part.equals("PARENTH")) {instance = new Transition();} else
		if (part.equals("S-PRO"))
		{
			Pronoun tmp = new Pronoun();
			tmp.setProperty("PronounType", Consts.PRONOUNTYPE_NOUN);
			instance = tmp;
		} else
		if (part.equals("A-PRO"))
		{
			Pronoun tmp = new Pronoun();
			tmp.setProperty("PronounType", Consts.PRONOUNTYPE_ADJECTIVE);
			instance = tmp;
		} else
		if (part.equals("ADV-PRO"))
		{
			Pronoun tmp = new Pronoun();
			tmp.setProperty("PronounType", Consts.PRONOUNTYPE_ADVERB);
			instance = tmp;
		} else
		if (part.equals("PRAEDIC-PRO"))
		{
			Pronoun tmp = new Pronoun();
			tmp.setProperty("PronounType", Consts.PRONOUNTYPE_NUMERAL);
			instance = tmp;
		} else
		if (part.equals("PR")) {instance = new Preposition();} else
		if (part.equals("CONJ")) {instance = new Conjunction();} else
		if (part.equals("PART")) {instance = new Particle();} else
		if (part.equals("INTJ")) {instance = new Interjection();} else
		if (part.equals("INIT") || part.equals("NONLEX")) {instance = new NonLexeme();} else
			throw new UnknownPartOfSpeechException(part);
		
		parseGrammar(instance, grammar);
		return instance;
	}

	public GrammaticalInstance createInstance(String allInfo) throws UnknownPartOfSpeechException, ImproperCategoryException, UnknownGrammemeException, AmbiguousGrammemeException
	{
		allInfo = allInfo.replace('=', ',');
		
		int ix;
		if ((ix = allInfo.indexOf(44)) == -1) // 44 is the ASCII code for ','
		{ // there is no ','
			return createInstance(allInfo, "");
		}
		else
		{
			return createInstance(allInfo.substring(0, ix), allInfo.substring(ix + 1));			
		}
	}
}
