const config = null;
const base = null;
const loglevel = vega.Warn;
const scale = 4;

function render(spec) {
  const view = new vega.View(vega.parse(spec, config), {
    loader: vega.loader({baseURL: base}),
    logger: vega.logger(loglevel, 'error'),
    renderer: 'none'
  }).finalize();

  return view.toSVG(scale);
}
