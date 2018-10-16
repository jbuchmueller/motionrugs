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
package dbvis.motionrugs.strategies;

import dbvis.motionrugs.data.DataPoint;

/**
 * The Strategy interface. Strategies must provide a method to order arrays of DataPoints.
 *
 * @author Juri Buchmüller, University of Konstanz
 * <buchmueller@dbvis.inf.uni-konstanz.de>
 */
public interface Strategy {

    /**
     * Retrieves the strategies name.
     *
     * @return the name of the strategy.
     */
    public String getName();

    public DataPoint[][] getOrderedValues(DataPoint[][] unsorted);

}
