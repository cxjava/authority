package com.chenxin.authority.entity;

import com.alibaba.fastjson.annotation.JSONField;
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
//    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date lastLoginTime;
    private String lastLoginIp;
    private String remark;

    /**
     * @return the account
     */
    public String getAccount() {
        return account;
    }

    /**
     * @param account the account to set
     */
    public void setAccount(String account) {
        this.account = account;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the realName nick name
     */
    public String getRealName() {
        return realName;
    }

    /**
     * @param realName the realName to set nick name
     */
    public void setRealName(String realName) {
        this.realName = realName;
    }

    /**
     * @return the sex
     */
    public Integer getSex() {
        return sex;
    }

    /**
     * @param sex the sex to set
     */
    public void setSex(Integer sex) {
        this.sex = sex;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the mobile
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * @param mobile the mobile to set
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * @return the officePhone
     */
    public String getOfficePhone() {
        return officePhone;
    }

    /**
     * @param officePhone the officePhone to set
     */
    public void setOfficePhone(String officePhone) {
        this.officePhone = officePhone;
    }

    /**
     * @return the errorCount count login error times
     */
    public Integer getErrorCount() {
        return errorCount;
    }

    /**
     * @param errorCount the errorCount to set count login error times
     */
    public void setErrorCount(Integer errorCount) {
        this.errorCount = errorCount;
    }

    /**
     * @return the lastLoginTime
     */
    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    /**
     * @param lastLoginTime the lastLoginTime to set
     */
    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    /**
     * @return the lastLoginIp
     */
    public String getLastLoginIp() {
        return lastLoginIp;
    }

    /**
     * @param lastLoginIp the lastLoginIp to set
     */
    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }

    /**
     * @return the remark
     */
    public String getRemark() {
        return remark;
    }

    /**
     * @param remark the remark to set
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

}