# Java - Bean Info Generator

[![Travis CI Build Status](https://travis-ci.org/mhagnumdw/bean-info-generator.png)](https://travis-ci.org/mhagnumdw/bean-info-generator)
[![Coverage Status](https://coveralls.io/repos/github/mhagnumdw/bean-info-generator/badge.svg?branch=master)](https://coveralls.io/github/mhagnumdw/bean-info-generator?branch=master)
[![Maven Central](http://img.shields.io/maven-central/v/io.github.mhagnumdw/bean-info-generator.svg)](http://search.maven.org/#search|ga|1|io.github.mhagnumdw)

Generate static information about Java Beans

### What is it?
It is an annotation processor for generating code containing static information about the fields of any Java class.

### What is the advantage/benefit?
Create typesafe code to be constructed in a strongly-typed manner when you need to reference fields by literal value.

### Usage
In Maven pom.xml

```xml
<dependency>
    <groupId>io.github.mhagnumdw</groupId>
    <artifactId>bean-info-generator</artifactId>
    <version>0.0.1</version>
    <scope>compile</scope>
</dependency>
```

Annotate a class with `@GenerateBeanMetaInfo`

```java
package test;
import io.github.mhagnumdw.beaninfogenerator.GenerateBeanMetaInfo;

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
import io.github.mhagnumdw.beaninfogenerator.BeanMetaInfo;
import javax.annotation.Generated;

@Generated(
    value = "io.github.mhagnumdw.beaninfogenerator.BeanMetaInfoProcessor",
    comments = "Only Name: false, Class metadata information of: test.People"
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

**Now it can be written like this:**
```java
Field field = People.class.getDeclaredField(People_INFO.age.getName());
```

Someday, when the attribute `age` is renamed it will break in the compilation or your IDE will warn of the problem, something that would not happen before.

### Supported Options (Parameters)
- debug (default: `false`): if `true` more information about annotation processing is written in the log;
- suffix (default: `_INFO`): sets the suffix to name the generated code;
- addGenerationDate (default: `false`): if `true` is added to the source its generation date;
- onlyName (default: `false`): if `true` a simple String is used to represent a field;
- useJdk9GeneratedAnnotation (default: `false`): if `true`, `javax.annotation.processing.Generated` will be used instead of `javax.annotation.Generated`.

The options can be seen in `@SupportedOptions` [here](/src/main/java/io/github/mhagnumdw/beaninfogenerator/BeanMetaInfoProcessor.java#L39).

### Option: onlyName=true
When `onlyName=true` `People_INFO` is generated as following:
```java
package test;
import java.lang.String;
import javax.annotation.Generated;

@Generated(
    value = "io.github.mhagnumdw.beaninfogenerator.BeanMetaInfoProcessor",
    comments = "Only Name: true, Class metadata information of: test.People"
)
public abstract class People_INFO {
    public static final String age = "age";

    public static final String birthday = "birthday";

    public static final String name = "name";
}
```

### Maven: changing processor parameters
In Maven pom.xml

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <version>${maven.compiler.plugin}</version>
    <configuration>
        <compilerArgs>
            <compilerArg>-AaddGenerationDate=true</compilerArg>
            <compilerArg>-Asuffix=_GENERATED</compilerArg>
        </compilerArgs>
    </configuration>
</plugin>
```

### Eclipse: configuration
1. Right click on the project > Properties
1. Java Compiler > Annotation Processing > Factory path
1. Add two jars:
    1. bean-info-generator-X.Y.Z.jar
    1. [javapoet-X.Y.Z.jar](https://github.com/square/javapoet)

### Limitations
- It currently generates information only for fields;
- Internal classes are not created within the generated class.
