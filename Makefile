SHELL := /usr/bin/env bash
ENV := dev

.PHONY: deps
deps: # Installs js dependencies.
	yarn install

.PHONY: create-migration
create-migration: # Creates new migration
	touch migrations/$$(date +%Y%m%d%H%M).{undo,do}.sql

.PHONY: clean
clean: # Cleans all build artifacts
	rm -rf target/
