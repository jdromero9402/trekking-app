package com.example.trekkingapp.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material3.Icon
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
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(imageVector = Icons.Default.ErrorOutline, contentDescription = "", tint = Color.Red)
        Text(stringResource(message), color = Color.Red)
    }
}

@Preview
@Composable
fun ErrorMessagePreview(){
    ErrorMessage(message = R.string.screen_in_app_camera_no_permission_label)
}