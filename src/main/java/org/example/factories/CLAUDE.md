# Factories Package

## Purpose
Data generation factories using EasyRandom + JavaFaker.
Provides configured generators that produce realistic randomized
payload objects for use in tests.

## Rules
- Classes are `final` with `private` constructor (non-instantiable)
- Faker instance is a `private static final` field (shared)
- Each public method returns a configured `EasyRandom` instance for one entity
- Method naming: `{entity}Generator()` — e.g., `userGenerator()`, `bookingGenerator()`
- Every field in the target payload MUST have an explicit `randomize()` rule
  using `FieldPredicates.named("field_name")` and a matching Faker provider
- Use `.seed(value)` for reproducible generation when needed

## Pattern
```java
public final class EasyRandomFactory {

    private EasyRandomFactory() {}
    private static final Faker faker = new Faker();

    public static EasyRandom userGenerator() {
        EasyRandomParameters parameters = new EasyRandomParameters()
                .seed(228L)
                .randomize(named("username"), () -> faker.credentials().username())
                .randomize(named("email"), () -> faker.internet().emailAddress())
                .randomize(named("password"), EasyRandomFactory::generatePassword)
                .randomize(named("first_name"), () -> faker.name().firstName())
                .randomize(named("last_name"), () -> faker.name().lastName());
        return new EasyRandom(parameters);
    }

    private static String generatePassword() {
        return faker.text().text(Text.TextSymbolsBuilder.builder()
                .len(8)
                .with(EN_UPPERCASE, 2)
                .with(DIGITS, 3).build());
    }
}
```

## Usage in Tests

```java
private EasyRandom generator;

@BeforeEach
public void setPayload(){
    generator = userGenerator();
    payload = generator.nextObject(User.class);
}
```

## When Adding a New Generator

- Identify all fields in the target request payload class
- Map each field to an appropriate Faker provider
- For fields with specific constraints (length, format), create a private helper method
- Add the generator method to EasyRandomFactory (or a dedicated factory class)
- Verify by generating an instance and printing toString()

## Anti-Patterns

- Do NOT leave fields without explicit randomize rules — EasyRandom defaults are not realistic
- Do NOT use random strings for structured fields (emails, phones, dates)
- Do NOT instantiate Faker per method call — use the shared static instance