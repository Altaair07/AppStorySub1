package com.dicoding.appstorysub2.ui.auth.login

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import com.dicoding.appstorysub2.R
import com.dicoding.appstorysub2.ui.main.MainActivity
import com.dicoding.appstorysub2.ui.splash.RoutingActivity
import com.dicoding.appstorysub2.util.EspressoIdlingResource
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class LoginActivityTest {

    @get:Rule
    val activity = ActivityScenarioRule(LoginActivity::class.java)

    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun loginSuccess() {
        Intents.init()

        onView(withId(R.id.ed_login_email)).perform(typeText("123@gmail.com"), closeSoftKeyboard())
        onView(withId(R.id.ed_login_password)).perform(typeText("12345678"), closeSoftKeyboard())
        onView(withId(R.id.btn_login)).perform(click())

        intended(hasComponent(MainActivity::class.java.name))
        Intents.release()
        onView(withId(R.id.rv_story)).check(matches(isDisplayed()))
    }

    @Test
    fun logoutSuccess() {
        Intents.init()

        onView(withId(R.id.ed_login_email)).perform(typeText("123@gmail.com"), closeSoftKeyboard())
        onView(withId(R.id.ed_login_password)).perform(typeText("12345678"), closeSoftKeyboard())
        onView(withId(R.id.btn_login)).perform(click())

        intended(hasComponent(MainActivity::class.java.name))
        Intents.release()

        Intents.init()
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().targetContext)
        onView(withText("Logout")).perform(click())
        intended(hasComponent(RoutingActivity::class.java.name))
        Intents.release()
    }
}