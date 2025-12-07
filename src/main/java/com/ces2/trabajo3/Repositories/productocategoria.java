package com.ces2.trabajo3.Repositories;

import com.ces2.trabajo3.Models.Categoria;
import com.ces2.trabajo3.Models.TipoCategoria;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class productocategoria {

    private final JdbcTemplate jdbc;

    public productocategoria(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public void asignarCategoria(Integer productoid, Integer categoriaid) {
        jdbc.update(
                "INSERT IGNORE INTO producto_categoria (productoid, categoriaid) VALUES (?, ?)",
                productoid, categoriaid
        );
    }

    public void eliminarCategoria(Integer productoid, Integer categoriaid) {
        jdbc.update(
                "DELETE FROM producto_categoria WHERE productoid=? AND categoriaid=?",
                productoid, categoriaid
        );
    }

    public List<Integer> categoriasDeProducto(Integer productoid) {
        return jdbc.queryForList(
                "SELECT categoriaid FROM producto_categoria WHERE productoid=?",
                Integer.class,
                productoid
        );
    }

    public List<Categoria> findCategoriasByProducto(Integer productoid) {

        String sql = """
            SELECT c.id, c.nombre, c.tipo, c.descripcion
            FROM categoria c
            INNER JOIN producto_categoria pc
                ON pc.categoriaid = c.id
            WHERE pc.productoid = ?
        """;

        return jdbc.query(sql, (rs, rowNum) -> {
            Categoria c = new Categoria();
            c.setId(rs.getInt("id"));
            c.setNombre(rs.getString("nombre"));
            c.setTipo(TipoCategoria.valueOf(rs.getString("tipo")));
            c.setDescripcion(rs.getString("descripcion"));
            return c;
        }, productoid);
    }
}
