/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna.platform.win32.COM.tlb.imp;

import com.sun.jna.platform.win32.COM.TypeInfoUtil;
import com.sun.jna.platform.win32.COM.TypeLibUtil;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class TlbBase {
    public static final String CR = "\n";
    public static final String CRCR = "\n\n";
    public static final String TAB = "\t";
    public static final String TABTAB = "\t\t";
    protected TypeLibUtil typeLibUtil;
    protected TypeInfoUtil typeInfoUtil;
    protected int index;
    protected StringBuffer templateBuffer;
    protected StringBuffer classBuffer;
    protected String content = "";
    protected String filename = "DefaultFilename";
    protected String name = "DefaultName";
    public static String[] IUNKNOWN_METHODS = new String[]{"QueryInterface", "AddRef", "Release"};
    public static String[] IDISPATCH_METHODS = new String[]{"GetTypeInfoCount", "GetTypeInfo", "GetIDsOfNames", "Invoke"};
    protected String bindingMode = "dispid";

    public TlbBase(int n2, TypeLibUtil typeLibUtil, TypeInfoUtil typeInfoUtil) {
        this(n2, typeLibUtil, typeInfoUtil, "dispid");
    }

    public TlbBase(int n2, TypeLibUtil typeLibUtil, TypeInfoUtil typeInfoUtil, String string) {
        this.index = n2;
        this.typeLibUtil = typeLibUtil;
        this.typeInfoUtil = typeInfoUtil;
        this.bindingMode = string;
        Object object = this.getClassTemplate();
        try {
            this.readTemplateFile((String)object);
            this.classBuffer = this.templateBuffer;
            return;
        }
        catch (IOException iOException) {
            object = iOException;
            iOException.printStackTrace();
            return;
        }
    }

    public void logError(String string) {
        this.log("ERROR", string);
    }

    public void logInfo(String string) {
        this.log("INFO", string);
    }

    public StringBuffer getClassBuffer() {
        return this.classBuffer;
    }

    public void createContent(String string) {
        this.replaceVariable("content", string);
    }

    public void setFilename(String string) {
        if (!string.endsWith("java")) {
            string = string + ".java";
        }
        this.filename = string;
    }

    public String getFilename() {
        return this.filename;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String string) {
        this.name = string;
    }

    protected void log(String string, String string2) {
        string = string + " " + this.getTime() + " : " + string2;
        System.out.println(string);
    }

    private String getTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        return simpleDateFormat.format(new Date());
    }

    protected abstract String getClassTemplate();

    protected void readTemplateFile(String object) throws IOException {
        this.templateBuffer = new StringBuffer();
        BufferedReader bufferedReader = null;
        try {
            object = this.getClass().getClassLoader().getResourceAsStream((String)object);
            bufferedReader = new BufferedReader(new InputStreamReader((InputStream)object));
            while ((object = bufferedReader.readLine()) != null) {
                this.templateBuffer.append((String)object + CR);
            }
        }
        catch (Throwable throwable) {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            throw throwable;
        }
        bufferedReader.close();
    }

    protected void replaceVariable(String object, String string) {
        if (string == null) {
            string = "";
        }
        object = Pattern.compile("\\$\\{" + (String)object + "\\}");
        object = ((Pattern)object).matcher(this.classBuffer);
        String string2 = "";
        while (((Matcher)object).find()) {
            string2 = ((Matcher)object).replaceAll(string);
        }
        if (string2.length() > 0) {
            this.classBuffer = new StringBuffer(string2);
        }
    }

    protected void createPackageName(String string) {
        this.replaceVariable("packagename", string);
    }

    protected void createClassName(String string) {
        this.replaceVariable("classname", string);
    }

    protected boolean isReservedMethod(String string) {
        int n2;
        for (n2 = 0; n2 < IUNKNOWN_METHODS.length; ++n2) {
            if (!IUNKNOWN_METHODS[n2].equalsIgnoreCase(string)) continue;
            return true;
        }
        for (n2 = 0; n2 < IDISPATCH_METHODS.length; ++n2) {
            if (!IDISPATCH_METHODS[n2].equalsIgnoreCase(string)) continue;
            return true;
        }
        return false;
    }

    protected boolean isVTableMode() {
        return this.bindingMode.equalsIgnoreCase("vtable");
    }

    protected boolean isDispIdMode() {
        return this.bindingMode.equalsIgnoreCase("dispid");
    }
}

