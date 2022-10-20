/*
 * Hunter - DnD 5th edition monster compendium application
 * Copyright (C) 2022 Alexandre Gomes Pereira
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package br.alexandregpereira.hunter.folder.preview

import br.alexandregpereira.hunter.folder.preview.event.FolderPreviewConsumerEvent
import br.alexandregpereira.hunter.folder.preview.event.FolderPreviewConsumerEventDispatcher
import br.alexandregpereira.hunter.folder.preview.event.FolderPreviewConsumerEventListener
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

internal class FolderPreviewConsumerEventDispatcherImpl :
    FolderPreviewConsumerEventDispatcher,
    FolderPreviewConsumerEventListener {

    private val _events: MutableSharedFlow<FolderPreviewConsumerEvent> = MutableSharedFlow(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    override val events: Flow<FolderPreviewConsumerEvent> = _events

    override fun dispatchEvent(event: FolderPreviewConsumerEvent) {
        _events.tryEmit(event)
    }
}