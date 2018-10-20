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

package com.sldeditor.filter.v2.envvar;

import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.common.vendoroption.GeoServerVendorOption;
import com.sldeditor.common.vendoroption.VendorOptionVersion;
import com.sldeditor.common.vendoroption.VersionData;
import com.sldeditor.filter.v2.envvar.dialog.EnvVarDlg;
import com.sldeditor.ui.attribute.DataSourceAttributePanel;
import com.sldeditor.ui.attribute.SubPanelUpdatedInterface;
import com.sldeditor.ui.detail.BasePanel;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import org.geotools.filter.LiteralExpressionImpl;
import org.geotools.filter.function.EnvFunction;
import org.opengis.filter.expression.Expression;

/**
 * Panel to be able to edit EnvironmentVariableField objects.
 *
 * @author Robert Ward (SCISYS)
 */
public class EnvironmentVariableField extends JPanel {

    /** The Constant ENVVARFIELD_PANEL. */
    private static final String ENVVARFIELD_PANEL = "EnvVarField";

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The env var combo box. */
    private JComboBox<String> envVarComboBox;

    /** The env var map. */
    private transient Map<String, EnvVar> envVarMap = new LinkedHashMap<>();

    /** The env var mgr. */
    private transient EnvironmentManagerInterface envVarMgr = null;

    /**
     * Gets the panel name.
     *
     * @return the panel name
     */
    public static String getPanelName() {
        return ENVVARFIELD_PANEL;
    }

    /**
     * Instantiates a new data source attribute panel.
     *
     * @param parentObj the parent obj
     * @param envVarMgr the env var mgr
     */
    public EnvironmentVariableField(
            SubPanelUpdatedInterface parentObj, EnvironmentManagerInterface envVarMgr) {
        this.envVarMgr = envVarMgr;
        setPreferredSize(new Dimension(300, BasePanel.WIDGET_HEIGHT));
        setMinimumSize(new Dimension(300, BasePanel.WIDGET_HEIGHT));
        setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));

        envVarComboBox = new JComboBox<>();
        add(envVarComboBox);
        envVarComboBox.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {

                        if (parentObj != null) {
                            parentObj.updateSymbol();
                        }
                    }
                });
        envVarComboBox.setPreferredSize(
                new Dimension(BasePanel.WIDGET_EXTENDED_WIDTH, BasePanel.WIDGET_HEIGHT));

        JButton editButton =
                new JButton(
                        Localisation.getString(EnvVarDlg.class, "EnvironmentVariableField.edit"));
        editButton.setPreferredSize(
                new Dimension(BasePanel.WIDGET_BUTTON_WIDTH, BasePanel.WIDGET_HEIGHT));
        editButton.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        if (envVarMgr.showDialog()) {
                            populateFunctionComboBox();
                        }
                    }
                });
        add(editButton);
    }

    /** Populate function combo box. */
    private void populateFunctionComboBox() {
        if (envVarComboBox != null) {
            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();

            model.addElement("");

            for (String name : envVarMap.keySet()) {
                model.addElement(name);
            }
            envVarComboBox.setModel(model);
        }
    }

    /**
     * Gets the selected item.
     *
     * @return the selected item
     */
    public String getSelectedItem() {
        return (String) envVarComboBox.getSelectedItem();
    }

    /**
     * Sets the panel enabled.
     *
     * @param enabled the new panel enabled
     */
    public void setPanelEnabled(boolean enabled) {
        envVarComboBox.setEnabled(enabled);
    }

    /**
     * Sets the environment variable.
     *
     * @param expression the new expression
     */
    public void setEnvironmentVariable(Expression expression) {

        if (expression instanceof EnvFunction) {
            EnvFunction envFunction = (EnvFunction) expression;
            LiteralExpressionImpl literal =
                    (LiteralExpressionImpl) envFunction.getParameters().get(0);

            envVarComboBox.setSelectedItem(literal.getValue());
        } else if (expression instanceof LiteralExpressionImpl) {
            LiteralExpressionImpl literal = (LiteralExpressionImpl) expression;

            envVarComboBox.setSelectedItem(literal.getValue());
        } else {
            ConsoleManager.getInstance()
                    .error(
                            this,
                            Localisation.getString(
                                    DataSourceAttributePanel.class,
                                    "DataSourceAttributePanel.error1"));
        }
    }

    /**
     * Gets the expression.
     *
     * @return the expression
     */
    public Expression getExpression() {
        String nameString = (String) envVarComboBox.getSelectedItem();

        EnvVar envVar = envVarMap.get(nameString);

        return envVarMgr.createExpression(envVar);
    }

    /**
     * Gets the vendor option.
     *
     * @return the vendor option
     */
    public static VendorOptionVersion getVendorOption() {
        return new VendorOptionVersion(
                GeoServerVendorOption.class,
                VersionData.decode(GeoServerVendorOption.class, "2.0.2"),
                VersionData.getLatestVersion(GeoServerVendorOption.class));
    }

    /**
     * Sets the data type.
     *
     * @param fieldType the new data type
     */
    public void setDataType(Class<?> fieldType) {
        envVarMap.clear();

        List<EnvVar> envVarList = envVarMgr.getEnvVarList();
        for (EnvVar envVar : envVarList) {
            if (envVar.getType() == fieldType) {
                envVarMap.put(envVar.getName(), envVar);
            }
        }

        populateFunctionComboBox();
    }
}
