package net.devnguyen.remitanomanager.entity;

import lombok.*;
import net.devnguyen.remitanomanager.dto.auth.GoogleDTO;
import net.devnguyen.remitanomanager.webapp.request.RegisterRequest;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tbl_user")
public class UserEntity extends AuditEntity {
    @Id
    private String id;

    private String email;

    private String username;

    private String password;

    private String fullName;

    private boolean isActive;

    public UserEntity(RegisterRequest request, PasswordEncoder passwordEncoder) {
        this.id = UUID.randomUUID().toString();
        this.email = request.getEmail();
        this.username = request.getEmail();
        this.fullName = request.getFullName();
        this.isActive = true;
        this.password = passwordEncoder.encode(request.getPassword());
    }

    public UserEntity(GoogleDTO dto, PasswordEncoder passwordEncoder) {
        this.id = UUID.randomUUID().toString();
        this.username = dto.getEmail();
        this.email = dto.getEmail();
        this.fullName = dto.getName();
        this.isActive = true;
        this.password = passwordEncoder.encode(UUID.randomUUID().toString());
    }
}
