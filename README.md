# TC Cell Membership App

Aplikasi Android modern untuk layanan keanggotaan operator seluler dengan fitur lengkap pengelolaan pulsa, poin rewards, dan pembelian paket data.

## Features

### Authentication System
- **Login dengan nomor HP** + verifikasi OTP
- **Auto-fill OTP** untuk demo testing
- **Quick login buttons** dengan nomor predefined
- **Modern UI** dengan gradient background

### Modern Dashboard
- **Enhanced header** dengan gradient aesthetic
- **Dual card layout** untuk pulsa dan poin terpisah
- **Real-time balance** display (Rp 88.800)
- **Interactive poin card** dengan 1250 poin + Gold Member status
- **Quick actions** - 4 tombol utama dengan icon modern
- **Usage statistics** - Internet, Telpon, SMS dengan visual indicators

### Poin Rewards System
- **Real-time poin exchange** yang beneran berfungsi
- **5 pilihan rewards:**
  - Diskon ShopeePay 50% (500 poin)
  - Voucher Grab Rp 20K (750 poin)
  - OVO Points 10K (900 poin)
  - Token Listrik 50K (1200 poin)
  - Netflix Premium (1500 poin)
- **Auto availability check** berdasarkan current points
- **Success animation** dengan checkmark
- **Instant balance update** setelah exchange

### Package Shopping
- **4 kategori** - Internet, Telpon, SMS, Roaming
- **Search functionality** dengan modern input
- **Featured packages** dengan badge "POPULER"
- **Package detail** lengkap dengan benefits & terms
- **Dynamic content** berdasarkan kategori

### Payment System
- **Multiple payment methods:**
  - Pulsa TC Cell
  - E-wallet (Gopay, OVO, LinkAja)
- **Complete payment flow** - Selection → Detail → Payment → Success
- **Transaction details** dengan ID dan timestamp
- **Receipt download** functionality

### Additional Services
- **Pulsa top-up** (Rp 5K - 100K)
- **Transfer credit** dengan contact picker
- **Usage monitoring** real-time
- **Promo sections** dengan asset gambar

## Tech Stack

- **Language:** Kotlin
- **UI Framework:** Jetpack Compose (100% declarative UI)
- **Navigation:** Navigation Compose dengan type-safe routing
- **Design System:** Material Design 3
- **State Management:** Compose State dengan remember & mutableStateOf
- **Minimum SDK:** API 24 (Android 7.0+)
- **Target SDK:** API 34 (Android 14)

## Architecture

```
TC Cell App
├── Authentication Flow
│   ├── Login Screen
│   └── OTP Verification
├── Dashboard
│   ├── Modern Header (Pulsa + Poin cards)
│   ├── Quick Actions
│   ├── Usage Stats
│   └── Content Sections (Promo cards)
├── Poin System
│   ├── Exchange Dialog
│   └── Rewards Page
├── Shopping Flow
│   ├── Package Browsing
│   ├── Package Detail
│   └── Payment Process
└── Supporting Services
    ├── Pulsa Management
    └── Transfer Credit
```

## Design System

### Color Palette
```kotlin
Primary: #5C2136    // Brand red
Secondary: #E53E3E  // Accent red  
Background: #F8F9FA // Light gray
Surface: #FFFFFF    // White
Text: #1A1A1A      // Dark gray
```

### Components
- **45+ Custom Composables**
- **8 Main Screens**
- **15+ Reactive State Variables**
- **Atomic Design Methodology**

## Getting Started

### Prerequisites
- Android Studio Arctic Fox atau lebih baru
- JDK 8 atau lebih tinggi
- Android SDK API 24+

### Installation
1. Clone repository
```bash
git clone https://github.com/ilomimo/ppb-eas-membership.git
```

2. Buka project di Android Studio

3. Sync Gradle files

4. Run aplikasi di emulator atau device

### Demo Credentials
```
Login Demo:
081112223344 → OTP: 123456
085567889900 → OTP: 654321
Nomor lain → OTP: 111111
```

## How to Use

### 1. Login Flow
- Buka app → Login screen
- Klik "Demo 1" atau "Demo 2" untuk auto-fill nomor
- Kirim OTP → Auto-fill setelah 2 detik
- Verifikasi → Masuk dashboard

### 2. Tukar Poin
- Di dashboard, klik tombol "Tukar" di poin card
- Pilih reward yang diinginkan
- Klik "Tukar Sekarang"
- Lihat animasi success + poin berkurang real-time

### 3. Belanja Paket
- Navigate ke halaman Belanja
- Pilih kategori (Internet/Telpon/SMS/Roaming)
- Klik paket → Lihat detail
- Beli Sekarang → Pilih payment method → Success

### 4. Flow Pembayaran
- Package selection → Detail → Payment method
- Pilih Gopay/OVO/LinkAja/Pulsa
- Konfirmasi & Bayar → Success page

## Project Stats

- **Total Lines of Code:** ~2,500
- **Custom Composables:** 45+
- **Navigation Routes:** 8 screens
- **Drawable Assets:** 15+ promo images
- **State Variables:** 15+ reactive states

## Screenshots

> *Screenshots akan ditambahkan setelah final build*

## Development Notes

### Key Features Implemented
- Real-time poin exchange dengan state management
- Complete authentication flow dengan OTP simulation
- Modern dashboard dengan enhanced UI
- Full payment flow dari selection ke success
- Asset integration dengan promo images
- Navigation dengan parameter passing

### Jetpack Compose Highlights
- Declarative UI untuk better development experience
- State management dengan automatic recomposition
- Modern navigation dengan type-safe routing
- Performance optimization dengan LazyColumn/LazyRow
- Consistent design system implementation

## Academic Project

Project ini dibuat untuk tugas akhir mata kuliah **Pemrograman Perangkat Bergerak** dengan fokus pada:
- Modern Android development dengan Jetpack Compose
- State management dan reactive UI
- Navigation architecture
- Design system implementation
- Real-world app functionality

