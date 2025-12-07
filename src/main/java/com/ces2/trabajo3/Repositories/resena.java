package com.ces2.trabajo3.Repositories;

import com.ces2.trabajo3.Models.Resena;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class resena implements DAO<Resena> {

    private final JdbcTemplate jdbc;

    public resena(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private Resena mapRow(ResultSet rs) throws SQLException {
        Resena r = new Resena();
        r.setId(rs.getInt("id"));
        r.setComentario(rs.getString("comentario"));
        r.setCalificacion(rs.getInt("calificacion"));
        r.setProductoId(rs.getInt("producto_id"));
        return r;
    }

    @Override
    public Resena create(Resena r) {
        jdbc.update(
                "INSERT INTO resena (comentario, calificacion, producto_id) VALUES (?, ?, ?)",
                r.getComentario(),
                r.getCalificacion(),
                r.getProductoId()
        );
        return r;
    }

    @Override
    public Optional<Resena> findById(Integer id) {
        try {
            Resena r = jdbc.queryForObject(
                    "SELECT * FROM resena WHERE id=?",
                    (rs, row) -> mapRow(rs),
                    id
            );
            return Optional.ofNullable(r);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Resena> findAll(int limit, int offset) {
        return jdbc.query(
                "SELECT * FROM resena LIMIT ? OFFSET ?",
                (rs, row) -> mapRow(rs),
                limit, offset
        );
    }

    public List<Resena> findByProducto(Integer productoid) {
        return jdbc.query(
                "SELECT * FROM resena WHERE producto_id=?",
                (rs, row) -> mapRow(rs),
                productoid
        );
    }

    @Override
    public Resena update(Resena r) {
        jdbc.update(
                "UPDATE resena SET comentario=?, calificacion=?, producto_id=? WHERE id=?",
                r.getComentario(),
                r.getCalificacion(),
                r.getProductoId(),
                r.getId()
        );
        return r;
    }

    @Override
    public boolean delete(Integer id) {
        return jdbc.update("DELETE FROM resena WHERE id=?", id) > 0;
    }

    @Override
    public long count() {
        Long total = jdbc.queryForObject("SELECT COUNT(*) FROM resena", Long.class);
        return (total != null) ? total : 0;
    }
}
