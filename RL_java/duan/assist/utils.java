package duan.assist;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import gao.RL.LspiAgent;

public class utils {
	
	
		// check whether the string is digit or not 
		public static final boolean isNumeric(String str)
		{
			for(int i = 0; i < str.length(); i++)
			{
				if(!Character.isDigit(str.charAt(i)))
				{
					return false;
				}
			}
			return true;
		}

		//compute precision, recall and F1-score
		public static Object[] computePRF( int[][] aMatrix )
		{
			String output = "\tPre\tRcl\tF1\n";
			double macroF1 = 0.0;
			
			for( int i=0; i<LspiAgent.ACTION_NUM; ++i )
			{
				switch(i)
				{
					case 0: output += "Type0\t"; break;
					case 1: output += "Type1\t"; break;
					case 2: output += "Type2\t"; break;
					case 3: output += "Type3\t"; break;
					case 4: output += "Type4\t"; break;
					case 5: output += "Type5\t"; break;
					case 6: output += "Type6\t"; break;
					case 7: output += "Type7\t"; break;
					case 8: output += "Type8\t"; break;
					case 9: output += "Type9\t"; break;
					case 10: output += "Type10\t"; break;
					case 11: output += "Type11\t"; break;
					case 12: output += "Type12\t"; break;
					case 13: output += "Type13\t"; break;
					case 14: output += "Type14\t"; break;
					case 15: output += "Type15\t"; break;
					default: break;
				}
				
				int trueNum = 0;
				int findNum = 0;
				
				for( int j=0; j<LspiAgent.ACTION_NUM; ++j )
				{
					trueNum += aMatrix[i][j];
					findNum += aMatrix[j][i];
				}
				
				double precision = aMatrix[i][i]*1.0/findNum;
				double recall = aMatrix[i][i]*1.0/trueNum;
				double f1score = 2*precision*recall/(precision+recall);
				
				if( !Double.isNaN(f1score) )
					macroF1 += f1score;
				
				output += precision+"\t"+recall+"\t"+f1score+"\n";
			}
			macroF1 /= LspiAgent.ACTION_NUM;
			
			return new Object[]{output,macroF1};
		}
		
		// generating confusion matrix String
		public static String generateConfusionMatrix( int[][] aConMatrix )
		{
			String output = "";
			
			output += "Relation Type\tCla\tPre\tRec\tBac\tNull\n";
			for( int p=0; p<LspiAgent.ACTION_NUM; ++p )
			{
				for( int q=0; q<LspiAgent.ACTION_NUM; ++q )
					output += aConMatrix[p][q] + "\t";
				
				switch(p)
				{
					case 0: output+="Type0 <-- true type\n"; break;
					case 1: output+="Type1 <-- true type\n"; break;
					case 2: output+="Type2 <-- true type\n"; break;
					case 3: output+="Type3 <-- true type\n"; break;
					case 4: output+="Type4 <-- true type\n"; break;
					case 5: output+="Type5 <-- true type\n"; break;
					case 6: output+="Type6 <-- true type\n"; break;
					case 7: output+="Type7 <-- true type\n"; break;
					case 8: output+="Type8 <-- true type\n"; break;
					case 9: output+="Type9 <-- true type\n"; break;
					case 10: output+="Type10 <-- true type\n"; break;
					case 11: output+="Type11 <-- true type\n"; break;
					case 12: output+="Type12 <-- true type\n"; break;
					case 13: output+="Type13 <-- true type\n"; break;
					case 14: output+="Type14 <-- true type\n"; break;
					case 15: output+="Type15 <-- true type\n"; break;
					default: break;
				}
			}
			
			return output;
		}
		
		
		//delete punctuation from the n-grams arrays
		public static  String[] deletePunctuation(String[] tokens)
		{
			List<String> list = new LinkedList<String>();
			for(int i = 0; i < tokens.length; i++)
			{
				if(!(tokens[i].equals(".") 
						||tokens[i].equals(",")
						||tokens[i].equals("!")
						||tokens[i].equals("?")
						||tokens[i].equals("-")
						||tokens[i].equals(":")
						||tokens[i].equals(";")
						||tokens[i].equals("(")
						||tokens[i].equals(")")
						||tokens[i].equals("...")))
				{
					list.add(tokens[i]);
				}
			}
			return (String[]) list.toArray(new String[list.size()]);
		}
		
		
		// filter the token in the production rules
		public static String filter(String production)
		{
			String filter = production;
			for(int i = 0; i < production.length(); i++)
			{
				String temp = "";
				int j =i;
				if(production.charAt(i) == ' ')
				{
					if(production.charAt(i+1) != '(')
					{
						while(production.charAt(j) != ')')
						{
							temp += production.charAt(j);
							j++;
						}
					}
				}
				if(j != i)
				{	
					filter = filter.replace(temp + ")", ")");
					i = j;
				}
			}
			return filter;
		}
	
