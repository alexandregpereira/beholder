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

package br.alexandregpereira.hunter.data.monster.lore

import br.alexandregpereira.hunter.domain.monster.lore.MonsterLoreLocalRepository
import br.alexandregpereira.hunter.domain.monster.lore.model.MonsterLore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

//TODO Implement SQLDelight
internal class DefaultMonsterLoreLocalRepository : MonsterLoreLocalRepository {

    private val cache = mutableListOf<MonsterLore>()

    override fun getMonsterLore(index: String): Flow<MonsterLore> {
        return flow {
            emit(
                cache.find { it.index == index }
                    ?: throw RuntimeException("Monster lore with index $index not found")
            )
        }
    }

    override fun getLocalMonstersLore(indexes: List<String>): Flow<List<MonsterLore>> {
        return flow { emit(cache.toList()) }
    }

    override fun save(monstersLore: List<MonsterLore>): Flow<Unit> {
        return flow {
            cache.clear()
            cache.addAll(monstersLore)
            emit(Unit)
        }
    }
}
