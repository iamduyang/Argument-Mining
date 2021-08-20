/**   
* @Title: AnnotatorFileHandle.java
* @Package IAAComputer.alpha_u
* @Description:   deal with 
* 
* @author Du Yang
* @date 17th Nov 2017
* @version version 1.0  
*/

package IAAComputer.alpha_u;



import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import org.apache.commons.lang.ArrayUtils;


public class AnnotatorFileHandle 
{
	public AnnotatorFileHandle()
	{
		 
	}

	public AnnotatorFileHandle(String dirName) throws IOException
	{
		 
	}
	
	public static String [] listFiles(String filePath2list)
	{
		File file = new File(filePath2list);
		String[] textNames=null;
		if(!file.isDirectory())
		{
			System.out.println( filePath2list +" is not directory!" );
		}
		else 
		{	
			textNames = file.list();
			for( int i=0; i<textNames.length; ++i )
			{
				if( textNames[i].trim().charAt(0) == '.' ){
					//System.out.println("Start with 0: "+textNames[i]);
					textNames = (String[]) ArrayUtils.remove(textNames, i);
					i--; // which is important 
				}
					
			}
			
			//for(String printString: textNames)
				//System.out.println(textNames.length);
		}
		return textNames.clone();
		
	}

	
	public static int getFilength(String fileName) throws IOException
	{
		
		int lengthOfFile=0;
		File f = new File(fileName);

		InputStreamReader read = new InputStreamReader (new FileInputStream(f),"UTF-8");

		BufferedReader reader=new BufferedReader(read);

		String line;

		while ((line = reader.readLine()) != null) {	 
			lengthOfFile+=line.length();
		}
		reader.close();
		return lengthOfFile;

	}
	
	public static AnnotationData getAnnotatorData(String fileName) throws IOException
	{
		ArrayList<Integer[]> mcList = new ArrayList<Integer[]>();
		ArrayList<Integer[]> claimList = new ArrayList<Integer[]>();
		ArrayList<Integer[]> pList = new ArrayList<Integer[]>();
		//ArrayList<Integer[]> ipList = new ArrayList<Integer[]>();
		//System.out.println("---------------------------");
		File f = new File(fileName);

		InputStreamReader read = new InputStreamReader (new FileInputStream(f),"UTF-8");

		BufferedReader reader=new BufferedReader(read);

		String line;

		while ((line = reader.readLine()) != null) {	 
			if(line.charAt(0)=='T'){// only classification of text is required for current work.
				//String regex = ",|，|\\s+"; 
				// split by one , two or more blank space(s)
				//String lineBar=handleSemicolonMark(line);
				String lineBar=handleSemicolonMarkEnglish(line);
				
				
				String [] lineArray = lineBar.split(",|，|\\s+");
				//if( lineBar.contains("Major_Claim") ){
				if( lineBar.contains("Major_Claim") ){//Major_Claim ...  Major_claim
					//Major_claim
					Integer[] tempIntAdd={Integer.parseInt(lineArray[2]),Integer.parseInt(lineArray[3])};
					mcList.add(tempIntAdd);
					continue;
					}
				//else if(lineBar.contains("Claim")&&(!lineBar.contains("Major_Claim"))){
				else if(lineBar.contains("Claim")&&(!lineBar.contains("Major_Claim"))){	
					Integer[] tempIntAdd={Integer.parseInt(lineArray[2]),Integer.parseInt(lineArray[3])};
					claimList.add(tempIntAdd);
					continue;
					}
				else if(lineBar.contains("Premise")&&(!lineBar.contains("Implicit_Premise"))){	
					Integer[] tempIntAdd={Integer.parseInt(lineArray[2]),Integer.parseInt(lineArray[3])};
					pList.add(tempIntAdd); 
					continue;
					}
				/**else if (lineBar.contains("Implicit_Premise")){
					
					Integer[] tempIntAdd={Integer.parseInt(lineArray[2]),Integer.parseInt(lineArray[3])};
					ipList.add(tempIntAdd);
				
					}*/
				

			}
			
		}
		reader.close(); 
 
		AnnotationData annotationOfOne =new AnnotationData(mcList,claimList,pList);
		return (AnnotationData)annotationOfOne;

	}


	public static String handleSemicolonMarkEnglish(String originString)  
	{
		if( !originString.contains(";") ) 
			return originString;
		
		String outPutString="";
		String analyzedString=null;
		/*
		String reg = "[\u4e00-\u9fa5]";
        int index = -2;
        if (originString.matches (".*" + reg + ".*"))
        {
            index = originString.split(reg)[0].length ();
        }
        */
       // System.out.println("originString:"+originString);
        
   
        	analyzedString=originString ;
        
       // System.out.println("analyzedString:"+analyzedString);
        
        //System.out.println("DY debug analyzedString: "+analyzedString);
        String [] lineArray = analyzedString.split(",|，|\\s+");
        for (String Hello:lineArray)
        	if( !Hello.contains(";") ) 
        		outPutString+=Hello+" ";
         
        return outPutString;
		 
	}
	
	public static String handleSemicolonMark(String originString)  
	{
		if( !originString.contains(";") ) 
			return originString;
		
		String outPutString="";
		String analyzedString=null;
		String reg = "[\u4e00-\u9fa5]";
        int index = -2;
        if (originString.matches (".*" + reg + ".*"))
        {
            index = originString.split (reg)[0].length ();
        }
        
        if(index>0){
        	analyzedString=originString.substring(0,index);
        }
        reg="^\\d+$";// to match number
        System.out.println("DY debug analyzedString: "+analyzedString);
        String [] lineArray = analyzedString.split(",|，|\\s+");
        for (String Hello:lineArray)
        	if( !Hello.contains(";") ) 
        		outPutString+=Hello+" ";
         
        return outPutString;
		 
	}

}
