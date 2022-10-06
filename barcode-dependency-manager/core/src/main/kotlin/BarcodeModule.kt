interface BarcodeModule {
    val path: String
    object Bukkit {
        object CORE : BarcodeModule {
            override val path: String = ":barcode-bukkit:plugin-core"
        }
        object ADVANCEMENT : BarcodeModule {
            override val path: String = ":barcode-bukkit:plugin-advancement"
        }

        object Modules {
            object DATABASE : BarcodeModule {
                override val path: String = ":barcode-bukkit:modules:database"
            }
        }
    }

    object Framework {
        object BUKKIT : BarcodeModule {
            override val path: String = ":barcode-framework:platform-bukkit"
        }
        object BUKKIT_COMMON : BarcodeModule {
            override val path: String = ":barcode-framework:platform-bukkit-common"
        }
        object BUKKIT_V1_19_R1 : BarcodeModule {
            override val path: String = ":barcode-framework:platform-bukkit-v1_19_R1"
        }

        object KTOR : BarcodeModule {
            override val path: String = ":barcode-framework:platform-ktor"
        }
        object DATABASE : BarcodeModule {
            override val path: String = ":barcode-framework:database"
        }
        object COMMON : BarcodeModule {
            override val path: String = ":barcode-framework:common"
        }
        object KOIN : BarcodeModule {
            override val path: String = ":barcode-framework:koin"
        }
        object GOOGLE_SHEETS : BarcodeModule {
            override val path: String = ":barcode-framework:google-sheets"
        }
    }

    object MMO {
        object ITEMS : BarcodeModule {
            override val path: String = ":barcodecore-mmo:mmoitems"
        }

        object CORE : BarcodeModule {
            override val path: String = ":barcodecore-mmo:mmocore"
        }

        object MYTHIC_LIB : BarcodeModule {
            override val path: String = ":barcodecore-mmo:mythiclib"
        }
    }
}