# Inspector Graphetto

_Inspector Graphetto_ is a Gradle build plugin to inspect and visualize the task execution graph of your build. 

## Installation

Inside your top-level Gradle file add the Inspector Graphetto plugin:

**In a `build.gradle` file (Groovy):**
```groovy
plugins {
    id 'at.droiddave.graphetto' version '0.0.1'
}
```

**In a `build.gradle.kts` Kotlin DSL file:**

```kotlin 
plugins {
    id("at.droiddave.graphetto") version "0.0.1"
}
```

## Usage

Simply run a build, and Inspector Graphetto will write a GraphViz `.dot` file to `build/reports/taskGraph/graph.dot`.

```shell
$ ./gradlew assembleDebug -Dat.droiddave.graphene.enabled=true
$ cat build/reports/taskGraph/graph.dot

strict digraph G {
  1 [ label=":someOtherTask" ];
  2 [ label=":someTask" ];
  2 -> 1;
}
```

You can also print the entire task dependency graph to the console:

```shell 
$ ./gradlew assembleDebug -Dat.droiddave.graphetto.consoleOutput=TREE

 ── :someTask
    └── :someOtherTask

> Task :someOtherTask UP-TO-DATE
> Task :someTask UP-TO-DATE

BUILD SUCCESSFUL in 92ms

```

## Configuration

The plugin registers an extension called `graphetto` on your project which can be used to configure the output:

```groovy
graphetto {
    enabled = true
    dotFile = "$buildDir/reports/taskGraph/graph.dot"
    renderFormat = RenderFormat.PNG
    outputFile = "$buildDir/reports/taskGraph/graph.png"
    consoleOutput = at.droiddave.graphetto.ConsoleOutput.TREE
}
```

### `enabled`

*Default:* `false`  
*Command line option:* `at.droiddave.graphene.enabled=[true|false]`

Enables task graph report generation. This is disabled by default to not affect build times, and should only be enabled on demand (i.e. if a task graph report should be generated).

When running a build from the command line, you can enable report generation using the `-Dat.droiddave.graphene.enabled=true` option:

```shell 
./gradlew assembleDebug -Dat.droiddave.graphene.enabled=true
```

Alternatively, to enable report generation inside your Gradle build file, simply set the `graphene.enabled` DSL property to `true`:

```groovy
graphene {
    enabled = true
}
```

Note that the command line option takes precedence over the value configured via the DSL, and can therefore be used to override the default.
### `dotFile`

Configures the path of the `.dot` output file containing the information about the task graph that was executed. Defaults to `reports/taskGraph/graph.dot`.  

```groovy
graphetto {
    dotFile = new File(buildDir, 'reports/my-task-graph.dot')
}
```

### `consoleOutput`

Configures the type of console output produced by the plugin.

* `TREE` will print the entire task dependency tree before after the configuration phase of your build.
* `NONE` will not print any console output. This is the default.

```groovy
graphetto {
    consoleOutput = at.droiddave.graphetto.ConsoleOutput.TREE
}
```

You can also override the current configuration by passing the `-Dat.droiddave.graphetto.consoleOutput=TREE` option when invoking your build on the command line.