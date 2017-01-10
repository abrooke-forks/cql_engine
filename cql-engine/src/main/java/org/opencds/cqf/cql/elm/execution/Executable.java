package org.opencds.cqf.cql.elm.execution;

import org.apache.commons.lang3.NotImplementedException;
import org.cqframework.cql.elm.execution.CodeSystemRef;
import org.cqframework.cql.elm.execution.Expression;
import org.cqframework.cql.elm.execution.ValueSetRef;
import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.*;

import java.math.BigDecimal;

/**
 * Created by Bryn on 1/11/2016.
 */
public class Executable {

    public Object doOperation() {
        throw new NotImplementedException(String.format("evaluate not implemented for class %s",
                this.getClass().getSimpleName()));
    }

    public Object doOperation(Expression operand) {
        throw new NotImplementedException(String.format("evaluate not implemented for class %s",
                this.getClass().getSimpleName()));
    }

    public Object doOperation(Integer operand) {
        throw new NotImplementedException(String.format("evaluate not implemented for class %s",
                this.getClass().getSimpleName()));
    }

    public Object doOperation(BigDecimal operand) {
        throw new NotImplementedException(String.format("evaluate not implemented for class %s",
                this.getClass().getSimpleName()));
    }

    public Object doOperation(Quantity operand) {
        throw new NotImplementedException(String.format("evaluate not implemented for class %s",
                this.getClass().getSimpleName()));
    }

    public Object doOperation(DateTime operand) {
        throw new NotImplementedException(String.format("evaluate not implemented for class %s",
                this.getClass().getSimpleName()));
    }

    public Object doOperation(Time operand) {
        throw new NotImplementedException(String.format("evaluate not implemented for class %s",
                this.getClass().getSimpleName()));
    }

    public Object doOperation(String operand) {
        throw new NotImplementedException(String.format("evaluate not implemented for class %s",
                this.getClass().getSimpleName()));
    }

    public Object doOperation(Boolean operand) {
        throw new NotImplementedException(String.format("evaluate not implemented for class %s",
                this.getClass().getSimpleName()));
    }

    public Object doOperation(Code operand) {
        throw new NotImplementedException(String.format("evaluate not implemented for class %s",
                this.getClass().getSimpleName()));
    }

    public Object doOperation(Concept operand) {
        throw new NotImplementedException(String.format("evaluate not implemented for class %s",
                this.getClass().getSimpleName()));
    }

    public Object doOperation(Iterable<Object> operand) {
        throw new NotImplementedException(String.format("evaluate not implemented for class %s",
                this.getClass().getSimpleName()));
    }

    public Object doOperation(Interval operand) {
        throw new NotImplementedException(String.format("evaluate not implemented for class %s",
                this.getClass().getSimpleName()));
    }

    public Object doOperation(Integer leftOperand, Integer rightOperand) {
        throw new NotImplementedException(String.format("evaluate not implemented for class %s",
                this.getClass().getSimpleName()));
    }

    public Object doOperation(BigDecimal leftOperand, BigDecimal rightOperand) {
        throw new NotImplementedException(String.format("evaluate not implemented for class %s",
                this.getClass().getSimpleName()));
    }

    public Object doOperation(BigDecimal leftOperand, Quantity rightOperand) {
        throw new NotImplementedException(String.format("evaluate not implemented for class %s",
                this.getClass().getSimpleName()));
    }

    public Object doOperation(BigDecimal leftOperand, Integer rightOperand) {
        throw new NotImplementedException(String.format("evaluate not implemented for class %s",
                this.getClass().getSimpleName()));
    }

    public Object doOperation(Quantity leftOperand, Quantity rightOperand) {
        throw new NotImplementedException(String.format("evaluate not implemented for class %s",
                this.getClass().getSimpleName()));
    }

    public Object doOperation(Quantity leftOperand, BigDecimal rightOperand) {
        throw new NotImplementedException(String.format("evaluate not implemented for class %s",
                this.getClass().getSimpleName()));
    }

    public Object doOperation(DateTime leftOperand, DateTime rightOperand) {
        throw new NotImplementedException(String.format("evaluate not implemented for class %s",
                this.getClass().getSimpleName()));
    }

    public Object doOperation(Time leftOperand, Time rightOperand) {
        throw new NotImplementedException(String.format("evaluate not implemented for class %s",
                this.getClass().getSimpleName()));
    }

