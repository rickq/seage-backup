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
package org.seage.metaheuristic.genetics;

import java.util.*;

/**
 * @author Richard Malek (original)
 */
public class Population
{
    private ArrayList<Subject> _population;

    public Population()
    {
        _population = new ArrayList<Subject>();
    }

    public void addSubject(Subject subject)
    {
        _population.add(subject);
    }

    public Subject getSubject(int ix) throws Exception
    {
        try
        {
            return (Subject)_population.get(ix);
        }
        catch (Exception ex)
        {
            throw ex;
        }
    }

    public void removeAll()
    {
        _population.clear();
    }

    public int getSize()
    {
        return _population.size();
    }

    public List<Subject> getList()
    {
        return _population;
    }

    public void removeTwins() throws Exception
    {
        try
        {
            ArrayList<Subject> newPopulation = new ArrayList<Subject>();
            newPopulation.add(getSubject(0));
            for (int i = 1; i < _population.size(); i++)
            {
                Subject prev = (Subject)_population.get(i - 1);
                Subject curr = (Subject)_population.get(i);
                if (curr.hashCode() != prev.hashCode())
                {
                    newPopulation.add(curr);
                }
            }
            _population.clear();
            _population.addAll(newPopulation);
        }
        catch (Exception ex)
        {
            throw ex;
        }
    }

    public void resize(int newLength)
    {
        if(getSize() > newLength)
            _population = new ArrayList<Subject>(_population.subList(0, newLength));
    }

    public Subject getBestSubject()
    {
        return (Subject)_population.get(0);
    }

    public void mergePopulation(Population population)
    {
        _population.addAll(population._population);
    }

    public Subject[] getSubjects() throws Exception
    {
        return getSubjects(_population.size());
    }

    public Subject[] getSubjects(int numSubjects) throws Exception
    {
        try
        {
            int length = _population.size();
            if (length > numSubjects)
                return (Subject[])_population.subList(0,numSubjects).toArray(new Subject[0]);
            else
                return (Subject[])_population.toArray(new Subject[0]);
        }
        catch (Exception ex)
        {
            throw ex;
        }
    }
}
