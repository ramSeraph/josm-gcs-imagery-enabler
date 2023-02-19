/* License: GPL, look at the top level JOSM-licenses folder */
package org.openstreetmap.josm.plugins.gcsenabler;

import java.awt.Container;
import javax.swing.JMenu;
import javax.swing.JPopupMenu;
import javax.swing.event.MenuListener;
import javax.swing.event.MenuEvent;

import org.openstreetmap.josm.gui.MapFrame;
import org.openstreetmap.josm.gui.MainMenu;
import org.openstreetmap.josm.gui.MainApplication;

import org.openstreetmap.josm.plugins.Plugin;
import org.openstreetmap.josm.plugins.PluginInformation;

import org.openstreetmap.josm.data.imagery.ImageryLayerInfo;

import org.openstreetmap.josm.tools.Logging;


/**
 * Main class for the gcs imagery enabler plugin.
 *
 * @author ramSeraph
 *
 */
public class GCSImageryEnablerPlugin extends Plugin
{

    private GCSImageryMenu gcsImageryMenu;
    private MenuListener[] wrappedListeners;
    private boolean overriden = false;

    public GCSImageryEnablerPlugin(PluginInformation info)
    {
        super(info);
        Logging.info("GCSEnabler: In Constructor");
        addOverrider();
    }

    @Override
    public void mapFrameInitialized(MapFrame oldFrame, MapFrame newFrame)
    {
        super.mapFrameInitialized(oldFrame, newFrame);
        Logging.info("GCSEnabler: In MapframeInialized callback");
        addOverrider();
    }

    /*
     * N.B: required because some piece of JOSM ImageryMenu code uses the global imagerySubMenu
     * instead of it's instance member when it unnests the imagerySubMenu
     */
    public void makeItNest()
    {
        MainMenu mainMenu = MainApplication.getMenu();
        JMenu menu = mainMenu.imageryMenu;
        JMenu subMenu = mainMenu.imagerySubMenu;
        if (subMenu.getParent() == (Container)menu)
        {
            Logging.info("GCSEnabler: Already nested.. Shortcircuiting");
            return;
        }
        // hacked to replicate the nesting logic from the ImageryMenu code
        int itemCountCeiling = MainApplication.getMainPanel().getHeight() / 30;
        int savedLayersCount = ImageryLayerInfo.instance.getLayers().size();
        int subMenuCount = mainMenu.imagerySubMenu.getItemCount();
        int toFill = itemCountCeiling - savedLayersCount - subMenuCount - 1;
        Logging.info("GCSEnabler: itemCountCeiling: {0}, savedLayersCount: {1}, subMenuCount: {2}, toFill: {3}", 
                      itemCountCeiling, savedLayersCount, subMenuCount, toFill);

        for (int i=0; i <= toFill; i++) 
        {
            subMenu.add(new JPopupMenu.Separator());
        }
    }

    public void addOverrider()
    {
        if (overriden)
        {
            return;
        }
        Logging.info("GCSEnabler: Add Overrider invoked");
        MainMenu mainMenu = MainApplication.getMenu();
        if (mainMenu == null)
        {
            return;
        }
        JMenu menu = mainMenu.imageryMenu;
        MenuListener[] listeners = menu.getMenuListeners();
        int num = listeners.length;
        wrappedListeners = new MenuListener[num];
        for (int i = 0; i < num; i++)
        {
            Logging.info("GCSEnabler: removing listener");
            MenuListener l = listeners[i];
            menu.removeMenuListener(l);
            wrappedListeners[i] = new MenuListener()
            {
                @Override
                public void menuSelected(MenuEvent e)
                {
                    makeItNest();
                    l.menuSelected(e);
                    makeItNest();
                }

                @Override
                public void menuDeselected(MenuEvent e) {}

                @Override
                public void menuCanceled(MenuEvent e) {}
            };
        }

        for (MenuListener l: wrappedListeners)
        {
            Logging.info("GCSEnabler: adding wrapped listener");
            menu.addMenuListener(l);
        }

        Logging.info("GCSEnabler: Adding GCS Imagery Menu");
        gcsImageryMenu = new GCSImageryMenu(new JMenu());
        mainMenu.imagerySubMenu.add(gcsImageryMenu);
        overriden = true;
    }
}

