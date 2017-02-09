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
import org.geotools.filter.BinaryComparisonAbstract;
import org.geotools.filter.FidFilterImpl;
import org.geotools.filter.FunctionExpressionImpl;
import org.geotools.filter.LiteralExpressionImpl;
import org.geotools.filter.LogicFilterImpl;
import org.geotools.filter.MathExpressionImpl;
import org.geotools.filter.text.cql2.CQL;
import org.opengis.filter.Filter;
import org.opengis.filter.Not;
import org.opengis.filter.PropertyIsBetween;
import org.opengis.filter.PropertyIsGreaterThan;
import org.opengis.filter.PropertyIsLike;
import org.opengis.filter.PropertyIsNull;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.spatial.BinarySpatialOperator;
import org.opengis.filter.temporal.BinaryTemporalOperator;

import com.sldeditor.common.Controller;
import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.common.vendoroption.VersionData;
import com.sldeditor.datasource.DataSourceInterface;
import com.sldeditor.datasource.DataSourceUpdatedInterface;
import com.sldeditor.datasource.impl.DataSourceFactory;
import com.sldeditor.datasource.impl.GeometryTypeEnum;
import com.sldeditor.filter.FilterPanelInterface;
import com.sldeditor.filter.v2.function.FilterConfigInterface;
import com.sldeditor.filter.v2.function.FilterManager;

/**
 * The Class FilterPanelv2.
 *
 * @author Robert Ward (SCISYS)
 */
