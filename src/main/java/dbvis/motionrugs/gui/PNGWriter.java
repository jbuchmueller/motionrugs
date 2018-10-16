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
package dbvis.motionrugs.gui;

import dbvis.motionrugs.color.BinnedPercentileColorMapper;
import dbvis.motionrugs.data.DataPoint;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 * PNGWriter is responsible for the creation of the visualization images. It
 * applies Colormaps and returns BufferedImages. Also, saves the resulting
 * images.
 *
 * @author Juri Buchmüller, University of Konstanz
 * <buchmueller@dbvis.inf.uni-konstanz.de>
 */
public class PNGWriter {

    /**
     *
     * According to a chosen Colormapper, creates a BufferedImage of a Rugs
     * using one feature, saves it to the default project directory and returns
     * the image for display in the GUI
     *
     * @param da the array with ordered values
     * @param min min value of the feature values for the color mapping
     * @param max max value of the feature values for the color mapping
     * @param decs the percentiles (bins) for the colors (limited to 10 currently)
     * @param featureID the name of the displayed feature
     * @param dsname the name of the displayed dataset
     * @param stratid the name of the chosen strategy
     * @return the MotionRug created from the ordered data
     */
    public static BufferedImage drawAndSaveRugs(DataPoint[][] da, double min, double max, Double[] decs, String featureID, String dsname, String stratid) {

        BufferedImage awtImage = new BufferedImage(da.length, da[0].length, BufferedImage.TYPE_INT_RGB);
        Color c1 = new Color(165, 0, 38);
        Color c2 = new Color(215, 48, 39);
        Color c3 = new Color(244, 109, 67);
        Color c4 = new Color(253, 174, 97);
        Color c5 = new Color(254, 224, 144);
        Color c6 = new Color(224, 243, 248);
        Color c7 = new Color(171, 217, 233);
        Color c8 = new Color(116, 173, 209);
        Color c9 = new Color(69, 117, 180);
        Color c10 = new Color(49, 54, 149);

        Color[] colors = {c10, c9, c8, c7, c6, c5, c4, c3, c2, c1};
        
        BinnedPercentileColorMapper bqcm = new BinnedPercentileColorMapper(decs, min, max, colors);

        for (int x = 0; x < da.length; x++) {
            for (int y = 0; y < da[x].length; y++) {
                try {
                    if (da[x][y].getValue(featureID) < min) {
                        System.out.println("ERROR: " + featureID + " " + da[x][y].getValue(featureID) + "<" + min + ", id " + min + ", frame " + y);
                    }
                    awtImage.setRGB(x, y, bqcm.getColorByValue(da[x][y].getValue(featureID)).getRGB());

                } catch (Exception ex) {
                    System.out.println(featureID);
                    Logger.getLogger(PNGWriter.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        File outputfile = new File(dsname + "_" + featureID + "_" + stratid + ".png");
        try {
            ImageIO.write(awtImage, "png", outputfile);
            return awtImage;
        } catch (IOException ex) {
            Logger.getLogger(PNGWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
        return awtImage;
    }

}
