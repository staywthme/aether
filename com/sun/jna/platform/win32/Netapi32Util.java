/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna.platform.win32;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.DsGetDC;
import com.sun.jna.platform.win32.Guid;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.LMAccess;
import com.sun.jna.platform.win32.Netapi32;
import com.sun.jna.platform.win32.Ole32Util;
import com.sun.jna.platform.win32.Secur32Util;
import com.sun.jna.platform.win32.Win32Exception;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;
import java.util.ArrayList;

public abstract class Netapi32Util {
    public static String getDCName() {
        return Netapi32Util.getDCName(null, null);
    }

    public static String getDCName(String string, String string2) {
        PointerByReference pointerByReference = new PointerByReference();
        try {
            int n2 = Netapi32.INSTANCE.NetGetDCName(string2, string, pointerByReference);
            if (n2 != 0) {
                throw new Win32Exception(n2);
            }
            String string3 = pointerByReference.getValue().getWideString(0L);
            return string3;
        }
        finally {
            if (0 != Netapi32.INSTANCE.NetApiBufferFree(pointerByReference.getValue())) {
                throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
            }
        }
    }

    public static int getJoinStatus() {
        return Netapi32Util.getJoinStatus(null);
    }

    public static int getJoinStatus(String string) {
        PointerByReference pointerByReference = new PointerByReference();
        IntByReference intByReference = new IntByReference();
        try {
            int n2 = Netapi32.INSTANCE.NetGetJoinInformation(string, pointerByReference, intByReference);
            if (n2 != 0) {
                throw new Win32Exception(n2);
            }
            n2 = intByReference.getValue();
            return n2;
        }
        finally {
            int n3;
            if (pointerByReference.getPointer() != null && (n3 = Netapi32.INSTANCE.NetApiBufferFree(pointerByReference.getValue())) != 0) {
                throw new Win32Exception(n3);
            }
        }
    }

    public static String getDomainName(String string) {
        PointerByReference pointerByReference = new PointerByReference();
        IntByReference intByReference = new IntByReference();
        try {
            int n2 = Netapi32.INSTANCE.NetGetJoinInformation(string, pointerByReference, intByReference);
            if (n2 != 0) {
                throw new Win32Exception(n2);
            }
            String string2 = pointerByReference.getValue().getWideString(0L);
            return string2;
        }
        finally {
            int n3;
            if (pointerByReference.getPointer() != null && (n3 = Netapi32.INSTANCE.NetApiBufferFree(pointerByReference.getValue())) != 0) {
                throw new Win32Exception(n3);
            }
        }
    }

    public static LocalGroup[] getLocalGroups() {
        return Netapi32Util.getLocalGroups(null);
    }

    public static LocalGroup[] getLocalGroups(String string) {
        PointerByReference pointerByReference = new PointerByReference();
        LMAccess.LOCALGROUP_INFO_1[] lOCALGROUP_INFO_1Array = new IntByReference();
        Object object = new IntByReference();
        try {
            int n2 = Netapi32.INSTANCE.NetLocalGroupEnum(string, 1, pointerByReference, -1, (IntByReference)lOCALGROUP_INFO_1Array, (IntByReference)object, null);
            if (n2 != 0 || pointerByReference.getValue() == Pointer.NULL) {
                throw new Win32Exception(n2);
            }
            ArrayList<LocalGroup> arrayList = new ArrayList<LocalGroup>();
            if (lOCALGROUP_INFO_1Array.getValue() > 0) {
                object = new LMAccess.LOCALGROUP_INFO_1(pointerByReference.getValue());
                LMAccess.LOCALGROUP_INFO_1[] lOCALGROUP_INFO_1Array2 = (LMAccess.LOCALGROUP_INFO_1[])((Structure)object).toArray(lOCALGROUP_INFO_1Array.getValue());
                lOCALGROUP_INFO_1Array = lOCALGROUP_INFO_1Array2;
                lOCALGROUP_INFO_1Array = lOCALGROUP_INFO_1Array2;
                int n3 = lOCALGROUP_INFO_1Array2.length;
                for (int i2 = 0; i2 < n3; ++i2) {
                    LMAccess.LOCALGROUP_INFO_1 lOCALGROUP_INFO_1 = lOCALGROUP_INFO_1Array[i2];
                    LocalGroup localGroup = new LocalGroup();
                    new LocalGroup().name = lOCALGROUP_INFO_1.lgrui1_name;
                    localGroup.comment = lOCALGROUP_INFO_1.lgrui1_comment;
                    arrayList.add(localGroup);
                }
            }
            LocalGroup[] localGroupArray = arrayList.toArray(new LocalGroup[0]);
            return localGroupArray;
        }
        finally {
            int n4;
            if (pointerByReference.getValue() != Pointer.NULL && (n4 = Netapi32.INSTANCE.NetApiBufferFree(pointerByReference.getValue())) != 0) {
                throw new Win32Exception(n4);
            }
        }
    }

