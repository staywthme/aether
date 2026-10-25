/*
 * Decompiled with CFR 0.152.
 */
package okhttp3.internal.cache;

import java.io.Closeable;
import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.Flushable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import kotlin.KotlinNothingValueException;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.io.CloseableKt;
import kotlin.jvm.JvmField;
import kotlin.jvm.JvmOverloads;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlin.text.Regex;
import kotlin.text.StringsKt;
import okhttp3.internal.Util;
import okhttp3.internal.cache.DiskLruCache;
import okhttp3.internal.cache.FaultHidingSink;
import okhttp3.internal.concurrent.Task;
import okhttp3.internal.concurrent.TaskQueue;
import okhttp3.internal.concurrent.TaskRunner;
import okhttp3.internal.io.FileSystem;
import okhttp3.internal.platform.Platform;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Sink;
import okio.Source;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000y\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\b\u0011\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\u000f\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0010\n\u0002\u0010)\n\u0002\b\u0007*\u0001\u0014\u0018\u0000 [2\u00020\u00012\u00020\u0002:\u0004[\\]^B7\b\u0000\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\u0006\u0010\t\u001a\u00020\b\u0012\u0006\u0010\n\u001a\u00020\u000b\u0012\u0006\u0010\f\u001a\u00020\r\u00a2\u0006\u0002\u0010\u000eJ\b\u00108\u001a\u000209H\u0002J\b\u0010:\u001a\u000209H\u0016J!\u0010;\u001a\u0002092\n\u0010<\u001a\u00060=R\u00020\u00002\u0006\u0010>\u001a\u00020\u0010H\u0000\u00a2\u0006\u0002\b?J\u0006\u0010@\u001a\u000209J \u0010A\u001a\b\u0018\u00010=R\u00020\u00002\u0006\u0010B\u001a\u00020(2\b\b\u0002\u0010C\u001a\u00020\u000bH\u0007J\u0006\u0010D\u001a\u000209J\b\u0010E\u001a\u000209H\u0016J\u0017\u0010F\u001a\b\u0018\u00010GR\u00020\u00002\u0006\u0010B\u001a\u00020(H\u0086\u0002J\u0006\u0010H\u001a\u000209J\u0006\u0010I\u001a\u00020\u0010J\b\u0010J\u001a\u00020\u0010H\u0002J\b\u0010K\u001a\u00020%H\u0002J\b\u0010L\u001a\u000209H\u0002J\b\u0010M\u001a\u000209H\u0002J\u0010\u0010N\u001a\u0002092\u0006\u0010O\u001a\u00020(H\u0002J\r\u0010P\u001a\u000209H\u0000\u00a2\u0006\u0002\bQJ\u000e\u0010R\u001a\u00020\u00102\u0006\u0010B\u001a\u00020(J\u0019\u0010S\u001a\u00020\u00102\n\u0010T\u001a\u00060)R\u00020\u0000H\u0000\u00a2\u0006\u0002\bUJ\b\u0010V\u001a\u00020\u0010H\u0002J\u0006\u00105\u001a\u00020\u000bJ\u0010\u0010W\u001a\f\u0012\b\u0012\u00060GR\u00020\u00000XJ\u0006\u0010Y\u001a\u000209J\u0010\u0010Z\u001a\u0002092\u0006\u0010B\u001a\u00020(H\u0002R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0012X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0013\u001a\u00020\u0014X\u0082\u0004\u00a2\u0006\u0004\n\u0002\u0010\u0015R\u001a\u0010\u0016\u001a\u00020\u0010X\u0080\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0017\u0010\u0018\"\u0004\b\u0019\u0010\u001aR\u0011\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u001cR\u0014\u0010\u0003\u001a\u00020\u0004X\u0080\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u001eR\u000e\u0010\u001f\u001a\u00020\u0010X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010 \u001a\u00020\u0010X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010!\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\"\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010#\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010$\u001a\u0004\u0018\u00010%X\u0082\u000e\u00a2\u0006\u0002\n\u0000R$\u0010&\u001a\u0012\u0012\u0004\u0012\u00020(\u0012\b\u0012\u00060)R\u00020\u00000'X\u0080\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b*\u0010+R&\u0010\n\u001a\u00020\u000b2\u0006\u0010,\u001a\u00020\u000b8F@FX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b-\u0010.\"\u0004\b/\u00100R\u000e\u00101\u001a\u00020\u0010X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u00102\u001a\u00020\u0010X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u00103\u001a\u00020\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u00104\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u00105\u001a\u00020\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\t\u001a\u00020\bX\u0080\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b6\u00107\u00a8\u0006_"}, d2={"Lokhttp3/internal/cache/DiskLruCache;", "Ljava/io/Closeable;", "Ljava/io/Flushable;", "fileSystem", "Lokhttp3/internal/io/FileSystem;", "directory", "Ljava/io/File;", "appVersion", "", "valueCount", "maxSize", "", "taskRunner", "Lokhttp3/internal/concurrent/TaskRunner;", "(Lokhttp3/internal/io/FileSystem;Ljava/io/File;IIJLokhttp3/internal/concurrent/TaskRunner;)V", "civilizedFileSystem", "", "cleanupQueue", "Lokhttp3/internal/concurrent/TaskQueue;", "cleanupTask", "okhttp3/internal/cache/DiskLruCache$cleanupTask$1", "Lokhttp3/internal/cache/DiskLruCache$cleanupTask$1;", "closed", "getClosed$okhttp", "()Z", "setClosed$okhttp", "(Z)V", "getDirectory", "()Ljava/io/File;", "getFileSystem$okhttp", "()Lokhttp3/internal/io/FileSystem;", "hasJournalErrors", "initialized", "journalFile", "journalFileBackup", "journalFileTmp", "journalWriter", "Lokio/BufferedSink;", "lruEntries", "Ljava/util/LinkedHashMap;", "", "Lokhttp3/internal/cache/DiskLruCache$Entry;", "getLruEntries$okhttp", "()Ljava/util/LinkedHashMap;", "value", "getMaxSize", "()J", "setMaxSize", "(J)V", "mostRecentRebuildFailed", "mostRecentTrimFailed", "nextSequenceNumber", "redundantOpCount", "size", "getValueCount$okhttp", "()I", "checkNotClosed", "", "close", "completeEdit", "editor", "Lokhttp3/internal/cache/DiskLruCache$Editor;", "success", "completeEdit$okhttp", "delete", "edit", "key", "expectedSequenceNumber", "evictAll", "flush", "get", "Lokhttp3/internal/cache/DiskLruCache$Snapshot;", "initialize", "isClosed", "journalRebuildRequired", "newJournalWriter", "processJournal", "readJournal", "readJournalLine", "line", "rebuildJournal", "rebuildJournal$okhttp", "remove", "removeEntry", "entry", "removeEntry$okhttp", "removeOldestEntry", "snapshots", "", "trimToSize", "validateKey", "Companion", "Editor", "Entry", "Snapshot", "okhttp"})
@SourceDebugExtension(value={"SMAP\nDiskLruCache.kt\nKotlin\n*S Kotlin\n*F\n+ 1 DiskLruCache.kt\nokhttp3/internal/cache/DiskLruCache\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n+ 3 Util.kt\nokhttp3/internal/Util\n+ 4 ArraysJVM.kt\nkotlin/collections/ArraysKt__ArraysJVMKt\n*L\n1#1,1065:1\n1#2:1066\n608#3,4:1067\n37#4,2:1071\n37#4,2:1073\n*S KotlinDebug\n*F\n+ 1 DiskLruCache.kt\nokhttp3/internal/cache/DiskLruCache\n*L\n215#1:1067,4\n672#1:1071,2\n721#1:1073,2\n*E\n"})
public final class DiskLruCache
implements Closeable,
Flushable {
    @NotNull
    public static final Companion Companion = new Companion(null);
    @NotNull
    private final FileSystem fileSystem;
    @NotNull
    private final File directory;
    private final int appVersion;
    private final int valueCount;
    private long maxSize;
    @NotNull
    private final File journalFile;
    @NotNull
    private final File journalFileTmp;
    @NotNull
    private final File journalFileBackup;
    private long size;
    @Nullable
    private BufferedSink journalWriter;
    @NotNull
    private final LinkedHashMap<String, Entry> lruEntries;
    private int redundantOpCount;
    private boolean hasJournalErrors;
    private boolean civilizedFileSystem;
    private boolean initialized;
    private boolean closed;
    private boolean mostRecentTrimFailed;
    private boolean mostRecentRebuildFailed;
    private long nextSequenceNumber;
    @NotNull
    private final TaskQueue cleanupQueue;
    @NotNull
    private final cleanupTask.1 cleanupTask;
    @JvmField
    @NotNull
    public static final String JOURNAL_FILE = "journal";
    @JvmField
    @NotNull
    public static final String JOURNAL_FILE_TEMP = "journal.tmp";
    @JvmField
    @NotNull
    public static final String JOURNAL_FILE_BACKUP = "journal.bkp";
    @JvmField
    @NotNull
    public static final String MAGIC = "libcore.io.DiskLruCache";
    @JvmField
    @NotNull
    public static final String VERSION_1 = "1";
    @JvmField
    public static final long ANY_SEQUENCE_NUMBER = -1L;
    @JvmField
    @NotNull
    public static final Regex LEGAL_KEY_PATTERN = new Regex("[a-z0-9_-]{1,120}");
    @JvmField
    @NotNull
    public static final String CLEAN = "CLEAN";
    @JvmField
    @NotNull
    public static final String DIRTY = "DIRTY";
    @JvmField
    @NotNull
    public static final String REMOVE = "REMOVE";
    @JvmField
    @NotNull
    public static final String READ = "READ";

    public DiskLruCache(@NotNull FileSystem object, @NotNull File file, int n2, int n3, long l2, @NotNull TaskRunner taskRunner) {
        Intrinsics.checkNotNullParameter(object, "");
        Intrinsics.checkNotNullParameter(file, "");
        Intrinsics.checkNotNullParameter(taskRunner, "");
        this.fileSystem = object;
        this.directory = file;
        this.appVersion = n2;
        this.valueCount = n3;
        this.maxSize = l2;
        this.lruEntries = new LinkedHashMap(0, 0.75f, true);
        this.cleanupQueue = taskRunner.newQueue();
        object = Util.okHttpName + " Cache";
        this.cleanupTask = new Task(this, (String)object){
            final /* synthetic */ DiskLruCache this$0;
            {
                this.this$0 = diskLruCache;
                super(string, false, 2, null);
            }

            public final long runOnce() {
                DiskLruCache diskLruCache = this.this$0;
                DiskLruCache diskLruCache2 = this.this$0;
                synchronized (diskLruCache) {
                    block9: {
                        if (DiskLruCache.access$getInitialized$p(diskLruCache2) && !diskLruCache2.getClosed$okhttp()) break block9;
                        return -1L;
                    }
                    try {
                        diskLruCache2.trimToSize();
                    }
                    catch (IOException iOException) {
                        DiskLruCache.access$setMostRecentTrimFailed$p(diskLruCache2, true);
                    }
                    try {
                        if (DiskLruCache.access$journalRebuildRequired(diskLruCache2)) {
                            diskLruCache2.rebuildJournal$okhttp();
                            DiskLruCache.access$setRedundantOpCount$p(diskLruCache2, 0);
                        }
                    }
                    catch (IOException iOException) {
                        DiskLruCache.access$setMostRecentRebuildFailed$p(diskLruCache2, true);
                        DiskLruCache.access$setJournalWriter$p(diskLruCache2, Okio.buffer(Okio.blackhole()));
                    }
                    return -1L;
                }
            }
        };
        if (!(l2 > 0L)) {
            object = "maxSize <= 0";
            throw new IllegalArgumentException(object.toString());
        }
        if (!(this.valueCount > 0)) {
            object = "valueCount <= 0";
            throw new IllegalArgumentException(object.toString());
        }
        this.journalFile = new File(this.directory, JOURNAL_FILE);
        this.journalFileTmp = new File(this.directory, JOURNAL_FILE_TEMP);
        this.journalFileBackup = new File(this.directory, JOURNAL_FILE_BACKUP);
    }

    @NotNull
    public final FileSystem getFileSystem$okhttp() {
        return this.fileSystem;
    }

    @NotNull
    public final File getDirectory() {
        return this.directory;
    }

    public final int getValueCount$okhttp() {
        return this.valueCount;
    }

    public final synchronized long getMaxSize() {
        return this.maxSize;
    }

    public final synchronized void setMaxSize(long l2) {
        this.maxSize = l2;
        if (this.initialized) {
            TaskQueue.schedule$default(this.cleanupQueue, this.cleanupTask, 0L, 2, null);
        }
    }

    @NotNull
    public final LinkedHashMap<String, Entry> getLruEntries$okhttp() {
        return this.lruEntries;
    }

    public final boolean getClosed$okhttp() {
        return this.closed;
    }

    public final void setClosed$okhttp(boolean bl) {
        this.closed = bl;
    }

    public final synchronized void initialize() throws IOException {
        DiskLruCache diskLruCache = this;
        if (Util.assertionsEnabled && !Thread.holdsLock(diskLruCache)) {
            throw new AssertionError((Object)("Thread " + Thread.currentThread().getName() + " MUST hold lock on " + diskLruCache));
        }
        if (this.initialized) {
            return;
        }
        if (this.fileSystem.exists(this.journalFileBackup)) {
            if (this.fileSystem.exists(this.journalFile)) {
                this.fileSystem.delete(this.journalFileBackup);
            } else {
                this.fileSystem.rename(this.journalFileBackup, this.journalFile);
            }
        }
        this.civilizedFileSystem = Util.isCivilized(this.fileSystem, this.journalFileBackup);
        if (this.fileSystem.exists(this.journalFile)) {
            try {
                this.readJournal();
                this.processJournal();
                this.initialized = true;
                return;
            }
            catch (IOException iOException) {
                Platform.Companion.get().log("DiskLruCache " + this.directory + " is corrupt: " + iOException.getMessage() + ", removing", 5, iOException);
                try {
                    this.delete();
                }
                finally {
                    this.closed = false;
                }
            }
        }
        this.rebuildJournal$okhttp();
        this.initialized = true;
    }

    private final void readJournal() throws IOException {
        Closeable closeable = Okio.buffer(this.fileSystem.source(this.journalFile));
        Throwable throwable = null;
        try {
            BufferedSource bufferedSource = (BufferedSource)closeable;
            String string = bufferedSource.readUtf8LineStrict();
            String string2 = bufferedSource.readUtf8LineStrict();
            String string3 = bufferedSource.readUtf8LineStrict();
            String string4 = bufferedSource.readUtf8LineStrict();
            String string5 = bufferedSource.readUtf8LineStrict();
            if (!(Intrinsics.areEqual(MAGIC, string) && Intrinsics.areEqual(VERSION_1, string2) && Intrinsics.areEqual(String.valueOf(this.appVersion), string3) && Intrinsics.areEqual(String.valueOf(this.valueCount), string4) && !(((CharSequence)string5).length() > 0))) {
                throw new IOException("unexpected journal header: [" + string + ", " + string2 + ", " + string4 + ", " + string5 + ']');
            }
            int n2 = 0;
            try {
                while (true) {
                    this.readJournalLine(bufferedSource.readUtf8LineStrict());
                    ++n2;
                }
            }
            catch (EOFException eOFException) {
                this.redundantOpCount = n2 - this.lruEntries.size();
                if (!bufferedSource.exhausted()) {
                    this.rebuildJournal$okhttp();
                } else {
                    this.journalWriter = this.newJournalWriter();
                }
                CloseableKt.closeFinally(closeable, null);
                return;
            }
        }
        catch (Throwable throwable2) {
            try {
                Throwable throwable3 = throwable2;
                throwable = throwable2;
                throw throwable3;
            }
            catch (Throwable throwable4) {
                CloseableKt.closeFinally(closeable, throwable);
                throw throwable4;
            }
        }
    }

    private final BufferedSink newJournalWriter() throws FileNotFoundException {
        Sink sink2 = this.fileSystem.appendingSink(this.journalFile);
        sink2 = new FaultHidingSink(sink2, (Function1<? super IOException, Unit>)new Function1<IOException, Unit>(this){
            final /* synthetic */ DiskLruCache this$0;
            {
                this.this$0 = diskLruCache;
                super(1);
            }

            public final void invoke(@NotNull IOException object) {
                Intrinsics.checkNotNullParameter(object, "");
                object = this.this$0;
                if (Util.assertionsEnabled && !Thread.holdsLock(object)) {
                    throw new AssertionError((Object)("Thread " + Thread.currentThread().getName() + " MUST hold lock on " + object));
                }
                DiskLruCache.access$setHasJournalErrors$p(this.this$0, true);
            }
        });
        return Okio.buffer(sink2);
    }

    private final void readJournalLine(String object) throws IOException {
        Entry entry;
        String string;
        int n2 = StringsKt.indexOf$default((CharSequence)object, ' ', 0, false, 6, null);
        if (n2 == -1) {
            throw new IOException("unexpected journal line: " + (String)object);
        }
        int n3 = n2 + 1;
        int n4 = StringsKt.indexOf$default((CharSequence)object, ' ', n3, false, 4, null);
        if (n4 == -1) {
            String string2 = ((String)object).substring(n3);
            Intrinsics.checkNotNullExpressionValue(string2, "");
            string = string2;
            if (n2 == REMOVE.length() && StringsKt.startsWith$default((String)object, REMOVE, false, 2, null)) {
                this.lruEntries.remove(string);
                return;
            }
        } else {
            String string3 = ((String)object).substring(n3, n4);
            Intrinsics.checkNotNullExpressionValue(string3, "");
            string = string3;
        }
        if ((entry = this.lruEntries.get(string)) == null) {
            entry = new Entry(string);
            ((Map)this.lruEntries).put(string, entry);
        }
        if (n4 != -1 && n2 == CLEAN.length() && StringsKt.startsWith$default((String)object, CLEAN, false, 2, null)) {
            String string4 = ((String)object).substring(n4 + 1);
            Intrinsics.checkNotNullExpressionValue(string4, "");
            char[] cArray = new char[1];
            object = cArray;
            cArray[0] = 32;
            object = StringsKt.split$default((CharSequence)string4, (char[])object, false, 0, 6, null);
            entry.setReadable$okhttp(true);
            entry.setCurrentEditor$okhttp(null);
            entry.setLengths$okhttp((List<String>)object);
            return;
        }
        if (n4 == -1 && n2 == DIRTY.length() && StringsKt.startsWith$default((String)object, DIRTY, false, 2, null)) {
            entry.setCurrentEditor$okhttp(new Editor(entry));
            return;
        }
        if (n4 != -1 || n2 != READ.length() || !StringsKt.startsWith$default((String)object, READ, false, 2, null)) {
            throw new IOException("unexpected journal line: " + (String)object);
        }
    }

    private final void processJournal() throws IOException {
        this.fileSystem.delete(this.journalFileTmp);
        Iterator<Entry> iterator2 = this.lruEntries.values().iterator();
        while (iterator2.hasNext()) {
            int n2;
            int n3;
            Entry entry;
            Intrinsics.checkNotNullExpressionValue(iterator2.next(), "");
            if (entry.getCurrentEditor$okhttp() == null) {
                n3 = this.valueCount;
                for (n2 = 0; n2 < n3; ++n2) {
                    this.size += entry.getLengths$okhttp()[n2];
                }
                continue;
            }
            entry.setCurrentEditor$okhttp(null);
            n3 = this.valueCount;
            for (n2 = 0; n2 < n3; ++n2) {
                this.fileSystem.delete(entry.getCleanFiles$okhttp().get(n2));
                this.fileSystem.delete(entry.getDirtyFiles$okhttp().get(n2));
            }
            iterator2.remove();
        }
    }

    public final synchronized void rebuildJournal$okhttp() throws IOException {
        BufferedSink bufferedSink = this.journalWriter;
        if (bufferedSink != null) {
            bufferedSink.close();
        }
        Closeable closeable = Okio.buffer(this.fileSystem.sink(this.journalFileTmp));
        Throwable throwable = null;
        try {
            BufferedSink bufferedSink2 = (BufferedSink)closeable;
            bufferedSink2.writeUtf8(MAGIC).writeByte(10);
            bufferedSink2.writeUtf8(VERSION_1).writeByte(10);
            bufferedSink2.writeDecimalLong(this.appVersion).writeByte(10);
            bufferedSink2.writeDecimalLong(this.valueCount).writeByte(10);
            bufferedSink2.writeByte(10);
            for (Entry entry : this.lruEntries.values()) {
                if (entry.getCurrentEditor$okhttp() != null) {
                    bufferedSink2.writeUtf8(DIRTY).writeByte(32);
                    bufferedSink2.writeUtf8(entry.getKey$okhttp());
                    bufferedSink2.writeByte(10);
                    continue;
                }
                bufferedSink2.writeUtf8(CLEAN).writeByte(32);
                bufferedSink2.writeUtf8(entry.getKey$okhttp());
                entry.writeLengths$okhttp(bufferedSink2);
                bufferedSink2.writeByte(10);
            }
        }
        catch (Throwable throwable2) {
            try {
                Throwable throwable3 = throwable2;
                throwable = throwable2;
                throw throwable3;
            }
            catch (Throwable throwable4) {
                CloseableKt.closeFinally(closeable, throwable);
                throw throwable4;
            }
        }
        CloseableKt.closeFinally(closeable, null);
        if (this.fileSystem.exists(this.journalFile)) {
            this.fileSystem.rename(this.journalFile, this.journalFileBackup);
        }
        this.fileSystem.rename(this.journalFileTmp, this.journalFile);
        this.fileSystem.delete(this.journalFileBackup);
        this.journalWriter = this.newJournalWriter();
        this.hasJournalErrors = false;
        this.mostRecentRebuildFailed = false;
    }

    @Nullable
    public final synchronized Snapshot get(@NotNull String string) throws IOException {
        Intrinsics.checkNotNullParameter(string, "");
        this.initialize();
        this.checkNotClosed();
        this.validateKey(string);
        Entry entry = this.lruEntries.get(string);
        if (entry == null) {
            return null;
        }
        Object object = entry;
        Snapshot snapshot = entry.snapshot$okhttp();
        if (snapshot == null) {
            return null;
        }
        object = snapshot;
        int n2 = this.redundantOpCount;
        this.redundantOpCount = n2 + 1;
        BufferedSink bufferedSink = this.journalWriter;
        Intrinsics.checkNotNull(bufferedSink);
        bufferedSink.writeUtf8(READ).writeByte(32).writeUtf8(string).writeByte(10);
        if (this.journalRebuildRequired()) {
            TaskQueue.schedule$default(this.cleanupQueue, this.cleanupTask, 0L, 2, null);
        }
        return object;
    }

    @JvmOverloads
    @Nullable
    public final synchronized Editor edit(@NotNull String object, long l2) throws IOException {
        Intrinsics.checkNotNullParameter(object, "");
        this.initialize();
        this.checkNotClosed();
        this.validateKey((String)object);
        Entry entry = this.lruEntries.get(object);
        if (l2 != ANY_SEQUENCE_NUMBER && (entry == null || entry.getSequenceNumber$okhttp() != l2)) {
            return null;
        }
        Entry entry2 = entry;
        if ((entry2 != null ? entry2.getCurrentEditor$okhttp() : null) != null) {
            return null;
        }
        if (entry != null && entry.getLockingSourceCount$okhttp() != 0) {
            return null;
        }
        if (this.mostRecentTrimFailed || this.mostRecentRebuildFailed) {
            TaskQueue.schedule$default(this.cleanupQueue, this.cleanupTask, 0L, 2, null);
            return null;
        }
        BufferedSink bufferedSink = this.journalWriter;
        Intrinsics.checkNotNull(bufferedSink);
        BufferedSink bufferedSink2 = bufferedSink;
        bufferedSink.writeUtf8(DIRTY).writeByte(32).writeUtf8((String)object).writeByte(10);
        bufferedSink2.flush();
        if (this.hasJournalErrors) {
            return null;
        }
        if (entry == null) {
            entry = new Entry((String)object);
            ((Map)this.lruEntries).put(object, entry);
        }
        object = new Editor(entry);
        entry.setCurrentEditor$okhttp((Editor)object);
        return object;
    }

    public static /* synthetic */ Editor edit$default(DiskLruCache diskLruCache, String string, long l2, int n2, Object object) throws IOException {
        if ((n2 & 2) != 0) {
            l2 = ANY_SEQUENCE_NUMBER;
        }
        return diskLruCache.edit(string, l2);
    }

    public final synchronized long size() throws IOException {
        this.initialize();
        return this.size;
    }

    public final synchronized void completeEdit$okhttp(@NotNull Editor object, boolean bl) throws IOException {
        int n2;
        int n3;
        Intrinsics.checkNotNullParameter(object, "");
        Entry entry = ((Editor)object).getEntry$okhttp();
        if (!Intrinsics.areEqual(entry.getCurrentEditor$okhttp(), object)) {
            String string = "Check failed.";
            throw new IllegalStateException(string.toString());
        }
        if (bl && !entry.getReadable$okhttp()) {
            n3 = this.valueCount;
            for (n2 = 0; n2 < n3; ++n2) {
                boolean[] blArray = ((Editor)object).getWritten$okhttp();
                Intrinsics.checkNotNull(blArray);
                if (!blArray[n2]) {
                    ((Editor)object).abort();
                    throw new IllegalStateException("Newly created entry didn't create value for index " + n2);
                }
                if (this.fileSystem.exists(entry.getDirtyFiles$okhttp().get(n2))) continue;
                ((Editor)object).abort();
                return;
            }
        }
        n3 = this.valueCount;
        for (n2 = 0; n2 < n3; ++n2) {
            object = entry.getDirtyFiles$okhttp().get(n2);
            if (bl && !entry.getZombie$okhttp()) {
                long l2;
                if (!this.fileSystem.exists((File)object)) continue;
                File file = entry.getCleanFiles$okhttp().get(n2);
                this.fileSystem.rename((File)object, file);
                long l3 = entry.getLengths$okhttp()[n2];
                entry.getLengths$okhttp()[n2] = l2 = this.fileSystem.size(file);
                this.size = this.size - l3 + l2;
                continue;
            }
            this.fileSystem.delete((File)object);
        }
        entry.setCurrentEditor$okhttp(null);
        if (entry.getZombie$okhttp()) {
            this.removeEntry$okhttp(entry);
            return;
        }
        n2 = this.redundantOpCount;
        this.redundantOpCount = n2 + 1;
        BufferedSink bufferedSink = this.journalWriter;
        Intrinsics.checkNotNull(bufferedSink);
        BufferedSink bufferedSink2 = bufferedSink;
        BufferedSink bufferedSink3 = bufferedSink;
        if (entry.getReadable$okhttp() || bl) {
            entry.setReadable$okhttp(true);
            bufferedSink3.writeUtf8(CLEAN).writeByte(32);
            bufferedSink3.writeUtf8(entry.getKey$okhttp());
            entry.writeLengths$okhttp(bufferedSink3);
            bufferedSink3.writeByte(10);
            if (bl) {
                long l4 = this.nextSequenceNumber;
                this.nextSequenceNumber = l4 + 1L;
                entry.setSequenceNumber$okhttp(l4);
            }
        } else {
            this.lruEntries.remove(entry.getKey$okhttp());
            bufferedSink3.writeUtf8(REMOVE).writeByte(32);
            bufferedSink3.writeUtf8(entry.getKey$okhttp());
            bufferedSink3.writeByte(10);
        }
        bufferedSink3.flush();
        if (this.size > this.maxSize || this.journalRebuildRequired()) {
            TaskQueue.schedule$default(this.cleanupQueue, this.cleanupTask, 0L, 2, null);
        }
    }

    private final boolean journalRebuildRequired() {
        return this.redundantOpCount >= 2000 && this.redundantOpCount >= this.lruEntries.size();
    }

    public final synchronized boolean remove(@NotNull String object) throws IOException {
        Intrinsics.checkNotNullParameter(object, "");
        this.initialize();
        this.checkNotClosed();
        this.validateKey((String)object);
        Entry entry = this.lruEntries.get(object);
        if (entry == null) {
            return false;
        }
        object = entry;
        boolean bl = this.removeEntry$okhttp((Entry)object);
        if (bl && this.size <= this.maxSize) {
            this.mostRecentTrimFailed = false;
        }
        return bl;
    }

    public final boolean removeEntry$okhttp(@NotNull Entry entry) throws IOException {
        int n2;
        Intrinsics.checkNotNullParameter(entry, "");
        if (!this.civilizedFileSystem) {
            if (entry.getLockingSourceCount$okhttp() > 0) {
                BufferedSink bufferedSink = this.journalWriter;
                if (bufferedSink != null) {
                    BufferedSink bufferedSink2 = bufferedSink;
                    bufferedSink.writeUtf8(DIRTY);
                    bufferedSink2.writeByte(32);
                    bufferedSink2.writeUtf8(entry.getKey$okhttp());
                    bufferedSink2.writeByte(10);
                    bufferedSink2.flush();
                }
            }
            if (entry.getLockingSourceCount$okhttp() > 0 || entry.getCurrentEditor$okhttp() != null) {
                entry.setZombie$okhttp(true);
                return true;
            }
        }
        Editor editor = entry.getCurrentEditor$okhttp();
        if (editor != null) {
            editor.detach$okhttp();
        }
        int n3 = this.valueCount;
        for (n2 = 0; n2 < n3; ++n2) {
            this.fileSystem.delete(entry.getCleanFiles$okhttp().get(n2));
            this.size -= entry.getLengths$okhttp()[n2];
            entry.getLengths$okhttp()[n2] = 0L;
        }
        n2 = this.redundantOpCount;
        this.redundantOpCount = n2 + 1;
        BufferedSink bufferedSink = this.journalWriter;
        if (bufferedSink != null) {
            BufferedSink bufferedSink3 = bufferedSink;
            bufferedSink.writeUtf8(REMOVE);
            bufferedSink3.writeByte(32);
            bufferedSink3.writeUtf8(entry.getKey$okhttp());
            bufferedSink3.writeByte(10);
        }
        this.lruEntries.remove(entry.getKey$okhttp());
        if (this.journalRebuildRequired()) {
            TaskQueue.schedule$default(this.cleanupQueue, this.cleanupTask, 0L, 2, null);
        }
        return true;
    }

    private final synchronized void checkNotClosed() {
        if (!(!this.closed)) {
            String string = "cache is closed";
            throw new IllegalStateException(string.toString());
        }
    }

    @Override
    public final synchronized void flush() throws IOException {
        if (!this.initialized) {
            return;
        }
        this.checkNotClosed();
        this.trimToSize();
        BufferedSink bufferedSink = this.journalWriter;
        Intrinsics.checkNotNull(bufferedSink);
        bufferedSink.flush();
    }

    public final synchronized boolean isClosed() {
        return this.closed;
    }

    @Override
    public final synchronized void close() throws IOException {
        if (!this.initialized || this.closed) {
            this.closed = true;
            return;
        }
        Collection<Entry> collection = this.lruEntries.values();
        Intrinsics.checkNotNullExpressionValue(collection, "");
        Collection<Entry> collection2 = collection;
        Collection<Entry> object2 = collection;
        for (Entry entry : collection.toArray(new Entry[0])) {
            if (entry.getCurrentEditor$okhttp() == null) continue;
            Editor editor = entry.getCurrentEditor$okhttp();
            if (editor == null) continue;
            editor.detach$okhttp();
        }
        this.trimToSize();
        BufferedSink bufferedSink = this.journalWriter;
        Intrinsics.checkNotNull(bufferedSink);
        bufferedSink.close();
        this.journalWriter = null;
        this.closed = true;
    }

    public final void trimToSize() throws IOException {
        while (this.size > this.maxSize) {
            if (this.removeOldestEntry()) continue;
            return;
        }
        this.mostRecentTrimFailed = false;
    }

    private final boolean removeOldestEntry() {
        for (Entry entry : this.lruEntries.values()) {
            if (entry.getZombie$okhttp()) continue;
            Intrinsics.checkNotNullExpressionValue(entry, "");
            this.removeEntry$okhttp(entry);
            return true;
        }
        return false;
    }

    public final void delete() throws IOException {
        this.close();
        this.fileSystem.deleteContents(this.directory);
    }

    public final synchronized void evictAll() throws IOException {
        this.initialize();
        Collection<Entry> collection = this.lruEntries.values();
        Intrinsics.checkNotNullExpressionValue(collection, "");
        Collection<Entry> collection2 = collection;
        Collection<Entry> object2 = collection;
        for (Entry entry : collection.toArray(new Entry[0])) {
            Intrinsics.checkNotNullExpressionValue(entry, "");
            this.removeEntry$okhttp(entry);
        }
        this.mostRecentTrimFailed = false;
    }

    private final void validateKey(String string) {
        if (!LEGAL_KEY_PATTERN.matches(string)) {
            string = "keys must match regex [a-z0-9_-]{1,120}: \"" + string + '\"';
            throw new IllegalArgumentException(string.toString());
        }
    }

    @NotNull
    public final synchronized Iterator<Snapshot> snapshots() throws IOException {
        this.initialize();
        return new Iterator<Snapshot>(this){
            @NotNull
            private final Iterator<Entry> delegate;
            @Nullable
            private Snapshot nextSnapshot;
            @Nullable
            private Snapshot removeSnapshot;
            final /* synthetic */ DiskLruCache this$0;
            {
                this.this$0 = diskLruCache;
                Iterator<Entry> iterator2 = new ArrayList<Entry>(diskLruCache.getLruEntries$okhttp().values()).iterator();
                Intrinsics.checkNotNullExpressionValue(iterator2, "");
                this.delegate = iterator2;
            }

            public final boolean hasNext() {
                if (this.nextSnapshot != null) {
                    return true;
                }
                DiskLruCache diskLruCache = this.this$0;
                DiskLruCache diskLruCache2 = this.this$0;
                synchronized (diskLruCache) {
                    block7: {
                        if (!diskLruCache2.getClosed$okhttp()) break block7;
                        return false;
                    }
                    while (this.delegate.hasNext()) {
                        Object object = this.delegate.next();
                        if (object == null || (object = ((Entry)object).snapshot$okhttp()) == null) continue;
                        this.nextSnapshot = object;
                        return true;
                    }
                }
                return false;
            }

            @NotNull
            public final Snapshot next() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                this.removeSnapshot = this.nextSnapshot;
                this.nextSnapshot = null;
                Snapshot snapshot = this.removeSnapshot;
                Intrinsics.checkNotNull(snapshot);
                return snapshot;
            }

            public final void remove() {
                Object object = this.removeSnapshot;
                if (object == null) {
                    object = "remove() before next()";
                    throw new IllegalStateException(object.toString());
                }
                try {
                    this.this$0.remove(((Snapshot)object).key());
                    return;
                }
                catch (IOException iOException) {
                    return;
                }
                finally {
                    this.removeSnapshot = null;
                }
            }
        };
    }

    @JvmOverloads
    @Nullable
    public final Editor edit(@NotNull String string) throws IOException {
        Intrinsics.checkNotNullParameter(string, "");
        return DiskLruCache.edit$default(this, string, 0L, 2, null);
    }

    public static final /* synthetic */ void access$setHasJournalErrors$p(DiskLruCache diskLruCache, boolean bl) {
        diskLruCache.hasJournalErrors = bl;
    }

    public static final /* synthetic */ boolean access$getInitialized$p(DiskLruCache diskLruCache) {
        return diskLruCache.initialized;
    }

    public static final /* synthetic */ void access$setMostRecentTrimFailed$p(DiskLruCache diskLruCache, boolean bl) {
        diskLruCache.mostRecentTrimFailed = bl;
    }

    public static final /* synthetic */ boolean access$journalRebuildRequired(DiskLruCache diskLruCache) {
        return diskLruCache.journalRebuildRequired();
    }

    public static final /* synthetic */ void access$setRedundantOpCount$p(DiskLruCache diskLruCache, int n2) {
        diskLruCache.redundantOpCount = n2;
    }

    public static final /* synthetic */ void access$setMostRecentRebuildFailed$p(DiskLruCache diskLruCache, boolean bl) {
        diskLruCache.mostRecentRebuildFailed = bl;
    }

    public static final /* synthetic */ void access$setJournalWriter$p(DiskLruCache diskLruCache, BufferedSink bufferedSink) {
        diskLruCache.journalWriter = bufferedSink;
    }

    @Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u0010\u0010\u0003\u001a\u00020\u00048\u0006X\u0087D\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u00020\u00068\u0006X\u0087D\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0007\u001a\u00020\u00068\u0006X\u0087D\u00a2\u0006\u0002\n\u0000R\u0010\u0010\b\u001a\u00020\u00068\u0006X\u0087D\u00a2\u0006\u0002\n\u0000R\u0010\u0010\t\u001a\u00020\u00068\u0006X\u0087D\u00a2\u0006\u0002\n\u0000R\u0010\u0010\n\u001a\u00020\u00068\u0006X\u0087D\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u000b\u001a\u00020\f8\u0006X\u0087\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\r\u001a\u00020\u00068\u0006X\u0087D\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u000e\u001a\u00020\u00068\u0006X\u0087D\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u000f\u001a\u00020\u00068\u0006X\u0087D\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0010\u001a\u00020\u00068\u0006X\u0087D\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0011"}, d2={"Lokhttp3/internal/cache/DiskLruCache$Companion;", "", "()V", "ANY_SEQUENCE_NUMBER", "", "CLEAN", "", "DIRTY", "JOURNAL_FILE", "JOURNAL_FILE_BACKUP", "JOURNAL_FILE_TEMP", "LEGAL_KEY_PATTERN", "Lkotlin/text/Regex;", "MAGIC", "READ", "REMOVE", "VERSION_1", "okhttp"})
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    @Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000@\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u0018\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0086\u0004\u0018\u00002\u00020\u0001B\u0013\b\u0000\u0012\n\u0010\u0002\u001a\u00060\u0003R\u00020\u0004\u00a2\u0006\u0002\u0010\u0005J\u0006\u0010\u000e\u001a\u00020\u000fJ\u0006\u0010\u0010\u001a\u00020\u000fJ\r\u0010\u0011\u001a\u00020\u000fH\u0000\u00a2\u0006\u0002\b\u0012J\u000e\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u0016J\u0010\u0010\u0017\u001a\u0004\u0018\u00010\u00182\u0006\u0010\u0015\u001a\u00020\u0016R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0018\u0010\u0002\u001a\u00060\u0003R\u00020\u0004X\u0080\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0016\u0010\n\u001a\u0004\u0018\u00010\u000bX\u0080\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\r\u00a8\u0006\u0019"}, d2={"Lokhttp3/internal/cache/DiskLruCache$Editor;", "", "entry", "Lokhttp3/internal/cache/DiskLruCache$Entry;", "Lokhttp3/internal/cache/DiskLruCache;", "(Lokhttp3/internal/cache/DiskLruCache;Lokhttp3/internal/cache/DiskLruCache$Entry;)V", "done", "", "getEntry$okhttp", "()Lokhttp3/internal/cache/DiskLruCache$Entry;", "written", "", "getWritten$okhttp", "()[Z", "abort", "", "commit", "detach", "detach$okhttp", "newSink", "Lokio/Sink;", "index", "", "newSource", "Lokio/Source;", "okhttp"})
    public final class Editor {
        @NotNull
        private final Entry entry;
        @Nullable
        private final boolean[] written;
        private boolean done;

        public Editor(Entry entry) {
            Intrinsics.checkNotNullParameter(entry, "");
            this.entry = entry;
            this.written = this.entry.getReadable$okhttp() ? null : new boolean[DiskLruCache.this.getValueCount$okhttp()];
        }

        @NotNull
        public final Entry getEntry$okhttp() {
            return this.entry;
        }

        @Nullable
        public final boolean[] getWritten$okhttp() {
            return this.written;
        }

        public final void detach$okhttp() {
            if (Intrinsics.areEqual(this.entry.getCurrentEditor$okhttp(), this)) {
                if (DiskLruCache.this.civilizedFileSystem) {
                    DiskLruCache.this.completeEdit$okhttp(this, false);
                    return;
                }
                this.entry.setZombie$okhttp(true);
            }
        }

        @Nullable
        public final Source newSource(int n2) {
            DiskLruCache diskLruCache = DiskLruCache.this;
            DiskLruCache diskLruCache2 = DiskLruCache.this;
            synchronized (diskLruCache) {
                Source source2;
                block7: {
                    if (!(!this.done)) {
                        String string = "Check failed.";
                        throw new IllegalStateException(string.toString());
                    }
                    if (this.entry.getReadable$okhttp() && Intrinsics.areEqual(this.entry.getCurrentEditor$okhttp(), this) && !this.entry.getZombie$okhttp()) break block7;
                    return null;
                }
                try {
                    source2 = diskLruCache2.getFileSystem$okhttp().source(this.entry.getCleanFiles$okhttp().get(n2));
                }
                catch (FileNotFoundException fileNotFoundException) {
                    source2 = null;
                }
                return source2;
            }
        }

        @NotNull
        public final Sink newSink(int n2) {
            DiskLruCache diskLruCache = DiskLruCache.this;
            DiskLruCache diskLruCache2 = DiskLruCache.this;
            synchronized (diskLruCache) {
                block9: {
                    if (!(!this.done)) {
                        String string = "Check failed.";
                        throw new IllegalStateException(string.toString());
                    }
                    if (Intrinsics.areEqual(this.entry.getCurrentEditor$okhttp(), this)) break block9;
                    Sink sink2 = Okio.blackhole();
                    return sink2;
                }
                if (!this.entry.getReadable$okhttp()) {
                    Intrinsics.checkNotNull(this.written);
                    this.written[n2] = true;
                }
                Object object = this.entry.getDirtyFiles$okhttp().get(n2);
                try {
                    object = diskLruCache2.getFileSystem$okhttp().sink((File)object);
                }
                catch (FileNotFoundException fileNotFoundException) {
                    object = Okio.blackhole();
                    return object;
                }
                object = new FaultHidingSink((Sink)object, (Function1<? super IOException, Unit>)new Function1<IOException, Unit>(diskLruCache2, this){
                    final /* synthetic */ DiskLruCache this$0;
                    final /* synthetic */ Editor this$1;
                    {
                        this.this$0 = diskLruCache;
                        this.this$1 = editor;
                        super(1);
                    }

                    public final void invoke(@NotNull IOException object) {
                        Intrinsics.checkNotNullParameter(object, "");
                        object = this.this$0;
                        Editor editor = this.this$1;
                        synchronized (object) {
                            editor.detach$okhttp();
                            return;
                        }
                    }
                });
                return object;
            }
        }

        public final void commit() throws IOException {
            DiskLruCache diskLruCache = DiskLruCache.this;
            Object object = DiskLruCache.this;
            synchronized (diskLruCache) {
                if (!(!this.done)) {
                    object = "Check failed.";
                    throw new IllegalStateException(object.toString());
                }
                if (Intrinsics.areEqual(this.entry.getCurrentEditor$okhttp(), this)) {
                    ((DiskLruCache)object).completeEdit$okhttp(this, true);
                }
                this.done = true;
                return;
            }
        }

        public final void abort() throws IOException {
            DiskLruCache diskLruCache = DiskLruCache.this;
            Object object = DiskLruCache.this;
            synchronized (diskLruCache) {
                if (!(!this.done)) {
                    object = "Check failed.";
                    throw new IllegalStateException(object.toString());
                }
                if (Intrinsics.areEqual(this.entry.getCurrentEditor$okhttp(), this)) {
                    ((DiskLruCache)object).completeEdit$okhttp(this, false);
                }
                this.done = true;
                return;
            }
        }
    }

    @Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000v\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u0016\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\t\n\u0002\b\b\n\u0002\u0010\u0001\n\u0000\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0080\u0004\u0018\u00002\u00020\u0001B\u000f\b\u0000\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0016\u0010.\u001a\u00020/2\f\u00100\u001a\b\u0012\u0004\u0012\u00020\u000301H\u0002J\u0010\u00102\u001a\u0002032\u0006\u00104\u001a\u00020\u001aH\u0002J\u001b\u00105\u001a\u0002062\f\u00100\u001a\b\u0012\u0004\u0012\u00020\u000301H\u0000\u00a2\u0006\u0002\b7J\u0013\u00108\u001a\b\u0018\u000109R\u00020\fH\u0000\u00a2\u0006\u0002\b:J\u0015\u0010;\u001a\u0002062\u0006\u0010<\u001a\u00020=H\u0000\u00a2\u0006\u0002\b>R\u001a\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0080\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR \u0010\n\u001a\b\u0018\u00010\u000bR\u00020\fX\u0080\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\u000e\"\u0004\b\u000f\u0010\u0010R\u001a\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0080\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\tR\u0014\u0010\u0002\u001a\u00020\u0003X\u0080\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u0014\u0010\u0015\u001a\u00020\u0016X\u0080\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0018R\u001a\u0010\u0019\u001a\u00020\u001aX\u0080\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001b\u0010\u001c\"\u0004\b\u001d\u0010\u001eR\u001a\u0010\u001f\u001a\u00020 X\u0080\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b!\u0010\"\"\u0004\b#\u0010$R\u001a\u0010%\u001a\u00020&X\u0080\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b'\u0010(\"\u0004\b)\u0010*R\u001a\u0010+\u001a\u00020 X\u0080\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b,\u0010\"\"\u0004\b-\u0010$\u00a8\u0006?"}, d2={"Lokhttp3/internal/cache/DiskLruCache$Entry;", "", "key", "", "(Lokhttp3/internal/cache/DiskLruCache;Ljava/lang/String;)V", "cleanFiles", "", "Ljava/io/File;", "getCleanFiles$okhttp", "()Ljava/util/List;", "currentEditor", "Lokhttp3/internal/cache/DiskLruCache$Editor;", "Lokhttp3/internal/cache/DiskLruCache;", "getCurrentEditor$okhttp", "()Lokhttp3/internal/cache/DiskLruCache$Editor;", "setCurrentEditor$okhttp", "(Lokhttp3/internal/cache/DiskLruCache$Editor;)V", "dirtyFiles", "getDirtyFiles$okhttp", "getKey$okhttp", "()Ljava/lang/String;", "lengths", "", "getLengths$okhttp", "()[J", "lockingSourceCount", "", "getLockingSourceCount$okhttp", "()I", "setLockingSourceCount$okhttp", "(I)V", "readable", "", "getReadable$okhttp", "()Z", "setReadable$okhttp", "(Z)V", "sequenceNumber", "", "getSequenceNumber$okhttp", "()J", "setSequenceNumber$okhttp", "(J)V", "zombie", "getZombie$okhttp", "setZombie$okhttp", "invalidLengths", "", "strings", "", "newSource", "Lokio/Source;", "index", "setLengths", "", "setLengths$okhttp", "snapshot", "Lokhttp3/internal/cache/DiskLruCache$Snapshot;", "snapshot$okhttp", "writeLengths", "writer", "Lokio/BufferedSink;", "writeLengths$okhttp", "okhttp"})
    @SourceDebugExtension(value={"SMAP\nDiskLruCache.kt\nKotlin\n*S Kotlin\n*F\n+ 1 DiskLruCache.kt\nokhttp3/internal/cache/DiskLruCache$Entry\n+ 2 Util.kt\nokhttp3/internal/Util\n*L\n1#1,1065:1\n608#2,4:1066\n*S KotlinDebug\n*F\n+ 1 DiskLruCache.kt\nokhttp3/internal/cache/DiskLruCache$Entry\n*L\n1001#1:1066,4\n*E\n"})
    public final class Entry {
        @NotNull
        private final String key;
        @NotNull
        private final long[] lengths;
        @NotNull
        private final List<File> cleanFiles;
        @NotNull
        private final List<File> dirtyFiles;
        private boolean readable;
        private boolean zombie;
        @Nullable
        private Editor currentEditor;
        private int lockingSourceCount;
        private long sequenceNumber;

        public Entry(String string) {
            Intrinsics.checkNotNullParameter(string, "");
            this.key = string;
            this.lengths = new long[DiskLruCache.this.getValueCount$okhttp()];
            this.cleanFiles = new ArrayList();
            this.dirtyFiles = new ArrayList();
            DiskLruCache.this = new StringBuilder(this.key).append('.');
            int n2 = ((StringBuilder)DiskLruCache.this).length();
            int n3 = DiskLruCache.this.getValueCount$okhttp();
            for (int i2 = 0; i2 < n3; ++i2) {
                ((StringBuilder)DiskLruCache.this).append(i2);
                ((Collection)this.cleanFiles).add(new File(DiskLruCache.this.getDirectory(), ((StringBuilder)DiskLruCache.this).toString()));
                ((StringBuilder)DiskLruCache.this).append(".tmp");
                ((Collection)this.dirtyFiles).add(new File(DiskLruCache.this.getDirectory(), ((StringBuilder)DiskLruCache.this).toString()));
                ((StringBuilder)DiskLruCache.this).setLength(n2);
            }
        }

        @NotNull
        public final String getKey$okhttp() {
            return this.key;
        }

        @NotNull
        public final long[] getLengths$okhttp() {
            return this.lengths;
        }

        @NotNull
        public final List<File> getCleanFiles$okhttp() {
            return this.cleanFiles;
        }

        @NotNull
        public final List<File> getDirtyFiles$okhttp() {
            return this.dirtyFiles;
        }

        public final boolean getReadable$okhttp() {
            return this.readable;
        }

        public final void setReadable$okhttp(boolean bl) {
            this.readable = bl;
        }

        public final boolean getZombie$okhttp() {
            return this.zombie;
        }

        public final void setZombie$okhttp(boolean bl) {
            this.zombie = bl;
        }

        @Nullable
        public final Editor getCurrentEditor$okhttp() {
            return this.currentEditor;
        }

        public final void setCurrentEditor$okhttp(@Nullable Editor editor) {
            this.currentEditor = editor;
        }

        public final int getLockingSourceCount$okhttp() {
            return this.lockingSourceCount;
        }

        public final void setLockingSourceCount$okhttp(int n2) {
            this.lockingSourceCount = n2;
        }

        public final long getSequenceNumber$okhttp() {
            return this.sequenceNumber;
        }

        public final void setSequenceNumber$okhttp(long l2) {
            this.sequenceNumber = l2;
        }

        public final void setLengths$okhttp(@NotNull List<String> list2) throws IOException {
            Intrinsics.checkNotNullParameter(list2, "");
            if (list2.size() != DiskLruCache.this.getValueCount$okhttp()) {
                this.invalidLengths(list2);
                throw new KotlinNothingValueException();
            }
            try {
                int n2 = list2.size();
                for (int i2 = 0; i2 < n2; ++i2) {
                    this.lengths[i2] = Long.parseLong(list2.get(i2));
                }
            }
            catch (NumberFormatException numberFormatException) {
                this.invalidLengths(list2);
                throw new KotlinNothingValueException();
            }
        }

        public final void writeLengths$okhttp(@NotNull BufferedSink bufferedSink) throws IOException {
            Intrinsics.checkNotNullParameter(bufferedSink, "");
            for (long l2 : this.lengths) {
                bufferedSink.writeByte(32).writeDecimalLong(l2);
            }
        }

        private final Void invalidLengths(List<String> list2) throws IOException {
            throw new IOException("unexpected journal line: " + list2);
        }

        @Nullable
        public final Snapshot snapshot$okhttp() {
            Object object = DiskLruCache.this;
            if (Util.assertionsEnabled && !Thread.holdsLock(object)) {
                throw new AssertionError((Object)("Thread " + Thread.currentThread().getName() + " MUST hold lock on " + object));
            }
            if (!this.readable) {
                return null;
            }
            if (!DiskLruCache.this.civilizedFileSystem && (this.currentEditor != null || this.zombie)) {
                return null;
            }
            object = new ArrayList();
            long[] lArray = (long[])this.lengths.clone();
            try {
                int n2 = DiskLruCache.this.getValueCount$okhttp();
                for (int i2 = 0; i2 < n2; ++i2) {
                    ((Collection)object).add(this.newSource(i2));
                }
                return new Snapshot(this.key, this.sequenceNumber, (List<? extends Source>)object, lArray);
            }
            catch (FileNotFoundException fileNotFoundException) {
                Iterator iterator2 = object.iterator();
                while (iterator2.hasNext()) {
                    object = (Source)iterator2.next();
                    Util.closeQuietly((Closeable)object);
                }
                try {
                    DiskLruCache.this.removeEntry$okhttp(this);
                }
                catch (IOException iOException) {}
                return null;
            }
        }

        private final Source newSource(int n2) {
            Source source2 = DiskLruCache.this.getFileSystem$okhttp().source(this.cleanFiles.get(n2));
            if (DiskLruCache.this.civilizedFileSystem) {
                return source2;
            }
            int n3 = this.lockingSourceCount;
            this.lockingSourceCount = n3 + 1;
            return new ForwardingSource(source2, DiskLruCache.this, this){
                private boolean closed;
                final /* synthetic */ DiskLruCache this$0;
                final /* synthetic */ Entry this$1;
                {
                    this.this$0 = diskLruCache;
                    this.this$1 = entry;
                    super(source2);
                }

                public final void close() {
                    super.close();
                    if (!this.closed) {
                        this.closed = true;
                        DiskLruCache diskLruCache = this.this$0;
                        Entry entry = this.this$1;
                        DiskLruCache diskLruCache2 = this.this$0;
                        synchronized (diskLruCache) {
                            int n2 = entry.getLockingSourceCount$okhttp();
                            entry.setLockingSourceCount$okhttp(n2 + -1);
                            if (entry.getLockingSourceCount$okhttp() == 0 && entry.getZombie$okhttp()) {
                                diskLruCache2.removeEntry$okhttp(entry);
                            }
                            return;
                        }
                    }
                }
            };
        }
    }

    @Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000B\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0016\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\u0004\u0018\u00002\u00020\u0001B-\b\u0000\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007\u0012\u0006\u0010\t\u001a\u00020\n\u00a2\u0006\u0002\u0010\u000bJ\b\u0010\f\u001a\u00020\rH\u0016J\f\u0010\u000e\u001a\b\u0018\u00010\u000fR\u00020\u0010J\u000e\u0010\u0011\u001a\u00020\u00052\u0006\u0010\u0012\u001a\u00020\u0013J\u000e\u0010\u0014\u001a\u00020\b2\u0006\u0010\u0012\u001a\u00020\u0013J\u0006\u0010\u0002\u001a\u00020\u0003R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0015"}, d2={"Lokhttp3/internal/cache/DiskLruCache$Snapshot;", "Ljava/io/Closeable;", "key", "", "sequenceNumber", "", "sources", "", "Lokio/Source;", "lengths", "", "(Lokhttp3/internal/cache/DiskLruCache;Ljava/lang/String;JLjava/util/List;[J)V", "close", "", "edit", "Lokhttp3/internal/cache/DiskLruCache$Editor;", "Lokhttp3/internal/cache/DiskLruCache;", "getLength", "index", "", "getSource", "okhttp"})
    public final class Snapshot
    implements Closeable {
        @NotNull
        private final String key;
        private final long sequenceNumber;
        @NotNull
        private final List<Source> sources;
        @NotNull
        private final long[] lengths;

        public Snapshot(String string, @NotNull long l2, @NotNull List<? extends Source> list2, long[] lArray) {
            Intrinsics.checkNotNullParameter(string, "");
            Intrinsics.checkNotNullParameter(list2, "");
            Intrinsics.checkNotNullParameter(lArray, "");
            this.key = string;
            this.sequenceNumber = l2;
            this.sources = list2;
            this.lengths = lArray;
        }

        @NotNull
        public final String key() {
            return this.key;
        }

        @Nullable
        public final Editor edit() throws IOException {
            return DiskLruCache.this.edit(this.key, this.sequenceNumber);
        }

        @NotNull
        public final Source getSource(int n2) {
            return this.sources.get(n2);
        }

        public final long getLength(int n2) {
            return this.lengths[n2];
        }

        @Override
        public final void close() {
            for (Source source2 : this.sources) {
                Util.closeQuietly(source2);
            }
        }
    }
}

