package yang.RL;

public class TupleItem 
{
	private int docIndex;
	private int curClauseID;
	private int curAct; 
	private int reward; 
	private int nextAct;
	private int[] currClauseLastRoundAnn;
	private int[] nextClauseLastRoundAnn;
	private int[] curPrecedingClauseThisRoundAnn;
	private int[] nextPrecedingClauseThisRoundAnn;
	
	public TupleItem( int aDocIndex, 
					int aCurID, int aCurAct, int aReward, int aNextAct,
					int[] curLastRound, int[] nextLastRound, int[] curPreThisRound,
					int[] nextPreThisRound)
	{
		docIndex = aDocIndex;
		curClauseID = aCurID;
		curAct = aCurAct;
		reward = aReward;
		nextAct = aNextAct;
		
		if(curLastRound == null)
			currClauseLastRoundAnn = null;
		else
			currClauseLastRoundAnn = curLastRound.clone();
		
		if( nextLastRound == null )
			nextClauseLastRoundAnn = null;
		else
			nextClauseLastRoundAnn = nextLastRound.clone();
		
		if(curPreThisRound == null)
			curPrecedingClauseThisRoundAnn = null;
		else
			curPrecedingClauseThisRoundAnn = curPreThisRound.clone();
		
		if(nextPreThisRound == null)
			nextPrecedingClauseThisRoundAnn = null;
		else
			nextPrecedingClauseThisRoundAnn = nextPreThisRound.clone();
	}
	
	public int getDocID()
	{
		return docIndex;
	}
	
	public int getClauseID()
	{
		return curClauseID;
	}
	
	public int getCurAct( )
	{
		return curAct;
	}
	
	public int getNextAct()
	{
		return nextAct;
	}
	
	public int getReward()
	{
		return reward;
	}
	
	public int[] getCurClauseLastRoundAnn()
	{
		return currClauseLastRoundAnn;
	}
	
	public int[] getNextClauseLastRoundAnn()
	{
		return nextClauseLastRoundAnn;
	}
	
	public int[] getCurPreClauseThisRoundAnn()
	{
		return curPrecedingClauseThisRoundAnn;
	}
	
	public int[] getNextPreClauseThisRoundAnn()
	{
		return nextPrecedingClauseThisRoundAnn;
	}

}
