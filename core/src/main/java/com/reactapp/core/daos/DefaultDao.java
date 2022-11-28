package com.reactapp.core.daos;

import com.reactapp.core.models.DefaultModel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface DefaultDao<T extends DefaultModel> {

    T findById(Integer id);

    List<T> list(Map<String, Object> params);

    T save(T model);

    T update(T model);

    void delete(Integer id);

    default T savedModel(T model, PreparedStatement ps) throws SQLException {
        if (ps.executeUpdate() == 1) {
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                model.setId(rs.getInt(1));
            }
            return model;
        } else {
            return null;
        }
    }


}
