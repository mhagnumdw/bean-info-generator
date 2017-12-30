package com.github.mhagnumdw;

import java.io.Serializable;

/**
 * Armazena as informações de metadados.
 */
public class BeanMetaInfo implements Serializable {

    private static final long serialVersionUID = -415621717904168964L;

    private String name;

    // private Type type; // field, method ...

    public BeanMetaInfo(String name) {
        super();
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