    public Object doOperation(Uncertainty leftOperand, Uncertainty rightOperand) {
        throw new NotImplementedException(String.format("evaluate not implemented for class %s",
                this.getClass().getSimpleName()));
    }

    public Object doOperation(String leftOperand, String rightOperand) {
        throw new NotImplementedException(String.format("evaluate not implemented for class %s",
                this.getClass().getSimpleName()));
    }

    public Object doOperation(Boolean leftOperand, Boolean rightOperand) {
        throw new NotImplementedException(String.format("evaluate not implemented for class %s",
                this.getClass().getSimpleName()));
    }

    public Object doOperation(Time leftOperand, Quantity rightOperand) {
        throw new NotImplementedException(String.format("evaluate not implemented for class %s",
                this.getClass().getSimpleName()));
    }

    public Object doOperation(DateTime leftOperand, Quantity rightOperand) {
        throw new NotImplementedException(String.format("evaluate not implemented for class %s",
                this.getClass().getSimpleName()));
    }

    public Object doOperation(Interval leftOperand, Interval rightOperand) {
        throw new NotImplementedException(String.format("evaluate not implemented for class %s",
                this.getClass().getSimpleName()));
    }

    public Object doOperation(Interval leftOperand, Object rightOperand) {
        throw new NotImplementedException(String.format("evaluate not implemented for class %s",
                this.getClass().getSimpleName()));
    }

    public Object doOperation(Object leftOperand, Interval rightOperand) {
        throw new NotImplementedException(String.format("evaluate not implemented for class %s",
                this.getClass().getSimpleName()));
    }

    public Object doOperation(Iterable<Object> leftOperand, Object rightOperand) {
        throw new NotImplementedException(String.format("evaluate not implemented for class %s",
                this.getClass().getSimpleName()));
    }

    public Object doOperation(Object rightOperand, Iterable<Object> leftOperand) {
        throw new NotImplementedException(String.format("evaluate not implemented for class %s",
                this.getClass().getSimpleName()));
    }

    public Object doOperation(Uncertainty leftOperand, Integer rightOperand) {
        throw new NotImplementedException(String.format("evaluate not implemented for class %s",
                this.getClass().getSimpleName()));
    }

    public Object doOperation(Iterable<Object> leftOperand, Iterable<Object> rightOperand) {
        throw new NotImplementedException(String.format("evaluate not implemented for class %s",
                this.getClass().getSimpleName()));
    }

    public Object doOperation(String leftOperand, Object rightOperand) {
        throw new NotImplementedException(String.format("evaluate not implemented for class %s",
                this.getClass().getSimpleName()));
    }

    public Object doOperation(Code leftOperand, Object rightOperand) {
        throw new NotImplementedException(String.format("evaluate not implemented for class %s",
                this.getClass().getSimpleName()));
    }

    public Object doOperation(Concept leftOperand, Object rightOperand) {
        throw new NotImplementedException(String.format("evaluate not implemented for class %s",
                this.getClass().getSimpleName()));
    }

    public Object doOperation(String leftOperand, Integer rightOperand) {
        throw new NotImplementedException(String.format("evaluate not implemented for class %s",
                this.getClass().getSimpleName()));
    }

    public Object doOperation(Iterable<Object> leftOperand, Integer rightOperand) {
        throw new NotImplementedException(String.format("evaluate not implemented for class %s",
                this.getClass().getSimpleName()));
    }

    public Object doOperation(Code leftOperand, Code rightOperand) {
        throw new NotImplementedException(String.format("evaluate not implemented for class %s",
                this.getClass().getSimpleName()));
    }

    public Object doOperation(Code leftOperand, ValueSetRef rightOperand) {
        throw new NotImplementedException(String.format("evaluate not implemented for class %s",
                this.getClass().getSimpleName()));
    }

    public Object doOperation(Concept leftOperand, Concept rightOperand) {
        throw new NotImplementedException(String.format("evaluate not implemented for class %s",
                this.getClass().getSimpleName()));
    }

    public Object doOperation(Concept leftOperand, ValueSetRef rightOperand) {
        throw new NotImplementedException(String.format("evaluate not implemented for class %s",
                this.getClass().getSimpleName()));
    }

