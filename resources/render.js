const config = null;
const base = null;
const loglevel = vega.Warn;
const scale = 1;

function render(spec) {
  if (spec['$schema'] == 'https://vega.github.io/schema/vega-lite/v4.json') {
    spec = vegaLite.compile(spec);
  }

  const view = new vega.View(vega.parse(spec, config), {
    loader: vega.loader({baseURL: base}),
    logger: vega.logger(loglevel, 'error'),
    renderer: 'none'
  }).finalize();

  var Atom = Java.type('clojure.lang.Atom');
  atom = new Atom(null);
  view.toSVG(scale).then(svg => atom.reset(svg));
  return atom;
}
