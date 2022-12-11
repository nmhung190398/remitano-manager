package net.devnguyen.remitanomanager.webapp.rest;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.devnguyen.remitanomanager.dto.RemitanoAccountDTO;
import net.devnguyen.remitanomanager.entity.RemitanoAccount;
import net.devnguyen.remitanomanager.exception.ResponseException;
import net.devnguyen.remitanomanager.exception.errorcode.BadRequestError;
import net.devnguyen.remitanomanager.exception.errorcode.NotFoundException;
import net.devnguyen.remitanomanager.repository.RemitanoAccountRepository;
import net.devnguyen.remitanomanager.service.auth.SecurityService;
import net.devnguyen.remitanomanager.webapp.request.EnableOfferRequest;
import net.devnguyen.remitanomanager.webapp.request.RemitanoAccountCreateRequest;
import org.apache.logging.log4j.core.util.ExecutorServices;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/remitano-accounts")
@Log4j2
public class RemitanoAccountResource {
    private final RemitanoAccountRepository repository;

    private final SecurityService securityService;

    @GetMapping("")
    public List<RemitanoAccount> getAccounts() {
        return repository.findByUserId(securityService.getUserIdOrNull());
    }

    @GetMapping("/{id}")
    public RemitanoAccountDTO getAccount(@PathVariable String id) {
        return repository.findById(id).filter(x -> x.getUserId().equals(securityService.getUserIdOrNull())).map(RemitanoAccountDTO::new).orElseThrow(NotFoundException.RECORD_NOT_FOUND::exception);
    }

    @PostMapping
    public RemitanoAccount create(@RequestBody RemitanoAccountCreateRequest request) {
        var userId = securityService.getUserIdOrNull();
        repository.findBySecretKeyAndAccessKey(request.getSecretKey(), request.getAccessKey())
                .filter(x -> x.getUserId().equals(userId))
                .ifPresent(x -> {
                    throw BadRequestError.DUPLICATE_ACCOUNT.exception();
                });

        var account = new RemitanoAccount(userId, request.getSecretKey(), request.getAccessKey());
        return repository.save(account);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        var entity = repository.findById(id).filter(x -> x.getUserId().equals(securityService.getUserIdOrNull())).orElseThrow(NotFoundException.RECORD_NOT_FOUND::exception);
        repository.delete(entity);
    }


    @PutMapping("/{id}/disable-offer/{offerId}")
    public Object disableOffer(@PathVariable String id, @PathVariable String offerId) {
        var userId = securityService.getUserIdOrNull();
        var account = repository.findById(id)
                .filter(x -> x.getUserId().equals(userId))
                .orElseThrow(NotFoundException.RECORD_NOT_FOUND::exception);

        return account.disableOffer(offerId);
    }


    @PutMapping("/{id}/enable-offer/{offerId}")
    public Object enableOffer(@PathVariable String id, @PathVariable String offerId, @RequestBody EnableOfferRequest request) {
        var userId = securityService.getUserIdOrNull();
        var account = repository.findById(id)
                .filter(x -> x.getUserId().equals(userId))
                .orElseThrow(NotFoundException.RECORD_NOT_FOUND::exception);

        return account.enableOffer(offerId, request);
    }


}
