//import statements

/**
 * @author Alex Foster, Jung Mi Lee
 *
 */

public class NaiveBayes {

// constants
int ATTRIBUTE_NUM = 10; //TODO update if necessary
// attribute indices
int SCHOLARSHIPS = 0;
int DIVERSITY = 1;
int IN_STATE = 2;

// data structures
ArrayList<University> totalSet = ArrayList<University>();
ArrayList<University> trainingSet = ArrayList<University>();
ArrayList<University> testingSet = ArrayList<University>();

// constructor reads in .csv file and adds contents to data structures
public NaiveBayes(String data){
	totalSet.add(new University(data.split(',')));
	trainingSet = this.extractTrainingSet(universities);
}

// take given classification list and compare against universities
public void learn(ArrayList<Classification> classifications){

Classification c;
University u;
bayesMemory.add(c, u);

}

public void classify(){

}

// for a given attribute, ie. scholarships, we need to get number of universities that have the attribute
public double getRatio(int attributeIndex){
	int num = 0;
	int total = 0;
	for(University u : universities){
		total++;
		if(u.attributeLikelihood(a))
			num++;
	}
	return (double) num/total;
}

public void csvToString(String file) throws Exception{
	BufferedReader br = null;
	String line = "";
	String cvsSplitBy = ",";
	StringBuilder universitiesString = new StringBuilder();
	
	br = BufferedReader(new FileReader(file));
	while ((line = br.readLine()) != null) {
		// get row
		String[] rowString = line.split(cvsSplitBy);
		// ensure our pre-processing is error-free
		if(rowString.length != ATTRIBUTE_NUM+1)
			throw new CsvParseException();
		totalSet.add(new University(rowString));
	}
}

public static void main(String args[]){

	if(args.length != 2){
		System.out.println("java NaiveBayes /data/csv/file.csv /classification/csv/file.csv");
		return;
	}
	
	NaiveBayes bayes = new NaiveBayes();
	try{
		bayes.csvToUniversities(args[1]);
		bayes.csvToUniversities(args[2]);
	}catch(CsvParseException e){
		System.out.println("Format: data row consists of University_Name|Attribute1|Attribute2|etc, "+
						"classification row consists of University|Classification";
		return;
	}catch(FileNotFoundException e){
		System.out.println("File not found.");
		return;
	}catch(IOException e){
		System.out.println("ioexception");
		return;	
	}catch(Exception e){
		System.out.println("other exception");
	}

	// learn the training set (~10% of data)
	bayes.learn();
	bayes.classify();

}

}
