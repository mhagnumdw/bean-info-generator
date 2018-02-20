package io.github.mhagnumdw.beaninfogenerator.builder;

import javax.lang.model.element.Element;

import com.squareup.javapoet.JavaFile;

public interface ClassBuilder {

    public JavaFile build(Element classElement);

}
