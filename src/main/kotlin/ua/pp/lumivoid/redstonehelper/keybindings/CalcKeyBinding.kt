package ua.pp.lumivoid.redstonehelper.keybindings

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.minecraft.client.MinecraftClient
import net.minecraft.client.option.KeyBinding
import net.minecraft.client.util.InputUtil
import org.lwjgl.glfw.GLFW
import ua.pp.lumivoid.redstonehelper.Constants
import ua.pp.lumivoid.redstonehelper.gui.CalcScreen

object CalcKeyBinding {
    private val logger = Constants.LOGGER

    private val calcKeyBinding: KeyBinding = KeyBindingHelper.registerKeyBinding(
        KeyBinding(Constants.LOCALIZEIDS.KEYBINDING_KEY_CALC,
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_N,
            Constants.LOCALIZEIDS.KEYBINDING_CATEGORY_BASIC
        )
    )

    fun register() {
        logger.debug("Registering CalcKeyBinding")

        ClientTickEvents.END_CLIENT_TICK.register { client ->
            while (calcKeyBinding.wasPressed()) {
                MinecraftClient.getInstance().setScreen(CalcScreen())
            }
        }
    }
}