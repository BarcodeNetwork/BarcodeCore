package com.vjh0107.barcode.cutscene

import be.seeseemelk.mockbukkit.MockBukkit
import com.vjh0107.barcode.cutscene.npc.data.Skin
import com.vjh0107.barcode.framework.BarcodeFrameworkModule
import com.vjh0107.barcode.framework.utils.print
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldNotBe
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import java.util.*

class SkinTest : AnnotationSpec() {
    lateinit var ktorComponent: BarcodeFrameworkModule

    @Before
    fun init() {
        ktorComponent = BarcodeFrameworkModule().also {
            it.load()
            BarcodeFrameworkModule.instance = it
        }
    }

    val server = MockBukkit.getMock()
    val plugin = MockBukkit.load(CutscenePluginMocked::class.java)

    @Test
    fun getSkin() = runTest {
        val dispatcher = StandardTestDispatcher(this.testScheduler)
        // 실제 있는 플레이어
        val uuid = "bc770b0e-60d6-4fe5-b5e9-c7b83c0daeb8"
        val skin = withContext(dispatcher) {
            Skin.fetchSkin(UUID.fromString(uuid))
        }
        skin.value.print() shouldNotBe null

        // 실제로 없는 플레이어 uuid 를 모장 api 서버에 요청하였을 때,
        val uuid1 = server.getPlayer(0).uniqueId.print()
        try {
            withContext(dispatcher) {
                Skin.fetchSkin(uuid1)
            }
            assert(false)
        } catch (exception: Exception) {
            assert(true)
        }
    }

    @After
    fun tearDown() {
        MockBukkit.unmock()
        BarcodeFrameworkModule.instance.close()
    }
}