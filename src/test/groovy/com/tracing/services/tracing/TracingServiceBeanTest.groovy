package com.tracing.services.tracing

import com.tracing.config.TracingProperties
import com.tracing.entities.LastTraceEntity
import com.tracing.entities.TracingEntity
import com.tracing.entities.VehicleEntity
import com.tracing.exceptions.ValidationException
import com.tracing.repositories.tracing.LastTracingRepository
import com.tracing.repositories.tracing.TracingRepository
import com.tracing.repositories.vehicle.VehicleRepository
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import spock.lang.Specification
import spock.lang.Subject

import java.time.Instant

class TracingServiceBeanTest extends Specification {

    private VehicleRepository vehicleRepository = Mock()
    private TracingRepository tracingRepository = Mock()
    private LastTracingRepository lastTracingRepository = Mock()
    private TracingProperties tracingProperties = new TracingProperties(timeInterval: 30)

    @Subject
    private TracingService tracingService = new TracingServiceBean(vehicleRepository, tracingRepository, lastTracingRepository, tracingProperties)

    def 'should trace the location if no duplicate tracing are found'() {
        given: 'vehicle id and the coordinates of the location'
            def vehicleId = UUID.fromString('4f761b42-9dd7-460b-a548-f8ec5836737d')
            def lon = 10.0f
            def lat = 20.0f
        when: 'the trace vehicle location is called'
            tracingService.traceVehicleLocation(vehicleId, lon, lat)
        then: 'the following behaviour is expected'
            1 * vehicleRepository.findVehicleById(vehicleId) >> {
                Optional.of(
                    new VehicleEntity(
                        id: vehicleId,
                        createdAt: Instant.now(),
                        updatedAt: Instant.now()
                    )
                )
            }
            1 * tracingRepository.findDuplicateTraces(vehicleId, lon, lat, _) >> {
                Optional.empty()
            }
            1* lastTracingRepository.findLastTracingByVehicleId(vehicleId) >> {
                Optional.of(
                    new LastTraceEntity(
                        vehicle: new VehicleEntity(id:vehicleId),
                        trace: new TracingEntity()
                    )
                )
            }
            1 * tracingRepository.save(_)
            1 * lastTracingRepository.updateLastTrace(_)
            0 * _
    }

    def 'should trace the location if no duplicate tracing are found and should insert last trace log if not found'() {
        given: 'vehicle id and the coordinates of the location'
            def vehicleId = UUID.fromString('4f761b42-9dd7-460b-a548-f8ec5836737d')
            def lon = 10.0f
            def lat = 20.0f
        when: 'the trace vehicle location is called'
            tracingService.traceVehicleLocation(vehicleId, lon, lat)
        then: 'the following behaviour is expected'
            1 * vehicleRepository.findVehicleById(vehicleId) >> {
                Optional.of(
                    new VehicleEntity(
                        id: vehicleId,
                        createdAt: Instant.now(),
                        updatedAt: Instant.now()
                    )
                )
            }
            1 * tracingRepository.findDuplicateTraces(vehicleId, lon, lat, _) >> {
                Optional.empty()
            }
            1* lastTracingRepository.findLastTracingByVehicleId(vehicleId) >> {
                Optional.empty()
            }
            1 * tracingRepository.save(_)
            1 * lastTracingRepository.save(_)
            0 * _
    }

    def 'should throw a validation error if vehicle is not found'() {
        given: 'vehicle id and the coordinates of the location'
            def vehicleId = UUID.fromString('4f761b42-9dd7-460b-a548-f8ec5836737d')
            def lon = 10.0f
            def lat = 20.0f
        when: 'the trace vehicle location is called'
            tracingService.traceVehicleLocation(vehicleId, lon, lat)
        then: 'the following behaviour is expected'
            1 * vehicleRepository.findVehicleById(vehicleId) >> {
                Optional.empty()
            }
        and: 'the following exception is thrown'
            def e = thrown(ValidationException)
            e.message.trim() != ''
    }

    def 'should not trace position if duplicate entries are found with in the time limit'() {
        given: 'vehicle id and the coordinates of the location'
            def vehicleId = UUID.fromString('4f761b42-9dd7-460b-a548-f8ec5836737d')
            def lon = 10.0f
            def lat = 20.0f
            def currentInstant = Instant.now()
        when: 'the trace vehicle location is called'
            tracingService.traceVehicleLocation(vehicleId, lon, lat)
        then: 'the following behaviour is expected'
            1 * vehicleRepository.findVehicleById(vehicleId) >> {
                Optional.of(
                    new VehicleEntity(
                        id: vehicleId,
                        createdAt: Instant.now(),
                        updatedAt: Instant.now()
                    )
                )
            }
            1 * tracingRepository.findDuplicateTraces(_, _, _, _) >> {
                Optional.of(
                    new TracingEntity(
                        id: 1,
                        longitude: 11f,
                        latitude: 12f,
                        createdAt: Instant.now().minusSeconds(20)
                    )
                )
            }
            0 * _
    }
}
