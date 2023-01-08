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

package br.alexandregpereira.hunter.domain.settings

import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class SaveContentVersionUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {

    operator fun invoke(contentVersion: Int): Flow<Unit> {
        return settingsRepository.saveSettings(mapOf(CONTENT_VERSION_KEY to contentVersion.toString()))
    }

    companion object {
        const val CONTENT_VERSION_KEY = "CONTENT_VERSION_KEY"
    }
}