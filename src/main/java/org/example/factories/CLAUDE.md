# Factories Package

## Purpose
EasyRandom + JavaFaker generators producing realistic randomized payload objects.

## Rules
- Each public method returns a configured `EasyRandom` for one entity
- Method naming: `{entity}Generator()` — e.g., `bookingGenerator()`
- Every field MUST have an explicit `randomize()` rule via `FieldPredicates.named()`
- Faker instance: `private static final` (shared across methods)

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
1. Identify all fields in the target request payload
2. Map each field to an appropriate Faker provider
3. For constrained fields (length, format) — create a private helper method
4. Add the method to `EasyRandomFactory` (or a dedicated factory class)

> See `test-data-generation` skill for Faker provider selection guide and data categories.

## Anti-Patterns
- Fields without explicit randomize rules — EasyRandom defaults are not realistic
- Random strings for structured fields (emails, phones, dates) — use Faker providers
- New Faker instance per method call — use the shared static instance
