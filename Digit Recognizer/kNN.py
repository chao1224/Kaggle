from numpy import *
from os import listdir
import csv
import operator


def classify0(inX, dataSet, labels, k=5):
    dataSetSize = dataSet.shape[0]
    diffMat = tile(inX,( dataSetSize,1)) - dataSet
    sqDiffMat = diffMat ** 2
    sqDistances = sqDiffMat.sum(axis=1)
    distances = sqDistances ** 0.5

    sortedDistIndicies = distances.argsort()
    classCount = {}
    for i in range(k):
        tempLabel = labels[sortedDistIndicies[i]]
        classCount[tempLabel] = classCount.get(tempLabel,0) + 1
    sortedClassCount = sorted(classCount.iteritems(), key=operator.itemgetter(1), reverse=True)
    return sortedClassCount[0][0]

def Train2Vector(filename="train.csv"):
    fr = open(filename)
    filelines = fr.readlines()
    del filelines[0]
    lenlines = len(filelines)
    returnVect = zeros((lenlines,784))
    labellist = [0]*lenlines
    for i in range(lenlines):
        lineStr = filelines[i]
        linearr = lineStr.split(',')
        labellist[i] = linearr[0]            
        for j in range(1,785):
            returnVect[i][j-1] = linearr[j]
    return returnVect,labellist

def Test2Vector(filename="test.csv"):
    fr = open(filename)
    lines = fr.readlines()
    del lines[0]
    linesNumber = len(lines)
    vector = zeros((linesNumber, 784))
    for i in range(linesNumber):
        lineStr = lines[i]
        lineArr = lineStr.split(',')
        for j in range(784):
            vector[i][j] = lineArr[j]
    return vector

def test():
    testSet = Test2Vector()
    trainSet, trainLabel = Train2Vector()
    testSetSize = testSet.shape[0]
    
    csvFile = file("result3.csv","wb")
    writer = csv.writer(csvFile)
    for i in range(testSetSize):
        data = testSet[i]
        res = classify0(data,trainSet,trainLabel,3)
        val = [i+1, res]
        writer.writerow(val)
    csvFile.close()

    
       
