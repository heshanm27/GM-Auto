package com.example.gmauto.helpers;
        import android.os.Parcel;
        import com.google.android.material.datepicker.CalendarConstraints.DateValidator;
        import java.util.Arrays;
        import java.util.Calendar;
        import java.util.TimeZone;

/** A {@link DateValidator} that enables all days of the week, except Saturday and Sunday. */
public class DateValidatorsweekDays implements DateValidator {

    private Calendar utc = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

    public static final Creator<DateValidatorsweekDays> CREATOR =
            new Creator<DateValidatorsweekDays>() {
                @Override
                public DateValidatorsweekDays createFromParcel(Parcel source) {
                    return new DateValidatorsweekDays();
                }

                @Override
                public DateValidatorsweekDays[] newArray(int size) {
                    return new DateValidatorsweekDays[size];
                }
            };

    @Override
    public boolean isValid(long date) {
        utc.setTimeInMillis(date);
        int dayOfWeek = utc.get(Calendar.DAY_OF_WEEK);
        return dayOfWeek != Calendar.SATURDAY && dayOfWeek != Calendar.SUNDAY;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DateValidatorsweekDays)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        Object[] hashedFields = {};
        return Arrays.hashCode(hashedFields);
    }
}