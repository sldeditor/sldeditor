/*
 * SLD Editor - The Open Source Java SLD Editor
 *
 * Copyright (C) 2016, SCISYS UK Limited
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.sldeditor.ui.menucombobox;

import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.common.vendoroption.VendorOptionManager;
import com.sldeditor.common.vendoroption.VendorOptionUpdateInterface;
import com.sldeditor.common.vendoroption.VersionData;
import com.sldeditor.ui.iface.ValueComboBoxDataSelectedInterface;
import com.sldeditor.ui.widgets.ValueComboBoxData;
import com.sldeditor.ui.widgets.ValueComboBoxDataGroup;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;

/**
 * The Class MenuComboBox.
 *
 * @author Robert Ward (SCISYS)
 */
public class MenuComboBox extends JMenuBar implements VendorOptionUpdateInterface {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The Constant MENU_SELECTION_BACKGROUND. */
    private static final String MENU_SELECTION_BACKGROUND = "Menu.selectionBackground";

    /** The menu. */
    private JMenu menu;

    /** The selected data. */
    private transient ValueComboBoxData selectedData = null;

    /** The preferred size. */
    private Dimension preferredSize;

    /** The data map. */
    private transient Map<String, ValueComboBoxData> dataMap = new HashMap<>();

    /** The listener. */
    private transient ValueComboBoxDataSelectedInterface listener = null;

    /** The first value. */
    private transient ValueComboBoxData firstValue = null;

    /** The data selection list. */
    private transient List<ValueComboBoxDataGroup> dataSelectionList = null;

    /** The vendor option versions list. */
    private transient List<VersionData> vendorOptionVersionsList = null;

    /**
     * Instantiates a new menu combo box.
     *
     * @param listener the listener
     */
    public MenuComboBox(ValueComboBoxDataSelectedInterface listener) {
        this.listener = listener;
    }

    /**
     * Creates the menu.
     *
     * @param listAll the list all
     * @return the combo menu
     */
    private ComboMenu createMenu(List<ValueComboBoxDataGroup> listAll) {
        ComboMenu localMenu = new ComboMenu("");

        if (listAll != null) {
            for (ValueComboBoxDataGroup group : listAll) {
                if (group.isSubMenu()) {
                    createSubMenu(localMenu, group);
                } else {
                    createMenuItem(localMenu, group);
                }
            }
        }
        return localMenu;
    }

    /**
     * Creates the menu item.
     *
     * @param localMenu the local menu
     * @param group the group
     */
    private void createMenuItem(ComboMenu localMenu, ValueComboBoxDataGroup group) {
        for (ValueComboBoxData data : group.getDataList()) {
            if (VendorOptionManager.getInstance()
                    .isAllowed(vendorOptionVersionsList, data.getVendorOption())) {
                ComboMenuItem menuItem = new ComboMenuItem(data);

                localMenu.add(menuItem);

                dataMap.put(data.getKey(), data);

                if (firstValue == null) {
                    firstValue = data;
                }
            }
        }
    }

    /**
     * Creates the sub menu.
     *
     * @param localMenu the local menu
     * @param group the group
     */
    private void createSubMenu(ComboMenu localMenu, ValueComboBoxDataGroup group) {
        JMenu subMenu = new JMenu(group.getGroupName());
        for (ValueComboBoxData data : group.getDataList()) {
            if (VendorOptionManager.getInstance()
                    .isAllowed(vendorOptionVersionsList, data.getVendorOption())) {
                ComboMenuItem menuItem = new ComboMenuItem(data);

                subMenu.add(menuItem);

                dataMap.put(data.getKey(), data);

                if (firstValue == null) {
                    firstValue = data;
                }
            }
        }

        if (subMenu.getMenuComponentCount() > 1) {
            localMenu.add(subMenu);
        }
    }

    /**
     * Gets the selected data.
     *
     * @return the selected data
     */
    public ValueComboBoxData getSelectedData() {
        return selectedData;
    }

    /**
     * Sets the selected data.
     *
     * @param selectedData the new selected data
     */
    public void setSelectedData(ValueComboBoxData selectedData) {
        this.selectedData = selectedData;

        if (selectedData != null) {
            this.menu.setText(selectedData.getText());
            this.menu.requestFocus();
        }

        if (listener != null) {
            listener.optionSelected(selectedData);
        }
    }

    /**
     * The listener interface for receiving menuItem events. The class that is interested in
     * processing a menuItem event implements this interface, and the object created with that class
     * is registered with a component using the component's addMenuItemListener method. When the
     * menuItem event occurs, that object's appropriate method is invoked.
     *
     * @see MenuItemEvent
     */
    class MenuItemListener implements ActionListener {

        /*
         * (non-Javadoc)
         *
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        public void actionPerformed(ActionEvent e) {
            ComboMenuItem item = (ComboMenuItem) e.getSource();

            setSelectedData(item.getData());
        }
    }

    /**
     * Sets the listener.
     *
     * @param item the item
     * @param listener the listener
     */
    private void setListener(JMenuItem item, ActionListener listener) {
        if (item instanceof JMenu) {
            JMenu localMenu = (JMenu) item;
            int n = localMenu.getItemCount();
            for (int i = 0; i < n; i++) {
                setListener(localMenu.getItem(i), listener);
            }
        } else if (item != null) { // null means separator
            item.addActionListener(listener);
        }
    }

