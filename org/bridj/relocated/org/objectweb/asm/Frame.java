/*
 * Decompiled with CFR 0.152.
 */
package org.bridj.relocated.org.objectweb.asm;

import org.bridj.relocated.org.objectweb.asm.ClassWriter;
import org.bridj.relocated.org.objectweb.asm.Item;
import org.bridj.relocated.org.objectweb.asm.Label;
import org.bridj.relocated.org.objectweb.asm.Type;

final class Frame {
    static final int[] a;
    Label b;
    int[] c;
    int[] d;
    private int[] e;
    private int[] f;
    private int g;
    private int h;
    private int[] i;

    Frame() {
    }

    private int a(int n2) {
        if (this.e == null || n2 >= this.e.length) {
            return 0x2000000 | n2;
        }
        int n3 = this.e[n2];
        if (n3 == 0) {
            n3 = this.e[n2] = 0x2000000 | n2;
        }
        return n3;
    }

    private void a(int n2, int n3) {
        int n4;
        if (this.e == null) {
            this.e = new int[10];
        }
        if (n2 >= (n4 = this.e.length)) {
            int[] nArray = new int[Math.max(n2 + 1, 2 * n4)];
            System.arraycopy(this.e, 0, nArray, 0, n4);
            this.e = nArray;
        }
        this.e[n2] = n3;
    }

    private void b(int n2) {
        int n3;
        if (this.f == null) {
            this.f = new int[10];
        }
        if (this.g >= (n3 = this.f.length)) {
            int[] nArray = new int[Math.max(this.g + 1, 2 * n3)];
            System.arraycopy(this.f, 0, nArray, 0, n3);
            this.f = nArray;
        }
        this.f[this.g++] = n2;
        int n4 = this.b.f + this.g;
        if (n4 > this.b.g) {
            this.b.g = n4;
        }
    }

    private void a(ClassWriter classWriter, String string) {
        int n2 = Frame.b(classWriter, string);
        if (n2 != 0) {
            this.b(n2);
            if (n2 == 0x1000004 || n2 == 0x1000003) {
                this.b(0x1000000);
            }
        }
    }

    private static int b(ClassWriter classWriter, String string) {
        int n2;
        int n3 = string.charAt(0) == '(' ? string.indexOf(41) + 1 : 0;
        switch (string.charAt(n3)) {
            case 'V': {
                return 0;
            }
            case 'B': 
            case 'C': 
            case 'I': 
            case 'S': 
            case 'Z': {
                return 0x1000001;
            }
            case 'F': {
                return 0x1000002;
            }
            case 'J': {
                return 0x1000004;
            }
            case 'D': {
                return 0x1000003;
            }
            case 'L': {
                string = string.substring(n3 + 1, string.length() - 1);
                return 0x1700000 | classWriter.c(string);
            }
        }
        int n4 = n3 + 1;
        while (string.charAt(n4) == '[') {
            ++n4;
        }
        switch (string.charAt(n4)) {
            case 'Z': {
                n2 = 0x1000009;
                break;
            }
            case 'C': {
                n2 = 0x100000B;
                break;
            }
            case 'B': {
                n2 = 0x100000A;
                break;
            }
            case 'S': {
                n2 = 0x100000C;
                break;
            }
            case 'I': {
                n2 = 0x1000001;
                break;
            }
            case 'F': {
                n2 = 0x1000002;
                break;
            }
            case 'J': {
                n2 = 0x1000004;
                break;
            }
            case 'D': {
                n2 = 0x1000003;
                break;
            }
            default: {
                string = string.substring(n4 + 1, string.length() - 1);
                n2 = 0x1700000 | classWriter.c(string);
            }
        }
        return n4 - n3 << 28 | n2;
    }

    private int a() {
        if (this.g > 0) {
            return this.f[--this.g];
        }
        return 0x3000000 | -(--this.b.f);
    }

    private void c(int n2) {
        if (this.g >= n2) {
            this.g -= n2;
            return;
        }
        this.b.f -= n2 - this.g;
        this.g = 0;
    }

    private void a(String string) {
        char c2 = string.charAt(0);
        if (c2 == '(') {
            this.c((Type.getArgumentsAndReturnSizes(string) >> 2) - 1);
            return;
        }
        if (c2 == 'J' || c2 == 'D') {
            this.c(2);
            return;
        }
        this.c(1);
    }

