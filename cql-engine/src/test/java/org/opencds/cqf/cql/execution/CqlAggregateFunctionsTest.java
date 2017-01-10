package org.opencds.cqf.cql.execution;

import org.opencds.cqf.cql.elm.execution.Execution;
import org.opencds.cqf.cql.runtime.Quantity;
import org.testng.annotations.Test;
import javax.xml.bind.JAXBException;
import org.opencds.cqf.cql.runtime.DateTime;
import org.opencds.cqf.cql.runtime.Time;
import org.joda.time.Partial;
import org.w3._1999.xhtml.Big;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.comparesEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

@Test(groups = {"a"})
public class CqlAggregateFunctionsTest extends CqlExecutionTestBase {

    /**
     * {@link org.opencds.cqf.cql.elm.execution.AllTrue#evaluate(Context)}
     */
    @Test
    public void testAllTrue() throws JAXBException {
        Context context = new Context(library);
        Object result = context.resolveExpressionRef("AllTrueAllTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("AllTrueTrueFirst").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("AllTrueFalseFirst").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("AllTrueAllTrueFalseTrue").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("AllTrueAllFalseTrueFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("AllTrueNullFirst").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("AllTrueEmptyList").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef("AllTrueAllNull").getExpression().evaluate(context);
        assertThat(result, is(true));
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.AnyTrue#evaluate(Context)}
     */
    @Test
    public void testAnyTrue() throws JAXBException {
        Context context = new Context(library);
        Object result = context.resolveExpressionRef("AnyTrueAllTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("AnyTrueAllFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("AnyTrueAllTrueFalseTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("AnyTrueAllFalseTrueFalse").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("AnyTrueTrueFirst").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("AnyTrueFalseFirst").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("AnyTrueNullFirstThenTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("AnyTrueNullFirstThenFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("AnyTrueEmptyList").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef("AnyTrueAllNull").getExpression().evaluate(context);
        assertThat(result, is(false));
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.Avg#evaluate(Context)}
     */
    @Test
    public void testAvg() throws JAXBException {
        Context context = new Context(library);
        Object result = context.resolveExpressionRef("AvgTestDecimal").getExpression().evaluate(context);
        assertThat((BigDecimal) result, comparesEqualTo(new BigDecimal("3.0")));

        result = context.resolveExpressionRef("AvgTestQuantity").getExpression().evaluate(context);
        assertThat(((Quantity) result).getValue(), comparesEqualTo(new BigDecimal("82.95")));
        assertThat(((Quantity) result).getUnit(), comparesEqualTo("g"));

        result = context.resolveExpressionRef("AvgTestNull").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef("AvgTestNull2").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef("AvgTestDecimalWithNull").getExpression().evaluate(context);
        assertThat((BigDecimal) result, comparesEqualTo(new BigDecimal("3.0")));

        result = context.resolveExpressionRef("AvgTestQuantityWithNull").getExpression().evaluate(context);
        assertThat(((Quantity) result).getValue(), comparesEqualTo(new BigDecimal("82.95")));
        assertThat(((Quantity) result).getUnit(), comparesEqualTo("g"));
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.Count#evaluate(Context)}
     */
    @Test
    public void testCount() throws JAXBException {
        Context context = new Context(library);
        Object result = context.resolveExpressionRef("CountTest1").getExpression().evaluate(context);
        assertThat((Integer) result, comparesEqualTo(4));

        result = context.resolveExpressionRef("CountTestDateTime").getExpression().evaluate(context);
        assertThat((Integer) result, comparesEqualTo(3));

        result = context.resolveExpressionRef("CountTestTime").getExpression().evaluate(context);
        assertThat((Integer) result, comparesEqualTo(3));

        result = context.resolveExpressionRef("CountTestBoolean").getExpression().evaluate(context);
        assertThat((Integer) result, comparesEqualTo(4));

        result = context.resolveExpressionRef("CountTestList").getExpression().evaluate(context);
        assertThat((Integer) result, comparesEqualTo(3));

        result = context.resolveExpressionRef("CountTestAllNull").getExpression().evaluate(context);
        assertThat((Integer) result, comparesEqualTo(0));
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.Max#evaluate(Context)}
     */
    @Test
    public void testMax() throws JAXBException {
        Context context = new Context(library);
        Object result = context.resolveExpressionRef("MaxTestInteger").getExpression().evaluate(context);
        assertThat((Integer) result, comparesEqualTo(90));

        result = context.resolveExpressionRef("MaxTestDecimal").getExpression().evaluate(context);
        assertThat((BigDecimal) result, comparesEqualTo(new BigDecimal("90.9")));

        result = context.resolveExpressionRef("MaxTestString").getExpression().evaluate(context);
        assertThat((String) result, comparesEqualTo("zebra"));

        result = context.resolveExpressionRef("MaxTestDateTime").getExpression().evaluate(context);
        assertThat(((DateTime)result).getPartial(), comparesEqualTo(new Partial(DateTime.getFields(3), new int[] {2012, 10, 6})));

        result = context.resolveExpressionRef("MaxTestTime").getExpression().evaluate(context);
        assertThat(((Time)result).getPartial(), comparesEqualTo(new Partial(Time.getFields(4), new int[] {20, 59, 59, 999})));
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.Median#evaluate(Context)}
     */
    @Test
    public void testMedian() throws JAXBException {
        Context context = new Context(library);
        Object result = context.resolveExpressionRef("MedianTestDecimalEven").getExpression().evaluate(context);
        assertThat((BigDecimal) result, comparesEqualTo(new BigDecimal("3.5")));

        result = context.resolveExpressionRef("MedianTestQuantityEven").getExpression().evaluate(context);
        assertThat(((Quantity) result).getValue(), comparesEqualTo(new BigDecimal("3.5")));
        assertThat(((Quantity) result).getUnit(), comparesEqualTo("cm"));

        result = context.resolveExpressionRef("MedianTestDecimalOdd").getExpression().evaluate(context);
        assertThat((BigDecimal) result, comparesEqualTo(new BigDecimal("3.0")));

        result = context.resolveExpressionRef("MedianTestQuantityOdd").getExpression().evaluate(context);
        assertThat(((Quantity) result).getValue(), comparesEqualTo(new BigDecimal("3.0")));
        assertThat(((Quantity) result).getUnit(), comparesEqualTo("cm"));

        result = context.resolveExpressionRef("MedianTestNull").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.Min#evaluate(Context)}
     */
    @Test
    public void testMin() throws JAXBException {
        Context context = new Context(library);
        Object result = context.resolveExpressionRef("MinTestInteger").getExpression().evaluate(context);
        assertThat((Integer) result, comparesEqualTo(0));

        result = context.resolveExpressionRef("MinTestDecimal").getExpression().evaluate(context);
        assertThat((BigDecimal) result, comparesEqualTo(new BigDecimal("0.0")));

        result = context.resolveExpressionRef("MinTestString").getExpression().evaluate(context);
        assertThat((String) result, comparesEqualTo("bye"));

        result = context.resolveExpressionRef("MinTestDateTime").getExpression().evaluate(context);
        assertThat(((DateTime)result).getPartial(), comparesEqualTo(new Partial(DateTime.getFields(3), new int[] {2012, 9, 5})));

        result = context.resolveExpressionRef("MinTestTime").getExpression().evaluate(context);
        assertThat(((Time)result).getPartial(), comparesEqualTo(new Partial(Time.getFields(4), new int[] {5, 59, 59, 999})));
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.Mode#evaluate(Context)}
     */
    @Test
    public void testMode() throws JAXBException {
        Context context = new Context(library);
        Object result = context.resolveExpressionRef("ModeTestInteger").getExpression().evaluate(context);
        assertThat((Integer) result, comparesEqualTo(9));

        result = context.resolveExpressionRef("ModeTestDecimal").getExpression().evaluate(context);
        assertThat((BigDecimal) result, comparesEqualTo(new BigDecimal("12.2")));

        result = context.resolveExpressionRef("ModeTestNull").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef("ModeTestDateTime").getExpression().evaluate(context);
        assertThat(((DateTime)result).getPartial(), comparesEqualTo(new Partial(DateTime.getFields(3), new int[] {2012, 9, 5})));

        result = context.resolveExpressionRef("ModeTestTime").getExpression().evaluate(context);
        assertThat(((Time)result).getPartial(), comparesEqualTo(new Partial(Time.getFields(4), new int[] {5, 59, 59, 999})));

    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.StdDev#evaluate(Context)}
     */
    @Test
    public void testPopulationStdDev() throws JAXBException {
        Context context = new Context(library);
        Object result = context.resolveExpressionRef("PopStdDevTestDecimal").getExpression().evaluate(context);
        assertThat((BigDecimal) result, comparesEqualTo(new BigDecimal("1.41421356")));

        result = context.resolveExpressionRef("PopStdDevTestQuantity").getExpression().evaluate(context);
        assertThat(((Quantity) result).getValue(), comparesEqualTo(new BigDecimal("1.41421356")));
        assertThat(((Quantity) result).getUnit(), comparesEqualTo("g"));
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.PopulationVariance#evaluate(Context)}
     */
    @Test
    public void testPopulationVariance() throws JAXBException {
      Context context = new Context(library);
      Object result = context.resolveExpressionRef("PopVarianceTestDecimal").getExpression().evaluate(context);
      assertThat((BigDecimal) result, comparesEqualTo(new BigDecimal("2.0")));

        result = context.resolveExpressionRef("PopVarianceTestQuantity").getExpression().evaluate(context);
        assertThat(((Quantity) result).getValue(), comparesEqualTo(new BigDecimal("2.0")));
        assertThat(((Quantity) result).getUnit(), comparesEqualTo("g"));
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.StdDev#evaluate(Context)}
     */
    @Test
    public void testStdDev() throws JAXBException {
        Context context = new Context(library);
        Object result = context.resolveExpressionRef("StdDevTestDecimal").getExpression().evaluate(context);
        assertThat((BigDecimal) result, comparesEqualTo(new BigDecimal("1.58113883")));

        result = context.resolveExpressionRef("StdDevTestQuantity").getExpression().evaluate(context);
        assertThat(((Quantity) result).getValue(), comparesEqualTo(new BigDecimal("1.58113883")));
        assertThat(((Quantity) result).getUnit(), comparesEqualTo("mm"));
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.Sum#evaluate(Context)}
     */
    @Test
    public void testSum() throws JAXBException {
        Context context = new Context(library);
        Object result = context.resolveExpressionRef("SumTest1").getExpression().evaluate(context);
        assertThat((BigDecimal) result, comparesEqualTo(new BigDecimal("20.0")));

        result = context.resolveExpressionRef("SumTestNull").getExpression().evaluate(context);
        assertThat((Integer) result, comparesEqualTo(1));

        result = context.resolveExpressionRef("SumTestAllNull").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.Variance#evaluate(Context)}
     */
    @Test
    public void testVariance() throws JAXBException {
        Context context = new Context(library);
        Object result = context.resolveExpressionRef("VarianceTestDecimal").getExpression().evaluate(context);
        assertThat((BigDecimal) result, comparesEqualTo(new BigDecimal("2.5")));

        result = context.resolveExpressionRef("VarianceTestQuantity").getExpression().evaluate(context);
        assertThat(((Quantity) result).getValue(), comparesEqualTo(new BigDecimal("2.5")));
        assertThat(((Quantity) result).getUnit(), comparesEqualTo("cm"));
    }
}
