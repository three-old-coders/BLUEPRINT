package com.github.three_old_coders.blueprint.t55bsBase.services.utils;

import org.apache.tapestry5.EventContext;

public class TocEventContextUtils
{
    private TocEventContextUtils()
    {
    }

    public static <T> T getValue(final EventContext ec, final Class<T> tClass, final int index)
    {
        try {
            return ec.get(tClass, index);
        } catch (final Exception e) {
            return null;
        }
    }

    public static <T> T getValue(final EventContext ec, final Class<T> tClass, final int index, final T defaultValue)
    {
        final T value = getValue(ec, tClass, index);
        return (null == value) ? defaultValue : value;
    }
}
