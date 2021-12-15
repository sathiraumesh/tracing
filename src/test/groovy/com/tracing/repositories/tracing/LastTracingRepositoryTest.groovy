package com.tracing.repositories.tracing

import com.tracing.entities.LastTraceEntity
import com.tracing.entities.TracingEntity
import com.tracing.entities.VehicleEntity
import com.tracing.repositories.vehicle.VehicleRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import spock.lang.Specification
import spock.lang.Subject

import java.time.Instant

@DataJpaTest
@Import([TracingRepository, VehicleRepository, LastTracingRepository])
class LastTracingRepositoryTest extends Specification {

    @Autowired
    @Subject
    private LastTracingRepository lastTracingRepository

    @Autowired
    private VehicleRepository vehicleRepository

    @Autowired
    private TracingRepository tracingRepository

    def cleanup() {
        lastTracingRepository.deleteAll()
        tracingRepository.deleteAll()
        vehicleRepository.deleteAll()
    }

    def 'should save last trace'(){
        given:'a vehicel and a trace '

       def vehicle = vehicleRepository.save( new VehicleEntity(
            createdAt: Instant.now(),
            updatedAt: Instant.now()
        ))
        def tracing = tracingRepository.save(
            new TracingEntity(
                vehicle:vehicle,
                createdAt: Instant.now()
            )
        )
        when: 'the last traced is saved'
        def result = lastTracingRepository.save(new LastTraceEntity(
            vehicle: vehicle,
            trace: tracing
        ))
        then:'the result is not empty'
        result
    }

    def 'should update last trace'(){
        given:'a vehicel, a trace and a last trace'
            def vehicle = vehicleRepository.save( new VehicleEntity(
                createdAt: Instant.now(),
                updatedAt: Instant.now()
            ))
            def tracing = tracingRepository.save(
                new TracingEntity(
                    vehicle:vehicle,
                    createdAt: Instant.now()
                )
            )
            def lastTrace = lastTracingRepository.save(new LastTraceEntity(
                vehicle: vehicle,
                trace: tracing
            ))
            def newTrace = tracingRepository.save(new TracingEntity(
                vehicle:vehicle,
                createdAt: Instant.now().plusSeconds(120)
            ))
        when: 'the last traced is saved'
            lastTrace.setTrace(newTrace)
           def result = lastTracingRepository.updateLastTrace(lastTrace)
        then:'the result is not empty and should contain the following properties'
            result.trace.id == newTrace.id
            result.vehicle.id == newTrace.vehicle.id
    }

    def 'should fetch the last trace by vehicel id'() {
        given: 'a last trace and a vehicle'
            def vehicle = vehicleRepository.save(
                new VehicleEntity(
                    createdAt: Instant.now(),
                    updatedAt: Instant.now(),
                )
            )
            def trace = tracingRepository.save(new TracingEntity(
                vehicle: vehicle,
                createdAt: Instant.now()
            ))
            def lastTrace = lastTracingRepository.save(
                new LastTraceEntity(
                    vehicle: vehicle,
                    trace: trace
                )
            )
        when: 'the last trace is fetched using vehicel id'
            def result = lastTracingRepository.findLastTracingByVehicleId(vehicle.id)
        then: 'the result should not be empty and have the following properties'
            result.isPresent()
            result.get().vehicle.id == vehicle.id
            result.get().trace.id == trace.id
    }
}
