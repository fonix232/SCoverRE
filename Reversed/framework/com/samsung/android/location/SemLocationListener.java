package com.samsung.android.location;

import android.location.Address;
import android.location.Location;

public interface SemLocationListener {
    void onLocationAvailable(Location[] locationArr);

    void onLocationChanged(Location location, Address address);
}
