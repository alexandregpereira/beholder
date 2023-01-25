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

package br.alexandregpereira.hunter.folder.preview.di

import br.alexandregpereira.hunter.folder.preview.FolderPreviewEventManager
import br.alexandregpereira.hunter.folder.preview.event.FolderPreviewEventDispatcher
import br.alexandregpereira.hunter.folder.preview.event.FolderPreviewResultListener
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.dsl.module

val folderPreviewModule = module {
    single { FolderPreviewEventManager() }
    single<FolderPreviewEventDispatcher> { get<FolderPreviewEventManager>() }
    single<FolderPreviewResultListener> { get<FolderPreviewEventManager>() }
}

@Module
@InstallIn(SingletonComponent::class)
class FolderPreviewEventDispatcherModuleImpl : KoinComponent {

    @Singleton
    @Provides
    internal fun provideFolderPreviewEventManager(): FolderPreviewEventManager {
        return get()
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class FolderPreviewEventDispatcherModule {

    @Singleton
    @Binds
    internal abstract fun bindFolderPreviewEventDispatcher(
        folderPreviewEventDispatcher: FolderPreviewEventManager
    ): FolderPreviewEventDispatcher

    @Singleton
    @Binds
    internal abstract fun bindFolderPreviewResultListener(
        folderPreviewEventDispatcher: FolderPreviewEventManager
    ): FolderPreviewResultListener
}
