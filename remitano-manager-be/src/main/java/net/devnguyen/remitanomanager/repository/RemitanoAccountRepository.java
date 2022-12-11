package net.devnguyen.remitanomanager.repository;

import net.devnguyen.remitanomanager.entity.RemitanoAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RemitanoAccountRepository extends JpaRepository<RemitanoAccount, String> {

    List<RemitanoAccount> findByUserId(String userId);
    Optional<RemitanoAccount> findBySecretKeyAndAccessKey(String secretKey,String accessKey);
}