    public static Group[] getGlobalGroups() {
        return Netapi32Util.getGlobalGroups(null);
    }

    public static Group[] getGlobalGroups(String string) {
        PointerByReference pointerByReference = new PointerByReference();
        LMAccess.GROUP_INFO_1[] gROUP_INFO_1Array = new IntByReference();
        Object object = new IntByReference();
        try {
            int n2 = Netapi32.INSTANCE.NetGroupEnum(string, 1, pointerByReference, -1, (IntByReference)gROUP_INFO_1Array, (IntByReference)object, null);
            if (n2 != 0 || pointerByReference.getValue() == Pointer.NULL) {
                throw new Win32Exception(n2);
            }
            ArrayList<LocalGroup> arrayList = new ArrayList<LocalGroup>();
            if (gROUP_INFO_1Array.getValue() > 0) {
                object = new LMAccess.GROUP_INFO_1(pointerByReference.getValue());
                LMAccess.GROUP_INFO_1[] gROUP_INFO_1Array2 = (LMAccess.GROUP_INFO_1[])((Structure)object).toArray(gROUP_INFO_1Array.getValue());
                gROUP_INFO_1Array = gROUP_INFO_1Array2;
                gROUP_INFO_1Array = gROUP_INFO_1Array2;
                int n3 = gROUP_INFO_1Array2.length;
                for (int i2 = 0; i2 < n3; ++i2) {
                    LMAccess.GROUP_INFO_1 gROUP_INFO_1 = gROUP_INFO_1Array[i2];
                    LocalGroup localGroup = new LocalGroup();
                    new LocalGroup().name = gROUP_INFO_1.grpi1_name;
                    localGroup.comment = gROUP_INFO_1.grpi1_comment;
                    arrayList.add(localGroup);
                }
            }
            Group[] groupArray = arrayList.toArray(new LocalGroup[0]);
            return groupArray;
        }
        finally {
            int n4;
            if (pointerByReference.getValue() != Pointer.NULL && (n4 = Netapi32.INSTANCE.NetApiBufferFree(pointerByReference.getValue())) != 0) {
                throw new Win32Exception(n4);
            }
        }
    }

    public static User[] getUsers() {
        return Netapi32Util.getUsers(null);
    }

    public static User[] getUsers(String string) {
        PointerByReference pointerByReference = new PointerByReference();
        LMAccess.USER_INFO_1[] uSER_INFO_1Array = new IntByReference();
        Object object = new IntByReference();
        try {
            int n2 = Netapi32.INSTANCE.NetUserEnum(string, 1, 0, pointerByReference, -1, (IntByReference)uSER_INFO_1Array, (IntByReference)object, null);
            if (n2 != 0 || pointerByReference.getValue() == Pointer.NULL) {
                throw new Win32Exception(n2);
            }
            ArrayList<User> arrayList = new ArrayList<User>();
            if (uSER_INFO_1Array.getValue() > 0) {
                object = new LMAccess.USER_INFO_1(pointerByReference.getValue());
                LMAccess.USER_INFO_1[] uSER_INFO_1Array2 = (LMAccess.USER_INFO_1[])((Structure)object).toArray(uSER_INFO_1Array.getValue());
                uSER_INFO_1Array = uSER_INFO_1Array2;
                uSER_INFO_1Array = uSER_INFO_1Array2;
                int n3 = uSER_INFO_1Array2.length;
                for (int i2 = 0; i2 < n3; ++i2) {
                    LMAccess.USER_INFO_1 uSER_INFO_1 = uSER_INFO_1Array[i2];
                    User user = new User();
                    if (uSER_INFO_1.usri1_name != null) {
                        user.name = uSER_INFO_1.usri1_name;
                    }
                    arrayList.add(user);
                }
            }
            User[] userArray = arrayList.toArray(new User[0]);
            return userArray;
        }
        finally {
            int n4;
            if (pointerByReference.getValue() != Pointer.NULL && (n4 = Netapi32.INSTANCE.NetApiBufferFree(pointerByReference.getValue())) != 0) {
                throw new Win32Exception(n4);
            }
        }
    }

