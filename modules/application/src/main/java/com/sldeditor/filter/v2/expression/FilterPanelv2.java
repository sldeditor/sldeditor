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
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

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

import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.data.FeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.AttributeExpressionImpl;
import org.geotools.filter.BinaryComparisonAbstract;
import org.geotools.filter.FidFilterImpl;
import org.geotools.filter.FunctionExpressionImpl;
import org.geotools.filter.LiteralExpressionImpl;
import org.geotools.filter.LogicFilterImpl;
import org.geotools.filter.MathExpressionImpl;
import org.geotools.filter.function.FilterFunction_strConcat;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.styling.UserLayer;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.PropertyDescriptor;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.Not;
import org.opengis.filter.PropertyIsBetween;
import org.opengis.filter.PropertyIsGreaterThan;
import org.opengis.filter.PropertyIsLike;
import org.opengis.filter.PropertyIsNull;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.spatial.BinarySpatialOperator;
import org.opengis.filter.temporal.BinaryTemporalOperator;

import com.sldeditor.common.Controller;
import com.sldeditor.common.DataSourceFieldInterface;
import com.sldeditor.common.DataSourcePropertiesInterface;
import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.common.vendoroption.VersionData;
import com.sldeditor.datasource.DataSourceInterface;
import com.sldeditor.datasource.DataSourceUpdatedInterface;
import com.sldeditor.datasource.SLDEditorFileInterface;
import com.sldeditor.datasource.attribute.DataSourceAttributeListInterface;
import com.sldeditor.datasource.impl.CreateDataSourceInterface;
import com.sldeditor.datasource.impl.DataSourceFactory;
import com.sldeditor.datasource.impl.GeometryTypeEnum;
import com.sldeditor.filter.FilterPanelInterface;
import com.sldeditor.filter.v2.function.FilterConfigInterface;
import com.sldeditor.filter.v2.function.FilterManager;
import com.vividsolutions.jts.geom.Geometry;

/**
 * The Class FilterPanelv2.
 *
 * @author Robert Ward (SCISYS)
 */
public class FilterPanelv2 extends JDialog implements ExpressionFilterInterface, DataSourceUpdatedInterface, FilterPanelInterface {

    /**
     * The Class TestDataSourceImpl.
     */
    public static class TestDataSourceImpl implements DataSourceInterface {

        /** The listener list. */
        private List<DataSourceUpdatedInterface> listenerList = new ArrayList<DataSourceUpdatedInterface>();

        /**
         * Adds the listener.
         *
         * @param listener the listener
         */
        @Override
        public void addListener(DataSourceUpdatedInterface listener) {
            listenerList.add(listener);
        }

        /**
         * Connect.
         *
         * @param editorFile the editor file
         */
        @Override
        public void connect(SLDEditorFileInterface editorFile) {
        }

        /**
         * Reset.
         */
        @Override
        public void reset() {
        }

        /**
         * Gets the feature source.
         *
         * @return the feature source
         */
        @Override
        public FeatureSource<SimpleFeatureType, SimpleFeature> getFeatureSource() {
            return null;
        }

        /**
         * Gets the attributes.
         *
         * @param expectedDataType the expected data type
         * @return the attributes
         */
        @Override
        public List<String> getAttributes(Class<?> expectedDataType) {
            List<String> attributeList = new ArrayList<String>();

            if(expectedDataType == Date.class)
            {
                attributeList.add("Date_1");
                attributeList.add("Date_2");
            }
            else if(expectedDataType == Number.class)
            {
                attributeList.add("Integer_1");
                attributeList.add("Double_2");
            }
            else if(expectedDataType == Geometry.class)
            {
                attributeList.add("Geometry_1");
            }
            else if(expectedDataType == String.class)
            {
                attributeList.add("String_1");
                attributeList.add("String_2");
                attributeList.add("String_3");
            }
            else
            {
                attributeList.add("Date_1");
                attributeList.add("Date_2");
                attributeList.add("Integer_1");
                attributeList.add("Double_2");
                attributeList.add("Geometry_1");
                attributeList.add("String_1");
                attributeList.add("String_2");
                attributeList.add("String_3");
            }

            return attributeList;
        }

        /**
         * Gets the geometry type.
         *
         * @return the geometry type
         */
        @Override
        public GeometryTypeEnum getGeometryType() {
            return null;
        }

        /**
         * Read attributes.
         *
         * @param attributeData the attribute data
         */
        @Override
        public void readAttributes(DataSourceAttributeListInterface attributeData) {
        }

        /**
         * Gets the data connector properties.
         *
         * @return the data connector properties
         */
        @Override
        public DataSourcePropertiesInterface getDataConnectorProperties() {
            return null;
        }

