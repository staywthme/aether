/*
 * Decompiled with CFR 0.152.
 */
package com.github.sarxos.webcam;

import com.github.sarxos.webcam.Webcam;
import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class WebcamPickerCellRenderer
extends JLabel
implements ListCellRenderer {
    private static final long serialVersionUID = 1L;
    private static final ImageIcon ICON = new ImageIcon(WebcamPickerCellRenderer.class.getResource("/com/github/sarxos/webcam/icons/camera-icon.png"));

    public WebcamPickerCellRenderer() {
        this.setOpaque(true);
        this.setHorizontalAlignment(2);
        this.setVerticalAlignment(0);
        this.setIcon(ICON);
    }

    public Component getListCellRendererComponent(JList jList, Object object, int n2, boolean bl, boolean bl2) {
        object = (Webcam)object;
        if (bl) {
            this.setBackground(jList.getSelectionBackground());
            this.setForeground(jList.getSelectionForeground());
        } else {
            this.setBackground(jList.getBackground());
            this.setForeground(jList.getForeground());
        }
        this.setText(((Webcam)object).getName());
        this.setFont(jList.getFont());
        return this;
    }
}

