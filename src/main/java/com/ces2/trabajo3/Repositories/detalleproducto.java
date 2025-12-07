package com.ces2.trabajo3.Repositories;

import com.ces2.trabajo3.Models.Detalleproducto;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.*;

@Repository
public class detalleproducto implements DAO<Detalleproducto> {

    private final JdbcTemplate jdbc;
    private final SimpleJdbcInsert insert;

    public detalleproducto(JdbcTemplate jdbc, DataSource dataSource) {
        this.jdbc = jdbc;
        this.insert = new SimpleJdbcInsert(dataSource)
                .withTableName("detalle_producto")
                .usingGeneratedKeyColumns("id");
    }

    private RowMapper<Detalleproducto> rowMapper = (rs, rn) -> {
        Detalleproducto d = new Detalleproducto();
        d.setId(rs.getInt("id"));
        d.setProductoid(rs.getInt("productoid"));
        d.setDescripcionLarga(rs.getString("descripcion_larga"));
        d.setMaterial(rs.getString("material"));
        d.setPeso(rs.getString("peso"));
        return d;
    };

    public Optional<Detalleproducto> findByProductId(Integer pid) {
        List<Detalleproducto> lista = jdbc.query(
                "SELECT * FROM detalle_producto WHERE productoid=?",
                rowMapper, pid
        );

        return lista.isEmpty() ? Optional.empty() : Optional.of(lista.get(0));
    }

    public Detalleproducto saveOrUpdate(Detalleproducto d) {

        Optional<Detalleproducto> existe = findByProductId(d.getProductoid());

        if (existe.isPresent()) {
            jdbc.update(
                    "UPDATE detalle_producto SET descripcion_larga=?, material=?, peso=? WHERE productoid=?",
                    d.getDescripcionLarga(), d.getMaterial(), d.getPeso(), d.getProductoid()
            );
        } else {

            Map<String, Object> params = new HashMap<>();
            params.put("productoid", d.getProductoid());
            params.put("descripcion_larga", d.getDescripcionLarga());
            params.put("material", d.getMaterial());
            params.put("peso", d.getPeso());
            insert.execute(params);
        }

        return d;
    }

    public boolean deleteByProductId(Integer productoid) {
        return jdbc.update(
                "DELETE FROM detalle_producto WHERE productoid = ?",
                productoid
        ) > 0;
    }

    @Override
    public Detalleproducto create(Detalleproducto d) {
        return saveOrUpdate(d);
    }

    @Override
    public Optional<Detalleproducto> findById(Integer id) {
        List<Detalleproducto> lista = jdbc.query(
                "SELECT * FROM detalle_producto WHERE id=?",
                rowMapper, id
        );
        return lista.isEmpty() ? Optional.empty() : Optional.of(lista.get(0));
    }

    @Override
    public List<Detalleproducto> findAll(int limit, int offset) {
        return jdbc.query(
                "SELECT * FROM detalle_producto LIMIT ? OFFSET ?",
                rowMapper, limit, offset
        );
    }

    @Override
    public Detalleproducto update(Detalleproducto d) {
        return saveOrUpdate(d);
    }

    @Override
    public boolean delete(Integer id) {
        return jdbc.update("DELETE FROM detalle_producto WHERE id=?", id) > 0;
    }

    @Override
    public long count() {
        return jdbc.queryForObject(
                "SELECT COUNT(*) FROM detalle_producto",
                Long.class
        );
    }
}
