<img height='200' src="app/src/main/res/mipmap-xxxhdpi/ic_movieverse.png" align="left">

# Movieverse
Movieverse is a movie catalogue Android app where you can find what's trending, what's currently playing on cinema, save favorite movie, and find any Movie or TV information. This app is written in Kotlin with <a href='https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html'>Clean Architecture</a>, Using Android <a href='https://developer.android.com/jetpack'>Jetpack</a> Library such as Navigation, Room, ViewModel, Paging, LiveData and Hilt. This app also using Kotlin <a href='https://github.com/Kotlin/kotlinx.coroutines'>Coroutine</a> to perform asyncronous operation, Coroutine Flow to handle asyncronous data stream and <a href='https://square.github.io/retrofit/'>Retrofit</a> to communicate with REST API. Movie and TV data is provided by <a href='https://www.themoviedb.org/'>TMDB</a>. Dark Mode is also available inside the app where you switch to darker theme scheme. This app was developed to complete <a href='https://www.dicoding.com/academies/14'>Belajar Fundamental Aplikasi Android</a> final project and got 5/5 score.

## Preview
<p float="center">
  <img src="docs/Movieverse%20App%20Presentation%20V1.png" height="600" />
</p>

## Prerequisites

In order to make app works, you need <a href='https://developers.google.com/youtube/v3/getting-started'>Youtube API</a> and <a href='https://www.themoviedb.org/documentation/api'>TheMovieDatabase API</a> Key. Then make a new file and name it to api.properties on the root folder and put your api key like this:

In your api.properties (Movieverse/api.properties) :

```xml
...

TMDB_API_KEY = "YOUR_API_KEY"
GOOGLE_YOUTUBE_API = "YOUR_API_KEY"

...
```

## Built With

<pre>
<a href='https://developer.android.com/jetpack'>Android Jetpack</a> - Room, Navigation, Paging, LiveData, ViewModel, Hilt etc.
<a href='https://github.com/google/gson'>Gson</a> - A Java serialization/deserialization library to convert Java Objects into JSON and back.
<a href='https://github.com/bumptech/glide'>Glide</a> - An image loading and caching library for Android focused on smooth scrolling.
<a href='https://github.com/vinc3m1/RoundedImageView'>RoundedImageView</a> - A fast ImageView that supports rounded corners, ovals, and circles.
<a href='https://square.github.io/retrofit/'>Retrofit</a> - A type-safe HTTP client for Android and Java.
<a href='https://github.com/facebook/shimmer-android'>Facebook Shimmer</a> - An easy, flexible way to add a shimmering effect to any view in an Android app.
<a href='https://github.com/IslamKhSh/CardSlider'>CardSlider</a> - Android Card Slider
<a href='https://github.com/yarolegovich/DiscreteScrollView'>DiscreteScrollView</a> - A scrollable list of items that centers the current element and provides easy-to-use APIs for cool item animations.
<a href='https://github.com/airbnb/lottie-android'>Lottie</a> - Lottie is a mobile library for Android and iOS that parses Adobe After Effects animations exported as json with Bodymovin and renders them natively on mobile.
</pre>

## Author

Nandra Saputra
* <a href='https://www.instagram.com/nandrasptr/'>@nandrasptr</a> on Instagram
* <a href='https://www.linkedin.com/in/nandra-saputra-b90b78157/'>LinkedIn</a> Account

## Download Application - APK File
This app require Android 5.0 - Lolipop or higher to run, Please check release section to download the app.

## License

Apache 2.0. See the <a href='https://github.com/nandrasaputra/Movieverse/blob/master/LICENSE'>LICENSE</a> file for details.
