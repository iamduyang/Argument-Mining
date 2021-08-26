##Remarks on Python Code for AM
（19 May. 2018, Yang Du）

###Purpose
Python code handle with the baseline methods such as SVM, Random Forest,  which our RL method compete with. It is implemented with sklearn. 
	    
### Usage
You could just may ` python Tune_All.py csvfileName featureName ` to get all results. Here, **csvfileName** is the csv file that stores the data, **featureName** is the number of festures used in this turn.

However, runing all the multiple methods in one script takes a lot of time in my machine. Thus, running the separate Python scripts is recommended, i.e.  running the below scripts one by one:
	    
	    python Tune_SVM.py csvfileName featureName
	    python Tune_RandomForest.py csvfileName featureName
	    python Tune_Logist.py csvfileName featureName
	    python Tune_Other2.py csvfileName featureName
	    
	    


###Other highlights
 Directory **dataResult** stores results ran by the author.


