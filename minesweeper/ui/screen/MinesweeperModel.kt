import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

data class BoardCell(val row: Int, val col: Int)

class MinesweeperModel : ViewModel() {

    var clickedCells = mutableStateListOf<BoardCell>()
    var flaggedCells = mutableStateListOf<BoardCell>()
    var cellNumbers = mutableStateMapOf<BoardCell, Int>()
    val mineLocations = mutableSetOf<BoardCell>()
    val numberOfMines = 3
    var remainingMines = 3
    var isLost = mutableStateOf(false)
    var isWon = mutableStateOf(false)

    init {
        placeMines()
    }

    fun onCellClicked(cell: BoardCell, isFlagMode : Boolean) {
        if (isLost.value || isWon.value) return
        if (isFlagMode) {
            if (flaggedCells.contains(cell)) {
                flaggedCells.remove(cell)
            } else {
                flaggedCells.add(cell)
                if (!mineLocations.contains(cell)) {
                    isLost.value = true
                } else{
                    remainingMines -=1
                    if (remainingMines == 0){
                        isWon.value = true
                    }
                }
            }
        } else {
            //not clicked
            if (!clickedCells.contains(cell)) {
                clickedCells.add(cell)
                // bomb
                if (mineLocations.contains(cell)) {
                    isLost.value = true
                    cellNumbers[cell] = -1
                } else {
                    val number = calculateNumberForCell(cell)
                    if (number == 0) {
                        collapse(cell)
                    }
                    cellNumbers[cell] = number
                }
            }
        }
    }

    fun calculateNumberForCell(cell: BoardCell): Int {
        var count = 0
        for (i in -1..1) {
            for (j in -1..1) {
                val neighborRow = cell.row + i
                val neighborCol = cell.col + j
                if (neighborRow in 0..4 && neighborCol in 0..4) {
                    val neighborCell = BoardCell(neighborRow, neighborCol)
                    if (mineLocations.contains(neighborCell)) {
                        count++
                    }
                }
            }
        }
        return count
    }

    fun collapse(cell: BoardCell) {
        for (i in -1..1) {
            for (j in -1..1) {
                val neighborRow = cell.row + i
                val neighborCol = cell.col + j
                if (neighborRow in 0..4 && neighborCol in 0..4) {
                    val neighborCell = BoardCell(neighborRow, neighborCol)

                    if (!clickedCells.contains(neighborCell)) {
                        clickedCells.add(neighborCell)
                        val number = calculateNumberForCell(neighborCell)
                        cellNumbers[neighborCell] = number

                        if (number == 0) {
                            collapse(neighborCell)
                        }
                    }
                }
            }
        }
    }

    fun placeMines() {
        val allCells = (0..4).flatMap { row ->
            (0..4).map { col -> BoardCell(row, col) }
        }
        mineLocations.addAll(allCells.shuffled().take(numberOfMines))
    }

    fun resetGame() {
        clickedCells.clear()
        cellNumbers.clear()
        flaggedCells.clear()
        mineLocations.clear()
        isLost.value = false
        isWon.value = false
        remainingMines = 3
        placeMines()
    }

}

