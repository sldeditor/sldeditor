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

import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.datasource.DataSourceInterface;
import com.sldeditor.ui.attribute.DataSourceAttributePanel;
import com.sldeditor.ui.attribute.SubPanelUpdatedInterface;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.tree.DefaultMutableTreeNode;
import org.opengis.filter.expression.Expression;

/**
 * The Class PropertyPanel.
 *
 * @author Robert Ward (SCISYS)
 */
public class PropertyPanel extends JPanel {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The data source attribute panel. */
    private transient DataSourceAttributePanel dataSourceAttributePanel;

    /** The selected node. */
    private transient ExpressionNode selectedNode = null;

    /** The parent. */
    private transient ExpressionFilterInterface parentObj = null;

    /** The btn apply. */
    private JButton btnApply;

    /** The btn revert. */
    private JButton btnRevert;

    /**
     * Instantiates a new property panel.
     *
     * @param parent the parent
     */
    public PropertyPanel(ExpressionFilterInterface parent) {
        this.parentObj = parent;
        createUI();
    }

    /** Creates the ui. */
    private void createUI() {
        setLayout(new BorderLayout());

        dataSourceAttributePanel =
                new DataSourceAttributePanel(
                        new SubPanelUpdatedInterface() {
                            @Override
                            public void updateSymbol() {
                                updateButtonState(true);
                            }

                            @Override
                            public void parameterAdded() {
                                // Do nothing
                            }
                        },
                        true);

        add(dataSourceAttributePanel, BorderLayout.NORTH);

        add(createApplyRevertPanel(), BorderLayout.CENTER);
    }

    /**
     * Sets the data type.
     *
     * @param fieldType the new data type
     */
    public void setDataType(Class<?> fieldType) {
        dataSourceAttributePanel.setDataType(fieldType);
    }

    /**
     * Display expression.
     *
     * @param node the node
     */
    private void displayExpression(ExpressionNode node) {

        if (node == null) {
            return;
        }

        dataSourceAttributePanel.setDataType(node.getType());
        dataSourceAttributePanel.setAttribute(node.getExpression());

        revalidate();
    }

    /**
     * Creates the apply revert panel.
     *
     * @return the j panel
     */
    private JPanel createApplyRevertPanel() {
        JPanel panel = new JPanel();

        btnApply = new JButton(Localisation.getString(ExpressionPanelv2.class, "common.apply"));
        btnApply.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        Expression expression = dataSourceAttributePanel.getExpression();

                        if (expression != null) {
                            selectedNode.setExpression(expression);
                        }

                        if (parentObj != null) {
                            parentObj.dataApplied();
                        }
                        updateButtonState(false);
                    }
                });
        panel.add(btnApply);

        btnRevert = new JButton(Localisation.getString(ExpressionPanelv2.class, "common.revert"));
        btnRevert.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        displayExpression(selectedNode);
                        updateButtonState(false);
                    }
                });
        panel.add(btnRevert);

        return panel;
    }

    /**
     * Sets the selected node.
     *
     * @param node the new selected node
     */
    public void setSelectedNode(DefaultMutableTreeNode node) {
        selectedNode = (ExpressionNode) node;

        displayExpression(selectedNode);

        updateButtonState(false);
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
     * Data source loaded.
     *
     * @param dataSource the data source
     */
    public void dataSourceLoaded(DataSourceInterface dataSource) {
        dataSourceAttributePanel.dataSourceLoaded(dataSource);
    }
}
