package io.github.mhagnumdw.beaninfogenerator;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.logging.Logger;

import javax.tools.JavaFileObject;

import com.google.common.collect.ImmutableList;
import com.google.common.io.ByteSource;
import com.google.testing.compile.Compilation;
import com.google.testing.compile.Compiler;

public final class TestUtils {

    private static final Logger LOGGER = Logger.getLogger(TestUtils.class.getName());

    /**
     * Compiles java source files.
     *
     * @param files
     *            java source files
     *
     * @return the results of the compilation
     */
    public static Compilation compile(JavaFileObject... files) {
        return compile(new Object[0], files);
    }

    /**
     * Compiles java source files.
     *
     * @param compileOptions
     *            command-line options to the compiler, eg: {@literal "-Asuffix=_GENERATED", "-AaddGenerationDate=true"}
     * @param files
     *            java source files
     *
     * @return the results of the compilation
     */
    public static Compilation compile(Object[] compileOptions, JavaFileObject... files) {
        try {
            final BeanMetaInfoProcessor processor = new BeanMetaInfoProcessor();
            return Compiler.javac().withOptions(compileOptions).withProcessors(processor).compile(files);
        } catch (Exception e) {
            final String optionsMsg = compileOptions.length == 0 ? "default" : Arrays.toString(compileOptions);
            final String errorMsg = GeneralUtils.format("Failed to compile '{}' with '{}' processor options", Arrays.toString(files), optionsMsg);
            throw new RuntimeException(errorMsg, e);
        }
    }

    /**
     * Prints the generated java source files in the default java log.
     *
     * @param compilation
     *            instance of {@link Compilation}
     */
    public static void printGeneratedSourceFiles(Compilation compilation) {
        try {
            ImmutableList<JavaFileObject> generatedFiles = compilation.generatedSourceFiles();
            if (generatedFiles.isEmpty()) {
                LOGGER.info("No generated files.");
                return;
            }
            LOGGER.info("Printing generated files:");
            for (JavaFileObject jfo : generatedFiles) {
                LOGGER.info(jfo.getName());
                LOGGER.info(jfo.toUri().toString());
                LOGGER.info(getContent(jfo));
                LOGGER.info("-------------------------------------------------");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String getContent(JavaFileObject jfo) throws IOException {
        ByteSource bs = new ByteSource() {
            @Override
            public InputStream openStream() throws IOException {
                return jfo.openInputStream();
            }
        };
        return bs.asCharSource(StandardCharsets.UTF_8).read();
    }

}
