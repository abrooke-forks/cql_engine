package org.opencds.cqf.cql.elm.execution;

import org.cqframework.cql.elm.execution.CodeSystemRef;
import org.cqframework.cql.elm.execution.Expression;
import org.cqframework.cql.elm.execution.ValueSetRef;
import org.opencds.cqf.cql.execution.Reporting;
import org.opencds.cqf.cql.runtime.*;

import java.math.BigDecimal;

/**
 * Created by Christopher on 12/22/2016.
 */
public class Execution {

    private static boolean doReport = false;
    private static Reporting report;
	public static Reporting getReport() { return report; }
    public static void enableReporting() {
        doReport = true;
        report = new Reporting();
    }

    public static Object resolveAggregateDoOperation(Executable e, Object operand) {
        Object result;
        if (operand instanceof Iterable) // all
            result = e.doOperation((Iterable<Object>) operand);
        else
            result = new IllegalArgumentException(String.format("Could not execute %s with type %s", e.getClass().getSimpleName(),
                    operand.getClass().getSimpleName()));

        if (doReport) {
            report.reportOperation(e.getClass().getSimpleName(), operand, result);
        }

        return result;
    }

    public static Object resolveArithmeticDoOperation(Executable e) {
        Object result = e.doOperation();
        if (doReport) {
            report.reportOperation(e.getClass().getSimpleName(), result);
        }
        return result;
    }

    public static Object resolveArithmeticDoOperation(Executable e, Object operand) {
        Object result;
        if (operand instanceof Integer) { // Abs, Negate, Predecessor, Successor
            result = e.doOperation((Integer) operand);
        }
        else if (operand instanceof BigDecimal) { // Abs, Ceiling, Floor, Exp, Ln, Negate, Predecessor, Round, Successor, Truncate
            result = e.doOperation((BigDecimal) operand);
        }
        else if (operand instanceof Quantity) { // Abs, Negate
            result = e.doOperation((Quantity) operand);
        }
        else if (operand instanceof DateTime) { // Predecessor, Successor
            result = e.doOperation((DateTime) operand);
        }
        else if (operand instanceof Time) { // Predecessor, Successor
            result = e.doOperation((Time) operand);
        }
        else if (operand == null) { // Maximum, Minimum
            result = e.doOperation();
        }
        else
            result = new IllegalArgumentException(String.format("Could not execute %s with type %s", e.getClass().getSimpleName(),
                operand.getClass().getSimpleName()));

        if (doReport) {
            report.reportOperation(e.getClass().getSimpleName(), operand, result);
        }

        return result;
    }

    public static Object resolveArithmeticDoOperation(Executable e, Object leftOperand, Object rightOperand) {
        Object result;
        if (leftOperand instanceof Integer && rightOperand instanceof Integer) { // Modulo, Multiply, Power, Div
            result = e.doOperation((Integer) leftOperand, (Integer) rightOperand);
        }
        else if (leftOperand instanceof BigDecimal && rightOperand instanceof BigDecimal) { // Divide, Log, Modulo, Multiply, Power, Div
            result = e.doOperation((BigDecimal) leftOperand, (BigDecimal) rightOperand);
        }
        else if (leftOperand instanceof Quantity && rightOperand instanceof Quantity) { // Divide, Multiply
            result = e.doOperation((Quantity) leftOperand, (Quantity) rightOperand);
        }
        else if (leftOperand instanceof Quantity && rightOperand instanceof BigDecimal) { // Divide, Multiply
            result = e.doOperation((Quantity) leftOperand, (BigDecimal) rightOperand);
        }
        else if (leftOperand instanceof BigDecimal && rightOperand instanceof Quantity) { // Multiply
            result = e.doOperation((BigDecimal) leftOperand, (Quantity) rightOperand);
        }
        else if (leftOperand instanceof BigDecimal && rightOperand instanceof Integer) { // Round
            result = e.doOperation((BigDecimal) leftOperand, (Integer) rightOperand);
        }
        else if (leftOperand instanceof Uncertainty && rightOperand instanceof Uncertainty) { // Divide, Multiply
            result = e.doOperation((Uncertainty) leftOperand, (Uncertainty) rightOperand);
        }
        else if (leftOperand instanceof BigDecimal && rightOperand == null) { // Round
            result = e.doOperation((BigDecimal) leftOperand, 0);
        }
        else if (leftOperand == null || rightOperand == null) { result = null; }
        else
            result = new IllegalArgumentException(String.format("Could not execute %s with types %s and %s", e.getClass().getSimpleName(),
                leftOperand.getClass().getSimpleName(), rightOperand.getClass().getSimpleName()));

        if (doReport) {
            report.reportOperation(e.getClass().getSimpleName(), leftOperand, rightOperand, result);
        }

        return result;
    }

