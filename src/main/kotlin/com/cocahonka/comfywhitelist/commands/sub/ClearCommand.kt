package com.cocahonka.comfywhitelist.commands.sub

import com.cocahonka.comfywhitelist.LegacyUtils.sendMessage
import com.cocahonka.comfywhitelist.api.Storage
import com.cocahonka.comfywhitelist.commands.SubCommand
import com.cocahonka.comfywhitelist.config.general.GeneralConfig
import com.cocahonka.comfywhitelist.config.message.MessageConfig
import org.bukkit.command.CommandSender

/**
 * Represents the "clear" command, which clears the whitelist.
 *
 * @property storage The [Storage] instance to interact with whitelist data.
 */
class ClearCommand(private val storage: Storage) : SubCommand {

    override val identifier = "clear"
    override val permission = "betterwhitelist.clear"
    override val usage = "/wl clear"

    override fun execute(sender: CommandSender, args: Array<String>): Boolean {
        if(isInvalidUsage(sender) { args.isEmpty() }) return false

        if(!GeneralConfig.clearCommandEnabled) {
            sender.sendMessage(MessageConfig.inactiveCommand)
            return false
        }

        sender.sendMessage(MessageConfig.whitelistCleared)

        return storage.clear()
    }

}