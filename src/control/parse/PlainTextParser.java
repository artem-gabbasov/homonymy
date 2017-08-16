package control.parse;

import java.sql.SQLException;

import lexis.WordForm;
import control.dispatch.RuleFinder;

public class PlainTextParser {

	public String parse(String source, RuleFinder finder) throws SQLException
	{
		RecognitionObserver observer = new RecognitionObserver();
		finder.setObserver(observer);
		
		String[] words = source.split("[^А-Яа-я]");

		String s = new String(source);
		for (int i = 0; i < words.length; i++)
		{
			if (!words[i].isEmpty())
			{
				int ix = s.indexOf(words[i]);
				
				observer.addSpace(s.substring(0, ix));
				s = s.substring(ix + words[i].length());
				
				finder.dispatch(new WordForm(words[i], null, null));
				//res += observer.getToken();
			}
		}
		
		finder.clearLists();
		
		observer.addSpace(s);
		
		return observer.getResult();
	}
}
