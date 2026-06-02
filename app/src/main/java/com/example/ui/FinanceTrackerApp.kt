package com.example.ui

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import com.example.R
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.BusinessCenter
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChildCare
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.DocumentScanner
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Hotel
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.LocalGasStation
import androidx.compose.material.icons.filled.LocalGroceryStore
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.LocalParking
import androidx.compose.material.icons.filled.LocalPharmacy
import androidx.compose.material.icons.filled.LocalTaxi
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.SmokingRooms
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material.icons.filled.Train
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.LaunchedEffect
import androidx.fragment.app.FragmentActivity
import androidx.core.content.ContextCompat
import androidx.biometric.BiometricPrompt
import androidx.biometric.BiometricManager
import android.widget.Toast
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.Transaction
import com.example.ui.theme.*
import com.example.viewmodel.FinanceViewModel
import com.example.viewmodel.DateFilter
import com.example.viewmodel.ChartPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

// Categories mapping
data class TransactionCategory(
    val name: String,
    val icon: ImageVector,
    val color: Color
)

val Categories = listOf(
    TransactionCategory("Salary", Icons.Default.AttachMoney, Color(0xFF10B981)),
    TransactionCategory("Business", Icons.Default.BusinessCenter, Color(0xFF059669)),
    TransactionCategory("Freelance", Icons.Default.Work, Color(0xFF14B8A6)),
    TransactionCategory("Bonus", Icons.Default.Celebration, Color(0xFF22C55E)),
    TransactionCategory("Interest", Icons.Default.AccountBalance, Color(0xFF84CC16)),
    TransactionCategory("Dividend", Icons.Default.AttachMoney, Color(0xFF65A30D)),
    TransactionCategory("Investment", Icons.Default.AccountBalanceWallet, Color(0xFF0EA5E9)),
    TransactionCategory("Stock", Icons.Default.AccountBalance, Color(0xFF0284C7)),
    TransactionCategory("Mutual Fund", Icons.Default.AccountBalanceWallet, Color(0xFF2563EB)),
    TransactionCategory("Rental Income", Icons.Default.Home, Color(0xFF38BDF8)),
    TransactionCategory("Gift", Icons.Default.Celebration, Color(0xFFF472B6)),
    TransactionCategory("Refund", Icons.Default.Receipt, Color(0xFF2DD4BF)),
    TransactionCategory("Food", Icons.Default.Fastfood, Color(0xFFF59E0B)),
    TransactionCategory("Grocery", Icons.Default.LocalGroceryStore, Color(0xFF16A34A)),
    TransactionCategory("Restaurant", Icons.Default.Restaurant, Color(0xFFEA580C)),
    TransactionCategory("Cafe", Icons.Default.LocalCafe, Color(0xFFA16207)),
    TransactionCategory("Drinks", Icons.Default.LocalDrink, Color(0xFF06B6D4)),
    TransactionCategory("Smoking", Icons.Default.SmokingRooms, Color(0xFF78716C)),
    TransactionCategory("Shopping", Icons.Default.ShoppingCart, Color(0xFFEC4899)),
    TransactionCategory("Clothing", Icons.Default.ShoppingCart, Color(0xFFDB2777)),
    TransactionCategory("Personal Care", Icons.Default.Favorite, Color(0xFFFB7185)),
    TransactionCategory("Rent", Icons.Default.Home, Color(0xFF3B82F6)),
    TransactionCategory("EMI", Icons.Default.CreditCard, Color(0xFF6366F1)),
    TransactionCategory("Loan", Icons.Default.AccountBalance, Color(0xFF4F46E5)),
    TransactionCategory("Credit Card", Icons.Default.CreditCard, Color(0xFF7C3AED)),
    TransactionCategory("Transport", Icons.Default.DirectionsCar, Color(0xFF8B5CF6)),
    TransactionCategory("Fuel", Icons.Default.LocalGasStation, Color(0xFF9333EA)),
    TransactionCategory("Parking", Icons.Default.LocalParking, Color(0xFF6D28D9)),
    TransactionCategory("Taxi", Icons.Default.LocalTaxi, Color(0xFFEAB308)),
    TransactionCategory("Bus", Icons.Default.DirectionsBus, Color(0xFFCA8A04)),
    TransactionCategory("Train", Icons.Default.Train, Color(0xFF0891B2)),
    TransactionCategory("Flight", Icons.Default.Flight, Color(0xFF2563EB)),
    TransactionCategory("Utilities", Icons.Default.Lightbulb, Color(0xFF06B6D4)),
    TransactionCategory("Bills", Icons.Default.Receipt, Color(0xFF0E7490)),
    TransactionCategory("Electricity", Icons.Default.Lightbulb, Color(0xFFFACC15)),
    TransactionCategory("Water", Icons.Default.LocalDrink, Color(0xFF38BDF8)),
    TransactionCategory("Gas", Icons.Default.LocalGasStation, Color(0xFFF97316)),
    TransactionCategory("Internet", Icons.Default.Wifi, Color(0xFF0EA5E9)),
    TransactionCategory("Mobile", Icons.Default.PhoneAndroid, Color(0xFF22D3EE)),
    TransactionCategory("Subscription", Icons.Default.Receipt, Color(0xFF64748B)),
    TransactionCategory("Insurance", Icons.Default.AccountBalanceWallet, Color(0xFF0F766E)),
    TransactionCategory("Medical", Icons.Default.LocalHospital, Color(0xFFDC2626)),
    TransactionCategory("Pharmacy", Icons.Default.LocalPharmacy, Color(0xFFEF4444)),
    TransactionCategory("Education", Icons.Default.School, Color(0xFF7C2D12)),
    TransactionCategory("Childcare", Icons.Default.ChildCare, Color(0xFFF43F5E)),
    TransactionCategory("Sports", Icons.Default.SportsSoccer, Color(0xFF22C55E)),
    TransactionCategory("Fitness", Icons.Default.FitnessCenter, Color(0xFF84CC16)),
    TransactionCategory("Entertainment", Icons.Default.Celebration, Color(0xFFEF4444)),
    TransactionCategory("Movies", Icons.Default.Movie, Color(0xFFBE123C)),
    TransactionCategory("Travel", Icons.Default.Flight, Color(0xFF14B8A6)),
    TransactionCategory("Hotel", Icons.Default.Hotel, Color(0xFF0D9488)),
    TransactionCategory("Home Repair", Icons.Default.Build, Color(0xFF92400E)),
    TransactionCategory("Maintenance", Icons.Default.Build, Color(0xFFB45309)),
    TransactionCategory("Taxes", Icons.Default.Receipt, Color(0xFF475569)),
    TransactionCategory("Charity", Icons.Default.Favorite, Color(0xFFE11D48)),
    TransactionCategory("Gifts", Icons.Default.Celebration, Color(0xFFC026D3)),
    TransactionCategory("Fees", Icons.Default.Receipt, Color(0xFF71717A)),
    TransactionCategory("Bank Charges", Icons.Default.AccountBalance, Color(0xFF52525B)),
    TransactionCategory("Cash Withdrawal", Icons.Default.AttachMoney, Color(0xFF15803D)),
    TransactionCategory("Other", Icons.Default.Category, Color(0xFF6B7280))
)

