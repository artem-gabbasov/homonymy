package intf;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.StringTokenizer;

import org.jdom.JDOMException;

import control.PHPMorphyProxy;
import control.dispatch.DBSaver;
import control.dispatch.RuleFinder;
import control.parse.RusCorporaParser;
import control.parse.TesterObserver;
import dao.MyDAO;

public class Tester {
	
    public static void main(String[] args) {

    	//(new GramInfo(System.getProperty("user.dir"), "intf.php")).getInfo("сливы");
    	
    	Statistics statistics = new Statistics();
    	Logger logger = new Logger("rules", false);
    	
    	TesterObserver observer = new TesterObserver(statistics, logger);
    	
    	RuleFinder finder = new RuleFinder(new MyDAO(false, false), new PHPMorphyProxy("/home/ralf/eclipse/homonymy", "intf.php"));
    	finder.setObserver(observer);    		
    	
        RusCorporaParser parser = new RusCorporaParser();
        
        Iterator<String> iterator = parseStrings(new FileReader().readFile(args[0])).iterator();
        
        while (iterator.hasNext())
        {
        	try {
				parser.parse(iterator.next(), finder);
			} catch (JDOMException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(statistics.getCorrect() + "/" + statistics.getTotal() + ", " + statistics.getPercentage() + "%");
	        logger.log(statistics.getCorrect() + "/" + statistics.getTotal() + ", " + statistics.getPercentage() + "%");
        }
        System.out.println(statistics.getCorrect() + "/" + statistics.getTotal() + ", " + statistics.getPercentage() + "%");
        logger.log(statistics.getCorrect() + "/" + statistics.getTotal() + ", " + statistics.getPercentage() + "%");
        logger.close();
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