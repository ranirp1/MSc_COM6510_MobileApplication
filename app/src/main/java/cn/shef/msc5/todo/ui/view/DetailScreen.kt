package cn.shef.msc5.todo.ui.view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.location.Geocoder
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.OutlinedTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cn.shef.msc5.todo.R
import cn.shef.msc5.todo.activity.GeoLocationActivity
import cn.shef.msc5.todo.activity.MapsActivity
import cn.shef.msc5.todo.base.component.BaseScaffold
import cn.shef.msc5.todo.base.component.CheckboxListTextFieldExample
import cn.shef.msc5.todo.base.component.Chips
import cn.shef.msc5.todo.base.component.DatePicker
import cn.shef.msc5.todo.base.component.bottombar.BottomActionBar
import cn.shef.msc5.todo.base.component.dialog.TimePickerDialog
import cn.shef.msc5.todo.model.TaskStateEnum
import cn.shef.msc5.todo.model.dto.SubTask
import cn.shef.msc5.todo.model.getPriorityValues
import cn.shef.msc5.todo.model.getTemplateStr
import cn.shef.msc5.todo.model.getTemplateTextStr
import cn.shef.msc5.todo.model.viewmodel.MainViewModel
import cn.shef.msc5.todo.utilities.DateConverter
import cn.shef.msc5.todo.base.component.dialog.ImageBottomSheet
import cn.shef.msc5.todo.utilities.GeneralUtil
import cn.shef.msc5.todo.utilities.ImageUtil
import cn.shef.msc5.todo.utilities.SharedPreferenceManger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.sql.Date

/**
 * @author Zhecheng Zhao
 * @email zzhao84@sheffield.ac.uk
 * @date Created in 13/11/2023 13:28
 */