        /**
         * Gets the available data store list.
         *
         * @return the available data store list
         */
        @Override
        public List<String> getAvailableDataStoreList() {
            return null;
        }

        /**
         * Update fields.
         *
         * @param attributeData the attribute data
         */
        @Override
        public void updateFields(DataSourceAttributeListInterface attributeData) {
        }

        /**
         * Adds the field.
         *
         * @param dataSourceField the data source field
         */
        @Override
        public void addField(DataSourceFieldInterface dataSourceField) {
        }

        /**
         * Sets the data source creation.
         *
         * @param internalDataSource the internal data source
         * @param externalDataSource the external data source
         */
        @Override
        public void setDataSourceCreation(CreateDataSourceInterface internalDataSource,
                CreateDataSourceInterface externalDataSource,
                CreateDataSourceInterface inlineDataSource) {
        }

        /**
         * Gets the property descriptor list.
         *
         * @return the property descriptor list
         */
        @Override
        public Collection<PropertyDescriptor> getPropertyDescriptorList() {
            return null;
        }

        /**
         * Notify data source loaded.
         */
        public void notifyDataSourceLoaded()
        {
            for(DataSourceUpdatedInterface listener : listenerList)
            {
                listener.dataSourceLoaded(getGeometryType(), false);
            }
        }

        /**
         * Removes the listener.
         *
         * @param listener the listener
         */
        @Override
        public void removeListener(DataSourceUpdatedInterface listener) {
        }

        @Override
        public AbstractGridCoverage2DReader getGridCoverageReader() {
            return null;
        }

        @Override
        public FeatureSource<SimpleFeatureType, SimpleFeature> getExampleFeatureSource() {
            return null;
        }

        @Override
        public Map<UserLayer, FeatureSource<SimpleFeatureType, SimpleFeature>> getUserLayerFeatureSource() {
            return null;
        }

        @Override
        public void updateUserLayers() {
        }
    }

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

    /** The enum value list. */
    private List<String> enumValueList = null;

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
        if(dataSource != null)
        {
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
     */
    @Override
    public void configure(String title, Class<?> fieldType) {

        setTitle(title);

        expressionPanel.setDataType(fieldType);
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
                selectedNode = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();

                CardLayout cardLayout = (CardLayout) dataPanel.getLayout();

                if(selectedNode instanceof ExpressionNode)
                {
                    ExpressionNode expressionNode = (ExpressionNode) selectedNode;
                    if(expressionNode.getExpressionType() == ExpressionTypeEnum.LITERAL)
                    {
                        cardLayout.show(dataPanel, literalPanel.getClass().getName());
                        literalPanel.setSelectedNode(selectedNode, enumValueList);
                    }
                    else if(expressionNode.getExpressionType() == ExpressionTypeEnum.PROPERTY)
                    {
                        cardLayout.show(dataPanel, propertyPanel.getClass().getName());
                        propertyPanel.setSelectedNode(selectedNode);
                    }
                    else if(expressionNode.getExpressionType() == ExpressionTypeEnum.ENVVAR)
                    {
                        cardLayout.show(dataPanel, envVarPanel.getClass().getName());
                        envVarPanel.setSelectedNode(selectedNode);
                    }
                    else
                    {
                        cardLayout.show(dataPanel, expressionPanel.getClass().getName());
                        expressionPanel.setSelectedNode(selectedNode, enumValueList);
                    }
                }
                else if(selectedNode instanceof FilterNode)
                {
                    cardLayout.show(dataPanel, filterPanel.getClass().getName());
                    filterPanel.setSelectedNode(selectedNode);
                }
                else
                {
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

        JButton btnOk = new JButton(Localisation.getString(ExpressionPanelv2.class, "common.ok"));
        btnOk.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                closeDialog(true);
            }
        });
        buttonPanel.add(btnOk);

