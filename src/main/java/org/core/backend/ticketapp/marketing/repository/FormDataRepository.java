package org.core.backend.ticketapp.marketing.repository;

import org.core.backend.ticketapp.marketing.entity.CustomerFormData;
import org.core.backend.ticketapp.marketing.entity.FormData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FormDataRepository extends JpaRepository<FormData, UUID> {
    @Query(value = "SELECT * FROM form_data WHERE user_id = ?1 AND deleted = false ", nativeQuery = true)
    Page<FormData> findAll(final @NotNull UUID userId, final @NotNull Pageable pageable);

    @Query(value = "SELECT * FROM form_data WHERE id = ?1 AND user_id = ?2 AND deleted = false ", nativeQuery = true)
    Optional<FormData> findById(UUID id, UUID userId);
}
