SHELL := /usr/bin/env bash

create-migration:
	touch migrations/$$(date +%Y%m%d%H%M).{undo,do}.sql

clean:
	rm -rf target/
