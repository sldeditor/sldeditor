package com.sldeditor.geometryfield.ui;

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
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreePath;

import org.geotools.styling.Symbolizer;

import com.sldeditor.common.data.SLDTreeUpdatedInterface;
import com.sldeditor.geometryfield.GeometryFieldManager;

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

        return userObject;
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

                    if(treeNode != null)
                    {
                        userObject = treeNode.getUserObject();
                        returnValue = true;
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

        if(ComponentCellRenderer.showCheckbox(userObject))
        {
            boolean selectedValue = !GeometryFieldManager.getInstance().isSelected(userObject);
            GeometryFieldManager.getInstance().setSelected(userObject, selectedValue);

            checkBox.setSelected(selectedValue);
            label.setText(ComponentCellRenderer.getItemText(userObject));

            // Update the child nodes
            ((DefaultTreeModel)tree.getModel()).reload(node);

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