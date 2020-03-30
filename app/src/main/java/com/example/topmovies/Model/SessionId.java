
package com.example.topmovies.Model;

import com.google.gson.annotations.SerializedName;

public class SessionId {

    @SerializedName("expires_at")
    private String mExpiresAt;
    @SerializedName("guest_session_id")
    private String mGuestSessionId;
    @SerializedName("success")
    private Boolean mSuccess;

    public String getExpiresAt() {
        return mExpiresAt;
    }

    public void setExpiresAt(String expiresAt) {
        mExpiresAt = expiresAt;
    }

    public String getGuestSessionId() {
        return mGuestSessionId;
    }

    public void setGuestSessionId(String guestSessionId) {
        mGuestSessionId = guestSessionId;
    }

    public Boolean getSuccess() {
        return mSuccess;
    }

    public void setSuccess(Boolean success) {
        mSuccess = success;
    }

}
