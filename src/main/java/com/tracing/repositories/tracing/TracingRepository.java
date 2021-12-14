package com.tracing.repositories.tracing;

import com.tracing.entities.TracingEntity;

import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import lombok.RequiredArgsConstructor;

@Repository
@Transactional
@RequiredArgsConstructor
public class TracingRepository {
    private final EntityManager em;

    public TracingEntity save(TracingEntity trace) {
        em.persist(trace);
        return trace;
    }

    public Optional<TracingEntity> findLastTraceByVehicleId(UUID vehicleId) {
        return em.createQuery(" select t from TracingEntity t " +
                    " inner join t.vehicle v " +
                    " where v.id = :vehicleId " +
                    " order by t.createdAt desc ",
                TracingEntity.class)
            .setParameter("vehicleId", vehicleId)
            .setHint("hibernate.query.passDistinctThrough", false)
            .setMaxResults(1)
            .getResultList().stream()
            .findFirst();
    }

    public Optional<TracingEntity> findDuplicateTraces(UUID vehicleId, float longitude, float latitude, Instant createdAt) {
        return em.createQuery(" select t from TracingEntity t " +
                    " inner join t.vehicle v " +
                    " where v.id =:vehicleId " +
                    " and t.longitude = :longitude " +
                    " and t.latitude = :latitude " +
                    " and t.createdAt > :createdAt " +
                    "order by t.createdAt desc ",
                TracingEntity.class)
            .setParameter("vehicleId", vehicleId)
            .setParameter("longitude", longitude)
            .setParameter("latitude", latitude)
            .setParameter("createdAt", createdAt)
            .setHint("hibernate.query.passDistinctThrough", false)
            .setMaxResults(1)
            .getResultList().stream()
            .findFirst();
    }

    public void deleteAll() {
        em.createQuery("delete from TracingEntity");
    }
}
