package com.sldeditor.ui.detail.vendor.geoserver.marker.wkt;

import com.sldeditor.common.Controller;
import com.sldeditor.common.localisation.Localisation;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

/**
 * Dialog to allow the user to view/edit WKT data.
 *
 * @author Robert Ward (SCISYS)
 */
public class WKTDialog extends JDialog {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The model. */
    private WKTTypeComboBoxModel model;

    /** The table. */
    private JTable table;

    /** The table point model. */
    private WKTPointModel tablePointModel = new WKTPointModel();

    /** The geometry type combo box. */
    private JComboBox<WKTType> geometryTypeComboBox = null;

    /** The wkt text area. */
    private JTextArea wktTextArea = null;

    /** The primitive list. */
    private JList<String> segmentList = null;

    /** The primitive list model. */
    private DefaultListModel<String> segmentListModel = new DefaultListModel<String>();

    /** The wkt geometry. */
    private WKTGeometry wktGeometry = null;

    /** The multi list. */
    private JList<String> multiList = null;

    /** The multi list model. */
    private DefaultListModel<String> multiListModel = new DefaultListModel<String>();

    /** The multi panel. */
    private JPanel multiPanel;

    /** The add multi button. */
    private JButton addMultiButton;

    /** The remove multi button. */
    private JButton removeMultiButton;

    /** The add segment button. */
    private JButton addSegmentButton;

    /** The remove segment button. */
    private JButton removeSegmentButton;

    /** The add point button. */
    private JButton addPointButton;

    /** The remove point button. */
    private JButton removePointButton;

    /** The ok button pressed. */
    private boolean okButtonPressed = false;

    /** The reload button. */
    private JButton btnReload;

    /** Constructor. */
    public WKTDialog() {
        super(Controller.getInstance().getFrame());
        setTitle(Localisation.getString(WKTDialog.class, "WKTDialog.title"));
        setResizable(false);
        setModal(true);

        createUI();

        Controller.getInstance().centreDialog(this);
    }

