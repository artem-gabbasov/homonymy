package newgrammar;

import java.util.HashMap;
import java.util.Iterator;

import newgrammar.partsofspeech.Consts;
import newgrammar.partsofspeech.ImproperCategoryException;

public abstract class GrammaticalInstance {

	protected HashMap<String, Integer> properties = new HashMap<String, Integer>();
	protected int others = 0;
	protected String omit = "";
	static String[] partsOfSpeech = {"Verb", "Noun", "Adjective", "Adverb", "Pronoun", "Predicative", "Numeral", "Participle", "Gerund", "Preposition", "Conjunction", "Particle", "Interjection", "Transition", "Idiom", "NonLexeme"};

	public int setProperty(String category, int value) throws ImproperCategoryException
	{
		if (category == "Others")
		{
			return (others |= value);
		}
		
		if (properties.containsKey(category))
		{
			return properties.put(category, value);
		}
		else
			{
				if (!omit.contains(category))
					throw new ImproperCategoryException(category, toString());
				else
					return Consts.DEFAULT;
			}
	}
	
	public int getProperty(String category)
	{
		Integer res = properties.get(category);
		if (res == null)
			res = Consts.DEFAULT;
		
		return res;
	}
	
	protected void instantiate(String[] categories)
	{
		for (int i = 0; i < categories.length; i++)
		{
			properties.put(categories[i], Consts.DEFAULT);
		}
	}
	
	protected abstract String className();
	
	public int getNumber()
	{
		int number = 0;
	
		String name = className();
		
		while (number < partsOfSpeech.length && !partsOfSpeech[number].equals(name))
		{
			number++;
		}
		
		return number;
	}
	
	public static GrammaticalInstance byNumber(int number)
	{
		try {
			return (GrammaticalInstance)Class.forName("newgrammar.partsofspeech." + partsOfSpeech[number]).newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public int getOthers()
	{
		return others;
	}
	
	public void setOthers(int others)
	{
		this.others = others;
	}
	
	public String toString()
	{
		String res = "[" + className();
		
		Iterator<String> keys = properties.keySet().iterator();
		
		while (keys.hasNext())
		{
			String key = keys.next();
			
			res += ", " + key + "=" + Integer.toString(properties.get(key));
		}
		
		return res + "]";
	}
}
