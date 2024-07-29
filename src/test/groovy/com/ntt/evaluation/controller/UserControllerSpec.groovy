package com.ntt.evaluation.controller

import com.ntt.tl.evaluation.controller.UserController
import com.ntt.tl.evaluation.dto.*
import com.ntt.tl.evaluation.service.IUserServices
import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.validation.BindingResult
import spock.lang.Specification
import spock.lang.Subject

@Subject(UserController)
class UserControllerSpec extends Specification {
    def userServices = Mock(IUserServices)
    def userController = new UserController(userServices: userServices)
    def authentication = Mock(Authentication)
    def securityContext = Mock(SecurityContext)
    def bindingResult = Mock(BindingResult)

    def setup() {
        SecurityContextHolder.setContext(securityContext)
        securityContext.getAuthentication() >> authentication
        authentication.getName() >> "test@example.com"
    }

    def "test getCurrentUser"() {
        given: "Un usuario autenticado"
        def expectedResponse = new UserDto(idUser: "123", name: "John Doe")
        userServices.getOneUser("test@example.com") >> expectedResponse

        when: "Se llama al método getCurrentUser"
        def result = userController.getCurrentUser(authentication)

        then: "El resultado debe ser el esperado"
        result.statusCode == HttpStatus.OK
        result.body == expectedResponse
    }

    def "test updateUser para USER"() {
        given: "Una solicitud de actualización de usuario"
        def userUpdate = new RequestUpdateUser(email: "test@example.com", name: "John Doe Updated")
        def expectedResponse = new ResponseGeneric(message: "Usuario actualizado exitosamente")
        userServices.updateUser(userUpdate) >> expectedResponse

        when: "Se llama al método updateUser"
        def result = userController.updateUser(userUpdate, authentication, bindingResult)

        then: "El resultado debe ser el esperado"
        result.statusCode == HttpStatus.OK
        result.body == expectedResponse
    }

    // Tests para endpoints de ADMIN

    def "test createUser para ADMIN"() {
        given: "Una solicitud de creación de usuario"
        def userData = new RequestUser(name: "John Doe", email: "john@example.com", password: "password123", roles: ["USER"])
        def expectedResponse = ResponseCreateUser.builder()
                .idUser("123")
                .created(new Date())
                .modified(new Date())
                .lastLogin(new Date())
                .isActive(true)
                .roles(["USER"])
                .build()
        userServices.createUser(userData) >> expectedResponse

        when: "Se llama al método createUser"
        def result = userController.createUser(userData)

        then: "El resultado debe ser el esperado"
        result.statusCode == HttpStatus.CREATED
        with(result.body) {
            idUser == expectedResponse.idUser
            created != null
            modified != null
            lastLogin != null
            isActive == expectedResponse.isActive
            roles == expectedResponse.roles
        }
    }

    def "test getAllUser para ADMIN"() {
        given: "Una lista de usuarios"
        def expectedResponse = new ResponseListUser(userData: [new UserDto(idUser: "123", name: "John Doe")])
        userServices.getAllUser() >> expectedResponse

        when: "Se llama al método getAllUser"
        def result = userController.getAllUser()

        then: "El resultado debe ser el esperado"
        result.statusCode == HttpStatus.OK
        result.body == expectedResponse
    }

    def "test getUser para ADMIN"() {
        given: "Un ID de usuario"
        def idUser = "123"
        def expectedResponse = new UserDto(idUser: idUser, name: "John Doe")
        userServices.getOneUser(idUser) >> expectedResponse

        when: "Se llama al método getUser"
        def result = userController.getUser(idUser)

        then: "El resultado debe ser el esperado"
        result.statusCode == HttpStatus.OK
        result.body == expectedResponse
    }

    def "test deleteUser para ADMIN"() {
        given: "Un ID de usuario"
        def idUser = "123"
        def expectedResponse = new ResponseGeneric(message: "Usuario eliminado exitosamente")
        userServices.deleteUser(idUser) >> expectedResponse

        when: "Se llama al método deleteUser"
        def result = userController.deleteUser(idUser)

        then: "El resultado debe ser el esperado"
        result.statusCode == HttpStatus.CREATED
        result.body == expectedResponse
    }

    def "test updateUser para ADMIN"() {
        given: "Una solicitud de actualización de usuario"
        def userUpdate = new RequestUpdateUser(email: "john@example.com", name: "John Doe Updated")
        def expectedResponse = new ResponseGeneric(message: "Usuario actualizado exitosamente")
        userServices.updateUser(userUpdate) >> expectedResponse

        when: "Se llama al método updateUser para ADMIN"
        def result = userController.updateUser(userUpdate, bindingResult)

        then: "El resultado debe ser el esperado"
        result.statusCode == HttpStatus.OK
        result.body == expectedResponse
    }

    def "test activateAccount para ADMIN"() {
        given: "Una solicitud de activación de cuenta"
        def requestActivateAccount = new RequestActivateAccount(email: "john@example.com")
        def expectedResponse = new ResponseGeneric(message: "Cuenta activada exitosamente")
        userServices.activeAccount(requestActivateAccount) >> expectedResponse

        when: "Se llama al método activateAccount"
        def result = userController.activateAccount(requestActivateAccount)

        then: "El resultado debe ser el esperado"
        result.statusCode == HttpStatus.OK
        result.body == expectedResponse
    }
}