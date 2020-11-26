# Note-App
![Frame 77](https://user-images.githubusercontent.com/61702243/89253701-f4b26280-d63a-11ea-8d73-9cd34c26f3d8.png)

# Features
 - Firebase Intergrated App
 
 - Add Note, Edit Note and Delete Note
 
 - User data management 
 
 - Check Note is added or not display "Empty" animation
 
 - Search Functionality
 
 # How to Connect ?
 - Connect your project to firebase
 - Add SHA certificate fingerprints into your firebase
 
 # Realtime DataBase rules :
 {
   "rules" : {
     ".read" : "auth!=null",
     ".write" : "auth!=null"
   }
 }
