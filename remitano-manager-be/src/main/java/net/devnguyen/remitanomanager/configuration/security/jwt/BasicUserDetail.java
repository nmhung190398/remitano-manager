package net.devnguyen.remitanomanager.configuration.security.jwt;//package net.devnguyen.security.jwt;
//
//import net.devnguyen.entity.ScopeEntity;
//import net.devnguyen.entity.UserEntity;
//import net.devnguyen.ulti.HnmUtil;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import java.util.*;
//import java.util.stream.Collectors;
//
//public class BasicUserDetail implements UserDetails {
//
//    private String username;
//    private String password;
//    private Set<SimpleGrantedAuthority> scopes;
//    private Boolean isRoot;
//
//    public BasicUserDetail(UserEntity user, Set<ScopeEntity> scopes) {
//        this.username = user.getUsername();
//        this.password = user.getPassword();
//        this.isRoot = user.getIsRoot();
//        this.scopes = HnmUtil.getOrDefault(scopes, new HashSet<ScopeEntity>()).stream().map(ScopeEntity::getScope)
//                .map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
//    }
//
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return HnmUtil.getOrDefault(scopes, new HashSet<>());
//    }
//
//    @Override
//    public String getPassword() {
//        return password;
//    }
//
//    @Override
//    public String getUsername() {
//        return username;
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return true;
//    }
//}
