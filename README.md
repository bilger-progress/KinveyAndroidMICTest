# KinveyAndroidMICTest
Sample Android application for Kinvey MIC testing.

In order to test *Kinvey MIC* with this application you need to make sure that:

1. Open the `MainActivity.java` of this project and make sure to set all the fields starting from line 19.
2. Make sure to set the desired login flow for the button click listener. The available login flows are declared as private functions down below on the code.
3. If you choose for the Default Kinvey Login flow with a Login Page, then make sure to open the `AndroidManifest.xml` file and set the correct Redirect URI for `<data android:scheme="xxx" />`. The same Redirect URI that you've set on `MainActivity.java` should go in here with a completely lowercase version of the scheme. For example, a redirect of `MyOauth://` would be entered here as `myoauth`.
4. After doing that all, just follow the Console Logs. When the Android app pings the Kinvey backend, if all is fine - a new Toast message will be shown. If not, the error will be shown on the Console logs. The same applies to the login procedure. If the login is successful, a new Toast message will be shown, and the User entity will be logged on the Console. In the case that there is an error, the error will be displayed on the Console logs.