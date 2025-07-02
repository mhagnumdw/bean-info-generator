# Contributing to bean-info-generator

We welcome contributions to `bean-info-generator`! Here are some ways you can contribute.

## Reporting Bugs

If you find a bug, please open an issue on our GitHub repository.

## Suggesting Enhancements

If you have an idea for a new feature or an improvement to an existing one, please open an issue on our GitHub repository. Describe your idea clearly and explain why you think it would be a valuable addition to the project.

## Development

This project uses Java 8.

To test in the real environment during development, just install the JAR and reference the SNAPSHOT version:

```bash
./mvnw -V install
```

> The artifact is installed in `~/.m2` with the GAV: `io.github.mhagnumdw:bean-info-generator:X.X.X-SNAPSHOT`

To run automated tests:

```bash
./mvnw test
```

Test coverage available at `target/site/jacoco/index.html`.

---

We appreciate your contributions!
