/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna.platform.win32.COM.tlb;

import com.sun.jna.platform.win32.COM.TypeLibUtil;
import com.sun.jna.platform.win32.COM.tlb.imp.TlbBase;
import com.sun.jna.platform.win32.COM.tlb.imp.TlbCmdlineArgs;
import com.sun.jna.platform.win32.COM.tlb.imp.TlbCoClass;
import com.sun.jna.platform.win32.COM.tlb.imp.TlbConst;
import com.sun.jna.platform.win32.COM.tlb.imp.TlbDispInterface;
import com.sun.jna.platform.win32.COM.tlb.imp.TlbEnum;
import com.sun.jna.platform.win32.COM.tlb.imp.TlbInterface;
import com.sun.jna.platform.win32.OaIdl;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;

public class TlbImp
implements TlbConst {
    private TypeLibUtil typeLibUtil;
    private File comRootDir;
    private File outputDir;
    private TlbCmdlineArgs cmdlineArgs;

    public static void main(String[] stringArray) {
        new TlbImp(stringArray);
    }

    public TlbImp(String[] object) {
        this.cmdlineArgs = new TlbCmdlineArgs((String[])object);
        if (this.cmdlineArgs.isTlbId()) {
            object = this.cmdlineArgs.getRequiredParam("tlb.id");
            int n2 = this.cmdlineArgs.getIntParam("tlb.major.version");
            int n3 = this.cmdlineArgs.getIntParam("tlb.minor.version");
            this.typeLibUtil = new TypeLibUtil((String)object, n2, n3);
            this.startCOM2Java();
            return;
        }
        if (this.cmdlineArgs.isTlbFile()) {
            object = this.cmdlineArgs.getRequiredParam("tlb.file");
            this.typeLibUtil = new TypeLibUtil((String)object);
            this.startCOM2Java();
            return;
        }
        this.cmdlineArgs.showCmdHelp();
    }

    public void startCOM2Java() {
        try {
            this.createDir();
            String string = this.cmdlineArgs.getBindingMode();
            int n2 = this.typeLibUtil.getTypeInfoCount();
            for (int i2 = 0; i2 < n2; ++i2) {
                OaIdl.TYPEKIND tYPEKIND = this.typeLibUtil.getTypeInfoType(i2);
                if (tYPEKIND.value == 0) {
                    this.createCOMEnum(i2, this.getPackageName(), this.typeLibUtil);
                    continue;
                }
                if (tYPEKIND.value == 1) {
                    TlbImp.logInfo("'TKIND_RECORD' objects are currently not supported!");
                    continue;
                }
                if (tYPEKIND.value == 2) {
                    TlbImp.logInfo("'TKIND_MODULE' objects are currently not supported!");
                    continue;
                }
                if (tYPEKIND.value == 3) {
                    this.createCOMInterface(i2, this.getPackageName(), this.typeLibUtil);
                    continue;
                }
                if (tYPEKIND.value == 4) {
                    this.createCOMDispInterface(i2, this.getPackageName(), this.typeLibUtil);
                    continue;
                }
                if (tYPEKIND.value == 5) {
                    this.createCOMCoClass(i2, this.getPackageName(), this.typeLibUtil, string);
                    continue;
                }
                if (tYPEKIND.value == 6) {
                    TlbImp.logInfo("'TKIND_ALIAS' objects are currently not supported!");
                    continue;
                }
                if (tYPEKIND.value != 7) continue;
                TlbImp.logInfo("'TKIND_UNION' objects are currently not supported!");
            }
            TlbImp.logInfo(n2 + " files sucessfully written to: " + this.comRootDir.toString());
            return;
        }
        catch (Exception exception) {
            Exception exception2 = exception;
            exception.printStackTrace();
            return;
        }
    }

    private void createDir() throws FileNotFoundException {
        String string = this.cmdlineArgs.getParam("output.dir");
        String string2 = "_jnaCOM_" + System.currentTimeMillis() + "\\myPackage\\" + this.typeLibUtil.getName().toLowerCase() + "\\";
        if (string != null) {
            this.comRootDir = new File(string + "\\" + string2);
        } else {
            string = System.getProperty("java.io.tmpdir");
            this.comRootDir = new File(string + "\\" + string2);
        }
        if (this.comRootDir.exists()) {
            this.comRootDir.delete();
        }
        if (this.comRootDir.mkdirs()) {
            TlbImp.logInfo("Output directory sucessfully created.");
            return;
        }
        throw new FileNotFoundException("Output directory NOT sucessfully created to: " + this.comRootDir.toString());
    }

    private String getPackageName() {
        return "myPackage." + this.typeLibUtil.getName().toLowerCase();
    }

    private void writeTextFile(String object, String string) throws IOException {
        object = this.comRootDir + File.separator + (String)object;
        object = new BufferedOutputStream(new FileOutputStream((String)object));
        ((FilterOutputStream)object).write(string.getBytes());
        ((FilterOutputStream)object).close();
    }

    private void writeTlbClass(TlbBase tlbBase) throws IOException {
        StringBuffer stringBuffer = tlbBase.getClassBuffer();
        this.writeTextFile(tlbBase.getFilename(), stringBuffer.toString());
    }

    private void createCOMEnum(int n2, String string, TypeLibUtil typeLibUtil) throws IOException {
        TlbEnum tlbEnum = new TlbEnum(n2, string, typeLibUtil);
        this.writeTlbClass(tlbEnum);
    }

    private void createCOMInterface(int n2, String string, TypeLibUtil typeLibUtil) throws IOException {
        TlbInterface tlbInterface = new TlbInterface(n2, string, typeLibUtil);
        this.writeTlbClass(tlbInterface);
    }

    private void createCOMDispInterface(int n2, String string, TypeLibUtil typeLibUtil) throws IOException {
        TlbDispInterface tlbDispInterface = new TlbDispInterface(n2, string, typeLibUtil);
        this.writeTlbClass(tlbDispInterface);
    }

    private void createCOMCoClass(int n2, String string, TypeLibUtil typeLibUtil, String string2) throws IOException {
        TlbCoClass tlbCoClass = new TlbCoClass(n2, this.getPackageName(), typeLibUtil, string2);
        this.writeTlbClass(tlbCoClass);
    }

    public static void logInfo(String string) {
        System.out.println(string);
    }
}

