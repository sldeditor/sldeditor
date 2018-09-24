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

import com.sldeditor.common.Controller;
import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.common.vendoroption.VersionData;
import com.sldeditor.datasource.DataSourceInterface;
import com.sldeditor.datasource.DataSourceUpdatedInterface;
import com.sldeditor.datasource.impl.DataSourceFactory;
import com.sldeditor.datasource.impl.GeometryTypeEnum;
import com.sldeditor.filter.ExpressionPanelInterface;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import org.geotools.data.DataStore;
import org.geotools.filter.AttributeExpressionImpl;
import org.geotools.filter.FunctionExpressionImpl;
import org.geotools.filter.LiteralExpressionImpl;
import org.geotools.filter.MathExpressionImpl;
import org.geotools.filter.function.string.ConcatenateFunction;
import org.opengis.filter.expression.Expression;

/**
 * The Class ExpressionPanelv2.
 *
 * @author Robert Ward (SCISYS)
 */
public class ExpressionPanelv2 extends JDialog
        implements ExpressionFilterInterface, DataSourceUpdatedInterface, ExpressionPanelInterface {

    /** The Constant EMPTY_PANEL. */
    private static final String EMPTY_PANEL = "EMPTY";

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The model. */
    protected ExpressionTreeModel model = null;

    /** The root node. */
    private DefaultMutableTreeNode rootNode = null;

    /** The ok pressed. */
    private boolean okButtonPressed = false;

    /** The data panel. */
    private JPanel dataPanel;

    /** The selected node. */
    private DefaultMutableTreeNode selectedNode;

    /** The text area. */
    private JTextArea textArea;

    /** The tree. */
    protected JTree tree;

    /** The expression panel. */
    private ExpressionSubPanel expressionPanel = null;

    /** The filter panel. */
    private FilterSubPanel filterPanel = null;

    /** The literal panel. */
    private LiteralPanel literalPanel = null;

    /** The property panel. */
    private PropertyPanel propertyPanel = null;

    /** The env var panel. */
    private EnvVarPanel envVarPanel = null;

    /** The empty panel. */
    private JPanel emptyPanel = new JPanel();

    /** The expression. */
    private Expression expression = null;

    /** The field type. */
    private Class<?> fieldType = null;

    /** The overall expression. */
    private Expression overallExpression;

    /** The vendor option list. */
    private List<VersionData> vendorOptionList = null;

    private JButton btnOk;

    /** The in test mode flag. */
    private boolean inTestMode = false;

    /**
     * Instantiates a new expression panel.
     *
     * @param vendorOptionList the vendor option list
     * @param inTestMode the in test mode
     */
    public ExpressionPanelv2(List<VersionData> vendorOptionList, boolean inTestMode) {

        super(Controller.getInstance().getFrame(), "", true);

        this.setResizable(false);
        this.vendorOptionList = vendorOptionList;
        this.inTestMode = inTestMode;

        setPreferredSize(new Dimension(800, 450));

        createUI();

        Controller.getInstance().setPopulating(true);
        DataSourceInterface dataSource = DataSourceFactory.getDataSource();
        if (dataSource != null) {
            dataSource.addListener(this);
        }
        Controller.getInstance().setPopulating(false);
        this.pack();

        Controller.getInstance().centreDialog(this);
    }

    /**
     * Configure the panel.
     *
     * @param title the title
     * @param fieldType the field type
     * @param isRasterSymbol the is raster symbol flag
     */
    @Override
    public void configure(String title, Class<?> fieldType, boolean isRasterSymbol) {
        this.fieldType = fieldType;

        setTitle(
                String.format(
                        "%s : %s",
                        title, ((fieldType == null) ? "???" : fieldType.getSimpleName())));

        expressionPanel.setDataType(fieldType, isRasterSymbol);
        propertyPanel.setDataType(fieldType);
        envVarPanel.setDataType(fieldType);

        textArea.setText("");
    }

    /** Creates the ui. */
    private void createUI() {
        JPanel treePanel = new JPanel();
        getContentPane().add(treePanel, BorderLayout.WEST);
        treePanel.setLayout(new BorderLayout(0, 0));

        JScrollPane scrollPane = new JScrollPane();
        treePanel.add(scrollPane);
        Dimension preferredSize = new Dimension(400, 350);
        scrollPane.setPreferredSize(preferredSize);
        model = new ExpressionTreeModel(rootNode);

        tree = new JTree(model);
        tree.addTreeSelectionListener(
                new TreeSelectionListener() {

                    public void valueChanged(TreeSelectionEvent e) {
                        selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();

                        treeSelected(selectedNode);
                    }
                });
        scrollPane.setViewportView(tree);

        dataPanel = new JPanel(new CardLayout());
        dataPanel.add(emptyPanel, EMPTY_PANEL);
        expressionPanel = new ExpressionSubPanel(this);
        dataPanel.add(expressionPanel, ExpressionSubPanel.class.getName());
        filterPanel = new FilterSubPanel(this);
        dataPanel.add(filterPanel, FilterSubPanel.class.getName());
        literalPanel = new LiteralPanel(this);
        dataPanel.add(literalPanel, LiteralPanel.class.getName());
        propertyPanel = new PropertyPanel(this);
        dataPanel.add(propertyPanel, PropertyPanel.class.getName());
        envVarPanel = new EnvVarPanel(this);
        dataPanel.add(envVarPanel, EnvVarPanel.class.getName());

        getContentPane().add(dataPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        FlowLayout flowLayout = (FlowLayout) buttonPanel.getLayout();
        flowLayout.setAlignment(FlowLayout.TRAILING);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        btnOk = new JButton(Localisation.getString(ExpressionPanelv2.class, "common.ok"));
        btnOk.setEnabled(false);
        btnOk.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        okButtonPressed = true;

                        setVisible(false);
                    }
                });
        buttonPanel.add(btnOk);

        JButton btnCancel =
                new JButton(Localisation.getString(ExpressionPanelv2.class, "common.cancel"));
        btnCancel.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        okButtonPressed = false;

                        setVisible(false);
                    }
                });
        buttonPanel.add(btnCancel);

        JPanel resultPanel = new JPanel();
        getContentPane().add(resultPanel, BorderLayout.NORTH);
        resultPanel.setLayout(new BorderLayout(0, 0));

        JScrollPane scrollPane_1 = new JScrollPane();
        scrollPane_1.setPreferredSize(new Dimension(800, 30));
        resultPanel.add(scrollPane_1);

        textArea = new JTextArea();
        textArea.setEditable(false);
        scrollPane_1.setViewportView(textArea);
    }

    /**
     * Show expression dialog.
     *
     * @param type the type
     * @param expression the expression
     */
    protected void showExpressionDialog(Class<?> type, Expression expression) {

        rootNode = createExpressionNode();
        if (model != null) {
            model.setRoot(rootNode);
            ExpressionNode expressionNode = (ExpressionNode) rootNode;
            expressionNode.setType(type);

            if (expression != null) {
                populateExpression(expressionNode, expression);
            }

            // Auto select the first item
            if (tree.getRowCount() > 0) {
                tree.setSelectionRow(0);
            }
        }
    }

    /**
     * Creates the expression node.
     *
     * @return the expression node
     */
    protected ExpressionNode createExpressionNode() {
        return new ExpressionNode();
    }

    /**
     * Populate expression.
     *
     * @param node the node
     * @param expression the expression
     */
    private void populateExpression(ExpressionNode node, Expression expression) {
        btnOk.setEnabled(false);
        if (node != null) {
            node.setExpression(expression);

            model.reload(); // This notifies the listeners and changes the GUI

            displayResult();

            // Auto select the first entry
            if (tree.getRowCount() > 0) {
                tree.setSelectionRow(0);
            }
        }
    }

    /** Data applied. */
    @Override
    public void dataApplied() {
        btnOk.setEnabled(true);
        if (selectedNode != null) {
            DefaultMutableTreeNode node = null;
            if (selectedNode.isLeaf()) {
                node = (DefaultMutableTreeNode) selectedNode.getParent();
                if (node == null) {
                    node = selectedNode;
                }
            } else {
                node = selectedNode;
            }
            TreeNode[] tmpNode = node.getPath();

            model.reload(); // This notifies the listeners and changes the GUI

            tree.expandPath(new TreePath(tmpNode));

            displayResult();
        }
    }

    /** Display result. */
    private void displayResult() {
        String result = "";
        if (rootNode instanceof ExpressionNode) {
            overallExpression = addExpression((ExpressionNode) rootNode);

            if (overallExpression != null) {
                result = overallExpression.toString();
            }
        }

        textArea.setText(result);
    }

    /**
     * Adds the expression.
     *
     * @param node the node
     * @return the expression
     */
    private Expression addExpression(ExpressionNode node) {
        Expression expression = node.getExpression();

        if (expression instanceof LiteralExpressionImpl) {
            return expression;
        } else if (expression instanceof AttributeExpressionImpl) {
            return expression;
        } else if (expression instanceof FunctionExpressionImpl) {
            FunctionExpressionImpl functionExpression = (FunctionExpressionImpl) expression;

            List<Expression> parameterlist = new ArrayList<Expression>();
            for (int childIndex = 0; childIndex < node.getChildCount(); childIndex++) {
                ExpressionNode childNode = (ExpressionNode) node.getChildAt(childIndex);

                parameterlist.add(addExpression(childNode));
            }

            functionExpression.setParameters(parameterlist);

            return functionExpression;
        } else if (expression instanceof MathExpressionImpl) {
            MathExpressionImpl mathExpression = (MathExpressionImpl) expression;
            ExpressionNode leftChildNode = (ExpressionNode) node.getChildAt(0);
            mathExpression.setExpression1(addExpression(leftChildNode));
            ExpressionNode rightChildNode = (ExpressionNode) node.getChildAt(1);
            mathExpression.setExpression2(addExpression(rightChildNode));

            return mathExpression;
        } else if (expression instanceof ConcatenateFunction) {
            ConcatenateFunction concatenateExpression = (ConcatenateFunction) expression;
            List<Expression> parameters = new ArrayList<Expression>();
            for (int index = 0; index < node.getChildCount(); index++) {
                ExpressionNode expressionNode = (ExpressionNode) node.getChildAt(0);
                parameters.add(addExpression(expressionNode));
            }
            concatenateExpression.setParameters(parameters);

            return concatenateExpression;
        }
        return null;
    }

    /**
     * Data source loaded.
     *
     * @param geometryType the geometry type
     * @param isConnectedToDataSourceFlag the is connected to data source flag
     */
    @Override
    public void dataSourceLoaded(
            GeometryTypeEnum geometryType, boolean isConnectedToDataSourceFlag) {
        DataSourceInterface dataSource = DataSourceFactory.getDataSource();

        propertyPanel.dataSourceLoaded(dataSource);
        expressionPanel.dataSourceLoaded(dataSource);
    }

    /**
     * Show dialog.
     *
     * @return true, if successful
     */
    @Override
    public boolean showDialog() {
        showExpressionDialog(fieldType, expression);

        if (!inTestMode) {
            setVisible(true);
        } else {
            okButtonPressed = true;
        }
        return okButtonPressed;
    }

    /**
     * Gets the expression string.
     *
     * @return the expression string
     */
    @Override
    public String getExpressionString() {
        return textArea.getText();
    }

    /**
     * Gets the expression.
     *
     * @return the expression
     */
    @Override
    public Expression getExpression() {
        return overallExpression;
    }

    /**
     * Populate.
     *
     * @param expressionString the expression string
     */
    @Override
    public void populate(String expressionString) {}

    /**
     * (non-Javadoc)
     *
     * @see
     *     com.sldeditor.filter.ExpressionPanelInterface#populate(org.opengis.filter.expression.Expression)
     */
    @Override
    public void populate(Expression storedExpression) {
        this.expression = storedExpression;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.filter.v2.expression.ExpressionFilterInterface#getVendorOptionList()
     */
    @Override
    public List<VersionData> getVendorOptionList() {
        return vendorOptionList;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.datasource.DataSourceUpdatedInterface#dataSourceAboutToUnloaded(org.geotools.data.DataStore)
     */
    @Override
    public void dataSourceAboutToUnloaded(DataStore dataStore) {
        // Does nothing
    }

    /**
     * Tree selected.
     *
     * @param node the node
     */
    protected void treeSelected(DefaultMutableTreeNode node) {
        CardLayout cardLayout = (CardLayout) dataPanel.getLayout();

        if (node instanceof ExpressionNode) {
            ExpressionNode expressionNode = (ExpressionNode) node;
            if (expressionNode.getExpressionType() == ExpressionTypeEnum.LITERAL) {
                cardLayout.show(dataPanel, literalPanel.getClass().getName());
                literalPanel.setSelectedNode(node);
            } else if (expressionNode.getExpressionType() == ExpressionTypeEnum.PROPERTY) {
                cardLayout.show(dataPanel, propertyPanel.getClass().getName());
                propertyPanel.setSelectedNode(node);
            } else if (expressionNode.getExpressionType() == ExpressionTypeEnum.ENVVAR) {
                cardLayout.show(dataPanel, envVarPanel.getClass().getName());
                envVarPanel.setSelectedNode(node);
            } else {
                cardLayout.show(dataPanel, expressionPanel.getClass().getName());
                expressionPanel.setSelectedNode(node);
            }
        } else {
            cardLayout.show(dataPanel, EMPTY_PANEL);
        }
    }
}
