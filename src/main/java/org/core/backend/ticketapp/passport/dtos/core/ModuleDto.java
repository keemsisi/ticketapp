package org.core.backend.ticketapp.passport.dtos.core;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.joda.time.DateTime;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.UUID;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ModuleDto {
    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "created_on", columnDefinition="TIMESTAMP default CURRENT_TIMESTAMP")
    private DateTime createdOn = new DateTime();

    @Column(name = "modified_on", columnDefinition="TIMESTAMP default CURRENT_TIMESTAMP")
    private DateTime modifiedOn = new DateTime();

    @Column(name = "created_by")
    private UUID createdBy;

    @Column(name = "modified_by")
    private UUID modifiedBy;

    @Column(name = "is_deleted")
    private boolean isDeleted;

}
