package com.vjh0107.barcode.framework.koin

import org.koin.core.definition.IndexKey
import org.koin.core.instance.InstanceFactory
import org.koin.core.module.Module

@Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")
fun Module.getMappings(): HashMap<IndexKey, InstanceFactory<*>> {
    return this.mappings
}