package yang.RL;
//lspi refer to lstd
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import org.jblas.FloatMatrix;
import org.jblas.Solve;

import duan.assist.FileOperation;

public class LspiAgent 
{
	public static final int LEARN = 111;
	public static final int TEST = 222;
	public static final int SAMPLING = 333;
	
	public static final int ACTION_NUM = 16;  // here, we modify 6 tp 16
	public static final int STAGE_STEP = 5;
	
	private static final float REWARD = 10;
	private static final float EPSILON_START_VALUE = 1.0f;

	private float epsilon;
	private float THRESHOLD_U = 0.1f;
	
	private int stateVectorLength;
	private int oriStateVectorLength;
	private int vectorLength;
	private int docNum;
	private int testDocNum;
	
	private FloatMatrix weights;
	private FloatMatrix matrixA;
	private FloatMatrix vectorb;
	private FloatMatrix vectorz;
	
	
	public LspiAgent( int aStateVectorLength, int aDocNum, int aTestNum )
	{
		oriStateVectorLength = aStateVectorLength;
		stateVectorLength = oriStateVectorLength + Entry_RL.ANN_WINDOW_SIZE + Entry_RL.HISTORY_WINDOW_SIZE;
		vectorLength = stateVectorLength * ACTION_NUM;
		docNum = aDocNum;
		testDocNum = aTestNum;
		epsilon = EPSILON_START_VALUE;
		
		matrixA = FloatMatrix.diag(FloatMatrix.rand(vectorLength));
		vectorb = FloatMatrix.zeros(vectorLength);
		weights = FloatMatrix.rand(vectorLength);
		//vectorz = FloatMatrix.zeros(vectorLength);
	}
	
	
	private int[] getLegalActions( int[] aExistActList )
	{
		//return new int[]{1,2,3,4,5,6};
		return new int[]{1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16};
	}
	
	
	protected void LSPI( int aFirstIndex, ArrayList<ArrayList<ClauseVector>> aClauseVectors )
	{
		String output = "\n\n+++++++++++++++ LSPI Phase +++++++++++++++\n\n";
		System.out.print( output );
		int stage = 0;
		long tStart = System.currentTimeMillis();
		long tEnd;
		int sampleCounter = 0;
		int totalSampleNum = (docNum-testDocNum)*Entry_RL.ITERATE_ROUND*Entry_RL.SAMPLING_ROUND;
		
		for( int iterateRound=0; iterateRound<Entry_RL.ITERATE_ROUND; ++iterateRound )
		{
			//for each round of policy iteration
			for( int docIndex=0; docIndex<aClauseVectors.size(); docIndex++ )
			{
				if( docIndex >= aFirstIndex && docIndex < aFirstIndex+testDocNum )
					continue;
				else
				{
					ArrayList<ClauseVector> docClauseList = aClauseVectors.get(docIndex);
					int docLength = docClauseList.size();
					int[] thisRoundAnn = new int[docLength];
					int[] lastRoundAnn;

					
					for( int sampleIndex=0; sampleIndex<Entry_RL.SAMPLING_ROUND; sampleIndex++ )
					{
						
						if( sampleCounter >= stage*totalSampleNum*0.01 )
						{
							tEnd = System.currentTimeMillis();
							System.out.print("\t\t"+stage+" percent finished");
							System.out.print("\tTime used: "+ (tEnd-tStart)/1000.0 +" sec.");
							System.out.println("\tEpsilon: "+epsilon);
							tStart = tEnd;
							stage += STAGE_STEP;
						}
						
						lastRoundAnn = thisRoundAnn.clone();
						thisRoundAnn = new int[docLength];
						int nextAct = this.getAction(docClauseList, 0, 
								thisRoundAnn, lastRoundAnn, this.LEARN);
						int curAct;
						float reward;
						
						//for delayed rewards
						float accReward = 0;
						
						//for eligibility traces
						vectorz = FloatMatrix.zeros(vectorLength);
						
						for( int clauseID=0; clauseID<docLength; clauseID++ )
						{
							curAct = nextAct;
							thisRoundAnn[clauseID] = curAct;
							
							reward = this.performAction(curAct, this.getClauseByIndex(docClauseList, clauseID));
							accReward += reward;
							
							/*
							//for delayed reward
							if( clauseID == docLength-1 )
								reward = accReward;
							else
								reward = 0;
								*/
							
							nextAct = this.getAction(docClauseList, clauseID+1, 
									thisRoundAnn, lastRoundAnn, this.LEARN);
							
							FloatMatrix curSAvector = 
									stateActionLastRoundAnnHistoryWindow2VectorMatrix(
										docClauseList, clauseID, curAct, 
										this.getLastRoundAnn(clauseID, lastRoundAnn), 
										this.getPreClauseThisRoundAnn(clauseID, thisRoundAnn)
										);
							
							vectorz.muli( Entry_RL.LAMBDA*Entry_RL.GAMMA );
							vectorz.addi( curSAvector );
							
							FloatMatrix nextSAvector;
							if( clauseID < docLength-1 )
								nextSAvector = 
									stateActionLastRoundAnnHistoryWindow2VectorMatrix(
										docClauseList, clauseID+1, nextAct, 
										this.getLastRoundAnn(clauseID+1, lastRoundAnn), 
										this.getPreClauseThisRoundAnn(clauseID+1, thisRoundAnn)
										);
							else
								nextSAvector = new FloatMatrix().zeros(vectorLength);
								
							
							FloatMatrix trans = curSAvector.sub( nextSAvector.muli(Entry_RL.GAMMA) ).transpose();

							//without eligibility traces
							//matrixA.addi( curSAvector.mmul(trans) );
							//vectorb.addi( curSAvector.mmul(reward) );
							
							//with eligibility traces
							matrixA.addi( vectorz.mmul(trans) );
							vectorb.addi( vectorz.mmul(reward) );
							
						}//end of each clause scanning
						
						sampleCounter += 1;
					}//for each document scanning
				}//else
				
			}//end of each policy iteration round
			
			this.decay(Entry_RL.DECAY_FACTOR);
			weights = Solve.pinv(matrixA).mmul(vectorb);
			
			/*
			//for debug purposes
			System.out.println("weights: ");
			for( int i=0; i<weights.length; ++i )
				System.out.print(weights.get(i)+", ");
			System.out.println("");
			*/
			
		}//end of full LSPI
		
		tEnd = System.currentTimeMillis();
		System.out.print("\t\t"+stage+" percent finished");
		System.out.println("\tTime used: "+ (tEnd-tStart)/1000.0 +" sec.");
	}
	
	
	private int getActNum( int[] aList )
	{
		int cnt = 0;
		
		for(int ele: aList)
		{
			if( ele != 0 )
				cnt += 1;
		}
		
		return cnt;
	}
	
