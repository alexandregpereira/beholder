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

@file:Suppress("EXPERIMENTAL_API_USAGE")

package br.alexandregpereira.hunter.data.monster.lore.di

import br.alexandregpereira.hunter.data.monster.lore.remote.MonsterLoreRemoteDataSource
import br.alexandregpereira.hunter.domain.monster.lore.MonsterLoreLocalRepository
import org.koin.core.module.Module
import org.koin.core.scope.Scope
import org.koin.dsl.module

internal actual fun getAdditionalModule(): Module {
    return module {  }
}

internal actual fun Scope.createRemoteDataSource(): MonsterLoreRemoteDataSource? {
    return null
}

internal actual fun Scope.createLocalRepository(): MonsterLoreLocalRepository? {
    return null
}
