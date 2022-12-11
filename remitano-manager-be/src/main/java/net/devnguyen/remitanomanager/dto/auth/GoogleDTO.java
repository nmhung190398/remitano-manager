package net.devnguyen.remitanomanager.dto.auth;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GoogleDTO implements Serializable {
    private String id;

    private String iss;
    private String sub;
    private String azp;
    private String aud;
    private String iat;
    private String exp;
    private String at_hash;
    private String alg;
    private String kid;
    private String typ;
    private String nonce;
    private String email;
    private boolean verified_email;
    private boolean email_verified;
    private String name;
    private String picture;
    private String given_name;
    private String family_name;
    private String link;
    private String phones;
    private String gender;
    private String birthday;

    private String locale;

    public GoogleDTO() {
    }

}
