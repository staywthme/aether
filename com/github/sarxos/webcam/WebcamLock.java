/*
 * Decompiled with CFR 0.152.
 */
package com.github.sarxos.webcam;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamException;
import com.github.sarxos.webcam.WebcamExceptionHandler;
import com.github.sarxos.webcam.WebcamLockException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebcamLock {
    private static final Logger LOG = LoggerFactory.getLogger(WebcamLock.class);
    public static final long INTERVAL = 2000L;
    private final Webcam webcam;
    private Thread updater = null;
    private final AtomicBoolean locked = new AtomicBoolean(false);
    private final AtomicBoolean disabled = new AtomicBoolean(false);
    private final File lock;

    protected WebcamLock(Webcam webcam) {
        this.webcam = webcam;
        this.lock = new File(System.getProperty("java.io.tmpdir"), this.getLockName());
        this.lock.deleteOnExit();
    }

    private String getLockName() {
        return String.format(".webcam-lock-%d", Math.abs(this.webcam.getName().hashCode()));
    }

    /*
     * Unable to fully structure code
     */
    private void write(long var1_1) {
        if (this.disabled.get()) {
            return;
        }
        var3_9 = this.getLockName();
        var4_11 = null;
        var3_9 = File.createTempFile(String.format("%s-tmp", new Object[]{var3_9}), "");
        var3_9.deleteOnExit();
        var4_11 = new DataOutputStream(new FileOutputStream((File)var3_9));
        var4_11.writeLong(var1_1);
        var4_11.flush();
        try {
            var4_11.close();
        }
        catch (IOException var1_2) {
            throw new RuntimeException(var1_2);
        }
        catch (IOException var1_3) {
            try {
                throw new RuntimeException(var1_3);
            }
            catch (Throwable var2_14) {
                if (var4_11 != null) {
                    try {
                        var4_11.close();
                    }
                    catch (IOException var4_12) {
                        throw new RuntimeException(var4_12);
                    }
                }
                throw var2_14;
            }
        }
        if (!this.locked.get()) {
            return;
        }
        if (var3_9.renameTo(this.lock)) {
            return;
        }
        if (!this.lock.exists()) {
            try {
                if (!this.lock.createNewFile()) {
                    throw new RuntimeException("Not able to create file " + this.lock);
                }
                WebcamLock.LOG.info("Lock file {} for {} has been created", (Object)this.lock, (Object)this.webcam);
            }
            catch (IOException var1_4) {
                throw new RuntimeException(var1_4);
            }
        }
        var1_5 = null;
        var2_15 = null;
        var4_13 = 0;
        var6_16 = new byte[8];
        var7_17 = false;
        var8_18 = this.webcam;
        synchronized (var8_18) {
            do {
                var1_5 = new FileOutputStream(this.lock);
                var2_15 = new FileInputStream((File)var3_9);
                while ((var5_19 = var2_15.read(var6_16)) != -1) {
                    var1_5.write(var6_16, 0, var5_19);
                }
                var7_17 = true;
                ** try [egrp 7[TRYBLOCK] [6 : 299->306)] { 
lbl-1000:
                // 1 sources

                {
                    var1_5.close();
                }
lbl58:
                // 1 sources

                catch (IOException var5_20) {
                    throw new RuntimeException(var5_20);
                }
                try {
                    var2_15.close();
                }
                catch (IOException var5_21) {
                    throw new RuntimeException(var5_21);
                }
                catch (IOException var5_22) {
                    WebcamLock.LOG.debug("Not able to rewrite lock file", var5_22);
                    continue;
                }
                finally {
                    if (var1_5 != null) {
                        try {
                            var1_5.close();
                        }
                        catch (IOException var5_23) {
                            throw new RuntimeException(var5_23);
                        }
                    }
                    if (var2_15 != null) {
                        try {
                            var2_15.close();
                        }
                        catch (IOException var5_24) {
                            throw new RuntimeException(var5_24);
                        }
                    }
                }
            } while (!var7_17 && var4_13++ < 5);
        }
        if (!var7_17) {
            throw new WebcamException("Not able to write lock file");
        }
        if (!var3_9.delete()) {
            var3_9.deleteOnExit();
        }
    }

    /*
     * Unable to fully structure code
     */
    private long read() {
        if (this.disabled.get()) {
            return -1L;
        }
        var1_1 = null;
        var2_4 = -1L;
        var4_6 = false;
        var5_11 = this.webcam;
        synchronized (var5_11) {
            block19: {
                try {
                    var1_1 = new DataInputStream(new FileInputStream(this.lock));
                    var2_4 = var1_1.readLong();
                    ** try [egrp 2[TRYBLOCK] [0 : 55->62)] { 
                }
                catch (EOFException var4_8) {
                    WebcamLock.LOG.debug("Webcam lock is broken - EOF when reading long variable from stream", var4_8);
                    var4_6 = true;
                    break block19;
                }
                catch (IOException var4_10) {
                    throw new RuntimeException(var4_10);
                    {
                        catch (Throwable var2_5) {
                            throw var2_5;
                        }
                    }
                }
                finally {
                    if (var1_1 != null) {
                        try {
                            var1_1.close();
                        }
                        catch (IOException var4_9) {
                            throw new RuntimeException(var4_9);
                        }
                    }
                }
lbl-1000:
                // 1 sources

                {
                    var1_1.close();
                    break block19;
                }
lbl15:
                // 1 sources

                catch (IOException var4_7) {
                    throw new RuntimeException(var4_7);
                }
            }
            if (var4_6) {
                WebcamLock.LOG.warn("Lock file {} for {} is broken - recreating it", (Object)this.lock, (Object)this.webcam);
                this.write(-1L);
            }
        }
        return var2_4;
    }

    private void update() {
        if (this.disabled.get()) {
            return;
        }
        this.write(System.currentTimeMillis());
    }

    public void lock() {
        if (this.disabled.get()) {
            return;
        }
        if (this.isLocked()) {
            throw new WebcamLockException(String.format("Webcam %s has already been locked", this.webcam.getName()));
        }
        if (!this.locked.compareAndSet(false, true)) {
            return;
        }
        LOG.debug("Lock {}", (Object)this.webcam);
        this.update();
        this.updater = new LockUpdater();
        this.updater.start();
    }

    public void disable() {
        if (this.disabled.compareAndSet(false, true)) {
            LOG.info("Locking mechanism has been disabled in {}", (Object)this.webcam);
            if (this.updater != null) {
                this.updater.interrupt();
            }
        }
    }

    public void unlock() {
        if (this.disabled.get()) {
            return;
        }
        if (!this.locked.compareAndSet(true, false)) {
            return;
        }
        LOG.debug("Unlock {}", (Object)this.webcam);
        this.updater.interrupt();
        this.write(-1L);
        if (!this.lock.delete()) {
            this.lock.deleteOnExit();
        }
    }

    public boolean isLocked() {
        if (this.disabled.get()) {
            return false;
        }
        if (this.locked.get()) {
            return true;
        }
        if (!this.lock.exists()) {
            return false;
        }
        long l2 = System.currentTimeMillis();
        long l3 = this.read();
        LOG.trace("Lock timestamp {} now {} for {}", l3, l2, this.webcam);
        return l3 > l2 - 4000L;
    }

    public File getLockFile() {
        return this.lock;
    }

    class LockUpdater
    extends Thread {
        public LockUpdater() {
            this.setName(String.format("webcam-lock-[%s]", WebcamLock.this.webcam.getName()));
            this.setDaemon(true);
            this.setUncaughtExceptionHandler(WebcamExceptionHandler.getInstance());
        }

        @Override
        public void run() {
            do {
                if (WebcamLock.this.disabled.get()) {
                    return;
                }
                WebcamLock.this.update();
                try {
                    Thread.sleep(2000L);
                }
                catch (InterruptedException interruptedException) {
                    LOG.debug("Lock updater has been interrupted");
                    return;
                }
            } while (WebcamLock.this.locked.get());
        }
    }
}

