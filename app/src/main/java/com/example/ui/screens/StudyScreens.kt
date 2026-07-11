package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.R
import com.example.data.StudyMaterial
import com.example.ui.StudyViewModel
import com.example.ui.components.PerplexityLogo
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object Routes {
    const val LOGIN = "login"
    const val DASHBOARD = "dashboard"
    const val UPLOAD = "upload"
    const val DETAILS = "details/{id}"
    fun detailsRoute(id: Int) = "details/$id"
}

@Composable
fun StudyAppNavigation(viewModel: StudyViewModel) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN
    ) {
        // 1. Google Login Screen
        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Routes.DASHBOARD) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        // 2. Home Dashboard Screen
        composable(Routes.DASHBOARD) {
            DashboardScreen(
                viewModel = viewModel,
                onNavigateToDetails = { id ->
                    navController.navigate(Routes.detailsRoute(id))
                },
                onNavigateToUpload = {
                    navController.navigate(Routes.UPLOAD)
                }
            )
        }

        // 3. Simple Upload Screen
        composable(Routes.UPLOAD) {
            UploadScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onUploadSuccess = {
                    navController.popBackStack()
                }
            )
        }

        // 4. PDF Reader / Details Screen
        composable(
            route = Routes.DETAILS,
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: 0
            val materialState = viewModel.allMaterials.collectAsStateWithLifecycle()
            val material = materialState.value.find { it.id == id }

            if (material != null) {
                DetailsScreen(
                    material = material,
                    onBack = { navController.popBackStack() },
                    onToggleFavorite = { viewModel.toggleFavorite(material) }
                )
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Material not found", style = MaterialTheme.typography.titleLarge)
                }
            }
        }
    }
}

