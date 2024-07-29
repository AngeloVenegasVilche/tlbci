package com.ntt.evaluation.controller

import com.ntt.tl.evaluation.controller.AuthController
import com.ntt.tl.evaluation.dto.LoginDto
import com.ntt.tl.evaluation.service.IUserServices
import com.ntt.tl.evaluation.config.security.JwtAuthorizationFilter
import org.springframework.http.HttpStatus
import spock.lang.Specification
import spock.lang.Subject

@Subject(AuthController)
class AuthControllerSpec extends Specification {
    def userServices = Mock(IUserServices)
    def authController = new AuthController(userServices: userServices)

    def "test generateToken"() {
        given:
        def loginDto = new LoginDto(username: "test@example.com", password: "password123")
        def expectedJwt = "test.jwt.token"

        userServices.loginUser(loginDto.username, loginDto.password) >> expectedJwt

        when:
        def result = authController.generateToken(loginDto)

        then:
        result.statusCode == HttpStatus.OK
        result.body.idToken == expectedJwt
        result.headers.getFirst(JwtAuthorizationFilter.AUTHORIZATION_HEADER) == "Bearer ${expectedJwt}"
    }
}