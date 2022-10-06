package io.lumine.mythic.lib.comp.hologram;

//import io.lumine.mythic.lib.comp.hologram.factory.CMIHologramFactory;
import io.lumine.mythic.lib.comp.hologram.factory.HDHologramFactory;
//import io.lumine.mythic.lib.comp.hologram.factory.HologramsHologramFactory;
//import io.lumine.mythic.lib.comp.hologram.factory.TrHologramFactory;
import io.lumine.utils.holograms.HologramFactory;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicePriority;

import java.lang.reflect.InvocationTargetException;

public enum CustomHologramFactoryList {
    HOLOGRAPHIC_DISPLAYS("HolographicDisplays", HDHologramFactory.class, ServicePriority.Highest);
//    HOLOGRAMS("Holograms", HologramsHologramFactory.class, ServicePriority.High),
//    CMI("CMI", CMIHologramFactory.class, ServicePriority.Normal),
//    TR_HOLOGRAM("TrHologram", TrHologramFactory.class, ServicePriority.High);

    private final String pluginName;
    private final Class<? extends HologramFactory> factoryClass;
    private final ServicePriority priority;

    private CustomHologramFactoryList(String pluginName, Class<? extends HologramFactory> factoryClass, ServicePriority priority) {
        this.pluginName = pluginName;
        this.factoryClass = factoryClass;
        this.priority = priority;
    }

    public String getPluginName() {
        return pluginName;
    }

    public boolean isInstalled(PluginManager manager) {
        return manager.getPlugin(pluginName) != null;
    }

    public HologramFactory generateFactory() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        return factoryClass.getConstructor().newInstance();
    }

    public ServicePriority getServicePriority() {
        return priority;
    }
}
