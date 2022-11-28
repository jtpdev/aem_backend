package com.reactapp.core.services;

import java.sql.Connection;

public interface DatabaseService {

    Connection getConnection();
}
