package com.lumivoid.commands

import com.lumivoid.Options
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.context.CommandContext
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.Text

object InstaLamp {

    fun register(dispatcher: CommandDispatcher<ServerCommandSource>) {
        dispatcher.register(CommandManager.literal("instaLamp")
            .executes { context: CommandContext<ServerCommandSource> ->
                Options.isInstantLamps = !Options.isInstantLamps
                if (Options.isInstantLamps) {
                    context.source.sendFeedback({ Text.translatable("info.redstone-helper.insta_on") }, true)
                } else {
                    context.source.sendFeedback({ Text.translatable("info.redstone-helper.insta_off") }, true)
                }
                1
            }
        )
    }
}