		public static String[] getProductionRules(String parseTree)
		{
				List<String> final_list = new LinkedList<String>();
				//String xString = "(S (NP (DT) (JJ) (JJ) (NN)) (VP (VBZ) (PP (IN) (NP (DT) (JJ) (NN)))) (.))";
				//String xString = "(FRAG (ADVP (RB)) (NP (JJ) (NN)) (.))";
				List<String> list = new LinkedList<String>();
				String temp = "";
				for(int i = 0; i < parseTree.length(); i++)
				{
					char ch = parseTree.charAt(i);
					temp += ch;
					
					if(ch == ' ' || ch == ')')
					{	
						list.add(temp);
						temp = "";
					}
					
				}
				
				
				List<String> noSpace_list = new LinkedList<String>();
				for(int i = 0; i < list.size(); i++)
				{
					if(!list.get(i).equals(" "))
					{
						noSpace_list.add(list.get(i));
					}
				}
				
				List<String> noSingleRightBrackets_list = new LinkedList<String>();
				for(int i = 0; i < noSpace_list.size(); i++)
				{
					int length = noSpace_list.get(i).length();
					if(noSpace_list.get(i).charAt(0) == '(' && noSpace_list.get(i).charAt(length -1) != ')' && noSpace_list.get(i+1).equals(")") )
					{
						noSingleRightBrackets_list.add(noSpace_list.get(i) + noSpace_list.get(i+1) );
						i++;
					}
					else
					{
						noSingleRightBrackets_list.add(noSpace_list.get(i));
					}
				}
				
			
				
				while(noSingleRightBrackets_list.size() != 1)
				{				

					
					// step.1
					int start  = -1;
					int length = 0;
					for(int i = 0; i < noSingleRightBrackets_list.size(); i++)
					{
						String test = noSingleRightBrackets_list.get(i);
						int size = test.length();
						if(test.charAt(0) == '(' && test.charAt(size - 1) != ')')
						{
							start = i;
							int j = i+1;
							while(noSingleRightBrackets_list.get(j).charAt(0) == '(' && noSingleRightBrackets_list.get(j).endsWith(")"))
							{
								j++;
								length++;
							}
							if(length > 0)
							{
								if(noSingleRightBrackets_list.get(j).equals(")"))
								{
									String add_item = "";
									for(int k = 0;k <= length; k++)
									{
										//output to file 
										int temp_size = noSingleRightBrackets_list.get(start + k).length();
										if(k == 0)
										{
											String head = noSingleRightBrackets_list.get(start + k).substring(1, temp_size - 1);
											add_item += head + "->";
										}
										else
										{
											String tail = noSingleRightBrackets_list.get(start + k).substring(1, temp_size - 1);
											add_item += tail + ",";
										}
									}
									final_list.add(add_item);
									break;
								}
								else
								{
									length = 0;
								}
							}
						}
					}
					
					//step 2
					List<String> temp_noSpacelist = new LinkedList<String>();
					for(int i = 0; i < noSingleRightBrackets_list.size(); i++)
					{
						if(i <= start || i >start + length )
						{
							temp_noSpacelist.add(noSingleRightBrackets_list.get(i));
						}
					}
					
					//step 3
					List<String> temp_noSRB_list = new LinkedList<String>();
					for(int i = 0; i < temp_noSpacelist.size(); i++)
					{
						int size = temp_noSpacelist.get(i).length();
						if(temp_noSpacelist.get(i).charAt(0) == '(' && temp_noSpacelist.get(i).charAt(size -1) != ')' && temp_noSpacelist.get(i+1).equals(")") )
						{
							String item = temp_noSpacelist.get(i) + temp_noSpacelist.get(i+1);
							item = item.replace(" )", ")");
							temp_noSRB_list.add(item );
							i++;
						}
						else
						{
							temp_noSRB_list.add(temp_noSpacelist.get(i));
						}
					}
					noSingleRightBrackets_list = temp_noSRB_list;
					
				}// while

			return (String[]) final_list.toArray(new String[final_list.size()]);
		}
		
		
		public static int countPunNumber(String[] token)
		{
			int number = 1;
			for(int i = 0; i < token.length; i++)
			{
				if(token[i].equals(",")
						||token[i].equals(".")
						||token[i].equals(":")
						||token[i].equals(";")
						||token[i].equals("!")
						||token[i].equals("?"))
				{
					number ++;
				}
			}
			
			return number;
		}
		
		
		/*
		 * ngrams.txt, ProRules.txt
		 */
		public static List<String> getDictionaryList(String pathname) throws IOException
		{
			List<String> list = new LinkedList<String>();
			File file = new File(pathname);
			BufferedReader br = new BufferedReader(new FileReader(file));
			
			String line = br.readLine();
			while(line != null)
			{
				list.add(line);
				line = br.readLine();
			}
			br.close();
			
			return list;
		}
		
		
		/*
		 * verbs.txt, adverbs.txt, connectives.txt, possessives.txt
		 */
		public static Set<String> getDictionarySet(String pathname) throws IOException
		{
			Set<String> set = new HashSet<String>();
			File file = new File(pathname);
			BufferedReader br = new BufferedReader(new FileReader(file));
			
			String line = br.readLine();
			while(line != null)
			{
				set.add(line);
				line = br.readLine();
			}
			br.close();
			
			return set;
		}
		
		
		
}
