package views.management;

import controllers.management.DiagramManagementController;
import models.project.Project;

import javax.swing.*;
import java.awt.*;
import java.util.Set;

public class DiagramManagementView extends JFrame {

    private DiagramManagementController controller = new DiagramManagementController(this);
    private Object[] diagramNames;
    private JList<String> diagramList;
    private JTextField newDiagramName;

    public DiagramManagementView(Project currentProject, Set<String> diagramNames){
        this.diagramNames = diagramNames.toArray();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.render();
    }

    public final void render(){
        this.setTitle("TikzCreator : choose a diagram or create a new one");
        this.setPreferredSize(new Dimension(900, 200));
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

        JScrollPane listScroller = new JScrollPane(diagramList);
        listScroller.setPreferredSize(new Dimension(250, 80));

        diagramPanel.add(listScroller);
        this.add(diagramPanel, BorderLayout.NORTH);

    }

    private void createNewDiagramDialog() {
        JPanel newDiagramPanel = new JPanel();

        this.newDiagramName = new JTextField();
        this.newDiagramName.setPreferredSize(new Dimension(189,25));
        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> controller.openDiagram(this.diagramList.getSelectedValue(),
                                                               this.newDiagramName.getText()));

        newDiagramPanel.add(this.newDiagramName);
        newDiagramPanel.add(okButton);
        this.add(newDiagramPanel, BorderLayout.CENTER);
    }
}