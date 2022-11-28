package com.reactapp.core.daos.impl;

import com.reactapp.core.core.Mappeable;
import com.reactapp.core.daos.ClienteDao;
import com.reactapp.core.core.Tableable;
import com.reactapp.core.models.Cliente;
import com.reactapp.core.services.DatabaseService;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component(immediate = true, service = ClienteDao.class)
public class ClienteDaoImpl extends Tableable implements ClienteDao, Mappeable<Cliente> {

    @Reference
    private DatabaseService databaseService;

    @Override
    public String getTable() {
        return "cliente";
    }

    @Override
    public Cliente findById(Integer id) {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = databaseService.getConnection();
            ps = connection.prepareStatement(selectQueryWithWhereClause("id = ?", "id", "nome"));
            ps.setInt(1, id);
            ps.execute();
            ResultSet rs = ps.getResultSet();
            Cliente cliente = null;
            if (rs.next()) {
                cliente = getModel(rs);
            }
            return cliente;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(connection, ps);
        }
    }

    @Override
    public List<Cliente> list(Map<String, Object> params) {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = databaseService.getConnection();
            ps = connection.prepareStatement(createSQLWithWhereClause(selectQuery("id", "nome"), params));

            paramsToPreparedStatement(ps, params);
            ps.execute();
            ResultSet rs = ps.getResultSet();
            List<Cliente> clientes = new ArrayList<>();
            while (rs.next()) {
                Cliente cliente = getModel(rs);
                clientes.add(cliente);
            }
            return clientes;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(connection, ps);
        }
    }

    @Override
    public Cliente save(Cliente model) {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = databaseService.getConnection();
            ps = prepareUpdateStatement(connection, insertQuery("nome"));
            ps.setString(1, model.getNome());
            return savedModel(model, ps);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(connection, ps);
        }
    }

    @Override
    public Cliente update(Cliente model) {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = databaseService.getConnection();
            ps = prepareUpdateStatement(connection, updateQuery("nome"));
            ps.setString(1, model.getNome());
            ps.setInt(2, model.getId());
            return savedModel(model, ps);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(connection, ps);
        }
    }

    @Override
    public void delete(Integer id) {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = databaseService.getConnection();
            ps = connection.prepareStatement(deleteQuery());
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(connection, ps);
        }
    }

    @Override
    public Cliente getModel(ResultSet rs) throws SQLException {
        Cliente cliente = new Cliente();
        cliente.setId(rs.getInt(1));
        cliente.setNome(rs.getString(2));
        return cliente;
    }
}