    public static Object resolveClinicalDoOperation(Executable e, Object operand) {
        Object result;
        if (operand instanceof DateTime) { // AgeAt, CalculateAge
            result = e.doOperation((DateTime) operand);
        }
        else if (operand == null) { // Age
            result = e.doOperation();
        }
        else
            result = new IllegalArgumentException(String.format("Could not execute %s with type %s", e.getClass().getSimpleName(),
                operand.getClass().getSimpleName()));

        if (doReport) {
            report.reportOperation(e.getClass().getSimpleName(), operand, result);
        }

        return result;
    }

    public static Object resolveClinicalDoOperation(Executable e, Object leftOperand, Object rightOperand) {
        Object result;
        if (leftOperand instanceof DateTime && rightOperand instanceof DateTime) { // CalculateAgeAt
            result = e.doOperation((DateTime) leftOperand, (DateTime) rightOperand);
        }
        else
            result = new IllegalArgumentException(String.format("Could not execute %s with types %s and %s", e.getClass().getSimpleName(),
                leftOperand.getClass().getSimpleName(), rightOperand.getClass().getSimpleName()));

        if (doReport) {
            report.reportOperation(e.getClass().getSimpleName(), leftOperand, rightOperand, result);
        }

        return result;
    }

    private static boolean uniformTypeCheck(Object ... operands) {
        Object other = operands[0];
        for (Object o : operands) {
            if (!o.getClass().getSimpleName().equals(other.getClass().getSimpleName()))
                return true;
        }
        return false;
    }

    public static Object resolveComparisonDoOperation(Executable e, Object leftOperand, Object rightOperand) {
        Object result;
        if (leftOperand instanceof BigDecimal && rightOperand instanceof BigDecimal) { // Equals, Equivalent, Greater/Equal,
            result = e.doOperation((BigDecimal) leftOperand, (BigDecimal) rightOperand); // Less/Equal, NotEqual
        }
        else if (leftOperand instanceof Boolean && rightOperand instanceof Boolean) { // Equals, Equivalent, Greater/Equal,
            result = e.doOperation((Boolean) leftOperand, (Boolean) rightOperand); // Less/Equal, NotEqual
        }
        else if (leftOperand instanceof Code && rightOperand instanceof Code) { // Equals, Equivalent, NotEqual
            result = e.doOperation((Code) leftOperand, (Code) rightOperand);
        }
        else if (leftOperand instanceof Code && rightOperand == null) { // Equivalent
            // Code and Concept equivalence never returns null
            result = false;
        }
        else if (leftOperand == null && rightOperand instanceof Code) { // Equivalent
            // Code and Concept equivalence never returns null
            result = false;
        }
        else if (leftOperand instanceof Concept && rightOperand instanceof Concept) { // Equals, Equivalent, NotEqual
            result = e.doOperation((Concept) leftOperand, (Concept) rightOperand);
        }
        else if (leftOperand instanceof Concept && rightOperand == null) { // Equivalent
            // Code and Concept equivalence never returns null
            result = false;
        }
        else if (leftOperand == null && rightOperand instanceof Concept) { // Equivalent
            // Code and Concept equivalence never returns null
            result = false;
        }
        else if (leftOperand instanceof DateTime && rightOperand instanceof DateTime) { // Equals, Equivalent, Greater/Equal,
            result = e.doOperation((DateTime) leftOperand, (DateTime) rightOperand);      // Less/Equal, NotEqual
        }
        else if (leftOperand instanceof Integer && rightOperand instanceof Integer) { // Equals, Equivalent, Greater/Equal
            result = e.doOperation((Integer) leftOperand, (Integer) rightOperand);      // Less/Equal, NotEqual
        }
        else if (leftOperand instanceof Quantity && rightOperand instanceof Quantity) { // Equals, Equivalent, Greater/Equal
            result = e.doOperation((Quantity) leftOperand, (Quantity) rightOperand);      // Less/Equal, NotEqual
        }
        else if (leftOperand instanceof String && rightOperand instanceof String) { // Equals, Equivalent, Greater/Equal
            result = e.doOperation((String) leftOperand, (String) rightOperand);      // Less/Equal, NotEqual
        }
        else if (leftOperand instanceof Time && rightOperand instanceof Time) { // Equals, Equivalent, Greater/Equal
            result = e.doOperation((Time) leftOperand, (Time) rightOperand);      // Less/Equal, NotEqual
        }
        else if (leftOperand instanceof Iterable && rightOperand instanceof Iterable) { // Equals, Equivalent, NotEqual
            result = e.doOperation((Iterable<Object>) leftOperand, (Iterable<Object>) rightOperand);
        }
        else if (leftOperand instanceof Interval && rightOperand instanceof Interval) { // Equals, Equivalent, NotEqual
            result = e.doOperation((Interval) leftOperand, (Interval) rightOperand);
        }
        else if (leftOperand instanceof Uncertainty && rightOperand instanceof Integer) { // Greater/Equal, Less/Equal
            result = e.doOperation((Uncertainty) leftOperand, (Integer) rightOperand);
        }
        else if (leftOperand instanceof Tuple && rightOperand instanceof Tuple) { // Equals, Equivalent, NotEqual
            result = e.doOperation((Tuple) leftOperand, (Tuple) rightOperand);
        }
        else if (leftOperand == null && rightOperand == null) { // Equivalent, Equals
            result = e.getClass().getSimpleName().equals("EquivalentEvaluator") ? true : null;
        }
        else if (leftOperand == null) { // Equivalent, Equals
            result = null;
        }
        else if (rightOperand == null) { // Equivalent, Equals
            result = null;
        }
        else {
            if (uniformTypeCheck(leftOperand, rightOperand)) {
                result = null;
            }
            else
                result = new IllegalArgumentException(String.format("Could not execute %s with types %s and %s", e.getClass().getSimpleName(),
                        leftOperand.getClass().getSimpleName(), rightOperand.getClass().getSimpleName()));
        }

        if (doReport) {
            report.reportOperation(e.getClass().getSimpleName(), leftOperand, rightOperand, result);
        }

        return result;
    }