    public static Group[] getCurrentUserLocalGroups() {
        return Netapi32Util.getUserLocalGroups(Secur32Util.getUserNameEx(2));
    }

    public static Group[] getUserLocalGroups(String string) {
        return Netapi32Util.getUserLocalGroups(string, null);
    }

    public static Group[] getUserLocalGroups(String string, String object) {
        PointerByReference pointerByReference = new PointerByReference();
        LMAccess.LOCALGROUP_USERS_INFO_0[] lOCALGROUP_USERS_INFO_0Array = new IntByReference();
        IntByReference intByReference = new IntByReference();
        try {
            int n2 = Netapi32.INSTANCE.NetUserGetLocalGroups((String)object, string, 0, 0, pointerByReference, -1, (IntByReference)lOCALGROUP_USERS_INFO_0Array, intByReference);
            if (n2 != 0) {
                throw new Win32Exception(n2);
            }
            ArrayList<LocalGroup> arrayList = new ArrayList<LocalGroup>();
            if (lOCALGROUP_USERS_INFO_0Array.getValue() > 0) {
                object = new LMAccess.LOCALGROUP_USERS_INFO_0(pointerByReference.getValue());
                lOCALGROUP_USERS_INFO_0Array = (LMAccess.LOCALGROUP_USERS_INFO_0[])object.toArray(lOCALGROUP_USERS_INFO_0Array.getValue());
                object = lOCALGROUP_USERS_INFO_0Array;
                int n3 = lOCALGROUP_USERS_INFO_0Array.length;
                for (int i2 = 0; i2 < n3; ++i2) {
                    Group group = object[i2];
                    LocalGroup localGroup = new LocalGroup();
                    if (((LMAccess.LOCALGROUP_USERS_INFO_0)((Object)group)).lgrui0_name != null) {
                        localGroup.name = ((LMAccess.LOCALGROUP_USERS_INFO_0)((Object)group)).lgrui0_name;
                    }
                    arrayList.add(localGroup);
                }
            }
            object = arrayList.toArray(new Group[0]);
            return object;
        }
        finally {
            int n4;
            if (pointerByReference.getValue() != Pointer.NULL && (n4 = Netapi32.INSTANCE.NetApiBufferFree(pointerByReference.getValue())) != 0) {
                throw new Win32Exception(n4);
            }
        }
    }

    public static Group[] getUserGroups(String string) {
        return Netapi32Util.getUserGroups(string, null);
    }

    public static Group[] getUserGroups(String string, String object) {
        PointerByReference pointerByReference = new PointerByReference();
        LMAccess.GROUP_USERS_INFO_0[] gROUP_USERS_INFO_0Array = new IntByReference();
        IntByReference intByReference = new IntByReference();
        try {
            int n2 = Netapi32.INSTANCE.NetUserGetGroups((String)object, string, 0, pointerByReference, -1, (IntByReference)gROUP_USERS_INFO_0Array, intByReference);
            if (n2 != 0) {
                throw new Win32Exception(n2);
            }
            ArrayList<Group> arrayList = new ArrayList<Group>();
            if (gROUP_USERS_INFO_0Array.getValue() > 0) {
                object = new LMAccess.GROUP_USERS_INFO_0(pointerByReference.getValue());
                gROUP_USERS_INFO_0Array = (LMAccess.GROUP_USERS_INFO_0[])object.toArray(gROUP_USERS_INFO_0Array.getValue());
                object = gROUP_USERS_INFO_0Array;
                int n3 = gROUP_USERS_INFO_0Array.length;
                for (int i2 = 0; i2 < n3; ++i2) {
                    Group group = object[i2];
                    Group group2 = new Group();
                    if (((LMAccess.GROUP_USERS_INFO_0)((Object)group)).grui0_name != null) {
                        group2.name = ((LMAccess.GROUP_USERS_INFO_0)((Object)group)).grui0_name;
                    }
                    arrayList.add(group2);
                }
            }
            object = arrayList.toArray(new Group[0]);
            return object;
        }
        finally {
            int n4;
            if (pointerByReference.getValue() != Pointer.NULL && (n4 = Netapi32.INSTANCE.NetApiBufferFree(pointerByReference.getValue())) != 0) {
                throw new Win32Exception(n4);
            }
        }
    }