@OptIn(ExperimentalMaterial3Api::class)
@ExperimentalAnimationApi
@SuppressLint("UnrememberedMutableState")
@Composable
fun DetailScreen(
    mainViewModel: MainViewModel
) {

    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope: CoroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    val dateConverter = DateConverter()
    val contentResolver = context.contentResolver
    val imageUtil = ImageUtil()

    var showCalender by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    var date by remember { mutableStateOf(mainViewModel.date) }
    val state = rememberTimePickerState()

    val priorityLevels = getPriorityValues()
    var prior by remember { mutableIntStateOf(2) }

    val templates = getTemplateStr()
    val templateDesc = getTemplateTextStr()
    var selectedTemplate by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("") }
    var text by remember { mutableStateOf("") }
    var subTasks by remember { mutableStateOf(listOf(SubTask("Enter subtask", false))) }
    var isSheetOpen by remember { mutableStateOf(false) }
    var capturedImageUri by remember { mutableStateOf<Uri?>(null) }
    var capturedImageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }

    val location = mainViewModel.location
    var latitude by remember { mutableDoubleStateOf(location.latitude) }
    var longitude by remember { mutableDoubleStateOf(location.longitude) }
    var address by remember { mutableStateOf("") }
    val resultLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if(result.resultCode == Activity.RESULT_OK){
            var latitude1 = result.data?.getDoubleExtra("latitude", 0.0)
            var longitude1 = result.data?.getDoubleExtra("longitude", 0.0)
            if (latitude1 != null) {
                latitude = latitude1
            }
            if (longitude1 != null) {
                longitude = longitude1
            }
//            val geoCoder = Geocoder(context)
//            address = geoCoder.getFromLocation(latitude, longitude, 1)?.get(0)?.getAddressLine(0).toString()
        }
    }

    if (selectedTemplate.isNotBlank()) {
        val templateIndex = templates.indexOf(selectedTemplate)
        if (templateIndex != -1 && templateIndex < templateDesc.size) {
            text = templateDesc[templateIndex]
            title = templates[templateIndex]
        }
    }

    BaseScaffold(
        showTopBar = true,
        showNavigationIcon = true,
        showFirstIcon = false,
        showSecondIcon = false,
        title = stringResource(R.string.todo_new_task),
        hostState = snackbarHostState,
        bottomBar = {
            BottomActionBar(modifier = Modifier.height(70.dp),
                title = "Save",
                onCamera = {
                    isSheetOpen = true
                },
                onLocation = {
//                    val intent = Intent(context, GeoLocationActivity::class.java)
//                    GeneralUtil.startActivity2(context, intent)
                    val intent = Intent(context, MapsActivity::class.java)
                    intent.putExtra("startActivity", 1)
                    resultLauncher.launch(intent)
                },
                onCalender = {
                    scope.launch {
                        showCalender = !showCalender
                    }
                },
                onReminder = {
                    scope.launch {
                        showTimePicker = !showTimePicker
                    }
                },
                addClick = {
                    if(title.isEmpty()){
                        scope.launch{
                            snackbarHostState.showSnackbar("Please fill in a title!")
                        }
                    }else{
                        val userId = SharedPreferenceManger(context).userId
                        mainViewModel.addTask(
                            title, userId, text, prior, longitude, latitude,
                            capturedImageUri.toString(), Date.valueOf(LocalDate.now().toString()),
                            Date.valueOf(LocalDate.now().toString()), date,
                            0, TaskStateEnum.UNFINISHED.level, subTasks, null
                        )
                        GeneralUtil.finishActivity2(context)
                    }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .verticalScroll(enabled = true, state = scrollState),
            verticalArrangement = Arrangement.Top
        ) {
            Chips("", "Templates: ", templates) {
                selectedTemplate = it
            }

            OutlinedTextField(modifier = Modifier
                .fillMaxWidth()
                .heightIn(80.dp)
                .background(MaterialTheme.colorScheme.background)
                .padding(10.dp),
                value = title,
                singleLine = true,
                label = { Text(text = "Title") },
                onValueChange = { title = it }
            )

            OutlinedTextField(modifier = Modifier
                .fillMaxWidth()
                .heightIn(135.dp)
                .background(MaterialTheme.colorScheme.background)
                .padding(10.dp), value = text,
                maxLines = 4,
                minLines = 1,
                label = { Text(text = "Description") },
                onValueChange = { text = it }
            )

            if(capturedImageBitmap != null){
                Row(
                    modifier = Modifier.fillMaxWidth().padding(10.dp),
                    horizontalArrangement = Arrangement.Center
                ){
                    Image(bitmap = capturedImageBitmap!!, contentDescription = "Captured Image")
                }
            }

            Chips(priorityLevels[1], "Priority: ", priorityLevels) {
                prior = priorityLevels.indexOf(it) + 1
            }

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 3.dp),
                text = "Location"
//                text = "Location " + "Latitude: " + latitude + "  Longitude: " + longitude + "  Address:" + address
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                text = "Latitude: " + latitude
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                text = "Longitude: " + longitude
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                text = "Due Date: $date at ${dateConverter.formatHourMinute(date)}",
            )

            CheckboxListTextFieldExample(subTasks){
                subTasks = it
            }

            if (showCalender) {
                DatePicker(
                    onDateSelected = { date = dateConverter.updateDateWithTime(it, state) },
                    onDismiss = { showCalender = false }
                )
            }

            if(showTimePicker){
                TimePickerDialog(
                    onCancel = { showTimePicker = false },
                    onConfirm = {
                        date = dateConverter.updateDateWithTime(date, state)
                        showTimePicker = false
                    }
                ) {
                    TimePicker(state = state)
                }
            }

            if (isSheetOpen) {
                ImageBottomSheet(
                    onCapturedImageUri = {
                        capturedImageUri = it
                        capturedImageBitmap = imageUtil.getImageBitmap(contentResolver, capturedImageUri)
                    },
                    onSelect = {isSheetOpen = it}
                )
            }
        }
    }
}

@Preview(name = "Light theme")
@Preview(name = "Dark theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewDetailScreen() {
//    MainScreen(LocalContext.current)
}
