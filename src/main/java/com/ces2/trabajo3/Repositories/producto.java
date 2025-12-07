package com.ces2.trabajo3.Repositories;

import com.ces2.trabajo3.Models.Categoria;
import com.ces2.trabajo3.Models.Producto;
import com.ces2.trabajo3.Models.TipoCategoria;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class producto {

    private final JdbcTemplate jdbc;

    public producto(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public Producto save(Producto p) {
        if (p.getId() == null) {
            // Crear nuevo producto
            jdbc.update(
                    "INSERT INTO producto(nombre, precio, stock, marca) VALUES (?, ?, ?, ?)",
                    p.getNombre(),
                    p.getPrecio(),
                    p.getStock(),
                    p.getMarca()
            );
        } else {

            jdbc.update(
                    "UPDATE producto SET nombre=?, precio=?, stock=?, marca=? WHERE id=?",
                    p.getNombre(),
                    p.getPrecio(),
                    p.getStock(),
                    p.getMarca(),
                    p.getId()
            );
        }
        return p;
    }

    public Optional<Producto> findById(Integer id) {
        try {
            Producto p = jdbc.queryForObject(
                    "SELECT * FROM producto WHERE id=?",
                    (rs, rowNum) -> mapProducto(rs),
                    id
            );

            p.setCategorias(findCategoriasByProductoId(p.getId()));

            return Optional.of(p);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public List<Producto> findAll(int limit, int offset) {
        return jdbc.query(
                "SELECT * FROM producto LIMIT ? OFFSET ?",
                (rs, rowNum) -> mapProducto(rs),
                limit,
                offset
        );
    }

    public long count() {
        return jdbc.queryForObject("SELECT COUNT(*) FROM producto", Long.class);
    }

    public boolean existsByNombre(String nombre) {
        Integer count = jdbc.queryForObject(
                "SELECT COUNT(*) FROM producto WHERE nombre=?",
                Integer.class,
                nombre
        );
        return count != null && count > 0;
    }

    public boolean deleteById(Integer id) {
        return jdbc.update("DELETE FROM producto WHERE id=?", id) > 0;
    }

    private Producto mapProducto(ResultSet rs) throws SQLException {
        Producto p = new Producto();

        p.setId(rs.getInt("id"));
        p.setNombre(rs.getString("nombre"));
        p.setPrecio(rs.getDouble("precio"));
        p.setStock(rs.getInt("stock"));
        p.setMarca(rs.getString("marca"));

        return p;
    }

    private List<Categoria> findCategoriasByProductoId(Integer productoid) {
        return jdbc.query(
                """
                SELECT c.id, c.nombre, c.tipo
                FROM categoria c
                INNER JOIN producto_categoria pc ON pc.categoriaid = c.id
                WHERE pc.productoid=?
                """,
                (rs, rowNum) -> mapCategoria(rs),
                productoid
        );
    }

    private Categoria mapCategoria(ResultSet rs) throws SQLException {
        Categoria c = new Categoria();

        c.setId(rs.getInt("id"));
        c.setNombre(rs.getString("nombre"));
        c.setTipo(TipoCategoria.valueOf(rs.getString("tipo")));

        return c;
    }
}
