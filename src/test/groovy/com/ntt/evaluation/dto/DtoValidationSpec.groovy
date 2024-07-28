package com.ntt.evaluation.controller.dto

import com.ntt.tl.evaluation.controller.AuthController
import com.ntt.tl.evaluation.dto.PhoneDto
import com.ntt.tl.evaluation.dto.PhoneUpdateDto
import com.ntt.tl.evaluation.dto.RequestDeletePhoneUser
import com.ntt.tl.evaluation.dto.RequestPhoneUser
import com.ntt.tl.evaluation.dto.RequestUpdateUser
import com.ntt.tl.evaluation.dto.RequestUser
import spock.lang.Narrative
import spock.lang.Specification
import jakarta.validation.Validation
import jakarta.validation.Validator
import jakarta.validation.ValidatorFactory
import spock.lang.Subject
import spock.lang.Title

@Title("Test unitarios para los Dto del proyecto")
@Narrative("Test unitarios para los Dto del proyecto")
class DtoValidationSpec extends Specification {
    Validator validator

    def setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory()
        validator = factory.getValidator()
    }

    def "test valid PhoneDto"() {
        given:
        def phoneDto = new PhoneDto(id: 1, number: "123456789", citycode: "12", contrycode: "34")

        when:
        def violations = validator.validate(phoneDto)

        then:
        violations.isEmpty()
    }

    def "test invalid PhoneDto"() {
        given:
        def phoneDto = new PhoneDto(id: 1, number: "12345", citycode: "1", contrycode: "3")

        when:
        def violations = validator.validate(phoneDto)

        then:
        !violations.isEmpty()
        violations.size() == 3
        violations.any { it.propertyPath.toString() == "number" }
        violations.any { it.propertyPath.toString() == "citycode" }
        violations.any { it.propertyPath.toString() == "contrycode" }
    }

    def "test valid PhoneUpdateDto"() {
        given:
        def phoneUpdateDto = new PhoneUpdateDto(id: "1", number: "123456789", citycode: "12", contrycode: "34")

        when:
        def violations = validator.validate(phoneUpdateDto)

        then:
        violations.isEmpty()
    }

    def "test invalid PhoneUpdateDto"() {
        given:
        def phoneUpdateDto = new PhoneUpdateDto(id: "1", number: "12345", citycode: "1", contrycode: "3")

        when:
        def violations = validator.validate(phoneUpdateDto)

        then:
        !violations.isEmpty()
        violations.size() == 3
        violations.any { it.propertyPath.toString() == "number" }
        violations.any { it.propertyPath.toString() == "citycode" }
        violations.any { it.propertyPath.toString() == "contrycode" }
    }

    def "test valid RequestDeletePhoneUser"() {
        given:
        def requestDeletePhoneUser = new RequestDeletePhoneUser(idUser: "user123", idPhone: 1)

        when:
        def violations = validator.validate(requestDeletePhoneUser)

        then:
        violations.isEmpty()
    }

    def "test invalid RequestDeletePhoneUser"() {
        given:
        def requestDeletePhoneUser = new RequestDeletePhoneUser(idUser: "", idPhone: null)

        when:
        def violations = validator.validate(requestDeletePhoneUser)

        then:
        !violations.isEmpty()
        violations.size() == 2
        violations.any { it.propertyPath.toString() == "idUser" }
        violations.any { it.propertyPath.toString() == "idPhone" }
    }

    def "test valid RequestPhoneUser"() {
        given:
        def requestPhoneUser = new RequestPhoneUser(idUser: "user123", phone: new PhoneDto(number: "123456789", citycode: "12", contrycode: "34"))

        when:
        def violations = validator.validate(requestPhoneUser)

        then:
        violations.isEmpty()
    }

    def "test valid RequestUpdateUser"() {
        given:
        def requestUpdateUser = new RequestUpdateUser(idUser: "12345", name: "Angelo Venegas", email: "avenegas@example.com", isActive: true, phons: [new PhoneUpdateDto(number: "123456789", citycode: "12", contrycode: "34")])

        when:
        def violations = validator.validate(requestUpdateUser)

        then:
        violations.isEmpty()
    }

    def "test invalid RequestUpdateUser"() {
        given:
        def requestUpdateUser = new RequestUpdateUser(idUser: "", name: "", email: "invalid-email", isActive: true, phons: null)

        when:
        def violations = validator.validate(requestUpdateUser)

        then:
        !violations.isEmpty()
        violations.size() == 4
        violations.any { it.propertyPath.toString() == "idUser" }
        violations.any { it.propertyPath.toString() == "name" }
        violations.any { it.propertyPath.toString() == "email" }
        violations.any { it.propertyPath.toString() == "phons" }
    }

    def "test valid RequestUser"() {
        given:
        def requestUser = new RequestUser(name: "Angelo Venegas", email: "avenegas@example.com", password: "password123", roles: ["USER"], phones: [new PhoneDto(number: "123456789", citycode: "12", contrycode: "34")])

        when:
        def violations = validator.validate(requestUser)

        then:
        violations.isEmpty()
    }
}