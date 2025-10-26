package com.example.appajicolorgrupo4.data.repository

import com.example.appajicolorgrupo4.data.local.user.UserDao       // DAO de usuario
import com.example.appajicolorgrupo4.data.local.user.UserEntity    // Entidad de usuario

// Repositorio: orquesta reglas de negocio para login/registro sobre el DAO.
class UserRepository(
    private val userDao: UserDao // Inyección del DAO
) {

    // Login: busca por correo y valida contraseña
    suspend fun login(correo: String, clave: String): Result<UserEntity> {
        val user = userDao.getByCorreo(correo)                       // Busca usuario
        return if (user != null && user.clave == clave) {            // Verifica pass
            Result.success(user)                                     // Éxito
        } else {
            Result.failure(IllegalArgumentException("Credenciales inválidas")) // Error
        }
    }

    // Registro: valida no duplicado y crea nuevo usuario
    suspend fun register(nombre: String, correo: String, clave: String, direccion: String): Result<Long> {
        val exists = userDao.getByCorreo(correo) != null             // ¿Correo ya usado?
        if (exists) {
            return Result.failure(IllegalStateException("El correo ya está registrado"))
        }
        val id = userDao.insert(                                     // Inserta nuevo
            UserEntity(
                nombre = nombre,
                correo = correo,
                clave = clave,
                direccion = direccion
            )
        )
        return Result.success(id)                                    // Devuelve ID generado
    }

    // Obtener usuario por ID
    suspend fun getUserById(id: Long): UserEntity? {
        return userDao.getById(id)
    }

    // Actualizar usuario
    suspend fun updateUser(user: UserEntity): Result<Unit> {
        return try {
            userDao.update(user)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}