    public static DomainController getDC() {
        DsGetDC.PDOMAIN_CONTROLLER_INFO pDOMAIN_CONTROLLER_INFO = new DsGetDC.PDOMAIN_CONTROLLER_INFO();
        int n2 = Netapi32.INSTANCE.DsGetDcName(null, null, null, null, 0, pDOMAIN_CONTROLLER_INFO);
        if (n2 != 0) {
            throw new Win32Exception(n2);
        }
        DomainController domainController = new DomainController();
        new DomainController().address = pDOMAIN_CONTROLLER_INFO.dci.DomainControllerAddress;
        domainController.addressType = pDOMAIN_CONTROLLER_INFO.dci.DomainControllerAddressType;
        domainController.clientSiteName = pDOMAIN_CONTROLLER_INFO.dci.ClientSiteName;
        domainController.dnsForestName = pDOMAIN_CONTROLLER_INFO.dci.DnsForestName;
        domainController.domainGuid = pDOMAIN_CONTROLLER_INFO.dci.DomainGuid;
        domainController.domainName = pDOMAIN_CONTROLLER_INFO.dci.DomainName;
        domainController.flags = pDOMAIN_CONTROLLER_INFO.dci.Flags;
        domainController.name = pDOMAIN_CONTROLLER_INFO.dci.DomainControllerName;
        n2 = Netapi32.INSTANCE.NetApiBufferFree(pDOMAIN_CONTROLLER_INFO.dci.getPointer());
        if (n2 != 0) {
            throw new Win32Exception(n2);
        }
        return domainController;
    }

    public static DomainTrust[] getDomainTrusts() {
        return Netapi32Util.getDomainTrusts(null);
    }

    public static DomainTrust[] getDomainTrusts(String string) {
        PointerByReference pointerByReference = new PointerByReference();
        DsGetDC.DS_DOMAIN_TRUSTS[] dS_DOMAIN_TRUSTSArray = new IntByReference();
        int n2 = Netapi32.INSTANCE.DsEnumerateDomainTrusts(string, 63, pointerByReference, (IntByReference)dS_DOMAIN_TRUSTSArray);
        if (n2 != 0) {
            throw new Win32Exception(n2);
        }
        try {
            ArrayList<DomainTrust> arrayList = new ArrayList<DomainTrust>(dS_DOMAIN_TRUSTSArray.getValue());
            if (dS_DOMAIN_TRUSTSArray.getValue() > 0) {
                DsGetDC.DS_DOMAIN_TRUSTS dS_DOMAIN_TRUSTS = new DsGetDC.DS_DOMAIN_TRUSTS(pointerByReference.getValue());
                DsGetDC.DS_DOMAIN_TRUSTS[] dS_DOMAIN_TRUSTSArray2 = (DsGetDC.DS_DOMAIN_TRUSTS[])dS_DOMAIN_TRUSTS.toArray(new DsGetDC.DS_DOMAIN_TRUSTS[dS_DOMAIN_TRUSTSArray.getValue()]);
                dS_DOMAIN_TRUSTSArray = dS_DOMAIN_TRUSTSArray2;
                dS_DOMAIN_TRUSTSArray = dS_DOMAIN_TRUSTSArray2;
                int n3 = dS_DOMAIN_TRUSTSArray2.length;
                for (int i2 = 0; i2 < n3; ++i2) {
                    DsGetDC.DS_DOMAIN_TRUSTS dS_DOMAIN_TRUSTS2 = dS_DOMAIN_TRUSTSArray[i2];
                    DomainTrust domainTrust = new DomainTrust();
                    if (dS_DOMAIN_TRUSTS2.DnsDomainName != null) {
                        domainTrust.DnsDomainName = dS_DOMAIN_TRUSTS2.DnsDomainName;
                    }
                    if (dS_DOMAIN_TRUSTS2.NetbiosDomainName != null) {
                        domainTrust.NetbiosDomainName = dS_DOMAIN_TRUSTS2.NetbiosDomainName;
                    }
                    domainTrust.DomainSid = dS_DOMAIN_TRUSTS2.DomainSid;
                    if (dS_DOMAIN_TRUSTS2.DomainSid != null) {
                        domainTrust.DomainSidString = Advapi32Util.convertSidToStringSid(dS_DOMAIN_TRUSTS2.DomainSid);
                    }
                    domainTrust.DomainGuid = dS_DOMAIN_TRUSTS2.DomainGuid;
                    if (dS_DOMAIN_TRUSTS2.DomainGuid != null) {
                        domainTrust.DomainGuidString = Ole32Util.getStringFromGUID(dS_DOMAIN_TRUSTS2.DomainGuid);
                    }
                    domainTrust.flags = dS_DOMAIN_TRUSTS2.Flags;
                    arrayList.add(domainTrust);
                }
            }
            DomainTrust[] domainTrustArray = arrayList.toArray(new DomainTrust[0]);
            return domainTrustArray;
        }
        finally {
            int n4 = Netapi32.INSTANCE.NetApiBufferFree(pointerByReference.getValue());
            if (n4 != 0) {
                throw new Win32Exception(n4);
            }
        }
    }

