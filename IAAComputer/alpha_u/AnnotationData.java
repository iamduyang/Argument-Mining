/**   
* @Title: AnnotationData
* @Package IAAComputer.alpha_u
* @Description: a class to store Annotation Data
* @author Du Yang
* @date 17th Nov 2017
* @version version 1.0  
*/


package IAAComputer.alpha_u;
import java.util.ArrayList;

public class AnnotationData{
	/**Major Claim list: every Integer Array store "begin & end" duple of one major claim. 
	 * The list store all Major Claim  one annotated 
	*/
  	 ArrayList<Integer[]> mcList;
 	/**Claim list:
 	 * The list store all Claim list  one annotated 
 	*/
  	 ArrayList<Integer[]> claimList;
  	/**Premise list:
  	 * The list store all Premise list:  one annotated 
  	*/
  	 ArrayList<Integer[]> pList;
  	 
   	/** Implict Premise list:
   	 * The list store all Implict Premise list:  one annotated 
   	*/
  	// ArrayList<Integer[]> ipList;
  	AnnotationData()
  	{
  		this.mcList= null;
  		this.claimList= null;
  		this.pList=null;
  		//this.ipList=null;
  		
  	}
  	/**AnnotationData(ArrayList<Integer[]> MCList,ArrayList<Integer[]> CLAIMList,
  			ArrayList<Integer[]> PList,ArrayList<Integer[]> IpList)*/
  	AnnotationData(ArrayList<Integer[]> MCList,ArrayList<Integer[]> CLAIMList,
  			ArrayList<Integer[]> PList)
  	{
  		this.mcList= ( ArrayList<Integer[]>)MCList.clone();
  		this.claimList= ( ArrayList<Integer[]>)CLAIMList.clone();
  		this.pList=( ArrayList<Integer[]>)PList.clone();
  		//this.ipList=( ArrayList<Integer[]>)IpList.clone();
  		
  		
  	}
  	public ArrayList<Integer[]> getMcList(){
    	return ( ArrayList<Integer[]>)mcList.clone();
 	}
 	public ArrayList<Integer[]> getClaimList(){
    	return ( ArrayList<Integer[]>)claimList.clone();
 	}
 	public ArrayList<Integer[]> getPList(){
 		return ( ArrayList<Integer[]>)pList.clone();
  
 	}
 	/**public ArrayList<Integer[]> getIpList(){
 		return ( ArrayList<Integer[]>)ipList.clone();
    	 
 	}*/
 }