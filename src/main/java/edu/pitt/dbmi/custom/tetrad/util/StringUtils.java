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

import edu.cmu.tetrad.graph.Node;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 * Mar 5, 2020 11:33:31 AM
 *
 * @author Kevin V. Bui (kvb2@pitt.edu)
 */
public final class StringUtils {

    private StringUtils() {
    }

    public static String strValueArray(double[] array, String delimiter) {
        return Arrays.stream(array)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining(delimiter));
    }

    public static String strValueArray(int[] array, String delimiter) {
        return Arrays.stream(array)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining(delimiter));
    }

    public static String strVarArray(Node[] array, String delimiter) {
        return Arrays.stream(array)
                .map(Node::getName)
                .collect(Collectors.joining(delimiter));
    }

    public static String strVarSet(Set<Node> set, String delimiter) {
        return set.stream()
                .map(Node::getName)
                .sorted((var1, var2) -> Integer.valueOf(var1.replace("X", "")).compareTo(Integer.valueOf(var2.replace("X", ""))))
                .collect(Collectors.joining(delimiter));
    }

}
