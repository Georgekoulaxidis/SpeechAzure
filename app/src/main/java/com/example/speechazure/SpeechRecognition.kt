package com.example.speechazure

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import com.example.speechazure.viewModels.SpeechViewModel
import com.microsoft.cognitiveservices.speech.SpeechConfig
import com.microsoft.cognitiveservices.speech.SpeechRecognizer

@Composable
fun SpeechRec(pressed: Boolean, speechViewModel: SpeechViewModel) {
    var recognizer: SpeechRecognizer?

    DisposableEffect(pressed) {
        var initRec = false
        val speechConfig = SpeechConfig.fromSubscription("YOUR_SUBSCRIPTION_KEY", "YOU_REGION")
        speechConfig.speechRecognitionLanguage = "YOUR_LANGUAGE"
        recognizer = SpeechRecognizer(speechConfig)

        recognizer!!.recognizing.addEventListener { _, eventArgs ->
            Log.d("SpeechRecognition", "Intermediate result: ${eventArgs.result.text}")
        }
        recognizer!!.recognized.addEventListener { _, eventArgs ->
            Log.d("SpeechRecognition", "Final result: ${eventArgs.result.text}")
            speechViewModel.setRecognizedText(eventArgs.result.text)
            speechViewModel.getSpeech(eventArgs.result.text)
        }
        recognizer!!.canceled.addEventListener { _, eventArgs ->
            Log.d("SpeechRecognition", "Recognition canceled: ${eventArgs.errorDetails}")
        }
        recognizer!!.sessionStarted.addEventListener { _, _ ->
            Log.d("SpeechRecognition", "Recognition session started")
        }
        recognizer!!.sessionStopped.addEventListener { _, _ ->
            Log.d("SpeechRecognition", "Recognition session stopped")
        }

        if(pressed) {
            initRec = true
            recognizer!!.startContinuousRecognitionAsync().get()
        }

        onDispose {
            if(initRec){
                recognizer!!.stopContinuousRecognitionAsync().get()
                recognizer!!.close()
                recognizer = null
            }
        }
    }

}