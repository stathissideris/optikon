# Optikon

This is a command-line tool that wraps vega and vega-lite, the
powerful visualization JS libraries. Some
[examples](https://vega.github.io/vega-lite/examples/) of what you can
do with vega-lite.

## Differences to vega

- Supports EDN input files
- Can only render SVG

## Development instructions

- In emacs `M-x setenv [enter] GRAALVM_HOME [enter] .../graalvm-[VERSION]/Contents/Home`
- In the resulting REPL you can eval `(System/getProperty "java.runtime.version")`
  to make sure you're in GraalVM.

## Compile instructions

- Download GraalVM and unzip
- Set `$GRAALVM_HOME` to `.../graalvm-[VERSION]/Contents/Home`
- Run `$GRAALVM_HOME/bin/gu install native-image` -- [gu reference](https://www.graalvm.org/docs/reference-manual/graal-updater/#component-installation)
- Run `export JAVA_HOME=$GRAALVM_HOME`
- Run `make get-vega`
- Run `clj -A:native-image`

To test the compiled image:

```
./optikon test-resources/bar-lite.vg.json bar.svg
```

Similarly with all the other files under `test-resources`.
