package net.devnguyen.remitanomanager.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import net.devnguyen.remitanomanager.t3.remitano.RemitanoClient;
import net.devnguyen.remitanomanager.webapp.request.EnableOfferRequest;
import org.apache.commons.codec.digest.DigestUtils;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"userId", "secretKey", "accessKey"})})
public class RemitanoAccount extends AuditEntity {

    public RemitanoAccount(String userId, String secretKey, String accessKey) {
        this.id = UUID.randomUUID().toString();
        this.userId = userId;
        this.secretKey = secretKey;
        this.accessKey = accessKey;
        var me = this.getClient().me();
        this.email = me.get("email").toString();
        this.remitanoUsername = me.get("username").toString();
    }

    @Id
    private String id;

    private String userId;

    @JsonIgnore
    private String accessKey;
    @JsonIgnore
    private String secretKey;

    private String email;

    private String remitanoUsername;

    @Transient
    private RemitanoClient client;

    @JsonIgnore
    public RemitanoClient getClient() {
        if (client == null) {
            client = new RemitanoClient(secretKey, accessKey);
        }

        return client;
    }

    public Map disableOffer(String offerId) {
        return getClient().disableOffer(offerId);
    }

    public Map enableOffer(String offerId, EnableOfferRequest request) {
        var offer = getClient().getOfferEdit(offerId);
        if (offer.get("offer_type").equals("buy")) {
            offer.put("total_amount", request.getTotalAmount());
        } else {
            Double vndr = (Double) getClient().meVndr().get("available_balance");

            offer.put("total_amount", vndr);
            offer.put("sell_all_fiat_token", true);
        }
        getClient().updateOffer(offerId, offer);
        return getClient().enableOffer(offerId);
    }
}
