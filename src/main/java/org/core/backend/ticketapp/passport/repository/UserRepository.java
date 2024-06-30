package org.core.backend.ticketapp.passport.repository;

import org.core.backend.ticketapp.passport.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Transactional
@Repository
public interface UserRepository extends JpaRepository<User, UUID>, PagingAndSortingRepository<User, UUID> {


    @Query(value = "SELECT users.* FROM users " +
            " WHERE LOWER(users.email) = LOWER(?1)", nativeQuery = true)
    @Transactional(readOnly = true)
    Optional<User> findByEmail(String email);

    @Query(value = "SELECT * from users where email = ?1", nativeQuery = true)
    Optional<User> findByUsername(String email);

    @Query(value = "SELECT users.* FROM users" +
            " WHERE users.id = ?1", nativeQuery = true)
    Optional<User> findByUUID(UUID id);

    @Query(value = "SELECT u.* FROM users u " +
            " AND COALESCE(LOWER(u.last_name),'') LIKE CONCAT('%', :lastName,'%') " +
            " AND COALESCE(LOWER(u.middle_name),'') LIKE CONCAT('%', :middleName,'%') " +
            " AND COALESCE(u.email,'') LIKE CONCAT('%', :email,'%') " +
            " AND COALESCE(u.gender,'') LIKE CONCAT('%', :gender,'%') " +
            " AND COALESCE(u.phone,'') LIKE CONCAT('%', :phone,'%') ",
            nativeQuery = true)
    Page<User> listUser(String firstName,
                        String lastName,
                        String middleName,
                        String email,
                        String gender,
                        String phone,
                        Pageable pageable);

    @Query(value = "SELECT u.* FROM users u WHERE locked = true AND COALESCE(LOWER(u.first_name),'') LIKE LOWER(CONCAT('%', :firstName,'%')) " +
            " AND COALESCE(LOWER(u.last_name),'') LIKE LOWER(CONCAT('%', :lastName,'%')) " +
            " AND COALESCE(u.email,'') LIKE CONCAT('%', :email,'%') ",
            nativeQuery = true)
    Page<User> listLockedUser(String firstName,
                              String lastName,
                              String email,
                              
                              Pageable pageable);

    @Query(value = "SELECT u.* FROM users u WHERE u.locked = true AND (CAST(u.lock_date as date) = CAST(CONCAT(:dateOn) as date))",
            nativeQuery = true)
    Page<User> filterLockedUserByDateOn(String dateOn, Pageable pageable);

    @Query(value = "SELECT u.* FROM users u WHERE u.locked = true AND (CAST(u.lock_date as date) < CAST(CONCAT(:dateBefore) as date)) ",
            nativeQuery = true)
    Page<User> filterLockedUserByDateBefore(String dateBefore,  Pageable pageable);

    @Query(value = "SELECT u.* FROM users u WHERE u.locked = true AND (CAST(u.lock_date as date) > CAST(CONCAT(:dateAfter) as date)) ",
            nativeQuery = true)
    Page<User> filterLockedUserByDateAfter(String dateAfter,  Pageable pageable);

    @Query(value = "SELECT u.* FROM users u WHERE u.locked = true AND u.lock_date BETWEEN CAST(CONCAT(:startDate) as date) AND CAST(CONCAT(:endDate) as date) ",
            nativeQuery = true)
    Page<User> filterLockedUserByDateBetween(String startDate, String endDate,  Pageable pageable);

    @Query(value = "SELECT u.*, ud.name as department FROM users u " +
            " LEFT JOIN department ud ON u.department_id = ud.id " +
            " WHERE COALESCE(u.first_name,'') LIKE CONCAT('%', :firstName,'%') OR COALESCE(u.last_name,'') LIKE CONCAT('%', :lastName,'%') OR COALESCE(u.middle_name,'') LIKE CONCAT('%', :middleName,'%') ",
            nativeQuery = true)
    List<User> findUser(String firstName,
                        String lastName,
                        String middleName);


    @Query(value = "SELECT u.*, ud.name as department_name " +
            " FROM users u LEFT JOIN user_department ud ON u.department_id = ud.id ", nativeQuery = true)
    List<User> findManagers();

    @Modifying
    @Query(value = "UPDATE users set has_changed_password_after_expiry = 0, password_expiry_date = :passwordExpiryDate  ", nativeQuery = true)
    int updatePasswordExpiryDate(@Param("passwordExpiryDate") Date passwordExpiryDate);

    @Query(value = "SELECT u.* FROM users u WHERE LOWER(users.email) = LOWER(?1) AND password=?2", nativeQuery = true)
    User findByEmailAndPassword(String email, String password);
}
