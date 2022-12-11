package net.devnguyen.remitanomanager.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BasicAuthority {
    private String userId;
    private String username;
    private String email;
    private String fullName;
    private Boolean isRoot;
    private Set<String> scopes;
    private Set<DataAuthority> dataAuthorities;
    private Set<String> departmentId;


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DataAuthority {
        private String scope;
        private String objectId;
    }
}
