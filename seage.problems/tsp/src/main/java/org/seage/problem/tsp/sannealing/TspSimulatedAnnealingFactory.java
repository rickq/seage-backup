/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://seage.sourceforge.net/license/cpl-v10.html
 *
 * Contributors:
 *     Jan Zmatlik
 *     - Initial implementation
 */
package org.seage.problem.tsp.sannealing;

import org.seage.aal.IAlgorithmAdapter;
import org.seage.aal.IAlgorithmFactory;
import org.seage.aal.sannealing.SimulatedAnnealingAdapter;
import org.seage.data.DataNode;
import org.seage.metaheuristic.sannealing.Solution;
import org.seage.problem.tsp.City;

/**
 *
 * @author Jan Zmatlik
 */
public class TspSimulatedAnnealingFactory implements IAlgorithmFactory
{
    //private DataNode _algParams;
    private City[] _cities;

    public TspSimulatedAnnealingFactory(DataNode params, City[] cities)
    {
        //_algParams = params;
        _cities = cities;
    }

    public IAlgorithmAdapter createAlgorithm() throws Exception
    {
        IAlgorithmAdapter algorithm;

        algorithm = new SimulatedAnnealingAdapter( 
                (Solution) new TspSolution( _cities ),
                new TspObjectiveFunction(),
                new TspMoveManager(),
                false,
                "");

        //Object[][] solutions = TspProblemSolver.generateInitialSolutions(_cities.length, _algParams.getValueInt("numSolution"));
        //algorithm.solutionsFromPhenotype(solutions);
        return algorithm;
    }

}
