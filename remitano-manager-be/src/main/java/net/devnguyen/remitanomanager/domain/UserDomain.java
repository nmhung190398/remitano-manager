package net.devnguyen.remitanomanager.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import net.devnguyen.remitanomanager.entity.UserEntity;
import net.devnguyen.remitanomanager.webapp.request.LoginRequest;
import net.devnguyen.remitanomanager.webapp.request.RegisterRequest;

@ToString
@Getter
@Setter(AccessLevel.NONE)
@NoArgsConstructor
public class UserDomain {

    private String id;

    private String email;

    private String username;

    @JsonIgnore
    private String password;

    private String fullName;

    private boolean isActive;


    public UserDomain(UserEntity userEntity) {
        this.id = userEntity.getId();
        this.email = userEntity.getEmail();
        this.fullName = userEntity.getFullName();
        this.password = userEntity.getPassword();
        this.isActive = userEntity.isActive();
    }

    public UserDomain(RegisterRequest request) {

    }

    public UserEntity toEntity() {
        return new UserEntity();
    }


    public boolean login(LoginRequest request) {
        return false;
    }
}
