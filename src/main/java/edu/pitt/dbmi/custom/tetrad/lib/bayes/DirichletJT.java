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
package edu.pitt.dbmi.custom.tetrad.lib.bayes;

import edu.cmu.tetrad.bayes.BayesPm;
import edu.cmu.tetrad.bayes.DirichletBayesIm;
import edu.cmu.tetrad.bayes.DirichletEstimator;
import edu.cmu.tetrad.data.DataModel;
import edu.cmu.tetrad.data.DataSet;
import edu.cmu.tetrad.graph.Graph;
import edu.cmu.tetrad.graph.Node;
import edu.pitt.dbmi.custom.tetrad.lib.util.TetradUtils;
import java.util.Arrays;

/**
 *
 * Apr 30, 2020 5:40:05 PM
 *
 * @author Kevin V. Bui (kvb2@pitt.edu)
 */
public class DirichletJT {

    public DirichletJT() {
    }

    public double[][] estimateDoProbAvg(int y, int x, int[] z, Graph graph, DataModel dataModel, double symmetricAlpha, int nSamples) {
        BayesPm bayesPm = TetradUtils.createBayesPm(dataModel, graph);

        Node yNode = getNode(y, bayesPm);
        Node xNode = getNode(x, bayesPm);
        Node[] zNodes = getNodes(z, bayesPm);

        int xDim = bayesPm.getNumCategories(xNode);
        int yDim = bayesPm.getNumCategories(yNode);
        double[][] results = new double[yDim][xDim];

        DirichletBayesIm prior = DirichletBayesIm.symmetricDirichletIm(bayesPm, symmetricAlpha);
        DirichletBayesIm posterior = DirichletEstimator.estimate(prior, (DataSet) dataModel);

        for (int n = 0; n < nSamples; n++) {
            JunctionTree jt = new JunctionTree(DirichletSampler.sampleFromPosterior(posterior));
            for (int yIndex = 0; yIndex < yDim; yIndex++) {
                for (int xIndex = 0; xIndex < xDim; xIndex++) {
                    double prob = 0;
                    int cardinality = getCardinality(z, bayesPm);
                    int size = zNodes.length;
                    int[] values = new int[size];
                    for (int i = 0; i < cardinality; i++) {
                        int[] parents = combine(x, z);
                        int[] parentValues = combine(xIndex, values);

                        double zProbJoint = jt.getJointProbability(z, values);
                        double condProb = jt.getConditionalProbability(y, yIndex, parents, parentValues);

                        prob += zProbJoint * condProb;

                        updateValues(size, values, zNodes, bayesPm);
                    }
                    results[yIndex][xIndex] += prob;
                }
            }
        }

        // compute average
        for (double[] result : results) {
            for (int j = 0; j < results.length; j++) {
                result[j] /= nSamples;
            }
        }

        return results;
    }

    public double[][] estimateDoProb(int y, int x, int[] z, BayesPm bayesPm, DirichletBayesIm posterior) {
        Node yNode = getNode(y, bayesPm);
        Node xNode = getNode(x, bayesPm);
        Node[] zNodes = getNodes(z, bayesPm);

        int xDim = bayesPm.getNumCategories(xNode);
        int yDim = bayesPm.getNumCategories(yNode);
        double[][] results = new double[yDim][xDim];

        JunctionTree jt = new JunctionTree(posterior);
        for (int yIndex = 0; yIndex < yDim; yIndex++) {
            for (int xIndex = 0; xIndex < xDim; xIndex++) {
                double prob = 0;
                int cardinality = getCardinality(z, bayesPm);
                int size = zNodes.length;
                int[] values = new int[size];
                for (int i = 0; i < cardinality; i++) {
                    int[] parents = combine(x, z);
                    int[] parentValues = combine(xIndex, values);

                    double zProbJoint = jt.getJointProbability(z, values);
                    double condProb = jt.getConditionalProbability(y, yIndex, parents, parentValues);

                    prob += zProbJoint * condProb;

                    updateValues(size, values, zNodes, bayesPm);
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

    private void updateValues(int size, int[] values, Node[] nodes, BayesPm bayesPm) {
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

    private int getCardinality(int[] nodes, BayesPm bayesPm) {
        int count = 1;

        for (int node : nodes) {
            count *= bayesPm.getNumCategories(bayesPm.getNode(node));
        }

        return count;
    }

    private Node[] getNodes(int[] nodeIndices, BayesPm bayesPm) {
        return Arrays.stream(nodeIndices)
                .mapToObj(bayesPm::getNode)
                .toArray(Node[]::new);
    }

    private Node getNode(int nodeIndex, BayesPm bayesPm) {
        return bayesPm.getNode(nodeIndex);
    }

}
