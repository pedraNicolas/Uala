package com.pedra.uala.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.pedra.uala.domain.repository.FavoritesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "favorites")

class FavoritesRepositoryImpl @Inject constructor(
    private val context: Context
) : FavoritesRepository {

    private val favoritesKey = stringSetPreferencesKey("favorite_cities")

    override fun getFavorites(): Flow<Set<Int>> {
        return context.dataStore.data.map { preferences ->
            preferences[favoritesKey]?.map { it.toInt() }?.toSet() ?: emptySet()
        }
    }

    override suspend fun addFavorite(cityId: Int) {
        context.dataStore.edit { preferences ->
            val currentFavorites = preferences[favoritesKey] ?: emptySet()
            preferences[favoritesKey] = currentFavorites + cityId.toString()
        }
    }

    override suspend fun removeFavorite(cityId: Int) {
        context.dataStore.edit { preferences ->
            val currentFavorites = preferences[favoritesKey] ?: emptySet()
            preferences[favoritesKey] = currentFavorites - cityId.toString()
        }
    }

    override suspend fun toggleFavorite(cityId: Int) {
        context.dataStore.edit { preferences ->
            val currentFavorites = preferences[favoritesKey] ?: emptySet()
            preferences[favoritesKey] = if (currentFavorites.contains(cityId.toString())) {
                currentFavorites - cityId.toString()
            } else {
                currentFavorites + cityId.toString()
            }
        }
    }

    override suspend fun clearFavorites() {
        context.dataStore.edit { preferences ->
            preferences.remove(favoritesKey)
        }
    }
} 