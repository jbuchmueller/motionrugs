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

import com.github.davidmoten.rtree.*;
import com.github.davidmoten.rtree.geometry.Geometries;
import com.github.davidmoten.rtree.geometry.Point;
import dbvis.motionrugs.data.DataPoint;
import java.util.List;

/**
 * R-Tree ordering strategy taken from https://github.com/davidmoten/rtree
 * 
 * @author Eren Cakmak, University of Konstanz <cakmak@dbvis.inf.uni-konstanz.de>
 * @author Juri Buchmüller, University of Konstanz <buchmueller@dbvis.inf.uni-konstanz.de>
 */
public class RTreeStrategy implements Strategy {

    @Override
    public String getName() {
        return "R-Tree";
    }

    @Override
    public DataPoint[][] getOrderedValues(DataPoint[][] unsorted) {
        DataPoint[][] result = new DataPoint[unsorted.length][unsorted[0].length];

        for (int x = 0; x < unsorted.length; x++) {
            //create R*-tree
            RTree<DataPoint, Point> tree = RTree.star().minChildren(15).maxChildren(30).create();

            //add the points
            for (int y = 0; y < unsorted[x].length; y++) {
                tree = tree.add(unsorted[x][y], Geometries.point(unsorted[x][y].getX(), unsorted[x][y].getY()));
            }
            List<Entry<DataPoint, Point>> list = tree.entries().toList().toBlocking().single();

            for (int y = 0; y < unsorted[x].length; y++) {
                result[x][y] = list.get(y).value();
            }
        }

        return result;
    }

}
