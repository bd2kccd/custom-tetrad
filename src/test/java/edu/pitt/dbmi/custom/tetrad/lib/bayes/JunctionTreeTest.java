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

import edu.cmu.tetrad.data.DataModel;
import edu.cmu.tetrad.graph.Graph;
import edu.pitt.dbmi.custom.tetrad.lib.util.FileUtils;
import edu.pitt.dbmi.custom.tetrad.lib.util.StringUtils;
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
@Disabled
public class JunctionTreeTest {

    @Test
    public void testFullJointProbs() throws IOException {
        String graphFile = this.getClass().getResource("/data/jta/graph_chain3node.txt").getFile();
        String dataFile = this.getClass().getResource("/data/jta/data_chain3node.txt").getFile();

        DataModel dataModel = FileUtils.readDiscreteData(Paths.get(dataFile));
        Graph graph = FileUtils.readGraph(Paths.get(graphFile));

        JunctionTree jt = new JunctionTree(graph, dataModel);
        JunctionTree.JointProbabilityDistribution jointProbDistrib = jt.getJointProbabilityDistribution();

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
