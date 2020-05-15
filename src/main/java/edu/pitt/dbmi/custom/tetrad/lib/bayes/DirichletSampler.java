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
import edu.cmu.tetrad.data.DataSet;
import edu.cmu.tetrad.graph.Node;
import org.apache.commons.math3.distribution.GammaDistribution;
import org.apache.commons.math3.random.JDKRandomGenerator;
import org.apache.commons.math3.random.RandomGenerator;

/**
 *
 * May 15, 2020 10:43:58 AM
 *
 * @author Kevin V. Bui (kvb2@pitt.edu)
 */
public final class DirichletSampler {

    private DirichletSampler() {
    }

    public static DirichletBayesIm estimate(DirichletBayesIm prior, DataSet dataSet) {
        if (prior == null || dataSet == null) {
            throw new NullPointerException();
        }

        // Create the posterior.
        BayesPm bayesPm = prior.getBayesPm();
        DirichletBayesIm posterior = DirichletBayesIm.blankDirichletIm(bayesPm);

        // loop over all nodes in prior.
        for (int n = 0; n < prior.getNumNodes(); ++n) {
            Node node = prior.getNode(n);
            for (int row = 0; row < prior.getNumRows(n); row++) {
                int numCategories = bayesPm.getNumCategories(node);
                for (int i = 0; i < numCategories; ++i) {
                    double pseudocount = prior.getPseudocount(n, row, i);

                    RandomGenerator randGen = new JDKRandomGenerator((int) System.currentTimeMillis());
                    GammaDistribution gammaDistrib = new GammaDistribution(randGen, pseudocount, 1.0);
                    double value = gammaDistrib.sample();
                    posterior.setPseudocount(n, row, i, value);

//                    System.out.printf("pseudocount: %f, value: %f%n", pseudocount, value);
                }
            }
        }
//        System.out.println();

        return posterior;
    }

}
