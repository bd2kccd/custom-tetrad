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
package edu.pitt.dbmi.custom.tetrad.bayes;

import edu.cmu.tetrad.bayes.BayesIm;
import edu.cmu.tetrad.bayes.JunctionTreeAlgorithm;
import edu.cmu.tetrad.data.DataModel;
import edu.cmu.tetrad.graph.Graph;

/**
 *
 * Apr 30, 2020 3:58:25 PM
 *
 * @author Kevin V. Bui (kvb2@pitt.edu)
 */
public class JunctionTree extends JunctionTreeAlgorithm {

    static final long serialVersionUID = 23L;

    public JunctionTree(Graph graph, DataModel dataModel) {
        super(graph, dataModel);
    }

    public JunctionTree(BayesIm bayesIm) {
        super(bayesIm);
    }

    public class JointProbabilityDistribution {

        private final int[][] values;

        private final double[] probabilities;

        public JointProbabilityDistribution(int[][] values, double[] probabilities) {
            this.values = values;
            this.probabilities = probabilities;
        }

        public int[][] getValues() {
            return values;
        }

        public double[] getProbabilities() {
            return probabilities;
        }

    }

}
