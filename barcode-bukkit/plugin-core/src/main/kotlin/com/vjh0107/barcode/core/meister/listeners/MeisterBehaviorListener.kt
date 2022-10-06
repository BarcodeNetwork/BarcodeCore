package com.vjh0107.barcode.core.meister.listeners

import com.vjh0107.barcode.core.database.getCorePlayerData
import com.vjh0107.barcode.core.dependencies.mythicmobs.adapters.MythicMobsAdapter.isActiveMob
import com.vjh0107.barcode.core.dependencies.mythicmobs.adapters.MythicMobsAdapter.toActiveMob
import com.vjh0107.barcode.core.dependencies.mythicmobs.adapters.MythicMobsAdapter.toOptionalActiveMob
import com.vjh0107.barcode.core.meister.events.MeisterDamageEvent
import com.vjh0107.barcode.core.meister.models.MeisterConfigs
import com.vjh0107.barcode.core.skill.isCalledByAllowedDamageSources
import com.vjh0107.barcode.framework.AbstractBarcodePlugin
import com.vjh0107.barcode.framework.component.BarcodeComponent
import com.vjh0107.barcode.framework.component.BarcodeListener
import com.vjh0107.barcode.framework.coroutine.extensions.MinecraftAsync
import com.vjh0107.barcode.framework.coroutine.extensions.MinecraftMain
import com.vjh0107.barcode.framework.serialization.data.SoundWrapper
import com.vjh0107.barcode.framework.utils.getEntityTarget
import com.vjh0107.barcode.framework.utils.messagesender.sendBNMessage
import io.lumine.mythic.lib.api.item.NBTItem
import kotlinx.coroutines.*
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.inventory.ItemStack

@BarcodeComponent
class MeisterBehaviorListener(private val plugin: AbstractBarcodePlugin) : BarcodeListener {
    @EventHandler(priority = EventPriority.LOW)
    fun onMythicMobDamage(event: EntityDamageByEntityEvent) {
        if (!event.entity.isActiveMob()) return
        if (isCalledByAllowedDamageSources()) return
        val player = event.damager as? Player ?: return
        val activeMob = event.entity.toActiveMob() ?: throw NullPointerException("can t get activemob")
        
        if (activeMob.type.config.getBoolean(MeisterConfigs.IS_MEISTER_MOB.configPath)) {
            val meisterType = activeMob.type.config.getString(MeisterConfigs.MEISTER_TYPE.configPath)
                ?: throw NullPointerException("can't get type of meister mob")
            val item = player.inventory.itemInMainHand
            val toolType = getToolType(item)
            if (toolType == null || toolType != meisterType) {
                player.sendBNMessage("&e$meisterType &c도구를 사용해서 시도해주세요!")
                player.playSound(player.location, Sound.ENTITY_VILLAGER_NO, 1f, 1f);
                event.isCancelled = true
                return
            }

            if (player.hasCooldown(item.type)) return
            val playerData = player.getCorePlayerData()
            //전문기술중이면 뿌순다
            if (playerData.isMeisterBehaving) {
                playerData.isMeisterBehaving = false
                return
            }

            val soundPath: String = activeMob.type.config.getString(MeisterConfigs.MEISTER_SOUND_PATH.configPath, "")
            val soundVolume: Double = activeMob.type.config.getDouble(MeisterConfigs.MEISTER_SOUND_VOLUME.configPath, 0.0)
            val soundPitch: Double = activeMob.type.config.getDouble(MeisterConfigs.MEISTER_SOUND_PITCH.configPath, 0.0)

            val soundWrapper = SoundWrapper.of(soundPath, soundVolume, soundPitch)

            CoroutineScope(Dispatchers.MinecraftAsync(plugin)).launch {
                player.sendBNMessage("&e$meisterType&f을(를) 시작합니다.")
                playerData.isMeisterBehaving = true
                while (isActive) {
                    // 마이스터행동중이 아니라면 뿌순다
                    if (!playerData.isMeisterBehaving) break
                    // 만약 4칸 이내에 엔티티가 없다면? 뿌순다
                    val lookingEntity = withContext(Dispatchers.MinecraftMain(plugin)) {
                        player.getEntityTarget(4.0)
                    } ?: break
                    val maybeActiveMob = lookingEntity.toOptionalActiveMob()
                    if (!maybeActiveMob.isPresent) break

                    // 만약 보고있는 몬스터가 처음 전문기술을 시작했던 몬스터가 아니라면? 뿌순다
                    if (maybeActiveMob.get() != activeMob) break
                    // toolType과 아이템을 다시 들고있는 아이템 타입으로 덮어씌워준다.
                    val itemInHand = player.inventory.itemInMainHand
                    val typeOfItemInHand = getToolType(itemInHand)
                    // 다시 검사했을때, 전문기술 몹과 맞는 도구를 들고 있지 않는다면? 뿌순다
                    if (typeOfItemInHand != meisterType) break

                    val cooldown = getToolCooldownTick(itemInHand)
                    val damage = getToolDamage(itemInHand)
                    withContext(Dispatchers.MinecraftMain(plugin)) {
                        player.swingMainHand()
                        player.setCooldown(itemInHand.type, cooldown)
                        //damage 주기

                        val called = MeisterDamageEvent(player, maybeActiveMob.get().entity.bukkitEntity, damage, soundWrapper)
                        Bukkit.getPluginManager().callEvent(called)
                        if (!called.isCancelled) {
                            (event.entity as LivingEntity).damage(called.damage, player)
                            called.soundWrapper.playSound(event.entity)
                        }
                    }

                    //틱을 Millisecond Long 으로 바꿔서 while 을 쉬어준다.
                    delay(cooldown.toLong() * 50L)
                }
                playerData.isMeisterBehaving = false
            }
        }
    }

    private fun getToolType(item: ItemStack): String? {
        val nbtItem = NBTItem.get(item)
        return nbtItem.getString("MMOITEMS_MEISTER_TYPE")
    }

    private fun getToolDamage(item: ItemStack): Double {
        val nbtItem = NBTItem.get(item)
        return nbtItem.getDouble("MMOITEMS_MEISTER_DAMAGE")
    }

    private fun getToolCooldownTick(item: ItemStack): Int {
        val nbtItem = NBTItem.get(item)
        val cooldown = 1 / nbtItem.getDouble("MMOITEMS_MEISTER_SPEED")
        return (cooldown * 20).toInt()
    }
}