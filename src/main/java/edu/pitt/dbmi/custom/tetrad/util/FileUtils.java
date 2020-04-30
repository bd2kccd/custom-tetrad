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

import edu.cmu.tetrad.data.DataModel;
import edu.cmu.tetrad.graph.Graph;
import edu.cmu.tetrad.graph.GraphUtils;
import edu.cmu.tetrad.util.DataConvertUtils;
import edu.pitt.dbmi.data.reader.Data;
import edu.pitt.dbmi.data.reader.Delimiter;
import edu.pitt.dbmi.data.reader.tabular.VerticalDiscreteTabularDatasetFileReader;
import edu.pitt.dbmi.data.reader.tabular.VerticalDiscreteTabularDatasetReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 *
 * Mar 5, 2020 11:37:15 AM
 *
 * @author Kevin V. Bui (kvb2@pitt.edu)
 */
public final class FileUtils {

    private FileUtils() {
    }

    /**
     * Read in tabular discrete dataset.
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static DataModel readDiscreteData(Path file) throws IOException {
        // specify data properties
        Delimiter delimiter = Delimiter.TAB;
        char quoteCharacter = '"';
        String commentMarker = "//";
        String missingValueMarker = "*";
        boolean hasHeader = true;

        // create a data reader specifically for the data
        VerticalDiscreteTabularDatasetReader dataReader = new VerticalDiscreteTabularDatasetFileReader(file, delimiter);
        dataReader.setCommentMarker(commentMarker);
        dataReader.setQuoteCharacter(quoteCharacter);
        dataReader.setMissingDataMarker(missingValueMarker);
        dataReader.setHasHeader(hasHeader);

        // read in the data
        Data data = dataReader.readInData();

        // convert the data read in to Tetrad data model
        return DataConvertUtils.toDataModel(data);
    }

    public static Graph readGraph(Path file) throws IOException {
        try (Reader reader = Files.newBufferedReader(file)) {
            return GraphUtils.readerToGraphTxt(reader);
        }
    }

}
