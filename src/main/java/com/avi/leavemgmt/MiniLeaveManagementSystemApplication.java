package com.avi.leavemgmt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class MiniLeaveManagementSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(MiniLeaveManagementSystemApplication.class, args);
        System.out.println("🚀 Mini Leave Management System is running!");
        System.out.println("📄 API Documentation: http://localhost:8080/swagger-ui.html");
        System.out.println("🗃️  H2 Console: http://localhost:8080/h2-console");
    }
}