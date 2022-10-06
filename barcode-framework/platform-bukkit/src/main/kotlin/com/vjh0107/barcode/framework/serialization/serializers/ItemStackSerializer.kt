package com.vjh0107.barcode.framework.serialization.serializers

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.bukkit.inventory.ItemStack
import org.bukkit.util.io.BukkitObjectInputStream
import org.bukkit.util.io.BukkitObjectOutputStream
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException

object ItemStackSerializer : KSerializer<ItemStack> {
    override val descriptor = PrimitiveSerialDescriptor("Bukkit.ItemStackSerializer", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): ItemStack {
        return try {
            val base64 = Base64Coder.decode(decoder.decodeString())
            val inputStream = ByteArrayInputStream(base64)
            val dataInput = BukkitObjectInputStream(inputStream)
            val itemStack = dataInput.readObject() as ItemStack
            dataInput.close()
            itemStack
//        } catch (e: IOException) {
//            throw IOException("Unable to decode class type.", e)
        } catch (e: ClassNotFoundException) {
            throw IOException("Unable to decode class type.", e)
        }
    }

    override fun serialize(encoder: Encoder, value: ItemStack) {
        val serialized = try {
            val outputStream = ByteArrayOutputStream()
            val dataOutput = BukkitObjectOutputStream(outputStream)
            dataOutput.writeObject(value)
            dataOutput.close()
            Base64Coder.encode(outputStream.toByteArray())
        } catch (e: IOException) {
            throw IllegalStateException("Unable to save item stacks.", e)
        }
        val toStringed = String(serialized)
        encoder.encodeString(toStringed)
    }
}
