package com.dili.sysadmin.exception;

import java.io.Serializable;
import java.util.*;
import java.sql.Timestamp;

/**
 * <B>Description</B> <br />
 * <B>Copyright</B> Copyright (c) 2014 www.diligrp.com All rights reserved.
 * <br />
 * 本软件源代码版权归地利集团,未经许可不得任意复制与传播.<br />
 * <B>Company</B> 地利集团
 * 
 * @createTime 2015-10-4 10:02:14
 * @author template
 */
public class DiliException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5951457927563322444L;

	private Map<?, ?> map = null;

	public DiliException() {
	}

	public DiliException(String msg) {
		super(msg);
	}

	public DiliException(String msg, Map<?, ?> map) {
		super(msg);
	}

	public Map<?, ?> getMap() {
		return map;
	}

	public void setMap(Map<?, ?> map) {
		this.map = map;
	}
}