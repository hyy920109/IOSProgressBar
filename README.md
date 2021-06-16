# 	IOSProgressBar

**IOSProgressBar** is a progress-bar lib for android. And the progress-bar looks like iOS system style

### Features:

- Support horizontal and vertical orientation.
- Support drag progress-bar and click progress-bar.
- Support progress divider and custom your divider height or color.
- Support show progress text
- Support custom progress rect conner radius and style

### Sample screenshots:

![ios-progress-bar-1](/Users/heyangyang/Desktop/ios-progress-bar-1.gif)



![ios-progrss-bar-2](/Users/heyangyang/Desktop/ios-progrss-bar-2.gif)

### Basic usage:

#### Gradle

Add  maven repositories of jitpack in your project's ***build.gradle*** file:

```groovy
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

Add dependence in your app's ***build.gradle*** file:

```groovy
dependencies {		
	implementation 'com.github.hyy920109:IOSProgressBar:v1.2.0'
}
```

#### Code Sample

In .xml files:

```xml
		...
<com.hyy.iosprogressbar.IOSProgressBar
     android:id="@+id/ios_progress_bar_horizontal"
     android:layout_width="164dp"
     android:layout_height="56dp"
     app:ipb_conner_radius="8dp"
     app:ipb_progress_show_text="true"
     app:ipb_progress_bar_orientation="horizontal"
     app:layout_constraintTop_toTopOf="parent"
     app:layout_constraintEnd_toEndOf="parent"
     app:layout_constraintStart_toStartOf="parent"
     android:layout_marginTop="@dimen/fab_margin"/>

<com.hyy.iosprogressbar.IOSProgressBar
     android:id="@+id/ios_progress_bar_vertical"
     android:layout_width="56dp"
     android:layout_height="164dp"
     app:ipb_conner_radius="8dp"
     app:ipb_progress_max="4"
     app:ipb_progress_show_divider="true"
     app:layout_constraintBottom_toBottomOf="parent"
     app:layout_constraintEnd_toEndOf="parent"
     app:layout_constraintStart_toStartOf="parent"
     app:layout_constraintTop_toTopOf="parent" />
...
   
```

And in activity or fragment:

```kotlin

view.findViewById<IOSProgressBar(R.id.ios_progress_bar_horizontal).setOnProgressChangeListener { iosProgressBar, progress, maxProgress, minProgress, actionUp ->
     //do some things
}
...
view.findViewById<IOSProgressBar>(R.id.ios_progress_bar_vertical)
    .setOnProgressChangeListener { iosProgressBar, progress, maxProgress, minProgress, actionUp ->
    //do some things
}
```

#### API doc

| Attribute                    | Description                                                |
| ---------------------------- | ---------------------------------------------------------- |
| ipb_conner_radius            | IOSProgressBar rect conner radius                          |
| ipb_background_color         | IOSProgressBar background color                            |
| ipb_progress_color           | IOSProgressBar progress color                              |
| ipb_progress_max             | IOSProgressBar max progress                                |
| ipb_progress_min             | IOSProgressBar min progress                                |
| ipb_progress                 | IOSProgressBar current progress                            |
| ipb_progress_show_divider    | IOSProgressBar show divider or not                         |
| ipb_progress_divider_height  | IOSProgressBar divider height                              |
| ipb_progress_divider_color   | IOSProgressBar divider color                               |
| ipb_progress_show_text       | IOSProgressBar show progress text ot not                   |
| ipb_progress_text_color      | IOSProgressBar progress text color                         |
| ipb_progress_text_size       | IOSProgressBar progress text size                          |
| ipb_progress_bar_orientation | IOSProgressBar progress orientation:vertical or horizontal |
| ipb_progress_conner_style    | IOSProgressBar progress rect conner style: round or cut.   |



### End

Above we introduce all attributes of [IOSProgressBar](https://github.com/hyy920109/IOSProgressBar) , if it helps you ,  thanks for your star. Or if you have some good advices, welcome commit a pull request. If [IOSProgressBar](https://github.com/hyy920109/IOSProgressBar) has some bug in use, you can commit an issue to me, I will reply as soon as.

