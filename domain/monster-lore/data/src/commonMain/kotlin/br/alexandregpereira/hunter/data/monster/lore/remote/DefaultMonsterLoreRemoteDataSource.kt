/*
 * Copyright (C) 2024 Alexandre Gomes Pereira
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package br.alexandregpereira.hunter.data.monster.lore.remote

import br.alexandregpereira.hunter.data.monster.lore.remote.model.MonsterLoreDto
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json

internal class DefaultMonsterLoreRemoteDataSource(
    private val client: HttpClient,
    private val json: Json
) : MonsterLoreRemoteDataSource {

    override fun getMonstersLore(sourceAcronym: String, lang: String): Flow<List<MonsterLoreDto>> {
        return flow {
            emit(
                client.get("$lang/lore/${sourceAcronym.lowercase()}/monster-lore.json")
                    .bodyAsText()
                    .let { json.decodeFromString(it) }
            )
        }
    }
}
