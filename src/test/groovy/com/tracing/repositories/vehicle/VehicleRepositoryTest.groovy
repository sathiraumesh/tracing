package com.tracing.repositories.vehicle

import com.tracing.entities.VehicleEntity
import com.tracing.repositories.tracing.TracingRepository
import com.tracing.repositories.vehicle.VehicleRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import spock.lang.Specification
import spock.lang.Subject

import java.time.Instant

@DataJpaTest
@Import([VehicleRepository])
class VehicleRepositoryTest extends Specification {

    @Autowired
    @Subject
    private VehicleRepository vehicleRepository;

    def cleanup() {
        vehicleRepository.deleteAll();
    }

    def 'save vehicle entity'() {
        given: 'a new vehicle entity'
            def vehicle = new VehicleEntity(
                id: UUID.randomUUID(),
                createdAt: Instant.now(),
                updatedAt: Instant.now()
            )
        when: 'the user is saved'
            def result = vehicleRepository.save(vehicle);
        then: 'the result should not be null'
            result
    }

    def 'should get vehicle by vehicle id'() {
        given: 'given a vehicle'
            def vehicle = vehicleRepository.save(
                new VehicleEntity(
                    id: UUID.randomUUID(),
                    createdAt: Instant.now(),
                    updatedAt: Instant.now())
            );
        when: 'the user is saved'
            def result = vehicleRepository.findVehicleById(vehicle.id)
        then: 'the result should not be null'
            !result.isEmpty()
        and: 'the result should contain the following properties'
            result.get().id == vehicle.id
    }
}
