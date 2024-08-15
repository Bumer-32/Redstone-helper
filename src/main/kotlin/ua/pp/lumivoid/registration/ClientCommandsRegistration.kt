package ua.pp.lumivoid.registration

import com.mojang.brigadier.CommandDispatcher
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import ua.pp.lumivoid.Config
import ua.pp.lumivoid.commands.*

object ClientCommandsRegistration {
    fun register() {
        ClientCommandRegistrationCallback.EVENT.register { dispatcher: CommandDispatcher<FabricClientCommandSource?>, registryAccess ->
            CalcCommand.register(dispatcher)
            AutoWireCommand.register(dispatcher)
            RedstoneHelperCommand.register(dispatcher)
            CalcRedstoneSignalCommand.register(dispatcher, registryAccess)
            BitsCommand.register(dispatcher)
            QuickTpCommand.register(dispatcher)
            RedstoneFillCommand.register(dispatcher, registryAccess)
            RedstoneFillSignalCommand.register(dispatcher, registryAccess)
            ClearInventoryCommand.register(dispatcher)
            AirPlaceCommand.register(dispatcher)
            MacroCommand.register(dispatcher)
            GlassCommand.register(dispatcher)
            //CopyStateCommand.register(dispatcher)

            val config = Config()
            if (config.customQuickTpEnabled) CustomQuickTpCommand.register(dispatcher)
        }
    }
}