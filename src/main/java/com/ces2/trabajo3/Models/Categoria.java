package com.ces2.trabajo3.Models;

public class Categoria {
        private Integer id;
        private String nombre;
        private TipoCategoria tipo;
        private String descripcion;   // ← NUEVO
        private Integer productoid;

        public Categoria() {}

        public Integer getId() { return id; }
        public void setId(Integer id) { this.id = id; }

        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }

        public TipoCategoria getTipo() { return tipo; }
        public void setTipo(TipoCategoria tipo) { this.tipo = tipo; }

        public String getDescripcion() { return descripcion; }
        public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

        public Integer getProductoid() { return productoid; }
        public void setProductoid(Integer productoid) { this.productoid = productoid; }
}
