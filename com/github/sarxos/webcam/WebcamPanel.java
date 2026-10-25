/*
 * Decompiled with CFR 0.152.
 */
package com.github.sarxos.webcam;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamEvent;
import com.github.sarxos.webcam.WebcamException;
import com.github.sarxos.webcam.WebcamExceptionHandler;
import com.github.sarxos.webcam.WebcamListener;
import com.github.sarxos.webcam.WebcamUtils;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebcamPanel
extends JPanel
implements WebcamListener,
PropertyChangeListener {
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LoggerFactory.getLogger(WebcamPanel.class);
    public static final double MIN_FREQUENCY = 0.016;
    private static final double MAX_FREQUENCY = 50.0;
    private static final ThreadFactory THREAD_FACTORY = new PanelThreadFactory();
    public static final Map<RenderingHints.Key, Object> DEFAULT_IMAGE_RENDERING_HINTS = new HashMap<RenderingHints.Key, Object>();
    private final Runnable repaint = new SwingRepainter(this);
    private Map<RenderingHints.Key, Object> imageRenderingHints = new HashMap<RenderingHints.Key, Object>(DEFAULT_IMAGE_RENDERING_HINTS);
    private ScheduledExecutorService executor = null;
    private ResourceBundle rb = null;
    private DrawMode drawMode = DrawMode.FIT;
    private double frequency = 5.0;
    private boolean frequencyLimit = false;
    private boolean frequencyDisplayed = false;
    private boolean imageSizeDisplayed = false;
    private boolean antialiasingEnabled = true;
    private final Webcam webcam;
    private final ImageSupplier supplier;
    private final ImageUpdater updater;
    private BufferedImage image = null;
    private volatile boolean starting = false;
    private volatile boolean paused = false;
    private volatile boolean errored = false;
    private final AtomicBoolean started = new AtomicBoolean(false);
    private final Painter defaultPainter;
    private Painter painter = this.defaultPainter = new DefaultPainter();
    private Dimension defaultSize = null;
    private boolean displayDebugInfo = false;
    private boolean mirrored = false;

    public WebcamPanel(Webcam webcam) {
        this(webcam, true);
    }

    public WebcamPanel(Webcam webcam, boolean bl) {
        this(webcam, null, bl);
    }

    public WebcamPanel(Webcam webcam, Dimension dimension, boolean bl) {
        this(webcam, dimension, bl, new DefaultImageSupplier(webcam));
    }

    public WebcamPanel(Webcam webcam, Dimension dimension, boolean bl, ImageSupplier imageSupplier) {
        if (webcam == null) {
            throw new IllegalArgumentException(String.format("Webcam argument in %s constructor cannot be null!", this.getClass().getSimpleName()));
        }
        this.defaultSize = dimension;
        this.webcam = webcam;
        this.updater = new ImageUpdater();
        this.supplier = imageSupplier;
        this.rb = WebcamUtils.loadRB(WebcamPanel.class, this.getLocale());
        this.setDoubleBuffered(true);
        this.addPropertyChangeListener("locale", this);
        if (dimension == null) {
            dimension = webcam.getViewSize();
            if (dimension == null) {
                dimension = webcam.getViewSizes()[0];
            }
            this.setPreferredSize(dimension);
        } else {
            this.setPreferredSize(dimension);
        }
        if (bl) {
            this.start();
        }
    }

    public void setPainter(Painter painter) {
        this.painter = painter;
    }

    public Painter getPainter() {
        return this.painter;
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        if (this.image == null) {
            this.painter.paintPanel(this, (Graphics2D)graphics);
            return;
        }
        WebcamPanel webcamPanel = this;
        this.painter.paintImage(webcamPanel, webcamPanel.image, (Graphics2D)graphics);
    }

    public void start() {
        if (!this.started.compareAndSet(false, true)) {
            return;
        }
        this.webcam.addWebcamListener(this);
        LOG.debug("Starting panel rendering and trying to open attached webcam");
        this.updater.start();
        this.starting = true;
        SwingWorker<Void, Void> swingWorker = new SwingWorker<Void, Void>(){

            @Override
            protected Void doInBackground() throws Exception {
                try {
                    if (!WebcamPanel.this.webcam.isOpen()) {
                        WebcamPanel.this.errored = !WebcamPanel.this.webcam.open();
                    }
                }
                catch (WebcamException webcamException) {
                    WebcamPanel.this.errored = true;
                    throw webcamException;
                }
                finally {
                    WebcamPanel.this.starting = false;
                    WebcamPanel.this.repaintPanel();
                }
                return null;
            }
        };
        swingWorker.execute();
    }

    public void stop() {
        if (!this.started.compareAndSet(true, false)) {
            return;
        }
        this.webcam.removeWebcamListener(this);
        LOG.debug("Stopping panel rendering and closing attached webcam");
        try {
            this.updater.stop();
        }
        catch (InterruptedException interruptedException) {
            throw new RuntimeException(interruptedException);
        }
        this.image = null;
        SwingWorker<Void, Void> swingWorker = new SwingWorker<Void, Void>(){

            @Override
            protected Void doInBackground() throws Exception {
                try {
                    if (WebcamPanel.this.webcam.isOpen()) {
                        WebcamPanel.this.errored = !WebcamPanel.this.webcam.close();
                    }
                }
                catch (WebcamException webcamException) {
                    WebcamPanel.this.errored = true;
                    throw webcamException;
                }
                finally {
                    WebcamPanel.this.repaintPanel();
                }
                return null;
            }
        };
        swingWorker.execute();
    }

    private void repaintPanel() {
        SwingUtilities.invokeLater(this.repaint);
    }

    public void pause() {
        if (this.paused) {
            return;
        }
        LOG.debug("Pausing panel rendering");
        this.paused = true;
    }

    public void resume() {
        if (!this.paused) {
            return;
        }
        LOG.debug("Resuming panel rendering");
        this.paused = false;
    }

    public boolean isFPSLimited() {
        return this.frequencyLimit;
    }

    public void setFPSLimited(boolean bl) {
        this.frequencyLimit = bl;
    }

    public double getFPSLimit() {
        return this.frequency;
    }

    public void setFPSLimit(double d2) {
        if (d2 > 50.0) {
            d2 = 50.0;
        }
        if (d2 < 0.016) {
            d2 = 0.016;
        }
        this.frequency = d2;
    }

    public boolean isDisplayDebugInfo() {
        return this.displayDebugInfo;
    }

    public void setDisplayDebugInfo(boolean bl) {
        this.displayDebugInfo = bl;
    }

    public boolean isFPSDisplayed() {
        return this.frequencyDisplayed;
    }

    public void setFPSDisplayed(boolean bl) {
        this.frequencyDisplayed = bl;
    }

    public boolean isImageSizeDisplayed() {
        return this.imageSizeDisplayed;
    }

    public void setImageSizeDisplayed(boolean bl) {
        this.imageSizeDisplayed = bl;
    }

    public void setAntialiasingEnabled(boolean bl) {
        this.antialiasingEnabled = bl;
    }

    public boolean isAntialiasingEnabled() {
        return this.antialiasingEnabled;
    }

    public boolean isStarting() {
        return this.starting;
    }

    public boolean isStarted() {
        return this.started.get();
    }

    public DrawMode getDrawMode() {
        return this.drawMode;
    }

    public void setDrawMode(DrawMode drawMode) {
        this.drawMode = drawMode;
    }

    public boolean isErrored() {
        return this.errored;
    }

    @Deprecated
    public Map<RenderingHints.Key, Object> getImageRenderingHints() {
        return this.imageRenderingHints;
    }

    @Deprecated
    public boolean isFitArea() {
        return this.drawMode == DrawMode.FIT;
    }

    @Deprecated
    public void setFitArea(boolean bl) {
        this.drawMode = bl ? DrawMode.FIT : DrawMode.NONE;
    }

    @Deprecated
    public void setFillArea(boolean bl) {
        this.drawMode = bl ? DrawMode.FILL : DrawMode.NONE;
    }

    @Deprecated
    public boolean isFillArea() {
        return this.drawMode == DrawMode.FILL;
    }

    public Painter getDefaultPainter() {
        return this.defaultPainter;
    }

    @Override
    public void propertyChange(PropertyChangeEvent serializable) {
        if ((serializable = (Locale)serializable.getNewValue()) != null) {
            this.rb = WebcamUtils.loadRB(WebcamPanel.class, (Locale)serializable);
        }
    }

    @Override
    public void webcamOpen(WebcamEvent webcamEvent) {
        if (this.defaultSize == null) {
            WebcamPanel webcamPanel = this;
            webcamPanel.setPreferredSize(webcamPanel.webcam.getViewSize());
        }
    }

    @Override
    public void webcamClosed(WebcamEvent webcamEvent) {
        this.stop();
    }

    @Override
    public void webcamDisposed(WebcamEvent webcamEvent) {
        this.stop();
    }

    @Override
    public void webcamImageObtained(WebcamEvent webcamEvent) {
    }

    public boolean isMirrored() {
        return this.mirrored;
    }

    public void setMirrored(boolean bl) {
        this.mirrored = bl;
    }

    public Webcam getWebcam() {
        return this.webcam;
    }

    public BufferedImage getImage() {
        return this.image;
    }

    static {
        DEFAULT_IMAGE_RENDERING_HINTS.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        DEFAULT_IMAGE_RENDERING_HINTS.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
        DEFAULT_IMAGE_RENDERING_HINTS.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }

    class ImageUpdater
    implements Runnable {
        private Thread scheduler = null;
        private AtomicBoolean running = new AtomicBoolean(false);

        private ImageUpdater() {
        }

        public void start() {
            if (this.running.compareAndSet(false, true)) {
                WebcamPanel.this.executor = Executors.newScheduledThreadPool(1, THREAD_FACTORY);
                this.scheduler = new RepaintScheduler();
                this.scheduler.start();
            }
        }

        public void stop() throws InterruptedException {
            if (this.running.compareAndSet(true, false)) {
                WebcamPanel.this.executor.shutdown();
                WebcamPanel.this.executor.awaitTermination(5000L, TimeUnit.MILLISECONDS);
                this.scheduler.join();
            }
        }

        @Override
        public void run() {
            try {
                this.update();
                return;
            }
            catch (Throwable throwable) {
                WebcamPanel.this.errored = true;
                WebcamExceptionHandler.handle(throwable);
                return;
            }
        }

        private void update() {
            if (!this.running.get() || !WebcamPanel.this.webcam.isOpen() || WebcamPanel.this.paused) {
                return;
            }
            BufferedImage bufferedImage = WebcamPanel.this.supplier.get();
            boolean bl = true;
            if (bufferedImage != null) {
                if (WebcamPanel.this.image == bufferedImage) {
                    bl = false;
                }
                WebcamPanel.this.errored = false;
                WebcamPanel.this.image = bufferedImage;
            }
            if (bl) {
                WebcamPanel.this.repaintPanel();
            }
        }

        class RepaintScheduler
        extends Thread {
            public RepaintScheduler() {
                this.setUncaughtExceptionHandler(WebcamExceptionHandler.getInstance());
                this.setName(String.format("repaint-scheduler-%s", WebcamPanel.this.webcam.getName()));
                this.setDaemon(true);
            }

            @Override
            public void run() {
                block9: {
                    if (!ImageUpdater.this.running.get()) {
                        return;
                    }
                    WebcamPanel.this.repaintPanel();
                    while (WebcamPanel.this.starting) {
                        try {
                            Thread.sleep(50L);
                        }
                        catch (InterruptedException interruptedException) {
                            throw new RuntimeException(interruptedException);
                        }
                    }
                    try {
                        if (WebcamPanel.this.webcam.isOpen()) {
                            if (WebcamPanel.this.isFPSLimited()) {
                                WebcamPanel.this.executor.scheduleAtFixedRate(WebcamPanel.this.updater, 0L, (long)(1000.0 / WebcamPanel.this.frequency), TimeUnit.MILLISECONDS);
                            } else {
                                WebcamPanel.this.executor.scheduleWithFixedDelay(WebcamPanel.this.updater, 100L, 1L, TimeUnit.MILLISECONDS);
                            }
                            break block9;
                        }
                        WebcamPanel.this.executor.schedule(this, 500L, TimeUnit.MILLISECONDS);
                        return;
                    }
                    catch (RejectedExecutionException rejectedExecutionException) {
                        LOG.warn("Executor rejected paint update");
                        LOG.trace("Executor rejected paint update because of", rejectedExecutionException);
                    }
                }
            }
        }
    }

    static final class SwingRepainter
    implements Runnable {
        private WebcamPanel panel = null;

        public SwingRepainter(WebcamPanel webcamPanel) {
            this.panel = webcamPanel;
        }

        @Override
        public final void run() {
            this.panel.repaint();
        }
    }

    static final class PanelThreadFactory
    implements ThreadFactory {
        private static final AtomicInteger number = new AtomicInteger(0);

        private PanelThreadFactory() {
        }

        @Override
        public final Thread newThread(Runnable runnable2) {
            runnable2 = new Thread(runnable2, String.format("webcam-panel-scheduled-executor-%d", number.incrementAndGet()));
            ((Thread)runnable2).setUncaughtExceptionHandler(WebcamExceptionHandler.getInstance());
            ((Thread)runnable2).setDaemon(true);
            return runnable2;
        }
    }

    public class DefaultPainter
    implements Painter {
        private String name = null;
        private long lastRepaintTime = -1L;
        private BufferedImage resizedImage = null;

        @Override
        public void paintPanel(WebcamPanel object, Graphics2D graphics2D) {
            assert (object != null);
            assert (graphics2D != null);
            object = graphics2D.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, WebcamPanel.this.isAntialiasingEnabled() ? RenderingHints.VALUE_ANTIALIAS_ON : RenderingHints.VALUE_ANTIALIAS_OFF);
            graphics2D.setBackground(Color.BLACK);
            graphics2D.fillRect(0, 0, WebcamPanel.this.getWidth(), WebcamPanel.this.getHeight());
            int n2 = (WebcamPanel.this.getWidth() - 70) / 2;
            int n3 = (WebcamPanel.this.getHeight() - 40) / 2;
            graphics2D.setStroke(new BasicStroke(2.0f));
            graphics2D.setColor(Color.LIGHT_GRAY);
            graphics2D.fillRoundRect(n2, n3, 70, 40, 10, 10);
            graphics2D.setColor(Color.WHITE);
            graphics2D.fillOval(n2 + 5, n3 + 5, 30, 30);
            graphics2D.setColor(Color.LIGHT_GRAY);
            graphics2D.fillOval(n2 + 10, n3 + 10, 20, 20);
            graphics2D.setColor(Color.WHITE);
            graphics2D.fillOval(n2 + 12, n3 + 12, 16, 16);
            graphics2D.fillRoundRect(n2 + 50, n3 + 5, 15, 10, 5, 5);
            graphics2D.fillRect(n2 + 63, n3 + 25, 7, 2);
            graphics2D.fillRect(n2 + 63, n3 + 28, 7, 2);
            graphics2D.fillRect(n2 + 63, n3 + 31, 7, 2);
            graphics2D.setColor(Color.DARK_GRAY);
            graphics2D.setStroke(new BasicStroke(3.0f));
            graphics2D.drawLine(0, 0, WebcamPanel.this.getWidth(), WebcamPanel.this.getHeight());
            graphics2D.drawLine(0, WebcamPanel.this.getHeight(), WebcamPanel.this.getWidth(), 0);
            String string = WebcamPanel.this.rb.getString("INITIALIZING_DEVICE");
            Object object2 = WebcamPanel.this.rb.getString("NO_IMAGE");
            String string2 = WebcamPanel.this.rb.getString("DEVICE_ERROR");
            string = WebcamPanel.this.errored ? string2 : (WebcamPanel.this.starting ? string : object2);
            object2 = graphics2D.getFontMetrics(WebcamPanel.this.getFont());
            int n4 = ((FontMetrics)object2).stringWidth(string);
            int n5 = ((FontMetrics)object2).getHeight();
            n4 = (WebcamPanel.this.getWidth() - n4) / 2;
            n5 = n3 - n5;
            graphics2D.setFont(WebcamPanel.this.getFont());
            graphics2D.setColor(Color.WHITE);
            graphics2D.drawString(string, n4, n5);
            if (this.name == null) {
                this.name = WebcamPanel.this.webcam.getName();
            }
            string = this.name;
            n4 = ((FontMetrics)object2).stringWidth(string);
            n5 = ((FontMetrics)object2).getHeight();
            graphics2D.drawString(string, (WebcamPanel.this.getWidth() - n4) / 2, n3 - 2 * n5);
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, object);
        }

        @Override
        public void paintImage(WebcamPanel webcamPanel, BufferedImage bufferedImage, Graphics2D graphics2D) {
            int n2;
            Object object;
            assert (webcamPanel != null);
            assert (bufferedImage != null);
            assert (graphics2D != null);
            int n3 = WebcamPanel.this.getWidth();
            int n4 = WebcamPanel.this.getHeight();
            int n5 = bufferedImage.getWidth();
            int n6 = bufferedImage.getHeight();
            Object object2 = graphics2D.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
            Object object3 = graphics2D.getRenderingHint(RenderingHints.KEY_RENDERING);
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
            graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
            graphics2D.setBackground(Color.BLACK);
            graphics2D.setColor(Color.BLACK);
            graphics2D.fillRect(0, 0, n3, n4);
            int n7 = 0;
            int n8 = 0;
            int n9 = 0;
            int n10 = 0;
            switch (WebcamPanel.this.drawMode) {
                case NONE: {
                    n9 = bufferedImage.getWidth();
                    n10 = bufferedImage.getHeight();
                    break;
                }
                case FILL: {
                    n9 = n3;
                    n10 = n4;
                    break;
                }
                case FIT: {
                    double d2 = Math.max((double)n5 / (double)n3, (double)n6 / (double)n4);
                    double d3 = (double)n5 / d2;
                    double d4 = (double)n6 / d2;
                    double d5 = ((double)n3 - d3) / 2.0;
                    double d6 = ((double)n4 - d4) / 2.0;
                    n9 = (int)d3;
                    n10 = (int)d4;
                    n7 = (int)d5;
                    n8 = (int)d6;
                }
            }
            if (this.resizedImage != null) {
                this.resizedImage.flush();
            }
            if (n9 == bufferedImage.getWidth() && n10 == bufferedImage.getHeight() && !WebcamPanel.this.mirrored) {
                this.resizedImage = bufferedImage;
            } else {
                GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
                object = graphicsEnvironment.getDefaultScreenDevice().getDefaultConfiguration();
                Graphics graphics = null;
                try {
                    int n11;
                    this.resizedImage = ((GraphicsConfiguration)object).createCompatibleImage(n3, n4);
                    graphics = this.resizedImage.createGraphics();
                    ((Graphics2D)graphics).setComposite(AlphaComposite.Src);
                    for (Map.Entry entry : WebcamPanel.this.imageRenderingHints.entrySet()) {
                        ((Graphics2D)graphics).setRenderingHint((RenderingHints.Key)entry.getKey(), entry.getValue());
                    }
                    ((Graphics2D)graphics).setBackground(Color.BLACK);
                    graphics.setColor(Color.BLACK);
                    graphics.fillRect(0, 0, n3, n4);
                    int n12 = n7;
                    int n13 = n8;
                    int n14 = n7 + n9;
                    n7 = n8 + n10;
                    if (WebcamPanel.this.mirrored) {
                        n2 = n5;
                        n11 = 0;
                    } else {
                        n2 = 0;
                        n11 = n5;
                    }
                    graphics.drawImage(bufferedImage, n12, n13, n14, n7, n2, 0, n11, n6, null);
                }
                finally {
                    if (graphics != null) {
                        graphics.dispose();
                    }
                }
            }
            graphics2D.drawImage((Image)this.resizedImage, 0, 0, null);
            if (WebcamPanel.this.isFPSDisplayed()) {
                String string = String.format("FPS: %.1f", WebcamPanel.this.webcam.getFPS());
                int n15 = n4 - 5;
                graphics2D.setFont(WebcamPanel.this.getFont());
                graphics2D.setColor(Color.BLACK);
                graphics2D.drawString(string, 6, n15 + 1);
                graphics2D.setColor(Color.WHITE);
                graphics2D.drawString(string, 5, n15);
            }
            if (WebcamPanel.this.isImageSizeDisplayed()) {
                String string = String.format("%d\u2a2f%d px", n5, n6);
                object = graphics2D.getFontMetrics(WebcamPanel.this.getFont());
                int n16 = ((FontMetrics)object).stringWidth(string);
                n2 = n3 - n16 - 5;
                int n17 = n4 - 5;
                graphics2D.setFont(WebcamPanel.this.getFont());
                graphics2D.setColor(Color.BLACK);
                graphics2D.drawString(string, n2 + 1, n17 + 1);
                graphics2D.setColor(Color.WHITE);
                graphics2D.drawString(string, n2, n17);
            }
            if (WebcamPanel.this.isDisplayDebugInfo()) {
                if (this.lastRepaintTime < 0L) {
                    this.lastRepaintTime = System.currentTimeMillis();
                } else {
                    long l2 = System.currentTimeMillis();
                    String string = String.format("DEBUG: repaints per second: %.1f", 1000.0 / (double)(l2 - this.lastRepaintTime));
                    this.lastRepaintTime = l2;
                    graphics2D.setFont(WebcamPanel.this.getFont());
                    graphics2D.setColor(Color.BLACK);
                    graphics2D.drawString(string, 6, 16);
                    graphics2D.setColor(Color.WHITE);
                    graphics2D.drawString(string, 5, 15);
                }
            }
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, object2);
            graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING, object3);
        }
    }

    public static interface Painter {
        public void paintPanel(WebcamPanel var1, Graphics2D var2);

        public void paintImage(WebcamPanel var1, BufferedImage var2, Graphics2D var3);
    }

    static class DefaultImageSupplier
    implements ImageSupplier {
        private final Webcam webcam;

        public DefaultImageSupplier(Webcam webcam) {
            this.webcam = webcam;
        }

        @Override
        public BufferedImage get() {
            return this.webcam.getImage();
        }
    }

    public static interface ImageSupplier {
        public BufferedImage get();
    }

    public static enum DrawMode {
        NONE,
        FILL,
        FIT;

    }
}

