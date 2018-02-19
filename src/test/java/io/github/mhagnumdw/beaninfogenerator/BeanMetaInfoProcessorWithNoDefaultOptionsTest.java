package io.github.mhagnumdw.beaninfogenerator;

import static com.google.testing.compile.CompilationSubject.assertThat;

import org.junit.Test;

import com.google.testing.compile.Compilation;

public class BeanMetaInfoProcessorWithNoDefaultOptionsTest extends BeanMetaInfoProcessorAbstractTest {

    private static final String SUFFIX = "_GENERATED"; // No default suffix

    @Test
    public void testCompilationSuccess_WithNoDefaultSuffix() {
        final ResourceData classData = new ResourceData("test/Class1WithAnnotation.java", SUFFIX, true);
        final Object[] compileOptions = { "-Asuffix=" + SUFFIX };
        final Compilation compilation = TestUtils.compile(compileOptions, classData.getOriginalJavaFileObject());
        assertThat(compilation).succeeded();
        assertThat(compilation).generatedSourceFile(classData.generated).hasSourceEquivalentTo(classData.getExpectedJavaFileObject());
    }

    @Test
    public void testCompilationSuccess_WithNoDefaultSuffixAndWithGeneratedDate() {
        final ResourceData classData = new ResourceData("test/Class3WithAnnotation.java", SUFFIX, true);
        final Object[] compileOptions = { "-Asuffix=" + SUFFIX, "-AaddGenerationDate=true" };
        final Compilation compilation = TestUtils.compile(compileOptions, classData.getOriginalJavaFileObject());
        assertThat(compilation).succeeded();
        assertThat(compilation).generatedSourceFile(classData.generated).contentsAsUtf8String().containsMatch(REGEX_DETECT_GENERATED_DATE);
    }

    @Test
    public void testCompilationSuccess_WithNoDefaultSuffixAndOnlyName() {
        final ResourceData classData = new ResourceData("test/Class1WithAnnotationOnlyName.java", SUFFIX, true);
        final Object[] compileOptions = { "-Asuffix=" + SUFFIX, "-AonlyName=true" };
        final Compilation compilation = TestUtils.compile(compileOptions, classData.getOriginalJavaFileObject());
        assertThat(compilation).succeeded();
        assertThat(compilation).generatedSourceFile(classData.generated).hasSourceEquivalentTo(classData.getExpectedJavaFileObject());
    }

    @Test
    public void testCompilationSuccess_WithNoDefaultSuffixAndOnlyNameAndInheritance() {
        final ResourceData classData = new ResourceData("test/Class5WithAnnotationOnlyName.java", SUFFIX, true);
        final Object[] compileOptions = { "-Asuffix=" + SUFFIX, "-AonlyName=true" };
        final Compilation compilation = TestUtils.compile(compileOptions, classData.getOriginalJavaFileObject());
        assertThat(compilation).succeeded();
        assertThat(compilation).generatedSourceFile(classData.generated).hasSourceEquivalentTo(classData.getExpectedJavaFileObject());
    }

}
