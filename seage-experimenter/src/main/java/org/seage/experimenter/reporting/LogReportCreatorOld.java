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
package org.seage.experimenter.reporting;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.util.Arrays;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.seage.data.DataNode;
import org.seage.data.file.FileHelper;
import org.seage.data.xml.XmlHelper;

import com.rapidminer.Process;
import com.rapidminer.RapidMiner;

/**
 *
 * @author rick
 */
public class LogReportCreatorOld implements ILogReport {
    
    private static String _logPath = "output";
    private static String _reportPath = "report";
    
    public LogReportCreatorOld()
    {
        RapidMiner.setExecutionMode(RapidMiner.ExecutionMode.COMMAND_LINE);
        RapidMiner.init();
    }
    
   
    public void report() throws Exception
    {
        report0();
        reportRapidMiner();
        //report1();
    }
    
    private void reportRapidMiner() throws Exception
    {
        Process process = new Process(new File("processDefifnition.xml"));
        
        System.out.println(process.getRootOperator().createProcessTree(0));
                        
        process.run();

//        Collection<Operator> ops = process.getAllOperators();
//        ExampleSet ex = null;
//
//        for(Operator op : ops)
//        {
//            System.out.println(op.getName());
//            if(op.getName().equals("Aggregate (2)"))
//                ex = op.getOutputPorts().getPortByName("example set output").getData();
//        }
//
//
//        System.out.println(ex);
    }
    
    
    
    private void report0() throws Exception
    {
        System.out.println("Creating reports 0...");
        
        File logDir = new File(_logPath);
        
        File reportDir = new File(_reportPath);
        
        if(reportDir.exists())
            FileHelper.deleteDirectory(reportDir);

        reportDir.mkdirs();
        
        File output = new File(reportDir.getPath()+"/report.csv");
        output.createNewFile();
        
        StreamResult outputStream = new StreamResult
                        ( new FileOutputStream(output));
        
        FilenameFilter filter = new FilenameFilter() {
            public boolean accept(File arg0, String arg1) {
                if(arg0.isDirectory()) 
                    return true;
                else 
                    return false;
            }
        };
        //DataNode xmlDoc = new DataNode("xml");
        
        for(String dirName : logDir.list(filter))
        {
            System.err.println(dirName);
            
            //File reportDirDir = new File(reportDir.getPath()+"/"+dirName);
            //reportDirDir.mkdirs();
            
            File logDirDir = new File(logDir.getPath()+"/"+dirName);
            Arrays.sort(logDirDir.list());
            
            for(String xmlFileName : logDirDir.list())
            {
                //System.out.println("\t"+xmlFileName);
                String xmlPath = logDir.getPath()+"/"+dirName+"/"+xmlFileName;
                //File xmlFile = new File();
                try{
                    //DataNode dn = XmlHelper.readXml(xmlFile);
                    //xmlDoc.putDataNodeRef(dn);
                    transform0(xmlPath, "report2csv_1.xsl", outputStream);
                }catch(Exception e)
                {
                    System.out.println(xmlPath);
                    //e.printStackTrace();
                }                
            }            
            
            
        }
        
        //XmlHelper.writeXml(xmlDoc, _reportPath+"/data.xml");
        
        //transform0(xmlDoc.toXml(), "report0.xsl", reportDir.getPath()+"/report.html");
        //transform0(xmlDoc.toXml(), "report2csv.xsl", reportDir.getPath()+"/report.csv");
        
    }
    
    private void transform0(String xmlPath, String xsltName, StreamResult outputStream) throws Exception
    {

                //XmlHelper.writeXml(xmlDoc, reportDir.getPath()+"/report.xml");
                
                System.setProperty("javax.xml.transform.TransformerFactory",
                    "net.sf.saxon.TransformerFactoryImpl");
                
                TransformerFactory tFactory = TransformerFactory.newInstance();
                Transformer transformer = tFactory.newTransformer(new javax.xml.transform.stream.StreamSource(getClass().getResourceAsStream(xsltName)));

                transformer.transform (new StreamSource(new FileInputStream(xmlPath)), outputStream);
                   //new javax.xml.transform.stream.StreamSource
                   //     (reportDir.getPath()+"/report.xml"),
                   

    }
    
