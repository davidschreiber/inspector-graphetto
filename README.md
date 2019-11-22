# Grapher

_Grapher_ is a Gradle build plugin to visualize the task execution graph of your build. 

## Installation

Inside your top-level Gradle file add the Grapher plugin:

**In a `build.gradle` file (Groovy):**
```groovy
plugins {
    id 'at.droiddave.grapher' version '1.0.0-SNAPSHOT'
}
```

**In a `build.gradle.kts` Kotlin DSL file:**

```kotlin 
plugins {
    id("at.droiddave.grapher") version "1.0.0-SNAPSHOT"
}
```

## Usage

Simply run a build, and Grapher will write a GraphViz `.dot` file to `build/reports/taskGraph/graph.dot`.


