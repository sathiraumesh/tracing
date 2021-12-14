package com.tracing.repositories.vehicle;

import com.tracing.entities.VehicleEntity;

import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import lombok.RequiredArgsConstructor;

@Repository
@Transactional
@RequiredArgsConstructor
public class VehicleRepository  {
    private final EntityManager em;

    public VehicleEntity save(VehicleEntity vehicle) {
        em.persist(vehicle);
        return vehicle;
    }

    public Optional<VehicleEntity> findVehicleById(UUID id) {
        return em.createQuery(" select v from VehicleEntity  v " +
                    " where v.id = :id ",
                VehicleEntity.class)
            .setParameter("id", id)
            .setHint("hibernate.query.passDistinctThrough", false)
            .setMaxResults(1)
            .getResultList().stream()
            .findFirst();
    }

    public void deleteAll() {
        em.createQuery("delete from VehicleEntity");
    }
}
