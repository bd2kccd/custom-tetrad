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

    public static DirichletBayesIm sampleFromPosterior(DirichletBayesIm posterior) {
        if (posterior == null) {
            throw new NullPointerException();
        }

        BayesPm bayesPm = posterior.getBayesPm();

        DirichletBayesIm sample = DirichletBayesIm.blankDirichletIm(bayesPm);
        for (int n = 0; n < posterior.getNumNodes(); ++n) {
            Node node = posterior.getNode(n);
            for (int row = 0; row < posterior.getNumRows(n); row++) {
                int numCategories = bayesPm.getNumCategories(node);
                for (int i = 0; i < numCategories; ++i) {
                    double pseudocount = posterior.getPseudocount(n, row, i);

                    RandomGenerator randGen = new JDKRandomGenerator((int) System.currentTimeMillis());
                    GammaDistribution gammaDistrib = new GammaDistribution(randGen, pseudocount, 1.0);
                    sample.setPseudocount(n, row, i, gammaDistrib.sample());
                }
            }
        }

        return sample;
    }

}
