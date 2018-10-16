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
import java.util.Arrays;
import java.util.Comparator;

/**
 * Hilbert curve ordering strategy
 * 
 * @author Eren Cakmak, University of Konstanz <cakmak@dbvis.inf.uni-konstanz.de>
 * @author Juri Buchmüller, University of Konstanz <buchmueller@dbvis.inf.uni-konstanz.de>
 */
public class HilbertCurveStrategy implements Strategy {

    //hilbert order value
    private int hilbertOrder = 2;

    @Override
    public String getName() {
        return "Hilbert curve";
    }

    /**
     * Returns dataset in hilbert ordering
     *
     * @param unsorted dataset
     * @return sorted dataset
     */
    @Override
    public DataPoint[][] getOrderedValues(DataPoint[][] unsorted) {
        DataPoint[][] result = new DataPoint[unsorted.length][unsorted[0].length];

        for (int x = 0; x < unsorted.length; x++) {

            //idx is an array of the indexes 
            Integer[] idx = new Integer[unsorted[x].length];
            //array to save the z-ordering numbers
            long hilbertValues[] = new long[unsorted[x].length];

            //calculate the z-ordering numbers
            for (int y = 0; y < unsorted[x].length; y++) {
                idx[y] = y;
                hilbertValues[y] = encode((int) unsorted[x][y].getX(), (int) unsorted[x][y].getY(), this.hilbertOrder);
            }

            //sort the index array with comparing the zValues array values 
            Arrays.sort(idx, new Comparator<Integer>() {
                @Override
                public int compare(final Integer o1, final Integer o2) {
                    return Long.compare(hilbertValues[o1], hilbertValues[o2]);
                    
                }
            });

            //sort the result set after the z-ordering 
            for (int y = 0; y < unsorted[x].length; y++) {
                result[x][y] = unsorted[x][idx[y]];
//                System.out.print(hilbertValues[idx[y]] + " ");
            }
        }
        return result;
    }

    /**
     * Source:
     * http://stackoverflow.com/questions/106237/calculate-the-hilbert-value-of-a-point-for-use-in-a-hilbert-r-tree
     *
     * Find the Hilbert order (=vertex index) for the given grid cell
     * coordinates.
     *
     * @param x cell column (from 0)
     * @param y cell row (from 0)
     * @param r resolution of Hilbert curve (grid will have Math.pow(2,r) rows
     * and cols)
     * @return Hilbert order
     */
    public int encode(int x, int y, int r) {

        int mask = (1 << r) - 1;
        int hodd = 0;
        int heven = x ^ y;
        int notx = ~x & mask;
        int noty = ~y & mask;
        int temp = notx ^ y;

        int v0 = 0, v1 = 0;
        for (int k = 1; k < r; k++) {
            v1 = ((v1 & heven) | ((v0 ^ noty) & temp)) >> 1;
            v0 = ((v0 & (v1 ^ notx)) | (~v0 & (v1 ^ noty))) >> 1;
        }
        hodd = (~v0 & (v1 ^ x)) | (v0 & (v1 ^ noty));

        return interleaveBits(hodd, heven);
    }

    /**
     * Interleave the bits from two input integer values
     *
     * @param odd integer holding bit values for odd bit positions
     * @param even integer holding bit values for even bit positions
     * @return the integer that results from interleaving the input bits
     *
     * @todo: I'm sure there's a more elegant way of doing this !
     */
    private int interleaveBits(int odd, int even) {
        int val = 0;
        // Replaced this line with the improved code provided by Tuska
        // int n = Math.max(Integer.highestOneBit(odd), Integer.highestOneBit(even));
        int max = Math.max(odd, even);
        int n = 0;
        while (max > 0) {
            n++;
            max >>= 1;
        }

        for (int i = 0; i < n; i++) {
            int bitMask = 1 << i;
            int a = (even & bitMask) > 0 ? (1 << (2 * i)) : 0;
            int b = (odd & bitMask) > 0 ? (1 << (2 * i + 1)) : 0;
            val += a + b;
        }

        return val;
    }

    /**
     * Set the hilbert order value
     *
     * @param value new hilbert order value
     */
    public void setHilbertOrder(int value) {
        this.hilbertOrder = value;
    }

}
