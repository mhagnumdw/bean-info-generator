package io.github.mhagnumdw.beaninfogenerator.builder;

import io.github.mhagnumdw.beaninfogenerator.Context;

public final class ClassFactory {

    private ClassFactory() {

    }

    public static ClassBuilder createBuilder(final Context context) {
        if (context.isOnlyName()) {
            return new OnlyNameClassBuilder(context);
        } else {
            return new BeanMetaInfoClassBuilder(context);
        }
    }

}
