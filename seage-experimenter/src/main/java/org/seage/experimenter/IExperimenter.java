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
 * 	   Richard Malek
 * 	   - Interface definition
 */
package org.seage.experimenter;

public interface IExperimenter {
    void runFromConfigFile(String configPath) throws Exception;
    void runExperiment(int numRuns, long timeoutS, String problemID) throws Exception ;
    void runExperiment(int numRuns, long timeoutS, String problemID, String[] algorithmIDs) throws Exception ;
    void runExperiment(int numRuns, long timeoutS, String problemID, String algorithmID, String instanceID) throws Exception;
    void runExperiment(int numRuns, long timeoutS, String problemID, String algorithmID, String[] instanceIDs) throws Exception;
    void runExperiment(int numRuns, long timeoutS, String problemID, String[] algorithmIDs, String[] instanceIDs) throws Exception;
}
