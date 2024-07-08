/*
 * Copyright 2023 Alexandre Gomes Pereira
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.alexandregpereira.hunter.state

import br.alexandregpereira.flow.StateFlowWrapper
import br.alexandregpereira.flow.wrap
import br.alexandregpereira.hunter.ui.EmptyStateRecovery
import br.alexandregpereira.hunter.ui.StateRecovery
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

abstract class UiModel<State : Any>(
    initialState: State,
    protected val uiStateRecovery: StateRecovery<State> = EmptyStateRecovery(),
) : StateHolder<State> {

    protected val scope = CoroutineScope(
        SupervisorJob() + Dispatchers.Main.immediate
    )

    private val _state = MutableStateFlow(initialState)
    override val state: StateFlowWrapper<State> = _state.wrap(scope)

    init {
        if (uiStateRecovery !is EmptyStateRecovery) {
            uiStateRecovery.onStateChanges.onEach { state ->
                setState { state }
            }.launchIn(scope)
        }
    }

    protected fun setState(block: State.() -> State) {
        _state.value = _state.value.block()
    }

    protected fun setStateAndSave(block: State.() -> State) {
        uiStateRecovery.saveState(state.value.block())
    }

    open fun onCleared() {
        scope.cancel()
    }
}

interface StateHolder<State : Any> {

    val state: StateFlowWrapper<State>
}
