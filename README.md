Smart Pantry AI

Smart Pantry AI is an Android app that suggests recipes based on ingredients you have in your pantry. It leverages ONNX Runtime for on-device recipe generation and SentencePiece tokenizer for natural language processing.
\
Features

Enter ingredients available in your pantry.

Tokenize input using SentencePiece.

Generate recipe suggestions with an on-device ONNX model.

Fully offline operation—no server required.

Displays decoded input tokens for transparency.

Project Structure

SmartPantryAI/

├─ app/

│  ├─ src/main/java/com/example/smartpantryai/

│  │  ├─ MainActivity.kt

│  │  ├─ OnDeviceRecipeGenerator.kt

│  │  └─ SentencePieceTokenizer.kt

│  ├─ src/main/assets/models/

│  │  ├─ spiece.model/

│  │  └─ flan_t5_small.onnx

│  └─ res/

│     └─ layout, values, etc.

└─ build.gradle

Setup

Clone the repository:

git clone <repository-url>

Open the project in Android Studio.

Add dependencies in build.gradle (Module: app):

dependencies {

    implementation 'ai.djl.sentencepiece:sentencepiece:0.34.0'
    
    implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3'
    
    implementation 'com.microsoft.onnxruntime:onnxruntime:1.15.1'
    
}

Place the models in assets/models/:

spiece.model — SentencePiece tokenizer model.

flan_t5_small.onnx — ONNX model for recipe generation.

Sync the Gradle project.

Usage

Run the app on an Android device or emulator.

Enter pantry ingredients in the text field.

Click Get Recipe Suggestions.

View the decoded input and ONNX-generated recipe output.

Notes

Currently, ONNX output is displayed as raw token IDs.

Integrate SentencePiece decoding for proper human-readable recipes.

The app works fully offline once the assets are included.

License
This project is open-source and free to use for educational purposes.
