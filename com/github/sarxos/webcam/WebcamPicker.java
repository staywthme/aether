/*
 * Decompiled with CFR 0.152.
 */
package com.github.sarxos.webcam;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPickerCellRenderer;
import com.github.sarxos.webcam.WebcamPickerModel;
import java.util.List;
import javax.swing.JComboBox;

public class WebcamPicker
extends JComboBox {
    private static final long serialVersionUID = 1L;
    private static final WebcamPickerCellRenderer RENDERER = new WebcamPickerCellRenderer();

    public WebcamPicker() {
        this(Webcam.getWebcams());
    }

    public WebcamPicker(List<Webcam> list2) {
        super(new WebcamPickerModel(list2));
        this.setRenderer(RENDERER);
    }

    public Webcam getSelectedWebcam() {
        return (Webcam)this.getSelectedItem();
    }
}

