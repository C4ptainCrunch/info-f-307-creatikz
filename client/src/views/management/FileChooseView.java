package views.management;

import java.io.File;

import javax.swing.*;

public class FileChooseView extends JPanel {
    JFileChooser chooser;

    public FileChooseView(String title, int selectionMode) {
        chooser = new JFileChooser();
        chooser.setCurrentDirectory(new java.io.File("."));
        chooser.setDialogTitle(title);
        chooser.setFileSelectionMode(selectionMode);
    }

    public File ask() {
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            return chooser.getSelectedFile();
        } else {
            return null;
        }
    }
}
