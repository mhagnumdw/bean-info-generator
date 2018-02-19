package io.github.mhagnumdw.beaninfogenerator;

import static com.google.testing.compile.CompilationSubject.assertThat;
import static com.google.testing.compile.JavaFileObjectSubject.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.tools.JavaFileObject;

import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.google.testing.compile.Compilation;

public class BeanMetaInfoProcessorWithDefaultOptionsTest extends BeanMetaInfoProcessorAbstractTest {

    private static final String SUFFIX = "_INFO"; // default suffix

    private final ResourceData class1Data = new ResourceData("test/Class1WithAnnotation.java", SUFFIX, true);
    private final ResourceData class2Data = new ResourceData("test/Class2WithoutAnnotation.java", SUFFIX, false);
    private final ResourceData class3Data = new ResourceData("test/Class3WithAnnotation.java", SUFFIX, true);
    private final ResourceData class4Data = new ResourceData("test/Class4WithAnnotationNoAttributes.java", SUFFIX, true);
    private final ResourceData class5Data = new ResourceData("test/Class5WithAnnotation.java", SUFFIX, true);

    private final List<ResourceData> classes = new ArrayList<ResourceData>() {
        {
            add(class1Data);
            add(class2Data);
            add(class3Data);
            add(class4Data);
            add(class5Data);
        }
    };

    private final Compilation compilation = compileAll();

    @Test
    public void testCompilationSuccess() {
        assertThat(compilation).succeeded();
    }

    @Test
    public void testTotalGeneratedSources() {
        final int totalGeneratedSources = compilation.generatedSourceFiles().size();
        assertEquals(4, totalGeneratedSources);
    }

    @Test
    public void testNamesOfGeneratedSources() {
        final ImmutableList<JavaFileObject> generatedSources = compilation.generatedSourceFiles();
        final List<String> generatedNames = generatedSources.stream().map(jfo -> jfo.getName().replaceFirst("^.*?/test/Class", "test/Class")).sorted().collect(Collectors.toList());
        final List<String> expectedNames = classes.stream().filter(c -> c.expected != null).map(c -> c.expected).sorted().collect(Collectors.toList());
        final String msgIfFail = GeneralUtils.format("Expected '{}' but got '{}'", expectedNames, generatedNames);
        // Check if two list are equal ignoring order
        assertTrue(msgIfFail, generatedNames.size() == expectedNames.size() && generatedNames.containsAll(expectedNames) && expectedNames.containsAll(generatedNames));
    }

    @Test
    public void testNoContainsGeneratedDate() {
        final ImmutableList<JavaFileObject> generatedSources = compilation.generatedSourceFiles();
        for (JavaFileObject jfo : generatedSources) {
            assertThat(jfo).contentsAsUtf8String().doesNotContainMatch(REGEX_DETECT_GENERATED_DATE);
        }
    }

    @Test
    public void testContentOfGeneratedSources_Class1() {
        assertThat(compilation).generatedSourceFile(class1Data.generated).hasSourceEquivalentTo(class1Data.getExpectedJavaFileObject());
    }

    @Test
    public void testContentOfGeneratedSources_Class3() {
        assertThat(compilation).generatedSourceFile(class3Data.generated).hasSourceEquivalentTo(class3Data.getExpectedJavaFileObject());
    }

    @Test
    public void testContentOfGeneratedSources_Class4() {
        assertThat(compilation).generatedSourceFile(class4Data.generated).hasSourceEquivalentTo(class4Data.getExpectedJavaFileObject());
    }

    @Test
    public void testContentOfGeneratedSources_Class5() {
        assertThat(compilation).generatedSourceFile(class5Data.generated).hasSourceEquivalentTo(class5Data.getExpectedJavaFileObject());
    }

    private Compilation compileAll() {
        final JavaFileObject[] toCompile = classes.stream().map(c -> c.getOriginalJavaFileObject()).toArray(JavaFileObject[]::new);
        return TestUtils.compile(toCompile);
    }

}
