package com.chenxin.authority.pojo;

import java.io.Serializable;

/**
 * Ext Ajax返回对象
 * 
 * @author Maty Chen
 * @date 2011-3-10 下午09:43:35
 */
public class ExtReturn implements Serializable {

	private static final long serialVersionUID = 4434330175390793196L;

	public ExtReturn() {
	}

	/**
	 * 是否更新成功的构造方法
	 * 
	 * @param success
	 *            是否成功
	 * @param msg
	 *            消息
	 */
	public ExtReturn(boolean success, Object msg) {
		this.success = success;
		this.msg = msg;
		this.o = "";
	}

	/**
	 * 是否更新成功的构造方法
	 * 
	 * @param success
	 *            是否成功
	 * @param msg
	 *            消息
	 * @param other
	 *            其他对象
	 */
	public ExtReturn(boolean success, Object msg, Object other) {
		this.success = success;
		this.msg = msg;
		this.o = other;
	}

	/**
	 * 异常时的构造方法
	 * 
	 * @param msg
	 *            异常消息
	 */
	public ExtReturn(Object errormsg) {
		// 异常情况
		this.success = false;
		this.msg = errormsg;
		this.o = "";
	}

	/**
	 * 是否成功
	 */
	private boolean success;
	/**
	 * 返回消息
	 */
	private Object msg;
	/**
	 * 其他对象
	 */
	private Object o;

	/**
	 * @return 是否成功
	 */
	public boolean isSuccess() {
		return success;
	}

	/**
	 * @param success
	 *            是否成功
	 */
	public void setSuccess(boolean success) {
		this.success = success;
	}

	/**
	 * @return 返回消息
	 */
	public Object getMsg() {
		return msg;
	}

	/**
	 * @param msg
	 *            返回消息
	 */
	public void setMsg(Object msg) {
		this.msg = msg;
	}

	/**
	 * @return 其他对象
	 */
	public Object getO() {
		return o;
	}

	/**
	 * @param o
	 *            其他对象
	 */
	public void setO(Object o) {
		this.o = o;
	}

}
