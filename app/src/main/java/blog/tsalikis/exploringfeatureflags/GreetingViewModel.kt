package blog.tsalikis.exploringfeatureflags

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import blog.tsalikis.exploringfeatureflags.data.ApiService
import kotlinx.coroutines.launch

class GreetingViewModel : ViewModel() {

    var state by mutableStateOf<GreetingState>(GreetingState.Loading)
        private set

    init {
        viewModelScope.launch {
            val country = "BR"
            val response = ApiService.retrofit.getFeatures(country)
            val showCountryFlag = response.showCountryFlag
            val flagEmoji = if (showCountryFlag) {
                when (country) {
                    "GR" -> "🇬🇷"
                    "DE" -> "🇩🇪"
                    "BR" -> "🇧🇷"
                    "US" -> "🇺🇸"
                    else -> "🏳️"
                }
            } else {
                ""
            }
            state = GreetingState.Success(response.showWelcomeMessage, flagEmoji)
        }
    }
}

sealed class GreetingState {
    object Loading : GreetingState()
    data class Success(
        val showWelcomeMessage: Boolean,
        val flagEmoji: String,
    ) : GreetingState()

    data class Error(val throwable: Throwable) : GreetingState()
}
