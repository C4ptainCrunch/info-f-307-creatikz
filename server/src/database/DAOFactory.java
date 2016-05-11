package database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

import utils.Log;
import constants.Database;

/**
 * Implementation of a Data Access Objects (DAO) Factory, which will be used to let the server communicate with the database
 */
public class DAOFactory {

    private static final Logger logger = Log.getLogger(DAOFactory.class);
    private String sqlite_db_connection;

    /**
     * Construct a new DAOFactory with the database's path.
     * @param sqlite_db_connection the database path
     */

    public DAOFactory(String sqlite_db_connection) {
        this.sqlite_db_connection = sqlite_db_connection;
    }

    private static boolean databaseExists() {
        File db_file = new File(Database.DB_FILE);
        if (db_file.exists()) {
            return true;
        }
        createDatabaseDir(db_file);
        return false;
    }

    private static void createDatabaseDir(File db_file) {
        db_file.getParentFile().mkdirs();
    }

    private static void createDatabase() {
        Connection connection;
        Statement statement;
        try {
            Class.forName(Database.SQLITE_JDBC);
            connection = DriverManager.getConnection(Database.SQLITE_DB_CONNECTION);
            statement = connection.createStatement();
            String sqlActivatePragmas = Database.SQLITE_DB_ACTIVATE_PRAGMAS;
            String sqlCreateUsers = Database.SQLITE_CREATE_TABLE_USERS;
            String sqlCreateProjects = Database.SQLITE_CREATE_TABLE_PROJECTS;
            String sqlCreatePermissions = Database.SQLITE_CREATE_TABLE_PERMISSIONS;
            statement.executeUpdate(sqlActivatePragmas);
            statement.executeUpdate(sqlCreateUsers);
            statement.executeUpdate(sqlCreateProjects);
            statement.executeUpdate(sqlCreatePermissions);
            statement.close();
            connection.close();
        } catch (Exception e) {
            logger.severe(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    /**
     * Create and return an instance of the DAOFactory (and create the SQLite database if doesn't exist)
     * @return An instance of the DAOFactory
     */
    public static DAOFactory getInstance() {
        if (!databaseExists()) {
            createDatabase();
        }
        DAOFactory instance = new DAOFactory(Database.SQLITE_DB_CONNECTION);
        return instance;
    }

    /**
     * Create and returns a connection to the database
     * @return A connection to the database
     * @throws SQLException
     */
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(this.sqlite_db_connection);
    }

    /**
     * Return a DataAccessObject for the database's Users table
     * @return A DAO for the database's Users table
     */
    public UsersDAO getUsersDAO() {
        return new UsersDAO(this);
    }
}
