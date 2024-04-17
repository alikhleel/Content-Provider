package com.example.contentprovider

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import com.example.contentprovider.ui.theme.ContentProviderTheme
import android.Manifest
import android.content.ContentUris
import android.os.Build
import android.provider.MediaStore
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import java.util.Calendar

class MainActivity : ComponentActivity() {
    val viewModel: ContentProviderViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.READ_MEDIA_IMAGES
            ),
            0
        )
        super.onCreate(savedInstanceState)
        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
        )
        val dateDuration = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, -10)
        }.timeInMillis

        val selection = "${MediaStore.Images.Media.DATE_TAKEN} >= ?"

        contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            arrayOf(dateDuration.toString()),
            "${MediaStore.Images.Media.DATE_TAKEN} DESC"
        )?.use {
            val idColumn = it.getColumnIndex(MediaStore.Images.Media._ID)
            val displayNameColumn = it.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME)
            val images = mutableListOf<ContentProviderDataItem>()
            while (it.moveToNext()) {
                val id = it.getLong(idColumn)
                val displayName = it.getString(displayNameColumn)
                val contentUri = ContentUris.withAppendedId(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    id
                )
                images.add(ContentProviderDataItem(id, displayName, contentUri))
            }
            viewModel.updateImages(images)
        }
        setContent {
            ContentProviderTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = 128.dp)
                    ) {
                        items(viewModel.imagesState.value) { image ->
                            AsyncImage(
                                model = image.contentUri,
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(8.dp),
                                contentScale = ContentScale.Crop,
                            )
                        }
                        items(viewModel.imagesState.value) { image ->
                            AsyncImage(
                                model = image.contentUri,
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(8.dp),
                                contentScale = ContentScale.Crop,
                            )
                        }
                        item { Spacer(modifier = Modifier.height(16.dp)) }
                    }
//                    LazyColumn {
//                        items(viewModel.imagesState.value) { image ->
//                            AsyncImage(
//                                model = image.contentUri,
//                                contentDescription = null,
//                                modifier = Modifier.fillMaxWidth(),
//                                contentScale = ContentScale.Crop
//                            )
//                        }
//
//                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ContentProviderTheme {
        Greeting("Android")
    }
}