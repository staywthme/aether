/*
 * Decompiled with CFR 0.152.
 */
package com.github.sarxos.webcam;

import com.github.sarxos.webcam.WebcamDevice;
import com.github.sarxos.webcam.WebcamDiscoverySupport;
import com.github.sarxos.webcam.WebcamDriver;
import java.util.ArrayList;
import java.util.List;

public class WebcamCompositeDriver
implements WebcamDiscoverySupport,
WebcamDriver {
    private List<WebcamDriver> drivers = new ArrayList<WebcamDriver>();
    private int scanInterval = -1;

    public WebcamCompositeDriver(WebcamDriver ... webcamDriverArray) {
        for (WebcamDriver webcamDriver : webcamDriverArray) {
            this.drivers.add(webcamDriver);
        }
    }

    public void add(WebcamDriver webcamDriver) {
        this.drivers.add(webcamDriver);
    }

    public List<WebcamDriver> getDrivers() {
        return this.drivers;
    }

    @Override
    public List<WebcamDevice> getDevices() {
        ArrayList<WebcamDevice> arrayList = new ArrayList<WebcamDevice>();
        for (WebcamDriver webcamDriver : this.drivers) {
            arrayList.addAll(webcamDriver.getDevices());
        }
        return arrayList;
    }

    @Override
    public boolean isThreadSafe() {
        boolean bl = true;
        for (WebcamDriver webcamDriver : this.drivers) {
            if (!(bl &= webcamDriver.isThreadSafe())) break;
        }
        return bl;
    }

    public void setScanInterval(int n2) {
        this.scanInterval = n2;
    }

    @Override
    public long getScanInterval() {
        if (this.scanInterval <= 0) {
            return 3000L;
        }
        return this.scanInterval;
    }

    @Override
    public boolean isScanPossible() {
        return true;
    }
}

