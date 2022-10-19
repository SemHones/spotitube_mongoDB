package datasource.dao;

import datasource.util.DatabaseProperties;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.*;

public class DefaultDAO {

    protected static final Logger LOGGER = Logger.getLogger(DefaultDAO.class.getName());
    protected DatabaseProperties databaseProperties;

    protected Connection connection = null;
    protected PreparedStatement pstmt = null;
    protected ResultSet rs = null;

    public DefaultDAO() {
        this.databaseProperties = new DatabaseProperties();
    }

    protected void closeConnections(){
        try {
            if(connection != null){connection.close();}
            if(pstmt != null){pstmt.close();}
            if(rs != null){rs.close();}
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
        }
    }

    protected void logError(SQLException e){
        LOGGER.log(Level.SEVERE, "Error communicating with database " + databaseProperties.connectionString(), e);
    }
}
