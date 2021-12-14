package com.tracing.api.tracing

import com.tracing.services.tracing.TracingServiceBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import spock.lang.Specification

@SpringBootTest
@AutoConfigureMockMvc
class TracingRestControllerTest extends Specification {

    @Autowired
    private MockMvc mvc;

    private TracingServiceBean tracingService = Mock(TracingServiceBean)


    def 'should hit the endpoint'() {
        given: 'the request '
            def request = MockMvcRequestBuilders.put("/tracing/vehicle/d03436a4-5d16-11ec-bf63-0242ac130002/position")
                .contentType(MediaType.APPLICATION_JSON)
                .content('{"lat":20.0, "lon":20.0}')
        when: 'the tracing end point is called'
            def result = mvc.perform(request)
        then: 'the following behaviour is expected'
            0 * _
            result.andExpect(MockMvcResultMatchers.status().isBadRequest())
    }
}
