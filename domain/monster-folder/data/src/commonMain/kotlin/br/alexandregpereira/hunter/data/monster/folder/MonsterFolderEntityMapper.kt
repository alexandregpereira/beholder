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

package br.alexandregpereira.hunter.data.monster.folder

import br.alexandregpereira.hunter.data.monster.folder.local.entity.MonsterFolderCompleteEntity
import br.alexandregpereira.hunter.data.monster.local.entity.MonsterEntity
import br.alexandregpereira.hunter.domain.folder.model.MonsterFolder
import br.alexandregpereira.hunter.domain.folder.model.MonsterPreviewFolder
import br.alexandregpereira.hunter.domain.folder.model.MonsterPreviewFolderImageContentScale
import br.alexandregpereira.hunter.domain.folder.model.MonsterPreviewFolderType

internal fun List<MonsterFolderCompleteEntity>.asDomain(
    monsterImageContentScale: MonsterPreviewFolderImageContentScale
): List<MonsterFolder> {
    return this.map { it.asDomain(monsterImageContentScale) }
}

internal fun MonsterFolderCompleteEntity.asDomain(
    monsterImageContentScale: MonsterPreviewFolderImageContentScale
): MonsterFolder {
    return MonsterFolder(
        name = monsterFolderEntity.folderName,
        monsters = monsters.asDomainMonsterPreviewFolderEntity(monsterImageContentScale)
    )
}

internal fun List<MonsterEntity>.asDomainMonsterPreviewFolderEntity(
    monsterImageContentScale: MonsterPreviewFolderImageContentScale
): List<MonsterPreviewFolder> {
    return map {
        it.run {
            MonsterPreviewFolder(
                index = index,
                name = name,
                type = MonsterPreviewFolderType.valueOf(type),
                challengeRating = challengeRating.getChallengeRatingFormatted(),
                imageUrl = imageUrl,
                backgroundColorLight = backgroundColorLight,
                backgroundColorDark = backgroundColorDark,
                isHorizontalImage = isHorizontalImage,
                imageContentScale = monsterImageContentScale,
            )
        }
    }
}

private fun Float.getChallengeRatingFormatted(): String {
    return if (this < 1) {
        val value = 1 / this
        "1/${value.toInt()}"
    } else {
        this.toInt().toString()
    }
}
