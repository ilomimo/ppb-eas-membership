package com.example.mymembershipapp
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import kotlinx.coroutines.delay
import androidx.compose.foundation.border
import androidx.compose.ui.draw.alpha
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.BorderStroke
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.zIndex
import androidx.compose.ui.layout.ContentScale
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.material3.OutlinedTextFieldDefaults




class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            MaterialTheme {
                // Navigasi antar halaman utama aplikasi
                NavHost(navController = navController, startDestination = "login") {
                    // Halaman untuk input nomor HP sebelum kirim OTP
                    composable("login") { LoginScreen(navController) }
                    // Navigasi ke halaman verifikasi
                    composable("verification/{phoneNumber}") { backStackEntry ->
                        val phoneNumber = backStackEntry.arguments?.getString("phoneNumber") ?: ""
                        VerificationScreen(navController, phoneNumber)
                    }
                    // Navigasi ke dashboard
                    composable("dashboard") { FullDashboardScreen(navController) }
                    // Navigasi ke halaman poin
                    composable("poin") { PoinPage(navController) }
                    // Navigasi ke halaman transfer pulsa
                    composable("transfer") { TransferPage(navController) }
                    // Navigasi ke halaman belanja/paket
                    composable("belanja") { BelanjaPage(navController) }
                    // Navigasi ke detail paket
                    composable("package_detail/{packageTitle}/{packageQuota}/{packagePrice}") { backStackEntry ->
                        val title = backStackEntry.arguments?.getString("packageTitle") ?: ""
                        val quota = backStackEntry.arguments?.getString("packageQuota") ?: ""
                        val price = backStackEntry.arguments?.getString("packagePrice") ?: ""
                        PackageDetailPage(navController, title, quota, price)
                    }
                    // Navigasi ke halaman metode pembayaran
                    composable("payment_method/{total}") { backStackEntry ->
                        val total = backStackEntry.arguments?.getString("total") ?: "0"
                        PaymentMethodPage(navController, total)
                    }
                    // Navigasi ke halaman sukses pembayaran
                    composable("payment_success/{method}/{total}") { backStackEntry ->
                        val method = backStackEntry.arguments?.getString("method") ?: ""
                        val total = backStackEntry.arguments?.getString("total") ?: "0"
                        PaymentSuccessPage(navController, method, total)
                    }
                    // Navigasi ke halaman topup pulsa
                    composable("pulsa_topup") { PulsaTopupPage(navController) }
                }
            }
        }
    }
}


// Halaman untuk input nomor HP sebelum kirim OTP
@Composable
fun LoginScreen(navController: NavController) {
    var phoneNumber by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF5C2136),
                        Color(0xFF8B3651),
                        Color(0xFFB8405A)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(80.dp))

            // Logo aplikasi di tengah
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(50.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "TC",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                "TC Cell",
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White
            )

            Text(
                "Selamat datang kembali! 👋",
                fontSize = 16.sp,
                color = Color.White.copy(alpha = 0.8f)
            )

            Spacer(modifier = Modifier.height(60.dp))

            // Form login dalam card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Masuk dengan Nomor HP",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A1A1A)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        "Masukkan nomor HP yang terdaftar",
                        fontSize = 14.sp,
                        color = Color(0xFF666666),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // Input nomor HP dengan kode negara +62
                    OutlinedTextField(
                        value = phoneNumber,
                        onValueChange = { phoneNumber = it },
                        label = { Text("Nomor HP") },
                        placeholder = { Text("08XXXXXXXXXX") },
                        leadingIcon = {
                            Text(
                                "🇮🇩 +62",
                                fontSize = 14.sp,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF5C2136),
                            focusedLabelColor = Color(0xFF5C2136),
                            cursorColor = Color(0xFF5C2136)
                        ),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // Tombol isi cepat nomor demo
                    Text(
                        "Atau pilih nomor cepat:",
                        fontSize = 12.sp,
                        color = Color(0xFF666666)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        QuickFillButton(
                            text = "Demo 1",
                            number = "081112223344",
                            modifier = Modifier.weight(1f)
                        ) {
                            phoneNumber = "081112223344"
                        }

                        QuickFillButton(
                            text = "Demo 2",
                            number = "085567889900",
                            modifier = Modifier.weight(1f)
                        ) {
                            phoneNumber = "085567889900"
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // Tombol kirim kode OTP
                    Button(
                        onClick = {
                            if (phoneNumber.isNotEmpty()) {
                                isLoading = true
                                // Navigasi ke halaman verifikasi
                                android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                                    isLoading = false
                                    navController.navigate("verification/$phoneNumber")
                                }, 1500)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF5C2136)
                        ),
                        enabled = phoneNumber.isNotEmpty() && !isLoading
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        } else {
                            Text(
                                "Kirim Kode OTP",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        "Dengan masuk, kamu menyetujui syarat & ketentuan TC Cell",
                        fontSize = 11.sp,
                        color = Color(0xFF999999),
                        textAlign = TextAlign.Center,
                        lineHeight = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Footer aplikasi
            Text(
                "TC Cell © 2025 • Version 1.0.0",
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.6f)
            )
        }
    }
}

// Tombol cepat untuk mengisi nomor HP demo
@Composable
fun QuickFillButton(
    text: String,
    number: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(40.dp),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, Color(0xFFE0E0E0)),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = Color(0xFF5C2136)
        )
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                number,
                fontSize = 8.sp,
                color = Color(0xFF666666)
            )
        }
    }
}

