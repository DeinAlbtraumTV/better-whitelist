package com.cocahonka.comfywhitelist.commands.sub

import be.seeseemelk.mockbukkit.command.MessageTarget
import com.cocahonka.comfywhitelist.commands.CommandTestBase
import com.cocahonka.comfywhitelist.commands.SubCommand
import com.cocahonka.comfywhitelist.config.message.MessageFormat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class HelpCommandTest : CommandTestBase() {

    private lateinit var helpCommand: HelpCommand
    private lateinit var label: String

    private lateinit var commands: List<SubCommand>
    private val helpMessage: String =
        "<comfy>\n" +
                "> /wl add <name>\n" +
                "> /wl remove <name>\n" +
                "> /wl list\n" +
                "> /wl status\n" +
                "> /wl on\n" +
                "> /wl off\n" +
                "> /wl clear\n" +
                "> /wl reload"

    @BeforeEach
    override fun setUp() {
        super.setUp()
        commands = listOf(
            AddCommand(storage),
            RemoveCommand(storage),
            ClearCommand(storage),
            ListCommand(storage),
            EnableCommand(generalConfig),
            DisableCommand(generalConfig),
            ReloadCommand(plugin),
        )

        helpCommand = HelpCommand(commands)
        label = helpCommand.identifier

        playerWithPermission.addAttachment(plugin, helpCommand.permission, true)
    }

    private fun assertOnlyHelpMessage(sender: MessageTarget) {
        assertEquals(
            sender.nextMessage(),
            legacySection.serialize(MessageFormat.applyStyles(helpMessage))
        )
        sender.assertNoMoreSaid()
    }

    @Test
    fun `when console is sender`() {
        val result = handler.onCommand(
            sender = console,
            command = command,
            label = label,
            args = arrayOf(helpCommand.identifier)
        )

        assertTrue(result)
        assertOnlyHelpMessage(console)
    }

    @Test
    fun `when player is sender without permission`() {
        val result = handler.onCommand(
            sender = playerWithoutPermission,
            command = command,
            label = label,
            args = arrayOf(helpCommand.identifier)
        )

        assertFalse(result)
        assertOnlyNoPermissionMessage(playerWithoutPermission)
    }

    @Test
    fun `when player is sender with permission`() {
        val result = handler.onCommand(
            sender = playerWithPermission,
            command = command,
            label = label,
            args = arrayOf(helpCommand.identifier)
        )

        assertTrue(result)
        assertOnlyHelpMessage(playerWithPermission)
    }

    @Test
    fun `when to many arguments`() {
        val result = handler.onCommand(
            sender = console,
            command = command,
            label = label,
            args = arrayOf(helpCommand.identifier, helpCommand.identifier),
        )

        assertFalse(result)
        assertOnlyInvalidUsageMessage(console, helpCommand.usage)
    }
}