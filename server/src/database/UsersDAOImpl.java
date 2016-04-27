package database;

import constants.Database;
import models.users.User;
import utils.TokenCreator;

import java.sql.*;

import static database.DAOUtilities.*;

/**
 * Created by mrmmtb on 25.04.16.
 */
public class UsersDAOImpl implements  UsersDAO {

    private DAOFactory daoFactory;
    private TokenCreator tokenCreator;

    UsersDAOImpl(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
        this.tokenCreator = new TokenCreator();
    }


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
            silentClosing( resultSet );
        }
        return user;
    }

    @Override
    public User findByUsernameAndPassword(String username, String password) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        User user = null;
        try {
            resultSet = executeQuery( connection, preparedStatement, resultSet,  Database.SQL_MATCH_USERNAME_PASSWORD, username, password);
            if (resultSet.next()) {
                user = findByUsername(username);
            }
        }catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }finally {
            silentClosing( resultSet );
        }
        return user;
    }

    @Override
    public void setPasswordToUser(User user, String password) {
        int statut = executeUpdate(Database.SQL_SET_PASSWORD_TO_USER, false, password, user.getUsername());
        if ( statut == 0 ) {
            System.err.println( "Failed to update user's password" );
        }
    }

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
            silentClosing( resultSet );
        }
        return activated;
    }

    @Override
    public void activateUser( User user ) {
        int statut = executeUpdate(Database.SQL_ACTIVATE_USER, false, user.getUsername());
        if ( statut == 0 ) {
            System.err.println("Failed to activate user");
        }
    }

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
            silentClosing( resultSet );
        }
        return token;
    }

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
