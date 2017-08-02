package cn.sbx.deeper.moblie.db;

import android.content.Context;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicInteger;

import cn.sbx.deeper.moblie.contrants.Constants;

/**
 * JDBC 驱动 ,加密数据库 创建数据库连接,查询等方法
 *
 * @author Administrator
 */
public class SQLiteData {

    private static Context context;
    private AtomicInteger mOpenCounter = new AtomicInteger();


    // 传入上下文,和数据库密码
    /*
     * public SQLiteData(Context context) { this.context = context; try { //
	 * 加载驱动 Class.forName("DMEDB.JDBCDriver").newInstance(); } catch
	 * (InstantiationException e) { e.printStackTrace(); } catch
	 * (IllegalAccessException e) { e.printStackTrace(); } catch
	 * (ClassNotFoundException e) { e.printStackTrace(); } }
	 */

    static {
        try {

//            Class.forName("org.sqldroid.SqldroidDriver");
            // 加载驱动
//            Class.forName("DMEDB.JDBCDriver").newInstance();
            DriverManager.registerDriver((Driver) Class.forName("org.sqldroid.SQLDroidDriver").newInstance());
            System.out.println("数据库驱动加载成功===");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("数据库驱动加载失败===");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    // execute.executeQuery

    /**
     * @return 返回数据库连接
     * @throws SQLException
     */
//    private static Connection connection = null;
    public static Connection openOrCreateDatabase(Context context) throws SQLException {
        // 连接数据库
//        if (connection == null) {
        //         connection = DriverManager.getConnection(Constants.path_db, "",
//                "test123456");
//        Connection connection=null;
//        if(connection==null){
//            connection = DriverManager.getConnection(Constants.path_db);
//            System.out.print("数据库connection====" + connection);
//            return connection;
//        }else{
//            return null;
//        }
        Connection connection = DriverManager.getConnection(Constants.path_db);

        System.out.print("数据库connection====" + connection);
        return connection;

//        }

    }

    public static Connection openOrCreateDatabase() throws SQLException {
        // 连接数据库
//        if (connection == null) {
        // Connection     connection = DriverManager.getConnection(Constants.path_db, "",
//                "test123456");//
        Connection connection = DriverManager.getConnection(Constants.path_db);
//        }


        System.out.print("数据库connection===" + connection);
        return connection;
    }

    /**
     * 创建数据库密码
     *
     * @param password
     * @return
     * @throws SQLException
     */

    public static Connection openOrCreateDatabase(String password) throws SQLException {

        // 连接数据库
//        Connection connection = DriverManager.getConnection(Constants.path_db, "",
//                "test123456");//
        Connection connection = DriverManager.getConnection(Constants.path_db);
        System.out.print("connection" + connection);
        return connection;
    }

    /**
     * 在更新数据库密码时,连接新数据库,或者 在验证离线密码时 使用
     *
     * @param url      指定数据库路径
     * @param password 数据库密码
     * @return 数据库连接
     * @throws SQLException
     */
    public static Connection openOrCreateDatabase(String url, String password)
            throws SQLException {

        Connection connection;
        // 连接数据库
        connection = DriverManager.getConnection(url, "", password);
        return connection;
    }


}
