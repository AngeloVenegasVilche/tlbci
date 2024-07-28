package com.ntt.evaluation.controller

import com.ntt.tl.evaluation.controller.AuthController
import com.ntt.tl.evaluation.dto.ResponseGeneric
import com.ntt.tl.evaluation.service.IUserServices
import org.springframework.http.HttpStatus
import spock.lang.Narrative
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Title

@Title("Test unitarios para AuthController")
@Narrative("")
@Subject(AuthController)
class AuthControllerSpec extends Specification {

    def userServices = Mock(IUserServices)
    def authController = new AuthController(userServices: userServices)

    def "test generateToken"() {
        given:
        def email = "test@example.com"
        def password = "password123"
        def expectedResponse = new ResponseGeneric("test")

        userServices.loginUser(_ as String, _ as String) >> expectedResponse;

        when:
        def result = authController.generateToken(email, password)

        then:
        result.getStatusCode() == HttpStatus.CREATED
        result.getBody() == expectedResponse
    }

}