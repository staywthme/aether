/*
 * Decompiled with CFR 0.152.
 */
package com.github.sarxos.webcam.ds.dummy;

import com.github.sarxos.webcam.WebcamDevice;
import com.github.sarxos.webcam.WebcamDiscoverySupport;
import com.github.sarxos.webcam.WebcamDriver;
import com.github.sarxos.webcam.ds.dummy.WebcamDummyDevice;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WebcamDummyDriver
implements WebcamDiscoverySupport,
WebcamDriver {
    private int count;

    public WebcamDummyDriver(int n2) {
        this.count = n2;
    }

    @Override
    public long getScanInterval() {
        return 10000L;
    }

    @Override
    public boolean isScanPossible() {
        return true;
    }

    @Override
    public List<WebcamDevice> getDevices() {
        ArrayList<WebcamDummyDevice> arrayList = new ArrayList<WebcamDummyDevice>();
        for (int i2 = 0; i2 < this.count; ++i2) {
            arrayList.add(new WebcamDummyDevice(i2));
        }
        return Collections.unmodifiableList(arrayList);
    }

    @Override
    public boolean isThreadSafe() {
        return false;
    }
}

