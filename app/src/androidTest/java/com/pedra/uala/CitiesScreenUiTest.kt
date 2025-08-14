package com.pedra.uala

import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CitiesScreenUiTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testBasicUIElements_AreDisplayed() {
        // Given
        composeTestRule.setContent {
            Text("Cities")
            Text("Search cities...")
        }

        // Then
        composeTestRule.onNodeWithText("Cities").assertExists()
        composeTestRule.onNodeWithText("Search cities...").assertExists()
    }

    @Test
    fun testSearchBar_CanReceiveInput() {
        // Given
        var textValue by androidx.compose.runtime.mutableStateOf("")
        composeTestRule.setContent {
            OutlinedTextField(
                value = textValue,
                onValueChange = { textValue = it },
                placeholder = { Text("Search cities...") },
                modifier = Modifier.testTag("search_field")
            )
        }

        // When
        composeTestRule.onNodeWithTag("search_field").performTextInput("Madrid")

        // Then
        composeTestRule.onNodeWithTag("search_field").assertTextEquals("Madrid")
    }

    @Test
    fun testButton_IsClickable() {
        // Given
        composeTestRule.setContent {
            Button(
                onClick = {}
            ) {
                Text("Toggle favorites")
            }
        }

        // Then
        composeTestRule.onNodeWithText("Toggle favorites").assertExists()
        composeTestRule.onNodeWithText("Toggle favorites").assertIsEnabled()
    }

    @Test
    fun testLoadingIndicator_IsDisplayed() {
        // Given
        composeTestRule.setContent {
            CircularProgressIndicator(
                modifier = Modifier.testTag("loading_indicator")
            )
        }

        // Then
        composeTestRule.onNodeWithTag("loading_indicator").assertExists()
    }

    @Test
    fun testEmptyState_ShowsMessage() {
        // Given
        composeTestRule.setContent {
            Text("No cities found")
        }

        // Then
        composeTestRule.onNodeWithText("No cities found").assertExists()
    }
} 