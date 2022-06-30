package com.debduttapanda.wormanimation

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.toSize
import com.debduttapanda.wormanimation.ui.theme.WormAnimationTheme
import kotlin.math.min

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WormAnimationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ){
                        val config = LocalConfiguration.current
                        val density = LocalDensity.current
                        var alignment by remember { mutableStateOf(Alignment.TopStart) }
                        var phase by remember { mutableStateOf(-1) }
                        var size by remember { mutableStateOf(IntSize(0,0)) }
                        var targetSize by remember { mutableStateOf(IntSize((64*density.density).toInt(),(64*density.density).toInt())) }
                        val animatedSize = animateIntSizeAsState(
                            targetValue = IntSize(
                                (targetSize.width/density.density).toInt(),
                                (targetSize.height/density.density).toInt(),
                            ),
                            animationSpec = tween(
                                durationMillis = 1000,
                                delayMillis = 500,
                                easing = LinearEasing
                            ),
                            finishedListener = {
                                phase = (phase + 1)%8
                            }
                        )
                        LaunchedEffect(key1 = phase){
                            val defaultSize = (64*density.density).toInt()
                            when(phase){
                                0->{
                                    alignment = Alignment.TopStart
                                    targetSize = IntSize(size.width,defaultSize)
                                }
                                1->{
                                    alignment = Alignment.TopEnd
                                    targetSize = IntSize(defaultSize,defaultSize)
                                }
                                2->{
                                    alignment = Alignment.TopEnd
                                    targetSize = IntSize(defaultSize,size.height)
                                }
                                3->{
                                    alignment = Alignment.BottomEnd
                                    targetSize = IntSize(defaultSize,defaultSize)
                                }
                                4->{
                                    alignment = Alignment.BottomEnd
                                    targetSize = IntSize(size.width,defaultSize)
                                }
                                5->{
                                    alignment = Alignment.BottomStart
                                    targetSize = IntSize(defaultSize,defaultSize)
                                }
                                6->{
                                    alignment = Alignment.BottomStart
                                    targetSize = IntSize(defaultSize,size.height)
                                }
                                7->{
                                    alignment = Alignment.TopStart
                                    targetSize = IntSize(defaultSize,defaultSize)
                                }
                            }
                        }
                        LaunchedEffect(key1 = size){
                            phase = 0
                        }
                        Box(
                            modifier = Modifier
                                .widthIn(max = min(config.screenWidthDp, config.screenHeightDp).dp)
                                .heightIn(max = min(config.screenWidthDp, config.screenHeightDp).dp)
                                .aspectRatio(1f)
                                .border(
                                    border = BorderStroke(
                                        width = 2.dp,
                                        color = Color.Blue
                                    ),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .onGloballyPositioned {
                                    size = it.size
                                }
                        ){
                            Box(
                                modifier = Modifier
                                    .align(alignment)
                                    .width(animatedSize.value.width.dp)
                                    .height(animatedSize.value.height.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                        .background(
                                            color = Color.Cyan
                                        )
                                ){

                                }
                            }
                        }
                    }
                }
            }
        }
    }
}