package lk.ijse.gdse.pawsandclawscaremvc.db;

import lk.ijse.gdse.pawsandclawscaremvc.dto.UserDto;
import lk.ijse.gdse.pawsandclawscaremvc.util.security.PwdManager;

import java.util.ArrayList;


public class Database {
        public static ArrayList<UserDto> userTable= new ArrayList();
        static {
            userTable.add(
                    new UserDto("Sasuni","Wijerathne",
                            "sasuni@gmail.com",
                            new PwdManager().encrypt("1234"))
            );
        }

//public class DBConnection {
//    private static DBConnection dbConnection;
//    private Connection connection;
//
//    private DBConnection() throws SQLException {
//        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/CICVetCare","root","Ijse@1234");
//    }
//    public static DBConnection getInstance() throws SQLException {
//        if (dbConnection == null) {
//            dbConnection = new DBConnection();
//        }
//        return dbConnection;
//    }
//
//    public Connection getConnection() {
//        return connection;
//    }
//}
}
