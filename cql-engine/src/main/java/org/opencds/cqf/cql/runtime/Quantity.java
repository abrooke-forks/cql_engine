package org.opencds.cqf.cql.runtime;

import javax.annotation.Nonnull;

import org.fhir.ucum.Decimal;
import org.fhir.ucum.Pair;
import org.fhir.ucum.UcumEssenceService;
import org.fhir.ucum.UcumException;
import org.opencds.cqf.cql.elm.execution.EqualEvaluator;
import org.opencds.cqf.cql.elm.execution.EquivalentEvaluator;

import java.math.BigDecimal;

/**
 * Created by Bryn on 4/15/2016.
 */
public class Quantity implements CqlType, Comparable<Quantity> {

    private UcumEssenceService ucumService;

    public Quantity() {
        this.value = new BigDecimal("0.0");
        this.unit = "";
        resolveUcumService();
    }

    public Quantity(BigDecimal value, String unit) {
        this.value = value;
        this.unit = unit;
        resolveUcumService();
    }

    public Quantity(double value, String unit) {
        this.value = new BigDecimal(value);
        this.unit = unit;
        resolveUcumService();
    }

    public Quantity(int value, String unit) {
        this.value = new BigDecimal(value);
        this.unit = unit;
        resolveUcumService();
    }

    private void resolveUcumService() {
        try {
            ucumService = new UcumEssenceService(UcumEssenceService.class.getResourceAsStream("/ucum-essence.xml"));
        } catch (UcumException ue) {
            ue.printStackTrace();
            throw new IllegalArgumentException("Error establishing UCUM service: " + ue.getMessage());
        }
    }

    private BigDecimal value;
    public BigDecimal getValue() {
        return value;
    }
    public void setValue(BigDecimal value) {
        this.value = value;
    }
    public Quantity withValue(BigDecimal value) {
        setValue(value);
        return this;
    }

    private String unit;
    public String getUnit() {
        return unit;
    }
    public void setUnit(String unit) {
        this.unit = unit;
    }
    public Quantity withUnit(String unit) {
        setUnit(unit);
        return this;
    }
    public Quantity withDefaultUnit() {
        setUnit("1");
        return this;
    }

    public Quantity convert(Quantity quantity, String newUnit) {
        try {
            quantity.withValue(
                        new BigDecimal(
                            ucumService.convert(
                                    new Decimal(quantity.value.toString()), quantity.unit, newUnit
                            ).asDecimal()
                        )
            )
                    .withUnit(this.unit);

        } catch (UcumException ue) {
            ue.printStackTrace();
            throw new IllegalArgumentException("Error converting " + quantity.unit + " to " + newUnit + ": " + ue.getMessage());
        }

        return quantity;
    }

    public Quantity normalize(Quantity quantity) {
        if (!this.unit.equals(quantity.unit)) {
            quantity = convert(quantity, this.unit);
        }
        return quantity;
    }

    public Quantity add(Quantity other) {
        other = normalize(other);
        return this.withValue(Value.verifyPrecision(this.value.add(other.value)));
    }

    public Quantity subtract(Quantity other) {
        other = normalize(other);
        return this.withValue(Value.verifyPrecision(this.value.subtract(other.value)));
    }

    public Quantity multiply(Quantity other) {
//        other = normalize(other);
        Pair result;
        try {
            result = ucumService.multiply(
                    new Pair(new Decimal(this.value.toString()), this.unit),
                    new Pair(new Decimal(other.value.toString()), other.unit)
            );
        } catch (UcumException ue) {
            ue.printStackTrace();
            throw new IllegalArgumentException("Error multiplying quantities " + this.toString() + " and " + other.toString() + ": " + ue.getMessage());
        }
        return this.withValue(Value.verifyPrecision(new BigDecimal(result.getValue().asDecimal()))).withUnit(result.getCode().isEmpty() ? "1" : result.getCode());
    }

    public Quantity divide(Quantity other) {
//        other = normalize(other);
//        TODO - root out all the possible edge cases with divide - seeing some strange goings on with UCUM service here
        if (this.unit.equals(other.unit)) {
            this.withUnit("1");
            other.withUnit("1");
        }
        Pair result;
        try {
            result = ucumService.multiply(
                    new Pair(new Decimal(this.value.toString()), this.unit),
                    new Pair(Decimal.one().divide(new Decimal(other.value.toString())), other.unit.equals("1") ? other.unit : "/" + other.unit)
            );
        } catch (UcumException ue) {
            ue.printStackTrace();
            throw new IllegalArgumentException("Error dividing quantities " + this.toString() + " and " + other.toString() + ": " + ue.getMessage());
        }
        return this.withValue(Value.verifyPrecision(new BigDecimal(result.getValue().asDecimal()))).withUnit(result.getCode().isEmpty() ? "1" : result.getCode());
    }

    @Override
    public int compareTo(@Nonnull Quantity other) {
        return this.getValue().compareTo(other.getValue());
    }

    @Override
    public Boolean equivalent(Object other) {
        return EquivalentEvaluator.equivalent(this.getValue(), ((Quantity) other).getValue())
                && EquivalentEvaluator.equivalent(this.getUnit(), ((Quantity) other).getUnit());
    }

    @Override
    public Boolean equal(Object other) {
        Boolean valueEqual = EqualEvaluator.equal(this.getValue(), ((Quantity) other).getValue());
        Boolean unitEqual = EqualEvaluator.equal(this.getUnit(), ((Quantity) other).getUnit());
        return valueEqual == null || unitEqual == null ? null : valueEqual && unitEqual;
    }

    @Override
    public String toString() {
        return String.format("%s %s", getValue().toString(), getUnit());
    }
}