    public static UserInfo getUserInfo(String string) {
        return Netapi32Util.getUserInfo(string, Netapi32Util.getDCName());
    }

    public static UserInfo getUserInfo(String string, String object) {
        PointerByReference pointerByReference = new PointerByReference();
        try {
            int n2 = Netapi32.INSTANCE.NetUserGetInfo((String)object, string, 23, pointerByReference);
            if (n2 == 0) {
                Object object2 = new LMAccess.USER_INFO_23(pointerByReference.getValue());
                object = new UserInfo();
                new UserInfo().comment = ((LMAccess.USER_INFO_23)object2).usri23_comment;
                ((UserInfo)object).flags = ((LMAccess.USER_INFO_23)object2).usri23_flags;
                ((UserInfo)object).fullName = ((LMAccess.USER_INFO_23)object2).usri23_full_name;
                ((User)object).name = ((LMAccess.USER_INFO_23)object2).usri23_name;
                if (((LMAccess.USER_INFO_23)object2).usri23_user_sid != null) {
                    ((UserInfo)object).sidString = Advapi32Util.convertSidToStringSid(((LMAccess.USER_INFO_23)object2).usri23_user_sid);
                }
                ((UserInfo)object).sid = ((LMAccess.USER_INFO_23)object2).usri23_user_sid;
                object2 = object;
                return object2;
            }
            throw new Win32Exception(n2);
        }
        finally {
            if (pointerByReference.getValue() != Pointer.NULL) {
                Netapi32.INSTANCE.NetApiBufferFree(pointerByReference.getValue());
            }
        }
    }

    public static class DomainTrust {
        public String NetbiosDomainName;
        public String DnsDomainName;
        public WinNT.PSID DomainSid;
        public String DomainSidString;
        public Guid.GUID DomainGuid;
        public String DomainGuidString;
        private int flags;

        public boolean isInForest() {
            return (this.flags & 1) != 0;
        }

        public boolean isOutbound() {
            return (this.flags & 2) != 0;
        }

        public boolean isRoot() {
            return (this.flags & 4) != 0;
        }

        public boolean isPrimary() {
            return (this.flags & 8) != 0;
        }

        public boolean isNativeMode() {
            return (this.flags & 0x10) != 0;
        }

        public boolean isInbound() {
            return (this.flags & 0x20) != 0;
        }
    }

    public static class DomainController {
        public String name;
        public String address;
        public int addressType;
        public Guid.GUID domainGuid;
        public String domainName;
        public String dnsForestName;
        public int flags;
        public String clientSiteName;
    }

    public static class LocalGroup
    extends Group {
        public String comment;
    }

    public static class UserInfo
    extends User {
        public String fullName;
        public String sidString;
        public WinNT.PSID sid;
        public int flags;
    }

    public static class User {
        public String name;
        public String comment;
    }

    public static class Group {
        public String name;
    }
}

