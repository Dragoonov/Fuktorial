package com.example.fuktorial.fucktivities.tutorial

import com.example.fuktorial.database.Repository
import com.example.fuktorial.fucktivities.FucktivityViewModel


class TutorialViewModel(repository: Repository): FucktivityViewModel(repository) {
    var numberOfClicks = 0
}