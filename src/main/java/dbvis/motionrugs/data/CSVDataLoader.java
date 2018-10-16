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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.math3.stat.StatUtils;

/**
 * Loads the datasets in a specified folder. Checks the specified folder for CSV
 * files and will parse only csv files. For each dataset, identifies the amount
 * of frames and unique movers and extracts frames, positions, and features of
 * them.
 *
 * @author Juri Buchmüller, University of Konstanz
 * <buchmueller@dbvis.inf.uni-konstanz.de>
 */
public class CSVDataLoader {

    public static void checkAndLoadCSVDataSets(String[] datapath) {
        File folder;
        String localdir = "./data";
        if (datapath.length > 0) {
            localdir = datapath[0];
            folder = new File(localdir);
        } else {
            Path path = Paths.get(localdir);
            folder = new File(localdir);
            if (Files.notExists(path)) {
                folder.mkdir();
            }
        }
        try {
            System.out.println(folder.getCanonicalPath());
        } catch (IOException ex) {
            Logger.getLogger(CSVDataLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
        File[] datafiles = folder.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".csv");
            }
        });

        if (datafiles.length < 1) {
            System.out.println("NO DATA FILES");
            System.exit(0);
        }
        for (File file : datafiles) {
            loadFile(file);
        }
    }

    private static void loadFile(File file) {

        DataPoint[][] baseData; //baseData[frame][fish]
        HashMap<String, Double> featureMinValues = new HashMap<>();
        HashMap<String, Double> featureMaxValues = new HashMap<>();
        HashMap<String, Double[]> deciles = new HashMap<>();

        Reader in;

        try {
            //Load CSV
            in = new FileReader(file);
            CSVFormat csvFileFormat = CSVFormat.RFC4180.withFirstRecordAsHeader();
            CSVParser csvFileParser = new CSVParser(in, csvFileFormat);
            List<CSVRecord> csvRecords = csvFileParser.getRecords();
            System.out.println("Dataset rows: " + csvRecords.size());

            //Extract unique movers (not very efficient, to be replaced with Maps)
            HashMap<String, String> moversids = new HashMap<>();
            for (CSVRecord csvr : csvRecords) {
                moversids.put(csvr.get("id"), csvr.get("id"));
            }
            int uniquemovers = moversids.keySet().size();
            System.out.println(uniquemovers + " unique movers.");

            //Extract featurelist
            ArrayList<String> featurelist = new ArrayList<>();
            featurelist.addAll(csvFileParser.getHeaderMap().keySet());
            System.out.println("Features found: " + featurelist);

            //Assign feature percentiles and min/max values
            for (String feature : featurelist) {
                if (feature.equals("frame") || feature.equals("id") || feature.equals("x") || feature.equals("y")) {
                    continue;
                }
                double[] temp = new double[csvRecords.size()];
                int i = 0;

                double min = Double.MAX_VALUE;
                double max = Double.MIN_VALUE;
                for (CSVRecord csvr : csvRecords) {
                    temp[i] = Double.parseDouble(csvr.get(feature));
                    if (temp[i] < min) {
                        min = temp[i];
                    }
                    if (temp[i] > max) {
                        max = temp[i];
                    }

                    i++;
                }

                Double[] decilesarr = new Double[9];
                for (int j = 1; j < 10; j++) {
                    decilesarr[j - 1] = StatUtils.percentile(temp, j * 10);
                    //System.out.println("Percentile " + j*10 + ": " + StatUtils.percentile(temp, j*10));
                }
                deciles.put(feature, decilesarr);
                featureMinValues.put(feature, min);
                featureMaxValues.put(feature, max);
            }

            //Determine dataset dimensions
            int frames = Math.toIntExact(csvRecords.size());
            System.out.println("Dataset size:" + uniquemovers + " movers in " + frames / uniquemovers + " frames.");
            baseData = new DataPoint[(int) (frames / uniquemovers)][uniquemovers];

            for (int i = 0; i < csvRecords.size(); i++) {
                CSVRecord csvr = csvRecords.get(i);

                DataPoint dp = new DataPoint(Double.parseDouble(csvr.get("x")), Double.parseDouble(csvr.get("y")), Integer.parseInt(csvr.get("id")));

                for (int j = 0; j < featurelist.size(); j++) {
                    String feature = featurelist.get(j);
                    if (feature.equals("frame") || feature.equals("id") || feature.equals("x") || feature.equals("y")) {
                        continue;
                    }
                    dp.putValue(feature, Double.parseDouble(csvr.get(feature)));
                }

                try {
                    baseData[Integer.parseInt(csvr.get("frame"))][Integer.parseInt(csvr.get("id"))] = dp;
                } catch (Exception e) {
                    System.out.println("baseData size: basedata[" + baseData.length + "][" + baseData[0].length + "]");
                    System.out.println(Integer.parseInt(csvr.get("frame")) + "/" + Integer.parseInt(csvr.get("id")));
                    e.printStackTrace();
                }
            }

            CSVDataSet csvd = new CSVDataSet(featurelist, baseData, deciles, file.getName(), featureMinValues, featureMaxValues);
            SessionData.getInstance().addDataset(csvd);

        } catch (FileNotFoundException ex) {
            Logger.getLogger(CSVDataLoader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(CSVDataLoader.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
