package com.user.etow.injection.components;

/*
 *  Copyright Ⓒ 2018. All rights reserved
 *  Author DangTin. Create on 2018/05/13
 */

import com.user.etow.injection.modules.ActivityModule;
import com.user.etow.ui.auth.enter_otp.EnterOTPActivity;
import com.user.etow.ui.auth.forgot_password.ForgotPasswordActivity;
import com.user.etow.ui.auth.sign_in.SignInActivity;
import com.user.etow.ui.auth.sign_up.SignUpActivity;
import com.user.etow.ui.auth.term_and_condition.TermAndConditionActivity;
import com.user.etow.ui.auth.user_start.UserStartActivity;
import com.user.etow.ui.auth.verify_mobile_number.VerifyMobileNumberActivity;
import com.user.etow.ui.confirm_booking.ConfirmBookingActivity;
import com.user.etow.ui.feedback.FeedbackActivity;
import com.user.etow.ui.main.MainActivity;
import com.user.etow.ui.main.get_in_touch.GetInTouchFragment;
import com.user.etow.ui.main.home.HomeFragment;
import com.user.etow.ui.main.my_account.MyAccountFragment;
import com.user.etow.ui.main.my_bookings.MyBookingsFragment;
import com.user.etow.ui.main.social_links.SocialLinksFragment;
import com.user.etow.ui.main.term_and_condition.TermAndConditionFragment;
import com.user.etow.ui.pay_card.PayCardActivity;
import com.user.etow.ui.rate_trip.RateTripActivity;
import com.user.etow.ui.splash.SplashActivity;
import com.user.etow.ui.trip_completed.TripCompletedActivity;
import com.user.etow.ui.trip_detail.TripDetailActivity;
import com.user.etow.ui.trip_process.TripProcessActivity;

import dagger.Subcomponent;

@Subcomponent(modules = ActivityModule.class)
public interface ActivityComponent {

    /*inject activity*/
    void inject(SplashActivity loginActivity);

    void inject(MainActivity mainActivity);

    void inject(UserStartActivity userStartActivity);

    void inject(SignInActivity signInActivity);

    void inject(ForgotPasswordActivity forgotPasswordActivity);

    void inject(VerifyMobileNumberActivity verifyMobileNumberActivity);

    void inject(EnterOTPActivity enterOTPActivity);

    void inject(TermAndConditionActivity termAndConditionActivity);

    void inject(SignUpActivity signUpActivity);

    void inject(FeedbackActivity feedbackActivity);

    void inject(TripDetailActivity tripCompletedDetailActivity);

    void inject(ConfirmBookingActivity confirmBookingActivity);

    void inject(TripProcessActivity tripProcessActivity);

    void inject(TripCompletedActivity tripCompletedActivity);

    void inject(RateTripActivity rateTripActivity);

    void inject(PayCardActivity payCardActivity);

    /*inject fragment*/
    void inject(HomeFragment myTaskFragment);

    void inject(TermAndConditionFragment termAndConditionFragment);

    void inject(GetInTouchFragment getInTouchFragment);

    void inject(SocialLinksFragment socialLinksFragment);

    void inject(MyAccountFragment myAccountFragment);

    void inject(MyBookingsFragment myBookingsFragment);
}
