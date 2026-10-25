/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

class ELFAnalyser {
    private static final byte[] ELF_MAGIC = new byte[]{127, 69, 76, 70};
    private static final int EF_ARM_ABI_FLOAT_HARD = 1024;
    private static final int EF_ARM_ABI_FLOAT_SOFT = 512;
    private static final int EI_DATA_BIG_ENDIAN = 2;
    private static final int E_MACHINE_ARM = 40;
    private static final int EI_CLASS_64BIT = 2;
    private final String filename;
    private boolean ELF = false;
    private boolean _64Bit = false;
    private boolean bigEndian = false;
    private boolean armHardFloatFlag = false;
    private boolean armSoftFloatFlag = false;
    private boolean armEabiAapcsVfp = false;
    private boolean arm = false;

    public static ELFAnalyser analyse(String object) throws IOException {
        object = new ELFAnalyser((String)object);
        ((ELFAnalyser)object).runDetection();
        return object;
    }

    public boolean isELF() {
        return this.ELF;
    }

    public boolean is64Bit() {
        return this._64Bit;
    }

    public boolean isBigEndian() {
        return this.bigEndian;
    }

    public String getFilename() {
        return this.filename;
    }

    public boolean isArmHardFloat() {
        return this.isArmEabiAapcsVfp() || this.isArmHardFloatFlag();
    }

    public boolean isArmEabiAapcsVfp() {
        return this.armEabiAapcsVfp;
    }

    public boolean isArmHardFloatFlag() {
        return this.armHardFloatFlag;
    }

    public boolean isArmSoftFloatFlag() {
        return this.armSoftFloatFlag;
    }

    public boolean isArm() {
        return this.arm;
    }

    private ELFAnalyser(String string) {
        this.filename = string;
    }

    private void runDetection() throws IOException {
        RandomAccessFile randomAccessFile;
        block12: {
            block11: {
                randomAccessFile = new RandomAccessFile(this.filename, "r");
                try {
                    if (randomAccessFile.length() > 4L) {
                        byte[] byArray = new byte[4];
                        randomAccessFile.seek(0L);
                        randomAccessFile.read(byArray);
                        if (Arrays.equals(byArray, ELF_MAGIC)) {
                            this.ELF = true;
                        }
                    }
                    if (this.ELF) break block11;
                }
                catch (Throwable throwable) {
                    try {
                        randomAccessFile.close();
                    }
                    catch (IOException iOException) {}
                    throw throwable;
                }
                try {
                    randomAccessFile.close();
                    return;
                }
                catch (IOException iOException) {
                    return;
                }
            }
            randomAccessFile.seek(4L);
            byte by = randomAccessFile.readByte();
            int n2 = randomAccessFile.readByte();
            this._64Bit = by == 2;
            this.bigEndian = n2 == 2;
            randomAccessFile.seek(0L);
            ByteBuffer byteBuffer = ByteBuffer.allocate(this._64Bit ? 64 : 52);
            randomAccessFile.getChannel().read(byteBuffer, 0L);
            byteBuffer.order(this.bigEndian ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN);
            boolean bl = this.arm = byteBuffer.get(18) == 40;
            if (!this.arm) break block12;
            n2 = byteBuffer.getInt(this._64Bit ? 48 : 36);
            this.armHardFloatFlag = (n2 & 0x400) == 1024;
            this.armSoftFloatFlag = (n2 & 0x200) == 512;
            this.parseEabiAapcsVfp(byteBuffer, randomAccessFile);
        }
        try {
            randomAccessFile.close();
            return;
        }
        catch (IOException iOException) {
            return;
        }
    }

