package net.devnguyen.remitanomanager.repository.custom.impl;

import net.devnguyen.remitanomanager.repository.custom.BaseCustomRepository;
import net.devnguyen.remitanomanager.repository.query.BaseSearchQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class BaseCustomRepositoryImpl<T, Q extends BaseSearchQuery> implements BaseCustomRepository<T, Q> {

    private final EntityManager entityManager;
    private final String keyName;
    private final Class entityType;
    private final String SQL_SELECT;
    private final String SQL_COUNT;

    public BaseCustomRepositoryImpl(EntityManager entityManager, String keyName, Class entityType) {
        this.entityManager = entityManager;
        this.keyName = keyName;
        this.entityType = entityType;
        this.SQL_SELECT = MessageFormat.format("select {0} from {1} {0} ", this.keyName, this.entityType.getName());
        this.SQL_COUNT = MessageFormat.format("select count({0}) from {1} {0} ", this.keyName, this.entityType.getName());
    }


    protected Query buildQuery(String sql, Q query, Sort sort, Class<?> type) {
        Map<String, Object> values = new HashMap<>();
        sql += this.createJoinQuery(query);
        sql += this.createWhereQuery(query, values);
        sql += this.createOrderQuery(sort);
        Query _query = this.entityManager.createQuery(sql, type);
        values.forEach(_query::setParameter);
        return _query;
    }


    protected String createOrderQuery(Sort sort) {
        if (sort == null || sort.equals(Sort.unsorted())) {
            return "";
        }

        //default order
        String orders = sort.stream()
                .map(order -> MessageFormat.format(" {0}.{1} {2} ", keyName, order.getProperty(), order.getDirection()))
                .collect(Collectors.joining(","));

        return " order by " + orders;
    }

    protected String createWhereQuery(Q query, Map<String, Object> values) {
        String sql = " WHERE 1 = 1 ";
        if (Objects.nonNull(query.getIds())) {
            sql += MessageFormat.format(" and {0}.id in :ids ", keyName);
            values.put("ids", query.getIds());
        }
        return sql;
    }

    protected String createJoinQuery(Q query) {
        return "";
    }

    @Override
    public Page<T> search(Q query, Pageable pageable) {
        String sql = this.SQL_SELECT;
        Query _query = this.buildQuery(sql, query, pageable.getSort(), this.entityType);

        if (!pageable.equals(Pageable.unpaged())) {
            _query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
            _query.setMaxResults(pageable.getPageSize());
        }
        List<T> list = _query.getResultList();
        long count = this.count(query);
        return new PageImpl<T>(list, pageable, count);

    }

    @Override
    public List<T> gets(Q query) {
        String sql = this.SQL_SELECT;
        Query _query = this.buildQuery(sql, query, Sort.unsorted(), this.entityType);
        List<T> list = _query.getResultList();
        return list;

    }

    @Override
    public long count(Q query) {
        String sql = this.SQL_COUNT;
        Query _query = this.buildQuery(sql, query, Sort.unsorted(), Long.class);
        Long count = (Long) _query.getSingleResult();
        return count;
    }
}
