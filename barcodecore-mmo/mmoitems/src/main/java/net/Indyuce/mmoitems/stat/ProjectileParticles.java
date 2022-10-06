package net.Indyuce.mmoitems.stat;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.api.item.ItemTag;
import io.lumine.mythic.lib.api.item.SupportedNBTTagValues;
import io.lumine.mythic.lib.api.util.AltChar;
import io.lumine.mythic.lib.version.VersionMaterial;
import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.MMOUtils;
import net.Indyuce.mmoitems.api.edition.StatEdition;
import net.Indyuce.mmoitems.api.item.build.ItemStackBuilder;
import net.Indyuce.mmoitems.api.item.mmoitem.ReadMMOItem;
import net.Indyuce.mmoitems.gui.edition.EditionInventory;
import net.Indyuce.mmoitems.stat.data.ProjectileParticlesData;
import net.Indyuce.mmoitems.stat.data.random.RandomStatData;
import net.Indyuce.mmoitems.stat.data.type.StatData;
import net.Indyuce.mmoitems.stat.type.StringStat;
import org.apache.commons.lang.Validate;
import org.bukkit.ChatColor;
import org.bukkit.Particle;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Projectile Particles are the particles fired in place of a projectile for certain weapons such as the Lute, Musket, or Whip [WIP]
 * @author Kasprr
 */

public class ProjectileParticles extends StringStat {
  public ProjectileParticles() {
    super("PROJECTILE_PARTICLES", VersionMaterial.LIME_STAINED_GLASS.toMaterial(), "Projectile Particles",
            new String[] { "The projectile particle that your weapon shoots" }, new String[] { "lute" } );
  }

  @Override
  public ProjectileParticlesData whenInitialized(Object object) {
    Validate.isTrue(object instanceof ConfigurationSection, "Must specify a valid config section");
    ConfigurationSection config = (ConfigurationSection) object;

    Validate.isTrue(config.contains("particle"), "Could not find projectile particle");
    Particle particle = Particle.valueOf(config.getString("particle").toUpperCase().replace("-", "_").replace(" ", ""));

    return ProjectileParticlesData.isColorable(particle)
            ? new ProjectileParticlesData(particle, config.getInt("color.red"), config.getInt("color.green"), config.getInt("color.blue"))
            : new ProjectileParticlesData(particle);
  }

  @Override
  public void whenClicked(@NotNull EditionInventory inv, @NotNull InventoryClickEvent event) {
    if (event.getAction() == InventoryAction.PICKUP_HALF) {
      inv.getEditedSection().set("projectile-particles", null);
      inv.registerTemplateEdition();
      inv.getPlayer().sendMessage(MMOItems.plugin.getPrefix() + "Successfully removed the projectile particle.");
    } else
      new StatEdition(inv, this).enable("Write in the chat the particle you want along with the color if applicable.",
              ChatColor.AQUA + "Format: {Particle} {Color}",
              "All particles can be found here: https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Particle.html");
  }

  @Override
  public void whenDisplayed(List<String> lore, Optional<RandomStatData> statData) {
    if (statData.isPresent()) {
      String dataStr = statData.get().toString();
      JsonObject data = MythicLib.plugin.getJson().parse(dataStr, JsonObject.class);

      Particle particle = Particle.valueOf(data.get("Particle").getAsString());

      lore.add(ChatColor.GRAY + "Current Value: " + ChatColor.GREEN + particle);

      if (ProjectileParticlesData.isColorable(particle)) {
        String red = String.valueOf(data.get("Red"));
        String green = String.valueOf(data.get("Green"));
        String blue = String.valueOf(data.get("Blue"));
        String colorStr = particle == Particle.NOTE ? red : red + " " + green + " " + blue;
        lore.add(ChatColor.GRAY + "Color: " + ChatColor.GREEN + colorStr);
      }
    } else
      lore.add(ChatColor.GRAY + "Current Value: " + ChatColor.RED + "None");

    lore.add("");
    lore.add(ChatColor.YELLOW + AltChar.listDash + " Left click to change this value.");
    lore.add(ChatColor.YELLOW + AltChar.listDash + " Right click to remove this value.");
  }

