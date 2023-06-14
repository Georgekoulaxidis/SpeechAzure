package com.example.speechazure.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.speechazure.R
import com.example.speechazure.SpeechRec
import com.example.speechazure.ui.theme.*
import com.example.speechazure.viewModels.SpeechViewModel

@Composable
fun HomeScreen() {
    val interactionSource = remember { MutableInteractionSource() } // collect change state for our button
    val viewModel = viewModel<SpeechViewModel>()
    val pressed by interactionSource.collectIsPressedAsState() // Collect pressed state
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(charcoal),
    ) {
        val modifier = Modifier
            .fillMaxHeight()
            .align(Alignment.Center)
        MicComponent(modifier, pressed, interactionSource, viewModel)
        SpeechRec(pressed = pressed, viewModel)
    }
}

@Composable
fun MicComponent(modifier: Modifier, pressed:Boolean, interactionSource: MutableInteractionSource, viewModel: SpeechViewModel) {
    val color = if (pressed) Purple700 else white
    val border: BorderStroke? = null
    val text = viewModel.recText.collectAsState().value
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Button(onClick = { println("Button press")},
        modifier = Modifier
            .background(Color.Transparent),
        interactionSource = interactionSource,
        border = border,
        elevation = null,
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent,
        disabledBackgroundColor = Color.Transparent,
        disabledContentColor = Color.Transparent,
        contentColor = Color.Transparent)) {
            Icon(imageVector = ImageVector.vectorResource(id = R.drawable.mic_ic),
            contentDescription = "Microphone icon",
            modifier = Modifier.size(100.dp, 100.dp),
            tint = color)
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = text
        )
    }
}