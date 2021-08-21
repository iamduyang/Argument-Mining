package yang.RL;

import java.util.ArrayList;

public class ClauseVector 
{
	//private String docName;
	private int clauseIndex;
	private float[] vector;
	private int trueAnnotation;
	
	public ClauseVector( int aIndex, float[] aVector, int aAnn )
	{
		//docName = aName;
		clauseIndex = aIndex;
		vector = aVector.clone();
		trueAnnotation = aAnn;
	}
	
	public ClauseVector( int aIndex, ArrayList<Float> aVector, int aAnn )
    {   
        clauseIndex = aIndex;
        vector = new float[aVector.size()];
        for( int i=0; i<vector.length; ++i )
            vector[i] = aVector.get(i);
        trueAnnotation = aAnn;
    }  
	
	/*
	public String getDocName()
	{
		return docName;
	}
	*/
	
	public int getClauseIndex()
	{
		return clauseIndex;
	}
	
	public float[] getVector()
	{
		return vector.clone();
	}
	
	public int getTrueAnnotation()
	{
		return trueAnnotation;
	}
}
