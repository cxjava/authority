package com.chenxin.authority.pojo;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * EXTCombo对象
 * 
 * @author cx
 * @date 2012-2-27 上午9:32:31
 */
public class Combo implements Serializable{
	private static final long serialVersionUID = 6720784189359113848L;
	private String k;
	private String v;
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this,
				ToStringStyle.SHORT_PREFIX_STYLE, true, false);
	}
	public Combo() {
		super();
	}

	public Combo(String k, String v) {
		super();
		this.k = k;
		this.v = v;
	}

	public String getK() {
		return k;
	}

	public void setK(String k) {
		this.k = k;
	}

	public String getV() {
		return v;
	}

	public void setV(String v) {
		this.v = v;
	}

}