// Verifikasi OTP setelah login
@Composable
fun VerificationScreen(navController: NavController, phoneNumber: String) {
    var otpCode by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var countdown by remember { mutableStateOf(60) }
    var canResend by remember { mutableStateOf(false) }

    // Timer mundur untuk resend OTP
    LaunchedEffect(Unit) {
        while (countdown > 0) {
            delay(1000)
            countdown--
        }
        canResend = true
    }

    // Simulasi auto-fill kode OTP demo
    LaunchedEffect(phoneNumber) {
        delay(2000) // Simulate receiving SMS
        when (phoneNumber) {
            "081112223344" -> otpCode = "123456"
            "085567889900" -> otpCode = "654321"
            else -> otpCode = "111111"
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF5C2136),
                        Color(0xFF8B3651),
                        Color(0xFFB8405A)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(60.dp))

            // Tombol kembali ke halaman sebelumnya
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                IconButton(
                    onClick = { navController.popBackStack() }
                ) {
                    Text("←", fontSize = 24.sp, color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Icon verifikasi
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(40.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text("📱", fontSize = 40.sp)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                "Verifikasi Nomor HP",
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                "Kode OTP telah dikirim ke",
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.8f)
            )

            Text(
                phoneNumber,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Card form verifikasi OTP
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Masukkan Kode OTP",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A1A1A)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        "Kode verifikasi 6 digit",
                        fontSize = 14.sp,
                        color = Color(0xFF666666)
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // Kotak input OTP 6 digit dengan fokus per digit
                    OTPInputField(
                        value = otpCode,
                        onValueChange = { otpCode = it },
                        length = 6
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Notifikasi jika OTP terisi otomatis
                    if (otpCode.isNotEmpty()) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFFE8F5E8)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("✅", fontSize = 16.sp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    "Kode OTP terdeteksi otomatis",
                                    fontSize = 12.sp,
                                    color = Color(0xFF2E7D32),
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(24.dp))
                    }

                    // Tombol verifikasi dan masuk dashboard
                    Button(
                        onClick = {
                            if (otpCode.length == 6) {
                                isLoading = true
                                // Navigasi ke dashboard
                                android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                                    isLoading = false
                                    navController.navigate("dashboard") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                }, 1500)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF5C2136)
                        ),
                        enabled = otpCode.length == 6 && !isLoading
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        } else {
                            Text(
                                "Verifikasi & Masuk",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Bagian kirim ulang kode OTP
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Tidak menerima kode? ",
                            fontSize = 14.sp,
                            color = Color(0xFF666666)
                        )

                        if (canResend) {
                            Text(
                                "Kirim Ulang",
                                fontSize = 14.sp,
                                color = Color(0xFF5C2136),
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.clickable {
                                    countdown = 60
                                    canResend = false
                                    // Reset auto-fill demo OTP
                                    otpCode = ""
                                    android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                                        when (phoneNumber) {
                                            "081112223344" -> otpCode = "123456"
                                            "085567889900" -> otpCode = "654321"
                                            else -> otpCode = "111111"
                                        }
                                    }, 2000)
                                }
                            )
                        } else {
                            Text(
                                "Kirim ulang dalam ${countdown}s",
                                fontSize = 14.sp,
                                color = Color(0xFF999999)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Info kode demo untuk testing
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFF5F5F5)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp)
                        ) {
                            Text(
                                "Demo Codes:",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF666666)
                            )
                            Text(
                                "• 081112223344 → 123456",
                                fontSize = 11.sp,
                                color = Color(0xFF666666)
                            )
                            Text(
                                "• 085567889900 → 654321",
                                fontSize = 11.sp,
                                color = Color(0xFF666666)
                            )
                            Text(
                                "• Others → 111111",
                                fontSize = 11.sp,
                                color = Color(0xFF666666)
                            )
                        }
                    }
                }
            }
        }
    }
}

// Kotak input OTP 6 digit dengan fokus per digit
@Composable
fun OTPInputField(
    value: String,
    onValueChange: (String) -> Unit,
    length: Int = 6
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(length) { index ->
            val char = if (index < value.length) value[index].toString() else ""
            val isFocused = index == value.length

            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        color = if (isFocused) Color(0xFFFFEBEE) else Color(0xFFF8F9FA),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .border(
                        width = 2.dp,
                        color = if (isFocused) Color(0xFF5C2136) else if (char.isNotEmpty()) Color(0xFF4CAF50) else Color(0xFFE0E0E0),
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = char,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1A1A)
                )
            }
        }
    }

    // TextField tersembunyi untuk menangkap input angka OTP
    OutlinedTextField(
        value = value,
        onValueChange = { newValue ->
            if (newValue.length <= length && newValue.all { it.isDigit() }) {
                onValueChange(newValue)
            }
        },
        modifier = Modifier
            .size(0.dp)
            .alpha(0f),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
}





// Halaman utama dashboard setelah login
@Composable
fun FullDashboardScreen(navController: NavController) {
    Box(modifier = Modifier.fillMaxSize().background(Color(0xFFF8F9FA))) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            item { ModernHeaderSection(navController) }
            item { ModernContentSection(navController) }
        }
        BottomNav(modifier = Modifier.align(Alignment.BottomCenter), navController = navController)
    }
}


// Bagian header modern di dashboard (greeting, saldo, poin)

@Composable
fun ModernHeaderSection(navController: NavController) {
    var currentPoints by remember { mutableStateOf(2000) }
    var showPoinDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp) // Increased height for better poin section
    ) {
        // Background with gradient
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF5C2136),
                            Color(0xFF8B3651)
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Row - Greeting & Notification
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        "Selamat Pagi! 👋",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 14.sp
                    )
                    Text(
                        "Clarissa",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    )
                }
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(20.dp))
                        .clickable { },
                    contentAlignment = Alignment.Center
                ) {
                    Text("🔔", fontSize = 20.sp)
                }
            }

            // Balance & Points Cards
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                // Pulsa Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("💳 Pulsa Kamu", fontSize = 12.sp, color = Color(0xFF666666))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    "Rp 88.800",
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFF1A1A1A)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .background(Color(0xFF5C2136), RoundedCornerShape(16.dp))
                                        .clickable { navController.navigate("pulsa_topup") },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("+", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                }
                            }
                            Text("Berlaku sampai 1 Jan 2026", fontSize = 11.sp, color = Color(0xFF999999))
                        }
                    }
                }

                // Enhanced Poin Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFFFEBEE) // Light red background
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("🎁", fontSize = 16.sp)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Poin Rewards", fontSize = 12.sp, color = Color(0xFF666666))
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    currentPoints.toString(),
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFFE53E3E)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("POIN", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFFE53E3E))
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("👑", fontSize = 12.sp)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Gold Member", fontSize = 11.sp, color = Color(0xFFDAA520), fontWeight = FontWeight.Medium)
                            }
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Button(
                                onClick = { showPoinDialog = true },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFE53E3E)
                                ),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.height(40.dp)
                            ) {
                                Text("Tukar", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                "Lihat Semua →",
                                color = Color(0xFFE53E3E),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.clickable { navController.navigate("poin") }
                            )
                        }
                    }
                }
            }
        }
    }

    // Poin Exchange Dialog
    if (showPoinDialog) {
        PoinExchangeDialog(
            currentPoints = currentPoints,
            onDismiss = { showPoinDialog = false },
            onExchange = { pointsUsed ->
                currentPoints -= pointsUsed
                showPoinDialog = false
            }
        )
    }
}

@Composable
fun PoinExchangeDialog(
    currentPoints: Int,
    onDismiss: () -> Unit,
    onExchange: (Int) -> Unit
) {
    var selectedReward by remember { mutableStateOf<ExchangeReward?>(null) }
    var showSuccess by remember { mutableStateOf(false) }

    val availableRewards = listOf(
        ExchangeReward("Diskon ShopeePay 50%", 500, "🛒", true),
        ExchangeReward("Voucher Grab Rp 20K", 750, "🚗", true),
        ExchangeReward("OVO Points 10K", 900, "💰", true),
        ExchangeReward("Token Listrik 50K", 1200, "⚡", currentPoints >= 1200),
        ExchangeReward("Netflix Premium", 1500, "🎬", currentPoints >= 1500)
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            if (selectedReward != null && selectedReward!!.available) {
                Button(
                    onClick = {
                        selectedReward?.let { reward ->
                            showSuccess = true
                            android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                                onExchange(reward.points)
                                showSuccess = false
                            }, 1500)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53E3E)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Tukar Sekarang", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                border = BorderStroke(1.dp, Color(0xFFE53E3E)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Batal", color = Color(0xFFE53E3E))
            }
        },
        title = {
            Column {
                Text(
                    "Tukar Poin Rewards 🎁",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color(0xFF1A1A1A)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Poin kamu: ", fontSize = 12.sp, color = Color(0xFF666666))
                    Text(
                        "$currentPoints POIN",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFE53E3E)
                    )
                }
            }
        },
        text = {
            if (showSuccess) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .background(Color(0xFF4CAF50), RoundedCornerShape(40.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("✓", fontSize = 40.sp, color = Color.White)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "Berhasil Ditukar! 🎉",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color(0xFF1A1A1A)
                    )
                    Text(
                        "Reward akan dikirim ke email kamu",
                        fontSize = 12.sp,
                        color = Color(0xFF666666),
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.height(300.dp)
                ) {
                    items(availableRewards) { reward ->
                        RewardSelectionItem(
                            reward = reward,
                            isSelected = selectedReward == reward,
                            onClick = { selectedReward = reward }
                        )
                    }
                }
            }
        },
        shape = RoundedCornerShape(20.dp)
    )
}

