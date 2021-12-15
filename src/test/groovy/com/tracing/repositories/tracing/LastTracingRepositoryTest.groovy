package com.tracing.repositories.tracing

import com.tracing.entities.LastTraceEntity
import com.tracing.entities.TracingEntity
import com.tracing.entities.VehicleEntity
import com.tracing.repositories.vehicle.VehicleRepository
import com.tracing.services.scheduled.batch.BatchDataReaderBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.TestExecutionListeners
import spock.lang.Specification
import spock.lang.Subject

import javax.persistence.EntityManager
import java.time.Instant
import java.time.temporal.ChronoUnit

@DataJpaTest
@Import([TracingRepository, VehicleRepository, LastTracingRepository, BatchDataReaderBean])
class LastTracingRepositoryTest extends Specification {

    @Autowired
    @Subject
    private LastTracingRepository lastTracingRepository

    @Autowired
    private VehicleRepository vehicleRepository

    @Autowired
    private TracingRepository tracingRepository

    @Autowired EntityManager em;

    @Autowired BatchDataReaderBean batchDataReaderBean;

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

    def 'should fetch last trace before time limit' (){
        given: 'a  last trace for vehicle'
            def vehicle = vehicleRepository.save( new VehicleEntity(
                createdAt: Instant.now(),
                updatedAt: Instant.now(),
            ))
            def trace = tracingRepository.save(new TracingEntity(
                vehicle:vehicle,
                latitude: 10.0f,
                longitude: 11.0f,
                createdAt:  Instant.parse("2021-12-14T18:36:00.00Z")
            ))
         def lastTrace = lastTracingRepository.save(new LastTraceEntity(
             vehicle: vehicle,
             trace: trace
         ))
        when: 'the traces are searched by before time limit'
        def timeLimit = Instant.parse("2021-12-14T18:41:00.00Z").minus(5,ChronoUnit.MINUTES)
        def result = em.createQuery(
            batchDataReaderBean.getFindLastTraceAfterTimeLimitQuery(),
            LastTraceEntity.class)
            .setParameter("timeLimit", timeLimit)
            .getResultList()
        then: 'the result should not be empty'
        !result.isEmpty()

    }
}
