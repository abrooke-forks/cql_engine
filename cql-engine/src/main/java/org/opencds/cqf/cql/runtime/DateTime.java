package org.opencds.cqf.cql.runtime;

import org.apache.commons.lang3.time.DateUtils;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalQuery;
import java.util.Calendar;
import java.util.Date;

public class DateTime extends BaseTemporal implements CqlType {

    public static final int YEAR_RANGE_MAX = 9999;
    public static final int YEAR_RANGE_MIN = 0001;

    private final DateTimeFormatter formatter = new DateTimeFormatterBuilder()
            .appendPattern("u[/M[/d['T'H[:m[:s[.SSS]]]]]][XXX]")
            .parseDefaulting(ChronoField.MONTH_OF_YEAR, 1)
            .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
            .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
            .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
            .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
            .toFormatter();

    public DateTime(Integer year, Integer month, Integer day,
                    Integer hour, Integer minute, Integer second,
                    Integer milli, BigDecimal offset)
    {
        StringBuilder dateString = new StringBuilder();
        precision = Precision.YEAR;
        decimalOffset = offset == null ? BigDecimal.ZERO : offset;

        if (year != null) {
            dateString.append(year.toString());
            if (month != null) {
                dateString.append("/").append(month.toString());
                precision = Precision.MONTH;
                if (day != null) {
                    dateString.append("/").append(day.toString());
                    precision = Precision.DAY;

                    resolveTimeString(hour, minute, second, milli, dateString);
                }
            }
        }

        resolveOffset(offset, dateString);

        accessor = formatter.parse(dateString);
    }

    public DateTime(String dateString) {
        dateString = resolveStringOffset(dateString).replaceAll("-", "/");

        // determine the precision
        precision = Precision.YEAR;
        String[] dateAndTime = dateString.split("T");
        if (dateAndTime.length > 1) {
            precision = Precision.getPrecisionFromFieldNoWeeks(
                    dateAndTime[0].split("/").length
                            + dateAndTime[1].split(":").length - 1
                            + (dateAndTime[1].contains(".") ? 1 : 0)
            );
        }
        else {
            precision = Precision.getPrecisionFromFieldNoWeeks(dateString.split("/").length - 1);
        }

        accessor = formatter.parse(dateString);
    }

    public Date getDate() {
        return Date.from(accessor.query(DateTimeQueries.instant()));
    }

    @Override
    public Integer compare(BaseTemporal other, Boolean forSort) {
        int comparison = DateUtils.truncatedCompareTo(
                getDate(),
                ((DateTime) other).getDate(),
                precision.getHighestPrecision(other.precision)
        );

        if (!this.precision.equals(other.precision)) {
            return forSort && comparison == 0
                    ? (this.precision.getCalendarFieldIndex() > other.precision.getCalendarFieldIndex() ? 1 : -1)
                    : null;
        }

        return comparison;
    }

    @Override
    public Boolean equivalent(Object other) {
        return this.precision.equals(((BaseTemporal) other).precision)
                && getDate().equals(((DateTime) other).getDate());
    }

    @Override
    public Boolean equal(Object other) {
        return !this.precision.equals(((BaseTemporal) other).precision) ? null
                : getDate().equals(((DateTime) other).getDate());
    }

    @Override
    public String toString() {
        return this.accessor.query(DateTimeQueries.instant()).toString();
    }

    // DateTime helper functions
    public Integer compareToPrecision(DateTime other, Precision precision) {
        if (this.precision.field < precision.field && other.precision.field < precision.field) {
            return null;
        }
        return atPrecision(other, precision) ? DateUtils.truncatedCompareTo(getDate(), other.getDate(), precision.getCalendarFieldIndex()) : null;
    }

