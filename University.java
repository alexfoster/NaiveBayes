public class University{

	Classification classification;

	// example attributes
	Attribute scholarships;
	Attribute diversity_ratio;
	Attribute in_state;	


	public University(String[] attributes){
		for(int i=0; i<NaiveBayes.ATTRIBUTE_NUM; i++){
			scholarships = new Attribute(attributes[i]);
		}
	}
}