    private void parseEabiAapcsVfp(ByteBuffer iterator2, RandomAccessFile randomAccessFile) throws IOException {
        iterator2 = new ELFSectionHeaders(this._64Bit, this.bigEndian, (ByteBuffer)((Object)iterator2), randomAccessFile);
        for (ELFSectionHeaderEntry eLFSectionHeaderEntry : ((ELFSectionHeaders)((Object)iterator2)).getEntries()) {
            if (!".ARM.attributes".equals(eLFSectionHeaderEntry.getName())) continue;
            ByteBuffer byteBuffer = ByteBuffer.allocate(eLFSectionHeaderEntry.getSize());
            byteBuffer.order(this.bigEndian ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN);
            randomAccessFile.getChannel().read(byteBuffer, eLFSectionHeaderEntry.getOffset());
            byteBuffer.rewind();
            Map<Integer, Map<ArmAeabiAttributesTag, Object>> map = ELFAnalyser.parseArmAttributes(byteBuffer);
            Map<ArmAeabiAttributesTag, Object> map2 = map.get(1);
            if (map2 == null) continue;
            Object object = map2.get(ArmAeabiAttributesTag.ABI_VFP_args);
            if (object instanceof Integer && ((Integer)object).equals(1)) {
                this.armEabiAapcsVfp = true;
                continue;
            }
            if (!(object instanceof BigInteger) || ((BigInteger)object).intValue() != 1) continue;
            this.armEabiAapcsVfp = true;
        }
    }

    private static Map<Integer, Map<ArmAeabiAttributesTag, Object>> parseArmAttributes(ByteBuffer byteBuffer) {
        int n2 = byteBuffer.get();
        if (n2 != 65) {
            return Collections.EMPTY_MAP;
        }
        while (byteBuffer.position() < byteBuffer.limit()) {
            n2 = byteBuffer.position();
            int n3 = byteBuffer.getInt();
            if (n3 <= 0) break;
            String string = ELFAnalyser.readNTBS(byteBuffer, null);
            if ("aeabi".equals(string)) {
                return ELFAnalyser.parseAEABI(byteBuffer);
            }
            ((Buffer)byteBuffer).position(n2 + n3);
        }
        return Collections.EMPTY_MAP;
    }

    private static Map<Integer, Map<ArmAeabiAttributesTag, Object>> parseAEABI(ByteBuffer byteBuffer) {
        HashMap<Integer, Map<ArmAeabiAttributesTag, Object>> hashMap = new HashMap<Integer, Map<ArmAeabiAttributesTag, Object>>();
        while (byteBuffer.position() < byteBuffer.limit()) {
            int n2 = byteBuffer.position();
            int n3 = ELFAnalyser.readULEB128(byteBuffer).intValue();
            int n4 = byteBuffer.getInt();
            if (n3 == 1) {
                hashMap.put(n3, ELFAnalyser.parseFileAttribute(byteBuffer));
            }
            ((Buffer)byteBuffer).position(n2 + n4);
        }
        return hashMap;
    }

    private static Map<ArmAeabiAttributesTag, Object> parseFileAttribute(ByteBuffer byteBuffer) {
        HashMap<ArmAeabiAttributesTag, Object> hashMap = new HashMap<ArmAeabiAttributesTag, Object>();
        while (byteBuffer.position() < byteBuffer.limit()) {
            int n2 = ELFAnalyser.readULEB128(byteBuffer).intValue();
            ArmAeabiAttributesTag armAeabiAttributesTag = ArmAeabiAttributesTag.getByValue(n2);
            switch (armAeabiAttributesTag.getParameterType()) {
                case UINT32: {
                    hashMap.put(armAeabiAttributesTag, byteBuffer.getInt());
                    break;
                }
                case NTBS: {
                    hashMap.put(armAeabiAttributesTag, ELFAnalyser.readNTBS(byteBuffer, null));
                    break;
                }
                case ULEB128: {
                    hashMap.put(armAeabiAttributesTag, ELFAnalyser.readULEB128(byteBuffer));
                }
            }
        }
        return hashMap;
    }

