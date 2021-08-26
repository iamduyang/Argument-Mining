# -*- coding:utf-8 -*-
"""
# Author:    Yang Du
# Email:     duyang2015 (-A-T-) iscas dot ac dot cn
# Date:      2018-05-09
# Purpose:
#------------------------------------------------------------#
# Copyright (C) 2018        Yang DU
# This code is freely available for non-commercial purposes
#------------------------------------------------------------#
"""

import sys

from pandas import read_csv
import numpy as np

from sklearn.model_selection import train_test_split

from sklearn.linear_model import LogisticRegression


from sklearn.metrics import precision_score
from sklearn.metrics import f1_score
from sklearn.metrics import confusion_matrix,recall_score,classification_report,accuracy_score


import warnings
warnings.filterwarnings('ignore')


# load data
def load_AM_data(featureNNum=800,csvFileName="800.csv"):
    featureNum = featureNNum
    dataframe = read_csv(csvFileName)

    array = dataframe.values

    X = array[:,0:featureNum]
    Y = array[:,featureNum]
    return [X,Y]





def logist_local_run(X,Y,cc=1):
    X_train, X_test, Y_train, Y_test = train_test_split(X, Y, test_size=0.20, random_state=5)
    clf2 = LogisticRegression(C=cc)
    clf2.fit(X_train, Y_train)
    pred2 = clf2.predict(X_test)
    matrix2 = confusion_matrix(Y_test, pred2)


    precision_svc=precision_score(Y_test, pred2,average='weighted')
    recall_svc = recall_score(Y_test, pred2,average='weighted')
    accuracy_svc = accuracy_score(Y_test, pred2)
    f1_svc =f1_score(Y_test, pred2,average='weighted')

    return_list = [accuracy_svc,f1_svc,precision_svc,recall_svc]
    return return_list



def Tune_Logisit(dataScope):
    X=dataScope[0]
    Y = dataScope[1]
    for c in range(0,101):
        cc = 0.5+c/100
        resultList = logist_local_run(X,Y,cc)
        print("c: %f\taccuracy: %f\tf1: %f\t precision: %f\t recall: %f"%(cc,resultList[0],resultList[1],resultList[2],resultList[3]))




if __name__ == "__main__":
    sysArgv=sys.argv
    lenArg=len(sysArgv)
    if lenArg!=3 and lenArg!=1:
        print("Wrong Arguements!")
        print("USAGE: python ThisScriptName.py csvfileName featureName ")
        
    else:
        dataScope = []
        if lenArg==1:
            print("Using default parameters!")
            dataScope = load_AM_data()
        else:
            print("csv file name: %s \nfeature numbers: %s  "%(sysArgv[1],sysArgv[2]))
            dataScope = load_AM_data(int(sysArgv[2]),sysArgv[1])




        print("Tuning parameters of Logisit: ")
        Tune_Logisit(dataScope)


        print("end of work!")