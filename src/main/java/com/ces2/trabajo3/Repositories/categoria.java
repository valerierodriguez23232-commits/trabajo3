package com.ces2.trabajo3.Repositories;

import com.ces2.trabajo3.Models.Categoria;
import com.ces2.trabajo3.Models.TipoCategoria;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class categoria implements DAO<Categoria> {

    private final JdbcTemplate jdbc;

    public categoria(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private Categoria mapRow(ResultSet rs) throws SQLException {
        Categoria c = new Categoria();

        c.setId(rs.getInt("id"));
        c.setNombre(rs.getString("nombre"));
        c.setDescripcion(rs.getString("descripcion"));
        c.setTipo(TipoCategoria.valueOf(rs.getString("tipo")));

        return c;
    }

    @Override
    public Categoria create(Categoria c) {
        jdbc.update(
                "INSERT INTO categoria (nombre, descripcion, tipo) VALUES (?, ?, ?)",
                c.getNombre(),
                c.getDescripcion(),
                c.getTipo().name()
        );
        return c;
    }

    @Override
    public Optional<Categoria> findById(Integer id) {
        try {
            Categoria c = jdbc.queryForObject(
                    "SELECT * FROM categoria WHERE id=?",
                    (rs, rowNum) -> mapRow(rs),
                    id
            );
            return Optional.ofNullable(c);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Categoria> findAll(int offset, int limit) {
        return jdbc.query(
                "SELECT * FROM categoria LIMIT ? OFFSET ?",
                (rs, rowNum) -> mapRow(rs),
                limit,
                offset
        );
    }

    @Override
    public Categoria update(Categoria c) {
        jdbc.update(
                "UPDATE categoria SET nombre=?, descripcion=?, tipo=? WHERE id=?",
                c.getNombre(),
                c.getDescripcion(),
                c.getTipo().name(),
                c.getId()
        );
        return c;
    }

    @Override
    public boolean delete(Integer id) {
        return jdbc.update("DELETE FROM categoria WHERE id=?", id) > 0;
    }

    @Override
    public long count() {
        return jdbc.queryForObject("SELECT COUNT(*) FROM categoria", Long.class);
    }
}
