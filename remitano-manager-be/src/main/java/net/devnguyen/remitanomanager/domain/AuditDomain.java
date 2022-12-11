package net.devnguyen.remitanomanager.domain;

import lombok.Data;
import net.devnguyen.remitanomanager.entity.AuditEntity;

import java.io.Serializable;
import java.time.Instant;

/**
 * @author nmhung-evotek on 3/13/2021
 */
@Data
public class AuditDomain implements Serializable {


    private Instant createdAt;

    private Instant updatedAt;

    private String createdBy;

    private String updatedBy;

    public AuditDomain(AuditEntity auditEntity) {
        this.createdAt = auditEntity.getCreatedAt();
        this.updatedAt = auditEntity.getUpdatedAt();
        this.createdBy = auditEntity.getCreatedBy();
        this.updatedBy = auditEntity.getUpdatedBy();
    }

    public AuditDomain() {
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }
}