val IncomeCategories = setOf(
    "Salary",
    "Business",
    "Freelance",
    "Bonus",
    "Interest",
    "Dividend",
    "Investment",
    "Stock",
    "Mutual Fund",
    "Rental Income",
    "Gift",
    "Refund",
    "Other"
)

fun getCategoryIcon(name: String): ImageVector {
    return Categories.find { it.name.equals(name, ignoreCase = true) }?.icon ?: Icons.Default.Category
}

fun getCategoryColor(name: String): Color {
    return Categories.find { it.name.equals(name, ignoreCase = true) }?.color ?: Color(0xFF6B7280)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinanceTrackerApp(viewModel: FinanceViewModel) {
    val transactions by viewModel.filteredTransactions.collectAsState()
    val balance by viewModel.totalBalance.collectAsState()
    val income by viewModel.totalIncome.collectAsState()
    val expense by viewModel.totalExpense.collectAsState()
    val categoryStats by viewModel.categoryStats.collectAsState()
    val currentFilter by viewModel.selectedFilter.collectAsState()
    val chartPoints by viewModel.chartPoints.collectAsState()

    val groupedTransactions = remember(transactions) {
        transactions.groupBy {
            val cal = Calendar.getInstance().apply { timeInMillis = it.date }
            cal.set(Calendar.HOUR_OF_DAY, 0)
            cal.set(Calendar.MINUTE, 0)
            cal.set(Calendar.SECOND, 0)
            cal.set(Calendar.MILLISECOND, 0)
            cal.timeInMillis
        }
    }

    var currentTab by rememberSaveable { mutableStateOf(0) }

    var showAddDialog by remember { mutableStateOf(false) }

    val context = androidx.compose.ui.platform.LocalContext.current
    val activity = remember(context) { context as? FragmentActivity }
    
    var isBiometricSetupEnabled by remember { mutableStateOf(viewModel.isBiometricEnabled()) }
    var isUnlocked by rememberSaveable { mutableStateOf(!isBiometricSetupEnabled) }

    fun showBiometricPrompt(
        title: String,
        subtitle: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit = {}
    ) {
        if (activity == null) {
            onError("Activity not found")
            return
        }
        val executor = ContextCompat.getMainExecutor(context)
        val biometricPrompt = BiometricPrompt(
            activity,
            executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    onError(errString.toString())
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    onSuccess()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    onError("Authentication failed")
                }
            }
        )

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(title)
            .setSubtitle(subtitle)
            .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL)
            .build()

        biometricPrompt.authenticate(promptInfo)
    }

    LaunchedEffect(isUnlocked) {
        if (!isUnlocked) {
            showBiometricPrompt(
                title = "Unlock RupeeFlow",
                subtitle = "Authenticate using fingerprint or phone password",
                onSuccess = { isUnlocked = true },
                onError = { err -> }
            )
        }
    }

    var isLoading by rememberSaveable { mutableStateOf(false) }
    var statusMessage by rememberSaveable { mutableStateOf<String?>(null) }
    var errorMessage by rememberSaveable { mutableStateOf<String?>(null) }

    var showCustomDateDialog by remember { mutableStateOf(false) }
    var tempStartDate by remember { mutableStateOf(0L) }
    var tempEndDate by remember { mutableStateOf(0L) }

    // Helper for date picker
    fun showDatePicker(onDateSelected: (Long) -> Unit) {
        val calendar = Calendar.getInstance()
        android.app.DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val selectedCalendar = Calendar.getInstance().apply {
                    set(Calendar.YEAR, year)
                    set(Calendar.MONTH, month)
                    set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }
                onDateSelected(selectedCalendar.timeInMillis)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    // Launcher for XML Export
    val exportXmlLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("text/xml")
    ) { uri ->
        uri?.let {
            isLoading = true
            viewModel.exportToXml(
                uri = it,
                onComplete = {
                    isLoading = false
                    statusMessage = "Transactions successfully exported to XML!"
                },
                onError = { err ->
                    isLoading = false
                    errorMessage = err
                }
            )
        }
    }

    // Launcher for XML Import
    val importXmlLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            try {
                val inputStream = context.contentResolver.openInputStream(it)
                if (inputStream != null) {
                    isLoading = true
                    viewModel.importFromXml(
                        inputStream = inputStream,
                        onComplete = { count ->
                            isLoading = false
                            statusMessage = "Successfully imported $count transactions!"
                        },
                        onError = { err ->
                            isLoading = false
                            errorMessage = err
                        }
                    )
                } else {
                    errorMessage = "Failed to open selected file"
                }
            } catch (e: Exception) {
                errorMessage = "Import failed: ${e.localizedMessage}"
            }
        }
    }

    // Launcher for PDF Export
    val exportPdfLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/pdf")
    ) { uri ->
        uri?.let {
            isLoading = true
            viewModel.exportToPdf(
                uri = it,
                onComplete = {
                    isLoading = false
                    statusMessage = "Statement exported to PDF successfully!"
                },
                onError = { err ->
                    isLoading = false
                    errorMessage = err
                }
            )
        }
    }

    // Launcher for CSV Export
    val exportCsvLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("text/csv")
    ) { uri ->
        uri?.let {
            isLoading = true
            viewModel.exportToCsv(
                uri = it,
                onComplete = {
                    isLoading = false
                    statusMessage = "Transactions successfully exported to CSV!"
                },
                onError = { err ->
                    isLoading = false
                    errorMessage = err
                }
            )
        }
    }

    // Launcher for AI Scan
    val scanLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        uri?.let {
            try {
                val mimeType = context.contentResolver.getType(it) ?: "image/jpeg"
                val inputStream = context.contentResolver.openInputStream(it)
                if (inputStream != null) {
                    isLoading = true
                    val bytes = inputStream.readBytes()
                    viewModel.scanInvoiceWithAi(
                        fileBytes = bytes,
                        mimeType = mimeType,
                        onComplete = { count ->
                            isLoading = false
                            statusMessage = "AI successfully scanned and added $count transactions!"
                        },
                        onError = { err ->
                            isLoading = false
                            errorMessage = err
                        }
                    )
                } else {
                    errorMessage = "Failed to read scanned file"
                }
            } catch (e: Exception) {
                errorMessage = "Scanning failed: ${e.localizedMessage}"
            }
        }
    }

    if (!isUnlocked) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(DarkBg)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = "App Locked",
                tint = AccentIndigo,
                modifier = Modifier.size(72.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "RupeeFlow is Locked",
                color = TextPrimary,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Use your device fingerprint or screen lock credentials to unlock",
                color = TextSecondary,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
            Spacer(modifier = Modifier.height(40.dp))
            Button(
                onClick = {
                    showBiometricPrompt(
                        title = "Unlock RupeeFlow",
                        subtitle = "Authenticate using fingerprint or phone password",
                        onSuccess = { isUnlocked = true }
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = AccentIndigo),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .height(56.dp)
                    .fillMaxWidth(0.7f)
            ) {
                Text(
                    text = "Unlock",
                    color = TextPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_codinghelp),
                            contentDescription = "CodingHelp Logo",
                            modifier = Modifier
                                .size(32.dp)
                                .padding(end = 8.dp)
                                .clip(CircleShape)
                        )
                        Text(
                            text = "RupeeFlow",
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            val newSetting = !isBiometricSetupEnabled
                            showBiometricPrompt(
                                title = if (newSetting) "Enable App Lock" else "Disable App Lock",
                                subtitle = "Authenticate to confirm your identity",
                                onSuccess = {
                                    viewModel.setBiometricEnabled(newSetting)
                                    isBiometricSetupEnabled = newSetting
                                    val status = if (newSetting) "enabled" else "disabled"
                                    Toast.makeText(context, "App lock $status successfully", Toast.LENGTH_SHORT).show()
                                },
                                onError = { err ->
                                    Toast.makeText(context, "Authentication failed: $err", Toast.LENGTH_SHORT).show()
                                }
                            )
                        }
                    ) {
                        Icon(
                            imageVector = if (isBiometricSetupEnabled) Icons.Default.Lock else Icons.Default.LockOpen,
                            contentDescription = "Toggle App Lock",
                            tint = if (isBiometricSetupEnabled) IncomeGreen else TextSecondary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkBg
                )
            )
        },
        bottomBar = {
            RupeeFlowBottomNavigation(
                selectedTab = currentTab,
                onTabSelected = { currentTab = it }
            )
        },
        floatingActionButton = {
            if (currentTab == 0) {
                FloatingActionButton(
                    onClick = { showAddDialog = true },
                    containerColor = AccentIndigo,
                    contentColor = TextPrimary,
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Transaction")
                }
            }
        },
        containerColor = DarkBg
    ) { paddingValues ->
        if (currentTab == 0) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
            // Header summary cards
            item {
                SummaryCard(balance = balance, income = income, expense = expense)
            }

            // Quick Tools & Actions row
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = 1.dp,
                            color = CardBorder.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(24.dp)
                        ),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = CardBg.copy(alpha = 0.6f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Quick Tools & Actions",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            ActionButton(
                                title = "AI Scan",
                                icon = Icons.Default.AutoAwesome,
                                iconColor = AccentViolet,
                                onClick = { scanLauncher.launch(arrayOf("image/*", "application/pdf")) },
                                modifier = Modifier.weight(1f)
                            )
                            ActionButton(
                                title = "Export PDF",
                                icon = Icons.Default.PictureAsPdf,
                                iconColor = ExpenseRed,
                                onClick = { exportPdfLauncher.launch("rupeeflow_statement.pdf") },
                                modifier = Modifier.weight(1f)
                            )
                            ActionButton(
                                title = "Export CSV",
                                icon = Icons.Default.FileDownload,
                                iconColor = AccentTeal,
                                onClick = { exportCsvLauncher.launch("rupeeflow_transactions.csv") },
                                modifier = Modifier.weight(1f)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            ActionButton(
                                title = "XML Save",
                                icon = Icons.Default.FileDownload,
                                iconColor = IncomeGreen,
                                onClick = { exportXmlLauncher.launch("rupeeflow_backup.xml") },
                                modifier = Modifier.weight(1f)
                            )
                            ActionButton(
                                title = "XML Load",
                                icon = Icons.Default.FileUpload,
                                iconColor = AlertOrange,
                                onClick = { importXmlLauncher.launch("*/*") },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            // Time Period Filter
            item {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Time Period Filter",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(DateFilter.values()) { filter ->
                            val isSelected = currentFilter == filter
                            val label = when (filter) {
                                DateFilter.ALL -> "All Time"
                                DateFilter.TODAY -> "Today"
                                DateFilter.YESTERDAY -> "Yesterday"
                                DateFilter.THIS_MONTH -> "This Month"
                                DateFilter.CUSTOM -> "Custom Range"
                            }
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (isSelected) Color(0xFF3B82F6).copy(alpha = 0.2f) else Color(0xFF1E293B))
                                    .border(
                                        width = 1.dp,
                                        color = if (isSelected) Color(0xFF3B82F6) else Color.Transparent,
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .clickable {
                                        if (filter == DateFilter.CUSTOM) {
                                            showCustomDateDialog = true
                                        } else {
                                            viewModel.selectedFilter.value = filter
                                        }
                                    }
                                    .padding(horizontal = 16.dp, vertical = 10.dp)
                            ) {
                                Text(
                                    text = label,
                                    color = if (isSelected) Color(0xFF3B82F6) else Color(0xFF94A3B8),
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
            }

            // Trend Graph
            item {
                TrendGraph(chartPoints = chartPoints)
            }

            // Spending breakdown insights
            if (categoryStats.isNotEmpty()) {
                item {
                    SpendingInsights(stats = categoryStats)
                }
            }

            // Section label
            item {
                Text(
                    text = "Transactions",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            // List of items grouped by date
            if (transactions.isEmpty()) {
                item {
                    EmptyState()
                }
            } else {
                groupedTransactions.forEach { (dateMillis, dailyList) ->
                    item(key = dateMillis) {
                        val dateLabel = remember(dateMillis) {
                            val now = Calendar.getInstance()
                            val today = Calendar.getInstance().apply {
                                set(Calendar.HOUR_OF_DAY, 0)
                                set(Calendar.MINUTE, 0)
                                set(Calendar.SECOND, 0)
                                set(Calendar.MILLISECOND, 0)
                            }
                            val yesterday = Calendar.getInstance().apply {
                                timeInMillis = today.timeInMillis
                                add(Calendar.DAY_OF_YEAR, -1)
                            }
                            
                            val itemCal = Calendar.getInstance().apply { timeInMillis = dateMillis }
                            when {
                                itemCal.get(Calendar.YEAR) == now.get(Calendar.YEAR) && 
                                itemCal.get(Calendar.DAY_OF_YEAR) == now.get(Calendar.DAY_OF_YEAR) -> "Today"
                                itemCal.timeInMillis == yesterday.timeInMillis -> "Yesterday"
                                else -> SimpleDateFormat("EEE, dd MMM yyyy", Locale.getDefault()).format(Date(dateMillis))
                            }
                        }

                        val dailyNet = dailyList.sumOf { 
                            if (it.type == "INCOME") it.amount else -it.amount
                        }
                        val formattedSubtotal = if (dailyNet >= 0) {
                            String.format("+₹%.2f", dailyNet)
                        } else {
                            String.format("-₹%.2f", -dailyNet)
                        }
                        val subtotalColor = if (dailyNet >= 0) IncomeGreen else ExpenseRed

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp, bottom = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = dateLabel.uppercase(),
                                color = TextSecondary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = formattedSubtotal,
                                color = subtotalColor,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    items(dailyList, key = { it.id }) { transaction ->
                        TransactionItem(
                            transaction = transaction,
                            onDelete = { viewModel.deleteTransaction(transaction) }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }

                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        } else {
            AnalysisScreen(
                viewModel = viewModel,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
            )
        }
    }

    if (showAddDialog) {
        AddTransactionDialog(
            onDismiss = { showAddDialog = false },
            onSave = { title, amount, type, category, date ->
                viewModel.addTransaction(
                    title = title,
                    amount = amount,
                    type = type,
                    category = category,
                    date = date
                )
                showAddDialog = false
            }
        )
    }

    // Loading overlay
    if (isLoading) {
        Dialog(onDismissRequest = {}) {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Color(0xFF1E293B),
                modifier = Modifier.width(280.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    androidx.compose.material3.CircularProgressIndicator(
                        color = Color(0xFF3B82F6),
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = "Analyzing file with Gemini AI...",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }

    // Success dialog
    if (statusMessage != null) {
        Dialog(onDismissRequest = { statusMessage = null }) {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Color(0xFF1E293B),
                modifier = Modifier.width(280.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Success",
                        tint = Color(0xFF10B981),
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = statusMessage ?: "",
                        color = Color.White,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = { statusMessage = null },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3B82F6)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("OK", color = Color.White)
                    }
                }
            }
        }
    }

    // Error dialog
    if (errorMessage != null) {
        Dialog(onDismissRequest = { errorMessage = null }) {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Color(0xFF1E293B),
                modifier = Modifier.width(280.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Error,
                        contentDescription = "Error",
                        tint = Color(0xFFEF4444),
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = errorMessage ?: "",
                        color = Color.White,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = { errorMessage = null },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3B82F6)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Dismiss", color = Color.White)
                    }
                }
            }
        }
    }

    // Custom date picker range dialog
    if (showCustomDateDialog) {
        Dialog(onDismissRequest = { showCustomDateDialog = false }) {
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = Color(0xFF1E293B),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(24.dp)
                ) {
                    Text(
                        text = "Select Date Range",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // Start Date Button
                    val startLabel = if (tempStartDate == 0L) "Select Start Date" else SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(tempStartDate))
                    Button(
                        onClick = {
                            showDatePicker { date ->
                                tempStartDate = date
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F172A)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(text = "Start: $startLabel", color = Color.White)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // End Date Button
                    val endLabel = if (tempEndDate == 0L) "Select End Date" else SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(tempEndDate))
                    Button(
                        onClick = {
                            showDatePicker { date ->
                                val cal = Calendar.getInstance().apply {
                                    timeInMillis = date
                                    set(Calendar.HOUR_OF_DAY, 23)
                                    set(Calendar.MINUTE, 59)
                                    set(Calendar.SECOND, 59)
                                    set(Calendar.MILLISECOND, 999)
                                }
                                tempEndDate = cal.timeInMillis
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F172A)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(text = "End: $endLabel", color = Color.White)
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { showCustomDateDialog = false },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF475569)),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Cancel", color = Color.White)
                        }

                        val canApply = tempStartDate != 0L && tempEndDate != 0L && tempEndDate >= tempStartDate
                        Button(
                            onClick = {
                                viewModel.customDateRange.value = Pair(tempStartDate, tempEndDate)
                                viewModel.selectedFilter.value = DateFilter.CUSTOM
                                showCustomDateDialog = false
                            },
                            enabled = canApply,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF3B82F6),
                                disabledContainerColor = Color(0xFF475569)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Apply", color = if (canApply) Color.White else Color(0xFF94A3B8))
                        }
                    }
                }
            }
        }
    }

}


