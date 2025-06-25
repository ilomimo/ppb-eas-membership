package com.example.mymembershipapp
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.zIndex
import androidx.compose.ui.layout.ContentScale
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                FullDashboardScreen()
            }
        }
    }
}

@Composable
fun FullDashboardScreen() {
    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
        Column(modifier = Modifier.fillMaxHeight()) {
            HeaderSection()
            ContentSection()
        }
        BottomNav(modifier = Modifier.align(Alignment.BottomCenter))
    }
}

@Composable
fun HeaderSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.header_background), // placeholder
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

@Composable
fun ContentSection() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            // Saldo Section
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
                                IconButton(onClick = { /* TODO: tambah pulsa */ }) {
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

            PulsaShortcutRow()

            Spacer(modifier = Modifier.height(12.dp))

            // Box fitur tambahan di bawah pulsa
            FeatureShortcutRow()
            Spacer(modifier = Modifier.height(16.dp))

            // Internet / Telpon / SMS
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

            // Section 1
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Cari Voucher, Yuk!", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text("Lihat Semua", color = Color(0xFF5C2136), fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow {
                items(listOf("Promo A", "Promo B")) {
                    PromoBox(title = it)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Section 2
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Terbaru dari Telkomsel", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text("Lihat Semua", color = Color(0xFF5C2136), fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow {
                items(listOf("Promo C", "Promo D")) {
                    PromoBox(title = it)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Section Tambahan
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Langganan Favorit", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text("Lihat Semua", color = Color(0xFF5C2136), fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow {
                items(listOf("Spotify Premium", "Netflix Mobile", "YouTube Tanpa Iklan")) {
                    PromoBox(title = it)
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
            LazyRow {
                items(listOf("Asuransi Digital", "Pulsa Darurat", "Kuota Family")) {
                    PromoBox(title = it)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Section Tambahan 2
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Spesial Untuk Kamu", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text("Lihat Semua", color = Color(0xFF5C2136), fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow {
                items(listOf("Promo Eksklusif", "Diskon Hari Ini", "Penawaran Pribadi")) {
                    PromoBox(title = it)
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun PulsaShortcutRow() {
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
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(icons[index], fontSize = 20.sp)
                    Text(label, fontSize = 12.sp)
                }
            }
        }
    }
}

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

@Composable
fun PromoBox(title: String) {
    Card(
        modifier = Modifier
            .padding(end = 12.dp)
            .width(170.dp)
            .height(120.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Gambar: $title", fontSize = 12.sp, textAlign = TextAlign.Center)
        }
    }
}

@Composable
fun BottomNav(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(bottom = 8.dp)
            .background(Color.White)
            .shadow(4.dp, shape = RoundedCornerShape(0.dp))            .zIndex(1f),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        NavItem("🏠", "Beranda", true)
        NavItem("🎁", "POIN", false)
        NavItem("🛒", "Belanja", false)
        NavItem("☰", "Menu", false)
    }
}

@Composable
fun NavItem(icon: String, label: String, selected: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(icon, fontSize = 18.sp)
        Text(
            label,
            fontSize = 12.sp,
            color = if (selected) Color(0xFF5C2136) else Color.Gray
        )
    }
}