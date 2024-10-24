package com.example.gestorinventario.security.repository;

import com.example.gestorinventario.entity.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolRepository extends JpaRepository<Rol, Long> {

    //Buscar por nombres
    Rol findByNombre(String nombre);

    //Compreba si existe un rol por nombre
    boolean existsByNombre(String nombre);
}