@Composable
fun RewardSelectionItem(
    reward: ExchangeReward,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = reward.available) { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = when {
                isSelected -> Color(0xFFFFEBEE)
                reward.available -> Color.White
                else -> Color(0xFFF5F5F5)
            }
        ),
        border = if (isSelected) BorderStroke(2.dp, Color(0xFFE53E3E)) else null,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                reward.icon,
                fontSize = 24.sp,
                modifier = Modifier.alpha(if (reward.available) 1f else 0.5f)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    reward.title,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    color = if (reward.available) Color(0xFF1A1A1A) else Color(0xFF999999)
                )
                Text(
                    "${reward.points} POIN",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (reward.available) Color(0xFFE53E3E) else Color(0xFF999999)
                )
                if (!reward.available) {
                    Text(
                        "Poin tidak cukup",
                        fontSize = 10.sp,
                        color = Color(0xFF999999),
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                    )
                }
            }

            RadioButton(
                selected = isSelected,
                onClick = { if (reward.available) onClick() },
                enabled = reward.available,
                colors = RadioButtonDefaults.colors(selectedColor = Color(0xFFE53E3E))
            )
        }
    }
}

// Data class for exchange rewards
data class ExchangeReward(
    val title: String,
    val points: Int,
    val icon: String,
    val available: Boolean
)

// Bagian konten utama dashboard (aksi cepat, promo, layanan)
@Composable
fun ModernContentSection(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        // Aksi cepat (beli paket, transfer, tukar poin, isi pulsa)
        ModernQuickActions(navController)

        Spacer(modifier = Modifier.height(24.dp))

        // Statistik penggunaan bulan ini
        ModernUsageStats()

        Spacer(modifier = Modifier.height(32.dp))

        // Penawaran spesial untuk user
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Penawaran Spesial ⭐",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp,
                color = Color(0xFF1A1A1A)
            )
            Text(
                "Lihat Semua",
                color = Color(0xFFE53E3E),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.clickable { navController.navigate("belanja") }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(horizontal = 4.dp)
        ) {
            items(getFeaturedOffers()) { offer ->
                ModernOfferCard(
                    title = offer.title,
                    subtitle = offer.subtitle,
                    price = offer.price,
                    originalPrice = offer.originalPrice,
                    backgroundColor = offer.backgroundColor,
                    textColor = offer.textColor,
                    onClick = {
                        navController.navigate("package_detail/${offer.title}/${offer.subtitle}/${offer.price}")
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Penukaran poin favorit
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Tukar Poin Favoritmu 🎁",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp,
                color = Color(0xFF1A1A1A)
            )
            Text(
                "Lihat Semua",
                color = Color(0xFFE53E3E),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.clickable { navController.navigate("poin") }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(horizontal = 4.dp)
        ) {
            items(getFeaturedRewards()) { reward ->
                ModernRewardCard(
                    title = reward.title,
                    points = reward.points,
                    icon = reward.icon,
                    available = reward.available,
                    onClick = { navController.navigate("poin") }
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Layanan digital lain (gaming, e-wallet, dsb)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Layanan Digital 🚀",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp,
                color = Color(0xFF1A1A1A)
            )
            Text(
                "Lihat Semua",
                color = Color(0xFFE53E3E),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.clickable { }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(horizontal = 4.dp)
        ) {
            items(getDigitalServices()) { service ->
                ModernServiceCard(
                    title = service.title,
                    subtitle = service.subtitle,
                    icon = service.icon,
                    onClick = { }
                )
            }
        }

        Spacer(modifier = Modifier.height(100.dp)) // Biar nav bar tidak ketutup
    }
}

// Komponen aksi cepat di dashboard
@Composable
fun ModernQuickActions(navController: NavController) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                "Aksi Cepat",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color(0xFF1A1A1A)
            )
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                QuickActionItem("📦", "Beli Paket") { navController.navigate("belanja") }
                QuickActionItem("🔁", "Transfer") { navController.navigate("transfer") }
                QuickActionItem("🎁", "Tukar Poin") { navController.navigate("poin") }
                QuickActionItem("📱", "Isi Pulsa") { navController.navigate("pulsa_topup") }
            }
        }
    }
}

// Item aksi cepat (icon + label)
@Composable
fun QuickActionItem(icon: String, label: String, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .background(Color(0xFFF8F9FA), RoundedCornerShape(28.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(icon, fontSize = 24.sp)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF666666),
            textAlign = TextAlign.Center
        )
    }
}

// Statistik penggunaan (internet, telpon, sms)
@Composable
fun ModernUsageStats() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                "Penggunaan Bulan Ini",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color(0xFF1A1A1A)
            )
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ModernStatItem("📶", "Internet", "15.6 GB", "dari 25 GB", Color(0xFF4CAF50))
                ModernStatItem("☎️", "Telpon", "45 Min", "dari ∞ Min", Color(0xFF2196F3))
                ModernStatItem("💬", "SMS", "102 SMS", "dari 200 SMS", Color(0xFFFF9800))
            }
        }
    }
}

// Item statistik penggunaan
@Composable
fun ModernStatItem(icon: String, title: String, used: String, total: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(icon, fontSize = 20.sp)
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            used,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = color
        )
        Text(
            total,
            fontSize = 10.sp,
            color = Color(0xFF999999)
        )
        Text(
            title,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF666666)
        )
    }
}

// Card penawaran spesial (promo paket internet)
@Composable
fun ModernOfferCard(
    title: String,
    subtitle: String,
    price: String,
    originalPrice: String? = null,
    backgroundColor: Color,
    textColor: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(200.dp)
            .height(140.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = textColor
                )
                Text(
                    subtitle,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = textColor
                )
            }

            Row(verticalAlignment = Alignment.Bottom) {
                Column {
                    if (originalPrice != null) {
                        Text(
                            originalPrice,
                            fontSize = 10.sp,
                            color = textColor.copy(alpha = 0.7f),
                            textDecoration = TextDecoration.LineThrough
                        )
                    }
                    Text(
                        price,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = textColor
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Text("→", fontSize = 20.sp, color = textColor)
            }
        }
    }
}

// Card reward penukaran poin
@Composable
fun ModernRewardCard(
    title: String,
    points: String,
    icon: String,
    available: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(160.dp)
            .height(120.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (available) Color.White else Color(0xFFF5F5F5)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = if (available) Color(0xFF1A1A1A) else Color(0xFF999999),
                textAlign = TextAlign.Center,
                maxLines = 2
            )

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    title,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (available) Color(0xFF1A1A1A) else Color(0xFF999999),
                    textAlign = TextAlign.Center,
                    maxLines = 2
                )
                Text(
                    points,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (available) Color(0xFFE53E3E) else Color(0xFF999999)
                )
            }
        }
    }
}

