package io.trieulh.simpleticketconstraintlayout.constants

import androidx.annotation.IntDef
import io.trieulh.simpleticketconstraintlayout.constants.SimpleOrientation.Companion.HORIZONTAL
import io.trieulh.simpleticketconstraintlayout.constants.SimpleOrientation.Companion.VERTICAL

/**
 * Created by trieulh on 9/11/20
 */

@IntDef(value = [HORIZONTAL, VERTICAL])
@Retention(AnnotationRetention.SOURCE)
annotation class SimpleOrientation {
    companion object {
        const val HORIZONTAL = 0
        const val VERTICAL = 1
    }
}