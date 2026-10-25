/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna.platform.dnd;

import com.sun.jna.platform.dnd.DragHandler;
import com.sun.jna.platform.dnd.DropTargetPainter;
import java.awt.Component;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetContext;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class DropHandler
implements DropTargetListener {
    private static final Logger LOG = Logger.getLogger(DropHandler.class.getName());
    private int acceptedActions;
    private List<DataFlavor> acceptedFlavors;
    private DropTarget dropTarget;
    private boolean active = true;
    private DropTargetPainter painter;
    private String lastAction;

    public DropHandler(Component component, int n2) {
        this(component, n2, new DataFlavor[0]);
    }

    public DropHandler(Component component, int n2, DataFlavor[] dataFlavorArray) {
        this(component, n2, dataFlavorArray, null);
    }

    public DropHandler(Component component, int n2, DataFlavor[] dataFlavorArray, DropTargetPainter dropTargetPainter) {
        this.acceptedActions = n2;
        this.acceptedFlavors = Arrays.asList(dataFlavorArray);
        this.painter = dropTargetPainter;
        DropHandler dropHandler = this;
        this.dropTarget = new DropTarget(component, n2, dropHandler, dropHandler.active);
    }

    protected DropTarget getDropTarget() {
        return this.dropTarget;
    }

    public boolean isActive() {
        return this.active;
    }

    public void setActive(boolean bl) {
        this.active = bl;
        if (this.dropTarget != null) {
            this.dropTarget.setActive(bl);
        }
    }

    protected int getDropActionsForFlavors(DataFlavor[] dataFlavorArray) {
        return this.acceptedActions;
    }

    protected int getDropAction(DropTargetEvent dropTargetEvent) {
        int n2;
        DropTargetEvent dropTargetEvent2;
        int n3 = 0;
        int n4 = 0;
        Point point = null;
        DataFlavor[] dataFlavorArray = new DataFlavor[]{};
        if (dropTargetEvent instanceof DropTargetDragEvent) {
            dropTargetEvent2 = (DropTargetDragEvent)dropTargetEvent;
            n3 = ((DropTargetDragEvent)dropTargetEvent2).getDropAction();
            n4 = ((DropTargetDragEvent)dropTargetEvent2).getSourceActions();
            dataFlavorArray = ((DropTargetDragEvent)dropTargetEvent2).getCurrentDataFlavors();
            point = ((DropTargetDragEvent)dropTargetEvent2).getLocation();
        } else if (dropTargetEvent instanceof DropTargetDropEvent) {
            dropTargetEvent2 = (DropTargetDropEvent)dropTargetEvent;
            n3 = ((DropTargetDropEvent)dropTargetEvent2).getDropAction();
            n4 = ((DropTargetDropEvent)dropTargetEvent2).getSourceActions();
            dataFlavorArray = ((DropTargetDropEvent)dropTargetEvent2).getCurrentDataFlavors();
            point = ((DropTargetDropEvent)dropTargetEvent2).getLocation();
        }
        if (this.isSupported(dataFlavorArray) && (n3 = this.getDropAction(dropTargetEvent, n3, n4, n2 = this.getDropActionsForFlavors(dataFlavorArray))) != 0 && this.canDrop(dropTargetEvent, n3, point)) {
            return n3;
        }
        return 0;
    }

    protected int getDropAction(DropTargetEvent dropTargetEvent, int n2, int n3, int n4) {
        int n5 = this.modifiersActive(n2);
        if ((n2 & n4) == 0 && n5 == 0) {
            n2 = n5 = n4 & n3;
        } else if (n5 != 0 && (n5 = n2 & n4 & n3) != n2) {
            n2 = n5;
        }
        return n2;
    }

    protected boolean modifiersActive(int n2) {
        int n3 = DragHandler.getModifiers();
        if (n3 == -1) {
            return n2 == 0x40000000 || n2 == 1;
        }
        return n3 != 0;
    }

    private void describe(String object, DropTargetEvent dropTargetEvent) {
        if (LOG.isLoggable(Level.FINE)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("drop: ");
            stringBuilder.append((String)object);
            if (dropTargetEvent instanceof DropTargetDragEvent) {
                object = dropTargetEvent.getDropTargetContext();
                object = ((DropTargetContext)object).getDropTarget();
                dropTargetEvent = (DropTargetDragEvent)dropTargetEvent;
                stringBuilder.append(": src=");
                stringBuilder.append(DragHandler.actionString(((DropTargetDragEvent)dropTargetEvent).getSourceActions()));
                stringBuilder.append(" tgt=");
                stringBuilder.append(DragHandler.actionString(((DropTarget)object).getDefaultActions()));
                stringBuilder.append(" act=");
                stringBuilder.append(DragHandler.actionString(((DropTargetDragEvent)dropTargetEvent).getDropAction()));
            } else if (dropTargetEvent instanceof DropTargetDropEvent) {
                object = dropTargetEvent.getDropTargetContext();
                object = ((DropTargetContext)object).getDropTarget();
                dropTargetEvent = (DropTargetDropEvent)dropTargetEvent;
                stringBuilder.append(": src=");
                stringBuilder.append(DragHandler.actionString(((DropTargetDropEvent)dropTargetEvent).getSourceActions()));
                stringBuilder.append(" tgt=");
                stringBuilder.append(DragHandler.actionString(((DropTarget)object).getDefaultActions()));
                stringBuilder.append(" act=");
                stringBuilder.append(DragHandler.actionString(((DropTargetDropEvent)dropTargetEvent).getDropAction()));
            }
            object = stringBuilder.toString();
            if (!((String)object).equals(this.lastAction)) {
                LOG.log(Level.FINE, (String)object);
                this.lastAction = object;
            }
        }
    }

    protected int acceptOrReject(DropTargetDragEvent dropTargetDragEvent) {
        int n2 = this.getDropAction(dropTargetDragEvent);
        if (n2 != 0) {
            dropTargetDragEvent.acceptDrag(n2);
        } else {
            dropTargetDragEvent.rejectDrag();
        }
        return n2;
    }

    @Override
    public void dragEnter(DropTargetDragEvent dropTargetDragEvent) {
        this.describe("enter(tgt)", dropTargetDragEvent);
        int n2 = this.acceptOrReject(dropTargetDragEvent);
        this.paintDropTarget(dropTargetDragEvent, n2, dropTargetDragEvent.getLocation());
    }

    @Override
    public void dragOver(DropTargetDragEvent dropTargetDragEvent) {
        this.describe("over(tgt)", dropTargetDragEvent);
        int n2 = this.acceptOrReject(dropTargetDragEvent);
        this.paintDropTarget(dropTargetDragEvent, n2, dropTargetDragEvent.getLocation());
    }

    @Override
    public void dragExit(DropTargetEvent dropTargetEvent) {
        this.describe("exit(tgt)", dropTargetEvent);
        this.paintDropTarget(dropTargetEvent, 0, null);
    }

    @Override
    public void dropActionChanged(DropTargetDragEvent dropTargetDragEvent) {
        this.describe("change(tgt)", dropTargetDragEvent);
        int n2 = this.acceptOrReject(dropTargetDragEvent);
        this.paintDropTarget(dropTargetDragEvent, n2, dropTargetDragEvent.getLocation());
    }

    @Override
    public void drop(DropTargetDropEvent dropTargetDropEvent) {
        this.describe("drop(tgt)", dropTargetDropEvent);
        int n2 = this.getDropAction(dropTargetDropEvent);
        if (n2 != 0) {
            dropTargetDropEvent.acceptDrop(n2);
            try {
                this.drop(dropTargetDropEvent, n2);
                dropTargetDropEvent.dropComplete(true);
            }
            catch (Exception exception) {
                dropTargetDropEvent.dropComplete(false);
            }
        } else {
            dropTargetDropEvent.rejectDrop();
        }
        this.paintDropTarget(dropTargetDropEvent, 0, dropTargetDropEvent.getLocation());
    }

    protected boolean isSupported(DataFlavor[] object) {
        object = new HashSet<DataFlavor>(Arrays.asList(object));
        object.retainAll(this.acceptedFlavors);
        return !object.isEmpty();
    }

    protected void paintDropTarget(DropTargetEvent dropTargetEvent, int n2, Point point) {
        if (this.painter != null) {
            this.painter.paintDropTarget(dropTargetEvent, n2, point);
        }
    }

    protected boolean canDrop(DropTargetEvent dropTargetEvent, int n2, Point point) {
        return true;
    }

    protected abstract void drop(DropTargetDropEvent var1, int var2) throws UnsupportedFlavorException, IOException;
}

