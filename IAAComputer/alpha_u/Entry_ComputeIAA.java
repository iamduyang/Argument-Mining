/**   
* @Title: Entry_ComputeIAA.java
* @Package IAAComputer.alpha_u
* @Description: Entry of the whole program
* @author Yang GAO  
* @date 17th Nov 2016
* @version version 1.0  
*/

package IAAComputer.alpha_u;

import org.dkpro.statistics.agreement.coding.*;
import org.dkpro.statistics.agreement.distance.NominalDistanceFunction;
import org.dkpro.statistics.agreement.unitizing.KrippendorffAlphaUnitizingAgreement;
import org.dkpro.statistics.agreement.unitizing.UnitizingAnnotationStudy;
import org.dkpro.statistics.agreement.visualization.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

import org.apache.commons.lang.ArrayUtils;



public class Entry_ComputeIAA {
	
	/**  oriPath is used for show data path of the project. */
	//final static String  oriPath = "./annotated_result_CHtemp/";
	//final static String  oriPath = "./annotated_result_CHtemp/";
	//final static String  oriPath = "./newOurs/";
	final static String  oriPath = "./mengxueDebug/";
	/**  oriPath is to define the Max Number of the annotator  */
	final static int  MAX_ANNOTATOR_NUMBER = 72;// is 5
	
	public static void main(String args[]) throws IOException
	{
		//alphaUExample();
		//example();
		obstainiaa_and_sort();
		
		
		System.out.println("Work Done!");

	}
	


	/** 
	* @Title: obstainiaa_and_sort
	* @Description:  calculate IAA , sort the results, write the sorted results into the desk
	* @param args Unused.
	* @return Nothing.
	* @author  Du
	*/
	
