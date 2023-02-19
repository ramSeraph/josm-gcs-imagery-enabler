/* License: GPL, look at the top level JOSM-licenses folder */
package org.openstreetmap.josm.plugins.gcsenabler;

import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Locale;


import java.awt.Container;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import org.openstreetmap.josm.gui.MainMenu;
import org.openstreetmap.josm.gui.MainApplication;
import org.openstreetmap.josm.gui.ImageryMenu;
import org.openstreetmap.josm.gui.layer.ImageryLayer;
import org.openstreetmap.josm.gui.layer.LayerManager.LayerAddEvent;
import org.openstreetmap.josm.gui.layer.LayerManager.LayerChangeListener;
import org.openstreetmap.josm.gui.layer.LayerManager.LayerOrderChangeEvent;
import org.openstreetmap.josm.gui.layer.LayerManager.LayerRemoveEvent;

import org.openstreetmap.josm.data.imagery.ImageryInfo;
import org.openstreetmap.josm.data.imagery.ImageryLayerInfo;

import org.openstreetmap.josm.actions.JosmAction;

import org.openstreetmap.josm.tools.Logging;

public class GCSImageryMenu
    extends ImageryMenu implements LayerChangeListener
{

    private MainMenu mainMenu = MainApplication.getMenu();
    private final JMenu subMenu;
    private final List<JMenuItem> dynamicItems = new ArrayList<>(20);
    private final List<JosmAction> dynamicJosmActions = new ArrayList<>(20);
    public static final Comparator<ImageryInfo> alphabeticImageryComparator =
        Comparator.comparing(ii -> ii.getName().toLowerCase(Locale.ENGLISH));

    public GCSImageryMenu(JMenu subMenu) {
        super(subMenu);
        this.subMenu = subMenu;
        setText("GCS Imagery");

        MenuListener[] menuListeners = getMenuListeners();
        for (MenuListener l: menuListeners) 
        {
            removeMenuListener(l);
        }

        addMenuListener(new MenuListener() 
        {
            @Override
            public void menuSelected(MenuEvent e) {
                clearThings();
                addThings();
            }

            @Override
            public void menuDeselected(MenuEvent e) {}

            @Override
            public void menuCanceled(MenuEvent e) {}
        });
    }

    public void clearThings()
    {
        Logging.info("GCSEnabler: clearing old items"); 
        dynamicJosmActions.forEach(a -> { a.destroy(); });
        dynamicJosmActions.clear();
        dynamicItems.forEach(it -> { this.remove(it); });
        dynamicItems.clear();
    }

    public void addThing(ImageryInfo u)
    {
        AddGCSEnabledImageryLayerAction a = new AddGCSEnabledImageryLayerAction(u);
        dynamicJosmActions.add(a);
        JMenuItem item = createActionComponent(a);
        item.setAction(a);
        dynamicItems.add(this.add(item));
    }

    public static List<ImageryInfo> getGCSImageryInfos()
    {
        List<ImageryInfo> savedLayers = new ArrayList<>(ImageryLayerInfo.instance.getLayers());
        savedLayers.removeIf(info -> !AddGCSEnabledImageryLayerAction.isGCSImagery(info));
        return savedLayers;
    }

    public static int getNumGCSLayers()
    {
        return getGCSImageryInfos().size();
    }

    public void addThings()
    {
        Logging.info("GCSEnabler: adding new items"); 
        List<ImageryInfo> savedLayers = getGCSImageryInfos();
        savedLayers.sort(alphabeticImageryComparator);
        savedLayers.forEach(u -> { addThing(u); });
    }
}
