package com.tracing.repositories.tracing

import com.tracing.entities.VehicleEntity
import com.tracing.entities.TracingEntity
import com.tracing.repositories.tracing.TracingRepository

import com.tracing.repositories.vehicle.VehicleRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import spock.lang.Specification
import spock.lang.Subject

import java.time.Instant

@DataJpaTest
@Import([TracingRepository, VehicleRepository])
class TracingRepositoryTest extends Specification {

    @Autowired
    @Subject
    private TracingRepository tracingRepository

    @Autowired
    VehicleRepository vehicleRepository

    def cleanup() {
        tracingRepository.deleteAll()
        vehicleRepository.deleteAll()
    }

    def 'should save a vehicle trace entity'() {
        given: 'a vehicle entity and vehicle tracing entity'
            def vehicle = vehicleRepository.save(
                new VehicleEntity(
                createdAt: Instant.now(),
                updatedAt: Instant.now())
            )
            def tracingDetail = new TracingEntity(
                vehicle: vehicle,
                createdAt: Instant.now()
            )
        when: 'the vehicle tracing is saved'
            def result = tracingRepository.save(tracingDetail)
        then: 'result should not be null'
            result
    }

    def 'should fetch the latest tracing details for a vehicle'() {
        given: 'a vehicle and ad a list of tracing records'
            def vehicle = vehicleRepository.save(
                new VehicleEntity(
                    createdAt: Instant.now(),
                    updatedAt: Instant.now())
            )
            def tracingDetails = [
                new TracingEntity(
                    vehicle: vehicle,
                    latitude: 10.0,
                    longitude: 10.0,
                    createdAt: Instant.parse("2021-12-14T18:35:24.00Z")
                ),
                new TracingEntity(
                    latitude: 10.0,
                    longitude: 10.0,
                    vehicle: vehicle,
                    createdAt: Instant.parse("2021-12-14T18:35:36.00Z")
                ),
                new TracingEntity(
                    latitude: 10.0,
                    longitude: 10.0,
                    vehicle: vehicle,
                    createdAt: Instant.parse("2021-12-14T18:35:35.00Z")
                ),
            ]
        tracingDetails.each {
            tracingRepository.save(it)
        }
        when:'the latest tracing detail is called'
            def result = tracingRepository.findLastTraceByVehicleId(vehicle.id)
        then:'the result should not be null'
            !result.isEmpty()
        and:'the result should have the following details'
            result.get().createdAt == tracingDetails[1].createdAt
    }

    def 'should fetch duplicate tracing with less than time difference in seconds'() {
        given: 'a vehicle and ad a list of tracing records and time difference in seconds'
            def timeDifferanceInSeconds = 30;
            def vehicle = vehicleRepository.save(
                new VehicleEntity(
                    createdAt: Instant.now(),
                    updatedAt: Instant.now())
            )
            def tracingDetails = [
                new TracingEntity(
                    vehicle: vehicle,
                    latitude: 10.0f,
                    longitude: 10.0f,
                    createdAt: Instant.parse("2021-12-14T18:35:24.00Z")
                ),
                new TracingEntity(
                    latitude: 10.0f,
                    longitude: 10.0f,
                    vehicle: vehicle,
                    createdAt: Instant.parse("2021-12-14T18:35:36.00Z")
                ),
                new TracingEntity(
                    latitude: 10.0f,
                    longitude: 10.0f,
                    vehicle: vehicle,
                    createdAt: Instant.parse("2021-12-14T18:35:35.00Z")
                ),
                new TracingEntity(
                    latitude: 10.0f,
                    longitude: 10.0f,
                    vehicle: vehicle,
                    createdAt: Instant.parse("2021-12-14T18:35:41.00Z")
                ),
                new TracingEntity(
                    latitude: 20.0f,
                    longitude: 10.0f,
                    vehicle: vehicle,
                    createdAt: Instant.parse("2021-12-14T18:35:41.00Z")
                ),
            ]
            tracingDetails.each {
                tracingRepository.save(it)
            }
        when:'the latest tracing detail is called'
            def result = tracingRepository
                .findDuplicateTraces(vehicle.id, 10.0f, 10.0f,
                    Instant.parse("2021-12-14T18:36:00.00Z")
                    .minusSeconds(30) )
        then:'the result should not be null'
            !result.isEmpty()
        and:'the result should have the following details'
            result.get().createdAt == tracingDetails[3].createdAt
    }
}
