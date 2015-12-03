//import statements

/**
 * @author Alex Foster, Jung Mi Lee
 *
 */

public class NaiveBayes {

// data structures
ArrayList<University> totalSet = ArrayList<University>();
ArrayList<University> trainingSet = ArrayList<University>();
ArrayList<University> testingSet = ArrayList<University>();

// constructor reads in .csv file and adds contents to data structures
public NaiveBayes(String data){
	totalSet.add(new University(data.split(',')));
	trainingSet = this.extractTrainingSet(universities);
}

// public methods

// take given classification list and compare against universities
public void learn(ArrayList<Classification> classifications){


}


// for a given attribute, ie. scholarships, we need to get number of universities that have the attribute
public double getRatio(Attribute a){
	int num = 0;
	int total = 0;
	for(University u : universities){
		total++;
		if(u.positiveAttribute(a))
			num++;
	}
	return (double) num/total;
}

public static void main(String args[]){

File file_data = File.parse(args[1]);
File file_classifications = File.parse(args[2]);
//convert file to string
String data;
String classifications;

NaiveBayes bayes = new NaiveBayes(data, classifications);
// learn the training set (~10% of data)
bayes.learn();
bayes.classify();

}

}
