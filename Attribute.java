public class Attribute{
	int attributeType;
	double attributeValue;

	public Attribute(int attributeType, String attribute){
		this.attributeType = attributeType - 1;
		if(attribute == null || attribute.equals("NULL")){
			this.attributeValue = -1.0;
		}
		else{
			try{
				this.attributeValue = Double.parseDouble(attribute.trim());
			}catch(Exception e){
				System.out.println("failed to double parse attribute string : "+attribute);
			}
		}
	}

	public String toString(){
		return this.attributeType+": "+this.attributeValue;
	}

	public double getValue(){
		return this.attributeValue;
	}
}
