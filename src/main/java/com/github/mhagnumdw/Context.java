package com.github.mhagnumdw;

import javax.annotation.processing.ProcessingEnvironment;

public final class Context {

	private final ProcessingEnvironment pe;

	public Context(ProcessingEnvironment pe) {
		this.pe = pe;
	}

}