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

package br.alexandregpereira.hunter.data.monster.spell.local.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class SpellUsageCompleteEntity(
    @Embedded val spellUsage: SpellUsageEntity,
    @Relation(
        parentColumn = "spellUsageId",
        entityColumn = "spellIndex",
        associateBy = Junction(SpellUsageSpellCrossRefEntity::class),
    )
    val spells: List<SpellPreviewEntity>
)