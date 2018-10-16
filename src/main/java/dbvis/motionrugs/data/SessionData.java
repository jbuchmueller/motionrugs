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
import java.util.Set;

/**
 * Stores the data during execution
 *
 * @author Juri Buchmüller, University of Konstanz
 * <buchmueller@dbvis.inf.uni-konstanz.de>
 */
public class SessionData {
    
    private static SessionData instance;
    private HashMap<String, DataSet> datasets;
    
    private SessionData(){
        datasets = new HashMap<>();
    }
    
    public static SessionData getInstance(){
        if(instance==null){
            instance = new SessionData();
        }
        return instance;
    }  
    
    public Set<String> getDatasetNames(){
        return datasets.keySet();
    }

    public DataSet getDataset(String name) {
        if(datasets.get(name)==null)System.out.println("NUHULL");
        return datasets.get(name);
    }

    public void addDataset(DataSet dataset) {
        datasets.put(dataset.getName(), dataset);
    }
    
    
    public void addOrderedData(String datasetname, String strategyname, DataPoint[][] ordered){
        System.out.println("Adding ordered data:" + datasetname + " " + strategyname + " " + ordered.length);
        DataSet tochange = datasets.get(datasetname);
        System.out.println("dataset in session is not null: " + (tochange != null));
        tochange.addOrderedData(ordered, strategyname);
        
        
        datasets.put(datasetname, tochange);
    }
    
    
}