    private void d(int n2) {
        int n3;
        if (this.i == null) {
            this.i = new int[2];
        }
        if (this.h >= (n3 = this.i.length)) {
            int[] nArray = new int[Math.max(this.h + 1, 2 * n3)];
            System.arraycopy(this.i, 0, nArray, 0, n3);
            this.i = nArray;
        }
        this.i[this.h++] = n2;
    }

    private int a(ClassWriter classWriter, int n2) {
        int n3;
        if (n2 == 0x1000006) {
            ClassWriter classWriter2 = classWriter;
            n3 = 0x1700000 | classWriter2.c(classWriter2.I);
        } else if ((n2 & 0xFFF00000) == 0x1800000) {
            String string = classWriter.H[n2 & 0xFFFFF].g;
            n3 = 0x1700000 | classWriter.c(string);
        } else {
            return n2;
        }
        for (int i2 = 0; i2 < this.h; ++i2) {
            int n4 = this.i[i2];
            int n5 = n4 & 0xF0000000;
            int n6 = n4 & 0xF000000;
            if (n6 == 0x2000000) {
                n4 = n5 + this.c[n4 & 0x7FFFFF];
            } else if (n6 == 0x3000000) {
                n4 = n5 + this.d[this.d.length - (n4 & 0x7FFFFF)];
            }
            if (n2 != n4) continue;
            return n3;
        }
        return n2;
    }

    final void a(ClassWriter classWriter, int n2, Type[] typeArray, int n3) {
        this.c = new int[n3];
        this.d = new int[0];
        int n4 = 0;
        if ((n2 & 8) == 0) {
            if ((n2 & 0x40000) == 0) {
                ++n4;
                ClassWriter classWriter2 = classWriter;
                this.c[0] = 0x1700000 | classWriter2.c(classWriter2.I);
            } else {
                ++n4;
                this.c[0] = 0x1000006;
            }
        }
        for (n2 = 0; n2 < typeArray.length; ++n2) {
            int n5 = Frame.b(classWriter, typeArray[n2].getDescriptor());
            this.c[n4++] = n5;
            if (n5 != 0x1000004 && n5 != 0x1000003) continue;
            this.c[n4++] = 0x1000000;
        }
        while (n4 < n3) {
            this.c[n4++] = 0x1000000;
        }
    }

