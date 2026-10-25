/*
 * Decompiled with CFR 0.152.
 */
package com.github.sarxos.webcam;

public interface WebcamDiscoverySupport {
    public static final long DEFAULT_SCAN_INTERVAL = 3000L;

    public long getScanInterval();

    public boolean isScanPossible();
}

