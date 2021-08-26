# -*- coding:utf-8 -*-
"""
# Author:    Yang Du
# Email:     duyang2015 (-A-T-) iscas dot ac dot cn
# Date:      2018-06-09
# Purpose:
#------------------------------------------------------------#
# Copyright (C) 2017       Yang DU
# This code is freely available for non-commercial purposes
#------------------------------------------------------------#
"""

import sys
import os




def various_feature_Others(feature_numberList):
    for feature_number in feature_numberList:
        exe_string = "python Tune_Other2.py "+str(feature_number)+".csv "+str(feature_number)
        #print(exe_string)
        os.system(exe_string)

if __name__ == "__main__":
    feature_numberList =[500,800,1000]
    various_feature_Others(feature_numberList)


    print("end of work!")