package database;

import static database.DAOUtilities.initializationPreparedRequest;
import static database.DAOUtilities.mapUser;
import static database.DAOUtilities.silentClosures;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import models.databaseModels.User;
import utils.Hasher;
import utils.Log;
import utils.TokenCreator;

class UserRequests{
    public static final String SQL_SELECT_BY_USERNAME = "SELECT id, first_name, last_name, username, email " +
            "FROM Users WHERE username = ?";
    public static final String SQL_MATCH_USERNAME_PASSWORD = "SELECT username, password " +
            "FROM Users WHERE username = ? and password = ?";
    public static final String SQL_INSERT_USER = "INSERT INTO Users(first_name, last_name, username, email, token, activated) " +
            "VALUES (?, ?, ?, ?, ?, 0)";

    public static final String SQL_SET_PASSWORD_TO_USER = "UPDATE Users SET password = ? WHERE username = ?";
    public static final String SQL_GET_TOKEN_BY_USERNAME = "SELECT token FROM Users WHERE username = ?";
    public static final String SQL_IS_ACTIVATED = "SELECT activated FROM Users WHERE username = ?";
    public static final String SQL_ACTIVATE_USER = "UPDATE Users SET activated = 1 WHERE username = ?";
    public static final String SQL_DELETE_USER = "DELETE FROM Users WHERE username = ?";

    public static final String SQL_UPDATE_USER = "UPDATE Users " +
            "SET first_name = ?,last_name = ?,email= ?" +
            "WHERE username= ?";
    public static final String SQL_SET_TOKEN_BY_USERNAME = "UPDATE Users SET activated = 0, token = ? WHERE username=?";

    public static final String SQL_SELECT_BY_ID = "SELECT id, first_name, last_name, username, email " +
            "FROM Users WHERE id = ?";
    public static final String SQL_GET_ALL = "SELECT id, first_name, last_name, username, email FROM Users";
}

/**
 * Implementation of a DAO to get and set information to the database's Users table.
 */
public class UsersDAO {
    private static final Logger logger = Log.getLogger(UsersDAO.class);
    private DAOFactorySingleton daoFactorySingleton;

    /**
     * Constructs a DAO for the database's Users table with a given DAOFactorySingleton.
     * @param daoFactorySingleton the main DAOFactorySingleton
     */
    UsersDAO(DAOFactorySingleton daoFactorySingleton) {
        this.daoFactorySingleton = daoFactorySingleton;
    }


