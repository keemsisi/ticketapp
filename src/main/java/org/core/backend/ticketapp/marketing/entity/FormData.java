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
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "form_data")
@TypeDefs({@TypeDef(name = "JSONB", typeClass = JsonBinaryType.class)})
public class FormData extends AbstractBaseEntity {
    @Column(name = "title", columnDefinition = "varchar(250)")
    private String title;
    @Column(name = "description", columnDefinition = "varchar(2500)")
    private String description;
    @Column(name = "code", columnDefinition = "varchar(250)")
    private String code;
    //define other fields to collect here

    @PrePersist
    public void onCreate() {
        id = ObjectUtils.defaultIfNull(id, UUID.randomUUID());
        dateCreated = ObjectUtils.defaultIfNull(dateCreated, LocalDateTime.now());
        code = UUID.randomUUID().toString().replace("-", "");
    }
}