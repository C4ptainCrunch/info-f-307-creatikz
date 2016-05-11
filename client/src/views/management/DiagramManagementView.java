package views.management;

import java.awt.*;
import java.io.IOException;
import java.util.Vector;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import models.project.Project;
import controllers.management.DiagramManagementController;

public class DiagramManagementView extends JDialog {

    private DiagramManagementController controller;
    private Vector<String> diagramNames;
    private JList<String> diagramList;
    private JTextField newDiagramName;
    private JButton okButton;

    public DiagramManagementView(Project currentProject) throws IOException {
        this.controller = new DiagramManagementController(this, currentProject);
        this.diagramNames = new Vector<>(currentProject.getDiagramNames());
        this.diagramNames.add("Create new diagram");
        this.render();
    }

    public final void render(){
        this.setTitle("TikzCreator : choose a diagram or create a new one");
        this.setPreferredSize(new Dimension(400, 300));
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());

        createDiagramsList();
        createNewDiagramDialog();

        this.pack();
        this.setVisible(true);

    }

    private void createDiagramsList() {
        JPanel diagramPanel = new JPanel();

        diagramList = new JList(this.diagramNames);
        diagramList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        diagramList.setLayoutOrientation(JList.VERTICAL);
        diagramList.setVisibleRowCount(-1);

        diagramList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent listSelectionEvent) {
                if(diagramList.getSelectedValue().equals("Create new diagram")) {
                    newDiagramName.setEditable(true);
                    okButton.setText("Create");

                } else {
                    newDiagramName.setText("");
                    okButton.setText("Open");
                }
            }
        });

        JScrollPane listScroller = new JScrollPane(diagramList);
        listScroller.setPreferredSize(new Dimension(250, 80));

        diagramPanel.add(listScroller);
        this.add(diagramPanel, BorderLayout.NORTH);

    }

    private void createNewDiagramDialog() {
        JPanel newDiagramPanel = new JPanel();

        this.newDiagramName = new JTextField();
        this.newDiagramName.setEditable(false);
        this.newDiagramName.setPreferredSize(new Dimension(189,25));
        this.okButton = new JButton("Open");
        okButton.addActionListener(e -> controller.openDiagram(this.diagramList.getSelectedValue(),
                                                               this.newDiagramName.getText()));

        newDiagramPanel.add(this.newDiagramName);
        newDiagramPanel.add(okButton);
        this.add(newDiagramPanel, BorderLayout.CENTER);
    }

    public void showAlert(String alertText) {
        JOptionPane.showMessageDialog(this,
                alertText,
                "Warning",
                JOptionPane.WARNING_MESSAGE);
    }
}
