package com.ntt.evaluation.controller


import com.ntt.tl.evaluation.controller.PhoneController
import com.ntt.tl.evaluation.dto.PhoneDto
import com.ntt.tl.evaluation.dto.RequestPhoneUser
import com.ntt.tl.evaluation.dto.ResponseGeneric
import com.ntt.tl.evaluation.service.IPhoneService
import org.springframework.http.HttpStatus
import spock.lang.Narrative
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Title

@Title("Test unitarios para PhoneController")
@Narrative("")
@Subject(PhoneController)
class PhoneControllerSpec extends Specification {

    def userPhoneService = Mock(IPhoneService)
    def phoneController = new PhoneController(userPhoneService: userPhoneService)

    def "test createUserPhone"() {
        given:
        def requestPhoneUser = new RequestPhoneUser(idUser: "user123", phone: new PhoneDto())
        def expectedResponse = new ResponseGeneric(message: "Phone created successfully")

        phoneController.createUserPhone(requestPhoneUser) >> expectedResponse

        when:
        def result = phoneController.createUserPhone(requestPhoneUser)

        then:
        result.getStatusCode() == HttpStatus.CREATED
    }

    def "test deleteUserPhone"() {
        given:
        def phoneId = 1
        def userId = "user123"
        def expectedResponse = new ResponseGeneric(message: "Phone deleted successfully")

        userPhoneService.deletePhoneToUser(userId, phoneId) >> expectedResponse

        when:
        def result = phoneController.deleteUserPhone(phoneId, userId)

        then:
        result.getStatusCode() == HttpStatus.OK
    }

    def "test modifyUserPhone"() {
        given:
        def requestPhoneUser = new RequestPhoneUser(idUser: "user123", phone: new PhoneDto())
        def expectedResponse = new ResponseGeneric(message: "Phone modified successfully")

        userPhoneService.modifyPhoneToUser(requestPhoneUser) >> expectedResponse

        when:
        def result = phoneController.modifyUserPhone(requestPhoneUser)

        then:
        result.getStatusCode() == HttpStatus.OK
    }
}
