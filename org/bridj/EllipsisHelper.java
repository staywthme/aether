/*
 * Decompiled with CFR 0.152.
 */
package org.bridj;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import org.bridj.CLong;
import org.bridj.NativeConstants;
import org.bridj.NativeObject;
import org.bridj.Pointer;
import org.bridj.SizeT;

class EllipsisHelper {
    static ThreadLocal<IntBuffer[]> holders = new ThreadLocal<IntBuffer[]>(){

        @Override
        protected final IntBuffer[] initialValue() {
            return new IntBuffer[1];
        }
    };

    EllipsisHelper() {
    }

    public static IntBuffer unrollEllipsis(Object[] objectArray) {
        IntBuffer[] intBufferArray = holders.get();
        int n2 = objectArray.length;
        IntBuffer intBuffer = intBufferArray[0];
        if (intBuffer == null || intBuffer.capacity() < n2) {
            intBuffer = intBufferArray[0] = ByteBuffer.allocateDirect(n2 << 2).asIntBuffer();
        }
        for (int i2 = 0; i2 < n2; ++i2) {
            NativeConstants.ValueType valueType;
            Object object = objectArray[i2];
            if (object == null || object instanceof Pointer) {
                valueType = NativeConstants.ValueType.ePointerValue;
            } else if (object instanceof Integer) {
                valueType = NativeConstants.ValueType.eIntValue;
            } else if (object instanceof Long) {
                valueType = NativeConstants.ValueType.eLongValue;
            } else if (object instanceof Short) {
                valueType = NativeConstants.ValueType.eShortValue;
            } else if (object instanceof Double) {
                valueType = NativeConstants.ValueType.eDoubleValue;
            } else if (object instanceof Float) {
                valueType = NativeConstants.ValueType.eFloatValue;
            } else if (object instanceof Byte) {
                valueType = NativeConstants.ValueType.eByteValue;
            } else if (object instanceof Boolean) {
                valueType = NativeConstants.ValueType.eBooleanValue;
            } else if (object instanceof Character) {
                valueType = NativeConstants.ValueType.eWCharValue;
            } else if (object instanceof SizeT) {
                valueType = NativeConstants.ValueType.eSizeTValue;
                objectArray[i2] = ((SizeT)object).longValue();
            } else if (object instanceof CLong) {
                valueType = NativeConstants.ValueType.eCLongValue;
                objectArray[i2] = ((CLong)object).longValue();
            } else if (object instanceof NativeObject) {
                valueType = NativeConstants.ValueType.eNativeObjectValue;
            } else {
                throw new IllegalArgumentException("Argument type not handled in variable argument calls  : " + object + " (" + object.getClass().getName() + ")");
            }
            intBuffer.put(i2, valueType.ordinal());
        }
        return intBuffer;
    }
}

