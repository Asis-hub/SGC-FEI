package db;

import java.sql.Connection;
import java.sql.SQLException;   
import java.sql.DriverManager;


public class ConexionDB {
    
    // Referencia a la biblioteca de MYSQL y JAVA
    private static String driver = "com.mysql.jdbc.Driver";
    // Nombre de la BD que nos conectaremos
    private static String database = "sgcfei";
    // URL del servidor de BD
    private static String hostname = "localhost";
    // Puerto del servidor de BD
    private static String port = "3307";
    // URL para conexión de JAVA al servidor BD
    private static String url = "jdbc:mysql://"+hostname+":"+port+"/"+database+"?allowPublicKeyRetrieval=true&useSSL=false";
    
    private static String username = "administrador";
    
    private static String password = "123456";
    
    public static Connection abrirConexionMySQL(){
        Connection conn = null;
        
        try{
            
            
            Class.forName(driver);
            conn = DriverManager.getConnection(url, username, password);
        }catch(ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }
        
        return conn;
    }
    
}
