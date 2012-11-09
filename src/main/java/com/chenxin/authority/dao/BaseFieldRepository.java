package com.chenxin.authority.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.chenxin.authority.pojo.BaseField;

public interface BaseFieldRepository extends PagingAndSortingRepository<BaseField, Long>, JpaSpecificationExecutor<BaseField> {

	public List<BaseField> findByEnabled(Integer enabled);
}
