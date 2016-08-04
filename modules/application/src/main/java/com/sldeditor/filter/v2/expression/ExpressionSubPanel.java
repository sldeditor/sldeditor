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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.tree.DefaultMutableTreeNode;

import org.geotools.filter.AttributeExpressionImpl;
import org.geotools.filter.FunctionExpressionImpl;
import org.geotools.filter.function.EnvFunction;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.filter.expression.Expression;

import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.common.vendoroption.VendorOptionManager;
import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.datasource.DataSourceInterface;
import com.sldeditor.filter.v2.envvar.EnvironmentVariableField;
import com.sldeditor.filter.v2.envvar.EnvironmentVariableManager;
import com.sldeditor.filter.v2.function.FunctionField;
import com.sldeditor.filter.v2.function.FunctionManager;
import com.sldeditor.ui.attribute.DataSourceAttributePanel;
import com.sldeditor.ui.attribute.SubPanelUpdatedInterface;
import com.sldeditor.ui.detail.config.FieldConfigBase;
import com.sldeditor.ui.detail.config.FieldConfigBoolean;
import com.sldeditor.ui.detail.config.FieldConfigBoundingBox;
import com.sldeditor.ui.detail.config.FieldConfigDate;
import com.sldeditor.ui.detail.config.FieldConfigDouble;
import com.sldeditor.ui.detail.config.FieldConfigEnum;
import com.sldeditor.ui.detail.config.FieldConfigGeometry;
import com.sldeditor.ui.detail.config.FieldConfigInteger;
import com.sldeditor.ui.detail.config.FieldConfigString;
import com.sldeditor.ui.detail.config.FieldId;
import com.sldeditor.ui.detail.config.symboltype.SymbolTypeConfig;
import com.sldeditor.ui.iface.UpdateSymbolInterface;
import com.vividsolutions.jts.geom.Geometry;

/**
 * The Class ExpressionSubPanel.
 *
 * @author Robert Ward (SCISYS)
 */
public class ExpressionSubPanel extends JPanel {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The Constant FUNCTION. */
    private static final String FUNCTION = "Function";

    /** The Constant ATTRIBUTE. */
    private static final String ATTRIBUTE = "Attribute";

    /** The Constant LITERAL. */
    private static final String LITERAL = "Literal";

    /** The Constant ENVVAR. */
    private static final String ENVVAR = "Env";

    /** The box. */
    private Box box;

    /** The field config. */
    private FieldConfigBase fieldConfig = null;

    /** The button group. */
    private ButtonGroup buttonGroup = new ButtonGroup();

    /** The rdbtn literal. */
    private JRadioButton rdbtnLiteral;

    /** The rdbtn attribute. */
    private JRadioButton rdbtnAttribute;

    /** The rdbtn function. */
    private JRadioButton rdbtnFunction;

    /** The rdbtn environment variable. */
    private JRadioButton rdbtnEnvVar;

    /** The panel literal. */
    private JPanel panelLiteral;

    /** The panel attribute. */
    private JPanel panelAttribute;

    /** The panel function. */
    private JPanel panelFunction;

    /**  The panel environment variable. */
    private JPanel panelEnvVar;

    /** The data source attribute panel. */
    private DataSourceAttributePanel dataSourceAttributePanel;

    /** The function panel. */
    private FunctionField functionPanel;

    /** The selected node. */
    private ExpressionNode selectedNode = null;

    /** The parent. */
    private ExpressionFilterInterface parent = null;

    /** The apply button. */
    private JButton btnApply;

    /** The revert button. */
    private JButton btnRevert;

    /** The environment variable panel. */
    private EnvironmentVariableField envVarField = null;

    /** The enum value list. */
    private List<String> enumValueList = null;

    /**
     * Instantiates a new expression panel.
     *
     * @param parent the parent
     */
    public ExpressionSubPanel(ExpressionFilterInterface parent) {
        this.parent = parent;
        createUI();
    }

    /**
     * Sets the data type for the ui fields.
     *
     * @param fieldType the new data type
     */
    public void setDataType(Class<?> fieldType)
    {
        if(envVarField != null)
        {
            envVarField.setDataType(fieldType);
        }

        Class<?> updatedFieldType = (fieldType == StringBuilder.class) ? String.class : fieldType;
        
        if(dataSourceAttributePanel != null)
        {
            dataSourceAttributePanel.setDataType(updatedFieldType);
        }

        if(functionPanel != null)
        {
            functionPanel.setDataType(updatedFieldType);
        }
    }