// ==========================================
// 1. LOGIN SCREEN (Google Login Only)
// ==========================================
@Composable
fun LoginScreen(onLoginSuccess: () -> Unit) {
    var isLoading by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PremiumBackground)
            .padding(24.dp)
    ) {
        // Ambient glow effect in background
        Box(
            modifier = Modifier
                .size(300.dp)
                .align(Alignment.Center)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(PremiumPrimary.copy(alpha = 0.08f), Color.Transparent)
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(28.dp)
        ) {
            // Animated rotating logo
            PerplexityLogo(
                size = 96.dp,
                petalColor = PremiumPrimary,
                modifier = Modifier.testTag("login_logo")
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "MereSathPadhlo",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    letterSpacing = (-0.5).sp
                )

                Text(
                    text = "Study Together. Grow Together.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextSlateDim,
                    fontWeight = FontWeight.Normal
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (isLoading) {
                CircularProgressIndicator(
                    color = PremiumPrimary,
                    modifier = Modifier.size(32.dp)
                )
            } else {
                // Google Login Button Only
                Card(
                    onClick = {
                        isLoading = true
                        coroutineScope.launch {
                            delay(800) // Realistic authentic sign-in latency
                            onLoginSuccess()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .height(54.dp)
                        .testTag("google_login_button"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = PremiumCard),
                    border = BorderStroke(1.dp, PremiumBorder)
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        GoogleIcon(modifier = Modifier.padding(end = 12.dp))
                        Text(
                            text = "Continue with Google",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Fine printed trust label at the bottom
        Text(
            text = "MereSathPadhlo V1.0 • Verified Academic Network",
            style = MaterialTheme.typography.labelSmall,
            color = TextSlateDim.copy(alpha = 0.5f),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
        )
    }
}

@Composable
fun GoogleIcon(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.size(20.dp)) {
        val sizePx = size.width
        val strokeWidth = sizePx * 0.18f
        val radius = (sizePx - strokeWidth) / 2f
        val center = center

        // Red arc (Top)
        drawArc(
            color = Color(0xFFEA4335),
            startAngle = 180f,
            sweepAngle = 90f,
            useCenter = false,
            style = androidx.compose.ui.graphics.drawscope.Stroke(width = strokeWidth)
        )
        // Yellow arc (Left)
        drawArc(
            color = Color(0xFFFBBC05),
            startAngle = 135f,
            sweepAngle = 45f,
            useCenter = false,
            style = androidx.compose.ui.graphics.drawscope.Stroke(width = strokeWidth)
        )
        // Green arc (Bottom)
        drawArc(
            color = Color(0xFF34A853),
            startAngle = 0f,
            sweepAngle = 135f,
            useCenter = false,
            style = androidx.compose.ui.graphics.drawscope.Stroke(width = strokeWidth)
        )
        // Blue arc (Right)
        drawArc(
            color = Color(0xFF4285F4),
            startAngle = 270f,
            sweepAngle = 90f,
            useCenter = false,
            style = androidx.compose.ui.graphics.drawscope.Stroke(width = strokeWidth)
        )
        // Horizontal bar of G
        val barLength = radius * 0.9f
        drawLine(
            color = Color(0xFF4285F4),
            start = center,
            end = center.copy(x = center.x + barLength),
            strokeWidth = strokeWidth
        )
    }
}

// ==========================================
// 2. DASHBOARD SCREEN (Browse, Search, Filters)
// ==========================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: StudyViewModel,
    onNavigateToDetails: (Int) -> Unit,
    onNavigateToUpload: () -> Unit
) {
    val materials by viewModel.filteredMaterials.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedSubject by viewModel.selectedSubject.collectAsStateWithLifecycle()
    val selectedBranch by viewModel.selectedBranch.collectAsStateWithLifecycle()
    val selectedSemester by viewModel.selectedSemester.collectAsStateWithLifecycle()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToUpload,
                containerColor = PremiumPrimary,
                contentColor = Color.White,
                modifier = Modifier.testTag("add_material_fab"),
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Upload Notes", modifier = Modifier.size(24.dp))
            }
        },
        contentWindowInsets = WindowInsets.safeDrawing
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(PremiumBackground)
        ) {
            // Smaller visual hero banner with rotating logo
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_study_banner_1783776176428),
                    contentDescription = "Study Room Atmosphere Banner",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                // Gradient scrim to maintain dark theme aesthetic
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, PremiumBackground.copy(alpha = 0.95f))
                            )
                        )
                )

                Row(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    PerplexityLogo(
                        size = 32.dp,
                        petalColor = PremiumPrimary
                    )
                    Column {
                        Text(
                            text = "MereSathPadhlo",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Study Together. Grow Together.",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSlateDim
                        )
                    }
                }
            }

            // Search Bar & Filters Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Elegant Outlined Search Bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.setSearchQuery(it) },
                    placeholder = { Text("Search notes, questions, PDFs...", color = TextSlateDim.copy(alpha = 0.7f)) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = TextSlateDim) },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { viewModel.setSearchQuery("") }) {
                                Icon(Icons.Default.Clear, contentDescription = "Clear Search", tint = TextSlateDim)
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("search_bar_input"),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = PremiumCard,
                        unfocusedContainerColor = PremiumCard,
                        focusedBorderColor = PremiumPrimary,
                        unfocusedBorderColor = PremiumBorder,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    singleLine = true
                )

                // 1. BRANCH FILTER ROW
                val branches = listOf("All", "Computer Science", "Mechanical", "Civil", "Chemical", "Electrical")
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Branch", style = MaterialTheme.typography.labelSmall, color = TextSlateDim)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        branches.forEach { branch ->
                            val isSelected = selectedBranch == branch
                            FilterChip(
                                selected = isSelected,
                                onClick = { viewModel.setSelectedBranch(branch) },
                                label = { Text(branch, style = MaterialTheme.typography.labelSmall) },
                                shape = RoundedCornerShape(12.dp),
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = PremiumPrimary.copy(alpha = 0.2f),
                                    selectedLabelColor = PremiumPrimary,
                                    containerColor = PremiumCard,
                                    labelColor = TextSlateDim
                                ),
                                border = BorderStroke(1.dp, if (isSelected) PremiumPrimary else PremiumBorder)
                            )
                        }
                    }
                }

                // 2. SEMESTER FILTER ROW
                val semesters = listOf("All", "Semester 1", "Semester 2", "Semester 3", "Semester 4", "Semester 5", "Semester 6", "Semester 7", "Semester 8")
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Semester", style = MaterialTheme.typography.labelSmall, color = TextSlateDim)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        semesters.forEach { sem ->
                            val isSelected = selectedSemester == sem
                            FilterChip(
                                selected = isSelected,
                                onClick = { viewModel.setSelectedSemester(sem) },
                                label = { Text(sem, style = MaterialTheme.typography.labelSmall) },
                                shape = RoundedCornerShape(12.dp),
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = PremiumPrimary.copy(alpha = 0.2f),
                                    selectedLabelColor = PremiumPrimary,
                                    containerColor = PremiumCard,
                                    labelColor = TextSlateDim
                                ),
                                border = BorderStroke(1.dp, if (isSelected) PremiumPrimary else PremiumBorder)
                            )
                        }
                    }
                }

                // 3. SUBJECT FILTER ROW
                val subjects = listOf("All", "Computer Science", "Physics", "Mathematics", "Chemistry")
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Subject", style = MaterialTheme.typography.labelSmall, color = TextSlateDim)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        subjects.forEach { subject ->
                            val isSelected = selectedSubject == subject
                            FilterChip(
                                selected = isSelected,
                                onClick = { viewModel.setSelectedSubject(subject) },
                                label = { Text(subject, style = MaterialTheme.typography.labelSmall) },
                                shape = RoundedCornerShape(12.dp),
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = PremiumPrimary.copy(alpha = 0.2f),
                                    selectedLabelColor = PremiumPrimary,
                                    containerColor = PremiumCard,
                                    labelColor = TextSlateDim
                                ),
                                border = BorderStroke(1.dp, if (isSelected) PremiumPrimary else PremiumBorder)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Materials Notes list
            if (materials.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.SearchOff,
                            contentDescription = null,
                            modifier = Modifier.size(56.dp),
                            tint = PremiumPrimary.copy(alpha = 0.5f)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "No study material matches your filters",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Try clearing search queries or filters to explore notes.",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSlateDim,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {
                                viewModel.setSearchQuery("")
                                viewModel.setSelectedSubject("All")
                                viewModel.setSelectedBranch("All")
                                viewModel.setSelectedSemester("All")
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = PremiumPrimary),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Clear Filters", color = Color.White)
                        }
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(top = 4.dp, bottom = 80.dp)
                ) {
                    items(materials, key = { it.id }) { material ->
                        StudyMaterialCard(
                            material = material,
                            onTap = { onNavigateToDetails(material.id) }
                        )
                    }
                }
            }
        }
    }
}

