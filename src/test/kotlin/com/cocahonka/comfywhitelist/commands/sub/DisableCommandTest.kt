package com.cocahonka.comfywhitelist.commands.sub

import be.seeseemelk.mockbukkit.command.MessageTarget
import be.seeseemelk.mockbukkit.entity.PlayerMock
import com.cocahonka.comfywhitelist.commands.CommandTestBase
import com.cocahonka.comfywhitelist.config.message.Message
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DisableCommandTest : CommandTestBase() {

    private lateinit var disableCommand: DisableCommand
    private lateinit var label: String

    @BeforeEach
    override fun setUp() {
        super.setUp()
        disableCommand = DisableCommand(generalConfig)
        label = disableCommand.identifier

        generalConfig.enableWhitelist()
        TODO("Register listener to prevent connection")

        playerWithPermission.addAttachment(plugin, disableCommand.permission, true)
    }

    private fun assertOnlyDisableMessage(sender: MessageTarget){
        assertEquals(
            sender.nextMessage(),
            Message.WhitelistDisabled.getDefault(locale)
        )
        sender.assertNoMoreSaid()
    }

    @Test
    fun `when console is sender`() {
        val result = handler.onCommand(
            sender = console,
            command = command,
            label = label,
            args = arrayOf(disableCommand.identifier)
        )

        val reconnectedPlayer = server.addPlayer()

        assertTrue(result)
        assertWhitelistDisabled()
        assertConnectedTrue(reconnectedPlayer)
        assertOnlyDisableMessage(console)
    }

    @Test
    fun `when player is sender without permission`() {
        val result = handler.onCommand(
            sender = playerWithoutPermission,
            command = command,
            label = label,
            args = arrayOf(disableCommand.identifier)
        )

        val reconnectedPlayer = server.addPlayer()

        assertFalse(result)
        assertWhitelistEnabled()
        assertConnectedFalse(reconnectedPlayer)
        assertOnlyNoPermissionMessage(playerWithoutPermission)
    }

    @Test
    fun `when player is sender with permission`() {
        val result = handler.onCommand(
            sender = playerWithPermission,
            command = command,
            label = label,
            args = arrayOf(disableCommand.identifier)
        )

        val reconnectedPlayer = server.addPlayer()

        assertTrue(result)
        assertWhitelistDisabled()
        assertConnectedTrue(reconnectedPlayer)
        assertOnlyDisableMessage(playerWithPermission)
    }

    @Test
    fun `when to many arguments`() {
        val result = handler.onCommand(
            sender = console,
            command = command,
            label = label,
            args = arrayOf(disableCommand.identifier, disableCommand.identifier)
        )

        val reconnectedPlayer = server.addPlayer()

        assertFalse(result)
        assertWhitelistEnabled()
        assertConnectedFalse(reconnectedPlayer)
        assertOnlyInvalidUsageMessage(console, disableCommand.usage)
    }

}