    /**
     * Creates a given User into the database's Users table.
     * @param user The user to create
     * @return A boolean, false if no error happened during the User's creation in the database.
     */
    public void create(User user) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet autoGeneratedValues = null;
        String token = TokenCreator.newToken();
        try {
            connection = daoFactorySingleton.getConnection();
            preparedStatement = initializationPreparedRequest(
                    connection,
                    UserRequests.SQL_INSERT_USER,
                    true,
                    user.getFirstName(),
                    user.getLastName(),
                    user.getUsername(),
                    user.getEmail(),
                    token
            );

            preparedStatement.executeUpdate();
            autoGeneratedValues = preparedStatement.getGeneratedKeys();
            autoGeneratedValues.next();
            user.setId(autoGeneratedValues.getInt(1));

        }
        finally {
            silentClosures( autoGeneratedValues, preparedStatement, connection );
        }
    }

    /**
     * Returns a new object User with information from the database regarding the given username.
     * @param username the username to get the information in the database from.
     * @return The User object created (if username exists, null otherwise)
     */
    public User findByUsername(String username) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        User user = null;
        try {
            resultSet = DAOUtilities.executeQuery(daoFactorySingleton, connection, preparedStatement, resultSet, UserRequests.SQL_SELECT_BY_USERNAME, username);
            if ( resultSet.next() ) {
                user = DAOUtilities.mapUser( resultSet );
            }
        } finally {
            silentClosures( resultSet, preparedStatement, connection );
        }
        return user;
    }

    /**
     * Returns a new object User with information from the database regarding the given username and password (if they match, else returns null)
     * @param username the username to get the information in the database from.
     * @param password the password to get the information in the database from.
     * @return the User object created (null if password-username don't match)
     */
    public User findByUsernameAndPassword(String username, String password) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        User user = null;
        String hash = Hasher.hash(password, username);
        try {
            resultSet = DAOUtilities.executeQuery(daoFactorySingleton, connection, preparedStatement, resultSet,  UserRequests.SQL_MATCH_USERNAME_PASSWORD, username, hash);
            if (resultSet.next()) {
                user = findByUsername(username);
            }
        }finally {
            silentClosures( resultSet, preparedStatement, connection );
        }
        return user;
    }

    /**
     * Change the password of the given user in the database.
     * @param user The User of which the password must be changed
     * @param password the Password to set
     */
    public void setPasswordToUser(User user, String password) {
        String hash = Hasher.hash(password, user.getUsername());
        int statut = DAOUtilities.executeUpdate(daoFactorySingleton, UserRequests.SQL_SET_PASSWORD_TO_USER, hash, user.getUsername());
        if ( statut == 0 ) {
            logger.severe( "Failed to update user's password" );
        }
    }

    /**
     * Returns true if the given user's account has been activated by mail.
     * @param user The User to know if its account his activated
     * @return the activation status of the User
     */
    public boolean isActivated(User user) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Boolean activated = false;
        try {
            resultSet = DAOUtilities.executeQuery(daoFactorySingleton, connection, preparedStatement, resultSet, UserRequests.SQL_IS_ACTIVATED, user.getUsername());
            if (resultSet.next() ) {
                activated = (resultSet.getInt("activated") == 1);
            }
        } finally {
            silentClosures( resultSet, preparedStatement, connection );
        }
        return activated;
    }

    /**
     * Activate a given User account (with his username).
     * @param username the username of the account to activate
     */
    public void activateUser(String username) {
        DAOUtilities.executeUpdate(daoFactorySingleton, UserRequests.SQL_ACTIVATE_USER, username);
    }

    /**
     * Returns the token stored in the database for the given username's account
     * @param username the username of the account to activate
     * @return The token of the given user
     */
    public String getTokenOfUser( String username ) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String token = null;
        try {
            resultSet = DAOUtilities.executeQuery(daoFactorySingleton, connection, preparedStatement, resultSet, UserRequests.SQL_GET_TOKEN_BY_USERNAME, username);
            if (resultSet.next() ) {
                token = (resultSet.getString("token"));
            }
        } finally {
            silentClosures( resultSet, preparedStatement, connection );
        }
        return token;
    }

    /**
     * Deletes a given User in the database.
     * @param user The User to delete
     */
    public void deleteUser(User user) throws Exception {
        int statut = DAOUtilities.executeUpdate(daoFactorySingleton, UserRequests.SQL_DELETE_USER, user.getUsername());
        if ( statut == 0 ) {
            throw new Exception("Created failed");
        }
    }

    public void update(User user) {
        DAOUtilities.executeUpdate(daoFactorySingleton, UserRequests.SQL_UPDATE_USER, user.getFirstName(), user.getLastName(), user.getEmail(), user.getUsername());
    }

    public void setToken(User user, String token) {
        DAOUtilities.executeUpdate(daoFactorySingleton, UserRequests.SQL_SET_TOKEN_BY_USERNAME, token , user.getUsername());
    }

    public User findById(int userID) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        User user = null;
        try {
            resultSet = DAOUtilities.executeQuery(daoFactorySingleton, connection, preparedStatement, resultSet, UserRequests.SQL_SELECT_BY_ID, userID);
            if ( resultSet.next() ) {
                user = DAOUtilities.mapUser( resultSet );
            }
        } finally {
            silentClosures( resultSet, preparedStatement, connection );
        }
        return user;
    }

    public List<User> getAll() throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<User> users = new ArrayList<>();
        try {
            resultSet = DAOUtilities.executeQuery(daoFactorySingleton, connection, preparedStatement, resultSet, UserRequests.SQL_GET_ALL);
            while (resultSet.next()){
                users.add(mapUser(resultSet));
            }
        } finally {
            silentClosures(resultSet, preparedStatement, connection);
        }
        return users;

    }
}
