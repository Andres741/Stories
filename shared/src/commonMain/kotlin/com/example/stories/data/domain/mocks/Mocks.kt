package com.example.stories.data.domain.mocks

import com.example.stories.data.domain.model.Element
import com.example.stories.data.domain.model.History
import com.example.stories.infrastructure.date.add
import com.example.stories.infrastructure.date.now
import com.example.stories.infrastructure.date.range
import com.example.stories.infrastructure.date.toRange
import com.example.stories.infrastructure.loading.LoadStatus
import kotlinx.datetime.LocalDate

class Mocks {

    companion object {
        var id = 0L
    }
    fun getMockStories() = listOf(
        History(
            id = id++,
            title = "Viaje al monte Bromo",
            dateRange = LocalDate.now().add(monthNumber = -2).toRange(),
            mainElement = Element.Image(id = id++, imageResource = "https://harindabama.files.wordpress.com/2012/10/bromo11.jpg"),
            elements = listOf(
                Element.Text(id = id++, text = "Monté a caballo en el mar de arena que rodea al volcán activo Bromo, en el este de Java."),
                Element.Image(id = id++, imageResource = "https://www.elperiodicodelturismo.com/images/crater-monte-bromo.webp"),
                Element.Text(id = id++, text = "Monté a caballo en el mar de arena que rodea al volcán activo Bromo, en el este de Java."),
                Element.Image(id = id++, imageResource = "https://www.elperiodicodelturismo.com/images/crater-monte-bromo.webp"),
                Element.Text(id = id++, text = "Monté a caballo en el mar de arena que rodea al volcán activo Bromo, en el este de Java."),
                Element.Image(id = id++, imageResource = "https://www.elperiodicodelturismo.com/images/crater-monte-bromo.webp"),
                Element.Text(id = id++, text = "Monté a caballo en el mar de arena que rodea al volcán activo Bromo, en el este de Java."),
                Element.Image(id = id++, imageResource = "https://www.elperiodicodelturismo.com/images/crater-monte-bromo.webp"),
                Element.Text(id = id++, text = "Monté a caballo en el mar de arena que rodea al volcán activo Bromo, en el este de Java."),
                Element.Image(id = id++, imageResource = "https://www.elperiodicodelturismo.com/images/crater-monte-bromo.webp"),
            ),
        ),
        History(
            id = id++,
            title = "Submarinismo en el USS Liberty",
            dateRange = LocalDate.now().add(monthNumber = -1).toRange(),
            mainElement = Element.Text(id = id++, text = "Hice submarinismo dentro del USS Liberty, un barco estadounidense hundido en el noreste de Bali derante la Segunda Gerra Mundial por un submarino japonés."),
            elements = listOf(
                Element.Image(id = id++, imageResource = "https://media.tacdn.com/media/attractions-splice-spp-674x446/07/95/10/33.jpg"),
            ),
        ),
        History(
            id = id++,
            title = "Visita a Kuala Lumpur",
            dateRange = LocalDate.now().add(dayOfMonth = -7) range LocalDate.now(),
            mainElement = Element.Text(id = id++, text = "Estuve una semana en Kuala Lumpur, la capital de Malasia."),
            elements = listOf(
                Element.Image(id = id++, imageResource = "https://images.pexels.com/photos/433989/pexels-photo-433989.jpeg"),
            ),
        ),
    )

    fun getMockLoadStatusStrings() = LoadStatus.Data(listOf("hello", "world", "kmm"))
    fun getHistoryElementText() = Element.Text(id = id++, text = "Monté a caballo en el mar de arena que rodea al volcán activo Bromo, en el este de Java.")
    fun getHistoryElementImage() = Element.Image(id = id++, imageResource = "https://www.elperiodicodelturismo.com/images/crater-monte-bromo.webp")

    fun getDateRange() = LocalDate.now().add(dayOfMonth = -7) range LocalDate.now()
}
