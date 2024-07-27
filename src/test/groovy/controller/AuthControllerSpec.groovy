package com.ntt.tl.evaluation.controller

import com.ntt.tl.evaluation.dto.ResponseGeneric
import com.ntt.tl.evaluation.service.IUserServices
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Narrative
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Title

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup

@Title("Test unitarios para AuthController")
@Narrative("")
@Subject(AuthController)
class AuthControllerSpec extends Specification {
    private AuthController authController;
    private IUserServices userServices = Mock(IUserServices);
    private MockMvc mockMvc;

    def setup() {
        authController = new AuthController(userServices);
        mockMvc = standaloneSetup(AuthController).build();
    }

    def "Test para "() {
        given: " "
        userServices.loginUser(_ as String, _ as String) >> new ResponseGeneric();

        when: " asdadadda"

        def resultAction = mockMvc
                .perform(get("/security/loginUser/angelo.venegas@hotmail.fll/Jus12.")
                        .accept(MediaType.APPLICATION_JSON))

        then: "asdadadad "

        resultAction.andExpect(content().contentType(MediaType.APPLICATION_JSON))
        resultAction.andExpect(status().is(200))


        where:
        1==1

    }


}