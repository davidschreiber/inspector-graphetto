# Grapher

_Grapher_ is a Gradle build plugin to visualize the task execution graph of your build. 

## Usage

Inside your `build.gradle`:

```groovy
plugins {
    id 'at.droiddave.grapher' version '1.0.0-SNAPSHOT'
}
```

Or if you use Gradle Kotlin script, in your `build.gradle.kts`:

```kotlin 
plugins {
    id("at.droiddave.grapher") version "1.0.0-SNAPSHOT"
}
```