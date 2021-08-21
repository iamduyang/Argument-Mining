package duan.assist;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;



public class FileOperation {

	
	/*
	 * Function : delete a file according to the dir 
	 * PARAM    : dir is the pathname of the file
	 * Return   : void
	 */
	public static void deleteFile(String dir)
	{
		File fp = new File(dir);
		if(fp.exists())
		{
			fp.delete();
		}
	}
	
	/**
	 * 
	 * @param 
	 * @return
	 */
	public static String joinPath( String str1, String str2 )
	{
		String reString = ""+str1;
		reString.trim();
		
		if( reString.charAt(reString.length()-1) == '/')
		{
			reString += str2.trim();
		}
		else
		{
			reString += "/" + str2.trim();
		}
		
		return reString;
			
	}
	
	//write the true and RL-based annotation, so as to perform comparison and error analyses
	/*
	public static void writeComparison( String[] aDocNames, int aDocID, int aClauseID, int aMyAnn, int aTrueAnn )
	{
		String fileName = aDocNames[aDocID].split("\\.")[0]+".compare";
		String output = aClauseID + "\tTRUE_"+aTrueAnn + "\tMY_"+aMyAnn + "\n";
		FileOperation.writeToFile(output, Entry_RL.comparisionDir, fileName, true);
	}
	*/
	/*
	 * Function : read the content of the file where the path is dir
	 * PARAM    : dir is the path of the file
	 * Return   : content of the file
	 */
	public static String readfileContent(String dir) throws IOException
	{
		File fp = new File(dir);
		byte[] filecontent = new byte[(int) fp.length()];
		InputStream in = new FileInputStream(fp);
		in.read(filecontent);
		in.close();
		return new String(filecontent,"UTF-8");
	}
	
	/*
	 * Function : write a String to a certain file in a certain directory
	 * PARAM : aStr is the output String, aFileDir is the file directory
	 * Return : void
	 */
	public static void writeToFile( String aStr, String aFileDir, String aFileName, boolean aAppend)
	{
		File outDir = new File(aFileDir);
		
		if(!outDir.exists())
		{
			outDir.mkdirs();
		}
		
		File fp = new File( outDir, aFileName );
		
		FileWriter writer = null;
		try {
			writer = new FileWriter(fp, aAppend);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		try {
			writer.write(aStr);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	
	/*
	 * Function : n-grams frequency statistics
	 * PARAM    : sa is the all n-grams extracted from the file   
	 * Return   : return a HashMap<String, Integer> , its entry_key is the string of n-grams, its entry_value is the numbers of the string
	 */
	public static HashMap<String, Integer> array_unique(String[] sa)
	{
		List<String> list = new LinkedList<String>();
		HashMap<String, Integer> Frequency = new HashMap<String,Integer>();
		int num;
		for(int i = 0; i < sa.length; i++ )
		{
			if(!list.contains(sa[i]))
			{
				list.add(sa[i]);
				Frequency.put(sa[i], 1);
			}
			else
			{
				num = Frequency.get(sa[i]);
				num++;
				//Frequency.replace(sa[i], num);
				Frequency.put(sa[i], num);
			}
		}
		
		return Frequency;
	}
	
	
	
	/*
	 * Function : change uniAllgrams(word)+ into unigrams(word, the numbers of the word in the unigrams)+,
	 * PARAM	: source-- the path of the sourcefile ,like uniAllgrams.txt; dest-- the path of the destfile, like unigrams.txt
	 * Return   : void 
	 */
	
	//the frequency of the grams
	public static void grams_unique(String source, String dest) throws IOException
	{
		// read content from the source file 
		List<String> list = new LinkedList<String>();
		
		File sourceFile = new File(source);
		BufferedReader reader = new BufferedReader(new FileReader(sourceFile));
		
		String temp = null;
		temp = reader.readLine();
		while(temp != null)
		{
			list.add(temp);
			temp = reader.readLine();
		}
		reader.close();
		
		//write unique grams to dest file
		deleteFile(dest);
		
		String[] allString 	  = (String[]) list.toArray(new String[list.size()]);
		HashMap<String, Integer> fr = array_unique(allString);
		
		
		// sort HashMap according to the value
		List<Map.Entry<String, Integer>> infoIds = 
				new ArrayList<Map.Entry<String,Integer>>(fr.entrySet());
		
		Collections.sort(infoIds, new Comparator<Map.Entry<String,Integer>>() {
			public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2)
			{
				return (o2.getValue() - o1.getValue()); 
			}
		});
		
		
		
		//append to the file
		FileWriter writer = new FileWriter(dest,false);
		for(int i = 0; i < infoIds.size(); i++)
		{
			String key = infoIds.get(i).getKey();
			int value  = infoIds.get(i).getValue();
			writer.write(key + '\t' + value + '\n');
		}
		
		writer.close();		
	}
	
	/*
	 * Function	: read the true annotation type of each clause
	 * PARAM	: aFilePath, the path of the annotation file
	 * Return	: String[], the type of each clause in a document 
	 * 
	 * delete getClauseTrueType    // April 5th Note
	 */
	
	
	/*
	 * Function : read position list(start, end) from a file
	 * PARAM    : filepath: the path of one standard .ann file (Format of each line likes that "T1	None 0 31	Satisfactory, but disappointing")
	 * Return   : String[]: the position of each sentence
	 */
	public static String[] getClausePos(String filepath) throws IOException
	{
		BufferedReader br = new BufferedReader(new FileReader(filepath));
		
		String line;
		List<String> list = new LinkedList<String>();
		while((line = br.readLine()) != null)
		{
			int start = line.indexOf("\t") + 1;
			String subline = line.substring(start);
			start = subline.indexOf(" ") + 1;
			int end   = subline.indexOf("\t");
			list.add(subline.substring(start,end));		
		}
		
		br.close();
		return (String[]) list.toArray(new String[list.size()]);
	}
	
	
	
	/*
	 * read Line from dictionary
	 * 
	 */
	public static List<String> readLine(String path) throws IOException
	{
		List<String> list = new LinkedList<String>();
		File sourceFile = new File(path);
		BufferedReader reader = new BufferedReader(new FileReader(sourceFile));
		
		String temp = null;
		temp = reader.readLine();
		while(temp != null)
		{
			list.add(temp);
			temp = reader.readLine();
		}
		reader.close();
		
		return list;
	}
}
