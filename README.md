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