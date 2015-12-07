//import statements
import java.util.*;
import java.util.ArrayList;
import java.io.*;
import java.io.BufferedReader;

/**
 * @author Alex Foster, Jung Mi Lee
 *
 */

public class NaiveBayes {

// constants
public static int ATTRIBUTE_NUM = 10; 
public static int STATS_NUM = 3;
// attribute indices
public int ADM_RATE = 1;
public int TUITION_LOW_INCOME = 2;
public int TUITION = 3;
public int PCT_PELL = 4;
public int COMPLETION_RATE = 5;
public int PCT_FEDERAL_LOAN = 6;
public int DEFAULT_RATE_3_YEARS = 7;
public int GRAD_DEBT = 8;
public int MEDIAN_FAM_INCOME = 9;
public int MEDIAN_GRAD_EARNINGS = 10;

public int FIRST_QUARTILE = 0;
public int MEDIAN = 1;
public int THIRD_QUARTILE = 2;

public static double[][] attributeStatsList;

// data structures
Hashtable<String, University> totalMap = new Hashtable<String, University>();
ArrayList<University> trainingSet = new ArrayList<University>();
ArrayList<University> testingSet = new ArrayList<University>();

public NaiveBayes(){
	//totalMap.add(new University(data.split(',')));
	//trainingSet = this.extractTrainingSet(universities);
}

// take given classification list and compare against universities
public void learn(ArrayList<Classification> classifications){

Classification c;
University u;
//bayesMemory.add(c, u);

}

public void classify(){

}

// for a given attribute, ie. scholarships, we need to get number of universities that have the attribute
/*public double getRatio(int attributeIndex){
	int num = 0;
	int total = 0;
	for(University u : universities){
		total++;
		if(u.attributeLikelihood(a))
			num++;
	}
	return (double) num/total;
}*/

// load totalMap with all our universities with their 10 attributes and 1 classification each
public void csvToUniversities(String attributeFile, String classificationFile) throws Exception{
	BufferedReader brAttributes = null;
	BufferedReader brClassifications = null;
	String line = "";
	String cvsSplitBy = ",";
	StringBuilder universitiesString = new StringBuilder();
	
	brAttributes = new BufferedReader(new FileReader(attributeFile));
	int i=0;	
	boolean foundIt = false;
	brAttributes.readLine();								// skip column names
	try{
	while ((line = brAttributes.readLine()) != null) {
		// get row of 1 uni name and 10 attributes
		if(line.isEmpty() || line == null)
			System.out.println("empty line at : "+i);
		String[] rowString = line.split(cvsSplitBy);
		// ensure our pre-processing is error-free
		if(rowString.length != ATTRIBUTE_NUM+1){	
			System.out.println("csv parse in load attributesList row: "+i+" , length: "+rowString.length +
							" content: "+printArray(rowString));
			throw new CsvParseException();
		}
		University university = new University(rowString);
		if(totalMap.containsKey(rowString[0].trim()))
			System.out.println("repeat college at : "+rowString[0].trim());
		totalMap.put(rowString[0].trim(), university);			// put (universityName, university object)
		if(!(totalMap.containsKey(rowString[0].trim())))
			System.out.println("not successfully put in hashtable at : "+i);
		i++;
		if(totalMap.size() != i && foundIt == false){
			System.out.println("problems begin at : "+i); 
			foundIt = true;
		}
	}
	}catch(Exception e){
		System.out.println("readLine error at line : "+i);
		e.printStackTrace();
	}
	System.out.println(i+" lines parses apart from col headers, totalMap size is : "+totalMap.size());
	brAttributes.close();

	brClassifications = new BufferedReader(new FileReader(classificationFile));
	brClassifications.readLine();						// skip column names
	while((line = brClassifications.readLine()) != null) {
		String[] rowString = line.split(cvsSplitBy);
		if(rowString.length != 2){	
			System.out.println("csv parse in load classificationsList, length: "+rowString.length);
			throw new CsvParseException();
		}
		totalMap.get(rowString[0]).setClassification(rowString[1]);		
	}
	brClassifications.close();
}

// load attributeStatsList with our statistics for each attribute (1st quartile, median, 3rd quartile)
public void loadStatsList(String statsFile) throws Exception{
	attributeStatsList = new double[ATTRIBUTE_NUM][STATS_NUM];
	BufferedReader br = null;
	String line = "";
	String cvsSplitBy = ",";
		
	int i=0;
	br = new BufferedReader(new FileReader(statsFile));
	br.readLine(); 						// skip column names
	while ((line = br.readLine()) != null) {
		// get row of 1 uni name and 10 attributes
		String[] rowString = line.split(cvsSplitBy);
		// ensure our pre-processing is error-free
		if(rowString.length != STATS_NUM+1){
			System.out.println("csv parse in load StatsList, length: "+rowString.length);
			throw new CsvParseException();		// we ignore 0 index cause it contains row name
		}
		attributeStatsList[i][FIRST_QUARTILE] = Double.parseDouble(rowString[1]);
		attributeStatsList[i][MEDIAN] = Double.parseDouble(rowString[2]);
		attributeStatsList[i][THIRD_QUARTILE] = Double.parseDouble(rowString[3]);
		i++;	
	}
	br.close();	
}

//debugging
public String printArray(String[] array){
	String s = "";
	for(int i=0; i<array.length; i++){
		s += (array[i] + "\n");
	}
	return s;
}

//debugging
public void printDataStructures(){
	System.out.println(totalMap.size());
	System.out.println(totalMap.get("Emerson College"));
	System.out.println(totalMap.get("Eastern Nazarene College"));
	for(int i=0; i<attributeStatsList.length; i++){
		for(int j=0; j<attributeStatsList[i].length; j++){
			System.out.print(attributeStatsList[i][j]+", ");
		}
		System.out.println();
	}
}

public static void main(String args[]){

	if(args.length != 3){
		System.out.println("java NaiveBayes /data/csv/file.csv /classification/csv/file.csv " + 
						" /stats/csv/file.csv");
		return;
	}
	
	NaiveBayes bayes = new NaiveBayes();
	try{
		bayes.csvToUniversities(args[0], args[1]);
		bayes.loadStatsList(args[2]);
	}catch(CsvParseException e){
		System.out.println("Format: data row consists of University_Name|Attribute1|Attribute2|etc, "+
						"classification row consists of University|Classification");
		return;
	}catch(FileNotFoundException e){
		System.out.println("File not found.");
		return;
	}catch(IOException e){
		System.out.println("ioexception");
		return;	
	}catch(Exception e){
		System.out.println("other exception\n");
		e.printStackTrace();
	}

	bayes.printDataStructures();

	// learn the training set (~10% of data)
	//bayes.learn();
	//bayes.classify();
}

}
