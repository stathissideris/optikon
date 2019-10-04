get-vega:
	cd resources && wget https://cdnjs.cloudflare.com/ajax/libs/vega-lite/4.0.0-beta.2/vega-lite.js
	cd resources && wget https://cdnjs.cloudflare.com/ajax/libs/vega/5.6.0/vega.js
native:
	clj -A:native-image
clean:
	rm -rf test-results
	rm -rf classes
	rm -rf optikon
	rm -rf *.svg
test-native:
	mkdir -p test-results
	./optikon test-resources/bar-lite.vg.edn test-results/bar-lite.vg.edn.svg
	./optikon test-resources/bar-lite.vg.json test-results/bar-lite.vg.json.svg
	./optikon test-resources/bar.vg.json test-results/bar.vg.json.svg
