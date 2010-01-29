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
package org.seage.metaheuristic.particles;

/**
 *
 * @author Jan Zmatlik
 */
public interface IMoveManager {
    
    public void calculateNewVelocity(Particle particle, Particle localMinimum, Particle globalMinimum, double alpha, double beta);

    public void calculateNewLocations(Particle particle);
}