// ==========================================
// CARD: DESIGN COMPLIANCE WITH CARD SPECS
// ==========================================
@Composable
fun StudyMaterialCard(
    material: StudyMaterial,
    onTap: () -> Unit
) {
    Card(
        onClick = onTap,
        modifier = Modifier
            .fillMaxWidth()
            .testTag("material_card_${material.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = PremiumCard),
        border = BorderStroke(1.dp, PremiumBorder)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left Column: Custom PDF Thumbnail (Decorative Page-turn styled graphic)
            Box(
                modifier = Modifier
                    .size(width = 65.dp, height = 85.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color(0xFF2C3E50), Color(0xFF0F172A))
                        )
                    )
                    .border(1.dp, PremiumBorder, RoundedCornerShape(8.dp))
            ) {
                // Page lines decoration inside thumbnail
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Box(modifier = Modifier.fillMaxWidth(0.6f).height(4.dp).background(Color.White.copy(alpha = 0.4f)))
                    Box(modifier = Modifier.fillMaxWidth(0.8f).height(2.dp).background(Color.White.copy(alpha = 0.2f)))
                    Box(modifier = Modifier.fillMaxWidth().height(2.dp).background(Color.White.copy(alpha = 0.2f)))
                    Box(modifier = Modifier.fillMaxWidth(0.7f).height(2.dp).background(Color.White.copy(alpha = 0.2f)))
                    Box(modifier = Modifier.fillMaxWidth(0.5f).height(2.dp).background(Color.White.copy(alpha = 0.2f)))
                }

                // Decorative "PDF" banner badge at bottom of thumbnail
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .background(PremiumPrimary)
                        .padding(vertical = 3.dp)
                ) {
                    Text(
                        text = material.type,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 8.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // Right Column: Document Information
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Branch & Semester Line
                Text(
                    text = "${material.branch} • ${material.semester}",
                    style = MaterialTheme.typography.labelSmall,
                    color = PremiumPrimary,
                    fontWeight = FontWeight.SemiBold
                )

                // Title
                Text(
                    text = material.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                // Short Description
                Text(
                    text = material.description.ifEmpty { "Detailed academic notes and previous exam solutions shared on MereSathPadhlo." },
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSlateDim,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 15.sp
                )

                Spacer(modifier = Modifier.height(2.dp))

                // Bottom actions row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = null,
                            modifier = Modifier.size(10.dp),
                            tint = TextSlateDim.copy(alpha = 0.7f)
                        )
                        Text(
                            text = "Shared recently",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSlateDim.copy(alpha = 0.7f),
                            fontSize = 9.sp
                        )
                    }

                    // Direct View Button
                    Button(
                        onClick = onTap,
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 2.dp),
                        modifier = Modifier.height(26.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PremiumPrimary),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text("View", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

// ==========================================
// 3. UPLOAD SCREEN (Beautiful Structured Form)
// ==========================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UploadScreen(
    viewModel: StudyViewModel,
    onBack: () -> Unit,
    onUploadSuccess: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedBranch by remember { mutableStateOf("Computer Science") }
    var selectedSemester by remember { mutableStateOf("Semester 1") }
    var selectedSubject by remember { mutableStateOf("Computer Science") }
    
    // PDF File Attachment Simulation State
    var isPdfAttached by remember { mutableStateOf(false) }
    var attachedFileName by remember { mutableStateOf("") }
    var attachedFileSize by remember { mutableStateOf("") }

    var showSuccessDialog by remember { mutableStateOf(false) }

    val branches = listOf("Computer Science", "Mechanical", "Civil", "Chemical", "Electrical")
    val semesters = listOf("Semester 1", "Semester 2", "Semester 3", "Semester 4", "Semester 5", "Semester 6", "Semester 7", "Semester 8")
    val subjects = listOf("Computer Science", "Physics", "Mathematics", "Chemistry", "General")

    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Upload Study Material", fontWeight = FontWeight.Bold, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PremiumBackground)
            )
        },
        contentWindowInsets = WindowInsets.safeDrawing
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(PremiumBackground)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 1. Title Input
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Document Title (e.g. OS Memory Management)", color = TextSlateDim) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("add_material_title"),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedContainerColor = PremiumCard,
                        unfocusedContainerColor = PremiumCard,
                        focusedBorderColor = PremiumPrimary,
                        unfocusedBorderColor = PremiumBorder
                    )
                )

                // 2. Branch Selection Segment
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("Select Branch", style = MaterialTheme.typography.labelMedium, color = Color.White, fontWeight = FontWeight.Bold)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        branches.forEach { br ->
                            val isSelected = selectedBranch == br
                            FilterChip(
                                selected = isSelected,
                                onClick = { selectedBranch = br },
                                label = { Text(br) },
                                shape = RoundedCornerShape(12.dp),
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = PremiumPrimary.copy(alpha = 0.2f),
                                    selectedLabelColor = PremiumPrimary,
                                    containerColor = PremiumCard,
                                    labelColor = TextSlateDim
                                ),
                                border = BorderStroke(1.dp, if (isSelected) PremiumPrimary else PremiumBorder)
                            )
                        }
                    }
                }

                // 3. Semester Selection Segment
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("Select Semester", style = MaterialTheme.typography.labelMedium, color = Color.White, fontWeight = FontWeight.Bold)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        semesters.forEach { sem ->
                            val isSelected = selectedSemester == sem
                            FilterChip(
                                selected = isSelected,
                                onClick = { selectedSemester = sem },
                                label = { Text(sem) },
                                shape = RoundedCornerShape(12.dp),
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = PremiumPrimary.copy(alpha = 0.2f),
                                    selectedLabelColor = PremiumPrimary,
                                    containerColor = PremiumCard,
                                    labelColor = TextSlateDim
                                ),
                                border = BorderStroke(1.dp, if (isSelected) PremiumPrimary else PremiumBorder)
                            )
                        }
                    }
                }

                // 4. Subject Selection Segment
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("Select Subject Theme", style = MaterialTheme.typography.labelMedium, color = Color.White, fontWeight = FontWeight.Bold)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        subjects.forEach { subj ->
                            val isSelected = selectedSubject == subj
                            FilterChip(
                                selected = isSelected,
                                onClick = { selectedSubject = subj },
                                label = { Text(subj) },
                                shape = RoundedCornerShape(12.dp),
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = PremiumPrimary.copy(alpha = 0.2f),
                                    selectedLabelColor = PremiumPrimary,
                                    containerColor = PremiumCard,
                                    labelColor = TextSlateDim
                                ),
                                border = BorderStroke(1.dp, if (isSelected) PremiumPrimary else PremiumBorder)
                            )
                        }
                    }
                }

                // 5. Description Input
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Brief Description / Syllabus Outline (Markdown supported)", color = TextSlateDim) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(110.dp)
                        .testTag("add_material_content"),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedContainerColor = PremiumCard,
                        unfocusedContainerColor = PremiumCard,
                        focusedBorderColor = PremiumPrimary,
                        unfocusedBorderColor = PremiumBorder
                    )
                )

                // 6. Interactive PDF Upload Box
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("PDF Attachment", style = MaterialTheme.typography.labelMedium, color = Color.White, fontWeight = FontWeight.Bold)
                    
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(95.dp)
                            .border(
                                width = 1.dp,
                                color = if (isPdfAttached) PremiumPrimary else PremiumBorder,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .background(PremiumCard)
                            .clickable {
                                // Simulate PDF File browsing and attachment
                                isPdfAttached = true
                                attachedFileName = "${title.trim().lowercase().replace(" ", "_").ifEmpty { "lecture_document" }}_v1.pdf"
                                attachedFileSize = "${(1.2 + (0..50).random() / 10.0).toString().substring(0, 3)} MB"
                                Toast.makeText(context, "PDF attached successfully!", Toast.LENGTH_SHORT).show()
                            }
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        if (!isPdfAttached) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(Icons.Default.CloudUpload, contentDescription = null, tint = PremiumPrimary, modifier = Modifier.size(24.dp))
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Click to browse PDF file", style = MaterialTheme.typography.bodySmall, color = TextSlateDim)
                                Text("Supports PDF notes up to 50MB", style = MaterialTheme.typography.labelSmall, color = TextSlateDim.copy(alpha = 0.5f))
                            }
                        } else {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(Icons.Default.PictureAsPdf, contentDescription = null, tint = Color.Red, modifier = Modifier.size(28.dp))
                                    Column {
                                        Text(
                                            text = attachedFileName,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Text(
                                            text = "Attached • $attachedFileSize",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = Color(0xFF34A853),
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                }
                                IconButton(
                                    onClick = {
                                        isPdfAttached = false
                                        attachedFileName = ""
                                        attachedFileSize = ""
                                    }
                                ) {
                                    Icon(Icons.Default.Cancel, contentDescription = "Remove file", tint = Color.Red.copy(alpha = 0.8f))
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Submit Button
                Button(
                    onClick = {
                        if (title.isBlank()) {
                            Toast.makeText(context, "Please enter a title", Toast.LENGTH_SHORT).show()
                        } else if (description.isBlank()) {
                            Toast.makeText(context, "Please enter a description", Toast.LENGTH_SHORT).show()
                        } else if (!isPdfAttached) {
                            Toast.makeText(context, "Please click to attach a PDF document", Toast.LENGTH_SHORT).show()
                        } else {
                            viewModel.insertMaterial(
                                title = title,
                                subject = selectedSubject,
                                branch = selectedBranch,
                                semester = selectedSemester,
                                description = description,
                                pdfUrl = attachedFileName
                            )
                            showSuccessDialog = true
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("submit_material_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = PremiumPrimary),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(Icons.AutoMirrored.Filled.Send, contentDescription = null, modifier = Modifier.padding(end = 8.dp).size(16.dp))
                    Text("Upload to Classroom", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }

            // Success visual: "🎉 Thank you for helping other students."
            if (showSuccessDialog) {
                AlertDialog(
                    onDismissRequest = { /* Force explicit confirm */ },
                    containerColor = PremiumCard,
                    title = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text("Success", color = PremiumPrimary, fontWeight = FontWeight.Bold)
                        }
                    },
                    text = {
                        Text(
                            text = "🎉 Thank you for helping other students.",
                            color = Color.White,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium
                        )
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                showSuccessDialog = false
                                onUploadSuccess()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = PremiumPrimary),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Okay", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                )
            }
        }
    }
}

// ==========================================
// 4. PDF READER SCREEN (View, bookmark, export)
// ==========================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    material: StudyMaterial,
    onBack: () -> Unit,
    onToggleFavorite: () -> Unit
) {
    val context = LocalContext.current
    var isBookmarked by remember { mutableStateOf(material.isFavorite) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(material.title, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                actions = {
                    // Bookmark / Favorite icon toggle
                    IconButton(
                        onClick = {
                            isBookmarked = !isBookmarked
                            onToggleFavorite()
                            val msg = if (isBookmarked) "Saved to your bookmarks!" else "Removed from bookmarks"
                            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                        }
                    ) {
                        Icon(
                            imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = "Bookmark notes",
                            tint = if (isBookmarked) PremiumPrimary else Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PremiumBackground)
            )
        },
        bottomBar = {
            // PDF Reader bottom action bar
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding(),
                color = PremiumCard,
                border = BorderStroke(1.dp, PremiumBorder)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 1. Download
                    TextButton(
                        onClick = {
                            Toast.makeText(context, "Downloading document to system storage...", Toast.LENGTH_SHORT).show()
                        },
                        colors = ButtonDefaults.textButtonColors(contentColor = Color.White)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(Icons.Default.Download, contentDescription = "Download Notes", modifier = Modifier.size(18.dp))
                            Text("Download")
                        }
                    }

                    // Divider
                    VerticalDivider(modifier = Modifier.height(24.dp), color = PremiumBorder)

                    // 2. Share
                    TextButton(
                        onClick = {
                            Toast.makeText(context, "Copying shareable secure document link...", Toast.LENGTH_SHORT).show()
                        },
                        colors = ButtonDefaults.textButtonColors(contentColor = Color.White)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(Icons.Default.Share, contentDescription = "Share Notes", modifier = Modifier.size(18.dp))
                            Text("Share")
                        }
                    }
                }
            }
        },
        contentWindowInsets = WindowInsets.safeDrawing
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(PremiumBackground)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Card displaying Branch, Semester, Subject info
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = PremiumCard),
                border = BorderStroke(1.dp, PremiumBorder),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("BRANCH", style = MaterialTheme.typography.labelSmall, color = TextSlateDim)
                        Text(material.branch, style = MaterialTheme.typography.bodyMedium, color = Color.White, fontWeight = FontWeight.Bold)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("SEMESTER", style = MaterialTheme.typography.labelSmall, color = TextSlateDim)
                        Text(material.semester, style = MaterialTheme.typography.bodyMedium, color = Color.White, fontWeight = FontWeight.Bold)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("SUBJECT", style = MaterialTheme.typography.labelSmall, color = TextSlateDim)
                        Text(material.subject, style = MaterialTheme.typography.bodyMedium, color = PremiumPrimary, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // White paper PDF viewport layout
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, PremiumBorder, RoundedCornerShape(12.dp)),
                color = Color.White,
                shape = RoundedCornerShape(12.dp),
                shadowElevation = 6.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    // Document Header Watermark inside the viewport
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Meresathpadhlo Academy",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.Gray,
                            fontWeight = FontWeight.Bold
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(Icons.Default.Verified, contentDescription = null, tint = Color(0xFF34A853), modifier = Modifier.size(10.dp))
                            Text(
                                text = "Verified PDF",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color(0xFF34A853),
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f), modifier = Modifier.padding(bottom = 16.dp))

                    // Stylized document body content rendering
                    SimpleMarkdownRendererDark(text = material.content)
                }
            }
        }
    }
}

