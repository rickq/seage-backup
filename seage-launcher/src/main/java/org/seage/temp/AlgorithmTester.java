/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors

 * This file is part of SEAGE.

 * SEAGE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * SEAGE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with SEAGE. If not, see <http://www.gnu.org/licenses/>.
 *
 */

/**
 * Contributors:
 *     Richard Malek
 *     - Initial implementation
 */
package org.seage.temp;

import java.io.File;
import java.util.Map;
import org.seage.aal.reporter.AlgorithmReport;
import org.seage.aal.algorithm.IAlgorithmAdapter;
import org.seage.aal.algorithm.IAlgorithmFactory;
import org.seage.aal.algorithm.IPhenotypeEvaluator;
import org.seage.aal.algorithm.IProblemProvider;
import org.seage.aal.data.ProblemConfig;
import org.seage.aal.data.ProblemInfo;
import org.seage.aal.data.ProblemInstanceInfo;
import org.seage.aal.algorithm.ProblemProvider;
import org.seage.aal.data.AlgorithmParams;
import org.seage.data.DataNode;
import org.seage.data.xml.XmlHelper;
import org.seage.experimenter.config.DefaultConfigurator;

/**
 *
 * @author RMalek
 */
public class AlgorithmTester {

    private Map<String, IProblemProvider> _providers ;
    
    public AlgorithmTester() throws Exception
    {
        _providers = ProblemProvider.getProblemProviders();
    }

    public void test() throws Exception
    {
        System.out.println("Testing algorithms:");
        System.out.println("-------------------");        

        for(String problemId : _providers.keySet())
        {          
            testProblem(_providers.get(problemId));
        }
    }

    public void test(String problemId)
    {
        System.out.println("Testing algorithms:");
        System.out.println("-------------------");

        testProblem(_providers.get(problemId));
    }
    
    public void test(String problemId, String algorithmId) throws Exception
    {
        System.out.println("Testing algorithms:");
        System.out.println("-------------------");
        
        IProblemProvider provider = _providers.get(problemId);
        ProblemInfo pi = provider.getProblemInfo();
        String problemName = pi.getValueStr("name");
        System.out.println(problemName);

        testProblem(provider, pi, pi.getDataNode("Algorithms").getDataNodeById(algorithmId));
    }

    private void testProblem( IProblemProvider provider)
    {
        try
        {            
            ProblemInfo pi = provider.getProblemInfo();
            String problemName = pi.getValueStr("name");
            System.out.println(problemName);

            for(DataNode alg : pi.getDataNode("Algorithms").getDataNodes())
            {
                testProblem(provider, pi, alg);
                //System.out.println("\t"+alg.getValueStr("id")/*+" ("+alg.getValueStr("id")+")"*/);
            }
        }
        catch(Exception ex)
        {
            //System.err.println(problemId+": "+ex.getLocalizedMessage());
            ex.printStackTrace();
        }
    }
    
    private void testProblem(IProblemProvider provider, ProblemInfo pi, DataNode alg)
    {
        String algName = "";
        try {
            algName = alg.getValueStr("name");
            System.out.print("\t" + algName);
            
            ProblemConfig config = new DefaultConfigurator().prepareConfigs(pi, alg.getValueStr("id"), 1)[0];
            
            IAlgorithmFactory factory = provider.getAlgorithmFactory(alg.getValueStr("id"));
            
            ProblemInstanceInfo instance = provider.initProblemInstance(config);
            IAlgorithmAdapter algorithm = factory.createAlgorithm(instance, config);
            AlgorithmParams algNode = config.getAlgorithmParams();
            Object[][] solutions = provider.generateInitialSolutions(algNode.getDataNode("Parameters").getValueInt("numSolutions"), instance, 1);
            algorithm.solutionsFromPhenotype(solutions);
            algorithm.setParameters(algNode);
            algorithm.startSearching();
            solutions = algorithm.solutionsToPhenotype();
            algorithm.solutionsFromPhenotype(solutions);
            algorithm.startSearching();

            System.out.printf("%"+(50-algName.length())+"s","OK\n");

        } catch (Exception ex) {
            System.out.printf("%"+(52-algName.length())+"s","FAIL\n");
            //System.out.println("\t"+"FAIL");
            ex.printStackTrace();
            //System.err.println(problemId+"/"+alg.getValueStr("id")+": "+ex.toString());
        }
    }

    public void runFromConfigFile(String configPath) throws Exception
    {
        ProblemConfig config = new ProblemConfig(XmlHelper.readXml(new File(configPath)));
        String problemID = config.getDataNode("Problem").getValueStr("id");
        String algorithmID = config.getDataNode("Algorithm").getValueStr("id");

        // provider and factory
        IProblemProvider provider = ProblemProvider.getProblemProviders().get(problemID);
        IAlgorithmFactory factory = provider.getAlgorithmFactory(algorithmID);

        // problem instance
        ProblemInstanceInfo instance = provider.initProblemInstance(config);

        // algorithm
        IAlgorithmAdapter algorithm = factory.createAlgorithm(instance, config);

        AlgorithmParams algNode = config.getAlgorithmParams();
        Object[][] solutions = provider.generateInitialSolutions(algNode.getDataNode("Parameters").getValueInt("numSolutions"), instance, 1);


        System.out.printf("%s: %4s %s\n", "Problem","", problemID);
        System.out.printf("%s: %2s %s\n", "Algorithm","", algorithmID);
        System.out.printf("%s: %3s %s\n", "Instance","", instance);
        System.out.println("Running ...");
        algorithm.solutionsFromPhenotype(solutions);
        algorithm.setParameters(algNode);
        algorithm.startSearching();
        solutions = algorithm.solutionsToPhenotype();

        // phenotype evaluator
        IPhenotypeEvaluator evaluator = provider.initPhenotypeEvaluator();
        double[] result = evaluator.evaluate(solutions[0], instance);

        System.out.printf("%s: %5s %s\n", "Result","", result[0]);
        //System.out.println(": " + result[0]);

        System.out.printf("%s: %3s ", "Solution","");
        for(int i=0;i<solutions[0].length;i++)
            System.out.print(solutions[0][i]+" ");
        System.out.println();

        AlgorithmReport report = algorithm.getReport();
        report.save("output/"+System.currentTimeMillis()+".xml");
    }
}
