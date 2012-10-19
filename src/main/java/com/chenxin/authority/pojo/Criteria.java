package com.chenxin.authority.pojo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.MapUtils;

/**
 * 公用条件查询类<br>
 * 也可以用于MVC层之间的参数传递
 */
public class Criteria implements Serializable {
	private static final long serialVersionUID = 279228900781988612L;

	/**
	 * 存放条件查询值
	 */
	private Map<String, Object> condition;

	/**
	 * 是否相异
	 */
	protected boolean distinct;

	/**
	 * 排序字段
	 */
	protected String orderByClause;

	private Integer oracleStart;

	private Integer oracleEnd;

	protected Criteria(Criteria example) {
		this.orderByClause = example.orderByClause;
		this.condition = example.condition;
		this.distinct = example.distinct;
		this.oracleStart = example.oracleStart;
		this.oracleEnd = example.oracleEnd;
	}

	public Criteria() {
		condition = new HashMap<String, Object>();
	}

	public void clear() {
		this.condition.clear();
		this.orderByClause = null;
		this.distinct = false;
		this.oracleStart = null;
		this.oracleEnd = null;
	}

	/**
	 * @param condition
	 *            查询的条件名称
	 * @param value
	 *            查询的值
	 */
	public Criteria put(String condition, Object value) {
		this.condition.put(condition, value);
		return this;
	}

	/**
	 * 得到键值，C层和S层的参数传递时取值所用<br>
	 * 自行转换对象
	 * 
	 * @param key
	 *            键值
	 * @return 返回指定键所映射的值
	 */
	public Object get(String key) {
		return this.condition.get(key);
	}

	/**
	 * @param orderByClause
	 *            排序字段
	 */
	public void setOrderByClause(String orderByClause) {
		this.orderByClause = orderByClause;
	}

	/**
	 * @param distinct
	 *            是否相异
	 */
	public void setDistinct(boolean distinct) {
		this.distinct = distinct;
	}

	public void setCondition(Map<String, Object> condition) {
		this.condition = condition;
	}

	public Map<String, Object> getCondition() {
		return condition;
	}

	/**
	 * @param oracleStart
	 *            开始记录数
	 */
	public void setOracleStart(Integer oracleStart) {
		this.oracleStart = oracleStart;
	}

	/**
	 * @param oracleEnd
	 *            结束记录数
	 */
	public void setOracleEnd(Integer oracleEnd) {
		this.oracleEnd = oracleEnd;
	}

	/**
	 * 以Integer类型返回键值
	 * 
	 * @param key
	 *            键名
	 * @return Integer 键值
	 */
	public Integer getInteger(String key) {
		return MapUtils.getInteger(condition, key);
	}

	public int getIntValue(String key) {
		return MapUtils.getIntValue(condition, key);
	}

	/**
	 * 以Long类型返回键值
	 * 
	 * @param key
	 *            键名
	 * @return Long 键值
	 */
	public Long getLong(String key) {
		return MapUtils.getLong(condition, key);
	}

	public long getLongValue(String key) {
		return MapUtils.getLongValue(condition, key);
	}

	/**
	 * 以String类型返回键值
	 * 
	 * @param key
	 *            键名
	 * @return String 键值
	 */
	public String getString(String key) {
		return MapUtils.getString(condition, key);
	}

	/**
	 * 以Boolean类型返回键值
	 * 
	 * @param key
	 *            键名
	 * @return Timestamp 键值
	 */
	public Boolean getBoolean(String key) {
		return MapUtils.getBoolean(condition, key);
	}

	public boolean getBooleanValue(String key) {
		return MapUtils.getBooleanValue(condition, key);
	}

}