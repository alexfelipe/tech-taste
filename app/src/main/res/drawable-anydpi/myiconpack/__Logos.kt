package myiconpack

import MyIconPack
import androidx.compose.ui.graphics.vector.ImageVector
import kotlin.collections.List as ____KtList

public object LogosGroup

public val MyIconPack.Logos: LogosGroup
  get() = LogosGroup

private var __AllIcons: ____KtList<ImageVector>? = null

public val LogosGroup.AllIcons: ____KtList<ImageVector>
  get() {
    if (__AllIcons != null) {
      return __AllIcons!!
    }
    __AllIcons= listOf()
    return __AllIcons!!
  }