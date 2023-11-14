package cn.shef.msc5.todo.ui.screen

import android.content.res.Configuration
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import cn.shef.msc5.todo.base.BaseScaffold
import cn.shef.msc5.todo.demos.ui.navigation.getIconForScreen
import cn.shef.msc5.todo.model.database.AppDatabase
import cn.shef.msc5.todo.model.viewmodel.MainViewModel
import cn.shef.msc5.todo.model.viewmodel.MainViewModelFactory
import cn.shef.msc5.todo.utilities.Constants

/**
 * @author Zhecheng Zhao
 * @email zzhao84@sheffield.ac.uk
 * @date Created in 05/11/2023 22:56
 */
@Composable
fun MainScreen() {

    val mainViewModel by lazy {
        MainViewModelFactory(AppDatabase.INSTANCE.getTaskDAO()).
            create(MainViewModel::class.java)
    }

    //get context
    val items = listOf(
        Constants.NAVIGATION_HOME,
        Constants.NAVIGATION_SEARCH,
        Constants.NAVIGATION_PROFILE
    )

    var selectedItem by remember { mutableStateOf(items.first()) }
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    BaseScaffold(
        showTopBar = false,
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = { Icon(getIconForScreen(item), contentDescription = null) },
                        label = { Text(item) },
                        selected = item == selectedItem,
                        onClick = {
                            selectedItem = item
                        },
                        alwaysShowLabel = false
                    )
                }
            }
        },
        hostState = snackbarHostState,
    ) {
        when (selectedItem) {
            Constants.NAVIGATION_HOME -> HomeScreen(context, mainViewModel)
            Constants.NAVIGATION_SEARCH -> EmptyScreen(context)
            Constants.NAVIGATION_PROFILE -> EmptyScreen(context)
        }
    }
}


@Preview(name = "Light theme")
@Preview(name = "Dark theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewMainScreen() {
//    MainScreen(LocalContext.current)
}
