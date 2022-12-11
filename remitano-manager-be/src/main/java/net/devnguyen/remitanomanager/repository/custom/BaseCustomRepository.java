package net.devnguyen.remitanomanager.repository.custom;

import net.devnguyen.remitanomanager.repository.query.BaseSearchQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BaseCustomRepository<T,Query extends BaseSearchQuery>{
    Page<T> search(Query query, Pageable pageable);
    List<T> gets(Query query);
    long count(Query query);
}
