//import statements
import java.util.*;
import java.util.ArrayList;

public class University{

	public String universityName;
	public ArrayList<Attribute> attributeList;
	public int classification;

	public University(String[] nameAndAttributes){
		attributeList = new ArrayList<Attribute>();
		this.universityName = nameAndAttributes[0].trim();
		for(int i=1; i<NaiveBayes.ATTRIBUTE_NUM+1; i++){
			this.attributeList.add(new Attribute(i, nameAndAttributes[i]));	// indices 0-9
		}
	}
	
	public String toString(){
		String s = "";
		s += this.universityName + ": \n";
		for(Attribute a : this.attributeList){
			s+=a+"\n";
		}
		s+=this.classification+"\n";
		return s;
	}

	public String getName(){
		return this.universityName;
	}

	public double getAttribute(int index){
		return this.attributeList.get(index).getValue();
	}

	public int getClassification(){
		return this.classification;
	}

	public void setClassification(int s){
		this.classification = s;
	}
	

}
