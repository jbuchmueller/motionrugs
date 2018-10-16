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

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

/**
 *  VisPanel contains and transforms the rugs for display in the GUI
 * 
 * @author Juri Buchmüller, University of Konstanz <buchmueller@dbvis.inf.uni-konstanz.de>
 */
public class VisPanel extends JPanel {

    private BufferedImage bf;

    public VisPanel(BufferedImage bf) {
        super();
        this.bf = bf;
    }

    @Override
    public void paintComponent(Graphics g) {
        this.setSize(bf.getWidth(), bf.getHeight());
        this.setPreferredSize(new Dimension(bf.getWidth(), this.getHeight()));

        Graphics2D g2 = (Graphics2D) g;
        AffineTransform af = new AffineTransform();
        af.scale(1, (double) this.getHeight() / bf.getHeight());
        AffineTransformOp afo = new AffineTransformOp(af, AffineTransformOp.TYPE_BICUBIC);

        g2.drawImage(bf, afo, 0, 0);
    }

}
