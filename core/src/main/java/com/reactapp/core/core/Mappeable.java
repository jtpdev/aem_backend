package com.reactapp.core.core;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface Mappeable<T> {

    T getModel(ResultSet rs) throws SQLException;

}
