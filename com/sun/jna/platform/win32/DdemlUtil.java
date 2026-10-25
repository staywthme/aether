/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna.platform.win32;

import com.sun.jna.IntegerType;
import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.sun.jna.PointerType;
import com.sun.jna.Structure;
import com.sun.jna.platform.win32.BaseTSD;
import com.sun.jna.platform.win32.Ddeml;
import com.sun.jna.platform.win32.User32Util;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.win32.W32APIOptions;
import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class DdemlUtil {

    public static interface IDdeConnectionList
    extends Closeable {
        public Ddeml.HCONVLIST getHandle();

        public IDdeConnection queryNextServer(IDdeConnection var1);

        @Override
        public void close();
    }

    public static interface IDdeClient
    extends Closeable {
        public Integer getInstanceIdentitifier();

        public void initialize(int var1) throws DdemlException;

        public Ddeml.HSZ createStringHandle(String var1) throws DdemlException;

        public String queryString(Ddeml.HSZ var1) throws DdemlException;

        public boolean freeStringHandle(Ddeml.HSZ var1);

        public boolean keepStringHandle(Ddeml.HSZ var1);

        public void nameService(Ddeml.HSZ var1, int var2) throws DdemlException;

        public void nameService(String var1, int var2) throws DdemlException;

        public int getLastError();

        public IDdeConnection connect(Ddeml.HSZ var1, Ddeml.HSZ var2, Ddeml.CONVCONTEXT var3);

        public IDdeConnection connect(String var1, String var2, Ddeml.CONVCONTEXT var3);

        public Ddeml.HDDEDATA createDataHandle(Pointer var1, int var2, int var3, Ddeml.HSZ var4, int var5, int var6);

        public void freeDataHandle(Ddeml.HDDEDATA var1);

        public Ddeml.HDDEDATA addData(Ddeml.HDDEDATA var1, Pointer var2, int var3, int var4);

        public int getData(Ddeml.HDDEDATA var1, Pointer var2, int var3, int var4);

        public Pointer accessData(Ddeml.HDDEDATA var1, WinDef.DWORDByReference var2);

        public void unaccessData(Ddeml.HDDEDATA var1);

        public void postAdvise(Ddeml.HSZ var1, Ddeml.HSZ var2);

        public void postAdvise(String var1, String var2);

        public void abandonTransactions();

        public IDdeConnectionList connectList(Ddeml.HSZ var1, Ddeml.HSZ var2, IDdeConnectionList var3, Ddeml.CONVCONTEXT var4);

        public IDdeConnectionList connectList(String var1, String var2, IDdeConnectionList var3, Ddeml.CONVCONTEXT var4);

        public boolean enableCallback(int var1);

        public boolean uninitialize();

        public IDdeConnection wrap(Ddeml.HCONV var1);

        public void registerAdvstartHandler(AdvstartHandler var1);

        public void unregisterAdvstartHandler(AdvstartHandler var1);

        public void registerAdvstopHandler(AdvstopHandler var1);

        public void unregisterAdvstopHandler(AdvstopHandler var1);

        public void registerConnectHandler(ConnectHandler var1);

        public void unregisterConnectHandler(ConnectHandler var1);

        public void registerAdvReqHandler(AdvreqHandler var1);

        public void unregisterAdvReqHandler(AdvreqHandler var1);

        public void registerRequestHandler(RequestHandler var1);

        public void unregisterRequestHandler(RequestHandler var1);

        public void registerWildconnectHandler(WildconnectHandler var1);

        public void unregisterWildconnectHandler(WildconnectHandler var1);

        public void registerAdvdataHandler(AdvdataHandler var1);

        public void unregisterAdvdataHandler(AdvdataHandler var1);

        public void registerExecuteHandler(ExecuteHandler var1);

        public void unregisterExecuteHandler(ExecuteHandler var1);

        public void registerPokeHandler(PokeHandler var1);

        public void unregisterPokeHandler(PokeHandler var1);

        public void registerConnectConfirmHandler(ConnectConfirmHandler var1);

        public void unregisterConnectConfirmHandler(ConnectConfirmHandler var1);

        public void registerDisconnectHandler(DisconnectHandler var1);

        public void unregisterDisconnectHandler(DisconnectHandler var1);

        public void registerErrorHandler(ErrorHandler var1);

        public void unregisterErrorHandler(ErrorHandler var1);

        public void registerRegisterHandler(RegisterHandler var1);

        public void unregisterRegisterHandler(RegisterHandler var1);

        public void registerXactCompleteHandler(XactCompleteHandler var1);

        public void unregisterXactCompleteHandler(XactCompleteHandler var1);

        public void registerUnregisterHandler(UnregisterHandler var1);

        public void unregisterUnregisterHandler(UnregisterHandler var1);

        public void registerMonitorHandler(MonitorHandler var1);

        public void unregisterMonitorHandler(MonitorHandler var1);
    }

    public static interface IDdeConnection
    extends Closeable {
        public Ddeml.HCONV getConv();

        public void execute(String var1, int var2, WinDef.DWORDByReference var3, BaseTSD.DWORD_PTR var4);

        public void poke(Pointer var1, int var2, Ddeml.HSZ var3, int var4, int var5, WinDef.DWORDByReference var6, BaseTSD.DWORD_PTR var7);

        public void poke(Pointer var1, int var2, String var3, int var4, int var5, WinDef.DWORDByReference var6, BaseTSD.DWORD_PTR var7);

        public Ddeml.HDDEDATA request(Ddeml.HSZ var1, int var2, int var3, WinDef.DWORDByReference var4, BaseTSD.DWORD_PTR var5);

        public Ddeml.HDDEDATA request(String var1, int var2, int var3, WinDef.DWORDByReference var4, BaseTSD.DWORD_PTR var5);

        public Ddeml.HDDEDATA clientTransaction(Pointer var1, int var2, Ddeml.HSZ var3, int var4, int var5, int var6, WinDef.DWORDByReference var7, BaseTSD.DWORD_PTR var8);

        public Ddeml.HDDEDATA clientTransaction(Pointer var1, int var2, String var3, int var4, int var5, int var6, WinDef.DWORDByReference var7, BaseTSD.DWORD_PTR var8);

        public void advstart(Ddeml.HSZ var1, int var2, int var3, WinDef.DWORDByReference var4, BaseTSD.DWORD_PTR var5);

        public void advstart(String var1, int var2, int var3, WinDef.DWORDByReference var4, BaseTSD.DWORD_PTR var5);

        public void advstop(Ddeml.HSZ var1, int var2, int var3, WinDef.DWORDByReference var4, BaseTSD.DWORD_PTR var5);

        public void advstop(String var1, int var2, int var3, WinDef.DWORDByReference var4, BaseTSD.DWORD_PTR var5);

        public void abandonTransaction(int var1);

        public void abandonTransactions();

        public void impersonateClient();

        @Override
        public void close();

        public void reconnect();

        public boolean enableCallback(int var1);

        public void setUserHandle(int var1, BaseTSD.DWORD_PTR var2) throws DdemlException;

        public Ddeml.CONVINFO queryConvInfo(int var1) throws DdemlException;
    }

    public static class DdemlException
    extends RuntimeException {
        private static final Map<Integer, String> ERROR_CODE_MAP;
        private final int errorCode;

        public static DdemlException create(int n2) {
            String string = ERROR_CODE_MAP.get(n2);
            return new DdemlException(n2, String.format("%s (Code: 0x%X)", string != null ? string : "", n2));
        }

        public DdemlException(int n2, String string) {
            super(string);
            this.errorCode = n2;
        }

        public int getErrorCode() {
            return this.errorCode;
        }

        static {
            HashMap<Integer, String> hashMap = new HashMap<Integer, String>();
            for (Field field : Ddeml.class.getFields()) {
                String string = field.getName();
                if (!string.startsWith("DMLERR_") || string.equals("DMLERR_FIRST") || string.equals("DMLERR_LAST")) continue;
                try {
                    hashMap.put(field.getInt(null), string);
                }
                catch (IllegalAccessException | IllegalArgumentException exception) {
                    throw new RuntimeException(exception);
                }
            }
            ERROR_CODE_MAP = Collections.unmodifiableMap(hashMap);
        }
    }

    public static class DdeAdapter
    implements Ddeml.DdeCallback {
        private static final Logger LOG = Logger.getLogger(DdeAdapter.class.getName());
        private int idInst;
        private final List<AdvstartHandler> advstartHandler = new CopyOnWriteArrayList<AdvstartHandler>();
        private final List<AdvstopHandler> advstopHandler = new CopyOnWriteArrayList<AdvstopHandler>();
        private final List<ConnectHandler> connectHandler = new CopyOnWriteArrayList<ConnectHandler>();
        private final List<AdvreqHandler> advReqHandler = new CopyOnWriteArrayList<AdvreqHandler>();
        private final List<RequestHandler> requestHandler = new CopyOnWriteArrayList<RequestHandler>();
        private final List<WildconnectHandler> wildconnectHandler = new CopyOnWriteArrayList<WildconnectHandler>();
        private final List<AdvdataHandler> advdataHandler = new CopyOnWriteArrayList<AdvdataHandler>();
        private final List<ExecuteHandler> executeHandler = new CopyOnWriteArrayList<ExecuteHandler>();
        private final List<PokeHandler> pokeHandler = new CopyOnWriteArrayList<PokeHandler>();
        private final List<ConnectConfirmHandler> connectConfirmHandler = new CopyOnWriteArrayList<ConnectConfirmHandler>();
        private final List<DisconnectHandler> disconnectHandler = new CopyOnWriteArrayList<DisconnectHandler>();
        private final List<ErrorHandler> errorHandler = new CopyOnWriteArrayList<ErrorHandler>();
        private final List<RegisterHandler> registerHandler = new CopyOnWriteArrayList<RegisterHandler>();
        private final List<XactCompleteHandler> xactCompleteHandler = new CopyOnWriteArrayList<XactCompleteHandler>();
        private final List<UnregisterHandler> unregisterHandler = new CopyOnWriteArrayList<UnregisterHandler>();
        private final List<MonitorHandler> monitorHandler = new CopyOnWriteArrayList<MonitorHandler>();

        public void setInstanceIdentifier(int n2) {
            this.idInst = n2;
        }

        @Override
        public WinDef.PVOID ddeCallback(int n2, int n3, Ddeml.HCONV object, Ddeml.HSZ hSZPAIRArray, Ddeml.HSZ hSZ, Ddeml.HDDEDATA hDDEDATA, BaseTSD.ULONG_PTR object2, BaseTSD.ULONG_PTR uLONG_PTR) {
            try {
                switch (n2) {
                    case 4144: {
                        n2 = this.onAdvstart(n2, n3, (Ddeml.HCONV)object, (Ddeml.HSZ)hSZPAIRArray, hSZ) ? 1 : 0;
                        return new WinDef.PVOID(Pointer.createConstant(new WinDef.BOOL(n2 != 0).intValue()));
                    }
                    case 4194: {
                        object = null;
                        if (((BaseTSD.ULONG_PTR)object2).toPointer() != null) {
                            object = new Ddeml.CONVCONTEXT(new Pointer(((IntegerType)object2).longValue()));
                        }
                        n2 = this.onConnect(n2, (Ddeml.HSZ)hSZPAIRArray, hSZ, (Ddeml.CONVCONTEXT)object, uLONG_PTR != null && uLONG_PTR.intValue() != 0) ? 1 : 0;
                        return new WinDef.PVOID(Pointer.createConstant(new WinDef.BOOL(n2 != 0).intValue()));
                    }
                    case 8226: {
                        int n4 = ((IntegerType)object2).intValue() & 0xFFFF;
                        Ddeml.HDDEDATA hDDEDATA2 = this.onAdvreq(n2, n3, (Ddeml.HCONV)object, (Ddeml.HSZ)hSZPAIRArray, hSZ, n4);
                        if (hDDEDATA2 == null) {
                            return new WinDef.PVOID();
                        }
                        return new WinDef.PVOID(hDDEDATA2.getPointer());
                    }
                    case 8368: {
                        Ddeml.HDDEDATA hDDEDATA3 = this.onRequest(n2, n3, (Ddeml.HCONV)object, (Ddeml.HSZ)hSZPAIRArray, hSZ);
                        if (hDDEDATA3 == null) {
                            return new WinDef.PVOID();
                        }
                        return new WinDef.PVOID(hDDEDATA3.getPointer());
                    }
                    case 8418: {
                        Object object3;
                        object = null;
                        if (((BaseTSD.ULONG_PTR)object2).toPointer() != null) {
                            object = new Ddeml.CONVCONTEXT(new Pointer(((IntegerType)object2).longValue()));
                        }
                        if ((object3 = this.onWildconnect(n2, (Ddeml.HSZ)hSZPAIRArray, hSZ, (Ddeml.CONVCONTEXT)object, uLONG_PTR != null && uLONG_PTR.intValue() != 0)) == null || ((Ddeml.HSZPAIR[])object3).length == 0) {
                            return new WinDef.PVOID();
                        }
                        int n5 = 0;
                        hSZPAIRArray = object3;
                        int n6 = ((Ddeml.HSZPAIR[])object3).length;
                        for (int i2 = 0; i2 < n6; ++i2) {
                            object2 = hSZPAIRArray[i2];
                            ((Structure)object2).write();
                            n5 += ((Structure)object2).size();
                        }
                        object3 = Ddeml.INSTANCE.DdeCreateDataHandle(this.idInst, object3[0].getPointer(), n5, 0, null, n3, 0);
                        return new WinDef.PVOID(((PointerType)object3).getPointer());
                    }
                    case 16400: {
                        n2 = this.onAdvdata(n2, n3, (Ddeml.HCONV)object, (Ddeml.HSZ)hSZPAIRArray, hSZ, hDDEDATA);
                        return new WinDef.PVOID(Pointer.createConstant(n2));
                    }
                    case 16464: {
                        n2 = this.onExecute(n2, (Ddeml.HCONV)object, (Ddeml.HSZ)hSZPAIRArray, hDDEDATA);
                        Ddeml.INSTANCE.DdeFreeDataHandle(hDDEDATA);
                        return new WinDef.PVOID(Pointer.createConstant(n2));
                    }
                    case 16528: {
                        n2 = this.onPoke(n2, n3, (Ddeml.HCONV)object, (Ddeml.HSZ)hSZPAIRArray, hSZ, hDDEDATA);
                        return new WinDef.PVOID(Pointer.createConstant(n2));
                    }
                    case 32832: {
                        this.onAdvstop(n2, n3, (Ddeml.HCONV)object, (Ddeml.HSZ)hSZPAIRArray, hSZ);
                        break;
                    }
                    case 32882: {
                        this.onConnectConfirm(n2, (Ddeml.HCONV)object, (Ddeml.HSZ)hSZPAIRArray, hSZ, uLONG_PTR != null && uLONG_PTR.intValue() != 0);
                        break;
                    }
                    case 32962: {
                        this.onDisconnect(n2, (Ddeml.HCONV)object, uLONG_PTR != null && uLONG_PTR.intValue() != 0);
                        break;
                    }
                    case 32770: {
                        this.onError(n2, (Ddeml.HCONV)object, (int)(uLONG_PTR.longValue() & 0xFFFFL));
                        break;
                    }
                    case 32930: {
                        this.onRegister(n2, (Ddeml.HSZ)hSZPAIRArray, hSZ);
                        break;
                    }
                    case 32896: {
                        this.onXactComplete(n2, n3, (Ddeml.HCONV)object, (Ddeml.HSZ)hSZPAIRArray, hSZ, hDDEDATA, (BaseTSD.ULONG_PTR)object2, uLONG_PTR);
                        break;
                    }
                    case 32978: {
                        this.onUnregister(n2, (Ddeml.HSZ)hSZPAIRArray, hSZ);
                        break;
                    }
                    case 33010: {
                        this.onMonitor(n2, hDDEDATA, uLONG_PTR.intValue());
                        break;
                    }
                    default: {
                        LOG.log(Level.FINE, String.format("Not implemented Operation - Transaction type: 0x%X (%s)", n2, null));
                        break;
                    }
                }
            }
            catch (BlockException blockException) {
                return new WinDef.PVOID(Pointer.createConstant(-1));
            }
            catch (Throwable throwable) {
                LOG.log(Level.WARNING, "Exception in DDECallback", throwable);
            }
            return new WinDef.PVOID();
        }

        public void registerAdvstartHandler(AdvstartHandler advstartHandler) {
            this.advstartHandler.add(advstartHandler);
        }

        public void unregisterAdvstartHandler(AdvstartHandler advstartHandler) {
            this.advstartHandler.remove(advstartHandler);
        }

        private boolean onAdvstart(int n2, int n3, Ddeml.HCONV hCONV, Ddeml.HSZ hSZ, Ddeml.HSZ hSZ2) {
            boolean bl = false;
            for (AdvstartHandler advstartHandler : this.advstartHandler) {
                if (!advstartHandler.onAdvstart(n2, n3, hCONV, hSZ, hSZ2)) continue;
                bl = true;
            }
            return bl;
        }

        public void registerAdvstopHandler(AdvstopHandler advstopHandler) {
            this.advstopHandler.add(advstopHandler);
        }

        public void unregisterAdvstopHandler(AdvstopHandler advstopHandler) {
            this.advstopHandler.remove(advstopHandler);
        }

        private void onAdvstop(int n2, int n3, Ddeml.HCONV hCONV, Ddeml.HSZ hSZ, Ddeml.HSZ hSZ2) {
            for (AdvstopHandler advstopHandler : this.advstopHandler) {
                advstopHandler.onAdvstop(n2, n3, hCONV, hSZ, hSZ2);
            }
        }

        public void registerConnectHandler(ConnectHandler connectHandler) {
            this.connectHandler.add(connectHandler);
        }

        public void unregisterConnectHandler(ConnectHandler connectHandler) {
            this.connectHandler.remove(connectHandler);
        }

        private boolean onConnect(int n2, Ddeml.HSZ hSZ, Ddeml.HSZ hSZ2, Ddeml.CONVCONTEXT cONVCONTEXT, boolean bl) {
            boolean bl2 = false;
            for (ConnectHandler connectHandler : this.connectHandler) {
                if (!connectHandler.onConnect(n2, hSZ, hSZ2, cONVCONTEXT, bl)) continue;
                bl2 = true;
            }
            return bl2;
        }

        public void registerAdvReqHandler(AdvreqHandler advreqHandler) {
            this.advReqHandler.add(advreqHandler);
        }

        public void unregisterAdvReqHandler(AdvreqHandler advreqHandler) {
            this.advReqHandler.remove(advreqHandler);
        }

        private Ddeml.HDDEDATA onAdvreq(int n2, int n3, Ddeml.HCONV hCONV, Ddeml.HSZ hSZ, Ddeml.HSZ hSZ2, int n4) {
            for (AdvreqHandler advreqHandler : this.advReqHandler) {
                Ddeml.HDDEDATA object = advreqHandler.onAdvreq(n2, n3, hCONV, hSZ, hSZ2, n4);
                if (object == null) continue;
                return object;
            }
            return null;
        }

        public void registerRequestHandler(RequestHandler requestHandler) {
            this.requestHandler.add(requestHandler);
        }

        public void unregisterRequestHandler(RequestHandler requestHandler) {
            this.requestHandler.remove(requestHandler);
        }

        private Ddeml.HDDEDATA onRequest(int n2, int n3, Ddeml.HCONV hCONV, Ddeml.HSZ hSZ, Ddeml.HSZ hSZ2) {
            for (RequestHandler requestHandler : this.requestHandler) {
                Ddeml.HDDEDATA object = requestHandler.onRequest(n2, n3, hCONV, hSZ, hSZ2);
                if (object == null) continue;
                return object;
            }
            return null;
        }

        public void registerWildconnectHandler(WildconnectHandler wildconnectHandler) {
            this.wildconnectHandler.add(wildconnectHandler);
        }

        public void unregisterWildconnectHandler(WildconnectHandler wildconnectHandler) {
            this.wildconnectHandler.remove(wildconnectHandler);
        }

        private Ddeml.HSZPAIR[] onWildconnect(int n2, Ddeml.HSZ hSZ, Ddeml.HSZ hSZ2, Ddeml.CONVCONTEXT cONVCONTEXT, boolean bl) {
            ArrayList<Ddeml.HSZPAIR> arrayList = new ArrayList<Ddeml.HSZPAIR>(1);
            for (WildconnectHandler wildconnectHandler : this.wildconnectHandler) {
                arrayList.addAll(wildconnectHandler.onWildconnect(n2, hSZ, hSZ2, cONVCONTEXT, bl));
            }
            return arrayList.toArray(new Ddeml.HSZPAIR[0]);
        }

        public void registerAdvdataHandler(AdvdataHandler advdataHandler) {
            this.advdataHandler.add(advdataHandler);
        }

        public void unregisterAdvdataHandler(AdvdataHandler advdataHandler) {
            this.advdataHandler.remove(advdataHandler);
        }

        private int onAdvdata(int n2, int n3, Ddeml.HCONV hCONV, Ddeml.HSZ hSZ, Ddeml.HSZ hSZ2, Ddeml.HDDEDATA hDDEDATA) {
            for (AdvdataHandler advdataHandler : this.advdataHandler) {
                int n4 = advdataHandler.onAdvdata(n2, n3, hCONV, hSZ, hSZ2, hDDEDATA);
                if (n4 == 0) continue;
                return n4;
            }
            return 0;
        }

        public void registerExecuteHandler(ExecuteHandler executeHandler) {
            this.executeHandler.add(executeHandler);
        }

        public void unregisterExecuteHandler(ExecuteHandler executeHandler) {
            this.executeHandler.remove(executeHandler);
        }

        private int onExecute(int n2, Ddeml.HCONV hCONV, Ddeml.HSZ hSZ, Ddeml.HDDEDATA hDDEDATA) {
            for (ExecuteHandler executeHandler : this.executeHandler) {
                int n3 = executeHandler.onExecute(n2, hCONV, hSZ, hDDEDATA);
                if (n3 == 0) continue;
                return n3;
            }
            return 0;
        }

        public void registerPokeHandler(PokeHandler pokeHandler) {
            this.pokeHandler.add(pokeHandler);
        }

        public void unregisterPokeHandler(PokeHandler pokeHandler) {
            this.pokeHandler.remove(pokeHandler);
        }

        private int onPoke(int n2, int n3, Ddeml.HCONV hCONV, Ddeml.HSZ hSZ, Ddeml.HSZ hSZ2, Ddeml.HDDEDATA hDDEDATA) {
            for (PokeHandler pokeHandler : this.pokeHandler) {
                int n4 = pokeHandler.onPoke(n2, n3, hCONV, hSZ, hSZ2, hDDEDATA);
                if (n4 == 0) continue;
                return n4;
            }
            return 0;
        }

        public void registerConnectConfirmHandler(ConnectConfirmHandler connectConfirmHandler) {
            this.connectConfirmHandler.add(connectConfirmHandler);
        }

        public void unregisterConnectConfirmHandler(ConnectConfirmHandler connectConfirmHandler) {
            this.connectConfirmHandler.remove(connectConfirmHandler);
        }

        private void onConnectConfirm(int n2, Ddeml.HCONV hCONV, Ddeml.HSZ hSZ, Ddeml.HSZ hSZ2, boolean bl) {
            for (ConnectConfirmHandler connectConfirmHandler : this.connectConfirmHandler) {
                connectConfirmHandler.onConnectConfirm(n2, hCONV, hSZ, hSZ2, bl);
            }
        }

        public void registerDisconnectHandler(DisconnectHandler disconnectHandler) {
            this.disconnectHandler.add(disconnectHandler);
        }

        public void unregisterDisconnectHandler(DisconnectHandler disconnectHandler) {
            this.disconnectHandler.remove(disconnectHandler);
        }

        private void onDisconnect(int n2, Ddeml.HCONV hCONV, boolean bl) {
            for (DisconnectHandler disconnectHandler : this.disconnectHandler) {
                disconnectHandler.onDisconnect(n2, hCONV, bl);
            }
        }

        public void registerErrorHandler(ErrorHandler errorHandler) {
            this.errorHandler.add(errorHandler);
        }

        public void unregisterErrorHandler(ErrorHandler errorHandler) {
            this.errorHandler.remove(errorHandler);
        }

        private void onError(int n2, Ddeml.HCONV hCONV, int n3) {
            for (ErrorHandler errorHandler : this.errorHandler) {
                errorHandler.onError(n2, hCONV, n3);
            }
        }

        public void registerRegisterHandler(RegisterHandler registerHandler) {
            this.registerHandler.add(registerHandler);
        }

        public void unregisterRegisterHandler(RegisterHandler registerHandler) {
            this.registerHandler.remove(registerHandler);
        }

        private void onRegister(int n2, Ddeml.HSZ hSZ, Ddeml.HSZ hSZ2) {
            for (RegisterHandler registerHandler : this.registerHandler) {
                registerHandler.onRegister(n2, hSZ, hSZ2);
            }
        }

        public void registerXactCompleteHandler(XactCompleteHandler xactCompleteHandler) {
            this.xactCompleteHandler.add(xactCompleteHandler);
        }

        public void xactCompleteXactCompleteHandler(XactCompleteHandler xactCompleteHandler) {
            this.xactCompleteHandler.remove(xactCompleteHandler);
        }

        private void onXactComplete(int n2, int n3, Ddeml.HCONV hCONV, Ddeml.HSZ hSZ, Ddeml.HSZ hSZ2, Ddeml.HDDEDATA hDDEDATA, BaseTSD.ULONG_PTR uLONG_PTR, BaseTSD.ULONG_PTR uLONG_PTR2) {
            for (XactCompleteHandler xactCompleteHandler : this.xactCompleteHandler) {
                xactCompleteHandler.onXactComplete(n2, n3, hCONV, hSZ, hSZ2, hDDEDATA, uLONG_PTR, uLONG_PTR2);
            }
        }

        public void registerUnregisterHandler(UnregisterHandler unregisterHandler) {
            this.unregisterHandler.add(unregisterHandler);
        }

        public void unregisterUnregisterHandler(UnregisterHandler unregisterHandler) {
            this.unregisterHandler.remove(unregisterHandler);
        }

        private void onUnregister(int n2, Ddeml.HSZ hSZ, Ddeml.HSZ hSZ2) {
            for (UnregisterHandler unregisterHandler : this.unregisterHandler) {
                unregisterHandler.onUnregister(n2, hSZ, hSZ2);
            }
        }

        public void registerMonitorHandler(MonitorHandler monitorHandler) {
            this.monitorHandler.add(monitorHandler);
        }

        public void unregisterMonitorHandler(MonitorHandler monitorHandler) {
            this.monitorHandler.remove(monitorHandler);
        }

        private void onMonitor(int n2, Ddeml.HDDEDATA hDDEDATA, int n3) {
            for (MonitorHandler monitorHandler : this.monitorHandler) {
                monitorHandler.onMonitor(n2, hDDEDATA, n3);
            }
        }

        public static class BlockException
        extends RuntimeException {
        }
    }

    public static interface MonitorHandler {
        public void onMonitor(int var1, Ddeml.HDDEDATA var2, int var3);
    }

    public static interface PokeHandler {
        public int onPoke(int var1, int var2, Ddeml.HCONV var3, Ddeml.HSZ var4, Ddeml.HSZ var5, Ddeml.HDDEDATA var6);
    }

    public static interface ExecuteHandler {
        public int onExecute(int var1, Ddeml.HCONV var2, Ddeml.HSZ var3, Ddeml.HDDEDATA var4);
    }

    public static interface UnregisterHandler {
        public void onUnregister(int var1, Ddeml.HSZ var2, Ddeml.HSZ var3);
    }

    public static interface XactCompleteHandler {
        public void onXactComplete(int var1, int var2, Ddeml.HCONV var3, Ddeml.HSZ var4, Ddeml.HSZ var5, Ddeml.HDDEDATA var6, BaseTSD.ULONG_PTR var7, BaseTSD.ULONG_PTR var8);
    }

    public static interface RegisterHandler {
        public void onRegister(int var1, Ddeml.HSZ var2, Ddeml.HSZ var3);
    }

    public static interface ErrorHandler {
        public void onError(int var1, Ddeml.HCONV var2, int var3);
    }

    public static interface DisconnectHandler {
        public void onDisconnect(int var1, Ddeml.HCONV var2, boolean var3);
    }

    public static interface ConnectConfirmHandler {
        public void onConnectConfirm(int var1, Ddeml.HCONV var2, Ddeml.HSZ var3, Ddeml.HSZ var4, boolean var5);
    }

    public static interface AdvdataHandler {
        public int onAdvdata(int var1, int var2, Ddeml.HCONV var3, Ddeml.HSZ var4, Ddeml.HSZ var5, Ddeml.HDDEDATA var6);
    }

    public static interface WildconnectHandler {
        public List<Ddeml.HSZPAIR> onWildconnect(int var1, Ddeml.HSZ var2, Ddeml.HSZ var3, Ddeml.CONVCONTEXT var4, boolean var5);
    }

    public static interface RequestHandler {
        public Ddeml.HDDEDATA onRequest(int var1, int var2, Ddeml.HCONV var3, Ddeml.HSZ var4, Ddeml.HSZ var5);
    }

    public static interface AdvreqHandler {
        public Ddeml.HDDEDATA onAdvreq(int var1, int var2, Ddeml.HCONV var3, Ddeml.HSZ var4, Ddeml.HSZ var5, int var6);
    }

    public static interface ConnectHandler {
        public boolean onConnect(int var1, Ddeml.HSZ var2, Ddeml.HSZ var3, Ddeml.CONVCONTEXT var4, boolean var5);
    }

    public static interface AdvstopHandler {
        public void onAdvstop(int var1, int var2, Ddeml.HCONV var3, Ddeml.HSZ var4, Ddeml.HSZ var5);
    }

    public static interface AdvstartHandler {
        public boolean onAdvstart(int var1, int var2, Ddeml.HCONV var3, Ddeml.HSZ var4, Ddeml.HSZ var5);
    }

    public static class DdeClient
    implements IDdeClient {
        private Integer idInst;
        private final DdeAdapter ddeAdapter = new DdeAdapter();

        @Override
        public Integer getInstanceIdentitifier() {
            return this.idInst;
        }

        @Override
        public void initialize(int n2) throws DdemlException {
            WinDef.DWORDByReference dWORDByReference = new WinDef.DWORDByReference();
            Integer n3 = Ddeml.INSTANCE.DdeInitialize(dWORDByReference, this.ddeAdapter, n2, 0);
            if (n3 != 0) {
                throw DdemlException.create(n3);
            }
            this.idInst = dWORDByReference.getValue().intValue();
            if (this.ddeAdapter instanceof DdeAdapter) {
                this.ddeAdapter.setInstanceIdentifier(this.idInst);
            }
        }

        @Override
        public Ddeml.HSZ createStringHandle(String object) throws DdemlException {
            if (object == null) {
                return null;
            }
            int n2 = W32APIOptions.DEFAULT_OPTIONS == W32APIOptions.UNICODE_OPTIONS ? 1200 : 1004;
            object = Ddeml.INSTANCE.DdeCreateStringHandle(this.idInst, (String)object, n2);
            if (object == null) {
                throw DdemlException.create(this.getLastError());
            }
            return object;
        }

        @Override
        public void nameService(Ddeml.HSZ pointerType, int n2) throws DdemlException {
            pointerType = Ddeml.INSTANCE.DdeNameService(this.idInst, (Ddeml.HSZ)pointerType, new Ddeml.HSZ(), n2);
            if (pointerType == null) {
                throw DdemlException.create(this.getLastError());
            }
        }

        @Override
        public void nameService(String string, int n2) throws DdemlException {
            Ddeml.HSZ hSZ = null;
            try {
                hSZ = this.createStringHandle(string);
                this.nameService(hSZ, n2);
                return;
            }
            finally {
                this.freeStringHandle(hSZ);
            }
        }

        @Override
        public int getLastError() {
            return Ddeml.INSTANCE.DdeGetLastError(this.idInst);
        }

        @Override
        public IDdeConnection connect(Ddeml.HSZ pointerType, Ddeml.HSZ hSZ, Ddeml.CONVCONTEXT cONVCONTEXT) {
            pointerType = Ddeml.INSTANCE.DdeConnect(this.idInst, (Ddeml.HSZ)pointerType, hSZ, cONVCONTEXT);
            if (pointerType == null) {
                throw DdemlException.create(this.getLastError());
            }
            return new DdeConnection(this, (Ddeml.HCONV)pointerType);
        }

        @Override
        public IDdeConnection connect(String object, String string, Ddeml.CONVCONTEXT cONVCONTEXT) {
            Ddeml.HSZ hSZ = null;
            Ddeml.HSZ hSZ2 = null;
            try {
                hSZ = this.createStringHandle((String)object);
                hSZ2 = this.createStringHandle(string);
                object = this.connect(hSZ, hSZ2, cONVCONTEXT);
                this.freeStringHandle(hSZ2);
            }
            catch (Throwable throwable) {
                this.freeStringHandle(hSZ2);
                this.freeStringHandle(hSZ);
                throw throwable;
            }
            this.freeStringHandle(hSZ);
            return object;
        }

        @Override
        public String queryString(Ddeml.HSZ object) throws DdemlException {
            int n2;
            int n3;
            if (W32APIOptions.DEFAULT_OPTIONS == W32APIOptions.UNICODE_OPTIONS) {
                n3 = 1200;
                n2 = 2;
            } else {
                n3 = 1004;
                n2 = 1;
            }
            Memory memory = new Memory(n2 * 257);
            try {
                Ddeml.INSTANCE.DdeQueryString(this.idInst, (Ddeml.HSZ)object, memory, 256, n3);
                if (W32APIOptions.DEFAULT_OPTIONS == W32APIOptions.UNICODE_OPTIONS) {
                    object = ((Pointer)memory).getWideString(0L);
                    return object;
                }
                object = memory.getString(0L);
                return object;
            }
            finally {
                memory.valid();
            }
        }

        @Override
        public Ddeml.HDDEDATA createDataHandle(Pointer object, int n2, int n3, Ddeml.HSZ hSZ, int n4, int n5) {
            object = Ddeml.INSTANCE.DdeCreateDataHandle(this.idInst, (Pointer)object, n2, n3, hSZ, n4, n5);
            if (object == null) {
                throw DdemlException.create(this.getLastError());
            }
            return object;
        }

        @Override
        public void freeDataHandle(Ddeml.HDDEDATA hDDEDATA) {
            boolean bl = Ddeml.INSTANCE.DdeFreeDataHandle(hDDEDATA);
            if (!bl) {
                throw DdemlException.create(this.getLastError());
            }
        }

        @Override
        public Ddeml.HDDEDATA addData(Ddeml.HDDEDATA hDDEDATA, Pointer pointer, int n2, int n3) {
            if ((hDDEDATA = Ddeml.INSTANCE.DdeAddData(hDDEDATA, pointer, n2, n3)) == null) {
                throw DdemlException.create(this.getLastError());
            }
            return hDDEDATA;
        }

        @Override
        public int getData(Ddeml.HDDEDATA hDDEDATA, Pointer pointer, int n2, int n3) {
            int n4 = Ddeml.INSTANCE.DdeGetData(hDDEDATA, pointer, n2, n3);
            int n5 = this.getLastError();
            if (n5 != 0) {
                throw DdemlException.create(n5);
            }
            return n4;
        }

        @Override
        public Pointer accessData(Ddeml.HDDEDATA object, WinDef.DWORDByReference dWORDByReference) {
            if ((object = Ddeml.INSTANCE.DdeAccessData((Ddeml.HDDEDATA)object, dWORDByReference)) == null) {
                throw DdemlException.create(this.getLastError());
            }
            return object;
        }

        @Override
        public void unaccessData(Ddeml.HDDEDATA hDDEDATA) {
            boolean bl = Ddeml.INSTANCE.DdeUnaccessData(hDDEDATA);
            if (!bl) {
                throw DdemlException.create(this.getLastError());
            }
        }

        @Override
        public void postAdvise(Ddeml.HSZ hSZ, Ddeml.HSZ hSZ2) {
            boolean bl = Ddeml.INSTANCE.DdePostAdvise(this.idInst, hSZ, hSZ2);
            if (!bl) {
                throw DdemlException.create(this.getLastError());
            }
        }

        @Override
        public void postAdvise(String string, String string2) {
            Ddeml.HSZ hSZ = null;
            Ddeml.HSZ hSZ2 = null;
            try {
                hSZ2 = this.createStringHandle(string);
                hSZ = this.createStringHandle(string2);
                this.postAdvise(hSZ2, hSZ);
                return;
            }
            finally {
                this.freeStringHandle(hSZ2);
                this.freeStringHandle(hSZ);
            }
        }

        @Override
        public boolean freeStringHandle(Ddeml.HSZ hSZ) {
            if (hSZ == null) {
                return true;
            }
            return Ddeml.INSTANCE.DdeFreeStringHandle(this.idInst, hSZ);
        }

        @Override
        public boolean keepStringHandle(Ddeml.HSZ hSZ) {
            return Ddeml.INSTANCE.DdeKeepStringHandle(this.idInst, hSZ);
        }

        @Override
        public void abandonTransactions() {
            boolean bl = Ddeml.INSTANCE.DdeAbandonTransaction(this.idInst, null, 0);
            if (!bl) {
                throw DdemlException.create(this.getLastError());
            }
        }

        @Override
        public IDdeConnectionList connectList(Ddeml.HSZ pointerType, Ddeml.HSZ hSZ, IDdeConnectionList iDdeConnectionList, Ddeml.CONVCONTEXT cONVCONTEXT) {
            pointerType = Ddeml.INSTANCE.DdeConnectList(this.idInst, (Ddeml.HSZ)pointerType, hSZ, iDdeConnectionList != null ? iDdeConnectionList.getHandle() : null, cONVCONTEXT);
            if (pointerType == null) {
                throw DdemlException.create(this.getLastError());
            }
            return new DdeConnectionList(this, (Ddeml.HCONVLIST)pointerType);
        }

        @Override
        public IDdeConnectionList connectList(String object, String string, IDdeConnectionList iDdeConnectionList, Ddeml.CONVCONTEXT cONVCONTEXT) {
            Ddeml.HSZ hSZ = null;
            Ddeml.HSZ hSZ2 = null;
            try {
                hSZ = this.createStringHandle((String)object);
                hSZ2 = this.createStringHandle(string);
                object = this.connectList(hSZ, hSZ2, iDdeConnectionList, cONVCONTEXT);
                this.freeStringHandle(hSZ2);
            }
            catch (Throwable throwable) {
                this.freeStringHandle(hSZ2);
                this.freeStringHandle(hSZ);
                throw throwable;
            }
            this.freeStringHandle(hSZ);
            return object;
        }

        @Override
        public boolean enableCallback(int n2) {
            boolean bl = Ddeml.INSTANCE.DdeEnableCallback(this.idInst, null, n2);
            if (!bl && n2 != 2 && (n2 = this.getLastError()) != 0) {
                throw DdemlException.create(this.getLastError());
            }
            return bl;
        }

        @Override
        public boolean uninitialize() {
            return Ddeml.INSTANCE.DdeUninitialize(this.idInst);
        }

        @Override
        public void close() {
            this.uninitialize();
        }

        @Override
        public IDdeConnection wrap(Ddeml.HCONV hCONV) {
            return new DdeConnection(this, hCONV);
        }

        @Override
        public void unregisterDisconnectHandler(DisconnectHandler disconnectHandler) {
            this.ddeAdapter.unregisterDisconnectHandler(disconnectHandler);
        }

        @Override
        public void registerAdvstartHandler(AdvstartHandler advstartHandler) {
            this.ddeAdapter.registerAdvstartHandler(advstartHandler);
        }

        @Override
        public void unregisterAdvstartHandler(AdvstartHandler advstartHandler) {
            this.ddeAdapter.unregisterAdvstartHandler(advstartHandler);
        }

        @Override
        public void registerAdvstopHandler(AdvstopHandler advstopHandler) {
            this.ddeAdapter.registerAdvstopHandler(advstopHandler);
        }

        @Override
        public void unregisterAdvstopHandler(AdvstopHandler advstopHandler) {
            this.ddeAdapter.unregisterAdvstopHandler(advstopHandler);
        }

        @Override
        public void registerConnectHandler(ConnectHandler connectHandler) {
            this.ddeAdapter.registerConnectHandler(connectHandler);
        }

        @Override
        public void unregisterConnectHandler(ConnectHandler connectHandler) {
            this.ddeAdapter.unregisterConnectHandler(connectHandler);
        }

        @Override
        public void registerAdvReqHandler(AdvreqHandler advreqHandler) {
            this.ddeAdapter.registerAdvReqHandler(advreqHandler);
        }

        @Override
        public void unregisterAdvReqHandler(AdvreqHandler advreqHandler) {
            this.ddeAdapter.unregisterAdvReqHandler(advreqHandler);
        }

        @Override
        public void registerRequestHandler(RequestHandler requestHandler) {
            this.ddeAdapter.registerRequestHandler(requestHandler);
        }

        @Override
        public void unregisterRequestHandler(RequestHandler requestHandler) {
            this.ddeAdapter.unregisterRequestHandler(requestHandler);
        }

        @Override
        public void registerWildconnectHandler(WildconnectHandler wildconnectHandler) {
            this.ddeAdapter.registerWildconnectHandler(wildconnectHandler);
        }

        @Override
        public void unregisterWildconnectHandler(WildconnectHandler wildconnectHandler) {
            this.ddeAdapter.unregisterWildconnectHandler(wildconnectHandler);
        }

        @Override
        public void registerAdvdataHandler(AdvdataHandler advdataHandler) {
            this.ddeAdapter.registerAdvdataHandler(advdataHandler);
        }

        @Override
        public void unregisterAdvdataHandler(AdvdataHandler advdataHandler) {
            this.ddeAdapter.unregisterAdvdataHandler(advdataHandler);
        }

        @Override
        public void registerExecuteHandler(ExecuteHandler executeHandler) {
            this.ddeAdapter.registerExecuteHandler(executeHandler);
        }

        @Override
        public void unregisterExecuteHandler(ExecuteHandler executeHandler) {
            this.ddeAdapter.unregisterExecuteHandler(executeHandler);
        }

        @Override
        public void registerPokeHandler(PokeHandler pokeHandler) {
            this.ddeAdapter.registerPokeHandler(pokeHandler);
        }

        @Override
        public void unregisterPokeHandler(PokeHandler pokeHandler) {
            this.ddeAdapter.unregisterPokeHandler(pokeHandler);
        }

        @Override
        public void registerConnectConfirmHandler(ConnectConfirmHandler connectConfirmHandler) {
            this.ddeAdapter.registerConnectConfirmHandler(connectConfirmHandler);
        }

        @Override
        public void unregisterConnectConfirmHandler(ConnectConfirmHandler connectConfirmHandler) {
            this.ddeAdapter.unregisterConnectConfirmHandler(connectConfirmHandler);
        }

        @Override
        public void registerDisconnectHandler(DisconnectHandler disconnectHandler) {
            this.ddeAdapter.registerDisconnectHandler(disconnectHandler);
        }

        @Override
        public void registerErrorHandler(ErrorHandler errorHandler) {
            this.ddeAdapter.registerErrorHandler(errorHandler);
        }

        @Override
        public void unregisterErrorHandler(ErrorHandler errorHandler) {
            this.ddeAdapter.unregisterErrorHandler(errorHandler);
        }

        @Override
        public void registerRegisterHandler(RegisterHandler registerHandler) {
            this.ddeAdapter.registerRegisterHandler(registerHandler);
        }

        @Override
        public void unregisterRegisterHandler(RegisterHandler registerHandler) {
            this.ddeAdapter.unregisterRegisterHandler(registerHandler);
        }

        @Override
        public void registerXactCompleteHandler(XactCompleteHandler xactCompleteHandler) {
            this.ddeAdapter.registerXactCompleteHandler(xactCompleteHandler);
        }

        @Override
        public void unregisterXactCompleteHandler(XactCompleteHandler xactCompleteHandler) {
            this.ddeAdapter.xactCompleteXactCompleteHandler(xactCompleteHandler);
        }

        @Override
        public void registerUnregisterHandler(UnregisterHandler unregisterHandler) {
            this.ddeAdapter.registerUnregisterHandler(unregisterHandler);
        }

        @Override
        public void unregisterUnregisterHandler(UnregisterHandler unregisterHandler) {
            this.ddeAdapter.unregisterUnregisterHandler(unregisterHandler);
        }

        @Override
        public void registerMonitorHandler(MonitorHandler monitorHandler) {
            this.ddeAdapter.registerMonitorHandler(monitorHandler);
        }

        @Override
        public void unregisterMonitorHandler(MonitorHandler monitorHandler) {
            this.ddeAdapter.unregisterMonitorHandler(monitorHandler);
        }
    }

    public static class DdeConnectionList
    implements IDdeConnectionList {
        private final IDdeClient client;
        private final Ddeml.HCONVLIST convList;

        public DdeConnectionList(IDdeClient iDdeClient, Ddeml.HCONVLIST hCONVLIST) {
            this.convList = hCONVLIST;
            this.client = iDdeClient;
        }

        @Override
        public Ddeml.HCONVLIST getHandle() {
            return this.convList;
        }

        @Override
        public IDdeConnection queryNextServer(IDdeConnection object) {
            if ((object = Ddeml.INSTANCE.DdeQueryNextServer(this.convList, object != null ? object.getConv() : null)) != null) {
                return new DdeConnection(this.client, (Ddeml.HCONV)object);
            }
            return null;
        }

        @Override
        public void close() {
            boolean bl = Ddeml.INSTANCE.DdeDisconnectList(this.convList);
            if (!bl) {
                throw DdemlException.create(this.client.getLastError());
            }
        }
    }

    public static class DdeConnection
    implements IDdeConnection {
        private Ddeml.HCONV conv;
        private final IDdeClient client;

        public DdeConnection(IDdeClient iDdeClient, Ddeml.HCONV hCONV) {
            this.conv = hCONV;
            this.client = iDdeClient;
        }

        @Override
        public Ddeml.HCONV getConv() {
            return this.conv;
        }

        @Override
        public void abandonTransaction(int n2) {
            boolean bl = Ddeml.INSTANCE.DdeAbandonTransaction(this.client.getInstanceIdentitifier(), this.conv, n2);
            n2 = bl ? 1 : 0;
            if (!bl) {
                throw DdemlException.create(this.client.getLastError());
            }
        }

        @Override
        public void abandonTransactions() {
            boolean bl = Ddeml.INSTANCE.DdeAbandonTransaction(this.client.getInstanceIdentitifier(), this.conv, 0);
            if (!bl) {
                throw DdemlException.create(this.client.getLastError());
            }
        }

        @Override
        public Ddeml.HDDEDATA clientTransaction(Pointer object, int n2, Ddeml.HSZ hSZ, int n3, int n4, int n5, WinDef.DWORDByReference dWORDByReference, BaseTSD.DWORD_PTR dWORD_PTR) {
            if (n5 == -1 && dWORDByReference == null) {
                dWORDByReference = new WinDef.DWORDByReference();
            }
            if ((object = Ddeml.INSTANCE.DdeClientTransaction((Pointer)object, n2, this.conv, hSZ, n3, n4, n5, dWORDByReference)) == null) {
                throw DdemlException.create(this.client.getLastError());
            }
            if (dWORD_PTR != null) {
                if (n5 != -1) {
                    this.setUserHandle(-1, dWORD_PTR);
                } else {
                    this.setUserHandle(dWORDByReference.getValue().intValue(), dWORD_PTR);
                }
            }
            return object;
        }

        @Override
        public Ddeml.HDDEDATA clientTransaction(Pointer object, int n2, String string, int n3, int n4, int n5, WinDef.DWORDByReference dWORDByReference, BaseTSD.DWORD_PTR dWORD_PTR) {
            Ddeml.HSZ hSZ = null;
            try {
                hSZ = this.client.createStringHandle(string);
                object = this.clientTransaction((Pointer)object, n2, hSZ, n3, n4, n5, dWORDByReference, dWORD_PTR);
                return object;
            }
            finally {
                this.client.freeStringHandle(hSZ);
            }
        }

        @Override
        public void poke(Pointer pointer, int n2, Ddeml.HSZ hSZ, int n3, int n4, WinDef.DWORDByReference dWORDByReference, BaseTSD.DWORD_PTR dWORD_PTR) {
            this.clientTransaction(pointer, n2, hSZ, n3, 16528, n4, dWORDByReference, dWORD_PTR);
        }

        @Override
        public void poke(Pointer pointer, int n2, String string, int n3, int n4, WinDef.DWORDByReference dWORDByReference, BaseTSD.DWORD_PTR dWORD_PTR) {
            Ddeml.HSZ hSZ = null;
            try {
                hSZ = this.client.createStringHandle(string);
                this.poke(pointer, n2, hSZ, n3, n4, dWORDByReference, dWORD_PTR);
                return;
            }
            finally {
                this.client.freeStringHandle(hSZ);
            }
        }

        @Override
        public Ddeml.HDDEDATA request(Ddeml.HSZ hSZ, int n2, int n3, WinDef.DWORDByReference dWORDByReference, BaseTSD.DWORD_PTR dWORD_PTR) {
            return this.clientTransaction(Pointer.NULL, 0, hSZ, n2, 8368, n3, dWORDByReference, dWORD_PTR);
        }

        @Override
        public Ddeml.HDDEDATA request(String object, int n2, int n3, WinDef.DWORDByReference dWORDByReference, BaseTSD.DWORD_PTR dWORD_PTR) {
            Ddeml.HSZ hSZ = null;
            try {
                hSZ = this.client.createStringHandle((String)object);
                object = this.request(hSZ, n2, n3, dWORDByReference, dWORD_PTR);
                return object;
            }
            finally {
                this.client.freeStringHandle(hSZ);
            }
        }

        @Override
        public void execute(String string, int n2, WinDef.DWORDByReference dWORDByReference, BaseTSD.DWORD_PTR dWORD_PTR) {
            Memory memory = new Memory((string.length() << 1) + 2);
            ((Pointer)memory).setWideString(0L, string);
            Memory memory2 = memory;
            this.clientTransaction((Pointer)memory2, (int)memory2.size(), (Ddeml.HSZ)null, 0, 16464, n2, dWORDByReference, dWORD_PTR);
        }

        @Override
        public void advstart(Ddeml.HSZ hSZ, int n2, int n3, WinDef.DWORDByReference dWORDByReference, BaseTSD.DWORD_PTR dWORD_PTR) {
            this.clientTransaction(Pointer.NULL, 0, hSZ, n2, 4144, n3, dWORDByReference, dWORD_PTR);
        }

        @Override
        public void advstart(String string, int n2, int n3, WinDef.DWORDByReference dWORDByReference, BaseTSD.DWORD_PTR dWORD_PTR) {
            Ddeml.HSZ hSZ = null;
            try {
                hSZ = this.client.createStringHandle(string);
                this.advstart(hSZ, n2, n3, dWORDByReference, dWORD_PTR);
                return;
            }
            finally {
                this.client.freeStringHandle(hSZ);
            }
        }

        @Override
        public void advstop(Ddeml.HSZ hSZ, int n2, int n3, WinDef.DWORDByReference dWORDByReference, BaseTSD.DWORD_PTR dWORD_PTR) {
            this.clientTransaction(Pointer.NULL, 0, hSZ, n2, 32832, n3, dWORDByReference, dWORD_PTR);
        }

        @Override
        public void advstop(String string, int n2, int n3, WinDef.DWORDByReference dWORDByReference, BaseTSD.DWORD_PTR dWORD_PTR) {
            Ddeml.HSZ hSZ = null;
            try {
                hSZ = this.client.createStringHandle(string);
                this.advstop(hSZ, n2, n3, dWORDByReference, dWORD_PTR);
                return;
            }
            finally {
                this.client.freeStringHandle(hSZ);
            }
        }

        @Override
        public void impersonateClient() {
            boolean bl = Ddeml.INSTANCE.DdeImpersonateClient(this.conv);
            if (!bl) {
                throw DdemlException.create(this.client.getLastError());
            }
        }

        @Override
        public void close() {
            boolean bl = Ddeml.INSTANCE.DdeDisconnect(this.conv);
            if (!bl) {
                throw DdemlException.create(this.client.getLastError());
            }
        }

        @Override
        public void reconnect() {
            Ddeml.HCONV hCONV = Ddeml.INSTANCE.DdeReconnect(this.conv);
            if (hCONV != null) {
                this.conv = hCONV;
                return;
            }
            throw DdemlException.create(this.client.getLastError());
        }

        @Override
        public boolean enableCallback(int n2) {
            boolean bl = Ddeml.INSTANCE.DdeEnableCallback(this.client.getInstanceIdentitifier(), this.conv, n2);
            if (!bl && n2 == 2) {
                throw DdemlException.create(this.client.getLastError());
            }
            return bl;
        }

        @Override
        public void setUserHandle(int n2, BaseTSD.DWORD_PTR dWORD_PTR) throws DdemlException {
            boolean bl = Ddeml.INSTANCE.DdeSetUserHandle(this.conv, n2, dWORD_PTR);
            n2 = bl ? 1 : 0;
            if (!bl) {
                throw DdemlException.create(this.client.getLastError());
            }
        }

        @Override
        public Ddeml.CONVINFO queryConvInfo(int n2) throws DdemlException {
            Ddeml.CONVINFO cONVINFO = new Ddeml.CONVINFO();
            new Ddeml.CONVINFO().cb = cONVINFO.size();
            cONVINFO.ConvCtxt.cb = cONVINFO.ConvCtxt.size();
            ((Structure)cONVINFO).write();
            n2 = Ddeml.INSTANCE.DdeQueryConvInfo(this.conv, n2, cONVINFO);
            if (n2 == 0) {
                throw DdemlException.create(this.client.getLastError());
            }
            return cONVINFO;
        }
    }

    static class MessageLoopWrapper
    implements InvocationHandler {
        private final Object delegate;
        private final User32Util.MessageLoopThread loopThread;

        public MessageLoopWrapper(User32Util.MessageLoopThread messageLoopThread, Object object) {
            this.loopThread = messageLoopThread;
            this.delegate = object;
        }

        @Override
        public Object invoke(Object object, Method method, Object[] object2) throws Throwable {
            try {
                object = method.invoke(this.delegate, object2);
                object2 = null;
                if (object instanceof IDdeConnection) {
                    object2 = IDdeConnection.class;
                } else if (object instanceof IDdeConnectionList) {
                    object2 = IDdeConnectionList.class;
                } else if (object instanceof IDdeClient) {
                    object2 = IDdeClient.class;
                }
                if (object2 != null && method.getReturnType().isAssignableFrom((Class<?>)object2)) {
                    object = this.wrap(object, (Class)object2);
                }
                return object;
            }
            catch (InvocationTargetException invocationTargetException) {
                object = invocationTargetException;
                object2 = invocationTargetException.getCause();
                if (object2 instanceof Exception) {
                    throw (Exception)object2;
                }
                throw object;
            }
        }

        private <V> V wrap(V object, Class clazz) {
            ClassLoader classLoader = StandaloneDdeClient.class.getClassLoader();
            Class[] classArray = new Class[]{clazz};
            User32Util.MessageLoopThread messageLoopThread = this.loopThread;
            messageLoopThread.getClass();
            object = Proxy.newProxyInstance(classLoader, classArray, (InvocationHandler)new User32Util.MessageLoopThread.Handler(messageLoopThread, object));
            object = Proxy.newProxyInstance(StandaloneDdeClient.class.getClassLoader(), new Class[]{clazz}, (InvocationHandler)new MessageLoopWrapper(this.loopThread, object));
            return object;
        }
    }

    public static class StandaloneDdeClient
    implements IDdeClient,
    Closeable {
        private final User32Util.MessageLoopThread messageLoop = new User32Util.MessageLoopThread();
        private final IDdeClient ddeClient = new DdeClient();
        private final IDdeClient clientDelegate;

        public StandaloneDdeClient() {
            ClassLoader classLoader = StandaloneDdeClient.class.getClassLoader();
            Class[] classArray = new Class[]{IDdeClient.class};
            User32Util.MessageLoopThread messageLoopThread = this.messageLoop;
            messageLoopThread.getClass();
            IDdeClient iDdeClient = (IDdeClient)Proxy.newProxyInstance(classLoader, classArray, (InvocationHandler)new User32Util.MessageLoopThread.Handler(messageLoopThread, this.ddeClient));
            this.clientDelegate = (IDdeClient)Proxy.newProxyInstance(StandaloneDdeClient.class.getClassLoader(), new Class[]{IDdeClient.class}, (InvocationHandler)new MessageLoopWrapper(this.messageLoop, iDdeClient));
            this.messageLoop.setDaemon(true);
            this.messageLoop.start();
        }

        @Override
        public Integer getInstanceIdentitifier() {
            return this.ddeClient.getInstanceIdentitifier();
        }

        @Override
        public void initialize(int n2) throws DdemlException {
            this.clientDelegate.initialize(n2);
        }

        @Override
        public Ddeml.HSZ createStringHandle(String string) throws DdemlException {
            return this.clientDelegate.createStringHandle(string);
        }

        @Override
        public void nameService(Ddeml.HSZ hSZ, int n2) throws DdemlException {
            this.clientDelegate.nameService(hSZ, n2);
        }

        @Override
        public int getLastError() {
            return this.clientDelegate.getLastError();
        }

        @Override
        public IDdeConnection connect(Ddeml.HSZ hSZ, Ddeml.HSZ hSZ2, Ddeml.CONVCONTEXT cONVCONTEXT) {
            return this.clientDelegate.connect(hSZ, hSZ2, cONVCONTEXT);
        }

        @Override
        public String queryString(Ddeml.HSZ hSZ) throws DdemlException {
            return this.clientDelegate.queryString(hSZ);
        }

        @Override
        public Ddeml.HDDEDATA createDataHandle(Pointer pointer, int n2, int n3, Ddeml.HSZ hSZ, int n4, int n5) {
            return this.clientDelegate.createDataHandle(pointer, n2, n3, hSZ, n4, n5);
        }

        @Override
        public void freeDataHandle(Ddeml.HDDEDATA hDDEDATA) {
            this.clientDelegate.freeDataHandle(hDDEDATA);
        }

        @Override
        public Ddeml.HDDEDATA addData(Ddeml.HDDEDATA hDDEDATA, Pointer pointer, int n2, int n3) {
            return this.clientDelegate.addData(hDDEDATA, pointer, n2, n3);
        }

        @Override
        public int getData(Ddeml.HDDEDATA hDDEDATA, Pointer pointer, int n2, int n3) {
            return this.clientDelegate.getData(hDDEDATA, pointer, n2, n3);
        }

        @Override
        public Pointer accessData(Ddeml.HDDEDATA hDDEDATA, WinDef.DWORDByReference dWORDByReference) {
            return this.clientDelegate.accessData(hDDEDATA, dWORDByReference);
        }

        @Override
        public void unaccessData(Ddeml.HDDEDATA hDDEDATA) {
            this.clientDelegate.unaccessData(hDDEDATA);
        }

        @Override
        public void postAdvise(Ddeml.HSZ hSZ, Ddeml.HSZ hSZ2) {
            this.clientDelegate.postAdvise(hSZ, hSZ2);
        }

        @Override
        public void close() throws IOException {
            this.clientDelegate.uninitialize();
            this.messageLoop.exit();
        }

        @Override
        public boolean freeStringHandle(Ddeml.HSZ hSZ) {
            return this.clientDelegate.freeStringHandle(hSZ);
        }

        @Override
        public boolean keepStringHandle(Ddeml.HSZ hSZ) {
            return this.clientDelegate.keepStringHandle(hSZ);
        }

        @Override
        public void abandonTransactions() {
            this.clientDelegate.abandonTransactions();
        }

        @Override
        public IDdeConnectionList connectList(Ddeml.HSZ hSZ, Ddeml.HSZ hSZ2, IDdeConnectionList iDdeConnectionList, Ddeml.CONVCONTEXT cONVCONTEXT) {
            return this.clientDelegate.connectList(hSZ, hSZ2, iDdeConnectionList, cONVCONTEXT);
        }

        @Override
        public boolean enableCallback(int n2) {
            return this.clientDelegate.enableCallback(n2);
        }

        @Override
        public IDdeConnection wrap(Ddeml.HCONV hCONV) {
            return this.clientDelegate.wrap(hCONV);
        }

        @Override
        public IDdeConnection connect(String string, String string2, Ddeml.CONVCONTEXT cONVCONTEXT) {
            return this.clientDelegate.connect(string, string2, cONVCONTEXT);
        }

        @Override
        public boolean uninitialize() {
            return this.clientDelegate.uninitialize();
        }

        @Override
        public void postAdvise(String string, String string2) {
            this.clientDelegate.postAdvise(string, string2);
        }

        @Override
        public IDdeConnectionList connectList(String string, String string2, IDdeConnectionList iDdeConnectionList, Ddeml.CONVCONTEXT cONVCONTEXT) {
            return this.clientDelegate.connectList(string, string2, iDdeConnectionList, cONVCONTEXT);
        }

        @Override
        public void nameService(String string, int n2) throws DdemlException {
            this.clientDelegate.nameService(string, n2);
        }

        @Override
        public void registerAdvstartHandler(AdvstartHandler advstartHandler) {
            this.clientDelegate.registerAdvstartHandler(advstartHandler);
        }

        @Override
        public void unregisterAdvstartHandler(AdvstartHandler advstartHandler) {
            this.clientDelegate.unregisterAdvstartHandler(advstartHandler);
        }

        @Override
        public void registerAdvstopHandler(AdvstopHandler advstopHandler) {
            this.clientDelegate.registerAdvstopHandler(advstopHandler);
        }

        @Override
        public void unregisterAdvstopHandler(AdvstopHandler advstopHandler) {
            this.clientDelegate.unregisterAdvstopHandler(advstopHandler);
        }

        @Override
        public void registerConnectHandler(ConnectHandler connectHandler) {
            this.clientDelegate.registerConnectHandler(connectHandler);
        }

        @Override
        public void unregisterConnectHandler(ConnectHandler connectHandler) {
            this.clientDelegate.unregisterConnectHandler(connectHandler);
        }

        @Override
        public void registerAdvReqHandler(AdvreqHandler advreqHandler) {
            this.clientDelegate.registerAdvReqHandler(advreqHandler);
        }

        @Override
        public void unregisterAdvReqHandler(AdvreqHandler advreqHandler) {
            this.clientDelegate.unregisterAdvReqHandler(advreqHandler);
        }

        @Override
        public void registerRequestHandler(RequestHandler requestHandler) {
            this.clientDelegate.registerRequestHandler(requestHandler);
        }

        @Override
        public void unregisterRequestHandler(RequestHandler requestHandler) {
            this.clientDelegate.unregisterRequestHandler(requestHandler);
        }

        @Override
        public void registerWildconnectHandler(WildconnectHandler wildconnectHandler) {
            this.clientDelegate.registerWildconnectHandler(wildconnectHandler);
        }

        @Override
        public void unregisterWildconnectHandler(WildconnectHandler wildconnectHandler) {
            this.clientDelegate.unregisterWildconnectHandler(wildconnectHandler);
        }

        @Override
        public void registerAdvdataHandler(AdvdataHandler advdataHandler) {
            this.clientDelegate.registerAdvdataHandler(advdataHandler);
        }

        @Override
        public void unregisterAdvdataHandler(AdvdataHandler advdataHandler) {
            this.clientDelegate.unregisterAdvdataHandler(advdataHandler);
        }

        @Override
        public void registerExecuteHandler(ExecuteHandler executeHandler) {
            this.clientDelegate.registerExecuteHandler(executeHandler);
        }

        @Override
        public void unregisterExecuteHandler(ExecuteHandler executeHandler) {
            this.clientDelegate.unregisterExecuteHandler(executeHandler);
        }

        @Override
        public void registerPokeHandler(PokeHandler pokeHandler) {
            this.clientDelegate.registerPokeHandler(pokeHandler);
        }

        @Override
        public void unregisterPokeHandler(PokeHandler pokeHandler) {
            this.clientDelegate.unregisterPokeHandler(pokeHandler);
        }

        @Override
        public void registerConnectConfirmHandler(ConnectConfirmHandler connectConfirmHandler) {
            this.clientDelegate.registerConnectConfirmHandler(connectConfirmHandler);
        }

        @Override
        public void unregisterConnectConfirmHandler(ConnectConfirmHandler connectConfirmHandler) {
            this.clientDelegate.unregisterConnectConfirmHandler(connectConfirmHandler);
        }

        @Override
        public void registerDisconnectHandler(DisconnectHandler disconnectHandler) {
            this.clientDelegate.registerDisconnectHandler(disconnectHandler);
        }

        @Override
        public void unregisterDisconnectHandler(DisconnectHandler disconnectHandler) {
            this.clientDelegate.unregisterDisconnectHandler(disconnectHandler);
        }

        @Override
        public void registerErrorHandler(ErrorHandler errorHandler) {
            this.clientDelegate.registerErrorHandler(errorHandler);
        }

        @Override
        public void unregisterErrorHandler(ErrorHandler errorHandler) {
            this.clientDelegate.unregisterErrorHandler(errorHandler);
        }

        @Override
        public void registerRegisterHandler(RegisterHandler registerHandler) {
            this.clientDelegate.registerRegisterHandler(registerHandler);
        }

        @Override
        public void unregisterRegisterHandler(RegisterHandler registerHandler) {
            this.clientDelegate.unregisterRegisterHandler(registerHandler);
        }

        @Override
        public void registerXactCompleteHandler(XactCompleteHandler xactCompleteHandler) {
            this.clientDelegate.registerXactCompleteHandler(xactCompleteHandler);
        }

        @Override
        public void unregisterXactCompleteHandler(XactCompleteHandler xactCompleteHandler) {
            this.clientDelegate.unregisterXactCompleteHandler(xactCompleteHandler);
        }

        @Override
        public void registerUnregisterHandler(UnregisterHandler unregisterHandler) {
            this.clientDelegate.registerUnregisterHandler(unregisterHandler);
        }

        @Override
        public void unregisterUnregisterHandler(UnregisterHandler unregisterHandler) {
            this.clientDelegate.unregisterUnregisterHandler(unregisterHandler);
        }

        @Override
        public void registerMonitorHandler(MonitorHandler monitorHandler) {
            this.clientDelegate.registerMonitorHandler(monitorHandler);
        }

        @Override
        public void unregisterMonitorHandler(MonitorHandler monitorHandler) {
            this.clientDelegate.unregisterMonitorHandler(monitorHandler);
        }
    }
}

