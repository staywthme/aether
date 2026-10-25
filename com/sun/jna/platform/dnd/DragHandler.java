/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna.platform.dnd;

import com.sun.jna.Platform;
import com.sun.jna.platform.dnd.GhostedDragImage;
import java.awt.AlphaComposite;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Point;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceContext;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.DragSourceMotionListener;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.InvalidDnDOperationException;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.text.JTextComponent;

public abstract class DragHandler
implements DragGestureListener,
DragSourceListener,
DragSourceMotionListener {
    private static final Logger LOG = Logger.getLogger(DragHandler.class.getName());
    public static final Dimension MAX_GHOST_SIZE = new Dimension(250, 250);
    public static final float DEFAULT_GHOST_ALPHA = 0.5f;
    public static final int UNKNOWN_MODIFIERS = -1;
    public static final Transferable UNKNOWN_TRANSFERABLE = null;
    protected static final int MOVE = 2;
    protected static final int COPY = 1;
    protected static final int LINK = 0x40000000;
    protected static final int NONE = 0;
    static final int MOVE_MASK = 64;
    static final boolean OSX = Platform.isMac();
    static final int COPY_MASK = OSX ? 512 : 128;
    static final int LINK_MASK = OSX ? 768 : 192;
    static final int KEY_MASK = 9152;
    private static int modifiers = -1;
    private static Transferable transferable = UNKNOWN_TRANSFERABLE;
    private int supportedActions;
    private boolean fixCursor = true;
    private Component dragSource;
    private GhostedDragImage ghost;
    private Point imageOffset;
    private Dimension maxGhostSize = MAX_GHOST_SIZE;
    private float ghostAlpha = 0.5f;
    private String lastAction;
    private boolean moved;

    static int getModifiers() {
        return modifiers;
    }

    public static Transferable getTransferable(DropTargetEvent dropTargetEvent) {
        if (dropTargetEvent instanceof DropTargetDragEvent) {
            try {
                return ((DropTargetDragEvent)dropTargetEvent).getTransferable();
            }
            catch (Exception exception) {
            }
        } else if (dropTargetEvent instanceof DropTargetDropEvent) {
            return ((DropTargetDropEvent)dropTargetEvent).getTransferable();
        }
        return transferable;
    }

    protected DragHandler(Component component, int n2) {
        this.dragSource = component;
        this.supportedActions = n2;
        try {
            String[] stringArray = System.getProperty("DragHandler.alpha");
            if (stringArray != null) {
                try {
                    this.ghostAlpha = Float.parseFloat((String)stringArray);
                }
                catch (NumberFormatException numberFormatException) {}
            }
            if ((stringArray = System.getProperty("DragHandler.maxDragImageSize")) != null && (stringArray = stringArray.split("x")).length == 2) {
                try {
                    this.maxGhostSize = new Dimension(Integer.parseInt(stringArray[0]), Integer.parseInt(stringArray[1]));
                }
                catch (NumberFormatException numberFormatException) {}
            }
        }
        catch (SecurityException securityException) {}
        this.disableSwingDragSupport(component);
        DragSource dragSource = DragSource.getDefaultDragSource();
        dragSource.createDefaultDragGestureRecognizer(component, this.supportedActions, this);
    }

    private void disableSwingDragSupport(Component component) {
        if (component instanceof JTree) {
            ((JTree)component).setDragEnabled(false);
            return;
        }
        if (component instanceof JList) {
            ((JList)component).setDragEnabled(false);
            return;
        }
        if (component instanceof JTable) {
            ((JTable)component).setDragEnabled(false);
            return;
        }
        if (component instanceof JTextComponent) {
            ((JTextComponent)component).setDragEnabled(false);
            return;
        }
        if (component instanceof JColorChooser) {
            ((JColorChooser)component).setDragEnabled(false);
            return;
        }
        if (component instanceof JFileChooser) {
            ((JFileChooser)component).setDragEnabled(false);
        }
    }

    protected boolean canDrag(DragGestureEvent dragGestureEvent) {
        int n2 = dragGestureEvent.getTriggerEvent().getModifiersEx() & 0x23C0;
        if (n2 == 64) {
            return (this.supportedActions & 2) != 0;
        }
        if (n2 == COPY_MASK) {
            return (this.supportedActions & 1) != 0;
        }
        if (n2 == LINK_MASK) {
            return (this.supportedActions & 0x40000000) != 0;
        }
        return true;
    }

    protected void setModifiers(int n2) {
        modifiers = n2;
    }

    protected abstract Transferable getTransferable(DragGestureEvent var1);

    protected Icon getDragIcon(DragGestureEvent dragGestureEvent, Point point) {
        return null;
    }

    protected void dragStarted(DragGestureEvent dragGestureEvent) {
    }

    @Override
    public void dragGestureRecognized(DragGestureEvent dragGestureEvent) {
        block7: {
            if ((dragGestureEvent.getDragAction() & this.supportedActions) != 0 && this.canDrag(dragGestureEvent)) {
                this.setModifiers(dragGestureEvent.getTriggerEvent().getModifiersEx() & 0x23C0);
                Transferable transferable = this.getTransferable(dragGestureEvent);
                if (transferable == null) {
                    return;
                }
                try {
                    Object object = new Point(0, 0);
                    Object object2 = this.getDragIcon(dragGestureEvent, (Point)object);
                    Point point = dragGestureEvent.getDragOrigin();
                    this.imageOffset = new Point(((Point)object).x - point.x, ((Point)object).y - point.y);
                    object = this.scaleDragIcon((Icon)object2, this.imageOffset);
                    if (object != null && DragSource.isDragImageSupported()) {
                        object2 = dragGestureEvent.getComponent().getGraphicsConfiguration();
                        dragGestureEvent.startDrag(null, this.createDragImage((GraphicsConfiguration)object2, (Icon)object), this.imageOffset, transferable, this);
                    } else {
                        if (object != null) {
                            object2 = this.dragSource.getLocationOnScreen();
                            ((Point)object2).translate(point.x, point.y);
                            point = new Point(-this.imageOffset.x, -this.imageOffset.y);
                            this.ghost = new GhostedDragImage(this.dragSource, (Icon)object, this.getImageLocation((Point)object2), point);
                            this.ghost.setAlpha(this.ghostAlpha);
                        }
                        dragGestureEvent.startDrag(null, transferable, this);
                    }
                    this.dragStarted(dragGestureEvent);
                    this.moved = false;
                    dragGestureEvent.getDragSource().addDragSourceMotionListener(this);
                    DragHandler.transferable = transferable;
                    return;
                }
                catch (InvalidDnDOperationException invalidDnDOperationException) {
                    if (this.ghost == null) break block7;
                    this.ghost.dispose();
                    this.ghost = null;
                }
            }
        }
    }

    protected Icon scaleDragIcon(Icon icon, Point point) {
        return icon;
    }

    protected Image createDragImage(GraphicsConfiguration object, Icon icon) {
        int n2 = icon.getIconWidth();
        int n3 = icon.getIconHeight();
        object = ((GraphicsConfiguration)object).createCompatibleImage(n2, n3, 3);
        Graphics2D graphics2D = (Graphics2D)((BufferedImage)object).getGraphics();
        graphics2D.setComposite(AlphaComposite.Clear);
        graphics2D.fillRect(0, 0, n2, n3);
        graphics2D.setComposite(AlphaComposite.getInstance(2, this.ghostAlpha));
        icon.paintIcon(this.dragSource, graphics2D, 0, 0);
        graphics2D.dispose();
        return object;
    }

    private int reduce(int n2) {
        if ((n2 & 2) != 0 && n2 != 2) {
            return 2;
        }
        if ((n2 & 1) != 0 && n2 != 1) {
            return 1;
        }
        return n2;
    }

    protected Cursor getCursorForAction(int n2) {
        switch (n2) {
            case 2: {
                return DragSource.DefaultMoveDrop;
            }
            case 1: {
                return DragSource.DefaultCopyDrop;
            }
            case 0x40000000: {
                return DragSource.DefaultLinkDrop;
            }
        }
        return DragSource.DefaultMoveNoDrop;
    }

    protected int getAcceptableDropAction(int n2) {
        DragHandler dragHandler = this;
        return dragHandler.reduce(dragHandler.supportedActions & n2);
    }

    protected int getDropAction(DragSourceEvent dragSourceEvent) {
        if (dragSourceEvent instanceof DragSourceDragEvent) {
            dragSourceEvent = (DragSourceDragEvent)dragSourceEvent;
            return ((DragSourceDragEvent)dragSourceEvent).getDropAction();
        }
        if (dragSourceEvent instanceof DragSourceDropEvent) {
            return ((DragSourceDropEvent)dragSourceEvent).getDropAction();
        }
        return 0;
    }

    protected int adjustDropAction(DragSourceEvent dragSourceEvent) {
        int n2 = this.getDropAction(dragSourceEvent);
        if (dragSourceEvent instanceof DragSourceDragEvent) {
            int n3;
            dragSourceEvent = (DragSourceDragEvent)dragSourceEvent;
            if (n2 == 0 && (n3 = ((DragSourceDragEvent)dragSourceEvent).getGestureModifiersEx() & 0x23C0) == 0) {
                n2 = this.getAcceptableDropAction(((DragSourceDragEvent)dragSourceEvent).getTargetActions());
            }
        }
        return n2;
    }

    protected void updateCursor(DragSourceEvent dragSourceEvent) {
        if (!this.fixCursor) {
            return;
        }
        DragHandler dragHandler = this;
        Cursor cursor = dragHandler.getCursorForAction(dragHandler.adjustDropAction(dragSourceEvent));
        dragSourceEvent.getDragSourceContext().setCursor(cursor);
    }

    static String actionString(int n2) {
        switch (n2) {
            case 2: {
                return "MOVE";
            }
            case 3: {
                return "MOVE|COPY";
            }
            case 0x40000002: {
                return "MOVE|LINK";
            }
            case 0x40000003: {
                return "MOVE|COPY|LINK";
            }
            case 1: {
                return "COPY";
            }
            case 0x40000001: {
                return "COPY|LINK";
            }
            case 0x40000000: {
                return "LINK";
            }
        }
        return "NONE";
    }

    private void describe(String object, DragSourceEvent object2) {
        if (LOG.isLoggable(Level.FINE)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("drag: ");
            stringBuilder.append((String)object);
            object = ((DragSourceEvent)object2).getDragSourceContext();
            if (object2 instanceof DragSourceDragEvent) {
                object2 = (DragSourceDragEvent)object2;
                stringBuilder.append(": src=");
                stringBuilder.append(DragHandler.actionString(((DragSourceContext)object).getSourceActions()));
                stringBuilder.append(" usr=");
                stringBuilder.append(DragHandler.actionString(((DragSourceDragEvent)object2).getUserAction()));
                stringBuilder.append(" tgt=");
                stringBuilder.append(DragHandler.actionString(((DragSourceDragEvent)object2).getTargetActions()));
                stringBuilder.append(" act=");
                stringBuilder.append(DragHandler.actionString(((DragSourceDragEvent)object2).getDropAction()));
                stringBuilder.append(" mods=");
                stringBuilder.append(((DragSourceDragEvent)object2).getGestureModifiersEx());
            } else {
                stringBuilder.append(": e=");
                stringBuilder.append(object2);
            }
            object2 = stringBuilder.toString();
            if (!((String)object2).equals(this.lastAction)) {
                LOG.log(Level.FINE, (String)object2);
                this.lastAction = object2;
            }
        }
    }

    @Override
    public void dragDropEnd(DragSourceDropEvent serializable) {
        this.describe("end", (DragSourceEvent)serializable);
        this.setModifiers(-1);
        transferable = UNKNOWN_TRANSFERABLE;
        if (this.ghost != null) {
            if (serializable.getDropSuccess()) {
                this.ghost.dispose();
            } else {
                this.ghost.returnToOrigin();
            }
            this.ghost = null;
        }
        serializable = serializable.getDragSourceContext().getDragSource();
        ((DragSource)serializable).removeDragSourceMotionListener(this);
        this.moved = false;
    }

    private Point getImageLocation(Point point) {
        point.translate(this.imageOffset.x, this.imageOffset.y);
        return point;
    }

    @Override
    public void dragEnter(DragSourceDragEvent dragSourceDragEvent) {
        this.describe("enter", dragSourceDragEvent);
        if (this.ghost != null) {
            this.ghost.move(this.getImageLocation(dragSourceDragEvent.getLocation()));
        }
        this.updateCursor(dragSourceDragEvent);
    }

    @Override
    public void dragMouseMoved(DragSourceDragEvent dragSourceDragEvent) {
        this.describe("move", dragSourceDragEvent);
        if (this.ghost != null) {
            this.ghost.move(this.getImageLocation(dragSourceDragEvent.getLocation()));
        }
        if (this.moved) {
            this.updateCursor(dragSourceDragEvent);
        }
        this.moved = true;
    }

    @Override
    public void dragOver(DragSourceDragEvent dragSourceDragEvent) {
        this.describe("over", dragSourceDragEvent);
        if (this.ghost != null) {
            this.ghost.move(this.getImageLocation(dragSourceDragEvent.getLocation()));
        }
        this.updateCursor(dragSourceDragEvent);
    }

    @Override
    public void dragExit(DragSourceEvent dragSourceEvent) {
        this.describe("exit", dragSourceEvent);
    }

    @Override
    public void dropActionChanged(DragSourceDragEvent dragSourceDragEvent) {
        this.describe("change", dragSourceDragEvent);
        this.setModifiers(dragSourceDragEvent.getGestureModifiersEx() & 0x23C0);
        if (this.ghost != null) {
            this.ghost.move(this.getImageLocation(dragSourceDragEvent.getLocation()));
        }
        this.updateCursor(dragSourceDragEvent);
    }
}

