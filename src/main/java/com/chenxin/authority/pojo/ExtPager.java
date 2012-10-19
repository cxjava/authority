package com.chenxin.authority.pojo;

import java.io.Serializable;

/**
 * Ext的分页请求对象
 * 
 * @author chenxin
 * @date 2011-3-17 上午10:37:27
 */
public class ExtPager implements Serializable{
	private static final long serialVersionUID = 8298962642187346187L;
	private Integer limit;
	private Integer start;
	/**
	 * 大写的ASC or DESC
	 */
	private String dir;
	/**
	 * 排序的字段
	 */
	private String sort;

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public Integer getStart() {
		return start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public String getDir() {
		return dir;
	}

	public void setDir(String dir) {
		this.dir = dir;
	}

	public String getSort() {
		// TODO:先转化
		return Table.toClumn(sort);
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

}
