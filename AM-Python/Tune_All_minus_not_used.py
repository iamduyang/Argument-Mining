# -*- coding:utf-8 -*-
"""
# Author:    Yang Du
# Email:     duyang2015 (-A-T-) iscas dot ac dot cn
# Date:      2018-05-09
# Purpose:
#------------------------------------------------------------#
# Copyright (C) 2018      Yang DU
# This code is freely available for non-commercial purposes
#------------------------------------------------------------#
"""

import sys

from pandas import read_csv
import numpy as np

from sklearn.model_selection import train_test_split

from sklearn.linear_model import LogisticRegression
from sklearn.ensemble import RandomForestClassifier
from sklearn.svm import SVC
from sklearn.tree import DecisionTreeClassifier
from sklearn.naive_bayes import GaussianNB

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
    #print(array[0,:])
    X = array[:,0:featureNum]
    Y = array[:,featureNum]
    return [X,Y]



def svc_local_run(X,Y,cc=1,gamma_para = 1,kernel_para ='linear'):
    X_train, X_test, Y_train, Y_test = train_test_split(X, Y, test_size=0.20, random_state=5)
    clf2 = SVC(C=cc, gamma=gamma_para,kernel=kernel_para)
    clf2.fit(X_train, Y_train)
    pred2 = clf2.predict(X_test)
    matrix2 = confusion_matrix(Y_test, pred2)


    precision_svc=precision_score(Y_test, pred2,average='weighted')
    recall_svc = recall_score(Y_test, pred2,average='weighted')
    accuracy_svc = accuracy_score(Y_test, pred2)
    f1_svc =f1_score(Y_test, pred2,average='weighted')

    return_list = [accuracy_svc,f1_svc]
    return return_list


def Tune_SVM(dataScope):
    X=dataScope[0]
    Y = dataScope[1]
    gamma_para = 1
    kernel_para = 'linear'
    for c in range(0,101):
        cc = 0.5+c/100
        resultList = svc_local_run(X,Y,cc,gamma_para,kernel_para)
        print("c: %f\taccuracy: %f\tf1: %f"%(cc,resultList[0],resultList[1]))


def random_forest_local_run(X,Y,n_estimators_para=100):
    '''
    n_estimators_para:
        The number of trees in the forest.
        default=10
    '''
    X_train, X_test, Y_train, Y_test = train_test_split(X, Y, test_size=0.20, random_state=5)
    clf2 = RandomForestClassifier(n_estimators=n_estimators_para)
    clf2.fit(X_train, Y_train)
    pred2 = clf2.predict(X_test)
    matrix2 = confusion_matrix(Y_test, pred2)


    precision_svc=precision_score(Y_test, pred2,average='weighted')
    recall_svc = recall_score(Y_test, pred2,average='weighted')
    accuracy_svc = accuracy_score(Y_test, pred2)
    f1_svc =f1_score(Y_test, pred2,average='weighted')

    return_list = [accuracy_svc,f1_svc]
    return return_list

def Tune_random_forest(dataScope):
    X=dataScope[0]
    Y = dataScope[1]
    gamma_para = 1
    kernel_para = 'linear'
    for xx in range(50,150):
        n_estimators_para =xx
        resultList = random_forest_local_run(X,Y,n_estimators_para)
        print("n_estimators_para: %d\taccuracy: %f\tf1: %f"%(n_estimators_para,resultList[0],resultList[1]))


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

    return_list = [accuracy_svc,f1_svc]
    return return_list



def Tune_Logisit(dataScope):
    X=dataScope[0]
    Y = dataScope[1]
    for c in range(0,101):
        cc = 0.5+c/100
        resultList = logist_local_run(X,Y,cc)
        print("c: %f\taccuracy: %f\tf1: %f"%(cc,resultList[0],resultList[1]))



def decision_tree_local_run(X,Y,cc=1):
    X_train, X_test, Y_train, Y_test = train_test_split(X, Y, test_size=0.20, random_state=5)
    clf2 = DecisionTreeClassifier()
    clf2.fit(X_train, Y_train)
    pred2 = clf2.predict(X_test)
    matrix2 = confusion_matrix(Y_test, pred2)


    precision_svc=precision_score(Y_test, pred2,average='weighted')
    recall_svc = recall_score(Y_test, pred2,average='weighted')
    accuracy_svc = accuracy_score(Y_test, pred2)
    f1_svc =f1_score(Y_test, pred2,average='weighted')

    return_list = [accuracy_svc,f1_svc]
    return return_list



def Tune_decision_tree(dataScope):
    X=dataScope[0]
    Y = dataScope[1]

    resultList = decision_tree_local_run(X,Y)
    print("accuracy: %f\tf1: %f"%(resultList[0],resultList[1]))


def GaussianNB_local_run(X,Y,cc=1):
    X_train, X_test, Y_train, Y_test = train_test_split(X, Y, test_size=0.20, random_state=5)
    clf2 = GaussianNB()
    clf2.fit(X_train, Y_train)
    pred2 = clf2.predict(X_test)
    matrix2 = confusion_matrix(Y_test, pred2)


    precision_svc=precision_score(Y_test, pred2,average='weighted')
    recall_svc = recall_score(Y_test, pred2,average='weighted')
    accuracy_svc = accuracy_score(Y_test, pred2)
    f1_svc =f1_score(Y_test, pred2,average='weighted')

    return_list = [accuracy_svc,f1_svc]
    return return_list



def Tune_GaussianNB(dataScope):

    X=dataScope[0]
    Y = dataScope[1]

    resultList = GaussianNB_local_run(X,Y)
    print("accuracy: %f\tf1: %f"%(resultList[0],resultList[1]))


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


        print("Tuning parameters of SVM: ")
        Tune_SVM(dataScope)

        print("Tuning parameters of random_forest: ")
        Tune_random_forest(dataScope)

        print("Tuning parameters of Logisit: ")
        Tune_Logisit(dataScope)

        print("Result of decision tree: ")
        Tune_decision_tree(dataScope)

        print("result of GaussianNB")
        Tune_GaussianNB(dataScope)

        print("end of work!")