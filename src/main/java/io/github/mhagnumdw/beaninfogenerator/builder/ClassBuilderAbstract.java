package io.github.mhagnumdw.beaninfogenerator.builder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Generated;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import io.github.mhagnumdw.beaninfogenerator.BeanMetaInfoProcessor;
import io.github.mhagnumdw.beaninfogenerator.Context;

abstract class ClassBuilderAbstract implements ClassBuilder {

    private static final String SIMPLE_DATE_FORMAT_STRING = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat(SIMPLE_DATE_FORMAT_STRING);

    private final Context context;

    public ClassBuilderAbstract(final Context context) {
        this.context = context;
    }

    @Override
    public JavaFile build(Element classElement) {
        final String className = classElement.getSimpleName().toString();
        final String fcqnOriginalSource = ((TypeElement) classElement).getQualifiedName().toString();
        final String packageName = context.getPackageOf(classElement).getQualifiedName().toString();

        AnnotationSpec generatedAnnotationSpec = buildGeneratedAnnotationSpec(fcqnOriginalSource, context);

        // @formatter:off
        final com.squareup.javapoet.TypeSpec.Builder typeBuilder = TypeSpec
                .classBuilder(className + context.getSuffix())
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addAnnotation(generatedAnnotationSpec);
        // @formatter:on

        List<? extends Element> fieldsElements = getFields(classElement);
        for (Element fieldElement : fieldsElements) {
            final String fieldName = fieldElement.getSimpleName().toString();
            final FieldSpec field = buildFieldSpec(fieldName);
            typeBuilder.addField(field);
        }

        final TypeSpec type = typeBuilder.build();

        return JavaFile.builder(packageName, type).build();
    }

    protected abstract FieldSpec buildFieldSpec(final String fieldName);

    protected List<? extends Element> getFields(Element classElement) {
        // @formatter:off
        return classElement
                .getEnclosedElements()
                .stream()
                .filter(e -> ElementKind.FIELD.equals(e.getKind()))
                .collect(Collectors.toList());
        // @formatter:on
    }

    private Date getDateNow() {
        return new Date();
    }

    protected AnnotationSpec buildGeneratedAnnotationSpec(final String fcqnOriginalSource, final Context context) {
        // @formatter:off
        final com.squareup.javapoet.AnnotationSpec.Builder generatedAnnotationSpecBuilder = AnnotationSpec
                .builder(Generated.class)
                .addMember("value", "$S", BeanMetaInfoProcessor.class.getName())
                .addMember("comments", "\"Only Name: $L, Class metadata information of: $L\"", context.isOnlyName(), fcqnOriginalSource);
        // @formatter:on

        if (context.isAddGenerationDate()) {
            generatedAnnotationSpecBuilder.addMember("date", "\"$L / $L\"", SIMPLE_DATE_FORMAT.format(getDateNow()), SIMPLE_DATE_FORMAT_STRING);
        }

        return generatedAnnotationSpecBuilder.build();
    }

}
