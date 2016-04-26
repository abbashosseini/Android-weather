
### Android App:

>this App base on Sunshine2 app (udacity) and **openweathermap** Weather webste and You can [Download](https://raw.githubusercontent.com/abbashosseini/Android-Persian-weather/master/app/app-release.apk) APK File.;

#### Language Support :

- [x]  Persian
- [x]  English
	
#### Screen Shots - App With English :
![ScreenShot](https://github.com/abbashosseini/Android-Persian-weather/blob/master/ScreenShots/ScreenShot1.gif)
![ScreenShot](https://github.com/abbashosseini/Android-Persian-weather/blob/master/ScreenShots/ScreenShot2.gif)


#### Screen Shots - App With Perisan :
![ScreenShot](https://github.com/abbashosseini/Android-Persian-weather/blob/master/4.jpg)
![ScreenShot](https://github.com/abbashosseini/Android-Persian-weather/blob/master/2.jpg)
![ScreenShot](https://github.com/abbashosseini/Android-Persian-weather/blob/master/3.jpg)

### i'm doing :

its get *14 days* data weather from *server* or you can limit it for that see [here](http://openweathermap.org/forecast5) or now you can use **16 days** see [here](http://openweathermap.org/forecast16) for **API**.

this app get weather one time for *14 days* and put it in [*SQLITE*](http://developer.android.com/guide/topics/providers/content-providers.html) Database if you have problem with connection to [server ](http://openweathermap.org/) its actually server limitation not application bug .
	
###FORMAT:

* q = id || city || geographic coordinats
* mode = typeFile
* units =  [°F](https://en.wikipedia.org/wiki/Fahrenheit) (imperial) or [°C](https://en.wikipedia.org/wiki/Celsius) (metric)
* cnt= Days number
	


###RESTful :

app use this url for Rest:

	http://api.openweathermap.org/data/2.5/forecast/daily?
		q=CityName&
		mode=json or xml&
		units=metric&
		cnt=between 1, 16&
		appid=2de143494c0b295cca9337e1e96b00e0

Real Example :

	http://api.openweathermap.org/data/2.5/forecast/daily?
		q=London&
		mode=json&
		units=metric&
		cnt=7&
		appid=2de143494c0b295cca9337e1e96b00e0
		
### JSON
example
```json
	{"cod":"200","message":0.0032,
	"city":{"id":1851632,"name":"Shuzenji",
	"coord":{"lon":138.933334,"lat":34.966671},
	"country":"JP"},
	"cnt":10,
	"list":[{
	    "dt":1406080800,
	    "temp":{
	        "day":297.77,
	        "min":293.52,
	        "max":297.77,
	        "night":293.52,
	        "eve":297.77,
	        "morn":297.77},
	    "pressure":925.04,
	    "humidity":76,
	    "weather":[{"id":803,"main":"Clouds","description":"broken clouds","icon":"04d"}],}
	        ]}
```	        
###XML
example
```xml	
	
	<weatherdata>
	<location>
	<name>London</name>
	<type/>
	<country>GB</country>
	<timezone/>
	<location altitude="0" latitude="51.50853" longitude="-0.12574" geobase="geonames" geobaseid="0"/>
	</location>
	<credit/>
	<meta>
	<lastupdate/>
	<calctime>0.0091</calctime>
	<nextupdate/>
	</meta>
	<sun rise="2015-06-04T03:46:26" set="2015-06-04T20:11:17"/>
	<forecast>
	<time day="2015-06-04">
	<symbol number="802" name="scattered clouds" var="03d"/>
	<precipitation/>
	<windDirection deg="148" code="SSE" name="South-southeast"/>
	<windSpeed mps="5.12" name="Gentle Breeze"/>
	<temperature day="23.65" min="17.27" max="23.74" night="17.27" eve="22.94" morn="17.54"/>
	<pressure unit="hPa" value="1032.24"/>
	<humidity value="70" unit="%"/>
	<clouds value="scattered clouds" all="36" unit="%"/>
	</time>
	</forecast>
	</weatherdata>
	
```