    /**
     * Creates the ui.
     */
    private void createUI() {
        setLayout(new BorderLayout());
        box = Box.createVerticalBox();
        add(box, BorderLayout.CENTER);

        //
        // Literal panel
        //
        setUpLiteralPanel();

        //
        // Property / attribute
        //
        setUpPropertyPanel();

        //
        // Environment variable
        //
        setUpEnvVarPanel();

        //
        // Function panel
        //
        setUpFunctionPanel();

        box.add(createApplyRevertPanel());
    }

    /**
     * Sets the up literal panel.
     */
    protected void setUpLiteralPanel() {
        panelLiteral = new JPanel();
        FlowLayout fl_panelLiteral = (FlowLayout) panelLiteral.getLayout();
        fl_panelLiteral.setAlignment(FlowLayout.LEFT);

        rdbtnLiteral = new JRadioButton(Localisation.getString(ExpressionPanelv2.class, "ExpressionPanelv2.literal"));
        rdbtnLiteral.setActionCommand(LITERAL);
        buttonGroup.add(rdbtnLiteral);
        panelLiteral.add(rdbtnLiteral);
        box.add(panelLiteral);
    }

    /**
     * Sets the up property panel.
     */
    protected void setUpPropertyPanel() {
        panelAttribute = new JPanel();
        FlowLayout fl_panelAttribute = (FlowLayout) panelAttribute.getLayout();
        fl_panelAttribute.setAlignment(FlowLayout.LEFT);

        rdbtnAttribute = new JRadioButton(Localisation.getString(ExpressionPanelv2.class, "ExpressionPanelv2.attribute"));
        rdbtnAttribute.setActionCommand(ATTRIBUTE);
        buttonGroup.add(rdbtnAttribute);
        panelAttribute.add(rdbtnAttribute);

        dataSourceAttributePanel = new DataSourceAttributePanel(new SubPanelUpdatedInterface()
        {
            @Override
            public void updateSymbol() {
                buttonGroup.setSelected(rdbtnAttribute.getModel(), true);
                updateButtonState(true);
            }
        });
        panelAttribute.add(dataSourceAttributePanel);
        box.add(panelAttribute);
    }

    /**
     * Sets the up env var panel.
     */
    protected void setUpEnvVarPanel() {
        if(VendorOptionManager.getInstance().isAllowed(this.parent.getVendorOptionList(), EnvironmentVariableField.getVendorOption()))
        {
            panelEnvVar = new JPanel();
            FlowLayout fl_panelEnvVar = (FlowLayout) panelEnvVar.getLayout();
            fl_panelEnvVar.setAlignment(FlowLayout.LEFT);

            rdbtnEnvVar = new JRadioButton(ENVVAR);
            rdbtnEnvVar.setActionCommand(ENVVAR);
            buttonGroup.add(rdbtnEnvVar);
            panelEnvVar.add(rdbtnEnvVar);

            envVarField = new EnvironmentVariableField(new SubPanelUpdatedInterface()
            {
                @Override
                public void updateSymbol() {
                    buttonGroup.setSelected(rdbtnEnvVar.getModel(), true);
                    updateButtonState(true);
                }
            }, EnvironmentVariableManager.getInstance());
            panelEnvVar.add(envVarField);
            box.add(panelEnvVar);
        }
    }

    /**
     * Sets the up function panel.
     */
    protected void setUpFunctionPanel() {
        panelFunction = new JPanel();
        FlowLayout fl_panelFunction = (FlowLayout) panelFunction.getLayout();
        fl_panelFunction.setAlignment(FlowLayout.LEFT);

        rdbtnFunction = new JRadioButton(Localisation.getString(ExpressionPanelv2.class, "ExpressionPanelv2.function"));
        rdbtnFunction.setActionCommand(FUNCTION);
        buttonGroup.add(rdbtnFunction);
        panelFunction.add(rdbtnFunction);

        functionPanel = new FunctionField(new SubPanelUpdatedInterface()
        {
            @Override
            public void updateSymbol() {
                buttonGroup.setSelected(rdbtnFunction.getModel(), true);
                updateButtonState(true);
            }
        }, FunctionManager.getInstance());
        panelFunction.add(functionPanel);
        box.add(panelFunction);
    }

