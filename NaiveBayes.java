//import statements
import java.util.*;
import java.util.ArrayList;
import java.io.*;
import java.io.BufferedReader;
import java.text.DecimalFormat;

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

// data structures
public Hashtable<String, University> totalMap = new Hashtable<String, University>();
public ArrayList<String> trainingSet = new ArrayList<String>();
public ArrayList<String> testingSet = new ArrayList<String>();

public ArrayList<String> highValueUniversities;
public ArrayList<String> lowValueUniversities;
public ArrayList<Integer> categoryList;

public ArrayList<Double> meanLowList;
public ArrayList<Double> meanHighList;
public ArrayList<Double> varianceLowList;
public ArrayList<Double> varianceHighList;

// results of our naive bayes here
public Hashtable<String, Integer> classificationTable;

public NaiveBayes(){
	this.classificationTable = new Hashtable<String, Integer>();
}

// classify new university with Guassian distribution
public int classify(University newUniversity){
	double highSize = ((double)highValueUniversities.size())/((double)trainingSet.size()); 
	double lowSize = ((double)lowValueUniversities.size())/((double)trainingSet.size());

	double high1 = gaussian(HIGH_VALUE, ADM_RATE, newUniversity.getAttribute(ADM_RATE));
	double high2 =	gaussian(HIGH_VALUE, TUITION_LOW_INCOME, newUniversity.getAttribute(TUITION_LOW_INCOME));
	double high3 =	gaussian(HIGH_VALUE, TUITION, newUniversity.getAttribute(TUITION));
	double high4 =	gaussian(HIGH_VALUE, PCT_PELL, newUniversity.getAttribute(PCT_PELL));
	double high5 =	gaussian(HIGH_VALUE, COMPLETION_RATE, newUniversity.getAttribute(COMPLETION_RATE));
	double high6 =	gaussian(HIGH_VALUE, PCT_FEDERAL_LOAN, newUniversity.getAttribute(PCT_FEDERAL_LOAN));
	double high7 =	gaussian(HIGH_VALUE, DEFAULT_RATE_3_YEARS, newUniversity.getAttribute(DEFAULT_RATE_3_YEARS));
	double high8 =	gaussian(HIGH_VALUE, GRAD_DEBT, newUniversity.getAttribute(GRAD_DEBT)); 
	double high9 =	gaussian(HIGH_VALUE, MEDIAN_FAM_INCOME, newUniversity.getAttribute(MEDIAN_FAM_INCOME)); 
	double high10 = gaussian(HIGH_VALUE, MEDIAN_GRAD_EARNINGS, newUniversity.getAttribute(MEDIAN_GRAD_EARNINGS)); 

	// P(high|A) = P(high)*P(A|high)
	double high = highSize * (high1*high2*high3*high4*high5*high6*high7*high8*high9*high10);
	
	double low1 = gaussian(LOW_VALUE, ADM_RATE, newUniversity.getAttribute(ADM_RATE));
	double low2 = gaussian(LOW_VALUE, TUITION_LOW_INCOME, newUniversity.getAttribute(TUITION_LOW_INCOME));
	double low3 = gaussian(LOW_VALUE, TUITION, newUniversity.getAttribute(TUITION));
	double low4 = gaussian(LOW_VALUE, PCT_PELL, newUniversity.getAttribute(PCT_PELL));
	double low5 = gaussian(LOW_VALUE, COMPLETION_RATE, newUniversity.getAttribute(COMPLETION_RATE));
	double low6 = gaussian(LOW_VALUE, PCT_FEDERAL_LOAN, newUniversity.getAttribute(PCT_FEDERAL_LOAN));
	double low7 = gaussian(LOW_VALUE, DEFAULT_RATE_3_YEARS, newUniversity.getAttribute(DEFAULT_RATE_3_YEARS));
	double low8 = gaussian(LOW_VALUE, GRAD_DEBT, newUniversity.getAttribute(GRAD_DEBT));
	double low9 = gaussian(LOW_VALUE, MEDIAN_FAM_INCOME, newUniversity.getAttribute(MEDIAN_FAM_INCOME));
	double low10 = gaussian(LOW_VALUE, MEDIAN_GRAD_EARNINGS, newUniversity.getAttribute(MEDIAN_GRAD_EARNINGS));
	
	// P(low|A) = P(low)*P(A|low)
	double low = lowSize * (low1*low2*low3*low4*low5*low6*low7*low8*low9*low10);

	if(high > low)
		return HIGH_VALUE;
	else
		return LOW_VALUE;		
	
	//System.out.println("relative size for high/low: "+highSize+"/"+lowSize);
	//System.out.println("Classified new university as : "+newUniversity.getClassification()+", high= "+high+", low= "+low);
	//System.out.println("Low: "+low1+", "+low2+", "+low3+", "+low4+", "+low5+", "+low6+", "+low7+", "+low8+", "+low9+", "+low10);
	//System.out.println("High: "+high1+", "+high2+", "+high3+", "+high4+", "+high5+", "+high6+", "+high7+", "+high8+", "+high9+
	//				", "+high10);
}

