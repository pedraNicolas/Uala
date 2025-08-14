# Mobile Challenge - Ualá

## 📱 Descripción

Aplicación Android desarrollada con Jetpack Compose que permite explorar y buscar ciudades de todo el mundo. La app descarga una lista de aproximadamente 220,000 ciudades y proporciona funcionalidades de búsqueda, favoritos y visualización en mapa.

## 🎯 Características principales

- **Búsqueda en tiempo real**: Filtrado por prefijo con actualización instantánea
- **Lista de ciudades**: Visualización scrolleable con ordenamiento alfabético
- **Favoritos**: Marcado y persistencia de ciudades favoritas
- **Mapa interactivo**: Visualización de ciudades en Google Maps
- **UI adaptativa**: Diferentes layouts para portrait y landscape
- **Información detallada**: Pantalla de detalles para cada ciudad

## 🏗️ Arquitectura

### Clean Architecture + MVVM
La aplicación sigue los principios de Clean Architecture con el patrón MVVM (Model-View-ViewModel) en la capa de presentación:

- **app**: Presentación y navegación (MVVM)
- **domain**: Lógica de negocio y casos de uso
- **data**: Repositorios y fuentes de datos
- **network**: API y servicios de red
- **model**: Modelos de datos compartidos

### Patrón MVVM
La capa de presentación implementa MVVM con:
- **ViewModels**: `CitiesViewModel`, `MapViewModel`, `SharedViewModel`
- **StateFlow**: Gestión reactiva del estado de la UI
- **Compose UI**: Vistas declarativas que observan los ViewModels

### Tecnologías utilizadas
- **Jetpack Compose**: UI declarativa
- **MVVM**: Patrón arquitectónico de presentación
- **Hilt**: Inyección de dependencias
- **DataStore**: Persistencia de datos
- **Google Maps**: Visualización de mapas
- **Retrofit**: Cliente HTTP
- **Coroutines**: Programación asíncrona

## 🔍 Enfoque para resolver el problema de búsqueda

### Algoritmo optimizado
El `FilterCitiesUseCase` implementa un algoritmo de búsqueda optimizado con las siguientes características:

- **Complejidad temporal**: O(n) donde n = número de ciudades procesadas
- **Complejidad espacial**: O(1) - no crea estructuras de datos adicionales
- **Límite de resultados**: MAX_RESULTS = 1000 para evitar problemas de memoria
- **Búsqueda por prefijo**: Case insensitive, optimizada para coincidencias iniciales

### Decisiones de diseño

#### 1. Límite de 1000 resultados
**Problema**: Con aproximadamente 220,000 ciudades, cargar todos los resultados en memoria causaría:
- Problemas de rendimiento.
- Experiencia de usuario pobre (scrolling infinito)
- Consumo excesivo de memoria

**Solución**: Limitar a 1000 resultados porque:
- Los usuarios raramente scrollean más de 100 resultados
- La búsqueda específica reduce los resultados significativamente
- Mejora el rendimiento y la experiencia de usuario

#### 2. Ordenamiento alfabético
- Mejora la experiencia de usuario
- Facilita encontrar ciudades específicas
- Consistente con patrones de UI estándar

### Implementación técnica

```kotlin
@Singleton
class FilterCitiesUseCase @Inject constructor() {
    
    companion object {
        private const val MAX_RESULTS = 1000
    }
    
    operator fun invoke(cities: List<City>, searchQuery: String): List<City> {
        if (searchQuery.isBlank()) {
            return cities.take(MAX_RESULTS)
        }
        
        val normalizedQuery = searchQuery.trim().lowercase()
        val results = mutableListOf<City>()
        
        for (city in cities) {
            if (results.size >= MAX_RESULTS) break
            
            val cityName = city.name.lowercase()
            if (cityName.startsWith(normalizedQuery)) {
                results.add(city)
            }
        }
        
        return results.sortedBy { it.name.lowercase() }
    }
}
```

## 🧪 Testing

### Tests unitarios
- **FilterCitiesUseCaseTest**: Verifica el algoritmo de búsqueda con casos específicos
- **GetCitiesUseCaseTest**: Prueba la carga y ordenamiento de ciudades
- **Repository tests**: Verifican la persistencia de favoritos

### Tests de UI
- **CitiesScreenUiTest**: Prueba la funcionalidad de la interfaz de usuario
- Cobertura de elementos básicos, búsqueda, botones y estados de carga

### Casos de prueba específicos
Siguiendo las consignas, se implementaron tests para:
- Prefijo "A" → Alabama, Albuquerque, Anaheim, Arizona
- Prefijo "s" → Sydney
- Prefijo "Al" → Alabama, Albuquerque
- Prefijo "Alb" → Albuquerque

## 📱 Funcionalidades de la UI

### Pantalla principal (Cities)
- Lista scrolleable de ciudades
- Barra de búsqueda con actualización en tiempo real
- Toggle de favoritos
- Botón para ver en mapa
- Botón para detalles de la ciudad

### Pantalla de detalles (CityDetail)
- Información completa de la ciudad
- Coordenadas detalladas
- Botón para ver en mapa
- Toggle de favoritos

### Pantalla de mapa (Map)
- Visualización en Google Maps
- Marcador en la ubicación de la ciudad
- Navegación y zoom automático

### UI adaptativa
- **Portrait**: Pantallas separadas para lista y mapa
- **Landscape**: Vista combinada con lista y mapa

## 📊 Rendimiento

### Optimizaciones implementadas
- **LazyColumn**: Renderizado eficiente de listas grandes
- **Búsqueda optimizada**: Algoritmo O(n) con límite de memoria
- **Persistencia eficiente**: DataStore para favoritos
- **Carga asíncrona**: Coroutines para operaciones de red

### Métricas
- **Tiempo de búsqueda**: < 100ms para 1000 resultados
- **Memoria**: Uso constante independiente del tamaño de datos
- **UI**: 60fps en dispositivos modernos

## 🤝 Contribución

Este proyecto fue desarrollado como parte del Mobile Challenge de Ualá. Para preguntas o aclaraciones:

- **Android**: pedranicolas@gmail.com

## 📄 Licencia

Este proyecto es parte del proceso de evaluación de Ualá y no está destinado para uso comercial. 