@file:JvmName("NMSModuleGen")
@file:JvmMultifileClass
package org.koin.ksp.generated

import org.koin.dsl.*


val com_vjh0107_barcode_framework_nms_NMSModule = module {
	val moduleInstance = com.vjh0107.barcode.framework.nms.NMSModule()
	single(qualifier=null) { moduleInstance.provideNMSWrapper() } bind(com.vjh0107.barcode.framework.nms.NMSService::class)
}
val com.vjh0107.barcode.framework.nms.NMSModule.module : org.koin.core.module.Module get() = com_vjh0107_barcode_framework_nms_NMSModule