package datasource.dao;

import datasource.objects.User;

import java.sql.DriverManager;
import java.sql.SQLException;

public class UserDAO extends DefaultDAO{
    private static final String SELECT_USER_WHERE_USERNAME_AND_PASSWORD =  "select * from spotitube.user where username = ? and password = ?";
    private static final String SELECT_USER_WHERE_TOKEN =  "select * from spotitube.user where token = ?";

    private User user;

    public UserDAO(){ super(); }

    public User getSingleUser(String username, String password){
        this.user = new User(0, "", "", "");
        try {
            connection = DriverManager.getConnection(databaseProperties.connectionString());
            pstmt = connection.prepareStatement(SELECT_USER_WHERE_USERNAME_AND_PASSWORD);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            rs = pstmt.executeQuery();
            while (rs.next()){
                return new User(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("token"),
                    rs.getString("password")
                );
            }

        } catch (SQLException e) {
            logError(e);
        } finally {
            closeConnections();
        }
        return this.user;
    }

    public User getSingleUserByToken(String token){
        this.user = new User(0, "", "", "");
        try {
            connection = DriverManager.getConnection(databaseProperties.connectionString());
            pstmt = connection.prepareStatement(SELECT_USER_WHERE_TOKEN);
            pstmt.setString(1, token);
            rs = pstmt.executeQuery();
            while (rs.next()){
                return new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("token"),
                        rs.getString("password")
                );
            }

        } catch (SQLException e) {
            logError(e);
        } finally {
            closeConnections();
        }
        return this.user;
    }


}
