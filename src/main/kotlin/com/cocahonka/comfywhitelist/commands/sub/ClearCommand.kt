package com.cocahonka.comfywhitelist.commands.sub

import com.cocahonka.comfywhitelist.api.Storage
import com.cocahonka.comfywhitelist.commands.SubCommand
import com.cocahonka.comfywhitelist.config.message.MessageConfig
import net.kyori.adventure.text.Component
import org.bukkit.command.CommandSender

/**
 * Represents the "clear" command, which clears the whitelist.
 *
 * @property storage The [Storage] instance to interact with whitelist data.
 */
class ClearCommand(private val storage: Storage) : SubCommand {

    override val identifier = "clear"
    override val permission = "comfywhitelist.clear"
    override val usage = "/comfywl clear"

    override fun execute(sender: CommandSender, args: Array<String>): Boolean {
        if (args.isNotEmpty()){
            val message = MessageConfig.invalidUsage.replace("%s", usage)
            sender.sendMessage(Component.text(message))
            return false
        }

        val message = MessageConfig.whitelistCleared
        sender.sendMessage(Component.text(message))
        return storage.clear()
    }

}