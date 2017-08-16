package intf;

//import grammar.grammemes.additional.AdditionalNote;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.StringTokenizer;

import newgrammar.GrammarCodes;
import newgrammar.GrammaticalInstance;
import newgrammar.adapters.PHPMorphyAdapter;
import newgrammar.adapters.RusCorporaAdapter;
import newgrammar.partsofspeech.AmbiguousGrammemeException;
import newgrammar.partsofspeech.Consts;
import newgrammar.partsofspeech.ImproperCategoryException;
import newgrammar.partsofspeech.UnknownGrammemeException;
import newgrammar.partsofspeech.UnknownPartOfSpeechException;

import org.jdom.JDOMException;
import org.postgresql.core.Logger;

import control.PHPMorphyProxy;
import control.dispatch.GrammarResolver;
import control.parse.RusCorporaParser;
import control.records.PHPMorphyRecord;
import dao.MyDAO;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		RusCorporaParser parser = new RusCorporaParser();
        
    	try {
			parser.parse(/*args[0]*/"/usr/final/RusCorpora/shuffled_rnc/laws_part1.xml", null);
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*Logger grammarLogger = new Logger("grammar", false);
		Logger exceptionsLogger = new Logger("grammarExceptions", false);
		
		GrammarResolver resolver = new GrammarResolver("/home/ralf/eclipse/homonymy", "intf.php", grammarLogger, exceptionsLogger);
		
		RusCorporaParser parser = new RusCorporaParser();
        
    	try {
			parser.parse(args[0], resolver, exceptionsLogger);
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        grammarLogger.close();
        exceptionsLogger.close();*/
		
/*		PHPMorphyProxy proxy = new PHPMorphyProxy("/home/ralf/eclipse/homonymy", "intf.php");
		
		try {
			LinkedList<PHPMorphyRecord> info = proxy.getInfo("было");
			
			if (info != null)
			{
				Iterator<PHPMorphyRecord> records = info.iterator();
				while (records.hasNext())
				{
					PHPMorphyRecord record = records.next();
					
					Iterator<GrammaticalInstance> grammars = record.getGrammars().iterator();
					
					while (grammars.hasNext())
					{
						GrammaticalInstance grammar = grammars.next();
						System.out.println(grammar + " (" + record.getLemma() + ")\n");
					}
				}
			}
		} catch (UnknownPartOfSpeechException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ImproperCategoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownGrammemeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AmbiguousGrammemeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		
		/*GrammarCodes tt = new GrammarCodes(Consts.CATEGORY_OTHERS, 5);
		
		GrammarCodes cc = test(args[0]);
		
		if (cc.getCategory() == Consts.CATEGORY_OTHERS)
			tt.orValueWith(cc.getValue());
		
		System.out.println("result: " + tt.getValue());*/
		
		//GrammaticalInstance pos = GrammaticalInstance.h();
		
		//System.out.println(pos.getClass());
		
		// TODO Auto-generated method stub
		//test();
		/*try {
			new MyDAO(false, false).insertLemmaCounts();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		//AdditionalNote.instantiate();
		
		/*RusCorporaParser parser = new RusCorporaParser();
        
        Iterator<String> iterator = parseStrings(readFile(args[0])).iterator();
        
        while (iterator.hasNext())
        {
        	try {
				parser.parse(iterator.next(), null);
			} catch (JDOMException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }*/		
	}
	
	static GrammarCodes test(String t)
	{
		String tnew = "," + t + ",";
		int tt = 0;
		
		String[] arr = new String[] {",a,b,A,", ",b,A,", ",c,a,A,", ",d,A,", ",a,b,c,d,e,f,g,A,", ",f,g,A,", ",f,g,A,"};
		for (int i = 0; i < arr.length; i++)
		{
			if (arr[i].contains(t))
			{
				tt |= (1 << i);
			}
		}
		
		return new GrammarCodes(Consts.CATEGORY_OTHERS, tt);
	}
	
	private static byte[] readFile(String fileName)
	{
		try
		{
			File inputFile = new File(fileName);
			FileInputStream fis = new FileInputStream(inputFile);

			byte[] bytes = new byte[(int)inputFile.length()];
            fis.read(bytes);

            fis.close();
            
            return bytes;
		} catch (FileNotFoundException e)
		{
			System.err.println("File '" + fileName + "' not found");
		} catch (IOException e) {
            System.err.println(e.getMessage());
        }
		
		return null;
	}

	private static LinkedList<String> parseStrings(byte[] data)
	{
		LinkedList<String> res = new LinkedList<String>();
		
		StringTokenizer tokenizer = new StringTokenizer(new String(data));
		
		while (tokenizer.hasMoreTokens())
		{
			res.add(tokenizer.nextToken());
		}
		
		return res;
	}
}