// Card layanan digital (gaming, streaming, dll)
@Composable
fun ModernServiceCard(
    title: String,
    subtitle: String,
    icon: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(140.dp)
            .height(100.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(icon, fontSize = 28.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                title,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A1A1A),
                textAlign = TextAlign.Center
            )
            Text(
                subtitle,
                fontSize = 10.sp,
                color = Color(0xFF666666),
                textAlign = TextAlign.Center
            )
        }
    }
}

// Data class untuk konten dashboard (penawaran, reward, layanan)
data class OfferData(
    val title: String,
    val subtitle: String,
    val price: String,
    val originalPrice: String? = null,
    val backgroundColor: Color,
    val textColor: Color
)

data class RewardData(
    val title: String,
    val points: String,
    val icon: String,
    val available: Boolean
)

data class ServiceData(
    val title: String,
    val subtitle: String,
    val icon: String
)

fun getFeaturedOffers(): List<OfferData> {
    return listOf(
        OfferData(
            title = "Paket Super",
            subtitle = "25 GB",
            price = "Rp 55.000",
            originalPrice = "Rp 75.000",
            backgroundColor = Color(0xFFE53E3E),
            textColor = Color.White
        ),
        OfferData(
            title = "Unlimited",
            subtitle = "∞ GB",
            price = "Rp 99.000",
            backgroundColor = Color(0xFF2196F3),
            textColor = Color.White
        ),
        OfferData(
            title = "Hemat Banget",
            subtitle = "10 GB",
            price = "Rp 25.000",
            backgroundColor = Color(0xFF4CAF50),
            textColor = Color.White
        )
    )
}

fun getFeaturedRewards(): List<RewardData> {
    return listOf(
        RewardData("Diskon ShopeePay 50%", "900 POIN", "🛒", true),
        RewardData("Voucher Grab", "500 POIN", "🚗", true),
        RewardData("Netflix Premium", "1500 POIN", "🎬", false),
        RewardData("Spotify Premium", "1200 POIN", "🎵", true),
        RewardData("Token Listrik", "1800 POIN", "⚡", false)
    )
}

fun getDigitalServices(): List<ServiceData> {
    return listOf(
        ServiceData("Gaming", "Mobile Legends", "🎮"),
        ServiceData("Streaming", "Netflix & Spotify", "📺"),
        ServiceData("E-Wallet", "TopUp Digital", "💰"),
        ServiceData("Insurance", "Asuransi Digital", "🛡️"),
        ServiceData("Emergency", "Pulsa Darurat", "🆘")
    )
}

// Header lama (tidak dipakai di dashboard modern)
@Composable
fun HeaderSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.header_background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 12.dp, start = 16.dp, end = 16.dp),
            verticalArrangement = Arrangement.Bottom
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Text("Selamat Pagi, Clarissa", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text("081112223345", color = Color.White, fontSize = 14.sp)
                }
                Text("🔔", color = Color.White, fontSize = 18.sp)
            }
        }
    }
}

// Konten utama lama (tidak dipakai di dashboard modern)
@Composable
fun ContentSection(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(16.dp)
    ) {
        Column {
            // Card saldo pulsa user
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column {
                            Text("Pulsa kamu", fontSize = 14.sp)
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Rp 88.800", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.width(8.dp))
                                IconButton(onClick = { navController.navigate("pulsa_topup") }) {
                                    Box(
                                        modifier = Modifier
                                            .size(24.dp)
                                            .background(Color(0xFF5C2136), shape = RoundedCornerShape(12.dp)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text("+", color = Color.White, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                            Text("Berlaku sampai 1 Januari 2026", fontSize = 12.sp, color = Color.Gray)
                        }
                    }
                }
            }

            PulsaShortcutRow(navController)

            Spacer(modifier = Modifier.height(12.dp))

            FeatureShortcutRow()
            Spacer(modifier = Modifier.height(16.dp))

            // Statistik penggunaan lama
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                StatCard("Internet", "15.6", "GB", Color(0xFFD50000))
                StatCard("Telpon", "45", "Min", Color.Black)
                StatCard("SMS", "102", "Pesan", Color(0xFFD50000))
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Section promo 1
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Lihat ", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text("Lihat Semua", color = Color(0xFF5C2136), fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Box(modifier = Modifier.height(150.dp)) {
                LazyRow(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(listOf("Promo A", "Promo B")) {
                        ModernPromoCard(title = it)
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Section promo 2
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Terbaru dari TC Cell", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text("Lihat Semua", color = Color(0xFF5C2136), fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Box(modifier = Modifier.height(150.dp)) {
                LazyRow(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(listOf("Promo C", "Promo D")) {
                        PromoBox(title = it)
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Section langganan favorit
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Langganan Favorit", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text("Lihat Semua", color = Color(0xFF5C2136), fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Box(modifier = Modifier.height(150.dp)) {
                LazyRow(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(listOf("Spotify Premium", "Netflix Mobile", "YouTube Tanpa Iklan")) {
                        PromoBox(title = it)
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Layanan Lainnya", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text("Lihat Semua", color = Color(0xFF5C2136), fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Box(modifier = Modifier.height(150.dp)) {
                LazyRow(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(listOf("Asuransi Digital", "Pulsa Darurat", "Kuota Family")) {
                        PromoBox(title = it)
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Section promo spesial
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Spesial Untuk Kamu", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text("Lihat Semua", color = Color(0xFF5C2136), fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Box(modifier = Modifier.height(150.dp)) {
                LazyRow(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(listOf("Promo Eksklusif", "Diskon Hari Ini", "Penawaran Pribadi")) {
                        PromoBox(title = it)
                    }
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

// Baris shortcut untuk fitur pulsa (beli paket, transfer, cek riwayat, dll)
@Composable
fun PulsaShortcutRow(navController: NavController) {
    val items = listOf("Beli Paket", "Transfer", "Cek Riwayat", "Lainnya")
    val icons = listOf("📦", "🔁", "🧾", "➕")
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEachIndexed { index, label ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable {
                        when (label) {
                            "Beli Paket" -> navController.navigate("pulsa_topup")
                            "Transfer" -> navController.navigate("transfer")
                        }
                    }
                ) {
                    Text(icons[index], fontSize = 20.sp)
                    Text(label, fontSize = 12.sp)
                }
            }
        }
    }
}

// Halaman isi ulang pulsa
@Composable
fun PulsaTopupPage(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
            .padding(16.dp)
    ) {
        // Header isi pulsa, ada tombol kembali
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 24.dp, top = 16.dp)
        ) {
            Text(
                "←",
                fontSize = 24.sp,
                modifier = Modifier.clickable { navController.navigate("dashboard") }
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                "Isi Pulsa",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A1A1A)
            )
        }

        // Card saldo pulsa saat ini
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Pulsa Saat Ini", fontSize = 14.sp, color = Color(0xFF666666))
                        Text("Rp 88.800", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1A1A1A))
                        Text("Berlaku sampai 1 Januari 2026", fontSize = 12.sp, color = Color(0xFF666666))
                    }
                    Text("📱", fontSize = 32.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Pilihan nominal pulsa
        Text(
            "Pilih Nominal Pulsa",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = Color(0xFF1A1A1A)
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            val pulsaOptions = listOf(
                Triple("Rp 5.000", "+7 Hari", "5000"),
                Triple("Rp 10.000", "+15 Hari", "10000"),
                Triple("Rp 25.000", "+30 Hari", "25000"),
                Triple("Rp 50.000", "+45 Hari", "50000"),
                Triple("Rp 100.000", "+60 Hari", "100000")
            )

            items(pulsaOptions) { (amount, validity, price) ->
                PulsaOptionCard(
                    amount = amount,
                    validity = validity,
                    onClick = {
                        navController.navigate("payment_method/$price")
                    }
                )
            }
        }
    }
}

// Card pilihan nominal pulsa
@Composable
fun PulsaOptionCard(
    amount: String,
    validity: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(Color(0xFFF0F0F0), shape = RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("💳", fontSize = 20.sp)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        amount,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color(0xFF1A1A1A)
                    )
                    Text(
                        validity,
                        fontSize = 12.sp,
                        color = Color(0xFF666666)
                    )
                }
            }
            Text("→", fontSize = 20.sp, color = Color(0xFF666666))
        }
    }
}

