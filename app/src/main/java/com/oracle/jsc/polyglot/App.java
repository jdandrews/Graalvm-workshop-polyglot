/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package com.oracle.jsc.polyglot;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public static void main(String[] args) {
        App app = new App();

        System.out.println(app.getGreeting());

        try {
            app.dbHello();
        } catch (ClassNotFoundException | SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    public String getGreeting() {
        log.debug("getGreeting()");
        return "Hello World!";
    }

    private void dbHello() throws ClassNotFoundException, SQLException, IOException {
        log.debug("dbHello()");
        // delete the database named 'test' in the user home directory
        Files.deleteIfExists(Path.of("~", "test"));

        Class.forName("org.h2.Driver");
        Connection conn = DriverManager.getConnection("jdbc:h2:~/test");
        Statement stat = conn.createStatement();

        // this line would initialize the database
        // from the SQL script file 'init.sql'
        // stat.execute("runscript from 'init.sql'");

        stat.execute("create table test(id int primary key, name varchar(255))");
        stat.execute("insert into test values(1, 'Hello')");
        ResultSet rs;
        rs = stat.executeQuery("select * from test");
        while (rs.next()) {
            System.out.println(rs.getString("name"));
        }
        stat.close();
        conn.close();
    }
}
