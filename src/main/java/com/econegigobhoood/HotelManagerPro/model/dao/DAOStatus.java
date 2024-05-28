package com.econegigobhoood.HotelManagerPro.model.dao;

import com.econegigobhoood.HotelManagerPro.model.entity.Status;
import com.econegigobhoood.HotelManagerPro.config.DBConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class DAOStatus implements IDAO<Status> {
    private String lembreteSQLExcept = "O tratamento deste erro, em aplicações que"
            + " não sejam CLI (tipo web), deve ser feito em outro lugar, tipo na"
            + " View. Deixando o tratamento aqui e retornando nulo, se não for CLI,"
            + " nunca saberemos que o erro aconteceu.";

    @Override
    public String getNomeClasse() {
        return Status.class.getSimpleName();
    }

    @Override
    public PreparedStatement dbConnect(String sql) throws SQLException {
        Connection connection = DBConfig.getCon();
        return connection.prepareStatement(sql,
                PreparedStatement.RETURN_GENERATED_KEYS);
    }

    @Override
    public int getStmtId(PreparedStatement stmt) throws SQLException {
        try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                int generatedId = generatedKeys.getInt(1);
                return generatedId;
            } else {
                throw new SQLException("Falha no ID gerado");
            }
        }
    }

    @Override
    public int cadastrar(Status entidade) {
        String sql = "INSERT INTO res_status (nome) VALUES (?)";
        try (PreparedStatement stmt = dbConnect(sql)) {
            stmt.setString(1, entidade.getNome());

            stmt.executeUpdate();
            int generatedId = getStmtId(stmt);
            return generatedId;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(lembreteSQLExcept);
        }
        return 0;
    }

    @Override
    public void atualizar(Status entidade) {
        String sql = "UPDATE res_status SET nome = ? WHERE id = ?";
        try (PreparedStatement stmt = dbConnect(sql)) {
            stmt.setString(1, entidade.getNome());
            stmt.setInt(2, entidade.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(lembreteSQLExcept);
        }
    }

    @Override
    public void excluir(int id) {
        String sql = "DELETE FROM res_status WHERE id = ?";
        try (PreparedStatement stmt = dbConnect(sql)) {
            stmt.setInt(1, id);

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(lembreteSQLExcept);
        }
    }

    @Override
    public Status buscar(int id) {
        String sql = "SELECT * FROM res_status WHERE id = ?";

        try (PreparedStatement stmt = dbConnect(sql)) {
            stmt.setInt(1, id);
            try(ResultSet rs = stmt.executeQuery()) {
                if(rs.next()) {
                    String nome = rs.getString("nome");
                    Status entidade = new Status(id, nome);

                    return entidade;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(lembreteSQLExcept);
        }
        return null;
    }

    @Override
    public List<Status> listar() {
        List<Status> entidades = new ArrayList<Status>();
        String sql = "SELECT * FROM res_status";

        try (PreparedStatement stmt = dbConnect(sql);
                ResultSet rs = stmt.executeQuery();) {
            while(rs.next()) {
                String nome = rs.getString("nome");
                int id = rs.getInt("id");
                Status entidade = new Status(id, nome);
                entidades.add(entidade);
            }
            return entidades;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(lembreteSQLExcept);
        }
        return null;
    }
}