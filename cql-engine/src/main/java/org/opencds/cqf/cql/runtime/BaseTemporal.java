package org.opencds.cqf.cql.runtime;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.temporal.TemporalAccessor;

public abstract class BaseTemporal implements CqlType, Comparable<BaseTemporal> {

    TemporalAccessor accessor;
    public TemporalAccessor getAccessor() {
        return accessor;
    }

    ZoneOffset offset;
    public ZoneOffset getOffset() {
        return offset;
    }

    BigDecimal decimalOffset;
    public BigDecimal getDecimalOffset() {
        return decimalOffset;
    }

    Precision precision;
    public Precision getPrecision() {
        return precision;
    }

    void resolveTimeString(Integer hour, Integer minute, Integer second,
                                    Integer milli, StringBuilder timeString) {
        if (hour != null) {
            timeString.append("T").append(hour.toString());
            precision = Precision.HOUR;

            if (minute != null) {
                timeString.append(":").append(minute.toString());
                precision = Precision.MINUTE;

                if (second != null) {
                    timeString.append(":").append(second.toString());
                    precision = Precision.SECOND;

                    if (milli != null) {
                        // HACK - to get leading zeroes instead of trailing zeroes
                        String millis = milli.toString();
                        while (millis.length() < 3) {
                            millis = "0" + millis;
                        }
                        timeString.append(".").append(millis);
                        precision = Precision.MILLI;
                    }
                }
            }
        }
    }

    void resolveOffset(BigDecimal offset, StringBuilder temporalString) {
        if (offset != null) {
            this.offset = ZoneOffset.ofHoursMinutes(
                    offset.stripTrailingZeros().intValue(),
                    new BigDecimal(60).multiply(offset.remainder(BigDecimal.ONE)).intValue()
            );
            temporalString.append(this.offset);
        }
        else {
            decimalOffset = BigDecimal.ZERO;
            this.offset = ZoneOffset.systemDefault().getRules().getStandardOffset(Instant.now());
            temporalString.append(this.offset);
        }
    }

    String resolveStringOffset(String dateString) {
        if (dateString.matches(".*(\\+|-)\\d+:\\d+")) {
            String offsetString;
            if (dateString.contains("+")) {
                offsetString = dateString.split("\\+")[1];
            }
            else {
                offsetString = dateString.substring(dateString.lastIndexOf("-"));
            }
            String[] hourMinuteOffsetSplit = offsetString.split(":");
            offset = hourMinuteOffsetSplit.length > 1
                    ? ZoneOffset.ofHoursMinutes(Integer.valueOf(hourMinuteOffsetSplit[0]), Integer.valueOf(hourMinuteOffsetSplit[1]))
                    : ZoneOffset.ofHours(Integer.valueOf(hourMinuteOffsetSplit[0]));
            decimalOffset = new BigDecimal(offset.getTotalSeconds() / 3600.0);
            dateString = dateString.replaceAll("(\\+|-)\\d+:\\d+", "");
        }
        else {
            decimalOffset = BigDecimal.ZERO;
            offset = ZoneOffset.systemDefault().getRules().getStandardOffset(Instant.now());
            dateString = dateString.replace("Z", "");
        }

        return dateString;
    }

    public Integer getComponentFrom(String precision) {
        return getComponentFrom(Precision.toPrecision(precision));
    }

    public Integer getComponentFrom(Precision precision) {
        if (precision.field > this.precision.field) {
            return null;
        }
        return accessor.get(precision.getChronoFieldIndex());
    }

    public boolean atPrecision(BaseTemporal other, Precision precision) {
        return this.precision.field >= precision.field && other.precision.field >= precision.field;
    }

    public abstract Integer compare(BaseTemporal other, Boolean forSort);

    // for list sorting
    @Override
    public int compareTo(@Nonnull BaseTemporal other) {
        return compare(other, true);
    }
}
