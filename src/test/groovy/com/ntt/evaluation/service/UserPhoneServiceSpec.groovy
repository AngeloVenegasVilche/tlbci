package com.ntt.evaluation.service

import com.ntt.tl.evaluation.config.AppConfig
import com.ntt.tl.evaluation.dto.PhoneDto
import com.ntt.tl.evaluation.dto.RequestPhoneUser
import com.ntt.tl.evaluation.entity.UsersEntity
import com.ntt.tl.evaluation.entity.UsersPhoneEntity
import com.ntt.tl.evaluation.repository.PhoneRepository
import com.ntt.tl.evaluation.repository.UserRepository
import com.ntt.tl.evaluation.service.PhoneService
import spock.lang.Narrative
import spock.lang.Specification
import com.ntt.tl.evaluation.errors.GenericException
import spock.lang.Subject
import spock.lang.Title

import java.time.Instant

@Title("Test unitarios para servicio phone")
@Narrative("Test unitarios para servicio phone")
@Subject(PhoneService)
class UserPhoneServiceSpec extends Specification {
    PhoneService userPhoneService
    PhoneRepository phoneRepository
    UserRepository userRepository
    AppConfig appConfig

    def setup() {
        phoneRepository = Mock(PhoneRepository)
        userRepository = Mock(UserRepository)
        appConfig = Mock(AppConfig)
        userPhoneService = new PhoneService(
                phoneRepository,
                userRepository,
                appConfig
        )
    }

    def "createPhoneToUser crea un telefono para un usuario existente"() {
        given:
        def email = "user@example.com"
        def requestPhoneUser = new RequestPhoneUser(
                emaial: email,
                phone: new PhoneDto(number: "123456789", citycode: "1", contrycode: "1")
        )
        def user = new UsersEntity(
                idUser: "user123",
                email: email,
                name: "Test User",
                created: Date.from(Instant.now()),
                modified: Date.from(Instant.now()),
                lastLogin: Date.from(Instant.now()),
                token: "token123",
                pass: "password123",
                isActive: true
        )
        def savedPhone = new UsersPhoneEntity(
                id: 1,
                phoneNumber: "123456789",
                cityCode: "1",
                countryCode: "1",
                user: user
        )
        appConfig.getEmailRegex() >> "[a-zA-Z0-9_]+([.][a-zA-Z0-9_]+)*@[a-zA-Z0-9_]+([.][a-zA-Z0-9_]+)*[.][a-zA-Z]{2,5}"
        userRepository.findByEmail(email) >> Optional.of(user)
        phoneRepository.existsByPhoneNumberCityCodeAndCountryCode(_ as String, _ as String, _ as String, _ as String) >> Optional.empty()
        phoneRepository.save(_) >> savedPhone

        when:
        def result = userPhoneService.createPhoneToUser(requestPhoneUser)

        then:
        result.message == "id :1"
    }

    def "createPhoneToUser lanza excepción para un usuario no encontrado"() {
        given:
        def email = "nonexistent@example.com"
        def requestPhoneUser = new RequestPhoneUser(emaial: email)
        appConfig.getEmailRegex() >> '^[A-Za-z0-9+_.-]+@(.+)$'
        userRepository.findByEmail(email) >> Optional.empty()
        when:
        userPhoneService.createPhoneToUser(requestPhoneUser)

        then:
        thrown(GenericException)
    }

    def "deletePhoneToUser elimina un teléfono de un usuario existente"() {
        given:
        def email = "user@example.com"
        def phoneId = 1
        def user = new UsersEntity(
                idUser: "user123",
                email: email,
                name: "Test User",
                created: Date.from(Instant.now()),
                modified: Date.from(Instant.now()),
                lastLogin: Date.from(Instant.now()),
                token: "token123",
                pass: "password123",
                isActive: true
        )
        def phone = new UsersPhoneEntity(
                id: phoneId,
                phoneNumber: "123456789",
                cityCode: "1",
                countryCode: "1",
                user: user
        )
        user.phones = [phone]

        appConfig.getEmailRegex() >> "[a-zA-Z0-9_]+([.][a-zA-Z0-9_]+)*@[a-zA-Z0-9_]+([.][a-zA-Z0-9_]+)*[.][a-zA-Z]{2,5}"
        userRepository.findByEmail(email) >> Optional.of(user)
        userRepository.save(user)
        phoneRepository.delete(phone)

        when:
        def result = userPhoneService.deletePhoneToUser(email, phoneId)

        then:
        result.message == "Ok"
    }

    def "createPhoneToUser lanza excepción cuando el teléfono ya existe"() {
        given:
        def email = "user@example.com"
        def requestPhoneUser = new RequestPhoneUser(
                emaial: email,
                phone: new PhoneDto(number: "123456789", citycode: "1", contrycode: "1")
        )
        def user = new UsersEntity(email: email)

        appConfig.getEmailRegex() >> '^[A-Za-z0-9+_.-]+@(.+)$'
        userRepository.findByEmail(email) >> Optional.of(user)

        def savedPhone = new UsersPhoneEntity(
                id: 1,
                phoneNumber: "123456789",
                cityCode: "1",
                countryCode: "1",
                user: user
        )

        phoneRepository.existsByPhoneNumberCityCodeAndCountryCode(_ as String, _ as String, _ as String, _ as String) >> Optional.of(savedPhone)
        when:
        userPhoneService.createPhoneToUser(requestPhoneUser)

        then:
        thrown(GenericException)
    }
}