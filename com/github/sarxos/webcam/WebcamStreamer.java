/*
 * Decompiled with CFR 0.152.
 */
package com.github.sarxos.webcam;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamEvent;
import com.github.sarxos.webcam.WebcamExceptionHandler;
import com.github.sarxos.webcam.WebcamListener;
import java.awt.image.BufferedImage;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicBoolean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebcamStreamer
implements WebcamListener,
ThreadFactory {
    private static final Logger LOG = LoggerFactory.getLogger(WebcamStreamer.class);
    private static final String BOUNDARY = "mjpegframe";
    private static final String CRLF = "\r\n";
    private Webcam webcam = null;
    private double fps = 0.0;
    private int number = 0;
    private int port = 0;
    private long last = -1L;
    private long delay = -1L;
    private BufferedImage image = null;
    private ExecutorService executor = Executors.newCachedThreadPool(this);
    private AtomicBoolean started = new AtomicBoolean(false);

    public WebcamStreamer(int n2, Webcam webcam, double d2, boolean bl) {
        if (webcam == null) {
            throw new IllegalArgumentException("Webcam for streaming cannot be null");
        }
        this.port = n2;
        this.webcam = webcam;
        this.fps = d2;
        this.delay = (long)(1000.0 / d2);
        if (bl) {
            this.start();
        }
    }

    @Override
    public Thread newThread(Runnable runnable2) {
        runnable2 = new Thread(runnable2, String.format("streamer-thread-%s", this.number++));
        ((Thread)runnable2).setUncaughtExceptionHandler(WebcamExceptionHandler.getInstance());
        ((Thread)runnable2).setDaemon(true);
        return runnable2;
    }

    public void start() {
        if (this.started.compareAndSet(false, true)) {
            this.webcam.addWebcamListener(this);
            this.webcam.open();
            this.executor.execute(new Acceptor());
        }
    }

    public void stop() {
        if (this.started.compareAndSet(true, false)) {
            this.executor.shutdown();
            this.webcam.removeWebcamListener(this);
            this.webcam.close();
        }
    }

    @Override
    public void webcamOpen(WebcamEvent webcamEvent) {
        this.start();
    }

    @Override
    public void webcamClosed(WebcamEvent webcamEvent) {
        this.stop();
    }

    @Override
    public void webcamDisposed(WebcamEvent webcamEvent) {
    }

    @Override
    public void webcamImageObtained(WebcamEvent webcamEvent) {
    }

    public double getFPS() {
        return this.fps;
    }

    public boolean isInitialized() {
        return this.started.get();
    }

    public int getPort() {
        return this.port;
    }

    static /* synthetic */ Webcam access$400(WebcamStreamer webcamStreamer) {
        return webcamStreamer.webcam;
    }

    static /* synthetic */ long access$500(WebcamStreamer webcamStreamer) {
        return webcamStreamer.last;
    }

    static /* synthetic */ long access$600(WebcamStreamer webcamStreamer) {
        return webcamStreamer.delay;
    }

    static /* synthetic */ BufferedImage access$702(WebcamStreamer webcamStreamer, BufferedImage bufferedImage) {
        webcamStreamer.image = bufferedImage;
        return webcamStreamer.image;
    }

    static /* synthetic */ BufferedImage access$700(WebcamStreamer webcamStreamer) {
        return webcamStreamer.image;
    }

    class Connection
    implements Runnable {
        private Socket socket = null;

        public Connection(Socket socket) {
            this.socket = socket;
        }

        /*
         * Exception decompiling
         */
        @Override
        public void run() {
            /*
             * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
             * 
             * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [3[TRYBLOCK]], but top level block is 50[DOLOOP]
             *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:435)
             *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:484)
             *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
             *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
             *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
             *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
             *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
             *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
             *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
             *     at org.benf.cfr.reader.entities.ClassFile.analyseInnerClassesPass1(ClassFile.java:923)
             *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1035)
             *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
             *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
             *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
             *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
             *     at org.benf.cfr.reader.Main.main(Main.java:54)
             */
            throw new IllegalStateException("Decompilation failed");
        }
    }

    class Acceptor
    implements Runnable {
        private Acceptor() {
        }

        @Override
        public void run() {
            try {
                ServerSocket serverSocket = new ServerSocket(WebcamStreamer.this.port, 50, InetAddress.getByName("0.0.0.0"));
                Throwable throwable = null;
                try {
                    while (WebcamStreamer.this.started.get()) {
                        WebcamStreamer.this.executor.execute(new Connection(serverSocket.accept()));
                    }
                }
                catch (Throwable throwable2) {
                    try {
                        Throwable throwable3 = throwable2;
                        throwable = throwable2;
                        throw throwable3;
                    }
                    catch (Throwable throwable4) {
                        if (throwable != null) {
                            try {
                                serverSocket.close();
                            }
                            catch (Throwable throwable5) {
                                throwable.addSuppressed(throwable5);
                            }
                        } else {
                            serverSocket.close();
                        }
                        throw throwable4;
                    }
                }
                serverSocket.close();
            }
            catch (Exception exception) {
                LOG.error("Cannot accept socket connection", exception);
            }
        }
    }
}

