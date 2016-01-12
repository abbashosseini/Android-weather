
##Android App:
---------
its Was Google project and i work on it couple months ago and converted to Persian One and Change it in both side (UI, Code) .

##i'm doing :
------------
its get *14 days* data weather from server and put it in *SQLITE* if you have problem with connection to http://openweathermap.org/ server 
its actually server limitation not application bug .

you can see api here :

	http://openweathermap.org/api

its letter changing happen in **openweathermap** webSite.

app now work with Temprory APPID. Create Valid one for your app and you can with this link see what country, city **openweathermap**
support and see samples :

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

