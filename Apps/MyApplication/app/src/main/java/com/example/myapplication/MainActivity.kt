package com.example.myapplication

import android.R.attr.onClick
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.room.Room
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.data.DataCollector
import com.example.myapplication.data.DataExtractor
import com.example.myapplication.data.Sort
import com.example.myapplication.data.TrashEntity
import com.example.myapplication.data.TrashEntityDatabase
import com.example.myapplication.data.TrashViewModel
import com.example.myapplication.data.UserModelDataBase
import com.example.myapplication.data.UserViewModel
import com.example.myapplication.data.WelcomeStore
import com.example.myapplication.ui.theme.BlueDark
import com.example.myapplication.ui.theme.BlueDefault
import com.example.myapplication.ui.theme.BlueLight
import com.example.myapplication.ui.theme.BrownDefault
import com.example.myapplication.ui.theme.GreenDark
import com.example.myapplication.ui.theme.GreenDefault
import com.example.myapplication.ui.theme.GreyDark
import com.example.myapplication.ui.theme.GreyDefault
import com.example.myapplication.ui.theme.OrangeDark
import com.example.myapplication.ui.theme.OrangeDefault
import com.example.myapplication.ui.theme.TestTheme
import com.example.myapplication.ui.theme.Typography
import com.example.myapplication.ui.theme.YellowDark
import com.example.myapplication.ui.theme.YellowDefault
import com.example.myapplication.ui.theme.YellowLight
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.Serializable
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.WeekFields
import kotlin.system.exitProcess

/****************** Resources ***********************/
class Trash {
    var name : String = "Es liegt keine Müllabfuhr an."
    var color : Color = Color(0xFFEEEEEE)
    var iconType : Int = R.drawable.empty_icon_100
    var iconColor : Color = Color(0xFF000000)
}
fun getCurrentData(type: String): Array<Trash> {

    /*** Types of Trash ***/
    if(type == "") return arrayOf(Trash())
    else{
        val arr = ArrayList<Trash>()

        if(type.contains("B")){
            val bio = Trash()
            bio.name = "Biotonne"
            bio.color = OrangeDark
            bio.iconType = R.drawable.bio_icon_100
            bio.iconColor = BrownDefault

            arr.add(bio)
        }
        if(type.contains("G")){
            val yellow = Trash()
            yellow.name = "Gelbe Tonne"
            yellow.color = YellowDark
            yellow.iconType = R.drawable.yellow_icon_100
            yellow.iconColor = OrangeDefault

            arr.add(yellow)
        }
        if(type.contains("P")){
            val blue = Trash()
            blue.name = "Papiertonne"
            blue.color = BlueDefault
            blue.iconType = R.drawable.blue_icon_100
            blue.iconColor = BlueDark

            arr.add(blue)
        }
        if(type.contains("R")){
            val grey = Trash()
            grey.name = "Restmülltonne"
            grey.color = GreyDefault
            grey.iconType = R.drawable.grey_icon_100
            grey.iconColor = GreyDark

            arr.add(grey)
        }
        return arr.toTypedArray()
    }
}
data class NTuple5<T1, T2, T3, T4, T5>(val t1: T1, val t2: T2, val t3: T3, val t4: T4, val t5: T5) : Serializable {
    override fun toString(): String = "($t1, $t2, $t3, $t4, $t5)"
}
//Loading Window
@Composable
fun CircularProgressWindow(modifier: Modifier = Modifier){
    val black = Brush.linearGradient(
        listOf(Color.Black, Color.Black),
    )
    Column (
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxSize()
            .background(black, alpha = .3f),
    ){
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .padding(10.dp)
                .fillMaxWidth()
                .height(200.dp)
                .shadow(6.dp)
                .background(Color.White, RoundedCornerShape(5)),
        ) {
            CircularProgressIndicator(
                modifier = modifier
                    .width(50.dp),
                color = GreenDark,
                trackColor = GreenDefault
            )
        }
    }
}
fun getJavaScript() : String{
    return """
        //change text-part
        document.querySelector('h2').innerHTML = "Deine Entsorgungstermine";
        
        //change text-part
        document.querySelector('.content b').innerHTML = "Bitte wähle deine Straße aus:";
        
        //hide input field 
        document.querySelector('.content DIV:nth-child(5) div').style.display = 'none';                                
        document.getElementById('strassetext').style.display = 'none';                                
        
        //save street
        document.getElementById('strasse').addEventListener('change', event => {
           Android.getStreetValue(event.target.value);
        });
        
        const callback1 = (mutationList) => {
            for (const mutation of mutationList) {
                if (mutation.type === "childList") {
                    let list = mutation.addedNodes;
                    for(const elem of list){
                        //change text-part
                        if(elem.nodeName == 'B'){
                            if(elem.innerHTML.includes('Bitte wählen')){
                                elem.innerHTML = 'Bitte wähle deine Hausnummer aus:';
                            }
                        }
                        //change text-part
                        if(elem.nodeName =='#text'){
                            elem.textContent = '';
                        }
                        //save house number
                        if(elem.nodeName == 'BUTTON'){
                            elem.addEventListener('click', event => {
                               Android.getNumValue(event.target.textContent);
                            });
                        }
                        if(elem.nodeName == 'DIV'){
                            //hide button
                            if(elem.className == 'row'){
                                elem.children[1].style.display = 'none';
                            }
                            //change text-part
                            if(elem.id == 'outA4'){
                                let elemChildren = elem.childNodes
                                for(const child of elemChildren){
                                    if(child.nodeName == '#text'){
                                        if(child.textContent.includes('Bitte wählen')){
                                            child.textContent = 'Bitte wähle deine Behältergröße aus:';
                                        }
                                        if(child.textContent.includes('Bitte prüfen')){
                                            child.textContent = 'Bitte prüfe, ob dein Stadtteil und deine Behältergröße korrekt ausgewählt wurden.';
                                        }
                                    }
                                }    
                                observer1.observe(elem, {childList: true});                                                   
                            }
                        }
                    }                                            
                }
            }
        };
        const observer1 = new MutationObserver(callback1);
        observer1.observe(document.getElementById('out'), {childList: true});
        observer1.observe(document.getElementById('out2'), {childList: true});
        
        //change text-part
        document.getElementById('out').innerHTML = "";
        
        const callback2 = (mutationList) => {
            for (const mutation of mutationList) {
                if (mutation.type === "childList") {
                    let list = mutation.addedNodes;
                    
                    // current year
                    const year = new Date().getFullYear()
                    for (const elem of list){
                        if(elem.nodeName == 'BUTTON'){
                            // hide button to prev year (if exists)
                            if (elem.textContent.includes(year-1)){
                                elem.style.display = 'none';
                            }
                            else {
                                // hide unnecessary button
                                if (elem.textContent.includes('Symbole')){
                                    elem.style.display = 'none';
                                }
                                // change appearance of button
                                else {
                                    elem.style.background = '#79AB32';
                                    let version = elem.textContent;
                                    elem.textContent = 'Weiter';
                                    Android.getPDFVersion(version);
                                    
                                    elem.addEventListener('click', () => {
                                       Android.onBtnClick(true);
                                    });
                                }
                            }
                        }
                    }
                }
            }
        };
        const observer2 = new MutationObserver(callback2);
        observer2.observe(document.getElementById('kalenderlink_unten'), {childList: true});
       
    """.trimIndent()
}
/****************** Resources ***********************/

