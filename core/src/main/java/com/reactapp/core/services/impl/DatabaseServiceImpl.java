package com.reactapp.core.services.impl;

import com.day.commons.datasource.poolservice.DataSourcePool;
import com.reactapp.core.services.ClienteService;
import com.reactapp.core.services.DatabaseService;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;

@Component(service = DatabaseService.class, immediate = true)
public class DatabaseServiceImpl implements DatabaseService {

    private final Logger logger = LoggerFactory.getLogger(DatabaseServiceImpl.class);

    @Reference
    private DataSourcePool dataSourcePool;

    @Override
    public Connection getConnection() {
        Connection connection = null;

        try {
            DataSource dataSource = (DataSource) dataSourcePool.getDataSource("desafio_db");
            connection = dataSource.getConnection();
            logger.debug("Connection opened");
        } catch (Exception e) {
            logger.debug(e.getMessage());
        }
        return connection;
    }
}
