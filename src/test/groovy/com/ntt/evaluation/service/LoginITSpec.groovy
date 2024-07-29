package com.ntt.evaluation.service

import com.ntt.tl.evaluation.EvaluationApplication
import com.ntt.tl.evaluation.config.AppConfig
import com.ntt.tl.evaluation.config.security.TokenProvider
import com.ntt.tl.evaluation.mapper.UserMapper
import com.ntt.tl.evaluation.repository.UserRepository
import com.ntt.tl.evaluation.service.IUserServices
import com.ntt.tl.evaluation.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

@SpringBootTest(classes = EvaluationApplication.class)
class LoginITSpec extends Specification {

    @Autowired
    IUserServices userService

    def setup() {
    }

    def "loginUser debería retornar un token para un usuario válido"() {

        when:
        String token = userService.loginUser(email, pass)

        then:
        token

        where:
        email  << ["admin@admin.com"]
        pass   << ["Just2."]

    }



}
