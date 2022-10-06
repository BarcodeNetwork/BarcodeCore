package com.vjh0107.barcode.cutscene.ui;

import org.bukkit.event.inventory.ClickType;

public interface Citem {

	abstract void call(MasterUI masterUI, ClickType clickType);
}
