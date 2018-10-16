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

import dbvis.motionrugs.data.CSVDataLoader;
import dbvis.motionrugs.data.DataPoint;
import dbvis.motionrugs.data.DataSet;
import dbvis.motionrugs.data.SessionData;
import dbvis.motionrugs.strategies.HilbertCurveStrategy;
import dbvis.motionrugs.strategies.QuadTreeStrategy;
import dbvis.motionrugs.strategies.RTreeStrategy;
import dbvis.motionrugs.strategies.Strategy;
import dbvis.motionrugs.strategies.ZOrderCurveStrategy;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *  MotionRugs main gui. Initializes processing of the rugs.
 * 
 * @author Juri Buchmüller, University of Konstanz <buchmueller@dbvis.inf.uni-konstanz.de>
 */
public class MotionRugsGUI extends javax.swing.JFrame {
    
    private DataSet curDataSet;
    private JPanel addPanel = new JPanel();
    
    //Ordering Strategies have to be instantiated here and added below where marked
    private Strategy pqrstrategy = new QuadTreeStrategy();
    private Strategy rtreestrategy = new RTreeStrategy();
    private Strategy zorderstrategy = new ZOrderCurveStrategy();
    private HilbertCurveStrategy hilbertcurvestrategy = new HilbertCurveStrategy();

    /**
     * Constructor initializing the datasets and strategies
     */
    public MotionRugsGUI(String[] datadir) {
        addPanel.setLayout(new BoxLayout(addPanel, BoxLayout.PAGE_AXIS));
        
        //If set, the first item of the datadir array  is taken as data directory location. If not set, defaults to /data/*
        CSVDataLoader.checkAndLoadCSVDataSets(datadir);
 
        SessionData data = SessionData.getInstance();
        initComponents();

        if (data.getDatasetNames().isEmpty()) {
            Logger.getLogger(MotionRugsGUI.class.getName()).log(Level.SEVERE, null, "NO DATASETS FOUND.");
            System.exit(-1);
        }
        
        //lists datasets
        jComboBox4.removeAllItems();
        for (String s : data.getDatasetNames()) {
            jComboBox4.addItem(s);
        }

        curDataSet = data.getDataset(jComboBox4.getItemAt(jComboBox4.getSelectedIndex()));
        jComboBox5.removeAllItems();
        
        //In the feature list, frame, id and position are excluded as features
        for (String s : curDataSet.getFeatureList()) {
            if (s.equals("frame") || s.equals("id") || s.equals("x") || s.equals("y")) {
                continue;
            }
            jComboBox5.addItem(s);
        }

        jComboBox6.removeAllItems();
        
        //Adding Strategy to selection menu. Has to be the same string as provided in Strategy Class when calling getName()
        jComboBox6.addItem("Hilbert curve");
        jComboBox6.addItem("Point QuadTree");
        jComboBox6.addItem("R-Tree");
        jComboBox6.addItem("Z-Order");
        
        //Sets the features according to the ones available in a chosen dataset (except standard features)
        jComboBox4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                curDataSet = data.getDataset(jComboBox4.getItemAt(jComboBox4.getSelectedIndex()));
                System.out.println("Selected Dataset: " + curDataSet.getName());
                jComboBox5.removeAllItems();
                for (String s : curDataSet.getFeatureList()) {
                    if (s.equals("frame") || s.equals("id") || s.equals("x") || s.equals("y")) {
                        continue;
                    }
                    jComboBox5.addItem(s);
                }
            }
        });

        
        jButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedDataSet = jComboBox4.getItemAt(jComboBox4.getSelectedIndex());
                String selectedFeature = jComboBox5.getItemAt(jComboBox5.getSelectedIndex());
                String selectedStrategy = jComboBox6.getItemAt(jComboBox6.getSelectedIndex());
                DataSet current = SessionData.getInstance().getDataset(selectedDataSet);
                BufferedImage bf = null;
                DataPoint[][] orderedpoints = null;

                //ADD NEW STRATEGIES HERE
                //According to the selected strategy the data of the chosen dataset is ordered
                switch (selectedStrategy) {
                    case "Point QuadTree":
                        orderedpoints = pqrstrategy.getOrderedValues(current.getBaseData());
                        break;
                    case "R-Tree":
                        orderedpoints = rtreestrategy.getOrderedValues(current.getBaseData());
                        break;
                    case "Hilbert curve":
                        hilbertcurvestrategy.setHilbertOrder(100);
                        orderedpoints = hilbertcurvestrategy.getOrderedValues(current.getBaseData());
                        break;
                    case "Z-Order":
                        orderedpoints = zorderstrategy.getOrderedValues(current.getBaseData());
                        break;
                }
                
                //Creates an image from the reordered data points. 
                bf = PNGWriter.drawAndSaveRugs(orderedpoints, current.getMin(selectedFeature), current.getMax(selectedFeature), current.getDeciles(selectedFeature), selectedFeature, current.getName(),selectedStrategy);
                System.out.println("DONE REORDERING"); 
                repaintPanel(bf);
            }
        });
    }

    /**
     * Repaints the Panel showing the visualizations.
     * 
     * @param toAdd the Image to be added to the VisPanel
     */
    private void repaintPanel(BufferedImage toAdd) {
        addPanel.add(new JScrollPane(new VisPanel(toAdd), JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS));
        addPanel.validate();
        this.validate();
        System.out.println("Added.");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jComboBox4 = new javax.swing.JComboBox<>();
        jPanel7 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jComboBox5 = new javax.swing.JComboBox<>();
        jPanel8 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jComboBox6 = new javax.swing.JComboBox<>();
        jPanel9 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane(addPanel);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(1024, 768));
        setSize(new java.awt.Dimension(1024, 768));

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.LINE_AXIS));

        jLabel4.setText("Dataset");

        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(53, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16))
        );

        jPanel1.add(jPanel6);

        jLabel5.setText("Feature");

        jComboBox5.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(53, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16))
        );

        jPanel1.add(jPanel7);

        jLabel6.setText("Strategy");

        jComboBox6.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox6, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(53, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jComboBox6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16))
        );

        jPanel1.add(jPanel8);

        jButton2.setText("Add Rug");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton2)
                .addContainerGap(169, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton2)
                .addGap(14, 14, 14))
        );

        jPanel1.add(jPanel9);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 716, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * GUI Starter
     * 
     * @param args The first String determines the data directory containing the datasets to be processed. If not set, defaults to /data/*
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info
                    : javax.swing.UIManager
                            .getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info
                        .getName())) {
                    javax.swing.UIManager
                            .setLookAndFeel(info
                                    .getClassName());

                    break;

                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger
                    .getLogger(MotionRugsGUI.class
                            .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger
                    .getLogger(MotionRugsGUI.class
                            .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger
                    .getLogger(MotionRugsGUI.class
                            .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger
                    .getLogger(MotionRugsGUI.class
                            .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue
                .invokeLater(new Runnable() {
                    public void run() {
                        new MotionRugsGUI(args
                        ).setVisible(true);

                    }
                });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton2;
    private javax.swing.JComboBox<String> jComboBox4;
    private javax.swing.JComboBox<String> jComboBox5;
    private javax.swing.JComboBox<String> jComboBox6;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
