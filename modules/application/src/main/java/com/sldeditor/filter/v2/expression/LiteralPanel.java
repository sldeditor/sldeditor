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
import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.ui.detail.config.FieldConfigBase;
import com.sldeditor.ui.iface.UpdateSymbolInterface;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.tree.DefaultMutableTreeNode;
import org.bouncycastle.util.StringList;
import org.opengis.filter.expression.Expression;

/**
 * The Class LiteralPanel.
 *
 * @author Robert Ward (SCISYS)
 */
public class LiteralPanel extends JPanel {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The field config. */
    private FieldConfigBase fieldConfig = null;

    /** The selected node. */
    private ExpressionNode selectedNode = null;

    /** The parent. */
    private ExpressionFilterInterface parent = null;

    /** The Apply button. */
    private JButton btnApply;

    /** The Revert button. */
    private JButton btnRevert;

    /**
     * Instantiates a new literal panel.
     *
     * @param parent the parent
     */
    public LiteralPanel(ExpressionFilterInterface parent) {
        this.parent = parent;
        createUI();
    }

    /** Creates the ui. */
    private void createUI() {
        setLayout(new BorderLayout());

        add(createApplyRevertPanel(), BorderLayout.CENTER);
    }

    /**
     * Display literal value.
     *
     * @param node the node
     */
    private void displayLiteral(ExpressionNode node) {

        if (node == null) {
            return;
        }

        if (fieldConfig != null) {
            remove(fieldConfig.getPanel());
        }

        fieldConfig =
                PanelField.getField(
                        ExpressionPanelv2.class,
                        "LiteralPanel.value",
                        (node.getType() == String.class) ? StringList.class : node.getType(),
                        null,
                        node.getMaxStringSize(),
                        node.isRegExpString());

        if (fieldConfig != null) {
            fieldConfig.createUI();
            fieldConfig.addDataChangedListener(
                    new UpdateSymbolInterface() {
                        @Override
                        public void dataChanged(FieldIdEnum changedField) {
                            updateButtonState(true);
                        }
                    });
            add(fieldConfig.getPanel(), BorderLayout.NORTH);

            fieldConfig.populate(node.getExpression());
        }

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
                        Expression expression = fieldConfig.getExpression();

                        if (expression != null) {
                            selectedNode.setExpression(expression);
                        }

                        if (parent != null) {
                            parent.dataApplied();
                        }
                        updateButtonState(false);
                    }
                });
        panel.add(btnApply);

        btnRevert = new JButton(Localisation.getString(ExpressionPanelv2.class, "common.revert"));
        btnRevert.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        displayLiteral(selectedNode);
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

        displayLiteral(selectedNode);

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
}
