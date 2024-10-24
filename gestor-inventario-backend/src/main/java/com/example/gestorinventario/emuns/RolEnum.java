package com.example.gestorinventario.emuns;

public enum RolEnum {

    ROLE_USER("ROLE_USER"),
    ROLE_ADMIN("ROLE_ADMIN");

    private final String rol;

    //Crear constructor
    RolEnum(String rol) {
        this.rol = rol;
    }

    //Crear getter
    public String getRol() {
        return rol;
    }
}
