package com.lambda.huds

import com.lambda.LambdaUtilities
import com.lambda.client.event.SafeClientEvent
import com.lambda.client.manager.managers.FriendManager
import com.lambda.client.plugin.api.PluginLabelHud

internal object ExampleLabelHud : PluginLabelHud(
    name = "ExampleLabelHud",
    category = Category.MISC,
    description = "Simple hud example",
    pluginMain = LambdaUtilities
) {
    private val prefix by setting("Prefix", "Server:")
    private val suffix by setting("Suffix", FriendManager.mc.currentServerData?.serverIP ?: "Unknown")

    override fun SafeClientEvent.updateText() {
        displayText.add(prefix, primaryColor)
        displayText.add(suffix, secondaryColor)
    }
}