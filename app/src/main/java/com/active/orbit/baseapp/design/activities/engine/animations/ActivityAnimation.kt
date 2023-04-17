package com.active.orbit.baseapp.design.activities.engine.animations

enum class ActivityAnimation(val value: Int) {
    FADE(1),
    LEFT_RIGHT(2),
    BOTTOM_TOP(3),
    DEFAULT(FADE.value);

    companion object {

        fun getByValue(value: Int): ActivityAnimation {
            enumValues<ActivityAnimation>().forEach { animation ->
                if (animation.value == value) {
                    return animation
                }
            }
            return DEFAULT
        }
    }
}