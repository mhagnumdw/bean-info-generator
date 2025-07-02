# Contributing to bean-info-generator

// TODO: review everything below 

We welcome contributions to `rewrite-format-sql`! Here are some ways you can contribute.

## Reporting Bugs

If you find a bug, please open an issue on our GitHub repository.

## Suggesting Enhancements

If you have an idea for a new feature or an improvement to an existing one, please open an issue on our GitHub repository. Describe your idea clearly and explain why you think it would be a valuable addition to the project.

## Development

This project uses Java 8 for the main source and Java 17 for the test source. When importing into Eclipse as a Maven project, you need to manually change to Java 17: `right-click on the project > Build Path > Configure Build Path... > Libraries`, remove Java 8 and add Java 17 using the `Add Library...` button.

To test in the real environment during development, just install the JAR and reference the SNAPSHOT version:

```bash
./mvnw -V install
```

> The artifact is installed in `~/.m2` with the GAV: `io.github.mhagnumdw:rewrite-format-sql:X.X.X-SNAPSHOT`

To run automated tests:

```bash
./mvnw test
```

Test coverage available at `target/site/jacoco/index.html`.

To run automatic code formatting:

```bash
# format (CAUTION, as the code will be modified!)
./mvnw rewrite:run
# just validate
./mvnw rewrite:dryRun
```

The project was initially created following the official guides:

- <https://docs.openrewrite.org/authoring-recipes/recipe-development-environment>
- <https://docs.openrewrite.org/authoring-recipes/writing-a-java-refactoring-recipe>

---

We appreciate your contributions!
