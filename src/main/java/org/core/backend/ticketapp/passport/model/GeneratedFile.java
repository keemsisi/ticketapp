package org.core.backend.ticketapp.passport.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import java.util.UUID;


@Entity
@Table(name = "generated_file")
@Data
public class GeneratedFile {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "name")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String name;

    @Column(name = "date_generated")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date dateGenerated;

    @Column(name = "download_location")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String downloadLocation;

    @Column(name = "type")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String type;

    @Column(name = "service")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String service;

    @Column(name = "generated_by")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String generatedBy;
}