    public static Object resolveComparisonDoOperation(Executable e, Object operand1, Object operand2, Object operand3) {
        Object result;
        if (operand1 instanceof Integer && operand2 instanceof Integer && operand3 instanceof Integer ) { // Between
            result = e.doOperation((Integer) operand1, (Integer) operand2, (Integer) operand3);
        }
        else if (operand1 instanceof BigDecimal && operand2 instanceof BigDecimal && operand3 instanceof BigDecimal ) { // Between
            result = e.doOperation((BigDecimal) operand1, (BigDecimal) operand2, (BigDecimal) operand3);
        }
        else if (operand1 instanceof Quantity && operand2 instanceof Quantity && operand3 instanceof Quantity ) { // Between
            result = e.doOperation((Quantity) operand1, (Quantity) operand2, (Quantity) operand3);
        }
        else if (operand1 instanceof DateTime && operand2 instanceof DateTime && operand3 instanceof DateTime ) { // Between
            result = e.doOperation((DateTime) operand1, (DateTime) operand2, (DateTime) operand3);
        }
        else if (operand1 instanceof Time && operand2 instanceof Time && operand3 instanceof Time ) { // Between
            result = e.doOperation((Time) operand1, (Time) operand2, (Time) operand3);
        }
        else if (operand1 instanceof String && operand2 instanceof String && operand3 instanceof String ) { // Between
            result = e.doOperation((String) operand1, (String) operand2, (String) operand3);
        }
        else
            result = new IllegalArgumentException(String.format("Could not execute %s with types %s and %s and %s", e.getClass().getSimpleName(),
                operand1.getClass().getSimpleName(), operand2.getClass().getSimpleName(), operand3.getClass().getSimpleName()));

        if (doReport) {
            report.reportOperation(e.getClass().getSimpleName(), operand1, operand2, operand3, result);
        }

        return result;
    }

    public static Object resolveConditionalDoOperation(Executable e, Object operand) {
        Object result;
        if (operand instanceof Expression) { // Case
            result = e.doOperation((Expression) operand);
        }
        else if (operand instanceof Boolean) { // If
            result = e.doOperation((Boolean) operand);
        }
        else if (operand == null) { // Case
            result = e.doOperation((Expression) operand);
        }
        else
            result = new IllegalArgumentException(String.format("Could not execute %s with type %s", e.getClass().getSimpleName(),
                operand.getClass().getSimpleName()));

        if (doReport) {
            report.reportOperation(e.getClass().getSimpleName(), operand, result);
        }

        return result;
    }

    public static Object resolveDateTimeDoOperation(Executable e) {
        Object result = e.doOperation();

        if (doReport) {
            report.reportOperation(e.getClass().getSimpleName(), result);
        }

        return result;
    }

    public static Object resolveDateTimeDoOperation(Executable e, Object operand) {
        Object result;
        if (operand instanceof DateTime) { // (Date/Time)ComponentFrom
            result = e.doOperation((DateTime) operand);
        }
        else if (operand instanceof Time) { // (Date/Time)ComponentFrom
            result = e.doOperation((Time) operand);
        }
        else if (operand instanceof Integer) { // DateTime, Time
            result = e.doOperation((Integer) operand);
        }
        else if (operand == null) { // Now, TimeOfDay, Today
            result = e.doOperation();
        }
        else
            result = new IllegalArgumentException(String.format("Could not execute %s with type %s", e.getClass().getSimpleName(),
                operand.getClass().getSimpleName()));

        if (doReport) {
            report.reportOperation(e.getClass().getSimpleName(), operand, result);
        }

        return result;
    }

