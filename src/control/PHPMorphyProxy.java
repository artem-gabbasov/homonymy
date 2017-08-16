package control;

import intf.ConsoleWriter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedList;

import newgrammar.adapters.PHPMorphyAdapter;
import newgrammar.partsofspeech.AmbiguousGrammemeException;
import newgrammar.partsofspeech.ImproperCategoryException;
import newgrammar.partsofspeech.UnknownGrammemeException;
import newgrammar.partsofspeech.UnknownPartOfSpeechException;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import control.records.PHPMorphyRecord;

//This class is intended to use phpMorphy package (http://sourceforge.net/projects/phpmorphy/) together with the dictionary from www.aot.com

public class PHPMorphyProxy {
	
	String path;
	String command;
	
	PHPMorphyAdapter adapter;
	
	public PHPMorphyProxy(String path, String fileName)
	{
		this.path = path;
		command = "php -f " + fileName + " ";
		
		adapter = new PHPMorphyAdapter();
	}
	
	public LinkedList<PHPMorphyRecord> getInfo(String wordForm) throws UnknownPartOfSpeechException, ImproperCategoryException, UnknownGrammemeException, AmbiguousGrammemeException
	{
		InputStream inputStream;

		try {
			Process proc = Runtime.getRuntime().exec(command + wordForm, null, new File(path));
			inputStream = proc.getInputStream();

			proc.waitFor();
			
			//byte [] bytes = new byte[10000];
			//inputStream.read(bytes);
			//System.out.println(new String(bytes));
			
			SAXBuilder sb = new SAXBuilder();
			Document doc = sb.build(inputStream);
			
			proc.destroy();
			
			return parse(doc);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public LinkedList<PHPMorphyRecord> parse(Document doc) throws UnknownPartOfSpeechException, ImproperCategoryException, UnknownGrammemeException, AmbiguousGrammemeException
	{
		LinkedList<PHPMorphyRecord> res = new LinkedList<PHPMorphyRecord>();
		
		Element wordForm = doc.getRootElement().getChild("wordForm");
		
		if (wordForm != null)
		{
			Iterator<Element> variants = wordForm.getChildren("variant").iterator();
			while (variants.hasNext())
			{
				Element variant = variants.next();
				String lemma = variant.getChildText("lemma");
				
				PHPMorphyRecord record = new PHPMorphyRecord(lemma);
				
				Iterator<Element> grammars = variant.getChildren("gram").iterator();
				while (grammars.hasNext())
				{
					Element grammar = grammars.next();
					
					record.addGrammar(adapter.createInstance(grammar.getChildText("partOfSpeech"), grammar.getChildText("grammemes")));
				}
				
				res.add(record);
			}
			return res;
		}
		else
			return null;
	}
}
