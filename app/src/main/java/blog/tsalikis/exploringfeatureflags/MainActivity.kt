@file:OptIn(ExperimentalMaterial3Api::class)

package blog.tsalikis.exploringfeatureflags

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.EmojiSupportMatch
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import blog.tsalikis.exploringfeatureflags.ui.theme.ExploringFeatureFlagsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: GreetingViewModel = viewModel()
            ExploringFeatureFlagsTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text(stringResource(R.string.app_name)) }
                        )
                    },
                ) { innerPadding ->
                    Greeting(
                        name = stringResource(R.string.main_content),
                        state = viewModel.state,
                        modifier = Modifier
                            .padding(innerPadding)
                            .padding(horizontal = 16.dp)
                            .verticalScroll(rememberScrollState())
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier, state: GreetingState) {
    Box(modifier = modifier) {
        Text(
            text = name,
            modifier = Modifier
        )
        when (state) {
            is GreetingState.Success -> {
                var showDialog by rememberSaveable { mutableStateOf(state.showWelcomeMessage) }
                if (showDialog) {
                    BasicAlertDialog(
                        onDismissRequest = { showDialog = false }
                    ) {
                        Surface(
                            shape = MaterialTheme.shapes.medium,
                            tonalElevation = 6.dp
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(24.dp)
                                    .width(IntrinsicSize.Min)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    if (state.flagEmoji.isNotEmpty()) {
                                        Text(
                                            text = state.flagEmoji,
                                            style = TextStyle(
                                                platformStyle = PlatformTextStyle(
                                                    emojiSupportMatch = EmojiSupportMatch.None
                                                )
                                            ),
                                            modifier = Modifier.padding(end = 5.dp)
                                        )
                                        Text(
                                            text = stringResource(R.string.welcome_title),
                                            style = MaterialTheme.typography.titleLarge
                                        )
                                    }
                                }

                                Spacer(Modifier.height(12.dp))
                                Text(stringResource(R.string.welcome_body))
                            }
                        }

                    }
                }
            }

            else -> {
                //do nothing
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ExploringFeatureFlagsTheme {
        Greeting("Android", state = GreetingState.Loading)
    }
}