    public static Object resolveDateTimeDoOperation(Executable e, Object leftOperand, Object rightOperand) {
        Object result;
        if (leftOperand instanceof DateTime && rightOperand instanceof DateTime) { // Difference, Duration,
            result = e.doOperation((DateTime) leftOperand, (DateTime) rightOperand); // SameAs, SameOrAfter, SameOrBefore
        }
        else if (leftOperand instanceof Time && rightOperand instanceof Time) {    // Difference, Duration, SameAs,
            result = e.doOperation((Time) leftOperand, (Time) rightOperand); // SameOrAfter, SameOrBefore
        }
        else if (leftOperand instanceof Integer && rightOperand instanceof Integer) { // DateTime, Time
            result = e.doOperation((Integer) leftOperand, (Integer) rightOperand);
        }
        else
            result = new IllegalArgumentException(String.format("Could not execute %s with types %s and %s", e.getClass().getSimpleName(),
                leftOperand.getClass().getSimpleName(), rightOperand.getClass().getSimpleName()));

        if (doReport) {
            report.reportOperation(e.getClass().getSimpleName(), leftOperand, rightOperand, result);
        }

        return result;
    }

    public static Object resolveDateTimeDoOperation(Executable e, Object operand1, Object operand2, Object operand3) {
        Object result;
        if (operand1 instanceof Integer && operand2 instanceof Integer && operand3 instanceof Integer ) { // DateTime, Time
            result = e.doOperation((Integer) operand1, (Integer) operand2, (Integer) operand3);
        }
        else
            result = new IllegalArgumentException(String.format("Could not execute %s with types %s and %s and %s", e.getClass().getSimpleName(),
                operand1.getClass().getSimpleName(), operand2.getClass().getSimpleName(), operand3.getClass().getSimpleName()));

        if (doReport) {
            report.reportOperation(e.getClass().getSimpleName(), operand1, operand2, operand3, result);
        }

        return result;
    }

    public static Object resolveDateTimeDoOperation(Executable e, Object operand1, Object operand2, Object operand3, Object operand4)
    {
        Object result;
        if (operand1 instanceof Integer && operand2 instanceof Integer
                && operand3 instanceof Integer && operand4 instanceof Integer) { // DateTime, Time
            result = e.doOperation((Integer) operand1, (Integer) operand2, (Integer) operand3, (Integer) operand4);
        }
        else
            result = new IllegalArgumentException(String.format(
                    "Could not execute %s with types %s and %s and %s and %s", e.getClass().getSimpleName(),
                    operand1.getClass().getSimpleName(), operand2.getClass().getSimpleName(), operand3.getClass().getSimpleName(),
                    operand4.getClass().getSimpleName()));

        if (doReport) {
            report.reportOperation(e.getClass().getSimpleName(), operand1, operand2, operand3, operand4, result);
        }

        return result;
    }

    public static Object resolveDateTimeDoOperation(Executable e, Object operand1, Object operand2,
                                                     Object operand3, Object operand4, Object operand5) {
        Object result;
        if (operand1 instanceof Integer && operand2 instanceof Integer
                && operand3 instanceof Integer && operand4 instanceof Integer && operand5 instanceof Integer) { // DateTime
            result = e.doOperation((Integer) operand1, (Integer) operand2,
                    (Integer) operand3, (Integer) operand4, (Integer) operand5);
        }
        else if (operand1 instanceof Integer && operand2 instanceof Integer
                && operand3 instanceof Integer && operand4 instanceof Integer && operand5 instanceof BigDecimal) { // Time
            result = e.doOperation((Integer) operand1, (Integer) operand2,
                    (Integer) operand3, (Integer) operand4, (BigDecimal) operand5);
        }
        else
            result = new IllegalArgumentException(String.format(
                    "Could not execute %s with types %s and %s and %s and %s and %s", e.getClass().getSimpleName(),
                    operand1.getClass().getSimpleName(), operand2.getClass().getSimpleName(), operand3.getClass().getSimpleName(),
                    operand4.getClass().getSimpleName(), operand5.getClass().getSimpleName()));

        if (doReport) {
            report.reportOperation(e.getClass().getSimpleName(), operand1, operand2, operand3, operand4, operand5, result);
        }

        return result;
    }

