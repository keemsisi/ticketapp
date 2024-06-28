package org.core.backend.ticketapp.passport.repository;

import org.core.backend.ticketapp.passport.entity.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@Transactional
@Repository
public interface ClientRepository extends JpaRepository<Client, UUID>, PagingAndSortingRepository<Client, UUID> {

    @Query(value = "SELECT * FROM api_client WHERE LOWER(name) = LOWER(?1)", nativeQuery = true)
    Optional<Client> findByName(String name);

    @Query(value = "SELECT * FROM api_client ", nativeQuery = true)
    Page<Client> findAll(Pageable pageable);

    @Query(value = "SELECT * FROM api_client WHERE id = :clientId ",
            countQuery = "SELECT count(*) FROM client WHERE id = :clientId",
            nativeQuery = true)
    ArrayList<Client> findAllWithoutPaging(UUID clientId);

    @Transactional
    @Query(value = "SELECT * FROM api_client WHERE id = :clientId ",
            nativeQuery = true)
    Client getById(UUID clientId);
}
