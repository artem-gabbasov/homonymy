package control.parse;

import intf.ConsoleWriter;
import intf.Logger;

import java.io.File;
import java.io.IOException;

import newgrammar.partsofspeech.GrammarException;

import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.postgresql.core.Encoding;

import control.dispatch.ContextDispatcher;
import control.dispatch.DBSaver;

public abstract class Parser {
	
	public void parse(String filename, ContextDispatcher dispatcher) throws JDOMException, IOException
	{
		parse(filename, dispatcher, null);
	}
	
	public void parse(String filename, ContextDispatcher dispatcher, Logger exceptionsLogger) throws JDOMException, IOException
	{
		SAXBuilder sb = new SAXBuilder();
		Document doc = sb.build(new File(filename));
		
		ConsoleWriter writer = new ConsoleWriter();
		writer.start("File '" + filename + "' ");
		
		try
		{
			doParse(doc, dispatcher, writer);
		} catch (GrammarException e)
		{
			if (exceptionsLogger != null)
			{
				exceptionsLogger.log(e.getMessage());
			}
			else
			{
				e.printStackTrace();
			}
		}
		
		writer.finish("has been processed");
	}
	
	protected abstract void doParse(Document doc, ContextDispatcher dispatcher, ConsoleWriter writer) throws GrammarException;
}
