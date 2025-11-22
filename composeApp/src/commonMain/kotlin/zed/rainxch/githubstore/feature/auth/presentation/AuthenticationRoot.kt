package zed.rainxch.githubstore.feature.auth.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import zed.rainxch.githubstore.feature.auth.data.copyToClipboard
import zed.rainxch.githubstore.core.presentation.utils.openBrowser
import zed.rainxch.githubstore.core.presentation.utils.ObserveAsEvents
import zed.rainxch.githubstore.core.presentation.designsystems.theme.GithubStoreTheme

@Composable
fun AuthenticationRoot(
    viewModel: AuthenticationViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is AuthenticationEffects.OpenBrowser -> openBrowser(event.url)
            is AuthenticationEffects.CopyToClipboard -> copyToClipboard(event.label, event.text)
        }
    }

    AuthenticationScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun AuthenticationScreen(
    state: AuthenticationState,
    onAction: (AuthenticationAction) -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            when (val s = state) {
                is AuthenticationState.LoggedOut -> {
                    Text("Welcome to GitHub Store", style = MaterialTheme.typography.titleLarge)
                    Spacer(Modifier.height(16.dp))
                    Button(
                        onClick = {
                            onAction(AuthenticationAction.StartLogin("read:user repo"))
                        }
                    ) {
                        Text("Sign in with GitHub")
                    }
                }

                is AuthenticationState.DevicePrompt -> {
                    var copied by remember { mutableStateOf(s.copied) }
                    Text("Enter this code on GitHub:")
                    Spacer(Modifier.height(8.dp))
                    SelectionContainer {
                        Text(s.start.userCode, style = MaterialTheme.typography.headlineMedium)
                    }
                    Spacer(Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Button(onClick = {
                            onAction(AuthenticationAction.CopyCode(s.start))
                            copied = true
                        }) { Text(if (copied) "Copied" else "Copy code") }
                        Spacer(Modifier.height(0.dp))
                    }
                    Spacer(Modifier.height(16.dp))
                    Button(onClick = {
                        onAction(AuthenticationAction.OpenGitHub(s.start))
                    }) { Text("Open GitHub") }
                }

                is AuthenticationState.Pending -> {
                    CircularProgressIndicator()
                    Spacer(Modifier.height(12.dp))
                    Text("Waiting for authorization...")
                }

                is AuthenticationState.LoggedIn -> {
                    Text("Signed in!", style = MaterialTheme.typography.titleLarge)
                    Spacer(Modifier.height(8.dp))
                    Text("You can now use the app.")
                }

                is AuthenticationState.Error -> {
                    Text("Error: ${s.message}", color = MaterialTheme.colorScheme.error)
                    Spacer(Modifier.height(12.dp))
                    Button(onClick = { onAction(AuthenticationAction.StartLogin("read:user repo")) }) {
                        Text(
                            "Try again"
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    GithubStoreTheme {
        AuthenticationScreen(
            state = AuthenticationState.LoggedOut,
            onAction = {}
        )
    }
}