package br.com.tick.ui.screens.wallet.usecases

import br.com.tick.sdk.repositories.categorycolor.CategoryColorRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetCategoryColors @Inject constructor(private val categoryColorRepository: CategoryColorRepository) {

    operator fun invoke(): Flow<List<Int>> {
        return categoryColorRepository.getColors().map { categoryColors -> categoryColors.map { it.color } }
    }
}
