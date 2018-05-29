package org.opencds.cqf.cql.runtime;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.OffsetTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;

public class Time extends BaseTemporal implements CqlType {

    private final DateTimeFormatter formatter = new DateTimeFormatterBuilder()
            .appendPattern("'T'H[:m[:s[.SSS]]][XXX]")
            .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
            .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
            .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
            .toFormatter();

    public Time(Integer hour, Integer minute,
                Integer second, Integer milli, BigDecimal offset) {
        StringBuilder timeString = new StringBuilder();
        decimalOffset = offset == null ? BigDecimal.ZERO : offset;

        resolveTimeString(hour, minute, second, milli, timeString);
        resolveOffset(offset, timeString);

        accessor = formatter.parse(timeString.toString());
    }

    public Time (String timeString) {
        timeString = resolveStringOffset(timeString);

        precision = Precision.getPrecisionFromFieldNoWeeks(
                timeString.split(":").length + 2
                        + (timeString.contains(".") ? 1 : 0)
        );

        accessor = formatter.parse(timeString);
    }

    public OffsetTime getTime() {
        return OffsetTime.from(accessor);
    }

    @Override
    public Integer compare(BaseTemporal other, Boolean forSort) {
        int comparison = getTime().compareTo(((Time) other).getTime());

        if (!this.precision.equals(other.precision)) {
            return forSort ? comparison : null;
        }
        return comparison;
    }

    @Override
    public Boolean equivalent(Object other) {
        return this.precision.equals(((BaseTemporal) other).precision)
                && getTime().equals(((Time) other).getTime());
    }

    @Override
    public Boolean equal(Object other) {
        return !this.precision.equals(((BaseTemporal) other).precision) ? null
                : getTime().equals(((Time) other).getTime());
    }

    @Override
    public String toString() {
        return getTime().toString();
    }

    // Time helper functions
    public Time getTimeAtPrecision(OffsetTime time, Precision precision, BigDecimal offset) {
        switch (precision) {
            case HOUR:
                return new Time(time.getHour(), null, null, null, offset);
            case MINUTE:
                return new Time(time.getHour(), time.getMinute(), null, null, offset);
            case SECOND:
                return new Time(time.getHour(), time.getMinute(), time.getSecond(), null, offset);
            case MILLI:
                return new Time(time.getHour(), time.getMinute(), time.getSecond(), time.get(ChronoField.MILLI_OF_SECOND), offset);
        }
        throw new IllegalArgumentException("Unexpected error occurred during Time creation at precision " + precision.name());
    }

    public Integer compareField(Time other, Precision precision) {
        if (!this.atPrecision(other, precision)) {
            return null;
        }
        return getTime().get(precision.getChronoFieldIndex()) < other.getTime().get(precision.getChronoFieldIndex()) ? 1
                : getTime().get(precision.getChronoFieldIndex()) > other.getTime().get(precision.getChronoFieldIndex()) ? -1
                : 0;
    }

    public Integer compareToPrecision(Time other, Precision precision) {
        if (this.precision.field < precision.field && other.precision.field < precision.field) {
            return null;
        }

        Integer comparisonResult = null;
        for (int i = Precision.HOUR.field; i <= precision.field; ++i) {
            comparisonResult = this.compareField(other, Precision.getPrecisionFromField(i));
            if (comparisonResult == null) {
                return null;
            }
            if (comparisonResult != 0) {
                return comparisonResult;
            }
        }
        return comparisonResult;
    }

    public Time getMinimumTimeAtPrecision(Time time, Precision precision, BigDecimal offset) {
        OffsetTime offsetTime = time.getTime();
        switch (precision) {
            case HOUR: return time;
            case MINUTE:
                switch (this.precision) {
                    case HOUR:
                        return new Time(offsetTime.getHour(), 0, 0, 0, offset);
                }
            case SECOND:
                switch (this.precision) {
                    case HOUR:
                        return new Time(offsetTime.getHour(), 0, 0, 0, offset);
                    case MINUTE:
                        return new Time(offsetTime.getHour(), offsetTime.getMinute(), 0, 0, offset);
                }
            case MILLI:
                switch (this.precision) {
                    case HOUR:
                        return new Time(offsetTime.getHour(), 0, 0, 0, offset);
                    case MINUTE:
                        return new Time(offsetTime.getHour(), offsetTime.getMinute(), 0, 0, offset);
                    case SECOND:
                        return new Time(offsetTime.getHour(), offsetTime.getMinute(), offsetTime.getSecond(), 0, offset);
                }
            default:
                throw new IllegalArgumentException("Unexpected error occurred during DateTime creation at precision " + precision.name());
        }
    }

