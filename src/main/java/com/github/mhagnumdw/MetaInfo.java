package com.github.mhagnumdw;

import java.io.Serializable;

// TODO: JavaDoc!
public class MetaInfo implements Serializable {

	private static final long serialVersionUID = -415621717904168964L;

	private String name;

	public MetaInfo(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
