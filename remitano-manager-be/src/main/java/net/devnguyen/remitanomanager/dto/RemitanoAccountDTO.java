package net.devnguyen.remitanomanager.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import net.devnguyen.remitanomanager.entity.AuditEntity;
import net.devnguyen.remitanomanager.entity.RemitanoAccount;
import net.devnguyen.remitanomanager.t3.remitano.RemitanoClient;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RemitanoAccountDTO {

    public RemitanoAccountDTO(RemitanoAccount account) {
        this.id = account.getId();
        this.userId = account.getUserId();
        this.email = account.getEmail();
        this.remitanoUsername = account.getRemitanoUsername();
        var client = account.getClient();
        this.meVndr = client.meVndr();
        this.offers = client.getOffers();
        this.earningSummary = client.earningSummary();
    }

    private Map earningSummary;

    private String id;

    private String userId;

    private String email;

    private String remitanoUsername;

    public Map meVndr;

    public List offers;
}
