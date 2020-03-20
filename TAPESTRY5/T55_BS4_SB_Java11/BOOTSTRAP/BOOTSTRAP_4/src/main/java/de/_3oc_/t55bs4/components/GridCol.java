package de._3oc_.t55bs4.components;

import de._3oc_.t55bs.components.GridColBase;
import de._3oc_.t55bs.services.EBSGrid;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.ioc.annotations.Inject;

@SuppressWarnings("unused")
public class GridCol
    extends GridColBase
{
    private @Inject ComponentResources _crs;

    //
    // ---->> PROTECTED
    //

    /**
     * override to handle different grid syntax
     * @param grid
     * @return
     */
    protected String getGridCss(final EBSGrid grid)
    {
        return "col-" + grid.name() + "-" + _crs.getInformalParameter(grid.name(), String.class);
    }

    /**
     * override to handle grids with less options
     * @param grid
     * @return
     */
    protected EBSGrid assureGrid(final EBSGrid grid)
    {
        // we do not support XXL and XXXL yet, so we fall back to xl. 
        if (grid == EBSGrid.xxxl || grid == EBSGrid.xxl) {
            return EBSGrid.xl;
        } else {
            return grid;
        }
    }
}
