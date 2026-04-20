package com.example.trekkingapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.android.gms.common.SignInButton

@Composable
fun PlayStopButton(modifier: Modifier = Modifier, onClick: () -> Unit = {}, recording: Boolean, photoCount: Int = 0) {

//    ElevatedCard(
//        modifier.padding(20.dp)
//    ) {
        BadgedBox(
            badge = {
                if (photoCount > 0 && recording) {
                    Badge {
                        Text(photoCount.toString())
                    }
                }
            }
        ) {
            FloatingActionButton(
                modifier = Modifier.size(64.dp),
                onClick = {
                    onClick()
                }
            ) {
                Icon(
                    imageVector = if (!recording) Icons.Default.PlayArrow else Icons.Default.Stop,
                    contentDescription = null,
                    modifier = Modifier.size(40.dp),
                )
            }
        }
//    }
}

@Composable
@Preview
fun PlayStopButtonPreviewRecording(){
    PlayStopButton(recording = true, photoCount = 5)
}

@Composable
@Preview
fun PlayStopButtonPreview(){
    PlayStopButton(recording = false, photoCount = 5)
}