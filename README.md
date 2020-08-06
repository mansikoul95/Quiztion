# Quiztion
Android Quiz Taker App

Project Description : This app deals with the development of an android-based multiple-choice question testing system, named Quiztion. This application is developed for education purpose, that challenges users with engaging questions while providing them with numerous topics  and area of interests to choose from. The application provides an effective challenge to the player. The system has an admin login that has overall control over the available Questions and Answers. Admin feeds the questions and the corresponding  answers into the system. These questions are picked from the online database and sent on user's android device. The admin also has the capabilities to view the leader board which displays user rankings on the basis of their aggregate quiz scores. In order to take the quiz , the user have to first create an account into the system for playing quiz. The user then selects one out of several available categories, and plays the quiz. At the end of the quiz, the system checks all the answer and generates a score card. The user is also provided with a performance icon on the side navigation bar that allows him/her to check their overall performance in different categories. 

Functionalities Description : Quiztion is an Android based application where user sign up using a unique username, once they sign up, they can access all the quiztion features like edit their profile, change password, view their performance history, access to help and about sections as well. The user has four categories of quizzes to choose from, which contains more sub-categories to choose from. At the end of the quiz, user can view scores, check their answers and see the correct answers as well. The performance history is displayed in form of progress bars for interactive display.  

Team Members : 
Mansi Koul,
Adarsh Sodagudi

Deliverable Items :

A. User/Admin Login Page

B. User Account :
I.	User Registration
II.	Username change
III.	Password change
IV.	Profile changes
V.	Setting changes page

C. Admin Account:
I.	Adding Quiz
II.	Adding question and answer 
III.	Viewing Leader board
IV.	Logout

D. Displays :
I.	Topic Options Display
II.	Question Display
III.	Answer Display
IV.	Score Card
V.	Correct quiz answers
VI.	User Performance Graph
VII.       Sign-out
VII.       Splash Screen

	
Third-party Tools Used :
I.	SQL
II.	PHP Server


List of Tables :

1.	User : Contains user and admin login details
2.	Discover : Contains various Quiz Categories
3.	Quiz List :  Contains various Quiz names under each category
4.	Question Bank : Contains quiz questions, along with 4 choices and the correct answer
5.	Leader : Contains user id, along with their aggregate quiz scores and ranking. 





Database Design :

1.	User: 

username : primary  key
user_id : auto-increment

user_id	username	password	email	institute	phone-_no
					

2.	Discover:

topic_id : unique key

topic_id	category
	

3.	Quizlist:

quizname :  unique key

topic_id	quizname
	

4.	QuestionBank:

quizname :  unique key

quizname	question	Choice 1	Choice 2	Choice 3	Choice 4	answer
						

5.	LeaderBoard:

user_id	quizname	score
		

