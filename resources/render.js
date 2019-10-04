const config = null;
const base = null;
const loglevel = vega.Warn;
const scale = 1;

function render(spec) {
  if (spec['$schema'].includes('vega-lite')) {
    spec = vegaLite.compile(spec)['spec'];
  }

  const view = new vega.View(vega.parse(spec, config), {
    loader: vega.loader({baseURL: base}),
    logger: vega.logger(loglevel, 'error'),
    renderer: 'none'
  }).finalize();

  var Future = Java.type('java.util.concurrent.CompletableFuture');
  svgFuture = new Future();
  view.toSVG(scale).then(svg => svgFuture.complete(svg));
  return svgFuture;
}
