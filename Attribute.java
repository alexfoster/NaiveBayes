public class Attribute{
	int attributeType;
	double attributeValue;

	public Attribute(int attributeType, String attribute){
		this.attributeType = attributeType;
		if(attribute == null || attribute.equals("NULL")){
			this.attributeValue = -1.0;
		}
		try{
			this.attributeValue = Double.parseDouble(attribute.trim());
		}catch(Exception e){
			System.out.println("failed to double parse attribute string : "+attribute);
		}
	}

	public int getAttributeCategory(){
		if(this.attributeValue == -1.0){		// we have a null attribute, ignore it
			return -1;
		}
		else if(this.attributeValue < NaiveBayes.attributeStatsList[attributeType][0]){
			return 0;
		}
		else if(this.attributeValue < NaiveBayes.attributeStatsList[attributeType][1]){
			return 1;
		}
		else if(this.attributeValue < NaiveBayes.attributeStatsList[attributeType][2]){
			return 2;
		}
		else
			return 3;
	}
}