    public Time getMaximumTimeAtPrecision(Time time, Precision precision, BigDecimal offset) {
        OffsetTime offsetTime = time.getTime();
        switch (precision) {
            case HOUR: return time;
            case MINUTE:
                switch (this.precision) {
                    case HOUR:
                        return new Time(offsetTime.getHour(), 59, 59, 999, offset);
                }
            case SECOND:
                switch (this.precision) {
                    case HOUR:
                        return new Time(offsetTime.getHour(), 59, 59, 999, offset);
                    case MINUTE:
                        return new Time(offsetTime.getHour(), offsetTime.getMinute(), 59, 999, offset);
                }
            case MILLI:
                switch (this.precision) {
                    case HOUR:
                        return new Time(offsetTime.getHour(), 59, 59, 999, offset);
                    case MINUTE:
                        return new Time(offsetTime.getHour(), offsetTime.getMinute(), 59, 999, offset);
                    case SECOND:
                        return new Time(offsetTime.getHour(), offsetTime.getMinute(), offsetTime.getSecond(), 999, offset);
                }
            default:
                throw new IllegalArgumentException("Unexpected error occurred during DateTime creation at precision " + precision.name());
        }
    }

    // Time operations
    public Time add(Quantity quantity) {
        OffsetTime time = getTime();

        switch (this.precision) {
            case HOUR: time.plusHours(this.precision.getQuantityForPrecision(quantity));
                break;
            case MINUTE: time.plusMinutes(this.precision.getQuantityForPrecision(quantity));
                break;
            case SECOND: time.plusSeconds(this.precision.getQuantityForPrecision(quantity));
                break;
            default: time.plus(this.precision.getQuantityForPrecision(quantity), ChronoUnit.MILLIS);
                break;
        }

        return getTimeAtPrecision(time, precision, decimalOffset);
    }

    public Boolean after(Time other, Precision precision) {
        if (precision.getCalendarFieldIndex() > this.getPrecision().getCalendarFieldIndex()
                && precision.getCalendarFieldIndex() > other.getPrecision().getCalendarFieldIndex())
        {
            return null;
        }

        int comparison = getTimeAtPrecision(other.getTime(), precision, decimalOffset).getTime()
                .compareTo(getTimeAtPrecision(this.getTime(), precision, decimalOffset).getTime());
        if (this.precision != other.precision && comparison == 0) {
            return null;
        }

        return comparison > 0;
    }

    public Object differenceBetween(Time other, String precision) {
        Precision p = Precision.toPrecision(precision);

        if (this.precision.field < p.field && other.precision.field < p.field) {
            return null;
        }

        if (this.precision.field < p.field) {
            return new Interval(
                    getMinimumTimeAtPrecision(this, p, decimalOffset).differenceBetween(other, precision), true,
                    getMaximumTimeAtPrecision(this, p, decimalOffset).differenceBetween(other, precision), true
            );
        }

        if (other.precision.field < p.field) {
            return new Interval(
                    this.differenceBetween(getMinimumTimeAtPrecision(other, p, decimalOffset), precision), true,
                    this.differenceBetween(getMaximumTimeAtPrecision(other, p, decimalOffset), precision), true
            );
        }

        return Duration.between(
                Instant.from(getTimeAtPrecision(getTime(), p, decimalOffset).getTime()),
                Instant.from(getTimeAtPrecision(other.getTime(), p, other.decimalOffset).getTime())
        ).get(p.getChronoUnitIndex());
    }

    public Object durationBetween(Time other, String precision) {
        Precision p = Precision.toPrecision(precision);

        if (this.precision.field < p.field && other.precision.field < p.field) {
            return null;
        }

        if (this.precision.field < p.field) {
            return new Interval(
                    getMinimumTimeAtPrecision(this, p, decimalOffset).durationBetween(other, precision), true,
                    getMaximumTimeAtPrecision(this, p, decimalOffset).durationBetween(other, precision), true
            );
        }

        if (other.precision.field < p.field) {
            return new Interval(
                    this.durationBetween(getMinimumTimeAtPrecision(other, p, decimalOffset), precision), true,
                    this.durationBetween(getMaximumTimeAtPrecision(other, p, decimalOffset), precision), true
            );
        }

        return Duration.between(Instant.from(getTime()), Instant.from(other.getTime())).get(p.getChronoUnitIndex());
    }

    public Boolean sameAs(Time other, String precision) {
        Precision p = Precision.toPrecision(precision);
        Integer comparison = compareToPrecision(other, p);
        return comparison == null ? null : comparison == 0;
    }

    public Boolean sameOrAfter(Time other, String precision) {
        Precision p = Precision.toPrecision(precision);
        Integer comparison = compareToPrecision(other, p);
        return comparison == null ? null : comparison > 0;
    }

    public Boolean sameOrBefore(Time other, String precision) {
        Precision p = Precision.toPrecision(precision);
        Integer comparison = compareToPrecision(other, p);
        return comparison == null ? null : comparison < 0;
    }
}
