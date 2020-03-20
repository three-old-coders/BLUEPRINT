package de.threeoldcoders.grayhair.services;

public class XValue
{
    private Long _pk;
    private String _s;

    public XValue()
    {
    }

    public XValue(final long pk, final String s)
    {
        _pk = pk;
        _s = s;
    }

    public Long getPk()
    {
        return _pk;
    }

    public void setPk(final Long pk)
    {
        _pk = pk;
    }

    public void setS(final String s)
    {
        _s = s;
    }

    public String getS()
    {
        return _s;
    }

    @Override
    public boolean equals(final Object o)
    {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final XValue xValue = (XValue) o;

        if (!_pk.equals(xValue._pk)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode()
    {
        return _pk.hashCode();
    }

    @Override public String toString()
    {
        final StringBuilder sb = new StringBuilder("XValue{");
        sb.append("_pk=").append(_pk);
        sb.append(", _s='").append(_s).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
