package com.reactapp.core.daos.impl;

import com.reactapp.core.core.Mappeable;
import com.reactapp.core.daos.ProdutoDao;
import com.reactapp.core.core.Tableable;
import com.reactapp.core.enums.Categoria;
import com.reactapp.core.models.Produto;
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

@Component(immediate = true, service = ProdutoDao.class)
public class ProdutoDaoImpl extends Tableable implements ProdutoDao, Mappeable<Produto> {

    @Reference
    private DatabaseService databaseService;

    @Override
    public String getTable() {
        return "produto";
    }

    @Override
    public Produto findById(Integer id) {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = databaseService.getConnection();
            ps = connection.prepareStatement(
                    selectQueryWithWhereClause("id = ?", "id", "nome", "categoria", "preco")
            );
            ps.setInt(1, id);
            ps.execute();
            ResultSet rs = ps.getResultSet();
            Produto produto = null;
            if (rs.next()) {
                produto = getModel(rs);
            }
            return produto;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(connection, ps);
        }
    }

    @Override
    public List<Produto> list(Map<String, Object> params) {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = databaseService.getConnection();
            ps = connection.prepareStatement(
                    createSQLWithWhereClause(selectQuery("id", "nome", "categoria", "preco"), params)
            );

            paramsToPreparedStatement(ps, params);
            ps.execute();
            ResultSet rs = ps.getResultSet();
            List<Produto> produtos = new ArrayList<>();
            while (rs.next()) {
                Produto produto = getModel(rs);
                produtos.add(produto);
            }
            return produtos;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(connection, ps);
        }
    }

    @Override
    public Produto save(Produto model) {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = databaseService.getConnection();
            ps = prepareUpdateStatement(connection, insertQuery("nome", "categoria", "preco" ));
            ps.setString(1, model.getNome());
            ps.setString(2, model.getCategoria().name());
            ps.setBigDecimal(3, model.getPreco());
            return savedModel(model, ps);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(connection, ps);
        }
    }

    @Override
    public Produto update(Produto model) {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = databaseService.getConnection();
            ps = prepareUpdateStatement(connection, updateQuery("nome", "categoria", "preco" ));
            ps.setString(1, model.getNome());
            ps.setString(2, model.getCategoria().name());
            ps.setBigDecimal(3, model.getPreco());
            ps.setInt(4, model.getId());
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
    public Produto getModel(ResultSet rs) throws SQLException {
        Produto produto = new Produto();
        produto.setId(rs.getInt(1));
        produto.setNome(rs.getString(2));
        produto.setCategoria(Categoria.from(rs.getString(3)));
        produto.setPreco(rs.getBigDecimal(4));
        return produto;
    }
}
