# **MedAid** 

**Group Name:** M.A.N.S

**Members:** Mufaddal Jerwalla, Aditi Shetty, Nabil Barakati, Stephanie Fok

## About
The problem we decided to tackle involves making an improvement to the field of health management. Our application would be used by stand alone users or by organizations with high involvement with managing other people’s health, such as nursing and senior homes. This will serve as an extremely useful tool as a centralized point of information used by those that consume medicines daily.

By providing the patient with a centralized system to input and manage their medicines, they can then share this information with the doctor as supplemental information regarding the prescription of new or pre-existing medication. Moreover, a mobile application would be the most applicable in solving such problems as a mobile device provides a compact, portable, and easily accessible solution. With ease-of-access as our primary goal, nurses and social workers working in senior homes and hospitals would be able to pull up such information effortlessly, move to various patients, and repeat the same process without being slowed down by carrying a larger device, such as a laptop. For stand alone users, a phone is something majority of people carry along with them, as a result this would be the easiest way for them to receive such information in a simple to use and timely manner.

## Functionalities
Our application includes multiple functionalities that allow users to easily manage and view information related to their medication and physical health.

### 1. Calendar
A ‘Today’ fragment, which presents to a user the medication they are expected to take on any given day of the week. In this view the user can add medications, which will then be used to render a list of all upcoming medications for that day.

A material calendar view which was implemented using a decorator design pattern to decorate certain days for different occasions. For example, a red dot is shown on the calendar when such as when a user adds an appointment they are scheduled for in the future.

### 2. Medical History
A ‘History’ component that keeps track of all the past medications and vaccinations a user has inputted. We also were able to implement a search functionality to allow for patients and doctors alike to quickly sift through this information, should the need arise.

Part of the history fragment also allows users to upload PDF files from their device. This can be used to store important medical scans such as MRI’s or CT scans, which can be difficult to otherwise reference back to, or find when meeting with medical professionals. With scans today still being provided on CD, this was a high priority for us because virtual copies on a person’s phone are significantly more convenient.

### 3. Insights
A ‘Conflicts/Insights’ view which allows for users to feel confident in the medications they are taking. This view provides the ability for a user to look up drug to drug interactions and drug to food interactions, which will help identify if the patient is at risk of any additional harm. Many people fall victim to unwanted side effects due to their unawareness of such drug and food combinations and we hope to reduce that with such functionality.

Likewise, this view also provides the user with insights regarding their medication intake which can then be monitored to form correlations between taking medication on time and how in doing so, impacts how they feel  on a day to day basis.


### 4. Multi-User Experience
Login systems for various user types, such as the single user experience, the multi-user experience (multiple patients under a single caretaker) and the doctor, who can connect to a patient over bluetooth to view someone’s medical history for a period of time. 

Multi-user allows for those that are elderly or sick in nursing homes or long term care to experience a better sense of personal care. By creating a single platform for a caretaker to easily manage all the medication information for each and every patient, we ensure the clients get the quality of care they deserve.


### 5. Sharing Medical Information with Doctor over Bluetooth
An additional feature was implemented such that a doctor and patient can both turn on bluetooth, set themselves as discoverable, connect to one another and then the patient can send their medical history, all directly within the app. We also do not store this data on the doctor’s phone for confidentiality purposes.


**Overview of the Application:** https://www.youtube.com/watch?v=TLQjxSndy14&fbclid=IwAR3J350hlHNCe89oHk4xrZ6acAE0TUGYqEImgU4lQikSGDG0u7NUd8hAavY
