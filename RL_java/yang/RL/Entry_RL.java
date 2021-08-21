package yang.RL;

import java.io.IOException;
import java.util.ArrayList;


import duan.assist.FileOperation;
import duan.assist.utils;


public class Entry_RL
{
	public static final String resultsDir = "./results";
	public static final String vectorDir = "./src/main/resources/vector";
	public static int[][] confusionMatrix;
	public static int[][] overallConfusionMatrix;
	public static int testClauseCounter;
	public static int correctAnnotationCounter;
	public static final String splitCharacter = "-";
	private static String docNames[];
	private static ArrayList<ArrayList<ClauseVector>> clauseVectors;
	
	/***************parameters used in kernelLspiAgent.java**********************/
	
	protected static float DECAY_FACTOR;
	protected static int ITERATE_ROUND;
	protected static int CROSS_FOLD_NUM = 12;
	protected static int SCAN_MAX_NUM;
	protected static int ANN_WINDOW_SIZE;
	protected static int HISTORY_WINDOW_SIZE;
	protected static int SAMPLING_ROUND;
	protected static float GAMMA;
	protected static float LAMBDA;
	
	protected static String resultFileName;
			//"infoGain-memReplay-windSize3-total20-sigma4-5fold-epi500-decay1.0.dat";
	
	public static void main( String args[] ) throws IOException
	{
		float[] decayList = {1};
		int[] samplingRoundList = {10};
		int[] scanMaxNumList = {10};
		int[] iterateRoundList = {20};
		
		// last round, surrounding clauses' annotations
		//int[] annWinSizeList = {1,3,5,7};
		int[] annWinSizeList = {1};// dy fixed the window sizes to one
		
		// this round, preceding clauses' annotations
		int[] historyWinSizeList = {0};
		
		float[] gammaList = {1};
		float[] lambdaList = {0};
		
		int listSize = 
                decayList.length 
                * scanMaxNumList.length
                * samplingRoundList.length
                * annWinSizeList.length
                * historyWinSizeList.length
                * iterateRoundList.length
                * gammaList.length
                * lambdaList.length;
		
		float[] macroF1List = new float[ listSize ];
		String[] sigList = new String[ listSize ];
		int counter = 0;
		String signiture = null;
		
		for( float decay : decayList )
		for( int scanMaxNum : scanMaxNumList )
		for( int samplingRound : samplingRoundList )
		for( int surroundWinSize : annWinSizeList )
		for( int histWinSize : historyWinSizeList )
		for( int itNum : iterateRoundList )
		for( float gamma: gammaList )
		for( float lambda: lambdaList )
		{
			clear();
			
			DECAY_FACTOR = decay;
			SAMPLING_ROUND = samplingRound;
			SCAN_MAX_NUM = scanMaxNum;
			ANN_WINDOW_SIZE = surroundWinSize;
			HISTORY_WINDOW_SIZE = histWinSize;
			ITERATE_ROUND = itNum;
			GAMMA = gamma;
			LAMBDA = lambda;
			
			signiture= 
					"decay"+Float.toString(decay)
					+"-gamma"+Float.toString(gamma)
					+"-lambda"+Float.toString(lambda)
					+"-sampleRound"+Float.toString(samplingRound)
					+"-iterNum"+Integer.toString(itNum)
					+"-scanMaxNum"+Integer.toString(scanMaxNum)
					+"-surrWinSize"+Integer.toString(surroundWinSize)
					+"-hisWinSize"+Integer.toString(histWinSize);
			
			resultFileName = 
					"lspi-withET-total84"
					+"-fold"+Integer.toString(CROSS_FOLD_NUM)
					+"-"+signiture+".dat";
			
			
			float accuracy = experiment();
			
			macroF1List[counter] = accuracy;
			sigList[counter] = resultFileName;
			counter += 1;
		}
		
		String output = getSortedResult( macroF1List, sigList );
		
		String summaryFileName = "ResultSummary.txt";
		FileOperation.writeToFile(output, resultsDir, summaryFileName, true);
		
	}
	
	private static String getSortedResult( float[] aAccList, String[] aSigList )
	{
		String output = new String("\n\n");
		int size = aAccList.length;
		ArrayList<String> printedList = new ArrayList<String>( size );
		float upbound = Float.POSITIVE_INFINITY;
		
		for( int i=0; i<size; ++i )
		{
			float max = Float.NEGATIVE_INFINITY;
			int maxIndex = -1;
			
			for( int j=0; j<size; ++j )
			{
				if( printedList.contains(aSigList[j]) )
					continue;
				else if( aAccList[j] > max && aAccList[j] <= upbound)
				{
					max = aAccList[j];
					maxIndex = j;
				}
			}
			
			upbound = max;
			printedList.add(aSigList[maxIndex]);
			output += aSigList[maxIndex] + "\t" + max + "\n";
		}
		
		return output;
	}
	
