package intf;

import java.sql.SQLException;

import control.rules.RuleBuilder;

import dao.MyDAO;

public class Variants {

	public static void main(String[] args) {
		
		MyDAO dao = new MyDAO(false, false);
		
		if (args.length > 0)
		{
			if (args[0].equals("clear"))
			{
				try {
					dao.clearRules();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			if (args.length > 1 && args[1].equals("only"))
				return;
		}
		
/*		try {
			dao.fillVariants();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/

		/*LinkedList<Rule> test = null;
		
		try {
			test = dao.getRules("банка");
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		LinkedList<Rule> test2 = test;*/
		
		RuleBuilder builder = new RuleBuilder(dao);
		
		try {
			builder.processVariants();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
}