	private int[] getPreClauseThisRoundAnn( int[] aThisRoundAnnList )
	{
		int actNum = getActNum( aThisRoundAnnList );
		int[] result = new int[Entry_RL.HISTORY_WINDOW_SIZE];
		int index = 0;
		
		for( int i=actNum-Entry_RL.HISTORY_WINDOW_SIZE; i<=actNum-1; ++i )
		{
			if( i>=0 )
				result[index] = aThisRoundAnnList[i];
			else
				result[index] = 0;
			
			index += 1;
		}
		
		return result;
	}
	
	private int[] getPreClauseThisRoundAnn( int aClauseIndex, int[] aThisRoundAnnList )
	{
		int[] result = new int[Entry_RL.HISTORY_WINDOW_SIZE];
		int index = 0;
		
		for( int i=aClauseIndex-Entry_RL.HISTORY_WINDOW_SIZE; i<=aClauseIndex-1; ++i )
		{
			if( i>=0 )
				result[index] = aThisRoundAnnList[i];
			
			index += 1;
		}
		
		return result;
	}
	
	private int[] getLastRoundAnn( int aClauseID, int[] aLastRoundAnn )
	{
		if( Entry_RL.ANN_WINDOW_SIZE == 0 )
			return null;
		
		int[] result = new int[Entry_RL.ANN_WINDOW_SIZE];
		int radius = (Entry_RL.ANN_WINDOW_SIZE-1)/2;
		int counter = 0;
		int docLength = aLastRoundAnn.length;
		
		if( aClauseID >= docLength )
		{
			Arrays.fill(result, 0);
			return result;
		}
		
		for( int i=aClauseID-radius; i<=aClauseID+radius; i++ )
		{
			if( i >= 0 && i < docLength )
				result[counter] = aLastRoundAnn[i];
			else
				result[counter] = 0;
			
			counter += 1;
		}
		
		return result;
	}
	
	
	private float performAction( int aAct, ClauseVector aVector )
	{
		int trueAnn = aVector.getTrueAnnotation();
		
		float reReward;
		if( aAct == trueAnn )
			reReward = REWARD;
		else
			reReward = -REWARD;
		
		return reReward;
	}
	
