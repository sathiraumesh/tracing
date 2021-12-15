package com.tracing.repositories.tracing;

import com.tracing.entities.LastTraceEntity;

import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import lombok.RequiredArgsConstructor;

@Repository
@Transactional
@RequiredArgsConstructor
public class LastTracingRepository {
    private final EntityManager em;

    public LastTraceEntity updateLastTrace(LastTraceEntity trace) {
        em.merge(trace);
        return trace;
    }

    public LastTraceEntity save(LastTraceEntity trace) {
        em.persist(trace);
        return trace;
    }

    public Optional<LastTraceEntity> findLastTracingByVehicleId(UUID vehicleId) {
        return em.createQuery(" select l from LastTraceEntity l " +
                    " inner join l.vehicle v " +
                    " left join l.trace t    " +
                    " where v.id = :vehicleId ",
                LastTraceEntity.class)
            .setParameter("vehicleId", vehicleId)
            .setHint("hibernate.query.passDistinctThrough", false)
            .setMaxResults(1)
            .getResultList().stream()
            .findFirst();
    }

    public void deleteAll() {
        em.createQuery("delete from LastTraceEntity");
    }
}
