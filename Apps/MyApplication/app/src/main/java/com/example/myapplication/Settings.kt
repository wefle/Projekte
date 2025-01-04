package com.example.myapplication

import android.os.Bundle
import android.os.Environment
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.BlueLight
import com.example.myapplication.ui.theme.GreenDark
import com.example.myapplication.ui.theme.GreenDefault
import com.example.myapplication.ui.theme.TestTheme
import com.example.myapplication.ui.theme.Typography
import com.example.myapplication.ui.theme.YellowDefault
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.myapplication.data.TrashEntityDatabase
import com.example.myapplication.data.TrashViewModel
import com.example.myapplication.data.UserModelDataBase
import com.example.myapplication.data.UserViewModel


/********************** Settings ********************/
class Settings : ComponentActivity() {
    private val tDB by lazy {
        Room.databaseBuilder(
            context = applicationContext,
            klass = TrashEntityDatabase::class.java,
            name = "trashdata.db"
        ).build()
    }
    private val uDB by lazy {
        Room.databaseBuilder(
            context = applicationContext,
            klass = UserModelDataBase::class.java,
            name = "userdata.db"
        ).build()
    }
    private val trashViewModel by viewModels<TrashViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return TrashViewModel(tDB.dao) as T
                }
            }
        }
    )
    private val userViewModel by viewModels<UserViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return UserViewModel(uDB.dao) as T
                }
            }
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            if(Environment.isExternalStorageManager()) {
                TestTheme {
                    val linear = Brush.linearGradient(
                        listOf(YellowDefault, BlueLight, GreenDefault),
                        start = Offset(600.0f, -20.0f),
                        end = Offset(200.0f, 1600.0f)
                    )
                    Scaffold(
                        topBar = { Header("Der Müllkalendar") },
                        bottomBar = { Footer(idx = 1) }
                    ) { innerPadding ->
                        Box(
                            modifier = Modifier
                                .padding(innerPadding)
                                .fillMaxSize()
                                .background(linear, alpha = 0.7f),
                            contentAlignment = Alignment.TopCenter,
                        ) {
                            SettingsView(tVM = trashViewModel, uVM = userViewModel)
                        }
                    }
                }
            }
        }
    }
}
/********************** Settings ********************/
/****************** Content *************************/
@Composable
fun SettingsView(modifier: Modifier = Modifier, tVM: TrashViewModel, uVM: UserViewModel) {

    var showEditPage by rememberSaveable { mutableStateOf(false) }

    val userList = uVM.getAllUsers().collectAsState(initial = emptyList()).value

    if(userList.isNotEmpty()) {
        Surface(
            modifier = modifier
                .padding(10.dp)
                .fillMaxSize(),
            color = Color.Transparent
        )
        {
            Column {
                Text(
                    text = "Einstellungen",
                    color = Color.Black,
                    style = Typography.titleLarge,
                )
                Surface(
                    color = Color.White,
                    modifier = modifier
                        .padding(20.dp)
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    shadowElevation = 6.dp,
                    shape = RoundedCornerShape(2),
                ) {
                    Column(
                        modifier = modifier
                            .padding(20.dp),
                    ) {
                        Text(
                            text = "Adresse:",
                            color = Color.Gray,
                            style = Typography.bodyLarge,
                            modifier = modifier.padding(bottom = 10.dp)
                        )
                        Column(
                            modifier = modifier
                                .padding(start = 10.dp, bottom = 10.dp)
                        ) {
                            Text(
                                text = "Ort:",
                                color = Color.Black,
                                style = Typography.bodyLarge,
                            )
                            Text(
                                text = userList[0].place,
                                color = Color.Gray,
                                style = Typography.bodyLarge,
                            )
                            Text(
                                text = "Straße und Hausnummer:",
                                color = Color.Black,
                                style = Typography.bodyLarge,
                                modifier = modifier.padding(top = 5.dp)
                            )
                            Text(
                                text = "${userList[0].str} ${userList[0].nr}",
                                color = Color.Gray,
                                style = Typography.bodyLarge,
                            )
                        }

                        Text(
                            text = "Müllsorten:",
                            color = Color.Gray,
                            style = Typography.bodyLarge,
                            modifier = modifier.padding(bottom = 10.dp)
                        )
                        Column(
                            modifier = modifier
                                .padding(start = 10.dp, bottom = 10.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                val switchIcon = if (userList[0].B) R.drawable.switch_on_32
                                else R.drawable.switch_off_32

                                Icon(
                                    imageVector = ImageVector.vectorResource(switchIcon),
                                    contentDescription = "Switch",
                                    tint = Color.Black,
                                    modifier = modifier
                                        .size(45.dp)
                                        .padding(end = 10.dp)
                                )
                                Text(
                                    text = "Biotonne",
                                    color = Color.Black,
                                    style = Typography.bodyLarge,
                                )
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                val switchIcon = if (userList[0].G) R.drawable.switch_on_32
                                else R.drawable.switch_off_32

                                Icon(
                                    imageVector = ImageVector.vectorResource(switchIcon),
                                    contentDescription = "Switch",
                                    tint = Color.Black,
                                    modifier = modifier
                                        .size(45.dp)
                                        .padding(end = 10.dp)
                                )
                                Text(
                                    text = "Gelbe Tonne",
                                    color = Color.Black,
                                    style = Typography.bodyLarge,
                                )
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                val switchIcon = if (userList[0].P) R.drawable.switch_on_32
                                else R.drawable.switch_off_32

                                Icon(
                                    imageVector = ImageVector.vectorResource(switchIcon),
                                    contentDescription = "Switch",
                                    tint = Color.Black,
                                    modifier = modifier
                                        .size(45.dp)
                                        .padding(end = 10.dp)
                                )
                                Text(
                                    text = "Papiertonne",
                                    color = Color.Black,
                                    style = Typography.bodyLarge,
                                    modifier = modifier.padding(top = 5.dp)
                                )
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                val switchIcon = if (userList[0].R) R.drawable.switch_on_32
                                else R.drawable.switch_off_32

                                Icon(
                                    imageVector = ImageVector.vectorResource(switchIcon),
                                    contentDescription = "Switch",
                                    tint = Color.Black,
                                    modifier = modifier
                                        .size(45.dp)
                                        .padding(end = 10.dp)
                                )
                                Text(
                                    text = "Restmülltonne",
                                    color = Color.Black,
                                    style = Typography.bodyLarge,
                                )
                            }
                        }
                        Button(
                            onClick = {
                                showEditPage = true
                            },
                            modifier = modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10),
                            colors = ButtonColors(
                                containerColor = GreenDark,
                                contentColor = Color.White,
                                disabledContainerColor = Color.DarkGray,
                                disabledContentColor = Color.White
                            )
                        ) {
                            Text(
                                text = "Bearbeiten",
                                style = Typography.bodySmall,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }
        }
        if(showEditPage) {
            DialogWindow(
                dialog = showEditPage, onDismissRequest = { showEditPage = false },
                tVM = tVM, uVM = uVM, text = "Möchtest du deine Daten anpassen?"
            )
        }
    }
}
/****************** Content *************************/

/****************** Preview *************************/
@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    TestTheme {
        TestTheme {
            val linear = Brush.linearGradient(
                listOf(YellowDefault, BlueLight, GreenDefault),
                start = Offset(600.0f, -20.0f),
                end = Offset(200.0f, 1600.0f)
            )
            Scaffold(
                topBar = {Header("Der Müllkalendar")},
                bottomBar = {Footer(idx = 1)}
            ){ innerPadding ->
                Box(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                        .background(linear, alpha = 0.7f),
                    contentAlignment = Alignment.TopCenter,
                ){
                    Text("Hello")
                }
            }
        }
    }
}
/****************** Preview *************************/