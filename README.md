Application in Java using the IDE IntelliJ for managing an e-learning platform. The app applies the concepts of OOP.

Classes:
--
-Main, which creates an object of type ApplicationSite and displays the menu which further calls the needed functions
-ApplicationSite - singletone which stores most complex functions and lists/sets... of all object types
-Course
-AccreditedCourse, extended from Course and mantaing data about accreditation period beside a basic Course
-Quiz, a course having more quizes
-Question, a quiz having more questions
-Subject - we store in the app a map (Subject, Course) because a Course can have multiple subjects
-Card, as a payment method
-User
-two enums, Difficulty(of Course, Quiz...) and Level (of accreditation)
-interface Comparable - I overrode its function compareTo to compare, for example, subjects (by name) or questions (by points they give)
-UtilityClass - for storing different utilitary methods I create, such as getCurrentDate() which transforms the current date in a string using a formator; this method is called in the User constructor to retain sign up date.

The App retain the user which is connected. If no user is connected, the menu only shows two options: login or sign up.
Otherwise, the user can, for example:
--
-add a Course as a teacher (at the moment there is a single type of user which acts as both the teacher and the student)
- 
