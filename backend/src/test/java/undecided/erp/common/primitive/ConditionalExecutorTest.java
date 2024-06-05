package undecided.erp.common.primitive;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ConditionalExecutorTest {

  private String testingObject;
  private Predicate<String> testingPredicate;
  private Consumer<String> trueConditionConsumer;
  private Consumer<String> falseConditionConsumer;
  private Function<String, String> trueConditionFunction;
  private Function<String, String> falseConditionFunction;

  @BeforeEach
  void setUp() {
    testingObject = "Test";
    trueConditionConsumer = mock(Consumer.class);
    falseConditionConsumer = mock(Consumer.class);
    trueConditionFunction = mock(Function.class);
    falseConditionFunction = mock(Function.class);
  }

  @Test
  void executeBasedOnPredicate_GivenTruthyPredicate_ShouldCallTruthyConsumer() {
    testingObject = "Test";
    testingPredicate = s -> true;

    ConditionalExecutor.executeBasedOnPredicate(testingObject, testingPredicate,
        trueConditionConsumer,
        falseConditionConsumer);

    verify(trueConditionConsumer).accept(testingObject);
    verifyNoInteractions(falseConditionConsumer);
  }

  @Test
  void executeBasedOnPredicate_GivenFalsyPredicate_ShouldCallFalsyConsumer() {
    testingObject = "Test";
    testingPredicate = s -> false;

    ConditionalExecutor.executeBasedOnPredicate(testingObject, testingPredicate,
        trueConditionConsumer, falseConditionConsumer);

    verify(falseConditionConsumer).accept(testingObject);
    verifyNoInteractions(trueConditionConsumer);
  }

  @Test
  void executeAndReturnBasedOnPredicate_GivenTruthyPredicate_ShouldCallTruthyFunction() {
    testingObject = "Test";
    testingPredicate = s -> true;

    ConditionalExecutor.executeAndReturnBasedOnPredicate(testingObject, testingPredicate,
        trueConditionFunction,
        falseConditionFunction);

    verify(trueConditionFunction).apply(testingObject);
    verifyNoInteractions(falseConditionFunction);
  }

  @Test
  void executeAndReturnBasedOnPredicate_GivenFalsyPredicate_ShouldCallFalsyFunction() {
    testingObject = "Test";
    testingPredicate = s -> false;

    ConditionalExecutor.executeAndReturnBasedOnPredicate(testingObject, testingPredicate,
        trueConditionFunction,
        falseConditionFunction);

    verify(falseConditionFunction).apply(testingObject);
    verifyNoInteractions(trueConditionFunction);
  }

  @Test
  void executeAndReturnBasedOnPredicate_GivenTruthyPredicateAndTruthyFunction_ShouldReturnResultOfTruthyFunction() {
    testingObject = "Test";
    testingPredicate = s -> true;
    Function<String, String> truthyFunction = s -> s + " passed";
    Function<String, String> falsyFunction = s -> s + " failed";

    String result = ConditionalExecutor.executeAndReturnBasedOnPredicate(testingObject,
        testingPredicate, truthyFunction, falsyFunction);

    assertEquals("Test passed", result);
  }

  @Test
  void executeAndReturnBasedOnPredicate_GivenFalsyPredicateAndFalsyFunction_ShouldReturnResultOfFalsyFunction() {
    testingObject = "Test";
    testingPredicate = s -> false;
    Function<String, String> truthyFunction = s -> s + " passed";
    Function<String, String> falsyFunction = s -> s + " failed";

    String result = ConditionalExecutor.executeAndReturnBasedOnPredicate(testingObject,
        testingPredicate, truthyFunction, falsyFunction);

    assertEquals("Test failed", result);
  }

  @Test
  void executeBasedOnExpression_GivenTruthyExpression_ShouldCallTruthyConsumer() {
    testingObject = "Test";
    boolean expression = true;

    ConditionalExecutor.executeBasedOnExpression(testingObject, expression, trueConditionConsumer,
        falseConditionConsumer);

    verify(trueConditionConsumer).accept(testingObject);
    verifyNoInteractions(falseConditionConsumer);
  }

  @Test
  void executeBasedOnExpression_GivenFalsyExpression_ShouldCallFalsyConsumer() {
    testingObject = "Test";
    boolean expression = false;

    ConditionalExecutor.executeBasedOnExpression(testingObject, expression, trueConditionConsumer,
        falseConditionConsumer);

    verify(falseConditionConsumer).accept(testingObject);
    verifyNoInteractions(trueConditionConsumer);
  }

  @Test
  void executeAndReturnBasedOnExpression_GivenTruthyExpression_ShouldCallTruthyFunction() {
    testingObject = "Test";
    boolean expression = true;

    ConditionalExecutor.executeAndReturnBasedOnExpression(testingObject, expression,
        trueConditionFunction, falseConditionFunction);

    verify(trueConditionFunction).apply(testingObject);
    verifyNoInteractions(falseConditionFunction);
  }

  @Test
  void executeAndReturnBasedOnExpression_GivenFalsyExpression_ShouldCallFalsyFunction() {
    testingObject = "Test";
    boolean expression = false;

    ConditionalExecutor.executeAndReturnBasedOnExpression(testingObject, expression,
        trueConditionFunction,
        falseConditionFunction);

    verify(falseConditionFunction).apply(testingObject);
    verifyNoInteractions(trueConditionFunction);
  }

  @Test
  void executeAndReturnBasedOnExpression_GivenTruthyExpressionAndTruthyFunction_ShouldReturnResultOfTruthyFunction() {
    testingObject = "Test";
    boolean expression = true;
    Function<String, String> truthyFunction = s -> s + " passed";
    Function<String, String> falsyFunction = s -> s + " failed";

    String result = ConditionalExecutor.executeAndReturnBasedOnExpression(testingObject, expression,
        truthyFunction, falsyFunction);

    assertEquals("Test passed", result);
  }

  @Test
  void executeAndReturnBasedOnExpression_GivenFalsyExpressionAndFalsyFunction_ShouldReturnResultOfFalsyFunction() {
    testingObject = "Test";
    boolean expression = false;
    Function<String, String> truthyFunction = s -> s + " passed";
    Function<String, String> falsyFunction = s -> s + " failed";

    String result = ConditionalExecutor.executeAndReturnBasedOnExpression(testingObject, expression,
        truthyFunction, falsyFunction);

    assertEquals("Test failed", result);
  }

  @Test
  void executeBasedOnPredicate_OneParameter_GivenTruthyPredicate_ShouldCallTruthyConsumer() {
    testingObject = "Test";
    Predicate<String> testingPredicate = s -> true;

    ConditionalExecutor.executeBasedOnPredicate(testingObject, testingPredicate,
        trueConditionConsumer);

    verify(trueConditionConsumer).accept(testingObject);
  }

  @Test
  void executeBasedOnPredicate_OneParameter_GivenFalsyPredicate_ShouldDoNothing() {
    testingObject = "Test";
    Predicate<String> testingPredicate = s -> false;

    ConditionalExecutor.executeBasedOnPredicate(testingObject, testingPredicate,
        trueConditionConsumer);

    verifyNoInteractions(trueConditionConsumer);
  }

  @Test
  void executeConsumerIfExpressionTrue_GivenTruthyExpression_ShouldCallConsumer() {
    testingObject = "Test";
    boolean expression = true;

    ConditionalExecutor.executeConsumerIfExpressionTrue(testingObject, expression,
        trueConditionConsumer);

    verify(trueConditionConsumer).accept(testingObject);
  }

  @Test
  void executeConsumerIfExpressionTrue_GivenFalsyExpression_ShouldNotCallConsumer() {
    testingObject = "Test";
    boolean expression = false;

    ConditionalExecutor.executeConsumerIfExpressionTrue(testingObject, expression,
        trueConditionConsumer);

    verifyNoInteractions(trueConditionConsumer);
  }
}
