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
public int ADM_RATE = 0;
public int TUITION_LOW_INCOME = 1;
public int TUITION = 2;
public int PCT_PELL = 3;
public int COMPLETION_RATE = 4;
public int PCT_FEDERAL_LOAN = 5;
public int DEFAULT_RATE_3_YEARS = 6;
public int GRAD_DEBT = 7;
public int MEDIAN_FAM_INCOME = 8;
public int MEDIAN_GRAD_EARNINGS = 9;

public int FIRST_QUARTILE = 0;
public int MEDIAN = 1;
public int THIRD_QUARTILE = 2;

public int LOW_VALUE = 0;
public int HIGH_VALUE = 1;

public static double[][] attributeStatsList;

// data structures
public Hashtable<String, University> totalMap = new Hashtable<String, University>();
public ArrayList<String> trainingSet = new ArrayList<String>();
public ArrayList<String> testingSet = new ArrayList<String>();

public ArrayList<String> highValueUniversities;
public ArrayList<String> lowValueUniversities;
public ArrayList<Integer> categoryList;

public NaiveBayes(){
	//totalMap.add(new University(data.split(',')));
	//trainingSet = this.extractTrainingSet(universities);
}

// take given classification list and compare against universities
public void learn(String universityName){

//bayesMemory.add(c, u);

}

// classify a given University object
public void classify(University unclassifiedUniversity){

// continuous attributes, must ascertain the category
this.categoryList = new ArrayList<Integer>();
this.categoryList.add(unclassifiedUniversity.getAttributeCategory(ADM_RATE));
this.categoryList.add(unclassifiedUniversity.getAttributeCategory(TUITION_LOW_INCOME));
this.categoryList.add(unclassifiedUniversity.getAttributeCategory(TUITION));
this.categoryList.add(unclassifiedUniversity.getAttributeCategory(PCT_PELL));
this.categoryList.add(unclassifiedUniversity.getAttributeCategory(COMPLETION_RATE));
this.categoryList.add(unclassifiedUniversity.getAttributeCategory(PCT_FEDERAL_LOAN));
this.categoryList.add(unclassifiedUniversity.getAttributeCategory(DEFAULT_RATE_3_YEARS));
this.categoryList.add(unclassifiedUniversity.getAttributeCategory(GRAD_DEBT));
this.categoryList.add(unclassifiedUniversity.getAttributeCategory(MEDIAN_FAM_INCOME));
this.categoryList.add(unclassifiedUniversity.getAttributeCategory(MEDIAN_GRAD_EARNINGS));

// bayes probability(HIGH_VALUE | categoryList) = P(HIGH_VALUE/ALL)*P(A1|HV)*P(A2|HV)...
double probHighValue = (highValueUniversities.size()/trainingSet.size()) * getProbability(HIGH_VALUE, ADM_RATE) *
					getProbability(HIGH_VALUE, TUITION_LOW_INCOME) * getProbability(HIGH_VALUE, TUITION) * 
					getProbability(HIGH_VALUE, PCT_PELL) * getProbability(HIGH_VALUE, COMPLETION_RATE) * 
					getProbability(HIGH_VALUE, PCT_FEDERAL_LOAN) * getProbability(HIGH_VALUE, DEFAULT_RATE_3_YEARS) * 
					getProbability(HIGH_VALUE, GRAD_DEBT) * getProbability(HIGH_VALUE, MEDIAN_FAM_INCOME) * 
					getProbability(HIGH_VALUE, MEDIAN_GRAD_EARNINGS);
double probLowValue = (lowValueUniversities.size()/trainingSet.size()) * getProbability(LOW_VALUE, ADM_RATE) *
					getProbability(LOW_VALUE, TUITION_LOW_INCOME) * getProbability(LOW_VALUE, TUITION) * 
					getProbability(LOW_VALUE, PCT_PELL) * getProbability(LOW_VALUE, COMPLETION_RATE) * 
					getProbability(LOW_VALUE, PCT_FEDERAL_LOAN) * getProbability(LOW_VALUE, DEFAULT_RATE_3_YEARS) * 
					getProbability(LOW_VALUE, GRAD_DEBT) * getProbability(LOW_VALUE, MEDIAN_FAM_INCOME) * 
					getProbability(HIGH_VALUE, MEDIAN_GRAD_EARNINGS);

System.out.println("probHighValue is: "+probHighValue+" and probLowValue is : "+probLowValue);

int classification;
if(probHighValue > probLowValue){
	classification = HIGH_VALUE;
}
else{
	classification = LOW_VALUE;
}

unclassifiedUniversity.setClassification(classification+"");

this.categoryList.clear();
}

// get P(Attribute|classification)
public double getProbability(int classification, int attribute){
	int count = 0;
	ArrayList<String> classificationUniversities;	
	if(classification == 0){
		classificationUniversities = this.lowValueUniversities;
	}
	else
		classificationUniversities = this.highValueUniversities;
	for(String s : classificationUniversities){
		if(this.totalMap.get(s).getAttributeCategory(attribute) == categoryList.get(attribute))
			count++;
	}
	return (double)(count/(classificationUniversities.size()));	
}

// get all high value universities from the training set
public void getHighValueUniversities(){
	ArrayList<String> highValue = new ArrayList<String>();
	for(String s : this.trainingSet){
		if(totalMap.get(s).getClassification().getClassification() == HIGH_VALUE){
			highValue.add(s);
		}
	}
	this.highValueUniversities = highValue;
}

// get all low value universities from the training set
public void getLowValueUniversities(){
	ArrayList<String> lowValue = new ArrayList<String>();
	for(String s : this.trainingSet){
		if(totalMap.get(s).getClassification().getClassification() == LOW_VALUE){
			lowValue.add(s);
		}
	}
	this.lowValueUniversities = lowValue;
}

// randomly select 90% of totalMap to be trainingSet
public void buildTrainingSet(){
	
}


// load totalMap with all our universities with their 10 attributes and 1 classification each
public void csvToUniversities(String attributeFile, String classificationFile) throws Exception{
	BufferedReader brAttributes = null;
	BufferedReader brClassifications = null;
	String line = "";
	String cvsSplitBy = ",";
	StringBuilder universitiesString = new StringBuilder();
	
	brAttributes = new BufferedReader(new FileReader(attributeFile));
	int i=0;	
	brAttributes.readLine();								// skip column names
	try{
	while ((line = brAttributes.readLine()) != null) {
		// get row of 1 uni name and 10 attributes
		String[] rowString = line.split(cvsSplitBy);
		// ensure our pre-processing is error-free
		if(rowString.length != ATTRIBUTE_NUM+1){	
			System.out.println("csv parse in load attributesList row: "+i+" , length: "+rowString.length +
							" content: "+printArray(rowString));
			throw new CsvParseException();
		}
		University university = new University(rowString);
		totalMap.put(rowString[0].trim(), university);			// put (universityName, university object)
		i++;
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
	System.out.println(totalMap.get("George Mason University"));
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

	//bayes.printDataStructures();


	// separate two classifications
	bayes.getHighValueUniversities();
	bayes.getLowValueUniversities();
	// training set 90%, testing set 10%
	bayes.buildTrainingSet();
	// learn the training set (~80% of data)
	//bayes.classify(bayes.totalMap.get("George Mason University"));
}

}
