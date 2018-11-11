[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

# SPA-DEMO

This repository serves as example for making single page applications. It is in very early stage.

It is written in [ClojureScript](https://clojurescript.org/).

## Possible future features

- server side rendering
- logging into [Sentry](https://sentry.io/) with source maps (currently there is logging into sentry without them)
- ...

## Directory structure

```
- /
  - browser
  - server
  - resources
```

### browser

Contains frontend part of the app.

### server

Contains backend part of the app.

### resources

Contains files that can be served by browser. Used by both server and browser.

## Used libraries

## [Maccchiato](https://macchiato-framework.github.io/)

Provides nice api for working with http.

## [Reitit](https://github.com/metosin/reitit)

Routing library with support for auto generating api documentation using [swagger](https://swagger.io/).

## [HoneySQL](https://github.com/jkk/honeysql)

Library for writing SQL queries as data structures (useful for dynamic queries - filters, sorters, ...).

## [Timbre](https://github.com/ptaoussanis/timbre)

Provides logging with various appenders (functions writing logs somewhere - e.g. into Sentry).

## [Phrase](https://github.com/alexanderkiel/phrase)

Library for translating [spec](https://clojure.org/about/spec) errors into nice error messages.

## [Re-frame](https://github.com/Day8/re-frame)

Framework managing state of application in browser using [Reagent](https://reagent-project.github.io/) for view part.

## [Material-UI](https://material-ui.com/)

Very useful for people that have absolutely no idea how to create frontend that doesn't look terribly.
