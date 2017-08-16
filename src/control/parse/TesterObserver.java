package control.parse;

import dao.MyDAO;
import intf.Logger;
import intf.Statistics;

public class TesterObserver extends Observer {

	//MyDAO dao;
	Statistics statistics;
	Logger logger;
	
	public TesterObserver(Statistics statistics, Logger logger)
	{
		//this.dao = dao;
		this.statistics = statistics;
		this.logger = logger;
	}
	
	@Override
	public void observe(String predictedLemma, String realLemma, String wordForm, double weight) {
		// TODO Auto-generated method stub
		
		if (predictedLemma.equals(realLemma))
		{
			statistics.registerCorrect();
			logger.log("Correct lemma \"" + predictedLemma + "\" for WordForm \"" + wordForm + "\". Total weight = " + weight);
		}
		else
		{
			statistics.registerWrong();
			logger.log("Wrong lemma \"" + predictedLemma + "\" for WordForm \"" + wordForm + "\" (correct lemma: " + realLemma + "). Total weight = " + weight);
		}
	}

}
