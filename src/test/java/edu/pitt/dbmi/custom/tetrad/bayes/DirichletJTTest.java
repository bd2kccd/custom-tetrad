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
import edu.pitt.dbmi.custom.tetrad.util.FileUtils;
import edu.pitt.dbmi.custom.tetrad.util.TetradUtils;
import java.io.IOException;
import java.nio.file.Paths;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 *
 * Apr 30, 2020 5:40:20 PM
 *
 * @author Kevin V. Bui (kvb2@pitt.edu)
 */
@Disabled
public class DirichletJTTest {

    @Test
    public void testestimateDoProbAvg() throws IOException {
        String graphFile = this.getClass().getResource("/data/dirichlet/graph.txt").getFile();
        String dataFile = this.getClass().getResource("/data/dirichlet/data.txt").getFile();

        DataModel dataModel = FileUtils.readDiscreteData(Paths.get(dataFile));
        Graph graph = FileUtils.readGraph(Paths.get(graphFile));

        int x = 0;
        int y = 1;
        int z = 2;

        double symmetricAlpha = 1.0;
        int nSamples = 3;

        DirichletJT djt = new DirichletJT();
        double[][] avgProb = djt.estimateDoProbAvg(y, x, new int[]{z}, graph, dataModel, symmetricAlpha, nSamples);
        for (int i = 0; i < avgProb.length; i++) {
            for (int j = 0; j < avgProb.length; j++) {
                System.out.printf("y=%d, x=%d: %f%n", i, j, avgProb[i][j]);
            }
        }
    }

    @Test
    public void testEstimateDoProb() throws IOException {
        String graphFile = this.getClass().getResource("/data/dirichlet/graph.txt").getFile();
        String dataFile = this.getClass().getResource("/data/dirichlet/data.txt").getFile();

        DataModel dataModel = FileUtils.readDiscreteData(Paths.get(dataFile));
        Graph graph = FileUtils.readGraph(Paths.get(graphFile));

        int x = 0;
        int y = 1;
        int z = 2;

        double symmetricAlpha = 1.0;
        BayesPm bayesPm = TetradUtils.createBayesPm(dataModel, graph);
        DirichletBayesIm prior = DirichletBayesIm.symmetricDirichletIm(bayesPm, symmetricAlpha);

        DirichletJT djt = new DirichletJT();
        for (int n = 0; n < 2; n++) {
            prior = DirichletEstimator.estimate(prior, (DataSet) dataModel);
            double[][] prob = djt.estimateDoProb(y, x, new int[]{z}, bayesPm, prior);
            for (int i = 0; i < prob.length; i++) {
                for (int j = 0; j < prob.length; j++) {
                    System.out.printf("y=%d, x=%d: %f%n", i, j, prob[i][j]);
                }
            }
            System.out.println();
        }
    }

}
