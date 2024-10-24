package org.core.backend.ticketapp.marketing.repository;

import org.core.backend.ticketapp.marketing.entity.CustomerFormData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerFormDataRepository extends JpaRepository<CustomerFormData, UUID> {
    @Query(value = "SELECT * FROM customer_form_data WHERE user_id = ?1 AND deleted = false ", nativeQuery = true)
    Page<CustomerFormData> findAll(final @NotNull UUID userId, final @NotNull Pageable pageable);

    @Query(value = "SELECT * FROM customer_form_data WHERE id = ?1 AND user_id = ?2 AND deleted = false ", nativeQuery = true)
    Optional<CustomerFormData> findById(UUID id, UUID userId);
}