// calculate probability along normal (gaussian) distribution
public double gaussian(int classification, int index, double value){
	if(value == -1)
		return 1;
	double sampleMean = 0.00;
	double sampleVariance = 0.00;
	if(classification == HIGH_VALUE){
		sampleMean = this.meanHighList.get(index);
		sampleVariance = this.varianceHighList.get(index);
	}
	else if(classification == LOW_VALUE){
		sampleMean = this.meanLowList.get(index);
		sampleVariance = this.varianceLowList.get(index);
	}
	//System.out.println("Sample mean/variance for "+classification+" : "+sampleMean+"/"+sampleVariance);
	double exponent = -1.00 * (Math.pow((value - sampleMean), 2) / (2.00 * sampleVariance));
	double probability = (1.00 / Math.sqrt(2*Math.PI*sampleVariance))*(Math.pow(Math.E, exponent));

	return probability; 	
}

// for each attribute in training set, compute its mean and variance for use in Gaussian
public void learnTraining(){
	this.meanHighList = new ArrayList<Double>();
	this.meanLowList = new ArrayList<Double>();
	this.varianceHighList = new ArrayList<Double>();
	this.varianceLowList = new ArrayList<Double>();

	System.out.println("Calculating mean/variance...");
	// for each attribute compute mean and variance
	// if null attribute, omit
	for(int i=ADM_RATE; i<=MEDIAN_GRAD_EARNINGS; i++){
		double totalHigh = 0.00;
		double totalLow = 0.00;
		double totalVarHigh = 0.00;
		double totalVarLow = 0.00;
		double sizeHigh = (double)this.highValueUniversities.size();
		double sizeLow = (double)this.lowValueUniversities.size();

		for(String s : this.highValueUniversities){
			if(this.totalMap.get(s).getAttribute(i) != -1.0)
				totalHigh+=this.totalMap.get(s).getAttribute(i);
			else{sizeHigh--;}
		}
		for(String s : this.lowValueUniversities){
			if(this.totalMap.get(s).getAttribute(i) != -1.0)
				totalLow+=this.totalMap.get(s).getAttribute(i);
			else{sizeLow--;}
		}
		double meanHigh = totalHigh/sizeHigh;
		double meanLow = totalLow/sizeLow;

		for(String s : this.highValueUniversities){
			if(this.totalMap.get(s).getAttribute(i) != -1.0)
				totalVarHigh+=(meanHigh-this.totalMap.get(s).getAttribute(i))*(meanHigh-this.totalMap.get(s).getAttribute(i));
		}
		for(String s : this.lowValueUniversities){
			if(this.totalMap.get(s).getAttribute(i) != -1.0)
				totalVarLow+=(meanLow-this.totalMap.get(s).getAttribute(i))*(meanLow-this.totalMap.get(s).getAttribute(i));
		}
		double varianceHigh = totalVarHigh/sizeHigh;
		double varianceLow = totalVarLow/sizeLow;
				
		if(Double.isNaN(meanHigh)){ meanHigh = 0.0; }
		if(Double.isNaN(meanLow)){ meanLow = 0.0; }
		if(Double.isNaN(varianceHigh)){ varianceHigh = 0.0; }
		if(Double.isNaN(varianceLow)){ varianceLow = 0.0; }
		this.meanHighList.add(meanHigh);
		this.meanLowList.add(meanLow);
		this.varianceHighList.add(varianceHigh);
		this.varianceLowList.add(varianceLow);	
	}
	System.out.println("Mean and variance of attributes lists: ");
	DecimalFormat df = new DecimalFormat("#.###");
	for(Double d : this.meanHighList)
		System.out.print(df.format(d)+" ");
	System.out.println();
	for(Double d : this.meanLowList)
		System.out.print(df.format(d)+" ");
	System.out.println();
	for(Double d : this.varianceHighList)
		System.out.print(df.format(d)+" ");
	System.out.println();
	for(Double d : this.varianceLowList)
		System.out.print(df.format(d)+" ");
	System.out.println();
}

// get all high value universities from the training set
public void getHighValueUniversities(){
	ArrayList<String> highValue = new ArrayList<String>();
	for(String s : this.trainingSet){
		if(totalMap.get(s).getClassification() == HIGH_VALUE){
			highValue.add(s);
		}
	}
	this.highValueUniversities = highValue;
}

// get all low value universities from the training set
public void getLowValueUniversities(){
	ArrayList<String> lowValue = new ArrayList<String>();
	for(String s : this.trainingSet){
		if(totalMap.get(s).getClassification() == LOW_VALUE){
			lowValue.add(s);
		}
	}
	this.lowValueUniversities = lowValue;
}

