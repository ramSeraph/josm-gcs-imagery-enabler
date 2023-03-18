/* License: GPL, look at the top level JOSM-licenses folder */
package org.openstreetmap.josm.plugins.gcsenabler;

import org.openstreetmap.josm.data.imagery.ImageryInfo;

import org.openstreetmap.josm.actions.JosmAction;
import org.openstreetmap.josm.actions.AdaptableAction;
import org.openstreetmap.josm.actions.AddImageryLayerAction;

public class AddGCSEnabledImageryLayerAction
    extends AddImageryLayerAction implements AdaptableAction
{

    private static String GCS_PATTERN="https://storage.googleapis.com/(.*)";

    private final transient ImageryInfo gcsInfo;

    public AddGCSEnabledImageryLayerAction(ImageryInfo info)
    {
        super(info);
        gcsInfo = info;
        updateEnabledState();
    }

    public static boolean isGCSImagery(ImageryInfo info)
    {
        return info.getUrl().matches(GCS_PATTERN);
    }

    @Override
    protected void updateEnabledState()
    {
        if (gcsInfo != null && isGCSImagery(gcsInfo))
        {
            setEnabled(true);
            return;
        }
        super.updateEnabledState();
    }

    @Override
    public String toString()
    {
        return "AddGCSEnabledImageryLayerAction [info=" + gcsInfo + ']';
    }
}
