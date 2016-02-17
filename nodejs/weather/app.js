/*
 * Module dependencies
 */
var express = require('express')
  , stylus = require('stylus')
  , weather = require('./weather')
  , nib = require('nib');
  
var app = express();

// setup css handling with stylus.
function compile(str, path) {
  return stylus(str)
    .set('filename', path)
    .use(nib());
}
app.use(stylus.middleware(
		  { src: __dirname + '/public'
		  , compile: compile
		  }
		));


// setup jade
app.set('views', __dirname + '/views');
app.set('view engine', 'jade');


// setup app for development environment.
app.configure('development', function() {
	app.use(express.logger('dev'));
    app.use(express.static(__dirname + '/public'));
    app.use(express.errorHandler({ dumpExceptions: true, showStack: true }));
});


// Our main url.
app.get('/', weather.fetchAllConditionsForJade);

// This is for an internal ajax call.
app.get('/weather/:state/:city', weather.conditions);


//This is for an internal ajax call.
app.get('/allweather', weather.fetchAllConditions);


app.listen(3000);