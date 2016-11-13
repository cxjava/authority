package com.chenxin.authority.service;

/**
 * Service层公用的Exception.
 * <p>
 * 继承自RuntimeException, 从由Spring管理事务的函数中抛出时会触发事务回滚.
 *
 * @author calvin
 * @author Maty Chen
 */
public class ServiceException extends RuntimeException {

    private static final long serialVersionUID = 3583566093089790852L;

    /**
     * 错误代码，配置在国际化资源文件中，通过MessageSource.getMessage(errorCode, null, locale)得到
     */
    private String errorCode;

    public ServiceException(String errorCode) {
        super();
        this.errorCode = errorCode;
    }

    public ServiceException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public ServiceException(Throwable cause, String errorCode) {
        super(cause);
        this.errorCode = errorCode;
    }

    public ServiceException(Throwable cause, String message, String errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    /**
     * @return the errorCode
     * 错误代码，配置在国际化资源文件中，通过MessageSource.getMessage(errorCode, null,
     * locale)得到
     */
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * @param errorCode the errorCode to set
     *                  错误代码，配置在国际化资源文件中，通过MessageSource.getMessage(errorCode, null,
     *                  locale)得到
     */
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

}
