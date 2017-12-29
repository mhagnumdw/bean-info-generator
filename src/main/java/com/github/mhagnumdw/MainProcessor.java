package com.github.mhagnumdw;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic.Kind;
import javax.tools.JavaFileObject;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeSpec.Builder;

@SupportedAnnotationTypes({ "com.github.mhagnumdw.GenerateBeanInfo" })
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class MainProcessor extends AbstractProcessor {

	private Messager messager;
	private Elements elementUtils;

	// keep track of all classes for which model have been generated
	private final Collection<String> generatedModelClasses = new HashSet<String>();

	@Override
	public synchronized void init(ProcessingEnvironment processingEnv) {
		messager =  processingEnv.getMessager();
		elementUtils = processingEnv.getElementUtils();
		super.init(processingEnv);
	}

	// TODO: testar classe dentro de classe

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		logNote("COMEÇANDO O PROCESSAMENTO DE ANOTAÇÕES");
		for (TypeElement annotation : annotations) {
			logNote("Anotação: {}", annotation.getQualifiedName());

			Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(GenerateBeanInfo.class);
			logNote("Serão inspecionadas {} classes", elements.size());

			for (Element classElement : elements) {
				final String fcqn = ((TypeElement) classElement).getQualifiedName().toString();
				final String className = classElement.getSimpleName().toString();

				//final String packageName = classElement.getEnclosingElement().toString();
				final String packageName = elementUtils.getPackageOf(classElement).getQualifiedName().toString();
				//final String packageName = fcqn.replaceAll("\\.?" + className + "$", "");

				logNote("Detalhes: {} / {} / {}" , fcqn, packageName, className);
				// TODO: se já tiver sido gerado não gerar mais!

				logNote("Classe: {}.{}", packageName, className);

				if (!fcqn.equals(packageName + "." + className)) {
					logWarning("O fcqn '{}' difere da concatenção do pacote '{}' com o nome da classe '{}'", fcqn, packageName, className);
				}

//				List<? extends Element> enclosedClassElements = classElement.getEnclosedElements();
//				for (Element element : enclosedClassElements) {
//					String mod = "";
//					Set<Modifier> modificadores = element.getModifiers();
//					for (Modifier modifier : modificadores) {
//						mod += modifier.name() + ", ";
//					}
//					logNote("Atributo: {}, Modificador: {}, {}, {}, {}", element.getSimpleName(), mod, element.asType(), element.getKind().name(), packageName);
//				}

				final Builder typeBuilder = TypeSpec.classBuilder(className + "_INFO").addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT);

				List<? extends Element> fieldsElements = getFields(classElement);
				for (Element fieldElement : fieldsElements) {
					final String fieldName = fieldElement.getSimpleName().toString();
					logNote("Atributo: {}, Modificador: {}, {}, {}, {}", fieldName, "mod", fieldElement.asType(), fieldElement.getKind().name(), packageName);
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

				try {
					final Filer filer = processingEnv.getFiler();
					JavaFileObject jfo = filer.createSourceFile(javaFile.packageName + "." + javaFile.typeSpec.name);
					Writer writer = jfo.openWriter();
					writer.append(javaFile.toString());
					writer.close();

					//JavaFileObject jfo = javaFile.toJavaFileObject();
					//logNote("File: {}", javaFile.typeSpec.name);
					//logNote("File1: {}", javaFile.typeSpec.name);
					//logNote("File2: {}", javaFile.packageName);
					//javaFile.writeTo(filer);
					//JavaFileObject jfo = filer.createSourceFile(fcqn + "BeanInfo");
					//Writer writer = jfo.openWriter();
					//OutputStream os = jfo.openOutputStream();
					//Class<?> clazz = Class.forName(className);
					//List<Field> allFields = ReflectionUtils.getAllFields(clazz);
					//writer.append("package com.github.mhagnumdw.test;");
					//writer.append(System.lineSeparator());
					//writer.append("public class " + className + "BeanInfo" + " { }");
					//javaFile.writeTo(os);
					//writer.append(javaFile.toString());
					//writer.close();
					//os.close();
				} catch (Exception e1) {
					logError(getStackTrace(e1));
					//logError(e1.getMessage());
					//e1.printStackTrace();
					//throw new RuntimeException(e1);
				}
			}
		}
		return true;
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
		messager.printMessage(Kind.NOTE, format(format, arguments));
	}

	private void logError(String format, Object... arguments) {
		messager.printMessage(Kind.ERROR, format(format, arguments));
	}

	private void logWarning(String format, Object... arguments) {
		messager.printMessage(Kind.WARNING, format(format, arguments));
	}

	private String format(String format, Object... arguments) {
        return String.format(format.replace("{}", "%s"), arguments);
    }

    private String getStackTrace(final Throwable throwable) {
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw, true);
        throwable.printStackTrace(pw);
        return sw.getBuffer().toString();
    }

}
