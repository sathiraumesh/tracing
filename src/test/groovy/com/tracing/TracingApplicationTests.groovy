package com.tracing

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import spock.lang.Specification

@SpringBootTest
class TracingApplicationTests extends Specification{

    @Autowired
    private ApplicationContext context;

    def 'should load the application context'() {
        expect: 'the context to not be null'
            context != null
    }
}
