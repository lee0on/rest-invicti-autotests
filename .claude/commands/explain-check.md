## Parameters
$ARGUMENTS = what to explain. Accepts either:
- test class: "BookingTest"
- specific test method: "BookingTest#testCreateBookingWithInvalidDates"
- file path: "src/test/java/org/example/checks/BookingTest.java"

## Actions
1. Read the specified test class or method
2. For each test method in scope, produce:
    - **What it tests**: which API endpoint and which scenario (happy path, negative, boundary)
    - **Test data**: how data is constructed (factory, builder, hardcoded) and what makes it specific to this scenario
    - **API call**: which request class method is called and with what arguments
    - **Assertions**: what exactly is verified and what values are expected
3. Assess quality of each test:
    - Coverage: is the scenario well-defined or too vague?
    - Assertions: specific enough or too shallow?
    - Independence: relies on external state or other tests?
    - Readability: is DisplayName clear? Is Arrange-Act-Assert structure followed?
4. Print the explanation in plain language, one block per test method