// Halaman transfer pulsa ke nomor lain
@Composable
fun TransferPage(navController: NavController) {
    val showDialog = remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        // Header transfer pulsa, tombol kembali
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Text("←", fontSize = 24.sp, modifier = Modifier.clickable { navController.navigate("dashboard") })
            Spacer(modifier = Modifier.width(8.dp))
            Text("Add Credit", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }

        // Input nomor tujuan transfer, bisa pilih kontak
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Phone number", fontSize = 12.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(Color.White, shape = RoundedCornerShape(12.dp))
                            .padding(12.dp)
                    ) {
                        Text("081112223345")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(onClick = { showDialog.value = true }) {
                        Icon(Icons.Default.Person, contentDescription = "Pick Contact")
                    }
                }
            }
        }

        // Info tambahan tentang masa aktif pulsa
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFD9F1FF))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("ℹ️", fontSize = 20.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "The active period extension is calculated from the\ndate of purchase of credit.",
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.weight(1f))
                Text("✕", fontSize = 16.sp)
            }
        }

        // Pilih nominal transfer pulsa
        Text("Select Credit Nominal", fontWeight = FontWeight.Bold, fontSize = 14.sp, modifier = Modifier.padding(vertical = 8.dp))

        LazyColumn {
            val data = listOf(
                "Rp5,000" to "+7 DAYS",
                "Rp10,000" to "+15 DAYS",
                "Rp15,000" to "+20 DAYS",
                "Rp20,000" to "+30 DAYS",
                "Rp25,000" to "+30 DAYS",
                "Rp30,000" to "+30 DAYS",
                "Rp50,000" to "+45 DAYS",
                "Rp75,000" to "+45 DAYS",
                "Rp100,000" to "+60 DAYS",
                "Rp150,000" to "+120 DAYS",
                "Rp200,000" to "+150 DAYS",
                "Rp300,000" to "+180 DAYS"
            )

            items(data.chunked(2)) { row ->
                Row(modifier = Modifier.fillMaxWidth()) {
                    row.forEach { (amount, days) ->
                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .padding(4.dp)
                                .clickable {
                                    val cleanAmount = amount.replace("Rp", "").replace(",", "")
                                    navController.navigate("payment_method/$cleanAmount")
                                },
                            elevation = CardDefaults.cardElevation(2.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(amount, color = Color.Red, fontWeight = FontWeight.Bold)
                                Text(days, fontSize = 12.sp)
                                Text("+0 POIN", fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        }

        if (showDialog.value) {
            ContactPickerDialog(onDismiss = { showDialog.value = false })
        }
    }
}

// Dialog pilih kontak untuk transfer pulsa
@Composable
fun ContactPickerDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Text("Ok", color = Color.White)
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss, border = BorderStroke(1.dp, Color.Red)) {
                Text("Cancel", color = Color.Red)
            }
        },
        title = {
            Text("Choose Phone Number", fontWeight = FontWeight.Bold, color = Color(0xFF00264D))
        },
        text = {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("📶 TC CELL")
                    RadioButton(selected = false, onClick = {})
                }
                HorizontalDivider()
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("📇 Choose from contact", fontWeight = FontWeight.Bold)
                    RadioButton(selected = true, onClick = {})
                }
            }
        }
    )
}

// Shortcut fitur tambahan (stamp, game, darurat, topup)
@Composable
fun FeatureShortcutRow() {
    val items = listOf("Stamp", "Game", "Darurat", "Topup")
    val icons = listOf("🎯", "🎮", "🆘", "💰")
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEachIndexed { index, label ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(icons[index], fontSize = 20.sp)
                    Text(label, fontSize = 12.sp)
                }
            }
        }
    }
}

// Card statistik penggunaan (lama)
@Composable
fun StatCard(title: String, value: String, unit: String, color: Color) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .width(100.dp)
            .height(60.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = value, color = color, fontWeight = FontWeight.Bold)
            Text(text = "$title $unit", fontSize = 12.sp)
        }
    }
}

