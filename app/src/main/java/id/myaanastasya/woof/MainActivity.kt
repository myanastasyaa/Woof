package id.myaanastasya.woof

import id.myaanastasya.woof.ui.theme.WoofTheme
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
/*Untuk dapat menggunakan import Icon Filled Expand, harus menambahkan
implementation("androidx.compose.material:material-icons-extended") pada build.gradle.kts(:app)*/
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import id.myaanastasya.woof.data.Dog
import id.myaanastasya.woof.data.dogs

//Aktivitas Utama pada aplikasi 
class MainActivity : ComponentActivity() {
    //inisialisasi awal saat aktivitas dibuat
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //mengatur tata letak (layout) dari aktivitas
        setContent {
            WoofTheme {
                // A surface container using the 'background' color from the theme
                // mengatur agar konten mengisi seluruh ruang yang ada
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    //komponen utama aplikasi
                    WoofApp()
                }
            }
        }
    }
}

/**
 * Composable that displays an app bar and a list of dogs.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WoofApp() {
    //membuat tata letak dasar
    Scaffold(
        //bagian atas
        topBar = {
            //mengatur komponen topBar
            WoofTopAppBar()
        }
    ) { it ->
        //tampilan vertikal scrollable list
        LazyColumn(contentPadding = it) {
            //memanggil items dalam daftar dogs
            items(dogs) {
                //menampilkan setiap item individu dalam daftar anjing (dogs)
                DogItem(
                    dog = it,
                    //menambahkan padding pada tiap item yang ditampilkan
                    modifier = Modifier.padding(dimensionResource(R.dimen.padding_small))
                )
            }
        }
    }
}

/**
 * Composable that displays a list item containing a dog icon and their information.
 *
 * @param dog contains the data that populates the list item
 * @param modifier modifiers to set to this composable
 */
@Composable
fun DogItem(
    dog: Dog,
    modifier: Modifier = Modifier
) {
    // membuat variabel yang dapat diubah nilainya (mutable) dan mengingat (menyimpan) nilai variabel
    var expanded by remember { mutableStateOf(false) }
    //wadah visual
    Card(
        modifier = modifier
    ) {
        //layout secara vertikal
        Column(
            modifier = Modifier
                //animasi perubahan ukuran konten ketika ada perubahan pada konten di dalamnya
                .animateContentSize(
                    //efek pegas atau per atau bump
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioNoBouncy,
                        stiffness = Spring.StiffnessMedium
                    )
                )
        ) {
            //mengatur tata letak secara horizontal
            Row(
                modifier = Modifier
                    //lebar penuh menyesuaikan layar
                    .fillMaxWidth()
                    //memberikan jarak ke sekeliling row
                    .padding(dimensionResource(R.dimen.padding_small))
            ) {
                //menampilkan gambar anjing yang disimpan pada package drawable
                DogIcon(dog.imageResourceId)
                //menampilkan nama dan umur anjing yang ada pada file Dog.kt
                DogInformation(dog.name, dog.age)
                //memberikan ruang kosong dengan bobot 1 (mengisi sebanyak mungkin ruang yang tersedia) di antara DogInformation dan DogItemButton
                Spacer(Modifier.weight(1f))
                //mengontrol status expanded
                DogItemButton(
                    expanded = expanded,
                    //saat onclick, nilai expanded akan diubah menjadi kebalikannya
                    onClick = { expanded = !expanded },
                )
            }
            if (expanded) {
                //apabila expanded, maka akan menampilkan detail hobi dari anjing
                DogHobby(
                    dog.hobbies, modifier = Modifier.padding(
                        start = dimensionResource(R.dimen.padding_medium),
                        top = dimensionResource(R.dimen.padding_small),
                        bottom = dimensionResource(R.dimen.padding_medium),
                        end = dimensionResource(R.dimen.padding_medium)
                    )
                )
            }
        }
    }
}

/**
 * Composable that displays a button that is clickable and displays an expand more or an expand less
 * icon.
 *
 * @param expanded represents whether the expand more or expand less icon is visible
 * @param onClick is the action that happens when the button is clicked
 * @param modifier modifiers to set to this composable
 */
