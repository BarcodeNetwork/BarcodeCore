package com.vjh0107.barcode.mmoitems

import com.vjh0107.barcode.framework.component.BarcodeComponent
import com.vjh0107.barcode.framework.component.BarcodeKoinModule
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.ksp.generated.module

@BarcodeComponent
@Module
@ComponentScan
class BarcodeMMOItemsModule : BarcodeKoinModule {
    override val targetModule get() = module
}