// Box promo dengan gambar (untuk list penawaran lama)
@Composable
fun PromoBox(title: String) {
    val imageRes = when (title) {
        "Promo A" -> R.drawable.promo_a
        "Promo B" -> R.drawable.promo_b
        "Promo C" -> R.drawable.promo_c
        "Promo D" -> R.drawable.promo_d
        "Spotify Premium" -> R.drawable.spotify_premium
        "Netflix Mobile" -> R.drawable.netflix_mobile
        "YouTube Tanpa Iklan" -> R.drawable.youtube_tanpa_iklan
        "Asuransi Digital" -> R.drawable.asuransi_digital
        "Pulsa Darurat" -> R.drawable.pulsa_darurat
        "Kuota Family" -> R.drawable.kuota_family
        "Promo Eksklusif" -> R.drawable.promo_eksklusif
        "Diskon Hari Ini" -> R.drawable.diskon_hari_ini
        "Penawaran Pribadi" -> R.drawable.penawaran_pribadi
        else -> R.drawable.default_promo
    }

    Card(
        modifier = Modifier
            .padding(end = 12.dp)
            .size(width = 170.dp, height = 150.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}

// Navigasi bawah (bottom nav) untuk pindah antar halaman utama
@Composable
fun BottomNav(modifier: Modifier = Modifier, navController: NavController) {
    val navBackStackEntry = navController.currentBackStackEntryAsState().value
    val currentRoute = navBackStackEntry?.destination?.route
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(70.dp)
            .padding(bottom = 4.dp, top = 0.dp)
            .offset(y = (-4).dp)
            .background(Color.White)
            .shadow(4.dp, shape = RoundedCornerShape(0.dp))
            .zIndex(1f),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        NavItem("🏠", "Beranda", currentRoute == "dashboard") { navController.navigate("dashboard") }
        NavItem("🎁", "POIN", currentRoute == "poin") { navController.navigate("poin") }
        NavItem("🛒", "Belanja", currentRoute == "belanja") { navController.navigate("belanja") }
        NavItem("☰", "Menu", false) { }
    }
}

// Item navigasi bawah (icon + label)
@Composable
fun NavItem(icon: String, label: String, selected: Boolean, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Text(icon, fontSize = 18.sp)
        Text(
            label,
            fontSize = 14.sp,
            color = if (selected) Color(0xFF5C2136) else Color.Gray,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

// Halaman penukaran poin reward
@Composable
fun PoinPage(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        // Header info jumlah poin & status member
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    "Kamu punya 2000 poin untuk ditukar 🎁",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1A1A)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("👑", fontSize = 16.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        "Gold",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFDAA520),
                        fontSize = 14.sp
                    )
                }
            }
            Icon(
                Icons.Default.Notifications,
                contentDescription = "Notification",
                tint = Color(0xFF666666)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Tab kategori reward & tombol urutkan
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Category tabs
                    Row(modifier = Modifier.weight(1f)) {
                        listOf("E-commerce", "Transport", "Digital Goods", "Food Delivery").forEachIndexed { index, label ->
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(end = if (index < 3) 20.dp else 0.dp)
                            ) {
                                Text(
                                    label,
                                    color = if (index == 0) Color(0xFFE53E3E) else Color(0xFF666666),
                                    fontWeight = if (index == 0) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 12.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                if (index == 0) {
                                    Box(
                                        modifier = Modifier
                                            .height(2.dp)
                                            .width(60.dp)
                                            .background(Color(0xFFE53E3E), shape = RoundedCornerShape(1.dp))
                                    )
                                } else {
                                    Spacer(modifier = Modifier.height(2.dp))
                                }
                            }
                        }
                    }

                    // Sort button
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { }
                    ) {
                        Text(
                            "Urutkan",
                            color = Color(0xFFE53E3E),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("↕️", fontSize = 12.sp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // List reward yang bisa ditukar
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                ImprovedRewardItem(
                    image = painterResource(id = R.drawable.shopeepay),
                    title = "Diskon ShopeePay 50%",
                    points = "900 POIN",
                    available = true
                )
            }
            item {
                ImprovedRewardItem(
                    image = painterResource(id = R.drawable.poco),
                    title = "Poco Redmi 9 Pro",
                    points = "600 POIN",
                    available = true
                )
            }
            item {
                ImprovedRewardItem(
                    image = painterResource(id = R.drawable.samsung),
                    title = "Samsung Air Headset Galaxy S19+",
                    points = "17000 POIN",
                    available = false
                )
            }
            item {
                ImprovedRewardItem(
                    image = painterResource(id = R.drawable.grab),
                    title = "Voucher Grab Rp 20.000",
                    points = "500 POIN",
                    available = true
                )
            }
            item {
                ImprovedRewardItem(
                    image = painterResource(id = R.drawable.gojek),
                    title = "Gojek GoPay Cashback",
                    points = "750 POIN",
                    available = true
                )
            }
            item {
                ImprovedRewardItem(
                    image = painterResource(id = R.drawable.telkomsel),
                    title = "Kuota Internet Telkomsel 10GB",
                    points = "1200 POIN",
                    available = true
                )
            }
            item {
                ImprovedRewardItem(
                    image = painterResource(id = R.drawable.blibli),
                    title = "Diskon Blibli 15%",
                    points = "800 POIN",
                    available = true
                )
            }
            item {
                ImprovedRewardItem(
                    image = painterResource(id = R.drawable.ovopoints),
                    title = "OVO Points 10.000",
                    points = "950 POIN",
                    available = true
                )
            }
            item {
                ImprovedRewardItem(
                    image = painterResource(id = R.drawable.pln),
                    title = "Token Listrik Rp 50.000",
                    points = "1800 POIN",
                    available = false
                )
            }

            // Add bottom padding for navigation
            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
fun ModernPromoCard(title: String) {
    val imageRes = when (title) {
        "Promo A" -> R.drawable.promo_a
        "Promo B" -> R.drawable.promo_b
        "Promo C" -> R.drawable.promo_c
        "Promo D" -> R.drawable.promo_d
        "Spotify Premium" -> R.drawable.spotify_premium
        "Netflix Mobile" -> R.drawable.netflix_mobile
        "YouTube Tanpa Iklan" -> R.drawable.youtube_tanpa_iklan
        "Asuransi Digital" -> R.drawable.asuransi_digital
        "Pulsa Darurat" -> R.drawable.pulsa_darurat
        "Kuota Family" -> R.drawable.kuota_family
        "Promo Eksklusif" -> R.drawable.promo_eksklusif
        "Diskon Hari Ini" -> R.drawable.diskon_hari_ini
        "Penawaran Pribadi" -> R.drawable.penawaran_pribadi
        else -> R.drawable.default_promo
    }

    Card(
        modifier = Modifier
            .width(200.dp)
            .height(140.dp)
            .clickable { },
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}








// Item reward di halaman poin
@Composable
fun ImprovedRewardItem(
    image: Painter,
    title: String,
    points: String,
    available: Boolean = true
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = available) { },
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (available) Color.White else Color(0xFFF5F5F5)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Image
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(8.dp))
            ) {
                Image(
                    painter = image,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    alpha = if (available) 1f else 0.6f
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Content
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = if (available) Color(0xFF1A1A1A) else Color(0xFF999999),
                    lineHeight = 20.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        points,
                        color = if (available) Color(0xFFE53E3E) else Color(0xFF999999),
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                    if (!available) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Poin tidak cukup",
                            color = Color(0xFF999999),
                            fontSize = 11.sp,
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                        )
                    }
                }
            }

            // Action button
            if (available) {
                Button(
                    onClick = { },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFE53E3E)
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.height(36.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    Text(
                        "Tukar",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            } else {
                Box(
                    modifier = Modifier
                        .height(36.dp)
                        .background(
                            Color(0xFFE0E0E0),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Tukar",
                        color = Color(0xFF999999),
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

@Composable
fun BelanjaPage(navController: NavController) {
    val categories = listOf("Internet", "Telpon", "SMS", "Roaming")
    var activeCategory by remember { mutableStateOf("Internet") }
    var searchQuery by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8F9FA))
                .padding(horizontal = 20.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(32.dp))

                // Header Section
                Column {
                    Text(
                        "Pilih Paket Terbaikmu! 📱",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 24.sp,
                        color = Color(0xFF1A1A1A),
                        lineHeight = 32.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Temukan paket yang sesuai dengan kebutuhanmu",
                        fontSize = 14.sp,
                        color = Color(0xFF666666)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Search Bar
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = {
                            Text(
                                "Cari paket favoritmu...",
                                color = Color(0xFF999999),
                                fontSize = 14.sp
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = null,
                                tint = Color(0xFF666666)
                            )
                        },
                        shape = RoundedCornerShape(20.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = Color.Transparent,
                            focusedBorderColor = Color(0xFFE53E3E),
                            unfocusedContainerColor = Color.White,
                            focusedContainerColor = Color.White
                        ),
                        singleLine = true
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Category Tabs
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(modifier = Modifier.weight(1f)) {
                                categories.forEachIndexed { index, category ->
                                    CategoryTab(
                                        title = category,
                                        isSelected = activeCategory == category,
                                        onClick = { activeCategory = category },
                                        modifier = Modifier.padding(end = if (index < categories.size - 1) 24.dp else 0.dp)
                                    )
                                }
                            }

                            Text(
                                "Lihat Semua",
                                color = Color(0xFFE53E3E),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.clickable { }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Featured Packages Section
                Text(
                    "Paket Pilihan ⭐",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color(0xFF1A1A1A)
                )

                Spacer(modifier = Modifier.height(12.dp))
            }

            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(horizontal = 4.dp)
                ) {
                    items(getPackagesByCategory(activeCategory)) { packageData ->
                        ModernPackageCard(
                            navController = navController,
                            title = packageData.title,
                            quota = packageData.quota,
                            price = packageData.price,
                            originalPrice = packageData.originalPrice,
                            isPopular = packageData.isPopular,
                            benefits = packageData.benefits
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Additional packages section
                Text(
                    "Paket Lainnya 📋",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color(0xFF1A1A1A)
                )

                Spacer(modifier = Modifier.height(12.dp))
            }

            items(getAdditionalPackages(activeCategory)) { packageData ->
                CompactPackageCard(
                    navController = navController,
                    title = packageData.title,
                    quota = packageData.quota,
                    price = packageData.price,
                    validity = packageData.validity
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            item {
                Spacer(modifier = Modifier.height(100.dp)) // Bottom padding for nav
            }
        }
    }
}

@Composable
fun CategoryTab(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.clickable { onClick() }
    ) {
        Text(
            title,
            color = if (isSelected) Color(0xFFE53E3E) else Color(0xFF666666),
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        if (isSelected) {
            Box(
                modifier = Modifier
                    .height(3.dp)
                    .width(24.dp)
                    .background(
                        Color(0xFFE53E3E),
                        shape = RoundedCornerShape(2.dp)
                    )
            )
        } else {
            Spacer(modifier = Modifier.height(3.dp))
        }
    }
}

@Composable
fun ModernPackageCard(
    navController: NavController,
    title: String,
    quota: String,
    price: String,
    originalPrice: String? = null,
    isPopular: Boolean = false,
    benefits: List<String> = emptyList()
) {
    Card(
        modifier = Modifier
            .width(200.dp)
            .height(280.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isPopular) Color(0xFFE53E3E) else Color.White
        )
    ) {
        Box {
            if (isPopular) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .background(
                            Color.White,
                            shape = RoundedCornerShape(bottomStart = 12.dp, topEnd = 20.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        "POPULER",
                        color = Color(0xFFE53E3E),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Icon placeholder
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .background(
                            if (isPopular) Color.White.copy(alpha = 0.2f) else Color(0xFFF0F0F0),
                            shape = RoundedCornerShape(16.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text("📶", fontSize = 24.sp)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    color = if (isPopular) Color.White else Color(0xFF1A1A1A),
                    lineHeight = 22.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    quota,
                    color = if (isPopular) Color.White else Color(0xFFE53E3E),
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 28.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    "30 hari",
                    fontSize = 12.sp,
                    color = if (isPopular) Color.White.copy(alpha = 0.8f) else Color(0xFF666666)
                )

                Spacer(modifier = Modifier.weight(1f))

                // Price section
                if (originalPrice != null) {
                    Text(
                        originalPrice,
                        fontSize = 12.sp,
                        color = if (isPopular) Color.White.copy(alpha = 0.7f) else Color(0xFF999999),
                        textDecoration = TextDecoration.LineThrough
                    )
                }

                Text(
                    price,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = if (isPopular) Color.White else Color(0xFF1A1A1A)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        val cleanPrice = price.replace("Rp ", "").replace(".", "")
                        val encodedTitle = java.net.URLEncoder.encode(title, "UTF-8")
                        val encodedQuota = java.net.URLEncoder.encode(quota, "UTF-8")
                        val encodedPrice = java.net.URLEncoder.encode(cleanPrice, "UTF-8")
                        navController.navigate("package_detail/$encodedTitle/$encodedQuota/$encodedPrice")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isPopular) Color.White else Color(0xFFE53E3E)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        "Pilih Paket",
                        color = if (isPopular) Color(0xFFE53E3E) else Color.White,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
fun CompactPackageCard(
    navController: NavController,
    title: String,
    quota: String,
    price: String,
    validity: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                val cleanPrice = price.replace("Rp ", "").replace(".", "")
                val encodedTitle = java.net.URLEncoder.encode(title, "UTF-8")
                val encodedQuota = java.net.URLEncoder.encode(quota, "UTF-8")
                val encodedPrice = java.net.URLEncoder.encode(cleanPrice, "UTF-8")
                navController.navigate("package_detail/$encodedTitle/$encodedQuota/$encodedPrice")
            },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        Color(0xFFF0F0F0),
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text("📊", fontSize = 20.sp)
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Color(0xFF1A1A1A)
                )
                Text(
                    validity,
                    fontSize = 12.sp,
                    color = Color(0xFF666666)
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    quota,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color(0xFFE53E3E)
                )
                Text(
                    price,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF1A1A1A)
                )
            }
        }
    }
}

@Composable
fun PackageDetailPage(
    navController: NavController,
    packageTitle: String,
    packageQuota: String,
    packagePrice: String
) {
    val formattedPrice = "Rp ${packagePrice.replace(Regex("[^\\d]"), "").toLongOrNull()?.let {
        java.text.DecimalFormat("#,###").format(it).replace(",", ".")
    } ?: packagePrice}"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
    ) {
        // Header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp)
        ) {
            Text(
                "←",
                fontSize = 24.sp,
                modifier = Modifier.clickable { navController.popBackStack() }
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                "Detail Paket",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A1A1A)
            )
        }

        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                // Package Info Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE53E3E)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("📶", fontSize = 48.sp)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            packageTitle,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            packageQuota,
                            fontSize = 36.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                        Text(
                            "Berlaku 30 hari",
                            fontSize = 14.sp,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                }
            }

            item {
                // Benefits Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            "Benefit Paket",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color(0xFF1A1A1A)
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        val benefits = listOf(
                            "✅ Kuota utama $packageQuota",
                            "✅ Gratis akses aplikasi populer",
                            "✅ Kecepatan hingga 4G+",
                            "✅ Berlaku 24 jam",
                            "✅ Tanpa FUP"
                        )

                        benefits.forEach { benefit ->
                            Text(
                                benefit,
                                fontSize = 14.sp,
                                color = Color(0xFF333333),
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                        }
                    }
                }
            }

            item {
                // Terms Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            "Syarat & Ketentuan",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color(0xFF1A1A1A)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            "• Paket akan aktif dalam 1x24 jam\n• Tidak dapat digabung dengan promo lain\n• Kuota hangus jika masa aktif berakhir\n• Berlaku untuk semua jaringan",
                            fontSize = 12.sp,
                            color = Color(0xFF666666),
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }

        // Bottom Purchase Section
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Total Pembayaran", fontSize = 12.sp, color = Color(0xFF666666))
                        Text(formattedPrice, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1A1A1A))
                    }
                    Button(
                        onClick = { navController.navigate("payment_method/$packagePrice") },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53E3E)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.height(48.dp)
                    ) {
                        Text("Beli Sekarang", color = Color.White, fontWeight = FontWeight.Medium)
                    }
                }
            }
        }
    }
}

