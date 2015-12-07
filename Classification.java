public class Classification{
	int classification;

	public Classification(String s){
		this.classification = Integer.parseInt(s);
	}

	public int getClassification(){
		return this.classification;
	}
	
	public String toString(){
		return "Classification: "+this.classification;
	}
}	