    /** Creates the ui. */
    @SuppressWarnings("unchecked")
    private void createUI() {
        JPanel wktSelectionPanel = new JPanel();
        getContentPane().add(wktSelectionPanel, BorderLayout.NORTH);

        model = new WKTTypeComboBoxModel(WKTConversion.getWKTTypeData());
        geometryTypeComboBox = new JComboBox<WKTType>(model);
        geometryTypeComboBox.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        geometryTypeUpdated();
                    }
                });
        wktSelectionPanel.add(geometryTypeComboBox);

        JPanel buttonPanel = new JPanel();
        FlowLayout flowLayout = (FlowLayout) buttonPanel.getLayout();
        flowLayout.setAlignment(FlowLayout.RIGHT);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        JButton btnOk = new JButton(Localisation.getString(WKTDialog.class, "common.ok"));
        btnOk.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        okButtonPressed = true;
                        setVisible(false);
                    }
                });
        buttonPanel.add(btnOk);

        JButton btnCancel = new JButton(Localisation.getString(WKTDialog.class, "common.cancel"));
        btnCancel.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        okButtonPressed = false;
                        setVisible(false);
                    }
                });
        buttonPanel.add(btnCancel);

        JPanel mainPanel = new JPanel();
        getContentPane().add(mainPanel, BorderLayout.CENTER);
        mainPanel.setLayout(new BorderLayout(0, 0));

        JPanel panel = new JPanel();
        BoxLayout boxLayout = new BoxLayout(panel, BoxLayout.X_AXIS);
        panel.setLayout(boxLayout);

        mainPanel.add(panel, BorderLayout.WEST);

        //
        // Multi shape panel
        //
        createMultiShapePanel(panel);

        panel.add(Box.createRigidArea(new Dimension(5, 0)));

        //
        // Segment panel
        //
        createSegmentPanel(panel);

        panel.add(Box.createRigidArea(new Dimension(5, 0)));

        //
        // Coordinate panel
        //

        createPointPanel(panel);

        //
        // Text area
        //
        JPanel stringPanel = new JPanel();
        mainPanel.add(stringPanel, BorderLayout.SOUTH);
        stringPanel.setLayout(new BorderLayout(0, 0));

        JScrollPane textAreaScrollPane = new JScrollPane();
        stringPanel.add(textAreaScrollPane);

        wktTextArea = new JTextArea();
        wktTextArea.setRows(5);
        wktTextArea.setEditable(true);
        wktTextArea.addKeyListener(
                new KeyListener() {

                    @Override
                    public void keyTyped(KeyEvent e) {
                        btnReload.setEnabled(true);
                    }

                    @Override
                    public void keyPressed(KeyEvent e) {
                        // Do nothing
                    }

                    @Override
                    public void keyReleased(KeyEvent e) {
                        // Do nothing
                    }
                });
        textAreaScrollPane.setViewportView(wktTextArea);

        JPanel textAreaButtonPanel = new JPanel();
        stringPanel.add(textAreaButtonPanel, BorderLayout.EAST);

        btnReload = new JButton(Localisation.getString(WKTDialog.class, "WKTDialog.reload"));
        btnReload.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        reload();
                    }
                });
        textAreaButtonPanel.add(btnReload);

        this.setSize(520, 365);
    }

    /**
     * Show multi panel.
     *
     * @param wktType the wkt type
     */
    private void showMultiPanel(WKTType wktType) {
        boolean enabled = false;

        if (wktType != null) {
            enabled = wktType.canHaveMultipleShapes();
        }
        multiList.setVisible(enabled);
        addMultiButton.setEnabled(enabled);
        removeMultiButton.setEnabled(enabled);
    }

    /**
     * Creates the segment panel.
     *
     * @param panel the panel
     */
    private void createSegmentPanel(JPanel panel) {
        JPanel segmentPanel = new JPanel();
        panel.add(segmentPanel);
        segmentPanel.setLayout(new BorderLayout(0, 0));
        segmentPanel.setPreferredSize(new Dimension(150, 200));

        JScrollPane segmentScrollPane = new JScrollPane();
        segmentPanel.add(segmentScrollPane, BorderLayout.CENTER);

        segmentList = new JList<String>();
        segmentList.setModel(segmentListModel);
        segmentList.addListSelectionListener(
                new ListSelectionListener() {
                    /**
                     * Value changed.
                     *
                     * @param e the e
                     */
                    @Override
                    public void valueChanged(ListSelectionEvent e) {
                        if (!e.getValueIsAdjusting()) {
                            int selectedIndex = segmentList.getSelectedIndex();

                            if (wktGeometry.getNoOfSegments() == 1) {
                                if (selectedIndex >= 0) {
                                    tablePointModel.populate(
                                            wktGeometry.getSegmentList(0).get(selectedIndex));
                                }
                            } else {
                                int multiSelectedIndex = multiList.getSelectedIndex();
                                if (selectedIndex < 0) {
                                    selectedIndex = 0;
                                }

                                WKTSegmentList wktPointList = null;
                                List<WKTSegmentList> segmentList2 =
                                        wktGeometry.getSegmentList(multiSelectedIndex);
                                if ((segmentList2 != null)
                                        && (selectedIndex >= 0)
                                        && (selectedIndex < segmentList2.size())) {
                                    wktPointList = segmentList2.get(selectedIndex);
                                }
                                tablePointModel.populate(wktPointList);
                            }

                            updateSegmentButtons();
                        }
                    }
                });
        segmentScrollPane.setViewportView(segmentList);

        JPanel segmentButtonPanel = new JPanel();
        FlowLayout flowLayout_1 = (FlowLayout) segmentButtonPanel.getLayout();
        flowLayout_1.setAlignment(FlowLayout.RIGHT);
        segmentPanel.add(segmentButtonPanel, BorderLayout.SOUTH);

        //
        // Add segment button
        //
        addSegmentButton = new JButton("+");
        addSegmentButton.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        addSegment();
                    }
                });
        segmentButtonPanel.add(addSegmentButton);

        //
        // Remove segment button
        //
        removeSegmentButton = new JButton("-");
        removeSegmentButton.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        removeSegment();
                    }
                });
        segmentButtonPanel.add(removeSegmentButton);

        JLabel lblSegment = new JLabel("Segment");
        segmentPanel.add(lblSegment, BorderLayout.NORTH);
    }

    /**
     * Creates the point panel.
     *
     * @param panel the panel
     */
    private void createPointPanel(JPanel panel) {
        JPanel coordinatePanel = new JPanel();
        panel.add(coordinatePanel);
        coordinatePanel.setLayout(new BorderLayout(5, 0));
        coordinatePanel.setPreferredSize(new Dimension(200, 200));

        JScrollPane scrollPanePoint = new JScrollPane();
        coordinatePanel.add(scrollPanePoint);

        table = new JTable();
        table.setModel(tablePointModel);
        tablePointModel.addTableModelListener(
                new TableModelListener() {

                    @Override
                    public void tableChanged(TableModelEvent e) {
                        updatePointButtons();
                        updateWKTString();
                    }
                });

        // Handle table selection changes
        ListSelectionModel selectionModel = table.getSelectionModel();

        selectionModel.addListSelectionListener(
                new ListSelectionListener() {
                    public void valueChanged(ListSelectionEvent e) {
                        updatePointButtons();
                    }
                });

        scrollPanePoint.setViewportView(table);

        JPanel pointButtonPanel = new JPanel();
        FlowLayout flowLayout_3 = (FlowLayout) pointButtonPanel.getLayout();
        flowLayout_3.setAlignment(FlowLayout.RIGHT);
        coordinatePanel.add(pointButtonPanel, BorderLayout.SOUTH);

        //
        // Add point button
        //

        addPointButton = new JButton("+");
        addPointButton.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        addPoint();
                    }
                });
        pointButtonPanel.add(addPointButton);

        removePointButton = new JButton("-");
        removePointButton.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        removePoint();
                    }
                });
        pointButtonPanel.add(removePointButton);

        JLabel lblCoordinates = new JLabel("Coordinates");
        coordinatePanel.add(lblCoordinates, BorderLayout.NORTH);
    }

    /**
     * Creates the multi shape panel.
     *
     * @param panel the panel
     */
    private void createMultiShapePanel(JPanel panel) {
        multiPanel = new JPanel();
        panel.add(multiPanel);
        multiPanel.setLayout(new BorderLayout(0, 0));
        multiPanel.setPreferredSize(new Dimension(150, 200));

        JScrollPane scrollPanelMulti = new JScrollPane();
        multiPanel.add(scrollPanelMulti, BorderLayout.CENTER);

        multiList = new JList<String>();
        multiList.setModel(multiListModel);
        multiList.addListSelectionListener(
                new ListSelectionListener() {
                    /**
                     * Value changed.
                     *
                     * @param e the e
                     */
                    @Override
                    public void valueChanged(ListSelectionEvent e) {
                        if (!e.getValueIsAdjusting()) {
                            int selectedIndex = multiList.getSelectedIndex();

                            if (selectedIndex >= 0) {
                                populateSegmentList(selectedIndex);
                            }
                        }
                    }
                });
        scrollPanelMulti.setViewportView(multiList);

        JPanel multiButtonPanel = new JPanel();
        FlowLayout flowLayout_2 = (FlowLayout) multiButtonPanel.getLayout();
        flowLayout_2.setAlignment(FlowLayout.RIGHT);
        multiPanel.add(multiButtonPanel, BorderLayout.SOUTH);

        //
        // Add multi shape button
        //
        addMultiButton = new JButton("+");
        addMultiButton.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        addMultiShape();
                    }
                });
        multiButtonPanel.add(addMultiButton);

        //
        // Remove multi shape button
        //
        removeMultiButton = new JButton("-");
        removeMultiButton.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        removeMultiShape();
                    }
                });
        multiButtonPanel.add(removeMultiButton);

        JLabel lblShape = new JLabel("Shape");
        multiPanel.add(lblShape, BorderLayout.NORTH);
    }

    //    /**
    //     * The main method.
    //     *
    //     * @param args the arguments
    //     */
    //    public static void main(String[] args) {
    //        WKTDialog dlg = new WKTDialog();
    //
    //        dlg.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    //        // CHECKSTYLE:OFF
    //        // String wktString = "wkt://MULTILINESTRING((-0.25 -0.25, -0.125 -0.25), (0.125
    // -0.25, 0.25
    //        // -0.25), (-0.25 0.25, -0.125 0.25), (0.125 0.25,
    //        // 0.25 0.25))";
    //        // String wktString = "wkt://POINT (30 10)";
    //        // String wktString = "wkt://LINESTRING (30 10, 10 30, 40 40)";
    //        // String wktString = "wkt://POLYGON ((30 10, 40 40, 20 40, 10 20, 30 10))";
    //        // String wktString = "wkt://POLYGON ((35 10, 45 45, 15 40, 10 20, 35 10),(20 30, 35
    // 35, 30
    //        // 20, 20 30))";
    //        // xx String wktString = "wkt://MULTIPOINT ((10 40), (40 30), (20 20), (30 10))";
    //        // String wktString = "wkt://MULTIPOINT (10 40, 40 30, 20 20, 30 10)";
    //        // String wktString = "wkt://MULTILINESTRING ((10 10, 20 20, 10 40), (40 40, 30 30, 40
    // 20,
    //        // 30 10))";
    //        // String wktString = "wkt://MULTIPOLYGON (((30 20, 45 40, 10 40, 30 20)), ((15 5, 40
    // 10, 10
    //        // 20, 5 10, 15 5)))";
    //        // String wktString = "wkt://MULTIPOLYGON (((40 40, 20 45, 45 30, 40 40)), ((20 35, 10
    // 30,
    //        // 10 10, 30 5, 45 20, 20 35), (30 20, 20 15, 20 25,
    //        // 30 20)))";
    //        // CHECKSTYLE:ON
    //        String wktString = "";
    //        dlg.showDialog(wktString);
    //    }

    /**
     * Populate dialog using wkt:// string.
     *
     * @param wktString the wkt string
     */
    protected void populate(String wktString) {

        wktGeometry = WKTConversion.parseWKTString(wktString);

        WKTType geometryType = null;
        boolean valid = false;

        if (wktGeometry != null) {
            geometryType = wktGeometry.getGeometryType();
            valid = wktGeometry.isValid();
        }

        model.setSelectedItem(geometryType);

        updateUI(geometryType);

        if (valid) {
            if (geometryType.canHaveMultipleShapes()) {
                populateMultiShapeList();
            } else {
                populateSegmentList(0);
            }
        }

        wktTextArea.setText(wktString);

        updateWKTString();
    }

    /**
     * Populate segment list.
     *
     * @param index the index
     */
    private void populateSegmentList(int index) {
        segmentListModel.clear();
        for (int segmentIndex = 0;
                segmentIndex < wktGeometry.getSegmentList(index).size();
                segmentIndex++) {
            segmentListModel.addElement(wktGeometry.getSegmentName(segmentIndex));
        }

        if (!segmentListModel.isEmpty()) {
            segmentList.setSelectedIndex(0);
        }
    }

    /** Populate multi shape list. */
    private void populateMultiShapeList() {
        multiListModel.clear();
        segmentListModel.clear();
        for (int index = 0; index < wktGeometry.getNoOfSegments(); index++) {
            multiListModel.addElement(wktGeometry.getMultiShapeName(index));
        }
        multiList.setSelectedIndex(0);
    }

    /** Update segment buttons. */
    private void updateSegmentButtons() {

        boolean enabled = false;

        if (wktGeometry != null) {
            WKTType geometryType = wktGeometry.getGeometryType();
            if (geometryType != null) {
                if (geometryType.canHaveMultipleShapes()) {
                    enabled = !multiListModel.isEmpty();
                } else {
                    enabled = true;
                }
            }
        }
        addSegmentButton.setEnabled(enabled);

        removeSegmentButton.setEnabled(!segmentList.isSelectionEmpty());
    }

    /** Update point buttons. */
    private void updatePointButtons() {
        WKTType wktType = (WKTType) geometryTypeComboBox.getSelectedItem();

        boolean enablePointButtons = false;

        if (wktType != null) {
            enablePointButtons =
                    (wktType.getNumOfPoints() < 0) && (tablePointModel.getRowCount() > 0);
        }

        addPointButton.setEnabled(enablePointButtons);

        int selectedRow = table.getSelectedRow();
        if (enablePointButtons) {
            enablePointButtons = (selectedRow >= 0);
        }
        removePointButton.setEnabled(enablePointButtons);
    }

    /** Update wkt string in the text area. */
    private void updateWKTString() {
        String wktString = WKTConversion.generateWKTString(wktGeometry, true);

        boolean valid = false;
        boolean empty = false;

        if (wktGeometry != null) {
            valid = wktGeometry.isValid();
            empty = wktGeometry.isEmpty();
        }

        if (valid) {
            wktTextArea.setText(wktString);
        }

        if (valid || empty) {
            wktTextArea.setBackground(Color.white);
        } else {
            wktTextArea.setBackground(Color.red);
        }

        btnReload.setEnabled(false);
    }

    /**
     * Show dialog.
     *
     * @param text the text
     * @return true, if successful
     */
    public boolean showDialog(String text) {
        populate(text);

        setVisible(true);

        return okButtonPressed;
    }

    /**
     * Gets the WKT string.
     *
     * @return the WKT string
     */
    public String getWKTString() {
        return WKTConversion.generateWKTString(wktGeometry, false);
    }

    /**
     * Update ui.
     *
     * @param wktType the wkt type
     */
    private void updateUI(WKTType wktType) {
        multiListModel.clear();
        segmentListModel.clear();
        tablePointModel.clear();

        showMultiPanel(wktType);
        tablePointModel.setWKTType(wktType);
        updateSegmentButtons();
        updatePointButtons();
    }

    /** Geometry type updated. */
    protected void geometryTypeUpdated() {
        WKTType wktType = (WKTType) geometryTypeComboBox.getSelectedItem();

        wktGeometry = WKTConversion.createEmpty(wktType);

        updateUI(wktType);
    }

    /** Reload. */
    protected void reload() {
        String wktString = wktTextArea.getText();

        populate(wktString);
    }

    /** Adds the segment. */
    protected void addSegment() {
        int index = 0;
        if (wktGeometry.getGeometryType().canHaveMultipleShapes()) {
            index = multiList.getSelectedIndex();
        }

        int segmentIndex = wktGeometry.addNewSegment(index);
        segmentListModel.addElement(wktGeometry.getSegmentName(segmentIndex));

        if (segmentListModel.size() == 1) {
            segmentList.setSelectedIndex(0);
        }
        updateWKTString();
    }

    /** Removes the segment. */
    protected void removeSegment() {
        int multiShapeIndex = 0;
        if (wktGeometry.getGeometryType().canHaveMultipleShapes()) {
            multiShapeIndex = multiList.getSelectedIndex();
        }

        int selectedSegmentIndex = segmentList.getSelectedIndex();
        wktGeometry.removeSegment(multiShapeIndex, selectedSegmentIndex);

        segmentListModel.remove(selectedSegmentIndex);

        if (selectedSegmentIndex >= segmentListModel.size()) {
            selectedSegmentIndex = segmentListModel.size() - 1;
        }

        if (selectedSegmentIndex < 0) {
            selectedSegmentIndex = 0;
        }
        segmentList.setSelectedIndex(selectedSegmentIndex);
        updateWKTString();
    }

    /** Adds the point. */
    protected void addPoint() {
        tablePointModel.addNewPoint();
    }

    /** Removes the point. */
    protected void removePoint() {
        int selectedIndex = segmentList.getSelectedIndex();
        tablePointModel.removePoint(selectedIndex);
    }

    /** Adds the multi shape. */
    protected void addMultiShape() {
        if (wktGeometry.getGeometryType().canHaveMultipleShapes()) {
            int index = wktGeometry.addNewShape();

            multiListModel.addElement(wktGeometry.getMultiShapeName(index));

            if (multiListModel.size() == 1) {
                multiList.setSelectedIndex(0);
            }
            updateWKTString();
        }
    }

    /** Removes the multi shape. */
    protected void removeMultiShape() {
        int selectedIndex = multiList.getSelectedIndex();
        wktGeometry.removeShape(selectedIndex);

        multiListModel.remove(selectedIndex);

        if (selectedIndex >= multiListModel.size()) {
            selectedIndex = multiListModel.size() - 1;
        }

        if (!multiListModel.isEmpty()) {
            if (selectedIndex < 0) {
                selectedIndex = 0;
            }

            multiList.setSelectedIndex(selectedIndex);
        }
        updateWKTString();
    }

    /**
     * Sets the geometry type.
     *
     * @param geometryType the new geometry type
     */
    protected void setGeometryType(WKTType geometryType) {
        geometryTypeComboBox.setSelectedItem(geometryType);
    }
}
