package com.sldeditor.ui.tree;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.EventObject;

import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreePath;

import org.geotools.styling.FillImpl;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.StrokeImpl;
import org.geotools.styling.Symbolizer;

import com.sldeditor.common.data.SLDTreeUpdatedInterface;
import com.sldeditor.common.tree.leaf.SLDTreeLeafFactory;
import com.sldeditor.datasource.SLDEditorFile;

/**
 * Class to allow the selecting of checkboxes in a JTree.
 * 
 * @author Robert Ward (SCISYS)
 */
class CheckBoxNodeEditor extends AbstractCellEditor implements TreeCellEditor, ActionListener {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The renderer. */
    private ComponentCellRenderer renderer;

    /** The tree. */
    private JTree tree;

    /** The symbolizer. */
    private Symbolizer symbolizer = null;

    /** The user object. */
    private Object userObject = null;

    /** The label. */
    private JLabel label;

    /** The check box. */
    private JCheckBox checkBox;

    /** The panel. */
    private JPanel panel;

    /**
     * Instantiates a new check box node editor.
     *
     * @param tree the tree
     * @param renderer the renderer
     * @param sldTree the sld tree
     */
    public CheckBoxNodeEditor(JTree tree, ComponentCellRenderer renderer, SLDTreeUpdatedInterface sldTree) {
        this.tree = tree;
        this.renderer = renderer;

        checkBox = new JCheckBox();
        checkBox.addActionListener(this);
        checkBox.setBackground(UIManager.getColor("Tree.background"));
        checkBox.setBorder(null);
        checkBox.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                checkBox.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
            }
            public void mouseReleased(MouseEvent e) {
                checkBox.setBorder(null);
                if(sldTree != null)
                {
                    sldTree.leafSelected();
                    SLDEditorFile.getInstance().renderSymbol();
                }
            }
        });
        label = new JLabel();
        label.setBackground(UIManager.getColor("Tree.background"));
        label.setBorder(null);
        panel = new JPanel();
        panel.setOpaque(false);
        panel.add(checkBox);
        panel.add(label);
    }

    /* (non-Javadoc)
     * @see javax.swing.CellEditor#getCellEditorValue()
     */
    public Object getCellEditorValue() {

        if(userObject instanceof StrokeImpl)
        {
            return SLDTreeLeafFactory.getInstance().updateStroke(checkBox.isSelected(), symbolizer);
        }
        else if(userObject instanceof FillImpl)
        {
            return SLDTreeLeafFactory.getInstance().updateFill(checkBox.isSelected(), symbolizer);
        }

        return null;
    }

    /* (non-Javadoc)
     * @see javax.swing.AbstractCellEditor#isCellEditable(java.util.EventObject)
     */
    public boolean isCellEditable(EventObject event) {
        boolean returnValue = false;
        if (event instanceof MouseEvent) {
            MouseEvent mouseEvent = (MouseEvent) event;
            TreePath path = tree.getPathForLocation(mouseEvent.getX(),
                    mouseEvent.getY());
            if (path != null) {
                Object node = path.getLastPathComponent();
                if ((node != null) && (node instanceof DefaultMutableTreeNode)) {
                    DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) node;

                    if(treeNode.getParent() != null)
                    {
                        Object parentUserObject = ((DefaultMutableTreeNode)treeNode.getParent()).getUserObject();

                        if(parentUserObject instanceof PolygonSymbolizer)
                        {
                            symbolizer = (Symbolizer)parentUserObject;
                            userObject = treeNode.getUserObject();
                            returnValue = treeNode.isLeaf();
                        }
                    }
                }
            }
        }
        return returnValue;
    }

    /* (non-Javadoc)
     * @see javax.swing.tree.TreeCellEditor#getTreeCellEditorComponent(javax.swing.JTree, java.lang.Object, boolean, boolean, boolean, int)
     */
    public Component getTreeCellEditorComponent(JTree tree, Object value,
            boolean selected, boolean expanded, boolean leaf, int row) {

        DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
        Object userObject = node.getUserObject();
        Object parentUserObject = null;

        DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) node.getParent();
        if (parentNode != null) {
            parentUserObject = parentNode.getUserObject();
        }

        if(ComponentCellRenderer.showCheckbox(parentUserObject, userObject))
        {
            DefaultMutableTreeNode parent = (DefaultMutableTreeNode) node.getParent();
            Symbolizer symbolizer = (Symbolizer) parent.getUserObject();
            boolean selectedItem = SLDTreeLeafFactory.getInstance().isItemSelected(userObject, symbolizer);

            checkBox.setSelected(selectedItem);
            label.setText(ComponentCellRenderer.getItemText(node, userObject));

            return panel;
        }

        Component editor = renderer.getTreeCellRendererComponent(tree, value,
                true, expanded, leaf, row, true);

        return editor;
    }

    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        super.stopCellEditing();
    }
}