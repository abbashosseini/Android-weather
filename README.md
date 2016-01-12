
##Android App:
---------
its Was Google project and i work on it couple months ago and converted to Persian One and Change it in both side (UI, Code) .

![ScreenShot](https://github.com/abbashosseini/Android-Persian-weather/blob/master/1.png)
![ScreenShot](https://github.com/abbashosseini/Android-Persian-weather/blob/master/2.png)
![ScreenShot](https://github.com/abbashosseini/Android-Persian-weather/blob/master/3.png)
![ScreenShot](https://github.com/abbashosseini/Android-Persian-weather/blob/master/4.png)
![ScreenShot](https://github.com/abbashosseini/Android-Persian-weather/blob/master/5.png)

##i'm doing :
------------
its get *14 days* data weather from server and put it in *SQLITE* if you have problem with connection to http://openweathermap.org/ server 
its actually server limitation not application bug .

you can see api here :

	http://openweathermap.org/api

its letter changing happen in **openweathermap** webSite.app now work with Temprory APPID. Create Valid one for your app.

you can with this link see what country, city **openweathermap** support and see samples :

	http://bulk.openweathermap.org/sample/

##RESTful :
-----------
app use this url for Rest:
	
	http://api.openweathermap.org/data/2.5/forecast/daily?
		q=CityName&
		mode=json or xml&
		units=metric&
		cnt=between 1, 10&
		appid=2de143494c0b295cca9337e1e96b00e0

Real Example :

	http://api.openweathermap.org/data/2.5/forecast/daily?
		q=London&
		mode=json&
		units=metric&
		cnt=7&
		appid=2de143494c0b295cca9337e1e96b00e0

##License

	Copyright (C) 2015 AbbasHosseini
	Copyright (C) 2010 The Android Open Source Project
	
	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at
	
	    http://www.apache.org/licenses/LICENSE-2.0
	
	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
