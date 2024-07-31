package ua.pp.lumivoid.keybindings

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.minecraft.client.option.KeyBinding
import net.minecraft.client.util.InputUtil
import net.minecraft.text.Text
import org.lwjgl.glfw.GLFW
import ua.pp.lumivoid.ClientOptions
import ua.pp.lumivoid.Constants
import ua.pp.lumivoid.gui.HudToast
import ua.pp.lumivoid.util.features.AutoWire

object PreviousAutoWireModeKeyBinding {
    private val logger = Constants.LOGGER

    private val previousAutoWireKeyBinding: KeyBinding = KeyBindingHelper.registerKeyBinding(
        KeyBinding(Constants.LocalizeIds.KEYBINDING_KEY_PREVIOUSAUTOWIREMODE,
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_COMMA,
            Constants.LocalizeIds.KEYBINDING_CATEGORY_BASIC
        )
    )

    fun register() {
        logger.debug("Registering PreviousAutoWireModeKeyBinding")

        ClientTickEvents.END_CLIENT_TICK.register { client ->
            while (previousAutoWireKeyBinding.wasPressed()) {
                if (client.player!!.commandSource.hasPermissionLevel(2)) {
                    AutoWire.setMode(AutoWire.previous(ClientOptions.autoWireMode))
                    HudToast.addToastToQueue(Text.translatable(Constants.LocalizeIds.FEATURE_AUTOWIRE_CURRENTAUTOWIREMODE, Text.translatable("redstone-helper.feature.auto_wire.modes.${ClientOptions.autoWireMode.toString().lowercase()}")), true)
                } else {
                    HudToast.addToastToQueue(Text.translatable(Constants.LocalizeIds.STUFF_INFO_ERROR_NOPERMISSION), true)
                }
            }
        }
    }
}