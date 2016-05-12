package database;

import models.databaseModels.Project;
import utils.Log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Logger;

import static database.DAOUtilities.*;

class ProjectRequests {
    public static final String SQL_INSERT_PROJECT = "INSERT INTO Projects(uid ,user_id, path, last_modification, default_perm_write, default_perm_read, name) VALUES (?, ?, ?, ?, ?, ?, ?);";
    public static final String SQL_EDIT_PROJECT = "UPDATE Projects SET uid = ?, path = ?, last_modification = ?, default_perm_write = ?, default_perm_read = ?, name = ? WHERE path = ?";
    public static final String SQL_SELECT_PROJECT_BY_UID = "SELECT uid, user_id, path, last_modification, default_perm_write, default_perm_read, name FROM Projects WHERE uid = ?";
    public static final String SQL_PROJECT_IS_READABLE = "SELECT default_perm_read FROM Projects WHERE uid = ?";
    public static final String SQL_PROJECT_IS_WRITABLE = "SELECT default_perm_write FROM Projects WHERE uid = ?";
    public static final String SQL_PROJECT_DELETE = "DELETE FROM Projects WHERE uid = ?";
    public static final String SQL_PROJECT_GETALL = "SELECT uid, user_id, path, last_modification, default_perm_write, default_perm_read, name FROM Projects";
    public static final String SQL_PROJECT_GETALLREADABLES = "SELECT uid, user_id, path, last_modification, default_perm_write, default_perm_read, name FROM Projects WHERE default_perm_read = 1 OR user_id = ?";
}

public class ProjectsDAO {
    private DAOFactory daoFactory;
    private PermissionsDAO permissionsDAO = new PermissionsDAO(daoFactory);

    private static final Logger logger = Log.getLogger(UsersDAO.class);

    ProjectsDAO(DAOFactory daoFactory){
        this.daoFactory = daoFactory;
    }

    public void create(Project project) throws Exception {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet autoGeneratedValues = null;
        try {
            connection = daoFactory.getConnection();
            preparedStatement = initializationPreparedRequest(connection, ProjectRequests.SQL_INSERT_PROJECT, true, project.getUid(), project.getUserID(), project.getPath(), project.getLast_modification(), project.isWrite_default()? 1:0, project.isRead_default()? 1:0, project.getName());
            int statut = preparedStatement.executeUpdate();
            if (statut == 0) {
                throw  new Exception("Failed to create a project");
            }
        }
        finally {
            silentClosures(autoGeneratedValues, preparedStatement, connection);
        }
    }

    public Project findByUid(String uid) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Project project = null;
        try{
            resultSet = DAOUtilities.executeQuery(daoFactory, connection, preparedStatement, resultSet, ProjectRequests.SQL_SELECT_PROJECT_BY_UID, uid);
            if (resultSet.next()){
                project = DAOUtilities.mapProject(resultSet);
            }
        } finally {
            silentClosures(resultSet, preparedStatement, connection);
        }
        return project;
    }

    private boolean isAbleByDefault(String query, String uid) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        boolean res = false;
        try{
            resultSet = DAOUtilities.executeQuery(daoFactory, connection, preparedStatement, resultSet, query, uid);
            if (resultSet.next()){
                res = resultSet.getInt(0) == 1;
            }
        } finally {
            silentClosures(resultSet, preparedStatement, connection);
        }
        return res;
    }

    public boolean isReadableByDefault(String uid) throws SQLException {
        return isAbleByDefault(ProjectRequests.SQL_PROJECT_IS_READABLE, uid);
    }

    public boolean isWritableByDefault(String uid) throws SQLException {
        return isAbleByDefault(ProjectRequests.SQL_PROJECT_IS_WRITABLE, uid);
    }

    public void deleteProject(String uid) {
        int statut = DAOUtilities.executeUpdate(daoFactory, ProjectRequests.SQL_PROJECT_DELETE, uid);
        if (statut == 0) {
            logger.severe("Failed to delete project " + uid);
        }
    }

    public ArrayList<Project> getAllProjects() throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        ArrayList<Project> projects = new ArrayList<>();
        try {
            resultSet = DAOUtilities.executeQuery(daoFactory, connection, preparedStatement, resultSet, ProjectRequests.SQL_PROJECT_GETALL);
            while (resultSet.next()){
                projects.add(mapProject(resultSet));
            }
        } finally {
        silentClosures(resultSet, preparedStatement, connection);
        }
        return projects;
    }

    public ArrayList<Project> getAllReadableProject(int userID) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        ArrayList<Project> projects = new ArrayList<>();
        try {
            resultSet = DAOUtilities.executeQuery(daoFactory, connection, preparedStatement, resultSet, ProjectRequests.SQL_PROJECT_GETALLREADABLES, userID);
            while (resultSet.next()) {
                Project p = mapProject(resultSet);
                projects.add(p);
            }
        } finally {
            silentClosures(resultSet, preparedStatement, connection);
        }
        return projects;
    }

}
