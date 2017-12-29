package com.github.mhagnumdw.test;

import java.io.Serializable;

import com.github.mhagnumdw.GenerateBeanInfo;

@GenerateBeanInfo
public class Classe1 implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int idade;
	private String nome;
	private Classe2 classeDois;
	
}