@Composable
fun SummaryCard(balance: Double, income: Double, expense: Double) {
    val netSavings = income - expense
    val savingsRatio = if (income > 0) (netSavings / income).coerceIn(0.0, 1.0).toFloat() else 0f
    val savingsPercent = (savingsRatio * 100).toInt()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = 0.08f),
                shape = RoundedCornerShape(24.dp)
            ),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .background(
                    Brush.linearGradient(
                        colors = listOf(AccentIndigo, AccentViolet)
                    )
                )
                .padding(24.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Total Balance",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 0.5.sp
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(Color.White.copy(alpha = 0.15f))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "Active Wallet",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = String.format("₹%.2f", balance),
                    color = Color.White,
                    fontSize = 34.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = (-0.5).sp
                )

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Income info
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.TrendingUp,
                                contentDescription = "Income",
                                tint = Color(0xFF86EFAC),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Income",
                                color = Color.White.copy(alpha = 0.7f),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = String.format("₹%.2f", income),
                                color = Color.White,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // Expense info
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.TrendingDown,
                                contentDescription = "Expense",
                                tint = Color(0xFFFCA5A5),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Expenses",
                                color = Color.White.copy(alpha = 0.7f),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = String.format("₹%.2f", expense),
                                color = Color.White,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                if (income > 0) {
                    Spacer(modifier = Modifier.height(20.dp))
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Savings Efficiency",
                                color = Color.White.copy(alpha = 0.7f),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = "$savingsPercent%",
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        LinearProgressIndicator(
                            progress = { savingsRatio },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(5.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = Color(0xFF86EFAC),
                            trackColor = Color.White.copy(alpha = 0.2f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SpendingInsights(stats: Map<String, Double>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Spending Breakdown",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(12.dp))

            stats.forEach { (category, percentage) ->
                val color = getCategoryColor(category)
                Column(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(color)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = category,
                                color = Color.White,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        Text(
                            text = String.format("%.1f%%", percentage),
                            color = Color(0xFF94A3B8),
                            fontSize = 13.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    LinearProgressIndicator(
                        progress = { (percentage / 100.0).toFloat() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = color,
                        trackColor = color.copy(alpha = 0.15f)
                    )
                }
            }
        }
    }
}

@Composable
fun TransactionItem(transaction: Transaction, onDelete: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = CardBorder.copy(alpha = 0.5f),
                shape = RoundedCornerShape(16.dp)
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardBg.copy(alpha = 0.8f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                // Category Icon with ring
                val categoryColor = getCategoryColor(transaction.category)
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(categoryColor.copy(alpha = 0.12f))
                        .border(1.dp, categoryColor.copy(alpha = 0.25f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = getCategoryIcon(transaction.category),
                        contentDescription = transaction.category,
                        tint = categoryColor,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                // Detail labels
                Column {
                    Text(
                        text = transaction.title,
                        color = TextPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = transaction.category,
                        color = TextSecondary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                // Price text
                val isIncome = transaction.type == "INCOME"
                val prefix = if (isIncome) "+ " else "- "
                val tint = if (isIncome) IncomeGreen else ExpenseRed

                Text(
                    text = String.format("%s₹%.2f", prefix, transaction.amount),
                    color = tint,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.End
                )
                Spacer(modifier = Modifier.width(6.dp))

                // Delete action button
                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = TextSecondary.copy(alpha = 0.6f),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyState() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "No transactions yet.",
            color = Color(0xFF6B7280),
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Tap the + button to add one.",
            color = Color(0xFF6B7280),
            fontSize = 13.sp
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionDialog(onDismiss: () -> Unit, onSave: (String, Double, String, String, Long) -> Unit) {
    var title by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("EXPENSE") } // "INCOME" or "EXPENSE"
    var selectedCategory by remember { mutableStateOf("Food") }
    
    val context = androidx.compose.ui.platform.LocalContext.current
    var selectedDate by remember { mutableStateOf(System.currentTimeMillis()) }
    val dateFormatter = remember { SimpleDateFormat("dd MMM yyyy", Locale.getDefault()) }

    fun showDatePickerDialog() {
        val calendar = Calendar.getInstance().apply { timeInMillis = selectedDate }
        android.app.DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val selectedCalendar = Calendar.getInstance().apply {
                    set(Calendar.YEAR, year)
                    set(Calendar.MONTH, month)
                    set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    set(Calendar.HOUR_OF_DAY, 12)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }
                selectedDate = selectedCalendar.timeInMillis
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(28.dp),
            color = CardBg,
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, CardBorder, RoundedCornerShape(28.dp))
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth()
            ) {
                // Header row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Add Transaction",
                        color = TextPrimary,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = TextSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Type select buttons row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = {
                            type = "EXPENSE"
                            if (IncomeCategories.contains(selectedCategory)) selectedCategory = "Food"
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .border(
                                width = 1.dp,
                                color = if (type == "EXPENSE") Color.Transparent else CardBorder,
                                shape = RoundedCornerShape(14.dp)
                            ),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (type == "EXPENSE") ExpenseRed else Color.Transparent
                        )
                    ) {
                        Text(
                            text = "Expense",
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                    Button(
                        onClick = {
                            type = "INCOME"
                            selectedCategory = "Salary"
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .border(
                                width = 1.dp,
                                color = if (type == "INCOME") Color.Transparent else CardBorder,
                                shape = RoundedCornerShape(14.dp)
                            ),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (type == "INCOME") IncomeGreen else Color.Transparent
                        )
                    ) {
                        Text(
                            text = "Income",
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Text fields
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedBorderColor = AccentIndigo,
                        unfocusedBorderColor = CardBorder,
                        focusedLabelColor = AccentIndigo,
                        unfocusedLabelColor = TextSecondary,
                        cursorColor = AccentIndigo,
                        focusedContainerColor = DarkBg,
                        unfocusedContainerColor = DarkBg
                    ),
                    shape = RoundedCornerShape(14.dp),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text("Amount (₹)") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedBorderColor = AccentIndigo,
                        unfocusedBorderColor = CardBorder,
                        focusedLabelColor = AccentIndigo,
                        unfocusedLabelColor = TextSecondary,
                        cursorColor = AccentIndigo,
                        focusedContainerColor = DarkBg,
                        unfocusedContainerColor = DarkBg
                    ),
                    shape = RoundedCornerShape(14.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Date Selector
                Text(
                    text = "Transaction Date",
                    color = TextSecondary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, CardBorder, RoundedCornerShape(14.dp))
                        .clickable { showDatePickerDialog() },
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkBg)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = dateFormatter.format(Date(selectedDate)),
                            color = TextPrimary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Icon(
                            imageVector = Icons.Default.Event,
                            contentDescription = "Select Date",
                            tint = AccentIndigo,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Category selector grid/list
                Text(
                    text = "Category",
                    color = TextSecondary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(8.dp))

                val filterCats = if (type == "INCOME") {
                    Categories.filter { IncomeCategories.contains(it.name) }
                } else {
                    Categories.filter { !IncomeCategories.contains(it.name) || it.name == "Investment" || it.name == "Stock" || it.name == "Mutual Fund" || it.name == "Other" }
                }

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(filterCats) { cat ->
                        val isSelected = selectedCategory == cat.name
                        val bgCol = if (isSelected) cat.color.copy(alpha = 0.12f) else DarkBg
                        val borderCol = if (isSelected) cat.color.copy(alpha = 0.4f) else CardBorder

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(14.dp))
                                .background(bgCol)
                                .border(1.dp, borderCol, RoundedCornerShape(14.dp))
                                .clickable { selectedCategory = cat.name }
                                .padding(horizontal = 14.dp, vertical = 10.dp)
                                .size(width = 80.dp, height = 56.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = cat.icon,
                                    contentDescription = cat.name,
                                    tint = cat.color,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = cat.name,
                                    color = TextPrimary,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                // Save button
                val parsedAmount = amount.toDoubleOrNull() ?: 0.0
                val isValid = title.isNotBlank() && parsedAmount > 0.0

                Button(
                    onClick = { onSave(title, parsedAmount, type, selectedCategory, selectedDate) },
                    enabled = isValid,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AccentIndigo,
                        disabledContainerColor = CardBorder
                    ),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Text(
                        text = "Save Transaction",
                        fontWeight = FontWeight.Bold,
                        color = if (isValid) TextPrimary else TextSecondary
                    )
                }
            }
        }
    }
}

@Composable
fun ActionButton(
    title: String,
    icon: ImageVector,
    iconColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(84.dp)
            .border(
                width = 1.dp,
                color = CardBorder.copy(alpha = 0.4f),
                shape = RoundedCornerShape(18.dp)
            )
            .clickable { onClick() },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = CardBg.copy(alpha = 0.85f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(6.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(iconColor.copy(alpha = 0.1f))
                    .border(1.dp, iconColor.copy(alpha = 0.25f), RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = iconColor,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                color = TextPrimary,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun TrendGraph(chartPoints: List<ChartPoint>, modifier: Modifier = Modifier) {
    if (chartPoints.size < 2) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(CardBg, RoundedCornerShape(24.dp))
                .border(1.dp, CardBorder, RoundedCornerShape(24.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Add more transactions to view trends",
                color = TextSecondary,
                fontSize = 14.sp
            )
        }
        return
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, CardBorder, RoundedCornerShape(24.dp)),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = CardBg)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "Trend Analysis",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(20.dp))

            // Graph Canvas
            androidx.compose.foundation.Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            ) {
                val width = size.width
                val height = size.height

                val labelHeight = 24.dp.toPx()
                val leftPadding = 12.dp.toPx()
                val rightPadding = 12.dp.toPx()
                val topPadding = 16.dp.toPx()

                val chartWidth = width - leftPadding - rightPadding
                val chartHeight = height - topPadding - labelHeight

                val maxVal = chartPoints.maxOf { maxOf(it.income, it.expense) }.toFloat()
                val yMax = if (maxVal == 0f) 100f else maxVal * 1.15f

                val textPaint = android.graphics.Paint().apply {
                    color = android.graphics.Color.parseColor("#94A3B8")
                    textSize = 10.dp.toPx()
                    textAlign = android.graphics.Paint.Align.CENTER
                }

                // Draw Grid Lines (horizontal and dashed)
                val gridLines = 3
                val dashEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                for (i in 0..gridLines) {
                    val y = topPadding + chartHeight * (i.toFloat() / gridLines)
                    drawLine(
                        color = Color(0xFF475569).copy(alpha = 0.25f),
                        start = androidx.compose.ui.geometry.Offset(leftPadding, y),
                        end = androidx.compose.ui.geometry.Offset(width - rightPadding, y),
                        strokeWidth = 1.dp.toPx(),
                        pathEffect = dashEffect
                    )
                }

                val incomePoints = mutableListOf<androidx.compose.ui.geometry.Offset>()
                val expensePoints = mutableListOf<androidx.compose.ui.geometry.Offset>()

                chartPoints.forEachIndexed { index, point ->
                    val x = leftPadding + index.toFloat() / (chartPoints.size - 1) * chartWidth
                    
                    val yIncome = topPadding + chartHeight - (point.income.toFloat() / yMax) * chartHeight
                    incomePoints.add(androidx.compose.ui.geometry.Offset(x, yIncome))

                    val yExpense = topPadding + chartHeight - (point.expense.toFloat() / yMax) * chartHeight
                    expensePoints.add(androidx.compose.ui.geometry.Offset(x, yExpense))

                    // Draw labels for some points to avoid clutter
                    if (chartPoints.size <= 7 || index % (chartPoints.size / 5 + 1) == 0 || index == chartPoints.size - 1) {
                        drawContext.canvas.nativeCanvas.drawText(
                            point.dateLabel,
                            x,
                            height - 4.dp.toPx(),
                            textPaint
                        )
                    }
                }

                // Helper to create smooth path from points
                fun createPath(points: List<androidx.compose.ui.geometry.Offset>): androidx.compose.ui.graphics.Path {
                    val path = androidx.compose.ui.graphics.Path()
                    if (points.isEmpty()) return path
                    path.moveTo(points.first().x, points.first().y)
                    for (i in 1 until points.size) {
                        val prev = points[i - 1]
                        val curr = points[i]
                        val cx = (prev.x + curr.x) / 2
                        path.cubicTo(cx, prev.y, cx, curr.y, curr.x, curr.y)
                    }
                    return path
                }

                // Draw Income Path & Area
                if (incomePoints.isNotEmpty()) {
                    val incomePath = createPath(incomePoints)
                    
                    // Draw Area (fill)
                    val fillPath = androidx.compose.ui.graphics.Path().apply {
                        addPath(incomePath)
                        lineTo(incomePoints.last().x, topPadding + chartHeight)
                        lineTo(incomePoints.first().x, topPadding + chartHeight)
                        close()
                    }
                    drawPath(
                        path = fillPath,
                        brush = Brush.verticalGradient(
                            colors = listOf(IncomeGreen.copy(alpha = 0.2f), IncomeGreen.copy(alpha = 0.0f))
                        )
                    )
                    // Draw Line
                    drawPath(
                        path = incomePath,
                        color = IncomeGreen,
                        style = androidx.compose.ui.graphics.drawscope.Stroke(width = 3.dp.toPx())
                    )

                    // Draw Point dots
                    incomePoints.forEachIndexed { idx, pt ->
                        if (idx == incomePoints.size - 1) {
                            drawCircle(
                                color = IncomeGreen.copy(alpha = 0.25f),
                                radius = 8.dp.toPx(),
                                center = pt
                            )
                            drawCircle(
                                color = IncomeGreen,
                                radius = 4.dp.toPx(),
                                center = pt
                            )
                        } else if (chartPoints.size <= 12) {
                            drawCircle(
                                color = IncomeGreen,
                                radius = 2.5f.dp.toPx(),
                                center = pt
                            )
                        }
                    }
                }

                // Draw Expense Path & Area
                if (expensePoints.isNotEmpty()) {
                    val expensePath = createPath(expensePoints)
                    
                    // Draw Area (fill)
                    val fillPath = androidx.compose.ui.graphics.Path().apply {
                        addPath(expensePath)
                        lineTo(expensePoints.last().x, topPadding + chartHeight)
                        lineTo(expensePoints.first().x, topPadding + chartHeight)
                        close()
                    }
                    drawPath(
                        path = fillPath,
                        brush = Brush.verticalGradient(
                            colors = listOf(ExpenseRed.copy(alpha = 0.15f), ExpenseRed.copy(alpha = 0.0f))
                        )
                    )
                    // Draw Line
                    drawPath(
                        path = expensePath,
                        color = ExpenseRed,
                        style = androidx.compose.ui.graphics.drawscope.Stroke(width = 3.dp.toPx())
                    )

                    // Draw Point dots
                    expensePoints.forEachIndexed { idx, pt ->
                        if (idx == expensePoints.size - 1) {
                            drawCircle(
                                color = ExpenseRed.copy(alpha = 0.25f),
                                radius = 8.dp.toPx(),
                                center = pt
                            )
                            drawCircle(
                                color = ExpenseRed,
                                radius = 4.dp.toPx(),
                                center = pt
                            )
                        } else if (chartPoints.size <= 12) {
                            drawCircle(
                                color = ExpenseRed,
                                radius = 2.5f.dp.toPx(),
                                center = pt
                            )
                        }
                    }
                }
            }

            // Legend
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.size(8.dp).background(IncomeGreen, CircleShape))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Income", color = TextSecondary, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                
                Spacer(modifier = Modifier.width(28.dp))
                
                Box(modifier = Modifier.size(8.dp).background(ExpenseRed, CircleShape))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Expense", color = TextSecondary, fontSize = 12.sp, fontWeight = FontWeight.Medium)
            }
        }
    }
}


@Composable
fun RupeeFlowBottomNavigation(selectedTab: Int, onTabSelected: (Int) -> Unit) {
    NavigationBar(
        containerColor = CardBg,
        tonalElevation = 8.dp,
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .border(
                width = 1.dp,
                color = CardBorder.copy(alpha = 0.5f),
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
            )
            .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
    ) {
        NavigationBarItem(
            selected = selectedTab == 0,
            onClick = { onTabSelected(0) },
            icon = {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Dashboard",
                    tint = if (selectedTab == 0) TextPrimary else TextSecondary
                )
            },
            label = {
                Text(
                    text = "Dashboard",
                    color = if (selectedTab == 0) TextPrimary else TextSecondary,
                    fontSize = 12.sp,
                    fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Medium
                )
            },
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = AccentIndigo.copy(alpha = 0.15f)
            )
        )
        NavigationBarItem(
            selected = selectedTab == 1,
            onClick = { onTabSelected(1) },
            icon = {
                Icon(
                    imageVector = Icons.Default.TrendingUp,
                    contentDescription = "Analysis",
                    tint = if (selectedTab == 1) TextPrimary else TextSecondary
                )
            },
            label = {
                Text(
                    text = "Analysis",
                    color = if (selectedTab == 1) TextPrimary else TextSecondary,
                    fontSize = 12.sp,
                    fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Medium
                )
            },
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = AccentIndigo.copy(alpha = 0.15f)
            )
        )
    }
}

@Composable
fun CategoryPieChart(categoryTotals: Map<String, Double>, modifier: Modifier = Modifier) {
    val total = categoryTotals.values.sum()
    if (total <= 0.0) return

    val categoriesList = categoryTotals.toList().sortedByDescending { it.second }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.size(160.dp),
            contentAlignment = Alignment.Center
        ) {
            androidx.compose.foundation.Canvas(modifier = Modifier.size(130.dp)) {
                var startAngle = -90f
                categoriesList.forEach { (category, amount) ->
                    val sweepAngle = ((amount / total) * 360f).toFloat()
                    val color = getCategoryColor(category)
                    drawArc(
                        color = color,
                        startAngle = startAngle,
                        sweepAngle = sweepAngle,
                        useCenter = false,
                        style = androidx.compose.ui.graphics.drawscope.Stroke(
                            width = 16.dp.toPx(),
                            cap = androidx.compose.ui.graphics.StrokeCap.Butt
                        )
                    )
                    startAngle += sweepAngle
                }
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Total Spent",
                    color = TextSecondary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = String.format("₹%.2f", total),
                    color = TextPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Grid-based Legend for clean visual layout
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            categoriesList.chunked(2).forEach { rowItems ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    rowItems.forEach { (category, amount) ->
                        val percentage = (amount / total) * 100
                        val color = getCategoryColor(category)
                        Row(
                            modifier = Modifier.weight(1f).padding(horizontal = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(CircleShape)
                                    .background(color)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Column {
                                Text(
                                    text = category,
                                    color = TextPrimary,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = String.format("₹%.2f (%.1f%%)", amount, percentage),
                                    color = TextSecondary,
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }
                    if (rowItems.size < 2) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
fun AnalysisScreen(viewModel: com.example.viewmodel.FinanceViewModel, modifier: Modifier = Modifier) {
    val averageSize by viewModel.averageTransactionSize.collectAsState()
    val topCategory by viewModel.topSpendingCategory.collectAsState()
    val healthScore by viewModel.financialHealthScore.collectAsState()
    val categoryTotals by viewModel.categoryTotals.collectAsState()
    val totalIncome by viewModel.totalIncome.collectAsState()
    val totalExpense by viewModel.totalExpense.collectAsState()

    val savingsRate = if (totalIncome > 0) ((totalIncome - totalExpense) / totalIncome * 100).coerceAtLeast(0.0) else 0.0

    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }

        // 1. Health Score Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, CardBorder, RoundedCornerShape(24.dp)),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = CardBg)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Financial Health",
                            color = TextSecondary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 0.5.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = when {
                                healthScore >= 80 -> "Excellent"
                                healthScore >= 50 -> "Good Budgeting"
                                else -> "Needs Attention"
                            },
                            color = TextPrimary,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Your score is computed from your savings rate and expense-to-income ratio.",
                            color = TextSecondary,
                            fontSize = 12.sp,
                            lineHeight = 16.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    // Circular Progress Dial
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.size(90.dp)
                    ) {
                        androidx.compose.foundation.Canvas(modifier = Modifier.size(90.dp)) {
                            // Background circle
                            drawCircle(
                                color = CardBorder,
                                radius = size.minDimension / 2,
                                style = androidx.compose.ui.graphics.drawscope.Stroke(width = 8.dp.toPx())
                            )
                            // Foreground circle representing score
                            val strokeColor = when {
                                healthScore >= 80 -> IncomeGreen
                                healthScore >= 50 -> AlertOrange
                                else -> ExpenseRed
                            }
                            drawArc(
                                color = strokeColor,
                                startAngle = -90f,
                                sweepAngle = (healthScore / 100f) * 360f,
                                useCenter = false,
                                style = androidx.compose.ui.graphics.drawscope.Stroke(
                                    width = 8.dp.toPx(),
                                    cap = androidx.compose.ui.graphics.StrokeCap.Round
                                )
                            )
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "$healthScore",
                                color = TextPrimary,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                            Text(
                                text = "/100",
                                color = TextSecondary,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // 2. Statistics Grid
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Avg Transaction Size Card
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .border(1.dp, CardBorder, RoundedCornerShape(20.dp)),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = CardBg)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Avg. Spent",
                            color = TextSecondary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = String.format("₹%.2f", averageSize),
                            color = TextPrimary,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }

                // Savings Rate Card
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .border(1.dp, CardBorder, RoundedCornerShape(20.dp)),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = CardBg)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Savings Rate",
                            color = TextSecondary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = String.format("%.1f%%", savingsRate),
                            color = if (savingsRate >= 20.0) IncomeGreen else ExpenseRed,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
            }
        }

        // Top Category Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, CardBorder, RoundedCornerShape(20.dp)),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = CardBg)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "Top Spending Category",
                            color = TextSecondary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = topCategory ?: "No Expenses",
                            color = TextPrimary,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    if (topCategory != null) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(getCategoryColor(topCategory!!).copy(alpha = 0.15f))
                                .border(1.dp, getCategoryColor(topCategory!!).copy(alpha = 0.3f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = getCategoryIcon(topCategory!!),
                                contentDescription = topCategory,
                                tint = getCategoryColor(topCategory!!),
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }
        }

        // 3. Category Spending Breakdown (Absolute Amounts)
        if (categoryTotals.isNotEmpty()) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, CardBorder, RoundedCornerShape(24.dp)),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = CardBg)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            text = "Absolute Spending",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        CategoryPieChart(categoryTotals = categoryTotals)

                        Spacer(modifier = Modifier.height(24.dp))

                        val maxSpent = categoryTotals.values.maxOrNull() ?: 1.0

                        categoryTotals.forEach { (category, amount) ->
                            val color = getCategoryColor(category)
                            Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(
                                            modifier = Modifier
                                                .size(28.dp)
                                                .clip(CircleShape)
                                                .background(color.copy(alpha = 0.12f))
                                                .border(1.dp, color.copy(alpha = 0.25f), CircleShape),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = getCategoryIcon(category),
                                                contentDescription = category,
                                                tint = color,
                                                modifier = Modifier.size(14.dp)
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Text(
                                            text = category,
                                            color = TextPrimary,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                    Text(
                                        text = String.format("₹%.2f", amount),
                                        color = TextPrimary,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                val ratio = (amount / maxSpent).toFloat()
                                LinearProgressIndicator(
                                    progress = { ratio },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(6.dp)
                                        .clip(RoundedCornerShape(3.dp)),
                                    color = color,
                                    trackColor = color.copy(alpha = 0.1f)
                                )
                            }
                        }
                    }
                }
            }
        }

        // 4. Smart Insights
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = AccentIndigo.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(24.dp)
                    ),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = CardBg)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Lightbulb,
                            contentDescription = "Smart Insights",
                            tint = AlertOrange,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Smart Financial Tips",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))

                    val tips = remember(savingsRate, topCategory) {
                        mutableListOf<String>().apply {
                            if (savingsRate < 20.0) {
                                add("Your savings rate is currently under the recommended 20%. Try setting up a budget limit for categories like Shopping or Entertainment to boost savings.")
                            } else {
                                add("Great job! Your savings rate of " + String.format("%.1f%%", savingsRate) + " is excellent. Consider investing these idle funds into mutual funds or savings instruments.")
                            }

                            when (topCategory) {
                                "Food" -> add("Food is your highest expense area. Consider cooking at home more often or planning weekly groceries to reduce your overall budget size.")
                                "Shopping" -> add("Shopping is currently your largest expense category. Implement a '24-hour cooling rule' before checkout to avoid impulse shopping purchases.")
                                "Rent" -> add("Rent accounts for your largest cash outflow. Since fixed costs are high, review other flexible categories to ensure you stay under budget.")
                                "Utilities" -> add("Utilities is your top expense. Small changes like energy-efficient appliances or switching off idle devices can lower monthly utility bills.")
                                "Entertainment" -> add("Entertainment is your largest cost category. Look out for free local events, shared streaming subscriptions, or outdoor activities.")
                                else -> add("Review your transaction descriptions for 'Other' categories regularly to identify unexpected or forgotten cash leakages.")
                            }

                            if (totalExpense > totalIncome && totalIncome > 0) {
                                add("Warning: You are currently spending more than you earn this period. Focus on cutting down non-essential flexible expenses immediately.")
                            }
                        }
                    }

                    tips.forEachIndexed { index, tip ->
                        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                            Text(
                                text = "• ",
                                color = AccentIndigo,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = tip,
                                color = TextSecondary,
                                fontSize = 13.sp,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}
