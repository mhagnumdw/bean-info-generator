package io.github.mhagnumdw.beaninfogenerator;

import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.tools.Diagnostic;

public final class Context {

    private ProcessingEnvironment pe;

    // Supported Options
    static final String DEBUG = "debug";
    static final String SUFFIX = "suffix";
    static final String ADD_GENERATION_DATE = "addGenerationDate";
    static final String ONLY_NAME = "onlyName";
    static final String USE_JDK9_GENERATED_ANNOTATION = "useJdk9GeneratedAnnotation";


    private boolean debug = false;
    private String suffix = "_INFO";
    private boolean addGenerationDate = false;
    private boolean onlyName = false;
    private boolean useJdk9GeneratedAnnotation = false;

    /**
     * Constructor. Loads some options like debug, suffix, add generated date, only name etc.
     *
     * @param pe
     *            instance of {@link ProcessingEnvironment}
     */
    Context(ProcessingEnvironment pe) {
        this.pe = pe;

        String tmp = pe.getOptions().get(DEBUG);
        if (GeneralUtils.isNotBlank(tmp)) {
            enableDebug(Boolean.parseBoolean(tmp));
        }

        tmp = pe.getOptions().get(SUFFIX);
        if (GeneralUtils.isNotBlank(tmp)) {
            setSuffix(tmp);
        }

        tmp = pe.getOptions().get(ADD_GENERATION_DATE);
        if (GeneralUtils.isNotBlank(tmp)) {
            setAddGenerationDate(Boolean.parseBoolean(tmp));
        }

        tmp = pe.getOptions().get(ONLY_NAME);
        if (GeneralUtils.isNotBlank(tmp)) {
            setOnlyName(Boolean.parseBoolean(tmp));
        }

        tmp = pe.getOptions().get(USE_JDK9_GENERATED_ANNOTATION);
        if (GeneralUtils.isNotBlank(tmp)) {
            setUseJdk9GeneratedAnnotation(Boolean.parseBoolean(tmp));
        }
    }

    private void enableDebug(boolean debug) {
        this.debug = debug;
    }

    boolean isDebug() {
        return debug;
    }

    private void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getSuffix() {
        return suffix;
    }

    private void setAddGenerationDate(boolean addGenerationDate) {
        this.addGenerationDate = addGenerationDate;
    }

    public boolean isAddGenerationDate() {
        return addGenerationDate;
    }

    private void setOnlyName(boolean onlyName) {
        this.onlyName = onlyName;
    }

    public boolean isOnlyName() {
        return onlyName;
    }

    public boolean isUseJdk9GeneratedAnnotation() {
        return useJdk9GeneratedAnnotation;
    }

    public void setUseJdk9GeneratedAnnotation(boolean useJdk9GeneratedAnnotation) {
        this.useJdk9GeneratedAnnotation = useJdk9GeneratedAnnotation;
    }

    String supportedOptionsSummary() {
        return GeneralUtils.format("debug: {}, suffix: {}, addGenerationDate: {}, onlyName: {}", debug, suffix, addGenerationDate, onlyName);
    }

    public PackageElement getPackageOf(Element classElement) {
        return pe.getElementUtils().getPackageOf(classElement);
    }

    void printMessage(Diagnostic.Kind kind, CharSequence msg) {
        pe.getMessager().printMessage(kind, msg);
    }

    Filer getFiler() {
        return pe.getFiler();
    }

}