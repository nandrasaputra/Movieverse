In order to make app works, you need Youtube, Yandex and TheMovieDatabase API Key, in build.gradle(app) :

Change:

```xml
...
def apiPropertiesFile = rootProject.file("api.properties")
def apiProperties = new Properties()
apiProperties.load(new FileInputStream(apiPropertiesFile))

android {
	defaultConfig {
		...
		buildConfigField("String", "TMDB_API_KEY", apiProperties["TMDB_API_KEY"])
		buildConfigField("String", "YANDEX_API_KEY", apiProperties["YANDEX_API_KEY"])
		buildConfigField("String", "GOOGLE_YOUTUBE_API", apiProperties["GOOGLE_YOUTUBE_API"])
		...
	}
}
```

Into

```xml
...
android {
	defaultConfig {
		...
		buildConfigField("String", "TMDB_API_KEY", "\"YOUR_API_KEY\"")
		buildConfigField("String", "YANDEX_API_KEY", "\"YOUR_API_KEY\"")
		buildConfigField("String", "GOOGLE_YOUTUBE_API", "\"YOUR_API_KEY\"")
		...
	}
}
```
