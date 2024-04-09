**E-learning platform**
--

Application in the IDE IntelliJ using Java for managing an e-learning platform. The app applies the concepts of OOP.

Classes:
--
-**Main**, which creates an object of type ApplicationSite and displays the menu which further calls the needed functions <br>
-**ApplicationSite** - singletone which stores most complex functions and lists/sets... of all object types <br>
-**Course**<br>
-**AccreditedCourse**, extended from Course and mantaing data about accreditation period beside a basic Course<br>
-**Quiz**, a course having more quizes<br>
-**Question**, a quiz having more questions<br>
-**Subject** - we store in the app a map (Subject, Course) because a Course can have multiple subjects<br>
-**Card**, as a payment method<br>
-**User**<br>
-two enums, **Difficulty**(of Course, Quiz...) and **Level** (of accreditation)<br>
-interface Comparable - I overrode its function compareTo to compare, for example, subjects (by name) or questions (by points they give)<br>
-**UtilityClass** - for storing different utilitary methods I create, such as getCurrentDate() which transforms the current date in a string using a formator; this method is called in the User constructor to retain sign up date.

<br>

The App retains the user which is connected. If no user is connected, the menu only shows two possible actions:
--
-login (**connectUser()**) <br>
-sign up (**signUpUser()**). <br>

Otherwise, the user can, for example: 
--
<br>

-add a Course as a teacher (**addCourse()**). At the moment there is a single type of user which acts as both the teacher and the student. The user adding the course will be its teacher<br>
-disconnect (**disconnectUser()**) <br>
-see all subjects sorted by name (**showSubjectsSorted()**) <br>
-see all courses alongside their subjects (**showAllCourses()**) <br>
-buy a course (**buyCourse()**): the user has to write the name of the course. <br>
-Before buying, the app permits to manage current cards and add another if necessary (**manageCardsToBuy()**).  <br>
-For buying, we use the function **User.payWithCard()** which needs a Card parameter (the card the user chose) <br>
-see courses the user bought (**showCoursesStarted()**). For each course the app shows how many quizes the user tried. <br>
-when displaying the courses joined, the user can opt for doing quizes in one of them writing the course name and then the quiz name. The method **Quiz.runQuiz()** displays the questions and returns the score, which will be added to quiz progress of the user - a map with the key Course and the value another map (Quiz, score).

