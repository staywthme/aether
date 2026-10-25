/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna.platform.win32;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.platform.win32.WinCrypt;

public abstract class WinCryptUtil {

    public static class MANAGED_CRYPT_SIGN_MESSAGE_PARA
    extends WinCrypt.CRYPT_SIGN_MESSAGE_PARA {
        private WinCrypt.CERT_CONTEXT[] rgpMsgCerts;
        private WinCrypt.CRL_CONTEXT[] rgpMsgCrls;
        private WinCrypt.CRYPT_ATTRIBUTE[] rgAuthAttrs;
        private WinCrypt.CRYPT_ATTRIBUTE[] rgUnauthAttrs;

        public void setRgpMsgCert(WinCrypt.CERT_CONTEXT[] cERT_CONTEXTArray) {
            this.rgpMsgCerts = cERT_CONTEXTArray;
            if (cERT_CONTEXTArray == null || cERT_CONTEXTArray.length == 0) {
                this.rgpMsgCert = null;
                this.cMsgCert = 0;
                return;
            }
            this.cMsgCert = cERT_CONTEXTArray.length;
            Memory memory = new Memory(Native.POINTER_SIZE * cERT_CONTEXTArray.length);
            for (int i2 = 0; i2 < cERT_CONTEXTArray.length; ++i2) {
                ((Pointer)memory).setPointer(i2 * Native.POINTER_SIZE, cERT_CONTEXTArray[i2].getPointer());
            }
            this.rgpMsgCert = memory;
        }

        @Override
        public WinCrypt.CERT_CONTEXT[] getRgpMsgCert() {
            return this.rgpMsgCerts;
        }

        public void setRgpMsgCrl(WinCrypt.CRL_CONTEXT[] cRL_CONTEXTArray) {
            this.rgpMsgCrls = cRL_CONTEXTArray;
            if (cRL_CONTEXTArray == null || cRL_CONTEXTArray.length == 0) {
                this.rgpMsgCert = null;
                this.cMsgCert = 0;
                return;
            }
            this.cMsgCert = cRL_CONTEXTArray.length;
            Memory memory = new Memory(Native.POINTER_SIZE * cRL_CONTEXTArray.length);
            for (int i2 = 0; i2 < cRL_CONTEXTArray.length; ++i2) {
                ((Pointer)memory).setPointer(i2 * Native.POINTER_SIZE, cRL_CONTEXTArray[i2].getPointer());
            }
            this.rgpMsgCert = memory;
        }

        @Override
        public WinCrypt.CRL_CONTEXT[] getRgpMsgCrl() {
            return this.rgpMsgCrls;
        }

        public void setRgAuthAttr(WinCrypt.CRYPT_ATTRIBUTE[] cRYPT_ATTRIBUTEArray) {
            this.rgAuthAttrs = cRYPT_ATTRIBUTEArray;
            if (cRYPT_ATTRIBUTEArray == null || cRYPT_ATTRIBUTEArray.length == 0) {
                this.rgAuthAttr = null;
                this.cMsgCert = 0;
                return;
            }
            this.cMsgCert = this.rgpMsgCerts.length;
            this.rgAuthAttr = cRYPT_ATTRIBUTEArray[0].getPointer();
        }

        @Override
        public WinCrypt.CRYPT_ATTRIBUTE[] getRgAuthAttr() {
            return this.rgAuthAttrs;
        }

        public void setRgUnauthAttr(WinCrypt.CRYPT_ATTRIBUTE[] cRYPT_ATTRIBUTEArray) {
            this.rgUnauthAttrs = cRYPT_ATTRIBUTEArray;
            if (cRYPT_ATTRIBUTEArray == null || cRYPT_ATTRIBUTEArray.length == 0) {
                this.rgUnauthAttr = null;
                this.cMsgCert = 0;
                return;
            }
            this.cMsgCert = this.rgpMsgCerts.length;
            this.rgUnauthAttr = cRYPT_ATTRIBUTEArray[0].getPointer();
        }

        @Override
        public WinCrypt.CRYPT_ATTRIBUTE[] getRgUnauthAttr() {
            return this.rgUnauthAttrs;
        }

        @Override
        public void write() {
            Structure structure;
            int n2;
            int n3;
            Structure[] structureArray;
            if (this.rgpMsgCerts != null) {
                structureArray = this.rgpMsgCerts;
                n3 = this.rgpMsgCerts.length;
                for (n2 = 0; n2 < n3; ++n2) {
                    structure = structureArray[n2];
                    structure.write();
                }
            }
            if (this.rgpMsgCrls != null) {
                structureArray = this.rgpMsgCrls;
                n3 = this.rgpMsgCrls.length;
                for (n2 = 0; n2 < n3; ++n2) {
                    structure = structureArray[n2];
                    structure.write();
                }
            }
            if (this.rgAuthAttrs != null) {
                structureArray = this.rgAuthAttrs;
                n3 = this.rgAuthAttrs.length;
                for (n2 = 0; n2 < n3; ++n2) {
                    structure = structureArray[n2];
                    structure.write();
                }
            }
            if (this.rgUnauthAttrs != null) {
                structureArray = this.rgUnauthAttrs;
                n3 = this.rgUnauthAttrs.length;
                for (n2 = 0; n2 < n3; ++n2) {
                    structure = structureArray[n2];
                    structure.write();
                }
            }
            this.cbSize = this.size();
            super.write();
        }

        @Override
        public void read() {
            Structure structure;
            int n2;
            int n3;
            Structure[] structureArray;
            if (this.rgpMsgCerts != null) {
                structureArray = this.rgpMsgCerts;
                n3 = this.rgpMsgCerts.length;
                for (n2 = 0; n2 < n3; ++n2) {
                    structure = structureArray[n2];
                    structure.read();
                }
            }
            if (this.rgpMsgCrls != null) {
                structureArray = this.rgpMsgCrls;
                n3 = this.rgpMsgCrls.length;
                for (n2 = 0; n2 < n3; ++n2) {
                    structure = structureArray[n2];
                    structure.read();
                }
            }
            if (this.rgAuthAttrs != null) {
                structureArray = this.rgAuthAttrs;
                n3 = this.rgAuthAttrs.length;
                for (n2 = 0; n2 < n3; ++n2) {
                    structure = structureArray[n2];
                    structure.read();
                }
            }
            if (this.rgUnauthAttrs != null) {
                structureArray = this.rgUnauthAttrs;
                n3 = this.rgUnauthAttrs.length;
                for (n2 = 0; n2 < n3; ++n2) {
                    structure = structureArray[n2];
                    structure.read();
                }
            }
            super.read();
        }
    }
}

