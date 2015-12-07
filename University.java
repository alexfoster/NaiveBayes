//import statements
import java.util.*;
import java.util.ArrayList;

public class University{

	private String universityName;
	private ArrayList<Attribute> attributeList;
	private Classification classification;

	public University(String[] nameAndAttributes){
		attributeList = new ArrayList<Attribute>();
		this.universityName = nameAndAttributes[0].trim();
		for(int i=1; i<NaiveBayes.ATTRIBUTE_NUM+1; i++){
			this.attributeList.add(new Attribute(i, nameAndAttributes[i]));
		}
	}

	public void setClassification(String s){
		this.classification = new Classification(s);
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

}
