package com.pedra.uala.presentation.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pedra.uala.R
import com.pedra.uala.presentation.model.CityUiModel
import com.pedra.uala.presentation.state.CitiesUiState
import com.pedra.uala.presentation.viewmodel.CitiesViewModel
import com.pedra.uala.presentation.viewmodel.SharedViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CitiesScreen(
    onCityClick: (CityUiModel) -> Unit,
    onMapClick: () -> Unit,
    viewModel: CitiesViewModel = hiltViewModel(),
    sharedViewModel: SharedViewModel,
    showTopBar: Boolean = true,
    showMapButton: Boolean = showTopBar,
    selectedCityId: Int? = null
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val favorites by viewModel.favorites.collectAsStateWithLifecycle()
    
    // Combine states to avoid multiple recompositions
    val combinedState by remember(uiState, favorites) {
        derivedStateOf {
            when (val currentState = uiState) {
                is CitiesUiState.Success -> {
                    val citiesWithFavorites = currentState.cities.map { city ->
                        city.copy(isFavorite = favorites.contains(city.id))
                    }
                    Triple(
                        citiesWithFavorites,
                        currentState.searchQuery,
                        currentState.showFavoritesOnly
                    )
                }
                else -> Triple(emptyList<CityUiModel>(), "", false)
            }
        }
    }
    
    val (cities, searchQuery, showFavoritesOnly) = combinedState

    Scaffold(
        topBar = {
            if (showTopBar) {
                TopAppBar(
                    title = { Text(stringResource(R.string.title_cities)) },
                    actions = {
                        IconButton(
                            onClick = { viewModel.toggleFavoritesOnly() }
                        ) {
                            Icon(
                                imageVector = if (showFavoritesOnly) {
                                    Icons.Default.Favorite
                                } else {
                                    Icons.Default.FavoriteBorder
                                },
                                contentDescription = stringResource(R.string.btn_toggle_favorites)
                            )
                        }
                    }
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            SearchBar(
                query = searchQuery,
                onQueryChange = { query ->
                    viewModel.searchCities(query)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )

            when (val currentState = uiState) {
                is CitiesUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                is CitiesUiState.Success -> {
                    if (cities.isEmpty()) {
                        EmptyState()
                    } else {
                        CitiesList(
                            cities = cities,
                            onCityClick = onCityClick,
                            onMapClick = { city ->
                                sharedViewModel.selectCity(city)
                                onMapClick()
                            },
                            onFavoriteToggle = { cityId ->
                                viewModel.toggleFavorite(cityId)
                            },
                            showMapButton = showMapButton,
                            selectedCityId = selectedCityId
                        )
                    }
                }
                is CitiesUiState.Error -> {
                    ErrorState(
                        message = currentState.message,
                        onRetry = { viewModel.loadCities() }
                    )
                }
                is CitiesUiState.Empty -> {
                    EmptyState()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = { Text(stringResource(R.string.search_placeholder)) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = stringResource(R.string.search_content_description)
            )
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(
                    onClick = { onQueryChange("") }
                ) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = stringResource(R.string.clear_search_content_description)
                    )
                }
            }
        },
        modifier = modifier,
        singleLine = true
    )
}

@Composable
private fun CitiesList(
    cities: List<CityUiModel>,
    onCityClick: (CityUiModel) -> Unit,
    onMapClick: (CityUiModel) -> Unit,
    onFavoriteToggle: (Int) -> Unit,
    showMapButton: Boolean,
    selectedCityId: Int?
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
                            items(cities) { city ->
                        CityItem(
                            city = city,
                            onCityClick = { onCityClick(city) },
                            onMapClick = { onMapClick(city) },
                            onFavoriteToggle = { onFavoriteToggle(city.id) },
                            showMapButton = showMapButton,
                            isSelected = city.id == selectedCityId
                        )
                    }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CityItem(
    city: CityUiModel,
    onCityClick: () -> Unit,
    onMapClick: () -> Unit,
    onFavoriteToggle: () -> Unit,
    showMapButton: Boolean = true,
    isSelected: Boolean = false
) {
    Card(
        onClick = onCityClick,
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 8.dp else 2.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) {
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
            } else {
                MaterialTheme.colorScheme.surface
            }
        ),
        border = if (isSelected) {
            BorderStroke(
                width = 2.dp,
                color = MaterialTheme.colorScheme.primary
            )
        } else null
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = city.displayName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = city.coordinates.displayText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (showMapButton) {
                    IconButton(onClick = onMapClick) {
                        Icon(
                            imageVector = Icons.Default.Map,
                            contentDescription = stringResource(R.string.btn_view_on_map)
                        )
                    }
                }
                
                IconButton(onClick = onFavoriteToggle) {
                    Icon(
                        imageVector = if (city.isFavorite) {
                            Icons.Default.Favorite
                        } else {
                            Icons.Default.FavoriteBorder
                        },
                        contentDescription = stringResource(R.string.btn_toggle_favorite),
                        tint = if (city.isFavorite) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(R.string.msg_no_cities_found),
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = stringResource(R.string.msg_try_adjusting_search),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ErrorState(
    message: String,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(R.string.msg_error),
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Button(onClick = onRetry) {
                Text(stringResource(R.string.btn_retry))
            }
        }
    }
} 