// randomly select 70% of totalMap to be trainingSet
public void buildTrainingAndTesting(){
	ArrayList<String> highTraining = new ArrayList<String>();
	ArrayList<String> lowTraining = new ArrayList<String>();
	ArrayList<String> highTesting = new ArrayList<String>();
	ArrayList<String> lowTesting = new ArrayList<String>();

	Random random = new Random(System.currentTimeMillis());
	for(String s : this.totalMap.keySet()){
		if(this.totalMap.get(s).getClassification() == HIGH_VALUE){
			int randomInt = random.nextInt(10);
			if(randomInt >= 3)
				highTraining.add(s);
			else
				highTesting.add(s);
		}
		else if(this.totalMap.get(s).getClassification() == LOW_VALUE){
			int randomInt = random.nextInt(10);
			if(randomInt >= 3)
				lowTraining.add(s);
			else
				lowTesting.add(s);
		}
	}
	this.trainingSet.addAll(highTraining);
	this.trainingSet.addAll(lowTraining);
	this.testingSet.addAll(highTesting);
	this.testingSet.addAll(lowTesting);	

	//System.out.println("Testing set: "+this.testingSet.size());
	//System.out.println("Training set: "+this.trainingSet.size());
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
	System.out.println(i+" csv lines parsed, bayes knows: "+totalMap.size()+" universities.");
	brAttributes.close();

	brClassifications = new BufferedReader(new FileReader(classificationFile));
	brClassifications.readLine();						// skip column names
	while((line = brClassifications.readLine()) != null) {
		String[] rowString = line.split(cvsSplitBy);
		if(rowString.length != 2){	
			System.out.println("csv parse in load classificationsList, length: "+rowString.length);
			throw new CsvParseException();
		}
		totalMap.get(rowString[0]).setClassification(Integer.parseInt(rowString[1]));		
	}
	brClassifications.close();
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
	int highTraining = 0;
	int lowTraining = 0;
	int highTesting = 0;
	int lowTesting = 0;

	for(String s : this.trainingSet){
		if(this.totalMap.get(s).getClassification()==HIGH_VALUE)
			highTraining++;
		else if(this.totalMap.get(s).getClassification()==LOW_VALUE)
			lowTraining++;
	}
	
	for(String s : this.testingSet){
		if(this.totalMap.get(s).getClassification()==HIGH_VALUE)
			highTesting++;
		else if(this.totalMap.get(s).getClassification()==LOW_VALUE)
			lowTesting++;
	}

	System.out.println("high/low in training: "+highTraining+"/"+lowTraining);
	System.out.println("high/low in testing: "+highTesting+"/"+lowTesting);
	//System.out.println(totalMap.size());
	//System.out.println(totalMap.get(testingSet.get(0)));
}

public static void main(String args[]){
	ArrayList<Integer> results = new ArrayList<Integer>();
	results.add(0); results.add(0); results.add(0); results.add(0);
	if(args.length != 2){
		System.out.println("java NaiveBayes /data/csv/file.csv /classification/csv/file.csv ");
		return;
	}
	//for(int j=0; j<50; j++){			
		NaiveBayes bayes = new NaiveBayes();
		try{
			bayes.csvToUniversities(args[0], args[1]);
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

		// training set 70%, testing set 30%
		bayes.buildTrainingAndTesting();

		// separate two classifications from the training set
		bayes.getHighValueUniversities();
		bayes.getLowValueUniversities();

		//System.out.println("size of high value: "+bayes.highValueUniversities.size());
		//System.out.println("size of low value: "+bayes.lowValueUniversities.size());
		//bayes.printDataStructures();
		// learn the training set 
		bayes.learnTraining();
		System.out.println("Classifying "+bayes.testingSet.size()+" unknown universities.");
		for(int i=0; i<bayes.testingSet.size(); i++){
			// put university name, new classification
			bayes.classificationTable.put(bayes.totalMap.get(bayes.testingSet.get(i)).getName(), 
							bayes.classify(bayes.totalMap.get(bayes.testingSet.get(i))));
		}
		int truePositive = 0;
		int trueNegative = 0;
		int falsePositive = 0;
		int falseNegative = 0;
		for(String s : bayes.testingSet){
			int actual = bayes.totalMap.get(s).getClassification();
			int predicted = bayes.classificationTable.get(s);
			if(actual == 1 && predicted == 1)
				truePositive++;
			else if(actual == 1 && predicted == 0){
				falseNegative++;
				System.out.println(s);
			}
			else if(actual == 0 && predicted == 1)
				falsePositive++;
			else if(actual == 0 && predicted == 0)
				trueNegative++;
		}
		results.set(0, results.get(0)+truePositive);
		results.set(1, results.get(1)+trueNegative);
		results.set(2, results.get(2)+falsePositive);
		results.set(3, results.get(3)+falseNegative);
		System.out.println("True positive: "+truePositive+", True negative: "+trueNegative+", False positive: "+falsePositive+
						", False negative: "+falseNegative);
	//}
	//System.out.println("50 iterations true positive: "+(results.get(0)/50)+", true negative: "+(results.get(1)/50)+
	//				", falsePositive: "+(results.get(2)/50)+", falseNegative: "+(results.get(3)/50));
}

}
