package com.example.idlegoodsinfo.web;

import com.example.idlegoodsinfo.config.DataSourceManager;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class StartupListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        String jdbcUrl = "jdbc:mysql://localhost:3306/idlegoodsinfo?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&useUnicode=true&characterEncoding=UTF-8";
        try {
            DataSourceManager.initialize(jdbcUrl, "【user】", "【password】");
        } catch (Exception ex) {
            sce.getServletContext().log("Failed to initialize data source", ex);
            ex.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        DataSourceManager.close();
    }
}
