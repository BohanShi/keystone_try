# MA10_SPJM_Safe_Amuse
Team MA10's IE Project - Semester 2, 2020

<h2 style="color: #2e6c80;">Prerequisite: JDK 8</h2>
<p>If you do not have the Java Development Kit (version 8 or newer) installed, you can download it from this <a href="https://www.oracle.com/au/java/technologies/javase/javase-jdk8-downloads.html">link</a>. This should be done before continuing with step 1.</p>
<p>If you have JDK installed but are unsure what version you are using, you can check the version by typing&nbsp;<em><strong>java -version</strong></em><em>&nbsp;</em>in your command line / terminal.</p>

<h2 style="color: #2e6c80;">Step 1 - Android Studio</h2>
<p><strong>1</strong>. As the application is not yet live on the playstore, it should be run using a virtual device within the Android Studio IDE.</p>
<p><strong>2.&nbsp;</strong>If Android Studio is not already installed, it is available for you to download at this <a href="https://developer.android.com/studio#downloads">link</a>.&nbsp;Please make sure you download the correct client for your operating system.</p>
<p><strong>3.</strong> Follow the installation wizard's instructions to set up the IDE to your preference.</p>
<p>&nbsp;</p>
<h2 style="color: #2e6c80;">Step 2 - Opening the SafeAmuse project</h2>
<p><strong>1.</strong> Download the SafeAmuse project files, and unzip the folder if downloaded in a compressed format.</p>
<p><strong>2.</strong>&nbsp;If this is your first time using Android Studio, when you open the IDE it will ask you to create or open a project. Click&nbsp;<em><strong>Open project</strong></em> and navigate to the folder you have just unzipped. The folder to be opened should be represented by the green Android icon (indicating it is runnable by Android Studio).</p>
<p><strong>3.</strong> If this is not your first time running Android Studio,&nbsp;look to the toolbar at the top left and click&nbsp;<em><strong>File</strong></em><em>,&nbsp;</em>then&nbsp;<em><strong>Open</strong></em><em>.&nbsp;</em>Locate the project folder in your system and select it.</p>
<h2 style="color: #2e6c80;">Step 3 - Setting up a virtual device</h2>
<p>The application will run on an Android Virtual Device (AVD), which emulates a device running the Android operating system. This step is only necessary for those who do not already have an AVD set up.&nbsp;</p>
<p><strong>1.</strong>&nbsp;To access the AVD manager, look to the toolbar at the top of the Android Studio window and select <strong><em>Tools</em></strong>, and select&nbsp;<strong><em>AVD Manager&nbsp;</em></strong>from the dropdown menu that appears.</p>
<p><strong>2.</strong>&nbsp;Look to the bottom-left corner of the window that pops up, and click the&nbsp;<em><strong>Create Virtual Device</strong></em><em>&nbsp;</em>button.</p>
<p><strong>3.</strong>&nbsp;In the ensuing <em><strong>Select Hardware</strong></em> screen, select <strong><em>phone</em></strong>&nbsp;device from the tab on the left of the screen, and then select a device from the list. It is suggested to use the <em>Google Pixel 3a</em>, as it is the one of the newest devices available on the AVD and has common screen dimensions. Click&nbsp;<em><strong>next</strong></em><em> when done.</em></p>
<p><strong>4.</strong>&nbsp;In the&nbsp;<em><strong>System Image</strong></em><em>&nbsp;</em>screen, select the&nbsp;<em><strong>Recommended</strong></em><em>&nbsp;</em>tab and select&nbsp;<em><strong>Q</strong></em><em>&nbsp;</em>from the list. Once this is selected, click&nbsp;<em><strong>next</strong></em><em>&nbsp;</em>at the bottom right.</p>
<p><strong>5.</strong> You may now enter a name for your AVD if you wish. Click&nbsp;<em><strong>Finish</strong></em><em>&nbsp;</em>at the bottom right of the pop-up window.</p>
<p><span style="color: #800080;"><span style="text-decoration: underline;"><em>Note</em></span><em>:&nbsp;It may take a few minutes for your AVD to install and set up</em>.</span></p>
<p><strong>6</strong>. To select your device from use, look to the Android Studio toolbar at the top of your screen. Between the center and the right side of your screen, you should see a green 'play' button, with a small drop-down selector next to it.</p>
<p>Click on this drop-down menu, and select the device you just created. Now, when you click the <em>Run app</em>&nbsp;(play) button, it will run the app on the selected virtual device.</p>
<p>&nbsp;</p>
<h2 style="color: #2e6c80;">Step 4 - Running the SafeAmuse application</h2>
<p>&nbsp;<strong>1.</strong> With your AVD selected and the SafeAmuse project open, click the&nbsp;<em><strong>Run app</strong></em><em>&nbsp;</em>(green play) button from the toolbar, located at the top of the window between the center and right of your screen (depending on screen resolution).</p>
<p><strong>2.</strong>&nbsp;The application will launch. This is our <em>home screen</em>. It accepts some basic infromation from the user through the on-screen form, which is used to produce recommendations. Fill in the form and select 'Go' to receive recommendations.</p>
<p><strong>3.</strong> The results screen is then displayed - showing all suggestions that match the preferences entered on the form in the previous screen.</p>
