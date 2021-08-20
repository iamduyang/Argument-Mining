/**   
* @Title: IaaCalculator.java
* @Package IAAComputer.alpha_u
* @Description: Provide methods for IAA Calculation
* @author Du  
* @date 17th Nov 2016
* @version version 1.0  
*/


package IAAComputer.alpha_u;

 

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.lang.ArrayUtils;
import org.dkpro.statistics.agreement.coding.*;
import org.dkpro.statistics.agreement.distance.NominalDistanceFunction;
import org.dkpro.statistics.agreement.unitizing.KrippendorffAlphaUnitizingAgreement;
import org.dkpro.statistics.agreement.unitizing.UnitizingAnnotationStudy;
import org.dkpro.statistics.agreement.visualization.*;
import org.dkpro.statistics.agreement.coding.*;
import org.dkpro.statistics.agreement.distance.NominalDistanceFunction;
import org.dkpro.statistics.agreement.unitizing.KrippendorffAlphaUnitizingAgreement;
import org.dkpro.statistics.agreement.unitizing.UnitizingAnnotationStudy;
import org.dkpro.statistics.agreement.visualization.*;

public class IaaCalculator 
{
	public IaaCalculator()
	{
		 
	}
	
	
	/** 
	* @Title: iaaCalculate
	* @Description:  calculate IAA using UnitizingAnnotationStudy.
	* @param args Unused.
	* @return finalPrintString a result string shows "whole IAA,IAA of MC, C , P, IP "of every comments annotated
	*/
	public static String iaaCalculate(AnnotationData[] annDataArray,int annotatorNumber,int fileCharNum)
	{
		//fileCharNum=10;
		//annotatorNumber=3;
		UnitizingAnnotationStudy study = new UnitizingAnnotationStudy(annotatorNumber,fileCharNum) ;
		
		for(int anntatorCount=0;anntatorCount < annotatorNumber;anntatorCount++){
			for (Integer[] tupleArray:annDataArray[anntatorCount].getMcList()){
				study.addUnit(tupleArray[0],(tupleArray[1]-tupleArray[0]),anntatorCount, "MC");
			}
			for (Integer[] tupleArray:annDataArray[anntatorCount].getClaimList()){
				study.addUnit(tupleArray[0],(tupleArray[1]-tupleArray[0]),anntatorCount, "C");
			}
			for (Integer[] tupleArray:annDataArray[anntatorCount].getPList()){
				study.addUnit(tupleArray[0],(tupleArray[1]-tupleArray[0]),anntatorCount, "P");
				System.out.println(tupleArray[0]+" "+(tupleArray[1]-tupleArray[0])+" "+anntatorCount+ "P");
			}
			/*
			for (Integer[] tupleArray:annDataArray[anntatorCount].getIpList()){
				study.addUnit(tupleArray[0],(tupleArray[1]-tupleArray[0]),anntatorCount, "IP");
			}*/
			
		}
		
		
		/*//for reviewer1
		study.addUnit(0, 5, 0, "MC");
		study.addUnit(5, 5, 0,"C");
		//study.addUnit(0, 10, 0, "P");
		//for reviewer2
		study.addUnit(0, 4, 1, "MC");
		study.addUnit(5, 5, 1, "C");
		//for reviewer3
		study.addUnit(0, 10, 2, "C");
		*/
		
		//////////////////////////////////////////////
		KrippendorffAlphaUnitizingAgreement alpha_u =
			new KrippendorffAlphaUnitizingAgreement( study );
		String agreementPrint="agreement: "+alpha_u.calculateAgreement();
		String agreementMCprint="agreement for MC: "+alpha_u.calculateCategoryAgreement("MC");
		String agreementCprint="agreement for C: "+alpha_u.calculateCategoryAgreement("C");
		String agreementPprint="agreement for P: "+alpha_u.calculateCategoryAgreement("P");
		//String agreementIPprint="agreement for IP: "+alpha_u.calculateCategoryAgreement("IP");
		String finalPrintString = agreementPrint+"\n"+agreementMCprint+'\n'+agreementCprint+"\n"
				+agreementPprint+"\n";
		//if only one annotated comment exists, the results will be NaN
		return finalPrintString;
		
		
	}
	
 

}
