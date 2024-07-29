package com.ntt.evaluation.controller

import com.ntt.tl.evaluation.controller.PhoneController
import com.ntt.tl.evaluation.dto.PhoneDto
import com.ntt.tl.evaluation.dto.RequestPhoneUser
import com.ntt.tl.evaluation.dto.ResponseGeneric
import com.ntt.tl.evaluation.service.IPhoneService
import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import spock.lang.Specification
import spock.lang.Subject

@Subject(PhoneController)
class PhoneControllerSpec extends Specification {

    def userPhoneService = Mock(IPhoneService)
    def phoneController = new PhoneController(userPhoneService: userPhoneService)
    def authentication = Mock(Authentication)
    def securityContext = Mock(SecurityContext)

    def setup() {
        // Configuración de la autenticación simulada
        SecurityContextHolder.setContext(securityContext)
        securityContext.getAuthentication() >> authentication
        authentication.getName() >> "test@example.com"
    }

    def "test createUserPhone para USER"() {
        given: "Una solicitud de creación de teléfono y una respuesta esperada"
        def requestPhoneUser = new RequestPhoneUser(emaial: "test@example.com", phone: new PhoneDto())
        def expectedResponse = new ResponseGeneric(message: "Teléfono creado exitosamente")

        userPhoneService.createPhoneToUser(_) >> expectedResponse

        when: "Se llama al método createUserPhone"
        def result = phoneController.createUserPhone(requestPhoneUser, authentication)

        then: "El resultado debe ser el esperado"
        result.statusCode == HttpStatus.CREATED
        result.body == expectedResponse

    }

    def "test deleteUserPhone para USER"() {
        given: "Un ID de teléfono y una respuesta esperada"
        def phoneId = 1
        def expectedResponse = new ResponseGeneric(message: "Teléfono eliminado exitosamente")

        userPhoneService.deletePhoneToUser(_, _) >> expectedResponse

        when: "Se llama al método deleteUserPhone"
        def result = phoneController.deleteUserPhone(phoneId, authentication)

        then: "El resultado debe ser el esperado"
        result.statusCode == HttpStatus.OK
        result.body == expectedResponse
    }

    def "test modifyUserPhone para USER"() {
        given: "Una solicitud de modificación de teléfono y una respuesta esperada"
        def requestPhoneUser = new RequestPhoneUser(emaial: "test@example.com", phone: new PhoneDto())
        def expectedResponse = new ResponseGeneric(message: "Teléfono modificado exitosamente")

        userPhoneService.modifyPhoneToUser(_) >> expectedResponse

        when: "Se llama al método modifyUserPhone"
        def result = phoneController.modifyUserPhone(requestPhoneUser, authentication)

        then: "El resultado debe ser el esperado"
        result.statusCode == HttpStatus.OK
        result.body == expectedResponse
    }

    // Tests para endpoints de ADMIN

    def "test createUserPhone para ADMIN"() {
        given: "Una solicitud de creación de teléfono y una respuesta esperada"
        def requestPhoneUser = new RequestPhoneUser(emaial: "admin@example.com", phone: new PhoneDto())
        def expectedResponse = new ResponseGeneric(message: "Teléfono creado exitosamente")

        userPhoneService.createPhoneToUser(_) >> expectedResponse

        when: "Se llama al método createUserPhone para ADMIN"
        def result = phoneController.createUserPhone(requestPhoneUser)

        then: "El resultado debe ser el esperado"
        result.statusCode == HttpStatus.CREATED
        result.body == expectedResponse
    }

    def "test deleteUserPhone para ADMIN"() {
        given: "Un ID de teléfono, un email de usuario y una respuesta esperada"
        def phoneId = 1
        def userEmail = "user@example.com"
        def expectedResponse = new ResponseGeneric(message: "Teléfono eliminado exitosamente")

        userPhoneService.deletePhoneToUser(_, _) >> expectedResponse

        when: "Se llama al método deleteUserPhone para ADMIN"
        def result = phoneController.deleteUserPhone(phoneId, userEmail)

        then: "El resultado debe ser el esperado"
        result.statusCode == HttpStatus.OK
        result.body == expectedResponse
    }

    def "test modifyUserPhone para ADMIN"() {
        given: "Una solicitud de modificación de teléfono y una respuesta esperada"
        def requestPhoneUser = new RequestPhoneUser(emaial: "admin@example.com", phone: new PhoneDto())
        def expectedResponse = new ResponseGeneric(message: "Teléfono modificado exitosamente")

        userPhoneService.modifyPhoneToUser(_) >> expectedResponse

        when: "Se llama al método modifyUserPhone para ADMIN"
        def result = phoneController.modifyUserPhone(requestPhoneUser)

        then: "El resultado debe ser el esperado"
        result.statusCode == HttpStatus.OK
        result.body == expectedResponse
    }
}