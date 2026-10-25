/*
 * Decompiled with CFR 0.152.
 */
package com.google.gson;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;

public enum LongSerializationPolicy {
    DEFAULT{

        @Override
        public final JsonElement serialize(Long l2) {
            if (l2 == null) {
                return JsonNull.INSTANCE;
            }
            return new JsonPrimitive(l2);
        }
    }
    ,
    STRING{

        @Override
        public final JsonElement serialize(Long l2) {
            if (l2 == null) {
                return JsonNull.INSTANCE;
            }
            return new JsonPrimitive(l2.toString());
        }
    };


    public abstract JsonElement serialize(Long var1);
}

