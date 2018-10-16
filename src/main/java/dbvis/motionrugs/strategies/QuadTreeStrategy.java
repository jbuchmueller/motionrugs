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
import java.awt.Point;
import java.util.ArrayList;

/**
 * QuadTree parent strategy. Can be used for different implementation (e.g.
 * default QuadTRee, Point Quadtree)
 *
 * @author Eren Cakmak, University of Konstanz
 * <cakmak@dbvis.inf.uni-konstanz.de>
 * @author Juri Buchmüller, University of Konstanz
 * <buchmueller@dbvis.inf.uni-konstanz.de>
 */
public class QuadTreeStrategy implements Strategy {

    @Override
    public String getName() {
        return "Point QuadTree";
    }

    @Override
    public DataPoint[][] getOrderedValues(DataPoint[][] unsorted) {
        DataPoint[][] result = new DataPoint[unsorted.length][unsorted[0].length];

        for (int x = 0; x < unsorted.length; x++) {
            // create the quadtree and insert the elements
            PointQuadTree quadTree = new PointQuadTree();

            for (int y = 0; y < unsorted[x].length; y++) {
                quadTree.insert(new Point((int) unsorted[x][y].getX(), (int) unsorted[x][y].getY()), unsorted[x][y]);
            }
            //return the inorder traversal
            ArrayList<DataPoint> list = quadTree.inorderTraversal();

            //System.out.println(list.size());
            for (int y = 0; y < list.size(); y++) {
                if (list.get(y) != null) {
                    result[x][y] = list.get(y);
                }
            }
        }
        return result;
    }

}
