package org.example.factories;

import net.datafaker.Faker;
import net.datafaker.providers.base.Text;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;

import static net.datafaker.providers.base.Text.DIGITS;
import static net.datafaker.providers.base.Text.EN_UPPERCASE;
import static org.jeasy.random.FieldPredicates.named;

public final class EasyRandomFactory {

    private EasyRandomFactory() {}
    private static final Faker faker = new Faker();

    public static EasyRandom userGenerator(){
        EasyRandomParameters parameters = new EasyRandomParameters()
                .seed(228L)
                .randomize(named("username"), () -> faker.credentials().username())
                .randomize(named("email"), () -> faker.internet().emailAddress())
                .randomize(named("password"), EasyRandomFactory::generatePassword)
                .randomize(named("first_name"), () -> faker.name().firstName())
                .randomize(named("last_name"), () -> faker.name().lastName());
        return new EasyRandom(parameters);
    }

    public static EasyRandom postGenerator() {
        EasyRandomParameters parameters = new EasyRandomParameters()
                .seed(228L)
                .randomize(named("userId"), () -> String.valueOf(faker.number().numberBetween(1, 100)))
                .randomize(named("title"), () -> faker.lorem().sentence())
                .randomize(named("content"), () -> faker.lorem().paragraph());
        return new EasyRandom(parameters);
    }

    public static EasyRandom commentGenerator() {
        EasyRandomParameters parameters = new EasyRandomParameters()
                .seed(228L)
                .randomize(named("userId"), () -> String.valueOf(faker.number().numberBetween(1, 100)))
                .randomize(named("postId"), () -> String.valueOf(faker.number().numberBetween(1, 100)))
                .randomize(named("comment"), () -> faker.lorem().sentence());
        return new EasyRandom(parameters);
    }

    private static String generatePassword(){
        return faker.text().text(Text.TextSymbolsBuilder.builder()
                .len(8)
                .with(EN_UPPERCASE, 2)
                .with(DIGITS, 3).build());
    }
}
