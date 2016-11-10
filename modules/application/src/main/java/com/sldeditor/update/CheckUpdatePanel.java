/*
 *    SLDEditor - SLD Editor application
 *
 *    (C) 2016, SCISYS
 *
 */

package com.sldeditor.update;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.sldeditor.common.Controller;
import com.sldeditor.common.localisation.Localisation;
import java.awt.FlowLayout;

/**
 * The class CheckUpdatePanel.
 *
 * @author Robert Ward (SCISYS)
 */
public class CheckUpdatePanel extends JDialog {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    private JLabel lblCurrentVersion;
    private JLabel lblLatestVersion;
    private JTextArea textArea;
    private JLabel lblStatus;
    private JButton btnGet;

    /**
     * Instantiates a new check update panel.
     */
    public CheckUpdatePanel() {
        super(Controller.getInstance().getFrame(), Localisation.getString(CheckUpdatePanel.class, "CheckUpdatePanel.title"), true);
        createUI();
        setSize(400,300);
    }

    /**
     * Creates the UI.
     */
    private void createUI() {
        JPanel buttonPanel = new JPanel();
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        JButton btnOk = new JButton(Localisation.getString(CheckUpdatePanel.class, "common.ok"));
        btnOk.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
        buttonPanel.add(btnOk);

        JPanel panel = new JPanel();
        getContentPane().add(panel, BorderLayout.CENTER);
        panel.setLayout(new BorderLayout(0, 0));

        textArea = new JTextArea();
        JScrollPane jp = new JScrollPane(textArea);
        panel.add(jp, BorderLayout.CENTER);

        JPanel panel_1 = new JPanel();
        getContentPane().add(panel_1, BorderLayout.NORTH);
        panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.Y_AXIS));

        JPanel panel_3 = new JPanel();
        FlowLayout flowLayout_1 = (FlowLayout) panel_3.getLayout();
        flowLayout_1.setAlignment(FlowLayout.LEFT);
        panel_1.add(panel_3);

        lblCurrentVersion = new JLabel("current");
        panel_3.add(lblCurrentVersion);

        JPanel panel_5 = new JPanel();
        panel_1.add(panel_5);
        FlowLayout flowLayout = (FlowLayout) panel_5.getLayout();
        flowLayout.setAlignment(FlowLayout.LEFT);

        lblLatestVersion = new JLabel("latest");
        panel_5.add(lblLatestVersion);

        JPanel panel_4 = new JPanel();
        panel_1.add(panel_4);
        panel_4.setLayout(new BorderLayout(0, 0));

        btnGet = new JButton(Localisation.getString(CheckUpdatePanel.class, "CheckUpdatePanel.get"));
        btnGet.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CheckUpdateClientInterface client = CheckUpdateClientFactory.getClient();
                CheckUpdate update = new CheckUpdate(client);

                update.showDownloadPage();
            }
        });
        panel_4.add(btnGet, BorderLayout.EAST);

        JPanel panel_2 = new JPanel();
        panel_4.add(panel_2, BorderLayout.WEST);
        FlowLayout flowLayout_2 = (FlowLayout) panel_2.getLayout();
        flowLayout_2.setAlignment(FlowLayout.LEFT);

        lblStatus = new JLabel("status");
        panel_2.add(lblStatus);
    }

    /**
     * Show panel.
     *
     * @param currentVersion the current version
     */
    public void showPanel(String currentVersion)
    {
        lblCurrentVersion.setText(String.format("%s %s", 
                Localisation.getField(CheckUpdatePanel.class, "CheckUpdatePanel.currentVersion"), 
                currentVersion));

        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run(){
                CheckUpdateClientInterface client = CheckUpdateClientFactory.getClient();
                CheckUpdate update = new CheckUpdate(client);

                boolean shouldUpdate = update.shouldUpdate(currentVersion);

                UpdateData latestData = update.getLatestData();
                String latestVersionString = String.format("%s %s",
                        Localisation.getField(CheckUpdatePanel.class, "CheckUpdatePanel.latestVersion"), 
                        latestData.getVersion());
                lblLatestVersion.setText(latestVersionString);
                btnGet.setVisible(shouldUpdate);
                if(shouldUpdate)
                {
                    textArea.setText(latestData.getDescription());
                    lblStatus.setText(Localisation.getString(CheckUpdatePanel.class, "CheckUpdatePanel.newVersionAvailable"));
                }
                else
                {
                    lblStatus.setText(Localisation.getString(CheckUpdatePanel.class, "CheckUpdatePanel.runningLatest"));
                }
            }
        });

        thread1.start();

        setVisible(true);
    }

    public static void main(String[] args) {

        CheckUpdatePanel panel = new CheckUpdatePanel();

        panel.showPanel("0.4.0");
    }

}
