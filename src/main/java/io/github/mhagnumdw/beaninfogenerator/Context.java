package io.github.mhagnumdw.beaninfogenerator;

import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.tools.Diagnostic;

final class Context {

    private ProcessingEnvironment pe;

    // Supported Options
    static final String DEBUG = "debug";
    static final String SUFFIX = "suffix";
    static final String ADD_GENERATION_DATE = "addGenerationDate";

    private boolean debug = false;
    private String suffix = "_INFO";
    private boolean addGenerationDate = false;

    /**
     * Constructor. Loads some options like debug, suffix, add generated date etc.
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
    }

    void enableDebug(boolean debug) {
        this.debug = debug;
    }

    boolean isDebug() {
        return debug;
    }

    void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    String getSuffix() {
        return suffix;
    }

    private void setAddGenerationDate(boolean addGenerationDate) {
        this.addGenerationDate = addGenerationDate;
    }

    boolean isAddGenerationDate() {
        return addGenerationDate;
    }

    String supportedOptionsSummary() {
        return GeneralUtils.format("debug: {}, suffix: {}, addGenerationDate: {}", debug, suffix, addGenerationDate);
    }

    PackageElement getPackageOf(Element classElement) {
        return pe.getElementUtils().getPackageOf(classElement);
    }

    void printMessage(Diagnostic.Kind kind, CharSequence msg) {
        pe.getMessager().printMessage(kind, msg);
    }

    Filer getFiler() {
        return pe.getFiler();
    }

}