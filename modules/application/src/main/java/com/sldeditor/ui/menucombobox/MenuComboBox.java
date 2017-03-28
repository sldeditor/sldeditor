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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;

import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.common.vendoroption.VendorOptionUpdateInterface;
import com.sldeditor.common.vendoroption.VendorOptionManager;
import com.sldeditor.common.vendoroption.VersionData;
import com.sldeditor.ui.iface.ValueComboBoxDataSelectedInterface;
import com.sldeditor.ui.widgets.ValueComboBoxData;
import com.sldeditor.ui.widgets.ValueComboBoxDataGroup;

/**
 * The Class MenuComboBox.
 * 
 * @author Robert Ward (SCISYS)
 */
public class MenuComboBox extends JMenuBar implements VendorOptionUpdateInterface {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The menu. */
    private JMenu menu;

    /** The selected data. */
    private ValueComboBoxData selectedData = null;

    /** The preferred size. */
    private Dimension preferredSize;

    /** The data map. */
    private Map<String, ValueComboBoxData> dataMap = new HashMap<String, ValueComboBoxData>();

    /** The listener. */
    private ValueComboBoxDataSelectedInterface listener = null;

    /** The first value. */
    private ValueComboBoxData firstValue = null;

    /** The data selection list. */
    private List<ValueComboBoxDataGroup> dataSelectionList = null;

    /** The vendor option versions list. */
    private List<VersionData> vendorOptionVersionsList = null;

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
        ComboMenu menu = new ComboMenu("");

        if (listAll != null) {
            for (ValueComboBoxDataGroup group : listAll) {
                if (group.isSubMenu()) {
                    JMenu subMenu = new JMenu(group.getGroupName());
                    for (ValueComboBoxData data : group.getDataList()) {
                        if (VendorOptionManager.getInstance().isAllowed(vendorOptionVersionsList,
                                data.getVendorOption())) {
                            ComboMenuItem menuItem = new ComboMenuItem(data);

                            subMenu.add(menuItem);

                            dataMap.put(data.getKey(), data);

                            if (firstValue == null) {
                                firstValue = data;
                            }
                        }
                    }

                    if (subMenu.getMenuComponentCount() > 1) {
                        menu.add(subMenu);
                    }
                } else {
                    for (ValueComboBoxData data : group.getDataList()) {
                        if (VendorOptionManager.getInstance().isAllowed(vendorOptionVersionsList,
                                data.getVendorOption())) {
                            ComboMenuItem menuItem = new ComboMenuItem(data);

                            menu.add(menuItem);

                            dataMap.put(data.getKey(), data);

                            if (firstValue == null) {
                                firstValue = data;
                            }
                        }
                    }
                }
            }
        }
        return menu;
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
     * The listener interface for receiving menuItem events. 
     * The class that is interested in processing a menuItem event implements this interface,
     * and the object created with that class is registered with 
     * a component using the component's <code>addMenuItemListener<code> method. When the
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
            JMenu menu = (JMenu) item;
            int n = menu.getItemCount();
            for (int i = 0; i < n; i++) {
                setListener(menu.getItem(i), listener);
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
    public void setPreferredSize(Dimension size) {
        preferredSize = size;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.JComponent#getPreferredSize()
     */
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

    /**
     * The main method.
     *
     * @param args the arguments
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception evt) {
            // Do nothing
        }

        List<ValueComboBoxData> list1 = new ArrayList<ValueComboBoxData>();
        list1.add(new ValueComboBoxData("circle", "Circle", MenuComboBox.class));
        ValueComboBoxData square = new ValueComboBoxData("square", "Square", MenuComboBox.class);
        list1.add(square);
        list1.add(new ValueComboBoxData("triangle", "Triangle", MenuComboBox.class));

        List<ValueComboBoxData> list2 = new ArrayList<ValueComboBoxData>();
        list2.add(new ValueComboBoxData("shp://lArrow", "shp://lArrow", MenuComboBox.class));
        list2.add(new ValueComboBoxData("shp://star", "shp://star", MenuComboBox.class));
        list2.add(new ValueComboBoxData("shp://dot", "shp://dot", MenuComboBox.class));

        List<ValueComboBoxDataGroup> listAll = new ArrayList<ValueComboBoxDataGroup>();
        listAll.add(new ValueComboBoxDataGroup("", list1, false));
        listAll.add(new ValueComboBoxDataGroup("Shapes", list2, true));

        MenuComboBox comboMenu = new MenuComboBox(null);
        comboMenu.initialiseMenu(listAll);

        comboMenu.setSelectedData(square);

        JFrame frame = new JFrame();
        JPanel panel = new JPanel();
        panel.add(comboMenu);
        frame.getContentPane().setLayout(new FlowLayout());
        frame.getContentPane().add(panel);

        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        frame.setSize(370, 100);
        frame.setVisible(true);
    }

    /**
     * The Class ComboMenuItem.
     */
    public static class ComboMenuItem extends JMenuItem {

        /** The Constant serialVersionUID. */
        private static final long serialVersionUID = 1L;

        /** The data. */
        private ValueComboBoxData data = null;

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

    /**
     * The Class ComboMenu.
     */
    public static class ComboMenu extends JMenu {

        /** The Constant serialVersionUID. */
        private static final long serialVersionUID = 1L;

        /** The icon renderer. */
        private ArrowIcon iconRenderer;

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

        /*
         * (non-Javadoc)
         * 
         * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
         */
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

    /**
     * Refresh menu.
     */
    private void refreshMenu() {
        if (menu != null) {
            // Remove the previous menu
            this.remove(menu);
        }
        this.menu = createMenu(dataSelectionList);

        Color color = UIManager.getColor("Menu.selectionBackground");
        UIManager.put("Menu.selectionBackground", UIManager.getColor("Menu.background"));
        menu.updateUI();
        UIManager.put("Menu.selectionBackground", color);

        MenuItemListener listener = new MenuItemListener();
        setListener(menu, listener);

        add(menu);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.preferences.iface.PrefUpdateVendorOptionInterface#vendorOptionsUpdated(java.util.List)
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
