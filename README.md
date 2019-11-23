# Graphene

_Graphene_ is a Gradle build plugin to visualize the task execution graph of your build. 

## Installation

Inside your top-level Gradle file add the Graphene plugin:

**In a `build.gradle` file (Groovy):**
```groovy
plugins {
    id 'at.droiddave.graphene' version '1.0.0-SNAPSHOT'
}
```

**In a `build.gradle.kts` Kotlin DSL file:**

```kotlin 
plugins {
    id("at.droiddave.graphene") version "1.0.0-SNAPSHOT"
}
```

## Usage

Simply run a build, and Graphene will write a GraphViz `.dot` file to `build/reports/taskGraph/graph.dot`.

```shell
gradle assembleDebug
ls -l build/reports/taskGraph/graph.dot
```

## Configuration

The plugin registers an extension called `graphene` on your project which can be used to configure the output:

```groovy
graphene {
    outputFile = new File(buildDir, 'reports/my-task-graph.dot')
}
```

### `outputFile`

Configures the path of the `.dot` output file containing the information about the task graph that was executed.  

```groovy
graphene {
    outputFile = new File(buildDir, 'reports/my-task-graph.dot')
}
```
