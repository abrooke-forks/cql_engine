package org.opencds.cqf.cql.execution;

import org.joda.time.*;
import org.opencds.cqf.cql.elm.execution.CalculateAgeAtEvaluator;
import org.opencds.cqf.cql.elm.execution.CalculateAgeEvaluator;
import org.opencds.cqf.cql.elm.execution.DurationBetweenEvaluator;
import org.opencds.cqf.cql.elm.execution.Execution;
import org.opencds.cqf.cql.runtime.DateTime;
import org.opencds.cqf.cql.runtime.Interval;
import org.opencds.cqf.cql.runtime.Uncertainty;
import org.testng.annotations.Test;

import javax.xml.bind.JAXBException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

public class CqlClinicalOperatorsTest extends CqlExecutionTestBase {

    @Test
    public void testAge() throws JAXBException {
        Context context = new Context(library);
        Object result;
    }

    @Test
    public void testAgeAt() throws JAXBException {
      Context context = new Context(library);
      Object result;
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.CalculateAge#evaluate(Context)}
     */
    @Test
    public void testCalculateAge() throws JAXBException {
        Context context = new Context(library);

        LocalDate today = LocalDate.now();
        LocalDate birthday = new LocalDate(2000, 1, 1);
        Years years = Years.yearsBetween(birthday, today);
        Months months = Months.monthsBetween(birthday, today);
        Days days = Days.daysBetween(birthday, today);
        Hours hours = Hours.hoursBetween(birthday, today);
        Minutes minutes = Minutes.minutesBetween(birthday, today);
        Seconds seconds = Seconds.secondsBetween(birthday, today);

        // TODO: fix this -- translation error
//         Object result = context.resolveExpressionRef("CalculateAgeYears").getExpression().evaluate(context);
//         assertThat(result, is(years.get(DurationFieldType.years())));

        Object result = context.resolveExpressionRef("CalculateAgeMonths").getExpression().evaluate(context);
        assertThat(result, is(months.get(DurationFieldType.months())));

        result = context.resolveExpressionRef("CalculateAgeDays").getExpression().evaluate(context);
        assertThat(result, is(days.get(DurationFieldType.days())));

        result = context.resolveExpressionRef("CalculateAgeHours").getExpression().evaluate(context);
        assertThat(result, is(hours.get(DurationFieldType.hours())));

        result = context.resolveExpressionRef("CalculateAgeMinutes").getExpression().evaluate(context);
        assertThat(result, is(minutes.get(DurationFieldType.minutes())));

        result = context.resolveExpressionRef("CalculateAgeSeconds").getExpression().evaluate(context);
        assertThat(result, is(seconds.get(DurationFieldType.seconds())));

        result = context.resolveExpressionRef("CalculateAgeUncertain").getExpression().evaluate(context);
        Integer low = (Integer) Execution.resolveDateTimeDoOperation(new DurationBetweenEvaluator().withPrecision("month"),
                new DateTime().withPartial(new Partial(DateTime.getFields(2), new int[] {2000, 12})), DateTime.getToday());
        Integer high = (Integer) Execution.resolveDateTimeDoOperation(new DurationBetweenEvaluator().withPrecision("month"),
                new DateTime().withPartial(new Partial(DateTime.getFields(2), new int[] {2000, 1})), DateTime.getToday());
        assertThat(((Uncertainty)result).getUncertaintyInterval().equal(new Interval(low, true, high, true)), is(true));
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.CalculateAgeAt#evaluate(Context)}
     */
    @Test
    public void testCalculateAgeAt() throws JAXBException {
        Context context = new Context(library);
        Object result = context.resolveExpressionRef("CalculateAgeAtYears").getExpression().evaluate(context);
        assertThat(result, is(17));

        result = context.resolveExpressionRef("CalculateAgeAtMonths").getExpression().evaluate(context);
        assertThat(result, is(197));

        result = context.resolveExpressionRef("CalculateAgeAtDays").getExpression().evaluate(context);
        assertThat(result, is(6038));

        result = context.resolveExpressionRef("CalculateAgeAtHours").getExpression().evaluate(context);
        assertThat(result, is(144912));

        result = context.resolveExpressionRef("CalculateAgeAtMinutes").getExpression().evaluate(context);
        assertThat(result, is(8694720));

        result = context.resolveExpressionRef("CalculateAgeAtSeconds").getExpression().evaluate(context);
        assertThat(result, is(521683200));

        result = context.resolveExpressionRef("CalculateAgeAtUncertain").getExpression().evaluate(context);
        assertThat(((Uncertainty)result).getUncertaintyInterval().equal(new Interval(187, true, 198, true)), is(true));
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.Equal#evaluate(Context)}
     */
    @Test
    public void testEqual() throws JAXBException {
        Context context = new Context(library);
        Object result = context.resolveExpressionRef("CodeEqualTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("CodeEqualFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("ConceptEqualTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("ConceptEqualFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("CodeEqualNull").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef("ConceptEqualNull").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.Equivalent#evaluate(Context)}
     */
    @Test
    public void testEquivalent() throws JAXBException {
        Context context = new Context(library);
        Object result = context.resolveExpressionRef("CodeEquivalentTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("CodeEquivalentFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("ConceptEquivalentTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("ConceptEquivalentFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("CodeEquivalentNull").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("ConceptEquivalentNull").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("CodeEquivalentNullTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("CodeEquivalentNullFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("ConceptEquivalentNullTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("ConceptEquivalentNullFalse").getExpression().evaluate(context);
        assertThat(result, is(false));
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.InCodeSystem#evaluate(Context)}
     */
    @Test
    public void testInCodesystem() throws JAXBException {
        // Tests in the fhir engine
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.ValueSetDef#evaluate(Context)}
     */
    @Test
    public void testInValueset() throws JAXBException {
        // Tests in the fhir engine
    }
}