public class FilterPanelv2 extends JDialog
        implements ExpressionFilterInterface, DataSourceUpdatedInterface, FilterPanelInterface {

    /**
     * 
     */
    private static final String INVALID_RESULT_STRING = Localisation
            .getString(ExpressionPanelv2.class, "FilterPanelv2.invalidResult");

    /** The Constant EMPTY_PANEL. */
    private static final String EMPTY_PANEL = "EMPTY";

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The model. */
    private ExpressionTreeModel model = null;

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
    private JTree tree;

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

    /** The filter. */
    private Filter filter = null;

    /** The overall filter. */
    private Filter overallFilter;

    /** The vendor option list. */
    private List<VersionData> vendorOptionList = null;

    private JButton btnOk;

    /**
     * Instantiates a new expression panel.
     *
     * @param vendorOptionList the vendor option list
     */
    public FilterPanelv2(List<VersionData> vendorOptionList) {

        super(Controller.getInstance().getFrame(), "", true);

        this.vendorOptionList = vendorOptionList;

        setPreferredSize(new Dimension(800, 415));

        createUI();

        DataSourceInterface dataSource = DataSourceFactory.getDataSource();
        if (dataSource != null) {
            dataSource.addListener(this);
        }
        this.pack();

        Controller.getInstance().centreDialog(this);
    }

    /**
     * Configure.
     *
     * @param title the title
     * @param fieldType the field type
     * @param isRasterSymbol the is raster symbol flag
     */
    @Override
    public void configure(String title, Class<?> fieldType, boolean isRasterSymbol) {

        setTitle(title);

        expressionPanel.setDataType(fieldType, isRasterSymbol);
        propertyPanel.setDataType(fieldType);
        envVarPanel.setDataType(fieldType);

        textArea.setText("");
    }

    /**
     * Creates the ui.
     */
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
        tree.addTreeSelectionListener(new TreeSelectionListener() {

            public void valueChanged(TreeSelectionEvent e) {
                selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();

                CardLayout cardLayout = (CardLayout) dataPanel.getLayout();

                if (selectedNode instanceof ExpressionNode) {
                    ExpressionNode expressionNode = (ExpressionNode) selectedNode;
                    if (expressionNode.getExpressionType() == ExpressionTypeEnum.LITERAL) {
                        cardLayout.show(dataPanel, literalPanel.getClass().getName());
                        literalPanel.setSelectedNode(selectedNode);
                    } else if (expressionNode.getExpressionType() == ExpressionTypeEnum.PROPERTY) {
                        cardLayout.show(dataPanel, propertyPanel.getClass().getName());
                        propertyPanel.setSelectedNode(selectedNode);
                    } else if (expressionNode.getExpressionType() == ExpressionTypeEnum.ENVVAR) {
                        cardLayout.show(dataPanel, envVarPanel.getClass().getName());
                        envVarPanel.setSelectedNode(selectedNode);
                    } else {
                        cardLayout.show(dataPanel, expressionPanel.getClass().getName());
                        expressionPanel.setSelectedNode(selectedNode);
                    }
                } else if (selectedNode instanceof FilterNode) {
                    cardLayout.show(dataPanel, filterPanel.getClass().getName());
                    filterPanel.setSelectedNode(selectedNode);
                } else {
                    cardLayout.show(dataPanel, EMPTY_PANEL);
                }
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
        btnOk.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                closeDialog(true);
            }
        });
        buttonPanel.add(btnOk);

        JButton btnCancel = new JButton(
                Localisation.getString(ExpressionPanelv2.class, "common.cancel"));
        btnCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                closeDialog(false);
            }
        });
        buttonPanel.add(btnCancel);

        JPanel resultPanel = new JPanel();
        getContentPane().add(resultPanel, BorderLayout.NORTH);
        resultPanel.setLayout(new BorderLayout(0, 0));

        JScrollPane scrollPane_1 = new JScrollPane();
        resultPanel.add(scrollPane_1);

        textArea = new JTextArea();
        textArea.setEditable(false);
        scrollPane_1.setViewportView(textArea);
    }

    /**
     * Show filter dialog.
     *
     * @param type the type
     * @param filter the filter
     * @return true, if successful
     */
    private boolean showFilterDialog(Class<?> type, Filter filter) {

        btnOk.setEnabled(false);
        rootNode = new FilterNode();
        if (model != null) {
            model.setRoot(rootNode);
            FilterNode filterNode = (FilterNode) rootNode;
            filterNode.setType(type);

            if (filter != null) {
                populateFilter((FilterNode) rootNode, filter);
            }
        }

        setVisible(true);
        return okButtonPressed;
    }

    /**
     * Populate filter.
     *
     * @param node the node
     * @param filter the filter
     */
    private void populateFilter(FilterNode node, Filter filter) {
        FilterConfigInterface filterConfig = null;

        if (filter != null) {
            filterConfig = FilterManager.getInstance().getFilterConfig(filter);
        }
        node.setFilter(filter, filterConfig);

        model.reload(); // This notifies the listeners and changes the GUI

        displayResult();
    }

    /**
     * Data applied.
     */
    @Override
    public void dataApplied() {
        btnOk.setEnabled(true);
        DefaultMutableTreeNode node;
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

        boolean valid = displayResult();
        btnOk.setEnabled(valid);
    }

    /**
     * Display result.
     *
     * @return true, if filter is valid
     */
    private boolean displayResult() {
        String result = INVALID_RESULT_STRING;
        if (rootNode instanceof FilterNode) {
            overallFilter = addFilter((FilterNode) rootNode);

            if (overallFilter == null) {
                result = "";
            } else {
                try {
                    result = CQL.toCQL(overallFilter);
                } catch (Exception e) {
                }
            }
        }

        textArea.setText(result);

        return (result.compareTo(INVALID_RESULT_STRING) != 0);
    }

    /**
     * Adds the filter.
     *
     * @param node the node
     * @return the filter
     */
    private Filter addFilter(FilterNode node) {
        Filter filter = node.getFilter();

        FilterConfigInterface filterConfig = node.getFilterConfig();

        if (filter instanceof LogicFilterImpl) {
            List<Filter> filterList = new ArrayList<Filter>();

            createFilterList(node, filterList);

            return filterConfig.createLogicFilter(filterList);
        }

        List<Expression> parameterFilter = new ArrayList<Expression>();

        if (filter instanceof FidFilterImpl) {
            createExpressionParameterList(node, 1, parameterFilter);
        } else if (filter instanceof BinaryTemporalOperator) {
            createExpressionParameterList(node, 2, parameterFilter);
        } else if (filter instanceof PropertyIsBetween) {
            createExpressionParameterList(node, 3, parameterFilter);
        } else if (filter instanceof PropertyIsNull) {
            createExpressionParameterList(node, 1, parameterFilter);
        } else if (filter instanceof PropertyIsLike) {
            createExpressionParameterList(node, 6, parameterFilter);
        } else if (filter instanceof BinarySpatialOperator) {
            createExpressionParameterList(node, 2, parameterFilter);
        } else if (filter instanceof BinaryComparisonAbstract) {
            if (filter instanceof Not) {
                createExpressionParameterList(node, 1, parameterFilter);
            } else if (filter instanceof PropertyIsGreaterThan) {
                createExpressionParameterList(node, 2, parameterFilter);
            } else {
                createExpressionParameterList(node, 3, parameterFilter);
            }
        } else {
            return filter;
        }

        return filterConfig.createFilter(parameterFilter);
    }

    /**
     * Creates the parameter list from expressions.
     *
     * @param node the node
     * @param noOfExpressions the no of expressions
     * @param parameterFilter the parameter filter
     */
    private void createExpressionParameterList(FilterNode node, int noOfExpressions,
            List<Expression> parameterFilter) {
        if (noOfExpressions <= node.getChildCount()) {
            for (int index = 0; index < noOfExpressions; index++) {
                ExpressionNode expressionNode = (ExpressionNode) node.getChildAt(index);

                Expression expression = addExpression(expressionNode);

                parameterFilter.add(expression);
            }
        }
    }

    /**
     * Creates the filter list.
     *
     * @param node the node
     * @param filterList the filter list
     */
    private void createFilterList(FilterNode node, List<Filter> filterList) {
        for (int index = 0; index < node.getChildCount(); index++) {
            FilterNode filterNode = (FilterNode) node.getChildAt(index);

            Filter filter = addFilter(filterNode);

            filterList.add(filter);
        }
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
    public void dataSourceLoaded(GeometryTypeEnum geometryType,
            boolean isConnectedToDataSourceFlag) {
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
        Class<?> dataType = Object.class;

        return showFilterDialog(dataType, filter);
    }

    /**
     * Populate.
     *
     * @param filter the filter
     */
    @Override
    public void populate(Filter filter) {
        this.filter = filter;
    }

    /**
     * Gets the filter string.
     *
     * @return the filter string
     */
    @Override
    public String getFilterString() {
        if (overallFilter != null) {
            return overallFilter.toString();
        }
        return null;
    }

    /**
     * Close dialog.
     *
     * @param okButton the ok button
     */
    private void closeDialog(boolean okButton) {
        okButtonPressed = okButton;

        DataSourceInterface dataSource = DataSourceFactory.getDataSource();
        if (dataSource != null) {
            dataSource.removeListener(this);
        }

        setVisible(false);
    }

    /**
     * Gets the filter.
     *
     * @return the filter
     */
    @Override
    public Filter getFilter() {
        return overallFilter;
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
}