    /**
     * Gets the selected item.
     *
     * @return the selected item
     */
    public String getSelectedItem() {
        return menu.getText();
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.swing.JComponent#setPreferredSize(java.awt.Dimension)
     */
    @Override
    public void setPreferredSize(Dimension size) {
        preferredSize = size;
    }

    /**
     * (non-Javadoc)
     *
     * @see javax.swing.JComponent#getPreferredSize()
     */
    @Override
    public Dimension getPreferredSize() {
        if (preferredSize == null) {
            Dimension menuD = getItemSize(menu);
            Insets margin = menu.getMargin();
            Dimension retD = new Dimension(menuD.width, margin.top + margin.bottom + menuD.height);
            menu.setPreferredSize(retD);
            preferredSize = retD;
        }
        return preferredSize;
    }

    /**
     * Gets the item size.
     *
     * @param menu the menu
     * @return the item size
     */
    private Dimension getItemSize(JMenu menu) {
        Dimension d = new Dimension(0, 0);
        int n = menu.getItemCount();
        for (int i = 0; i < n; i++) {
            Dimension itemD;
            JMenuItem item = menu.getItem(i);
            if (item instanceof JMenu) {
                itemD = getItemSize((JMenu) item);
            } else if (item != null) {
                itemD = item.getPreferredSize();
            } else {
                itemD = new Dimension(0, 0); // separator
            }
            d.width = Math.max(d.width, itemD.width);
            d.height = Math.max(d.height, itemD.height);
        }
        return d;
    }

    /** The Class ComboMenuItem. */
    public static class ComboMenuItem extends JMenuItem {

        /** The Constant serialVersionUID. */
        private static final long serialVersionUID = 1L;

        /** The data. */
        private transient ValueComboBoxData data = null;

        /**
         * Instantiates a new combo menu item.
         *
         * @param data the data
         */
        public ComboMenuItem(ValueComboBoxData data) {
            super(data.getText());
            this.data = data;
        }

        /**
         * Gets the data.
         *
         * @return the data
         */
        public ValueComboBoxData getData() {
            return this.data;
        }
    }

    /** The Class ComboMenu. */
    public static class ComboMenu extends JMenu {

        /** The Constant serialVersionUID. */
        private static final long serialVersionUID = 1L;

        /** The icon renderer. */
        private transient ArrowIcon iconRenderer;

        /**
         * Instantiates a new combo menu.
         *
         * @param label the label
         */
        public ComboMenu(String label) {
            super(label);
            iconRenderer = new ArrowIcon(SwingConstants.SOUTH, true);
            setBorder(new EtchedBorder());
            setIcon(new BlankIcon(null, 11));
            setHorizontalTextPosition(JButton.LEFT);
            setFocusPainted(true);
        }

        /**
         * (non-Javadoc)
         *
         * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
         */
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Dimension d = this.getPreferredSize();
            int x = Math.max(0, d.width - iconRenderer.getIconWidth() - 3);
            int y = Math.max(0, (d.height - iconRenderer.getIconHeight()) / 2 - 2);
            iconRenderer.paintIcon(this, g, x, y);
        }
    }

    /**
     * Initialise menu.
     *
     * @param dataSelectionList the data selection list
     */
    public void initialiseMenu(List<ValueComboBoxDataGroup> dataSelectionList) {

        this.dataSelectionList = dataSelectionList;

        refreshMenu();
    }

    /** Refresh menu. */
    private void refreshMenu() {
        if (menu != null) {
            // Remove the previous menu
            this.remove(menu);
        }
        this.menu = createMenu(dataSelectionList);

        Color color = UIManager.getColor(MENU_SELECTION_BACKGROUND);
        UIManager.put(MENU_SELECTION_BACKGROUND, UIManager.getColor("Menu.background"));
        menu.updateUI();
        UIManager.put(MENU_SELECTION_BACKGROUND, color);

        MenuItemListener localListener = new MenuItemListener();
        setListener(menu, localListener);

        add(menu);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.sldeditor.preferences.iface.PrefUpdateVendorOptionInterface#vendorOptionsUpdated(java.
     * util.List)
     */
    @Override
    public void vendorOptionsUpdated(List<VersionData> vendorOptionVersionsList) {
        this.vendorOptionVersionsList = vendorOptionVersionsList;

        refreshMenu();
    }

    /**
     * Gets the default value.
     *
     * @return the default value
     */
    public ValueComboBoxData getDefaultValue() {
        return firstValue;
    }

    /**
     * Sets the selected data key.
     *
     * @param key the new selected data key
     */
    public void setSelectedDataKey(String key) {
        ValueComboBoxData value = dataMap.get(key);
        if (value == null) {
            ConsoleManager.getInstance().error(this, "Unknown menu combo box key : " + key);
        } else {
            setSelectedData(value);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.swing.JComponent#setEnabled(boolean)
     */
    @Override
    public void setEnabled(boolean enabled) {
        if (this.menu != null) {
            this.menu.setEnabled(enabled);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see java.awt.Component#isEnabled()
     */
    @Override
    public boolean isEnabled() {
        if (this.menu != null) {
            return this.menu.isEnabled();
        }
        return false;
    }
}
