package hu.ait.minesweeper.ui.screen

import BoardCell
import MinesweeperModel
import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp

import androidx.lifecycle.viewmodel.compose.viewModel
import hu.ait.minesweeper.R
import kotlinx.coroutines.launch

@Composable
fun MinesweeperScreen(
    modifier: Modifier = Modifier,
    viewModel: MinesweeperModel = viewModel()
) {
    val scale = remember { Animatable(1f) }
    val coroutineScope = rememberCoroutineScope()
    val clickedCells = viewModel.clickedCells
    val flaggedCells = viewModel.flaggedCells
    var isFlagMode by remember { mutableStateOf(false) }
    val cellNumbers = viewModel.cellNumbers
    val isLost = viewModel.isLost.value
    val isWon = viewModel.isWon.value

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Checkbox(checked = isFlagMode, onCheckedChange = { isChecked ->
                isFlagMode = isChecked
            })
            Text(text = "Flag Mode")
        }

        MinesweeperBoard(
            onCellClick = { boardCell -> viewModel.onCellClicked(boardCell, isFlagMode) },
            scale = scale.value,
            clickedCells = clickedCells,
            flaggedCells = flaggedCells,
            cellNumbers = cellNumbers
        )

        if (isLost) {
            Text(
                text = "You Lost!",
                color = Color.Red,
                modifier = Modifier.padding(vertical = 16.dp)
            )
        }

        if (isWon) {
            Text(
                text = "You Won!",
                color = Color.Red,
                modifier = Modifier.padding(vertical = 16.dp)
            )
        }

        Button(onClick = {
            viewModel.resetGame()
            coroutineScope.launch {
                scale.animateTo(
                    1.5f,
                    animationSpec = tween(durationMillis = 300)
                )
                scale.animateTo(1f)
            }
        }) {
            Text(stringResource(R.string.btn_reset))
        }
    }
}


@Composable
fun MinesweeperBoard(
    onCellClick: (BoardCell) -> Unit,
    clickedCells: SnapshotStateList<BoardCell>,
    flaggedCells: List<BoardCell>,
    scale: Float,
    cellNumbers: Map<BoardCell, Int>
) {
    val imageBackGround = ImageBitmap.imageResource(R.drawable.grid)
    val flagImage = ImageBitmap.imageResource(R.drawable.flag)
    val textMeasurer = rememberTextMeasurer()
    val density = LocalDensity.current

    Canvas(
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .aspectRatio(1.0f)
            .scale(scale)
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    val row = (offset.y / (size.height / 5)).toInt()
                    val col = (offset.x / (size.width / 5)).toInt()

                    onCellClick(BoardCell(row, col))
                }
            }
    ) {
        val gridSize = size.minDimension
        val cellSize = gridSize / 5

        for (row in 0..4) {
            for (col in 0..4) {
                val cell = BoardCell(row, col)
                val x = col * cellSize
                val y = row * cellSize

                if (!clickedCells.contains(cell)) {
                    drawImage(
                        image = imageBackGround,
                        srcOffset = IntOffset(0, 0),
                        srcSize = IntSize(imageBackGround.width, imageBackGround.height),
                        dstOffset = IntOffset(x.toInt(), y.toInt()),
                        dstSize = IntSize(cellSize.toInt(), cellSize.toInt())
                    )
                    if (flaggedCells.contains(cell)) {
                        drawImage(
                            image = flagImage,
                            srcOffset = IntOffset(0, 0),
                            srcSize = IntSize(flagImage.width, flagImage.height),
                            dstOffset = IntOffset(x.toInt()+cellSize.toInt()/4, y.toInt()+cellSize.toInt()/4),
                            dstSize = IntSize(cellSize.toInt()/2, cellSize.toInt()/2)
                        )
                    }
                } else {
                    drawRect(
                        color = Color.Gray,
                        topLeft = Offset(x, y),
                        size = Size(cellSize, cellSize)
                    )

                    val number = cellNumbers[cell]
                    val textToDraw = when (number) {
                        -1 -> "\uD83D\uDCA3" // Bomb emoji
                        0 -> ""
                        else -> number.toString()
                    }

                    val fontSizePx = cellSize * 0.5f
                    val textStyle = TextStyle(
                        fontSize = with(density) { fontSizePx.toSp() },
                        fontWeight = FontWeight.Bold,
                        color = if (number == -1) Color.Red else Color.Black
                    )

                    val textLayoutResult = textMeasurer.measure(
                        text = textToDraw,
                        style = textStyle
                    )

                    val textSize = textLayoutResult.size
                    val textX = x + (cellSize - textSize.width) / 2
                    val textY = y + (cellSize - textSize.height) / 2

                    drawText(
                        textLayoutResult = textLayoutResult,
                        topLeft = Offset(textX, textY)
                    )
                }
            }
        }

        // Draw the grid lines
        for (i in 1..4) {
            drawLine(
                color = Color.DarkGray,
                strokeWidth = 5f,
                start = Offset(cellSize * i, 0f),
                end = Offset(cellSize * i, gridSize)
            )
            drawLine(
                color = Color.DarkGray,
                strokeWidth = 5f,
                start = Offset(0f, cellSize * i),
                end = Offset(gridSize, cellSize * i)
            )
        }
    }
}
