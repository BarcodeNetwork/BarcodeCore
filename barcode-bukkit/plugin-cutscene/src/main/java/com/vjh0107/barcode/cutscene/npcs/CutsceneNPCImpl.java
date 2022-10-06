package com.vjh0107.barcode.cutscene.npcs;

import com.vjh0107.barcode.cutscene.npc.npcs.CutsceneNPC;
import com.vjh0107.barcode.cutscene.npc.data.Equipment;
import com.vjh0107.barcode.cutscene.npc.data.Skin;
import com.vjh0107.barcode.cutscene.utils.ErrorHandler;
import com.vjh0107.barcode.cutscene.BarcodeCutscenePlugin;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPosition;
import net.minecraft.network.chat.ChatMessage;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.game.*;
import net.minecraft.network.protocol.game.PacketPlayOutEntity.PacketPlayOutEntityLook;
import net.minecraft.network.syncher.DataWatcher;
import net.minecraft.network.syncher.DataWatcherObject;
import net.minecraft.network.syncher.DataWatcherRegistry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.WorldServer;
import net.minecraft.server.network.PlayerConnection;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ambient.EntityBat;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.entity.animal.goat.Goat;
import net.minecraft.world.entity.animal.horse.EntityHorse;
import net.minecraft.world.entity.animal.horse.EntityHorseSkeleton;
import net.minecraft.world.entity.animal.horse.EntityHorseZombie;
import net.minecraft.world.entity.animal.horse.EntityLlama;
import net.minecraft.world.entity.boss.enderdragon.EntityEnderDragon;
import net.minecraft.world.entity.boss.wither.EntityWither;
import net.minecraft.world.entity.decoration.EntityArmorStand;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.monster.hoglin.EntityHoglin;
import net.minecraft.world.entity.monster.piglin.EntityPiglin;
import net.minecraft.world.entity.monster.piglin.EntityPiglinBrute;
import net.minecraft.world.entity.npc.EntityVillager;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_18_R2.CraftServer;
import org.bukkit.craftbukkit.v1_18_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_18_R2.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CutsceneNPCImpl implements CutsceneNPC {

    GameProfile gameProfile;
    EntityLiving entityPlayer;

    EntityType type;
    int data;

    public EntityType getEntityType() {
        return type;
    }

    public CutsceneNPC spawn(Location loc, Skin skin, String displayName, EntityType type, int data) {
        this.data = data;
        this.type = type;

        MinecraftServer minecraftServer = ((CraftServer) Bukkit.getServer()).getServer();
        WorldServer worldServer = ((CraftWorld) loc.getWorld()).getHandle();

        try {
            this.gameProfile = new GameProfile(UUID.randomUUID(), ChatColor.translateAlternateColorCodes('&', displayName));
            this.gameProfile.getProperties().put("textures", new Property("textures", skin.getValue(), skin.getSignature()));
        } catch (Exception e) {
            ErrorHandler.handleError(e);
        }

        switch (type) {
            case AREA_EFFECT_CLOUD:
                break;
            case ARMOR_STAND:
                this.entityPlayer = new EntityArmorStand(EntityTypes.c, worldServer);
                break;
            case BAT:
                this.entityPlayer = new EntityBat(EntityTypes.f, worldServer);
                break;
            case BEE:
                this.entityPlayer = new EntityBee(EntityTypes.g, worldServer);
                break;
            case BLAZE:
                this.entityPlayer = new EntityBlaze(EntityTypes.h, worldServer);
                break;
            case CAT:
                this.entityPlayer = new EntityCat(EntityTypes.j, worldServer);
                break;
            case CAVE_SPIDER:
                this.entityPlayer = new EntityCaveSpider(EntityTypes.k, worldServer);
                break;
            case CHICKEN:
                this.entityPlayer = new EntityChicken(EntityTypes.l, worldServer);
                break;
            case COD:
                this.entityPlayer = new EntityCod(EntityTypes.m, worldServer);
                break;
            case COW:
                this.entityPlayer = new EntityCow(EntityTypes.n, worldServer);
                break;
            case CREEPER:
                this.entityPlayer = new EntityCreeper(EntityTypes.o, worldServer);
                break;
            case DOLPHIN:
                this.entityPlayer = new EntityDolphin(EntityTypes.p, worldServer);
                break;
            case DROWNED:
                this.entityPlayer = new EntityDrowned(EntityTypes.s, worldServer);
                break;
            case ELDER_GUARDIAN:
                this.entityPlayer = new EntityGuardianElder(EntityTypes.t, worldServer);
                break;
            case ENDERMAN:
                this.entityPlayer = new EntityEnderman(EntityTypes.w, worldServer);
                break;
            case ENDERMITE:
                this.entityPlayer = new EntityEndermite(EntityTypes.x, worldServer);
                break;
            case ENDER_DRAGON:
                this.entityPlayer = new EntityEnderDragon(EntityTypes.v, worldServer);
                break;
            case EVOKER:
                this.entityPlayer = new EntityEvoker(EntityTypes.y, worldServer);
                break;
            case FOX:
                this.entityPlayer = new EntityFox(EntityTypes.E, worldServer);
                break;
            case GHAST:
                this.entityPlayer = new EntityGhast(EntityTypes.F, worldServer);
                break;
            case GUARDIAN:
                this.entityPlayer = new EntityGuardian(EntityTypes.K, worldServer);
                break;
            case HOGLIN:
                this.entityPlayer = new EntityHoglin(EntityTypes.L, worldServer);
                break;
            case HORSE:
                this.entityPlayer = new EntityHorse(EntityTypes.M, worldServer);
                break;
            case HUSK:
                this.entityPlayer = new EntityZombieHusk(EntityTypes.N, worldServer);
                break;
            case ILLUSIONER:
                this.entityPlayer = new EntityIllagerIllusioner(EntityTypes.O, worldServer);
                break;
            case IRON_GOLEM:
                this.entityPlayer = new EntityIronGolem(EntityTypes.P, worldServer);
                break;
            case LLAMA:
                this.entityPlayer = new EntityLlama(EntityTypes.V, worldServer);
                break;
            case MAGMA_CUBE:
                this.entityPlayer = new EntityMagmaCube(EntityTypes.X, worldServer);
                break;
            case MUSHROOM_COW:
                this.entityPlayer = new EntityMushroomCow(EntityTypes.ah, worldServer);
                break;
            case OCELOT:
                this.entityPlayer = new EntityOcelot(EntityTypes.ai, worldServer);
                break;
            case PANDA:
                this.entityPlayer = new EntityPanda(EntityTypes.ak, worldServer);
                break;
            case PARROT:
                this.entityPlayer = new EntityParrot(EntityTypes.al, worldServer);
                break;
            case PHANTOM:
                this.entityPlayer = new EntityPhantom(EntityTypes.am, worldServer);
                break;
            case PIG:
                this.entityPlayer = new EntityPig(EntityTypes.an, worldServer);
                break;
            case PIGLIN:
                this.entityPlayer = new EntityPiglin(EntityTypes.ao, worldServer);
                break;
            case PIGLIN_BRUTE:
                this.entityPlayer = new EntityPiglinBrute(EntityTypes.ap, worldServer);
                break;
            case PILLAGER:
                this.entityPlayer = new EntityPillager(EntityTypes.aq, worldServer);
                break;
            case PLAYER:
                //PlayerInteractManager manager = new PlayerInteractManager(worldServer);
                this.entityPlayer = new EntityPlayer(minecraftServer, worldServer, gameProfile);
                break;
            case POLAR_BEAR:
                this.entityPlayer = new EntityPolarBear(EntityTypes.ar, worldServer);
                break;
            case PUFFERFISH:
                this.entityPlayer = new EntityPufferFish(EntityTypes.at, worldServer);
                break;
            case RABBIT:
                this.entityPlayer = new EntityRabbit(EntityTypes.au, worldServer);
                break;
            case RAVAGER:
                this.entityPlayer = new EntityRavager(EntityTypes.av, worldServer);
                break;
            case SALMON:
                this.entityPlayer = new EntitySalmon(EntityTypes.aw, worldServer);
                break;
            case SHEEP:
                this.entityPlayer = new EntitySheep(EntityTypes.ax, worldServer);
                break;
            case SHULKER:
                this.entityPlayer = new EntityShulker(EntityTypes.ay, worldServer);
                break;
            case SILVERFISH:
                this.entityPlayer = new EntitySilverfish(EntityTypes.aA, worldServer);
                break;
            case SKELETON:
                this.entityPlayer = new EntitySkeleton(EntityTypes.aB, worldServer);
                break;
            case SKELETON_HORSE:
                this.entityPlayer = new EntityHorseSkeleton(EntityTypes.aC, worldServer);
                break;
            case ZOMBIE_HORSE:
                this.entityPlayer = new EntityHorseZombie(EntityTypes.bf, worldServer);
                break;
            case SLIME:
                this.entityPlayer = new EntitySlime(EntityTypes.aD, worldServer);
                break;
            case SNOWMAN:
                this.entityPlayer = new EntitySnowman(EntityTypes.aF, worldServer);
                break;
            case SPIDER:
                this.entityPlayer = new EntitySpider(EntityTypes.aI, worldServer);
                break;
            case SQUID:
                this.entityPlayer = new EntitySquid(EntityTypes.aJ, worldServer);
                break;
            case STRAY:
                this.entityPlayer = new EntitySkeletonStray(EntityTypes.aK, worldServer);
                break;
            case STRIDER:
                this.entityPlayer = new EntityStrider(EntityTypes.aL, worldServer);
                break;
            case TROPICAL_FISH:
                this.entityPlayer = new EntityTropicalFish(EntityTypes.aS, worldServer);
                break;
            case TURTLE:
                this.entityPlayer = new EntityTurtle(EntityTypes.aT, worldServer);
                break;
            case VEX:
                this.entityPlayer = new EntityVex(EntityTypes.aU, worldServer);
                break;
            case VILLAGER:
                this.entityPlayer = new EntityVillager(EntityTypes.aV, worldServer);
                break;
            case VINDICATOR:
                this.entityPlayer = new EntityVindicator(EntityTypes.aW, worldServer);
                break;
            case WITCH:
                this.entityPlayer = new EntityWitch(EntityTypes.aY, worldServer);
                break;
            case WITHER:
                this.entityPlayer = new EntityWither(EntityTypes.aZ, worldServer);
                break;
            case WITHER_SKELETON:
                this.entityPlayer = new EntitySkeletonWither(EntityTypes.ba, worldServer);
                break;
            case WOLF:
                this.entityPlayer = new EntityWolf(EntityTypes.bc, worldServer);
                break;
            case ZOGLIN:
                this.entityPlayer = new EntityZoglin(EntityTypes.bd, worldServer);
                break;
            case ZOMBIE:
                this.entityPlayer = new EntityZombie(EntityTypes.be, worldServer);
                break;
            case ZOMBIE_VILLAGER:
                this.entityPlayer = new EntityZombieVillager(EntityTypes.bg, worldServer);
                break;
            case GOAT:
                this.entityPlayer = new Goat(EntityTypes.J, worldServer);
                break;
            case GLOW_SQUID:
                this.entityPlayer = new GlowSquid(EntityTypes.I, worldServer);
                break;
            case AXOLOTL:
                this.entityPlayer = new Axolotl(EntityTypes.e, worldServer);
                break;
            default:
                break;
        }

        if (type != EntityType.PLAYER) {
            if (displayName != null && !displayName.equalsIgnoreCase("null")) {
                IChatBaseComponent chatBase = new ChatMessage(ChatColor.translateAlternateColorCodes('&', displayName));
                entityPlayer.a(chatBase);
                entityPlayer.n(true);
            } else {
                entityPlayer.n(false);
            }

            // entityPlayer.setAbsorptionHearts(49);
        }

        this.entityPlayer.a(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        entityPlayer.aF = false;

        return this;
    }

    public EntityLiving getEntity() {
        return entityPlayer;
    }

    public void show(Player player) {
        if (exclusivePlayer == null || exclusivePlayer.equals(player.getUniqueId())) {
            PlayerConnection playerConnection = ((CraftPlayer) player).getHandle().b;

            if (entityPlayer instanceof EntityPlayer) {
                playerConnection.a(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.a, (EntityPlayer) entityPlayer));
                playerConnection.a(new PacketPlayOutNamedEntitySpawn((EntityPlayer) entityPlayer));
            } else {
                playerConnection.a(new PacketPlayOutSpawnEntityLiving(entityPlayer));
            }

            playerConnection.a(new PacketPlayOutEntityHeadRotation(entityPlayer, (byte) (entityPlayer.getBukkitYaw() * 256 / 360)));
            changeEntityMetadata(17, 127);

            BarcodeCutscenePlugin.instance.getServer().getScheduler().scheduleSyncDelayedTask(BarcodeCutscenePlugin.instance, new Runnable() {
                public void run() {
                    if (entityPlayer instanceof EntityPlayer) {
                        playerConnection.a(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.e, (EntityPlayer) entityPlayer));
                    }
                    //16 to 17
                    changeEntityMetadata(17, 127);
                }
            }, 5);
        }
    }

    public Location getLocation(World world) {
        return new Location(world, entityPlayer.dc(), entityPlayer.de(), entityPlayer.di());
    }

    UUID exclusivePlayer = null;

    public void setExclusivePlayer(Player player) {
        if (player != null) {
            exclusivePlayer = player.getUniqueId();
        }
    }

    public void despawn() {
        entityPlayer.a(0.0D, -100.0D, 0.0D, 0.0F, 0.0F);
        entityPlayer.b(Entity.RemovalReason.a);

        for (Player p : Bukkit.getOnlinePlayers()) {
            PlayerConnection playerConnection = ((CraftPlayer) p).getHandle().b;

            playerConnection.a(new PacketPlayOutEntityDestroy(entityPlayer.ae()));
        }
    }

    public void teleport(Location location) {
        PacketPlayOutEntityLook turn = new PacketPlayOutEntityLook(entityPlayer.ae(), (byte) (location.getYaw() * 256 / 360), (byte) location.getPitch(), true);

        for (Player p : Bukkit.getOnlinePlayers()) {
            if (exclusivePlayer == null || exclusivePlayer.equals(p.getUniqueId())) {
                PlayerConnection playerConnection = ((CraftPlayer) p).getHandle().b;

                this.entityPlayer.getBukkitEntity().teleport(location);
                this.entityPlayer.a(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());

                playerConnection.a(new PacketPlayOutEntityHeadRotation(entityPlayer, (byte) (location.getYaw() * 256 / 360)));
                playerConnection.a(turn);
                playerConnection.a(new PacketPlayOutEntityTeleport(entityPlayer));
            }
        }
    }

    public void playAnimation(int animation) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (exclusivePlayer == null || exclusivePlayer.equals(p.getUniqueId())) {
                PlayerConnection playerConnection = ((CraftPlayer) p).getHandle().b;
                playerConnection.a(new PacketPlayOutAnimation(entityPlayer, animation));
            }
        }
    }

    public void changeEntityMetadata(int one, int two) {
        changeEntityMetadata((byte) one, (byte) two);
    }

    public void changeEntityMetadata(byte one, byte two) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (exclusivePlayer == null || exclusivePlayer.equals(p.getUniqueId())) {
                DataWatcher watcher = new DataWatcher(entityPlayer);
                watcher.a(new DataWatcherObject<>(one, DataWatcherRegistry.a), two);
                ((CraftPlayer) p).getHandle().b.a(new PacketPlayOutEntityMetadata(entityPlayer.ae(), watcher, true));
            }
        }
    }

    public void setPosition(Position status) {
        if (type == EntityType.FOX) {
            if (status == Position.FACEPLANTING) {
                changeEntityMetadata(17, 20);
            } else if (status == Position.SLEEPING) {
                changeEntityMetadata(17, 40);
            } else {
                changeEntityMetadata(17, 0);
            }

            return;
        }

        if (type == EntityType.SHEEP) {
            changeEntityMetadata(16, data);
            return;
        }

        if (type == EntityType.HORSE) {
            if (status == Position.REGULAR) {
                changeEntityMetadata(16, 4);
            } else if (status == Position.PRANCING) {
                changeEntityMetadata(16, 100);
            } else if (status == Position.EATING) {
                changeEntityMetadata(16, 84);
            }
            return;
        }

        if (type == EntityType.BAT) {
            if (status == Position.REGULAR) {
                changeEntityMetadata(15, 0);
            } else if (status == Position.HANGING) {
                changeEntityMetadata(15, 1);
            }
        }

        if (status == Position.REGULAR) {
            changePose("STANDING");
        } else if (status == Position.CROUCHING) {
            changePose("CROUCHING");
        } else if (status == Position.SWIMMING) {
            changePose("SWIMMING");
        } else if (status == Position.GLIDING) {
            changePose("FALL_FLYING");
        } else if (status == Position.SLEEPING) {
            changePose("SLEEPING");
        }
    }

    private void changePose(String value) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (exclusivePlayer == null || exclusivePlayer.equals(p.getUniqueId())) {
                DataWatcher datas = entityPlayer.ai();
                datas.b(DataWatcherRegistry.s.a(6), EntityPose.valueOf(value));
                ((CraftPlayer) p).getHandle().b.a(new PacketPlayOutEntityMetadata(entityPlayer.ae(), datas, false));
            }
        }

        changeEntityMetadata(17, 127);
    }

    public void setChestOpen(Location chest, int value) {
        if (chest.getBlock().getType() != Material.CHEST &&
                chest.getBlock().getType() != Material.TRAPPED_CHEST &&
                chest.getBlock().getType() != Material.ENDER_CHEST) {
            return;
        }

        Block type = Blocks.bX;

        if (chest.getBlock().getType() == Material.TRAPPED_CHEST) {
            type = Blocks.fE;
        }

        if (chest.getBlock().getType() == Material.ENDER_CHEST) {
            type = Blocks.ex;
        }

        BlockPosition pos = new BlockPosition(chest.getBlockX(), chest.getBlockY(), chest.getBlockZ());
        PacketPlayOutBlockAction packet = new PacketPlayOutBlockAction(pos, type, 1, value);

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (exclusivePlayer != null && !exclusivePlayer.equals(player.getUniqueId())) {
                continue;
            }
            ((CraftPlayer) player).getHandle().b.a(packet);
        }
    }

    public void setEquipment(Equipment equipment) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (exclusivePlayer == null || exclusivePlayer.equals(p.getUniqueId())) {
                setFakeArmor(p, entityPlayer.ae(), equipment.getHelmet(), equipment.getChestplate(), equipment.getLeggings(), equipment.getBoots(), equipment.getMainHand(), equipment.getOffHand());
            }
        }
    }

    private void setFakeArmor(Player player, int entityID, ItemStack helmet, ItemStack chestplate, ItemStack leggings, ItemStack boots, ItemStack hand, ItemStack offHand) {
        final List<Pair<EnumItemSlot, net.minecraft.world.item.ItemStack>> equipmentList = new ArrayList<>();

        equipmentList.add(new Pair<>(EnumItemSlot.f, CraftItemStack.asNMSCopy(helmet)));
        equipmentList.add(new Pair<>(EnumItemSlot.e, CraftItemStack.asNMSCopy(chestplate)));
        equipmentList.add(new Pair<>(EnumItemSlot.d, CraftItemStack.asNMSCopy(leggings)));
        equipmentList.add(new Pair<>(EnumItemSlot.c, CraftItemStack.asNMSCopy(boots)));
        equipmentList.add(new Pair<>(EnumItemSlot.a, CraftItemStack.asNMSCopy(hand)));
        equipmentList.add(new Pair<>(EnumItemSlot.b, CraftItemStack.asNMSCopy(offHand)));

        final PacketPlayOutEntityEquipment entityEquipment = new PacketPlayOutEntityEquipment(entityID, equipmentList);
        ((CraftPlayer) player).getHandle().b.a(entityEquipment);

    }
}
