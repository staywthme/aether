/*
 * Decompiled with CFR 0.152.
 */
package x;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.Serializable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public final class a {
    private static final AtomicReference<WebSocket> a = new AtomicReference<Object>(null);
    private static volatile JFrame b = null;
    private static volatile JTextArea c = null;
    private static volatile JTextField d = null;
    private static volatile boolean e = false;

    public static void a(String object, String string) {
        string = string != null && !string.isEmpty() ? string : $S.d(234);
        object = new Thread(new Runnable(){
            private /* synthetic */ String a;
            private /* synthetic */ String b;
            {
                this.a = string;
                this.b = string2;
            }

            @Override
            public final void run() {
                x.a.b(this.a, this.b);
            }
        }, $S.d(235));
        ((Thread)object).setDaemon(true);
        ((Thread)object).start();
    }

    private static synchronized void h() {
        if (e) {
            return;
        }
        e = true;
        CountDownLatch countDownLatch = new CountDownLatch(1);
        SwingUtilities.invokeLater(new Runnable(countDownLatch){
            private /* synthetic */ CountDownLatch a;
            {
                this.a = countDownLatch;
            }

            @Override
            public final void run() {
                x.a.e();
                this.a.countDown();
            }
        });
        try {
            countDownLatch.await(5L, TimeUnit.SECONDS);
            return;
        }
        catch (InterruptedException interruptedException) {
            return;
        }
    }

    private static void e(String string, String string2) {
        SwingUtilities.invokeLater(new Runnable(){
            private /* synthetic */ String a;
            private /* synthetic */ String b;
            {
                this.a = string;
                this.b = string2;
            }

            @Override
            public final void run() {
                if (c == null) {
                    return;
                }
                c.append(this.a + ": " + this.b + "\n");
                c.setCaretPosition(c.getDocument().getLength());
            }
        });
    }

    static /* synthetic */ void b(String string, String string2) {
        while (true) {
            try {
                Object object = string2;
                Object object2 = string;
                object2 = (String)object + $S.d(236) + (String)object2 + $S.d(237);
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
                        a.set(webSocket);
                    }

                    @Override
                    public final void onMessage(WebSocket object, String string) {
                        if (string.contains($S.d(232))) {
                            SwingUtilities.invokeLater(new Runnable(this){
                                private /* synthetic */ 2 a;
                                {
                                    this.a = var1_1;
                                }

                                @Override
                                public final void run() {
                                    if (b != null) {
                                        b.setVisible(false);
                                    }
                                }
                            });
                            return;
                        }
                        object = x.a.c(string, "s");
                        if (object != null && !((String)object).isEmpty()) {
                            x.a.h();
                            x.a.e($S.d(233), (String)object);
                            x.a.d();
                        }
                    }

                    @Override
                    public final void onMessage(WebSocket webSocket, ByteString byteString) {
                    }

                    @Override
                    public final void onClosing(WebSocket webSocket, int n2, String string) {
                        a.compareAndSet(webSocket, null);
                        webSocket.close(1000, null);
                    }

                    @Override
                    public final void onClosed(WebSocket webSocket, int n2, String string) {
                        a.compareAndSet(webSocket, null);
                        this.a.set(true);
                        this.b.countDown();
                    }

                    @Override
                    public final void onFailure(WebSocket webSocket, Throwable throwable, Response response) {
                        a.compareAndSet(webSocket, null);
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

    static /* synthetic */ String c(String string, String string2) {
        int n2 = string.indexOf(string2 = "\"" + string2 + $S.d(238));
        if (n2 < 0) {
            return null;
        }
        int n3 = n2 += string2.length();
        while (n2 < string.length()) {
            char c2 = string.charAt(n2);
            if (c2 == '\\') {
                n2 += 2;
                continue;
            }
            if (c2 == '\"') break;
            ++n2;
        }
        return string.substring(n3, n2).replace("\\\"", "\"").replace("\\\\", "\\").replace("\\n", "\n");
    }

    static /* synthetic */ void d() {
        SwingUtilities.invokeLater(new Runnable(){

            @Override
            public final void run() {
                if (b == null) {
                    return;
                }
                b.setVisible(true);
                b.toFront();
                b.requestFocus();
            }
        });
    }

    static /* synthetic */ void e() {
        b = new JFrame($S.d(233));
        b.setSize(340, 400);
        b.setDefaultCloseOperation(0);
        b.setAlwaysOnTop(true);
        b.setResizable(false);
        Serializable serializable = Toolkit.getDefaultToolkit().getScreenSize();
        b.setLocation(((Dimension)serializable).width - 360, ((Dimension)serializable).height - 450);
        serializable = new Color(18, 18, 18);
        Serializable serializable2 = new Color(28, 28, 28);
        Serializable serializable3 = new Color(30, 30, 30);
        Color color = new Color(112, 80, 245);
        Color color2 = new Color(220, 220, 220);
        new Color(100, 100, 100);
        JPanel jPanel = new JPanel(new BorderLayout(0, 0));
        jPanel.setBackground((Color)serializable);
        c = new JTextArea();
        c.setEditable(false);
        c.setBackground((Color)serializable3);
        c.setForeground(color2);
        c.setFont(new Font($S.d(239), 0, 13));
        c.setLineWrap(true);
        c.setWrapStyleWord(true);
        c.setMargin(new Insets(10, 10, 10, 10));
        serializable = new JScrollPane(c, 20, 31);
        ((JComponent)serializable).setBorder(BorderFactory.createEmptyBorder());
        ((JScrollPane)serializable).getViewport().setBackground((Color)serializable3);
        serializable3 = new JPanel(new BorderLayout(6, 0));
        ((JComponent)serializable3).setBackground((Color)serializable2);
        ((JComponent)serializable3).setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(40, 40, 40)), BorderFactory.createEmptyBorder(8, 8, 8, 8)));
        d = new JTextField();
        d.setBackground(new Color(40, 40, 40));
        d.setForeground(color2);
        d.setFont(new Font($S.d(239), 0, 13));
        d.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(55, 55, 55)), BorderFactory.createEmptyBorder(5, 8, 5, 8)));
        d.setCaretColor(color2);
        serializable2 = new JButton($S.d(240));
        ((JComponent)serializable2).setBackground(color);
        ((JComponent)serializable2).setForeground(Color.WHITE);
        ((AbstractButton)serializable2).setFocusPainted(false);
        ((AbstractButton)serializable2).setBorderPainted(false);
        ((JComponent)serializable2).setFont(new Font($S.d(239), 1, 12));
        ((Component)serializable2).setCursor(Cursor.getPredefinedCursor(12));
        ((JComponent)serializable2).setPreferredSize(new Dimension(70, 30));
        ((AbstractButton)serializable2).addActionListener(new ActionListener(){

            @Override
            public final void actionPerformed(ActionEvent actionEvent) {
                x.a.f();
            }
        });
        d.addKeyListener(new KeyAdapter(){

            @Override
            public final void keyPressed(KeyEvent keyEvent) {
                if (keyEvent.getKeyCode() == 10) {
                    x.a.f();
                }
            }
        });
        ((Container)serializable3).add(d, $S.d(241));
        ((Container)serializable3).add((Component)serializable2, $S.d(242));
        jPanel.add((Component)serializable, $S.d(241));
        jPanel.add((Component)serializable3, $S.d(243));
        b.setContentPane(jPanel);
    }

    static /* synthetic */ void f() {
        String string;
        if (d != null && !(string = d.getText().trim()).isEmpty()) {
            d.setText("");
            WebSocket webSocket = a.get();
            if (webSocket != null) {
                String string2 = string.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "");
                webSocket.send($S.d(244) + string2 + "\"}");
            }
            x.a.e($S.d(245), string);
        }
    }
}

