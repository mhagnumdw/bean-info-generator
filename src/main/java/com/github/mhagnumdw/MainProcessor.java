package com.github.mhagnumdw;

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
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
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
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic.Kind;
import javax.tools.JavaFileObject;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeSpec.Builder;

//@formatter:off
@SupportedAnnotationTypes({
	"com.github.mhagnumdw.GenerateBeanInfo"
})
@SupportedOptions({
	MainProcessor.DEBUG_OPTION,
	MainProcessor.SUFFIX
})
//@formatter:on
@SupportedSourceVersion(SourceVersion.RELEASE_8)

// TODO: adicionar JavaDoc

// TODO: configurar pom.xml: gerar javadoc, gerar sources, autoria, profile para shaded

public class MainProcessor extends AbstractProcessor {

	// TODO: testar classe dentro de classe

	// TODO: talvez seja mais interessante colocar a classe gerada como final e
	// com um contrutor default privado e dentro do contrutor algo como:
	// throw new UnsupportedOperationException("Something very wrong here!");

	// TODO: tornar compatível com Java 7

	public static final String DEBUG_OPTION = "debug";
	public static final String SUFFIX = "suffix";

	private static final boolean ALLOW_OTHER_PROCESSORS_TO_CLAIM_ANNOTATIONS = false;

	private static final String SIMPLE_DATE_FORMAT_STRING = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
	private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat(SIMPLE_DATE_FORMAT_STRING);

	private boolean logDebug = false;
	private String suffix = "_INFO";

	private Messager messager;
	private Elements elementUtils;

	// keep track of all classes for which info have been generated
	private final Collection<String> generatedMetaInfoClasses = new HashSet<String>();

	@Override
	public synchronized void init(ProcessingEnvironment processingEnv) {
		super.init(processingEnv);

		messager = processingEnv.getMessager();
		elementUtils = processingEnv.getElementUtils();

		logNote("Starting annotation processing: {}", getSupportedAnnotationTypes());

		String tmp = processingEnv.getOptions().get(DEBUG_OPTION);
		if (GeneralUtils.isNotBlank(tmp)) {
			logDebug = Boolean.parseBoolean(tmp);
		}
		logDebug("logDebug: {}", logDebug);

		tmp = processingEnv.getOptions().get(SUFFIX);
		if (GeneralUtils.isNotBlank(tmp)) {
			suffix = tmp;
		}
		logDebug("suffix: {}", suffix);
	}

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		if (roundEnv.processingOver() || annotations.size() == 0) {
			return ALLOW_OTHER_PROCESSORS_TO_CLAIM_ANNOTATIONS;
		}

		logDebug("Annotations will be processed: {}", annotations);

		for (TypeElement annotation : annotations) {
			Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(GenerateBeanInfo.class);
			logNote("The annotation '{}' is present in {} classes", annotation.getQualifiedName(), elements.size());

			for (Element classElement : elements) {
				final String fcqnOriginalSource = ((TypeElement) classElement).getQualifiedName().toString();
				final String className = classElement.getSimpleName().toString();
				final String packageName = elementUtils.getPackageOf(classElement).getQualifiedName().toString();

				if (!generatedMetaInfoClasses.contains(fcqnOriginalSource)) {
					logNote("Processing source: {}", fcqnOriginalSource);
				} else {
					logDebug("Source '{}' already processed, going to the next", fcqnOriginalSource);
					continue;
				}

				if (!fcqnOriginalSource.equals(packageName + "." + className)) {
					logWarning("fcqn '{}' differs from the concatenation of the package '{}' with the class name '{}'",
							fcqnOriginalSource, packageName, className);
				}

				// @formatter:off
				AnnotationSpec generatedAnnotationSpec = AnnotationSpec
						.builder(Generated.class)
						.addMember("value", "$S", MainProcessor.class.getName())
						.addMember("date", "\"$L / $L\"", SIMPLE_DATE_FORMAT.format(new Date()), SIMPLE_DATE_FORMAT_STRING)
						.addMember("comments", "\"Class metadata information: $L\"", fcqnOriginalSource)
						.build();
				// @formatter:on

				// @formatter:off
				final Builder typeBuilder = TypeSpec
						.classBuilder(className + suffix)
						.addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
						.addAnnotation(generatedAnnotationSpec);
				// @formatter:on

				List<? extends Element> fieldsElements = getFields(classElement);
				for (Element fieldElement : fieldsElements) {
					final String fieldName = fieldElement.getSimpleName().toString();
					// @formatter:off
					final FieldSpec field = FieldSpec
						.builder(MetaInfo.class, fieldName)
						.addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
						.initializer("new MetaInfo(\"" + fieldName + "\")")
						.build();
					// @formatter:on
					typeBuilder.addField(field);
				}

				final TypeSpec type = typeBuilder.build();

				final JavaFile javaFile = JavaFile.builder(packageName, type).build();
				final String fcqnGeneratedSource = javaFile.packageName + "." + javaFile.typeSpec.name;

				final Filer filer = processingEnv.getFiler();
				try {
					logNote("Generating: {}", fcqnGeneratedSource);
					JavaFileObject jfo = filer.createSourceFile(fcqnGeneratedSource);
					Writer writer = jfo.openWriter();
					writer.append(javaFile.toString());
					writer.close();
					logDebug("Generated: {}", fcqnGeneratedSource);
					generatedMetaInfoClasses.add(fcqnOriginalSource);
				} catch (Exception e) {
					logError("Failed to generate java code: {}: {}", fcqnGeneratedSource, e.getMessage());
					throw new RuntimeException("Failed to generate java code: " + fcqnGeneratedSource, e);
				}
			}
		}

		return ALLOW_OTHER_PROCESSORS_TO_CLAIM_ANNOTATIONS;
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
		messager.printMessage(Kind.NOTE, format("[INFO ] " + format, arguments));
	}

	private void logDebug(String format, Object... arguments) {
		if (logDebug) {
			messager.printMessage(Kind.NOTE, format("[DEBUG] " + format, arguments));
		}
	}

	private void logError(String format, Object... arguments) {
		messager.printMessage(Kind.ERROR, format("[ERROR] " + format, arguments));
	}

	private void logWarning(String format, Object... arguments) {
		messager.printMessage(Kind.WARNING, format("[WARN ] " + format, arguments));
	}

	private String format(String format, Object... arguments) {
		return String.format(format.replace("{}", "%s"), arguments);
	}

}
