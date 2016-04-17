package gui.projectManagement.views;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;

import javax.swing.*;

import constants.GUI;
import gui.projectManagement.controllers.ProjectManagementController;
import models.Project;
import models.RecentProjects;

public class ProjectManagementView extends JFrame {
    private ProjectManagementController controller = new ProjectManagementController(this);
    private JComboBox<Project> projectChooser;
    private JTextPane infoPanel;

    public ProjectManagementView() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.render();
    }

    public final void render() {
        this.setTitle("TikzCreator : choose a project");

        this.setSize(new Dimension(1000, 200));
        this.setLocationRelativeTo(null);

        Container pane = getContentPane();
        pane.setLayout(new GridLayout());

        JPanel left = new JPanel(new GridLayout());
        JPanel right = new JPanel(new GridLayout());

        pane.add(left);
        pane.add(right);

        createButtonsPanel(right);
        createChooserPanel(left);
        createInfoPanel(pane);

        this.setVisible(true);
    }

    private void createButtonsPanel(Container pane) {
        JPanel buttons = new JPanel();
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));

        JButton create = new JButton(GUI.ProjectManagement.CREATE_BUTTON);
        create.addActionListener(e -> controller.createProject());

        JButton open = new JButton(GUI.ProjectManagement.OPEN_BUTTON);
        open.addActionListener(e -> controller.openProject());

        JButton rename = new JButton(GUI.ProjectManagement.RENAME_BUTTON);
        rename.addActionListener(e -> controller.renameProject());

        buttons.add(create);
        buttons.add(open);
        buttons.add(rename);

        pane.add(buttons);
    }

    private void createChooserPanel(Container pane) {
        Vector<Project> recentProjects = null;
        try {
            recentProjects = new Vector<>(RecentProjects.getRecentProjects());
            Collections.reverse(recentProjects);
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.projectChooser = new JComboBox<>();
        this.projectChooser.setModel(new DefaultComboBoxModel(recentProjects));

        this.projectChooser.addActionListener(e -> controller.dropdownSelected(e));

        pane.add(new JLabel(GUI.ProjectManagement.DROPDOWN_HEADER));
        pane.add(projectChooser);

    }

    private void createInfoPanel(Container pane) {
        JPanel infoPanel = new JPanel();
        this.infoPanel = new JTextPane();
        this.setInfoText("                                                        ");

        infoPanel.add(this.infoPanel);
        pane.add(this.infoPanel, BorderLayout.EAST);
    }

    public Project getSelectedProject() {
        return (Project) this.projectChooser.getSelectedItem();
    }

    public void setInfoText(String infoText) {
        this.infoPanel.setText(infoText);
    }
}
