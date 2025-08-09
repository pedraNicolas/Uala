package com.pedra.uala.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pedra.uala.presentation.model.CityUiModel
import com.pedra.uala.presentation.state.CitiesUiState
import com.pedra.uala.presentation.viewmodel.CitiesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CitiesScreen(
    onCityClick: (Int) -> Unit,
    onMapClick: (Int) -> Unit,
    viewModel: CitiesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val favorites by viewModel.favorites.collectAsStateWithLifecycle()
    var searchQuery by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cities") },
                actions = {
                    IconButton(
                        onClick = { viewModel.toggleFavoritesOnly() }
                    ) {
                        val isShowingFavorites = when (val currentState = uiState) {
                            is CitiesUiState.Success -> currentState.showFavoritesOnly
                            else -> false
                        }
                        Icon(
                            imageVector = if (isShowingFavorites) {
                                Icons.Default.Favorite
                            } else {
                                Icons.Default.FavoriteBorder
                            },
                            contentDescription = "Toggle favorites"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Search Bar
            SearchBar(
                query = searchQuery,
                onQueryChange = { query ->
                    searchQuery = query
                    viewModel.searchCities(query)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )

            // Content
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
                    if (currentState.cities.isEmpty()) {
                        EmptyState()
                    } else {
                        CitiesList(
                            cities = currentState.cities,
                            onCityClick = onCityClick,
                            onMapClick = onMapClick,
                            onFavoriteToggle = { cityId ->
                                viewModel.toggleFavorite(cityId)
                            }
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
        placeholder = { Text("Search cities...") },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search"
            )
        },
        modifier = modifier,
        singleLine = true
    )
}

@Composable
private fun CitiesList(
    cities: List<CityUiModel>,
    onCityClick: (Int) -> Unit,
    onMapClick: (Int) -> Unit,
    onFavoriteToggle: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(cities) { city ->
            CityItem(
                city = city,
                onCityClick = { onCityClick(city.id) },
                onMapClick = { onMapClick(city.id) },
                onFavoriteToggle = { onFavoriteToggle(city.id) }
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
    onFavoriteToggle: () -> Unit
) {
    Card(
        onClick = onCityClick,
        modifier = Modifier.fillMaxWidth()
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
                IconButton(onClick = onMapClick) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "View on map"
                    )
                }
                
                IconButton(onClick = onFavoriteToggle) {
                    Icon(
                        imageVector = if (city.isFavorite) {
                            Icons.Default.Favorite
                        } else {
                            Icons.Default.FavoriteBorder
                        },
                        contentDescription = "Toggle favorite",
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
                text = "No cities found",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "Try adjusting your search or filters",
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
                text = "Error",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Button(onClick = onRetry) {
                Text("Retry")
            }
        }
    }
} 