    /**
     * Display expression.
     *
     * @param node the node
     */
    private void displayExpression(ExpressionNode node) {

        if(panelLiteral.getComponentCount() == 2)
        {
            panelLiteral.remove(1);
        }

        if(node == null)
        {
            return;
        }

        String valueText = Localisation.getString(ExpressionPanelv2.class, "ExpressionSubPanel.value");
        if(node.getType() == Geometry.class)
        {
            fieldConfig = new FieldConfigGeometry(null, new FieldId(FieldIdEnum.FUNCTION), valueText, true, null);
        }
        else if(node.getType() == Date.class)
        {
            fieldConfig = new FieldConfigDate(null, new FieldId(FieldIdEnum.FUNCTION), valueText, true);
        }
        else if(node.getType() == ReferencedEnvelope.class)
        {
            fieldConfig = new FieldConfigBoundingBox(null, new FieldId(FieldIdEnum.FUNCTION), "", true);
        }
        else if((node.getType() == String.class) ||
                (node.getType() == Object.class))
        {
            fieldConfig = new FieldConfigString(null, new FieldId(FieldIdEnum.FUNCTION), valueText, true, null);
        }
        else if(node.getType() == Boolean.class)
        {
            fieldConfig = new FieldConfigBoolean(null, new FieldId(FieldIdEnum.FUNCTION), valueText, true);
        }
        else if(node.getType() == Integer.class)
        {
            fieldConfig = new FieldConfigInteger(null, new FieldId(FieldIdEnum.FUNCTION), valueText, true);
        }
        else if(node.getType() == Double.class)
        {
            fieldConfig = new FieldConfigDouble(null, new FieldId(FieldIdEnum.FUNCTION), valueText, true);
        }
        else if(node.getType() == Number.class)
        {
            Class<?> filterType = TypeManager.getInstance().getDataType();
            if((filterType == Float.class) || (filterType == Double.class))
            {
                fieldConfig = new FieldConfigDouble(null, new FieldId(FieldIdEnum.FUNCTION), valueText, true);
            }
            else
            {
                fieldConfig = new FieldConfigInteger(null, new FieldId(FieldIdEnum.FUNCTION), valueText, true);
            }
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
            System.err.println("Unknown type : " + node.getType());
        }

        fieldConfig.createUI();
        fieldConfig.setFunctionParameterType(node.getType());
        fieldConfig.addDataChangedListener(new UpdateSymbolInterface()
        {
            @Override
            public void dataChanged(FieldId changedField) {
                buttonGroup.setSelected(rdbtnLiteral.getModel(), true);
                updateButtonState(true);
            }
        });
        panelLiteral.add(fieldConfig.getPanel());

        Expression expression = node.getExpression();

        if(expression instanceof AttributeExpressionImpl)
        {
            dataSourceAttributePanel.setAttribute(expression);
            buttonGroup.setSelected(rdbtnAttribute.getModel(), true);
        }
        else if(expression instanceof EnvFunction)
        {
            envVarField.setEnvironmentVariable(expression);
            buttonGroup.setSelected(rdbtnEnvVar.getModel(), true);
        }
        else if(expression instanceof FunctionExpressionImpl)
        {
            functionPanel.setFunction(expression);
            buttonGroup.setSelected(rdbtnFunction.getModel(), true);
        }
        else
        {
            fieldConfig.populate(expression);
            buttonGroup.setSelected(rdbtnLiteral.getModel(), true);
        }

        Dimension boxSize = box.getPreferredSize();
        setPreferredSize(boxSize);
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
                Expression expression = null;

                String actionCommand = buttonGroup.getSelection().getActionCommand();

                if(actionCommand.compareTo(LITERAL) == 0)
                {
                    expression = fieldConfig.getExpression();
                }
                else if(actionCommand.compareTo(ATTRIBUTE) == 0)
                {
                    expression = dataSourceAttributePanel.getExpression();
                }
                else if(actionCommand.compareTo(FUNCTION) == 0)
                {
                    expression = functionPanel.getExpression();
                }
                else if(actionCommand.compareTo(ENVVAR) == 0)
                {
                    expression = envVarField.getExpression();
                }

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
     * @param enumValueList 
     */
    public void setSelectedNode(DefaultMutableTreeNode node, List<String> enumValueList) {
        selectedNode = (ExpressionNode) node;
        this.enumValueList = enumValueList;

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
