package com.example.stories.data.repository.history.model

import com.example.stories.infrastructure.date.add
import com.example.stories.infrastructure.date.now
import com.example.stories.infrastructure.date.range
import com.example.stories.infrastructure.date.toRange
import com.example.stories.infrastructure.loading.LoadStatus
import kotlinx.datetime.LocalDate

class HistoryMocks {

    companion object {
        var id = 0L
    }
    fun getMockStories() = listOf(
        History(
            id = id++,
            title = "Viaje al monte Bromo",
            dateRange = LocalDate(2023, 5, 21).toRange(),
            elements = listOf(
                HistoryElement.Image(id = id++, imageResource = "https://harindabama.files.wordpress.com/2012/10/bromo11.jpg"),
                HistoryElement.Text(id = id++, text = "Monté a caballo en el mar de arena que rodea al volcán activo Bromo, en el este de Java."),
                HistoryElement.Image(id = id++, imageResource = "https://www.elperiodicodelturismo.com/images/crater-monte-bromo.webp"),
                HistoryElement.Text(id = id++, text = "Monté a caballo en el mar de arena que rodea al volcán activo Bromo, en el este de Java."),
                HistoryElement.Image(id = id++, imageResource = "https://www.elperiodicodelturismo.com/images/crater-monte-bromo.webp"),
                HistoryElement.Text(id = id++, text = "Monté a caballo en el mar de arena que rodea al volcán activo Bromo, en el este de Java."),
                HistoryElement.Image(id = id++, imageResource = "https://www.elperiodicodelturismo.com/images/crater-monte-bromo.webp"),
                HistoryElement.Text(id = id++, text = "Monté a caballo en el mar de arena que rodea al volcán activo Bromo, en el este de Java."),
                HistoryElement.Image(id = id++, imageResource = "https://www.elperiodicodelturismo.com/images/crater-monte-bromo.webp"),
                HistoryElement.Text(id = id++, text = "Monté a caballo en el mar de arena que rodea al volcán activo Bromo, en el este de Java."),
                HistoryElement.Image(id = id++, imageResource = "https://www.elperiodicodelturismo.com/images/crater-monte-bromo.webp"),
            ),
        ),
        History(
            id = id++,
            title = "Submarinismo en el USS Liberty",
            dateRange = LocalDate(2023, 7, 12).toRange(),
            elements = listOf(
                HistoryElement.Text(id = id++, text = "Hice submarinismo dentro del USS Liberty, un barco estadounidense hundido en el noreste de Bali derante la Segunda Gerra Mundial por un submarino japonés."),
                HistoryElement.Image(id = id++, imageResource = "https://media.tacdn.com/media/attractions-splice-spp-674x446/07/95/10/33.jpg"),
            ),
        ),
        History(
            id = id++,
            title = "Visita a Kuala Lumpur",
            dateRange = LocalDate(2023, 8, 5) range LocalDate.now(),
            elements = listOf(
                HistoryElement.Text(id = id++, text = "Estuve una semana en Kuala Lumpur, la capital de Malasia."),
                HistoryElement.Image(id = id++, imageResource = "https://images.pexels.com/photos/433989/pexels-photo-433989.jpeg"),
            ),
        ),
    )

    fun getMockLoadStatusStrings() = LoadStatus.Data(listOf("hello", "world", "kmm"))
    fun getHistoryElementText() = HistoryElement.Text(id = id++, text = "Monté a caballo en el mar de arena que rodea al volcán activo Bromo, en el este de Java.")
    fun getHistoryElementImage() = HistoryElement.Image(id = id++, imageResource = "https://www.elperiodicodelturismo.com/images/crater-monte-bromo.webp")

    fun getDateRange() = LocalDate.now().add(dayOfMonth = -7) range LocalDate.now()
}
