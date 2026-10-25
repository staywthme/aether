/*
 * Decompiled with CFR 0.152.
 */
package org.conscrypt;

abstract class PeerInfoProvider {
    private static final PeerInfoProvider NULL_PEER_INFO_PROVIDER = new PeerInfoProvider(){

        @Override
        String getHostname() {
            return null;
        }

        @Override
        public String getHostnameOrIP() {
            return null;
        }

        @Override
        public int getPort() {
            return -1;
        }
    };

    PeerInfoProvider() {
    }

    abstract String getHostname();

    abstract String getHostnameOrIP();

    abstract int getPort();

    static PeerInfoProvider nullProvider() {
        return NULL_PEER_INFO_PROVIDER;
    }

    static PeerInfoProvider forHostAndPort(final String string, final int n2) {
        return new PeerInfoProvider(){

            @Override
            String getHostname() {
                return string;
            }

            @Override
            public String getHostnameOrIP() {
                return string;
            }

            @Override
            public int getPort() {
                return n2;
            }
        };
    }
}

