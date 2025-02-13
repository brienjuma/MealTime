/*
 * Copyright 2022 Joel Kanyi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kanyideveloper.presentation.home

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.kanyideveloper.compose_ui.components.StandardToolbar
import com.kanyideveloper.core.model.Meal
import com.kanyideveloper.mealtime.core.R
import com.kanyideveloper.presentation.home.composables.TabItem
import com.kanyideveloper.presentation.home.composables.Tabs
import com.ramcosta.composedestinations.annotation.Destination

interface HomeNavigator {
    fun openHome()
    fun openMealDetails(meal: Meal)
    fun openAddMeal()
    fun popBackStack()
    fun openOnlineMealDetails(mealId: String)
    fun openRandomMeals()
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Destination
@Composable
fun HomeScreen(
    navigator: HomeNavigator,
    viewModel: HomeViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = {
            StandardToolbar(
                navigate = {},
                title = {
                    Image(
                        modifier = Modifier.size(100.dp, 100.dp),
                        painter = painterResource(id = R.drawable.ic_meal_time_banner),
                        contentDescription = null
                    )
                },
                showBackArrow = false,
                navActions = {}
            )
        },
        floatingActionButton = {
            if (viewModel.isMyMeal.value) {
                FloatingActionButton(
                    modifier = Modifier
                        .height(50.dp),
                    shape = MaterialTheme.shapes.large,
                    containerColor = MaterialTheme.colorScheme.primary,
                    content = {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.fork_knife_thin),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onTertiary
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = "Add Meal",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onTertiary
                            )
                        }
                    },
                    onClick = {
                        navigator.openAddMeal()
                    }
                )
            }
        }
    ) { paddingValues ->
        MainContent(
            navigator = navigator,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = paddingValues),
            onClick = { page ->
                if (page == 0) {
                    viewModel.setIsMyMeal(true)
                } else {
                    viewModel.setIsMyMeal(false)
                }
            }
        )
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun MainContent(
    navigator: HomeNavigator,
    modifier: Modifier = Modifier,
    onClick: (Int) -> Unit
) {
    val listOfTabs = listOf(
        TabItem.Outgoing(navigator = navigator),
        TabItem.Incoming(navigator = navigator)
    )
    val pagerState = rememberPagerState()

    Column(modifier = modifier) {
        Tabs(
            tabs = listOfTabs,
            pagerState = pagerState,
            onClick = {
                onClick(it)
            }
        )
        TabContent(
            tabs = listOfTabs,
            pagerState = pagerState
        )
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun TabContent(
    tabs: List<TabItem>,
    pagerState: PagerState
) {
    HorizontalPager(
        count = tabs.size,
        state = pagerState,
        modifier = Modifier,
        userScrollEnabled = false
    ) { page ->
        tabs[page].screen(
            onClick = {
            }
        )
    }
}
