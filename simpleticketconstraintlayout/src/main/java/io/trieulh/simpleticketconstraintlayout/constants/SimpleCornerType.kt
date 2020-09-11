package io.trieulh.simpleticketconstraintlayout.constants

import androidx.annotation.IntDef
import io.trieulh.simpleticketconstraintlayout.constants.SimpleCornerType.Companion.OUTER_ROUND
import io.trieulh.simpleticketconstraintlayout.constants.SimpleCornerType.Companion.ROUND
import io.trieulh.simpleticketconstraintlayout.constants.SimpleCornerType.Companion.TRIANGLE

/**
 * Created by trieulh on 9/11/20
 */

@IntDef(value = [ROUND, TRIANGLE, OUTER_ROUND])
@Retention(AnnotationRetention.SOURCE)
annotation class SimpleCornerType {
    companion object {
        const val ROUND = 0
        const val TRIANGLE = 1
        const val OUTER_ROUND = 2
    }
}