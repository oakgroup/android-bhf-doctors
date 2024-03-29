package com.active.orbit.baseapp.design.activities.engine

import com.active.orbit.baseapp.design.activities.WebViewActivity
import com.active.orbit.baseapp.design.activities.activity.ActivityActivity
import com.active.orbit.baseapp.design.activities.activity.DebugActivity
import com.active.orbit.baseapp.design.activities.activity.MapActivity
import com.active.orbit.baseapp.design.activities.activity.TripsActivity
import com.active.orbit.baseapp.design.activities.main.DoctorActivity
import com.active.orbit.baseapp.design.activities.main.PatientActivity
import com.active.orbit.baseapp.design.activities.main.SplashActivity
import com.active.orbit.baseapp.design.activities.menu.AboutActivity
import com.active.orbit.baseapp.design.activities.menu.ContactUsActivity
import com.active.orbit.baseapp.design.activities.menu.FaqActivity
import com.active.orbit.baseapp.design.activities.menu.HelpActivity
import com.active.orbit.baseapp.design.activities.menu.SettingsActivity
import com.active.orbit.baseapp.design.activities.messaging.SuccessMessageActivity
import com.active.orbit.baseapp.design.activities.registration.OnBoardingBatteryActivity
import com.active.orbit.baseapp.design.activities.registration.OnBoardingLocationActivity
import com.active.orbit.baseapp.design.activities.registration.OnBoardingUnusedRestrictionsActivity
import com.active.orbit.baseapp.design.activities.onboarding.WelcomeActivity
import com.active.orbit.baseapp.design.activities.questionnaire.HealthActivity
import com.active.orbit.baseapp.design.activities.questionnaire.HealthAnxietyActivity
import com.active.orbit.baseapp.design.activities.questionnaire.HealthMobilityActivity
import com.active.orbit.baseapp.design.activities.questionnaire.HealthPainActivity
import com.active.orbit.baseapp.design.activities.questionnaire.HealthResponseActivity
import com.active.orbit.baseapp.design.activities.questionnaire.HealthScoreActivity
import com.active.orbit.baseapp.design.activities.questionnaire.HealthSelfCareActivity
import com.active.orbit.baseapp.design.activities.questionnaire.HealthUsualActivitiesActivity
import com.active.orbit.baseapp.design.activities.registration.ConsentFormActivity
import com.active.orbit.baseapp.design.activities.registration.ConsentPrivacyActivity
import com.active.orbit.baseapp.design.activities.registration.PatientDetailsActivity
import com.active.orbit.baseapp.design.activities.symptoms.ReportSymptomDetailsActivity
import com.active.orbit.baseapp.design.activities.symptoms.ReportSymptomTimeActivity
import com.active.orbit.baseapp.design.activities.symptoms.SymptomsActivity
import com.active.orbit.baseapp.design.activities.tour.TourActivity
import com.active.orbit.baseapp.design.protocols.ActivityProvider

enum class Activities(private val activity: Class<out BaseActivity>) : ActivityProvider {
    ABOUT(AboutActivity::class.java),
    ACTIVITY(ActivityActivity::class.java),
    CONTACT_US(ContactUsActivity::class.java),
    DEBUG(DebugActivity::class.java),
    TRIPS(TripsActivity::class.java),
    DOCTOR(DoctorActivity::class.java),
    FAQ(FaqActivity::class.java),
    HEALTH(HealthActivity::class.java),
    HEALTH_MOBILITY(HealthMobilityActivity::class.java),
    HEALTH_SELFCARE(HealthSelfCareActivity::class.java),
    HEALTH_USUAL_ACTIVITIES(HealthUsualActivitiesActivity::class.java),
    HEALTH_PAIN(HealthPainActivity::class.java),
    HEALTH_ANXIETY(HealthAnxietyActivity::class.java),
    HEALTH_SCORE(HealthScoreActivity::class.java),
    HEALTH_RESPONSE(HealthResponseActivity::class.java),
    HELP(HelpActivity::class.java),
    MAP(MapActivity::class.java),
    ON_BOARDING_BATTERY(OnBoardingBatteryActivity::class.java),
    ON_BOARDING_LOCATION(OnBoardingLocationActivity::class.java),
    ON_BOARDING_UNUSED_RESTRICTIONS(OnBoardingUnusedRestrictionsActivity::class.java),
    PATIENT(PatientActivity::class.java),
    PATIENT_DETAILS(PatientDetailsActivity::class.java),
    REPORT_SYMPTOM_DATE_TIME(ReportSymptomTimeActivity::class.java),
    REPORT_SYMPTOM_DETAILS(ReportSymptomDetailsActivity::class.java),
    SETTINGS(SettingsActivity::class.java),
    SPLASH(SplashActivity::class.java),
    SUCCESS_MESSAGE(SuccessMessageActivity::class.java),
    SYMPTOMS(SymptomsActivity::class.java),
    CONSENT_FORM(ConsentFormActivity::class.java),
    CONSENT_PRIVACY(ConsentPrivacyActivity::class.java),
    TOUR(TourActivity::class.java),
    WEB_VIEW(WebViewActivity::class.java),
    WELCOME(WelcomeActivity::class.java);

    override fun getActivity(): Class<out BaseActivity> {
        return activity
    }
}