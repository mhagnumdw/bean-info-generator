# Java - Bean Info Generator

Generate static information about Java Beans

### What is it?
It is an annotation processor for generating code containing static information about the fields of any Java class.

## What is the advantage/benefit?
Create typesafe code to be constructed in a strongly-typed manner when you need to reference fields by literal value.

## Usage
Annotate a class with @GenerateBeanMetaInfo

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

When the build run, People_INFO.java and People_INFO.class are generated:
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

Someday, when the attribute "age" is renamed it will break in the compilation or your IDE will warn of the problem, something that would not happen before.

## Limitations
- It currently generates information only from for fields;
- Internal classes are not created within the generated class.