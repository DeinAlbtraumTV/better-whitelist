package com.cocahonka.comfywhitelist.commands.sub

import com.cocahonka.comfywhitelist.LegacyUtils.sendMessage
import com.cocahonka.comfywhitelist.api.Storage
import com.cocahonka.comfywhitelist.commands.SubCommand
import com.cocahonka.comfywhitelist.config.message.MessageConfig
import com.cocahonka.comfywhitelist.config.message.MessageFormat
import org.bukkit.command.CommandSender

/**
 * Represents the "remove" command, which removes a player from the whitelist.
 *
 * @property storage The [Storage] instance to interact with whitelist data.
 */
class RemoveCommand(private val storage: Storage) : SubCommand {

    override val identifier = "remove"
    override val permission = "betterwhitelist.remove"
    override val usage = "/wl remove <name>"

    override fun execute(sender: CommandSender, args: Array<String>): Boolean {
        if(isInvalidUsage(sender) { args.size == 1 }) return false

        val playerName = args[0]
        if (!playerName.matches(SubCommand.playerNameRegex)){
            sender.sendMessage(MessageConfig.invalidPlayerName)
            return false
        }

        if(!storage.isPlayerWhitelisted(playerName)) {
            val replacementConfig = MessageFormat.ConfigBuilders.nameReplacementConfigBuilder(playerName)
            val message = MessageConfig.nonExistentPlayerName.replaceText(replacementConfig)
            sender.sendMessage(message)
            return false
        }

        val replacementConfig = MessageFormat.ConfigBuilders.nameReplacementConfigBuilder(playerName)
        val message = MessageConfig.playerRemoved.replaceText(replacementConfig)
        sender.sendMessage(message)

        val playerUUID = sender.server.getPlayerUniqueId(playerName).toString()

        return storage.removePlayer(playerUUID)
    }

}