# Optikon

## Development instructions

- In emacs `M-x setenv [enter] GRAALVM_HOME [enter] .../graalvm-[VERSION]/Contents/Home`
- In the resulting REPL you can eval `(System/getProperty "java.runtime.version")`
  to make sure you're in GraalVM.

## Compile instructions

- Download GraalVM and unzip
- Set `$GRAALVM_HOME` to `.../graalvm-[VERSION]/Contents/Home`
- Run `$GRAALVM_HOME/bin/gu install native-image` -- [gu reference](https://www.graalvm.org/docs/reference-manual/graal-updater/#component-installation)
- Run `export JAVA_HOME=$GRAALVM_HOME`
- Run `clj -A:native-image`
