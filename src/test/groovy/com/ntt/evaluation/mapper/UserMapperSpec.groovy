package com.ntt.evaluation.controller.mapper


import spock.lang.Specification
import com.ntt.tl.evaluation.mapper.UserMapper
import com.ntt.tl.evaluation.entity.*
import com.ntt.tl.evaluation.dto.*
import com.ntt.tl.evaluation.constant.ERole;
class UserMapperSpec extends Specification {

    UserMapper userMapper

    def setup() {
        userMapper = new UserMapper()
    }

    def "listRoleToEntity debería convertir una lista de nombres de roles a una lista de RoleEntities"() {
        given: "Una lista de nombres de roles"
        def roles = ["ADMIN", "EDITOR"]

        when: "Se llama al método listRoleToEntity"
        def resultado = userMapper.listRoleToEntity(roles)

        then: "El resultado debe ser una lista de RoleEntity con los roles correctos"
        resultado.size() == 2
        resultado[0].name == ERole.ADMIN
        resultado[1].name == ERole.EDITOR
    }

    def "listPhoneDtoToUsersListPhoneEntity debería convertir una lista de PhoneDto a una lista de UsersPhoneEntity"() {
        given: "Una lista de PhoneDto y un usuario"
        def phoneDtos = [
                new PhoneDto(number: "123456789", citycode: "1", contrycode: "34"),
                new PhoneDto(number: "987654321", citycode: "2", contrycode: "34")
        ]

        def usuario = new UsersEntity(idUser: "usuario1")

        when: "Se llama al método listPhoneDtoToUsersListPhoneEntity"
        def resultado = userMapper.listPhoneDtoToUsersListPhoneEntity(phoneDtos, usuario)

        then: "El resultado debe ser una lista de UsersPhoneEntity con los datos correctos"
        resultado.size() == 2
        resultado.every { it.user == usuario }
        resultado[0].phoneNumber == "123456789"
        resultado[1].phoneNumber == "987654321"
    }

    def "phoneBdToPhoneDto debería convertir una lista de UsersPhoneEntity a una lista de PhoneDto"() {
        given: "Una lista de UsersPhoneEntity"
        def telefonos = [
                new UsersPhoneEntity(id: 1, phoneNumber: "123456789", cityCode: "1", countryCode: "34"),
                new UsersPhoneEntity(id: 2, phoneNumber: "987654321", cityCode: "2", countryCode: "34")
        ]

        when: "Se llama al método phoneBdToPhoneDto"
        def resultado = userMapper.phoneBdToPhoneDto(telefonos)

        then: "El resultado debe ser una lista de PhoneDto con los datos correctos"
        resultado.size() == 2
        resultado[0].id == 1
        resultado[0].number == "123456789"
        resultado[1].id == 2
        resultado[1].number == "987654321"
    }

    def "phoneUpdateDtoToUsersPhoneEntity debería convertir un PhoneUpdateDto a un UsersPhoneEntity"() {
        given: "Un PhoneUpdateDto y un usuario"
        def phoneUpdateDto = new PhoneUpdateDto(number: "123456789", citycode: "1", contrycode: "34")
        def usuario = new UsersEntity(idUser: "usuario1")

        when: "Se llama al método phoneUpdateDtoToUsersPhoneEntity"
        def resultado = userMapper.phoneUpdateDtoToUsersPhoneEntity(phoneUpdateDto, usuario)

        then: "El resultado debe ser un UsersPhoneEntity con los datos correctos"
        resultado.phoneNumber == "123456789"
        resultado.cityCode == "1"
        resultado.countryCode == "34"
        resultado.user == usuario
    }

    def "userBdToUserDto debería convertir un UsersEntity a un UserDto"() {
        given: "Un UsersEntity"
        def usuario = new UsersEntity(
                idUser: "usuario1",
                name: "Usuario de Prueba",
                email: "prueba@test.com",
                lastLogin: new Date(),
                modified: new Date(),
                created: new Date(),
                isActive: true,
                token: "token123",
                phones: []
        )

        when: "Se llama al método userBdToUserDto"
        def resultado = userMapper.userBdToUserDto(usuario)

        then: "El resultado debe ser un UserDto con los datos correctos"
        resultado.idUser == "usuario1"
        resultado.name == "Usuario de Prueba"
        resultado.email == "prueba@test.com"
        resultado.isActive
        resultado.token == "token123"
    }

    def "listUsersEntityToListUserDto debería convertir una lista de UsersEntity a una lista de UserDto"() {
        given: "Una lista de UsersEntity"
        def usuarios = [
                new UsersEntity(idUser: "usuario1", name: "Usuario 1", phones: []),
                new UsersEntity(idUser: "usuario2", name: "Usuario 2", phones: [])
        ]

        when: "Se llama al método listUsersEntityToListUserDto"
        def resultado = userMapper.listUsersEntityToListUserDto(usuarios)

        then: "El resultado debe ser una lista de UserDto con los datos correctos"
        resultado.size() == 2
        resultado[0].idUser == "usuario1"
        resultado[1].idUser == "usuario2"
    }

    def "usersEntityToResponseCreateUser debería convertir un UsersEntity a un ResponseCreateUser"() {
        given: "Un UsersEntity"
        def usuario = new UsersEntity(
                idUser: "usuario1",
                isActive: true,
                lastLogin: new Date(),
                modified: new Date(),
                created: new Date(),
                token: "token123",
                roles: [new RoleEntity(name: ERole.ADMIN)]
        )

        when: "Se llama al método usersEntityToResponseCreateUser"
        def resultado = userMapper.usersEntityToResponseCreateUser(usuario)

        then: "El resultado debe ser un ResponseCreateUser con los datos correctos"
        resultado.idUser == "usuario1"
        resultado.isActive
        resultado.token == "token123"
        resultado.roles == ["ADMIN"]
    }

    def "requestUserToUsersEntity debería convertir un RequestUser a un UsersEntity"() {
        given: "Un RequestUser y datos adicionales"
        def requestUser = new RequestUser(name: "Usuario de Prueba", email: "prueba@test.com")
        def password = "contraseñaEncriptada"
        def token = "token123"
        def fecha = new Date()
        def estado = true

        when: "Se llama al método requestUserToUsersEntity"
        def resultado = userMapper.requestUserToUsersEntity(requestUser, password, token, fecha, estado)

        then: "El resultado debe ser un UsersEntity con los datos correctos"
        resultado.name == "Usuario de Prueba"
        resultado.email == "prueba@test.com"
        resultado.pass == password
        resultado.token == token
        resultado.modified == fecha
        resultado.created == fecha
        resultado.lastLogin == fecha
        resultado.isActive == estado
        resultado.idUser != null // Asumiendo que CommonUtil.generateUUID() genera un UUID no nulo
    }
}