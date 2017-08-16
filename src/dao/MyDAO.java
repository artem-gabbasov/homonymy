package dao;

import java.math.BigDecimal;
import java.sql.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Stack;
import java.util.concurrent.LinkedBlockingDeque;

import newgrammar.GrammaticalInstance;
import newgrammar.adapters.Category;
import newgrammar.partsofspeech.Consts;
import newgrammar.partsofspeech.GrammarException;

import control.records.FullContextRecord;
import control.records.GrammaticalRecord;
import control.records.IDContextRecord;
import control.records.IDRecord;
import control.records.MostProbableLemmaRecord;
import control.records.WordFormRecord;
import control.rules.ContextsGroup;
import control.rules.Rule;
import control.rules.VariantsGroup;

import lexis.WordForm;

public class MyDAO {

	Connection db;
	ResultSet variants;

	public MyDAO(boolean clearContexts, boolean clearLemmaCounts)
	{
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String url = "jdbc:postgresql:homonymy";
		String username = "ralf";
		String password = "ralfbase";
		
		try {
			db = DriverManager.getConnection(url, username, password);
			restartSequences();
			if (clearLemmaCounts)
				clearLemmaCounts();
			if (clearContexts)
				clearContexts();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void restartSequences() throws SQLException
	{
		Statement st = db.createStatement();
		
		// wordforms
		
		ResultSet rs = st.executeQuery("SELECT max(id) FROM wordforms");
		rs.next();
		
		Statement ps = db.createStatement();
		ps.executeUpdate("ALTER SEQUENCE seq_wordforms RESTART WITH " + Integer.toString(rs.getInt(1) + 1));
		
		// elements
		
		rs = st.executeQuery("SELECT max(id) FROM elements");
		rs.next();

		ps.executeUpdate("ALTER SEQUENCE seq_elements RESTART WITH " + Integer.toString(rs.getInt(1) + 1));

		// properties
		
		rs = st.executeQuery("SELECT max(id) FROM properties");
		rs.next();
		
		ps.executeUpdate("ALTER SEQUENCE seq_properties RESTART WITH " + Integer.toString(rs.getInt(1) + 1));
		
		// contexts
		
		rs = st.executeQuery("SELECT max(id) FROM contexts");
		rs.next();
		
		ps.executeUpdate("ALTER SEQUENCE seq_contexts RESTART WITH " + Integer.toString(rs.getInt(1) + 1));
		
		// rules
		
		rs = st.executeQuery("SELECT max(id) FROM rules");
		rs.next();
		
		ps.executeUpdate("ALTER SEQUENCE seq_rules RESTART WITH " + Integer.toString(rs.getInt(1) + 1));

		st.close();
		rs.close();
		ps.close();
	}
	
	public void clearAll() throws SQLException
	{
		Statement ps = db.createStatement();
		ps.executeUpdate("DELETE FROM elements");
		ps.executeUpdate("DELETE FROM properties");
		ps.executeUpdate("DELETE FROM wordforms");
		
		ps.close();
	}
	
	//----------------------------LemmaCounts--------------------------------------
	
	public void clearLemmaCounts() throws SQLException
	{
		Statement ps = db.createStatement();
		ps.executeUpdate("DELETE FROM lemmacounts");
		
		ps.close();		
	}
	
	public void insertLemmaCounts() throws SQLException
	{
		Statement ps = db.createStatement();
		ps.executeUpdate("INSERT INTO lemmacounts (SELECT wfid, lemma, count(*) as cnt FROM (SELECT elements.id as elid, wordforms.id as wfid, elements.lemma FROM elements JOIN wordforms ON elements.wordform = wordforms.id) els JOIN contexts ON contexts.wordform = els.elid GROUP BY wfid, lemma)");
		
		ps.close();			
	}
	
	/*public int getUniqueLemma(int wordFormId) throws SQLException
	{
		int res = 0;
		
		PreparedStatement ps = db.prepareStatement("SELECT lemma FROM lemmacounts WHERE wordform = ?");
		ps.setInt(1, wordFormId);
		
		ResultSet rs = ps.executeQuery();
		if (rs.next())
			res = rs.getInt(1);
		
		// if there is another lemma
		if (rs.next())
			res = 0;
		
		rs.close();
		ps.close();
		
		return res;
	}*/
	
	/*public int getMostProbableLemmaId(int wordFormId) throws SQLException
	{
		int res = 0;
		
		PreparedStatement ps = db.prepareStatement("SELECT lemma FROM lemmacounts WHERE wfid = ? ORDER BY cnt DESC LIMIT 1");
		ps.setInt(1, wordFormId);
		
		ResultSet rs = ps.executeQuery();
		if (rs.next())
			res = rs.getInt(1);
		
		rs.close();
		ps.close();
		
		return res;
	}*/
	
	public MostProbableLemmaRecord getMostProbableLemma(int wordFormId) throws SQLException
	{
		int lemma = 0;
		int max = 0;
		boolean isUnique = true;
		
		PreparedStatement ps = db.prepareStatement("SELECT lemma, cnt FROM lemmacounts WHERE wfid = ?");
		ps.setInt(1, wordFormId);
		
		ResultSet rs = ps.executeQuery();
		if (rs.next())
		{
			lemma = rs.getInt(1);
			max = rs.getInt(2);
		}

		int tmp;
		while (rs.next())
		{
			isUnique = false;
			tmp = rs.getInt(2);
			if (tmp > max)
			{
				max = tmp;
				lemma = rs.getInt(1);
			}
		}
		
		rs.close();
		ps.close();
		
		return new MostProbableLemmaRecord(lemma, isUnique);
	}
	
	//----------------------------Constants----------------------------------------

	public int getPrevContextLength() throws SQLException
	{
		Statement st = db.createStatement();
		ResultSet rs = st.executeQuery("SELECT value FROM constants WHERE name = 'prev_context_length'");
		
		rs.next();
		return rs.getInt(1);
	}
	
	public int getPostContextLength() throws SQLException
	{
		Statement st = db.createStatement();
		ResultSet rs = st.executeQuery("SELECT value FROM constants WHERE name = 'post_context_length'");
		
		rs.next();
		return rs.getInt(1);
	}
	
	//----------------------------WordForms----------------------------------------
	
	public int addWordForm(String wordForm) throws SQLException
	{
		if (wordForm == null || wordForm.isEmpty())
			return 0;
		
		int id = getWordFormId(wordForm);

		if (id != 0) // if the wordform is found (it is already stored in the DB)
			return id;
		
		Statement st2 = db.createStatement();
		ResultSet rs = st2.executeQuery("SELECT nextval('seq_wordforms')");
		
		int res;
		rs.next();
		res = rs.getInt(1);
		
		PreparedStatement st = db.prepareStatement("INSERT INTO wordforms VALUES (?, ?)");
		st.setInt(1, res);
		st.setString(2, wordForm);
		
		st.executeUpdate();
		st.close();
		
		return res;
	}
	
	public int getWordFormId(String wordForm) throws SQLException // 0 means 'not found'
	{
		int res = 0;
		
		PreparedStatement st = db.prepareStatement("SELECT id FROM wordforms WHERE txt = ?");
		st.setString(1, wordForm);
		
		ResultSet rs = st.executeQuery();
		if (rs.next())
			res = rs.getInt(1);
		
		rs.close();
		st.close();
		
		return res;
	}	
	
	public String getWordForm(int id) throws SQLException
	{
		String res = null;
		
		if (id > 0)
		{
			PreparedStatement st = db.prepareStatement("SELECT txt FROM wordforms WHERE id = ?");
			st.setInt(1, id);
			
			ResultSet rs = st.executeQuery();
			if (rs.next())
				res = rs.getString(1);
			
			rs.close();
			st.close();
		}
		return res;
	}
	
	/*public void test() throws SQLException
	{
		PreparedStatement st = db.prepareStatement("SELECT txt FROM wordforms");
		
		ResultSet rs = st.executeQuery();
		while (rs.next())
			System.out.println(rs.getString(1));
		
		rs.close();
		st.close();
	}*/
	
	/*public int getUniqueLemma(int wordFormId) throws SQLException
	{
		int res = 0;
		
		PreparedStatement ps = db.prepareStatement("SELECT distinct lemma FROM elements WHERE wordform = ?");
		ps.setInt(1, wordFormId);
		
		ResultSet rs = ps.executeQuery();
		if (rs.next())
			res = rs.getInt(1);
		
		// if there is another lemma
		if (rs.next())
			res = 0;
		
		rs.close();
		ps.close();
		
		return res;
	}*/
	
	/*public int getMostProbableLemmaId(int wordFormId) throws SQLException
	{
		int res = 0;
		
		PreparedStatement ps = db.prepareStatement("SELECT lemma, count(*) as cnt FROM (SELECT id, lemma FROM elements WHERE wordform = ?) els JOIN contexts ON els.id = contexts.wordform GROUP BY lemma ORDER BY cnt DESC LIMIT 1");
		ps.setInt(1, wordFormId);
		
		ResultSet rs = ps.executeQuery();
		if (rs.next())
			res = rs.getInt(1);
		
		rs.close();
		ps.close();
		
		return res;
	}*/
	
	//---------------------------Properties----------------------------------------
	
/*	public int addProperties(String grammar, String specific) throws SQLException
	{
		PreparedStatement st1;
		
		if (specific == null)
		{
			st1 = db.prepareStatement("SELECT id FROM properties WHERE grammar = ? AND specific IS NULL");
			st1.setString(1, grammar);
		}
		else
		{
			st1 = db.prepareStatement("SELECT id FROM properties WHERE grammar = ? AND specific = ?");
			st1.setString(1, grammar);
			st1.setString(2, specific);			
		}
		
		ResultSet rs = st1.executeQuery();
		if (rs.next())
		{
			int res = rs.getInt(1);
			rs.close();
			st1.close();
			return res;
		}
		
		Statement st2 = db.createStatement();
		rs = st2.executeQuery("SELECT nextval('seq_properties')");
		
		int res;
		rs.next();
		res = rs.getInt(1);
		
		PreparedStatement st = db.prepareStatement("INSERT INTO properties VALUES (?, ?, ?)");
		st.setInt(1, res);
		st.setString(2, grammar);
		st.setString(3, specific);
		
		st.executeUpdate();
		st.close();
		
		return res;
	}*/
	
	public int addProperties(GrammaticalInstance grammar) throws SQLException
	{
		
		// coding grammar
		
		int code = 0;
		int mask = 0;
		
		for (int i = 0; i < GrammaticalRecord.categories.length; i++)
		{
			int value = grammar.getProperty(GrammaticalRecord.categories[i]);
			
			if (value != Consts.DEFAULT)
			{
				code += value << GrammaticalRecord.offset[i];
				mask += 1 << GrammaticalRecord.offset[i];
			}
		}
		
		// DB
		
		PreparedStatement st1;
		
		int partofspeech = grammar.getNumber();
		
		st1 = db.prepareStatement("SELECT id FROM properties WHERE partofspeech = ? AND code = ? AND mask = ? AND others = ?");
		st1.setInt(1, partofspeech);
		st1.setInt(2, code);
		st1.setInt(3, mask);
		st1.setInt(4, grammar.getOthers());
		
		ResultSet rs = st1.executeQuery();
		if (rs.next())
		{
			int res = rs.getInt(1);
			rs.close();
			st1.close();
			return res;
		}
		
		Statement st2 = db.createStatement();
		rs = st2.executeQuery("SELECT nextval('seq_properties')");
		
		int res;
		rs.next();
		res = rs.getInt(1);
		
		PreparedStatement st = db.prepareStatement("INSERT INTO properties VALUES (?, ?, ?, ?, ?)");
		st.setInt(1, res);
		st.setInt(2, partofspeech);
		st.setInt(3, code);
		st.setInt(4, mask);
		st.setInt(5, grammar.getOthers());
		
		st.executeUpdate();
		st.close();
		
		return res;
	}
	
	public GrammaticalRecord getProperties(int id) throws SQLException//, GrammarException
	{
		GrammaticalRecord res = null;
		
		PreparedStatement st = db.prepareStatement("SELECT partofspeech, code, mask FROM properties WHERE id = ?");
		st.setInt(1, id);
		
		ResultSet rs = st.executeQuery();
		if (rs.next())
		{
			//res = GrammaticalInstance.byNumber(rs.getInt(1));
			res = new GrammaticalRecord(rs.getInt(1));
			
			// decoding grammar
			
			int code = rs.getInt(2);
			int mask = rs.getInt(3);
			
			for (int i = 0; i < GrammaticalRecord.categories.length; i++)
			{
				code >>= GrammaticalRecord.offset[i];
				mask >>= GrammaticalRecord.offset[i];
				
				if (mask % 2 == 1) // this category was set during coding
				{
					//res.setProperty(categories[i], code % sizes[i]);
					res.setGrammeme(i, code % GrammaticalRecord.sizes[i]);
				}
			}
			
			//res.setOthers(rs.getInt(4));
		}
		
		rs.close();
		st.close();
		
		return res;
	}
	
/*	public String getProperties(int id) throws SQLException
	{
		String res = null;
		
		PreparedStatement st = db.prepareStatement("SELECT txt FROM wordforms WHERE id = ?");
		st.setInt(1, id);
		
		ResultSet rs = st.executeQuery();
		if (rs.next())
			res = rs.getString(1);
		
		rs.close();
		st.close();
		
		return res;
	}*/

	//-----------------------------Elements----------------------------------------

	public int addElement(int wordFormId, int lemmaId, int propertiesId) throws SQLException
	{
		PreparedStatement st1 = db.prepareStatement("SELECT id FROM elements WHERE wordform = ? AND lemma = ? AND properties = ?");
		st1.setInt(1, wordFormId);
		st1.setInt(2, lemmaId);
		st1.setInt(3, propertiesId);
		
		ResultSet rs = st1.executeQuery();
		if (rs.next())
		{
			int res = rs.getInt(1);
			rs.close();
			st1.close();
			return res;
		}
		
		Statement st2 = db.createStatement();
		rs = st2.executeQuery("SELECT nextval('seq_elements')");
		
		int res;
		rs.next();
		res = rs.getInt(1);
		
		PreparedStatement st = db.prepareStatement("INSERT INTO elements VALUES (?, ?, ?, ?)");
		st.setInt(1, res);
		st.setInt(2, wordFormId);
		st.setInt(3, lemmaId);
		st.setInt(4, propertiesId);
		
		st.executeUpdate();
		st.close();
		
		return res;
	}
	
	//-----------------------------Contexts----------------------------------------

	public void addContext(int elementId, Stack<Integer> contextBefore, LinkedBlockingDeque<Integer> contextAfter) throws SQLException
	{
		int[] elements = new int[6]; // 5 elements of context are stored (count from 1)
		
		for (int pos = 3; pos >= 1; pos--)
		{
			if (contextBefore.isEmpty())
				elements[pos] = 0;
			else
				elements[pos] = contextBefore.pop();
		}
		
		for (int pos = 4; pos <= 5; pos++)
		{
			if (contextAfter.isEmpty())
				elements[pos] = 0;
			else
				elements[pos] = contextAfter.poll();
		}
		
		/*String statement = "UPDATE contexts SET cnt = cnt + 1 WHERE wordform = ?";
		
		for (int i = 1; i <= 5; i++)
		{
			if (elements[i] == 0)
				statement += " AND element" + Integer.toString(i) + " IS NULL";
			else
				statement += " AND element" + Integer.toString(i) + " = ?";
		}
		
		PreparedStatement upd = db.prepareStatement(statement);
	
		upd.setInt(1, elementId);
		
		int pos = 2;
		for (int i = 1; i <= 5; i++)
		{
			if (elements[i] > 0)
				upd.setInt(pos++, elements[i]);
		}
		
		if (upd.executeUpdate() == 0) // i.e. there is no such context
		{*/
			PreparedStatement st = db.prepareStatement("INSERT INTO contexts VALUES (nextval('seq_contexts'), ?, ?, ?, ?, ?, ?)");
			
			st.setInt(1, elementId);
			
			for (int i = 1; i <= 5; i++)
			{
				if (elements[i] > 0)
					st.setInt(i + 1, elements[i]);
				else
					st.setNull(i + 1, Types.INTEGER);
			}
			
			st.executeUpdate();
			st.close();
		//}
		
		//upd.close();
	}
	
	public void clearContexts() throws SQLException
	{
		Statement st = db.createStatement();
		
		st.executeUpdate("DELETE FROM contexts");
		st.close();
	}
	
	public ContextsGroup getContexts(int wordformId, int lemmaId) throws SQLException//, GrammarException
	{
		PreparedStatement ps = db.prepareStatement("SELECT q4.wf1, q4.pr1, q4.wf2, q4.pr2, q4.wf3, q4.pr3, q4.wf4, q4.pr4, elements.wordform AS wf5, elements.properties AS pr5 FROM (SELECT elements.wordform AS wf4, elements.properties AS pr4, q3.el5, q3.wf1, q3.pr1, q3.wf2, q3.pr2, q3.wf3, q3.pr3 FROM (SELECT elements.wordform AS wf3, elements.properties AS pr3, q2.el4, q2.el5, q2.wf1, q2.pr1, q2.wf2, q2.pr2 FROM (SELECT elements.wordform AS wf2, elements.properties AS pr2, q1.el3, q1.el4, q1.el5, q1.wf1, q1.pr1 FROM (SELECT elements.wordform AS wf1, elements.properties AS pr1, gen.el2, gen.el3, gen.el4, gen.el5 FROM (SELECT contexts.element1 AS el1, contexts.element2 AS el2, contexts.element3 AS el3, contexts.element4 AS el4, contexts.element5 AS el5 FROM contexts JOIN (SELECT elword.elid FROM (SELECT elements.id AS elid, elements.lemma FROM elements JOIN wordforms ON elements.wordform = wordforms.id WHERE wordforms.id = ?) elword JOIN wordforms ON elword.lemma = wordforms.id WHERE wordforms.id = ?) el ON contexts.wordform = el.elid) gen LEFT JOIN elements ON elements.id = gen.el1) q1 LEFT JOIN elements ON elements.id = q1.el2) q2 LEFT JOIN elements ON elements.id = q2.el3) q3 LEFT JOIN elements ON elements.id = q3.el4) q4 LEFT JOIN elements ON elements.id = q4.el5");
		ps.setInt(1, wordformId);
		ps.setInt(2, lemmaId);
		
		ContextsGroup group = new ContextsGroup();
		
		ResultSet rs = ps.executeQuery();
		while (rs.next())
		{
			IDContextRecord record = new IDContextRecord();
			
			for (int i = 0; i < IDContextRecord.size; i++)
			{
				//record.add(i, new WordForm(rs.getString(i * 3 + 1), null, rs.getString(i * 3 + 2), rs.getString(i * 3 + 3)));
				record.add(i, new IDRecord(rs.getInt(i * 2 + 1), getProperties(rs.getInt(i * 2 + 2))));
			}
			
			group.add(record);
		}
		
		return group;
	}
	
	//-----------------------------Variants----------------------------------------

	/*public void fillVariants() throws SQLException
	{
		Statement st = db.createStatement();
		ResultSet rs = st.executeQuery("SELECT max(id) FROM variants");
		rs.next();
		
		Statement ps = db.createStatement();
		ps.executeUpdate("ALTER SEQUENCE seq_variants RESTART WITH " + Integer.toString(rs.getInt(1) + 1));
		
		st.executeUpdate("INSERT INTO variants (SELECT nextval('seq_variants'), elements.wordform AS w, elements.lemma AS lemma FROM (SELECT wordform FROM (SELECT wordform, count(distinct lemma) AS cnt FROM elements GROUP BY wordform) groups WHERE cnt > 1) q1 JOIN elements ON q1.wordform = elements.wordform GROUP BY w, lemma)");
		
		st.close();
		ps.close();
		rs.close();
	}
	
	public LinkedList<VariantRecord> getVariants()
	{
		LinkedList<VariantRecord> res = new LinkedList<VariantRecord>();
		
		
		
		return res;
	}
	
	public void clearVariants() throws SQLException
	{
		Statement st = db.createStatement();
		
		st.executeUpdate("DELETE FROM variants");
		st.close();
	}*/
	
	public void findVariants() throws SQLException
	{
		Statement st = db.createStatement();
		
		variants = st.executeQuery("SELECT l.w, l.lemma, r.cnt FROM (SELECT elements.wordform AS w, elements.lemma AS lemma FROM (SELECT wordform FROM (SELECT wordform, count(distinct lemma) AS cnt FROM elements GROUP BY wordform) groups WHERE cnt > 1) q1 JOIN elements ON q1.wordform = elements.wordform GROUP BY w, lemma) l JOIN (SELECT q.w, count(*) AS cnt FROM (SELECT elements.wordform AS w, elements.lemma AS lemma FROM (SELECT wordform FROM (SELECT wordform, count(distinct lemma) AS cnt FROM elements GROUP BY wordform) groups WHERE cnt > 1) q1 JOIN elements ON q1.wordform = elements.wordform GROUP BY w, lemma) q GROUP BY w)r ON l.w = r.w");
	}
	
	public VariantsGroup nextVariants() throws SQLException
	{
		if (variants.next())
		{
			//LinkedList<VariantRecord> res = new LinkedList<VariantRecord>();
			
			VariantsGroup res = new VariantsGroup(variants.getInt(1));
			res.addLemma(variants.getInt(2));
			
			//res.add(new VariantRecord(variants.getString(2), variants.getString(4)));
			int count = variants.getInt(3) - 1;
			
			while (count > 0)
			{
				variants.next();
				res.addLemma(variants.getInt(2));
				//res.add(new VariantRecord(variants.getString(2), variants.getString(4)));
				count--;
			}
				
			return res;
		}
		else
		{
			variants.close();
			return null;
		}
	}
	
	//--------------------------------Rules----------------------------------------
	
	public void clearRules() throws SQLException
	{
		Statement ps = db.createStatement();
		ps.executeUpdate("DELETE FROM rules");
		
		ps.close();		
	}
	
	public void addRule(Rule rule) throws SQLException
	{
		PreparedStatement st = db.prepareStatement("INSERT INTO rules VALUES (nextval('seq_rules'), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
		st.setInt(1, rule.getWordFormId());
		
		int grammemesSize = GrammaticalRecord.categories.length;
		
		for (int i = 0; i < IDContextRecord.size; i++)
		{
			IDRecord record = rule.getContext().getRecord(i);
			//int wordFormId = record.getWordFormId();
			if (record != null && record.getWordFormId() != 0)
				st.setInt(i + 2, record.getWordFormId());
			else
				st.setNull(i + 2, Types.INTEGER);
			
			GrammaticalRecord grammar;
			
			if (record != null && (grammar = record.getProperties()) != null)
			{
				// coding grammar
				
				int code = 0;
				int mask = 0;
				
				for (int j = 0; j < grammemesSize; j++)
				{
					int value = grammar.getGrammeme(j);
					
					if (value != Consts.DEFAULT)
					{
						code += value << GrammaticalRecord.offset[j];
						mask += 1 << GrammaticalRecord.offset[j];
					}
				}
				
				st.setInt(i * 3 + IDContextRecord.size + 4, code);
				st.setInt(i * 3 + IDContextRecord.size + 5, mask);
				st.setInt(i * 3 + IDContextRecord.size + 6, grammar.getPartOfSpeech());
			}
			else
			{
				st.setInt(i * 3 + IDContextRecord.size + 4, 0);
				st.setInt(i * 3 + IDContextRecord.size + 5, 0);
				st.setInt(i * 3 + IDContextRecord.size + 6, IDContextRecord.NULL);
			}
		}
		st.setInt(IDContextRecord.size + 2, rule.getLemmaId());
		st.setDouble(IDContextRecord.size + 3, rule.getWeight());
		
		st.executeUpdate();
		st.close();
	}
	
	public LinkedList<Rule> getRules(int wordFormId) throws SQLException
	{
		int size = IDContextRecord.size;
		int grammemesSize = GrammaticalRecord.categories.length;
		
		PreparedStatement ps = db.prepareStatement("SELECT * FROM rules WHERE wordform = ?");
		ps.setInt(1, wordFormId);		
		
		ResultSet rs = ps.executeQuery();
		if (!rs.next())
			return null;
		
		LinkedList<Rule> res = new LinkedList<Rule>();
		do
		{
			IDContextRecord context = new IDContextRecord();
			for (int i = 0; i < size; i++)
			{
				GrammaticalRecord grammar = new GrammaticalRecord(rs.getInt(i * 3 + size + 7));
				
				// decoding grammar
				
				int code = rs.getInt(i * 3 + size + 5);
				int mask = rs.getInt(i * 3 + size + 6);
				
				for (int j = 0; j < grammemesSize; j++)
				{
					code >>= GrammaticalRecord.offset[j];
					mask >>= GrammaticalRecord.offset[j];
					
					if (mask % 2 == 1) // this category was set during coding
					{
						grammar.setGrammeme(j, code % GrammaticalRecord.sizes[j]);
					}
				}
				
				context.add(i, new IDRecord(rs.getInt(i + 3), grammar));
			}
			res.add(new Rule(wordFormId, context, rs.getInt(size + 3), rs.getDouble(size + 4)));
		} while (rs.next());
		
		return res;
	}
	
	//-----------------------------------------------------------------------------
	
	public void close()
	{
		try {
			db.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
