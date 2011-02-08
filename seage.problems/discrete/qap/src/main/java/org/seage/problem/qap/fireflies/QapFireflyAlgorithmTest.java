/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://seage.sourceforge.net/license/cpl-v10.html
 *
 * Contributors:
 *     Richard Malek
 *     - Initial implementation
 */
package org.seage.problem.qap.fireflies;

import org.seage.problem.qap.fireflies.*;
import org.seage.metaheuristic.fireflies.*;
import org.seage.problem.qap.*;

/**
 *
 * @author Richard Malek
 */
public class QapFireflyAlgorithmTest implements FireflySearchListener
{
    
    private static String _dataPath = "data/tai12a.dat";
//    private static String _dataPath = "D:\\qap\\bur26a.dat";

    public static void main(String[] args)
    {
        try
        {
            new QapFireflyAlgorithmTest().run(_dataPath);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void run(String path) throws Exception
    {
        Double[][][] facilityLocations = FacilityLocationProvider.readFacilityLocations(path);

        System.out.println("Loading an instance from path: " + path);
        System.out.println("Number of facilities/location: " + facilityLocations.length+","+facilityLocations[0].length+","+facilityLocations[0][0].length);

        boolean _withDecreasingRandomness=true;
        boolean _withHillClimbingBestSolution=true;
        double _initialIntensity=1;
        double _initialRandomness=0.7;
        double _finalRandomness=0.1;
        double _absorption=0.001;
        double _timeStep=0.1;
        int populationSize = 100;
        boolean _maximizing = false;
        int iterationsToGo = 100;

        QapFireflyOperator qfo = new QapFireflyOperator(_initialIntensity,_initialRandomness,_finalRandomness,_absorption,_timeStep,_withDecreasingRandomness);
        QapFireflyOperator._facilityLocations=facilityLocations;
        QapObjectiveFunction qof = new QapObjectiveFunction(facilityLocations);

        FireflySearch fs = new FireflySearch(qfo, qof);
        fs.addEFASearchListener(this);
        fs.setWithDecreasingRandomness(_withDecreasingRandomness);
        fs.setWithHillClimbingBestSolution(_withHillClimbingBestSolution);
        fs.setInitialIntensity(_initialIntensity);
        fs.setInitialRandomness(_initialRandomness);
        fs.setFinalRandomness(_finalRandomness);
        fs.setAbsorption(_absorption);
        fs.setTimeStep(_timeStep);
        fs.setPopulationCount(populationSize);
        fs.setMaximizing(_maximizing);
        fs.setIterationsToGo(iterationsToGo);
        System.out.println("Length of solution"+(new QapRandomSolution(facilityLocations)._assign.length));
        fs.startSolving(generateInitialSolutions(facilityLocations,populationSize));
    }

    private Solution[] generateInitialSolutions(Double[][][] fl, int count) throws Exception
    {
        Solution[] result = new Solution[count];
//        result[0]=new QapGreedyStartSolution(fl);
        for(int i=0;i<count;i++){
            result[i]=new QapRandomSolution(fl);
        }
        return result;
    }

    public void FireflySearchStarted(FireflySearchEvent e) {
        System.out.println("EFA Algorithm for QAP started.");
    }

    public void FireflySearchStopped(FireflySearchEvent e) {
        System.out.println("EFA Algorithm for QAP stopped.");
    }

    public void newBestSolutionFound(FireflySearchEvent e) {
        System.out.println("New best: " + e.getEFASearch().getBestSolution().getObjectiveValue()[0]);
    }

    public void noChangeInValueIterationMade(FireflySearchEvent e) {
        System.out.println("EFA Algorithm for QAP - no change in value iteration made.");
    }

    public void newCurrentSolutionFound(FireflySearchEvent e) {
        System.out.println("EFA Algorithm for QAP - new current solution found.");
    }

    public void unimprovingMoveMade(FireflySearchEvent e) {
        System.out.println("EFA Algorithm for QAP - unimproving move made.");
    }

    public void improvingMoveMade(FireflySearchEvent e) {
        System.out.println("EFA Algorithm for QAP - improving move made.");
    }

    public void noChangeInValueMoveMade(FireflySearchEvent e) {
        System.out.println("EFA Algorithm for QAP - no change in value made.");
    }
}
