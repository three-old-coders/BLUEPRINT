package de.threeoldcoders.grayhair.pages;

import de.threeoldcoders.grayhair.services.XValue;
import org.apache.tapestry5.Block;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.Id;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;

import javax.inject.Inject;
import java.util.*;

// http://localhost:8080/GH1Z/index.xvalueform
// we had a filter as workaround? mailinglist??
public class Index
{
    private static final List<XValue> _allXValues
        = new ArrayList<XValue>(Arrays.asList(new XValue(1, "1"), new XValue(2, "2"), new XValue(3, "3")));

    private static final Set<XValue> _changes = new HashSet<XValue>();

    @Inject private Request _request;
    @Inject private AjaxResponseRenderer _arr;

    @Inject @Id("readOnly") private Block _blockReadOnly;
    @Inject @Id("editable") private Block _blockEdit;

    @Property XValue _xValue;

    void onBeginSubmit()
    {
        _xValue = new XValue(-1, "");
    }

    void onAfterSubmit()
    {
        _changes.remove(_xValue);
        _changes.add(_xValue);  // modified instance replaces old one
    }

    void onSuccessFromXValueForm()
    {
        // do whatever you want
        for (final XValue change : _changes) {
            _allXValues.remove(change);
            _allXValues.add(change);
        }
        _changes.clear();

        // keep ordering by PK for clarity reasons
        Collections.sort(_allXValues, new Comparator<XValue>()
        {
            @Override public int compare(final XValue xValue1, final XValue xValue2)
            {
                return xValue1.getPk().compareTo(xValue2.getPk());
            }
        });
    }

    Object onModifyXValue(final long pk)
    {
        // just retrieve original instance and mark it as changed
        final XValue orig = getXValueEncoder().toValue(Long.toString(pk));
        _changes.add(new XValue(orig.getPk(), orig.getS()));

        if (_request.isXHR()) {
            _xValue = orig;
            _arr.addRender("row_" + _xValue.getPk(), _blockEdit);
        }
        return null;
    }

    Object onRevertXValue(final long pk)
    {
        // just retrieve original instance and mark it as changed
        final XValue orig = getXValueEncoder().toValue(Long.toString(pk));
        _changes.remove(orig);

        if (_request.isXHR()) {
            _xValue = orig;
            _arr.addRender("row_" + _xValue.getPk(), _blockReadOnly);
        }
        return null;
    }

    public List<XValue> getXValues()
    {
        return _allXValues;
    }

    public boolean isXValueChanged()
    {
        return _changes.contains(_xValue);
    }

    public ValueEncoder<XValue> getXValueEncoder()
    {
        return new ValueEncoder<XValue>()
        {
            @Override public String toClient(final XValue value)
            {
                return value.getPk().toString();
            }

            @Override public XValue toValue(final String clientValue)
            {
                return getXValues().get(Integer.parseInt(clientValue) - 1);
            }
        };
    }
}