/************ Main Activity / Home Page *************/
class MainActivity : ComponentActivity() {

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

    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            //check if permission to use external storage was granted
            if(Environment.isExternalStorageManager()) {
                TestTheme {
                    val linear = Brush.linearGradient(
                        listOf(YellowDefault, BlueLight, GreenDefault),
                        start = Offset(600.0f, -20.0f),
                        end = Offset(200.0f, 1600.0f)
                    )
                    val context = LocalContext.current
                    val welcome = WelcomeStore(context)
                    // when app is opened the first time -> user gets shown welcome screen
                    if (!welcome.getWelcome.collectAsState(initial = false).value) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(linear, alpha = 0.7f),
                            contentAlignment = Alignment.TopCenter,
                        ) {
                            WelcomeScreen(uVM = userViewModel, tVM = trashViewModel)
                        }
                    }
                    // otherwise user gets shown app content
                    else {
                        val radioOptions = listOf("Tag", "Woche", "Monat")
                        val (selectedOption, onOptionSelected) = remember {
                            mutableStateOf(
                                radioOptions[0]
                            )
                        }
                        Scaffold(
                            topBar = { Header("Der Müllkalendar") },
                            bottomBar = { Footer(idx = 0) }
                        ) { innerPadding ->
                            Column(
                                modifier = Modifier
                                    .padding(innerPadding)
                                    .fillMaxSize()
                                    .background(linear, alpha = 0.7f),
                            ) {
                                // view
                                CalendarView(
                                    tVM = trashViewModel,
                                    uVM = userViewModel,
                                    selected = selectedOption
                                )

                                // menu for different views
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    for (text in radioOptions) {
                                        Column(
                                            Modifier
                                                .selectable(
                                                    selected = (text == selectedOption),
                                                    onClick = {
                                                        onOptionSelected(text)
                                                    }
                                                )
                                                .padding(horizontal = 16.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            RadioButton(
                                                selected = (text == selectedOption),
                                                onClick = { onOptionSelected(text) },
                                                colors = RadioButtonColors(
                                                    selectedColor = GreenDark,
                                                    unselectedColor = Color.Gray,
                                                    disabledSelectedColor = Color.LightGray,
                                                    disabledUnselectedColor = Color.LightGray
                                                )
                                            )
                                            Text(
                                                text = text,
                                                style = Typography.bodyLarge,
                                                color = Color.Black,
                                                modifier = Modifier.padding(start = 16.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            else{
                //ask for permission to use external storage
                val linear = Brush.linearGradient(
                    listOf(YellowDefault, BlueLight, GreenDefault),
                    start = Offset(600.0f, -20.0f),
                    end = Offset(200.0f, 1600.0f)
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(linear, alpha = 0.7f),
                        contentAlignment = Alignment.Center,
                ){
                    Column(
                        modifier = Modifier
                            .padding(20.dp)
                    ){
                        Text(
                            text = "Achtung!",
                            style = Typography.bodySmall,
                            fontSize = 18.sp,
                            color = Color.Black
                        )
                        Text(
                            text = "Die App benötigt die Berechtigung auf alle Dateien zugreifen zu können. " +
                                    "Während der Erstellung der Termindaten muss eine PDF-Datei heruntergeladen werden. Diese wird im " +
                                    "Anschluss wieder gelöscht.\n" +
                                    "Bitte erteile der App die nötige Berechtigung.",
                            style = Typography.bodySmall,
                            fontSize = 16.sp,
                            color = Color.Black
                        )
                        Row(
                            modifier = Modifier.padding(top = 20.dp),
                            horizontalArrangement = Arrangement.spacedBy(
                                130.dp,
                                Alignment.CenterHorizontally)
                        ){
                            Button(
                                onClick = {
                                    startActivity(Intent(ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION))
                                },
                                shape = RoundedCornerShape(10),
                                colors = ButtonColors(
                                    containerColor = GreenDark,
                                    contentColor = Color.White,
                                    disabledContainerColor = Color.DarkGray,
                                    disabledContentColor = Color.White
                                )
                            ){
                                Text(
                                    text = "OK",
                                    style = Typography.bodyLarge,
                                    fontSize = 16.sp
                                )
                            }
                            Button(
                                onClick = {
                                    MainActivity().finish()
                                    exitProcess(0)
                                },
                                shape = RoundedCornerShape(10),
                                colors = ButtonColors(
                                    containerColor = Color.Gray,
                                    contentColor = Color.White,
                                    disabledContainerColor = Color.DarkGray,
                                    disabledContentColor = Color.White
                                )
                            ){
                                Text(
                                    text = "NEIN",
                                    style = Typography.bodyLarge,
                                    fontSize = 16.sp
                                )
                            }
                        }
                    }
                }

            }
        }
    }
}
/************ Main Activity / Home Page *************/

/****************** Welcome *************************/
@Composable
fun WelcomeScreen(modifier: Modifier = Modifier, uVM: UserViewModel, tVM: TrashViewModel){
    var welcomeState by remember { mutableStateOf(false) }
    var currentPage by remember { mutableStateOf(1) }

    var btn by remember { mutableStateOf(false) }

    var uStr by remember { mutableStateOf("") }
    var uNr by remember { mutableStateOf(0) }
    var fileVersion by remember { mutableStateOf("") }
    var pdfLnk by remember { mutableStateOf("") }
    var uPrefArray = arrayOf(false, false, false, false)

    var showPressWindow by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val btnText = "Abschließen"

    Surface(
        modifier = modifier
            .padding(10.dp)
            .fillMaxSize(),
        color = Color.Transparent
    ) {
        //first page
        if (!welcomeState) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = modifier
                    .fillMaxSize()
                    .clickable(onClick = {
                        welcomeState = true
                    })
            ) {
                Text(
                    text = "WILLKOMMEN!",
                    fontSize = 40.sp,
                    modifier = modifier
                        .padding(bottom = 20.dp)
                )
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.welcome_icon_64),
                    contentDescription = "Welcome",
                    tint = Color.Unspecified,
                    modifier = modifier
                        .size(200.dp)
                )
                Text(
                    text = "Der Müllkalendar!",
                    fontSize = 34.sp,
                    modifier = modifier
                        .padding(top = 20.dp)
                )
            }
        }
        //other pages
        else {
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = modifier
                    .padding(10.dp)
            ) {
                when (currentPage) {
                    1 -> {
                        val (t1, t2, t3, t4, t5) = welcomePage1()
                        uStr = t1
                        uNr = t2
                        fileVersion = t3
                        pdfLnk = t4
                        btn = t5
                    }
                    2 -> {
                        val (t1, t2) = welcomePage2()
                        uPrefArray = t1
                        btn = t2
                    }
                }

                Text(
                    text = "Hinweis:",
                    style = Typography.bodySmall,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Text(
                    text = "Es wird eine PDF Datei heruntergeladen. Diese wird nach Erhalten der Daten wieder gelöscht.",
                    style = Typography.bodySmall,
                    color = Color.Gray
                )

                if(btn && currentPage == 2) {
                    Button(
                        modifier = modifier
                            .padding(top = 20.dp)
                            .fillMaxWidth(),
                        enabled = btn,
                        shape = RoundedCornerShape(20),
                        colors = ButtonColors(
                            containerColor = GreenDark,
                            contentColor = Color.White,
                            disabledContainerColor = Color.Gray,
                            disabledContentColor = Color.LightGray
                        ),
                        onClick = {
                            scope.launch {
                                uVM.addUser(1, "Magdeburg", uStr, uNr, uPrefArray)

                                showPressWindow = true

                                DataExtractor().extractPDFData(
                                    tVM,
                                    str = uStr,
                                    nr = uNr,
                                    version = fileVersion
                                )

                                delay(100)

                                val welcome = WelcomeStore(context)
                                welcome.saveWelcome(true)
                            }
                        }
                    ) {
                        Text(
                            text = btnText,
                            style = Typography.bodyLarge,
                            fontSize = 18.sp
                        )
                    }
                }
                else if(btn && currentPage == 1){
                    btn = false
                    currentPage += 1
                }
            }
            if(showPressWindow){
                CircularProgressWindow()
            }
        }
    }
}

@SuppressLint("SetJavaScriptEnabled", "JavascriptInterface")
@Composable
fun welcomePage1(modifier: Modifier = Modifier): NTuple5<String, Int,
        String, String, Boolean> {

    var uStreet by remember { mutableStateOf("") }
    var uNum by remember { mutableStateOf("") }
    var version by remember { mutableStateOf("") }
    var pdf by remember { mutableStateOf("") }
    var getDataDone by remember { mutableStateOf(false) }
    var getUserDataDone by remember { mutableStateOf(false) }

    Text(
        text = "Abfuhrtermine",
        style = Typography.titleLarge,
        modifier = modifier
            .padding(top = 20.dp, bottom = 10.dp)
    )
    AndroidView(
        modifier = modifier
            .height(620.dp)
            .clip(RoundedCornerShape(5))
            .border(1.dp, GreenDark, RoundedCornerShape(5)),
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true

                settings.loadWithOverviewMode = true
                settings.useWideViewPort = true
                settings.setSupportZoom(true)

                addJavascriptInterface(object: Any() {
                    @JavascriptInterface
                    fun getNumValue(buttonNum: String) {
                        uNum = buttonNum
                    }
                    @JavascriptInterface
                    fun getStreetValue(selectedItem: String) {
                        uStreet = selectedItem
                    }
                    @JavascriptInterface
                    fun getPDFVersion(fileVersion: String) {
                        version = fileVersion.filter { it.isDigit() }
                    }

                    @JavascriptInterface
                    fun onBtnClick(getUDDone: Boolean) {
                        getUserDataDone = getUDDone
                    }
                }, "Android")

                webViewClient = object : WebViewClient() {
                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        evaluateJavascript(getJavaScript(), null)
                    }

                    override fun onLoadResource(view: WebView?, url: String?) {
                        super.onLoadResource(view, url)
                        if(getUserDataDone){
                            pdf = url.toString()
                            getDataDone = true
                        }
                    }
                }
            }
        },
        update = { webView ->
            webView.loadUrl("https://sab.ssl.metageneric.de/app/sab_i_tp/index.php")
        }
    )

    return if(getDataDone){
        val context = LocalContext.current

        DataCollector().getPDFData(
            str = uStreet,
            nr = uNum.toInt(),
            version = version,
            con = context,
            pdfLnk = pdf
        )

        NTuple5(t1 = uStreet, t2 = uNum.toInt(), t3 = version, t4 = pdf, t5 = true)
    }
    else NTuple5(t1 = "", t2 = 0, t3 = "", t4 = "", t5 = false)
}
@Composable
fun welcomePage2(modifier: Modifier = Modifier): Pair<Array<Boolean>, Boolean> {
    var switch_b by remember { mutableStateOf(false) }
    var switch_g by remember { mutableStateOf(false) }
    var switch_p by remember { mutableStateOf(false) }
    var switch_r by remember { mutableStateOf(false) }

    Text(
        text = "Müllsorten*",
        style = Typography.titleLarge,
        modifier = modifier
            .padding(top = 20.dp)
    )
    Text(
        text = "Für welche Mülltonnen und ihre Abfuhrzeiten willst du informiert werden?",
        style = Typography.bodySmall,
        color = Color.Gray
    )
    Column (modifier = modifier
        .wrapContentSize(Alignment.Center)
        .padding(top = 30.dp, bottom = 50.dp)
    )
    {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxWidth()
                .padding(start = 30.dp, bottom = 20.dp)
        ) {
            Switch(
                checked = switch_b,
                onCheckedChange = {
                    switch_b = it
                },
                modifier = modifier
                    .scale(1f)
                    .size(45.dp)
                    .padding(end = 30.dp),
                colors = SwitchDefaults.colors(
                    uncheckedThumbColor = Color.Black,
                    uncheckedTrackColor = Color.Transparent,
                    uncheckedBorderColor = Color.Black,

                    checkedThumbColor = Color.White,
                    checkedTrackColor = Color.Black,
                    checkedBorderColor = Color.Black,
                )
            )
            Text(
                text = "Biotonne",
                modifier = modifier
                    .padding(start = 30.dp),
                color = Color.Black,
                style = Typography.bodyLarge,
                fontSize = 20.sp
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxWidth()
                .padding(start = 30.dp, bottom = 20.dp)
        ) {
            Switch(
                checked = switch_g,
                onCheckedChange = {
                    switch_g = it
                },
                modifier = modifier
                    .scale(1f)
                    .size(45.dp)
                    .padding(end = 30.dp),
                colors = SwitchDefaults.colors(
                    uncheckedThumbColor = Color.Black,
                    uncheckedTrackColor = Color.Transparent,
                    uncheckedBorderColor = Color.Black,

                    checkedThumbColor = Color.White,
                    checkedTrackColor = Color.Black,
                    checkedBorderColor = Color.Black,
                )
            )
            Text(
                text = "Gelbe Tonne",
                modifier = modifier
                    .padding(start = 30.dp),
                color = Color.Black,
                style = Typography.bodyLarge,
                fontSize = 20.sp
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxWidth()
                .padding(start = 30.dp, bottom = 20.dp)
        ) {
            Switch(
                checked = switch_p,
                onCheckedChange = {
                    switch_p = it
                },
                modifier = modifier
                    .scale(1f)
                    .size(45.dp)
                    .padding(end = 30.dp),
                colors = SwitchDefaults.colors(
                    uncheckedThumbColor = Color.Black,
                    uncheckedTrackColor = Color.Transparent,
                    uncheckedBorderColor = Color.Black,

                    checkedThumbColor = Color.White,
                    checkedTrackColor = Color.Black,
                    checkedBorderColor = Color.Black,
                )
            )
            Text(
                text = "Papiertonne",
                modifier = modifier
                    .padding(start = 30.dp),
                color = Color.Black,
                style = Typography.bodyLarge,
                fontSize = 20.sp
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxWidth()
                .padding(start = 30.dp, bottom = 20.dp)
        ) {
            Switch(
                checked = switch_r,
                onCheckedChange = {
                    switch_r = it
                },
                modifier = modifier
                    .scale(1f)
                    .size(45.dp)
                    .padding(end = 30.dp),
                colors = SwitchDefaults.colors(
                    uncheckedThumbColor = Color.Black,
                    uncheckedTrackColor = Color.Transparent,
                    uncheckedBorderColor = Color.Black,

                    checkedThumbColor = Color.White,
                    checkedTrackColor = Color.Black,
                    checkedBorderColor = Color.Black,
                )
            )
            Text(
                text = "Restmülltonne",
                modifier = modifier
                    .padding(start = 30.dp),
                color = Color.Black,
                style = Typography.bodyLarge,
                fontSize = 20.sp
            )
        }
    }

    return Pair(arrayOf(switch_b, switch_g, switch_p, switch_r), true)
}
/****************** Welcome *************************/

/****************** Content *************************/
@Composable
fun Header(name: String, modifier: Modifier = Modifier){
    Surface (color = GreenDark){
        Text(
            text = name,
            color = Color.White,
            style = Typography.bodyLarge,
            modifier = modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.CenterHorizontally)
                .padding(start = 24.dp, end = 24.dp, top = 64.dp, bottom = 6.dp)
        )
    }
}

/*_________________ Views _________________________*/
@SuppressLint("CoroutineCreationDuringComposition", "ObsoleteSdkInt")
@Composable
fun CalendarView(modifier: Modifier = Modifier, tVM: TrashViewModel, uVM: UserViewModel, selected: String){
    //fake infinity scroll
    val date = LocalDate.now()
    val trashDataList = tVM.sortBy(Sort.MONTH).collectAsState(initial = emptyList()).value
    val userList = uVM.getAllUsers().collectAsState(initial = emptyList()).value

    //check if data exists
    if(trashDataList.isNotEmpty() && userList.isNotEmpty()) {

        //check if data is up to date
        if(trashDataList[0].year == date.year) {

            //Day View
            if (selected == "Tag") {
                if (trashDataList.isNotEmpty() && userList.isNotEmpty()) {
                    val pagerState = rememberPagerState(
                        initialPage = date.dayOfYear - 1,
                        pageCount = { trashDataList.size })
                    val prefArr =
                        arrayOf(userList[0].B, userList[0].G, userList[0].P, userList[0].R)

                    Surface(
                        modifier = modifier.padding(10.dp, bottom = 0.dp),
                        color = Color.Transparent
                    ) {
                        Text(
                            text = "Abfuhrtermine",
                            color = Color.Black,
                            style = Typography.titleLarge,
                        )
                        Column(modifier = modifier.verticalScroll(rememberScrollState())) {
                            HorizontalPager(state = pagerState) { currentPage ->
                                val trashEntity = trashDataList[currentPage]
                                CalendarPageDay(trashEntity = trashEntity, userPrefs = prefArr)
                            }
                        }
                    }
                }
            }
            //Week View
            else if (selected == "Woche") {
                if (trashDataList.isNotEmpty() && userList.isNotEmpty()) {
                    val currentWeek = date.get(WeekFields.of(DayOfWeek.MONDAY, 7).weekOfYear())
                    val lastWeek = trashDataList[trashDataList.size - 1].week

                    val pagerState =
                        rememberPagerState(
                            initialPage = currentWeek,
                            pageCount = { lastWeek + 1 })
                    val prefArr =
                        arrayOf(userList[0].B, userList[0].G, userList[0].P, userList[0].R)

                    Surface(
                        modifier = modifier.padding(10.dp, bottom = 0.dp),
                        color = Color.Transparent
                    ) {
                        Text(
                            text = "Abfuhrtermine",
                            color = Color.Black,
                            style = Typography.titleLarge,
                        )
                        Column(modifier = modifier.verticalScroll(rememberScrollState())) {
                            HorizontalPager(state = pagerState) { currentPage ->
                                val trashEntities = trashDataList.filter { elem -> elem.week == currentPage }
                                CalendarPageWeek(
                                    trashEntities = trashEntities,
                                    userPrefs = prefArr
                                )
                            }
                        }
                    }
                }
            }
            //Month View
            else if (selected == "Monat") {
                if (trashDataList.isNotEmpty() && userList.isNotEmpty()) {
                    val currentMonth = date.month.value - 1
                    val lastMonth = trashDataList[trashDataList.size - 1].month

                    val pagerState =
                        rememberPagerState(
                            initialPage = currentMonth,
                            pageCount = { lastMonth + 1 })
                    val prefArr =
                        arrayOf(userList[0].B, userList[0].G, userList[0].P, userList[0].R)

                    Surface(
                        modifier = modifier.padding(10.dp, bottom = 0.dp),
                        color = Color.Transparent
                    ) {
                        Text(
                            text = "Abfuhrtermine",
                            color = Color.Black,
                            style = Typography.titleLarge,
                        )
                        Column(modifier = modifier.verticalScroll(rememberScrollState())) {
                            HorizontalPager(state = pagerState) { currentPage ->
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    val trashEntities =
                                        trashDataList.filter { elem -> elem.month == currentPage }
                                    CalendarPageMonth(
                                        trashEntities = trashEntities,
                                        userPrefs = prefArr
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
        else{
            var showDialog = true
            DialogWindow(dialog = showDialog, onDismissRequest = {showDialog = false},
                tVM = tVM, uVM = uVM,
                text = "Die Müllabfuhrtermindaten scheinen nicht länger aktuell zu sein. Sollen diese jetzt aktualisiert werden?")
        }
    }
}
@SuppressLint("SimpleDateFormat")
@Composable
fun CalendarPageDay(modifier: Modifier = Modifier, trashEntity: TrashEntity, userPrefs: Array<Boolean>) {
    val pageDate = LocalDate.of(trashEntity.year, trashEntity.month+1, trashEntity.day)
    var type = trashEntity.type

    val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    val dayFormatter = DateTimeFormatter.ofPattern("EEEE")
    val monthFormatter = DateTimeFormatter.ofPattern("MMMM")

    val month = monthFormatter.format(pageDate)
    val day = dayFormatter.format(pageDate)
    val date = dateFormatter.format(pageDate)

    if(type.contains("B") && !userPrefs[0]) type = type.replace("B", "")
    if(type.contains("G") && !userPrefs[1]) type = type.replace("G", "")
    if(type.contains("P") && !userPrefs[2]) type = type.replace("P", "")
    if(type.contains("R") && !userPrefs[3]) type = type.replace("R", "")

    val data = getCurrentData(type)

    Column(
        modifier = modifier
            .padding(start = 10.dp, end = 18.dp, top = 70.dp, bottom = 0.dp)
    ) {
        Text(
            text = month,
            style = Typography.bodyLarge,
            color = Color.Black,
            fontSize = 20.sp
        )
        Column(
            modifier = modifier
                .padding(bottom = 15.dp)
        ) {
            Text(
                text = day,
                style = Typography.bodyLarge,
                color = Color.Black,
                modifier = modifier
                    .padding(10.dp)
                    .align(Alignment.CenterHorizontally)
            )
            if (data.size > 1) {
                var count = 0
                val height = 310 / data.size
                val size = if (data.size == 2) 80 else 60
                val pad = if (data.size == 2) 35 else 65

                for (i in data) {
                    Surface(
                        color = data[count].color,
                        shadowElevation = 6.dp,
                        shape = RoundedCornerShape(5),
                        modifier = modifier
                            .padding(bottom = 5.dp),
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = modifier
                                .wrapContentWidth(Alignment.CenterHorizontally)
                                .height(height.dp)
                                .fillMaxWidth()
                                .padding(start = pad.dp)
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(data[count].iconType),
                                contentDescription = data[count].name,
                                tint = data[count].iconColor,
                                modifier = modifier
                                    .size(size.dp)
                                    .padding(end = 10.dp)
                            )
                            Text(
                                text = data[count].name,
                                style = Typography.bodyLarge,
                                color = Color.Black,
                                modifier = modifier
                                    .padding(start = 5.dp)
                            )
                        }
                    }
                    count += 1
                }
            } else {
                Surface(
                    color = data[0].color,
                    shadowElevation = 6.dp,
                    shape = RoundedCornerShape(2),
                    modifier = modifier
                        .padding(bottom = 5.dp),
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = modifier
                            .height(315.dp)
                            .fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(data[0].iconType),
                            contentDescription = data[0].name,
                            tint = data[0].iconColor,
                            modifier = modifier
                                .size(100.dp)
                                .padding(end = 10.dp)
                        )
                        Text(
                            text = data[0].name,
                            style = Typography.bodyLarge,
                            color = Color.Black,
                            modifier = modifier
                                .padding(start = 5.dp)
                        )
                    }
                }
            }
            Text(
                text = date,
                style = Typography.bodyLarge,
                color = Color.Black,
                modifier = modifier
                    .padding(10.dp, bottom = 5.dp)
                    .align(Alignment.CenterHorizontally)
            )
        }
    }
}
@Composable
fun CalendarPageWeek(modifier: Modifier = Modifier, trashEntities: List<TrashEntity>,
                     userPrefs: Array<Boolean>) {

    val startDate = LocalDate.of(trashEntities[0].year, trashEntities[0].month+1, trashEntities[0].day)
    val endDate = LocalDate.of(trashEntities[trashEntities.size-1].year,
        trashEntities[trashEntities.size-1].month+1, trashEntities[trashEntities.size-1].day)

    val weekFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    val monthFormatter = DateTimeFormatter.ofPattern("MMMM")

    val month = if(trashEntities[0].month+1 != trashEntities[trashEntities.size-1].month+1)
        "${monthFormatter.format(startDate)}/${monthFormatter.format(endDate)}" else
            monthFormatter.format(startDate)
    val date =
        if(trashEntities.size< 7)
            if(startDate.dayOfWeek == DayOfWeek.MONDAY)
                "${weekFormatter.format(startDate)} - ${weekFormatter.format(startDate.plusDays(7))}"
            else
                "${weekFormatter.format(endDate.minusDays(7))} - ${weekFormatter.format(endDate)}"
        else
            "${weekFormatter.format(startDate)} - ${weekFormatter.format(endDate)}"

    Column(
        modifier = modifier
            .padding(start = 10.dp, end = 18.dp, top = 70.dp, bottom = 5.dp)
    ) {
        Text(
            text = month,
            style = Typography.bodyLarge,
            color = Color.Black,
            fontSize = 20.sp
        )
        Column(
            modifier = modifier
                .padding(bottom = 15.dp)
        ) {
            Row(
                modifier = modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ){
                Text(text = "Mo", color = Color.Black, modifier = modifier.width(30.dp) )
                Text(text = "Di", color = Color.Black, modifier = modifier.width(30.dp) )
                Text(text = "Mi", color = Color.Black, modifier = modifier.width(30.dp) )
                Text(text = "Do", color = Color.Black, modifier = modifier.width(30.dp) )
                Text(text = "Fr", color = Color.Black, modifier = modifier.width(30.dp) )
                Text(text = "Sa", color = Color.Black, modifier = modifier.width(30.dp) )
                Text(text = "So", color = Color.Black, modifier = modifier.width(30.dp) )
            }
            Surface(
                color = Color.White,
                shadowElevation = 6.dp,
                shape = RoundedCornerShape(2),
                modifier = modifier
                    .padding(bottom = 5.dp)
                    .fillMaxWidth()
                    .height(315.dp),
            ){
                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(top = 15.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    if(trashEntities.size < 7){
                        if(startDate.dayOfWeek == DayOfWeek.MONDAY){
                            for (trash in trashEntities) {
                                Column {
                                    var type = trash.type

                                    if (type.contains("B") && !userPrefs[0]) type =
                                        type.replace("B", "")
                                    if (type.contains("G") && !userPrefs[1]) type =
                                        type.replace("G", "")
                                    if (type.contains("P") && !userPrefs[2]) type =
                                        type.replace("P", "")
                                    if (type.contains("R") && !userPrefs[3]) type =
                                        type.replace("R", "")

                                    val data = getCurrentData(type)
                                    for (elem in data) {
                                        Box(
                                            modifier = modifier
                                                .background(elem.color, RoundedCornerShape(5))
                                                .height(90.dp)
                                                .wrapContentHeight(Alignment.CenterVertically),
                                        ) {
                                            Icon(
                                                imageVector = ImageVector.vectorResource(elem.iconType),
                                                contentDescription = elem.name,
                                                tint = elem.iconColor,
                                                modifier = modifier
                                                    .size(40.dp)
                                                    .padding(2.dp)
                                            )
                                        }
                                        Spacer(modifier = modifier.size(5.dp))
                                    }
                                }
                            }
                            for(i in 0 until 7 - trashEntities.size){
                                Column {
                                    Box(
                                        modifier = modifier
                                            .background(Color.White, RoundedCornerShape(5))
                                            .height(90.dp)
                                            .width(40.dp)
                                    )
                                }
                            }
                        }
                        else{
                            for(i in 0 until 7 - trashEntities.size){
                                Column {
                                    Box(
                                        modifier = modifier
                                            .background(Color.White, RoundedCornerShape(5))
                                            .height(90.dp)
                                            .width(40.dp)
                                    )
                                }
                            }
                            for (trash in trashEntities) {
                                Column {
                                    var type = trash.type

                                    if (type.contains("B") && !userPrefs[0]) type =
                                        type.replace("B", "")
                                    if (type.contains("G") && !userPrefs[1]) type =
                                        type.replace("G", "")
                                    if (type.contains("P") && !userPrefs[2]) type =
                                        type.replace("P", "")
                                    if (type.contains("R") && !userPrefs[3]) type =
                                        type.replace("R", "")

                                    val data = getCurrentData(type)
                                    for (elem in data) {
                                        Box(
                                            modifier = modifier
                                                .background(elem.color, RoundedCornerShape(5))
                                                .height(90.dp)
                                                .wrapContentHeight(Alignment.CenterVertically),
                                        ) {
                                            Icon(
                                                imageVector = ImageVector.vectorResource(elem.iconType),
                                                contentDescription = elem.name,
                                                tint = elem.iconColor,
                                                modifier = modifier
                                                    .size(40.dp)
                                                    .padding(2.dp)
                                            )
                                        }
                                        Spacer(modifier = modifier.size(5.dp))
                                    }
                                }
                            }
                        }
                    }
                    else {
                        for (trash in trashEntities) {
                            Column {
                                var type = trash.type

                                if (type.contains("B") && !userPrefs[0]) type =
                                    type.replace("B", "")
                                if (type.contains("G") && !userPrefs[1]) type =
                                    type.replace("G", "")
                                if (type.contains("P") && !userPrefs[2]) type =
                                    type.replace("P", "")
                                if (type.contains("R") && !userPrefs[3]) type =
                                    type.replace("R", "")

                                val data = getCurrentData(type)
                                for (elem in data) {
                                    Box(
                                        modifier = modifier
                                            .background(elem.color, RoundedCornerShape(5))
                                            .height(90.dp)
                                            .wrapContentHeight(Alignment.CenterVertically),
                                    ) {
                                        Icon(
                                            imageVector = ImageVector.vectorResource(elem.iconType),
                                            contentDescription = elem.name,
                                            tint = elem.iconColor,
                                            modifier = modifier
                                                .size(40.dp)
                                                .padding(2.dp)
                                        )
                                    }
                                    Spacer(modifier = modifier.size(5.dp))
                                }
                            }
                        }
                    }
                }
            }
            Text(
                text = date,
                style = Typography.bodyLarge,
                color = Color.Black,
                modifier = modifier
                    .padding(10.dp, bottom = 0.dp)
                    .align(Alignment.CenterHorizontally)
            )
        }
    }
}
@Composable
fun CalendarPageMonth(modifier: Modifier = Modifier, trashEntities: List<TrashEntity>,
                     userPrefs: Array<Boolean>) {

    val startDate = LocalDate.of(trashEntities[0].year, trashEntities[0].month+1, trashEntities[0].day)

    val monthFormatter = DateTimeFormatter.ofPattern("MMMM")
    val yearFormatter = DateTimeFormatter.ofPattern("yyyy")

    val month = "${monthFormatter.format(startDate)} ${yearFormatter.format(startDate)}"

    Column(
        modifier = modifier
            .padding(start = 10.dp, end = 18.dp, top = 50.dp, bottom = 5.dp)
    ) {
        Row(
            modifier = modifier
                .padding(10.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ){
            Text(
                text = "Mo",
                color = Color.Black,
                modifier = modifier
                    .width(30.dp)
            )
            Text(
                text = "Di",
                color = Color.Black,
                modifier = modifier
                    .width(30.dp)
            )
            Text(
                text = "Mi",
                color = Color.Black,
                modifier = modifier
                    .width(30.dp)
            )
            Text(
                text = "Do",
                color = Color.Black,
                modifier = modifier
                    .width(30.dp)
            )
            Text(
                text = "Fr",
                color = Color.Black,
                modifier = modifier
                    .width(30.dp)
            )
            Text(
                text = "Sa",
                color = Color.Black,
                modifier = modifier
                    .width(30.dp)
            )
            Text(
                text = "So",
                color = Color.Black,
                modifier = modifier
                    .width(30.dp)
            )
        }
        Surface(
            color = Color.White,
            shadowElevation = 6.dp,
            shape = RoundedCornerShape(2),
            modifier = modifier
                .padding(bottom = 5.dp)
                .height(390.dp)
                .fillMaxWidth(),
        ){
            Column(
                modifier = modifier
                    .padding(top = 10.dp, bottom = 10.dp),
            ) {
                var count = 0
                while (count < trashEntities.size){
                    Row(
                        modifier = modifier
                            .fillMaxWidth()
                            .height((390 / 6).dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        //first week of month
                        if(LocalDate.of(trashEntities[count].year, trashEntities[count].month+1,
                                trashEntities[count].day) == startDate &&
                                    startDate.dayOfWeek != DayOfWeek.MONDAY){
                            for (j in 1 until startDate.dayOfWeek.value) {
                                Column(
                                    modifier = modifier
                                        .width(30.dp)
                                        .height(65.dp)
                                ) {}
                            }
                            for (j in startDate.dayOfWeek.value-1 until 7) {
                                var type = trashEntities[count].type

                                if (type.contains("B") && !userPrefs[0]) type =
                                    type.replace("B", "")
                                if (type.contains("G") && !userPrefs[1]) type =
                                    type.replace("G", "")
                                if (type.contains("P") && !userPrefs[2]) type =
                                    type.replace("P", "")
                                if (type.contains("R") && !userPrefs[3]) type =
                                    type.replace("R", "")

                                val data = getCurrentData(type)
                                Column(
                                    modifier = modifier
                                        .height(65.dp)
                                        .width(30.dp),
                                ) {
                                    Text(trashEntities[count].day.toString())
                                    for(elem in data){
                                        Box(
                                            modifier = modifier
                                                .background(elem.color, RoundedCornerShape(5))
                                                .height(8.dp)
                                                .width(30.dp)
                                        ) {}
                                        Spacer(modifier = modifier.size(2.dp))
                                    }
                                }
                                count += 1
                            }
                        }
                        else {
                            //rest of weeks of month
                            for (j in 0 until 7) {
                                if(count < trashEntities.size) {
                                    var type = trashEntities[count].type

                                    if (type.contains("B") && !userPrefs[0]) type =
                                        type.replace("B", "")
                                    if (type.contains("G") && !userPrefs[1]) type =
                                        type.replace("G", "")
                                    if (type.contains("P") && !userPrefs[2]) type =
                                        type.replace("P", "")
                                    if (type.contains("R") && !userPrefs[3]) type =
                                        type.replace("R", "")

                                    val data = getCurrentData(type)
                                    Column(
                                        modifier = modifier
                                            .height(65.dp)
                                            .width(30.dp),
                                    ) {
                                        Text(trashEntities[count].day.toString())
                                        for (elem in data) {
                                            Box(
                                                modifier = modifier
                                                    .background(
                                                        elem.color,
                                                        RoundedCornerShape(5)
                                                    )
                                                    .width(30.dp)
                                                    .height(8.dp),
                                            ) {}
                                            Spacer(modifier = modifier.size(2.dp))
                                        }
                                    }
                                }
                                else{
                                    Column(
                                        modifier = modifier
                                            .height(65.dp)
                                            .width(30.dp),
                                    ) {}
                                }
                                count += 1
                            }
                        }
                    }
                }
            }
        }
        Text(
            text = month,
            style = Typography.bodyLarge,
            color = Color.Black,
            modifier = modifier
                .padding(10.dp, bottom = 0.dp)
                .align(Alignment.CenterHorizontally)
        )
    }
}
/*_________________ Views _________________________*/

/*_________________ Dialog ________________________*/
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun DialogWindow(modifier: Modifier = Modifier, onDismissRequest: () -> Unit,
                 dialog: Boolean, text: String, tVM: TrashViewModel, uVM: UserViewModel){

    var showDataDialog by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(dialog) }

    if(showDialog) {
        Dialog(onDismissRequest = { onDismissRequest() }) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(Alignment.CenterVertically)
                    .background(Color.White, RoundedCornerShape(2))
            ) {
                Column(
                    modifier = Modifier
                        .padding(20.dp)
                ) {
                    Text(text)
                    Row(
                        modifier = modifier.padding(top = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(
                            130.dp,
                            Alignment.CenterHorizontally
                        )
                    ) {
                        Button(
                            onClick = {
                                showDataDialog = true
                                showDialog = false
                            },
                            shape = RoundedCornerShape(10),
                            colors = ButtonColors(
                                containerColor = GreenDark,
                                contentColor = Color.White,
                                disabledContainerColor = Color.DarkGray,
                                disabledContentColor = Color.White
                            )
                        )
                        { Text(
                            text = "JA",
                            style = Typography.bodyLarge,
                            fontSize = 14.sp
                        ) }
                        Button(
                            onClick = {onDismissRequest() },
                            shape = RoundedCornerShape(10),
                            colors = ButtonColors(
                                containerColor = Color.Gray,
                                contentColor = Color.White,
                                disabledContainerColor = Color.DarkGray,
                                disabledContentColor = Color.White
                            )
                        ) { Text(
                            text = "NEIN",
                            style = Typography.bodyLarge,
                            fontSize = 14.sp
                        ) }
                    }
                }
            }
        }
    }
    if(showDataDialog)
        DataDialog(tVM = tVM, uVM =  uVM, showDDialog = true)
}
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun DataDialog(modifier: Modifier = Modifier, tVM: TrashViewModel, uVM: UserViewModel, showDDialog: Boolean){
    var showDataDialog by remember { mutableStateOf(showDDialog) }

    if(showDataDialog) {

        var uStreet by remember { mutableStateOf("") }
        var uNum by remember { mutableStateOf("0") }
        var version by remember { mutableStateOf("") }
        var pdf by remember { mutableStateOf("") }
        var getDataDone by remember { mutableStateOf(false) }
        var pdfDownloaded by remember { mutableStateOf(false) }
        var getUserDataDone by remember { mutableStateOf(false) }

        var switch_b by remember { mutableStateOf(false) }
        var switch_g by remember { mutableStateOf(false) }
        var switch_p by remember { mutableStateOf(false) }
        var switch_r by remember { mutableStateOf(false) }

        var showProgressWindow by remember { mutableStateOf(false) }

        val context = LocalContext.current
        val scope = rememberCoroutineScope()

        val userList = uVM.getAllUsers().collectAsState(initial = emptyList()).value
        val trashDataList = tVM.getAllTrashData().collectAsState(initial = emptyList()).value

        if (userList.isNotEmpty() && trashDataList.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(YellowLight)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = "Abfuhrtermine",
                        style = Typography.titleLarge,
                        modifier = modifier
                            .padding(top = 20.dp, bottom = 10.dp)
                    )
                    Text(
                        text = "In welcher Straße wohnst du und welche Behältergröße trifft für deine Wohnung zu?",
                        style = Typography.bodySmall,
                        color = Color.Gray
                    )
                    AndroidView(
                        modifier = modifier
                            .height(650.dp)
                            .clip(RoundedCornerShape(5))
                            .border(1.dp, GreenDark, RoundedCornerShape(5)),
                        factory = { context ->
                            WebView(context).apply {
                                settings.javaScriptEnabled = true

                                settings.loadWithOverviewMode = true
                                settings.useWideViewPort = true
                                settings.setSupportZoom(true)

                                addJavascriptInterface(object : Any() {
                                    @JavascriptInterface
                                    fun getNumValue(buttonNum: String) {
                                        uNum = buttonNum
                                    }

                                    @JavascriptInterface
                                    fun getStreetValue(selectedItem: String) {
                                        uStreet = selectedItem
                                    }

                                    @JavascriptInterface
                                    fun getPDFVersion(fileVersion: String) {
                                        version = fileVersion.filter { it.isDigit() }
                                    }

                                    @JavascriptInterface
                                    fun onBtnClick(getUDDone: Boolean) {
                                        getUserDataDone = getUDDone
                                    }
                                }, "Android")

                                webViewClient = object : WebViewClient() {
                                    override fun onPageFinished(view: WebView?, url: String?) {
                                        super.onPageFinished(view, url)
                                        evaluateJavascript(getJavaScript(), null)
                                    }

                                    override fun onLoadResource(view: WebView?, url: String?) {
                                        super.onLoadResource(view, url)
                                        if (getUserDataDone) {
                                            pdf = url.toString()
                                            getDataDone = true
                                        }
                                    }
                                }
                            }
                        },
                        update = { webView ->
                            webView.loadUrl("https://sab.ssl.metageneric.de/app/sab_i_tp/index.php")
                        }
                    )
                    Text(
                        text = "Hinweis:",
                        style = Typography.bodySmall,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = "Innerhalb der eingebetteten Ansicht muss mit zwei Fingern gescrollt werden.",
                        style = Typography.bodySmall,
                        color = Color.Gray
                    )
                    if (getDataDone) {
                        DataCollector().getPDFData(
                            str = uStreet,
                            nr = uNum.toInt(),
                            version = version,
                            con = context,
                            pdfLnk = pdf
                        )
                        getDataDone = false
                        pdfDownloaded = true
                    }

                    Text(
                        text = "Müllsorten",
                        style = Typography.titleLarge,
                        modifier = modifier
                            .padding(top = 20.dp)
                    )
                    Text(
                        text = "Für welche Mülltonnen und ihre Abfuhrzeiten willst du informiert werden?",
                        style = Typography.bodySmall,
                        color = Color.Gray
                    )
                    Column(
                        modifier = modifier
                            .wrapContentSize(Alignment.Center)
                            .padding(top = 30.dp, bottom = 30.dp)
                    )
                    {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = modifier
                                .fillMaxWidth()
                                .padding(start = 30.dp, bottom = 20.dp)
                        ) {
                            Switch(
                                checked = switch_b,
                                onCheckedChange = {
                                    switch_b = it
                                },
                                modifier = modifier
                                    .scale(.8f)
                                    .size(25.dp)
                                    .padding(end = 30.dp),
                                colors = SwitchDefaults.colors(
                                    uncheckedThumbColor = Color.Black,
                                    uncheckedTrackColor = Color.Transparent,
                                    uncheckedBorderColor = Color.Black,

                                    checkedThumbColor = Color.White,
                                    checkedTrackColor = Color.Black,
                                    checkedBorderColor = Color.Black,
                                )
                            )
                            Text(
                                text = "Biotonne",
                                modifier = modifier
                                    .padding(start = 30.dp),
                                color = Color.Black,
                                style = Typography.bodyLarge,
                                fontSize = 18.sp
                            )
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = modifier
                                .fillMaxWidth()
                                .padding(start = 30.dp, bottom = 20.dp)
                        ) {
                            Switch(
                                checked = switch_g,
                                onCheckedChange = {
                                    switch_g = it
                                },
                                modifier = modifier
                                    .scale(.8f)
                                    .size(25.dp)
                                    .padding(end = 30.dp),
                                colors = SwitchDefaults.colors(
                                    uncheckedThumbColor = Color.Black,
                                    uncheckedTrackColor = Color.Transparent,
                                    uncheckedBorderColor = Color.Black,

                                    checkedThumbColor = Color.White,
                                    checkedTrackColor = Color.Black,
                                    checkedBorderColor = Color.Black,
                                )
                            )
                            Text(
                                text = "Gelbe Tonne",
                                modifier = modifier
                                    .padding(start = 30.dp),
                                color = Color.Black,
                                style = Typography.bodyLarge,
                                fontSize = 18.sp
                            )
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = modifier
                                .fillMaxWidth()
                                .padding(start = 30.dp, bottom = 20.dp)
                        ) {
                            Switch(
                                checked = switch_p,
                                onCheckedChange = {
                                    switch_p = it
                                },
                                modifier = modifier
                                    .scale(.8f)
                                    .size(25.dp)
                                    .padding(end = 30.dp),
                                colors = SwitchDefaults.colors(
                                    uncheckedThumbColor = Color.Black,
                                    uncheckedTrackColor = Color.Transparent,
                                    uncheckedBorderColor = Color.Black,

                                    checkedThumbColor = Color.White,
                                    checkedTrackColor = Color.Black,
                                    checkedBorderColor = Color.Black,
                                )
                            )
                            Text(
                                text = "Papiertonne",
                                modifier = modifier
                                    .padding(start = 30.dp),
                                color = Color.Black,
                                style = Typography.bodyLarge,
                                fontSize = 18.sp
                            )
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = modifier
                                .fillMaxWidth()
                                .padding(start = 30.dp, bottom = 20.dp)
                        ) {
                            Switch(
                                checked = switch_r,
                                onCheckedChange = {
                                    switch_r = it
                                },
                                modifier = modifier
                                    .scale(.8f)
                                    .size(25.dp)
                                    .padding(end = 30.dp),
                                colors = SwitchDefaults.colors(
                                    uncheckedThumbColor = Color.Black,
                                    uncheckedTrackColor = Color.Transparent,
                                    uncheckedBorderColor = Color.Black,

                                    checkedThumbColor = Color.White,
                                    checkedTrackColor = Color.Black,
                                    checkedBorderColor = Color.Black,
                                )
                            )
                            Text(
                                text = "Restmülltonne",
                                modifier = modifier
                                    .padding(start = 30.dp),
                                color = Color.Black,
                                style = Typography.bodyLarge,
                                fontSize = 18.sp
                            )
                        }
                    }
                    Text(
                        text = "Hinweis:",
                        style = Typography.bodySmall,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = "Es wird eine PDF Datei heruntergeladen. Diese wird nach Erhalten der Daten wieder gelöscht.",
                        style = Typography.bodySmall,
                        color = Color.Gray
                    )
                    Button(
                        onClick = {
                            scope.launch {
                                uVM.updateUserData(
                                    userList[0],
                                    "Magdeburg",
                                    uStreet,
                                    uNum.toInt(),
                                    arrayOf(switch_b, switch_g, switch_p, switch_r)
                                )

                                if (pdfDownloaded) {
                                    showProgressWindow = true

                                    DataExtractor().updatePDFData(
                                        trashDataList = trashDataList,
                                        vm = tVM,
                                        str = uStreet,
                                        nr = uNum.toInt(),
                                        version = version
                                    )

                                    delay(100)
                                }

                                showDataDialog = false
                            }
                        },
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp),
                        shape = RoundedCornerShape(10),
                        colors = ButtonColors(
                            containerColor = GreenDark,
                            contentColor = Color.White,
                            disabledContainerColor = Color.DarkGray,
                            disabledContentColor = Color.White
                        )
                    ) {
                        Text(
                            style = Typography.bodyLarge,
                            text = "Speichern"
                        )
                    }
                }
            }
            if(showProgressWindow) CircularProgressWindow()
        }
    }
}
/*_________________ Dialog ________________________*/

