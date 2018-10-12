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
import com.sldeditor.common.vendoroption.VendorOptionManager;
import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.datasource.DataSourceInterface;
import com.sldeditor.filter.v2.envvar.EnvironmentVariableField;
import com.sldeditor.filter.v2.envvar.EnvironmentVariableManager;
import com.sldeditor.filter.v2.function.FunctionField;
import com.sldeditor.filter.v2.function.FunctionManager;
import com.sldeditor.ui.attribute.DataSourceAttributePanel;
import com.sldeditor.ui.attribute.SubPanelUpdatedInterface;
import com.sldeditor.ui.detail.BasePanel;
import com.sldeditor.ui.detail.config.FieldConfigBase;
import com.sldeditor.ui.iface.UpdateSymbolInterface;
import com.sldeditor.ui.widgets.FieldPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.tree.DefaultMutableTreeNode;
import org.geotools.filter.AttributeExpressionImpl;
import org.geotools.filter.FunctionExpression;
import org.geotools.filter.FunctionExpressionImpl;
import org.geotools.filter.function.EnvFunction;
import org.geotools.filter.function.string.ConcatenateFunction;
import org.opengis.filter.capability.FunctionName;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;
import org.opengis.parameter.Parameter;

/**
 * The Class ExpressionSubPanel.
 *
 * @author Robert Ward (SCISYS)
 */
public class ExpressionSubPanel extends JPanel {

    /** The Constant VERTICAL_STRUCT_SIZE. */
    private static final int VERTICAL_STRUCT_SIZE = 30;

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

    /** The field allowing the configuration change. */
    private FieldConfigBase fieldConfig = null;

    /** The button group. */
    private ButtonGroup buttonGroup = new ButtonGroup();

    /** The radio button literal. */
    private JRadioButton rdbtnLiteral;

    /** The radio button attribute. */
    private JRadioButton rdbtnAttribute;

    /** The radio button function. */
    private JRadioButton rdbtnFunction;

    /** The radio button environment variable. */
    private JRadioButton rdbtnEnvVar;

    /** The panel literal. */
    private JPanel panelLiteral;

    /** The panel attribute. */
    private JPanel panelAttribute;

    /** The panel function. */
    private JPanel panelFunction;

    /** The panel environment variable. */
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

    /** The panel containing the remove parameter button. */
    private JPanel panelRemoveParameter;

    /** The remove parameter button. */
    private JButton btnRemoveParameter;

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
     * @param isRasterSymbol the is raster symbol flag
     */
    public void setDataType(Class<?> fieldType, boolean isRasterSymbol) {
        if (envVarField != null) {
            envVarField.setDataType(fieldType);
        }

        Class<?> updatedFieldType = (fieldType == StringBuilder.class) ? String.class : fieldType;

        if (dataSourceAttributePanel != null) {
            dataSourceAttributePanel.setDataType(updatedFieldType);
        }
        panelAttribute.setVisible(!isRasterSymbol);

        if (functionPanel != null) {
            functionPanel.setIsRasterSymbol(isRasterSymbol);
            functionPanel.setDataType(updatedFieldType);
        }
    }

