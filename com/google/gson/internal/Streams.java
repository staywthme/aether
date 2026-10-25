/*
 * Decompiled with CFR 0.152.
 */
package com.google.gson.internal;

import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonNull;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.google.gson.stream.MalformedJsonException;
import java.io.EOFException;
import java.io.IOException;
import java.io.Writer;
import java.util.Objects;

public final class Streams {
    private Streams() {
        throw new UnsupportedOperationException();
    }

    public static JsonElement parse(JsonReader jsonReader) throws JsonParseException {
        boolean bl = true;
        try {
            jsonReader.peek();
            bl = false;
            return TypeAdapters.JSON_ELEMENT.read(jsonReader);
        }
        catch (EOFException eOFException) {
            if (bl) {
                return JsonNull.INSTANCE;
            }
            throw new JsonSyntaxException(eOFException);
        }
        catch (MalformedJsonException malformedJsonException) {
            throw new JsonSyntaxException(malformedJsonException);
        }
        catch (IOException iOException) {
            throw new JsonIOException(iOException);
        }
        catch (NumberFormatException numberFormatException) {
            throw new JsonSyntaxException(numberFormatException);
        }
    }

    public static void write(JsonElement jsonElement, JsonWriter jsonWriter) throws IOException {
        TypeAdapters.JSON_ELEMENT.write(jsonWriter, jsonElement);
    }

    public static Writer writerForAppendable(Appendable appendable) {
        if (appendable instanceof Writer) {
            return (Writer)appendable;
        }
        return new AppendableWriter(appendable);
    }

    static final class AppendableWriter
    extends Writer {
        private final Appendable appendable;
        private final CurrentWrite currentWrite = new CurrentWrite();

        AppendableWriter(Appendable appendable) {
            this.appendable = appendable;
        }

        @Override
        public final void write(char[] cArray, int n2, int n3) throws IOException {
            this.currentWrite.setChars(cArray);
            int n4 = n2;
            this.appendable.append(this.currentWrite, n4, n4 + n3);
        }

        @Override
        public final void flush() {
        }

        @Override
        public final void close() {
        }

        @Override
        public final void write(int n2) throws IOException {
            this.appendable.append((char)n2);
        }

        @Override
        public final void write(String string, int n2, int n3) throws IOException {
            Objects.requireNonNull(string);
            int n4 = n2;
            this.appendable.append(string, n4, n4 + n3);
        }

        @Override
        public final Writer append(CharSequence charSequence) throws IOException {
            this.appendable.append(charSequence);
            return this;
        }

        @Override
        public final Writer append(CharSequence charSequence, int n2, int n3) throws IOException {
            this.appendable.append(charSequence, n2, n3);
            return this;
        }

        static class CurrentWrite
        implements CharSequence {
            private char[] chars;
            private String cachedString;

            private CurrentWrite() {
            }

            void setChars(char[] cArray) {
                this.chars = cArray;
                this.cachedString = null;
            }

            @Override
            public int length() {
                return this.chars.length;
            }

            @Override
            public char charAt(int n2) {
                return this.chars[n2];
            }

            @Override
            public CharSequence subSequence(int n2, int n3) {
                return new String(this.chars, n2, n3 - n2);
            }

            @Override
            public String toString() {
                if (this.cachedString == null) {
                    this.cachedString = new String(this.chars);
                }
                return this.cachedString;
            }
        }
    }
}

