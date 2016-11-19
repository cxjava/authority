package com.chenxin.authority.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * user info table
 */
@Entity
@Table(name = "t_base_user")
@Data
public class BaseUser extends IdEntity {

    @NotBlank
    private String account;
    @NotBlank
    @JSONField(serialize = false)
    private String password;
    /**
     * nick name
     */
    private String realName;
    private Integer sex;
    private String email;
    private String mobile;
    private String officePhone;
    /**
     * count login error times
     */
    private Integer errorCount;
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @JSONField(format = "yyyy-MM-dd HH:mm")
    private Date lastLoginTime;
    private String lastLoginIp;
    private String remark;


}