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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.tree.DefaultMutableTreeNode;

import org.opengis.filter.expression.Expression;
import org.opengis.geometry.BoundingBox;

import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.filter.v2.function.temporal.TimePeriod;
import com.sldeditor.ui.detail.config.FieldConfigBase;
import com.sldeditor.ui.detail.config.FieldConfigBoolean;
import com.sldeditor.ui.detail.config.FieldConfigBoundingBox;
import com.sldeditor.ui.detail.config.FieldConfigDate;
import com.sldeditor.ui.detail.config.FieldConfigEnum;
import com.sldeditor.ui.detail.config.FieldConfigString;
import com.sldeditor.ui.detail.config.FieldConfigTimePeriod;
import com.sldeditor.ui.detail.config.FieldId;
import com.sldeditor.ui.detail.config.symboltype.SymbolTypeConfig;
import com.sldeditor.ui.iface.UpdateSymbolInterface;

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

    /** The btn apply. */
    private JButton btnApply;

    /** The btn revert. */
    private JButton btnRevert;

    /** The enum value list. */
    private List<String> enumValueList = null;

    /**
     * Instantiates a new expression panel.
     *
     * @param parent the parent
     */
    public LiteralPanel(ExpressionFilterInterface parent) {
        this.parent = parent;
        createUI();
    }

    /**
     * Creates the ui.
     */
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

        if(node == null)
        {
            return;
        }

        if(fieldConfig != null)
        {
            remove(fieldConfig.getPanel());
        }

        String valueText = Localisation.getString(ExpressionPanelv2.class, "LiteralPanel.value");

        if(node.getType() == Date.class)
        {
            fieldConfig = new FieldConfigDate(null, new FieldId(FieldIdEnum.FUNCTION), valueText, true);
        }
        else if(node.getType() == TimePeriod.class)
        {
            fieldConfig = new FieldConfigTimePeriod(null, new FieldId(FieldIdEnum.FUNCTION), true);
        }
        else if(node.getType() == String.class)
        {
            fieldConfig = new FieldConfigString(null, new FieldId(FieldIdEnum.FUNCTION), valueText, true, null);
        }
        else if(node.getType() == Boolean.class)
        {
            fieldConfig = new FieldConfigBoolean(null, new FieldId(FieldIdEnum.FUNCTION), valueText, true);
        }
        else if(node.getType() == BoundingBox.class)
        {
            fieldConfig = new FieldConfigBoundingBox(null, new FieldId(FieldIdEnum.FUNCTION), null, true);
        }
        else if(node.getType() == StringBuilder.class)
        {
            FieldConfigEnum fieldConfigEnum = new FieldConfigEnum(null, new FieldId(FieldIdEnum.FUNCTION), valueText, true);

            List<SymbolTypeConfig> configList = new ArrayList<SymbolTypeConfig>();
            SymbolTypeConfig symbolTypeConfig = new SymbolTypeConfig(null);

            for(String enumValue : enumValueList)
            {
                symbolTypeConfig.addOption(enumValue, enumValue);
            }
            configList.add(symbolTypeConfig);
            fieldConfigEnum.addConfig(configList);
            fieldConfig = fieldConfigEnum;
        }
        else
        {
            System.err.println("Unknown field type : " + node.getType());
        }

        if(fieldConfig != null)
        {
            fieldConfig.createUI();
            fieldConfig.addDataChangedListener(new UpdateSymbolInterface()
            {
                @Override
                public void dataChanged(FieldId changedField) {
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
    private JPanel createApplyRevertPanel()
    {
        JPanel panel = new JPanel();

        btnApply = new JButton(Localisation.getString(ExpressionPanelv2.class, "common.apply"));
        btnApply.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Expression expression = fieldConfig.getExpression();

                if(expression != null)
                {
                    selectedNode.setExpression(expression);
                }

                if(parent != null)
                {
                    parent.dataApplied();
                }
                updateButtonState(false);
            }
        });
        panel.add(btnApply);

        btnRevert = new JButton(Localisation.getString(ExpressionPanelv2.class, "common.revert"));
        btnRevert.addActionListener(new ActionListener() {
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
     * @param enumValueList 
     */
    public void setSelectedNode(DefaultMutableTreeNode node, List<String> enumValueList) {
        selectedNode = (ExpressionNode) node;
        this.enumValueList = enumValueList;

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
