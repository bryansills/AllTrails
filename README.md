# AllTrails at lunch
Demo app made while interviewing at AllTrails

## Setup
Run the following command and fill out the `secrets.properties` file with valid values.
```
cp secrets.properties.example secrets.properties
```
This isn't what you should do for a real, production app, but works fine for an interview task.

## Notes from the applicant
- This took me about 10 hours
- Things I didn't have time to do:
  - Show actual photos of the places, instead of a photo of who took the photo. There would have to be an addition network request per image, and I think I could have figured out a way to make Coil do it, but I wanted to focus on other things.
  - Database for saving favorites. A simple Room database would suffice here.
  - Detail page for an individual restaurant. Navigation is here, it probably wouldn't be too much work to hook up various pieces.
  - Unit testing. There isn't really a ton of "behavior" to test, but I'm sure I could work up something that brings value to the project.
  - Better UX for the map. This is my first time using the Compose Maps SDK, so I just tried to get something to work.
  - Better error UX. The app shouldn't crash, but there are places the UX could be improved. For example, try searching "ghdfhfdgserr" in the text box.

## So you want to give me a job...

Companies love to give coding assignments to people when they are interviewing for jobs and I've done my fair share.
Here are a few projects of mine that I think do a good job of showing off my technical skills as an Android developer:

- [Photo Gallery](https://github.com/bryansills/Photo-Gallery): A Flickr client. Based on a project in the 5th edition of Big Nerd Ranch's Android guide (which I wrote), this Jetpack Compose implementation shows off infinite paging and clean unit tests.
- [Loud Ping](https://github.com/bryansills/LoudPing/): An unwieldy side project of mine trying to help me have a better relationship with the music I listen to. The UI side of things may be a bit basic, but I'm doing a lot of neat things in the Gradle world and one of my more thoroughly architected apps.
- [AllTrails](https://github.com/bryansills/AllTrails) (this project): An app to search for nearby restaurants to have lunch. The best app for showing off my UI skills.
- [Coda Pizza Multiplatform](https://github.com/bryansills/CodaPizzaMultiplatform): A multiplatform pizza customizing app. Based on a project in the 5th edition of Big Nerd Ranch's Android guide (which I wrote), I wrote and presented this Kotlin multiplatform app as a codelab at KotlinConf 2023 in Amsterdam.
