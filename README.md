# OpenPay_technical

The goal of this application is to show my knowledge in various technologies in Android development. 

In the following document I will explain what's been done, how and the libraries and technologies used divided in screens.

General: 
All screens are fragments, I used navigation component and a botom navigation bar to navigate between the screens.
Used MVVM as an architecture.
When a service must be consumed, there's a check for internet connection that shows a Dialog Fragment with a warning. Also every error, us catched and shown a frienly error to the user.
Libraries:
 - Realm Database
 - Lottie
 - Retrofit
 - Glide
 - Firebase
 - Hilt (dependency injection)
 - MockK (for unit testing)


In the first tab in which it must be shown information about the most popular user and all his reviews. Since the API rest doesn’t have an endpoint
that could do that, I brought the most popular actor, and all the movies and tv shows he/her appears in.
First I consumed the most popular actor endpoint, grabbing the first one and mapping it response to get an Actor object. This service was consumed using the Retrofit library, in a Coroutine scope (specifically ViewModelScope) and using the IO Dispatcher so the UI doesn’t block until the call finalises, then, I showed this actor information in the screen, and proceed to consume the second service which brings the movies the actor is in, also mapping that list to a list of Movies. Then this list is shown to the user displayed in a RecyclerView, for this I needed to implement an Adapter. 
After all this, (if all the information came successfully), everything is saved in a local database. For this I used Realm Database library (which uses MongoDB on the back), for storing all Movies and also the most popular actor.
The service call where made all in the fragment's ViewModel, using a LiveData for observing an comunicating the changes.

<img width="319" alt="image" src="https://user-images.githubusercontent.com/84336381/222619355-ad17079e-2826-4b7a-b760-ca4e01c49571.png">

* Movie: Every Movie object can be a movie or a tv show, changing only that it has a "title" field or a "name" field. Also has a "section" custom field, that I use for determining if is a movie from the actor, or if it belongs to the Top Rated, Popular or Recommended tab. Also, the Movie Entity, for the local database, has an actorId, used to get all movies from the local database that the desired actor is in.

* Actor: The Actor has an Id, which is used for getting the movies in he/her is in.

The second tab is devided in three RecyclerViews, used for displaying the Top rated, the Popular and Recommended movies. The last one, since there's no endpoint for bringing random recomendations, I used the Id from the most top rated movie, to get recomendations based on that movie.
Again, after getting all the movies, they are stored in the local database. All this write transactions in the Realm database, are suspended functions, so they are called from a ViewModelScope.

<img width="319" alt="image" src="https://user-images.githubusercontent.com/84336381/222619496-c9281523-7c39-4297-9c03-e656ab4009b7.png">

In the third tab it was asked to grab images from gallery or let the user take a photo, and then, uploading it or them, in Firebase Store.
Both actions are posible using Intents, but first I declared all the permissions necessary to perform any of those actions in the app manifest.
When clicking one of the options for getting the image, an event called startActivityForResult is lunched, allowing to either get the image from gallery or take a picture (this are two different intents).
After we get the image, we can either select or take another one, or upload them.
In this screen I would improve the code, maybe making it a bit cleaner, and also in the UI, I would add a recycler view, for visualising which images are in queue to upload.


https://user-images.githubusercontent.com/84336381/222623427-98ec7f9a-d75a-45be-9882-730719287600.mov


In the location tab it was asked to consume from Firebase and show the localizations in a map, showing also the date of storage. And, every 5 minutes should upload the current location to Firebase and notify the user using NotificationCompat.
Due to a time shortage the last tab I was not able to complete it, neither start it.

However, I would use the Google maps API for displaying in a map all the positions, and also I could get the actual position, requesting first permission to the user, and declaring the permissions in the manifest. Also checking if the device has internet connection so we can consume the Firebase data.
For the timer, I would make it using a CountDownTimer, resetting it every time it finalizes, also asking the location from the device gps, checking again for internet connection, and uploading it. Also I would maybe, persist locally this location, in case the user is without internet, so we are able to upload it later.
