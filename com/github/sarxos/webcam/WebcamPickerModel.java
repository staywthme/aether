/*
 * Decompiled with CFR 0.152.
 */
package com.github.sarxos.webcam;

import com.github.sarxos.webcam.Webcam;
import java.util.List;
import javax.swing.DefaultComboBoxModel;

public class WebcamPickerModel
extends DefaultComboBoxModel {
    private static final long serialVersionUID = 1L;

    public WebcamPickerModel(List<Webcam> list2) {
        List<Webcam> list3 = list2;
        super(list3.toArray(new Webcam[list3.size()]));
    }

    @Override
    public Webcam getSelectedItem() {
        return (Webcam)super.getSelectedItem();
    }

    @Override
    public void setSelectedItem(Object object) {
        if (!(object instanceof Webcam)) {
            throw new IllegalArgumentException("Selected object has to be an Webcam instance");
        }
        super.setSelectedItem(object);
    }
}

