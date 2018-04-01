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

package com.sldeditor.filter.v2.expression;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.tree.DefaultMutableTreeNode;

import org.geotools.filter.LogicFilterImpl;
import org.opengis.filter.Filter;

import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.filter.v2.function.FilterField;
import com.sldeditor.filter.v2.function.FilterManager;
import com.sldeditor.ui.attribute.SubPanelUpdatedInterface;

/**
 * The Class FilterSubPanel.
 *
 * @author Robert Ward (SCISYS)
 */
public class FilterSubPanel extends JPanel {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The box. */
    private Box box;

    /** The panel filter. */
    private JPanel panelFilter;

    /** The function panel. */
    private FilterField filterPanel;

    /** The selected node. */
    private FilterNode selectedNode = null;

    /** The parent. */
    private ExpressionFilterInterface parent = null;

    /** The lbl filter. */
    private JLabel lblFilter;

    /** The btn apply. */
    private JButton btnApply;

    /** The btn revert. */
    private JButton btnRevert;

    /** The btn add filter. */
    private JButton btnAddFilter;

    /** The btn remove filter. */
    private JButton btnRemoveFilter;

    /**
     * Instantiates a new expression panel.
     *
     * @param parent the parent
     */
    public FilterSubPanel(ExpressionFilterInterface parent) {
        this.parent = parent;
        createUI();
    }

    /**
     * Creates the ui.
     */
    private void createUI() {
        setLayout(new BorderLayout());
        box = Box.createVerticalBox();
        add(box, BorderLayout.CENTER);

        //
        // Filter panel
        //
        panelFilter = new JPanel(new FlowLayout());

        lblFilter = new JLabel(
                Localisation.getString(ExpressionPanelv2.class, "ExpressionPanelv2.filter"));
        panelFilter.add(lblFilter);

        filterPanel = new FilterField(new SubPanelUpdatedInterface() {
            @Override
            public void updateSymbol() {
                updateButtonState(true);
            }

            @Override
            public void parameterAdded() {
                // Do nothing
            }
        }, FilterManager.getInstance());
        panelFilter.add(filterPanel);

        box.add(panelFilter);

        box.add(createAddRemoveFilterPanel());
        box.add(createApplyRevertPanel());
    }

    /**
     * Display filter.
     *
     * @param node the node
     */
    private void displayFilter(FilterNode node) {

        if (node == null) {
            return;
        }

        Filter filter = node.getFilter();

        filterPanel.setFilter(filter, node.getFilterConfig());

        Dimension boxSize = box.getPreferredSize();
        setPreferredSize(boxSize);

        boolean addFilterButtonFlag = filter instanceof LogicFilterImpl;
        btnAddFilter.setVisible(addFilterButtonFlag);

        boolean removeFilterButtonFlag = false;
        FilterNode parentNode = (FilterNode) node.getParent();
        if (parentNode != null) {
            removeFilterButtonFlag = (parentNode.getFilter() instanceof LogicFilterImpl)
                    && (parentNode.getChildCount() > 2);
        }
        btnRemoveFilter.setVisible(removeFilterButtonFlag);
        revalidate();
    }

    /**
     * Creates the add filter panel.
     *
     * @return the j panel
     */
    private JPanel createAddRemoveFilterPanel() {

        btnAddFilter = new JButton(
                Localisation.getString(ExpressionPanelv2.class, "FilterSubPanel.addFilter"));
        btnAddFilter.setVisible(false);
        btnAddFilter.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                selectedNode.addFilter();

                if (parent != null) {
                    parent.dataApplied();
                }

                updateButtonState(false);
            }
        });
        JPanel panel = new JPanel();
        panel.add(btnAddFilter);

        btnRemoveFilter = new JButton(
                Localisation.getString(ExpressionPanelv2.class, "FilterSubPanel.removeFilter"));
        btnRemoveFilter.setVisible(false);
        btnRemoveFilter.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                FilterNode parentNode = (FilterNode) selectedNode.getParent();
                if (parentNode != null) {
                    parentNode.remove(selectedNode);
                }
                if (parent != null) {
                    parent.dataApplied();
                }

                updateButtonState(false);
            }
        });
        panel.add(btnRemoveFilter);

        return panel;
    }

    /**
     * Creates the apply revert panel.
     *
     * @return the j panel
     */
    private JPanel createApplyRevertPanel() {

        btnApply = new JButton(Localisation.getString(ExpressionPanelv2.class, "common.apply"));
        btnApply.setEnabled(false);
        btnApply.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                selectedNode.setFilter(filterPanel.getFilter(), filterPanel.getFilterConfig());

                if (parent != null) {
                    parent.dataApplied();
                }

                updateButtonState(false);
            }
        });
        JPanel panel = new JPanel();
        panel.add(btnApply);

        btnRevert = new JButton(Localisation.getString(ExpressionPanelv2.class, "common.revert"));
        btnRevert.setEnabled(false);
        btnRevert.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayFilter(selectedNode);

                updateButtonState(false);
            }
        });
        panel.add(btnRevert);

        return panel;
    }

    /**
     * Update button Apply/Revert state.
     *
     * @param dataChanged the data changed
     */
    private void updateButtonState(boolean dataChanged) {
        btnApply.setEnabled(dataChanged);
        btnRevert.setEnabled(dataChanged);
    }

    /**
     * Sets the selected node.
     *
     * @param node the new selected node
     */
    public void setSelectedNode(DefaultMutableTreeNode node) {
        selectedNode = (FilterNode) node;

        displayFilter(selectedNode);
        updateButtonState(false);
    }
}