    final void a(int n2, int n3, ClassWriter classWriter, Item item) {
        switch (n2) {
            case 0: 
            case 116: 
            case 117: 
            case 118: 
            case 119: 
            case 145: 
            case 146: 
            case 147: 
            case 167: 
            case 177: {
                return;
            }
            case 1: {
                this.b(0x1000005);
                return;
            }
            case 2: 
            case 3: 
            case 4: 
            case 5: 
            case 6: 
            case 7: 
            case 8: 
            case 16: 
            case 17: 
            case 21: {
                this.b(0x1000001);
                return;
            }
            case 9: 
            case 10: 
            case 22: {
                this.b(0x1000004);
                this.b(0x1000000);
                return;
            }
            case 11: 
            case 12: 
            case 13: 
            case 23: {
                this.b(0x1000002);
                return;
            }
            case 14: 
            case 15: 
            case 24: {
                this.b(0x1000003);
                this.b(0x1000000);
                return;
            }
            case 18: {
                switch (item.b) {
                    case 3: {
                        this.b(0x1000001);
                        return;
                    }
                    case 5: {
                        this.b(0x1000004);
                        this.b(0x1000000);
                        return;
                    }
                    case 4: {
                        this.b(0x1000002);
                        return;
                    }
                    case 6: {
                        this.b(0x1000003);
                        this.b(0x1000000);
                        return;
                    }
                    case 7: {
                        this.b(0x1700000 | classWriter.c("java/lang/Class"));
                        return;
                    }
                    case 8: {
                        this.b(0x1700000 | classWriter.c("java/lang/String"));
                        return;
                    }
                    case 16: {
                        this.b(0x1700000 | classWriter.c("java/lang/invoke/MethodType"));
                        return;
                    }
                }
                this.b(0x1700000 | classWriter.c("java/lang/invoke/MethodHandle"));
                return;
            }
            case 25: {
                Frame frame = this;
                frame.b(frame.a(n3));
                return;
            }
            case 46: 
            case 51: 
            case 52: 
            case 53: {
                this.c(2);
                this.b(0x1000001);
                return;
            }
            case 47: 
            case 143: {
                this.c(2);
                this.b(0x1000004);
                this.b(0x1000000);
                return;
            }
            case 48: {
                this.c(2);
                this.b(0x1000002);
                return;
            }
            case 49: 
            case 138: {
                this.c(2);
                this.b(0x1000003);
                this.b(0x1000000);
                return;
            }
            case 50: {
                this.c(1);
                int n4 = this.a();
                this.b(n4 + -268435456);
                return;
            }
            case 54: 
            case 56: 
            case 58: {
                int n5 = this.a();
                this.a(n3, n5);
                if (n3 <= 0) break;
                n2 = this.a(n3 - 1);
                if (n2 == 0x1000004 || n2 == 0x1000003) {
                    this.a(n3 - 1, 0x1000000);
                    return;
                }
                if ((n2 & 0xF000000) == 0x1000000) break;
                this.a(n3 - 1, n2 | 0x800000);
                return;
            }
            case 55: 
            case 57: {
                this.c(1);
                int n6 = this.a();
                this.a(n3, n6);
                this.a(n3 + 1, 0x1000000);
                if (n3 <= 0) break;
                n2 = this.a(n3 - 1);
                if (n2 == 0x1000004 || n2 == 0x1000003) {
                    this.a(n3 - 1, 0x1000000);
                    return;
                }
                if ((n2 & 0xF000000) == 0x1000000) break;
                this.a(n3 - 1, n2 | 0x800000);
                return;
            }
            case 79: 
            case 81: 
            case 83: 
            case 84: 
            case 85: 
            case 86: {
                this.c(3);
                return;
            }
            case 80: 
            case 82: {
                this.c(4);
                return;
            }
            case 87: 
            case 153: 
            case 154: 
            case 155: 
            case 156: 
            case 157: 
            case 158: 
            case 170: 
            case 171: 
            case 172: 
            case 174: 
            case 176: 
            case 191: 
            case 194: 
            case 195: 
            case 198: 
            case 199: {
                this.c(1);
                return;
            }
            case 88: 
            case 159: 
            case 160: 
            case 161: 
            case 162: 
            case 163: 
            case 164: 
            case 165: 
            case 166: 
            case 173: 
            case 175: {
                this.c(2);
                return;
            }
            case 89: {
                int n7 = this.a();
                this.b(n7);
                this.b(n7);
                return;
            }
            case 90: {
                int n8 = this.a();
                n2 = this.a();
                this.b(n8);
                this.b(n2);
                this.b(n8);
                return;
            }
            case 91: {
                int n9 = this.a();
                n2 = this.a();
                n3 = this.a();
                this.b(n9);
                this.b(n3);
                this.b(n2);
                this.b(n9);
                return;
            }
            case 92: {
                int n10 = this.a();
                n2 = this.a();
                this.b(n2);
                this.b(n10);
                this.b(n2);
                this.b(n10);
                return;
            }
            case 93: {
                int n11 = this.a();
                n2 = this.a();
                n3 = this.a();
                this.b(n2);
                this.b(n11);
                this.b(n3);
                this.b(n2);
                this.b(n11);
                return;
            }
            case 94: {
                int n12 = this.a();
                n2 = this.a();
                n3 = this.a();
                int n13 = this.a();
                this.b(n2);
                this.b(n12);
                this.b(n13);
                this.b(n3);
                this.b(n2);
                this.b(n12);
                return;
            }
            case 95: {
                int n14 = this.a();
                n2 = this.a();
                this.b(n14);
                this.b(n2);
                return;
            }
            case 96: 
            case 100: 
            case 104: 
            case 108: 
            case 112: 
            case 120: 
            case 122: 
            case 124: 
            case 126: 
            case 128: 
            case 130: 
            case 136: 
            case 142: 
            case 149: 
            case 150: {
                this.c(2);
                this.b(0x1000001);
                return;
            }
            case 97: 
            case 101: 
            case 105: 
            case 109: 
            case 113: 
            case 127: 
            case 129: 
            case 131: {
                this.c(4);
                this.b(0x1000004);
                this.b(0x1000000);
                return;
            }
            case 98: 
            case 102: 
            case 106: 
            case 110: 
            case 114: 
            case 137: 
            case 144: {
                this.c(2);
                this.b(0x1000002);
                return;
            }
            case 99: 
            case 103: 
            case 107: 
            case 111: 
            case 115: {
                this.c(4);
                this.b(0x1000003);
                this.b(0x1000000);
                return;
            }
            case 121: 
            case 123: 
            case 125: {
                this.c(3);
                this.b(0x1000004);
                this.b(0x1000000);
                return;
            }
            case 132: {
                this.a(n3, 0x1000001);
                return;
            }
            case 133: 
            case 140: {
                this.c(1);
                this.b(0x1000004);
                this.b(0x1000000);
                return;
            }
            case 134: {
                this.c(1);
                this.b(0x1000002);
                return;
            }
            case 135: 
            case 141: {
                this.c(1);
                this.b(0x1000003);
                this.b(0x1000000);
                return;
            }
            case 139: 
            case 190: 
            case 193: {
                this.c(1);
                this.b(0x1000001);
                return;
            }
            case 148: 
            case 151: 
            case 152: {
                this.c(4);
                this.b(0x1000001);
                return;
            }
            case 168: 
            case 169: {
                throw new RuntimeException("JSR/RET are not supported with computeFrames option");
            }
            case 178: {
                this.a(classWriter, item.i);
                return;
            }
            case 179: {
                this.a(item.i);
                return;
            }
            case 180: {
                this.c(1);
                this.a(classWriter, item.i);
                return;
            }
            case 181: {
                this.a(item.i);
                this.a();
                return;
            }
            case 182: 
            case 183: 
            case 184: 
            case 185: {
                this.a(item.i);
                if (n2 != 184) {
                    int n15 = this.a();
                    if (n2 == 183 && item.h.charAt(0) == '<') {
                        this.d(n15);
                    }
                }
                this.a(classWriter, item.i);
                return;
            }
            case 186: {
                this.a(item.h);
                this.a(classWriter, item.h);
                return;
            }
            case 187: {
                this.b(0x1800000 | classWriter.a(item.g, n3));
                return;
            }
            case 188: {
                this.a();
                switch (n3) {
                    case 4: {
                        this.b(0x11000009);
                        return;
                    }
                    case 5: {
                        this.b(0x1100000B);
                        return;
                    }
                    case 8: {
                        this.b(0x1100000A);
                        return;
                    }
                    case 9: {
                        this.b(0x1100000C);
                        return;
                    }
                    case 10: {
                        this.b(0x11000001);
                        return;
                    }
                    case 6: {
                        this.b(0x11000002);
                        return;
                    }
                    case 7: {
                        this.b(0x11000003);
                        return;
                    }
                }
                this.b(0x11000004);
                return;
            }
            case 189: {
                String string = item.g;
                this.a();
                if (string.charAt(0) == '[') {
                    this.a(classWriter, "[" + string);
                    return;
                }
                this.b(0x11700000 | classWriter.c(string));
                return;
            }
            case 192: {
                String string = item.g;
                this.a();
                if (string.charAt(0) == '[') {
                    this.a(classWriter, string);
                    return;
                }
                this.b(0x1700000 | classWriter.c(string));
                return;
            }
            default: {
                this.c(n3);
                this.a(classWriter, item.g);
            }
        }
    }

