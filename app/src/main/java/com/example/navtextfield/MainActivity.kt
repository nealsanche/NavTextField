package com.example.navtextfield

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.navtextfield.ui.theme.NavTextFieldTheme

class MainActivity : ComponentActivity() {

    data class Screen(val route: String, val title: String, val icon: ImageVector)

    private val bottomNavigationItems =
        listOf(
            Screen("search", "Search", Icons.Rounded.Search),
            Screen("settings", "Settings", Icons.Rounded.Settings)
        )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val navController = rememberNavController()
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination

            NavTextFieldTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Scaffold(bottomBar = {
                            BottomNavigation(elevation = 20.dp) {
                                bottomNavigationItems.forEach { screen ->
                                    BottomNavigationItem(
                                        icon = { Icon(screen.icon, screen.title) },
                                        label = { Text(screen.title) },
                                        alwaysShowLabel = false,
                                        selectedContentColor = Color.White,
                                        unselectedContentColor = Color.White.copy(alpha = ContentAlpha.disabled),
                                        selected = currentDestination?.hierarchy?.any {
                                            it.route?.startsWith(
                                                screen.route
                                            ) ?: false
                                        } == true,
                                        onClick = {
                                            navController.navigate(screen.route) {
                                                popUpTo(navController.graph.findStartDestination().id) {
                                                    saveState = true
                                                }
                                                launchSingleTop = true
                                                restoreState = true
                                            }
                                        }
                                    )
                                }
                        }
                    }) { paddingValues ->
                        NavHost(
                            navController = navController,
                            startDestination = "search",
                            modifier = Modifier.padding(paddingValues)
                        ) {

                            composable("search") {

                                Search()

                            }

                            composable("settings") {

                                Text("Settings")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Search() {
    var searchFieldValue by rememberSaveable {
        mutableStateOf("")
    }

    TextField(
        value = searchFieldValue,
        singleLine = true,
        label = { Text("Search") },
        leadingIcon = { Icon(Icons.Default.Search, "Search") },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search
        ),
        onValueChange = {
            searchFieldValue = it
        },
        modifier = Modifier.fillMaxWidth()
    )
}
