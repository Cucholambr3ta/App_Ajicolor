package com.example.appajicolorgrupo4.data.local.user

import androidx.room.Dao                       // Marca esta interfaz como DAO de Room
import androidx.room.Insert                    // Para insertar filas
import androidx.room.OnConflictStrategy        // Estrategia de conflicto en inserción
import androidx.room.Query                     // Para queries SQL
import androidx.room.Update                    // Para actualizar filas

// @Dao indica que define operaciones para la tabla users.
@Dao
interface UserDao {

    // Inserta un usuario. ABORT si hay conflicto de PK (no de correo; ese lo controlamos a mano).
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(user: UserEntity): Long

    // Devuelve un usuario por correo (o null si no existe).
    @Query("SELECT * FROM users WHERE correo = :correo LIMIT 1")
    suspend fun getByCorreo(correo: String): UserEntity?

    // Devuelve un usuario por ID
    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): UserEntity?

    // Actualiza un usuario
    @Update
    suspend fun update(user: UserEntity): Int

    // Cuenta total de usuarios (para saber si hay datos y/o para seeds).
    @Query("SELECT COUNT(*) FROM users")
    suspend fun count(): Int

    // Lista completa (útil para debug/administración).
    @Query("SELECT * FROM users ORDER BY id ASC")
    suspend fun getAll(): List<UserEntity>
}