    final boolean a(ClassWriter classWriter, Frame frame, int n2) {
        int n3;
        int n4;
        int n5;
        int n6;
        boolean bl = false;
        int n7 = this.c.length;
        int n8 = this.d.length;
        if (frame.c == null) {
            frame.c = new int[n7];
            bl = true;
        }
        for (n6 = 0; n6 < n7; ++n6) {
            if (this.e != null && n6 < this.e.length && (n5 = this.e[n6]) != 0) {
                n4 = n5 & 0xF0000000;
                n3 = n5 & 0xF000000;
                if (n3 == 0x1000000) {
                    n4 = n5;
                } else {
                    n4 = n3 == 0x2000000 ? (n4 += this.c[n5 & 0x7FFFFF]) : (n4 += this.d[n8 - (n5 & 0x7FFFFF)]);
                    if ((n5 & 0x800000) != 0 && (n4 == 0x1000004 || n4 == 0x1000003)) {
                        n4 = 0x1000000;
                    }
                }
            } else {
                n4 = this.c[n6];
            }
            if (this.i != null) {
                n4 = this.a(classWriter, n4);
            }
            bl |= Frame.a(classWriter, n4, frame.c, n6);
        }
        if (n2 > 0) {
            for (n6 = 0; n6 < n7; ++n6) {
                n4 = this.c[n6];
                bl |= Frame.a(classWriter, n4, frame.c, n6);
            }
            if (frame.d == null) {
                frame.d = new int[1];
                bl = true;
            }
            return bl |= Frame.a(classWriter, n2, frame.d, 0);
        }
        n2 = this.d.length + this.b.f;
        if (frame.d == null) {
            frame.d = new int[n2 + this.g];
            bl = true;
        }
        for (n6 = 0; n6 < n2; ++n6) {
            n4 = this.d[n6];
            if (this.i != null) {
                n4 = this.a(classWriter, n4);
            }
            bl |= Frame.a(classWriter, n4, frame.d, n6);
        }
        for (n6 = 0; n6 < this.g; ++n6) {
            n5 = this.f[n6];
            n4 = n5 & 0xF0000000;
            n3 = n5 & 0xF000000;
            if (n3 == 0x1000000) {
                n4 = n5;
            } else {
                n4 = n3 == 0x2000000 ? (n4 += this.c[n5 & 0x7FFFFF]) : (n4 += this.d[n8 - (n5 & 0x7FFFFF)]);
                if ((n5 & 0x800000) != 0 && (n4 == 0x1000004 || n4 == 0x1000003)) {
                    n4 = 0x1000000;
                }
            }
            if (this.i != null) {
                n4 = this.a(classWriter, n4);
            }
            bl |= Frame.a(classWriter, n4, frame.d, n2 + n6);
        }
        return bl;
    }

