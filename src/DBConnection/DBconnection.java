package DBConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DBconnection {
    private static DBconnection dBconnection;
    private Connection connection;


    private DBconnection(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/library?createDatabaseIfNotExist=true&allowMultiQueries=true","root","123");
            PreparedStatement show_tables = connection.prepareStatement("SHOW TABLES");
            ResultSet execute = show_tables.executeQuery();
            if(!execute.next()){
                PreparedStatement createTable = connection.prepareStatement("CREATE TABLE manage_book\n" +
                        "(\n"+
                        "bookId VARCHAR(10) NOT NULL\n" +
                        "PRIMARY KEY,\n" +
                        "bookName VARCHAR(20) NOT NULL,\n" +
                        "bookAuther VARCHAR(50) NOT NULL,\n" +
                        "bookPrice DECIMAL NOT NULL,\n" +
                        "bookQty INT(20) NOT NULL,\n" +
                        "exactQty INT(20) NOT NULL\n" +
                        ");\n" +
                        "\n" +
                        "CREATE TABLE member\n" +
                        "(\n" +
                        "Id VARCHAR(10) NOT NULL\n" +
                        "PRIMARY KEY,\n" +
                        "name VARCHAR(20) NOT NULL,\n" +
                        "Address VARCHAR(30) NOT NULL,\n" +
                        "Contact INT NOT NULL\n" +
                        ");\n" +
                        "\n" +
                        "CREATE TABLE issue_book\n" +
                        "(\n" +
                        "dateissue DATE NOT NULL,\n" +
                        "orderId VARCHAR(10) NOT NULL\n" +
                        "PRIMARY KEY,\n" +
                        "bookId VARCHAR(10) NOT NULL,\n" +
                        "memberId VARCHAR(10) NOT NULL,\n" +
                        "CONSTRAINT issue_book_manage_book__fk\n" +
                        "FOREIGN KEY(bookId) REFERENCES manage_book (bookId)\n" +
                        "ON UPDATE CASCADE\n" +
                        "ON DELETE CASCADE,\n" +
                        "CONSTRAINT issue_book_member__fk\n" +
                        "FOREIGN KEY(memberId) REFERENCES member (Id)\n" +
                        "ON UPDATE CASCADE\n" +
                        "ON DELETE CASCADE\n" +
                        ");\n" +
                        "\n" +
                        "CREATE TABLE reserve_book\n" +
                        "(\n" +
                        "bookId VARCHAR(20) DEFAULT '' NOT NULL,\n" +
                        "bookName VARCHAR(20) NOT NULL,\n" +
                        "orderId VARCHAR(10) NOT NULL,\n" +
                        "memberName VARCHAR(20) NOT NULL,\n" +
                        "issuedDate DATE NOT NULL,\n" +
                        "resivedDate DATE NOT NULL,\n" +
                        "note VARCHAR(50) NULL,\n" +
                        "additionalPrice INT(20) NULL,\n" +
                        "totalPrice INT(20) NOT NULL,\n" +
                        "PRIMARY KEY (orderId, bookId)\n" +
                        ");");

                createTable.execute();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public static DBconnection getInstance(){
        return (dBconnection==null)?(dBconnection=new DBconnection()):dBconnection;
    }

    public Connection getConnection(){return connection;}

}
