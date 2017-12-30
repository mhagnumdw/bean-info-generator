package com.github.mhagnumdw.beaninfogenerator;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.tools.Diagnostic;

public final class Context {

    private ProcessingEnvironment pe;

    public static final String DEBUG = "debug";
    public static final String SUFFIX = "suffix";

    private boolean debug = false;
    private String suffix = "_INFO";

    private Context() {

    }

    /**
     * Constructor. Loads some options like debug, suffix etc.
     * 
     * @param pe
     *            instance of {@link ProcessingEnvironment}
     */
    public Context(ProcessingEnvironment pe) {
        this.pe = pe;

        String tmp = pe.getOptions().get(DEBUG);
        if (GeneralUtils.isNotBlank(tmp)) {
            enableDebug(Boolean.parseBoolean(tmp));
        }

        tmp = pe.getOptions().get(SUFFIX);
        if (GeneralUtils.isNotBlank(tmp)) {
            setSuffix(tmp);
        }
    }

    public void enableDebug(boolean debug) {
        this.debug = debug;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getSuffix() {
        return suffix;
    }

    public String supportedOptionsSummary() {
        return GeneralUtils.format("debug: {}, suffix: {}", debug, suffix);
    }

    public PackageElement getPackageOf(Element classElement) {
        return pe.getElementUtils().getPackageOf(classElement);
    }

    public void printMessage(Diagnostic.Kind kind, CharSequence msg) {
        pe.getMessager().printMessage(kind, msg);
    }

}