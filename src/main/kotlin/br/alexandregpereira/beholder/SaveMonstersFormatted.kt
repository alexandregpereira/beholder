package br.alexandregpereira.beholder

import br.alexandregpereira.beholder.data.model.*
import br.alexandregpereira.beholder.dndapi.data.Monster
import br.alexandregpereira.beholder.dndapi.data.MonsterType
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.serialization.decodeFromString
import okhttp3.*
import java.io.IOException
import br.alexandregpereira.beholder.data.model.Monster as MonsterFormatted
import br.alexandregpereira.beholder.data.model.MonsterType as MonsterTypeFormatted

private const val GITHUB_IMAGE_HOST =
    "https://raw.githubusercontent.com/alexandregpereira/dnd-monster-manual/main/images"

@FlowPreview
@ExperimentalCoroutinesApi
suspend fun main() = start {
    val monsters = json.decodeFromString<List<Monster>>(readJsonFile(JSON_FILE_NAME))
        .asMonstersFormatted()
        .asSequence()
        .asFlow()
        .flatMapMerge {
            it.downloadImage()
        }
        .toList()
        .filterNotNull()
        .sortedBy { it.id }

    println("\n${monsters.size} monsters formatted")
    monsters.forEach { println("id: ${it.id}, name: ${it.name}") }

    saveJsonFile(monsters, JSON_FORMATTED_FILE_NAME)
}

private fun List<Monster>.asMonstersFormatted(): List<MonsterFormatted> {
    return this.filter { it.type != MonsterType.OTHER }.map {
        MonsterFormatted(
            id = it.getId(),
            type = MonsterTypeFormatted.valueOf(it.type.name),
            name = it.name,
            subtitle = it.formatSubtitle(),
            imageUrl = it.getImageUrl(),
            size = it.size,
            alignment = it.alignment,
            subtype = it.subtype,
            armorClass = it.armorClass,
            hitPoints = it.hitPoints,
            hitDice = it.hitDice,
            speed = it.asSpeedFormatted(),
            abilityScores = it.asAbilityScoresFormatted()
        )
    }
}

private fun Monster.formatSubtitle(): String {
    val subType = if (subtype.isNullOrEmpty()) "," else "($subtype),"
    return "$size ${type.name.toLowerCase()}$subType $alignment"
}

private fun Monster.getImageUrl(): String {
    val imageName = getId()
    return "$GITHUB_IMAGE_HOST/$imageName.png"
}

private fun Monster.getId(): String {
    return if (isDragon()) {
        "dragon-$index"
    } else if (subtype.isNullOrEmpty() || subtypeBlackList.contains(subtype)) {
        index
    } else {
        "$subtype-$index"
    }
}

private fun Monster.isDragon(): Boolean {
    return this.index.endsWith("-dragon")
}

private fun Monster.asSpeedFormatted(): Speed = speed.run {
    val burrow = createSpeedValue(SpeedType.BURROW, burrow)
    val climb = createSpeedValue(SpeedType.CLIMB, climb)
    val fly = createSpeedValue(SpeedType.FLY, fly)
    val walk = createSpeedValue(SpeedType.WALK, walk)
    val swim = createSpeedValue(SpeedType.SWIM, swim)
    return Speed(
        hover = hover,
        values = listOfNotNull(burrow, climb, fly, walk, swim)
    )
}

private fun createSpeedValue(speedType: SpeedType, value: String?): SpeedValue? = value?.let {
    SpeedValue(
        type = speedType,
        measurementUnit = MeasurementUnit.FEET,
        distance = it.split(" ").first().toFloat()
    )
}

private fun Monster.asAbilityScoresFormatted(): List<AbilityScore> {
    val strength = AbilityScore(
        type = AbilityScoreType.STRENGTH,
        value = strength,
        modifier = calculateAbilityScoreModifier(strength)
    )
    val dexterity = AbilityScore(
        type = AbilityScoreType.DEXTERITY,
        value = dexterity,
        modifier = calculateAbilityScoreModifier(dexterity)
    )
    val constitution = AbilityScore(
        type = AbilityScoreType.CONSTITUTION,
        value = constitution,
        modifier = calculateAbilityScoreModifier(constitution)
    )
    val intelligence = AbilityScore(
        type = AbilityScoreType.INTELLIGENCE,
        value = intelligence,
        modifier = calculateAbilityScoreModifier(intelligence)
    )
    val wisdom = AbilityScore(
        type = AbilityScoreType.WISDOM,
        value = wisdom,
        modifier = calculateAbilityScoreModifier(wisdom)
    )
    val charisma = AbilityScore(
        type = AbilityScoreType.CHARISMA,
        value = charisma,
        modifier = calculateAbilityScoreModifier(charisma)
    )
    return listOf(strength, dexterity, constitution, intelligence, wisdom, charisma)
}

private fun calculateAbilityScoreModifier(value: Int): Int {
    return when (value) {
        1 -> -5
        in 2..3 -> -4
        in 4..5 -> -3
        in 6..7 -> -2
        in 8..9 -> -1
        in 10..11 -> 0
        in 12..13 -> 1
        in 14..15 -> 2
        in 16..17 -> 3
        in 18..19 -> 4
        in 20..21 -> 5
        in 22..23 -> 6
        in 24..25 -> 7
        in 26..27 -> 8
        in 28..29 -> 9
        else -> 10
    }
}

@ExperimentalCoroutinesApi
private fun MonsterFormatted.downloadImage(): Flow<MonsterFormatted?> = callbackFlow {
    val client = OkHttpClient()

    val request: Request = Request.Builder()
        .url(imageUrl)
        .build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            println("request failed: $id: " + e.message)
            channel.offer(null)
            channel.close()
        }

        @Throws(IOException::class)
        override fun onResponse(call: Call, response: Response) {
            if (response.isSuccessful) {
                println("request success: $id")
                channel.offer(element = this@downloadImage)

            } else {
                println("request failed: $id")
                channel.offer(element = null)
            }
            channel.close()
        }
    })

    awaitClose()
}

private val subtypeBlackList = listOf(
    "any race",
    "dwarf",
    "elf",
    "gnoll",
    "gnome",
    "goblinoid",
    "grimlock",
    "human",
    "kobold",
    "sahuagin",
    "shapechanger",
    "titan",
)