    private static String readNTBS(ByteBuffer byteBuffer, Integer n2) {
        int n3;
        if (n2 != null) {
            ((Buffer)byteBuffer).position(n2);
        }
        int n4 = byteBuffer.position();
        while ((n3 = byteBuffer.get()) != 0 && byteBuffer.position() <= byteBuffer.limit()) {
        }
        n3 = byteBuffer.position();
        byte[] byArray = new byte[n3 - n4 - 1];
        ((Buffer)byteBuffer).position(n4);
        byteBuffer.get(byArray);
        ByteBuffer byteBuffer2 = byteBuffer;
        ((Buffer)byteBuffer2).position(byteBuffer2.position() + 1);
        try {
            return new String(byArray, "ASCII");
        }
        catch (UnsupportedEncodingException unsupportedEncodingException) {
            throw new RuntimeException(unsupportedEncodingException);
        }
    }

    private static BigInteger readULEB128(ByteBuffer byteBuffer) {
        BigInteger bigInteger = BigInteger.ZERO;
        int n2 = 0;
        while (true) {
            byte by = byteBuffer.get();
            bigInteger = bigInteger.or(BigInteger.valueOf(by & 0x7F).shiftLeft(n2));
            if ((by & 0x80) == 0) break;
            n2 += 7;
        }
        return bigInteger;
    }