    public static Object resolveDateTimeDoOperation(Executable e, Object operand1, Object operand2,
                                                    Object operand3, Object operand4, Object operand5, Object operand6) {
        Object result;
        if (operand1 instanceof Integer && operand2 instanceof Integer
                && operand3 instanceof Integer && operand4 instanceof Integer
                && operand5 instanceof Integer && operand6 instanceof Integer) { // DateTime
            result = e.doOperation((Integer) operand1, (Integer) operand2,
                    (Integer) operand3, (Integer) operand4, (Integer) operand5,
                    (Integer) operand6);
        }
        else
            result = new IllegalArgumentException(String.format(
                    "Could not execute %s with types %s and %s and %s and %s and %s and %s", e.getClass().getSimpleName(),
                    operand1.getClass().getSimpleName(), operand2.getClass().getSimpleName(), operand3.getClass().getSimpleName(),
                    operand4.getClass().getSimpleName(), operand5.getClass().getSimpleName(), operand6.getClass().getSimpleName()));

        if (doReport) {
            report.reportOperation(e.getClass().getSimpleName(), operand1,
                    operand2, operand3, operand4, operand5, operand6, result);
        }

        return result;
    }

    public static Object resolveDateTimeDoOperation(Executable e, Object operand1, Object operand2,
                                                    Object operand3, Object operand4, Object operand5,
                                                    Object operand6, Object operand7) {
        Object result;
        if (operand1 instanceof Integer && operand2 instanceof Integer
                && operand3 instanceof Integer && operand4 instanceof Integer
                && operand5 instanceof Integer && operand6 instanceof Integer
                && operand7 instanceof Integer) { // DateTime
            result = e.doOperation((Integer) operand1, (Integer) operand2,
                    (Integer) operand3, (Integer) operand4, (Integer) operand5,
                    (Integer) operand6, (Integer) operand7);
        }
        else
            result = new IllegalArgumentException(String.format(
                    "Could not execute %s with types %s and %s and %s and %s and %s and %s and %s", e.getClass().getSimpleName(),
                    operand1.getClass().getSimpleName(), operand2.getClass().getSimpleName(), operand3.getClass().getSimpleName(),
                    operand4.getClass().getSimpleName(), operand5.getClass().getSimpleName(), operand6.getClass().getSimpleName(),
                    operand7.getClass().getSimpleName()));

        if (doReport) {
            report.reportOperation(e.getClass().getSimpleName(), operand1,
                    operand2, operand3, operand4, operand5, operand6, operand7, result);
        }

        return result;
    }

    public static Object resolveDateTimeDoOperation(Executable e, Object operand1, Object operand2,
                                                    Object operand3, Object operand4, Object operand5,
                                                    Object operand6, Object operand7, Object operand8) {
        Object result;
        if (operand1 instanceof Integer && operand2 instanceof Integer
                && operand3 instanceof Integer && operand4 instanceof Integer
                && operand5 instanceof Integer && operand6 instanceof Integer
                && operand7 instanceof Integer && operand8 instanceof BigDecimal) { // DateTime
            result = e.doOperation((Integer) operand1, (Integer) operand2,
                    (Integer) operand3, (Integer) operand4, (Integer) operand5,
                    (Integer) operand6, (Integer) operand7, (BigDecimal) operand8);
        }
        else
            result = new IllegalArgumentException(String.format(
                    "Could not execute %s with types %s and %s and %s and %s and %s and %s and %s and %s", e.getClass().getSimpleName(),
                    operand1.getClass().getSimpleName(), operand2.getClass().getSimpleName(), operand3.getClass().getSimpleName(),
                    operand4.getClass().getSimpleName(), operand5.getClass().getSimpleName(), operand6.getClass().getSimpleName(),
                    operand7.getClass().getSimpleName(), operand8.getClass().getSimpleName()));

        if (doReport) {
            report.reportOperation(e.getClass().getSimpleName(), operand1, operand2,
                    operand3, operand4, operand5, operand6, operand7, operand8, result);
        }

        return result;
    }

    public static Object resolveIntervalDoOperation(Executable e, Object operand) {
        Object result;
        if (operand instanceof Interval) { // End, Start, Width
            result = e.doOperation((Interval) operand);
        }
        else if (operand instanceof Iterable) { // Collapse
            result = e.doOperation((Iterable<Object>) operand);
        }
        else
            result = new IllegalArgumentException(String.format("Could not execute %s with type %s", e.getClass().getSimpleName(),
                    operand.getClass().getSimpleName()));

        if (doReport) {
            report.reportOperation(e.getClass().getSimpleName(), operand, result);
        }

        return result;
    }

    public static Object resolveIntervalDoOperation(Executable e, Object leftOperand, Object rightOperand) {
        Object result;
        if (leftOperand instanceof Interval && rightOperand instanceof Interval) { // Ends, Intersect, Meets, Overlaps
            result = e.doOperation((Interval) leftOperand, (Interval) rightOperand); // Starts, Union
        }
        else
            result = new IllegalArgumentException(String.format("Could not execute %s with types %s and %s", e.getClass().getSimpleName(),
                    leftOperand.getClass().getSimpleName(), rightOperand.getClass().getSimpleName()));

        if (doReport) {
            report.reportOperation(e.getClass().getSimpleName(), leftOperand, rightOperand, result);
        }

        return result;
    }

