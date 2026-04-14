package com.example.trekkingapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.trekkingapp.R


@Composable
fun ErrorMessage(modifier: Modifier = Modifier, message: Int) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(imageVector = Icons.Default.ErrorOutline, contentDescription = "", tint = Color.Red)
        Text(stringResource(message), color = Color.Red)
    }
}

@Preview
@Composable
fun ErrorMessagePreview(){

    OutlinedCard (
        Modifier
            .fillMaxWidth()
            .fillMaxHeight()
     ) {
        Box(){
            ErrorMessage(message = R.string.screen_in_app_camera_no_permission_label,
                modifier = Modifier.fillMaxSize().align(
                    Alignment.Center

                ))
        }
    }
}