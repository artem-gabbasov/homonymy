package newgrammar.adapters;

import java.util.StringTokenizer;

import newgrammar.GrammarCodes;
import newgrammar.GrammaticalInstance;
import newgrammar.partsofspeech.Adjective;
import newgrammar.partsofspeech.Adverb;
import newgrammar.partsofspeech.AmbiguousGrammemeException;
import newgrammar.partsofspeech.Conjunction;
import newgrammar.partsofspeech.Consts;
import newgrammar.partsofspeech.Gerund;
import newgrammar.partsofspeech.Idiom;
import newgrammar.partsofspeech.ImproperCategoryException;
import newgrammar.partsofspeech.Interjection;
import newgrammar.partsofspeech.NonLexeme;
import newgrammar.partsofspeech.Noun;
import newgrammar.partsofspeech.Numeral;
import newgrammar.partsofspeech.Participle;
import newgrammar.partsofspeech.Particle;
import newgrammar.partsofspeech.Predicative;
import newgrammar.partsofspeech.Preposition;
import newgrammar.partsofspeech.Pronoun;
import newgrammar.partsofspeech.Transition;
import newgrammar.partsofspeech.UnknownGrammemeException;
import newgrammar.partsofspeech.UnknownPartOfSpeechException;
import newgrammar.partsofspeech.Verb;

public class PHPMorphyAdapter extends Adapter {

	public PHPMorphyAdapter()
	{
		categories = new Category[]{
				new Category("Gender", new String[] {",МР,", ",ЖР,", ",СР,", ",МР-ЖР,"}),
				new Category("Animacy", new String[] {",ОД,", ",НО,"}),
				new Category("Number", new String[] {",ЕД,", ",МН,"}),
				new Category("Case", new String[] {",ИМ,", ",РД,", ",ДТ,", ",ВН,", ",ТВ,", ",ПР,", ",2,", ",2,", ",ЗВ,", "", ""}),
				new Category("Brevity", new String[] {"", ""}),
				new Category("Comparison", new String[] {"", ",СРАВН,", ",ПРЕВ,"}),
				new Category("Aspect", new String[] {",СВ,", ",НС,"}),
				new Category("Mood", new String[] {"", "", ",ПВЛ,", ""}),
				new Category("Reflexivity", new String[] {"", ""}),
				new Category("Transitivity", new String[] {",ПЕ,", ",НП,"}),
				new Category("Tense", new String[] {",ПРШ,", ",НСТ,", ",БУД,"}),
				new Category("Person", new String[] {",1Л,", ",2Л,", ",3Л,", "БЕЗЛ"}),
				new Category("Voice", new String[] {",ДСТ,", ",СТР,"}),
				new Category("NumeralType", new String[] {"", ""}),
				new Category("AdjectiveType", new String[] {"КАЧ", "", "ПРИТЯЖ"}),
//				new Category("AdverbType", new String[] {"", "", "", "", "", "", ""}),
				new Category("PronounType", new String[] {"", "", "", ""})				
		};
		
		unused = /*check it!*/ ",ВОПР,УКАЗАТ,ДФСТ,ЛОК,ОРГ,ОПЧ,ПОЭТ,ПРОФ,ПОЛОЖ,"; // ciph!!!
		
		others = new String[] {",0,", ",ИМЯ,", ",ФАМ,", ",ОТЧ,", ",АББР,", ",ЖАРГ,РАЗГ,", ",АРХ,"};
	}
	
	@Override
	public GrammaticalInstance createInstance(String part, String grammar) throws UnknownPartOfSpeechException, ImproperCategoryException, UnknownGrammemeException, AmbiguousGrammemeException
	{
		GrammaticalInstance instance;
		if (part.equals("С")) {instance = new Noun();} else
		if (part.equals("П"))
		{
			Adjective tmp = new Adjective();
			tmp.setProperty("Brevity", Consts.BREVITY_PLENARY);
			instance = tmp;
		} else
		if (part.equals("КР_ПРИЛ"))
		{
			Adjective tmp = new Adjective();
			tmp.setProperty("Brevity", Consts.BREVITY_BREVE);
			instance = tmp;
		} else
		if (part.equals("ИНФИНИТИВ"))
		{
			Verb tmp = new Verb();
			tmp.setProperty("Mood", Consts.MOOD_INFINITIVE);
			instance = tmp;
		} else
		if (part.equals("Г")) {instance = new Verb();} else			
		if (part.equals("ДЕЕПРИЧАСТИЕ")) {instance = new Gerund();} else	
		if (part.equals("ПРИЧАСТИЕ"))
		{
			Participle tmp = new Participle();
			tmp.setProperty("Brevity", Consts.BREVITY_PLENARY);
			instance = tmp;
		} else
		if (part.equals("КР_ПРИЧАСТИЕ"))
		{
			Participle tmp = new Participle();
			tmp.setProperty("Brevity", Consts.BREVITY_BREVE);
			instance = tmp;
		} else
		if (part.equals("ЧИСЛ"))
		{
			Numeral tmp = new Numeral();
			tmp.setProperty("NumeralType", Consts.NUMERALTYPE_CARDINAL);
			instance = tmp;
		} else
		if (part.equals("ЧИСЛ-П"))
		{
			Numeral tmp = new Numeral();
			tmp.setProperty("NumeralType", Consts.NUMERALTYPE_ORDINAL);
			instance = tmp;
		} else				
		if (part.equals("МС"))
		{
			Pronoun tmp = new Pronoun();
			tmp.setProperty("PronounType", Consts.PRONOUNTYPE_NOUN);
			instance = tmp;
		} else
		if (part.equals("МС-П"))
		{
			Pronoun tmp = new Pronoun();
			tmp.setProperty("PronounType", Consts.PRONOUNTYPE_ADJECTIVE);
			instance = tmp;
		} else
		if (part.equals("МС-ПРЕДК"))
		{
			Pronoun tmp = new Pronoun();
			tmp.setProperty("PronounType", Consts.PRONOUNTYPE_NUMERAL);
			instance = tmp;
		} else			
		if (part.equals("Н")) {instance = new Adverb();} else	
		if (part.equals("ПРЕДК")) {instance = new Predicative();} else
		if (part.equals("ВВОДН")) {instance = new Transition();} else			
		if (part.equals("ПРЕДЛ")) {instance = new Preposition();} else
		if (part.equals("СОЮЗ")) {instance = new Conjunction();} else
		if (part.equals("ЧАСТ")) {instance = new Particle();} else
		if (part.equals("МЕЖД")) {instance = new Interjection();} else
		if (part.equals("ФРАЗ")) {instance = new Idiom();} else			
				throw new UnknownPartOfSpeechException(part);
		
		parseGrammar(instance, grammar);
		return instance;
	}
}