    private static boolean a(ClassWriter classWriter, int n2, int[] nArray, int n3) {
        int n4;
        int n5 = nArray[n3];
        if (n5 == n2) {
            return false;
        }
        if ((n2 & 0xFFFFFFF) == 0x1000005) {
            if (n5 == 0x1000005) {
                return false;
            }
            n2 = 0x1000005;
        }
        if (n5 == 0) {
            nArray[n3] = n2;
            return true;
        }
        if ((n5 & 0xFF00000) == 0x1700000 || (n5 & 0xF0000000) != 0) {
            if (n2 == 0x1000005) {
                return false;
            }
            n4 = (n2 & 0xFFF00000) == (n5 & 0xFFF00000) ? ((n5 & 0xFF00000) == 0x1700000 ? n2 & 0xF0000000 | 0x1700000 | classWriter.a(n2 & 0xFFFFF, n5 & 0xFFFFF) : 0x1700000 | classWriter.c("java/lang/Object")) : ((n2 & 0xFF00000) == 0x1700000 || (n2 & 0xF0000000) != 0 ? 0x1700000 | classWriter.c("java/lang/Object") : 0x1000000);
        } else {
            n4 = n5 == 0x1000005 ? ((n2 & 0xFF00000) == 0x1700000 || (n2 & 0xF0000000) != 0 ? n2 : 0x1000000) : 0x1000000;
        }
        if (n5 != n4) {
            nArray[n3] = n4;
            return true;
        }
        return false;
    }

    static {
        int[] nArray = new int[202];
        String string = "EFFFFFFFFGGFFFGGFFFEEFGFGFEEEEEEEEEEEEEEEEEEEEDEDEDDDDDCDCDEEEEEEEEEEEEEEEEEEEEBABABBBBDCFFFGGGEDCDCDCDCDCDCDCDCDCDCEEEEDDDDDDDCDCDCEFEFDDEEFFDEDEEEBDDBBDDDDDDCCCCCCCCEFEDDDCDCDEEEEEEEEEEFEEEEEEDDEEDDEE";
        for (int i2 = 0; i2 < nArray.length; ++i2) {
            nArray[i2] = string.charAt(i2) - 69;
        }
        a = nArray;
    }
}

