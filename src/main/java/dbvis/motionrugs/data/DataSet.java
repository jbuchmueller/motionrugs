/*
 * Copyright 2018 Juri Buchmueller <motionrugs@dbvis.inf.uni-konstanz.de>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dbvis.motionrugs.data;

import java.util.List;

/**
 * The Dataset interface. Intended to provide a common frame for datasets from
 * different sources.
 *
 * @author Juri Buchmüller, University of Konstanz
 * <buchmueller@dbvis.inf.uni-konstanz.de>
 */
public interface DataSet {

    /**
     * Returns the unsorted base data, first array being frames, second the
     * points in the frame
     *
     * @return the unsorted base data
     */
    public DataPoint[][] getBaseData();

    /**
     * Stores the sorted data as 2D-array of DataPoints. First dimension are the
     * columns, second dimension the ordered values.
     *
     * @param data The data to be stored.
     * @param strategyID The id of the strategy the data was sorted with.
     */
    public void addOrderedData(DataPoint[][] data, String strategyID);

    /**
     * Retrieves the sorted data as double[][] by strategy identifier.
     *
     * @param strategyID The desired strategy
     * @return The data sorted according to the chosen strategy or null if the
     * data was not sorted according to the specified strategy.
     */
    public DataPoint[][] getData(String strategyID);

    /**
     * Returns the minimum value of the base dataset
     *
     * @param feature The feature for which the max value is sought
     * @return the minimum value of the base dataset
     */
    public double getMin(String feature);

    /**
     * Returns the maximum value of the base dataset
     *
     * @param feature The feature for which the min value is sought
     * @return the maximum value of the base dataset
     */
    public double getMax(String feature);

    /**
     * Returns a list of available features
     *
     * @return a list with features of the dataset
     */
    public List<String> getFeatureList();

    /**
     * Returns the deciles of the chosen feature
     *
     * @param feature The feature for which the deciles are requested
     * @return the deciles of the requested feature
     */
    public Double[] getDeciles(String feature);

    /**
     * Returns the name of the dataset.
     *
     * @return the name of the dataset.
     */
    public String getName();
}
