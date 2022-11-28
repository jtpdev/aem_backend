package com.reactapp.core.daos.impl;

import com.reactapp.core.core.Mappeable;
import com.reactapp.core.core.Tableable;
import com.reactapp.core.daos.NotaFiscalDao;
import com.reactapp.core.models.NotaFiscal;
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
import java.util.stream.Collectors;

@Component(immediate = true, service = NotaFiscalDao.class)
public class NotaFiscalDaoImpl extends Tableable implements NotaFiscalDao {

    @Reference
    private DatabaseService databaseService;

    @Override
    public String getTable() {
        return "nota_fiscal";
    }

    @Override
    public NotaFiscal findById(Integer id) {
        Connection connection = null;
        PreparedStatement ps = null;

        try {
            connection = databaseService.getConnection();
            ps = connection.prepareStatement(
                    selectQueryWithWhereClause("id = ?", "id", "numero", "cliente_id", "valor")
            );
            ps.setInt(1, id);
            ps.execute();
            ResultSet rs = ps.getResultSet();
            NotaFiscal notaFiscal = null;
            if (rs.next()) {
                notaFiscal = getModel(rs, connection);
            }
            return notaFiscal;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(connection, ps);
        }
    }

    @Override
    public List<NotaFiscal> list(Map<String, Object> params) {
        Connection connection = null;
        PreparedStatement ps = null;
        String sql = createSQLWithWhereClause(selectQuery("id", "numero", "cliente_id", "valor"), params);

        try {
            connection = databaseService.getConnection();
            ps = connection.prepareStatement(sql);

            paramsToPreparedStatement(ps, params);
            ps.execute();
            ResultSet rs = ps.getResultSet();
            List<NotaFiscal> notaFiscals = new ArrayList<>();
            while (rs.next()) {
                NotaFiscal notaFiscal = getModel(rs, connection);
                notaFiscals.add(notaFiscal);
            }
            return notaFiscals;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(connection, ps);
        }
    }

    @Override
    public NotaFiscal save(NotaFiscal model) {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = databaseService.getConnection();
            ps = prepareUpdateStatement(connection, insertQuery("numero", "cliente_id", "valor" ));
            ps.setInt(1, model.getNumero());
            ps.setInt(2, model.getIdCliente());
            ps.setBigDecimal(3, model.getValor());
            return savedModel(model, ps);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(connection, ps);
            saveProdutos(model);
        }
    }

    @Override
    public NotaFiscal update(NotaFiscal model) {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = databaseService.getConnection();
            ps = prepareUpdateStatement(connection, updateQuery("numero", "cliente_id", "valor" ));
            ps.setInt(1, model.getNumero());
            ps.setInt(2, model.getIdCliente());
            ps.setBigDecimal(3, model.getValor());
            ps.setInt(4, model.getId());
            return savedModel(model, ps);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(connection, ps);
            deleteProdutos(model.getId());
            saveProdutos(model);
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
            deleteProdutos(id);
        }
    }

    public NotaFiscal getModel(ResultSet rs, Connection connection) throws SQLException {
        NotaFiscal notaFiscal = new NotaFiscal();
        notaFiscal.setId(rs.getInt(1));
        notaFiscal.setNumero(rs.getInt(2));
        notaFiscal.setIdCliente(rs.getInt(3));
        notaFiscal.setValor(rs.getBigDecimal(4));
        notaFiscal.setIdsProduto(listProdutos(notaFiscal.getId(), connection));
        return notaFiscal;
    }

    public void deleteProdutos(Integer id) {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = databaseService.getConnection();
            ps = connection.prepareStatement(
                    String.format(DELETE_BASE_QUERY, "nf_produto", "nota_fiscal_id = ?")
            );
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(connection, ps);
        }
    }

    public List<Integer> listProdutos(Integer id, Connection connection) {
        PreparedStatement ps = null;
        String sql = String.format(
                SELECT_BASE_QUERY,
                "produto_id",
                "nf_produto"
        ) + WHERE_CLAUSE + "nota_fiscal_id = ?";

        try {
            ps = connection.prepareStatement(sql);

            ps.setInt(1, id);
            ps.execute();
            ResultSet rs = ps.getResultSet();
            List<Integer> produtos = new ArrayList<>();
            while (rs.next()) {
                produtos.add(rs.getInt(1));
            }
            return produtos;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveProdutos(NotaFiscal model) {
        Connection connection = null;
        PreparedStatement ps = null;
        connection = databaseService.getConnection();
        String sql = String.format(
                INSERT_BASE_QUERY,
                "nf_produto",
                "nota_fiscal_id, produto_id",
                model.getIdsProduto().stream()
                        .map(p -> "(?, ?)")
                        .collect(Collectors.joining(","))
        );

        try {

            ps = connection.prepareStatement(sql);
            for (int i = 0; i < model.getIdsProduto().size(); i++) {
                Integer p = model.getIdsProduto().get(i);
                ps.setInt((i+1)*2-1, model.getNumero());
                ps.setInt((i+1)*2, model.getIdCliente());
            }
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(connection, ps);
        }
    }
}
