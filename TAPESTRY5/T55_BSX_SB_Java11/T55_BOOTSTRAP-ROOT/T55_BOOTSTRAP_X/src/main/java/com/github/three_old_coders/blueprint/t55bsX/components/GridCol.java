package com.github.three_old_coders.blueprint.t55bsX.components;

import com.github.three_old_coders.blueprint.t55bs.components.GridColBase;
import com.github.three_old_coders.blueprint.t55bs.services.EBSGrid;

import com.github.three_old_coders.blueprint.t55bs.services.EBSVersions;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;

@SuppressWarnings("unused")
public class GridCol
    extends GridColBase
{
    private @Inject ComponentResources _crs;
    private @Inject @Symbol("bootstrap.version") EBSVersions _bsVersion;

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
    protected String getGridCss(final EBSGrid gCol, final String gColVal, final EBSGrid gOfs, final String gOfsVal)
    {
        String s = "";
        if (null != gCol) {
            s += " col-" + gCol.name() + "-" + gColVal;
        }

        if (null != gOfs) {
            if (_bsVersion == EBSVersions.v3) {
                s += " col-offset-" + gOfsVal;
            } else {
                s += " offset-" + gOfs.name() + "-" + gOfsVal;
            }
        }
        return s;
    }

    /**
     * override to handle grids with less options
     * @param grid
     * @return
     */
    protected EBSGrid assureGrid(final EBSGrid grid)
    {
        // we do not support XXL and XXXL for BS 3, so we fall back to xl.
        if ((grid == EBSGrid.xxxl || grid == EBSGrid.xxl) && _bsVersion == EBSVersions.v3) {
            return EBSGrid.xl;
        } else {
            return grid;
        }
    }
}
