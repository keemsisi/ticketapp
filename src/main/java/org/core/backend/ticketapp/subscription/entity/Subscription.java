package org.core.backend.ticketapp.subscription.entity;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.core.backend.ticketapp.common.entity.AbstractBaseEntity;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "subscription")
@TypeDefs({@TypeDef(name = "JSONB", typeClass = JsonBinaryType.class)})
public class Subscription extends AbstractBaseEntity {

    @Column(name = "vendor_id")
    private String vendorId;

    @Column(name = "vendor_link")
    private String vendorLink;

    @Column(name = "vendor_email")
    private String vendorEmail;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "code")
    private String code;
}
