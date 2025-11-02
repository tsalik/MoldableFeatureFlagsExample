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
            val response = ApiService.retrofit.getFeatures("GR")
            state = GreetingState.Success(response.showWelcomeMessage)
        }
    }
}

sealed class GreetingState {
    object Loading : GreetingState()
    data class Success(val showWelcomeMessage: Boolean) : GreetingState()
    data class Error(val throwable: Throwable) : GreetingState()
}
