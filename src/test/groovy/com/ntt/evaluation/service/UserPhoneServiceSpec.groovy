package com.ntt.evaluation.service

import com.ntt.tl.evaluation.dto.PhoneDto
import com.ntt.tl.evaluation.dto.RequestPhoneUser
import com.ntt.tl.evaluation.entity.UsersEntity
import com.ntt.tl.evaluation.entity.UsersPhoneEntity
import com.ntt.tl.evaluation.repository.PhoneRepository
import com.ntt.tl.evaluation.repository.UserRepository
import com.ntt.tl.evaluation.service.UserPhoneService
import spock.lang.Narrative
import spock.lang.Specification
import com.ntt.tl.evaluation.errors.GenericException
import spock.lang.Subject
import spock.lang.Title


@Title("Test unitarios para servicio phone")
@Narrative("Test unitarios para servicio phone")
@Subject(UserPhoneService)
class UserPhoneServiceSpec extends Specification {
    UserPhoneService userPhoneService
    PhoneRepository phoneRepository
    UserRepository userRepository

    def setup() {
        phoneRepository = Mock(PhoneRepository)
        userRepository = Mock(UserRepository)
        userPhoneService = new UserPhoneService(
                phoneRepository: phoneRepository,
                userRepository: userRepository
        )
    }

    def "createPhoneToUser crea un telefono par un usuario existente"() {
        given:
        def requestPhoneUser = new RequestPhoneUser(
                idUser: "user123",
                phone: new PhoneDto(number: "123456789", citycode: "1", contrycode: "1")
        )
        def user = new UsersEntity(idUser: "user123")
        def savedPhone = new UsersPhoneEntity(id: 1)

        when:
        def result = userPhoneService.createPhoneToUser(requestPhoneUser)

        then:
        1 * userRepository.findById("user123") >> Optional.of(user)
        1 * phoneRepository.existsByPhoneNumberCityCodeAndCountryCode(_, _, _, _) >> Optional.empty()
        1 * phoneRepository.save(_) >> savedPhone
        result.message == "id :1"
    }

    def "createPhoneToUser excepcion para un usuario no encontrado"() {
        given:
        def requestPhoneUser = new RequestPhoneUser(idUser: "nonexistent")

        when:
        userPhoneService.createPhoneToUser(requestPhoneUser)

        then:
        1 * userRepository.findById("nonexistent") >> Optional.empty()
        thrown(GenericException)
    }

    def "deletePhoneToUser elimina un usuario existente"() {
        given:
        def userId = "user123"
        def phoneId = 1
        def user = new UsersEntity(idUser: userId)
        def phone = new UsersPhoneEntity(id: phoneId)
        user.phones = [phone]

        when:
        def result = userPhoneService.deletePhoneToUser(userId, phoneId)

        then:
        1 * userRepository.findById(userId) >> Optional.of(user)
        1 * userRepository.save(user)
        1 * phoneRepository.delete(phone)
        result.message == "Ok"
    }

}