    static class ArmAeabiAttributesTag {
        private final int value;
        private final String name;
        private final ParameterType parameterType;
        private static final List<ArmAeabiAttributesTag> tags = new LinkedList<ArmAeabiAttributesTag>();
        private static final Map<Integer, ArmAeabiAttributesTag> valueMap = new HashMap<Integer, ArmAeabiAttributesTag>();
        private static final Map<String, ArmAeabiAttributesTag> nameMap = new HashMap<String, ArmAeabiAttributesTag>();
        public static final ArmAeabiAttributesTag File = ArmAeabiAttributesTag.addTag(1, "File", ParameterType.UINT32);
        public static final ArmAeabiAttributesTag Section = ArmAeabiAttributesTag.addTag(2, "Section", ParameterType.UINT32);
        public static final ArmAeabiAttributesTag Symbol = ArmAeabiAttributesTag.addTag(3, "Symbol", ParameterType.UINT32);
        public static final ArmAeabiAttributesTag CPU_raw_name = ArmAeabiAttributesTag.addTag(4, "CPU_raw_name", ParameterType.NTBS);
        public static final ArmAeabiAttributesTag CPU_name = ArmAeabiAttributesTag.addTag(5, "CPU_name", ParameterType.NTBS);
        public static final ArmAeabiAttributesTag CPU_arch = ArmAeabiAttributesTag.addTag(6, "CPU_arch", ParameterType.ULEB128);
        public static final ArmAeabiAttributesTag CPU_arch_profile = ArmAeabiAttributesTag.addTag(7, "CPU_arch_profile", ParameterType.ULEB128);
        public static final ArmAeabiAttributesTag ARM_ISA_use = ArmAeabiAttributesTag.addTag(8, "ARM_ISA_use", ParameterType.ULEB128);
        public static final ArmAeabiAttributesTag THUMB_ISA_use = ArmAeabiAttributesTag.addTag(9, "THUMB_ISA_use", ParameterType.ULEB128);
        public static final ArmAeabiAttributesTag FP_arch = ArmAeabiAttributesTag.addTag(10, "FP_arch", ParameterType.ULEB128);
        public static final ArmAeabiAttributesTag WMMX_arch = ArmAeabiAttributesTag.addTag(11, "WMMX_arch", ParameterType.ULEB128);
        public static final ArmAeabiAttributesTag Advanced_SIMD_arch = ArmAeabiAttributesTag.addTag(12, "Advanced_SIMD_arch", ParameterType.ULEB128);
        public static final ArmAeabiAttributesTag PCS_config = ArmAeabiAttributesTag.addTag(13, "PCS_config", ParameterType.ULEB128);
        public static final ArmAeabiAttributesTag ABI_PCS_R9_use = ArmAeabiAttributesTag.addTag(14, "ABI_PCS_R9_use", ParameterType.ULEB128);
        public static final ArmAeabiAttributesTag ABI_PCS_RW_data = ArmAeabiAttributesTag.addTag(15, "ABI_PCS_RW_data", ParameterType.ULEB128);
        public static final ArmAeabiAttributesTag ABI_PCS_RO_data = ArmAeabiAttributesTag.addTag(16, "ABI_PCS_RO_data", ParameterType.ULEB128);
        public static final ArmAeabiAttributesTag ABI_PCS_GOT_use = ArmAeabiAttributesTag.addTag(17, "ABI_PCS_GOT_use", ParameterType.ULEB128);
        public static final ArmAeabiAttributesTag ABI_PCS_wchar_t = ArmAeabiAttributesTag.addTag(18, "ABI_PCS_wchar_t", ParameterType.ULEB128);
        public static final ArmAeabiAttributesTag ABI_FP_rounding = ArmAeabiAttributesTag.addTag(19, "ABI_FP_rounding", ParameterType.ULEB128);
        public static final ArmAeabiAttributesTag ABI_FP_denormal = ArmAeabiAttributesTag.addTag(20, "ABI_FP_denormal", ParameterType.ULEB128);
        public static final ArmAeabiAttributesTag ABI_FP_exceptions = ArmAeabiAttributesTag.addTag(21, "ABI_FP_exceptions", ParameterType.ULEB128);
        public static final ArmAeabiAttributesTag ABI_FP_user_exceptions = ArmAeabiAttributesTag.addTag(22, "ABI_FP_user_exceptions", ParameterType.ULEB128);
        public static final ArmAeabiAttributesTag ABI_FP_number_model = ArmAeabiAttributesTag.addTag(23, "ABI_FP_number_model", ParameterType.ULEB128);
        public static final ArmAeabiAttributesTag ABI_align_needed = ArmAeabiAttributesTag.addTag(24, "ABI_align_needed", ParameterType.ULEB128);
        public static final ArmAeabiAttributesTag ABI_align8_preserved = ArmAeabiAttributesTag.addTag(25, "ABI_align8_preserved", ParameterType.ULEB128);
        public static final ArmAeabiAttributesTag ABI_enum_size = ArmAeabiAttributesTag.addTag(26, "ABI_enum_size", ParameterType.ULEB128);
        public static final ArmAeabiAttributesTag ABI_HardFP_use = ArmAeabiAttributesTag.addTag(27, "ABI_HardFP_use", ParameterType.ULEB128);
        public static final ArmAeabiAttributesTag ABI_VFP_args = ArmAeabiAttributesTag.addTag(28, "ABI_VFP_args", ParameterType.ULEB128);
        public static final ArmAeabiAttributesTag ABI_WMMX_args = ArmAeabiAttributesTag.addTag(29, "ABI_WMMX_args", ParameterType.ULEB128);
        public static final ArmAeabiAttributesTag ABI_optimization_goals = ArmAeabiAttributesTag.addTag(30, "ABI_optimization_goals", ParameterType.ULEB128);
        public static final ArmAeabiAttributesTag ABI_FP_optimization_goals = ArmAeabiAttributesTag.addTag(31, "ABI_FP_optimization_goals", ParameterType.ULEB128);
        public static final ArmAeabiAttributesTag compatibility = ArmAeabiAttributesTag.addTag(32, "compatibility", ParameterType.NTBS);
        public static final ArmAeabiAttributesTag CPU_unaligned_access = ArmAeabiAttributesTag.addTag(34, "CPU_unaligned_access", ParameterType.ULEB128);
        public static final ArmAeabiAttributesTag FP_HP_extension = ArmAeabiAttributesTag.addTag(36, "FP_HP_extension", ParameterType.ULEB128);
        public static final ArmAeabiAttributesTag ABI_FP_16bit_format = ArmAeabiAttributesTag.addTag(38, "ABI_FP_16bit_format", ParameterType.ULEB128);
        public static final ArmAeabiAttributesTag MPextension_use = ArmAeabiAttributesTag.addTag(42, "MPextension_use", ParameterType.ULEB128);
        public static final ArmAeabiAttributesTag DIV_use = ArmAeabiAttributesTag.addTag(44, "DIV_use", ParameterType.ULEB128);
        public static final ArmAeabiAttributesTag nodefaults = ArmAeabiAttributesTag.addTag(64, "nodefaults", ParameterType.ULEB128);
        public static final ArmAeabiAttributesTag also_compatible_with = ArmAeabiAttributesTag.addTag(65, "also_compatible_with", ParameterType.NTBS);
        public static final ArmAeabiAttributesTag conformance = ArmAeabiAttributesTag.addTag(67, "conformance", ParameterType.NTBS);
        public static final ArmAeabiAttributesTag T2EE_use = ArmAeabiAttributesTag.addTag(66, "T2EE_use", ParameterType.ULEB128);
        public static final ArmAeabiAttributesTag Virtualization_use = ArmAeabiAttributesTag.addTag(68, "Virtualization_use", ParameterType.ULEB128);
        public static final ArmAeabiAttributesTag MPextension_use2 = ArmAeabiAttributesTag.addTag(70, "MPextension_use", ParameterType.ULEB128);

