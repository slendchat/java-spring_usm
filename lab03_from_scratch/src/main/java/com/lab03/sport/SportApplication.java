package com.lab03.sport;

import org.h2.tools.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.sql.SQLException;

@SpringBootApplication
public class SportApplication {

    public static void main(String[] args) {
        try {
            // HTTP-консоль на 8082, доступ с любых хостов
            Server.createWebServer(
                "-web",
                "-webAllowOthers", 
                "-webPort", "8082"
            ).start();
            
            // TCP-сервер на 9092, доступ с любых хостов
            Server.createTcpServer(
                "-tcp",
                "-tcpAllowOthers",
                "-tcpPort", "9092"
            ).start();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        SpringApplication.run(SportApplication.class, args);
    }
}
