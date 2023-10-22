/*
 * Copyright 2022 Alexandre Gomes Pereira
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

@file:OptIn(ExperimentalAnimationApi::class, ExperimentalFoundationApi::class)

package br.alexandregpereira.hunter.detail

import androidx.activity.compose.BackHandler
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.detail.ui.MonsterDetailOptionPicker
import br.alexandregpereira.hunter.detail.ui.MonsterDetailScreen
import br.alexandregpereira.hunter.ui.compose.FormField
import br.alexandregpereira.hunter.ui.compose.FormBottomSheet
import br.alexandregpereira.hunter.ui.compose.LoadingScreen
import br.alexandregpereira.hunter.ui.compose.SwipeVerticalToDismiss
import br.alexandregpereira.hunter.ui.theme.HunterTheme
import br.alexandregpereira.hunter.ui.theme.Shapes
import org.koin.androidx.compose.koinViewModel

@Composable
fun MonsterDetailFeature(
    contentPadding: PaddingValues = PaddingValues(0.dp),
) = HunterTheme {
    val viewModel: MonsterDetailViewModel = koinViewModel()
    val viewState by viewModel.state.collectAsState()

    BackHandler(enabled = viewState.showDetail) {
        viewModel.onClose()
    }

    SwipeVerticalToDismiss(visible = viewState.showDetail, onClose = viewModel::onClose) {
        LoadingScreen(
            isLoading = viewState.isLoading,
            showCircularLoading = false,
            modifier = Modifier
                .background(MaterialTheme.colors.surface)
                .clip(Shapes.large)
        ) {
            if (viewState.monsters.isEmpty()) return@LoadingScreen
            MonsterDetailScreen(
                viewState.monsters,
                viewState.initialMonsterListPositionIndex,
                contentPadding,
                onMonsterChanged = { monster ->
                    viewModel.onMonsterChanged(monster.index)
                },
                onOptionsClicked = viewModel::onShowOptionsClicked,
                onSpellClicked = viewModel::onSpellClicked,
                onLoreClicked = viewModel::onLoreClicked,
            )

            MonsterDetailOptionPicker(
                options = viewState.options,
                showOptions = viewState.showOptions,
                onOptionSelected = viewModel::onOptionClicked,
                onClosed = viewModel::onShowOptionsClosed
            )

            FormBottomSheet(
                title = stringResource(viewState.formTitle.titleRes),
                formFields = viewState.formFields.map {
                    FormField(
                        key = it.key.name,
                        label = stringResource(it.key.labelRes),
                        value = it.value
                    )
                },
                opened = viewState.showForm,
                contentPadding = contentPadding,
                buttonEnabled = viewState.formButtonEnabled,
                onFormChanged = viewModel::onFormChanged,
                onClosed = viewModel::onFormClosed,
                onSaved = viewModel::onFormSaved,
            )
        }
    }
}
