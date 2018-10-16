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

import java.util.HashMap;
import java.util.List;

/**
 * Allows the loading of csv datasets. CSV datasets have to have the following
 * fields:
 * <ul>
 * <li>frame: needs to be an integer beginning at 0 sequentially without gaps.
 * The movement has to be muniformly sampled, in other words, for each frame, a
 * position and feature values have to be known for all movers</li>
 * <li>id: mover id. Integer starting at 0, needs to be gapless sequential</li>
 * <li>x: The x coordinate as decimal, in a cartesian coordinate system.</li>
 * <li>y: The y coordinate as decimal, in a cartesian coordinate system.</li>
 * <li>All other features need to be decimal with a . as decimal separator</li>
 * </ul>
 *
 * The csv should come with a comma as separator and <b>a header line</b> is
 * required! At the moment, all frames need to be filled with the same amount of
 * movers. No gaps are covered.
 *
 * @author Juri Buchmüller, University of Konstanz
 * <buchmueller@dbvis.inf.uni-konstanz.de>
 */
public class CSVDataSet implements DataSet {

    private List<String> features;
    private DataPoint[][] baseData;
    private HashMap<String, Double[]> deciles;
    private HashMap<String, DataPoint[][]> orderedDataSets;
    private HashMap<String, Double> featureMins;
    private HashMap<String, Double> featureMaxs;
    private String name;

    /**
     *
     * @param features the list of features contained in the dataset
     * @param baseData the unordered base data of the movment
     * @param deciles a map containing deciles of the feature value ranges for
     * each feature
     * @param name the name of the dataset
     * @param featureMins the min value per feature
     * @param featureMaxs the max value per feature
     */
    public CSVDataSet(List<String> features, DataPoint[][] baseData, HashMap<String, Double[]> deciles, String name, HashMap<String, Double> featureMins, HashMap<String, Double> featureMaxs) {
        this.features = features;
        this.baseData = baseData;
        this.deciles = deciles;
        this.name = name;
        this.name = this.name.replace(".csv", "");
        orderedDataSets = new HashMap<>();
        this.featureMins = featureMins;
        this.featureMaxs = featureMaxs;
    }

    /**
     * @return the base data
     */
    @Override
    public DataPoint[][] getBaseData() {
        return baseData;
    }

    /**
     * Stores results of applied ordering strategies separately
     *
     * @param data the ordered data to store
     * @param strategyID the id of the strategy the data was ordered with
     */
    @Override
    public void addOrderedData(DataPoint[][] data, String strategyID) {
        if (data == null) {
            System.out.println("DATA IS NULL");
        }
        if (strategyID == null) {
            System.out.println("STRATID IS NULL");
        }
        orderedDataSets.put(strategyID, data);
    }

    /**
     * Returns the ordered data
     *
     * @param strategyID the strategy for which ordered data is returned
     * @return the ordered data
     */
    @Override
    public DataPoint[][] getData(String strategyID) {
        System.out.println("TRYING TO GET DATA. INPUT ID: " + strategyID);
        System.out.println("EXISTING KEYS: " + orderedDataSets.keySet());
        return orderedDataSets.get(strategyID);
    }

    /**
     * Returns the min value of the requested feature
     *
     * @param featureid the feature for which the min value is requested
     * @return the min value for the requested feature
     */
    @Override
    public double getMin(String featureid) {
        System.out.println("MINVAL " + featureid + featureMins.get(featureid));
        return featureMins.get(featureid);
    }

    /**
     * Returns the max value of the requested feature
     *
     * @param featureid the feature for which the max value is requested
     * @return the min value for the requested feature
     */
    @Override
    public double getMax(String featureid) {
        return featureMaxs.get(featureid);
    }

    /**
     * Returns the list of available features in the Dataset.
     *
     * @return the ist of available features
     */
    @Override
    public List<String> getFeatureList() {
        return features;
    }

    /**
     * Returns the deciles of a requested feature
     *
     * @param feature the feature for which the deciles are requested
     * @return the deciles for the requested feature
     */
    @Override
    public Double[] getDeciles(String feature) {
        return deciles.get(feature);
    }

    /**
     * Returns the name of the dataset
     *
     * @return the name of the dataset
     */
    @Override
    public String getName() {
        return name;
    }

}
