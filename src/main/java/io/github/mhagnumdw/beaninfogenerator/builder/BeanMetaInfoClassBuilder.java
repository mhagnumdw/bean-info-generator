package io.github.mhagnumdw.beaninfogenerator.builder;

import javax.lang.model.element.Modifier;

import com.squareup.javapoet.FieldSpec;

import io.github.mhagnumdw.beaninfogenerator.BeanMetaInfo;
import io.github.mhagnumdw.beaninfogenerator.Context;

class BeanMetaInfoClassBuilder extends ClassBuilderAbstract implements ClassBuilder {

    public BeanMetaInfoClassBuilder(final Context context) {
        super(context);
    }

    @Override
    protected FieldSpec buildFieldSpec(final String fieldName) {
        // @formatter:off
        return FieldSpec
            .builder(BeanMetaInfo.class, fieldName)
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
            .initializer("new BeanMetaInfo($S)", fieldName)
            .build();
        // @formatter:on
    }

}
