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

import edu.cmu.tetrad.bayes.BayesPm;
import edu.cmu.tetrad.bayes.DirichletBayesIm;
import edu.cmu.tetrad.bayes.DirichletEstimator;
import edu.cmu.tetrad.data.DataModel;
import edu.cmu.tetrad.data.DataSet;
import edu.cmu.tetrad.graph.Graph;
import edu.cmu.tetrad.graph.Node;
import edu.pitt.dbmi.custom.tetrad.util.TetradUtils;
import java.util.Arrays;

/**
 *
 * Apr 30, 2020 5:40:05 PM
 *
 * @author Kevin V. Bui (kvb2@pitt.edu)
 */
public class DirichletJT {

    private final DataSet dataSet;
    private final BayesPm bayesPm;
    private final DirichletBayesIm dirichletBayesIm;

    /**
     *
     * @param graph Bayesian network
     * @param dataModel data for the Bayesian network
     * @param symmetricAlpha the value that all Dirichlet parameters are
     * initially set to, which must be nonnegative, or Double.NaN if all
     * parameters should be set initially to "unspecified."
     */
    public DirichletJT(Graph graph, DataModel dataModel, double symmetricAlpha) {
        this.dataSet = (DataSet) dataModel;
        this.bayesPm = TetradUtils.createBayesPm((DataSet) dataModel, graph);
        this.dirichletBayesIm = TetradUtils.createDirichletBayesIm(bayesPm, symmetricAlpha);
    }

    public double[][] estimateDoProb(int y, int x, int[] z) {
        Node yNode = getNode(y);
        Node xNode = getNode(x);
        Node[] zNodes = getNodes(z);

        int xDim = bayesPm.getNumCategories(xNode);
        int yDim = bayesPm.getNumCategories(yNode);
        double[][] results = new double[yDim][xDim];

        for (int yIndex = 0; yIndex < yDim; yIndex++) {
            for (int xIndex = 0; xIndex < xDim; xIndex++) {
                double prob = 0;
                int cardinality = getCardinality(z);
                int size = zNodes.length;
                int[] values = new int[size];
                for (int i = 0; i < cardinality; i++) {
                    int[] parents = combine(x, z);
                    int[] parentValues = combine(xIndex, values);

                    JunctionTree jt = new JunctionTree(DirichletEstimator.estimate(dirichletBayesIm, dataSet));
                    double zProbJoint = jt.getJointProbability(z, values);
                    double condProb = jt.getConditionalProbability(y, yIndex, parents, parentValues);

                    prob += zProbJoint * condProb;

                    updateValues(size, values, zNodes);
                }
                results[yIndex][xIndex] = prob;
            }
        }

        return results;
    }

    private int[] combine(int i, int[] array) {
        int[] combined = new int[array.length + 1];

        combined[0] = i;
        System.arraycopy(array, 0, combined, 1, array.length);

        return combined;
    }

    private void updateValues(int size, int[] values, Node[] nodes) {
        int j = size - 1;
        values[j]++;
        while (j >= 0 && values[j] == bayesPm.getNumCategories(nodes[j])) {
            values[j] = 0;
            j--;
            if (j >= 0) {
                values[j]++;
            }
        }
    }

    private int getCardinality(int[] nodes) {
        int count = 1;

        for (int node : nodes) {
            count *= bayesPm.getNumCategories(bayesPm.getNode(node));
        }

        return count;
    }

    private Node[] getNodes(int[] nodeIndices) {
        return Arrays.stream(nodeIndices)
                .mapToObj(bayesPm::getNode)
                .toArray(Node[]::new);
    }

    private Node getNode(int nodeIndex) {
        return bayesPm.getNode(nodeIndex);
    }

}
