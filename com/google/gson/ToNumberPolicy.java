/*
 * Decompiled with CFR 0.152.
 */
package com.google.gson;

import com.google.gson.JsonParseException;
import com.google.gson.ToNumberStrategy;
import com.google.gson.internal.LazilyParsedNumber;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.MalformedJsonException;
import java.io.IOException;
import java.math.BigDecimal;

public enum ToNumberPolicy implements ToNumberStrategy
{
    DOUBLE{

        @Override
        public final Double readNumber(JsonReader jsonReader) throws IOException {
            return jsonReader.nextDouble();
        }
    }
    ,
    LAZILY_PARSED_NUMBER{

        @Override
        public final Number readNumber(JsonReader jsonReader) throws IOException {
            return new LazilyParsedNumber(jsonReader.nextString());
        }
    }
    ,
    LONG_OR_DOUBLE{

        @Override
        public final Number readNumber(JsonReader jsonReader) throws IOException, JsonParseException {
            String string = jsonReader.nextString();
            try {
                return Long.parseLong(string);
            }
            catch (NumberFormatException numberFormatException) {
                try {
                    Double d2 = Double.valueOf(string);
                    if ((d2.isInfinite() || d2.isNaN()) && !jsonReader.isLenient()) {
                        throw new MalformedJsonException("JSON forbids NaN and infinities: " + d2 + "; at path " + jsonReader.getPreviousPath());
                    }
                    return d2;
                }
                catch (NumberFormatException numberFormatException2) {
                    throw new JsonParseException("Cannot parse " + string + "; at path " + jsonReader.getPreviousPath(), numberFormatException2);
                }
            }
        }
    }
    ,
    BIG_DECIMAL{

        @Override
        public final BigDecimal readNumber(JsonReader jsonReader) throws IOException {
            String string = jsonReader.nextString();
            try {
                return new BigDecimal(string);
            }
            catch (NumberFormatException numberFormatException) {
                throw new JsonParseException("Cannot parse " + string + "; at path " + jsonReader.getPreviousPath(), numberFormatException);
            }
        }
    };

}