    public DateTime getDateTimeAtPrecision(Date date, Precision precision, BigDecimal offset) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        switch (precision) {
            case YEAR:
                return new DateTime(
                    calendar.get(Calendar.YEAR), null, null,
                        null, null, null, null, offset
                );
            case MONTH:
                return new DateTime(
                        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), null,
                        null, null, null, null, offset
                );
            case DAY:
                return new DateTime(
                        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                        null, null, null, null, offset
                );
            case HOUR:
                return new DateTime(
                        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                        calendar.get(Calendar.HOUR_OF_DAY), null, null, null, offset
                );
            case MINUTE:
                return new DateTime(
                        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                        calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), null, null, offset
                );
            case SECOND:
                return new DateTime(
                        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                        calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND),
                        null, offset
                );
            case MILLI:
                return new DateTime(
                        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                        calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND),
                        calendar.get(Calendar.MILLISECOND), offset);
        }

        throw new IllegalArgumentException("Unexpected error occurred during DateTime creation at precision " + precision.name());
    }

    public DateTime getMinimumDateTimeAtPrecision(DateTime dateTime, Precision precision, BigDecimal offset) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateTime.getDate());

        switch (precision) {
            case YEAR:
                return dateTime;
            case MONTH:
                switch (dateTime.precision) {
                    case YEAR:
                        return new DateTime(
                                calendar.get(Calendar.YEAR), calendar.getActualMinimum(Calendar.MONTH), calendar.getActualMinimum(Calendar.DAY_OF_MONTH),
                                calendar.getActualMinimum(Calendar.HOUR_OF_DAY), calendar.getActualMinimum(Calendar.MINUTE), calendar.getActualMinimum(Calendar.SECOND),
                                calendar.getActualMinimum(Calendar.MILLISECOND), offset
                        );
                }
            case DAY:
                switch (dateTime.precision) {
                    case YEAR:
                        return new DateTime(
                                calendar.get(Calendar.YEAR), calendar.getActualMinimum(Calendar.MONTH), calendar.getActualMinimum(Calendar.DAY_OF_MONTH),
                                calendar.getActualMinimum(Calendar.HOUR_OF_DAY), calendar.getActualMinimum(Calendar.MINUTE), calendar.getActualMinimum(Calendar.SECOND),
                                calendar.getActualMinimum(Calendar.MILLISECOND), offset
                        );
                    case MONTH:
                        return new DateTime(
                                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.getActualMinimum(Calendar.DAY_OF_MONTH),
                                calendar.getActualMinimum(Calendar.HOUR_OF_DAY), calendar.getActualMinimum(Calendar.MINUTE), calendar.getActualMinimum(Calendar.SECOND),
                                calendar.getActualMinimum(Calendar.MILLISECOND), offset
                        );
                }
            case HOUR:
                switch (dateTime.precision) {
                    case YEAR:
                        return new DateTime(
                                calendar.get(Calendar.YEAR), calendar.getActualMinimum(Calendar.MONTH), calendar.getActualMinimum(Calendar.DAY_OF_MONTH),
                                calendar.getActualMinimum(Calendar.HOUR_OF_DAY), calendar.getActualMinimum(Calendar.MINUTE), calendar.getActualMinimum(Calendar.SECOND),
                                calendar.getActualMinimum(Calendar.MILLISECOND), offset
                        );
                    case MONTH:
                        return new DateTime(
                                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.getActualMinimum(Calendar.DAY_OF_MONTH),
                                calendar.getActualMinimum(Calendar.HOUR_OF_DAY), calendar.getActualMinimum(Calendar.MINUTE), calendar.getActualMinimum(Calendar.SECOND),
                                calendar.getActualMinimum(Calendar.MILLISECOND), offset
                        );
                    case DAY:
                        return new DateTime(
                                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                                calendar.getActualMinimum(Calendar.HOUR_OF_DAY), calendar.getActualMinimum(Calendar.MINUTE), calendar.getActualMinimum(Calendar.SECOND),
                                calendar.getActualMinimum(Calendar.MILLISECOND), offset
                        );
                }
            case MINUTE:
                switch (dateTime.precision) {
                    case YEAR:
                        return new DateTime(
                                calendar.get(Calendar.YEAR), calendar.getActualMinimum(Calendar.MONTH), calendar.getActualMinimum(Calendar.DAY_OF_MONTH),
                                calendar.getActualMinimum(Calendar.HOUR_OF_DAY), calendar.getActualMinimum(Calendar.MINUTE), calendar.getActualMinimum(Calendar.SECOND),
                                calendar.getActualMinimum(Calendar.MILLISECOND), offset
                        );
                    case MONTH:
                        return new DateTime(
                                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.getActualMinimum(Calendar.DAY_OF_MONTH),
                                calendar.getActualMinimum(Calendar.HOUR_OF_DAY), calendar.getActualMinimum(Calendar.MINUTE), calendar.getActualMinimum(Calendar.SECOND),
                                calendar.getActualMinimum(Calendar.MILLISECOND), offset
                        );
                    case DAY:
                        return new DateTime(
                                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                                calendar.getActualMinimum(Calendar.HOUR_OF_DAY), calendar.getActualMinimum(Calendar.MINUTE), calendar.getActualMinimum(Calendar.SECOND),
                                calendar.getActualMinimum(Calendar.MILLISECOND), offset
                        );
                    case HOUR:
                        return new DateTime(
                                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                                calendar.get(Calendar.HOUR_OF_DAY), calendar.getActualMinimum(Calendar.MINUTE), calendar.getActualMinimum(Calendar.SECOND),
                                calendar.getActualMinimum(Calendar.MILLISECOND), offset
                        );
                }
            case SECOND:
                switch (dateTime.precision) {
                    case YEAR:
                        return new DateTime(
                                calendar.get(Calendar.YEAR), calendar.getActualMinimum(Calendar.MONTH), calendar.getActualMinimum(Calendar.DAY_OF_MONTH),
                                calendar.getActualMinimum(Calendar.HOUR_OF_DAY), calendar.getActualMinimum(Calendar.MINUTE), calendar.getActualMinimum(Calendar.SECOND),
                                calendar.getActualMinimum(Calendar.MILLISECOND), offset
                        );
                    case MONTH:
                        return new DateTime(
                                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.getActualMinimum(Calendar.DAY_OF_MONTH),
                                calendar.getActualMinimum(Calendar.HOUR_OF_DAY), calendar.getActualMinimum(Calendar.MINUTE), calendar.getActualMinimum(Calendar.SECOND),
                                calendar.getActualMinimum(Calendar.MILLISECOND), offset
                        );
                    case DAY:
                        return new DateTime(
                                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                                calendar.getActualMinimum(Calendar.HOUR_OF_DAY), calendar.getActualMinimum(Calendar.MINUTE), calendar.getActualMinimum(Calendar.SECOND),
                                calendar.getActualMinimum(Calendar.MILLISECOND), offset
                        );
                    case HOUR:
                        return new DateTime(
                                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                                calendar.get(Calendar.HOUR_OF_DAY), calendar.getActualMinimum(Calendar.MINUTE), calendar.getActualMinimum(Calendar.SECOND),
                                calendar.getActualMinimum(Calendar.MILLISECOND), offset
                        );
                    case MINUTE:
                        return new DateTime(
                                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.getActualMinimum(Calendar.SECOND),
                                calendar.getActualMinimum(Calendar.MILLISECOND), offset
                        );
                }
            case MILLI:
                switch (dateTime.precision) {
                    case YEAR:
                        return new DateTime(
                                calendar.get(Calendar.YEAR), calendar.getActualMinimum(Calendar.MONTH), calendar.getActualMinimum(Calendar.DAY_OF_MONTH),
                                calendar.getActualMinimum(Calendar.HOUR_OF_DAY), calendar.getActualMinimum(Calendar.MINUTE), calendar.getActualMinimum(Calendar.SECOND),
                                calendar.getActualMinimum(Calendar.MILLISECOND), offset
                        );
                    case MONTH:
                        return new DateTime(
                                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.getActualMinimum(Calendar.DAY_OF_MONTH),
                                calendar.getActualMinimum(Calendar.HOUR_OF_DAY), calendar.getActualMinimum(Calendar.MINUTE), calendar.getActualMinimum(Calendar.SECOND),
                                calendar.getActualMinimum(Calendar.MILLISECOND), offset
                        );
                    case DAY:
                        return new DateTime(
                                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                                calendar.getActualMinimum(Calendar.HOUR_OF_DAY), calendar.getActualMinimum(Calendar.MINUTE), calendar.getActualMinimum(Calendar.SECOND),
                                calendar.getActualMinimum(Calendar.MILLISECOND), offset
                        );
                    case HOUR:
                        return new DateTime(
                                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                                calendar.get(Calendar.HOUR_OF_DAY), calendar.getActualMinimum(Calendar.MINUTE), calendar.getActualMinimum(Calendar.SECOND),
                                calendar.getActualMinimum(Calendar.MILLISECOND), offset
                        );
                    case MINUTE:
                        return new DateTime(
                                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.getActualMinimum(Calendar.SECOND),
                                calendar.getActualMinimum(Calendar.MILLISECOND), offset
                        );
                    case SECOND:
                        return new DateTime(
                                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND),
                                calendar.getActualMinimum(Calendar.MILLISECOND), offset
                        );
                }
            default:
                throw new IllegalArgumentException("Unexpected error occurred during DateTime creation at precision " + precision.name());
        }
    }

    public DateTime getMaximumDateTimeAtPrecision(DateTime dateTime, Precision precision, BigDecimal offset) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateTime.getDate());

        switch (precision) {
            case YEAR:
                return dateTime;
            case MONTH:
                switch (dateTime.precision) {
                    case YEAR:
                        return new DateTime(
                                calendar.get(Calendar.YEAR), calendar.getActualMaximum(Calendar.MONTH), calendar.getActualMaximum(Calendar.DAY_OF_MONTH),
                                calendar.getActualMaximum(Calendar.HOUR_OF_DAY), calendar.getActualMaximum(Calendar.MINUTE), calendar.getActualMaximum(Calendar.SECOND),
                                calendar.getActualMaximum(Calendar.MILLISECOND), offset
                        );
                }
            case DAY:
                switch (dateTime.precision) {
                    case YEAR:
                        return new DateTime(
                                calendar.get(Calendar.YEAR), calendar.getActualMaximum(Calendar.MONTH), calendar.getActualMaximum(Calendar.DAY_OF_MONTH),
                                calendar.getActualMaximum(Calendar.HOUR_OF_DAY), calendar.getActualMaximum(Calendar.MINUTE), calendar.getActualMaximum(Calendar.SECOND),
                                calendar.getActualMaximum(Calendar.MILLISECOND), offset
                        );
                    case MONTH:
                        return new DateTime(
                                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.getActualMaximum(Calendar.DAY_OF_MONTH),
                                calendar.getActualMaximum(Calendar.HOUR_OF_DAY), calendar.getActualMaximum(Calendar.MINUTE), calendar.getActualMaximum(Calendar.SECOND),
                                calendar.getActualMaximum(Calendar.MILLISECOND), offset
                        );
                }
            case HOUR:
                switch (dateTime.precision) {
                    case YEAR:
                        return new DateTime(
                                calendar.get(Calendar.YEAR), calendar.getActualMaximum(Calendar.MONTH), calendar.getActualMaximum(Calendar.DAY_OF_MONTH),
                                calendar.getActualMaximum(Calendar.HOUR_OF_DAY), calendar.getActualMaximum(Calendar.MINUTE), calendar.getActualMaximum(Calendar.SECOND),
                                calendar.getActualMaximum(Calendar.MILLISECOND), offset
                        );
                    case MONTH:
                        return new DateTime(
                                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.getActualMaximum(Calendar.DAY_OF_MONTH),
                                calendar.getActualMaximum(Calendar.HOUR_OF_DAY), calendar.getActualMaximum(Calendar.MINUTE), calendar.getActualMaximum(Calendar.SECOND),
                                calendar.getActualMaximum(Calendar.MILLISECOND), offset
                        );
                    case DAY:
                        return new DateTime(
                                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                                calendar.getActualMaximum(Calendar.HOUR_OF_DAY), calendar.getActualMaximum(Calendar.MINUTE), calendar.getActualMaximum(Calendar.SECOND),
                                calendar.getActualMaximum(Calendar.MILLISECOND), offset
                        );
                }
            case MINUTE:
                switch (dateTime.precision) {
                    case YEAR:
                        return new DateTime(
                                calendar.get(Calendar.YEAR), calendar.getActualMaximum(Calendar.MONTH), calendar.getActualMaximum(Calendar.DAY_OF_MONTH),
                                calendar.getActualMaximum(Calendar.HOUR_OF_DAY), calendar.getActualMaximum(Calendar.MINUTE), calendar.getActualMaximum(Calendar.SECOND),
                                calendar.getActualMaximum(Calendar.MILLISECOND), offset
                        );
                    case MONTH:
                        return new DateTime(
                                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.getActualMaximum(Calendar.DAY_OF_MONTH),
                                calendar.getActualMaximum(Calendar.HOUR_OF_DAY), calendar.getActualMaximum(Calendar.MINUTE), calendar.getActualMaximum(Calendar.SECOND),
                                calendar.getActualMaximum(Calendar.MILLISECOND), offset
                        );
                    case DAY:
                        return new DateTime(
                                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                                calendar.getActualMaximum(Calendar.HOUR_OF_DAY), calendar.getActualMaximum(Calendar.MINUTE), calendar.getActualMaximum(Calendar.SECOND),
                                calendar.getActualMaximum(Calendar.MILLISECOND), offset
                        );
                    case HOUR:
                        return new DateTime(
                                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                                calendar.get(Calendar.HOUR_OF_DAY), calendar.getActualMaximum(Calendar.MINUTE), calendar.getActualMaximum(Calendar.SECOND),
                                calendar.getActualMaximum(Calendar.MILLISECOND), offset
                        );
                }
            case SECOND:
                switch (dateTime.precision) {
                    case YEAR:
                        return new DateTime(
                                calendar.get(Calendar.YEAR), calendar.getActualMaximum(Calendar.MONTH), calendar.getActualMaximum(Calendar.DAY_OF_MONTH),
                                calendar.getActualMaximum(Calendar.HOUR_OF_DAY), calendar.getActualMaximum(Calendar.MINUTE), calendar.getActualMaximum(Calendar.SECOND),
                                calendar.getActualMaximum(Calendar.MILLISECOND), offset
                        );
                    case MONTH:
                        return new DateTime(
                                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.getActualMaximum(Calendar.DAY_OF_MONTH),
                                calendar.getActualMaximum(Calendar.HOUR_OF_DAY), calendar.getActualMaximum(Calendar.MINUTE), calendar.getActualMaximum(Calendar.SECOND),
                                calendar.getActualMaximum(Calendar.MILLISECOND), offset
                        );
                    case DAY:
                        return new DateTime(
                                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                                calendar.getActualMaximum(Calendar.HOUR_OF_DAY), calendar.getActualMaximum(Calendar.MINUTE), calendar.getActualMaximum(Calendar.SECOND),
                                calendar.getActualMaximum(Calendar.MILLISECOND), offset
                        );
                    case HOUR:
                        return new DateTime(
                                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                                calendar.get(Calendar.HOUR_OF_DAY), calendar.getActualMaximum(Calendar.MINUTE), calendar.getActualMaximum(Calendar.SECOND),
                                calendar.getActualMaximum(Calendar.MILLISECOND), offset
                        );
                    case MINUTE:
                        return new DateTime(
                                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.getActualMaximum(Calendar.SECOND),
                                calendar.getActualMaximum(Calendar.MILLISECOND), offset
                        );
                }
            case MILLI:
                switch (dateTime.precision) {
                    case YEAR:
                        return new DateTime(
                                calendar.get(Calendar.YEAR), calendar.getActualMaximum(Calendar.MONTH), calendar.getActualMaximum(Calendar.DAY_OF_MONTH),
                                calendar.getActualMaximum(Calendar.HOUR_OF_DAY), calendar.getActualMaximum(Calendar.MINUTE), calendar.getActualMaximum(Calendar.SECOND),
                                calendar.getActualMaximum(Calendar.MILLISECOND), offset
                        );
                    case MONTH:
                        return new DateTime(
                                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.getActualMaximum(Calendar.DAY_OF_MONTH),
                                calendar.getActualMaximum(Calendar.HOUR_OF_DAY), calendar.getActualMaximum(Calendar.MINUTE), calendar.getActualMaximum(Calendar.SECOND),
                                calendar.getActualMaximum(Calendar.MILLISECOND), offset
                        );
                    case DAY:
                        return new DateTime(
                                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                                calendar.getActualMaximum(Calendar.HOUR_OF_DAY), calendar.getActualMaximum(Calendar.MINUTE), calendar.getActualMaximum(Calendar.SECOND),
                                calendar.getActualMaximum(Calendar.MILLISECOND), offset
                        );
                    case HOUR:
                        return new DateTime(
                                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                                calendar.get(Calendar.HOUR_OF_DAY), calendar.getActualMaximum(Calendar.MINUTE), calendar.getActualMaximum(Calendar.SECOND),
                                calendar.getActualMaximum(Calendar.MILLISECOND), offset
                        );
                    case MINUTE:
                        return new DateTime(
                                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.getActualMaximum(Calendar.SECOND),
                                calendar.getActualMaximum(Calendar.MILLISECOND), offset
                        );
                    case SECOND:
                        return new DateTime(
                                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND),
                                calendar.getActualMaximum(Calendar.MILLISECOND), offset
                        );
                }
            default:
                throw new IllegalArgumentException("Unexpected error occurred during DateTime creation at precision " + precision.name());
        }
    }

    // DateTime operations
    public DateTime add(Quantity quantity) {
        Date date;

        switch (this.precision) {
            case YEAR: date = DateUtils.addYears(getDate(), this.precision.getQuantityForPrecision(quantity));
                break;
            case MONTH: date = DateUtils.addMonths(getDate(), this.precision.getQuantityForPrecision(quantity));
                break;
            case DAY: date = DateUtils.addDays(getDate(), this.precision.getQuantityForPrecision(quantity));
                break;
            case HOUR: date = DateUtils.addHours(getDate(), this.precision.getQuantityForPrecision(quantity));
                break;
            case MINUTE: date = DateUtils.addMinutes(getDate(), this.precision.getQuantityForPrecision(quantity));
                break;
            case SECOND: date = DateUtils.addSeconds(getDate(), this.precision.getQuantityForPrecision(quantity));
                break;
            default: date = DateUtils.addMilliseconds(getDate(), this.precision.getQuantityForPrecision(quantity));
                break;
        }

        return getDateTimeAtPrecision(date, precision, decimalOffset);
    }

    public Boolean after(DateTime other, Precision precision) {
        if (precision.getCalendarFieldIndex() > this.getPrecision().getCalendarFieldIndex()
                && precision.getCalendarFieldIndex() > other.getPrecision().getCalendarFieldIndex())
        {
            return null;
        }

        int comparison = DateUtils.truncatedCompareTo(other.getDate(), this.getDate(), precision.getCalendarFieldIndex());
        if (this.precision != other.precision && comparison == 0) {
            return null;
        }

        return comparison > 0;
    }

    public Object differenceBetween(DateTime other, String precision) {
        Precision p = Precision.toPrecision(precision);

        if (this.precision.field < p.field && other.precision.field < p.field) {
            return null;
        }

        if (this.precision.field < p.field) {
            return new Interval(
                    getMinimumDateTimeAtPrecision(this, p, decimalOffset).differenceBetween(other, precision), true,
                    getMaximumDateTimeAtPrecision(this, p, decimalOffset).differenceBetween(other, precision), true
            );
        }

        if (other.precision.field < p.field) {
            return new Interval(
                    this.differenceBetween(getMinimumDateTimeAtPrecision(other, p, decimalOffset), precision), true,
                    this.differenceBetween(getMaximumDateTimeAtPrecision(other, p, decimalOffset), precision), true
            );
        }

        return Duration.between(
                getDateTimeAtPrecision(getDate(), p, this.decimalOffset).getDate().toInstant(),
                getDateTimeAtPrecision(other.getDate(), p, other.decimalOffset).getDate().toInstant()
        ).get(p.getChronoUnitIndex());
    }

    public Object durationBetween(DateTime other, String precision) {
        Precision p = Precision.toPrecision(precision);

        if (this.precision.field < p.field && other.precision.field < p.field) {
            return null;
        }

        if (this.precision.field < p.field) {
            return new Interval(
                    getMinimumDateTimeAtPrecision(this, p, decimalOffset).durationBetween(other, precision), true,
                    getMaximumDateTimeAtPrecision(this, p, decimalOffset).durationBetween(other, precision), true
            );
        }

        if (other.precision.field < p.field) {
            return new Interval(
                    this.durationBetween(getMinimumDateTimeAtPrecision(other, p, decimalOffset), precision), true,
                    this.durationBetween(getMaximumDateTimeAtPrecision(other, p, decimalOffset), precision), true
            );
        }

        return Duration.between(getDate().toInstant(), other.getDate().toInstant()).get(p.getChronoUnitIndex());
    }

    public Boolean sameAs(DateTime other, String precision) {
        Precision p = Precision.toPrecision(precision);
        Integer comparison = compareToPrecision(other, p);

        return comparison == null ? null : comparison == 0;
    }

    public Boolean sameOrAfter(DateTime other, String precision) {
        Precision p = Precision.toPrecision(precision);
        Integer comparison = compareToPrecision(other, p);

        return comparison == null ? null : comparison > 0;
    }

    public Boolean sameOrBefore(DateTime other, String precision) {
        Precision p = Precision.toPrecision(precision);
        Integer comparison = compareToPrecision(other, p);

        return comparison == null ? null : comparison < 0;
    }

    public static DateTime now() {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        return new DateTime(
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND),
                calendar.get(Calendar.MILLISECOND), null
        );
    }
}

class DateTimeQueries {

    public static TemporalQuery<LocalDateTime> localDateTime() {
        return DateTimeQueries.LOCAL_DATE_TIME;
    }

    public static TemporalQuery<Instant> instant() {
        return DateTimeQueries.INSTANT;
    }

    private static final TemporalQuery<LocalDateTime> LOCAL_DATE_TIME = LocalDateTime::from;

    private static final TemporalQuery<Instant> INSTANT = Instant::from;
}