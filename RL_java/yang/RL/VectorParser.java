package yang.RL;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.lang.ArrayUtils;


import duan.assist.FileOperation;

public class VectorParser 
{
	
	
	public static Object[] getDocVectorDic( String aDir ) throws IOException
	{	
		Object reObj[]= new Object[2];
		
		File file = new File( aDir );
		if(!file.isDirectory())
		{
			System.out.println( aDir +" is not directory!" );
		}
		else 
		{	
			String[] textNames = file.list();
			for( int i=0; i<textNames.length; ++i )
			{
				if( textNames[i].trim().charAt(0) == '.' )
					textNames = (String[]) ArrayUtils.remove(textNames, i);
			}
			
			ArrayList< ArrayList<ClauseVector> > clauseVectors = new ArrayList< ArrayList<ClauseVector> >();
			for( String tName : textNames )
			{
				clauseVectors.add( parseDocVector(aDir, tName) );
			}
			
			reObj[0] = textNames;
			reObj[1] = clauseVectors;
		}
		
		return reObj;
	}
	
	private static ArrayList<ClauseVector> parseDocVector( String aPath, String aName ) throws IOException
	{
		ArrayList<ClauseVector> vectorList = new ArrayList<ClauseVector>();
		
		String content = FileOperation.readfileContent( FileOperation.joinPath(aPath, aName) );
		String lines[] = content.split("\n");
		
		for( int i=0; i<lines.length; ++i )
		{
			String components[] = lines[i].split("\t");
			int index = Integer.parseInt( components[0] );
			int trueAnnotation = Integer.parseInt( components[2] ) + 1;
			String vectorElements[] = components[1].split(Entry_RL.splitCharacter);
			float[] vectorNums = new float[vectorElements.length];
			for( int j=0; j<vectorNums.length; ++j )
				vectorNums[j] = Float.parseFloat(vectorElements[j]);
			
			ClauseVector vector = new ClauseVector( index, vectorNums, trueAnnotation );
			vectorList.add(vector);
		}
		
		return vectorList;
	}
	

}