  @Override
  public void whenInput(@NotNull EditionInventory inv, @NotNull String message, Object... info) {
    String[] msg = message.replace(", ", " ").replace(",", " ").split(" ");
    Particle particle = Particle.valueOf(msg[0].toUpperCase().replace("-", "_").replace(" ", "_"));
    if (ProjectileParticlesData.isColorable(particle)) {
      Validate.isTrue(msg.length <= 4, "Too many arguments provided.");
      if (particle.equals(Particle.NOTE)) {
        Validate.isTrue(msg.length == 2, "You must provide a color for this particle.\n"
                + MMOItems.plugin.getPrefix() + "NOTE particle colors only take a single value between 1 and 24.\n"
                + MMOItems.plugin.getPrefix() + ChatColor.AQUA + "Format: {Particle} {Color}");
        int red = Math.min(24, Math.max(1, Integer.parseInt(msg[1])));
        inv.getEditedSection().set("projectile-particles.particle", particle.name());
        inv.getEditedSection().set("projectile-particles.color.red", red);
        inv.getEditedSection().set("projectile-particles.color.green", 0);
        inv.getEditedSection().set("projectile-particles.color.blue", 0);
        inv.registerTemplateEdition();
        inv.getPlayer().sendMessage(MMOItems.plugin.getPrefix() + "Particle successfully set to "
                + MMOUtils.caseOnWords(particle.name().toLowerCase().replace("_", " ")) + " with color " + red);
      } else {
        Validate.isTrue(msg.length == 4, "You must provide a color for this particle.\n"
                + MMOItems.plugin.getPrefix() + ChatColor.AQUA + "Format: {Particle} {R G B}");
        int red = msg[1] != null ? Math.min(255, Math.max(0, Integer.parseInt(msg[1]))) : 0;
        int green = msg[2] != null ? Math.min(255, Math.max(0, Integer.parseInt(msg[2]))) : 0;
        int blue = msg[3] != null ? Math.min(255, Math.max(0, Integer.parseInt(msg[3]))) : 0;
        inv.getEditedSection().set("projectile-particles.particle", particle.name());
        inv.getEditedSection().set("projectile-particles.color.red", red);
        inv.getEditedSection().set("projectile-particles.color.green", green);
        inv.getEditedSection().set("projectile-particles.color.blue", blue);
        inv.registerTemplateEdition();
        inv.getPlayer().sendMessage(MMOItems.plugin.getPrefix() + "Particle successfully set to "
                + MMOUtils.caseOnWords(particle.name().toLowerCase().replace("_", " ")) + " with RGB color " + red + " " + green + " " + blue);
      }
    } else {
      Validate.isTrue(msg.length == 1, "That particle cannot be assigned a color");
      inv.getEditedSection().set("projectile-particles.particle", particle.name());
      inv.getEditedSection().set("projectile-particles.color.red", 0);
      inv.getEditedSection().set("projectile-particles.color.green", 0);
      inv.getEditedSection().set("projectile-particles.color.blue", 0);
      inv.registerTemplateEdition();
      inv.getPlayer().sendMessage(MMOItems.plugin.getPrefix() + "Particle successfully set to "
              + MMOUtils.caseOnWords(particle.name().toLowerCase().replace("_", " ")));
    }
  }

  @Override
  public void whenApplied(@NotNull ItemStackBuilder item, @NotNull StatData data) {
    item.addItemTag(getAppliedNBT(data));
  }

  @NotNull
  @Override
  public ArrayList<ItemTag> getAppliedNBT(@NotNull StatData data) {
    ArrayList<ItemTag> tags = new ArrayList<>();
    tags.add(new ItemTag(getNBTPath(), data.toString()));
    return tags;
  }

  public void whenLoaded(@NotNull ReadMMOItem mmoItem) {
    // Get tags
    ArrayList<ItemTag> tags = new ArrayList<>();
    if (mmoItem.getNBT().hasTag(getNBTPath()))
      tags.add(ItemTag.getTagAtPath(getNBTPath(), mmoItem.getNBT(), SupportedNBTTagValues.STRING));

    StatData data = getLoadedNBT(tags);

    if (data != null) {
      mmoItem.setData(this, data);
    }
  }

  public StatData getLoadedNBT(@NotNull ArrayList<ItemTag> storedTags) {
    // Get tags
    ItemTag tags = ItemTag.getTagAtPath(getNBTPath(), storedTags);

    if (tags != null) {
      try {
        JsonObject json = new JsonParser().parse((String) tags.getValue()).getAsJsonObject();
        Particle particle = Particle.valueOf(json.get("Particle").getAsString());

        if (ProjectileParticlesData.isColorable(particle)) {
          return new ProjectileParticlesData(particle, json.get("Red").getAsInt(), json.get("Green").getAsInt(), json.get("Blue").getAsInt());
        } else {
          return new ProjectileParticlesData(particle);
        }

      } catch (JsonSyntaxException e) {
        e.printStackTrace();
      }


    }

    return null;
  }

}
