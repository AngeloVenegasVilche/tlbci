package com.ntt.evaluation.service

import com.ntt.tl.evaluation.config.AppConfig
import com.ntt.tl.evaluation.config.security.TokenProvider
import com.ntt.tl.evaluation.constant.ERoleUser
import com.ntt.tl.evaluation.dto.*
import com.ntt.tl.evaluation.entity.RoleEntity
import com.ntt.tl.evaluation.entity.UsersEntity
import com.ntt.tl.evaluation.entity.UsersPhoneEntity
import com.ntt.tl.evaluation.mapper.UserMapper
import com.ntt.tl.evaluation.repository.UserRepository
import com.ntt.tl.evaluation.service.UserService
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import spock.lang.Narrative
import spock.lang.Specification
import spock.lang.Title

import java.time.Instant
import java.util.Date

@Title("Test unitarios para servicio de usuarios")
@Narrative("Test unitarios para servicio de usuarios")
class UserServiceSpec extends Specification {
    UserService userService
    UserRepository userRepository
    UserMapper userMapper
    PasswordEncoder passwordEncoder
    TokenProvider tokenProvider
    AppConfig appConfig
    AuthenticationManagerBuilder authenticationManagerBuilder

    SecurityContext securityContext = Mock(SecurityContext)


    def setup() {
        userRepository = Mock(UserRepository)
        userMapper = Mock(UserMapper)
        passwordEncoder = Mock(PasswordEncoder)
        tokenProvider = Mock(TokenProvider)
        appConfig = Mock(AppConfig)
        authenticationManagerBuilder = Mock(AuthenticationManagerBuilder)
        userService = new UserService(
                userRepository: userRepository,
                userMapper: userMapper,
                passwordEncoder: passwordEncoder,
                tokenProvider: tokenProvider,
                appConfig: appConfig,
                authenticationManagerBuilder: authenticationManagerBuilder
        )
    }

    def "createUser crea un usuario correctamente"() {
        given:
        def requestUser = new RequestUser(
                name: "John Doe",
                email: "new@example.com",
                password: "validPassword",
                roles: ["USER"],
                phones: [new PhoneDto(number: "123456789", citycode: "01", contrycode: "34")]
        )

        def savedUser = new UsersEntity(
                idUser: "uuid",
                name: requestUser.name,
                email: requestUser.email,
                created: Date.from(Instant.now()),
                modified: Date.from(Instant.now()),
                lastLogin: Date.from(Instant.now()),
                token: "new_token",
                isActive: true,
                phones: [new UsersPhoneEntity(phoneNumber: "123456789", cityCode: "01", countryCode: "34")],
                roles: [new RoleEntity(name: ERoleUser.USER)]
        )

        userRepository.findByEmail(requestUser.email) >> Optional.empty()
        appConfig.getPassRegex() >> ".*"
        appConfig.getEmailRegex() >> ".*"
        userMapper.requestUserToUsersEntity(_, _, _, _, _) >> savedUser
        userRepository.save(_) >> savedUser
        userMapper.usersEntityToResponseCreateUser(savedUser) >> new ResponseCreateUser(
                idUser: savedUser.idUser,
                created: savedUser.created,
                modified: savedUser.modified,
                lastLogin: savedUser.lastLogin,
                isActive: savedUser.isActive,
                roles: ["USER"]
        )

        when:
        def result = userService.createUser(requestUser)

        then:
        result.idUser == savedUser.idUser
        result.isActive == savedUser.isActive
        result.roles == ["USER"]
    }

    def "getAllUser obtiene una lista de usuarios"() {
        given:
        def users = [
                new UsersEntity(idUser: "1", name: "Pedro", email: "Pedro@example.com"),
                new UsersEntity(idUser: "2", name: "Juan", email: "Juan@example.com")
        ]
        def userDtos = [
                new UserDto(idUser: "1", name: "Luis", email: "Luis@example.com"),
                new UserDto(idUser: "2", name: "jose", email: "jose@example.com")
        ]
        userRepository.findAll() >> users
        userMapper.listUsersEntityToListUserDto(users) >> userDtos

        when:
        def result = userService.getAllUser()

        then:
        result.userData.size() == 2
        result.userData == userDtos
    }

    def "getOneUser retorna un usuario válido con email"() {
        given:
        def email = "john@example.com"
        def user = new UsersEntity(
                idUser: "user123",
                name: "John Doe",
                email: email,
                created: new Date(),
                modified: new Date(),
                lastLogin: new Date(),
                isActive: true,
                phones: [new UsersPhoneEntity(phoneNumber: "123456789", cityCode: "01", countryCode: "34")],
                roles: [new RoleEntity(name: ERoleUser.USER)]
        )

        def userDto = new UserDto(
                idUser: "user123",
                name: "John Doe",
                email: email,
                created: user.created,
                modified: user.modified,
                lastLogin: user.lastLogin,
                isActive: true,
                phons: [new PhoneDto(number: "123456789", citycode: "01", contrycode: "34")]
        )
        appConfig.getEmailRegex() >> ".*"
        userRepository.findByEmail(email) >> Optional.of(user)
        userMapper.userBdToUserDto(user) >> userDto

        when:
        def result = userService.getOneUser(email)

        then:
        result == userDto
        result.email == email
        result.name == "John Doe"
        result.phons.size() == 1
        result.phons[0].number == "123456789"
    }

    def "updateUser actualiza un usuario existente"() {
        given:
        def updateRequest = new RequestUpdateUser(
                pass: "newPassword",
                name: "New Name",
                email: "existing@example.com"
        )
        def existingUser = new UsersEntity(
                idUser: "user123",
                name: "Old Name",
                email: "existing@example.com",
                pass: "oldPassword",
                isActive: true
        )

        appConfig.getEmailRegex() >> ".*"
        userRepository.findByEmail(updateRequest.email) >> Optional.of(existingUser)
        passwordEncoder.encode(updateRequest.pass) >> "encodedNewPassword"

        when:
        def result = userService.updateUser(updateRequest)

        then:
        result.message == "Ok"
        1 * userRepository.save(_) >> { UsersEntity savedUser ->
            assert savedUser.name == "New Name"
            assert savedUser.pass == "encodedNewPassword"
            assert savedUser.modified != null
        }
    }
}