    /** Creates the ui. */
    private void createUI() {
        setLayout(new BorderLayout());
        box = Box.createVerticalBox();
        box.setBorder(null);
        add(box, BorderLayout.CENTER);

        //
        // Literal panel
        //
        setUpLiteralPanel();
        box.add(Box.createVerticalStrut(VERTICAL_STRUCT_SIZE));

        //
        // Property / attribute
        //
        setUpPropertyPanel();
        box.add(Box.createVerticalStrut(VERTICAL_STRUCT_SIZE));

        //
        // Environment variable
        //
        setUpEnvVarPanel();
        box.add(Box.createVerticalStrut(VERTICAL_STRUCT_SIZE));

        //
        // Function panel
        //
        setUpFunctionPanel();
        box.add(Box.createVerticalStrut(VERTICAL_STRUCT_SIZE));

        panelRemoveParameter = new JPanel();
        FlowLayout flowLayout = (FlowLayout) panelRemoveParameter.getLayout();
        flowLayout.setVgap(1);
        flowLayout.setHgap(1);
        panelRemoveParameter.setMinimumSize(new Dimension(150, 25));
        panelRemoveParameter.setPreferredSize(new Dimension(150, 25));
        box.add(panelRemoveParameter);

        btnRemoveParameter =
                new JButton(
                        Localisation.getString(
                                ExpressionPanelv2.class, "ExpressionSubPanel.removeParameter"));
        btnRemoveParameter.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        removeParameter();
                    }
                });
        panelRemoveParameter.add(btnRemoveParameter);

        box.add(createApplyRevertPanel());
    }

    /** Sets the up literal panel. */
    protected void setUpLiteralPanel() {
        panelLiteral = new JPanel();
        panelLiteral.setBorder(null);
        panelLiteral.setLayout(new BoxLayout(panelLiteral, BoxLayout.X_AXIS));

        rdbtnLiteral =
                new JRadioButton(
                        Localisation.getString(
                                ExpressionPanelv2.class, "ExpressionPanelv2.literal"));
        rdbtnLiteral.setMinimumSize(new Dimension(100, 20));
        rdbtnLiteral.setPreferredSize(new Dimension(100, 20));
        panelLiteral.add(rdbtnLiteral);
        rdbtnLiteral.setActionCommand(LITERAL);
        buttonGroup.add(rdbtnLiteral);
        box.add(panelLiteral);
    }

    /** Sets the up property panel. */
    protected void setUpPropertyPanel() {
        panelAttribute = new JPanel();
        panelAttribute.setBorder(null);
        panelAttribute.setLayout(new BoxLayout(panelAttribute, BoxLayout.X_AXIS));
        rdbtnAttribute =
                new JRadioButton(
                        Localisation.getString(
                                ExpressionPanelv2.class, "ExpressionPanelv2.attribute"));
        panelAttribute.add(rdbtnAttribute);
        rdbtnAttribute.setMinimumSize(new Dimension(100, 20));
        rdbtnAttribute.setPreferredSize(new Dimension(100, 20));
        rdbtnAttribute.setActionCommand(ATTRIBUTE);
        buttonGroup.add(rdbtnAttribute);

        dataSourceAttributePanel =
                new DataSourceAttributePanel(
                        new SubPanelUpdatedInterface() {
                            @Override
                            public void updateSymbol() {
                                buttonGroup.setSelected(rdbtnAttribute.getModel(), true);
                                updateButtonState(true);
                            }

                            @Override
                            public void parameterAdded() {
                                // Do nothing
                            }
                        },
                        true);

        panelAttribute.add(dataSourceAttributePanel);
        box.add(panelAttribute);
    }

    /** Sets the up env var panel. */
    protected void setUpEnvVarPanel() {
        if (VendorOptionManager.getInstance()
                .isAllowed(
                        this.parent.getVendorOptionList(),
                        EnvironmentVariableField.getVendorOption())) {
            panelEnvVar = new JPanel();
            panelEnvVar.setBorder(null);
            panelEnvVar.setLayout(new BoxLayout(panelEnvVar, BoxLayout.X_AXIS));

            rdbtnEnvVar = new JRadioButton(ENVVAR);
            rdbtnEnvVar.setMinimumSize(new Dimension(100, 20));
            rdbtnEnvVar.setPreferredSize(new Dimension(100, 20));
            panelEnvVar.add(rdbtnEnvVar);
            rdbtnEnvVar.setActionCommand(ENVVAR);
            buttonGroup.add(rdbtnEnvVar);

            envVarField =
                    new EnvironmentVariableField(
                            new SubPanelUpdatedInterface() {
                                @Override
                                public void updateSymbol() {
                                    buttonGroup.setSelected(rdbtnEnvVar.getModel(), true);
                                    updateButtonState(true);
                                }

                                @Override
                                public void parameterAdded() {
                                    // Do nothing
                                }
                            },
                            EnvironmentVariableManager.getInstance());

            panelEnvVar.add(envVarField);
            box.add(panelEnvVar);
        }
    }

    /** Sets the up function panel. */
    protected void setUpFunctionPanel() {
        panelFunction = new JPanel();
        panelFunction.setBorder(null);
        panelFunction.setLayout(new BoxLayout(panelFunction, BoxLayout.X_AXIS));

        rdbtnFunction =
                new JRadioButton(
                        Localisation.getString(
                                ExpressionPanelv2.class, "ExpressionPanelv2.function"));
        rdbtnFunction.setMinimumSize(new Dimension(100, 20));
        rdbtnFunction.setPreferredSize(new Dimension(100, 20));
        panelFunction.add(rdbtnFunction);
        rdbtnFunction.setActionCommand(FUNCTION);
        buttonGroup.add(rdbtnFunction);

        functionPanel =
                new FunctionField(
                        new SubPanelUpdatedInterface() {
                            @Override
                            public void updateSymbol() {
                                buttonGroup.setSelected(rdbtnFunction.getModel(), true);
                                updateButtonState(true);
                            }

                            @Override
                            public void parameterAdded() {
                                if (parent != null) {
                                    parent.dataApplied();
                                }
                            }
                        },
                        FunctionManager.getInstance());

        panelFunction.add(functionPanel);
        box.add(panelFunction);
    }

    /**
     * Display expression.
     *
     * @param node the node
     */
    @SuppressWarnings("unchecked")
    private void displayExpression(ExpressionNode node) {

        if (panelLiteral.getComponentCount() == 2) {
            panelLiteral.remove(1);
        }

        if (node == null) {
            return;
        }

        List<String> enumList = null;
        Parameter<?> param = node.getParameter();
        if (param instanceof org.geotools.data.Parameter<?>) {
            org.geotools.data.Parameter<?> paramData = (org.geotools.data.Parameter<?>) param;
            Object obj = paramData.metadata.get(org.geotools.data.Parameter.OPTIONS);
            if (obj instanceof List<?>) {
                enumList = (List<String>) obj;
            }
        }

        fieldConfig =
                PanelField.getField(
                        ExpressionPanelv2.class,
                        "ExpressionSubPanel.value",
                        node.getType(),
                        enumList,
                        node.getMaxStringSize(),
                        node.isRegExpString(),
                        false);

        if (fieldConfig != null) {
            fieldConfig.createUI();
            fieldConfig.addDataChangedListener(
                    new UpdateSymbolInterface() {
                        @Override
                        public void dataChanged(FieldIdEnum changedField) {
                            buttonGroup.setSelected(rdbtnLiteral.getModel(), true);
                            updateButtonState(true);
                        }
                    });

            // Set the size of the panels so they all align
            FieldPanel panel = fieldConfig.getPanel();
            Dimension dimension = panel.getPreferredSize();
            panel.setMaximumSize(dimension);

            dataSourceAttributePanel.setMaximumSize(
                    new Dimension(dimension.width, BasePanel.WIDGET_HEIGHT));

            if (envVarField != null) {
                envVarField.setMaximumSize(new Dimension(dimension.width, BasePanel.WIDGET_HEIGHT));
            }

            functionPanel.setMaximumSize(
                    new Dimension(dimension.width, BasePanel.WIDGET_HEIGHT * 3));

            panelLiteral.add(panel);

            // Reset the fields
            dataSourceAttributePanel.setAttribute(null);
            functionPanel.setDataType(node.getType());
            functionPanel.setFunction(null, null);

            dataSourceAttributePanel.setDataType(node.getType());

            Expression expression = node.getExpression();

            if (expression instanceof AttributeExpressionImpl) {
                dataSourceAttributePanel.setAttribute(expression);
                buttonGroup.setSelected(rdbtnAttribute.getModel(), true);
            } else if (expression instanceof EnvFunction) {
                if (envVarField != null) {
                    envVarField.setEnvironmentVariable(expression);
                    buttonGroup.setSelected(rdbtnEnvVar.getModel(), true);
                }
            } else if ((expression instanceof FunctionExpressionImpl)
                    || (expression instanceof ConcatenateFunction)
                    || (expression instanceof Function)) {
                functionPanel.setFunction(expression, node);
                buttonGroup.setSelected(rdbtnFunction.getModel(), true);
            } else {
                fieldConfig.populate(expression);
                buttonGroup.setSelected(rdbtnLiteral.getModel(), true);
            }

            // Now work out whether the Remove Parameter button should be displayed
            boolean displayRemoveParameter = false;

            if (node.getParent() instanceof ExpressionNode) {
                ExpressionNode parentNode = (ExpressionNode) node.getParent();
                if (parentNode.getExpression() instanceof FunctionExpressionImpl) {
                    FunctionExpression functionExpression =
                            (FunctionExpression) parentNode.getExpression();
                    FunctionName functionName = functionExpression.getFunctionName();

                    int argCount = functionName.getArgumentCount();

                    if (functionName.getArgumentCount() < 0) {
                        displayRemoveParameter = true;
                        argCount *= -1;

                        btnRemoveParameter.setEnabled(
                                functionExpression.getParameters().size() > argCount);
                    }
                } else if (parentNode.getExpression() instanceof ConcatenateFunction) {
                    ConcatenateFunction concatenateFunction =
                            (ConcatenateFunction) parentNode.getExpression();
                    FunctionName functionName = concatenateFunction.getFunctionName();

                    int argCount = functionName.getArgumentCount();

                    if (functionName.getArgumentCount() < 0) {
                        displayRemoveParameter = true;
                        argCount *= -1;

                        btnRemoveParameter.setEnabled(
                                concatenateFunction.getParameters().size() > argCount);
                    }
                }
            }
            btnRemoveParameter.setVisible(displayRemoveParameter);
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
    private JPanel createApplyRevertPanel() {
        JPanel panel = new JPanel();

        btnApply = new JButton(Localisation.getString(ExpressionPanelv2.class, "common.apply"));
        btnApply.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        applyButton();
                    }
                });
        panel.add(btnApply);

        btnRevert = new JButton(Localisation.getString(ExpressionPanelv2.class, "common.revert"));
        btnRevert.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        revertButton();
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

        revertButton();
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

    /** Removes the parameter. */
    protected void removeParameter() {
        ExpressionNode parentNode = (ExpressionNode) selectedNode.getParent();

        int index = parentNode.getIndex(selectedNode);
        parentNode.remove(index);

        if (parentNode.getExpression() instanceof FunctionExpressionImpl) {
            FunctionExpression functionExpression = (FunctionExpression) parentNode.getExpression();

            functionExpression.getParameters().remove(index);
        } else if (parentNode.getExpression() instanceof ConcatenateFunction) {
            ConcatenateFunction concatenateFunction =
                    (ConcatenateFunction) parentNode.getExpression();

            List<Expression> parameters = concatenateFunction.getParameters();
            parameters.remove(index);
            concatenateFunction.setParameters(parameters);
        }
        parentNode.setDisplayString();

        if (parent != null) {
            parent.dataApplied();
        }
    }

    /** Apply button. */
    protected void applyButton() {
        Expression expression = null;

        String actionCommand = buttonGroup.getSelection().getActionCommand();

        if (actionCommand.compareTo(LITERAL) == 0) {
            expression = fieldConfig.getExpression();
        } else if (actionCommand.compareTo(ATTRIBUTE) == 0) {
            expression = dataSourceAttributePanel.getExpression();
        } else if (actionCommand.compareTo(FUNCTION) == 0) {
            expression = functionPanel.getExpression();
        } else if (actionCommand.compareTo(ENVVAR) == 0) {
            expression = envVarField.getExpression();
        }

        if (expression != null) {
            selectedNode.setExpression(expression);
        }

        // Update the display string for the parent function, needed for
        // function expression nodes when function parameter has been changed
        if (selectedNode.getParent() instanceof ExpressionNode) {
            ExpressionNode parentNode = (ExpressionNode) selectedNode.getParent();
            if (parentNode != null) {

                int index = parentNode.getIndex(selectedNode);

                if (parentNode.getExpression() instanceof FunctionExpressionImpl) {
                    FunctionExpression functionExpression =
                            (FunctionExpression) parentNode.getExpression();

                    List<Expression> parameterList = functionExpression.getParameters();
                    parameterList.remove(index);
                    parameterList.add(index, expression);
                } else {
                    FunctionInterfaceUtils.handleFunctionInterface(parentNode, index, expression);
                }

                parentNode.setDisplayString();
            }
        }

        if (parent != null) {
            parent.dataApplied();
        }
        updateButtonState(false);
    }

    /** Revert button. */
    protected void revertButton() {
        displayExpression(selectedNode);
        updateButtonState(false);
    }

    /**
     * Checks if is removes the button enabled.
     *
     * @return true, if is removes the button enabled
     */
    protected boolean isRemoveButtonEnabled() {
        return btnRemoveParameter.isEnabled();
    }

    /**
     * Checks if is removes the button visible.
     *
     * @return true, if is removes the button visible
     */
    protected boolean isRemoveButtonVisible() {
        return btnRemoveParameter.isVisible();
    }
}