	private static void clear()
	{
		confusionMatrix = new int[LspiAgent.ACTION_NUM][LspiAgent.ACTION_NUM];
		overallConfusionMatrix = new int[LspiAgent.ACTION_NUM][LspiAgent.ACTION_NUM];
		testClauseCounter = 0;
		correctAnnotationCounter = 0;
	}
	
	
	private static float experiment() throws IOException
	{
		//read and parse vectors from vector files
		Object docNameAndVectors[] = VectorParser.getDocVectorDic( vectorDir );
		
		docNames = (String[])docNameAndVectors[0];
		clauseVectors = (ArrayList<ArrayList<ClauseVector>>)docNameAndVectors[1];
		
		//perform train/test split, and do cross validation
		int docNum = docNames.length;
		int testDocNum = docNum/CROSS_FOLD_NUM;
		int trainDocNum = docNum - testDocNum;
		for( int foldID=0; foldID<CROSS_FOLD_NUM; ++foldID )
		{
			
			String output = "\n\n\n==========================================\n"+
					"\tCross Validation Round "+(foldID+1)+
					"\n==========================================";
			System.out.println(output);
			FileOperation.writeToFile(output, resultsDir, resultFileName, true);
			
			//the first test document's index; used to split test and train documents
			int testFirstIndex = foldID*testDocNum;
			
			//find orthogonal vectors' dictionary
			LspiAgent rlAgent = new LspiAgent( clauseVectors.get(0).get(0).getVector().length, 
					docNum, testDocNum);
			//ilstdAgent rlAgent = new ilstdAgent( clauseVectors.get(0).get(0).getVector().length, 
					//docNum, testDocNum);
			
			//perform policy iteration
			long tStart = System.currentTimeMillis();
			
			rlAgent.LSPI( testFirstIndex, clauseVectors );
			long timeUsed = System.currentTimeMillis() - tStart;
			output = "\nTime used for training (kLSPI): " + timeUsed/1000.0 + " seconds\n";
			System.out.println(output);
			FileOperation.writeToFile(output, resultsDir, resultFileName, true);
			
			//use the learnt policy to annotate test documents
			int[] testResults = rlAgent.annotate( testFirstIndex, clauseVectors, docNames );
			
			output = "\n\n+++++++++++++ Confusion Matrix ++++++++++++++++\n\n";
			output += "total number of clauses: "+testResults[0]+"\n";
			output += "correctly classified clause: "+testResults[1]+"\n";
			output += "accuracy: "+(testResults[1]*1.0/testResults[0])+"\n\n";
			output += utils.generateConfusionMatrix( confusionMatrix );
			output += "\n\n+++++++++++++ Detailed Statistics ++++++++++++++++\n\n";
			Object[] result = utils.computePRF(confusionMatrix);
			output += (String)result[0];
			//double macroF1 = (double)result[1]; //change by DY
			double macroF1 = Double.parseDouble(result[1].toString());
			output += "\n\n Macro F1 : "+macroF1;
			FileOperation.writeToFile(output, resultsDir, resultFileName, true);
			System.out.println(output);

			for( int i=0; i<LspiAgent.ACTION_NUM; i++ )
				for( int j=0; j<LspiAgent.ACTION_NUM; j++ )
				{
					overallConfusionMatrix[i][j] += confusionMatrix[i][j];
					confusionMatrix[i][j] = 0;
				}
		}
		
		float fullAccuracy = (correctAnnotationCounter*1.0f/testClauseCounter);
		
		String lastOut = "\n\n\n========================================\n"+
				"\tOverall Confusion Matrix"+
				"\n========================================\n\n";
		lastOut += "total number of clauses: "+testClauseCounter+"\n";
		lastOut += "correctly classified clause: "+correctAnnotationCounter+"\n";
		lastOut += "overall accuracy: "+ fullAccuracy +"\n\n";
		lastOut += utils.generateConfusionMatrix( overallConfusionMatrix );
		FileOperation.writeToFile(lastOut, resultsDir, resultFileName, true);
		System.out.println(lastOut);
		
		lastOut = "\n\n+++++++++++++ Detailed Statistics ++++++++++++++++\n\n";
		Object[] overallPRF = utils.computePRF( overallConfusionMatrix );
		lastOut += (String)overallPRF[0];
		//double fullMacroF1 = (double)overallPRF[1];
		//added by DY
		double fullMacroF1 = Double.parseDouble(overallPRF[1].toString());
		lastOut += "\n\n Macro F1 : "+fullMacroF1;
		FileOperation.writeToFile(lastOut, resultsDir, resultFileName, true);
		System.out.println(lastOut);
		
		return (float)fullMacroF1;
	}
	
	
	

}




