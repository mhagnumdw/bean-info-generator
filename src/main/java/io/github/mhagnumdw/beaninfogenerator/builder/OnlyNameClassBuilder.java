package io.github.mhagnumdw.beaninfogenerator.builder;

import javax.lang.model.element.Modifier;

import com.squareup.javapoet.FieldSpec;

import io.github.mhagnumdw.beaninfogenerator.Context;

class OnlyNameClassBuilder extends ClassBuilderAbstract {

    public OnlyNameClassBuilder(final Context context) {
        super(context);
    }

    @Override
    protected FieldSpec buildFieldSpec(final String fieldName) {
        // @formatter:off
        return FieldSpec
            .builder(String.class, fieldName)
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
            .initializer("$S", fieldName)
            .build();
        // @formatter:on
    }

}
