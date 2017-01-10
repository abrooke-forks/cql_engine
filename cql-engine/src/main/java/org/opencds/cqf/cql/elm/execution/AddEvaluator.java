package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.*;

import java.math.BigDecimal;
import java.math.RoundingMode;

/*
*** NOTES FOR ARITHMETIC OPERATORS ***
+(left Integer, right Integer) Integer
+(left Decimal, right Decimal) Decimal
+(left Quantity, right Quantity) Quantity

The add (+) operator performs numeric addition of its arguments.
When invoked with mixed Integer and Decimal arguments, the Integer argument will be implicitly converted to Decimal.
TODO: When adding quantities, the dimensions of each quantity must be the same, but not necessarily the unit.
    For example, units of 'cm' and 'm' can be added, but units of 'cm2' and    'cm' cannot.
        The unit of the result will be the most granular unit of either input.
If either argument is null, the result is null.


*** NOTES FOR DATETIME ***
+(left DateTime, right Quantity) DateTime
+(left Time, right Quantity) Time

The add (+) operator returns the value of the given date/time, incremented by the time-valued quantity,
    respecting variable length periods for calendar years and months.
For DateTime values, the quantity unit must be one of: years, months, days, hours, minutes, seconds, or milliseconds.
For Time values, the quantity unit must be one of: hours, minutes, seconds, or milliseconds.
The operation is performed by attempting to derive the highest granularity precision first, working down successive
    granularities to the granularity of the time-valued quantity. For example, the following addition:
        DateTime(2014) + 24 months
        This example results in the value DateTime(2016) even though the date/time value is not specified to the level
            of precision of the time-valued quantity.
        NOTE: this implementation (v3) returns a DateTime which truncates minimum element values until the original DateTime's precision is reached.
        For Example:
            DateTime(2014) + 735 days
                returns DateTime(2016)
If either argument is null, the result is null.
*/

/**
 * Created by Bryn on 5/25/2016 
 */
public class AddEvaluator extends org.cqframework.cql.elm.execution.Add {

    private static final int YEAR_RANGE_MAX = 9999;

    private BigDecimal verifyPrecision(BigDecimal operand) {
        if (operand.precision() > 8) {
            return operand.setScale(8, RoundingMode.FLOOR);
        }
        return operand;
    }

    @Override
    public Object doOperation(Integer leftOperand, Integer rightOperand) {
        return leftOperand + rightOperand;
    }

    @Override
    public Object doOperation(BigDecimal leftOperand, BigDecimal rightOperand) {
        return verifyPrecision(leftOperand.add(rightOperand));
    }

    @Override
    public Object doOperation(Quantity leftOperand, Quantity rightOperand) {
        return new Quantity().withValue(verifyPrecision(leftOperand.getValue().add(rightOperand.getValue()))).withUnit(leftOperand.getUnit());
    }

    @Override
    public Object doOperation(DateTime leftOperand, Quantity rightOperand) {
        int idx = DateTime.getFieldIndex2(rightOperand.getUnit());
        if (idx != -1) {
            int startSize = leftOperand.getPartial().size();
            // check that the Partial has the precision specified
            if (startSize < idx + 1) {
                leftOperand = DateTime.expandPartialMin(leftOperand, idx + 1);
            }
            // do the addition
            leftOperand.setPartial(leftOperand.getPartial().property(DateTime.getField(idx))
                    .addToCopy(rightOperand.getValue().intValue()));
            // truncate to original precision
            for (int i = idx; i >= startSize; --i) {
                leftOperand.setPartial(leftOperand.getPartial().without(DateTime.getField(i)));
            }
        }
        else {
            throw new IllegalArgumentException(String.format("Invalid duration unit: %s", rightOperand.getUnit()));
        }

        if (leftOperand.getPartial().getValue(0) > YEAR_RANGE_MAX) {
            throw new ArithmeticException("The date time addition results in a year greater than the accepted range.");
        }

        return leftOperand;
    }

    @Override
    public Object doOperation(Time leftOperand, Quantity rightOperand) {
        int idx = Time.getFieldIndex2(rightOperand.getUnit());

        if (idx != -1) {
            int startSize = leftOperand.getPartial().size();
            // check that the Partial has the precision specified
            if (startSize < idx + 1) {
                // expand the Partial to the proper precision
                Time.expandPartialMin(leftOperand, idx + 1);
            }
            // do the addition
            leftOperand.setPartial(
                    leftOperand.getPartial().property(Time.getField(idx))
                            .addToCopy(rightOperand.getValue().intValue()));
            // truncate to original precision
            for (int i = idx; i >= startSize; --i) {
                leftOperand.setPartial(leftOperand.getPartial().without(Time.getField(i)));
            }
        }

        else {
            throw new IllegalArgumentException(String.format("Invalid duration unit: %s", rightOperand.getUnit()));
        }

        return leftOperand;
    }

    // NOTE: this operation signature is not specified in the spec.
    // It is being used for uncertainty arithmetic
    @Override
    public Object doOperation(Uncertainty leftOperand, Uncertainty rightOperand) {
        Interval leftInterval = leftOperand.getUncertaintyInterval();
        Interval rightInterval = rightOperand.getUncertaintyInterval();
        return new Uncertainty().withUncertaintyInterval(new Interval(
                Execution.resolveSharedDoOperation(this, leftInterval.getStart(), rightInterval.getStart()), true,
                Execution.resolveSharedDoOperation(this, leftInterval.getEnd(), rightInterval.getEnd()), true));
    }

    @Override
    public Object evaluate(Context context) {
        Object left = getOperand().get(0).evaluate(context);
        Object right = getOperand().get(1).evaluate(context);

        if (left == null || right == null)
            return null;

        return Execution.resolveSharedDoOperation(this, left, right);
    }
}
