package com.lab03.sport;

import org.h2.tools.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SportApplication {

	public static void main(String[] args) {
		try {
			Server.createTcpServer(
				"-tcp",
				"-tcpAllowOthers",
				"-tcpPort", "9092"
			).start();
		} 
		catch (SQLException e) {
			e.printStackTrace();
			return;
		}
		SpringApplication.run(SportApplication.class, args);
	}

}