    private void report1()
    {
        System.out.println("Creating reports 1...");
        
        File logDir = new File(_logPath);
        
        File reportDir = new File(_reportPath+"1");
        
        if(reportDir.exists())
            FileHelper.deleteDirectory(reportDir);
        
        reportDir.mkdirs();
        
        FilenameFilter filter = new FilenameFilter() {
            public boolean accept(File arg0, String arg1) {
                if(arg0.isDirectory()) 
                    return true;
                else 
                    return false;
            }
        };
        
        DataNode xmlDoc = new DataNode("xml");
        
        for(String dirName : logDir.list(filter))
        {
            //System.out.println(dirName);
            
            //File reportDirDir = new File(reportDir.getPath()+"/"+dirName);
            //reportDirDir.mkdirs();
            
            File logDirDir = new File(logDir.getPath()+"/"+dirName);
            
            
            for(String xmlFileName : logDirDir.list())
            {
                //System.out.println("\t"+xmlFileName);
                File xmlFile = new File(logDir.getPath()+"/"+dirName+"/"+xmlFileName);
                try{
                    DataNode report = XmlHelper.readXml(xmlFile); 
                    
                    String experimentID = getExperimentID(report);                    
                    DataNode experiment = touchDataNode(xmlDoc, "Experiment", experimentID);                    
                    
                    String problemID = report.getDataNode("Config").getDataNode("Problem").getValueStr("id");
                    DataNode problem = touchDataNode(experiment, "Problem", problemID);
                    
                    String instanceID = report.getDataNode("Config").getDataNode("Problem").getDataNode("Instance").getValueStr("name");
                    DataNode instance = touchDataNode(problem, "Instance", instanceID);
                    
                    //String algorithmID = report.getDataNode("Config").getDataNode("Algorithm").getValueStr("id");
                    //DataNode algorithm = touchDataNode(instance, "Algorithm", algorithmID);
                    
                    String configID = report.getDataNode("Config").getValueStr("configID");
                    DataNode config = touchDataNode(instance, "Config", configID);
                    
                    //config.putValue("algorithmID", algorithm.getValue("id"));
                    DataNode algorithm = report.getDataNode("Config").getDataNode("Algorithm");
                    if(!config.containsNode("Algorithm"))
                        config.putDataNodeRef(algorithm);
                    
                    String runID = getRuntID(report);
                    //touchDataNode(config, "Algorithm", runID);
                    DataNode run = touchDataNode(config, "Run", runID);
                    
                    DataNode algReport = report.getDataNode("AlgorithmReport");
                    String algID = algReport.getValueStr("algorithmID");
                    DataNode algReport2 = touchDataNode(run, "AlgorithmReport", algID);
                    algReport2.putDataNodeRef(algReport.getDataNode("Statistics"));

                    
                    
                    //experiment.putDataNode(problem);
                    //xmlDoc.putDataNode(experiment);
                }catch(Exception e)
                {
                    e.printStackTrace();
                }                
            }
            //XmlHelper.writeXml(xmlDoc, reportDirDir.getPath()+"/report.xml");
            
            
        }
        
        //XmlHelper.writeXml(xmlDoc, reportDir.getPath()+"/report.xml");
        
        try
            {          
                System.setProperty("javax.xml.transform.TransformerFactory",
                    "net.sf.saxon.TransformerFactoryImpl");

                TransformerFactory tFactory = TransformerFactory.newInstance();
                Transformer transformer = tFactory.newTransformer(new javax.xml.transform.stream.StreamSource(getClass().getResourceAsStream("report1.xsl")));

                transformer.transform
                  (new DOMSource(xmlDoc.toXml()),
                   //new javax.xml.transform.stream.StreamSource
                   //     (reportDir.getPath()+"/report.xml"),
                   new javax.xml.transform.stream.StreamResult
                        ( new FileOutputStream(reportDir.getPath()+"/report.html")));
            }
            catch(Exception e)
            {
                e.printStackTrace();
            } 
    }
    
    private String getExperimentID(DataNode report) throws Exception
    {
        if(report.containsValue("experimentID"))
            return report.getValueStr("experimentID");
        else
            return report.getDataNode("Config").getValueStr("runID");
    }
    
    private String getRuntID(DataNode report) throws Exception
    {
        if(report.containsValue("runID"))
            return report.getValueStr("runID");
        else
            return report.getDataNode("AlgorithmReport").getValueStr("created");
    }
    
    private DataNode touchDataNode(DataNode parent, String elemName, String elemID) throws Exception
    {
        DataNode result;
        if(!parent.containsNode(elemName) || parent.getDataNodeById(elemID)==null)
        {
            result = new DataNode(elemName);
            result.putValue("id", elemID);

            parent.putDataNodeRef(result);            
        }
        else
            result = parent.getDataNodeById(elemID);
        
        return result;
    }
}
