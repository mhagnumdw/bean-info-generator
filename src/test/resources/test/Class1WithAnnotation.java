package test;

import java.io.Serializable;
import java.util.Date;

import com.github.mhagnumdw.beaninfogenerator.GenerateBeanMetaInfo;

@GenerateBeanMetaInfo
public class Class1WithAnnotation implements Serializable {

    private static final long serialVersionUID = 1L;

    private int contador;
    private Date idade;
    private String nome;
    private Class2WithoutAnnotation classeDois;

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
