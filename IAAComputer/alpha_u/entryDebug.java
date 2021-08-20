/**   
* @Title: Entry_ComputeIAA.java
* @Package IAAComputer.alpha_u
* @Description: Entry of the whole program
* @author DU YANG
* @date 20th Dec 2021
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



public class entryDebug {
	
	/**  oriPath is used for show data path of the project. */
	//final static String  oriPath = "./annotated_result_CHtemp/";
	//final static String  oriPath = "./annotated_result_CHtemp/";
	final static String  oriPath = "./newOurs/";
	/**  oriPath is to define the Max Number of the annotator  */
	final static int  MAX_ANNOTATOR_NUMBER = 6;// is 5
	
	public static void main(String args[]) throws IOException
	{
		String oriString = "T2	Premise "
				+ "122 316;317 425;426 544;545 618;619 731;732 785	problems started with a phone call enquiry where i was simply referred to the website for all information - the guy on "
				+ "the phone repeatedly referred me to the website with each question i asked. then there was a misquote on the price - a substantial sum but given lack of availability we had to take it. front desk staff were clearly annoyed with having to field innocent enquiries, did not make for a pleasant atmosphere. staff let themselves into the room late at night while room was occupied. then at check out there was an extended delay as they claimed we had not paid , which we had done so in advance. on "
				+ "discovering that we had we were offered no apology";
		String oriSt2 = "T3	Major_claim 787 803;804 779	NOT recommended!"; 
		System.out.println("Begins");
		System.out.println(AnnotatorFileHandle.handleSemicolonMark(oriSt2));
	}
	


	

}



 
