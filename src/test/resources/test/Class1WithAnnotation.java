package test;

import java.io.Serializable;
import java.util.Date;

import com.github.mhagnumdw.beaninfogenerator.GenerateBeanMetaInfo;

@GenerateBeanMetaInfo
public class Class1WithAnnotation implements Serializable {

    private static final long serialVersionUID = 1L;

    private int age;
    private Date birthday;
    private String name;
    private Class2WithoutAnnotation classTwo;

    public void aaa() {

    }

    public final void bbb() {

    }

    protected void ccc() {

    }

    private void ddd() {

    }

    void eee() {

    }

}
