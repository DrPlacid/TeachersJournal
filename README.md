# TeachersJournal
Tescher's Journal is simple and efficient app, designed to deliver a functional gradebook in the phone to both univercity and school teachers. 
The app allows to manage a number of editable tables (groups), which contains both list of students, and an extendable range of lessons with auto-set date. 
For more info on using Tescher's Journal see in-app hints.

![alt text](https://github.com/DrPlacid/TeachersJournal/blob/master/photo_2020-08-20_02-05-35.jpg?raw=true)



Tescher's Journal is designed around MVVM pattern, using Room Persistence Library & LiveData. 
The table UI is represented by a set of nested & synchronized RecyclerViews which contains a number of editable ViewHolders.
The table range can be easily changed by adding/deleting rows and columns via the corresponding buttons. 
In order to prevent user from ill-considered moves, each unalterable action is followed by confirmation alert dialog.
Furthermore, the app UI is prevented from sharp & unpleasable movements by using a number of smooth animations & delayed actions.

![alt text](https://github.com/DrPlacid/TeachersJournal/blob/master/photo_2020-08-20_02-05-22.jpg?raw=true)
![alt text](https://github.com/DrPlacid/TeachersJournal/blob/master/photo_night.jpg?raw=true)
