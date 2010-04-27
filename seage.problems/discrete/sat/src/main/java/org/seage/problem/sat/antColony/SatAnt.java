/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.seage.problem.sat.antColony;

import java.util.Vector;
import org.seage.metaheuristic.antcolony.Ant;
import org.seage.metaheuristic.antcolony.AntBrain;
import org.seage.metaheuristic.antcolony.Edge;
import org.seage.metaheuristic.antcolony.Graph;
import org.seage.metaheuristic.antcolony.Node;

/**
 *
 * @author Zagy
 */
 public class SatAnt extends Ant {
    private SatAntBrain _satBrain;

    public SatAnt(Graph graph, double qantumPheromone, AntBrain brain) {
        super(graph, qantumPheromone, brain);
        _satBrain = (SatAntBrain)brain;
        Node start = _graph.getNodeList().get(0);
        _startPosition = start;
        _currentPosition = start;
        _visited.add(start);
    }

    @Override
    public Vector<Edge> explore() {
        for (int i = 0; i < (_graph.getNodeList().size() - 1)/2; i++) {
            updatePosition(_brain.getNextEdge(null, _currentPosition));
        }
        _distanceTravelled = _satBrain.solutionCost(_path);
        leavePheromone();
        return _path;
    }

    @Override
    protected void updatePosition(Edge chosedEdge) {
        _path.add(chosedEdge);
        _visited.add(chosedEdge.getNode2());
        _currentPosition = chosedEdge.getNode2();
    }

    @Override
    public void leavePheromone() {
        for (Edge edge : _path) {
            edge.addLocalPheromone(_qantumPheromone / (_distanceTravelled));
        }
    }
}
