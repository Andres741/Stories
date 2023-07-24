package com.example.stories.data.domain.mocks

import com.example.stories.data.domain.model.Element
import com.example.stories.data.domain.model.History
import com.example.stories.infrastructure.date.now
import kotlinx.datetime.LocalDateTime

class Mocks {
    fun getMockStories() = listOf(
        History(
            id = 0,
            title = "Viaje al monte Bromo",
            mainImage = "https://harindabama.files.wordpress.com/2012/10/bromo11.jpg",
            date = LocalDateTime.now(),
            elements = listOf(
                Element.Text("Monté a caballo en el mar de arena que rodea al volcán activo Bromo, en el este de Java."),
                Element.Image("https://www.elperiodicodelturismo.com/images/crater-monte-bromo.webp")
            )
        ),
        History(
            id = 1,
            title = "Submarinismo en el USS Liberty",
            mainImage = null,
            date = LocalDateTime.now(),
            elements = listOf(
                Element.Text("Hice submarinismo dentro del USS Liberty, un barco estadounidense hundido en el noreste de Bali derante la Segunda Gerra Mundial por un submarino japonés."),
                Element.Image("https://media.tacdn.com/media/attractions-splice-spp-674x446/07/95/10/33.jpg")
            )
        ),
    )
}
