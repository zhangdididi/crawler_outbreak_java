package dao;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import javax.sql.DataSource;
import java.sql.*;

public class DBUtil {

    private static volatile DataSource dataSource = null;

    private static final String URL = "jdbc:mysql://localhost:3306/crawler?useUnicode=yes&characterEncoding=utf8";
//    private static final String URL = "jdbc:mysql://localhost:3306/crawler";
    private static final String USERNAME = "root";
//    private static final String PASSWORD = "m513....";
    private static final String PASSWORD = "19981216";


    private static DataSource getDataSource() {
        if (dataSource == null) {
            synchronized(DBUtil.class) {
                if (dataSource == null) {
                    dataSource = new MysqlDataSource();;
                    MysqlDataSource mysqlDataSource = (MysqlDataSource)dataSource;;
                    mysqlDataSource.setUrl(URL);
                    mysqlDataSource.setUser(USERNAME);
                    mysqlDataSource.setPassword(PASSWORD);
                }
            }
        }
        return dataSource;
    }

    public static Connection getConnection() {
        try {
            return getDataSource().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void close(Connection connection, PreparedStatement statement, ResultSet resultSet) {
        try {
            if (resultSet != null) resultSet.close();
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