	private ClauseVector getClauseByIndex( ArrayList<ClauseVector> aDocVectors, int aIndex )
	{
		if( aDocVectors.get(aIndex).getClauseIndex() == aIndex )
			return aDocVectors.get(aIndex);
		else
		{
			for( int i=0; i<aDocVectors.size(); ++i )
			{
				if( aDocVectors.get(i).getClauseIndex() == aIndex )
					return aDocVectors.get(i);
			}
		}
		
		System.out.println("Error in getClauseByIndex! index: "+aIndex);
		return null;
	}
	
	private int[] oneRoundScan( ArrayList<ClauseVector> aClauseList, int[] aLastRoundActList )
	{
		int length = aClauseList.size();
		int[] annotations = new int[length];
		
		for( int clauseID=0; clauseID<length; ++clauseID )
		{
			//float[] state = getClauseByIndex( docVectors,clauseID ).getVector();
			annotations[clauseID] = getAction( aClauseList, clauseID, annotations, aLastRoundActList, TEST );
			
		}
		
		return annotations;
	}
	
	private boolean isEquivalent( int[] aList1, int[] aList2 )
	{
		if( aList1.length != aList2.length )
			return false;
		else{
			for( int i=0; i<aList1.length; ++i )
			{
				if(aList1[i] != aList2[i])
					return false;
			}
			return true;
		}
	}
	
	private int[] scanAndAnnotate( ArrayList<ClauseVector> aClauseList, int[] aActList )
	{
		int length = aClauseList.size();
		int bestResult = -999;
		int[] bestAnn = new int[length];
		int[] lastAnnResult = new int[length];
		
		int roundCounter;
		
		for( roundCounter=0; roundCounter<Entry_RL.SCAN_MAX_NUM; ++roundCounter )
		{
			int annResult[] = oneRoundScan( aClauseList, lastAnnResult );
			if( isEquivalent( annResult, lastAnnResult ) && roundCounter != 0 )
			{
				bestResult = evaluateAnnotation( aClauseList,annResult );
				bestAnn = annResult.clone();
				
				break;
			}
			else
			{
				int corrCounter = evaluateAnnotation( aClauseList,annResult );
				if( corrCounter > bestResult )
				{
					bestResult = corrCounter;
					bestAnn = annResult.clone();
				}
			}
			lastAnnResult = annResult.clone();
		}
		
		for( int i=0; i<aActList.length; ++i )
			aActList[i] = bestAnn[i];
		
		return new int[] {bestResult, roundCounter};
	}
	
	private int evaluateAnnotation( ArrayList<ClauseVector> aClauseList, int[] aActList )
	{
		int length = aClauseList.size();
		int counter = 0;
		
		for( int i=0; i<length; ++i )
		{
			if( aActList[i] == getClauseByIndex(aClauseList, i).getTrueAnnotation() )
				counter += 1;
		}
		
		return counter;
	}
	
	protected int[] annotate( int aFirstIndex, ArrayList<ArrayList<ClauseVector>> aClauseVectors, String aDocNames[] ) 
	{
		System.out.println("\n+++++++++++++++ Test Phase +++++++++++++++\n");
		int correctCounter = 0;
		int clauseCounter = 0;
		
		for( int testID=aFirstIndex; testID<aFirstIndex+testDocNum; ++testID )
		{
			System.out.print("\t Doc "+aDocNames[testID]);
			String output = aDocNames[testID];
			
			float avgAcc = 0;
			ArrayList<ClauseVector> docVectors = aClauseVectors.get(testID);
			int length = docVectors.size();
			int[] annotations = new int[length];
			
			int scanResult[] = scanAndAnnotate( docVectors, annotations );
			int correctNum = scanResult[0];
			int scanRound = scanResult[1];
			
			for( int i=0; i<length; ++i )
			{
				int trueAnn = getClauseByIndex( docVectors, i ).getTrueAnnotation();
				int ourAnn = annotations[i];
				Entry_RL.confusionMatrix[trueAnn-1][ourAnn-1] += 1;
				
				//write the true and RL-based annotation, so as to perform comparision and 
				//error analyses
				//FileOperation.writeComparison( aDocNames, testID, i, ourAnn, trueAnn );
			}
				
			
			Entry_RL.correctAnnotationCounter += correctNum;
			Entry_RL.testClauseCounter += length;
			
			avgAcc += correctNum;
			correctCounter += correctNum;
			clauseCounter += docVectors.size();
			System.out.print("\taccuracy: "+(float)avgAcc/length );
			System.out.println("\tscan round: " + scanRound);
			output += "\t accuracy: " + (float)avgAcc/length;
			output += "\t scan round: "+scanRound+"\n";
			FileOperation.writeToFile(output, Entry_RL.resultsDir, Entry_RL.resultFileName, true);
			
		}
		
		int[] results = {clauseCounter, correctCounter};
		return results;
	}	
	
