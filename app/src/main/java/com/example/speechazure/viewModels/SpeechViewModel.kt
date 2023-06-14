package com.example.speechazure.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.microsoft.cognitiveservices.speech.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SpeechViewModel: ViewModel(){
    //Speech Synthesis variables
    private var speechConfig: SpeechConfig? = null
    private var synthesizer: SpeechSynthesizer? = null


    override fun onCleared() {
        super.onCleared()
        if (synthesizer != null) {
            synthesizer!!.close()
        }
        if(speechConfig != null) {
            speechConfig!!.close()
        }
    }

    private var myJob: Job? = null

    private val _recText = MutableStateFlow("Press to speak")
    val recText = _recText.asStateFlow()

    fun setRecognizedText(text: String) {
        _recText.value = text
    }

    fun getSpeech(q: String){
        myJob?.cancel()
        myJob = viewModelScope.launch(Dispatchers.IO){
            if(synthesizer != null){
                synthesizer!!.StopSpeakingAsync().get()
                synthesizer!!.close()
            }

            if(speechConfig != null){
                speechConfig!!.close()
            }

            speechConfig = SpeechConfig.fromSubscription("YOUR_SUBSCRIPTION_KEY", "YOUR_REGION")
            speechConfig?.speechSynthesisLanguage = "YOUR_LANGUAGE"
            speechConfig?.speechSynthesisVoiceName = "VOICE_NAME_MODEL";
            synthesizer = SpeechSynthesizer(speechConfig)

            val result = synthesizer?.SpeakText(q)

            if (result?.reason == ResultReason.SynthesizingAudioCompleted) {
                //Do something when synthesized audio is complete
            } else if (result?.reason == ResultReason.Canceled) {
                val cancellationDetails = SpeechSynthesisCancellationDetails.fromResult(result)
                Log.e("SpeechSynthesizer", "Speech synthesis canceled: ${cancellationDetails.errorDetails}")
            }
        }
    }


}