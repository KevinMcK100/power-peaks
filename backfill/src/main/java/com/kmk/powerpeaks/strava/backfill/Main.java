package com.kmk.powerpeaks.strava.backfill;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.kmk.powerpeaks.strava.backfill.guice.BackfillModule;

public class Main {

    public static void main(String[] args) {

        Injector backfillInjector = Guice.createInjector(new BackfillModule());
        backfillActivities(backfillInjector);
        backfillPower(backfillInjector);
    }

    private static void backfillActivities(Injector backfillInjector) {
        BackfillActivities backfillActivities = backfillInjector.getInstance(BackfillActivities.class);
        backfillActivities.backfillActivities();
    }

    private static void backfillPower(Injector backfillInjector) {
        BackfillPower backfillPower = backfillInjector.getInstance(BackfillPower.class);
        backfillPower.backfillPower();
    }
}