	private float computeValue( FloatMatrix aStateActionVector )
	{
		float result = aStateActionVector.dot(weights);
		
		return result;
	}
	
	private int getAction( ArrayList<ClauseVector> aClauseList, int aStateIndex, 
			int[] aCurRoundActList, int[] aLastRoundActList, int aStatus )
	{
		float maxQValue = Float.NEGATIVE_INFINITY;
		int bestAction = 0;
		int[] legalActList = getLegalActions(aCurRoundActList);
		
		//if next state is the terminal state, return pseudo action -1
		if( aStateIndex >= aClauseList.size() )
			return 0;
		else if( aStatus == SAMPLING || 
				(aStatus == LEARN && new Random().nextFloat() < epsilon)
				)
		{
			return legalActList[ new Random().nextInt(legalActList.length) ];
		}
		else
		{
			for( int act : legalActList )
			{
				float qValue = computeValue( 
						stateActionLastRoundAnnFullHistory2VectorMatrix(
								aClauseList, aStateIndex, act, aLastRoundActList, 
								aCurRoundActList) 
						);
				if( qValue > maxQValue )
				{
					maxQValue = qValue;
					bestAction = act;
				}
			}

			return bestAction;
		}
		
	}
	
	
	private float[] getStateVector( ArrayList<ClauseVector> aClauseList, int aStateIndex, 
			int[] aLastRoundAnn, int[] aThisRoundAnn)
	{
		
		float[] result = new float[stateVectorLength];
		int index = 0;
		
		float[] oriStateVector = getClauseByIndex( aClauseList, aStateIndex ).getVector();
		for( ; index<oriStateVectorLength; index += 1 )
			result[index] = oriStateVector[index];
		
		//add last round annotations into the state representation
		for( int i=0; i<Entry_RL.ANN_WINDOW_SIZE; ++i )
		{
			result[index] = aLastRoundAnn[i];
			index += 1;
		}
		
		//add this round annotations on preceding clauses into the state representation
		for( int i=0; i<Entry_RL.HISTORY_WINDOW_SIZE; ++i )
		{
			result[index] = aThisRoundAnn[i];
			index += 1;
		}
		
		return result;
	}
	
	
	private FloatMatrix stateActionLastRoundAnnFullHistory2VectorMatrix( 
			ArrayList<ClauseVector> aClauseList, int aStateIndex, int aAct,
			int[] aLastRoundAnn, int[] aThisRoundAnn)
	{
		float[] stateActionVector = new float[vectorLength];
		int[] historyAnnWindow = getPreClauseThisRoundAnn( aStateIndex, aThisRoundAnn );
		int[] lastRoundAnn = getLastRoundAnn( aStateIndex, aLastRoundAnn );
		float[] stateVector = getStateVector( 
				aClauseList, aStateIndex, lastRoundAnn, historyAnnWindow);
		
		if( aAct != 0 )
		{
			int count = 0;
			
			for( int act=1; act<=ACTION_NUM; ++act )
			{
				if( act == aAct )
				{
					for( int i=0; i<stateVector.length; ++i )
						stateActionVector[count++] = stateVector[i];
				}
				else
				{
					count += stateVectorLength;
				}
			}
		}
		
		FloatMatrix reMatrix = new FloatMatrix( stateActionVector );
		
		return reMatrix;
	}
	
	private FloatMatrix stateActionLastRoundAnnHistoryWindow2VectorMatrix( 
			ArrayList<ClauseVector> aClauseList, int aStateIndex, int aAct,
			int[] aLastRoundAnn, int[] historyAnnWindow)
	{
		float[] stateActionVector = new float[vectorLength];
		float[] stateVector = getStateVector( 
				aClauseList, aStateIndex, aLastRoundAnn, historyAnnWindow);
		
		if( aAct != 0 )
		{
			int count = 0;
			
			for( int act=1; act<=ACTION_NUM; ++act )
			{
				if( act == aAct )
				{
					for( int i=0; i<stateVector.length; ++i )
						stateActionVector[count++] = stateVector[i];
				}
				else
				{
					count += stateVectorLength;
				}
			}
		}
		
		FloatMatrix reMatrix = new FloatMatrix( stateActionVector );
		
		return reMatrix;
	}
	
	public void decay( float aDecayFactor )
	{
		epsilon *= aDecayFactor;
		if( epsilon < 0.05f )
			epsilon = 0.05f;
	}
	

}
