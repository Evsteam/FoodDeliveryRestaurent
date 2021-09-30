package com.evs.foodexp.stripe;


import com.evs.foodexp.stripe.model.Token;

public abstract class TokenCallback {
    public abstract void onError(Exception error);
    public abstract void onSuccess(Token token);
}
