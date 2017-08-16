package intf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.StringTokenizer;

import control.PHPMorphyProxy;
import control.dispatch.RuleFinder;
import control.parse.PlainTextParser;
import control.rules.Rule;
import control.rules.RuleBuilder;
import dao.MyDAO;

public class Recognition {

	public static void main(String[] args) {
		
		if (args.length == 2)
		{
			try {
				byte[] input = new FileReader().readFile(args[0]);
				
				if (input != null)
				{
					File outputFile = new File(args[1]);
					FileOutputStream fos = new FileOutputStream(outputFile);

					fos.write(recognize(new String(input)).getBytes());

		            fos.close();
				}
				//System.out.println(parser.parse("", finder));
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				System.err.println("File '" + args[1] + "' not found");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else
		{
			System.out.println("Usage: Recognition <inputfile> <outputfile>");
		}
		
/*		if (args.length > 0)
		{
			MyDAO dao = new MyDAO(false, false);
			
			String text = new String(new FileReader().readFile(args[0]));
			
			StringTokenizer sentences = new StringTokenizer(text, ".!?");
			
			while (sentences.hasMoreTokens())
			{
				StringTokenizer words = new StringTokenizer(sentences.nextToken(), " \n\f\r");
				while (words.hasMoreTokens())
				{
					String word = words.nextToken();
					
					// store
					
					try {
						int wordId = dao.getWordFormId(word);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					// do something if there is no such a word in the DB 
					
				}
			}
		}
		else System.out.println("No file given!");*/
		
		/*try {
			dao.findVariants();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int count = 0;
		try {
			while (dao.nextVariants() != null)
				count++;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(count);*/
	}
	
	public static String recognize(String input) throws SQLException
	{
		PlainTextParser parser = new PlainTextParser();
		
		RuleFinder finder = new RuleFinder(new MyDAO(false, false), new PHPMorphyProxy("/home/ralf/eclipse/homonymy", "intf.php"));
		
		return parser.parse(input, finder);
	}
}
