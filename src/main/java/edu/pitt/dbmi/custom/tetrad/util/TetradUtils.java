/*
 * Copyright (C) 2020 University of Pittsburgh.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package edu.pitt.dbmi.custom.tetrad.util;

import edu.cmu.tetrad.bayes.BayesIm;
import edu.cmu.tetrad.bayes.BayesPm;
import edu.cmu.tetrad.bayes.DirichletBayesIm;
import edu.cmu.tetrad.bayes.EmBayesEstimator;
import edu.cmu.tetrad.data.DataModel;
import edu.cmu.tetrad.data.DataSet;
import edu.cmu.tetrad.graph.Dag;
import edu.cmu.tetrad.graph.Edge;
import edu.cmu.tetrad.graph.Endpoint;
import edu.cmu.tetrad.graph.Graph;
import edu.cmu.tetrad.graph.Node;

/**
 *
 * Mar 5, 2020 11:35:24 AM
 *
 * @author Kevin V. Bui (kvb2@pitt.edu)
 */
public final class TetradUtils {

    private TetradUtils() {
    }

    public static BayesPm createBayesPm(DataModel dataModel, Graph graph) {
        Dag dag = new Dag(dataModel.getVariables());
        (new Dag(graph)).getEdges().forEach(edge -> {
            Node node1 = dag.getNode(edge.getNode1().getName());
            Node node2 = dag.getNode(edge.getNode2().getName());
            Endpoint endpoint1 = edge.getEndpoint1();
            Endpoint endpoint2 = edge.getEndpoint2();

            dag.addEdge(new Edge(node1, node2, endpoint1, endpoint2));
        });

        return new BayesPm(dag);
    }

    public static DirichletBayesIm createDirichletBayesIm(BayesPm bayesPm, double symmetricAlpha) {
        return DirichletBayesIm.symmetricDirichletIm(bayesPm, symmetricAlpha);
    }

    public static BayesIm createEmBayesEstimator(DataModel dataModel, BayesPm bayesPm) {
        return (new EmBayesEstimator(bayesPm, (DataSet) dataModel)).getEstimatedIm();
    }

}
