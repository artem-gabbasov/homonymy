package control.records;

public class RuleIDRecord {

	int id;
	double weight;
	
	public RuleIDRecord(int id, double weight)
	{
		this.id = id;
		this.weight = weight;
	}

	public int getId() {
		return id;
	}

	public double getWeight() {
		return weight;
	}
}
