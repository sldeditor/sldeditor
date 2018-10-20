package com.sldeditor.ui.tree;

import com.sldeditor.common.data.SLDTreeUpdatedInterface;
import com.sldeditor.common.tree.leaf.SLDTreeLeafFactory;
import com.sldeditor.datasource.SLDEditorFile;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.EventObject;
import javax.swing.AbstractCellEditor;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreePath;
import org.geotools.styling.FillImpl;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.StrokeImpl;
import org.geotools.styling.Symbolizer;

/**
 * Class to allow the selecting of checkboxes in a JTree.
 *
 * @author Robert Ward (SCISYS)
 */
class CheckBoxNodeEditor extends AbstractCellEditor implements TreeCellEditor, ActionListener {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The renderer. */
    private transient ComponentCellRenderer renderer;

    /** The tree. */
    private JTree tree;

    /** The symbolizer. */
    private transient Symbolizer symbolizer = null;

    /** The user object. */
    private transient Object userObject = null;

    /** The checkbox node panel. */
    private CheckBoxPanel panel = new CheckBoxPanel();

    /**
     * Instantiates a new check box node editor.
     *
     * @param tree the tree
     * @param renderer the renderer
     * @param sldTree the sld tree
     */
    public CheckBoxNodeEditor(
            JTree tree, ComponentCellRenderer renderer, SLDTreeUpdatedInterface sldTree) {
        this.tree = tree;
        this.renderer = renderer;

        panel.setCheckboxActionListener(this);
        panel.setCheckboxMouseListener(
                new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        // Do nothing
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                        if (sldTree != null) {
                            sldTree.leafSelected();
                            SLDEditorFile.getInstance().renderSymbol();
                        }
                    }
                });
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.swing.CellEditor#getCellEditorValue()
     */
    public Object getCellEditorValue() {

        if (userObject instanceof StrokeImpl) {
            return SLDTreeLeafFactory.getInstance()
                    .updateStroke(panel.isCheckBoxSelected(), symbolizer);
        } else if (userObject instanceof FillImpl) {
            return SLDTreeLeafFactory.getInstance()
                    .updateFill(panel.isCheckBoxSelected(), symbolizer);
        }

        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.swing.AbstractCellEditor#isCellEditable(java.util.EventObject)
     */
    public boolean isCellEditable(EventObject event) {
        boolean returnValue = false;
        if (event instanceof MouseEvent) {
            MouseEvent mouseEvent = (MouseEvent) event;
            TreePath path = tree.getPathForLocation(mouseEvent.getX(), mouseEvent.getY());
            if (path != null) {
                Object node = path.getLastPathComponent();
                if (node instanceof DefaultMutableTreeNode) {
                    DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) node;

                    if (treeNode.getParent() != null) {
                        Object parentUserObject =
                                ((DefaultMutableTreeNode) treeNode.getParent()).getUserObject();

                        if (parentUserObject instanceof PolygonSymbolizer) {
                            symbolizer = (Symbolizer) parentUserObject;
                            userObject = treeNode.getUserObject();
                            returnValue = treeNode.isLeaf();
                        }
                    }
                }
            }
        }
        return returnValue;
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.swing.tree.TreeCellEditor#getTreeCellEditorComponent(javax.swing.JTree, java.lang.Object, boolean, boolean, boolean, int)
     */
    public Component getTreeCellEditorComponent(
            JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row) {

        DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
        Object localUserObject = node.getUserObject();
        Object parentUserObject = null;

        DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) node.getParent();
        if (parentNode != null) {
            parentUserObject = parentNode.getUserObject();
        }

        if (ComponentCellRenderer.showCheckbox(parentUserObject, localUserObject)) {
            DefaultMutableTreeNode parent = (DefaultMutableTreeNode) node.getParent();
            Symbolizer localSymbolizer = (Symbolizer) parent.getUserObject();
            boolean selectedItem =
                    SLDTreeLeafFactory.getInstance()
                            .isItemSelected(localUserObject, localSymbolizer);

            panel.setCheckboxSelected(selectedItem);
            panel.setLabelText(ComponentCellRenderer.getItemText(node, localUserObject));
            panel.setSelected(true, true);

            return panel;
        }

        return renderer.getTreeCellRendererComponent(tree, value, true, expanded, leaf, row, true);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        super.stopCellEditing();
    }
}
