package control.parse;

import intf.ConsoleWriter;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Iterator;

import lexis.WordForm;

import newgrammar.GrammaticalInstance;
import newgrammar.adapters.RusCorporaAdapter;
import newgrammar.partsofspeech.AmbiguousGrammemeException;
import newgrammar.partsofspeech.GrammarException;
import newgrammar.partsofspeech.ImproperCategoryException;
import newgrammar.partsofspeech.UnknownGrammemeException;
import newgrammar.partsofspeech.UnknownPartOfSpeechException;

import org.jdom.Content;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Parent;
import org.jdom.Text;
import org.jdom.input.SAXBuilder;

import control.dispatch.ContextDispatcher;
import control.dispatch.DBSaver;

public class RusCorporaParser extends Parser {
	
	//int count = 0;
	
	@SuppressWarnings("unchecked")
	public void doParse(Document doc, ContextDispatcher dispatcher, ConsoleWriter writer) throws GrammarException
	{
		int total = doc.getRootElement().getChildren("se").size();
		int processed = 0;
		
		RusCorporaAdapter adapter = new RusCorporaAdapter();
		
		Iterator<Element> sentences = doc.getRootElement().getChildren("se").iterator();
		
		//RusCorporaFinder finder = new RusCorporaFinder();
		
		while (sentences.hasNext())
		{
			
			writer.printPercents((double)processed / total);
			
			//System.out.println();
			Iterator<Element> words = sentences.next().getChildren("w").iterator();
			while (words.hasNext())
			{
				//System.out.print(words.next().getText() + " ");
				
				//count++;

				Element word = words.next();

				//if (count == 15012)
					//System.out.println(word.getText());
				//{
					Element analysis = word.getChild("ana");
					
					String txt = word.getText();
					String lex = analysis.getAttributeValue("lex");
					String gr = analysis.getAttributeValue("gr");
					String joined = analysis.getAttributeValue("joined");
					
					try {
						GrammaticalInstance gram = adapter.createInstance(gr);
						dispatcher.dispatch(new WordForm(txt.replaceAll("`", ""), lex, gram/*, joined*/)); // deleting all accents (stresses)
					} catch (SQLException e) {
						e.printStackTrace();
					}
					//new GrammaticalProperties(gr, finder);
				//}
			}
			
			try {
				dispatcher.clearLists();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			processed++;
			writer.erase();
		}
		
		//System.out.println(count);
	}
}
