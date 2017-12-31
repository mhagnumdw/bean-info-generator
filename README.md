# Java - Bean Info Generator

[![Travis CI Build Status](https://travis-ci.org/mhagnumdw/bean-info-generator.png)](https://travis-ci.org/mhagnumdw/bean-info-generator)
[![Coverage Status](https://coveralls.io/repos/github/mhagnumdw/bean-info-generator/badge.svg?branch=master)](https://coveralls.io/github/mhagnumdw/bean-info-generator?branch=master)
[![Maven Central](http://img.shields.io/maven-central/v/com.github.mhagnumdw/bean-info-generator.svg)](http://search.maven.org/#search|ga|1|com.github.mhagnumdw)

Generate static information about Java Beans

### What is it?
It is an annotation processor for generating code containing static information about the fields of any Java class.

## What is the advantage/benefit?
Create typesafe code to be constructed in a strongly-typed manner when you need to reference fields by literal value.

## Usage
Annotate a class with `@GenerateBeanMetaInfo`

```java
package test;
import com.github.mhagnumdw.beaninfogenerator.GenerateBeanMetaInfo;

@GenerateBeanMetaInfo
public class People {
    private int age;
    private Date birthday;
    private String name;
}
```

When the build run, `People_INFO.java` and `People_INFO.class` are generated:
```java
package test;
import com.github.mhagnumdw.beaninfogenerator.BeanMetaInfo;
import javax.annotation.Generated;

@Generated(
    value = "com.github.mhagnumdw.beaninfogenerator.BeanMetaInfoProcessor",
    comments = "Class metadata information of: test.People"
)
public abstract class People_INFO {
    public static final BeanMetaInfo age = new BeanMetaInfo("age");

    public static final BeanMetaInfo birthday = new BeanMetaInfo("birthday");

    public static final BeanMetaInfo name = new BeanMetaInfo("name");
}
```

So, a code that is traditionally written like this:
```java
Field field = People.class.getDeclaredField("age");
```

Now it can be written like this:
```java
Field field = People.class.getDeclaredField(People_INFO.age.getName());
```

Someday, when the attribute `age` is renamed it will break in the compilation or your IDE will warn of the problem, something that would not happen before.

## // TODO: Configurar no build do Eclipse
- // TODO: escrever
- // TODO: lembrar que depende da lib: com.squareup:javapoet:1.9.0

## // TODO: Mostrar como no maven alterar alguns parâmetros
- // Esse processaodr de anotação suporta algumas opções, mostrar quais
- // Por padrão o maven já executa os processadores de anotação

## Limitations
- It currently generates information only from for fields;
- Internal classes are not created within the generated class.
