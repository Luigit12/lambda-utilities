package com.lambda

import com.lambda.client.LambdaMod
import com.lambda.client.plugin.api.Plugin
import com.lambda.client.util.threads.BackgroundJob
import com.lambda.commands.ExampleCommand
import com.lambda.huds.ExampleLabelHud
import com.lambda.managers.ExampleManager
import com.lambda.modules.ExampleModule
import com.lambda.client.event.SafeClientEvent
import com.lambda.client.event.events.PacketEvent
import com.lambda.client.util.text.MessageSendHelper
import com.lambda.client.util.threads.safeListener

internal object LambdaUtilities : Plugin() {

    override fun onLoad() {
        // Load any modules, commands, or HUD elements here
        modules.add(ExampleModule)
        commands.add(ExampleCommand)
        hudElements.add(ExampleLabelHud)
        managers.add(ExampleManager)

        //bgJobs.add(BackgroundJob("ExampleJob", 10000L) { LambdaMod.LOG.info("Hello its me the BackgroundJob of your example plugin.") })
        safeListener<PacketEvent.Receive> {
            LambdaMod.LOG.info(mc.player?.serverBrand)
            mc.player?.serverBrand?.let { it1 -> MessageSendHelper.sendChatMessage(it1) }
        }
    }

    override fun onUnload() {

    }
}
