package com.rajendarreddyj.basics.swing.demo;

import java.awt.Color;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicButtonUI;

/**
 * 
 * This class provides a JCheckBoxMenuItrem Functionality, optionally working like a JMenuItem, primarily used with
 * XJPopupMenu. Rationale for development of this component was the inability of a JMenuItem to work in a Scrollable
 * Popup menu as in XJPopupMenu
 * 
 * @author rajendarreddy
 *
 */

public class XCheckedButton extends JButton {

    private static final long serialVersionUID = 1L;

    // Icon to be used to for the Checked Icon of the Button
    private static ImageIcon checkedIcon;

    /**
     * These colors are required in order to simulate the JMenuItem's L&F
     */
    public static final Color MENU_HIGHLIGHT_BG_COLOR = UIManager.getColor("MenuItem.selectionBackground");
    public static final Color MENU_HIGHLIGHT_FG_COLOR = UIManager.getColor("MenuItem.selectionForeground");
    public static final Color MENUITEM_BG_COLOR = UIManager.getColor("MenuItem.background");
    public static final Color MENUITEM_FG_COLOR = UIManager.getColor("MenuItem.foreground");

    // This property if set to false, will result in the checked Icon not being displayed when the button is selected
    private boolean displayCheck = true;

    public XCheckedButton() {
        super();
        this.init();

    }

    public XCheckedButton(final Action a) {
        super(a);
        this.init();
    }

    public XCheckedButton(final Icon icon) {
        super(icon);
        this.init();
    }

    public XCheckedButton(final String text, final Icon icon) {
        super(text, icon);
        this.init();
    }

    public XCheckedButton(final String text) {
        super(text);
        this.init();
    }

    /**
     * Initialize component LAF and add Listeners
     */
    private void init() {
        MouseAdapter mouseAdapter = this.getMouseAdapter();

        // Basically JGoodies LAF UI for JButton does not allow Background color to be set.
        // So we need to set the default UI,
        ComponentUI ui = BasicButtonUI.createUI(this);
        this.setUI(ui);
        this.setBorder(BorderFactory.createEmptyBorder(3, 0, 3, 2));
        this.setMenuItemDefaultColors();
        // setContentAreaFilled(false);
        this.setHorizontalTextPosition(SwingConstants.RIGHT);
        this.setHorizontalAlignment(SwingConstants.LEFT);
        // setModel(new JToggleButton.ToggleButtonModel());
        this.setModel(new XCheckedButtonModel());
        this.setSelected(false);
        this.addMouseListener(mouseAdapter);

    }

    private void setMenuItemDefaultColors() {
        XCheckedButton.this.setBackground(MENUITEM_BG_COLOR);
        XCheckedButton.this.setForeground(MENUITEM_FG_COLOR);
    }

    /**
     * @return
     */
    private MouseAdapter getMouseAdapter() {
        return new MouseAdapter() {
            /*
            * For static menuitems, the background color remains the highlighted color, if this is not overridden
            */
            @Override
            public void mousePressed(final MouseEvent e) {
                XCheckedButton.this.setMenuItemDefaultColors();
            }

            @Override
            public void mouseEntered(final MouseEvent e) {
                XCheckedButton.this.setBackground(MENU_HIGHLIGHT_BG_COLOR);
                XCheckedButton.this.setForeground(MENU_HIGHLIGHT_FG_COLOR);
            }

            @Override
            public void mouseExited(final MouseEvent e) {
                XCheckedButton.this.setMenuItemDefaultColors();
            }

        };
    }

    /**
     * @param checkedFlag
     */
    public void displayIcon(final boolean checkedFlag) {
        if (checkedFlag && this.isDisplayCheck()) {
            if (checkedIcon == null) {
                checkedIcon = new ImageIcon("src/main/resources/images/check.gif");
            }
            this.setIcon(checkedIcon);
        } else {
            this.setIcon(XConstant.EMPTY_IMAGE_ICON);
        }
        this.repaint();
    }

    private class XCheckedButtonModel extends JToggleButton.ToggleButtonModel {
        private static final long serialVersionUID = 1L;

        /*
        * Need to Override keeping the super code, else the check mark won't come  
        */
        @Override
        public void setSelected(boolean b) {

            ButtonGroup group = this.getGroup();
            if (group != null) {
                // use the group model instead
                group.setSelected(this, b);
                b = group.isSelected(this);
            }

            if (this.isSelected() == b) {
                return;
            }

            if (b) {
                this.stateMask |= SELECTED;
            } else {
                this.stateMask &= ~SELECTED;
            }

            // Send ChangeEvent
            this.fireStateChanged();

            // Send ItemEvent
            this.fireItemStateChanged(new ItemEvent(this, ItemEvent.ITEM_STATE_CHANGED, this, this.isSelected() ? ItemEvent.SELECTED : ItemEvent.DESELECTED));

            XCheckedButton.this.displayIcon(b);

        }

    }

    /**
     * Returns true if Button will display Checked Icon on Click. Default Behaviour is to display a Checked Icon
     * 
     * @return
     */
    public boolean isDisplayCheck() {
        return this.displayCheck;
    }

    /**
     * Sets the property which determines whether a checked Icon should be displayed or not Setting to false, makes this
     * button display like a normal button
     * 
     * @param displayCheck
     */
    public void setDisplayCheck(final boolean displayCheck) {
        this.displayCheck = displayCheck;
    }

}
