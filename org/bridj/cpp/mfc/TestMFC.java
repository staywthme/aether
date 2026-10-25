/*
 * Decompiled with CFR 0.152.
 */
package org.bridj.cpp.mfc;

import org.bridj.Pointer;
import org.bridj.cpp.mfc.CCmdUI;
import org.bridj.cpp.mfc.CWnd;
import org.bridj.cpp.mfc.OnCommand;
import org.bridj.cpp.mfc.OnCommandEx;
import org.bridj.cpp.mfc.OnMessage;
import org.bridj.cpp.mfc.OnRegisteredMessage;
import org.bridj.cpp.mfc.OnUpdateCommand;

public class TestMFC {
    public static void main(String[] stringArray) {
        new CWnd(){

            @OnMessage(value=OnMessage.Type.WM_KEYDOWN)
            public final void OnKeyDown(int n2, int n3, int n4) {
            }

            @OnCommand(value=57600)
            public final void OnSomething() {
            }

            @OnCommandEx(value={57603, 57607})
            public final boolean OnSomethingEx(int n2) {
                return true;
            }

            @OnUpdateCommand(value=57600)
            public final void OnUpdateSomething(Pointer<CCmdUI> pointer) {
                if (pointer == null) {
                    return;
                }
                pointer.get().Enable(true);
            }

            @OnRegisteredMessage(value="MYAPP_MYMESSAGE")
            public final void OnMyMessage(int n2, int n3, int n4) {
            }
        };
    }
}

