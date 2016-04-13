package gui.projectManagement.controllers;


import gui.editor.views.EditorView;
import gui.projectManagement.views.ProjectManagementView;
import constants.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ProjectManagementController {
    private ProjectManagementView view;

    public ProjectManagementController(ProjectManagementView view){
        this.view = view;
    }

    private List<String> getArrayListSavedPaths() {
        String path = getSavedPath();
        List<String> lines = new ArrayList<>();

        lines.add("Choose existing project from this list (if it exists) and press 'Import'");

        try {
            FileReader fileReader = new FileReader(path);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                lines.add(line);
            }
            bufferedReader.close();
        }
        catch (IOException e){
        }
        return lines;
    }

    public String[] getStringListSavedPaths(){
        List<String> lines = getArrayListSavedPaths();
        return lines.toArray(new String[lines.size()]);
    }

    private String getSavedPath(){
        String homeDir = System.getProperty("user.home");
        String fileSeparator = System.getProperty("file.separator");
        // Default for now, will receive current username when accounts exist.
        String filename = fileSeparator+ "default.paths";
        switch (System.getProperty("os.name")){
            case "Linux":
                return homeDir + Utils.LINUX_PATH + filename;
            case "Mac OS X":
                return homeDir + Utils.MAC_PATH + filename;
            default:  // Unfortunately, default is Windows OS...
                String windowsPath = Utils.WINDOWS_PATH_ONE + System.getProperty("user.name") + Utils.WINDOWS_PATH_TWO;
                return homeDir + windowsPath + filename;
        }
    }

    private java.io.File getSavedPathsFileFromPath(String path){
        java.io.File savedPathsFile = new java.io.File(path);

        if(! savedPathsFile.exists()){
            try {
                savedPathsFile.getParentFile().mkdirs();
                savedPathsFile.createNewFile();
            } catch (IOException e) {  // Not logical but necessary
                System.err.println("Exists: " + e.getMessage());
            }
        }

        return savedPathsFile;
    }

    private void updateSavedProjectsFile(String oldName, String newName) throws IOException{
        BufferedReader file = new BufferedReader(new FileReader(getSavedPath()));
        String line;
        String input = "";

        while ((line = file.readLine()) != null)
            input += line + System.lineSeparator();

        input = input.replace(oldName, newName);

        FileOutputStream os = new FileOutputStream(getSavedPath());
        os.write(input.getBytes());

        file.close();
        os.close();
    }

    private boolean isInList(List<String> list, String path){
        boolean res = false;
        for(String s : list){
            if (s.equals(path)){
                res = true;
            }
        }
        return res;
    }

    private void addProjectPathToSavedPathFile(String projectPath) {
        List<String> paths = getArrayListSavedPaths();
        if(!isInList(paths, projectPath)) {
            String path = getSavedPath();
            java.io.File savedPathsFile = getSavedPathsFileFromPath(path);

            try (FileWriter fw = new FileWriter(savedPathsFile, true);
                 BufferedWriter bw = new BufferedWriter(fw)
            ) {
                bw.append(projectPath + System.lineSeparator());
            } catch (IOException e) {

            }
        }
    }

    public void createProject() {

        java.io.File f = createPanel("Choose location to create your project", JFileChooser.DIRECTORIES_ONLY);
        if (f != null){
            String filePath = f.getAbsolutePath();

            if(!f.exists()){
                try{
                    f.mkdir();
                    addProjectPathToSavedPathFile(filePath);
                    java.awt.EventQueue.invokeLater(()->new EditorView(filePath, false));
                    view.dispose(); //Exit previous windows
                }catch(SecurityException e){
                    e.getStackTrace();
                    //TODO: Alerte erreur
                }
            }
            else{
                //TODO:Alerte file already exists
            }
        }
    }

    public void importProject() {
        if(this.view.getSelectedPathIndex()==0){
            java.io.File f = createPanel("Choose location to import your project", JFileChooser.FILES_ONLY);
            if (f != null){
                String filePath = f.getAbsolutePath();
                int endIndex = filePath.lastIndexOf("/");
                addProjectPathToSavedPathFile(filePath.substring(0,endIndex));
                java.awt.EventQueue.invokeLater(()->new EditorView(filePath, true));
                view.dispose(); //Exit previous windows
            }
        }
        else{
            String filePath = this.view.getSelectedPath() + System.getProperty("file.separator") + "tikz.save";
            java.awt.EventQueue.invokeLater(()->new EditorView(filePath, true));
            view.dispose(); //Exit previous windows
        }

    }

    private java.io.File createPanel(String title, int selectionMode) {
        System.out.println("create panel");
        JPanel panel = new JPanel();
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new java.io.File("."));
        fileChooser.setDialogTitle(title);
        fileChooser.setFileSelectionMode(selectionMode);

        if (fileChooser.showOpenDialog(panel) == JFileChooser.APPROVE_OPTION) {
            System.out.println("getCurrentDirectory(): " +  fileChooser.getCurrentDirectory());
            System.out.println("getSelectedFile() : " +  fileChooser.getSelectedFile());
            return fileChooser.getSelectedFile();
        }
        else {
            System.out.println("No Selection ");
            return null;
        }
    }

    public void renameProject() {
        int selectedPathIndex = this.view.getSelectedPathIndex();
        if (selectedPathIndex != 0) {  //0 is the "Choose existing project... in JComboBox
            String selectedPath = this.view.getSelectedPath();
            int endIndex = selectedPath.lastIndexOf("/");
            java.io.File dir = new java.io.File(selectedPath);
            String newProjectName = JOptionPane.showInputDialog("New project name");
            String newName = selectedPath.substring(0, endIndex) + System.getProperty("file.separator") + newProjectName;
            java.io.File newDir = new java.io.File(newName);
            if (dir.isDirectory() && newProjectName != null) {
                dir.renameTo(newDir);
                try {
                    this.updateSavedProjectsFile(selectedPath, newName);
                } catch (IOException e) {

                }
                this.view.updateComboBox(newName);
            }
        }
        else {
            JOptionPane.showMessageDialog(this.view,"Please select a project to rename first", "Rename Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    public String getLastRevision(){
        String ch = "";

        try{
            String pathToRevisionFile = this.view.getSelectedPath();
            ArrayList tmp = new ArrayList<String>();
            FileReader fr = new FileReader(pathToRevisionFile+System.getProperty("file.separator")+"diffs");
            BufferedReader br = new BufferedReader(fr);

            do {
                try{
                    ch = br.readLine();
                }
                catch (IOException e){

                }
                tmp.add(ch);
            } while (! ch.startsWith("2016"));
        }
        catch (FileNotFoundException e){
            System.out.println("File not found.");
        }
        return ch;
    }


}
