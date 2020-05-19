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
 * Apr 30, 2020 4:28:15 PM
 *
 * @author Kevin V. Bui (kvb2@pitt.edu)
 */
public class JunctionTreeTest {

    @Disabled
    @Test
    public void testDivert3Node() throws IOException {
        String graphFile = this.getClass().getResource("/data/jta/divert3node/graph.txt").getFile();
        String dataFile = this.getClass().getResource("/data/jta/divert3node/data.txt").getFile();

        DataModel dataModel = FileUtils.readDiscreteData(Paths.get(dataFile));
        Graph graph = FileUtils.readGraph(Paths.get(graphFile));

        BayesPm bayesPm = TetradUtils.createBayesPm(dataModel, graph);
        BayesIm bayesIm = TetradUtils.createEmBayesEstimator(dataModel, bayesPm);
        JunctionTree jt = new JunctionTree(bayesIm);

        int z = bayesIm.getNodeIndex(bayesIm.getNode("z"));
        int x = bayesIm.getNodeIndex(bayesIm.getNode("x"));
        int y = bayesIm.getNodeIndex(bayesIm.getNode("y"));

        int xNode = x;
        int xNodeValue = 0;
        int[] xParents = {z};
        int[] xParentValues = {0};
        double probXGivenZ = jt.getConditionalProbability(xNode, xNodeValue, xParents, xParentValues);

        int yNode = y;
        int yNodeValue = 0;
        int[] yParents = {z};
        int[] yParentValues = {0};
        double probYGivenZ = jt.getConditionalProbability(yNode, yNodeValue, yParents, yParentValues);

        int[] nodes = {x, y};
        int[] values = {0, 0};
        int[] parents = {z};
        int[] parentValues = {0};
        double probXYGivenZ = jt.getConditionalProbabilities(nodes, values, parents, parentValues);

        System.out.println("================================================================================");
        System.out.println(graph.toString().trim());
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println(bayesIm);
        System.out.println("--------------------------------------------------------------------------------");
        System.out.printf("P(x=%d|z=%d) = %f%n", xNodeValue, xParentValues[0], probXGivenZ);
        System.out.printf("P(y=%d|z=%d) = %f%n", yNodeValue, yParentValues[0], probYGivenZ);
        System.out.printf("P(x=%d,y=%d|z=%d) = %f%n", xNodeValue, yNodeValue, xParentValues[0], probXGivenZ * probYGivenZ);
        System.out.println(probXYGivenZ);
        System.out.println("================================================================================");
    }

    @Disabled
    @Test
    public void testGeneralConditionalProbabilities() throws IOException {
        String graphFile = this.getClass().getResource("/data/jta/general_cond_prob/graph.txt").getFile();
        String dataFile = this.getClass().getResource("/data/jta/general_cond_prob/data.txt").getFile();

        DataModel dataModel = FileUtils.readDiscreteData(Paths.get(dataFile));
        Graph graph = FileUtils.readGraph(Paths.get(graphFile));

        BayesPm bayesPm = TetradUtils.createBayesPm(dataModel, graph);
        BayesIm bayesIm = TetradUtils.createEmBayesEstimator(dataModel, bayesPm);
        JunctionTree jt = new JunctionTree(bayesIm);

        // a;b;c;x;y
        int a = bayesIm.getNodeIndex(bayesIm.getNode("a"));
        int b = bayesIm.getNodeIndex(bayesIm.getNode("b"));
        int c = bayesIm.getNodeIndex(bayesIm.getNode("c"));
        int x = bayesIm.getNodeIndex(bayesIm.getNode("x"));
        int y = bayesIm.getNodeIndex(bayesIm.getNode("y"));

        int[] nodes = {x, y};
        int[] parents = {a, b, c};
        int[] parentValues = {0, 0, 0};

        JunctionTree.ProbabilityDistribution probDistrib = jt.getConditionalProbabilities(nodes, parents, parentValues);

        System.out.println("================================================================================");
        System.out.println(graph.toString().trim());
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println(bayesIm);
        System.out.println("--------------------------------------------------------------------------------");
        int[][] values = probDistrib.getValues();
        double[] probs = probDistrib.getProbabilities();
        for (int i = 0; i < values.length; i++) {
            System.out.printf("%s: %f%n", StringUtils.strValueArray(values[i], ","), probs[i]);
        }
        System.out.println("================================================================================");
    }

    @Disabled
    @Test
    public void testFullJointProbs() throws IOException {
        String graphFile = this.getClass().getResource("/data/jta/chain3node/graph_chain3node.txt").getFile();
        String dataFile = this.getClass().getResource("/data/jta/chain3node/data_chain3node.txt").getFile();

        DataModel dataModel = FileUtils.readDiscreteData(Paths.get(dataFile));
        Graph graph = FileUtils.readGraph(Paths.get(graphFile));

        JunctionTree jt = new JunctionTree(graph, dataModel);
        JunctionTree.ProbabilityDistribution jointProbDistrib = jt.getJointProbabilityDistribution();

        System.out.println("================================================================================");
        System.out.println("testFullJointProbs");
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println(graph.toString().trim());
        System.out.println("--------------------------------------------------------------------------------");
        int[][] values = jointProbDistrib.getValues();
        double[] probs = jointProbDistrib.getProbabilities();
        for (int i = 0; i < values.length; i++) {
            String value = StringUtils.strValueArray(values[i], ",");
            System.out.printf("%s: %f%n", value, probs[i]);
        }
        System.out.println("================================================================================");
    }

}
