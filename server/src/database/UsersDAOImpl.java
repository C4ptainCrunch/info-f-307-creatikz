package database;

import constants.Database;
import models.users.User;
import utils.Hasher;
import utils.TokenCreator;

import java.sql.*;
import java.util.ArrayList;

import static database.DAOUtilities.*;

/**
 * Implementation of a DAO to get and set information to the database's Users table.
 */
public class UsersDAOImpl implements  UsersDAO {

    private DAOFactory daoFactory;
    private TokenCreator tokenCreator;

    /**
     * Constructs a DAO for the database's Users table with a given DAOFactory.
     * @param daoFactory the main DAOFactory
     */
    UsersDAOImpl(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
        this.tokenCreator = new TokenCreator();
    }


    /**
     * Creates a given User into the database's Users table.
     * @param user The user to create
     * @return A boolean, false if no error happened during the User's creation in the database.
     */
    @Override
    public boolean create(User user){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet autoGeneratedValues = null;
        boolean error = false;
        String token = this.tokenCreator.newToken();
        try {
            connection = daoFactory.getConnection();
            preparedStatement = initializationPreparedRequest( connection, Database.SQL_INSERT_USER, true,
                                                               user.getFirstName(), user.getLastName(),
                                                               user.getUsername(), user.getEmail(), token
                                                             );
            int statut = preparedStatement.executeUpdate();
            if ( statut == 0 ) {
                System.err.println("Failed to create a user, no new line added to the table.");
            }
            autoGeneratedValues = preparedStatement.getGeneratedKeys();
            if ( autoGeneratedValues.next() ) {
                user.setId( autoGeneratedValues.getInt( 1 ) );
            }
            else {
               System.err.println("Failed to create a user, no auto-generated ID returned." );
            }
        }
        catch (SQLIntegrityConstraintViolationException e){
            System.out.print("Failed to create a user, username or email already exists\n");
            error = true;
        }
        catch ( SQLException e ) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        finally {
            silentClosures( autoGeneratedValues, preparedStatement, connection );
        }
        return error;
    }

    /**
     * Edits a given User in the database's Users table.
     * @param data The user's new data
     * @return A boolean, false if no error happened during the User's edition in the database.
     */
    @Override
    public boolean edit(ArrayList<String> data, String originalUserName) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet autoGeneratedValues = null;
        boolean error = false;
        try {
            connection = daoFactory.getConnection();
            preparedStatement = initializationPreparedRequest( connection, Database.SQL_EDIT_USER, true,
                    data.get(0), data.get(1), data.get(2),
                    data.get(3), data.get(4),originalUserName
            );
            int statut = preparedStatement.executeUpdate();
            if ( statut == 0 ) {
                System.err.println("Failed to edit a user.");
            }
        }
        catch ( SQLException e ) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        finally {
            silentClosures( autoGeneratedValues, preparedStatement, connection );
        }
        return error;
    }

    /**
     * Returns a new object User with information from the database regarding the given username.
     * @param username the username to get the information in the database from.
     * @return The User object created (if username exists, null otherwise)
     */
    @Override
    public User findByUsername(String username) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        User user = null;
        try {
            resultSet = executeQuery( connection, preparedStatement, resultSet, Database.SQL_SELECT_BY_USERNAME, username);
            if ( resultSet.next() ) {
                user = map( resultSet );
            }
        } catch ( SQLException e ) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
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
    @Override
    public User findByUsernameAndPassword(String username, String password) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        User user = null;
        String hash = Hasher.hash(password, username);
        try {
            resultSet = executeQuery( connection, preparedStatement, resultSet,  Database.SQL_MATCH_USERNAME_PASSWORD, username, hash);
            if (resultSet.next()) {
                user = findByUsername(username);
            }
        }catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
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
    @Override
    public void setPasswordToUser(User user, String password) {
        String hash = Hasher.hash(password, user.getUsername());
        int statut = executeUpdate(Database.SQL_SET_PASSWORD_TO_USER, false, hash, user.getUsername());
        if ( statut == 0 ) {
            System.err.println( "Failed to update user's password" );
        }
    }

    /**
     * Returns true if the given user's account has been activated by mail.
     * @param user The User to know if its account his activated
     * @return the activation status of the User
     */
    @Override
    public boolean isActivated(User user) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Boolean activated = false;
        try {
            resultSet = executeQuery( connection, preparedStatement, resultSet, Database.SQL_IS_ACTIVATED, user.getUsername());
            if (resultSet.next() ) {
                activated = (resultSet.getInt("activated") == 1) ? true : false;
            }
        } catch (SQLException e ) {
            System.err.println( "Failed to get user's activated status" );
        } finally {
            silentClosures( resultSet, preparedStatement, connection );
        }
        return activated;
    }

    /**
     * Activate a given User account (with his username).
     * @param username the username of the account to activate
     */
    @Override
    public void activateUser( String username ) {
        int statut = executeUpdate(Database.SQL_ACTIVATE_USER, false, username);
        if ( statut == 0 ) {
            System.err.println("Failed to activate user");
        }
    }

    /**
     * Returns the token stored in the database for the given username's account
     * @param username the username of the account to activate
     * @return The token of the given user
     */
    @Override
    public String getTokenOfUser( String username ) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String token = null;
        try {
            resultSet = executeQuery(connection, preparedStatement, resultSet, Database.SQL_GET_TOKEN_BY_USERNAME, username);
            if (resultSet.next() ) {
                token = (resultSet.getString("token"));
            }
        } catch (SQLException e ) {
            System.err.println( "Failed to retrieve user's token" );
        } finally {
            silentClosures( resultSet, preparedStatement, connection );
        }
        return token;
    }

    /**
     * Deletes a given User in the database.
     * @param user The User to delete
     */
    @Override
    public void deleteUser(User user) {
        int statut = executeUpdate(Database.SQL_DELETE_USER, false, user.getUsername());
        if ( statut == 0 ) {
            System.err.println( "Failed to delete user" );
        }
    }

    private ResultSet executeQuery(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet, String sqlQuery, Object... objects) {
        try {
            connection = daoFactory.getConnection();
            preparedStatement = initializationPreparedRequest(connection, sqlQuery, false, objects);
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            System.err.println(e.getErrorCode());
        }
        return resultSet;
    }

    private int executeUpdate(String sqlQuery, Boolean returnGeneratedKeys, Object... objects) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        int status = 0;
        try {
           connection = daoFactory.getConnection();
           preparedStatement = initializationPreparedRequest(connection, sqlQuery, returnGeneratedKeys, objects);
           status = preparedStatement.executeUpdate();
        } catch (SQLException e) {
           System.err.println(e.getErrorCode());
        } finally {
            silentClosures(preparedStatement, connection);
        }
        return status;
    }

    private static User map(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String first_name = resultSet.getString("first_name");
        String last_name = resultSet.getString("last_name");
        String username = resultSet.getString("username");
        String email = resultSet.getString("email");
        return new User(id, username, first_name, last_name, email);
    }
}
