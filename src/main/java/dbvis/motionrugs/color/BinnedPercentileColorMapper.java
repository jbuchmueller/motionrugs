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
package dbvis.motionrugs.color;

import java.awt.Color;

/**
 * Provides value-color mapping according to a given set of percentiles
 *
 * @author Juri Buchmüller, University of Konstanz
 * <buchmueller@dbvis.inf.uni-konstanz.de>
 */
public class BinnedPercentileColorMapper implements ColorMapper {

    private double min;
    private double max;
    private Double[] percentiles;
    private Color[] colors;

    public BinnedPercentileColorMapper(Double[] percentiles, double min, double max, Color[] colors) {
        this.min = min;
        this.max = max;
        this.percentiles = percentiles;
        this.colors = colors;
    }

    @Override
    public Color getColorByValue(double value) throws Exception {
        if (value < min || value > max) {
            throw new Exception("The given value " + value + " is outside the preset value range. The range is set from " + min + " to " + max);
        }
        //Todo: Normalization if quantiles and colors differ.
        return colors[searchBin(value)];
    }

    public int searchBin(double value) throws Exception {
        if (value <= percentiles[0]) {
            return 0;
        }
        if (value > percentiles[percentiles.length - 1]) {
            return percentiles.length;
        }

        for (int i = 0; i < percentiles.length; i++) {

            if (value > percentiles[i] && value <= percentiles[i + 1]) {
                return i + 1;
            }
        }
        return -1;
    }

}