    public static Object resolveListDoOperation(Executable e, Object operand) {
        Object result;
        if (operand instanceof Iterable) { // Distinct, Exists, Flatten, First, Last, SingletonFrom
            result = e.doOperation((Iterable) operand);
        }
        else
            result = new IllegalArgumentException(String.format("Could not execute %s with type %s", e.getClass().getSimpleName(),
                    operand.getClass().getSimpleName()));

        if (doReport) {
            report.reportOperation(e.getClass().getSimpleName(), operand, result);
        }

        return result;
    }

    public static Object resolveListDoOperation(Executable e, Object leftOperand, Object rightOperand) {
        Object result;
        if (leftOperand instanceof Iterable && rightOperand instanceof Iterable) { // Intersect, Union
            result = e.doOperation((Iterable<Object>) leftOperand, (Iterable<Object>) rightOperand);
        }
        else if (leftOperand instanceof Iterable) { // IndexOf
            result = e.doOperation((Iterable<Object>) leftOperand, rightOperand);
        }
        else
            result = new IllegalArgumentException(String.format("Could not execute %s with types %s and %s", e.getClass().getSimpleName(),
                    leftOperand.getClass().getSimpleName(), rightOperand.getClass().getSimpleName()));

        if (doReport) {
            report.reportOperation(e.getClass().getSimpleName(), leftOperand, rightOperand, result);
        }

        return result;
    }

    public static Object resolveSharedDoOperation(Executable e, Object operand) {
        Object result;
        if (operand instanceof Iterable) { // Length
            result = e.doOperation((Iterable) operand);
        }
        else if (operand instanceof String) { // Length
            result = e.doOperation((String) operand);
        }
        else
            result = new IllegalArgumentException(String.format("Could not execute %s with type %s", e.getClass().getSimpleName(),
                    operand.getClass().getSimpleName()));

        if (doReport) {
            report.reportOperation(e.getClass().getSimpleName(), operand, result);
        }

        return result;
    }

    public static Object resolveSharedDoOperation(Executable e, Object leftOperand, Object rightOperand) {
        Object result;
        if (leftOperand instanceof Interval && rightOperand instanceof Interval) { // After, Before, Except
            result = e.doOperation((Interval) leftOperand, (Interval) rightOperand); // (P)Includes, (P)IncludedIn, Intersect
        }
        else if (leftOperand instanceof Iterable && rightOperand instanceof Iterable) { // Except, (P)Includes, (P)IncludedIn, Intersect
            result = e.doOperation((Iterable<Object>) leftOperand, (Iterable<Object>) rightOperand);
        }
        else if (leftOperand instanceof Iterable && rightOperand instanceof Integer) { // Indexer
            result = e.doOperation((Iterable<Object>) leftOperand, (Integer) rightOperand);
        }
        else if (leftOperand instanceof Iterable) { // IndexOf
            result = e.doOperation((Iterable<Object>) leftOperand, rightOperand);
        }
        else if (rightOperand instanceof Iterable) { // In, Contains
            result = e.doOperation(leftOperand, (Iterable<Object>) rightOperand);
        }
        else if (leftOperand instanceof Interval) { // After, Before, Contains
            result = e.doOperation((Interval) leftOperand, rightOperand);
        }
        else if (rightOperand instanceof Interval) { // After, Before, In
            result = e.doOperation(leftOperand, (Interval) rightOperand);
        }
        else if (leftOperand instanceof DateTime && rightOperand instanceof Quantity) { // Add, Subtract
            result = e.doOperation((DateTime) leftOperand, (Quantity) rightOperand);
        }
        else if (leftOperand instanceof Time && rightOperand instanceof Quantity) { // Add, Subtract
            result = e.doOperation((Time) leftOperand, (Quantity) rightOperand);
        }
        else if (leftOperand instanceof DateTime && rightOperand instanceof DateTime) { // After, Before
            result = e.doOperation((DateTime) leftOperand, (DateTime) rightOperand);
        }
        else if (leftOperand instanceof Time && rightOperand instanceof Time) { // After, Before
            result = e.doOperation((Time) leftOperand, (Time) rightOperand);
        }
        else if (leftOperand instanceof String && rightOperand instanceof Integer) { // Indexer
            result = e.doOperation((String) leftOperand, (Integer) rightOperand);
        }
        else if (leftOperand instanceof String && rightOperand instanceof ValueSetRef) { // In (CodeSystem/ValueSet)
            result = e.doOperation((String) leftOperand, (ValueSetRef) rightOperand);
        }
        else if (leftOperand instanceof Code && rightOperand instanceof ValueSetRef) { // In (CodeSystem/ValueSet)
            result = e.doOperation((Code) leftOperand, (ValueSetRef) rightOperand);
        }
        else if (leftOperand instanceof Concept && rightOperand instanceof ValueSetRef) { // In (CodeSystem/ValueSet)
            result = e.doOperation((Concept) leftOperand, (ValueSetRef) rightOperand);
        }
        else if (leftOperand instanceof String && rightOperand instanceof CodeSystemRef) { // In (CodeSystem/ValueSet)
            result = e.doOperation((String) leftOperand, (CodeSystemRef) rightOperand);
        }
        else if (leftOperand instanceof Code && rightOperand instanceof CodeSystemRef) { // In (CodeSystem/ValueSet)
            result = e.doOperation((Code) leftOperand, (CodeSystemRef) rightOperand);
        }
        else if (leftOperand instanceof Concept && rightOperand instanceof CodeSystemRef) { // In (CodeSystem/ValueSet)
            result = e.doOperation((Concept) leftOperand, (CodeSystemRef) rightOperand);
        }
        else if (leftOperand instanceof Integer && rightOperand instanceof Integer) { // Add, Subtract
            result = e.doOperation((Integer) leftOperand, (Integer) rightOperand);
        }
        else if (leftOperand instanceof BigDecimal && rightOperand instanceof BigDecimal) { // Add, Subtract
            result = e.doOperation((BigDecimal) leftOperand, (BigDecimal) rightOperand);
        }
        else if (leftOperand instanceof Quantity && rightOperand instanceof Quantity) { // Add, Subtract
            result = e.doOperation((Quantity) leftOperand, (Quantity) rightOperand);
        }
        else if (leftOperand instanceof Uncertainty && rightOperand instanceof Uncertainty) { // Add, Subtract
            result = e.doOperation((Uncertainty) leftOperand, (Uncertainty) rightOperand);
        }
        else
            result = new IllegalArgumentException(String.format("Could not execute %s with types %s and %s", e.getClass().getSimpleName(),
                    leftOperand.getClass().getSimpleName(), rightOperand.getClass().getSimpleName()));

        if (doReport) {
            report.reportOperation(e.getClass().getSimpleName(), leftOperand, rightOperand, result);
        }

        return result;
    }

