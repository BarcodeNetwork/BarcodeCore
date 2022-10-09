package com.vjh0107.barcode.framework.nms

import com.vjh0107.barcode.framework.utils.uncheckedNonnullCast
import org.bukkit.Bukkit
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
class NMSModule {
    @Single(binds = [NMSService::class])
    fun provideNMSWrapper(): NMSService {
        val nmsWrapper = try {
            Class.forName("com.vjh0107.barcode.framework.nms.impl.NMSWrapper_${getNMSVersion()}")
        } catch (exception: ClassNotFoundException) {
            Bukkit.getLogger().severe("버전에 맞는 NMSWrapper 가 없습니다: ${getNMSVersion()}")
            throw exception
        }
        return nmsWrapper.getDeclaredConstructor().newInstance().uncheckedNonnullCast()
    }

    private fun getNMSVersion(): String {
        val version = Bukkit.getServer().javaClass.getPackage().name;
        return version.substring(version.lastIndexOf('.') + 1);
    }
}