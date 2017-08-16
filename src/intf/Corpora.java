package intf;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.StringTokenizer;

import org.jdom.JDOMException;
import org.postgresql.core.Encoding;

import control.dispatch.DBSaver;
import control.parse.RusCorporaParser;

import dao.MyDAO;

public class Corpora {
	
    public static void main(String[] args) {
    	
    	if (args[0].equals("clearAll"))
			try {
				new MyDAO(true, true).clearAll();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		else
    	{
			MyDAO dao;
	    	DBSaver saver;
	    	
	    	if (args.length > 1 && args[1].equals("clearContexts"))
	    		saver = new DBSaver(dao = new MyDAO(true, true));
	    	else
	    		saver = new DBSaver(dao = new MyDAO(false, true));
	    		
	        RusCorporaParser parser = new RusCorporaParser();
	        
	        Iterator<String> iterator = parseStrings(new FileReader().readFile(args[0])).iterator();
	        
	        while (iterator.hasNext())
	        {
	        	try {
					parser.parse(iterator.next(), saver);
				} catch (JDOMException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }
	        
	        try {
				dao.insertLemmaCounts();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	
/*        try {
			dao.addWordForm("word");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
        
/*        try {
			System.out.println(dao.getWordForm(4));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
    	
    	/*try {
			(new MyDAO()).clearContexts();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
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
