/*
 * Decompiled with CFR 0.152.
 */
package org.bridj;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bridj.NativeLibrary;
import org.bridj.demangling.Demangler;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class HeadersReconstructor {
    public static void reconstructHeaders(Iterable<NativeLibrary> iterator2, PrintWriter printWriter) {
        ArrayList<Demangler.MemberRef> arrayList = new ArrayList<Demangler.MemberRef>();
        HashMap<Demangler.TypeRef, ArrayList<Demangler.MemberRef>> hashMap = new HashMap<Demangler.TypeRef, ArrayList<Demangler.MemberRef>>();
        iterator2 = iterator2.iterator();
        while (iterator2.hasNext()) {
            NativeLibrary nativeLibrary = (NativeLibrary)iterator2.next();
            for (Demangler.Symbol symbol : nativeLibrary.getSymbols()) {
                Demangler.MemberRef memberRef = symbol.getParsedRef();
                if (memberRef == null) continue;
                Object object = memberRef.getEnclosingType();
                if (object == null) {
                    arrayList.add(memberRef);
                    continue;
                }
                ArrayList<Demangler.MemberRef> arrayList2 = (ArrayList<Demangler.MemberRef>)hashMap.get(object);
                if (arrayList2 == null) {
                    arrayList2 = new ArrayList<Demangler.MemberRef>();
                    hashMap.put((Demangler.TypeRef)object, arrayList2);
                }
                arrayList2.add(memberRef);
            }
        }
        for (Demangler.TypeRef typeRef : hashMap.keySet()) {
            printWriter.println("class " + typeRef + ";");
        }
        for (Demangler.MemberRef memberRef : arrayList) {
            printWriter.println(memberRef + ";");
        }
        for (Map.Entry entry : hashMap.entrySet()) {
            Demangler.TypeRef typeRef = (Demangler.TypeRef)entry.getKey();
            List list2 = (List)entry.getValue();
            printWriter.println("class " + typeRef + " \n{");
            for (Object object : list2) {
                printWriter.println("\t" + object + ";");
            }
            printWriter.println("}");
        }
    }
}

