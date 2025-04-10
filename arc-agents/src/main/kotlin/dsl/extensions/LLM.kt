// SPDX-FileCopyrightText: 2025 Deutsche Telekom AG and others
//
// SPDX-License-Identifier: Apache-2.0

package org.eclipse.lmos.arc.agents.dsl.extensions

import org.eclipse.lmos.arc.agents.ArcException
import org.eclipse.lmos.arc.agents.conversation.AssistantMessage
import org.eclipse.lmos.arc.agents.conversation.SystemMessage
import org.eclipse.lmos.arc.agents.conversation.UserMessage
import org.eclipse.lmos.arc.agents.dsl.DSLContext
import org.eclipse.lmos.arc.agents.dsl.get
import org.eclipse.lmos.arc.agents.llm.ChatCompleterProvider
import org.eclipse.lmos.arc.agents.llm.ChatCompletionSettings
import org.eclipse.lmos.arc.core.result

/**
 * Extensions enabling accessing LLMs in the DSLContext.
 */
suspend fun DSLContext.llm(
    user: String? = null,
    system: String? = null,
    model: String? = null,
    settings: ChatCompletionSettings? = null,
) = result<AssistantMessage, ArcException> {
    val chatCompleterProvider = get<ChatCompleterProvider>()
    val chatCompleter = chatCompleterProvider.provideByModel(model = model)
    val messages = buildList {
        if (system != null) add(SystemMessage(system))
        if (user != null) add(UserMessage(user))
    }
    return chatCompleter.complete(messages, null, settings = settings)
}
