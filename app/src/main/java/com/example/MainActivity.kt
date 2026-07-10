package com.example

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.Bookmark
import com.example.ui.MainViewModel
import com.example.ui.MainViewModelFactory
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.NeonGreen
import kotlin.random.Random

val cyberResources = listOf(
    Bookmark(
        id = "https://www.kali.org/",
        title = "Download Kali Linux",
        url = "https://www.kali.org/",
        category = "Linux Resources"
    ),
    Bookmark(
        id = "https://play.google.com/store/apps/details?id=com.termux",
        title = "Download Termux",
        url = "https://play.google.com/store/apps/details?id=com.termux",
        category = "Terminal Tools"
    ),
    Bookmark(
        id = "https://youtube.com/shorts/LWoBD4j62Tw?si=t8isA6maRSm0x5FQ",
        title = "Watch Tutorial",
        url = "https://youtube.com/shorts/LWoBD4j62Tw?si=t8isA6maRSm0x5FQ",
        category = "Video Tutorials"
    )
)

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels {
        MainViewModelFactory((application as CyberApp).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                CyberAppContent(viewModel)
            }
        }
    }
}

@Composable
fun CyberAppContent(viewModel: MainViewModel) {
    var currentScreen by remember { mutableStateOf("home") }
    
    Crossfade(targetState = currentScreen, label = "screen_transition") { screen ->
        when (screen) {
            "home" -> HomeScreen(
                viewModel = viewModel,
                onNavigateToAbout = { currentScreen = "about" }
            )
            "about" -> AboutScreen(
                onNavigateBack = { currentScreen = "home" }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    onNavigateToAbout: () -> Unit
) {
    val savedBookmarks by viewModel.bookmarks.collectAsStateWithLifecycle()
    var searchQuery by remember { mutableStateOf("") }
    
    val filteredResources = cyberResources.filter {
        it.title.contains(searchQuery, ignoreCase = true) || it.category.contains(searchQuery, ignoreCase = true)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = "Logo",
                            tint = NeonGreen,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "ANSH SAHU Cyber",
                            color = NeonGreen,
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onNavigateToAbout) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "About",
                            tint = NeonGreen
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Black,
                    titleContentColor = NeonGreen
                )
            )
        },
        containerColor = Color.Black
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            MatrixBackground()
            
            Column(modifier = Modifier.fillMaxSize()) {
                // Subtitle
                Text(
                    text = "Learn Ethical Hacking & Cybersecurity",
                    color = Color.White,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )

                // Search Bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search resources...", color = Color.Gray) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search", tint = NeonGreen) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeonGreen,
                        unfocusedBorderColor = Color.DarkGray,
                        focusedTextColor = NeonGreen,
                        unfocusedTextColor = Color.White,
                        cursorColor = NeonGreen
                    ),
                    shape = RoundedCornerShape(8.dp),
                    singleLine = true
                )
                
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(filteredResources) { resource ->
                        val isBookmarked = savedBookmarks.any { it.id == resource.id }
                        ResourceCard(
                            resource = resource,
                            isBookmarked = isBookmarked,
                            onBookmarkToggle = { viewModel.toggleBookmark(resource, isBookmarked) }
                        )
                    }
                }
                
                // Footer
                Text(
                    text = "Developed by ANSH SAHU",
                    color = NeonGreen,
                    style = MaterialTheme.typography.labelMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )
            }
        }
    }
}

@Composable
fun ResourceCard(
    resource: Bookmark,
    isBookmarked: Boolean,
    onBookmarkToggle: () -> Unit
) {
    val context = LocalContext.current
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, NeonGreen, RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
            .clickable {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(resource.url))
                context.startActivity(intent)
            },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = resource.category,
                color = NeonGreen,
                style = MaterialTheme.typography.labelSmall
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = resource.title,
                color = Color.White,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = onBookmarkToggle) {
                    Icon(
                        imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = "Bookmark",
                        tint = NeonGreen
                    )
                }
                IconButton(onClick = {
                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, "Check out this resource: ${resource.title}\n${resource.url}")
                    }
                    context.startActivity(Intent.createChooser(shareIntent, "Share via"))
                }) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share",
                        tint = NeonGreen
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(onNavigateBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("About", color = NeonGreen) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = NeonGreen
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Black,
                    titleContentColor = NeonGreen
                )
            )
        },
        containerColor = Color.Black
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Ethical Hacking & Legal Usage",
                color = NeonGreen,
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = "This application is intended solely for cybersecurity education and authorized security testing. Users are responsible for complying with all applicable laws and obtaining permission before testing any systems.",
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium
            )
            Divider(color = NeonGreen)
            Text(
                text = "Careers in Cybersecurity",
                color = NeonGreen,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "Cybersecurity is a rapidly growing field with diverse roles like Penetration Tester, Security Analyst, and Cryptographer. Continuous learning and practical, ethical experience are key to a successful career.",
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "Developed by ANSH SAHU",
                color = NeonGreen,
                style = MaterialTheme.typography.labelMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun MatrixBackground() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        
        // Simple matrix-like subtle background elements
        for (i in 0..50) {
            val x = Random.nextFloat() * w
            val y = Random.nextFloat() * h
            val alpha = Random.nextFloat() * 0.3f
            drawCircle(
                color = NeonGreen.copy(alpha = alpha),
                radius = Random.nextFloat() * 4f,
                center = Offset(x, y)
            )
        }
    }
}
