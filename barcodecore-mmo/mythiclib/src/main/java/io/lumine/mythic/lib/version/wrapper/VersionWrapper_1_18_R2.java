package io.lumine.mythic.lib.version.wrapper;

import io.lumine.mythic.lib.api.MMORayTraceResult;
import io.lumine.mythic.lib.api.item.ItemTag;
import io.lumine.mythic.lib.api.item.NBTCompound;
import io.lumine.mythic.lib.api.item.NBTItem;
import io.lumine.mythic.lib.api.util.NBTTypeHelper;
import io.lumine.utils.adventure.text.Component;
import io.lumine.utils.adventure.text.serializer.gson.GsonComponentSerializer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.chat.ChatMessageType;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.game.PacketPlayInArmAnimation;
import net.minecraft.network.protocol.game.PacketPlayOutAnimation;
import net.minecraft.network.protocol.game.PacketPlayOutChat;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.network.PlayerConnection;
import net.minecraft.world.EnumHand;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_18_R2.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_18_R2.util.CraftMagicNumbers;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;

public class VersionWrapper_1_18_R2 implements VersionWrapper {

	@Override
	public void sendJson(Player player, String message) {
		((CraftPlayer) player).getHandle().b.a(new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a(message), ChatMessageType.a, UUID.randomUUID()));
	}

	@Override
	public void sendActionBar(Player player, String message) {
		((CraftPlayer) player).getHandle().b.a(new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + message + "\"}"), ChatMessageType.c, UUID.randomUUID()));
	}
	private EntityPlayer toNMS(Player player) {
		return ((CraftPlayer) player).getHandle();
	}

	@Override
	public NBTItem getNBTItem(ItemStack item) {
		return new NBTItem_v1_18_R2(item);
	}

	public static class NBTItem_v1_18_R2 extends NBTItem {
		private final net.minecraft.world.item.ItemStack nms;
		private final NBTTagCompound compound;

		public NBTItem_v1_18_R2(ItemStack item) {
			super(item);

			nms = CraftItemStack.asNMSCopy(item);
			compound = nms.s() ? nms.t() : new NBTTagCompound();
		}

		@Override
		public Object get(String path) {
			return compound.c(path);
		}

		@Override
		public String getString(String path) {
			return compound.l(path);
		}

		@Override
		public boolean hasTag(String path) {
			return compound.e(path);
		}

		@Override
		public boolean getBoolean(String path) {
			return compound.q(path);
		}

		@Override
		public double getDouble(String path) {
			return compound.k(path);
		}

		@Override
		public int getInteger(String path) {
			return compound.h(path);
		}

		@Override
		public NBTCompound getNBTCompound(String path) {
			return new NBTCompound_v1_18_R2(this, path);
		}

		@Override
		public NBTItem addTag(List<ItemTag> tags) {
			tags.forEach(tag -> {
				if (tag.getValue() instanceof Boolean) compound.a(tag.getPath(), (boolean) tag.getValue());
				else if (tag.getValue() instanceof Double) compound.a(tag.getPath(), (double) tag.getValue());
				else if (tag.getValue() instanceof String) compound.a(tag.getPath(), (String) tag.getValue());
				else if (tag.getValue() instanceof Integer) compound.a(tag.getPath(), (int) tag.getValue());
				else if (tag.getValue() instanceof List<?>) {
					NBTTagList tagList = new NBTTagList();
					for (Object s : (List<?>) tag.getValue())
						if (s instanceof String) tagList.add(NBTTagString.a((String) s));
					compound.a(tag.getPath(), tagList);
				}
			});
			return this;
		}

		@Override
		public NBTItem removeTag(String... paths) {
			for (String path : paths)
				compound.r(path);
			return this;
		}

		@Override
		public Set<String> getTags() {
			return compound.d();
		}

		@Override
		public ItemStack toItem() {
			nms.c(compound);
			return CraftItemStack.asBukkitCopy(nms);
		}

		@Override
		public int getTypeId(String path) {
			return compound.c(path).a();
		}

		@Override
		public Component getDisplayNameComponent() {
			if (compound.p("display").e("Name")) {
				return GsonComponentSerializer.gson().deserialize(compound.p("display").l("Name"));
			}
			return Component.empty();
		}

		@Override
		// Replaces the current name component with the passed parameter.
		public void setDisplayNameComponent(Component component) {
			if (component != null)
				compound.p("display").a("Name", GsonComponentSerializer.gson().serialize(component));
			else compound.p("display").r("Name");
		}

		@Override
		public List<Component> getLoreComponents() {
			List<Component> lore = new ArrayList<>();

			if (compound.p("display").e("Lore")) {
				NBTTagList strings = compound.p("display").c("Lore", CraftMagicNumbers.NBT.TAG_STRING);
				for (int i = 0; i < strings.size(); i++)
					lore.add(GsonComponentSerializer.gson().deserialize(strings.j(i)));
			}

			return lore;
		}

		@Override
		// Replaces the current lore component with the passed parameter.
		public void setLoreComponents(List<Component> components) {
			NBTTagList lore = new NBTTagList();
			if (components != null && !components.isEmpty()) {
				for (Component component : components)
					lore.add(NBTTagString.a(GsonComponentSerializer.gson().serialize(component)));

				compound.p("display").a("Lore", lore);
			} else {
				compound.p("display").r("Lore");
			}
		}

		@Override
		public NBTItem_v1_18_R2 cancelVanillaAttributeModifiers() {
			return this;
		}
	}

	private static class NBTCompound_v1_18_R2 extends NBTCompound {
		private final NBTTagCompound compound;

		public NBTCompound_v1_18_R2(NBTItem_v1_18_R2 item, String path) {
			super();
			compound = (item.hasTag(path) && NBTTypeHelper.COMPOUND.is(item.getTypeId(path))) ? item.compound.p(path) : new NBTTagCompound();
		}

		public NBTCompound_v1_18_R2(NBTCompound_v1_18_R2 comp, String path) {
			super();
			compound = (comp.hasTag(path) && NBTTypeHelper.COMPOUND.is(comp.getTypeId(path))) ? comp.compound.p(path) : new NBTTagCompound();
		}

		@Override
		public boolean hasTag(String path) {
			return compound.e(path);
		}

		@Override
		public Object get(String path) {
			return compound.c(path);
		}

		@Override
		public NBTCompound getNBTCompound(String path) {
			return new NBTCompound_v1_18_R2(this, path);
		}

		@Override
		public String getString(String path) {
			return compound.l(path);
		}

		@Override
		public boolean getBoolean(String path) {
			return compound.q(path);
		}

		@Override
		public double getDouble(String path) {
			return compound.k(path);
		}

		@Override
		public int getInteger(String path) {
			return compound.h(path);
		}

		@Override
		public Set<String> getTags() {
			return compound.d();
		}

		@Override
		public int getTypeId(String path) {
			return compound.c(path).a();
		}
	}

	@Override
	public boolean isInBoundingBox(Entity entity, Location loc) {
		return entity.getBoundingBox().expand(.2, .2, .2, .2, .2, .2).contains(loc.toVector());
	}

	@Override
	public void playArmAnimation(Player player) {
		EntityPlayer p = ((CraftPlayer) player).getHandle();
		PlayerConnection connection = p.b;
		PacketPlayOutAnimation armSwing = new PacketPlayOutAnimation(p, 0);
		connection.a(armSwing);
		connection.a(new PacketPlayInArmAnimation(EnumHand.a));
	}

	@Override
	public MMORayTraceResult rayTrace(Location loc, Vector direction, double range, Predicate<Entity> option) {
		RayTraceResult hit = loc.getWorld().rayTrace(loc, direction, range, FluidCollisionMode.NEVER, true, .2, option);
		return new MMORayTraceResult(hit != null ? (LivingEntity) hit.getHitEntity() : null, hit != null ? hit.getHitPosition().distance(loc.toVector()) : range);
	}

	@Override
	public NBTItem copyTexture(NBTItem item) {
		return getNBTItem(new ItemStack(item.getItem().getType())).addTag(new ItemTag("CustomModelData", item.getInteger("CustomModelData")));
	}

	@Override
	public ItemStack textureItem(Material material, int model) {
		return getNBTItem(new ItemStack(material)).addTag(new ItemTag("CustomModelData", model)).toItem();
	}
}
