/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna.internal;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Cleaner {
    private static final Cleaner INSTANCE = new Cleaner();
    private final ReferenceQueue<Object> referenceQueue = new ReferenceQueue();
    private Thread cleanerThread;
    private CleanerRef firstCleanable;

    public static Cleaner getCleaner() {
        return INSTANCE;
    }

    private Cleaner() {
    }

    public synchronized Cleanable register(Object object, Runnable runnable2) {
        return this.add(new CleanerRef(this, object, this.referenceQueue, runnable2));
    }

    private synchronized CleanerRef add(CleanerRef cleanerRef) {
        ReferenceQueue<Object> referenceQueue = this.referenceQueue;
        synchronized (referenceQueue) {
            if (this.firstCleanable == null) {
                this.firstCleanable = cleanerRef;
            } else {
                cleanerRef.setNext(this.firstCleanable);
                this.firstCleanable.setPrevious(cleanerRef);
                this.firstCleanable = cleanerRef;
            }
            if (this.cleanerThread == null) {
                Logger.getLogger(Cleaner.class.getName()).log(Level.FINE, "Starting CleanerThread");
                this.cleanerThread = new CleanerThread();
                this.cleanerThread.start();
            }
            return cleanerRef;
        }
    }

    private synchronized boolean remove(CleanerRef cleanerRef) {
        ReferenceQueue<Object> referenceQueue = this.referenceQueue;
        synchronized (referenceQueue) {
            boolean bl = false;
            if (cleanerRef == this.firstCleanable) {
                this.firstCleanable = cleanerRef.getNext();
                bl = true;
            }
            if (cleanerRef.getPrevious() != null) {
                cleanerRef.getPrevious().setNext(cleanerRef.getNext());
            }
            if (cleanerRef.getNext() != null) {
                cleanerRef.getNext().setPrevious(cleanerRef.getPrevious());
            }
            if (cleanerRef.getPrevious() != null || cleanerRef.getNext() != null) {
                bl = true;
            }
            cleanerRef.setNext(null);
            cleanerRef.setPrevious(null);
            return bl;
        }
    }

    class CleanerThread
    extends Thread {
        private static final long CLEANER_LINGER_TIME = 30000L;

        public CleanerThread() {
            super("JNA Cleaner");
            this.setDaemon(true);
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        @Override
        public void run() {
            while (true) {
                try {
                    while (true) {
                        Object object;
                        if ((object = Cleaner.this.referenceQueue.remove(30000L)) instanceof CleanerRef) {
                            ((CleanerRef)object).clean();
                            continue;
                        }
                        if (object != null) continue;
                        object = Cleaner.this.referenceQueue;
                        synchronized (object) {
                            Logger logger = Logger.getLogger(Cleaner.class.getName());
                            if (Cleaner.this.firstCleanable == null) {
                                Cleaner.this.cleanerThread = null;
                                logger.log(Level.FINE, "Shutting down CleanerThread");
                                return;
                            }
                            if (logger.isLoggable(Level.FINER)) {
                                StringBuilder stringBuilder = new StringBuilder();
                                CleanerRef cleanerRef = Cleaner.this.firstCleanable;
                                while (cleanerRef != null) {
                                    if (stringBuilder.length() != 0) {
                                        stringBuilder.append(", ");
                                    }
                                    stringBuilder.append(cleanerRef.cleanupTask.toString());
                                    cleanerRef = cleanerRef.next;
                                }
                                logger.log(Level.FINER, "Registered Cleaners: {0}", stringBuilder.toString());
                            }
                        }
                    }
                }
                catch (InterruptedException interruptedException) {
                    return;
                }
                catch (Exception exception) {
                    Logger.getLogger(Cleaner.class.getName()).log(Level.SEVERE, null, exception);
                    continue;
                }
                break;
            }
        }
    }

    public static interface Cleanable {
        public void clean();
    }

    static class CleanerRef
    extends PhantomReference<Object>
    implements Cleanable {
        private final Cleaner cleaner;
        private final Runnable cleanupTask;
        private CleanerRef previous;
        private CleanerRef next;

        public CleanerRef(Cleaner cleaner, Object object, ReferenceQueue<? super Object> referenceQueue, Runnable runnable2) {
            super(object, referenceQueue);
            this.cleaner = cleaner;
            this.cleanupTask = runnable2;
        }

        @Override
        public void clean() {
            if (this.cleaner.remove(this)) {
                this.cleanupTask.run();
            }
        }

        CleanerRef getPrevious() {
            return this.previous;
        }

        void setPrevious(CleanerRef cleanerRef) {
            this.previous = cleanerRef;
        }

        CleanerRef getNext() {
            return this.next;
        }

        void setNext(CleanerRef cleanerRef) {
            this.next = cleanerRef;
        }
    }
}