        public ArmAeabiAttributesTag(int n2, String string, ParameterType parameterType) {
            this.value = n2;
            this.name = string;
            this.parameterType = parameterType;
        }

        public int getValue() {
            return this.value;
        }

        public String getName() {
            return this.name;
        }

        public ParameterType getParameterType() {
            return this.parameterType;
        }

        public String toString() {
            return this.name + " (" + this.value + ")";
        }

        public int hashCode() {
            int n2 = 469 + this.value;
            return n2;
        }

        public boolean equals(Object object) {
            if (this == object) {
                return true;
            }
            if (object == null) {
                return false;
            }
            if (this.getClass() != object.getClass()) {
                return false;
            }
            object = (ArmAeabiAttributesTag)object;
            return this.value == ((ArmAeabiAttributesTag)object).value;
        }

        private static ArmAeabiAttributesTag addTag(int n2, String string, ParameterType parameterType) {
            ArmAeabiAttributesTag armAeabiAttributesTag = new ArmAeabiAttributesTag(n2, string, parameterType);
            if (!valueMap.containsKey(armAeabiAttributesTag.getValue())) {
                valueMap.put(armAeabiAttributesTag.getValue(), armAeabiAttributesTag);
            }
            if (!nameMap.containsKey(armAeabiAttributesTag.getName())) {
                nameMap.put(armAeabiAttributesTag.getName(), armAeabiAttributesTag);
            }
            tags.add(armAeabiAttributesTag);
            return armAeabiAttributesTag;
        }

        public static List<ArmAeabiAttributesTag> getTags() {
            return Collections.unmodifiableList(tags);
        }

        public static ArmAeabiAttributesTag getByName(String string) {
            return nameMap.get(string);
        }

        public static ArmAeabiAttributesTag getByValue(int n2) {
            if (valueMap.containsKey(n2)) {
                return valueMap.get(n2);
            }
            ArmAeabiAttributesTag armAeabiAttributesTag = new ArmAeabiAttributesTag(n2, "Unknown " + n2, ArmAeabiAttributesTag.getParameterType(n2));
            return armAeabiAttributesTag;
        }

        private static ParameterType getParameterType(int n2) {
            ArmAeabiAttributesTag armAeabiAttributesTag = ArmAeabiAttributesTag.getByValue(n2);
            if (armAeabiAttributesTag == null) {
                if (n2 % 2 == 0) {
                    return ParameterType.ULEB128;
                }
                return ParameterType.NTBS;
            }
            return armAeabiAttributesTag.getParameterType();
        }

