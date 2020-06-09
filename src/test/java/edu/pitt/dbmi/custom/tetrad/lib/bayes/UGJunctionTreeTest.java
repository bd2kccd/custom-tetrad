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

import edu.cmu.tetrad.bayes.BayesIm;
import edu.cmu.tetrad.bayes.BayesPm;
import edu.cmu.tetrad.data.DataModel;
import edu.cmu.tetrad.graph.Graph;
import edu.pitt.dbmi.custom.tetrad.lib.util.FileUtils;
import edu.pitt.dbmi.custom.tetrad.lib.util.StringUtils;
import edu.pitt.dbmi.custom.tetrad.lib.util.TetradUtils;
import java.io.IOException;
import java.nio.file.Paths;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 *
 * Jun 5, 2020 11:28:16 AM
 *
 * @author Kevin V. Bui (kvb2@pitt.edu)
 */
public class UGJunctionTreeTest {

    @Disabled
    @Test
    public void testUnconnectedGraphs2() throws IOException {
        String graphFile = this.getClass().getResource("/data/unconnect_graph2/graph.txt").getFile();
        String dataFile = this.getClass().getResource("/data/unconnect_graph2/data.txt").getFile();

        DataModel dataModel = FileUtils.readDiscreteData(Paths.get(dataFile));
        Graph graph = FileUtils.readGraph(Paths.get(graphFile));

        BayesPm bayesPm = TetradUtils.createBayesPm(dataModel, graph);
        BayesIm bayesIm = TetradUtils.createEmBayesEstimator(dataModel, bayesPm);

        JunctionTree jt = new JunctionTree(bayesIm);
        UGJunctionTree ugjt = new UGJunctionTree(bayesIm);

        System.out.println("================================================================================");
        System.out.println(graph);
        System.out.println("================================================================================");
        System.out.println(jt);
        System.out.println("================================================================================");
        System.out.println(ugjt);
        System.out.println("================================================================================");

        int x1 = bayesIm.getNodeIndex(bayesIm.getNode("X1"));
        int x2 = bayesIm.getNodeIndex(bayesIm.getNode("X2"));
        int x3 = bayesIm.getNodeIndex(bayesIm.getNode("X3"));
        int x4 = bayesIm.getNodeIndex(bayesIm.getNode("X4"));
        int x5 = bayesIm.getNodeIndex(bayesIm.getNode("X5"));

        System.out.println("================================================================================");
        int[] nodes = new int[]{x3, x4};
        int[] parents = new int[]{x1};
        int[] parentValues = new int[]{0};
        System.out.println("P(X3,X4|X1)");
        System.out.println("--------------------------------------------------------------------------------");
        JunctionTree.ProbabilityDistribution jtPd = jt.getConditionalProbabilities(nodes, parents, parentValues);
        print(jtPd.getValues(), jtPd.getProbabilities());

        System.out.println();

        UGJunctionTree.ProbabilityDistribution ugjtPd = ugjt.getConditionalProbabilities(nodes, parents, parentValues);
        print(ugjtPd.getValues(), ugjtPd.getProbabilities());

        System.out.println();
        System.out.println();
        System.out.println("P(X4,X5)");
        System.out.println("--------------------------------------------------------------------------------");
        double[] margProbs = jt.getMarginalProbability(x4);
        System.out.printf("X4: %s%n", StringUtils.strValueArray(margProbs, " "));

        margProbs = jt.getMarginalProbability(x5);
        System.out.printf("X5: %s%n", StringUtils.strValueArray(margProbs, " "));
        System.out.println("--------------------------------------------------------------------------------");
        nodes = new int[]{x4, x5};
        jtPd = jt.getJointProbabilityDistribution(nodes);
        print(jtPd.getValues(), jtPd.getProbabilities());

        System.out.println();

        ugjtPd = ugjt.getJointProbabilityDistribution(nodes);
        print(ugjtPd.getValues(), ugjtPd.getProbabilities());
        System.out.println("================================================================================");
    }

