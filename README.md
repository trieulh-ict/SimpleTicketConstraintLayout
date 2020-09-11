# SimpleTicketConstraintLayout

[![](https://jitpack.io/v/trieulh-ict/SimpleTicketConstraintLayout.svg)](https://jitpack.io/#trieulh-ict/SimpleTicketConstraintLayout)

Custom ConstraintLayout that helps you create a flexible ticket background UI in ease.

It is an open source project, feel free to fork and customize it.

### Installation:

In project `build.gradle`:

```kotlin
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

In app `build.gradle`:

```kotlin
dependencies {
    implementation 'com.github.trieulh-ict:SimpleTicketConstraintLayout:version'
}
```

### Some basic features:

- Creata ticket with **TRIANGLE** or **ROUND** corner
- Adjust Inner Radius for the corner
- Adjust Shadow size and Shadow color

![Example-Corner](images/example.png) 

![Example-Triangle](images/example-triangle.png)

![Example-Corn-Divider](images/example-2.0.png)



# Usage

```kotlin
<io.trieulh.simpleticketconstraintlayout.SimpleTicketConstraintLayout
        android:id="@+id/ticketLayout"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:ds_corner_type="OUTER_ROUND"
        app:ds_divider_distance="76dp"
        app:ds_divider_ratio="0.5"
        app:ds_inner_corner_radius="8dp"
        app:ds_orientation="HORIZONTAL"
        app:ds_shadow_radius="5dp"
        app:ds_should_display_divider="true"
        app:ds_should_display_stroke="true"
        app:ds_stroke_color="@color/colorAccent"
        app:ds_stroke_width="1dp"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintTop_toTopOf="parent">

        ...

</io.trieulh.simpleticketconstraintlayout.SimpleTicketConstraintLayout>
```

# Attributes

| Atribute              | Value Type           | Description                                                             |
| --------------------- | -------------------- | ----------------------------------------------------------------------- |
| _ds_inner_corner_radius_ | Dimension            | Dimension value of Inner Radius (Default is 0)                          |
| _ds_corner_type_         | Enum(ROUND,TRIANGLE) | Corner type (Default is ROUND)                                          |
| _ds_shadow_radius_       | Dimension            | Dimension value of Shadow Radius (Default is 0)                         |
| _ds_shadow_color_        | Color Int            | Color code for Shadow Color (Default is Color.argb(200, 200, 200, 200)) |
| _ds_orientation_        | Enum(HORIZONTAL,VERTICAL)| Orientation of Corn divider                                          |
| _ds_should_display_divider_| Boolean| Flag to display Corn divider                                          |
| _ds_should_display_stroke_| Boolean| Flag to display Background stroke                                          |
| _ds_divider_ratio_| Float(0.0 -> 1.0)| Ratio of divider distance                                          |
| _ds_divider_distance_| Boolean| Distance of divider from LEFT or TOP                                          |
| _ds_stroke_width_| Dimension| Width of border stroke                                          |
| _ds_stroke_color_| Color Int| Color of border stroke                                          |

# Changelog
- v2.0: Now support OUTER_ROUND corner, Corn Divider and more configuration

- v0.1.0: Init the library, Support CORNER,TRIANGLE corner and some configuration

# License

MIT License

Copyright (c) 2019 Tristan Le

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