        JButton btnCancel = new JButton(Localisation.getString(ExpressionPanelv2.class, "common.cancel"));
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
     * The main method.
     *
     * @param args the arguments
     */
    public static void main(String[] args) {

        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {

                TestDataSourceImpl testDataSource = new TestDataSourceImpl();
                @SuppressWarnings("unused")
                DataSourceInterface dataSource = DataSourceFactory.createDataSource(testDataSource);

                FilterFactory ff = CommonFactoryFinder.getFilterFactory( null );

                FilterPanelv2 panel = new FilterPanelv2(null);
                FilterFunction_strConcat expression = new FilterFunction_strConcat();
                List<Expression> paramList = new ArrayList<Expression>();
                paramList.add(ff.literal("abc"));
                paramList.add(ff.literal("xyz"));
                expression.setParameters(paramList);
                Class<?> type = String.class;

                testDataSource.notifyDataSourceLoaded();

                try {
                    //  Filter result = CQL.toFilter("Date_1 DOES-NOT-EXIST" );
                    Filter result = CQL.toFilter("Date_1 EXISTS" );
                    panel.showFilterDialog(type, result);
                }
                catch (CQLException e) {
                    e.printStackTrace();
                }

                System.exit(0);
            }
        });
    }

    /**
     * Show filter dialog.
     *
     * @param type the type
     * @param filter the filter
     * @return true, if successful
     */
    private boolean showFilterDialog(Class<?> type, Filter filter) {

        rootNode = new FilterNode();
        if(model != null)
        {
            model.setRoot(rootNode);
            FilterNode filterNode = (FilterNode) rootNode;
            filterNode.setType(type);

            if(filter != null)
            {
                populateFilter((FilterNode)rootNode, filter);
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

        if(filter != null)
        {
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
    public void dataApplied()
    {
        DefaultMutableTreeNode node;
        if(selectedNode.isLeaf())
        {
            node = (DefaultMutableTreeNode) selectedNode.getParent();
            if(node == null)
            {
                node = selectedNode;
            }
        }
        else
        {
            node = selectedNode;
        }
        TreeNode[] tmpNode = node.getPath();

        model.reload(); // This notifies the listeners and changes the GUI

        tree.expandPath(new TreePath(tmpNode));

        displayResult();
    }

    /**
     * Display result.
     */
    private void displayResult() {
        String result = "";
        if(rootNode instanceof FilterNode)
        {
            overallFilter = addFilter((FilterNode)rootNode);

            try
            {
                result = CQL.toCQL( overallFilter );
            }
            catch(Exception e)
            {
            }
        }

        textArea.setText(result);
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

        if(filter instanceof LogicFilterImpl)
        {
            List<Filter> filterList = new ArrayList<Filter>();

            createFilterList(node, filterList);

            return filterConfig.createLogicFilter(filterList);
        }

        List<Expression> parameterFilter = new ArrayList<Expression>();

        if(filter instanceof FidFilterImpl)
        {
            createExpressionParameterList(node, 1, parameterFilter);
        }
        else if(filter instanceof BinaryTemporalOperator)
        {
            createExpressionParameterList(node, 2, parameterFilter);
        }
        else if(filter instanceof PropertyIsBetween)
        {
            createExpressionParameterList(node, 3, parameterFilter);
        }
        else if(filter instanceof PropertyIsNull)
        {
            createExpressionParameterList(node, 1, parameterFilter);
        }
        else if(filter instanceof PropertyIsLike)
        {
            createExpressionParameterList(node, 6, parameterFilter);
        }
        else if(filter instanceof BinarySpatialOperator)
        {
            createExpressionParameterList(node, 2, parameterFilter);
        }
        else if(filter instanceof BinaryComparisonAbstract)
        {
            if(filter instanceof Not)
            {
                createExpressionParameterList(node, 1, parameterFilter);
            }
            else if(filter instanceof PropertyIsGreaterThan)
            {
                createExpressionParameterList(node, 2, parameterFilter);
            }
            else
            {
                createExpressionParameterList(node, 3, parameterFilter);
            }
        }
        else
        {
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
    private void createExpressionParameterList(FilterNode node, int noOfExpressions, List<Expression> parameterFilter) {
        if(noOfExpressions <= node.getChildCount())
        {
            for(int index = 0; index < noOfExpressions; index ++)
            {
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
        for(int index = 0; index < node.getChildCount(); index ++)
        {
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

        if(expression instanceof LiteralExpressionImpl)
        {
            return expression;
        }
        else if(expression instanceof AttributeExpressionImpl)
        {
            return expression;
        }
        else if(expression instanceof FunctionExpressionImpl)
        {
            FunctionExpressionImpl functionExpression = (FunctionExpressionImpl) expression;

            List<Expression> parameterlist = new ArrayList<Expression>();
            for(int childIndex = 0; childIndex < node.getChildCount(); childIndex ++)
            {
                ExpressionNode childNode = (ExpressionNode) node.getChildAt(childIndex);

                parameterlist.add(addExpression(childNode));
            }

            functionExpression.setParameters(parameterlist);

            return functionExpression;
        }
        else if(expression instanceof MathExpressionImpl)
        {
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
        return textArea.getText();
    }

    /**
     * Close dialog.
     *
     * @param okButton the ok button
     */
    private void closeDialog(boolean okButton) {
        okButtonPressed = okButton;

        DataSourceInterface dataSource = DataSourceFactory.getDataSource();
        if(dataSource != null)
        {
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

    /* (non-Javadoc)
     * @see com.sldeditor.filter.v2.expression.ExpressionFilterInterface#getVendorOptionList()
     */
    @Override
    public List<VersionData> getVendorOptionList() {
        return vendorOptionList;
    }
}
