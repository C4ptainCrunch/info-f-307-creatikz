package database;

import constants.Database;
import models.databaseModels.Permissions;
import utils.Log;

import java.sql.*;
import java.util.logging.Logger;

import static database.DAOUtilities.initializationPreparedRequest;
import static database.DAOUtilities.silentClosures;

public class PermissionsDAO {
    private static final Logger logger = Log.getLogger(UsersDAO.class);

    private DAOFactory daoFactory;

    PermissionsDAO(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    public boolean create(Permissions permissions) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet autoGeneratedValues = null;
        boolean error = false;
        try {
            connection = daoFactory.getConnection();
            preparedStatement = initializationPreparedRequest(connection, Database.SQL_INSERT_PERMISSIONS, true, permissions.getProjectID(), permissions.getUserID(), permissions.isReadable(), permissions.isWritable());
            int statut = preparedStatement.executeUpdate();
            if (statut == 0) {
                logger.severe("Failed to create a new permissions instance");
                error = true;
            }
        } catch (SQLException e) {
            logger.severe(String.format("%s: %s", e.getClass().getName(), e.getMessage()));
            error = true;
        }
        return error;
    }

    public Permissions findPermissions(int project_id, int user_id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Permissions permissions = null;
        try {
            resultSet = DAOUtilities.executeQuery(daoFactory, connection, preparedStatement, resultSet, Database.SQL_SELECT_PERMISSIONS, project_id, user_id);
            if (resultSet.next()){
                permissions = DAOUtilities.mapPermissions(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return permissions;
    }

    public boolean changePermissions(int user_id, int project_id, boolean read_perm, boolean write_perm) {
        int statut = DAOUtilities.executeUpdate(daoFactory, Database.SQL_CHANGE_PERMISSIONS, project_id, user_id);
        if (statut == 0) {
            logger.severe(String.format("Failed to set permissions for User %d in Project %d", user_id, project_id));
        }
        return statut != 0;
    }

    public boolean deletePermissions(int user_id, int project_id) {
        int statut = DAOUtilities.executeUpdate(daoFactory, Database.SQL_PERMISSINOS_DELETE, project_id, user_id);
        if (statut == 0) {
            logger.severe(String.format("Failed to delete permissions for User %d in Project %d", user_id, project_id));
        }
        return statut != 0;
    }
}
