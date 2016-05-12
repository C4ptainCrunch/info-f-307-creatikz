package views.management;

import constants.GUI;
import controllers.management.CloudManagementController;
import misc.logic.CloudLogic;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class CloudManagementView extends JPanel {

    private ManagementView parentView;
    private CloudManagementController controller = new CloudManagementController();
    private JList<models.databaseModels.Project> projectChooser;
    private JTextPane infoPanel;
    private JPanel chooserPanel;


    public CloudManagementView(ManagementView parentView) {
        this.parentView = parentView;
        this.render();
    }

    public void render() {
        this.setPreferredSize(new Dimension(600, 300));
        this.setSize(900, 200);

        createInfoPanel();
        createChooserPanel();
        this.add(this.chooserPanel);
        this.add(this.infoPanel);
    }

    public void dispose() {
        this.parentView.dispose();
    }

    private void createChooserPanel() {
        this.chooserPanel = new JPanel();
        this.chooserPanel.setLayout(new BorderLayout());
        List<models.databaseModels.Project> projectsList = CloudLogic.getSharedProjects();
        Vector<models.databaseModels.Project> sharedProjects = new Vector<>();
        if(projectsList != null){
            sharedProjects = new Vector<>(projectsList);
        }

        this.projectChooser = new JList<>(sharedProjects);
        this.projectChooser.addListSelectionListener(e -> controller.dropdownSelected(projectChooser.getSelectedValue()));
        this.projectChooser.setSelectedIndex(0);

        this.chooserPanel.add(new JLabel(GUI.ProjectManagement.DROPDOWN_HEADER), BorderLayout.NORTH);

        JScrollPane listScroller = new JScrollPane(this.projectChooser);
        this.chooserPanel.add(listScroller, BorderLayout.CENTER);

    }

    private void createInfoPanel() {
        this.infoPanel = new JTextPane();
        this.infoPanel.setOpaque(false);
        this.infoPanel.setEnabled(false);
        this.infoPanel.setPreferredSize(new Dimension(100,100));
    }

    public void refresh() {
        // TODO
    }
}