@Composable
private fun DogItemButton(
    //menunjukkan apakah elemen DogItem sedang diperluas atau tidak
    expanded: Boolean,
    //tindakan yang akan dijalankan saat tombol ditekan
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    //membuat icon menjadi clickable
    IconButton(
        //Ketika tombol ditekan, aksi yang diberikan pada saat pemanggilan fungsi DogItemButton akan dijalankan
        onClick = onClick,
        modifier = modifier
    ) {
        //menampilkan icon
        Icon(
            /*bagian yang menentukan ikon yang akan ditampilkan berdasarkan status expanded.
            Jika expanded adalah true, maka akan menampilkan ikon ExpandLess,
            jika tidak, maka akan menampilkan ikon ExpandMore.*/
            imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
            //deskripsi konten icon
            contentDescription = stringResource(R.string.expand_button_content_description),
            //mengatur warna icon
            tint = MaterialTheme.colorScheme.secondary
        )
    }
}

/**
 * Composable that displays a Top Bar with an icon and text.
 *
 * @param modifier modifiers to set to this composable
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
//menampilkan top app bar
fun WoofTopAppBar(modifier: Modifier = Modifier) {
    //menerapkan AppBar di tengah atas aplikasi
    CenterAlignedTopAppBar(
        //menunjukkan judul atau konten di sebelah kiri
        title = {
            //tata letak secara horizontal
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                //menampilkan gambar
                Image(
                    modifier = Modifier
                        .size(dimensionResource(R.dimen.image_size))
                        .padding(dimensionResource(R.dimen.padding_small)),
                    //mengambil gambar dari resource drawable sebagai painter
                    painter = painterResource(R.drawable.ic_woof_logo),

                    // Content Description is not needed here - image is decorative, and setting a
                    // null content description allows accessibility services to skip this element
                    // during navigation.

                    contentDescription = null
                )
                //menampilkan teks
                Text(
                    //mengambil teks dari resource values sebagai string
                    text = stringResource(R.string.app_name),
                    style = MaterialTheme.typography.displayLarge
                )
            }
        },
        modifier = modifier
    )
}

/**
 * Composable that displays a photo of a dog.
 *
 * @param dogIcon is the resource ID for the image of the dog
 * @param modifier modifiers to set to this composable
 */
@Composable
//menampilkan icon anjing
fun DogIcon(
    //mengambil referensi drawable (ikon anjing) sebagai argumen
    @DrawableRes dogIcon: Int,
    modifier: Modifier = Modifier
) {
    //menampilkan gambar
    Image(
        modifier = modifier
            .size(dimensionResource(R.dimen.image_size))
            .padding(dimensionResource(R.dimen.padding_small))
            //memberikan bentuk clip pada gambar / rounder pada tiap sudut gambar
            .clip(MaterialTheme.shapes.small),
        //memastikan gambar dipotong atau diubah skala sehingga cocok dengan batas-batas yang diberikan tanpa deformasi
        contentScale = ContentScale.Crop,
        //menggunakan referensi drawable
        painter = painterResource(dogIcon),

        // Content Description is not needed here - image is decorative, and setting a null content
        // description allows accessibility services to skip this element during navigation.

        contentDescription = null
    )
}

/**
 * Composable that displays a dog's name and age.
 *
 * @param dogName is the resource ID for the string of the dog's name
 * @param dogAge is the Int that represents the dog's age
 * @param modifier modifiers to set to this composable
 */
@Composable
//menampilkan informasi tiap anjing
fun DogInformation(
    //mengambil referensi String (nama dan umur anjing) sebagai argumen
    @StringRes dogName: Int,
    dogAge: Int,
    modifier: Modifier = Modifier
) {
    //tata letak vertikal
    Column(modifier = modifier) {
        //menampilkan teks
        Text(
            //menampilkan teks yang berasal dari dogName
            text = stringResource(dogName),
            style = MaterialTheme.typography.displayMedium,
            modifier = Modifier.padding(top = dimensionResource(R.dimen.padding_small))
        )
        Text(
            //menampilkan teks yang berasal dari dogAge
            text = stringResource(R.string.years_old, dogAge),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

/**
 * Composable that displays a dog's hobbies.
 *
 * @param dogHobby is the resource ID for the text string of the hobby to display
 * @param modifier modifiers to set to this composable
 */
@Composable
//menampilkan hobi dari tiap anjing
fun DogHobby(
    @StringRes dogHobby: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = stringResource(R.string.about),
            style = MaterialTheme.typography.labelSmall
        )
        Text(
            text = stringResource(dogHobby),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

/**
 * Composable that displays what the UI of the app looks like in light theme in the design tab.
 */
@Preview
@Composable
fun WoofPreview() {
    WoofTheme(darkTheme = false) {
        WoofApp()
    }
}

/**
 * Composable that displays what the UI of the app looks like in dark theme in the design tab.
 */
@Preview
@Composable
fun WoofDarkThemePreview() {
    WoofTheme(darkTheme = true) {
        WoofApp()
    }
}