// ==========================================
// COMPONENT: MARKDOWN RENDERERS
// ==========================================
@Composable
fun SimpleMarkdownRendererDark(text: String) {
    val lines = text.split("\n")
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        for (line in lines) {
            val trimmed = line.trim()
            if (trimmed.isEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                continue
            }
            when {
                trimmed.startsWith("###") -> {
                    val heading = trimmed.removePrefix("###").trim()
                    Text(
                        text = heading,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E293B),
                        modifier = Modifier.padding(top = 12.dp, bottom = 4.dp)
                    )
                }
                trimmed.startsWith("####") -> {
                    val heading = trimmed.removePrefix("####").trim()
                    Text(
                        text = heading,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF475569),
                        modifier = Modifier.padding(top = 8.dp, bottom = 2.dp)
                    )
                }
                trimmed.startsWith("-") || trimmed.startsWith("*") -> {
                    val bulletText = trimmed.substring(1).trim()
                    Row(
                        modifier = Modifier.padding(start = 8.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(
                            text = "•",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(end = 8.dp),
                            color = Color(0xFFF97316)
                        )
                        Text(
                            text = bulletText,
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color(0xFF334155)
                        )
                    }
                }
                else -> {
                    Text(
                        text = line,
                        style = MaterialTheme.typography.bodyLarge,
                        lineHeight = 24.sp,
                        color = Color(0xFF334155)
                    )
                }
            }
        }
    }
}
