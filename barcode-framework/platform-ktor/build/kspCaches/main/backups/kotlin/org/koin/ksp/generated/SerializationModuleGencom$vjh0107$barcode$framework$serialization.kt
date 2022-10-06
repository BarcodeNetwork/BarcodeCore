@file:JvmName("SerializationModuleGen")
@file:JvmMultifileClass
package org.koin.ksp.generated

import org.koin.dsl.*


val com_vjh0107_barcode_framework_serialization_SerializationModule = module {
	val moduleInstance = com.vjh0107.barcode.framework.serialization.SerializationModule()
	single(qualifier=null) { moduleInstance.json() } 
}
val com.vjh0107.barcode.framework.serialization.SerializationModule.module : org.koin.core.module.Module get() = com_vjh0107_barcode_framework_serialization_SerializationModule