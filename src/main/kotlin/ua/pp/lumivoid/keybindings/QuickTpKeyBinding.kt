package ua.pp.lumivoid.keybindings

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.minecraft.client.option.KeyBinding
import net.minecraft.client.util.InputUtil
import org.lwjgl.glfw.GLFW
import ua.pp.lumivoid.Config
import ua.pp.lumivoid.Constants
import ua.pp.lumivoid.packets.QuickTeleportPacket
import ua.pp.lumivoid.util.SendPacket

object QuickTpKeyBinding {
    private val logger = Constants.LOGGER

    private val quickTpKeyBinding: KeyBinding = KeyBindingHelper.registerKeyBinding(
        KeyBinding("key.redstone-helper.quickTp_keybinding",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_G,
            "key.redstone-helper.category.basic"
        )
    )

    fun register() {
        logger.debug("Registering QuickTpKeyBinding")

        ClientTickEvents.END_CLIENT_TICK.register { client ->
            while (quickTpKeyBinding.wasPressed()) {
                val config = Config()
                SendPacket.sendPacket(QuickTeleportPacket(config.quickTpDistance, config.quickTpIncludeFluids, Constants.aMinecraftClass))
            }
        }
    }
}