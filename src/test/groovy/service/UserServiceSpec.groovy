package service

import com.ntt.tl.evaluation.config.AppConfig
import com.ntt.tl.evaluation.config.security.JwtUtils
import com.ntt.tl.evaluation.constant.ERole
import com.ntt.tl.evaluation.dto.PhoneDto
import com.ntt.tl.evaluation.dto.PhoneUpdateDto
import com.ntt.tl.evaluation.dto.RequestUpdateUser
import com.ntt.tl.evaluation.dto.RequestUser
import com.ntt.tl.evaluation.dto.ResponseCreateUser
import com.ntt.tl.evaluation.dto.UserDto
import com.ntt.tl.evaluation.entity.RoleEntity
import com.ntt.tl.evaluation.entity.UsersEntity
import com.ntt.tl.evaluation.entity.UsersPhoneEntity
import com.ntt.tl.evaluation.mapper.UserMapper
import com.ntt.tl.evaluation.repository.UserRepository
import com.ntt.tl.evaluation.service.UserService
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.crypto.password.PasswordEncoder
import spock.lang.Narrative
import spock.lang.Specification
import spock.lang.Title

@Title("Test unitarios para servicio de usuarios")
@Narrative("Test unitarios para servicio de usuarios")
class UserServiceSpec extends Specification {
    UserService userService
    UserRepository userRepository
    UserMapper userMapper
    PasswordEncoder passwordEncoder
    JwtUtils jwtUtils
    AppConfig appConfig
    AuthenticationManager authenticationManager


    def setup() {
        userRepository = Mock(UserRepository)
        userMapper = Mock(UserMapper)
        passwordEncoder = Mock(PasswordEncoder)
        jwtUtils = Mock(JwtUtils)
        appConfig = Mock(AppConfig)
        authenticationManager = Mock(AuthenticationManager)
        userService = new UserService(
                userRepository: userRepository,
                userMapper: userMapper,
                passwordEncoder: passwordEncoder,
                jwtUtils: jwtUtils,
                appConfig: appConfig,
                authenticationManager: authenticationManager
        )
    }

    def "loginUser deberia retornar un token para un usuario valido"() {
        given:
        def email = "test@example.com"
        def password = "password123"
        def user = new UsersEntity(
                email: email,
                pass: "encodedPassword",
                isActive: true,
                roles: [new RoleEntity(name: ERole.ADMIN)]
        )
        def token = "jwt_token"

        userRepository.findByEmail(email) >> Optional.of(user)
        passwordEncoder.matches(password, user.pass) >> true
        jwtUtils.createToken(_, email) >> token
        authenticationManager.authenticate(_) >> null

        when:
        def result = userService.loginUser(email, password)

        then:
        result.message == token
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

        Date dateNew = new Date();

        def savedUser = new UsersEntity(
                idUser: "uuid",
                name: requestUser.name,
                email: requestUser.email,
                created: dateNew,
                modified:  dateNew,
                lastLogin:  dateNew,
                token: "new_token",
                isActive: true,
                phones: [new UsersPhoneEntity(phoneNumber: "123456789", cityCode: "01", countryCode: "34")],
                roles: [new RoleEntity(name: ERole.USER)]
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
                token: savedUser.token,
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

    def "getOneUser retorna un usuario valido con id"() {
        given:
        def userId = "user123"
        Date dateNew = new Date();
        def user = new UsersEntity(
                idUser: userId,
                name: "John Doe",
                email: "john@example.com",
                created: dateNew,
                modified: dateNew,
                lastLogin: dateNew,
                isActive: true,
                phones: [new UsersPhoneEntity(phoneNumber: "123456789", cityCode: "01", countryCode: "34")],
                roles: [new RoleEntity(name: ERole.USER)]
        )

        def userDto = new UserDto(
                idUser: userId,
                name: "John Doe",
                email: "john@example.com",
                created: user.created,
                modified: user.modified,
                lastLogin: user.lastLogin,
                isActive: true,
                phons: [new PhoneDto(number: "123456789", citycode: "01", contrycode: "34")]
        )
        userRepository.findById(userId) >> Optional.of(user)
        userMapper.userBdToUserDto(user) >> userDto

        when:
        def result = userService.getOneUser(userId)

        then:
        result == userDto
        result.idUser == userId
        result.name == "John Doe"
        result.email == "john@example.com"
        result.phons.size() == 1
        result.phons[0].number == "123456789"
    }

    def "updateUser actualiza un usuario existente"() {
        given:
        def updateRequest = new RequestUpdateUser(
                idUser: "user123",
                name: "New Name",
                email: "new@example.com",
                isActive: true,
                phons: [new PhoneUpdateDto(id: "1", number: "987654321", citycode: "02", contrycode: "34")]
        )
        def existingUser = new UsersEntity(
                idUser: updateRequest.idUser,
                name: "Old Name",
                email: "old@example.com",
                isActive: false,
                phones: [new UsersPhoneEntity(id: 1, phoneNumber: "123456789", cityCode: "01", countryCode: "34")]
        )

        userRepository.findById(updateRequest.idUser) >> Optional.of(existingUser)

        when:
        def result = userService.updateUser(updateRequest)

        then:
        result.message == "Ok"
    }
}
