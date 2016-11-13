package com.chenxin.authority.util;

import com.chenxin.authority.entity.ExtPager;
import com.chenxin.authority.entity.SearchFilter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;

import javax.persistence.criteria.*;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

public class JpaTools {
    private static final ConversionService conversionService = new DefaultConversionService();

    protected JpaTools() {
    }

    /**
     * create page request
     *
     * @param pager  extjs object
     * @param define " field desc ,sort asc "
     * @return
     */
    public static PageRequest getPageRequest(ExtPager pager, String define) {
        if (null == pager.getLimit() || null == pager.getStart()) {
            return new PageRequest(0, Integer.MAX_VALUE, JpaTools.getSort(pager, define));
        } else {
            return new PageRequest(Integer.valueOf(pager.getStart() / pager.getLimit()), pager.getLimit(), JpaTools.getSort(pager, define));
        }
    }

    /**
     * long sort string
     *
     * @param define " field DESC ,sort ASC "
     * @return
     */
    public static Sort getSort(ExtPager pager, String define) {
        Sort sort = null;
        // 排序信息
        if (StringUtils.isNotBlank(define)) {
            String[] parts = StringUtils.split(define, ',');
            List<Order> orders = Lists.newArrayList();
            for (String part : parts) {
                String[] r = StringUtils.split(part, ' ');
                orders.add(new Order(Direction.fromString(r[1]), r[0]));
            }
            sort = new Sort(orders);
        } else if (StringUtils.isNotBlank(pager.getDir()) && StringUtils.isNotBlank(pager.getSort())) {
            sort = new Sort(Direction.fromString(pager.getDir()), pager.getSort());
        } else {
            sort = new Sort(Direction.ASC, "id");
        }
        return sort;
    }

    /**
     * map to searchFilter
     *
     * @param searchParams key->EQ_userName key->LIKE_title
     * @return
     */
    public static Map<String, SearchFilter> parse(Map<String, Object> searchParams) {
        Map<String, SearchFilter> filters = Maps.newHashMap();

        for (Entry<String, Object> entry : searchParams.entrySet()) {
            // 过滤掉空值
            Object value = entry.getValue();
            if (value == null || StringUtils.isBlank(value.toString())) {
                continue;
            }
            String[] names = StringUtils.split(entry.getKey(), "_");
            SearchFilter filter = null;
            if (names.length == 2) {
                filter = new SearchFilter(names[1], SearchFilter.Operator.valueOf(names[0].toUpperCase(Locale.US)), value);
            } else if (names.length == 1) {
                //deal with this condition:parameters.put("DIStINCT", Any Object);
                if ("DISTINCT".equalsIgnoreCase(names[0])) {
                    filter = new SearchFilter(names[0], SearchFilter.Operator.DISTINCT, names[0]);
                } else {
                    filter = new SearchFilter(names[0], value);
                }
            } else if (names.length == 0 || names.length > 2) {
                throw new IllegalArgumentException(entry.getKey() + " is not a valid search filter name");
            }
            filters.put(filter.getFieldName(), filter);
        }

        return filters;
    }

    public static <T> Specification<T> getSpecification(Map<String, Object> parameters, final Class<T> clazz) {
        return JpaTools.getSpecification(JpaTools.parse(parameters).values(), clazz);
    }

    public static <T> Specification<T> getSpecification(final Collection<SearchFilter> filters, final Class<T> clazz) {
        return new Specification<T>() {
            @SuppressWarnings({"rawtypes", "unchecked"})
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                if (!CollectionUtils.isEmpty(filters)) {

                    List<Predicate> predicates = Lists.newArrayList();
                    for (SearchFilter filter : filters) {
                        //deal with this condition:parameters.put("DIStINCT", Any Object);
                        if (filter.getOperator().equals(SearchFilter.Operator.DISTINCT)) {
                            query.distinct(true);
                            continue;
                        }
                        // nested path translate, 如Task的名为"user.name"的filedName,
                        // 转换为Task.user.name属性
                        String[] names = StringUtils.split(filter.getFieldName(), ".");
                        Path expression = root.get(names[0]);
                        for (int i = 1; i < names.length; i++) {
                            expression = expression.get(names[i]);
                        }

                        // convert value from string to target type
                        Class attributeClass = expression.getJavaType();
                        if (!attributeClass.equals(String.class) && filter.getValue() instanceof String
                                && conversionService.canConvert(String.class, attributeClass)) {
                            filter.setValue(conversionService.convert(filter.getValue(), attributeClass));
                        }

                        // logic operator
                        switch (filter.getOperator()) {
                            case EQ:
                                predicates.add(builder.equal(expression, filter.getValue()));
                                break;
                            case LIKE:
                                predicates.add(builder.like(expression, "%" + filter.getValue() + "%"));
                                break;
                            case GT:
                                predicates.add(builder.greaterThan(expression, (Comparable) filter.getValue()));
                                break;
                            case LT:
                                predicates.add(builder.lessThan(expression, (Comparable) filter.getValue()));
                                break;
                            case GTE:
                                predicates.add(builder.greaterThanOrEqualTo(expression, (Comparable) filter.getValue()));
                                break;
                            case LTE:
                                predicates.add(builder.lessThanOrEqualTo(expression, (Comparable) filter.getValue()));
                                break;
                            case DISTINCT:
                                query.distinct(true);
                                break;
                        }
                    }

                    // 将所有条件用 and 联合起来
                    if (predicates.size() > 0) {
                        return builder.and(predicates.toArray(new Predicate[predicates.size()]));
                    }
                }

                return builder.conjunction();
            }
        };
    }
}
