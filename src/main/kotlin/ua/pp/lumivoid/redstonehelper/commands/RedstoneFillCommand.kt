@file:Suppress("LoggingStringTemplateAsArgument", "LoggingSimilarMessage")

package ua.pp.lumivoid.redstonehelper.commands

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.IntegerArgumentType
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.client.MinecraftClient
import net.minecraft.command.CommandRegistryAccess
import net.minecraft.command.argument.ItemStackArgumentType
import net.minecraft.inventory.Inventory
import net.minecraft.registry.Registries
import net.minecraft.text.Text
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.hit.HitResult
import net.minecraft.util.hit.HitResult.Type
import ua.pp.lumivoid.redstonehelper.Constants
import ua.pp.lumivoid.redstonehelper.gui.HudToast
import ua.pp.lumivoid.redstonehelper.network.SendPacket
import ua.pp.lumivoid.redstonehelper.network.packets.c2s.FillInventoryC2SPacket
import kotlin.random.Random


object RedstoneFillCommand {
    private val logger = Constants.LOGGER

    fun register(dispatcher: CommandDispatcher<FabricClientCommandSource?>, registryAccess: CommandRegistryAccess) {
        logger.debug("/redstone-fill: Registering redstone-fill command")

        dispatcher.register(ClientCommandManager.literal("redstone-fill")
            .requires { source -> source.hasPermissionLevel(2) }
            .executes { context ->
                logger.debug("/redstone-fill: Missing arguments!")
                context.source.sendError(Text.translatable(Constants.LOCALIZEIDS.STUFF_INFO_ERROR_MISSINGARGUMENTS))
                1
            }
            .then(ClientCommandManager.argument("item", ItemStackArgumentType.itemStack(registryAccess))
                .executes { context ->
                    logger.debug("/redstone-fill: Missing arguments!")
                    context.source.sendError(Text.translatable(Constants.LOCALIZEIDS.STUFF_INFO_ERROR_MISSINGARGUMENTS))
                    1
                }
                .then(
                    ClientCommandManager.argument("count", IntegerArgumentType.integer(0, 1728))
                    .executes { context ->
                        if (IntegerArgumentType.getInteger(context, "count") == 0) {
                            val funnyInt = Random.nextInt(1, Constants.LOCALIZEIDS.Counts.FUNNY_COUNT + 1)
                            HudToast.addToastToQueue(Text.translatable("redstone-helper.stuff.funny.$funnyInt"), false)
                        } else {
                            logger.debug("/redstone-fill: Trying to fill inventory with blocks")

                            val hit: HitResult = MinecraftClient.getInstance().crosshairTarget!!

                            if (hit.type == Type.BLOCK) {
                                val blockHit = hit as BlockHitResult
                                val blockPos = blockHit.blockPos

                                try {
                                    context.source.world.getBlockEntity(blockPos) as Inventory // just to testing is it block entity

                                    val item = ItemStackArgumentType.getItemStackArgument(context, "item").item
                                    val amount = IntegerArgumentType.getInteger(context, "count")

                                    SendPacket.sendPacket(FillInventoryC2SPacket(blockPos, Registries.ITEM.getId(item), amount, Constants.aMinecraftClass))
                                } catch (e: NullPointerException) {
                                    logger.debug("/redstone-fill: Failed to get block inventory at $blockPos, think it`s not a block entity with inventory")
                                    HudToast.addToastToQueue(Text.translatable(Constants.LOCALIZEIDS.STUFF_INFO_ERROR_INVALIDBLOCKINVENTORY))
                                }
                            } else {
                                logger.debug("/calc-redstone-signal: No block in crosshair target")
                                HudToast.addToastToQueue(Text.translatable(Constants.LOCALIZEIDS.STUFF_INFO_ERROR_BLOCKNOTFOUND))
                            }
                        }
                        1
                    }
                )
            )
        )
    }
}