package com.chenxin.authority.dao;

import com.chenxin.authority.entity.BaseField;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface BaseFieldRepository extends JpaRepository<BaseField, Long>, JpaSpecificationExecutor<BaseField> {

    List<BaseField> findByEnabled(Integer enabled);
}
