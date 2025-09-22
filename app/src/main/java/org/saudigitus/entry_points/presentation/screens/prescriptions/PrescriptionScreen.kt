package org.saudigitus.entry_points.presentation.screens.prescriptions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FolderOff
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import org.saudigitus.entry_points.R
import org.saudigitus.entry_points.presentation.components.AppSnackbarHost
import org.saudigitus.entry_points.presentation.screens.prescriptions.components.ErrorBottomSheet
import org.saudigitus.entry_points.presentation.screens.prescriptions.components.PrescriptionCard
import org.saudigitus.entry_points.presentation.screens.prescriptions.components.SaveBottomSheet
import org.saudigitus.entry_points.presentation.screens.prescriptions.components.TeiCard
import org.saudigitus.entry_points.presentation.screens.prescriptions.model.InputFieldModel
import org.saudigitus.entry_points.presentation.theme.darkSuccess


@Composable
fun PrescriptionScreen(
    viewModel: PrescriptionViewModel,
    uid: String,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val cache by viewModel.cacheGivenMedicines.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadData(uid)
    }

    if (uiState.displayErrors && uiState.errorState != null) {
        ErrorBottomSheet(uiState.errorState!!) {
            viewModel.onUiEvent(PrescriptionUiEvent.CloseErrorBottomSheet)
        }
    }

    if (uiState.isSaving && uiState.saveState != null) {
        SaveBottomSheet(
            state = uiState.saveState!!,
            onDismissRequest = { viewModel.onUiEvent(PrescriptionUiEvent.CloseSaveBottomSheet) }
        ) {
            viewModel.onUiEvent(PrescriptionUiEvent.OnSave)
        }
    }

    PrescriptionUI(uiState = uiState, inputFieldModels = cache) {
        when(it) {
            is PrescriptionUiEvent.OnBack -> {
                onBack.invoke()
            }
            else -> {
                viewModel.onUiEvent(it)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PrescriptionUI(
    modifier: Modifier = Modifier,
    uiState: PrescriptionUiState,
    inputFieldModels: List<InputFieldModel>,
    onEvent: (PrescriptionUiEvent) -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val context = LocalContext.current

    if (uiState.isSaved) {
        LaunchedEffect(Unit) {
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = context.getString(R.string.prescriptions_saved)
                )
            }

            onEvent(PrescriptionUiEvent.OnBack)
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.prescriptions),
                        overflow = TextOverflow.Ellipsis,
                        softWrap = true,
                        maxLines = 1,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onEvent(PrescriptionUiEvent.OnBack) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.nav_back)
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    onEvent(PrescriptionUiEvent.OnTempSave)
                },
            ) {
                Icon(Icons.Default.Save, contentDescription = stringResource(R.string.save))

                Text(stringResource(R.string.save))
            }
        },
        snackbarHost = {
            AppSnackbarHost(snackbarHostState) { data ->
                Snackbar(
                    snackbarData = data,
                    containerColor = darkSuccess,
                    contentColor = Color.White
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (!uiState.isLoading) {
                if (uiState.prescTei != null) {
                    TeiCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        patient = uiState.prescTei
                    )
                }

                Text(
                    text = stringResource(R.string.prescription_list),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.fillMaxWidth()
                        .align(Alignment.Start)
                        .padding(horizontal = 16.dp)
                )
                if (uiState.prescriptions.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        items(uiState.prescriptions, key = { it.uid }) {
                            PrescriptionCard(
                                modifier = Modifier.fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                prescription = it,
                                inputFieldModels = inputFieldModels
                            ) { prescription, value ->
                                onEvent(
                                    PrescriptionUiEvent.OnPrescriptionValueChange(
                                        prescription,
                                        value
                                    )
                                )
                            }
                        }
                    }
                } else {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.FolderOff,
                            contentDescription = stringResource(R.string.no_prescriptions),
                            modifier = Modifier.size(128.dp)
                        )
                        Text(
                            text = stringResource(R.string.no_prescriptions),
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = MaterialTheme.typography.titleMedium.fontSize
                            ),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                        )
                    }
                }
            } else {
                CircularProgressIndicator()
            }
        }
    }
}