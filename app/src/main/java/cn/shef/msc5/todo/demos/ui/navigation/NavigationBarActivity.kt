package cn.shef.msc5.todo.demos.ui.navigation

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import cn.shef.msc5.todo.ui.view.getIconForScreen
import cn.shef.msc5.todo.utilities.Constants.Companion.NAVIGATION_HOME
import cn.shef.msc5.todo.utilities.Constants.Companion.NAVIGATION_DASHBOARD
import cn.shef.msc5.todo.utilities.Constants.Companion.NAVIGATION_TASKS


/**
 * @author Zhecheng Zhao
 * @email zzhao84@sheffield.ac.uk
 * @date Created in 31/10/2023 11:56
 */
class NavigationBarActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BottomNavigation()
                    //NavigationBarWithOnlySelected()
                }
            }
        }
    }
}

@Composable
fun BottomNavigation() {

    val items = listOf(NAVIGATION_HOME, NAVIGATION_TASKS, NAVIGATION_DASHBOARD)
    var selectedItem by remember { mutableStateOf(items.first()) }

    NavigationBar {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                //Change the icon using getIconForScreen function
                icon = { Icon(getIconForScreen(item), contentDescription = item) },
                label = { Text(item) },
                selected = selectedItem == item,
                onClick = { selectedItem = item },
                alwaysShowLabel = true
            )
        }
    }

}


@Composable
fun NavigationBarWithOnlySelected() {

    val items = listOf(NAVIGATION_HOME, NAVIGATION_TASKS, NAVIGATION_DASHBOARD)
    var selectedItem by remember { mutableStateOf(items.first()) }

    NavigationBar {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = { Icon(getIconForScreen(item), contentDescription = null) },
                label = { Text(item) },
                selected = item == selectedItem,
                onClick = {
                    selectedItem = item
                    Log.d("NavigationBar", item)
                },
                alwaysShowLabel = false
            )
        }
    }
}

@Preview(name = "Light theme")
@Preview(name = "Dark theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewBottomNavigation() {
    BottomNavigation()
}