        public static enum ParameterType {
            UINT32,
            NTBS,
            ULEB128;

        }
    }

    static class ELFSectionHeaderEntry {
        private final int nameOffset;
        private String name;
        private final int type;
        private final int flags;
        private final int offset;
        private final int size;

        public ELFSectionHeaderEntry(boolean bl, ByteBuffer byteBuffer) {
            this.nameOffset = byteBuffer.getInt(0);
            this.type = byteBuffer.getInt(4);
            this.flags = (int)(bl ? byteBuffer.getLong(8) : (long)byteBuffer.getInt(8));
            this.offset = (int)(bl ? byteBuffer.getLong(24) : (long)byteBuffer.getInt(16));
            this.size = (int)(bl ? byteBuffer.getLong(32) : (long)byteBuffer.getInt(20));
        }

        public String getName() {
            return this.name;
        }

        public void setName(String string) {
            this.name = string;
        }

        public int getNameOffset() {
            return this.nameOffset;
        }

        public int getType() {
            return this.type;
        }

        public int getFlags() {
            return this.flags;
        }

        public int getOffset() {
            return this.offset;
        }

        public int getSize() {
            return this.size;
        }

        public String toString() {
            return "ELFSectionHeaderEntry{nameIdx=" + this.nameOffset + ", name=" + this.name + ", type=" + this.type + ", flags=" + this.flags + ", offset=" + this.offset + ", size=" + this.size + '}';
        }
    }

    static class ELFSectionHeaders {
        private final List<ELFSectionHeaderEntry> entries = new ArrayList<ELFSectionHeaderEntry>();

        public ELFSectionHeaders(boolean bl, boolean bl2, ByteBuffer byteBuffer, RandomAccessFile randomAccessFile) throws IOException {
            ByteBuffer byteBuffer2;
            short s2;
            int n2;
            short s3;
            long l2;
            if (bl) {
                l2 = byteBuffer.getLong(40);
                s3 = byteBuffer.getShort(58);
                n2 = byteBuffer.getShort(60);
                s2 = byteBuffer.getShort(62);
            } else {
                l2 = byteBuffer.getInt(32);
                s3 = byteBuffer.getShort(46);
                n2 = byteBuffer.getShort(48);
                s2 = byteBuffer.getShort(50);
            }
            int n3 = n2 * s3;
            ByteBuffer byteBuffer3 = ByteBuffer.allocate(n3);
            byteBuffer3.order(bl2 ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN);
            randomAccessFile.getChannel().read(byteBuffer3, l2);
            for (int i2 = 0; i2 < n2; ++i2) {
                byteBuffer3.position(i2 * s3);
                byteBuffer2 = byteBuffer3.slice();
                byteBuffer2.order(byteBuffer3.order());
                byteBuffer2.limit(s3);
                this.entries.add(new ELFSectionHeaderEntry(bl, byteBuffer2));
            }
            ELFSectionHeaderEntry eLFSectionHeaderEntry = this.entries.get(s2);
            byteBuffer2 = ByteBuffer.allocate(eLFSectionHeaderEntry.getSize());
            byteBuffer2.order(bl2 ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN);
            randomAccessFile.getChannel().read(byteBuffer2, eLFSectionHeaderEntry.getOffset());
            byteBuffer2.rewind();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(20);
            for (ELFSectionHeaderEntry eLFSectionHeaderEntry2 : this.entries) {
                byte by;
                byteArrayOutputStream.reset();
                ((Buffer)byteBuffer2).position(eLFSectionHeaderEntry2.getNameOffset());
                while (byteBuffer2.position() < byteBuffer2.limit() && (by = byteBuffer2.get()) != 0) {
                    byteArrayOutputStream.write(by);
                }
                eLFSectionHeaderEntry2.setName(byteArrayOutputStream.toString("ASCII"));
            }
        }

        public List<ELFSectionHeaderEntry> getEntries() {
            return this.entries;
        }
    }
}