    public Object doOperation(String leftOperand, ValueSetRef rightOperand) {
        throw new NotImplementedException(String.format("evaluate not implemented for class %s",
                this.getClass().getSimpleName()));
    }

    public Object doOperation(Code leftOperand, CodeSystemRef rightOperand) {
        throw new NotImplementedException(String.format("evaluate not implemented for class %s",
                this.getClass().getSimpleName()));
    }

    public Object doOperation(Concept leftOperand, CodeSystemRef rightOperand) {
        throw new NotImplementedException(String.format("evaluate not implemented for class %s",
                this.getClass().getSimpleName()));
    }

    public Object doOperation(Tuple leftOperand, Tuple rightOperand) {
        throw new NotImplementedException(String.format("evaluate not implemented for class %s",
                this.getClass().getSimpleName()));
    }

    public Object doOperation(String leftOperand, CodeSystemRef rightOperand) {
        throw new NotImplementedException(String.format("evaluate not implemented for class %s",
                this.getClass().getSimpleName()));
    }

    public Object doOperation(Iterable<Object> leftOperand, String rightOperand) {
        throw new NotImplementedException(String.format("evaluate not implemented for class %s",
                this.getClass().getSimpleName()));
    }

    public Object doOperation(Integer operand1, Integer operand2, Integer operand3) {
        throw new NotImplementedException(String.format("evaluate not implemented for class %s",
                this.getClass().getSimpleName()));
    }

    public Object doOperation(BigDecimal operand1, BigDecimal operand2, BigDecimal operand3) {
        throw new NotImplementedException(String.format("evaluate not implemented for class %s",
                this.getClass().getSimpleName()));
    }

    public Object doOperation(Quantity operand1, Quantity operand2, Quantity operand3) {
        throw new NotImplementedException(String.format("evaluate not implemented for class %s",
                this.getClass().getSimpleName()));
    }

    public Object doOperation(DateTime operand1, DateTime operand2, DateTime operand3) {
        throw new NotImplementedException(String.format("evaluate not implemented for class %s",
                this.getClass().getSimpleName()));
    }

    public Object doOperation(Time operand1, Time operand2, Time operand3) {
        throw new NotImplementedException(String.format("evaluate not implemented for class %s",
                this.getClass().getSimpleName()));
    }

    public Object doOperation(String operand1, String operand2, String operand3) {
        throw new NotImplementedException(String.format("evaluate not implemented for class %s",
                this.getClass().getSimpleName()));
    }

    public Object doOperation(String operand1, Integer operand2, Integer operand3) {
        throw new NotImplementedException(String.format("evaluate not implemented for class %s",
                this.getClass().getSimpleName()));
    }

    public Object doOperation(Integer operand1, Integer operand2, Integer operand3, Integer operand4) {
        throw new NotImplementedException(String.format("evaluate not implemented for class %s",
                this.getClass().getSimpleName()));
    }

    public Object doOperation(Integer operand1, Integer operand2, Integer operand3, Integer operand4, Integer operand5) {
        throw new NotImplementedException(String.format("evaluate not implemented for class %s",
                this.getClass().getSimpleName()));
    }

    public Object doOperation(Integer operand1, Integer operand2, Integer operand3, Integer operand4, BigDecimal operand5) {
        throw new NotImplementedException(String.format("evaluate not implemented for class %s",
                this.getClass().getSimpleName()));
    }

    public Object doOperation(Integer operand1, Integer operand2, Integer operand3,
                              Integer operand4, Integer operand5, Integer operand6) {
        throw new NotImplementedException(String.format("evaluate not implemented for class %s",
                this.getClass().getSimpleName()));
    }

    public Object doOperation(Integer operand1, Integer operand2, Integer operand3,
                              Integer operand4, Integer operand5, Integer operand6,
                              Integer operand7) {
        throw new NotImplementedException(String.format("evaluate not implemented for class %s",
                this.getClass().getSimpleName()));
    }

    public Object doOperation(Integer operand1, Integer operand2, Integer operand3,
                              Integer operand4, Integer operand5, Integer operand6,
                              Integer operand7, BigDecimal operand8) {
        throw new NotImplementedException(String.format("evaluate not implemented for class %s",
                this.getClass().getSimpleName()));
    }

    public Object evaluate(Context context) {
        throw new NotImplementedException(String.format("evaluate not implemented for class %s",
                this.getClass().getSimpleName()));
    }
}
