package com.github.three_old_coders.blueprint.t55bs.components;

import com.github.three_old_coders.blueprint.t55bs.services.EBSGrid;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.ioc.annotations.Inject;

import java.util.HashSet;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.trimToEmpty;

@SupportsInformalParameters
@SuppressWarnings("unused")
public abstract class GridColBase
{
    private @Inject ComponentResources _crs;

    // regular grid col width
    @Parameter(name = "xs", defaultPrefix = BindingConstants.LITERAL) private int _xs;
    @Parameter(name = "sm", defaultPrefix = BindingConstants.LITERAL) private int _sm;
    @Parameter(name = "md", defaultPrefix = BindingConstants.LITERAL) private int _md;
    @Parameter(name = "lg", defaultPrefix = BindingConstants.LITERAL) private int _lg;
    @Parameter(name = "xl", defaultPrefix = BindingConstants.LITERAL) private int _xl;
    @Parameter(name = "xxl", defaultPrefix = BindingConstants.LITERAL) private int _xxl;
    @Parameter(name = "xxxl", defaultPrefix = BindingConstants.LITERAL) private int _xxxl;

    // grid col offset
    @Parameter(name = "ofs_xs", defaultPrefix = BindingConstants.LITERAL) private int _ofs_xs;
    @Parameter(name = "ofs_sm", defaultPrefix = BindingConstants.LITERAL) private int _ofs_sm;
    @Parameter(name = "ofs_md", defaultPrefix = BindingConstants.LITERAL) private int _ofs_md;
    @Parameter(name = "ofs_lg", defaultPrefix = BindingConstants.LITERAL) private int _ofs_lg;
    @Parameter(name = "ofs_xl", defaultPrefix = BindingConstants.LITERAL) private int _ofs_xl;
    @Parameter(name = "ofs_xxl", defaultPrefix = BindingConstants.LITERAL) private int _ofs_xxl;
    @Parameter(name = "ofs_xxxl", defaultPrefix = BindingConstants.LITERAL) private int _ofs_xxxl;

    @Parameter(name = "class", defaultPrefix = BindingConstants.LITERAL, value = "") private String _cssClass;

    public String getCssClass()
    {
        final StringBuilder sb = new StringBuilder(trimToEmpty(_cssClass));
        final Set<String> boundValues = new HashSet<>();
        for (final EBSGrid g : EBSGrid.values()) {
            final String pn = g.name();
            EBSGrid gCol = null;
            String vCol = null;
            if (_crs.isBound(pn) && (! boundValues.contains(pn))) {
                boundValues.add(pn);
                gCol = g;
                vCol = trimToEmpty(_crs.getInformalParameter(pn, String.class));
            }

            final String pnOfs = "ofs_" + pn;
            EBSGrid gOfs = null;
            String vOfs = null;
            if (_crs.isBound(pnOfs) && (! boundValues.contains(pnOfs))){
                boundValues.add(pnOfs);
                gOfs = g;
                vOfs = trimToEmpty(_crs.getInformalParameter(pnOfs, String.class));
            }

            sb.append(getGridCss(gCol, vCol, gOfs, vOfs));
        }

        return sb.toString();
    }

    //
    // ---->> PROTECTED
    //

    /**
     * override to handle different grid syntax
     * @param gCol
     * @param gColVal
     * @param gOfs
     * @param gOfsVal
     * @return
     */
    protected abstract String getGridCss(final EBSGrid gCol, final String gColVal, final EBSGrid gOfs, final String gOfsVal);

    /**
     * override to handle grids with less options
     * @param grid
     * @return
     */
    protected abstract EBSGrid assureGrid(final EBSGrid grid);
}
