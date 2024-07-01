@file:Suppress("LoggingStringTemplateAsArgument")

package ua.pp.lumivoid.commands

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.context.CommandContext
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.client.MinecraftClient
import net.minecraft.command.CommandRegistryAccess
import net.minecraft.command.argument.ItemStackArgumentType
import net.minecraft.inventory.Inventory
import net.minecraft.item.Item
import net.minecraft.registry.Registries
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.hit.HitResult
import net.minecraft.util.hit.HitResult.Type
import ua.pp.lumivoid.Constants
import kotlin.random.Random

object CalcRedstoneSignalCommand {
    private val logger = Constants.LOGGER

    fun register(dispatcher: CommandDispatcher<FabricClientCommandSource?>, registryAccess: CommandRegistryAccess) {
        logger.debug("/calc-redstone-signal: Registering calc-redstone-signal command")

        dispatcher.register(ClientCommandManager.literal("calc-redstone-signal")
            .requires { source -> source.hasPermissionLevel(2) }
            .executes { context ->
                context.source.sendError(Text.translatable("info_error.redstone-helper.missing_arguments"))
                1
            }
            .then(ClientCommandManager.argument("signal", IntegerArgumentType.integer(1, 15))
                .executes { context ->
                    logger.debug("/calc-redstone-signal: Calculating redstone signal from chat")
                    execute(context, Registries.ITEM.get(Identifier.of("minecraft:wooden_shovel")), IntegerArgumentType.getInteger(context, "signal"))
                    1
                }
                .then(ClientCommandManager.argument("item", ItemStackArgumentType.itemStack(registryAccess))
                    .executes { context ->
                        logger.debug("/calc-redstone-signal: Calculating redstone signal from chat with item")
                        execute(context, ItemStackArgumentType.getItemStackArgument(context, "item").item, IntegerArgumentType.getInteger(context, "signal"))
                        1
                    }
                )
            )
        )
    }

    private fun execute(context: CommandContext<FabricClientCommandSource>, item: Item, redstoneSignal: Int) {
        if (redstoneSignal == 0) {
            val funnyInt = Random.nextInt(
                1,
                Text.translatable("dontlocalize.stuff.redstone-helper.funny_count").string.toInt() + 1
            )
            context.source.sendFeedback(Text.translatable("info.redstone-helper.funny.$funnyInt"))
        } else {
            val hit: HitResult = MinecraftClient.getInstance().crosshairTarget!!
            if (hit.type == Type.BLOCK) {
                val blockHit = hit as BlockHitResult
                val blockPos = blockHit.blockPos
                try {
                    val blockInventory: Inventory = context.source.client.world!!.getBlockEntity(blockPos) as Inventory
                    val amount = ((redstoneSignal - 1) * item.maxCount * blockInventory.size() / 14.0).toInt() + 1

                    logger.debug("/calc-redstone-signal: Calculated redstone signal: $amount, item: ${item.name}")
                    context.source.sendFeedback(
                        Text.translatable(
                            "info.redstone-helper.calculated_signal",
                            amount,
                            item.name
                        )
                    )
                } catch (e: NullPointerException) {
                    logger.debug("/calc-redstone-signal: Failed to get block inventory at $blockPos, think it`s not a block entity with inventory")
                    context.source.sendError(Text.translatable("info_error.redstone-helper.invalid_block_inventory"))
                }
            } else {
                logger.debug("/calc-redstone-signal: No block in crosshair target")
                context.source.sendError(Text.translatable("info_error.redstone-helper.no_block_found"))
            }
        }
    }
}


