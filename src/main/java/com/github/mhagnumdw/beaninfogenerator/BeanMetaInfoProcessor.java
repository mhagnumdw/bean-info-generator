package com.github.mhagnumdw.beaninfogenerator;

import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Generated;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic.Kind;
import javax.tools.JavaFileObject;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

/**
 * Annotation processor to generate the metadata information of the classes annotated with {@link GenerateBeanMetaInfo}.
 */
//@formatter:off
@SupportedAnnotationTypes({
    "com.github.mhagnumdw.beaninfogenerator.GenerateBeanMetaInfo"
})
@SupportedOptions({
    Context.DEBUG,
    Context.SUFFIX,
    Context.ADD_GENERATION_DATE
})
//@formatter:on
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class BeanMetaInfoProcessor extends AbstractProcessor {

    // TODO: configurar pom.xml: gerar javadoc, gerar sources, autoria, profile para shaded

    // TODO: testar classe dentro de classe. Suportar isso?

    // TODO: talvez seja mais interessante colocar a classe gerada como final e
    // com um contrutor default privado e dentro do contrutor algo como:
    // throw new UnsupportedOperationException("Something very wrong here!");

    // TODO: tornar compatível com Java 7

    private static final boolean ALLOW_OTHER_PROCESSORS_TO_CLAIM_ANNOTATIONS = false;

    private static final String SIMPLE_DATE_FORMAT_STRING = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat(SIMPLE_DATE_FORMAT_STRING);

    private Context context;

    // keep track of all classes for which info have been generated
    private final Collection<String> generatedBeanMetaInfoClasses = new HashSet<String>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        context = new Context(processingEnv);
        logNote("Starting annotation processing: {}", getSupportedAnnotationTypes());
        logNote("Supported options: {}", getSupportedOptions());
        logNote("Supported options summary: {}", context.supportedOptionsSummary());
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (roundEnv.processingOver() || annotations.size() == 0) {
            return ALLOW_OTHER_PROCESSORS_TO_CLAIM_ANNOTATIONS;
        }

        logDebug("Annotations will be processed: {}", annotations);

        for (TypeElement annotation : annotations) {
            Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(GenerateBeanMetaInfo.class);
            logNote("The annotation '{}' is present in {} classes", annotation.getQualifiedName(), elements.size());

            for (Element classElement : elements) {
                final String fcqnOriginalSource = ((TypeElement) classElement).getQualifiedName().toString();
                final String className = classElement.getSimpleName().toString();
                final String packageName = context.getPackageOf(classElement).getQualifiedName().toString();

                if (!generatedBeanMetaInfoClasses.contains(fcqnOriginalSource)) {
                    logNote("Processing source: {}", fcqnOriginalSource);
                } else {
                    logDebug("Source '{}' already processed, going to the next", fcqnOriginalSource);
                    continue;
                }

                if (!fcqnOriginalSource.equals(buildFcqn(packageName, className))) {
                    logWarning("fcqn '{}' differs from the concatenation of the package '{}' with the class name '{}'", fcqnOriginalSource, packageName, className);
                }

                AnnotationSpec generatedAnnotationSpec = buildGeneratedAnnotationSpec(fcqnOriginalSource);

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

                final JavaFile javaFile = JavaFile.builder(packageName, type).build();
                final String fcqnGeneratedSource = buildFcqn(javaFile);

                try {
                    logNote("Generating: {}", fcqnGeneratedSource);
                    createSourceFile(javaFile);
                    generatedBeanMetaInfoClasses.add(fcqnOriginalSource);
                    logDebug("Generated: {}", fcqnGeneratedSource);
                } catch (Exception e) {
                    logError("Failed to generate java code: {}: {}", fcqnGeneratedSource, e.getMessage());
                    throw new RuntimeException("Failed to generate java code: " + fcqnGeneratedSource, e);
                }
            }
        }

        return ALLOW_OTHER_PROCESSORS_TO_CLAIM_ANNOTATIONS;
    }

    private void createSourceFile(JavaFile javaFile) throws Exception {
        final String fcqnGeneratedSource = buildFcqn(javaFile);
        JavaFileObject jfo = context.getFiler().createSourceFile(fcqnGeneratedSource);
        Writer writer = null;
        try {
            writer = jfo.openWriter();
            writer.append(javaFile.toString());
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    private List<? extends Element> getFields(Element classElement) {
        // @formatter:off
        return classElement
                .getEnclosedElements()
                .stream()
                .filter(e -> ElementKind.FIELD.equals(e.getKind()))
                .collect(Collectors.toList());
        // @formatter:on
    }

    private void logNote(String format, Object... arguments) {
        context.printMessage(Kind.NOTE, GeneralUtils.format("[INFO ] " + format, arguments));
    }

    private void logDebug(String format, Object... arguments) {
        if (context.isDebug()) {
            context.printMessage(Kind.NOTE, GeneralUtils.format("[DEBUG] " + format, arguments));
        }
    }

    private void logError(String format, Object... arguments) {
        context.printMessage(Kind.ERROR, GeneralUtils.format("[ERROR] " + format, arguments));
    }

    private void logWarning(String format, Object... arguments) {
        context.printMessage(Kind.WARNING, GeneralUtils.format("[WARN ] " + format, arguments));
    }

    private String buildFcqn(JavaFile javaFile) {
        return buildFcqn(javaFile.packageName, javaFile.typeSpec.name);
    }

    private String buildFcqn(String packageName, String className) {
        if (GeneralUtils.isNotBlank(packageName)) {
            packageName += ".";
        }
        return packageName + className;
    }

    private Date getDateNow() {
        return new Date();
    }

    private AnnotationSpec buildGeneratedAnnotationSpec(final String fcqnOriginalSource) {
        // @formatter:off
        final com.squareup.javapoet.AnnotationSpec.Builder generatedAnnotationSpecBuilder = AnnotationSpec
                .builder(Generated.class)
                .addMember("value", "$S", BeanMetaInfoProcessor.class.getName())
                .addMember("comments", "\"Class metadata information: $L\"", fcqnOriginalSource);
        // @formatter:on

        if (context.isAddGenerationDate()) {
            generatedAnnotationSpecBuilder.addMember("date", "\"$L / $L\"", SIMPLE_DATE_FORMAT.format(getDateNow()), SIMPLE_DATE_FORMAT_STRING);
        }

        return generatedAnnotationSpecBuilder.build();
    }

    private FieldSpec buildFieldSpec(final String fieldName) {
        // @formatter:off
        return FieldSpec
            .builder(BeanMetaInfo.class, fieldName)
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
            .initializer("new BeanMetaInfo($S)", fieldName)
            .build();
        // @formatter:on
    }

}
