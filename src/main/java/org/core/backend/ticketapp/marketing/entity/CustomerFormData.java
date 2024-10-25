package org.core.backend.ticketapp.marketing.entity;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.*;
import org.apache.commons.lang3.ObjectUtils;
import org.core.backend.ticketapp.common.entity.AbstractBaseEntity;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "customer_form_data")
@TypeDefs({@TypeDef(name = "JSONB", typeClass = JsonBinaryType.class)})
public class CustomerFormData extends AbstractBaseEntity {
    @Column(name = "first_name", columnDefinition = "varchar(250)")
    private String firstName;
    @Column(name = "last_name", columnDefinition = "varchar(250)")
    private String lastName;
    @Column(name = "phone_number", columnDefinition = "varchar(15)")
    private String phoneNumber;
    @Email(message = "Oops! Invalid email address")
    @Column(name = "email", columnDefinition = "varchar(250)")
    private String email;
    @NotBlank(message = "dob can't be blank")
    private LocalDateTime dob;
    @NotBlank(message = "address can't be blank")
    private String address;

    @PrePersist
    public void onCreate() {
        id = ObjectUtils.defaultIfNull(id, UUID.randomUUID());
        dateCreated = ObjectUtils.defaultIfNull(dateCreated, LocalDateTime.now());
    }
}
