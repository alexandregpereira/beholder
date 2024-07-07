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

package br.alexandregpereira.hunter.spell.detail.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.ui.compose.ScreenHeader
import br.alexandregpereira.hunter.ui.compose.Window
import br.alexandregpereira.hunter.ui.util.toColor
import org.jetbrains.compose.resources.painterResource
import br.alexandregpereira.hunter.ui.compose.SchoolOfMagicState as UiSchoolOfMagicState

@Composable
internal fun SpellHeader(
    title: String,
    subtitle: String,
    schoolIcon: UiSchoolOfMagicState,
    modifier: Modifier = Modifier
) = Layout(
    modifier = modifier,
    content = {
        val iconColor = if (isSystemInDarkTheme()) {
            schoolIcon.iconColorDark
        } else schoolIcon.iconColorLight
        val iconAlpha = 0.2f

        Icon(
            painter = painterResource(schoolIcon.icon),
            contentDescription = subtitle,
            tint = iconColor.toColor(),
            modifier = Modifier.alpha(iconAlpha)
        )

        ScreenHeader(
            title = title,
            subTitle = subtitle,
            modifier = Modifier
                .padding(vertical = 16.dp)
        )

        Box(
            modifier = Modifier.background(
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.3f)
            )
        )
    }
) { measurables, constraints ->
    val titlePlaceable = measurables[1].measure(constraints)

    val iconHeight = titlePlaceable.height * 2
    val iconPlaceable = measurables[0].measure(
        Constraints.fixed(width = iconHeight, height = iconHeight)
    )
    val dividerHeight = measurables[2].measure(
        Constraints.fixed(
            width = constraints.maxWidth - iconPlaceable.width + 8.dp.toPx().toInt(),
            height = 1.dp.toPx().toInt()
        )
    )

    val outOfBoundsDistance = 24.dp.toPx().toInt()

    layout(constraints.maxWidth, titlePlaceable.height + dividerHeight.height) {
        iconPlaceable.placeRelative(
            x = constraints.maxWidth - (iconPlaceable.width - outOfBoundsDistance),
            y = -(titlePlaceable.height / 2)
        )

        titlePlaceable.placeRelative(
            x = 0,
            y = 0
        )

        dividerHeight.placeRelative(
            x = 0,
            y = titlePlaceable.height
        )
    }
}

@Preview
@Composable
private fun SpellHeaderPreview() = Window {
    SpellHeader(
        title = "Detect Good and Evil asda",
        subtitle = "Level 1",
        schoolIcon = UiSchoolOfMagicState.ABJURATION
    )
}
