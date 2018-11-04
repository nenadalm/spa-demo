var Sentry = {};

Sentry.Scope = {};
Sentry.Scope.setUser = function(user) {};

/**
 * @typedef {function(Sentry.Scope)} Sentry.configureScopeCallback
 */

Sentry.init = function(config) {};
/**
 * @param {Sentry.configureScopeCallback} callback
 */
Sentry.configureScope = function(callback) {};
Sentry.captureMessage = function(m) {};
Sentry.captureException = function(e) {};
