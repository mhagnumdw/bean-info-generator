package com.github.mhagnumdw;

import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic.Kind;

@SupportedAnnotationTypes({ "com.github.mhagnumdw.GenerateBeanInfo" })
public class MainProcessor extends AbstractProcessor {

	private Elements elements = null;
	private Types types = null;

	@Override
	public synchronized void init(ProcessingEnvironment processingEnv) {
		elements = processingEnv.getElementUtils();
		types = processingEnv.getTypeUtils();
		super.init(processingEnv);
	}

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		processingEnv.getMessager().printMessage(Kind.NOTE, "COMEÇANDO O PROCESSAMENTO DE ANOTAÇÕES");
		for (TypeElement annotation : annotations) {
			processingEnv.getMessager().printMessage(Kind.NOTE, "Anotação: " + annotation.getQualifiedName());
			Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(GenerateBeanInfo.class);
			for (Element e : elements) {
				processingEnv.getMessager().printMessage(Kind.NOTE, "Classe: " + e.getSimpleName() + " - "
						+ e.asType().toString() + " - " + ((TypeElement) e).getQualifiedName().toString());
			}
		}
		return true;
	}

}