    public static Object resolveLogicalDoOperation(Executable e, Object operand) {
        Object result;
        if (operand instanceof Boolean) { // Implies, Not
            result = e.doOperation((Boolean) operand);
        }
        else if (operand == null) {
            result = e.doOperation((Boolean) operand);
        }
        else
            result = new IllegalArgumentException(String.format("Could not execute %s with type %s", e.getClass().getSimpleName(),
                    operand.getClass().getSimpleName()));

        if (doReport) {
            report.reportOperation(e.getClass().getSimpleName(), operand, result);
        }

        return result;
    }

    public static Object resolveLogicalDoOperation(Executable e, Object leftOperand, Object rightOperand) {
        Object result;
        if (leftOperand instanceof Boolean && rightOperand instanceof Boolean) { // And, Or, Xor
            result = e.doOperation((Boolean) leftOperand, (Boolean) rightOperand);
        }
        else if (leftOperand == null) { // Or, And
            result = e.doOperation((Boolean) leftOperand, (Boolean) rightOperand);
        }
        else if (rightOperand == null) { // Or, And
            result = e.doOperation((Boolean) leftOperand, (Boolean) rightOperand);
        }
        else
            result = new IllegalArgumentException(String.format("Could not execute %s with types %s and %s", e.getClass().getSimpleName(),
                    leftOperand.getClass().getSimpleName(), rightOperand.getClass().getSimpleName()));

        if (doReport) {
            report.reportOperation(e.getClass().getSimpleName(), leftOperand, rightOperand, result);
        }

        return result;
    }

    public static Object resolveNullogicalDoOperation(Executable e, Object operand) {
        Object result;
        if (operand instanceof Boolean) { // IsNull, IsFalse, IsTrue
            result = e.doOperation((Boolean) operand);
        }
        else if (operand instanceof BigDecimal) { // IsNull
            result = e.doOperation((BigDecimal) operand);
        }
        else if (operand instanceof Code) { // IsNull
            result = e.doOperation((Code) operand);
        }
        else if (operand instanceof Concept) { // IsNull
            result = e.doOperation((Concept) operand);
        }
        else if (operand instanceof DateTime) { // IsNull
            result = e.doOperation((DateTime) operand);
        }
        else if (operand instanceof Integer) { // IsNull
            result = e.doOperation((Integer) operand);
        }
        else if (operand instanceof Interval) { // IsNull
            result = e.doOperation((Interval) operand);
        }
        else if (operand instanceof Iterable) { // IsNull, Coalesce
            result = e.doOperation((Iterable<Object>) operand);
        }
        else if (operand instanceof Quantity) { // IsNull
            result = e.doOperation((Quantity) operand);
        }
        else if (operand instanceof String) { // IsNull
            result = e.doOperation((String) operand);
        }
        else if (operand instanceof Time) { // IsNull
            result = e.doOperation((Time) operand);
        }
        else if (operand == null) { // IsNull
            result = e.doOperation((Boolean) operand);
        }
        else
            result = new IllegalArgumentException(String.format("Could not execute %s with type %s", e.getClass().getSimpleName(),
                    operand.getClass().getSimpleName()));

        if (doReport) {
            report.reportOperation(e.getClass().getSimpleName(), operand, result);
        }

        return result;
    }