@Composable
fun PaymentMethodPage(navController: NavController, total: String) {
    var selectedMethod by remember { mutableStateOf("LinkAja") }

    val formattedTotal = "Rp ${total.replace(Regex("[^\\d]"), "").toLongOrNull()?.let {
        java.text.DecimalFormat("#,###").format(it).replace(",", ".")
    } ?: total}"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
    ) {
        // Header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp)
        ) {
            Text(
                "←",
                fontSize = 24.sp,
                modifier = Modifier.clickable { navController.popBackStack() }
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                "Metode Pembayaran",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A1A1A)
            )
        }

        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                // Pulsa Section
                Text(
                    "Pembayaran di MyTelkomsel",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color(0xFF1A1A1A)
                )
                Spacer(modifier = Modifier.height(8.dp))

                PaymentMethodItem(
                    icon = "💳",
                    title = "Pulsa",
                    subtitle = "Rp 16.500",
                    isSelected = selectedMethod == "Pulsa",
                    onClick = { selectedMethod = "Pulsa" }
                )

                PaymentMethodItem(
                    icon = "🔗",
                    title = "LinkAja",
                    subtitle = formattedTotal,
                    isSelected = selectedMethod == "LinkAja",
                    onClick = { selectedMethod = "LinkAja" }
                )
            }

            item {
                Text(
                    "E-Wallet",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color(0xFF1A1A1A)
                )
                Spacer(modifier = Modifier.height(8.dp))

                PaymentMethodItem(
                    icon = "💙",
                    title = "Gopay",
                    subtitle = "",
                    isSelected = selectedMethod == "Gopay",
                    onClick = { selectedMethod = "Gopay" }
                )

                PaymentMethodItem(
                    icon = "💜",
                    title = "OVO",
                    subtitle = "",
                    isSelected = selectedMethod == "OVO",
                    onClick = { selectedMethod = "OVO" }
                )

                PaymentMethodItem(
                    icon = "🔗",
                    title = "LinkAja",
                    subtitle = "",
                    isSelected = selectedMethod == "LinkAja2",
                    onClick = { selectedMethod = "LinkAja2" }
                )
            }
        }

        // Bottom Section
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Total", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text(formattedTotal, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFFE53E3E))
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { navController.navigate("payment_success/$selectedMethod/$total") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53E3E)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Konfirmasi & Bayar", color = Color.White, fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}