@Composable
fun Footer(modifier: Modifier = Modifier, idx : Int){
    val context = LocalContext.current

    var selectedItem by remember { mutableIntStateOf(idx) }
    val pages = listOf(MainActivity::class.java, Settings::class.java)
    val items = listOf("Home", "Einstellungen")
    val selectedIcons = listOf(ImageVector.vectorResource(R.drawable.home_icon_24), ImageVector.vectorResource(R.drawable.settings_icon_24))
    val unselectedIcons =
        listOf(ImageVector.vectorResource(R.drawable.home_icon_24), ImageVector.vectorResource(R.drawable.settings_icon_24))

    Surface (
        modifier = modifier
            .fillMaxWidth()
    ) {
        NavigationBar (
            containerColor = GreenDark,
        ) {
            items.forEachIndexed { index, item ->
                NavigationBarItem(
                    icon = {
                        Icon(
                            if (selectedItem == index) selectedIcons[index] else unselectedIcons[index] ,
                            contentDescription = item,
                            modifier = modifier.size(35.dp)
                        )
                    },
                    modifier = modifier
                        .padding(0.dp)
                        .wrapContentHeight(Alignment.Bottom),
                    label = { Text(
                        text = item,
                        color = Color.White,
                        style = Typography.bodySmall) },
                    colors =
                        NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.White,
                            unselectedIconColor = Color.White,
                            selectedTextColor = Color.Transparent,
                            indicatorColor = GreenDefault
                        ),
                    selected = selectedItem == index,
                    onClick = {
                        try {
                            selectedItem = index
                            context.startActivity(Intent(context, pages[index]))
                        }catch (e: Exception){
                            Log.e("MainActivityFooter", "Something went wrong", e)
                            e.printStackTrace()
                        }
                    }
                )
            }
        }
    }
}
/****************** Content *************************/

/****************** Preview *************************/
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    val linear = Brush.linearGradient(
        listOf(YellowDefault, BlueLight, GreenDefault),
        start = Offset(600.0f, -20.0f),
        end = Offset(200.0f, 1600.0f)
    )
    TestTheme {
        Scaffold(
            topBar = { Header("Der Müllkalendar") },
            bottomBar = { Footer(idx = 0) }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .background(linear, alpha = 0.7f),
            ) {
            }
        }
    }
}
/****************** Preview *************************/