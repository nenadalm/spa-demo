SHELL := /usr/bin/env bash
ENV := dev

.PHONY: build-js
build-js: # Builds js dependencies
	./node_modules/.bin/webpack -p $(if $(findstring dev,${ENV}),--devtool inline-source-map)

.PHONY: clean-js
clean-js: # Cleans built js dependencies.
	rm -f ./target/bundle.js

.PHONY: deps
deps: # Installs js dependencies.
	yarn install
