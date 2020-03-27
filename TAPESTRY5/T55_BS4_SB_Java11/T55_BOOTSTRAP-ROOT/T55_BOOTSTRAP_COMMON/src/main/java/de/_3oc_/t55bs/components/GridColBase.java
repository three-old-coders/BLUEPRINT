package de._3oc_.t55bs.components;

import de._3oc_.t55bs.services.EBSGrid;
import org.apache.commons.lang3.StringUtils;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.ioc.annotations.Inject;

import java.util.HashSet;
import java.util.Set;

@SupportsInformalParameters
@SuppressWarnings("unused")
public abstract class GridColBase
{
    private @Inject ComponentResources _crs;

    @Parameter(name = "xs", defaultPrefix = BindingConstants.LITERAL) private int _xs;
    @Parameter(name = "sm", defaultPrefix = BindingConstants.LITERAL) private int _sm;
    @Parameter(name = "md", defaultPrefix = BindingConstants.LITERAL) private int _md;
    @Parameter(name = "lg", defaultPrefix = BindingConstants.LITERAL) private int _lg;
    @Parameter(name = "xl", defaultPrefix = BindingConstants.LITERAL) private int _xl;
    @Parameter(name = "xxl", defaultPrefix = BindingConstants.LITERAL) private int _xxl;
    @Parameter(name = "xxxl", defaultPrefix = BindingConstants.LITERAL) private int _xxxl;

    @Parameter(name = "class", defaultPrefix = BindingConstants.LITERAL, value = "") private String _cssClass;

    public String getCssClass()
    {
        final StringBuilder sb = new StringBuilder(StringUtils.trimToEmpty(_cssClass));
        final Set<EBSGrid> boundValues = new HashSet<>();
        for (final EBSGrid value : EBSGrid.values()) {
            if (_crs.isBound(value.name())) {
                if (! boundValues.contains(value)) {
                    sb.append(" ").append(getGridCss(value));
                    // prevent rendering of duplicate grid entries
                    boundValues.add(value);
                }
            }
        }

        return sb.toString();
    }

    //
    // ---->> PROTECTED
    //

    /**
     * override to handle different grid syntax
     * @param grid
     * @return
     */
    protected abstract String getGridCss(final EBSGrid grid);

    /**
     * override to handle grids with less options
     * @param grid
     * @return
     */
    protected abstract EBSGrid assureGrid(final EBSGrid grid);
}
