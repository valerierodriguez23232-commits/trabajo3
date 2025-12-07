package com.ces2.trabajo3.Models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class Detalleproducto {

    private Integer id;

    @NotNull(message = "El ID del producto es obligatorio")
    private Integer productoid;

    @NotBlank(message = "La descripción larga es obligatoria")
    private String descripcionLarga;

    @NotBlank(message = "El material es obligatorio")
    private String material;

    @NotBlank(message = "El peso es obligatorio")
    private String peso;

    public Detalleproducto() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProductoid() {
        return productoid;
    }

    public void setProductoid(Integer productoid) {
        this.productoid = productoid;
    }

    public String getDescripcionLarga() {
        return descripcionLarga;
    }

    public void setDescripcionLarga(String descripcionLarga) {
        this.descripcionLarga = descripcionLarga;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getPeso() {
        return peso;
    }

    public void setPeso(String peso) {
        this.peso = peso;
    }
}
