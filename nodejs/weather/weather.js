var key = "e4d264fc774deafa";
var util = require('util');

/*
 * This is a proxy for the external weather API webservice.
 * It just gets the data for a single city.
 */
exports.conditions = function conditions(req, res) {
    var state = req.params.state;
    var city = req.params.city;
	
	res.setHeader('Content-Type', 'application/json');
	var allWeatherDataForCities = new Object;
	fetchConditions(state, city, allWeatherDataForCities, function() {
		var cityWeatherData = allWeatherDataForCities[state+'/'+city];
        res.write(JSON.stringify(cityWeatherData));
    	res.end();							
	});
};


/*
 * This retrieves weather data for all of our cities using the weather API webservices.
 */
exports.fetchAllConditions = function fetchAllConditions(req, res) {
	res.setHeader('Content-Type', 'application/json');
	var allWeatherDataForCities = new Object;
	fetchConditions('CA', 'Campbell', allWeatherDataForCities, function() {
		fetchConditions('NE', 'Omaha', allWeatherDataForCities, function() {
			fetchConditions('TX', 'Austin', allWeatherDataForCities, function() {
				fetchConditions('MD', 'Timonium', allWeatherDataForCities, function() {
					//console.log(util.inspect(allWeatherDataForCities, false, null));
			    	res.write(JSON.stringify(allWeatherDataForCities));
			    	res.end();					
			    })
			}) 
		})
	});
}


/*
 * This retrieves weather data for all of our cities using the weather API webservices.
 */
exports.fetchAllConditionsForJade = function fetchAllConditionsForJade(req, res) {
	var allWeatherDataForCities = new Object;
	fetchConditions('CA', 'Campbell', allWeatherDataForCities, function() {
		fetchConditions('NE', 'Omaha', allWeatherDataForCities, function() {
			fetchConditions('TX', 'Austin', allWeatherDataForCities, function() {
				fetchConditions('MD', 'Timonium', allWeatherDataForCities, function() {

					var cities = new Array("CA/Campbell","NE/Omaha","TX/Austin", "MD/Timonium");
					var weatherDataArray = new Array();
					var j = 0;
			        for (i=0; i<cities.length; i++) {
			            if (cities[i] in allWeatherDataForCities) {
			              var current_observation = allWeatherDataForCities[cities[i]].current_observation;
			              if (!(typeof current_observation === 'undefined')) {
			            	  weatherDataArray[j] = current_observation;
			            	  j = j + 1;
			              }
			            }
			          }					
					res.render('index',
					  {'title': 'Home', 'weatherDataArray': weatherDataArray});
			    })
			}) 
		})
	});
}



/*
 * This function does the work of making the http call to the 
 * external weather API webservice.
 */
function fetchConditions(state, city, allWeatherDataForCities, callback) {
	  var location = state + "/" + city;  
	  
	  var http = require('http'), options = {
	          host : "api.wunderground.com",
	          port : 80,
	          path : "/api/" + key + "/conditions/q/" + location + ".json",
	          method : 'GET'
	  };

	  console.log('making API call for ' + location);
	  http.get(options, function (http_res) {
	      // initialize the container for our data
	      var data = "";  
	  		
		    // this event fires many times, each time collecting another piece of the response
		    http_res.on("data", function (chunk) {
		        // append this chunk to our growing 'data' var
		        data += chunk;
		    });

		    // this event fires *one* time, after all the 'data' events/chunks have been gathered
		    http_res.on("end", function () {
		    	console.log('got ' + location);
		    	allWeatherDataForCities[location] = JSON.parse(data);
		    	callback(allWeatherDataForCities);
		    });
		    
		});  
};