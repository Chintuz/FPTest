# FPTest

- This project includes MVVM architecture with data binding.

- As the data is the type of selection and category, the best UI which is good for user experience is Header and its item individual.
- Facility name will be displayed in header and their options are as items

- It behaves as per the scenarios given in assignment with exclusions logic.
- app will update data when last fetched record is due more than a day.
- so once data is fetched from server it will take it from DB until unless data is not there in DB.

- for testing of refresh you can use TEST_TIME_STAMP set in Const.kt file.

- Room persistent library is used to store the data.
- RxJava for some of the background task and network related operation with Retrofit. 