    @Disabled
    @Test
    public void testUnconnectedGraphs() throws IOException {
        String graphFile = this.getClass().getResource("/data/unconnect_graph/graph.txt").getFile();
        String dataFile = this.getClass().getResource("/data/unconnect_graph/data.txt").getFile();

        DataModel dataModel = FileUtils.readDiscreteData(Paths.get(dataFile));
        Graph graph = FileUtils.readGraph(Paths.get(graphFile));

        BayesPm bayesPm = TetradUtils.createBayesPm(dataModel, graph);
        BayesIm bayesIm = TetradUtils.createEmBayesEstimator(dataModel, bayesPm);

        JunctionTree jt = new JunctionTree(bayesIm);
        UGJunctionTree ugjt = new UGJunctionTree(bayesIm);

        System.out.println("================================================================================");
        System.out.println(graph);
        System.out.println("================================================================================");
        System.out.println(jt);
        System.out.println("================================================================================");
        System.out.println(ugjt);
        System.out.println("================================================================================");

        int x1 = bayesIm.getNodeIndex(bayesIm.getNode("X1"));
        int x2 = bayesIm.getNodeIndex(bayesIm.getNode("X2"));
        int x3 = bayesIm.getNodeIndex(bayesIm.getNode("X3"));
        int x4 = bayesIm.getNodeIndex(bayesIm.getNode("X4"));
        int x5 = bayesIm.getNodeIndex(bayesIm.getNode("X5"));
        int x6 = bayesIm.getNodeIndex(bayesIm.getNode("X6"));
        int x7 = bayesIm.getNodeIndex(bayesIm.getNode("X7"));
        int x8 = bayesIm.getNodeIndex(bayesIm.getNode("X8"));

        System.out.println();

        System.out.println("================================================================================");
        int[] nodes = new int[]{x3, x4};
        int[] parents = new int[]{x1};
        int[] parentValues = new int[]{0};
        System.out.println("P(X3,X4|X1)");
        System.out.println("--------------------------------------------------------------------------------");
        JunctionTree.ProbabilityDistribution jtPd = jt.getConditionalProbabilities(nodes, parents, parentValues);
        print(jtPd.getValues(), jtPd.getProbabilities());

        System.out.println();

        UGJunctionTree.ProbabilityDistribution ugjtPd = ugjt.getConditionalProbabilities(nodes, parents, parentValues);
        print(ugjtPd.getValues(), ugjtPd.getProbabilities());

        System.out.println();
        System.out.println();
        System.out.println("P(X3,X4)");
        System.out.println("--------------------------------------------------------------------------------");
        nodes = new int[]{x3, x4};
        jtPd = jt.getJointProbabilityDistribution(nodes);
        print(jtPd.getValues(), jtPd.getProbabilities());

        System.out.println();

        ugjtPd = ugjt.getJointProbabilityDistribution(nodes);
        print(ugjtPd.getValues(), ugjtPd.getProbabilities());

        System.out.println();
        System.out.println();
        System.out.println("P(X4,X8)");
        System.out.println("--------------------------------------------------------------------------------");
        nodes = new int[]{x4, x8};
        jtPd = jt.getJointProbabilityDistribution(nodes);
        print(jtPd.getValues(), jtPd.getProbabilities());

        System.out.println();
        System.out.printf("P(X4=0,X8=0) = P(%f,%f)%n", jt.getMarginalProbability(x4, 0), jt.getMarginalProbability(x8, 0));

        System.out.println();

        ugjtPd = ugjt.getJointProbabilityDistribution(nodes);
        print(ugjtPd.getValues(), ugjtPd.getProbabilities());

        System.out.println();
        System.out.printf("P(X4=0,X8=0) = P(%f,%f)%n", ugjt.getMarginalProbability(x4, 0), ugjt.getMarginalProbability(x8, 0));
        System.out.println("================================================================================");
    }

    private void print(int[][] values, double[] probabilities) {
        for (int i = 0; i < values.length; i++) {
            String value = StringUtils.strValueArray(values[i], ",");
            System.out.printf("%s: %f%n", value, probabilities[i]);
        }
    }

}
