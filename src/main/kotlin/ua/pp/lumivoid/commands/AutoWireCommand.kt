package ua.pp.lumivoid.commands

import com.mojang.brigadier.CommandDispatcher
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.client.MinecraftClient
import ua.pp.lumivoid.Constants
import ua.pp.lumivoid.gui.AutowireScreen

object AutoWireCommand {
    private val logger = Constants.LOGGER

    fun register(dispatcher: CommandDispatcher<FabricClientCommandSource?>) {
        logger.debug("/autowire: Registering autowire command")

        dispatcher.register(ClientCommandManager.literal("autowire")
            .requires {source -> source.hasPermissionLevel(2)}
            .executes {
                logger.debug("/autowire: Opening autowire menu")
                MinecraftClient.getInstance().send(Runnable {
                    MinecraftClient.getInstance().setScreen(AutowireScreen())
                })
                1
            }
        )
    }
}