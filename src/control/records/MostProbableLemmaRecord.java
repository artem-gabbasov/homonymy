package control.records;

public class MostProbableLemmaRecord {
	
	int lemmaId;
	boolean isUnique;
	
	public MostProbableLemmaRecord(int lemmaId, boolean isUnique) {
		super();
		this.lemmaId = lemmaId;
		this.isUnique = isUnique;
	}

	public int getLemmaId() {
		return lemmaId;
	}

	public boolean isUnique() {
		return isUnique;
	}

	public void setLemmaId(int lemmaId) {
		this.lemmaId = lemmaId;
	}

	public void setUnique(boolean isUnique) {
		this.isUnique = isUnique;
	}
}