	public static void obstainiaa_and_sort() throws IOException
	{
		List<String> list2sort =calculateIAAviaIO();

		Collections.sort(list2sort, new SortByAgreement());

		
		String outSortString="";
		String NaNfile="";
		for (String printString: list2sort)
			if(printString.contains("NaN"))
				NaNfile+=printString+"\n";
			else 
				outSortString+=printString+"\n";
		
		//System.out.println(outSortString);
		String sortFileWritePath="./"+"sortIaa.txt";
		
		File fileSort = new File(sortFileWritePath);
		if (!fileSort.exists()) {
			fileSort.createNewFile();
			   }
		
		FileWriter fw = new FileWriter(fileSort.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(outSortString);
		bw.close();
		
		String nanFileWritePath="./"+"nanFile.txt";
		
		fileSort = new File(nanFileWritePath);
		if (!fileSort.exists()) {
			fileSort.createNewFile();
			   }
		
		fw = new FileWriter(fileSort.getAbsoluteFile());
		bw = new BufferedWriter(fw);
		bw.write(NaNfile);
		bw.close();
	}
	
	// to list all the files in the path
	
	/** 
	* @Title: calculateIAAviaIO
	* @Description:  calculate IAA , sort the results, write the sorted results into the desk
	* @param args Unused.
	* @return Nothing.
	* @author  Du
	*/
	
	public static ArrayList<String> calculateIAAviaIO() throws IOException
	{
		ArrayList<String> returnArrayList= new ArrayList<String>();
		String[] commentfiles=AnnotatorFileHandle.listFiles(oriPath);
		AnnotationData[] annDataArray= new AnnotationData[MAX_ANNOTATOR_NUMBER];
		int commentFileLength = 0;
		for(String comment_X:commentfiles){
			System.out.println("Processing: "+comment_X);
			String annotationPath = oriPath+comment_X+"/";
			String[] annfiles=AnnotatorFileHandle.listFiles(annotationPath);
			int annotatorNum = 0;
			for (String fileAnnCandiate:annfiles){
				if(fileAnnCandiate.contains(".ann"))
					annotatorNum++;
			}
			

			//System.out.println("annotator Number: "+annotatorNum);
			if(annotatorNum>1){
				for(int ii=1;ii<(annotatorNum+1);ii++){
					
					String annofilePath = annotationPath+ii+".ann";
					annDataArray[ii-1]=AnnotatorFileHandle.getAnnotatorData(annofilePath);
					//System.out.println(annofilePath);
				}
				
				commentFileLength=AnnotatorFileHandle.getFilength(annotationPath+comment_X+".txt");
				//System.out.println(commentFileLength);
				String outIAAString = IaaCalculator.iaaCalculate(annDataArray,annotatorNum,commentFileLength);
				//System.out.print(outIAAString);
				String fileWritePath=annotationPath+"IAA_"+comment_X+".txt";
				//System.out.println(fileWritePath);
				File file = new File(fileWritePath);
				if (!file.exists()) {
					    file.createNewFile();
					   }
				String finalSortString=comment_X+" "+outIAAString.split("\n")[0];
				//System.out.println(finalSortString);
				FileWriter fw = new FileWriter(file.getAbsoluteFile());
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write(outIAAString);
				bw.close();
				returnArrayList.add(finalSortString);
			}
		}  //end of for(String comment_X:commentfiles){
		return returnArrayList;
	}
	

	
	
	
	
	public static void alphaUExample()
	{
		UnitizingAnnotationStudy study = new UnitizingAnnotationStudy(3,10) ;
		
		/*
		study.addUnit(2, 5, 0, "MC");
		study.addUnit(0, 7, 1, "MC");
		study.addUnit(3, 4, 2, "MC");
		*/
		
		
		//for reviewer1
		study.addUnit(0, 5, 0, "MC");
		study.addUnit(5, 5, 0,"C");
		//for reviewer2
		study.addUnit(0, 4, 1, "MC");
		study.addUnit(5, 5, 1, "C");
		//for reviewer3
		study.addUnit(0, 10, 2, "C");
		
		
		KrippendorffAlphaUnitizingAgreement alpha_u =
			new KrippendorffAlphaUnitizingAgreement( study );
		
		System.out.println("agreement: "+alpha_u.calculateAgreement());
		System.out.println("agreement for MC: "+alpha_u.calculateCategoryAgreement("MC"));
		System.out.println("agreement for C: "+alpha_u.calculateCategoryAgreement("C"));
		
		//System.out.println("unitizing matrix : ");
		//new UnitizingMatrixPrinter().print(System.out, study, "MC", 1, 2);
	
		System.out.println("units for category MC : ");
		new UnitizingStudyPrinter().printUnitsForCategory(System.out, study, "MC", "");
		
		System.out.println("units for category C : ");
		new UnitizingStudyPrinter().printUnitsForCategory(System.out, study, "C", "");
		
		//System.out.println("continuum: ");
		//new UnitizingStudyPrinter().printContinuum(System.out, study, "");
		
		//System.out.println("units for rater: ");
		//new UnitizingStudyPrinter().printUnitsForRater(System.out, study, 0, "MC", "");
	}
	
	public static void example()
	{
		CodingAnnotationStudy study = new CodingAnnotationStudy(3);  
		
		study.addItem(1, 1, 1); study.addItem(1, 2, 2); study.addItem(2, 2, 2);   
		study.addItem(4, 4, 4); study.addItem(1, 4, 4); study.addItem(2, 2, 2); 
		study.addItem(1, 2, 3); study.addItem(3, 3, 3); study.addItem(2, 2, 2);
		
		PercentageAgreement pa = new PercentageAgreement(study);
		System.out.println("percentage agreement:" + pa.calculateAgreement());
		
		KrippendorffAlphaAgreement alpha = 
				new KrippendorffAlphaAgreement( study, new NominalDistanceFunction());
		System.out.println("observed disagreement: "+alpha.calculateObservedDisagreement()); 
		System.out.println("expected disagreement: "+alpha.calculateExpectedDisagreement()); 
		System.out.println("alpha: "+alpha.calculateAgreement());
		System.out.println("agreement for label 1: "+alpha.calculateCategoryAgreement(1));   
		System.out.println("agreement for label 2: "+alpha.calculateCategoryAgreement(2));
		System.out.println("coincidence matrix : ");
		new CoincidenceMatrixPrinter().print(System.out, study);
	}
	
	public static void sortTheResult() // from high to low
	{
		
	}
	

}

/** 
* @ClassName: SortByAgreement
* @Description:  implements Comparator， compare the third elements in a string
*/
class SortByAgreement  implements Comparator<Object> {//implements

	 public int compare(Object o1, Object o2) {
	  String s1 = (String) o1;
	  String s2 = (String) o2;
	 // s1.
	  if (Double.parseDouble(s1.split("\\s+")[2]) <Double.parseDouble(s2.split("\\s+")[2]))
		  return 1;
	  return -1;
	 }


}

 
