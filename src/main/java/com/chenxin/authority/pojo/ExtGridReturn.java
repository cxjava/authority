package com.chenxin.authority.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * Ext Grid返回对象
 * 
 * @author Maty Chen
 * @date 2011-3-10 下午09:43:35
 */
public class ExtGridReturn implements Serializable {
	private static final long serialVersionUID = -8009510606594225281L;
	/**
	 * 总共条数
	 */
	private Long results;
	/**
	 * 所有数据
	 */
	private List<?> rows;

	public ExtGridReturn() {
	}

	public ExtGridReturn(Long results, List<?> rows) {
		this.results = results;
		this.rows = rows;
	}

	public Long getResults() {
		return results;
	}

	public void setResults(Long results) {
		this.results = results;
	}

	public List<?> getRows() {
		return rows;
	}

	public void setRows(List<?> rows) {
		this.rows = rows;
	}

}
