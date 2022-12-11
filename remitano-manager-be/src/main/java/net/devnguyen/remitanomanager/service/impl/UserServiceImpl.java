package net.devnguyen.remitanomanager.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devnguyen.remitanomanager.configuration.security.jwt.JWTToken;
import net.devnguyen.remitanomanager.configuration.security.jwt.JwtTokenProvider;
import net.devnguyen.remitanomanager.domain.UserDomain;
import net.devnguyen.remitanomanager.dto.auth.BasicAuthority;
import net.devnguyen.remitanomanager.dto.auth.GoogleDTO;
import net.devnguyen.remitanomanager.entity.UserEntity;
import net.devnguyen.remitanomanager.exception.errorcode.AccessDeniedError;
import net.devnguyen.remitanomanager.exception.errorcode.InternalServerError;
import net.devnguyen.remitanomanager.exception.errorcode.NotFoundException;
import net.devnguyen.remitanomanager.repository.UserRepository;
import net.devnguyen.remitanomanager.service.UserService;
import net.devnguyen.remitanomanager.service.auth.GoogleService;
import net.devnguyen.remitanomanager.webapp.request.LoginRequest;
import net.devnguyen.remitanomanager.webapp.request.RegisterRequest;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final GoogleService googleService;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

//    @Override
//    public PagingResponse<UserDomain> search(UserSearchRequest request) {
//        var keyword = request.getKeyword() == null ? "" : request.getKeyword().toLowerCase().trim();
//        var page = this.userRepository.search(keyword, request.pageable());
//
//        return PagingResponse.of(page, this::enrich);
//    }
//
//    @Override
//    @Transactional
//    public UserDomain add(UserAddOrChangeRequest request) {
//        this.userRepository.findByUsername(request.getUsername()).ifPresent((user) -> {
//            throw new ResponseException("Tài khoản bị trùng");
//        });
//
//        var user = this.userMapper.toEntity(request);
//        user.setPassword(this.defaultPassword);
//        user.setRoot(false);
//        user.setDeleted(false);
//        user.setIsActive(true);
//        user = this.userRepository.save(user);
//        var roles = this.userMapper.toNewRole(request.getRoleTypes(), user.getId());
//        this.roleRepository.saveAll(roles);
//
//
//        return this.userMapper.toDomain(user);
//    }
//
//    @Override
//    @Transactional
//    public UserDomain change(String id, UserAddOrChangeRequest request) {
//        var user = this.userRepository.findById(id)
//                .orElseThrow(() -> new ResponseException("Không tìm thấy người dùng"));
//
//        var roleDelete = this.roleRepository.findAllByUserId(user.getId());
//        this.roleRepository.deleteAll(roleDelete);
//
////        user.setPassword(this.defaultPassword);
//        user.setFullName(request.getFullName());
//
//        var roles = this.userMapper.toNewRole(request.getRoleTypes(), user.getId());
//        this.roleRepository.saveAll(roles);
//
//        return this.userMapper.toDomain(this.userRepository.save(user));
//    }
//

    public UserDomain getDetail(String id) {
        return this.userRepository.findById(id)
                .map(this::enrich)
                .orElseThrow(NotFoundException.USER_NOT_FOUND::exception);
    }

    private UserDomain enrich(UserEntity userEntity) {
        var user = new UserDomain(userEntity);
        return user;
    }
//
//    @Override
//    @Transactional
//    public UserDomain changeMyPassword(String currentPassword, String newPassword) {
//        var user = super.getUserInfo();
//        var userEntity = this.userRepository.findByUsername(user.getUsername())
//                .orElseThrow(() -> new ResponseException("Không tìm thấy user"));
//        if (!this.passwordEncoder.matches(userEntity.getPassword(), currentPassword)) {
//            throw new ResponseException("Mật khẩu không đung");
//        }
//
//        userEntity.setPassword(newPassword);
//        return this.enrich(this.userRepository.save(userEntity));
//    }
//
//    @Override
//    @Transactional
//    public void delete(String userId) {
//        var user = this.userRepository.findById(userId)
//                .orElseThrow(() -> new ResponseException("Không tìm thấy người dùng"));
//
//        this.userRepository.delete(user);
//    }x


    public JWTToken login(LoginRequest request) {
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(AccessDeniedError.USER_NOT_FOUND::exception);

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw AccessDeniedError.PASSWORD_WRONG.exception();
        }
        if (!user.isActive()) {
            throw AccessDeniedError.USER_NOT_ACTIVE.exception();
        }

        return jwtTokenProvider.generateToken(user.getUsername());
    }


    public JWTToken loginWithGoogle(String code) {
        System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");
        try {
            String accessToken = googleService.getToken(code);
            GoogleDTO dto = googleService.getUserInfo(accessToken);
            var userOp = userRepository.findByEmail(dto.getEmail());
            UserEntity userEntity = null;
            if (userOp.isEmpty()) {
                userEntity = new UserEntity(dto, passwordEncoder);

                userEntity = userRepository.save(userEntity);
            } else {
                userEntity = userOp.get();
            }

            return jwtTokenProvider.generateToken(userEntity.getUsername());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw InternalServerError.INTERNAL_SERVER_ERROR.exception(e.getMessage());
        }
    }

    @Cacheable(cacheNames = "AUTHORITY_USERNAME",key = "#username")
    public BasicAuthority getUserAuthority(String username) {
        var user = userRepository.findByEmail(username)
                .orElseThrow(AccessDeniedError.ACCESS_DENIED::exception);

        return BasicAuthority.builder()
                .dataAuthorities(new HashSet<BasicAuthority.DataAuthority>())
                .scopes(new HashSet<>())
                .userId(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .departmentId(new HashSet<>())
                .isRoot(false)
                .build();
    }

    public void register(RegisterRequest request) {
        userRepository.findByEmail(request.getEmail())
                .ifPresent(x -> {
                    throw AccessDeniedError.EMAIL_EXISTED.exception();
                });

        var user = new UserEntity(request, passwordEncoder);
        userRepository.save(user);
    }
}
