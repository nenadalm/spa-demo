const webpack = require('webpack');
const path = require('path');

const BUILD_DIR = path.resolve(__dirname, 'target');
const APP_DIR = path.resolve(__dirname, 'src', 'js');

module.exports = {
    entry: `${APP_DIR}/main.js`,
    output: {
        path: BUILD_DIR,
        filename: 'bundle.js'
    }
};
