package com.ntt.evaluation.controller

import com.ntt.tl.evaluation.controller.UserController
import com.ntt.tl.evaluation.dto.RequestUpdateUser
import com.ntt.tl.evaluation.dto.RequestUser
import com.ntt.tl.evaluation.dto.ResponseCreateUser
import com.ntt.tl.evaluation.dto.ResponseGeneric
import com.ntt.tl.evaluation.dto.ResponseListUser
import com.ntt.tl.evaluation.dto.UserDto
import com.ntt.tl.evaluation.service.IUserServices
import org.springframework.http.HttpStatus
import spock.lang.Specification

class UserControllerSpec extends Specification {
    def userServices = Mock(IUserServices)
    def userController = new UserController(userServices: userServices)

    def "test createUser"() {
        given:
        def userData = new RequestUser(name: "John Doe", email: "john@example.com", password: "password123", roles: ["USER"], phones: [])
        def expectedResponse = new ResponseCreateUser(idUser: "123", created: new Date(), modified: new Date(), lastLogin: new Date(), token: "token123", isActive: true, roles: ["USER"])

        userServices.createUser(userData) >> expectedResponse

        when:
        def result = userController.createUser(userData)

        then:
        result.getStatusCode() == HttpStatus.CREATED
    }

    def "test getAllUser"() {
        given:
        def expectedResponse = new ResponseListUser(userData: [new UserDto(idUser: "123", name: "John Doe")])

        userServices.getAllUser() >> expectedResponse

        when:
        def result = userController.getAllUser()

        then:
        result.getStatusCode() == HttpStatus.CREATED
    }

    def "test getUser"() {
        given:
        def idUser = "123"
        def expectedResponse = new UserDto(idUser: idUser, name: "John Doe")

        userServices.getOneUser(idUser) >> expectedResponse

        when:
        def result = userController.getUser(idUser)

        then:
        result.getStatusCode() == HttpStatus.CREATED
    }

    def "test deleteUser"() {
        given:
        def idUser = "123"
        def expectedResponse = new ResponseGeneric(message: "User deleted successfully")

        userServices.deleteUser(idUser) >> expectedResponse

        when:
        def result = userController.deleteUser(idUser)

        then:
        result.getStatusCode() == HttpStatus.CREATED
    }

    def "test updateUser"() {
        given:
        def userUpdate = new RequestUpdateUser(idUser: "123", name: "John Doe Updated", email: "john@example.com", isActive: true, phons: [])
        def expectedResponse = new ResponseGeneric(message: "User updated successfully")

        userServices.updateUser(userUpdate) >> expectedResponse

        when:
        def result = userController.updateUser(userUpdate, null)

        then:
        result.getStatusCode() == HttpStatus.CREATED
    }
}