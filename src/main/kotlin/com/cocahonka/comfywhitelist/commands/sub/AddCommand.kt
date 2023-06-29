package com.cocahonka.comfywhitelist.commands.sub

import com.cocahonka.comfywhitelist.LegacyUtils.sendMessage
import com.cocahonka.comfywhitelist.api.Storage
import com.cocahonka.comfywhitelist.commands.SubCommand
import com.cocahonka.comfywhitelist.config.message.MessageConfig
import com.cocahonka.comfywhitelist.config.message.MessageFormat
import org.bukkit.Server
import org.bukkit.command.CommandSender

/**
 * Represents the "add" command, which adds a player to the whitelist.
 *
 * @property storage The [Storage] instance to interact with whitelist data.
 */
class AddCommand(private val storage: Storage) : SubCommand {

    override val identifier = "add"
    override val permission = "betterwhitelist.add"
    override val usage = "/wl add <name>"

    override fun execute(sender: CommandSender, args: Array<String>): Boolean {
        if(isInvalidUsage(sender) { args.size == 1 }) return false

        val playerName = args[0]
        if (!playerName.matches(SubCommand.playerNameRegex)){
            sender.sendMessage(MessageConfig.invalidPlayerName)
            return false
        }

        val replacementConfig = MessageFormat.ConfigBuilders.nameReplacementConfigBuilder(playerName)
        val message = MessageConfig.playerAdded.replaceText(replacementConfig)
        sender.sendMessage(message)

        val playerUUID = sender.server.getPlayerUniqueId(playerName).toString()

        return storage.addPlayer(playerUUID)
    }

}
