package com.example.appajicolorgrupo4.data.repository

import com.example.appajicolorgrupo4.data.local.user.UserDao
import com.example.appajicolorgrupo4.data.local.user.UserEntity
import com.example.appajicolorgrupo4.data.remote.NetworkResult
import com.example.appajicolorgrupo4.data.remote.api.ApiService
import com.example.appajicolorgrupo4.data.remote.dto.LoginRequest
import com.example.appajicolorgrupo4.data.remote.dto.RegisterRequest
import com.example.appajicolorgrupo4.data.remote.dto.UserDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Repositorio mejorado que implementa el patrón Repository
 * Gestiona tanto la fuente de datos local (Room) como la remota (Retrofit)
 *
 * IMPORTANTE: Este repositorio ahora soporta dos modos:
 * 1. Modo LOCAL: Solo usa Room Database (actual)
 * 2. Modo REMOTO: Usa la API con Retrofit y cachea en Room
 */
class UserRepositoryWithRetrofit(
    private val userDao: UserDao,
    private val apiService: ApiService
) {

    /**
     * Flag para cambiar entre modo local y remoto
     * true = usa API remota, false = usa solo base de datos local
     */
    private val useRemoteApi = false // Cambiar a true cuando tengas el backend listo

    // ==================== MÉTODOS DE AUTENTICACIÓN ====================

    /**
     * Registra un nuevo usuario
     * Si useRemoteApi = true, llama a la API y luego guarda en local
     * Si useRemoteApi = false, solo guarda en la base de datos local
     */
    suspend fun register(
        nombre: String,
        correo: String,
        telefono: String,
        clave: String,
        direccion: String
    ): NetworkResult<Long> {
        return withContext(Dispatchers.IO) {
            try {
                if (useRemoteApi) {
                    // Modo remoto: llamar a la API
                    val request = RegisterRequest(nombre, correo, telefono, clave, direccion)
                    val response = apiService.register(request)

                    if (response.isSuccessful && response.body() != null) {
                        val registerResponse = response.body()!!

                        if (registerResponse.success && registerResponse.data != null) {
                            // Guardar en base de datos local
                            val userEntity = mapUserDtoToEntity(registerResponse.data)
                            val userId = userDao.insert(userEntity)
                            NetworkResult.Success(userId)
                        } else {
                            NetworkResult.Error(registerResponse.message)
                        }
                    } else {
                        NetworkResult.Error(
                            "Error en el servidor: ${response.code()}",
                            response.code()
                        )
                    }
                } else {
                    // Modo local: solo base de datos
                    if (userDao.getUserByEmail(correo) != null) {
                        NetworkResult.Error("El correo ya está registrado")
                    } else {
                        val newUser = UserEntity(
                            nombre = nombre,
                            correo = correo,
                            telefono = telefono,
                            clave = clave,
                            direccion = direccion
                        )
                        val userId = userDao.insert(newUser)
                        NetworkResult.Success(userId)
                    }
                }
            } catch (e: Exception) {
                NetworkResult.Error("Error al registrar: ${e.message}")
            }
        }
    }

    /**
     * Inicia sesión de usuario
     */
    suspend fun login(correo: String, clave: String): NetworkResult<UserEntity> {
        return withContext(Dispatchers.IO) {
            try {
                if (useRemoteApi) {
                    // Modo remoto: llamar a la API
                    val request = LoginRequest(correo, clave)
                    val response = apiService.login(request)

                    if (response.isSuccessful && response.body() != null) {
                        val loginResponse = response.body()!!

                        if (loginResponse.success && loginResponse.data != null) {
                            // Guardar/actualizar en base de datos local
                            val userEntity = mapUserDtoToEntity(loginResponse.data)

                            // Verificar si el usuario ya existe en local
                            val existingUser = userDao.getUserByEmail(correo)
                            if (existingUser != null) {
                                // Actualizar usuario existente
                                userDao.update(userEntity.copy(id = existingUser.id))
                            } else {
                                // Insertar nuevo usuario
                                userDao.insert(userEntity)
                            }

                            // Guardar token si existe (implementar SessionManager)
                            loginResponse.token?.let { token ->
                                // TODO: Guardar token en SessionManager/DataStore
                            }

                            NetworkResult.Success(userEntity)
                        } else {
                            NetworkResult.Error(loginResponse.message)
                        }
                    } else {
                        NetworkResult.Error(
                            "Error en el servidor: ${response.code()}",
                            response.code()
                        )
                    }
                } else {
                    // Modo local: solo base de datos
                    val user = userDao.getUserByEmail(correo)
                    when {
                        user == null -> NetworkResult.Error("Usuario no encontrado")
                        user.clave != clave -> NetworkResult.Error("Contraseña incorrecta")
                        else -> NetworkResult.Success(user)
                    }
                }
            } catch (e: Exception) {
                NetworkResult.Error("Error al iniciar sesión: ${e.message}")
            }
        }
    }

    // ==================== MÉTODOS DE USUARIO ====================

    /**
     * Obtiene un usuario por ID
     */
    suspend fun getUserById(userId: Long): UserEntity? {
        return withContext(Dispatchers.IO) {
            userDao.getUserById(userId)
        }
    }

    /**
     * Actualiza la información del usuario
     */
    suspend fun updateUser(user: UserEntity): NetworkResult<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                if (useRemoteApi) {
                    // TODO: Implementar llamada a API para actualizar usuario
                    // val response = apiService.updateUser(user.id, token, mapEntityToDto(user))
                    userDao.update(user)
                    NetworkResult.Success(Unit)
                } else {
                    userDao.update(user)
                    NetworkResult.Success(Unit)
                }
            } catch (e: Exception) {
                NetworkResult.Error("Error al actualizar usuario: ${e.message}")
            }
        }
    }

    /**
     * Elimina todos los usuarios (solo para testing)
     */
    suspend fun deleteAllUsers() {
        withContext(Dispatchers.IO) {
            userDao.deleteAll()
        }
    }

    // ==================== MÉTODOS DE MAPEO ====================

    /**
     * Convierte un UserDto (de la API) a UserEntity (de Room)
     */
    private fun mapUserDtoToEntity(dto: UserDto): UserEntity {
        return UserEntity(
            id = dto.id,
            nombre = dto.nombre,
            correo = dto.correo,
            telefono = dto.telefono,
            clave = "", // No almacenar contraseña desde API
            direccion = dto.direccion ?: ""
        )
    }

    /**
     * Convierte un UserEntity (de Room) a UserDto (para la API)
     */
    private fun mapEntityToDto(entity: UserEntity): UserDto {
        return UserDto(
            id = entity.id,
            nombre = entity.nombre,
            correo = entity.correo,
            telefono = entity.telefono,
            direccion = entity.direccion
        )
    }
}

