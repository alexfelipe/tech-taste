package myiconpack

import MyIconPack
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.StrokeCap.Companion.Butt
import androidx.compose.ui.graphics.StrokeJoin.Companion.Miter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

public val MyIconPack.PetiscosCategory: ImageVector
    get() {
        if (_petiscosCategory != null) {
            return _petiscosCategory!!
        }
        _petiscosCategory = Builder(name = "PetiscosCategory", defaultWidth = 64.0.dp, defaultHeight
                = 56.0.dp, viewportWidth = 64.0f, viewportHeight = 56.0f).apply {
            path(fill = null, stroke = null, strokeLineWidth = 0.0f, strokeLineCap = Butt,
                    strokeLineJoin = Miter, strokeLineMiter = 4.0f, pathFillType = NonZero) {
                moveTo(4.0f, 0.0f)
                horizontalLineToRelative(56.0f)
                verticalLineToRelative(48.0f)
                horizontalLineToRelative(-56.0f)
                close()
            }
        }
        .build()
        return _petiscosCategory!!
    }

private var _petiscosCategory: ImageVector? = null
