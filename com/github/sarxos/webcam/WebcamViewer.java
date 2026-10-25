/*
 * Decompiled with CFR 0.152.
 */
package com.github.sarxos.webcam;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamEvent;
import com.github.sarxos.webcam.WebcamListener;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamPicker;
import com.github.sarxos.webcam.WebcamResolution;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebcamViewer
extends JFrame
implements WebcamListener,
ItemListener,
WindowListener,
Runnable,
Thread.UncaughtExceptionHandler {
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LoggerFactory.getLogger(WebcamViewer.class);
    private Webcam webcam = null;
    private WebcamPanel panel = null;
    private WebcamPicker picker = null;

    public WebcamViewer() {
        SwingUtilities.invokeLater(this);
    }

    @Override
    public void run() {
        this.setTitle("Webcam Capture Viewer");
        this.setDefaultCloseOperation(3);
        this.setLayout(new BorderLayout());
        WebcamViewer webcamViewer = this;
        webcamViewer.addWindowListener(webcamViewer);
        this.picker = new WebcamPicker();
        this.picker.addItemListener(this);
        this.webcam = this.picker.getSelectedWebcam();
        if (this.webcam == null) {
            LOG.error("No webcams found");
            System.exit(1);
        }
        this.webcam.setViewSize(WebcamResolution.VGA.getSize());
        this.webcam.addWebcamListener(this);
        this.panel = new WebcamPanel(this.webcam, false);
        this.panel.setFPSDisplayed(true);
        WebcamViewer webcamViewer2 = this;
        webcamViewer2.add((Component)webcamViewer2.picker, "North");
        WebcamViewer webcamViewer3 = this;
        webcamViewer3.add((Component)webcamViewer3.panel, "Center");
        this.pack();
        this.setVisible(true);
        Thread thread2 = new Thread(){

            @Override
            public void run() {
                WebcamViewer.this.panel.start();
            }
        };
        thread2.setName("webcam-viewer-starter");
        thread2.setDaemon(true);
        thread2.setUncaughtExceptionHandler(this);
        thread2.start();
    }

    @Override
    public void webcamOpen(WebcamEvent webcamEvent) {
        LOG.info("Webcam open");
    }

    @Override
    public void webcamClosed(WebcamEvent webcamEvent) {
        LOG.info("Webcam closed");
    }

    @Override
    public void webcamDisposed(WebcamEvent webcamEvent) {
        LOG.info("Webcam disposed");
    }

    @Override
    public void webcamImageObtained(WebcamEvent webcamEvent) {
    }

    @Override
    public void windowActivated(WindowEvent windowEvent) {
    }

    @Override
    public void windowClosed(WindowEvent windowEvent) {
        this.webcam.close();
    }

    @Override
    public void windowClosing(WindowEvent windowEvent) {
    }

    @Override
    public void windowOpened(WindowEvent windowEvent) {
    }

    @Override
    public void windowDeactivated(WindowEvent windowEvent) {
    }

    @Override
    public void windowDeiconified(WindowEvent windowEvent) {
        LOG.info("Webcam viewer resumed");
        this.panel.resume();
    }

    @Override
    public void windowIconified(WindowEvent windowEvent) {
        LOG.info("Webcam viewer paused");
        this.panel.pause();
    }

    @Override
    public void uncaughtException(Thread thread2, Throwable throwable) {
        throwable.printStackTrace();
        LOG.error(String.format("Exception in thread %s", thread2.getName()), throwable);
    }

    @Override
    public void itemStateChanged(ItemEvent object) {
        if (((ItemEvent)object).getItem() == this.webcam) {
            return;
        }
        if (this.webcam == null) {
            return;
        }
        final WebcamPanel webcamPanel = this.panel;
        WebcamViewer webcamViewer = this;
        webcamViewer.remove(webcamViewer.panel);
        this.webcam.removeWebcamListener(this);
        this.webcam = (Webcam)((ItemEvent)object).getItem();
        this.webcam.setViewSize(WebcamResolution.VGA.getSize());
        this.webcam.addWebcamListener(this);
        System.out.println("selected " + this.webcam.getName());
        this.panel = new WebcamPanel(this.webcam, false);
        WebcamViewer webcamViewer2 = this;
        webcamViewer2.add((Component)webcamViewer2.panel, "Center");
        object = new Thread(){

            @Override
            public void run() {
                webcamPanel.stop();
                WebcamViewer.this.panel.start();
            }
        };
        ((Thread)object).setDaemon(true);
        ((Thread)object).setUncaughtExceptionHandler(this);
        ((Thread)object).start();
    }
}

