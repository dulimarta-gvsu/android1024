package edu.gvsu.cis357.play1024

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.gvsu.cis357.play1024.ui.theme.Play1024Theme
import kotlin.math.absoluteValue

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContent {
            Play1024Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Game1024(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

suspend fun handleSwipe(scope: PointerInputScope, whenDetected: (Swipe) -> Unit) {
    scope.run {
        var totalDragX = 0f
        var totalDragY = 0f
        detectDragGestures(
            onDragStart = {
                totalDragX = 0f
                totalDragY = 0f
            },
            onDrag = { change, dragAmout ->
                change.consume()
                totalDragX += dragAmout.x
                totalDragY += dragAmout.y
            },
            onDragEnd = {
                val dir =
                    if (totalDragX.absoluteValue > totalDragY.absoluteValue) {
                        if (totalDragX > 0) Swipe.RIGHT else Swipe.LEFT
                    } else {
                        if (totalDragY > 0) Swipe.DOWN else Swipe.UP
                    }
                whenDetected(dir)
            },
        )
    }
}

@Composable
fun Game1024(modifier: Modifier = Modifier) {
    var swipeDirection by remember { mutableStateOf<Swipe?>(null) }
    Column(
        modifier
            .padding(top = 16.dp)
            .pointerInput(Unit) {
                val which = handleSwipe(this) {
                    println("Swipe to $it")
                    swipeDirection = it
                }
            })
    {
        Text(
            "Welcome to <YourName> 1024",
            fontSize = 20.sp
        )
        NumberGrid(4)
        Text(
            "You swipe ${swipeDirection ?: "Unknown"}",
            fontSize = 18.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }

}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NumberGrid(size: Int = 4, modifier: Modifier = Modifier) {
    val cells = (1..size * size).toList()
    FlowRow(maxItemsInEachRow = size, modifier = modifier.padding(16.dp)) {
        cells.forEach {
            Text(
                it.toString(),
                textAlign = TextAlign.Center,
                fontSize = 30.sp,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f)
                    .border(2.dp, Color.Black)
                    .wrapContentHeight()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Play1024Theme {
        Game1024()
    }
}