    public static Object resolveStringDoOperation(Executable e, Object operand) {
        Object result;
        if (operand instanceof String) { // Lower, Upper
            result = e.doOperation((String) operand);
        }
        else
            result = new IllegalArgumentException(String.format("Could not execute %s with type %s", e.getClass().getSimpleName(),
                    operand.getClass().getSimpleName()));

        if (doReport) {
            report.reportOperation(e.getClass().getSimpleName(), operand, result);
        }

        return result;
    }

    public static Object resolveStringDoOperation(Executable e, Object leftOperand, Object rightOperand) {
        Object result;
        if (leftOperand instanceof String && rightOperand instanceof String) { // Concatenate, PositionOf, Split
            result = e.doOperation((String) leftOperand, (String) rightOperand);
        }
        else if (leftOperand instanceof Iterable && rightOperand instanceof String) { // Combine
            result = e.doOperation((Iterable<Object>) leftOperand, (String) rightOperand);
        }
        else if (leftOperand instanceof String && rightOperand instanceof Integer) { // Substring
            result = e.doOperation((String) leftOperand, (Integer) rightOperand);
        }
        else if (leftOperand instanceof String && rightOperand == null) { // Split
            result = e.doOperation((String) leftOperand, (String) rightOperand);
        }
        else
            result = new IllegalArgumentException(String.format("Could not execute %s with types %s and %s", e.getClass().getSimpleName(),
                    leftOperand.getClass().getSimpleName(), rightOperand.getClass().getSimpleName()));

        if (doReport) {
            report.reportOperation(e.getClass().getSimpleName(), leftOperand, rightOperand, result);
        }

        return result;
    }

    public static Object resolveStringDoOperation(Executable e, Object operand1, Object operand2, Object operand3) {
        Object result;
        if (operand1 instanceof String && operand2 instanceof Integer && operand3 instanceof Integer ) { // Substring
            result = e.doOperation((String) operand1, (Integer) operand2, (Integer) operand3);
        }
        else
            result = new IllegalArgumentException(String.format("Could not execute %s with types %s and %s and %s", e.getClass().getSimpleName(),
                    operand1.getClass().getSimpleName(), operand2.getClass().getSimpleName(), operand3.getClass().getSimpleName()));

        if (doReport) {
            report.reportOperation(e.getClass().getSimpleName(), operand1, operand2, operand3, result);
        }

        return result;
    }

    public static Object resolveTypeDoOperation(Executable e, Object operand) {
        Object result;
        if (operand instanceof Boolean) { // As, Convert, Is, ToString
            result = e.doOperation((Boolean) operand);
        }
        else if (operand instanceof BigDecimal) { // As, Convert, Is, ToString
            result = e.doOperation((BigDecimal) operand);
        }
        else if (operand instanceof Code) { // As, Convert, Is, ToConcept
            result = e.doOperation((Code) operand);
        }
        else if (operand instanceof Concept) { // As, Convert, Is
            result = e.doOperation((Concept) operand);
        }
        else if (operand instanceof DateTime) { // As, Convert, Is, ToString
            result = e.doOperation((DateTime) operand);
        }
        else if (operand instanceof Integer) { // As, Convert, Is, ToString
            result = e.doOperation((Integer) operand);
        }
        else if (operand instanceof Interval) { // As, Convert, Is
            result = e.doOperation((Interval) operand);
        }
        else if (operand instanceof Iterable) { // As, Convert, Is
            result = e.doOperation((Iterable<Object>) operand);
        }
        else if (operand instanceof Quantity) { // As, Convert, Is, ToString
            result = e.doOperation((Quantity) operand);
        }
        else if (operand instanceof String) {       // As, Convert, Is, ToBoolean, ToDateTime, ToDecimal, ToInteger,
            result = e.doOperation((String) operand); // ToQuantity, ToTime
        }
        else if (operand instanceof Time) { // As, Convert, Is, ToString
            result = e.doOperation((Time) operand);
        }
        else
            result = new IllegalArgumentException(String.format("Could not execute %s with type %s", e.getClass().getSimpleName(),
                    operand.getClass().getSimpleName()));

        if (doReport) {
            report.reportOperation(e.getClass().getSimpleName(), operand, result);
        }

        return result;
    }
}
