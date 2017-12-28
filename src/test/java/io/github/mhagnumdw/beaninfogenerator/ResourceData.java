package io.github.mhagnumdw.beaninfogenerator;

import javax.tools.JavaFileObject;

import com.google.testing.compile.JavaFileObjects;

class ResourceData {

    final String original;
    final String generated;
    final String expected;
    final String suffix;

    ResourceData(String original, String suffix, boolean expected) {
        super();
        this.suffix = suffix;
        this.original = original;
        this.generated = original.replaceAll("\\/", ".").replaceFirst("\\.java$", suffix);
        if (expected) {
            this.expected = original.replaceFirst("\\.java$", suffix + ".java");
        } else {
            this.expected = null;
        }
    }

    JavaFileObject getOriginalJavaFileObject() {
        return JavaFileObjects.forResource(original);
    }

    JavaFileObject getExpectedJavaFileObject() {
        return JavaFileObjects.forResource(expected);
    }

}