@Composable
fun PaymentMethodItem(
    icon: String,
    title: String,
    subtitle: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFFFFEBEE) else Color.White
        ),
        border = if (isSelected) BorderStroke(2.dp, Color(0xFFE53E3E)) else null,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(icon, fontSize = 24.sp)
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    title,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    color = Color(0xFF1A1A1A)
                )
                if (subtitle.isNotEmpty()) {
                    Text(
                        subtitle,
                        fontSize = 12.sp,
                        color = Color(0xFF666666)
                    )
                }
            }
            RadioButton(
                selected = isSelected,
                onClick = onClick,
                colors = RadioButtonDefaults.colors(selectedColor = Color(0xFFE53E3E))
            )
        }
    }
}

@Composable
fun PaymentSuccessPage(navController: NavController, method: String, total: String) {
    val formattedTotal = "Rp ${total.replace(Regex("[^\\d]"), "").toLongOrNull()?.let {
        java.text.DecimalFormat("#,###").format(it).replace(",", ".")
    } ?: total}"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Success Icon
        Box(
            modifier = Modifier
                .size(120.dp)
                .background(Color(0xFF4CAF50), shape = RoundedCornerShape(60.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text("✓", fontSize = 48.sp, color = Color.White)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            "Pembayaran Berhasil!",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A1A1A),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            "Transaksi kamu telah berhasil diproses",
            fontSize = 14.sp,
            color = Color(0xFF666666),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Transaction Details
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    "Detail Transaksi",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color(0xFF1A1A1A)
                )

                Spacer(modifier = Modifier.height(16.dp))

                TransactionDetailRow("Metode Pembayaran", method)
                TransactionDetailRow("Total Pembayaran", formattedTotal)
                TransactionDetailRow("Status", "Berhasil")
                TransactionDetailRow("Waktu", "25 Jun 2025, 14:30")
                TransactionDetailRow("ID Transaksi", "TXN${System.currentTimeMillis().toString().takeLast(8)}")
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = { /* TODO: Download receipt */ },
                modifier = Modifier.weight(1f),
                border = BorderStroke(1.dp, Color(0xFFE53E3E)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Unduh Struk", color = Color(0xFFE53E3E))
            }

            Button(
                onClick = { navController.navigate("dashboard") },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53E3E)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Kembali ke Beranda", color = Color.White)
            }
        }
    }
}

@Composable
fun TransactionDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            label,
            fontSize = 14.sp,
            color = Color(0xFF666666)
        )
        Text(
            value,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF1A1A1A)
        )
    }
}

// Data classes and helper functions
data class PackageData(
    val title: String,
    val quota: String,
    val price: String,
    val originalPrice: String? = null,
    val validity: String = "30 hari",
    val isPopular: Boolean = false,
    val benefits: List<String> = emptyList()
)

fun getPackagesByCategory(category: String): List<PackageData> {
    return when (category) {
        "Internet" -> listOf(
            PackageData("Paket Super", "25 GB", "Rp 55.000", "Rp 75.000", isPopular = true),
            PackageData("Paket Unlimited", "∞ GB", "Rp 99.000"),
            PackageData("Paket Hemat", "10 GB", "Rp 25.000")
        )
        "Telpon" -> listOf(
            PackageData("Nelpon Sepuasnya", "∞ Min", "Rp 45.000", isPopular = true),
            PackageData("Paket 100 Menit", "100 Min", "Rp 20.000"),
            PackageData("Paket 500 Menit", "500 Min", "Rp 35.000")
        )
        "SMS" -> listOf(
            PackageData("SMS Unlimited", "∞ SMS", "Rp 15.000", isPopular = true),
            PackageData("100 SMS", "100 SMS", "Rp 5.000"),
            PackageData("500 SMS", "500 SMS", "Rp 12.000")
        )
        "Roaming" -> listOf(
            PackageData("Roaming Asia", "5 GB", "Rp 150.000", isPopular = true),
            PackageData("Roaming Global", "3 GB", "Rp 200.000"),
            PackageData("Roaming ASEAN", "2 GB", "Rp 100.000")
        )
        else -> emptyList()
    }
}

fun getAdditionalPackages(category: String): List<PackageData> {
    return when (category) {
        "Internet" -> listOf(
            PackageData("Internet Malam", "20 GB", "Rp 20.000", validity = "30 hari"),
            PackageData("Weekend Special", "15 GB", "Rp 30.000", validity = "30 hari"),
            PackageData("Student Package", "12 GB", "Rp 35.000", validity = "30 hari")
        )
        "Telpon" -> listOf(
            PackageData("Telpon Lokal", "200 Min", "Rp 25.000", validity = "30 hari"),
            PackageData("Telpon Internasional", "50 Min", "Rp 50.000", validity = "30 hari")
        )
        else -> listOf(
            PackageData("Paket Combo", "8 GB + 100 Min", "Rp 40.000", validity = "30 hari"),
            PackageData("Paket Keluarga", "50 GB", "Rp 120.000", validity = "30 hari")
        )
    }
}